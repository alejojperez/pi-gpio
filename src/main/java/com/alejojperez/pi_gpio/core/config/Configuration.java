package com.alejojperez.pi_gpio.core.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configuration")
public class Configuration
{
    GPIO gpio;

    public GPIO getGpio()
    {
        return this.gpio;
    }

    @XmlElement(name = "GPIO")
    public void setGpio(GPIO gpio)
    {
        this.gpio = gpio;
    }
}
