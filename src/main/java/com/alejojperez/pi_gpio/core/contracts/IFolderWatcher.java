/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.contracts;

public interface IFolderWatcher
{
    /**
     * Start the folder watcher
     *
     * @param path
     */
    void start(String path);

    /**
     * Stop the folder watcher
     */
    void stop();
}
