package ru.itmo.data;

import  ru.itmo.XMLUtils.LocalDateTimeXMLAdapter;
import  ru.itmo.entities.DBPerson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    public Person(String name, Coordinates coordinates, Float height, Color eyeColor, Color hairColor, Country nationality, Location location) {
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Coordinates coordinates; //Поле не может быть null

    @XmlJavaTypeAdapter(LocalDateTimeXMLAdapter.class)
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private Float height; //Значение поля должно быть больше 0

    private Color eyeColor; //Поле может быть null

    private Color hairColor; //Поле может быть null

    private Country nationality; //Поле не может быть null

    private Location location; //Поле не может быть null

    public DBPerson toDBPerson() {
        return new DBPerson(
                name,
                coordinates.toDBCoordinates(),
                LocalDateTime.now(),
                height,
                eyeColor,
                hairColor,
                nationality,
                location.toDBLocation()
        );
    }
}