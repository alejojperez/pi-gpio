/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core.implementations;

import com.alejojperez.pi_gpio.core.contracts.IFolderWatcher;
import com.alejojperez.pi_gpio.core.contracts.IGPIOController;
import com.alejojperez.pi_gpio.core.contracts.ILogger;
import com.alejojperez.pi_gpio.core.contracts.IPin;
import com.alejojperez.pi_gpio.core.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import javax.xml.xpath.XPathConstants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

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
    protected ObservableMap<Integer, IPin> pins = FXCollections.observableMap(new HashMap<Integer, IPin>());

    /**
     * Service to listen for folder change
     */
    protected IFolderWatcher folderWatcher;

    /**
     * Create a private constructor, so the
     * class can not be instantiate
     */
    private GPIOController()
    {
        this.generalPath = (String) Utils.config("//system/GPIO/pin/paths/generalPath/text()", XPathConstants.STRING);
        this.sync();

        try {
            this.folderWatcher = new FolderWatcher(Paths.get(this.generalPath), true);
            this.folderWatcher.start(this);
        } catch(IOException e) {
            this.logMessageIfPossible(e);
        }
    }

    /**
     * @inheritdoc
     */
    public IGPIOController addPin(IPin pin)
    {
        try {
            if(!this.pins.containsValue(pin)) {
                this.pins.put(pin.getPinNumber(), pin);

                if(this.logger != null)
                    this.pins.get(pin.getPinNumber()).registerLogger(this.logger);
            }
        } catch(Exception e) {
            this.logMessageIfPossible(e);
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController addPins(ObservableMap<Integer, IPin> pins)
    {
        pins.forEach((pinNumber, pin) -> this.addPin(pin));

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController deletePin(IPin pin)
    {
        if(this.pins.containsKey(pin.getPinNumber())) {
            // Destroy the pin
            this.pins.get(pin.getPinNumber()).destroy();

            // Remove the pin from the list
            this.pins.remove(pin.getPinNumber());
        }

        return this;
    }

    /**
     * @inheritdoc
     */
    public IGPIOController flushPins()
    {
        this.pins.forEach((pinNumber, pin) -> this.deletePin(pin));

        return this;
    }

    /**
     * @inheritdoc
     */
    public IPin get(Integer pinNumber)
    {
        return this.pins.get(pinNumber);
    }

    @Override
    public ObservableMap<Integer, IPin> getPins()
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
                        String[] directories = path.toString().split("/");
                        String directory = directories[ directories.length - 1 ];

                        return Files.isDirectory(path) && directory.startsWith("gpio");
                    })
                    .forEach(action -> {
                        String[] directories = action.toString().split("/");
                        String strPin = directories[ directories.length - 1 ].replaceAll("gpio", "");

                        try {
                            this.addPin(new Pin(Integer.parseInt(strPin)));
                        } catch(Exception e) {
                            this.logMessageIfPossible(System.err.toString());
                        }
                    });
        } catch(IOException e) {
            this.logMessageIfPossible(System.err.toString());
        }

        return this;
    }
}
