package ru.itmo.data.validators;

import  ru.itmo.data.Coordinates;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CoordinatesValidator  implements Validator<Coordinates> {
    public List<String> validate(Coordinates coordinates) throws IllegalAccessException {
        List<String> errorList = new ArrayList<>();
        if (coordinates == null) {
            return errorList;
        }
        for (Field f : Coordinates.class.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.get(coordinates) == null) {
                errorList.add(String.format("Coordinates %s isn't specified or have a wrong type", f.getName()));
            }
        }
        if (coordinates.getX() != null && coordinates.getX() <= -375) {
            errorList.add("Coordinates x should be bigger than -375");
        }

        if (coordinates.getY() != null && coordinates.getY() > 796) {
            errorList.add("Coordinates y should be lower or equal than 796");
        }

        return errorList;
    }
}