package ru.itmo.data.validators;

import ru.itmo.data.Color;
import ru.itmo.data.Country;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FilterValidator implements Validator<HttpServletRequest> {
    @Override
    public List<String> validate(HttpServletRequest request) {
        List<String> errorList = new ArrayList<>();

        String sHeight = request.getParameter("height");

        String sEyeColor = request.getParameter("eye_color");
        String sHairColor = request.getParameter("hair_color");
        String sNationality = request.getParameter("nationality");

        String sCoordinatesX = request.getParameter("coordinates_x");
        String sCoordinatesY = request.getParameter("coordinates_y");
        String sLocationX = request.getParameter("location_x");
        String sLocationY = request.getParameter("location_y");
        String sLocationZ = request.getParameter("location_z");

        String sCreationDate = request.getParameter("creation_date");

        if (sHeight != null){
            Integer height = null;
            try{
                height = Integer.parseInt(sHeight);
            }catch (NumberFormatException e){
                errorList.add("height should be positive not zero number");
            }
            if (height!= null && height <= 0){
                errorList.add("height should be positive not zero number");
            }
        }

        if (sCoordinatesX != null){
            try{
                Integer.parseInt(sCoordinatesX);
            }catch (NumberFormatException e){
                errorList.add("coordinates_x should be an integer");
            }
        }

        if (sCoordinatesY != null){
            try{
                Double.parseDouble(sCoordinatesY);
            }catch (NumberFormatException e){
                errorList.add("coordinates_y should be a float number");
            }
        }

        if (sLocationX != null){
            try{
                Integer.parseInt(sLocationX);
            }catch (NumberFormatException e){
                errorList.add("location_x should be an integer");
            }
        }

        if (sLocationY != null){
            try{
                Float.parseFloat(sLocationY);
            }catch (NumberFormatException e){
                errorList.add("location_y should be a float number");
            }
        }

        if (sLocationZ != null){
            try{
                Float.parseFloat(sLocationZ);
            }catch (NumberFormatException e){
                errorList.add("location_z should be a float number");
            }
        }

        if (sEyeColor != null){
            try{
                Color.valueOf(sEyeColor);
            }catch (IllegalArgumentException e){
                errorList.add("eye_color should be one of: green, black, yellow, orange, white ");
            }
        }

        if (sHairColor != null){
            try{
                Color.valueOf(sHairColor);
            }catch (IllegalArgumentException e){
                errorList.add("hair_color should be one of: green, black, yellow, orange, white ");
            }
        }

        if (sNationality != null){
            try{
                Country.valueOf(sNationality);
            }catch (IllegalArgumentException e){
                errorList.add("nationality should be one of: india, vatican, north_korea, japan");
            }
        }

        if (sCreationDate != null){
            try{
                LocalDateTime.parse(sCreationDate);
            }catch (DateTimeParseException e){
                errorList.add("creation_date should have datetime format like: 2021-09-19T10:18:27.254270");
            }
        }

        return errorList;
    }
}
