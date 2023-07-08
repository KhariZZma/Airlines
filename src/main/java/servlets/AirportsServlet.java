package servlets;

import dao.AirportDao;
import database.OwnConnectionPool;
import dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Airport;
import services.ResponseService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet(name = "AirportsServlet", value = "/airports")
public class AirportsServlet extends HttpServlet {
    private AirportDao airportDao;
    OwnConnectionPool connectionPool;
    @Override
    public void init() throws ServletException {
        super.init();
        connectionPool = (OwnConnectionPool) getServletContext().getAttribute("connPool");
        airportDao = new AirportDao(connectionPool.getConnection());
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Airport> airports = airportDao.getAll();
            ResponseService.send(airports, response);
        } catch (SQLException sqlException) {
            new ErrorResponse(SC_INTERNAL_SERVER_ERROR, "Database is not available").send(response);
        }
    }
}
