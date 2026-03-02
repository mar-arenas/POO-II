package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos MySQL.
 * Implementa el patrón Singleton para garantizar una única instancia de conexión.
 */
public class ConexionDB {
    // Parámetros de conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "admin";

    private static Connection conexion = null;

    /**
     * Constructor privado para evitar instanciación externa
     */
    private ConexionDB() {
    }

    /**
     * Obtiene una conexión a la base de datos.
     * Si no existe una conexión activa, la crea.
     *
     * @return Connection objeto de conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConexion() throws SQLException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Si no hay conexión o está cerrada, crear una nueva
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión establecida con la base de datos");
            }
            return conexion;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL");
            System.err.println("Asegúrate de tener el conector JDBC en las librerías del proyecto");
            throw new SQLException("Driver no encontrado", e);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Cierra la conexión activa a la base de datos
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión está activa
     *
     * @return true si la conexión está activa, false en caso contrario
     */
    public static boolean isConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
