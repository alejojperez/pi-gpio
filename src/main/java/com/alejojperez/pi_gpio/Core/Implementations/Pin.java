/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.Core.Implementations;

import com.alejojperez.pi_gpio.Core.Contracts.ICanLog;
import com.alejojperez.pi_gpio.Core.Contracts.ILogger;
import com.alejojperez.pi_gpio.Core.Contracts.IPin;
import com.alejojperez.pi_gpio.Core.Utils;

import javax.xml.xpath.XPathConstants;
import java.io.File;
import java.io.FileWriter;

public class Pin implements IPin, ICanLog
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
    protected final String placeholderPath;

    /**
     * The file instance used to write to the
     * system files
     */
    protected File file = null;

    /**
     * Flag to determine if the pin was successfully initialized
     */
    protected boolean initialized = false;

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
        this.placeholderPath = (String) Utils.config("//system/GPIO/pin/placeholder/text()", XPathConstants.STRING);
    }

    /**
     * @inheritdoc
     */
    public IPin initialize()
    {
        String strPin = Integer.toString(this.pin);

        if(this.initialized)
        {
            this.logMessageIfPossible("The pin number " + strPin + " is already initialized; therefore, it can not be initialized again.");
        }
        else
        {
            try {
                FileWriter fileWriter;

                // Tell the system that the pin "x" is going to be used
                fileWriter = new FileWriter(this.exportPath);
                fileWriter.write(strPin);
                fileWriter.close();

                this.initialized = true;

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin destroy()
    {
        String strPin = Integer.toString(this.pin);

        if(!this.initialized)
        {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, it can not be destroyed.");
        }
        else
        {
            try {
                FileWriter fileWriter;

                // Tell the system that the pin "x" is not going to be used any more
                fileWriter = new FileWriter(this.unexportPath);
                fileWriter.write(strPin);
                fileWriter.close();

                this.initialized = false;

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
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

        if(!this.initialized) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its direction can not be set.");
        }
        else if(direction != Pin.GPIO_IN && direction != Pin.GPIO_OUT) {
            logMessageIfPossible("Sorry, the direction [" + direction + "] set for pin number " + strPin + " is not valid.");
        }
        else {
            try {
                FileWriter fileWriter;

                // Set the direction of the pin
                String direcPath = this.directionPath.replace(this.placeholderPath, strPin);
                fileWriter = new FileWriter(direcPath);
                fileWriter.write(direction);
                fileWriter.close();

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

        if(!this.initialized) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its value can not be set.");
        }
        else if(value != Pin.GPIO_ON && value != Pin.GPIO_OFF) {
            logMessageIfPossible("Sorry, the value [" + value + "] set for pin number " + strPin + " is not valid.");
        }
        else {
            try {
                FileWriter fileWriter;

                // Set the direction of the pin
                String valPath = this.valuePath.replace(this.placeholderPath, strPin);
                fileWriter = new FileWriter(valPath, true);
                fileWriter.write(value);
                fileWriter.close();

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }
}
