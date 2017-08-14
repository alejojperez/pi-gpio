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
    protected static GPIOController instance;

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
    }

    /**
     * @inheritdoc
     */
    public IGPIOController addPin(IPin pin)
    {
        try {
            // Check if the pin exists and if the new pin does not
            // have an alias, if so, set the same alias it had before
            if((pin.getAlias() == null || pin.getAlias().isEmpty()) && this.pins.containsKey(pin.getGPIOPin()))
                pin.setAlias(this.pins.get(pin.getGPIOPin()).getAlias());

            this.pins.put(pin.getGPIOPin(), pin);

            if(this.logger != null)
                this.pins.get(pin.getGPIOPin()).registerLogger(this.logger);
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
        if(this.pins.containsKey(pin.getGPIOPin())) {
            // Destroy the pin
            this.pins.get(pin.getGPIOPin()).destroy();

            // Remove the pin from the list
            this.pins.remove(pin.getGPIOPin());
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
        if(instance == null)
             instance = new GPIOController();

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
    public void startFolderWatcher()
    {
        if(this.folderWatcher != null && this.folderWatcher.getExecutor() != null && !this.folderWatcher.getExecutor().isShutdown())
        {
            this.logMessageIfPossible("The folder watch cannot we started because it is already running.");
            return;
        }

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
    public void stopFolderWatcher()
    {
        if(this.folderWatcher == null)
        {
            this.logMessageIfPossible("The folder watch cannot we stopped because it is not running.");
            return;
        }

        this.folderWatcher.stop();
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
                            this.addPin(new Pin(Integer.parseInt(strPin), 0));
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
