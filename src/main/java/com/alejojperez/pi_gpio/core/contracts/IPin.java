/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.contracts;

public interface IPin extends ICanLog
{
    /**
     * Unexport the pin on the system
     *
     * @return IPin
     */
    IPin destroy();

    /**
     * Get the pin direction
     *
     * @return String
     */
    String getDirection();

    /**
     * Get the pin value
     *
     * @return String
     */
    String getValue();

    /**
     * Get the pin number
     *
     * @return int
     */
    int getPinNumber();

    /**
     * Export the pin on the system
     *
     * @return IPin
     */
    IPin initialize();

    /**
     * Whether the pin is initialized or not
     *
     * @return
     */
    boolean isInitialized();

    /**
     * @inheritdoc
     */
    IPin registerLogger(ILogger logger);

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
