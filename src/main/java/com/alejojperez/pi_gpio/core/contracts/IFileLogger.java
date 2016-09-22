/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.contracts;

public interface IFileLogger extends ILogger
{
    /**
     * Set the path to the file where the message
     * log is going to be written
     *
     * @param path
     * @return
     */
    IFileLogger setPath(String path);
}
