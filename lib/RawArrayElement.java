package lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The type Raw array element.
 * @author Rajat Garg
 * @package lib
 * @copyright Copyright (c) 2001-2012, Kayako
 * @license http ://www.kayako.com/license
 * @link http ://www.kayako.com
 * <p/>
 */
public class RawArrayElement {

    Map<String, String> attributes = new HashMap<String, String>();
    //Map<String, RawArrayElement> components = new HashMap<String, RawArrayElement>();
    ArrayList<RawArrayElement> components = new ArrayList<RawArrayElement>();
    private String content;
    private Boolean isComposite;
    private String elementName;

    /**
     * Instantiates a new Raw array element.
     */ //Constructors overloaded to be used according to the requirement
    public RawArrayElement() {
        this("Auto Element");
    }

    /**
     * Instantiates a new Raw array element.
     *
     * @param elementName the element name
     */
    public RawArrayElement(String elementName) {
        this.elementName = elementName;
        this.isComposite = false;

    }

    /**
     * Instantiates a new Raw array element.
     *
     * @param elementName the element name
     * @param attributes the attributes
     */
    public RawArrayElement(String elementName, Map<String, String> attributes) {
        this.elementName = elementName;
        this.attributes = attributes;
        this.isComposite = false;
    }

    /**
     * Instantiates a new Raw array element.
     *
     * @param elementName the element name
     * @param content the content
     */
    public RawArrayElement(String elementName, String content) {
        this.elementName = elementName;
        this.content = content;
        this.isComposite = false;
    }

    /**
     * Instantiates a new Raw array element.
     *
     * @param elementName the element name
     * @param attributes the attributes
     * @param content the content
     */
    public RawArrayElement(String elementName, Map<String, String> attributes, String content) {
        this.isComposite = false;
        this.elementName = elementName;
        this.attributes = attributes;
        this.content = content;
    }

    /**
     * Get string.
     *
     * @return the string
     * @throws IllegalAccessException the illegal access exception
     */
    public String get() throws IllegalAccessException {
        if (this.isComposite()) {
            throw new IllegalAccessException();
        }
        return this.getContent();
    }

    /**
     * Gets components.
     *
     * @return the components
     */
    public ArrayList<RawArrayElement> getComponents() {
        return this.components;
    }

    /**
     * Sets components.
     *
     * @param components the components
     */
    public void setComponents(ArrayList<RawArrayElement> components) {
        this.components = components;
    }

    /**
     * Gets components.
     *
     * @param elementName the element name
     * @return the components
     */
    public ArrayList<RawArrayElement> getComponents(String elementName) {
        ArrayList<RawArrayElement> filteredComponents = new ArrayList<RawArrayElement>();
        for (int i = 0; i < this.components.size(); i++) {
            if (this.components.get(i).elementName.equalsIgnoreCase(elementName)) {
                filteredComponents.add(this.components.get(i));
            }
        }
        return filteredComponents;
    }

    //this function can be used as a filter on RawArrayElement type, so as to act as a generic filter on all element types
    private ArrayList<RawArrayElement> filterBy(String filterName, String value) throws KayakoException {
        ArrayList<RawArrayElement> filteredComponents = new ArrayList<RawArrayElement>();
        for (RawArrayElement component : this.getComponents()) {
            ArrayList<RawArrayElement> filterComponents = component.getComponents(filterName);
            if (filterComponents.size() == 0) {
                throw new KayakoException("Supplied filter not available on this data.");
            }
            //there may be a case of multiple components to be used as a filter - any of the filtered attribute equals
            for (RawArrayElement filterComponent : filterComponents) {
                if (Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE).matcher(filterComponent.getContent()).find()) {
                    filteredComponents.add(component);
                    break;
                }
            }
        }
        return filteredComponents;
    }

    /**
     * Filter by component attribute.
     *
     * @param attributeName the attribute name
     * @param attributeValue the attribute value
     * @return the raw array element
     */
    public RawArrayElement filterByComponentAttribute(String attributeName, String attributeValue) {
        for (RawArrayElement component : this.getComponents()) {
            if (!Pattern.compile(Pattern.quote(attributeValue), Pattern.CASE_INSENSITIVE).matcher(component.getAttribute(attributeName)).find()) {
                this.components.remove(component);
            }
        }
        return this;
    }

    /**
     * Filter by component value.
     *
     * @param componentName the component name
     * @param componentValue the component value
     * @return the raw array element
     * @throws KayakoException the kayako exception
     */
    public RawArrayElement filterByComponentValue(String componentName, String componentValue) throws KayakoException {
        this.setComponents(this.filterBy(componentName, componentValue));
        return this;
    }

    /**
     * Filter by component name.
     *
     * @param componentName the component name
     * @return the raw array element
     * @throws KayakoException the kayako exception
     */
    public RawArrayElement filterByComponentName(String componentName) throws KayakoException {
        this.setComponents(this.getComponents(componentName));
        return this;
    }

    /**
     * Get string.
     *
     * @param key the key
     * @return the string
     */
    public String get(String key) {
        return this.getAttribute(key);
    }

    /**
     * Put raw array element.
     *
     * @param content the content
     * @return the raw array element
     */
    public RawArrayElement put(String content) {
        this.setComposite(false);
        this.setContent(content);
        return this;
    }

    /**
     * Put raw array element.
     *
     * @param component the component
     * @return the raw array element
     */
    public RawArrayElement put(RawArrayElement component) {
        this.setComposite(true);
        this.components.add(component);
        return this;
    }

    /**
     * Gets attribute.
     *
     * @param key the key
     * @return the attribute
     */
    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * Sets attribute.
     *
     * @param key the key
     * @param value the value
     * @return the attribute
     */
    public RawArrayElement setAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    /**
     * Gets attributes.
     *
     * @return the attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String toString() {
        String rawArrayElementString = "";
        rawArrayElementString += "<" + this.elementName;
        for (Map.Entry<String, String> attribute : this.getAttributes().entrySet()) {
            rawArrayElementString += " " + attribute.getKey() + " = '" + attribute.getValue() + "' ";
        }
        rawArrayElementString += ">\n";
        if (this.isComposite()) {
            for (RawArrayElement component : this.components) {
                rawArrayElementString += component.toString();
            }
        } else {
            rawArrayElementString += this.content + "\n";
        }
        rawArrayElementString += "</" + this.elementName + ">\n";
        return rawArrayElementString;
    }

    /**
     * Gets content.
     *
     * @return the content
     */ //Simple getters and setters
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     * @return the content
     */
    public RawArrayElement setContent(String content) {
        if (this.isComposite) {
            return this;
        }
        this.content = content;
        return this;
    }

    /**
     * Is composite.
     *
     * @return the boolean
     */
    public Boolean isComposite() {
        return this.isComposite;
    }

    /**
     * Gets element name.
     *
     * @return the element name
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Sets element name.
     *
     * @param elementName the element name
     * @return the element name
     */
    public RawArrayElement setElementName(String elementName) {
        this.elementName = elementName;
        return this;
    }

    /**
     * Sets composite.
     *
     * @param composite the composite
     * @return the composite
     */
    public RawArrayElement setComposite(Boolean composite) {
        isComposite = composite;
        return this;
    }
}
