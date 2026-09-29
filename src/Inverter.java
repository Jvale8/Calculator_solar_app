/**
 * Representa un inversor solar disponible
 * para el sistema SolarCalc.
 *
 * @author Juana Valentina Sánchez
 * @author Walter Ortiz
 * @version 1.0
 */
public class Inverter {

    private static int idCounter = 1;

    private String inverterId;
    private String brand;
    private String model;
    private String systemType;
    private String phaseType;
    private double power;
    private double acOperatingVoltage;
    private double maxDcVoltage;
    private double acOutputCurrent;
    private double maxDcInputCurrent;
    private boolean integratedMonitoring;

    /**
     * Crea un inversor solar.
     * @param brand marca
     * @param model modelo
     * @param systemType tipo de sistema: hibrido o interconectado
     * @param phaseType tipo de fase
     * @param power potencia en kW
     * @param acOperatingVoltage tensión AC de operación
     * @param maxDcVoltage tensión DC máxima
     * @param acOutputCurrent corriente AC de entrega
     * @param maxDcInputCurrent corriente DC máxima de recepción
     * @param integratedMonitoring indica si posee monitoreo integrado
     */
    public Inverter( String brand, String model,
         String systemType, String phaseType, double power,
            double acOperatingVoltage, double maxDcVoltage,
            double acOutputCurrent, double maxDcInputCurrent,
            boolean integratedMonitoring) {

        this.inverterId = String.format("SI%03d", idCounter);
        idCounter++;

        this.brand = brand;
        this.model = model;
        this.systemType = systemType;
        this.phaseType = phaseType;
        this.power = power;
        this.acOperatingVoltage = acOperatingVoltage;
        this.maxDcVoltage = maxDcVoltage;
        this.acOutputCurrent = acOutputCurrent;
        this.maxDcInputCurrent = maxDcInputCurrent;
        this.integratedMonitoring = integratedMonitoring;
    }

/**
     * Obtiene el identificador del inversor.
     *
     * @return identificador del inversor
     */
    public String getInverterId() {
        return inverterId;
    }

    /**
     * Obtiene el tipo de sistema.
     *
     * @return tipo de sistema
     */
    public String getSystemType() {
        return systemType;
    }

    /**
     * Obtiene el tipo de fase.
     *
     * @return tipo de fase
     */
    public String getPhaseType() {
        return phaseType;
    }

    /**
     * Obtiene la marca del inversor.
     *
     * @return marca del inversor
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Obtiene el modelo del inversor.
     *
     * @return modelo del inversor
     */
    public String getModel() {
        return model;
    }

    /**
     * Obtiene la potencia del inversor.
     *
     * @return potencia del inversor
     */
    public double getPower() {
        return power;
    }
    
    /**
     * Obtiene la tensión AC de operación del inversor.
     * 
     * @return tensión AC de operación
     *
     */
       public double getAcOperatingVoltage() {
        return acOperatingVoltage;
    }

    /**
     * Obtiene la tensión DC máxima del inversor.
     *
     * @return tensión DC máxima
     */
    public double getMaxDcVoltage() {
        return maxDcVoltage;
    }

    /**
     * Obtiene la corriente AC de entrega del inversor.
     *
     * @return corriente AC de entrega
     */
    public double getAcOutputCurrent() {
        return acOutputCurrent;
    }

    /**
     * Obtiene la corriente DC máxima de recepción del inversor.
     *
     * @return corriente DC máxima de recepción
     */
    public double getMaxDcInputCurrent() {
        return maxDcInputCurrent;
    }

    /**
     * Obtiene la información sobre el monitoreo integrado.
     *
     * @return true si posee monitoreo integrado, false en caso contrario
     */
    public boolean getIntegratedMonitoring() {
        return integratedMonitoring;
    }
}