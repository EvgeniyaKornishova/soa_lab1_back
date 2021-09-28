package ru.itmo.entities;

import  ru.itmo.data.Coordinates;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Objects;

@NoArgsConstructor
@Entity
@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "coordinate")
public class DBCoordinates {

    public DBCoordinates(long x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Id
    @XmlTransient
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // в модели отсутствует

    @Column(columnDefinition = "REAL CHECK (coordinate.x > -375)")
    private long x; //Значение поля должно быть больше -375

    @Column(columnDefinition = "REAL NOT NULL CHECK (coordinate.y <= 796)")
    private Double y; //Максимальное значение поля: 796, Поле не может быть null

    public void update(Coordinates data) {
        this.x = data.getX();
        this.y = data.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBCoordinates that = (DBCoordinates) o;
        return id == that.id && x == that.x && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y);
    }
}
