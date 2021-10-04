package ru.itmo.data.validators;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SorterValidator implements Validator<HttpServletRequest> {
    private static final List<String> possibleValues = new ArrayList<>(Arrays.asList(
            "id",
            "name",
            "height",
            "eye_color",
            "hair_color",
            "nationality",
            "creation_date",
            "location_x",
            "location_y",
            "location_z",
            "coordinates_x",
            "coordinates_y",
            "coordinates_z"
    ));

    @Override
    public List<String> validate(HttpServletRequest request) {
        List<String> errorList = new ArrayList<>();

        String sort = request.getParameter("sort");
        if (sort != null && !possibleValues.contains(sort))
            errorList.add("unknown sort field");

        return errorList;
    }
}
