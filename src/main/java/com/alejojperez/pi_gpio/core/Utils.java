/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.alejojperez.pi_gpio.core.config.Configuration;
import com.alejojperez.pi_gpio.core.contracts.ResolveConfigurationInputStreamCallback;

import java.io.*;

public class Utils
{
    public static InputStream config;

    public static ResolveConfigurationInputStreamCallback callback;

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
            return (Configuration) jaxbUnmarshaller.unmarshal(Utils.resolveInputStream());
        }
        catch (JAXBException ignore) { }

        return null;
    }

    /**
     * @return
     */
    protected static InputStream resolveInputStream()
    {
        if(Utils.callback == null)
            Utils.callback = () -> Utils.config = Utils.class.getResourceAsStream("configuration.xml");

        Utils.callback.resolve();

        return Utils.config;
    }
}
