/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.config.Configuration;
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
    private final String directionPath;
    private final String exportPath;
    private final String unexportPath;
    private final String valuePath;
    private final String isInitializedPath;
    private final String placeholderPath;

    /**
     * The pin alias
     */
    private String alias = "";

    /**
     * Determines if the alias is editable or not
     */
    private boolean editable = false;

    /**
     * Determines if it is 5 volts power
     */
    private boolean fiveVolts = false;

    /**
     * Determines if it is 3 volts power
     */
    private boolean threeVolts = false;

    /**
     * Determines if it is ground
     */
    private boolean ground = false;

    /**
     * Logger class in charge of logging any relevant information
     */
    private ILogger logger;

    /**
     * The pin number
     */
    private int pin;

    /**
     * The pin gpio number
     */
    private int gpioPin;

    /**
     * Constructor
     * @param gpioPin the gpio pin's number
     * @param pinNumber the pin's number
     * @throws Exception
     */
    public Pin(int gpioPin, int pinNumber) throws Exception
    {
        this.gpioPin = gpioPin;
        this.pin = pinNumber;

        Configuration config = Utils.configuration();

        this.directionPath = config.getGpio().getPaths().getDirection();
        this.exportPath = config.getGpio().getPaths().getExport();
        this.unexportPath = config.getGpio().getPaths().getUnexport();
        this.valuePath = config.getGpio().getPaths().getValue();
        this.isInitializedPath = config.getGpio().getPaths().getIsInitialized();
        this.placeholderPath = config.getGpio().getPaths().getGeneralPath();
    }

    /**
     * @inheritdoc
     */
    public IPin destroy()
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized())
        {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, it can not be destroyed.");
        }
        else
        {
            try {
                // Tell the system that the pin "x" is not going to be used any more
                Files.write(Paths.get(this.unexportPath), strGPIOPin.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public String getAlias()
    {
        return this.alias;
    }

    /**
     * @inheritdoc
     */
    public String getDirection()
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its direction can not be obtained.");
        }
        else
            {
            try {
                // Set the direction of the pin
                String direcPath = this.directionPath.replace(this.placeholderPath, strGPIOPin);
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
    public int getGPIOPin()
    {
        return this.gpioPin;
    }

    /**
     * @inheritdoc
     */
    public int getPin()
    {
        return this.pin;
    }

    /**
     * @inheritdoc
     */
    public String getValue()
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its value can not be obtained.");
        }
        else
        {
            try {
                // Set the direction of the pin
                String valuePath = this.valuePath.replace(this.placeholderPath, strGPIOPin);
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
    public IPin initialize()
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String strPin = Integer.toString(this.pin);

        if(this.isInitialized())
        {
            this.logMessageIfPossible("The pin number " + strPin + " is already initialized; therefore, it can not be initialized again.");
        }
        else
        {
            try {
                // Tell the system that the pin "x" is going to be used
                Files.write(Paths.get(this.exportPath), strGPIOPin.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public boolean isEditable()
    {
        return this.editable;
    }

    /**
     * @inheritdoc
     */
    public boolean isFiveVolts()
    {
        return this.fiveVolts;
    }

    /**
     * @inheritdoc
     */
    public boolean isGround()
    {
        return ground;
    }

    /**
     * @inheritdoc
     */
    public boolean isInitialized()
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String initPath = this.isInitializedPath.replace(this.placeholderPath, strGPIOPin);

        return Files.isDirectory(Paths.get(initPath));
    }

    /**
     * @inheritdoc
     */
    public boolean isThreeVolts()
    {
        return this.threeVolts;
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
    public Pin setAlias(String alias)
    {
        this.alias = alias;
        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin setDirection(String direction)
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its direction can not be set.");
        }
        else if(!direction.equals(Pin.GPIO_IN) && !direction.equals(Pin.GPIO_OUT)) {
            logMessageIfPossible("Sorry, the direction [" + direction + "] set for pin number " + strPin + " is not valid.");
        }
        else {
            try {
                // Set the direction of the pin
                String direcPath = this.directionPath.replace(this.placeholderPath, strGPIOPin);
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
    public Pin setEditable(boolean editable)
    {
        this.editable = editable;
        return this;
    }

    /**
     * @inheritdoc
     */
    public Pin setFiveVolts(boolean fiveVolts)
    {
        this.fiveVolts = fiveVolts;
        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin setGPIOPin(int gpioPin)
    {
        this.gpioPin = gpioPin;

        return  this;
    }

    /**
     * @inheritdoc
     */
    public Pin setGround(boolean ground)
    {
        this.ground = ground;
        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin setPin(int pin)
    {
        this.pin = pin;

        return this;
    }

    /**
     * @inheritdoc
     */
    public Pin setThreeVolts(boolean threeVolts)
    {
        this.threeVolts = threeVolts;
        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin setValue(String value)
    {
        String strGPIOPin = Integer.toString(this.gpioPin);
        String strPin = Integer.toString(this.pin);

        if(!this.isInitialized()) {
            this.logMessageIfPossible("The pin number " + strPin + " is not initialized; therefore, its value can not be set.");
        }
        else if(!value.equals(Pin.GPIO_ON) && !value.equals(Pin.GPIO_OFF)) {
            this.logMessageIfPossible("Sorry, the value [" + value + "] set for pin number " + strPin + " is not valid.");
        }
        else {
            try {
                // Set the value of the pin
                String valPath = this.valuePath.replace(this.placeholderPath, strGPIOPin);
                Files.write(Paths.get(valPath), value.getBytes());

            } catch(Exception e) {
                this.logMessageIfPossible(e);
            }
        }

        return this;
    }
}
