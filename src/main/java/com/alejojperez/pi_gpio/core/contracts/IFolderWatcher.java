/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.contracts;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

public interface IFolderWatcher
{
    /**
     * Get the executor of the folder watch service
     * @return
     */
    ExecutorService getExecutor();

    /**
     * Register the given directory with the WatchService
     */
    void register(Path dir) throws IOException;

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    void registerAll(final Path start) throws IOException;

    /**
     * Start listening for all events
     */
    void start(IGPIOController controller);

    /**
     * Stop listening for all events
     */
    void stop();
}
