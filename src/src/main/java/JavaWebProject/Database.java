package src.main.java.JavaWebProject;

import java.sql.*;

public class Database {

    public static void insertSpots(Integer totalSpots, int idParking) throws SQLException {
        String query = "INSERT INTO spots (referencial, reserved, parking_id) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        Connection conBD = null;
        try {
            conBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/parkingdatabase",
                    "root", ""
            );

            PreparedStatement stmt = conBD.prepareStatement(query);

            try {
                PreparedStatement pstmt = conBD.prepareStatement("SELECT internal_id FROM parkings WHERE id = ?");
                pstmt.setInt(1, idParking);
                ResultSet rs = pstmt.executeQuery();
                Integer internalId = null;
                if (rs.next()) {
                    System.out.println(pstmt);
                    internalId = rs.getInt("internal_id");
                } else {
                    throw new SQLException("No internal ID found");
                }

                for (int i = 0; i < totalSpots; i++) {
                    int s = internalId + i;
                    String referencial = "p" + String.format("%03d", idParking) + String.format("%06d", s);
                    boolean reserved = false;

                    stmt.setString(1, referencial);
                    stmt.setBoolean(2, reserved);
                    stmt.setInt(3, idParking);

                    stmt.addBatch();
                }
            } catch (Exception error) {
                System.out.println("Error al crear el batch de InsertSpots: " + error.getMessage());
            }

            try {
                String updateQuery = "UPDATE parkings SET internal_id = internal_id + ? WHERE id = ?";
                PreparedStatement pstmt = conBD.prepareStatement(updateQuery);
                pstmt.setInt(1, totalSpots);
                pstmt.setInt(2, idParking);

                int filasActualizadas = pstmt.executeUpdate();

                System.out.println(pstmt);

                if (filasActualizadas == 0) {
                    throw new SQLException("No se actualizó ninguna fila: ID no encontrado");
                }
            } catch (Exception error) {
                System.out.println("Error al actualizar el internalId InsertSpots: " + error.getMessage());
            }

            try {
                stmt.executeBatch();
            } catch (Exception error) {
                System.out.println("Error al ejecutar el batch en InsertSpots: " + error.getMessage());
            }

        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }
    }

    public static void saveParking(String name, Integer totalSpots){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        String url = "jdbc:mysql://localhost:3306/parkingdatabase";
        String user = "root";
        String password = "";

        int newParkingId = -1;

        try (Connection conBD = DriverManager.getConnection(url, user, password);

            PreparedStatement pstmt = conBD.prepareStatement(
                    "INSERT INTO parkings (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newParkingId = generatedKeys.getInt(1);
                    System.out.println("DEBUG: Nuevo parking ID: " + newParkingId);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el nuevo parking.");
                }
            }

            insertSpots(totalSpots, newParkingId);

            try {
                pstmt.close();
                conBD.close();
            } catch (SQLException error) {
                System.out.println("Error al cerrar conexión a servidor MySQL/MariaDB: " + error.getMessage());
            }

        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }
    }

    public static void deleteParking(Integer id){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        Connection conBD = null;
        try {
            conBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/parkingdatabase",
                    "root", "");
        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }

        Statement mStm = null;
        try {
            mStm = conBD.createStatement();
        } catch (SQLException error) {
            System.out.println("Error al establecer declaración de conexión MySQL/MariaDB: " + error.getMessage());
        }

        try {
            String query = "DELETE FROM parkings WHERE id=" + id;
            System.out.println(query);
            mStm.executeUpdate(query);
        } catch (SQLException error) {
            System.out.println("Error al ejecutar SQL en servidor MySQL/MariaDB: " + error.getMessage());
        }

        try {
            mStm.close();
            conBD.close();
        } catch (SQLException error) {
            System.out.println("Error al cerrar conexión a servidor MySQL/MariaDB: " + error.getMessage());
        }

    }

    public static void armagedonCreate(){
        saveParking("Parking Rosales", 50);
        saveParking("Parking Gran Vía", 20);
        saveParking("Parking Aserje-Diurno", 0);
        saveParking("Parking Heyne", 85);
        saveParking("Parking Esperanza", 20);
    }

    public static void armagedonDelete(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        Connection conBD = null;
        try {
            conBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/parkingdatabase?allowMultiQueries=true",
                    "root", "");
        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }

        Statement mStm = null;
        try {
            mStm = conBD.createStatement();
        } catch (SQLException error) {
            System.out.println("Error al establecer declaración de conexión MySQL/MariaDB: " + error.getMessage());
        }

        try {
            String query = "SET FOREIGN_KEY_CHECKS = 0; " + // Evitar problemas de PK y restricciones
                    "TRUNCATE TABLE reserved_spots; " +
                    "TRUNCATE TABLE spot_uses_history; " +
                    "TRUNCATE TABLE parkings; " +
                    "TRUNCATE TABLE spots; " +
                    "TRUNCATE TABLE users; " +
                    "TRUNCATE TABLE vehicles; " +
                    "SET FOREIGN_KEY_CHECKS = 1;"; // Reestablecer logica de PK y restricciones
            System.out.println(query);
            mStm.executeUpdate(query);
        } catch (SQLException error) {
            System.out.println("Error al ejecutar SQL en servidor MySQL/MariaDB: " + error.getMessage());
        }

        try {
            mStm.close();
            conBD.close();
        } catch (SQLException error) {
            System.out.println("Error al cerrar conexión a servidor MySQL/MariaDB: " + error.getMessage());
        }

    }

    public static String downloadParking() {
        String resultat = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        Connection conBD = null;
        try {
            conBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/parkingdatabase",
                    "root", "");
        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }

        Statement mStm = null;
        try {
            mStm = conBD.createStatement();
        } catch (SQLException error) {
            System.out.println("Error al establecer declaración de conexión MySQL/MariaDB: " + error.getMessage());
        }

        try {
            String query = "SELECT p.id, p.name, COUNT(s.id) AS total_spots, COUNT(CASE WHEN s.reserved = TRUE THEN 1 END) AS reserved_spots " +
                    "FROM parkings p " +
                    "LEFT JOIN spots s ON p.id = s.parking_id " +
                    "GROUP BY p.id, p.name;";
            System.out.println(query);
            ResultSet rs = mStm.executeQuery(query);

            while (rs.next()){
                resultat=resultat+
                        "<tr>"+
                        "<td>"+rs.getString("id")+"</td>"+
                        "<td>"+rs.getString("name")+"</td>"+
                        "<td>"+rs.getString("total_spots")+"</td>"+
                        "<td>"+rs.getString("reserved_spots")+"</td>"+
                        "<td>"+"<button class='btn btn-outline-light me-2 transition-ss' onclick='detailParking("+rs.getString("id")+")'>Detalles</button>"+
                        "<button class='btn btn-outline-danger me-2 transition-ss' onclick='del("+rs.getString("id")+")'>Eliminar</button>"+"</td>"+
                        "</tr>";
            }
        } catch (SQLException error) {
            System.out.println("Error al ejecutar SQL en servidor MySQL/MariaDB: " + error.getMessage());
        }

        try {
            mStm.close();
            conBD.close();
        } catch (SQLException error) {
            System.out.println("Error al cerrar conexión a servidor MySQL/MariaDB: " + error.getMessage());
        }
        return resultat;
    }

    public static String downloadParkingDetails(Integer id) {
        String resultat = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        Connection conBD = null;
        try {
            conBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/parkingdatabase",
                    "root", "");
        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }

        Statement mStm = null;
        try {
            mStm = conBD.createStatement();
        } catch (SQLException error) {
            System.out.println("Error al establecer declaración de conexión MySQL/MariaDB: " + error.getMessage());
        }

        try {
            String query = "SELECT p.id, p.name, COUNT(s.id) AS total_spots, COUNT(CASE WHEN s.reserved = TRUE THEN 1 END) AS reserved_spots " +
                    "FROM parkings p " +
                    "LEFT JOIN spots s ON p.id = s.parking_id " +
                    "WHERE p.id = " + id + " " +
                    "GROUP BY p.id, p.name;";
            System.out.println(query);
            ResultSet rs = mStm.executeQuery(query);

            while (rs.next()){
                resultat=resultat+
                        "<tr>"+
                        "<td id='detailsParkingId'>"+rs.getString("id")+"</td>"+
                        "<td>"+rs.getString("name")+"</td>"+
                        "<td>"+rs.getString("total_spots")+"</td>"+
                        "<td>"+rs.getString("reserved_spots")+"</td>"+
                        "<td><button onclick='delParking("+rs.getString("id")+")'>Eliminar</button></td>"+
                        "</tr>";
            }
        } catch (SQLException error) {
            System.out.println("Error al ejecutar SQL en servidor MySQL/MariaDB: " + error.getMessage());
        }

        try {
            mStm.close();
            conBD.close();
        } catch (SQLException error) {
            System.out.println("Error al cerrar conexión a servidor MySQL/MariaDB: " + error.getMessage());
        }
        return resultat;
    }

    public static String downloadParkingSpots(Integer id) {
        String resultat = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            System.out.println("Error al cargar el driver JDBC de MySQL: " + error.getMessage());
        }

        Connection conBD = null;
        try {
            conBD = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/parkingdatabase",
                    "root", "");
        } catch (SQLException error) {
            System.out.println("Error al conectar con el servidor MySQL/MariaDB: " + error.getMessage());
        }

        Statement mStm = null;
        try {
            mStm = conBD.createStatement();
        } catch (SQLException error) {
            System.out.println("Error al establecer declaración de conexión MySQL/MariaDB: " + error.getMessage());
        }

        try {
            String query = "SELECT s.id, s.referencial, " +
                    "CASE WHEN s.reserved = 0 THEN 'Libre' ELSE 'Reservado' END AS reserved " +
                    "FROM spots s " +
                    "WHERE s.parking_id = " + id + " " +
                    "ORDER BY s.id ASC;";
            System.out.println(query);
            ResultSet rs = mStm.executeQuery(query);

            while (rs.next()){
                resultat=resultat+
                        "<tr>"+
                        "<td>"+rs.getString("id")+"</td>"+
                        "<td>"+rs.getString("referencial")+"</td>"+
                        "<td>"+rs.getString("reserved")+"</td>"+
                        "</tr>";
            }
        } catch (SQLException error) {
            System.out.println("Error al ejecutar SQL en servidor MySQL/MariaDB: " + error.getMessage());
        }

        try {
            mStm.close();
            conBD.close();
        } catch (SQLException error) {
            System.out.println("Error al cerrar conexión a servidor MySQL/MariaDB: " + error.getMessage());
        }
        return resultat;
    }
}