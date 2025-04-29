package src.main.java.JavaWebProject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/ParkingSpots")
public class ParkingSpots extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        String id = request.getParameter("id");

        response.getWriter().append(Database.downloadParkingSpots(Integer.valueOf(id)));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        String id = request.getParameter("id");
        String total_spots = request.getParameter("totalSpots");

        try {
            Database.insertSpots(Integer.valueOf(total_spots), Integer.valueOf(id));
        } catch (SQLException error) {
            System.out.println("Error al insertar datos SQL | Funcion: InsertSpots(Integer, Integer): " + error.getMessage());
        } catch (Exception error) {
            System.out.println("Error desconocido ParkingSpots POST Method: " + error.getMessage());
        }
    }
}