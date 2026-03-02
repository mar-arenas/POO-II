package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel para gestionar entregas (CRUD completo)
 */
public class PanelEntregas extends JPanel {

    private EntregaDAO entregaDAO;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    // Componentes de la interfaz
    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;
    private JComboBox<ItemCombo> cboPedido, cboRepartidor;
    private JTextField txtFecha, txtHora;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar, btnLimpiar;
    private JButton btnFechaActual;

    // Variable para saber si estamos editando
    private int idSeleccionado = -1;

    public PanelEntregas() {
        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();
        inicializarComponentes();
        cargarCombos();
        cargarEntregas();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Entrega"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pedido
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Pedido:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        cboPedido = new JComboBox<>();
        panelFormulario.add(cboPedido, gbc);

        // Repartidor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Repartidor:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        cboRepartidor = new JComboBox<>();
        panelFormulario.add(cboRepartidor, gbc);

        // Fecha
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Fecha (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        txtFecha = new JTextField(15);
        txtFecha.setToolTipText("Formato: dd/MM/yyyy");
        panelFormulario.add(txtFecha, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        btnFechaActual = new JButton("Hoy");
        panelFormulario.add(btnFechaActual, gbc);

        // Hora
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Hora (HH:mm):"), gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        txtHora = new JTextField(15);
        txtHora.setToolTipText("Formato: HH:mm (ejemplo: 14:30)");
        panelFormulario.add(txtHora, gbc);

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

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // Panel central - Tabla
        String[] columnas = {"ID", "Pedido", "Dirección", "Repartidor", "Fecha", "Hora"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEntregas = new JTable(modeloTabla);
        tablaEntregas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEntregas.getTableHeader().setReorderingAllowed(false);

        // Ajustar ancho de columnas
        tablaEntregas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEntregas.getColumnModel().getColumn(1).setPreferredWidth(70);
        tablaEntregas.getColumnModel().getColumn(2).setPreferredWidth(250);
        tablaEntregas.getColumnModel().getColumn(3).setPreferredWidth(150);
        tablaEntregas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaEntregas.getColumnModel().getColumn(5).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tablaEntregas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Entregas"));
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        configurarEventos();
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(e -> agregarEntrega());
        btnEditar.addActionListener(e -> editarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRefrescar.addActionListener(e -> {
            cargarCombos();
            cargarEntregas();
        });

        btnFechaActual.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            LocalTime ahora = LocalTime.now();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            txtFecha.setText(hoy.format(formatoFecha));
            txtHora.setText(ahora.format(formatoHora));
        });

        tablaEntregas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void agregarEntrega() {
        if (!validarFormulario()) {
            return;
        }

        ItemCombo pedidoSelec = (ItemCombo) cboPedido.getSelectedItem();
        ItemCombo repartidorSelec = (ItemCombo) cboRepartidor.getSelectedItem();

        LocalDate fecha = parsearFecha();
        LocalTime hora = parsearHora();

        if (fecha == null || hora == null) {
            return;
        }

        Entrega entrega = new Entrega(
            pedidoSelec.getId(),
            repartidorSelec.getId(),
            fecha,
            hora
        );

        if (entregaDAO.create(entrega)) {
            JOptionPane.showMessageDialog(this,
                "Entrega registrada exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarEntregas();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al registrar la entrega",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarEntrega() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione una entrega de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        ItemCombo pedidoSelec = (ItemCombo) cboPedido.getSelectedItem();
        ItemCombo repartidorSelec = (ItemCombo) cboRepartidor.getSelectedItem();

        LocalDate fecha = parsearFecha();
        LocalTime hora = parsearHora();

        if (fecha == null || hora == null) {
            return;
        }

        Entrega entrega = new Entrega(
            idSeleccionado,
            pedidoSelec.getId(),
            repartidorSelec.getId(),
            fecha,
            hora
        );

        if (entregaDAO.update(entrega)) {
            JOptionPane.showMessageDialog(this,
                "Entrega actualizada exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarEntregas();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar la entrega",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEntrega() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione una entrega de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta entrega?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (entregaDAO.delete(idSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "Entrega eliminada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarEntregas();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar la entrega",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarCombos() {
        // Cargar pedidos
        cboPedido.removeAllItems();
        List<Pedido> pedidos = pedidoDAO.readAll();
        for (Pedido p : pedidos) {
            cboPedido.addItem(new ItemCombo(p.getId(),
                "Pedido #" + p.getId() + " - " + p.getDireccionEntrega()));
        }

        // Cargar repartidores
        cboRepartidor.removeAllItems();
        List<Repartidor> repartidores = repartidorDAO.readAll();
        for (Repartidor r : repartidores) {
            cboRepartidor.addItem(new ItemCombo(r.getId(),
                r.getId() + " - " + r.getNombre()));
        }
    }

    private void cargarEntregas() {
        modeloTabla.setRowCount(0);
        List<Entrega> entregas = entregaDAO.readAll();

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        for (Entrega e : entregas) {
            Object[] fila = {
                e.getId(),
                e.getIdPedido(),
                e.getPedidoDireccion(),
                e.getRepartidorNombre(),
                e.getFecha().format(formatoFecha),
                e.getHora().format(formatoHora)
            };
            modeloTabla.addRow(fila);
        }
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tablaEntregas.getSelectedRow();

        if (filaSeleccionada != -1) {
            idSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);

            // Cargar datos desde la base de datos
            Entrega entrega = entregaDAO.readById(idSeleccionado);
            if (entrega != null) {
                // Seleccionar pedido
                for (int i = 0; i < cboPedido.getItemCount(); i++) {
                    if (((ItemCombo) cboPedido.getItemAt(i)).getId() == entrega.getIdPedido()) {
                        cboPedido.setSelectedIndex(i);
                        break;
                    }
                }

                // Seleccionar repartidor
                for (int i = 0; i < cboRepartidor.getItemCount(); i++) {
                    if (((ItemCombo) cboRepartidor.getItemAt(i)).getId() == entrega.getIdRepartidor()) {
                        cboRepartidor.setSelectedIndex(i);
                        break;
                    }
                }

                // Cargar fecha y hora
                DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
                txtFecha.setText(entrega.getFecha().format(formatoFecha));
                txtHora.setText(entrega.getHora().format(formatoHora));
            }
        }
    }

    private void limpiarFormulario() {
        if (cboPedido.getItemCount() > 0) cboPedido.setSelectedIndex(0);
        if (cboRepartidor.getItemCount() > 0) cboRepartidor.setSelectedIndex(0);
        txtFecha.setText("");
        txtHora.setText("");
        idSeleccionado = -1;
        tablaEntregas.clearSelection();
    }

    private boolean validarFormulario() {
        if (cboPedido.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay pedidos disponibles. Cree un pedido primero.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cboRepartidor.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay repartidores disponibles. Cree un repartidor primero.",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (txtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La fecha es obligatoria",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtFecha.requestFocus();
            return false;
        }

        if (txtHora.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La hora es obligatoria",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtHora.requestFocus();
            return false;
        }

        return true;
    }

    private LocalDate parsearFecha() {
        try {
            String fecha = txtFecha.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(fecha, formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Formato de fecha inválido.\nUse: dd/MM/yyyy",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private LocalTime parsearHora() {
        try {
            String hora = txtHora.getText().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(hora, formatter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Formato de hora inválido.\nUse: HH:mm (ejemplo: 14:30)",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void refrescar() {
        cargarCombos();
        cargarEntregas();
    }

    /**
     * Clase interna para manejar items en los ComboBox
     */
    private static class ItemCombo {
        private int id;
        private String descripcion;

        public ItemCombo(int id, String descripcion) {
            this.id = id;
            this.descripcion = descripcion;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }
}
