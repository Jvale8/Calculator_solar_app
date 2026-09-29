/**
 * Genera los archivos de texto utilizados como información
 * de entrada para la aplicación SolarCalc.
 *
 * <p>
 * Esta clase permite crear datos de prueba relacionados con
 * zonas geográficas, clientes, propiedades, consumos,
 * paneles solares, inversores y empresas electrificadoras.
 * </p>
 *
 * @author Juana Valentina Sánchez
 * @author Walter Ortiz
 * @author jvale
 * @version 1.0
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateInfoFiles {

    public static void main(String[] args) {

        createZonesFile(20);

        createClientsFile(4);

        ArrayList<ElectricCompany> companies =
                createElectricCompaniesFile(8);

        ArrayList<Property> properties =
                createPropertiesFile(8);

        createConsumptionsFile(
                6,
                properties,
                companies);

        createSolarPanelsFile();

        createInvertersFile();
    }

    /**
     * Genera el archivo zones.txt con información de las zonas.
     *
     * <p>
     * Las zonas son representadas mediante objetos de la clase Zone
     * y posteriormente almacenadas en un archivo de texto.
     * </p>
     *
     * @param numberOfZones cantidad de zonas que se generarán
     */
    public static void createZonesFile(int numberOfZones) {

        // Creación de ArrayList para crear objetos de la clase Zone
        ArrayList<Zone> zones = new ArrayList<Zone>();

        zones.add(new Zone("Bogota", "Cundinamarca", 3.5));
        zones.add(new Zone("Medellin", "Antioquia", 4.5));
        zones.add(new Zone("Cali", "Valle del Cauca", 4.8));
        zones.add(new Zone("Barranquilla", "Atlantico", 5.0));
        zones.add(new Zone("Cartagena", "Bolivar", 5.2));
        zones.add(new Zone("Bucaramanga", "Santander", 4.5));
        zones.add(new Zone("Pereira", "Risaralda", 4.3));
        zones.add(new Zone("Manizales", "Caldas", 4.2));
        zones.add(new Zone("Armenia", "Quindio", 4.4));
        zones.add(new Zone("Cucuta", "Norte de Santander", 4.8));
        zones.add(new Zone("Ibague", "Tolima", 4.4));
        zones.add(new Zone("Villavicencio", "Meta", 4.5));
        zones.add(new Zone("Neiva", "Huila", 5.0));
        zones.add(new Zone("Santa Marta", "Magdalena", 5.3));
        zones.add(new Zone("Valledupar", "Cesar", 5.2));
        zones.add(new Zone("Monteria", "Cordoba", 5.0));
        zones.add(new Zone("Sincelejo", "Sucre", 5.1));
        zones.add(new Zone("Pasto", "Narino", 4.5));
        zones.add(new Zone("Tunja", "Boyaca", 4.0));
        zones.add(new Zone("Popayan", "Cauca", 4.6));

        // Validación de datos
        if (numberOfZones <= 0) {

            System.out.println(
                    "Error: la cantidad de zonas debe ser mayor que cero.");

            return;
        }

        if (numberOfZones > zones.size()) {

            System.out.println(
                    "Error: la cantidad de zonas solicitada "
                    + "supera las zonas disponibles.");

            return;
        }

        try {

            FileWriter writer =
                    new FileWriter("data/zones.txt");

            for (int i = 0; i < numberOfZones; i++) {

                String line =
                        zones.get(i).getZoneId() + ";"
                        + zones.get(i).getCity() + ";"
                        + zones.get(i).getDepartment() + ";"
                        + zones.get(i).getPeakSunHours();

                writer.write(line + "\n");
                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo zones.txt creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo zones.txt.");
        }
    }

    /**
     * Genera el archivo clients.txt con información de los clientes.
     *
     * <p>
     * Los clientes son representados mediante objetos de la clase
     * Client y posteriormente almacenados en un archivo de texto.
     * </p>
     *
     * @param numberOfClients cantidad de clientes que se generarán
     */
    public static void createClientsFile(int numberOfClients) {

        // Creación de ArrayList para crear objetos de la clase Client
        ArrayList<Client> clients =
                new ArrayList<Client>();

        clients.add(new Client(
                "Juana", "Sanchez", "CC", 123456789));

        clients.add(new Client(
                "Carlos", "Perez", "CC", 987654321));

        clients.add(new Client(
                "Maria", "Gomez", "CC", 456789123));

        clients.add(new Client(
                "Juliana", "Franco", "CC", 321654987));

        // Validación de datos
        if (numberOfClients <= 0) {

            System.out.println(
                    "Error: la cantidad de clientes debe ser mayor que cero.");

            return;
        }

        if (numberOfClients > clients.size()) {

            System.out.println(
                    "Error: la cantidad de clientes solicitados "
                    + "supera los clientes disponibles.");

            return;
        }

        try {

            FileWriter writer =
                    new FileWriter("data/clients.txt");

            for (int i = 0; i < numberOfClients; i++) {

                String line =
                        clients.get(i).getClientId() + ";"
                        + clients.get(i).getFirstName() + ";"
                        + clients.get(i).getLastName() + ";"
                        + clients.get(i).getTypeId() + ";"
                        + clients.get(i).getNumberId();

                writer.write(line + "\n");
                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo clients.txt creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo clients.txt.");
        }
    }

    /**
     * Genera el archivo electric_companies.txt con información
     * de las compañías eléctricas.
     *
     * <p>
     * Las compañías eléctricas son representadas mediante objetos
     * de la clase ElectricCompany y posteriormente almacenadas
     * en un archivo de texto.
     * </p>
     *
     * @param numberOfCompanies cantidad de compañías eléctricas
     *                           que se generarán
     * @return lista de compañías eléctricas generadas
     */
    private static ArrayList<ElectricCompany> createElectricCompaniesFile(
            int numberOfCompanies) {

        // Creación de ArrayList para crear objetos de la clase ElectricCompany
        ArrayList<ElectricCompany> companies =
                new ArrayList<ElectricCompany>();

        companies.add(new ElectricCompany(
                "Vatia", "Z001",
                450, 550, 650, 850, 1050, 1050));

        companies.add(new ElectricCompany(
                "Enel Colombia", "Z001",
                400, 500, 700, 820, 980, 980));

        companies.add(new ElectricCompany(
                "EBSA", "Z019",
                470, 570, 680, 890, 1080, 1080));

        companies.add(new ElectricCompany(
                "Enerca", "Z015",
                460, 560, 670, 875, 1060, 1060));

        companies.add(new ElectricCompany(
                "EMSA", "Z012",
                480, 580, 690, 905, 1090, 1090));

        companies.add(new ElectricCompany(
                "ESSA", "Z006",
                470, 570, 680, 885, 1070, 1070));

        companies.add(new ElectricCompany(
                "Air-e", "Z004",
                450, 550, 670, 890, 1070, 1070));

        companies.add(new ElectricCompany(
                "EPM", "Z002",
                430, 530, 650, 815, 980, 980));

        // Validación de datos
        if (numberOfCompanies <= 0) {

            System.out.println(
                    "Error: la cantidad de compañías eléctricas "
                    + "debe ser mayor que cero.");

            return null;
        }

        if (numberOfCompanies > companies.size()) {

            System.out.println(
                    "Error: la cantidad de compañías eléctricas "
                    + "solicitadas supera las disponibles.");

            return null;
        }

        try {

            FileWriter writer =
                    new FileWriter("data/electric_companies.txt");

            for (int i = 0; i < numberOfCompanies; i++) {

                String line =
                        companies.get(i).getCompanyId() + ";"
                        + companies.get(i).getCompanyName() + ";"
                        + companies.get(i).getZoneId() + ";"
                        + companies.get(i).getTariffStratum1() + ";"
                        + companies.get(i).getTariffStratum2() + ";"
                        + companies.get(i).getTariffStratum3() + ";"
                        + companies.get(i).getTariffStratum4() + ";"
                        + companies.get(i).getTariffStratum5() + ";"
                        + companies.get(i).getTariffStratum6();

                writer.write(line + "\n");
                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo electric_companies.txt "
                    + "creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo "
                    + "electric_companies.txt.");
        }

        return companies;
    }

    /**
     * Genera el archivo properties.txt con información
     * de las propiedades.
     *
     * <p>
     * Cada propiedad se relaciona con un cliente y una
     * compañía eléctrica mediante sus respectivos identificadores.
     * </p>
     *
     * @param numberOfProperties cantidad de propiedades
     *                            que se generarán
     * @return lista de propiedades generadas
     */
    private static ArrayList<Property> createPropertiesFile(
            int numberOfProperties) {

        // Creación de ArrayList para crear objetos de la clase Property
        ArrayList<Property> properties =
                new ArrayList<Property>();

        properties.add(new Property(
                "C001",
                "Calle 123 #45-67",
                "Z001",
                "E002",
                6));

        properties.add(new Property(
                "C002",
                "Carrera 45 #12-34",
                "Z002",
                "E008",
                4));

        properties.add(new Property(
                "C003",
                "Avenida 78 #56-89",
                "Z003",
                "E001",
                2));

        properties.add(new Property(
                "C004",
                "Calle 56 #78-90",
                "Z004",
                "E007",
                5));

        properties.add(new Property(
                "C001",
                "Carrera 12 #34-56",
                "Z002",
                "E008",
                3));

        properties.add(new Property(
                "C002",
                "Avenida 34 #56-78",
                "Z006",
                "E006",
                4));

        properties.add(new Property(
                "C003",
                "Calle 78 #90-12",
                "Z007",
                "E001",
                6));

        properties.add(new Property(
                "C004",
                "Carrera 90 #12-34",
                "Z001",
                "E008",
                5));

        // Validación de datos
        if (numberOfProperties <= 0) {

            System.out.println(
                    "Error: la cantidad de propiedades "
                    + "debe ser mayor que cero.");

            return null;
        }

        if (numberOfProperties > properties.size()) {

            System.out.println(
                    "Error: la cantidad de propiedades solicitadas "
                    + "supera las propiedades disponibles.");

            return null;
        }

        try {

            FileWriter writer =
                    new FileWriter("data/properties.txt");

            for (int i = 0; i < numberOfProperties; i++) {

                String line =
                        properties.get(i).getPropertyId() + ";"
                        + properties.get(i).getClientId() + ";"
                        + properties.get(i).getAddress() + ";"
                        + properties.get(i).getZoneId() + ";"
                        + properties.get(i).getElectricCompanyId() + ";"
                        + properties.get(i).getStratum();

                writer.write(line + "\n");
                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo properties.txt creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo properties.txt.");
        }

        return properties;
    }

    /**
     * Genera el archivo consumptions.txt con información
     * de los consumos mensuales.
     *
     * <p>
     * Cada consumo se relaciona con una propiedad existente.
     * La compañía eléctrica se obtiene mediante el identificador
     * almacenado en la propiedad.
     * </p>
     *
     * @param numberOfConsumptions cantidad de consumos que se generarán
     * @param properties lista de propiedades existentes
     * @param companies lista de compañías eléctricas existentes
     */
    private static void createConsumptionsFile(
            int numberOfConsumptions,
            ArrayList<Property> properties,
            ArrayList<ElectricCompany> companies) {

        // Validación de listas
        if (properties == null || companies == null) {

            System.out.println(
                    "Error: no se pudieron cargar las propiedades "
                    + "o las compañías eléctricas.");

            return;
        }

        // Validación de cantidad de consumos
        if (numberOfConsumptions <= 0) {

            System.out.println(
                    "Error: la cantidad de consumos "
                    + "debe ser mayor que cero.");

            return;
        }

        if (numberOfConsumptions > properties.size()) {

            System.out.println(
                    "Error: la cantidad de consumos solicitados "
                    + "supera las propiedades disponibles.");

            return;
        }

        // Valores de consumo mensual de prueba
        double[] monthlyConsumption = {
            323,
            467,
            538,
            650,
            412,
            589
        };

        if (numberOfConsumptions > monthlyConsumption.length) {

            System.out.println(
                    "Error: no hay suficientes valores de "
                    + "consumo disponibles.");

            return;
        }

        // Creación de ArrayList para los objetos Consumption
        ArrayList<Consumption> consumptions =
                new ArrayList<Consumption>();

        for (int i = 0; i < numberOfConsumptions; i++) {

            Property property = properties.get(i);

            ElectricCompany company = null;

            /*
             * Busca la compañía eléctrica cuyo identificador
             * coincide con el identificador almacenado
             * en la propiedad.
             */
            for (ElectricCompany currentCompany : companies) {

                if (currentCompany.getCompanyId().equals(
                        property.getElectricCompanyId())) {

                    company = currentCompany;
                    break;
                }
            }

            if (company == null) {

                System.out.println(
                        "Error: no se encontró la compañía eléctrica "
                        + "asociada a la propiedad "
                        + property.getPropertyId());

                return;
            }

            /*
             * Crea el consumo utilizando la propiedad existente,
             * el consumo mensual y la compañía eléctrica encontrada.
             */
            Consumption consumption =
                    new Consumption(
                            property,
                            monthlyConsumption[i],
                            company);

            consumptions.add(consumption);
        }

        try {

            FileWriter writer =
                    new FileWriter("data/consumptions.txt");

            for (Consumption consumption : consumptions) {

                String line =
                        consumption.getPropertyId() + ";"
                        + consumption.getKWhMonth();

                writer.write(line + "\n");

                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo consumptions.txt creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo consumptions.txt.");
        }
    }

    /**
     * Genera el archivo solarPanels.txt con información
     * de los paneles solares.
     */
    private static void createSolarPanelsFile() {

        // Creación de ArrayList para crear objetos de la clase SolarPanel
        ArrayList<SolarPanel> panels =
                new ArrayList<SolarPanel>();

        panels.add(new SolarPanel("Trina", "Vertex",550, 41.6, 13.22, 25));

        panels.add(new SolarPanel("LONGi", "Hi-MO", 600, 42.5, 14.12, 25));

        panels.add(new SolarPanel("JA Solar", "DeepBlue",650, 43.8, 14.84, 25));

        panels.add(new SolarPanel("Canadian Solar", "TOPHiKu",700, 44.1, 15.87, 25));

        try {

            FileWriter writer =
                    new FileWriter("data/solarPanels.txt");

            for (int i = 0; i < panels.size(); i++) {

                String line =
                        panels.get(i).getPanelId() + ";"
                        + panels.get(i).getBrand() + ";"
                        + panels.get(i).getModel() + ";"
                        + panels.get(i).getPower() + ";"
                        + panels.get(i).getDcVoltage() + ";"
                        + panels.get(i).getDcCurrent() + ";"
                        + panels.get(i).getWarrantyYears();

                writer.write(line + "\n");
                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo solarPanels.txt creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo solarPanels.txt.");
        }
    }

    /**
     * Genera el archivo inverters.txt con información
     * de los inversores solares.
     */
    private static void createInvertersFile() {

        // Creación de ArrayList para crear objetos de la clase Inverter
        ArrayList<Inverter> inverters =
                new ArrayList<Inverter>();

        inverters.add(new Inverter(
                "Interconectado",
                "Monofasico",
                "Hoymiles",
                "HMS-2000-4T",
                2.0,
                220,
                65,
                9.09,
                16,
                true));

        inverters.add(new Inverter(
                "Hibrido",
                "Monofasico",
                "Hoymiles",
                "HIS-3L-G3",
                3.0,
                220,
                550,
                15.0,
                18,
                true));

        inverters.add(new Inverter(
                "Interconectado",
                "Monofasico",
                "Huawei",
                "SUN2000",
                5.0,
                220,
                600,
                22.7,
                25,
                true));

        inverters.add(new Inverter(
                "Interconectado",
                "Trifasico",
                "SMA",
                "Sunny Tripower",
                10.0,
                220,
                1000,
                26.0,
                30,
                true));

        try {

            FileWriter writer =
                    new FileWriter("data/inverters.txt");

            for (int i = 0; i < inverters.size(); i++) {

                String line =
                        inverters.get(i).getInverterId() + ";"
                        + inverters.get(i).getSystemType() + ";"
                        + inverters.get(i).getPhaseType() + ";"
                        + inverters.get(i).getBrand() + ";"
                        + inverters.get(i).getModel() + ";"
                        + inverters.get(i).getPower() + ";"
                        + inverters.get(i).getAcOperatingVoltage() + ";"
                        + inverters.get(i).getMaxDcVoltage() + ";"
                        + inverters.get(i).getAcOutputCurrent() + ";"
                        + inverters.get(i).getMaxDcInputCurrent() + ";"
                        + inverters.get(i).getIntegratedMonitoring();

                writer.write(line + "\n");
                System.out.println(line);
            }

            writer.close();

            System.out.println(
                    "Archivo inverters.txt creado correctamente.");

        } catch (IOException e) {

            System.out.println(
                    "Error al crear el archivo inverters.txt.");
        }
    }
}