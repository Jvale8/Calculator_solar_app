import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Interfaz para consultar los archivos .txt y los informes generados por SolarCalc.
 * No declara método main; la ventana se abre desde la clase main.
 *
 * @author jvale
 * @author Walter Ortiz
 * @author Cristian Tovar   
 * @version 1.1
 */
public class VisualizadorFinal extends JFrame {

    private static final long serialVersionUID = 1L;
    private final File projectRoot;
    private final JTabbedPane tabs;
    private final JLabel statusLabel;

    public VisualizadorFinal() {
        projectRoot = findProjectRoot();
        tabs = new JTabbedPane();
        statusLabel = new JLabel("Listo para consultar archivos.");

        setTitle("Calculator Solar App - Visualizador de archivos e informes");
        setSize(1100, 720);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        JLabel title = new JLabel("Archivos de entrada e informes generados");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton refresh = new JButton("Actualizar");
        refresh.addActionListener(event -> refreshTabs());
        header.add(title, BorderLayout.WEST);
        header.add(refresh, BorderLayout.EAST);

        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));
        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        refreshTabs();
    }

    /** Abre el visualizador automáticamente sin agregar un tercer método main. */
    public static void showWindow() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("No hay entorno gráfico. Consulta los archivos en las carpetas data y reports.");
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VisualizadorFinal().setVisible(true);
            }
        });
    }

    private void refreshTabs() {
        tabs.removeAll();
        addFileTab("CLIENTES", "data/clients.txt");
        addFileTab("PROPIEDADES", "data/properties.txt");
        addFileTab("CONSUMOS", "data/consumptions.txt");
        addFileTab("EMPRESAS", "data/electric_companies.txt");
        addFileTab("ZONAS", "data/zones.txt");
        addFileTab("PANELES", "data/solarPanels.txt");
        addFileTab("INVERSORES", "data/inverters.txt");
        addFileTab("RESUMEN PROYECTOS", "reports/resumen_proyectos.csv");
        addFileTab("RESUMEN PANELES", "reports/resumen_paneles.csv");
        addFileTab("RESUMEN INVERSORES", "reports/resumen_inversores.csv");
        tabs.addTab("INFORMES POR PROPIEDAD", createReportsPanel());
        statusLabel.setText("Carpeta del proyecto: " + projectRoot.getAbsolutePath());
        revalidate();
        repaint();
    }

    private void addFileTab(String title, String relativePath) {
        JTextArea area = createTextArea();
        loadFile(new File(projectRoot, relativePath), area);
        tabs.addTab(title, new JScrollPane(area));
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        DefaultListModel<File> model = new DefaultListModel<File>();
        File reportsDirectory = new File(projectRoot, "reports");
        File[] files = reportsDirectory.listFiles(file -> file.isFile()
                && file.getName().endsWith("_informe.txt"));
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));
            for (File file : files) {
                model.addElement(file);
            }
        }

        JList<File> list = new JList<File>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(new Font("Monospaced", Font.PLAIN, 13));
        list.setCellRenderer((listComponent, value, index, selected, focus) -> {
            JLabel label = new JLabel(value.getName());
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(5, 6, 5, 6));
            if (selected) {
                label.setBackground(listComponent.getSelectionBackground());
                label.setForeground(listComponent.getSelectionForeground());
            } else {
                label.setBackground(listComponent.getBackground());
                label.setForeground(listComponent.getForeground());
            }
            return label;
        });

        JTextArea reportArea = createTextArea();
        reportArea.setText("Selecciona un informe de la lista.");
        list.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && list.getSelectedValue() != null) {
                File selectedFile = list.getSelectedValue();
                loadFile(selectedFile, reportArea);
                statusLabel.setText("Informe abierto: " + selectedFile.getName());
            }
        });

        JScrollPane listPane = new JScrollPane(list);
        listPane.setPreferredSize(new Dimension(240, 400));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listPane,
                new JScrollPane(reportArea));
        split.setResizeWeight(0.25);
        panel.add(split, BorderLayout.CENTER);

        if (model.isEmpty()) {
            reportArea.setText("Todavía no se encontraron informes individuales.\n\n"
                    + "Ejecuta GenerateInfoFiles y después main para generarlos.");
        } else {
            list.setSelectedIndex(0);
        }
        return panel;
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setLineWrap(false);
        area.setTabSize(4);
        return area;
    }

    private void loadFile(File file, JTextArea area) {
        try {
            if (!file.isFile()) {
                area.setText("No se encontró el archivo:\n" + file.getAbsolutePath()
                        + "\n\nEjecuta GenerateInfoFiles y después main.");
                return;
            }
            area.setText(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));
            area.setCaretPosition(0);
        } catch (IOException e) {
            area.setText("No fue posible leer el archivo:\n" + file.getAbsolutePath()
                    + "\n\nDetalle: " + e.getMessage());
        }
    }

    private static File findProjectRoot() {
        try {
            File current = new File(".").getCanonicalFile();
            if (new File(current, "data").isDirectory() && new File(current, "src").isDirectory()) {
                return current;
            }
            File nested = new File(current, "Calculator_solar_app").getCanonicalFile();
            if (new File(nested, "data").isDirectory() && new File(nested, "src").isDirectory()) {
                return nested;
            }
            throw new IOException("No se encontró la raíz del proyecto.");
        } catch (IOException e) {
            return new File(".").getAbsoluteFile();
        }
    }
}
