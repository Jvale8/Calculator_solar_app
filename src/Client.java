/**
 * Representa un cliente registrado utilizado por Calculator_Solar_app.
 *
 * <p>
 * Una cliente contiene la información necesaria para relacionar
 * una nombre, apeliido y zona.
 * </p>
 *
 * @author jvale
 * @version 1.0
 */
public class Client {
	
	private static int idCounter = 1;

    private String clientId;
	private String firstName;
	private String lastName;
	private String typeId;
    private int numberId;   
	

/**
 * Crea un cliente con la información proporcionada.
 *
 * @param firstName nombre del cliente
 * @param lastName apellido del cliente
 * @param typeId tipo de identificación del cliente
 * @param numberId número de identificación del cliente
 * 
*/
public Client(String firstName, String lastName,String typeId, int numberId) {

this.clientId =String.format("C%03d", idCounter);
idCounter++;

this.firstName = firstName;
this.lastName = lastName;
this.typeId = typeId;
this.numberId = numberId;
}

/**
 * Obtiene el identificador del cliente.
 *
 * @return identificador del cliente
 */
public String getClientId() {
    return clientId;
}

/**
 * Obtiene el nombre del cliente.
 *
 * @return nombre del cliente
 */
public String getFirstName() {
    return firstName;
}

/**
 * Obtiene el apellido del cliente.
 *
 * @return apellido del cliente
 */
public String getLastName() {
    return lastName;
}

/**
 * Obtiene el tipo de identificación del cliente.
 *
 * @return tipo de identificación del cliente
 */
public String getTypeId() {
    return typeId;
}

/**
 * Obtiene el número de identificación del cliente.
 *
 * @return número de identificación del cliente
 */
public int getNumberId() {
    return numberId;
}
}