package com.alejojperez.pi_gpio.core.config;

import javax.xml.bind.annotation.XmlElement;

public class GPIO
{
    String placeholder;
    Paths paths;

    public String getPlaceholder()
    {
        return this.placeholder;
    }

    @XmlElement(name = "placeholder")
    public void setPlaceholder(String placeholder)
    {
        this.placeholder = placeholder;
    }

    public Paths getPaths()
    {
        return this.paths;
    }

    @XmlElement(name = "paths")
    public void setPaths(Paths paths)
    {
        this.paths = paths;
    }
}
