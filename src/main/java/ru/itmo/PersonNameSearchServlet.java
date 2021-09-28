package ru.itmo;

import  ru.itmo.XMLUtils.XMLConverter;
import  ru.itmo.entities.DBPerson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.itmo.operations.PersonOperations;

import java.util.List;

@WebServlet("/persons/name/search")
public class PersonNameSearchServlet extends HttpServlet {
    private XMLConverter converter;
    private PersonOperations po;

    @Override
    public void init() throws ServletException {
        super.init();
        converter = new XMLConverter();
        po = new PersonOperations();
    }

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/xml");

        String name = request.getParameter("name");
        if (name == null){
            response.setStatus(400);
            response.getWriter().write("Get parameter 'name' must be specified");
            return;
        }

        List<DBPerson> dbPersonList = po.searchByName(name);

        response.setStatus(200);
        response.getWriter().write(converter.listToStr(dbPersonList, "persons", new DBPerson[0]));
    }




}
