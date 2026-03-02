import vista.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal del sistema SpeedFast.
 * Inicia la aplicación con interfaz gráfica Swing.
 *
 * Sistema de Gestión de Entregas con CRUD completo
 */
public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo configurar el Look and Feel del sistema");
        }

        // Iniciar la aplicación en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            System.out.println("=".repeat(60));
            System.out.println("    SPEEDFAST - Sistema de Gestión de Entregas");
            System.out.println("    Programación Orientada a Objetos II - Semana 8");
            System.out.println("=".repeat(60));
            System.out.println();

            new VentanaPrincipal();
        });
    }
}

