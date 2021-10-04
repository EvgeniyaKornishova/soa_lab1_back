package ru.itmo;

import  ru.itmo.data.Color;
import  ru.itmo.data.Country;
import  ru.itmo.entities.DBPerson;
import javax.servlet.http.HttpServletRequest;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class PersonFilter {
    private final Map<String, Function<String, ?>> converters = new HashMap<String, Function<String, ?>>() {{
        put("name", (String x) -> x);
        put("height", Float::parseFloat);
        put("eyeColor", Color::valueOf);
        put("hairColor", Color::valueOf);
        put("nationality", Country::valueOf);
        put("coordinates.x", Long::parseLong);
        put("coordinates.y", Double::parseDouble);
        put("location.x", Integer::parseInt);
        put("location.y", Float::parseFloat);
        put("location.z", Float::parseFloat);
        put("creationDate", LocalDateTime::parse);
    }};
    private final HashMap<String, String> filters = new HashMap<>();

    public PersonFilter(HttpServletRequest request){
        List<String> param_fields = Arrays.asList("name", "height", "eye_color", "hair_color", "nationality", "coordinates_x", "coordinates_y", "location_x", "location_y", "location_z", "creation_date");
        Map<String, String> params_to_object_fields = new HashMap<String, String>(){
            {
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
        List<Predicate> predicates = new ArrayList<>(Collections.emptyList());

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
