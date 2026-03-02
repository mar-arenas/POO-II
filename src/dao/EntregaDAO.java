package dao;

import conexion.ConexionDB;
import modelo.Entrega;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones CRUD de Entregas en la base de datos.
 * Utiliza PreparedStatement para operaciones seguras y eficientes.
 */
public class EntregaDAO {

    /**
     * Crea una nueva entrega en la base de datos
     *
     * @param entrega la entrega a crear
     * @return true si se creó exitosamente, false en caso contrario
     */
    public boolean create(Entrega entrega) {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, entrega.getIdPedido());
            pstmt.setInt(2, entrega.getIdRepartidor());
            pstmt.setDate(3, Date.valueOf(entrega.getFecha()));
            pstmt.setTime(4, Time.valueOf(entrega.getHora()));

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID generado automáticamente
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entrega.setId(rs.getInt(1));
                    }
                }
                System.out.println("Entrega creada exitosamente con ID: " + entrega.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al crear entrega: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lee todas las entregas de la base de datos con información de pedidos y repartidores
     *
     * @return lista de entregas
     */
    public List<Entrega> readAll() {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT e.*, p.direccion as pedido_direccion, r.nombre as repartidor_nombre " +
                     "FROM entregas e " +
                     "JOIN pedidos p ON e.id_pedido = p.id " +
                     "JOIN repartidores r ON e.id_repartidor = r.id " +
                     "ORDER BY e.id";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Entrega entrega = new Entrega(
                    rs.getInt("id"),
                    rs.getInt("id_pedido"),
                    rs.getInt("id_repartidor"),
                    rs.getDate("fecha").toLocalDate(),
                    rs.getTime("hora").toLocalTime()
                );
                entrega.setPedidoDireccion(rs.getString("pedido_direccion"));
                entrega.setRepartidorNombre(rs.getString("repartidor_nombre"));
                entregas.add(entrega);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer entregas: " + e.getMessage());
            e.printStackTrace();
        }

        return entregas;
    }

    /**
     * Busca una entrega por su ID
     *
     * @param id el ID de la entrega a buscar
     * @return la entrega encontrada o null si no existe
     */
    public Entrega readById(int id) {
        String sql = "SELECT e.*, p.direccion as pedido_direccion, r.nombre as repartidor_nombre " +
                     "FROM entregas e " +
                     "JOIN pedidos p ON e.id_pedido = p.id " +
                     "JOIN repartidores r ON e.id_repartidor = r.id " +
                     "WHERE e.id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                    );
                    entrega.setPedidoDireccion(rs.getString("pedido_direccion"));
                    entrega.setRepartidorNombre(rs.getString("repartidor_nombre"));
                    return entrega;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar entrega: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Filtra entregas por pedido
     *
     * @param pedidoId el ID del pedido
     * @return lista de entregas de ese pedido
     */
    public List<Entrega> readByPedido(int pedidoId) {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT e.*, p.direccion as pedido_direccion, r.nombre as repartidor_nombre " +
                     "FROM entregas e " +
                     "JOIN pedidos p ON e.id_pedido = p.id " +
                     "JOIN repartidores r ON e.id_repartidor = r.id " +
                     "WHERE e.id_pedido = ? " +
                     "ORDER BY e.id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pedidoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                    );
                    entrega.setPedidoDireccion(rs.getString("pedido_direccion"));
                    entrega.setRepartidorNombre(rs.getString("repartidor_nombre"));
                    entregas.add(entrega);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al filtrar entregas por pedido: " + e.getMessage());
            e.printStackTrace();
        }

        return entregas;
    }

    /**
     * Filtra entregas por repartidor
     *
     * @param repartidorId el ID del repartidor
     * @return lista de entregas de ese repartidor
     */
    public List<Entrega> readByRepartidor(int repartidorId) {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT e.*, p.direccion as pedido_direccion, r.nombre as repartidor_nombre " +
                     "FROM entregas e " +
                     "JOIN pedidos p ON e.id_pedido = p.id " +
                     "JOIN repartidores r ON e.id_repartidor = r.id " +
                     "WHERE e.id_repartidor = ? " +
                     "ORDER BY e.id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, repartidorId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                    );
                    entrega.setPedidoDireccion(rs.getString("pedido_direccion"));
                    entrega.setRepartidorNombre(rs.getString("repartidor_nombre"));
                    entregas.add(entrega);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al filtrar entregas por repartidor: " + e.getMessage());
            e.printStackTrace();
        }

        return entregas;
    }

    /**
     * Actualiza una entrega existente en la base de datos
     *
     * @param entrega la entrega con los datos actualizados
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    public boolean update(Entrega entrega) {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, entrega.getIdPedido());
            pstmt.setInt(2, entrega.getIdRepartidor());
            pstmt.setDate(3, Date.valueOf(entrega.getFecha()));
            pstmt.setTime(4, Time.valueOf(entrega.getHora()));
            pstmt.setInt(5, entrega.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Entrega actualizada exitosamente");
                return true;
            } else {
                System.out.println("No se encontró la entrega con ID: " + entrega.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar entrega: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina una entrega de la base de datos
     *
     * @param id el ID de la entrega a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM entregas WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Entrega eliminada exitosamente");
                return true;
            } else {
                System.out.println("No se encontró la entrega con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar entrega: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
