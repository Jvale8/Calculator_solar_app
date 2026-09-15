/**
 * Clase que representa una propiedad perteneciente a un cliente en el sistema de Calculator_Solar_app.
 * 
 * <p>
 * Una propiedad contiene la información necesaria para relacionar  como un propietario, dirección, zona, compañía eléctrica y estrato.
 * </p> 
 * @author jvale
 * @version 1.0
*/

public class Property {
    private static int idCounter = 1;
    
    private String propertyId;
    private String clientId;
    private String address;
    private String zoneId;
    private String electricCompanyId;
    private int stratum;

/**
 * Crea una propiedad con la información proporcionada. 
 * 
 * @param clientId identificador del cliente propietario de la propiedad
 * @param address dirección de la propiedad
 * @param zoneId identificador de la zona geográfica donde se encuentra la propiedad
 * @param electricCompanyId identificador de la compañía eléctrica asociada a la propiedad
 * @param stratum estrato socioeconómico de la propiedad
 * 
 * */
    public Property(String clientId, String address, String zoneId, String electricCompanyId, int stratum) {
        this.propertyId = String.format("P%03d", idCounter);
        idCounter++;
        this.clientId = clientId;
        this.address = address;
        this.zoneId = zoneId;
        this.electricCompanyId = electricCompanyId;
        this.stratum = stratum;
    }

/**
 * Obtiene el identificador de la propiedad.
 * 
 * @return identificador de la propiedad
 * */

    public String getPropertyId() {
        return propertyId;
    }  

/**
 * Obtiene el identificador del cliente propietario de la propiedad.
 * 
 * @return identificador del cliente
 * */

    public String getClientId() {
        return clientId;
    }

/**
 * Obtiene la dirección de la propiedad
 *  
 * @return dirección de la propiedad
 * */
    public String getAddress() {
        return address;
    }

/**
 * Obtiene el identificador de la zona geográfica donde se encuentra la propiedad.
 * 
 * @return identificador de la zona
 * */
    public String getZoneId() {
        return zoneId;
    }

/**
 * Obtiene el identificador de la compañía eléctrica asociada a la propiedad.
 * 
 * @return identificador de la compañía eléctrica
 * */
    public String getElectricCompanyId() {
        return electricCompanyId;
    }

/**
 * Obtiene el estrato socioeconómico de la propiedad.
 * 
 * @return estrato socioeconómico
 * */
    public int getStratum() {
        return stratum;
    }
}