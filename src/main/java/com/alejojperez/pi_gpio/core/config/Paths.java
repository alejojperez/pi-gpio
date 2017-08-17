package com.alejojperez.pi_gpio.core.config;

import javax.xml.bind.annotation.XmlElement;

public class Paths
{
    String direction;
    String export;
    String unexport;
    String value;
    String isInitialized;
    String generalPath;

    public String getDirection()
    {
        return direction;
    }

    @XmlElement(name = "direction")
    public Paths setDirection(String direction)
    {
        this.direction = direction;
        return this;
    }

    public String getExport()
    {
        return export;
    }

    @XmlElement(name = "export")
    public Paths setExport(String export)
    {
        this.export = export;
        return this;
    }

    public String getUnexport()
    {
        return unexport;
    }

    @XmlElement(name = "unexport")
    public Paths setUnexport(String unexport)
    {
        this.unexport = unexport;
        return this;
    }

    public String getValue()
    {
        return value;
    }

    @XmlElement(name = "value")
    public Paths setValue(String value)
    {
        this.value = value;
        return this;
    }

    public String getIsInitialized()
    {
        return isInitialized;
    }

    @XmlElement(name = "isInitialized")
    public Paths setIsInitialized(String isInitialized)
    {
        this.isInitialized = isInitialized;
        return this;
    }

    public String getGeneralPath()
    {
        return generalPath;
    }

    @XmlElement(name = "generalPath")
    public Paths setGeneralPath(String generalPath)
    {
        this.generalPath = generalPath;
        return this;
    }
}
