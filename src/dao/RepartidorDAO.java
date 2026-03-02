package dao;

import conexion.ConexionDB;
import modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones CRUD de Repartidores en la base de datos.
 * Utiliza PreparedStatement para operaciones seguras y eficientes.
 */
public class RepartidorDAO {

    /**
     * Crea un nuevo repartidor en la base de datos
     *
     * @param repartidor el repartidor a crear
     * @return true si se creó exitosamente, false en caso contrario
     */
    public boolean create(Repartidor repartidor) {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, repartidor.getNombre());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID generado automáticamente
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        repartidor.setId(rs.getInt(1));
                    }
                }
                System.out.println("Repartidor creado exitosamente con ID: " + repartidor.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al crear repartidor: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lee todos los repartidores de la base de datos
     *
     * @return lista de repartidores
     */
    public List<Repartidor> readAll() {
        List<Repartidor> repartidores = new ArrayList<>();
        String sql = "SELECT * FROM repartidores ORDER BY id";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Repartidor repartidor = new Repartidor(
                    rs.getInt("id"),
                    rs.getString("nombre")
                );
                repartidores.add(repartidor);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer repartidores: " + e.getMessage());
            e.printStackTrace();
        }

        return repartidores;
    }

    /**
     * Busca un repartidor por su ID
     *
     * @param id el ID del repartidor a buscar
     * @return el repartidor encontrado o null si no existe
     */
    public Repartidor readById(int id) {
        String sql = "SELECT * FROM repartidores WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar repartidor: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza un repartidor existente en la base de datos
     *
     * @param repartidor el repartidor con los datos actualizados
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    public boolean update(Repartidor repartidor) {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, repartidor.getNombre());
            pstmt.setInt(2, repartidor.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Repartidor actualizado exitosamente");
                return true;
            } else {
                System.out.println("No se encontró el repartidor con ID: " + repartidor.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar repartidor: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un repartidor de la base de datos
     *
     * @param id el ID del repartidor a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM repartidores WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Repartidor eliminado exitosamente");
                return true;
            } else {
                System.out.println("No se encontró el repartidor con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar repartidor: " + e.getMessage());
            System.err.println("Nota: Puede que el repartidor tenga entregas asociadas");
            e.printStackTrace();
        }

        return false;
    }
}
