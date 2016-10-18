/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.contracts.ILogger;
import com.alejojperez.pi_gpio.core.contracts.IPin;
import com.alejojperez.pi_gpio.core.Utils;
import javax.xml.xpath.XPathConstants;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Pin implements IPin
{
    public static final String GPIO_OUT = "out";
    public static final String GPIO_IN = "in";
    public static final String GPIO_ON = "1";
    public static final String GPIO_OFF = "0";

    /**
     * All the GPIO file paths
     */
    protected final String directionPath;
    protected final String exportPath;
    protected final String unexportPath;
    protected final String valuePath;
    protected final String isInitializedPath;
    protected final String placeholderPath;

    /**
     * Logger class in charge of logging any relevant information
     */
    protected ILogger logger;

    /**
     * The pin number
     */
    protected int pin;

    /**
     * The class constructor
     *
     * @param pinNumber
     */
    public Pin(int pinNumber) throws Exception
    {
        if(!Utils.validPinNumber(pinNumber))
            throw new Exception("Sorry, the pin number [" + Integer.toString(pinNumber) + "] is not valid.");

        this.pin = pinNumber;

        this.directionPath = (String) Utils.config("//system/GPIO/pin/paths/direction/text()", XPathConstants.STRING);
        this.exportPath = (String) Utils.config("//system/GPIO/pin/paths/export/text()", XPathConstants.STRING);
        this.unexportPath = (String) Utils.config("//system/GPIO/pin/paths/unexport/text()", XPathConstants.STRING);
        this.valuePath = (String) Utils.config("//system/GPIO/pin/paths/value/text()", XPathConstants.STRING);
        this.isInitializedPath = (String) Utils.config("//system/GPIO/paths/isInitialized/text()", XPathConstants.STRING);
        this.placeholderPath = (String) Utils.config("//system/GPIO/pin/placeholder/text()", XPathConstants.STRING);
    }

    /**
     * @inheritdoc
     */
    public IPin destroy()
    {
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized())
        {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, it can not be destroyed.");
        }
        else
        {
            try {
                // Tell the system that the pin "x" is not going to be used any more
                Files.write(Paths.get(this.unexportPath), strPin.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public String getDirection()
    {
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its direction can not be obtained.");
        }
        else
            {
            try {
                // Set the direction of the pin
                String direcPath = this.directionPath.replace(this.placeholderPath, strPin);
                return new String(Files.readAllBytes(Paths.get(direcPath)));

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return "";
    }

    /**
     * @inheritdoc
     */
    public String getValue()
    {
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its value can not be obtained.");
        }
        else
        {
            try {
                // Set the direction of the pin
                String valuePath = this.valuePath.replace(this.placeholderPath, strPin);
                return new String(Files.readAllBytes(Paths.get(valuePath)));

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return "";
    }

    /**
     * @inheritdoc
     */
    public int getPinNumber()
    {
        return this.pin;
    }

    /**
     * @inheritdoc
     */
    public IPin initialize()
    {
        String strPin = Integer.toString(this.pin);

        if(this.isInitialized())
        {
            this.logMessageIfPossible("The pin number " + strPin + " is already initialized; therefore, it can not be initialized again.");
        }
        else
        {
            try {
                // Tell the system that the pin "x" is going to be used
                Files.write(Paths.get(this.exportPath), strPin.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public boolean isInitialized()
    {
        String strPin = Integer.toString(this.pin);
        String initPath = this.isInitializedPath.replace(this.placeholderPath, strPin);

        return Files.isDirectory(Paths.get(initPath));
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
    public IPin registerLogger(ILogger logger)
    {
        this.logger = logger;

        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin setDirection(String direction)
    {
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its direction can not be set.");
        }
        else if(direction != Pin.GPIO_IN && direction != Pin.GPIO_OUT) {
            logMessageIfPossible("Sorry, the direction [" + direction + "] set for pin number " + strPin + " is not valid.");
        }
        else {
            try {
                // Set the direction of the pin
                String direcPath = this.directionPath.replace(this.placeholderPath, strPin);
                Files.write(Paths.get(direcPath), direction.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin setValue(String value)
    {
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its value can not be set.");
        }
        else if(value != Pin.GPIO_ON && value != Pin.GPIO_OFF) {
            logMessageIfPossible("Sorry, the value [" + value + "] set for pin number " + strPin + " is not valid.");
        }
        else {
            try {
                // Set the value of the pin
                String valPath = this.valuePath.replace(this.placeholderPath, strPin);
                Files.write(Paths.get(valPath), value.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }
}
