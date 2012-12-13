package lib;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.xml.parsers.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * ###############################################
 * <p/>
 * Kayako App
 * _______________________________________________
 *
 * @author Rajat Garg
 * @package lib
 * @copyright Copyright (c) 2001-2012, Kayako
 * @license http://www.kayako.com/license
 * @link http://www.kayako.com
 * <p/>
 * ###############################################
 */
public class XMLHandler extends DefaultHandler {

    Boolean currentElement = false;
    String currentValue = null;
    private RawArrayElement rawArrayElement = null;
    //private RawArrayElement openElement = null;
    private Stack<String> elementStack = new Stack<String>();
    private Stack<RawArrayElement> objectStack = new Stack<RawArrayElement>();

    /**
     * Called when tag starts ( ex:- <name>AndroidPeople</name>
     * -- <name> )
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {


        Map<String, String> attributeMap = new HashMap<String, String>();

        /** Start */

        for (int i = 0; i < attributes.getLength(); i++) {
            attributeMap.put(attributes.getQName(i), attributes.getValue(i));
        }
        RawArrayElement temp = new RawArrayElement(qName, attributeMap);
        if (rawArrayElement == null) {
            rawArrayElement = temp;
        }
        if (!objectStack.empty()) {
            RawArrayElement parentTemp = objectStack.pop().put(temp);
            objectStack.push(parentTemp);
        }
        objectStack.push(temp);

        currentElement = true;
    }

    /**
     * Called when tag closing ( ex:- <name>AndroidPeople</name>
     * -- </name> )
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        objectStack.pop();
        currentElement = false;

        /** set value */
    }

    /**
     * Called to get tag characters ( ex:- <name>AndroidPeople</name>
     * -- to get AndroidPeople Character )
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement && !objectStack.isEmpty()) {
            currentValue = new String(ch, start, length);
            objectStack.push(objectStack.pop().put(currentValue));
            currentElement = false;

        }

    }

    public RawArrayElement getRawArrayElement() {
        return rawArrayElement;
    }

    private RawArrayElement getCurrentElement() {
        return this.rawArrayElement;
    }


}
