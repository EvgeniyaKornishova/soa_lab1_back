package data;

import entities.DBLocation;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {
    private Integer x; //Поле не может быть null
    private Float y;
    private Float z;

    public DBLocation toDBLocation(){return new DBLocation(x, y, z);}
}
