package operations;

import entities.DBPerson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
public class PersonList {

    @XmlAttribute
    Long count;

    @XmlElement(name="person")
    List<DBPerson> persons;
}
