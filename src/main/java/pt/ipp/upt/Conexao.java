package pt.ipp.upt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	private static final String URL = "jdbc:mysql://localhost:3306/objetos_encontrados_perdidos";
    private static final String UTILIZADOR = "root";
    private static final String PASSWORD = "password";

    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, UTILIZADOR, PASSWORD);
    }
}