package uz.itpu.pt2.jaxb.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import uz.itpu.pt2.jaxb.JaxbXmlConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB DTO for employee skill collection.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"skills"})
public class SkillsXml {

    @XmlElement(name = "skill", namespace = JaxbXmlConstants.NAMESPACE)
    private List<String> skills = new ArrayList<String>();

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}

