package uz.itpu.pt2.jaxb.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import uz.itpu.pt2.jaxb.JaxbXmlConstants;

import java.math.BigDecimal;

/**
 * JAXB DTO for an employee element.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "firstName",
        "lastName",
        "active",
        "salary",
        "department",
        "skills",
        "address"
})
public class EmployeeXml {

    @XmlAttribute(name = "id", required = true)
    private int id;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private String firstName;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private String lastName;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE)
    private boolean active;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private BigDecimal salary;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private String department;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private SkillsXml skills;

    @XmlElement(namespace = JaxbXmlConstants.NAMESPACE, required = true)
    private AddressXml address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public SkillsXml getSkills() {
        return skills;
    }

    public void setSkills(SkillsXml skills) {
        this.skills = skills;
    }

    public AddressXml getAddress() {
        return address;
    }

    public void setAddress(AddressXml address) {
        this.address = address;
    }
}

