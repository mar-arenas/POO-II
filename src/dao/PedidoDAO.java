package dao;

import conexion.ConexionDB;
import modelo.EstadoPedido;
import modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones CRUD de Pedidos en la base de datos.
 * Utiliza PreparedStatement para operaciones seguras y eficientes.
 */
public class PedidoDAO {

    /**
     * Crea un nuevo pedido en la base de datos
     *
     * @param pedido el pedido a crear
     * @return true si se creó exitosamente, false en caso contrario
     */
    public boolean create(Pedido pedido) {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pedido.getDireccionEntrega());
            pstmt.setString(2, pedido.getTipo());
            pstmt.setString(3, pedido.getEstado().name());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener el ID generado automáticamente
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    }
                }
                System.out.println("Pedido creado exitosamente con ID: " + pedido.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al crear pedido: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lee todos los pedidos de la base de datos
     *
     * @return lista de pedidos
     */
    public List<Pedido> readAll() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY id";

        try (Connection conn = ConexionDB.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id"),
                    rs.getString("direccion"),
                    rs.getString("tipo"),
                    EstadoPedido.valueOf(rs.getString("estado"))
                );
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer pedidos: " + e.getMessage());
            e.printStackTrace();
        }

        return pedidos;
    }

    /**
     * Busca un pedido por su ID
     *
     * @param id el ID del pedido a buscar
     * @return el pedido encontrado o null si no existe
     */
    public Pedido readById(int id) {
        String sql = "SELECT * FROM pedidos WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        EstadoPedido.valueOf(rs.getString("estado"))
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pedido: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Filtra pedidos por estado
     *
     * @param estado el estado a filtrar
     * @return lista de pedidos con ese estado
     */
    public List<Pedido> readByEstado(EstadoPedido estado) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE estado = ? ORDER BY id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estado.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        EstadoPedido.valueOf(rs.getString("estado"))
                    );
                    pedidos.add(pedido);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al filtrar pedidos por estado: " + e.getMessage());
            e.printStackTrace();
        }

        return pedidos;
    }

    /**
     * Filtra pedidos por tipo
     *
     * @param tipo el tipo a filtrar (COMIDA, ENCOMIENDA, EXPRESS)
     * @return lista de pedidos con ese tipo
     */
    public List<Pedido> readByTipo(String tipo) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE tipo = ? ORDER BY id";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tipo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo"),
                        EstadoPedido.valueOf(rs.getString("estado"))
                    );
                    pedidos.add(pedido);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al filtrar pedidos por tipo: " + e.getMessage());
            e.printStackTrace();
        }

        return pedidos;
    }

    /**
     * Actualiza un pedido existente en la base de datos
     *
     * @param pedido el pedido con los datos actualizados
     * @return true si se actualizó exitosamente, false en caso contrario
     */
    public boolean update(Pedido pedido) {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getDireccionEntrega());
            pstmt.setString(2, pedido.getTipo());
            pstmt.setString(3, pedido.getEstado().name());
            pstmt.setInt(4, pedido.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Pedido actualizado exitosamente");
                return true;
            } else {
                System.out.println("No se encontró el pedido con ID: " + pedido.getId());
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un pedido de la base de datos
     *
     * @param id el ID del pedido a eliminar
     * @return true si se eliminó exitosamente, false en caso contrario
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Pedido eliminado exitosamente");
                return true;
            } else {
                System.out.println("No se encontró el pedido con ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            System.err.println("Nota: Puede que el pedido tenga entregas asociadas");
            e.printStackTrace();
        }

        return false;
    }
}
