package ru.itmo;

import  ru.itmo.XMLUtils.XMLConverter;
import  ru.itmo.entities.DBLocation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import  ru.itmo.operations.PersonOperations;
import  ru.itmo.CORSFilter;

import java.util.List;

@WebServlet("/persons/locations/unique")
public class UniqLocationsSearchServlet extends HttpServlet {
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

        List<DBLocation> locations = po.listUniqLocations();

        response.setStatus(200);
        response.getWriter().write(converter.listToStr(locations, "locations", new DBLocation[0]));
    }


}
