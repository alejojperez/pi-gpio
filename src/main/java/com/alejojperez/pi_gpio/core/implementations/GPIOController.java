/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.contracts.IGPIOController;
import com.alejojperez.pi_gpio.core.contracts.ILogger;
import com.alejojperez.pi_gpio.core.contracts.IPin;
import com.alejojperez.pi_gpio.core.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import javax.xml.xpath.XPathConstants;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Consumer;

public class GPIOController implements IGPIOController
{
    /**
     * General path to where all the pins are located
     */
    protected String generalPath;

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
    protected ObservableMap<String, IPin> pins = FXCollections.observableMap(new HashMap<String, IPin>(40));

    /**
     * Create a private constructor, so the
     * class can not be instantiate
     */
    private GPIOController()
    {
        this.generalPath = (String) Utils.config("//system/GPIO/pin/paths/generalPath/text()", XPathConstants.STRING);
        this.sync();
    }

    /**
     * @inheritdoc
     */
    public IGPIOController addPin(String alias, int pinNumber)
    {
        if(!Utils.validPinNumber(pinNumber)) {
            this.logMessageIfPossible("Sorry, the pin number provided for '" + alias + ":" + Integer.toString(pinNumber) + "' is not valid.");
        } else {
            try {
                IPin pin = new Pin(pinNumber);
                if(!this.pins.containsValue(pin))
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
    public IGPIOController addPins(ObservableMap<String, Integer> pins)
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
        if(this.pins.containsKey(alias)) {
            // Destroy the pin
            this.pins.get(alias).destroy();

            // Remove the pin from the list
            this.pins.remove(alias);
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public void finalize()
    {
        try {
            super.finalize();
        } catch(Throwable throwable) {
            this.logMessageIfPossible(System.err.toString());
        }

        this.flushPins();
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
     * @inheritdoc
     */
    public IPin get(String alias)
    {
        return this.pins.get(alias);
    }

    /**
     * @inheritdoc
     */
    public ObservableMap<String, IPin> getAll()
    {
        return this.pins;
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

    /**
     * @inheritdoc
     */
    public IGPIOController sync()
    {
        try {
            Files.list(Paths.get(this.generalPath))
                    .filter(path -> {
                        String[] directoires = path.toString().split("/");
                        String directory = directoires[ directoires.length - 1 ];

                        return Files.isDirectory(path) && directory.startsWith("gpio");
                    })
                    .forEach(action -> {
                        String[] directoires = action.toString().split("/");
                        String strPin = directoires[ directoires.length - 1 ].replaceAll("gpio", "");

                        int pin = Integer.getInteger(strPin).intValue();

                        this.addPin("Default Pin Alias: " + strPin, pin);
                    });
        } catch(IOException e) {
            this.logMessageIfPossible(System.err.toString());
        }

        return this;
    }

    //region Helpers

    private void logInvalidPinNumber(String alias, int pinNumber)
    {
        this.logMessageIfPossible("Sorry, the pin number provided for '" + alias + ":" + pinNumber + "' is not valid.");
    }

    //endregion
}
