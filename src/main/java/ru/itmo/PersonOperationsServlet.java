package ru.itmo;

import ru.itmo.XMLUtils.XMLConverter;
import ru.itmo.data.Person;
import ru.itmo.data.validators.FilterValidator;
import ru.itmo.data.validators.PaginatorValidator;
import ru.itmo.data.validators.PersonValidator;
import ru.itmo.data.validators.SorterValidator;
import ru.itmo.entities.DBLocation;
import ru.itmo.entities.DBPerson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import ru.itmo.operations.PersonList;
import ru.itmo.operations.PersonOperations;
import ru.itmo.pagination.Paginator;
import ru.itmo.sort.PersonSorter;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/persons/*")
public class PersonOperationsServlet extends HttpServlet {

    private XMLConverter converter;
    private PersonOperations po;
    private PersonValidator personValidator;
    private PaginatorValidator paginatorValidator;
    private SorterValidator sorterValidator;
    private FilterValidator filterValidator;


    @Override
    public void init() throws ServletException {
        super.init();
        converter = new XMLConverter();
        po = new PersonOperations();
        personValidator = new PersonValidator();
        paginatorValidator = new PaginatorValidator();
        sorterValidator = new SorterValidator();
        filterValidator = new FilterValidator();
    }

    String getBody(HttpServletRequest request) throws IOException {
        String body;
        try (BufferedReader br = request.getReader()) {
            body = br.lines().collect(Collectors.joining());
        }
        return body;
    }

    Long getIdFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null)
            return null;

        String[] servletPath = pathInfo.split("/");
        String id_param;
        if (servletPath.length == 2) {
            id_param = servletPath[1];
        } else {
            return null;
        }

        long id;
        try {
            id = Long.parseLong(id_param);
        } catch (NumberFormatException e) {
            return null;
        }

        if (id <= 0) {
            return null;
        }

        return id;
    }

    @Override
    @SneakyThrows
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            Person person;
            try {
                person = converter.fromStr(getBody(request), Person.class);
            } catch (javax.xml.bind.JAXBException e) {
                response.setStatus(400);
                if (e.getMessage() != null)
                    response.getWriter().write("Unknown enum value");
                else
                    response.getWriter().write("Invalid XML structure");
                return;
            }

            List<String> errors = personValidator.validate(person);
            if (!errors.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
                return;
            }

            long id = po.createPerson(person.toDBPerson());

            response.setStatus(201);
            response.getWriter().write(converter.toStr(new ServerResponse<>(id)));
        } else {
            response.setStatus(404);
            response.getWriter().write("Page not found");
        }
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        // if id present then need return 1 person, otherwise all persons
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {

            List<String> errors = paginatorValidator.validate(request);
            if (!errors.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
                return;
            }

            errors = sorterValidator.validate(request);
            if (!errors.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
                return;
            }

            errors = filterValidator.validate(request);
            if (!errors.isEmpty()) {
                response.setStatus(400);
                response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
                return;
            }

            PersonFilter filter = new PersonFilter(request);
            PersonSorter sorter = new PersonSorter(request);
            Paginator paginator = new Paginator(request);

            List<DBPerson> dbPersonList = po.listPerson(filter, sorter, paginator);
            Long count = po.countPersons();
            PersonList personList = new PersonList(count, dbPersonList);

            response.setStatus(200);
            response.getWriter().write(converter.toStr(personList));
        } else {
            String[] servletPath = pathInfo.split("/");
            if (servletPath.length == 2) {


                Long id = getIdFromPath(request);
                if (id == null) {
                    response.setStatus(404);
                    response.getWriter().write("Page not found");
                    return;
                }

                Optional<DBPerson> opDbPerson = po.getPerson(id);
                if (!opDbPerson.isPresent()) {
                    response.setStatus(404);
                    response.getWriter().write("Person with specified id not found");
                    return;
                }
                DBPerson dbPerson = opDbPerson.get();

                response.setStatus(200);
                response.getWriter().write(converter.toStr(dbPerson));
            } else if (servletPath.length == 3) {
                switch (servletPath[1]) {
                    case "locations":
                        if (servletPath[2].equals("uniq")) {
                            List<DBLocation> locations = po.listUniqLocations();

                            response.setStatus(200);
                            response.getWriter().write(converter.listToStr(locations, "locations", new DBLocation[0]));
                            return;
                        }
                        break;
                    case "heights":
                        if (servletPath[2].equals("sum")) {
                            Double sumHeight = po.calcSumHeight();

                            response.setStatus(200);
                            response.getWriter().write(converter.toStr(new ServerResponse<>(sumHeight)));
                            return;
                        }
                        break;
                    case "names":
                        if (servletPath[2].equals("search")) {
                            String name = request.getParameter("name");
                            if (name == null) {
                                response.setStatus(400);
                                response.getWriter().write("Get parameter 'name' must be specified");
                                return;
                            }

                            List<DBPerson> dbPersonList = po.searchByName(name);

                            response.setStatus(200);
                            response.getWriter().write(converter.listToStr(dbPersonList, "persons", new DBPerson[0]));
                            return;
                        }
                        break;
                }
            }

            response.setStatus(404);
            response.getWriter().write("Page not found");
        }
    }

    @Override
    @SneakyThrows
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        Long id = getIdFromPath(request);
        if (id == null) {
            response.setStatus(404);
            response.getWriter().write("Page not found");
            return;
        }

        Person person;
        try {
            person = converter.fromStr(getBody(request), Person.class);
        } catch (javax.xml.bind.JAXBException e) {
            response.setStatus(400);
            response.getWriter().write("Invalid XML structure");
            return;
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
            response.getWriter().write("Invalid enum value");
            return;
        }

        List<String> errors = personValidator.validate(person);
        if (!errors.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
            return;
        }

        Optional<DBPerson> opDbPerson = po.getPerson(id);

        if (!opDbPerson.isPresent()) {
            response.setStatus(404);
            response.getWriter().write("Person with specified id not found");
            return;
        }
        DBPerson dbPerson = opDbPerson.get();

        dbPerson.update(person);
        po.updatePerson(dbPerson);

        response.setStatus(204);
    }

    @Override
    @SneakyThrows
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        Long id = getIdFromPath(request);
        if (id == null) {
            response.setStatus(404);
            response.getWriter().write("Page not found");
            return;
        }

        if (!po.deletePerson(id)) {
            response.setStatus(404);
            response.getWriter().write("Person with specified id not found");
            return;
        }

        response.setStatus(204);
    }

}

