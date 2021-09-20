import XMLUtils.XMLConverter;
import data.*;
import data.validators.PersonValidator;
import entities.DBPerson;
import filter.PersonFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import operations.PersonList;
import operations.PersonOperations;
import pagination.Paginator;
import sort.PersonSorter;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/persons")
public class PersonOperationsServlet extends HttpServlet {

    private XMLConverter converter;
    private PersonOperations po;
    private PersonValidator validator;

    @Override
    public void init() throws ServletException {
        super.init();
        converter = new XMLConverter();
        po = new PersonOperations();
        validator = new PersonValidator();
    }

    String getBody(HttpServletRequest request) throws IOException {
        String body;
        BufferedReader br = null;
        try {
            br = request.getReader();
            body = br.lines().collect(Collectors.joining());
        } finally {
            if (br != null)
                br.close();
        }
        return body;
    }

    @Override
    @SneakyThrows
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        Person person = converter.fromStr(getBody(request), Person.class);

        List<String> errors = validator.validate(person);
        if (!errors.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
            return;
        }

        long id = po.createPerson(person.toDBPerson());

        response.setStatus(200);
        response.getWriter().write(converter.toStr(new ServerResponse<>(id)));
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        String id_param = request.getParameter("id");

        // if id present then need return 1 person, otherwise all persons
        if (id_param != null) {
            long id = Long.parseLong(id_param);

            Optional<DBPerson> opDbPerson = po.getPerson(id);
            if (opDbPerson.isEmpty()) {
                response.setStatus(404);
                response.getWriter().write("Person with specified id not found");
                return;
            }
            DBPerson dbPerson = opDbPerson.get();

            response.setStatus(200);
            response.getWriter().write(converter.toStr(dbPerson));
        }
        else {
            PersonFilter filter = new PersonFilter(request);
            PersonSorter sorter = new PersonSorter(request);
            Paginator paginator = new Paginator(request);

            List<DBPerson> dbPersonList = po.listPerson(filter, sorter, paginator);
            Long count = po.countPersons();
            PersonList personList = new PersonList(count, dbPersonList);

            response.setStatus(200);
            response.getWriter().write(converter.toStr(personList));
        }
    }

    @Override
    @SneakyThrows
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        Person person = converter.fromStr(getBody(request), Person.class);

        List<String> errors = validator.validate(person);
        if (!errors.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write(converter.listToStr(errors, "errors", new String[0]));
            return;
        }

        String id_param = request.getParameter("id");
        if (id_param == null){
            response.setStatus(400);
            response.getWriter().write("Get parameter id must be specified");
            return;
        }
        long id = Long.parseLong(id_param);

        Optional<DBPerson> opDbPerson = po.getPerson(id);

        if (opDbPerson.isEmpty()) {
            response.setStatus(404);
            response.getWriter().write("Person with specified id not found");
            return;
        }
        DBPerson dbPerson = opDbPerson.get();

        dbPerson.update(person);
        po.updatePerson(dbPerson);

        response.setStatus(200);
    }

    @Override
    @SneakyThrows
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        String id_param = request.getParameter("id");
        if (id_param == null){
            response.setStatus(400);
            response.getWriter().write("Get parameter id must be specified");
            return;
        }
        long id = Long.parseLong(id_param);

        if (!po.deletePerson(id)) {
            response.setStatus(404);
            response.getWriter().write("Person with specified id not found");
            return;
        }

        response.setStatus(200);
    }

}

