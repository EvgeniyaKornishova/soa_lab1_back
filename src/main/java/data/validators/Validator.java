package data.validators;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface Validator<T> {
    List<String> validate(T value)  throws IllegalAccessException, ValidationException;
}
