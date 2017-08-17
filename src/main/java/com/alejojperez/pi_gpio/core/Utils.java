/**
 * Created by Alejandro Perez on 4/28/16
 * github page: https://github.com/alejojperez
 */
package com.alejojperez.pi_gpio.core;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import java.io.InputStream;

public class Utils
{
    public static InputStream config;

    /**
     * Returns a configuration value based on the XPtah expression
     *
     * @param xPathExpression
     * @param resultType
     * @return
     */
    public static Object config(String xPathExpression, QName resultType)
    {
        return Utils.xmlValue(Utils.resolveInputStream(), xPathExpression, resultType);
    }

    /**
     * Returns an xml value based on the XPtah expression
     *
     * @param xPathExpression
     * @param resultType
     * @return
     */
    public static Object xmlValue(InputStream filePath, String xPathExpression, QName resultType)
    {
        Object result;

        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);

            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();

            XPathExpression expr = xpath.compile(xPathExpression);
            result = expr.evaluate(doc, resultType);
        }
        catch(Exception e)
        {
            result = null;
        }

        return result;
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
