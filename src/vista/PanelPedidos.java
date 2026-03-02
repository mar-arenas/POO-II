package vista;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para gestionar pedidos (CRUD completo)
 */
public class PanelPedidos extends JPanel {

    private PedidoDAO pedidoDAO;

    // Componentes de la interfaz
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private JTextField txtDireccion;
    private JComboBox<String> cboTipo, cboEstado;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar, btnLimpiar;
    private JComboBox<String> cboFiltroEstado, cboFiltroTipo;
    private JButton btnFiltrar, btnQuitarFiltro;

    // Variable para saber si estamos editando
    private int idSeleccionado = -1;

    public PanelPedidos() {
        pedidoDAO = new PedidoDAO();
        inicializarComponentes();
        cargarPedidos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior completo
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dirección
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Dirección:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtDireccion = new JTextField(30);
        panelFormulario.add(txtDireccion, gbc);

        // Tipo
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        String[] tipos = {"COMIDA", "ENCOMIENDA", "EXPRESS"};
        cboTipo = new JComboBox<>(tipos);
        panelFormulario.add(cboTipo, gbc);

        // Estado
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        String[] estados = {"PENDIENTE", "EN_REPARTO", "ENTREGADO"};
        cboEstado = new JComboBox<>(estados);
        panelFormulario.add(cboEstado, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnRefrescar = new JButton("Refrescar");

        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.BLACK);
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.BLACK);
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.BLACK);

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        panelFiltros.add(new JLabel("Estado:"));
        cboFiltroEstado = new JComboBox<>(new String[]{"Todos", "PENDIENTE", "EN_REPARTO", "ENTREGADO"});
        panelFiltros.add(cboFiltroEstado);

        panelFiltros.add(new JLabel("Tipo:"));
        cboFiltroTipo = new JComboBox<>(new String[]{"Todos", "COMIDA", "ENCOMIENDA", "EXPRESS"});
        panelFiltros.add(cboFiltroTipo);

        btnFiltrar = new JButton("Aplicar Filtro");
        btnQuitarFiltro = new JButton("Quitar Filtro");
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnQuitarFiltro);

        panelSuperior.add(panelFiltros, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central - Tabla
        String[] columnas = {"ID", "Dirección", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable directamente
            }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPedidos.getTableHeader().setReorderingAllowed(false);

        // Ajustar ancho de columnas
        tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Pedidos"));
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        configurarEventos();
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(e -> agregarPedido());
        btnEditar.addActionListener(e -> editarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRefrescar.addActionListener(e -> cargarPedidos());
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnQuitarFiltro.addActionListener(e -> cargarPedidos());

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void agregarPedido() {
        if (!validarFormulario()) {
            return;
        }

        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cboTipo.getSelectedItem();
        EstadoPedido estado = EstadoPedido.valueOf((String) cboEstado.getSelectedItem());

        Pedido pedido = new Pedido(direccion, tipo);
        pedido.setEstado(estado);

        if (pedidoDAO.create(pedido)) {
            JOptionPane.showMessageDialog(this,
                "Pedido agregado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarPedidos();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al agregar el pedido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPedido() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un pedido de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cboTipo.getSelectedItem();
        EstadoPedido estado = EstadoPedido.valueOf((String) cboEstado.getSelectedItem());

        Pedido pedido = new Pedido(idSeleccionado, direccion, tipo, estado);

        if (pedidoDAO.update(pedido)) {
            JOptionPane.showMessageDialog(this,
                "Pedido actualizado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarPedidos();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar el pedido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPedido() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un pedido de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este pedido?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (pedidoDAO.delete(idSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "Pedido eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarPedidos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar el pedido.\nPuede tener entregas asociadas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.readAll();

        for (Pedido p : pedidos) {
            Object[] fila = {
                p.getId(),
                p.getDireccionEntrega(),
                p.getTipo(),
                p.getEstado().name()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void aplicarFiltros() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos;

        String filtroEstado = (String) cboFiltroEstado.getSelectedItem();
        String filtroTipo = (String) cboFiltroTipo.getSelectedItem();

        // Aplicar filtros según lo seleccionado
        if (!filtroEstado.equals("Todos")) {
            pedidos = pedidoDAO.readByEstado(EstadoPedido.valueOf(filtroEstado));
        } else if (!filtroTipo.equals("Todos")) {
            pedidos = pedidoDAO.readByTipo(filtroTipo);
        } else {
            pedidos = pedidoDAO.readAll();
        }

        for (Pedido p : pedidos) {
            Object[] fila = {
                p.getId(),
                p.getDireccionEntrega(),
                p.getTipo(),
                p.getEstado().name()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tablaPedidos.getSelectedRow();

        if (filaSeleccionada != -1) {
            idSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String direccion = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String tipo = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
            String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 3);

            txtDireccion.setText(direccion);
            cboTipo.setSelectedItem(tipo);
            cboEstado.setSelectedItem(estado);
        }
    }

    private void limpiarFormulario() {
        txtDireccion.setText("");
        cboTipo.setSelectedIndex(0);
        cboEstado.setSelectedIndex(0);
        idSeleccionado = -1;
        tablaPedidos.clearSelection();
    }

    private boolean validarFormulario() {
        String direccion = txtDireccion.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La dirección es obligatoria",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtDireccion.requestFocus();
            return false;
        }

        if (direccion.length() < 5) {
            JOptionPane.showMessageDialog(this,
                "La dirección debe tener al menos 5 caracteres",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtDireccion.requestFocus();
            return false;
        }

        return true;
    }

    public void refrescar() {
        cargarPedidos();
    }
}
