<center>
<h1>pi-gpio</h1>
<h4 style="margin-bottom: 60px">A package to control the gpio pins in a raspberry pi.</h4>
</center>

<center>
<h3>Use Example</h3>
</center>

<h5>Using a single pin</h5>
<pre>
public static void main(String[] args)
{
    Pin pin;
    IFileLogger logger = new FileLogger();

    try {
        pin = new Pin(11);
        pin.registerLogger(logger);

        pin.initialize();
        pin.setDirection(Pin.GPIO_OUT);
        pin.setValue(Pin.GPIO_ON);

        Thread.sleep(3000);

        pin.setValue(Pin.GPIO_OFF);
        pin.destroy();

    } catch(Exception e) {
        e.printStackTrace();
    }
}
</pre>
<hr/>
<h5>Using the pins controller</h5>
<pre>
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
</pre>

<center style="text-aling: center">
    <h3 style="margin-bottom: 0;">Apache License</h3>
    Version 2.0, January 2004<br/>
    http://www.apache.org/licenses
</center>