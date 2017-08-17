/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import com.alejojperez.pi_gpio.core.config.Configuration;
import org.w3c.dom.Document;

import java.io.InputStream;

public class Utils
{
    public static InputStream config;

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
        if(Utils.config != null)
            return Utils.config;

        return Utils.class.getResourceAsStream("configuration.xml");
    }
}
