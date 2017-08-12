/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.contracts;

import javafx.collections.ObservableMap;

public interface IGPIOController extends ICanLog
{
    /**
     * Adds a new pin to the collection, if it is not
     * already within it
     *
     * @param pin the pin entity
     * @return IGPIOController
     */
    IGPIOController addPin(IPin pin);

    /**
     * Adds all the new pins to the collection, if they are not
     * already within it
     *
     * @param pins the pin's collection
     * @return IGPIOController
     */
    IGPIOController addPins(ObservableMap<Integer, IPin> pins);

    /**
     * Remove a pin, if it exists, by the given pin number
     *
     * @param pin the pin entity
     * @return IGPIOController
     */
    IGPIOController deletePin(IPin pin);

    /**
     * Remove all the pins
     *
     * @return
     */
    IGPIOController flushPins();

    /**
     * Get a pin entity by the pin number
     *
     * @param pinNumber the pin number
     * @return IGPIOController
     */
    IPin get(Integer pinNumber);

    /**
     * Get an observable list containing all the pins
     *
     * @return ObservableMap
     */
    ObservableMap<Integer, IPin> getPins();

    /**
     * Start the folder watch service
     */
    void startFolderWatcher();

    /**
     * Stop the folder watch service
     */
    void stopFolderWatcher();

    /**
     * Sync all the initialized pins
     *
     * @return
     */
    IGPIOController sync();
}
