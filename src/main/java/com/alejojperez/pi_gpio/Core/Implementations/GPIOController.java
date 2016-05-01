/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.Core.Implementations;

import com.alejojperez.pi_gpio.Core.Contracts.ICanLog;
import com.alejojperez.pi_gpio.Core.Contracts.IGPIOController;
import com.alejojperez.pi_gpio.Core.Contracts.ILogger;
import com.alejojperez.pi_gpio.Core.Contracts.IPin;
import com.alejojperez.pi_gpio.Core.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class GPIOController implements IGPIOController, ICanLog
{
    /**
     * Class instance: using singleton design pattern
     */
    protected static GPIOController instance = new GPIOController();

    /**
     * Logger class in charge of logging any relevant information
     */
    protected ILogger logger;

    /**
     * Dictionary containing all the pins in use
     */
    protected HashMap<String, IPin> pins = new HashMap<String, IPin>(40);

    /**
     * Create a private constructor, so the
     * class can not be instantiate
     */
    private GPIOController() { }

    /**
     * @inheritdoc
     */
    public IGPIOController addPin(String alias, int pinNumber)
    {
        if(!Utils.validPinNumber(pinNumber)) {
            this.logMessageIfPossible("Sorry, the pin number provided for '" + alias + ":" + Integer.toString(pinNumber) + "' in not valid.");
        } else {
            try {
                IPin pin = new Pin(pinNumber);
                this.pins.putIfAbsent(alias, pin);
            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController addPins(HashMap<String, Integer> pins)
    {
        pins.forEach((alias, pinNumber) -> this.addPin(alias, pinNumber));

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController changePin(String alias, int pinNumber)
    {
        if(!Utils.validPinNumber(pinNumber))
        {
            this.logInvalidPinNumber(alias, pinNumber);
        }
        else
        {
            Object classInstance = null;
            Method method = null;

            if(this.pins.containsKey(alias))
            {
                try {
                    classInstance = this.pins;
                    method = HashMap.class.getDeclaredMethod("replace", Object.class, Object.class);
                } catch(NoSuchMethodException e) {
                    this.logMessageIfPossible(e);
                }
            }
            else
            {
                try {
                    classInstance = this;
                    method = IGPIOController.class.getDeclaredMethod("addPin", String.class, int.class);
                } catch(NoSuchMethodException e) {
                    this.logMessageIfPossible(e);
                }
            }

            if(classInstance != null && method != null)
            {
                try {
                    method.invoke(classInstance, alias, pinNumber);
                } catch(IllegalAccessException e) {
                    this.logMessageIfPossible(e);
                } catch(InvocationTargetException e) {
                    this.logMessageIfPossible(e);
                }
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController deletePin(String alias)
    {
        if(this.pins.containsKey(alias))
            this.pins.remove(alias);

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController flushPins()
    {
        this.pins.forEach((alias, pinNumber) -> this.deletePin(alias));

        return this;
    }

    /**
     * Returns a class instance: using singleton design pattern
     *
     * @return IGPIOController
     */
    public static IGPIOController getInstance()
    {
        return instance;
    }

    /**
     * @inheritdoc
     */
    public void logMessageIfPossible(Object object)
    {
        if(this.logger != null)
        {
            this.logger.log(object);
        }
    }

    /**
     * @inheritdoc
     */
    public IGPIOController registerLogger(ILogger logger)
    {
        this.logger = logger;

        return this;
    }

    //region Helpers

    private void logInvalidPinNumber(String alias, int pinNumber)
    {
        this.logMessageIfPossible("Sorry, the pin number provided for '" + alias + ":" + pinNumber + "' in not valid.");
    }

    //endregion
}
