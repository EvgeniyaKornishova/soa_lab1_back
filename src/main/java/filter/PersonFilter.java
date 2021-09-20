package filter;

import data.Color;
import data.Country;
import entities.DBPerson;
import jakarta.servlet.http.HttpServletRequest;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PersonFilter {
    private final Map<String, Function<String, ?>> converters = Map.ofEntries(
        Map.entry("name", (String x) -> x),
        Map.entry("height", Float::parseFloat),
        Map.entry("eyeColor", Color::valueOf),
        Map.entry("hairColor", Color::valueOf),
        Map.entry("nationality", Country::valueOf),
        Map.entry("coordinates.x", Long::parseLong),
        Map.entry("coordinates.y", Double::parseDouble),
        Map.entry("location.x", Integer::parseInt),
        Map.entry("location.y", Float::parseFloat),
        Map.entry("location.z", Float::parseFloat),
        Map.entry("creationDate", LocalDateTime::parse)
    );
    private final HashMap<String, String> filters = new HashMap<>();

    public PersonFilter(HttpServletRequest request){
        List<String> param_fields = List.of("name", "height", "eye_color", "hair_color", "nationality", "coordinates_x", "coordinates_y", "location_x", "location_y", "location_z", "creation_date");
        Map<String, String> params_to_object_fields = Map.ofEntries(
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


        for (String field_name : param_fields) {
            String field_value = request.getParameter(field_name);
            if (field_value != null) {
                filters.put(params_to_object_fields.get(field_name), field_value);
            }
        }
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

    public void prepareFilter(CriteriaBuilder cb, CriteriaQuery<DBPerson> query, Root<DBPerson> root) {
        List<Predicate> predicates = new java.util.ArrayList<>(List.of());

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            try {
                String fieldName = filter.getKey();
                predicates.add(cb.equal(get_field(root, fieldName), converters.get(fieldName).apply(filter.getValue())));
            } catch (Exception ignored) {
            }
        }
        query.where(predicates.toArray(new Predicate[]{}));
    }
}
