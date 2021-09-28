package ru.itmo.data.validators;
import  ru.itmo.data.Person;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.ValidationException;
import javax.xml.namespace.QName;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PersonValidator implements Validator<Person> {

    private final CoordinatesValidator coordinatesValidator;
    private final LocationValidator locationValidator;

//    private final String correctForm;
    private final List<String> nullableFields;
    private final List<String> prohibitedFields;

    public PersonValidator() {
        coordinatesValidator = new CoordinatesValidator();
        locationValidator = new LocationValidator();
        nullableFields = List.of("eyeColor", "hairColor");
        prohibitedFields = List.of("id", "creationDate");
//        correctForm = "Correct format:\n" +
//                "<person>\n" +
//                "    <name>String</name>\n" +
//                "    <coordinates>\n" +
//                "        <x>long > -375</x>\n" +
//                "        <y>double <= 796</y>\n" +
//                "    </coordinates>\n" +
//                "    <height>float</height>\n" +
//                "    <eyeColor>optional (GREEN, BLACK, YELLOW, ORANGE, WHITE)</eyeColor>\n" +
//                "    <hairColor>optional (GREEN, BLACK, YELLOW, ORANGE, WHITE)</hairColor>\n" +
//                "    <nationality>(INDIA, VATICAN, NORTH_KOREA, JAPAN)</nationality>\n" +
//                "    <location>\n" +
//                "        <x>int</x>\n" +
//                "        <y>float</y>\n" +
//                "        <z>float</z>\n" +
//                "    </location>\n" +
//                "</person>";
    }

    @Override
    public List<String> validate(Person person) throws IllegalAccessException, ValidationException {
        List<String> errorList = new ArrayList<>();

        for (Field field : Person.class.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(person) == null) {
                if (!(nullableFields.contains(field.getName()) || prohibitedFields.contains(field.getName())))
                    errorList.add((String.format("person %s isn't specified or have a wrong type", field.getName())));
            } else {
                if (prohibitedFields.contains(field.getName())){
                    field.set(person, null);
                }
            }
        }

        if (person.getName() != null && person.getName().trim().length() == 0) {
            errorList.add("person name should be not empty");
        }

        if (person.getHeight() != null && person.getHeight() <= 0.0f) {
            errorList.add("person height should be bigger than 0");
        }

        errorList.addAll(coordinatesValidator.validate(person.getCoordinates()));
        errorList.addAll(locationValidator.validate(person.getLocation()));

//        if (!errorList.isEmpty()){
//            errorList.add(correctForm);
//        }

        return errorList;
    }
}
