/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.contracts.IFileLogger;

public class FileLogger implements IFileLogger
{
    private String path;

    public IFileLogger setPath(String path)
    {
        this.path = path;

        return this;
    }

    public void log(Object object)
    {
        System.out.println(object.toString());
    }
}
