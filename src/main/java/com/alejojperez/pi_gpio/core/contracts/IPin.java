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
     * @return
     */
    IPin destroy();

    /**
     * Get the alias of the pin
     *
     * @return the pin's alias
     */
    String getAlias();

    /**
     * Get the pin direction
     *
     * @return the pin's direction
     */
    String getDirection();

    /**
     * Get the pin's gpio number
     *
     * @return the pin's gpio number
     */
    int getGPIOPin();

    /**
     * Get the pin's number
     *
     * @return the pin's number
     */
    int getPin();

    /**
     * Get the pin value
     *
     * @return the pin's value
     */
    String getValue();

    /**
     * Export the pin on the system
     *
     * @return
     */
    IPin initialize();

    /**
     * Whether the pin is editable or not
     *
     * @return the pin's edition state
     */
    boolean isEditable();

    /**
     * Whether the pin is five volts or not
     *
     * @return the pin's five volts state
     */
    boolean isFiveVolts();

    /**
     * Whether the pin is ground or not
     *
     * @return the pin's ground state
     */
    boolean isGround();

    /**
     * Whether the pin is initialized or not
     *
     * @return the pin's initialization state
     */
    boolean isInitialized();

    /**
     * Whether the pin is three volts or not
     *
     * @return the pin's three volts state
     */
    boolean isThreeVolts();

    /**
     * @inheritdoc
     */
    IPin registerLogger(ILogger logger);

    /**
     * Set the pin alias: any string
     *
     * @param alias the pin's alias
     * @return
     */
    IPin setAlias(String alias);

    /**
     * Set the direction of the pin: "in" or "out"
     *
     * @param direction the pin's direction
     * @return
     */
    IPin setDirection(String direction);

    /**
     * Set the edition state of the pin
     *
     * @param editable the pin's edition state
     * @return
     */
    IPin setEditable(boolean editable);

    /**
     * Set the five volt state of the pin
     *
     * @param fiveVolts the pin's five volt state
     * @return
     */
    IPin setFiveVolts(boolean fiveVolts);

    /**
     * Set the pin's gpio number
     *
     * @param pin the pin's gpio number
     * @return
     */
    IPin setGPIOPin(int gpioPin);

    /**
     * Set the ground state of the pin
     *
     * @param ground the pin's ground state
     * @return
     */
    IPin setGround(boolean ground);

    /**
     * Set the pin's number
     *
     * @param pin the pin's number
     * @return
     */
    IPin setPin(int pin);

    /**
     * Set the three volts state of the pin
     *
     * @param threeVolts the pin's three volt state
     * @return
     */
    IPin setThreeVolts(boolean threeVolts);

    /**
     * Set the value of the pin: "1" or "0"
     *
     * @param value pin's value
     * @return IPin
     */
    IPin setValue(String value);
}
