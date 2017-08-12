/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.contracts.IFolderWatcher;
import com.alejojperez.pi_gpio.core.contracts.IGPIOController;
import java.io.IOException;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FolderWatcher implements IFolderWatcher
{
    private final WatchService watcher;
    private final ExecutorService executor;
    private final Map<WatchKey,Path> keys;
    private final Path dir;
    private final boolean recursive;
    private boolean trace = false;
    public static boolean log = false;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * @inheritdoc
     */
    FolderWatcher(Path dir, boolean recursive) throws IOException
    {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.executor = Executors.newSingleThreadExecutor();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;
        this.dir = dir;

        if (recursive) {
            this.logMessageIfPossible("SCANNING: "+this.dir);
            registerAll(this.dir);
            this.logMessageIfPossible("DONE SCANNING: "+this.dir);
        } else {
            register(this.dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * @inheritdoc
     */
    public ExecutorService getExecutor()
    {
        return executor;
    }

    /**
     * @inheritdoc
     */
    public void logMessageIfPossible(Object object)
    {
        if(FolderWatcher.log)
        {
            System.out.println(object.toString());
        }
    }

    /**
     * @inheritdoc
     */
    public void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                this.logMessageIfPossible("REGISTER: "+dir);
            } else {
                if (!dir.equals(prev)) {
                    this.logMessageIfPossible("UPDATE: "+prev+" -> "+dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * @inheritdoc
     */
    public void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * @inheritdoc
     */
    public void start(IGPIOController controller) {
        this.executor.submit(() -> {
            while (true) {

                // wait for key to be signalled
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                Path dir = keys.get(key);
                if (dir == null) {
                    logMessageIfPossible("ERROR: WatchKey not recognized!");
                    continue;
                }

                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind kind = event.kind();

                    // TBD - provide example of how OVERFLOW event is handled
                    if (kind == OVERFLOW) {
                        continue;
                    }

                    // Context for directory entry event is the file name of entry
                    WatchEvent<Path> ev = cast(event);
                    Path name = ev.context();
                    Path child = dir.resolve(name);

                    // print out event
                    logMessageIfPossible(event.kind().name()+": "+child);

                    // if directory is created, and watching recursively, then
                    // register it and its sub-directories
                    if (recursive && (kind == ENTRY_CREATE)) {
                        try {
                            if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                                registerAll(child);
                            }
                        } catch (IOException x) {
                            // ignore to keep sample readbale
                        }
                    }

                    controller.sync();
                }

                // reset key and remove from set if directory no longer accessible
                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);

                    // all directories are inaccessible
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            }
        });
    }

    /**
     * @inheritdoc
     */
    public void stop()
    {
        if(!this.executor.isShutdown())
        {
            logMessageIfPossible("SHUTTING DOWN: " + this.dir);
            try {
                watcher.close();
            } catch(IOException e) {
                logMessageIfPossible("Sorry, we could not stop the folder watcher.");
            }

            this.executor.shutdown();
            logMessageIfPossible("DONE SHUTTING DOWN: " + this.dir);
        }
    }
}
