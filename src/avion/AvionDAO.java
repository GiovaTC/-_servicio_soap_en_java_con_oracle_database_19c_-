package avion;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AvionDAO {

    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USER = "system";
    private static final String PASS = "Tapiero123";

    public void registrarAvion(Avion avion) {

        String sql = "INSERT INTO AVION (MODELO, FABRICANTE, CAPACIDAD, AUTONOMIA) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, avion.getModelo());
            ps.setString(2, avion.getFabricante());
            ps.setInt(3, avion.getCapacidad());
            ps.setInt(4, avion.getAutonomia());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
