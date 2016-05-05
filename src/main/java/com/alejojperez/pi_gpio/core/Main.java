/**
 * Created by Alejandro Perez on 04/28/2016
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.Core;

import com.alejojperez.pi_gpio.Core.Contracts.IFileLogger;
import com.alejojperez.pi_gpio.Core.Contracts.IGPIOController;
import com.alejojperez.pi_gpio.Core.Implementations.FileLogger;
import com.alejojperez.pi_gpio.Core.Implementations.GPIOController;
import com.alejojperez.pi_gpio.Core.Implementations.Pin;

public class Main
{
    public static void main(String[] args)
    {
        IGPIOController controller = GPIOController.getInstance();
        IFileLogger logger = new FileLogger();

        try {
            controller.addPin("red-pin", 11).get("red-pin").registerLogger(logger).initialize().setDirection(Pin.GPIO_OUT);
            controller.addPin("blue-pin", 12).get("blue-pin").registerLogger(logger).initialize().setDirection(Pin.GPIO_OUT);
            controller.addPin("yellow-pin", 13).get("yellow-pin").registerLogger(logger).initialize().setDirection(Pin.GPIO_OUT);

            controller.get("red-pin").setValue(Pin.GPIO_ON);
            Thread.sleep(3000);
            controller.get("red-pin").setValue(Pin.GPIO_OFF);

            controller.get("blue-pin").setValue(Pin.GPIO_ON);
            Thread.sleep(3000);
            controller.get("blue-pin").setValue(Pin.GPIO_OFF);

            controller.get("yellow-pin").setValue(Pin.GPIO_ON);
            Thread.sleep(3000);
            controller.get("yellow-pin").setValue(Pin.GPIO_OFF);

            controller.flushPins();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
