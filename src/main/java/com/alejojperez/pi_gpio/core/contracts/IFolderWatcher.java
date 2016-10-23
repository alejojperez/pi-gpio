/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.contracts;

import java.io.IOException;
import java.nio.file.Path;

public interface IFolderWatcher
{
    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents(IGPIOController controller);

    /**
     * Register the given directory with the WatchService
     */
    void register(Path dir) throws IOException;

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    void registerAll(final Path start) throws IOException;
}
