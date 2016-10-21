/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.contracts.IFolderWatcher;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FolderWatcher implements IFolderWatcher
{
    private WatchService watcher;

    private ExecutorService executor;

    @Override
    public void start(String path)
    {
        try {
            this.watcher = FileSystems.getDefault().newWatchService();
        } catch(IOException e) {
            e.printStackTrace();
        }

        this.executor = Executors.newSingleThreadExecutor();

        if(this.watcher != null)
            this.initialize(path);
        else
            System.out.println("Sorry, we could not start the folder watcher.");
    }

    @Override
    public void stop()
    {
        try {
            watcher.close();
        } catch (IOException e) {
            System.out.println("Sorry, we could not stop the folder watcher.");
        }

        this.executor.shutdown();
    }

    private void initialize(String path)
    {
        final Map<WatchKey, Path> keys = new HashMap<>();

        Consumer<Path> register = p -> {
            if (!p.toFile().exists() || !p.toFile().isDirectory()) {
                throw new RuntimeException("folder " + p + " does not exist or is not a directory");
            }
            try {
                Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        System.out.println("Registering " + dir + "in folder watcher service.");
                        WatchKey watchKey = dir.register(watcher, new WatchEvent.Kind[]{ENTRY_CREATE}, SensitivityWatchEventModifier.HIGH);
                        keys.put(watchKey, dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Error registering path " + p);
            }
        };

        register.accept(Paths.get(path));

        executor.submit(() -> {
            while (true) {
                final WatchKey key;
                try {
                    key = watcher.take(); // wait for a key to be available
                } catch (InterruptedException ex) {
                    return;
                }

                final Path dir = keys.get(key);
                if (dir == null) {
                    System.err.println("WatchKey " + key + " not recognized!");
                    continue;
                }

                key.pollEvents().stream()
                        .filter(e -> (e.kind() != OVERFLOW))
                        .map(e -> ((WatchEvent<Path>) e).context())
                        .forEach(p -> {
                            final Path absPath = dir.resolve(p);
                            if (absPath.toFile().isDirectory()) {
                                register.accept(absPath);
                            } else {
                                final File f = absPath.toFile();
                                System.err.println("Detected new file" + f.getAbsolutePath());
                            }
                        });

                boolean valid = key.reset(); // IMPORTANT: The key must be reset after processed
                if (!valid) {
                    break;
                }
            }
        });
    }
}
