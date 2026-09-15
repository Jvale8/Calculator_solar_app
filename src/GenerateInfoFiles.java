/**
 * Genera los archivos de texto utilizados como información
 * de entrada para la aplicación SolarCalc.
 *
 * <p>
 * Esta clase permite crear datos de prueba relacionados con
 * zonas geográficas, clientes, paneles solares, inversores
 * y empresas electrificadoras.
 * </p>
 *
 * @author  Juana Valentina Sánchez
 * @author jvale
 * @version 1.0
 */
//importación de librerias para utilizar las clases File, FileWriter y IOException que estan dentro de Java.io
//importación de libreria para trabajar con ArrayList en java y manejar colecciones de datos
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateInfoFiles {

	public static void main(String[] args) {
	    createZonesFile(20);
	    createClientsFile(4);
		createConsumptionsFile(4);
		createElectricCompaniesFile(8);
		createPropertiesFile(8);
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
		
//Creacion de array List para crear obejtos de la clase Zona
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
		
	//Condicionales para la validación de datos solicitados
	if (numberOfZones <= 0) {
		    System.out.println("Error: la cantidad de zonas debe ser mayor que cero.");
		    return;
		}

	if (numberOfZones > zones.size()) {
		    System.out.println("Error: la cantidad de zonas solicitada supera las zonas disponibles.");
		    return;
		}
	
		 try {

	        FileWriter writer = new FileWriter("data/zones.txt");

	        for (int i = 0; i < numberOfZones; i++) {
	        	
	        	String line = zones.get(i).getZoneId()+ ";"
	        	        + zones.get(i).getCity()+ ";"
	        	        + zones.get(i).getDepartment()+ ";"
	        	        + zones.get(i).getPeakSunHours();

	        	    writer.write(line + "\n");

	        	    System.out.println(line);
	        }

	        writer.close();

	        System.out.println("Archivo zones.txt creado correctamente.");

	    } 
		 catch (IOException e) {

	        System.out.println("Error al crear el archivo zones.txt.");
	    }
	}

public static void createClientsFile(int numberOfClients) {
	
	//Creación de arrayLista para creación de objetos cliente
	ArrayList<Client> clients = new ArrayList<Client>();
	
	clients.add(new Client("Juana", "Sanchez", "CC", 123456789));
	clients.add(new Client("Carlos", "Perez", "CC", 987654321));
	clients.add(new Client("Maria", "Gomez", "CC", 456789123));
	clients.add(new Client("Juliana","Franco", "CC", 321654987));

	
	//Condicionales para la validación de datos solicitados
	if (numberOfClients <= 0) {
		    System.out.println("Error: la cantidad de clientes debe ser mayor que cero.");
		    return;
		}

	if (numberOfClients> clients.size()) {
		    System.out.println("Error: la cantidad de clientes solicitados supera los clientes disponibles.");
		    return;
		}
	
		 try {

	        FileWriter writer = new FileWriter("data/clients.txt");
	
	   for (int i = 0; i < numberOfClients; i++) {

		 String line = clients.get(i).getClientId()+ ";"
			        + clients.get(i).getFirstName()+ ";"
			        + clients.get(i).getLastName()+ ";"
			        + clients.get(i).getTypeId()+ ";"
			        + clients.get(i).getNumberId();

			    writer.write(line + "\n");

			    System.out.println(line);
	}
	
	writer.close();

	System.out.println("Archivo clients.txt creado correctamente.");
		 } 
		 
		 catch (IOException e) {
			 
			    System.out.println("Error al crear el archivo clients.txt.");
			}
}

public static void createConsumptionsFile(int numberOfConsumptions) {
	
	//Creación de arrayLista para creación de objetos consumo
	ArrayList<Consumption> consumptions = new ArrayList<Consumption>();
	
	consumptions.add(new Consumption("C001", 150, true));
	consumptions.add(new Consumption("C002", 200, true));
	consumptions.add(new Consumption("C003", 550, true));
	consumptions.add(new Consumption("C004", 250, true));
	
	
	//Condicionales para la validación de datos solicitados
	if (numberOfConsumptions <= 0) {
		    System.out.println("Error: la cantidad de consumos debe ser mayor que cero.");
		    return;
		}

	if (numberOfConsumptions> consumptions.size()) {
		    System.out.println("Error: la cantidad de consumos solicitados supera los consumos disponibles.");
		    return;
		}
	
		 try {

	        FileWriter writer = new FileWriter("data/consumptions.txt");
	
	   for (int i = 0; i < numberOfConsumptions; i++) {

		 String line = consumptions.get(i).getClientId()+ ";"
			        + consumptions.get(i).getKWhMonth()+ ";"
			        + consumptions.get(i).getContribution();

			    writer.write(line + "\n");

			    System.out.println(line);
	}
	
	writer.close();

	System.out.println("Archivo consumptions.txt creado correctamente.");
		 } 
		 
		 catch (IOException e) {
			 
			    System.out.println("Error al crear el archivo consumptions.txt.");
			}
}
private static void createElectricCompaniesFile(int numberOfCompanies) {
	//creacion de arrayList para crear objetos de la clase ElectricCompany
	ArrayList<ElectricCompany> companies = new ArrayList<ElectricCompany>();

    companies.add(new ElectricCompany("Vatia","Z001",450, 550, 650, 850, 1050, 1050));
    companies.add(new ElectricCompany("Enel Colombia","Z001",400, 500, 700, 820, 980, 980));
    companies.add(new ElectricCompany("EBSA","Z019",470, 570, 680, 890, 1080, 1080));
    companies.add(new ElectricCompany("Enerca","Z015",460, 560, 670, 875, 1060, 1060));
    companies.add(new ElectricCompany("EMSA","Z012",480, 580, 690, 905, 1090, 1090));
    companies.add(new ElectricCompany("ESSA","Z006",470, 570, 680, 885, 1070, 1070));
    companies.add(new ElectricCompany("Air-e","Z004",450, 550, 670, 890, 1070, 1070));
    companies.add(new ElectricCompany("EPM","Z002",430, 530, 650, 815, 980, 980));

	//condicionales para la validación de datos solicitados
	if (numberOfCompanies <= 0) {
	    System.out.println("Error: la cantidad de compañías eléctricas debe ser mayor que cero.");
	    return;
	}
	if(numberOfCompanies > companies.size()) {
	    System.out.println("Error: la cantidad de compañías eléctricas solicitadas supera las disponibles.");
	    return;
	}
	try {
		FileWriter writer = new FileWriter("data/electric_companies.txt");

		for (int i = 0; i < numberOfCompanies; i++) {
			String line = companies.get(i).getCompanyId()+ ";"
			        + companies.get(i).getCompanyName()+ ";"
			        + companies.get(i).getZoneId()+ ";"
			        + companies.get(i).getTariffStratum1()+ ";"
			        + companies.get(i).getTariffStratum2()+ ";"
			        + companies.get(i).getTariffStratum3()+ ";"
			        + companies.get(i).getTariffStratum4()+ ";"
			        + companies.get(i).getTariffStratum5()+ ";"
			        + companies.get(i).getTariffStratum6();

			    writer.write(line + "\n");

			    System.out.println(line);
		}

		writer.close();

		System.out.println("Archivo electric_companies.txt creado correctamente.");
	}
	 catch (IOException e) {

		System.out.println("Error al crear el archivo electric_companies.txt.");		
	}
}
	private static void createPropertiesFile(int numberOfProperties) {

		//creacion de arrayList para crear objetos de la clase Property	
		ArrayList<Property> properties = new ArrayList<Property>();
	
		properties.add(new Property("C001", "Calle 123 #45-67", "Z001", "E002", 3));
		properties.add(new Property("C002", "Carrera 45 #12-34", "Z002", "E008", 4));
		properties.add(new Property("C007", "Avenida 78 #56-89", "Z003", "E001", 2));
		properties.add(new Property("C004", "Calle 56 #78-90", "Z004", "E007", 5));
		properties.add(new Property("C001", "Carrera 12 #34-56", "Z002", "E008", 3));
		properties.add(new Property("C006", "Avenida 34 #56-78",	 "Z006", "E006", 4));
		properties.add(new Property("C007", "Calle 78 #90-12", "Z007", "E001", 2));
		properties.add(new Property("C005", "Carrera 90 #12-34", "Z001", "E008", 5));

		//condicionales para la validación de datos solicitados
		if (numberOfProperties <= 0) {
		    System.out.println("Error: la cantidad de propiedades debe ser mayor que cero.");
		    return;
		}
		if(numberOfProperties > properties.size()) {
		    System.out.println("Error: la cantidad de propiedades solicitadas supera las disponibles.");
		    return;
		}

		try {
			FileWriter writer = new FileWriter("data/properties.txt");

			for (int i = 0; i < numberOfProperties; i++) {
				String line = properties.get(i).getPropertyId()+ ";"
				        + properties.get(i).getClientId()+ ";"
				        + properties.get(i).getAddress()+ ";"
				        + properties.get(i).getZoneId()+ ";"
				        + properties.get(i).getElectricCompanyId()+ ";"
				        + properties.get(i).getStratum();

				    writer.write(line + "\n");

				    System.out.println(line);
			}

			writer.close();

			System.out.println("Archivo properties.txt creado correctamente.");
		}
		 catch (IOException e) {

			System.out.println("Error al crear el archivo properties.txt.");		
		}
 }
}