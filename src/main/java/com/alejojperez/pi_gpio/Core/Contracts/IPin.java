/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.Core.Contracts;

public interface IPin
{
    /**
     * Export the pin on the system
     *
     * @return IPin
     */
    IPin initialize();

    /**
     * Unexport the pin on the system
     *
     * @return IPin
     */
    IPin destroy();

    /**
     * Set the direction of the pin: "in" or "out"
     *
     * @param direction
     * @return IPin
     */
    IPin setDirection(String direction);

    /**
     * Set the value of the pin: "1" or "0"
     *
     * @param value
     * @return IPin
     */
    IPin setValue(String value);
}
