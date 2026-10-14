import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Procesa los archivos de entrada de Calculator Solar App y genera un informe
 * de dimensionamiento preliminar para cada propiedad que tenga consumo.
 * También genera archivos CSV consolidados y abre el visualizador al finalizar.
 *
 * @author jvale
 * @author Walter Ortiz
 * @version 1.0
 */
public class main {

    private static final double COVERAGE_FACTOR = 0.80;
    private static final double PERFORMANCE_FACTOR = 0.80;
    private static final int DAYS_PER_MONTH = 30;
    private static final double CONTRIBUTION_FACTOR = 1.20;
    private static final double MAX_DC_AC_RATIO = 1.35;
    private static final Locale COLOMBIAN_LOCALE = Locale.forLanguageTag("es-CO");

    public static void main(String[] args) {
        try {
            File root = getProjectRoot();
            File dataDirectory = new File(root, "data");
            File reportsDirectory = new File(root, "reports");

            if (!dataDirectory.isDirectory()) {
                throw new IOException("No se encontró la carpeta data: " + dataDirectory.getAbsolutePath());
            }
            if (!reportsDirectory.exists() && !reportsDirectory.mkdirs()) {
                throw new IOException("No fue posible crear la carpeta reports.");
            }

            Catalog catalog = loadCatalog(dataDirectory);
            ArrayList<ProjectReport> reports = calculateReports(catalog);
            if (reports.isEmpty()) {
                throw new IOException("No hay consumos relacionados con propiedades para generar informes.");
            }

            writeIndividualReports(reports, reportsDirectory);
            writeProjectSummary(reports, reportsDirectory);
            writePanelSummary(reports, reportsDirectory);
            writeInverterSummary(reports, reportsDirectory);

            System.out.println("Procesamiento finalizado correctamente.");
            System.out.println("Informes individuales generados: " + reports.size());
            System.out.println("Archivos disponibles en: " + reportsDirectory.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error al generar los informes de SolarCalc: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // La interfaz se abre automáticamente al finalizar main.
            VisualizadorFinal.showWindow();
        }
    }

    private static Catalog loadCatalog(File dataDirectory) throws IOException {
        Catalog catalog = new Catalog();
        loadZones(new File(dataDirectory, "zones.txt"), catalog);
        loadClients(new File(dataDirectory, "clients.txt"), catalog);
        loadCompanies(new File(dataDirectory, "electric_companies.txt"), catalog);
        loadProperties(new File(dataDirectory, "properties.txt"), catalog);
        loadPanels(new File(dataDirectory, "solarPanels.txt"), catalog);
        loadInverters(new File(dataDirectory, "inverters.txt"), catalog);
        loadConsumptions(new File(dataDirectory, "consumptions.txt"), catalog);
        return catalog;
    }

    private static void loadZones(File file, Catalog catalog) throws IOException {
        for (String[] row : readRecords(file, 4)) {
            Zone zone = new Zone(row[1], row[2], parseDouble(row[3], file, "horas de sol pico"));
            checkId(row[0], zone.getZoneId(), file);
            catalog.zones.put(zone.getZoneId(), zone);
        }
    }

    private static void loadClients(File file, Catalog catalog) throws IOException {
        for (String[] row : readRecords(file, 5)) {
            Client client = new Client(row[1], row[2], row[3], parseInt(row[4], file, "número de identificación"));
            checkId(row[0], client.getClientId(), file);
            catalog.clients.put(client.getClientId(), client);
        }
    }

    private static void loadCompanies(File file, Catalog catalog) throws IOException {
        for (String[] row : readRecords(file, 9)) {
            ElectricCompany company = new ElectricCompany(row[1], row[2],
                    parseDouble(row[3], file, "tarifa estrato 1"),
                    parseDouble(row[4], file, "tarifa estrato 2"),
                    parseDouble(row[5], file, "tarifa estrato 3"),
                    parseDouble(row[6], file, "tarifa estrato 4"),
                    parseDouble(row[7], file, "tarifa estrato 5"),
                    parseDouble(row[8], file, "tarifa estrato 6"));
            checkId(row[0], company.getCompanyId(), file);
            catalog.companies.put(company.getCompanyId(), company);
        }
    }

    private static void loadProperties(File file, Catalog catalog) throws IOException {
        for (String[] row : readRecords(file, 6)) {
            if (!catalog.clients.containsKey(row[1])) {
                throw new IOException("La propiedad " + row[0] + " tiene un cliente inexistente: " + row[1]);
            }
            if (!catalog.zones.containsKey(row[3])) {
                throw new IOException("La propiedad " + row[0] + " tiene una zona inexistente: " + row[3]);
            }
            if (!catalog.companies.containsKey(row[4])) {
                throw new IOException("La propiedad " + row[0] + " tiene una empresa inexistente: " + row[4]);
            }
            int stratum = parseInt(row[5], file, "estrato");
            if (stratum < 1 || stratum > 6) {
                throw new IOException("Estrato fuera de rango en propiedad " + row[0]);
            }
            Property property = new Property(row[1], row[2], row[3], row[4], stratum);
            checkId(row[0], property.getPropertyId(), file);
            catalog.properties.put(property.getPropertyId(), property);
        }
    }

    private static void loadPanels(File file, Catalog catalog) throws IOException {
        for (String[] row : readRecords(file, 7)) {
            SolarPanel panel = new SolarPanel(row[1], row[2],
                    parseInt(row[3], file, "potencia del panel"),
                    parseDouble(row[4], file, "tensión DC del panel"),
                    parseDouble(row[5], file, "corriente DC del panel"),
                    parseInt(row[6], file, "garantía del panel"));
            checkId(row[0], panel.getPanelId(), file);
            if (panel.getPower() <= 0 || panel.getDcVoltage() <= 0 || panel.getDcCurrent() <= 0) {
                throw new IOException("Especificaciones inválidas para el panel " + row[0]);
            }
            catalog.panels.add(panel);
        }
    }

    /**
     * Lee el formato producido por las clases originales:
     * ID;Marca;Modelo;TipoSistema;Fases;Potencia;TensiónAC;TensiónDCMax;CorrienteAC;CorrienteDCMax;Monitoreo
     */
    private static void loadInverters(File file, Catalog catalog) throws IOException {
        for (String[] row : readRecords(file, 11)) {
            Inverter inverter = new Inverter(row[1], row[2], row[3], row[4],
                    parseDouble(row[5], file, "potencia del inversor"),
                    parseDouble(row[6], file, "tensión AC de operación"),
                    parseDouble(row[7], file, "tensión DC máxima"),
                    parseDouble(row[8], file, "corriente AC de entrega"),
                    parseDouble(row[9], file, "corriente DC máxima"),
                    parseBoolean(row[10], file, "monitoreo integrado"));
            checkId(row[0], inverter.getInverterId(), file);
            if (inverter.getPower() <= 0) {
                throw new IOException("La potencia del inversor " + row[0] + " debe ser positiva.");
            }
            catalog.inverters.add(inverter);
        }
    }

    /**
     * Admite el formato original IDPropiedad;kWhMes y también el formato extendido
     * IDCliente;IDPropiedad;IDEmpresa;kWhMes;Contribución;ValorPagar.
     */
    private static void loadConsumptions(File file, Catalog catalog) throws IOException {
        List<String[]> rows = readRecordsVariable(file);
        for (String[] row : rows) {
            String propertyId;
            double monthlyKWh;
            if (row.length == 2) {
                propertyId = row[0];
                monthlyKWh = parseDouble(row[1], file, "consumo mensual");
            } else if (row.length == 6) {
                propertyId = row[1];
                monthlyKWh = parseDouble(row[3], file, "consumo mensual");
            } else {
                throw new IOException("Formato no reconocido en " + file.getName()
                        + ". Se esperaban 2 o 6 campos por registro.");
            }

            Property property = catalog.properties.get(propertyId);
            if (property == null) {
                throw new IOException("El consumo hace referencia a una propiedad inexistente: " + propertyId);
            }
            ElectricCompany company = catalog.companies.get(property.getElectricCompanyId());
            if (company == null) {
                throw new IOException("No se encontró la empresa " + property.getElectricCompanyId()
                        + " relacionada con " + propertyId);
            }
            if (monthlyKWh < 0) {
                throw new IOException("El consumo de la propiedad " + propertyId + " no puede ser negativo.");
            }
            Consumption consumption = new Consumption(property, monthlyKWh, company);
            catalog.consumptions.put(propertyId, consumption);
        }
    }

    private static ArrayList<ProjectReport> calculateReports(Catalog catalog) throws IOException {
        if (catalog.zones.isEmpty() || catalog.clients.isEmpty() || catalog.companies.isEmpty()
                || catalog.properties.isEmpty() || catalog.panels.isEmpty() || catalog.inverters.isEmpty()) {
            throw new IOException("Uno o más archivos de entrada están vacíos.");
        }

        SolarPanel selectedPanel = selectPanel(catalog.panels);
        ArrayList<ProjectReport> reports = new ArrayList<ProjectReport>();

        for (Property property : catalog.properties.values()) {
            Consumption consumption = catalog.consumptions.get(property.getPropertyId());
            // Solo se genera informe cuando la propiedad tiene un consumo registrado.
            if (consumption == null) {
                continue;
            }
            Client client = catalog.clients.get(property.getClientId());
            Zone zone = catalog.zones.get(property.getZoneId());
            ElectricCompany company = catalog.companies.get(property.getElectricCompanyId());
            if (client == null || zone == null || company == null) {
                throw new IOException("Faltan datos relacionados para la propiedad " + property.getPropertyId());
            }

            double perPanelGeneration = selectedPanel.getPower() / 1000.0
                    * zone.getPeakSunHours() * DAYS_PER_MONTH * PERFORMANCE_FACTOR;
            if (perPanelGeneration <= 0) {
                throw new IOException("Generación mensual inválida para la zona " + zone.getZoneId());
            }
            double targetEnergy = consumption.getKWhMonth() * COVERAGE_FACTOR;
            int panelCount = Math.max(1, (int) Math.ceil(targetEnergy / perPanelGeneration));
            double installedPowerKW = panelCount * selectedPanel.getPower() / 1000.0;
            double estimatedGeneration = panelCount * perPanelGeneration;
            double eligibleOffset = Math.min(targetEnergy, estimatedGeneration);

            InverterSelection inverterSelection = selectInverter(catalog.inverters,
                    installedPowerKW, selectedPanel);
            double tariff = tariffForStratum(company, property.getStratum());
            double adjustedTariff = consumption.getContribution() ? tariff * CONTRIBUTION_FACTOR : tariff;
            double savings = Math.min(consumption.getAmountToPay(), eligibleOffset * adjustedTariff);
            double remainingBill = Math.max(0, consumption.getAmountToPay() - savings);

            reports.add(new ProjectReport(property, client, zone, company, consumption,
                    selectedPanel, panelCount, perPanelGeneration, estimatedGeneration,
                    eligibleOffset, installedPowerKW, savings, savings * 12.0,
                    remainingBill, inverterSelection.inverter, inverterSelection.quantity,
                    inverterSelection.totalPowerKW));
        }
        return reports;
    }

    private static SolarPanel selectPanel(ArrayList<SolarPanel> panels) throws IOException {
        SolarPanel selected = null;
        for (SolarPanel panel : panels) {
            if (selected == null || panel.getPower() > selected.getPower()) {
                selected = panel;
            }
        }
        if (selected == null) {
            throw new IOException("No hay paneles disponibles en solarPanels.txt.");
        }
        return selected;
    }

    /** Elige una combinación de inversores con potencia AC total >= potencia DC / 1,35. */
    private static InverterSelection selectInverter(ArrayList<Inverter> inverters,
            double installedPowerKW, SolarPanel panel) throws IOException {
        double requiredAcPower = installedPowerKW / MAX_DC_AC_RATIO;
        InverterSelection best = null;
        for (Inverter inverter : inverters) {
            if (inverter.getMaxDcVoltage() < panel.getDcVoltage()
                    || inverter.getMaxDcInputCurrent() < panel.getDcCurrent()) {
                continue;
            }
            int quantity = Math.max(1, (int) Math.ceil(requiredAcPower / inverter.getPower()));
            double totalPower = quantity * inverter.getPower();
            double excess = totalPower - requiredAcPower;
            InverterSelection candidate = new InverterSelection(inverter, quantity, totalPower, excess);
            if (best == null || candidate.excessPowerKW < best.excessPowerKW - 0.000001
                    || (Math.abs(candidate.excessPowerKW - best.excessPowerKW) < 0.000001
                        && candidate.quantity < best.quantity)) {
                best = candidate;
            }
        }
        if (best == null) {
            throw new IOException("No hay inversores compatibles con el panel seleccionado (tensión/corriente DC).");
        }
        return best;
    }

    private static double tariffForStratum(ElectricCompany company, int stratum) throws IOException {
        switch (stratum) {
        case 1: return company.getTariffStratum1();
        case 2: return company.getTariffStratum2();
        case 3: return company.getTariffStratum3();
        case 4: return company.getTariffStratum4();
        case 5: return company.getTariffStratum5();
        case 6: return company.getTariffStratum6();
        default: throw new IOException("Estrato inválido: " + stratum);
        }
    }

    private static void writeIndividualReports(ArrayList<ProjectReport> reports, File directory) throws IOException {
        for (ProjectReport report : reports) {
            ArrayList<String> lines = new ArrayList<String>();
            lines.add("============================================================");
            lines.add("INFORME PRELIMINAR DE DIMENSIONAMIENTO SOLAR - SOLARCALC");
            lines.add("============================================================");
            lines.add("");
            lines.add("CLIENTE");
            lines.add("ID: " + report.client.getClientId());
            lines.add("Nombre: " + report.client.getFirstName() + " " + report.client.getLastName());
            lines.add("Identificación: " + report.client.getTypeId() + " " + report.client.getNumberId());
            lines.add("");
            lines.add("PROPIEDAD");
            lines.add("ID: " + report.property.getPropertyId());
            lines.add("Dirección: " + report.property.getAddress());
            lines.add("Zona: " + report.zone.getCity() + " - " + report.zone.getDepartment());
            lines.add("Horas de sol pico usadas: " + decimal(report.zone.getPeakSunHours()) + " h/día");
            lines.add("Estrato: " + report.property.getStratum());
            lines.add("Empresa comercializadora: " + report.company.getCompanyName()
                    + " (" + report.company.getCompanyId() + ")");
            lines.add("");
            lines.add("CONSUMO Y COSTO ESTIMADO");
            lines.add("Consumo mensual: " + decimal(report.consumption.getKWhMonth()) + " kWh");
            lines.add("Contribución del 20 % aplicada: " + (report.consumption.getContribution() ? "Sí" : "No"));
            lines.add("Valor mensual estimado sin sistema solar: " + cop(report.consumption.getAmountToPay()));
            lines.add("");
            lines.add("DIMENSIONAMIENTO FOTOVOLTAICO");
            lines.add("Cobertura objetivo: 80 % del consumo mensual");
            lines.add("Energía objetivo: " + decimal(report.consumption.getKWhMonth() * COVERAGE_FACTOR) + " kWh/mes");
            lines.add("Panel seleccionado: " + report.panel.getBrand() + " " + report.panel.getModel());
            lines.add("Potencia por panel: " + report.panel.getPower() + " W");
            lines.add("Cantidad estimada de paneles: " + report.panelCount);
            lines.add("Potencia DC instalada: " + decimal(report.installedPowerKW) + " kW");
            lines.add("Generación teórica por panel: " + decimal(report.generationPerPanelKWh) + " kWh/mes");
            lines.add("Generación estimada total: " + decimal(report.estimatedGenerationKWh) + " kWh/mes");
            lines.add("Energía considerada para ahorro (limitada al objetivo del 80 %): "
                    + decimal(report.eligibleOffsetKWh) + " kWh/mes");
            lines.add("Garantía del panel indicada en catálogo: " + report.panel.getWarrantyYears() + " años");
            lines.add("");
            lines.add("INVERSORES RECOMENDADOS");
            lines.add("Marca/modelo: " + report.inverter.getBrand() + " " + report.inverter.getModel());
            lines.add("Tipo: " + report.inverter.getSystemType());
            lines.add("Fases: " + report.inverter.getPhaseType());
            lines.add("Potencia unitaria: " + decimal(report.inverter.getPower()) + " kW");
            lines.add("Cantidad: " + report.inverterQuantity);
            lines.add("Potencia AC total: " + decimal(report.totalInverterPowerKW) + " kW");
            lines.add("Tensión DC máxima: " + decimal(report.inverter.getMaxDcVoltage()) + " V");
            lines.add("Corriente DC máxima: " + decimal(report.inverter.getMaxDcInputCurrent()) + " A");
            lines.add("Monitoreo integrado: " + (report.inverter.getIntegratedMonitoring() ? "Sí" : "No"));
            lines.add("Garantía del inversor: no especificada en el catálogo de entrada");
            lines.add("");
            lines.add("AHORRO ESTIMADO");
            lines.add("Ahorro mensual estimado: " + cop(report.monthlySavingsCOP));
            lines.add("Ahorro anual estimado: " + cop(report.annualSavingsCOP));
            lines.add("Factura mensual estimada después del ahorro: " + cop(report.estimatedBillAfterSolarCOP));
            lines.add("");
            lines.add("Nota: estimación académica preliminar. No sustituye el diseño, estudio ni validación de un profesional.");
            lines.add("La generación usa un factor de rendimiento supuesto del 80 % y 30 días por mes.");
            writeLines(new File(directory, report.property.getPropertyId() + "_informe.txt"), lines);
        }
    }

    private static void writeProjectSummary(ArrayList<ProjectReport> reports, File directory) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("PropertyId;ClientId;ClientName;Zone;Stratum;MonthlyConsumption_kWh;PanelCount;PanelPower_W;InstalledPower_kW;InverterBrand;InverterModel;InverterCount;MonthlySavings_COP;AnnualSavings_COP;PanelWarrantyYears");
        for (ProjectReport report : reports) {
            lines.add(report.property.getPropertyId() + ";" + report.client.getClientId() + ";"
                    + csv(report.client.getFirstName() + " " + report.client.getLastName()) + ";"
                    + csv(report.zone.getCity()) + ";" + report.property.getStratum() + ";"
                    + decimal(report.consumption.getKWhMonth()) + ";" + report.panelCount + ";"
                    + report.panel.getPower() + ";" + decimal(report.installedPowerKW) + ";"
                    + csv(report.inverter.getBrand()) + ";" + csv(report.inverter.getModel()) + ";"
                    + report.inverterQuantity + ";" + decimal(report.monthlySavingsCOP) + ";"
                    + decimal(report.annualSavingsCOP) + ";" + report.panel.getWarrantyYears());
        }
        writeLines(new File(directory, "resumen_proyectos.csv"), lines);
    }

    private static void writePanelSummary(ArrayList<ProjectReport> reports, File directory) throws IOException {
        Map<String, Integer> panelCounts = new HashMap<String, Integer>();
        Map<String, SolarPanel> panels = new HashMap<String, SolarPanel>();
        for (ProjectReport report : reports) {
            String key = report.panel.getBrand() + "|" + report.panel.getModel();
            Integer count = panelCounts.get(key);
            panelCounts.put(key, Integer.valueOf((count == null ? 0 : count.intValue()) + report.panelCount));
            panels.put(key, report.panel);
        }
        ArrayList<String> keys = new ArrayList<String>(panelCounts.keySet());
        Collections.sort(keys);
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("Brand;Model;Power_W;Warranty_years;Total_panels_recommended");
        for (String key : keys) {
            SolarPanel panel = panels.get(key);
            lines.add(panel.getBrand() + ";" + panel.getModel() + ";" + panel.getPower() + ";"
                    + panel.getWarrantyYears() + ";" + panelCounts.get(key));
        }
        writeLines(new File(directory, "resumen_paneles.csv"), lines);
    }

    private static void writeInverterSummary(ArrayList<ProjectReport> reports, File directory) throws IOException {
        Map<String, Integer> inverterCounts = new HashMap<String, Integer>();
        Map<String, Inverter> inverters = new HashMap<String, Inverter>();
        for (ProjectReport report : reports) {
            String key = report.inverter.getBrand() + "|" + report.inverter.getModel();
            Integer count = inverterCounts.get(key);
            inverterCounts.put(key, Integer.valueOf((count == null ? 0 : count.intValue()) + report.inverterQuantity));
            inverters.put(key, report.inverter);
        }
        ArrayList<String> keys = new ArrayList<String>(inverterCounts.keySet());
        Collections.sort(keys);
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("Brand;Model;Power_kW;SystemType;PhaseType;Total_inverters_recommended");
        for (String key : keys) {
            Inverter inverter = inverters.get(key);
            lines.add(inverter.getBrand() + ";" + inverter.getModel() + ";" + decimal(inverter.getPower())
                    + ";" + inverter.getSystemType() + ";" + inverter.getPhaseType() + ";" + inverterCounts.get(key));
        }
        writeLines(new File(directory, "resumen_inversores.csv"), lines);
    }

    private static List<String[]> readRecords(File file, int expectedFields) throws IOException {
        List<String[]> records = readRecordsVariable(file);
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).length != expectedFields) {
                throw new IOException("Formato inválido en " + file.getName() + ", línea " + (i + 1)
                        + ": se esperaban " + expectedFields + " campos y se encontraron " + records.get(i).length + ".");
            }
        }
        return records;
    }

    private static List<String[]> readRecordsVariable(File file) throws IOException {
        if (!file.isFile()) {
            throw new IOException("No se encontró el archivo de entrada: " + file.getAbsolutePath());
        }
        List<String[]> records = new ArrayList<String[]>();
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] fields = line.split(";", -1);
            for (int i = 0; i < fields.length; i++) {
                fields[i] = fields[i].trim();
            }
            records.add(fields);
        }
        return records;
    }

    private static void checkId(String expected, String actual, File file) throws IOException {
        if (!expected.equals(actual)) {
            throw new IOException("Los identificadores de " + file.getName() + " no coinciden con el orden esperado: "
                    + "se esperaba " + actual + " y se encontró " + expected + ".");
        }
    }

    private static double parseDouble(String value, File file, String fieldName) throws IOException {
        try {
            double number = Double.parseDouble(value);
            if (Double.isNaN(number) || Double.isInfinite(number)) {
                throw new NumberFormatException();
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IOException("Dato numérico inválido para " + fieldName + " en " + file.getName() + ": " + value);
        }
    }

    private static int parseInt(String value, File file, String fieldName) throws IOException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IOException("Dato entero inválido para " + fieldName + " en " + file.getName() + ": " + value);
        }
    }

    private static boolean parseBoolean(String value, File file, String fieldName) throws IOException {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IOException("Dato booleano inválido para " + fieldName + " en " + file.getName() + ": " + value);
    }

    private static void writeLines(File file, List<String> lines) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("No fue posible crear la carpeta " + parent.getAbsolutePath());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private static File getProjectRoot() throws IOException {
        File current = new File(".").getCanonicalFile();
        if (new File(current, "data").isDirectory() && new File(current, "src").isDirectory()) {
            return current;
        }
        File nested = new File(current, "Calculator_solar_app").getCanonicalFile();
        if (new File(nested, "data").isDirectory() && new File(nested, "src").isDirectory()) {
            return nested;
        }
        throw new IOException("No se pudo detectar la raíz del proyecto. Ejecuta la aplicación desde Calculator_solar_app.");
    }

    private static String decimal(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private static String cop(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(COLOMBIAN_LOCALE);
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);
        return format.format(value);
    }

    private static String csv(String value) {
        if (value.indexOf(';') >= 0 || value.indexOf('"') >= 0) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static final class Catalog {
        private final Map<String, Zone> zones = new HashMap<String, Zone>();
        private final Map<String, Client> clients = new HashMap<String, Client>();
        private final Map<String, ElectricCompany> companies = new HashMap<String, ElectricCompany>();
        private final Map<String, Property> properties = new HashMap<String, Property>();
        private final Map<String, Consumption> consumptions = new HashMap<String, Consumption>();
        private final ArrayList<SolarPanel> panels = new ArrayList<SolarPanel>();
        private final ArrayList<Inverter> inverters = new ArrayList<Inverter>();
    }

    private static final class ProjectReport {
        private final Property property;
        private final Client client;
        private final Zone zone;
        private final ElectricCompany company;
        private final Consumption consumption;
        private final SolarPanel panel;
        private final int panelCount;
        private final double generationPerPanelKWh;
        private final double estimatedGenerationKWh;
        private final double eligibleOffsetKWh;
        private final double installedPowerKW;
        private final double monthlySavingsCOP;
        private final double annualSavingsCOP;
        private final double estimatedBillAfterSolarCOP;
        private final Inverter inverter;
        private final int inverterQuantity;
        private final double totalInverterPowerKW;

        private ProjectReport(Property property, Client client, Zone zone, ElectricCompany company,
                Consumption consumption, SolarPanel panel, int panelCount, double generationPerPanelKWh,
                double estimatedGenerationKWh, double eligibleOffsetKWh, double installedPowerKW,
                double monthlySavingsCOP, double annualSavingsCOP, double estimatedBillAfterSolarCOP,
                Inverter inverter, int inverterQuantity, double totalInverterPowerKW) {
            this.property = property;
            this.client = client;
            this.zone = zone;
            this.company = company;
            this.consumption = consumption;
            this.panel = panel;
            this.panelCount = panelCount;
            this.generationPerPanelKWh = generationPerPanelKWh;
            this.estimatedGenerationKWh = estimatedGenerationKWh;
            this.eligibleOffsetKWh = eligibleOffsetKWh;
            this.installedPowerKW = installedPowerKW;
            this.monthlySavingsCOP = monthlySavingsCOP;
            this.annualSavingsCOP = annualSavingsCOP;
            this.estimatedBillAfterSolarCOP = estimatedBillAfterSolarCOP;
            this.inverter = inverter;
            this.inverterQuantity = inverterQuantity;
            this.totalInverterPowerKW = totalInverterPowerKW;
        }
    }

    private static final class InverterSelection {
        private final Inverter inverter;
        private final int quantity;
        private final double totalPowerKW;
        private final double excessPowerKW;

        private InverterSelection(Inverter inverter, int quantity, double totalPowerKW, double excessPowerKW) {
            this.inverter = inverter;
            this.quantity = quantity;
            this.totalPowerKW = totalPowerKW;
            this.excessPowerKW = excessPowerKW;
        }
    }
}
