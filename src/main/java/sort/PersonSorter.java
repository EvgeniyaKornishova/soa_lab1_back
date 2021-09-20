package sort;

import entities.DBPerson;
import jakarta.servlet.http.HttpServletRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

public class PersonSorter {
    private String fieldName;
    private final Map<String, String> params_to_object_fields = Map.ofEntries(
            Map.entry("id", "id"),
            Map.entry("name", "name"),
            Map.entry("height", "height"),
            Map.entry("eye_color", "eyeColor"),
            Map.entry("hair_color", "hairColor"),
            Map.entry("nationality", "nationality"),
            Map.entry("creation_date", "creationDate"),
            Map.entry("location_x", "location.x"),
            Map.entry("location_y", "location.y"),
            Map.entry("location_z", "location.z"),
            Map.entry("coordinates_x", "coordinates.x"),
            Map.entry("coordinates_y", "coordinates.y"),
            Map.entry("coordinates_z", "coordinates.z")
    );

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
