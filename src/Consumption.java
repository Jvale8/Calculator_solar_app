/**
 * Representa un consumo de cliente registrado utilizado por Calculator_Solar_app.
 *
 * <p>
 * Un consumo contiene la información necesaria para relacionar
 * una cliente, cantidad de Kwh/mes, contibución
 * </p>
 *
 * @author jvale
 * @version 1.0
 */
public class Consumption {
	private String clientId;
	private int kWhMonth;
	private boolean contribution;
	

/**
 * Construye un objeto Consumption con la información
 * del consumo mensual de un cliente.
 *
 * @param clientId identificador del cliente
 * @param kWhMonth consumo mensual de energía en kWh
 * @param contribution porcentaje de contribución aplicado
 */
	public Consumption(String clientId, int kWhMonth, boolean contribution) {

	    this.clientId = clientId;
	    this.kWhMonth = kWhMonth;
	    this.contribution = contribution;
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
 * Obtiene el consumo mensual de energía en kWh.
 * 
 * @return consumo mensual de energía en kWh
 */
public int getKWhMonth() {
	return kWhMonth;
}

/**
 * Obtiene el porcentaje de contribución aplicado.
 *
 * @return porcentaje de contribución aplicado
 */	
public boolean getContribution() {
	return contribution;	
}
}