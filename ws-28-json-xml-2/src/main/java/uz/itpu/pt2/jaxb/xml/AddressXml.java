package uz.itpu.pt2.jaxb.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import uz.itpu.pt2.jaxb.JaxbXmlConstants;

/**
 * JAXB DTO for employee address.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"city", "country", "zipCode"})
public class AddressXml {

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private String city;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private String country;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private String zipCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}

