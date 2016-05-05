/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.Core.Contracts;

import java.util.HashMap;

public interface IGPIOController
{
    /**
     * Adds a new pin to the collection, if it is not
     * already within it
     *
     * @param alias The name to be used for the pin
     * @param pinNumber The GPIO pin number
     * @return IGPIOController
     */
    IGPIOController addPin(String alias, int pinNumber);

    /**
     * Adds all the new pins to the collection, if they are not
     * already within it
     *
     * @param pins
     * @return
     * @throws Exception
     */
    IGPIOController addPins(HashMap<String, Integer> pins);

    /**
     * Change an existing pin alias to use a different pin number
     * if the alias exists, if it does not exist, then a new pin
     * will be registered
     *
     * @param alias
     * @param pinNumber
     * @return
     * @throws Exception
     */
    IGPIOController changePin(String alias, int pinNumber);

    /**
     * Remove a pin, if it exists, by the given alias
     *
     * @param alias
     * @return
     */
    IGPIOController deletePin(String alias);

    /**
     * This method is called by the JVE Garbage Collector when is going
     * dispose the object, so at this moment call flush pins to make sure
     * that all the pins are reset
     */
    void finalize();

    /**
     * Remove all the pins
     *
     * @return
     */
    IGPIOController flushPins();
}
