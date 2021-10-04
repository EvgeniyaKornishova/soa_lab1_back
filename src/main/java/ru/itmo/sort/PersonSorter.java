package ru.itmo.sort;

import ru.itmo.entities.DBPerson;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PersonSorter {
    private String fieldName;
    private final Map<String, String> params_to_object_fields = new HashMap<String, String>() {{
            put("id", "id");
            put("name", "name");
            put("height", "height");
            put("eye_color", "eyeColor");
            put("hair_color", "hairColor");
            put("nationality", "nationality");
            put("creation_date", "creationDate");
            put("location_x", "location.x");
            put("location_y", "location.y");
            put("location_z", "location.z");
            put("coordinates_x", "coordinates.x");
            put("coordinates_y", "coordinates.y");
            put("coordinates_z", "coordinates.z");
        }};

    public PersonSorter(HttpServletRequest request){
       String sortParam = request.getParameter("sort");

       if (sortParam != null)
           fieldName = params_to_object_fields.get(sortParam);
       else
           fieldName = null;
    }

    private Path<Object> get_field(Root<DBPerson> root, String fieldPath){
        String[] path = fieldPath.split("\\.");

        Path<Object> field = null;

        for (String fieldName : path){
            if (field != null) field = field.get(fieldName);
            else field = root.get(fieldName);
        }

        return field;
    }

    public void prepareSort(CriteriaBuilder cb, CriteriaQuery<DBPerson> query, Root<DBPerson> root){
        if (fieldName != null){
            query.orderBy(cb.asc(get_field(root, fieldName)));
        }
    }
}
