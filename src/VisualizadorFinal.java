import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

/**
 * Interfaz gráfica básica de la aplicación Calculator Solar App.
 *
 * Permite visualizar la información generada por la aplicación
 * a partir de los archivos de texto ubicados en la carpeta data.
 *
 * @author Juana Valentina Sánchez
 * @version 1.0
 */
public class VisualizadorFinal extends JFrame {

    /**
     * Constructor de la ventana principal.
     */
    public VisualizadorFinal() {

        setTitle("Calculator Solar App");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("CLIENTES", createPanel("data/clients.txt"));
        tabs.addTab("PROPIEDADES", createPanel("data/properties.txt"));
        tabs.addTab("CONSUMOS", createPanel("data/consumptions.txt"));
        tabs.addTab("EMPRESAS", createPanel("data/electric_companies.txt"));
        tabs.addTab("ZONAS", createPanel("data/zones.txt"));
        tabs.addTab("PANELES", createPanel("data/solarPanels.txt"));
        tabs.addTab("INVERSORES", createPanel("data/inverters.txt"));

        add(tabs);
    }

    /**
     * Crea un panel para mostrar el contenido de un archivo.
     *
     * @param path ruta del archivo que se desea visualizar
     * @return panel con el contenido del archivo
     */
    private JScrollPane createPanel(String path) {

        JTextArea textArea = new JTextArea();

        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(false);

        try {

            File file = new File(path);

            /*
             * Permite buscar el archivo dentro de la carpeta
             * principal del proyecto.
             */
            if (!file.exists()) {
                file = new File("Calculator_solar_app/" + path);
            }

            if (file.exists()) {

                String content = new String(
                        Files.readAllBytes(file.toPath()),
                        "UTF-8"
                );

                textArea.setText(content);

            } else {

                textArea.setText(
                        "Archivo no encontrado.\n\n"
                        + "Ejecuta primero GenerateInfoFiles.java\n\n"
                        + "Archivo buscado: " + path
                );
            }

        } catch (Exception e) {

            textArea.setText(
                    "Se produjo un error al leer el archivo.\n\n"
                    + e.getMessage()
            );
        }

        return new JScrollPane(textArea);
    }

    /**
     * Método principal de la interfaz gráfica.
     *
     * @param args argumentos de ejecución
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                VisualizadorFinal window = new VisualizadorFinal();
                window.setVisible(true);
            }
        });
    }
}