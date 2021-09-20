import XMLUtils.XMLConverter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import operations.PersonOperations;


@WebServlet("/calc_height")
public class HeightCalculationServlet extends HttpServlet {

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

        Double sumHeight = po.calcSumHeight();

        response.setStatus(200);
        response.getWriter().write(converter.toStr(new ServerResponse<>(sumHeight)));
    }


}
