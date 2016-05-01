/**
 * Created by Alejandro Perez on 4/30/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.Core.Contracts;

public interface ICanLog
{
    /**
     * Try to log a message if there is any kind
     * of logger instance that can be used
     *
     * @param object
     */
    void logMessageIfPossible(Object object);

    /**
     * Register a logger using any logic desired
     *
     * Ex: a variable in the class that can be used
     * when trying to log a message
     *
     * @param logger
     * @return
     */
    Object registerLogger(ILogger logger);
}
