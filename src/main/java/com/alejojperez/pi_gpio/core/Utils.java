/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.alejojperez.pi_gpio.core.config.Configuration;

import java.io.*;

public class Utils
{
    public static String config;

    /**
     * Returns configuration
     *
     * @return
     */
    public static Configuration configuration()
    {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (Configuration) jaxbUnmarshaller.unmarshal(new File(Utils.resolveInputStream()));
        }
        catch (JAXBException ignore) { }

        return null;
    }

    /**
     * @return
     */
    protected static String resolveInputStream()
    {
        if(Utils.config == null)
            Utils.config = Utils.class.getResource("configuration.xml").getPath();

        return Utils.config;
    }
}
