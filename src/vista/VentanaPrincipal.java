package vista;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import conexion.ConexionDB;

/**
 * Ventana principal del sistema SpeedFast.
 * Contiene pestañas para gestionar Repartidores, Pedidos y Entregas.
 */
public class VentanaPrincipal extends JFrame {

    private JTabbedPane tabbedPane;
    private PanelRepartidores panelRepartidores;
    private PanelPedidos panelPedidos;
    private PanelEntregas panelEntregas;
    private JLabel lblEstadoConexion;

    public VentanaPrincipal() {
        inicializarVentana();
        inicializarComponentes();
        verificarConexion();
        setVisible(true);
    }

    private void inicializarVentana() {
        setTitle("SpeedFast - Sistema de Gestión de Entregas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Icono de la ventana (si existe)
        try {
            // setIconImage(new ImageIcon("resources/icon.png").getImage());
        } catch (Exception e) {
            // Icono no encontrado, continuar sin él
        }
    }

    private void inicializarComponentes() {
        // Panel superior - Título y estado
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(41, 128, 185));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("SpeedFast - Sistema de Gestión de Entregas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        lblEstadoConexion = new JLabel("● Conectado");
        lblEstadoConexion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEstadoConexion.setForeground(Color.WHITE);
        panelSuperior.add(lblEstadoConexion, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central - Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Crear los paneles
        panelRepartidores = new PanelRepartidores();
        panelPedidos = new PanelPedidos();
        panelEntregas = new PanelEntregas();

        // Agregar pestañas
        tabbedPane.addTab("Repartidores", panelRepartidores);
        tabbedPane.addTab("Pedidos", panelPedidos);
        tabbedPane.addTab("Entregas", panelEntregas);

        // Listener para refrescar datos al cambiar de pestaña
        tabbedPane.addChangeListener(e -> {
            int indice = tabbedPane.getSelectedIndex();
            switch (indice) {
                case 0:
                    panelRepartidores.refrescar();
                    break;
                case 1:
                    panelPedidos.refrescar();
                    break;
                case 2:
                    panelEntregas.refrescar();
                    break;
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior - Barra de estado
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblInfo = new JLabel("Sistema de Gestión de Entregas SpeedFast");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        lblInfo.setForeground(Color.GRAY);
        panelInferior.add(lblInfo);

        add(panelInferior, BorderLayout.SOUTH);

        // Menú
        crearMenuBar();
    }

    private void crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem itemActualizar = new JMenuItem("Actualizar Todo");
        itemActualizar.setAccelerator(KeyStroke.getKeyStroke("F5"));
        itemActualizar.addActionListener(e -> actualizarTodo());

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setAccelerator(KeyStroke.getKeyStroke("alt X"));
        itemSalir.addActionListener(e -> salir());

        menuArchivo.add(itemActualizar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        // Menú Ver
        JMenu menuVer = new JMenu("Ver");

        JMenuItem itemRepartidores = new JMenuItem("Repartidores");
        itemRepartidores.addActionListener(e -> tabbedPane.setSelectedIndex(0));

        JMenuItem itemPedidos = new JMenuItem("Pedidos");
        itemPedidos.addActionListener(e -> tabbedPane.setSelectedIndex(1));

        JMenuItem itemEntregas = new JMenuItem("Entregas");
        itemEntregas.addActionListener(e -> tabbedPane.setSelectedIndex(2));

        menuVer.add(itemRepartidores);
        menuVer.add(itemPedidos);
        menuVer.add(itemEntregas);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");

        JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
        itemAcercaDe.addActionListener(e -> mostrarAcercaDe());

        JMenuItem itemConexion = new JMenuItem("Verificar Conexión");
        itemConexion.addActionListener(e -> verificarConexion());

        menuAyuda.add(itemAcercaDe);
        menuAyuda.add(itemConexion);

        menuBar.add(menuArchivo);
        menuBar.add(menuVer);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);
    }

    private void verificarConexion() {
        try {
            ConexionDB.getConexion();
            lblEstadoConexion.setText("Conectado");
            lblEstadoConexion.setForeground(new Color(46, 204, 113));
        } catch (SQLException e) {
            lblEstadoConexion.setText("Desconectado");
            lblEstadoConexion.setForeground(new Color(231, 76, 60));

            JOptionPane.showMessageDialog(this,
                "No se pudo conectar a la base de datos.\n\n" +
                "Verifique:\n" +
                "1. MySQL está ejecutándose\n" +
                "2. La base de datos 'speedfast_db' existe\n" +
                "3. Usuario y contraseña en ConexionDB.java son correctos\n\n" +
                "Error: " + e.getMessage(),
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTodo() {
        panelRepartidores.refrescar();
        panelPedidos.refrescar();
        panelEntregas.refrescar();
        JOptionPane.showMessageDialog(this,
            "Datos actualizados correctamente",
            "Actualización",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAcercaDe() {
        String mensaje = """
                SpeedFast - Sistema de Gestión de Entregas

                Versión: 1.0

                Desarrollado para:
                Desarrollo Orientado a Objetos II

                Características:
                • Gestión completa de Repartidores
                • Gestión completa de Pedidos
                • Gestión completa de Entregas
                • Persistencia con MySQL
                • Interfaz gráfica con Swing

                """;

        JOptionPane.showMessageDialog(this,
            mensaje,
            "Acerca de SpeedFast",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de salir del sistema?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            ConexionDB.cerrarConexion();
            System.exit(0);
        }
    }
}
