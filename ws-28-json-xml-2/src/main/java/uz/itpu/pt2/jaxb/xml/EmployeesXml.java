package uz.itpu.pt2.jaxb.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import uz.itpu.pt2.jaxb.JaxbXmlConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB root wrapper for employee collection.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"employees"})
@XmlRootElement(name = "employees", namespace = JaxbXmlConstants.NAMESPACE)
public class EmployeesXml {

    @XmlElement(name = "employee", namespace = JaxbXmlConstants.NAMESPACE, type = EmployeeXml.class)
    private List<EmployeeXml> employees = new ArrayList<EmployeeXml>();

    public List<EmployeeXml> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeXml> employees) {
        this.employees = employees;
    }
}

