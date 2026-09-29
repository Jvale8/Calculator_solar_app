/**
 * Representa un panel solar fotovoltaico disponible
 * para el sistema SolarCalc.
 *
 * @author jvale
 * @author Walter Ortiz
 * @version 1.0
 */
public class SolarPanel {

    private static int idCounter= 1;

    private String panelId;
    private String brand;
    private String model;
    private int powerWatts;  // Potencia en vatios
    private double dcVoltage;  //Voltaje DC
    private double dcCurrent; //Corriente DC
    private int warrantyYears; //Años de garantía

    /**
     * Crea un panel solar.
     *
     * @param brand marca del panel
     * @param model modelo del panel
     * @param power potencia del panel en W
     * @param dcVoltage tensión DC del panel
     * @param dcCurrent corriente DC del panel
     * @param warrantyYears años de garantía del panel
     */
    public SolarPanel(String brand, String model, int power,
            double dcVoltage, double dcCurrent, int warrantyYears) {

        this.panelId = String.format("SP%03d", idCounter);
        idCounter++;

        this.brand = brand;
        this.model = model;
        this.powerWatts = power;
        this.dcVoltage = dcVoltage;
        this.dcCurrent = dcCurrent;
        this.warrantyYears = warrantyYears;
    }

    /**
     * Obtiene el identificador del panel.
     *
     * @return identificador del panel
     */
    public String getPanelId() {
        return panelId;
    }

    /**
     * Obtiene la marca del panel.
     *
     * @return marca
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Obtiene el modelo del panel.
     *
     * @return modelo
     */
    public String getModel() {
        return model;
    }

    /**
     * Obtiene la potencia del panel.
     *
     * @return potencia en W
     */
    public int getPower() {
        return powerWatts;
    }

    /**
     * Obtiene la tensión DC.
     *
     * @return tensión DC
     */
    public double getDcVoltage() {
        return dcVoltage;
    }

    /**
     * Obtiene la corriente DC.
     *
     * @return corriente DC
     */
    public double getDcCurrent() {
        return dcCurrent;
    }

    /**
     * Obtiene los años de garantía del panel.
     *
     * @return años de garantía
     */
    public int getWarrantyYears() {
        return warrantyYears;
}
}