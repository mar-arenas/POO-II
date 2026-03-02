package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para gestionar repartidores (CRUD completo)
 */
public class PanelRepartidores extends JPanel {

    private RepartidorDAO repartidorDAO;

    // Componentes de la interfaz
    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre;
    private JButton btnAgregar, btnEditar, btnEliminar, btnRefrescar, btnLimpiar;

    // Variable para saber si estamos editando
    private int idSeleccionado = -1;

    public PanelRepartidores() {
        repartidorDAO = new RepartidorDAO();
        inicializarComponentes();
        cargarRepartidores();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Repartidor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre, gbc);

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

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // Panel central - Tabla
        String[] columnas = {"ID", "Nombre"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable directamente
            }
        };

        tablaRepartidores = new JTable(modeloTabla);
        tablaRepartidores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRepartidores.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaRepartidores);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Repartidores"));
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        configurarEventos();
    }

    private void configurarEventos() {
        // Agregar
        btnAgregar.addActionListener(e -> agregarRepartidor());

        // Editar
        btnEditar.addActionListener(e -> editarRepartidor());

        // Eliminar
        btnEliminar.addActionListener(e -> eliminarRepartidor());

        // Limpiar
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Refrescar
        btnRefrescar.addActionListener(e -> cargarRepartidores());

        // Selección en tabla
        tablaRepartidores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void agregarRepartidor() {
        // Validaciones
        if (!validarFormulario()) {
            return;
        }

        String nombre = txtNombre.getText().trim();
        Repartidor repartidor = new Repartidor(nombre);

        if (repartidorDAO.create(repartidor)) {
            JOptionPane.showMessageDialog(this,
                "Repartidor agregado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarRepartidores();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al agregar el repartidor",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarRepartidor() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un repartidor de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        String nombre = txtNombre.getText().trim();
        Repartidor repartidor = new Repartidor(idSeleccionado, nombre);

        if (repartidorDAO.update(repartidor)) {
            JOptionPane.showMessageDialog(this,
                "Repartidor actualizado exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarRepartidores();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al actualizar el repartidor",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRepartidor() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un repartidor de la tabla",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este repartidor?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (repartidorDAO.delete(idSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "Repartidor eliminado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarRepartidores();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar el repartidor.\nPuede tener entregas asociadas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarRepartidores() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Repartidor> repartidores = repartidorDAO.readAll();

        for (Repartidor r : repartidores) {
            Object[] fila = {r.getId(), r.getNombre()};
            modeloTabla.addRow(fila);
        }
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tablaRepartidores.getSelectedRow();

        if (filaSeleccionada != -1) {
            idSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

            txtNombre.setText(nombre);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        idSeleccionado = -1;
        tablaRepartidores.clearSelection();
    }

    private boolean validarFormulario() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre es obligatorio",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }

        if (nombre.length() < 3) {
            JOptionPane.showMessageDialog(this,
                "El nombre debe tener al menos 3 caracteres",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Método público para refrescar la lista (usado desde otros paneles)
     */
    public void refrescar() {
        cargarRepartidores();
    }
}
