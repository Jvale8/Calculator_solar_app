/**
 * Representa el consumo mensual de energía eléctrica
 * asociado a una propiedad dentro del sistema SolarCalc.
 *
 * <p>
 * La clase relaciona el cliente y la propiedad con el consumo
 * mensual y permite calcular el valor aproximado a pagar
 * de acuerdo con el estrato y la tarifa de la empresa
 * comercializadora.
 * </p>
 *
 * <p>
 * Para los estratos 5 y 6 se aplica una contribución del 20 %
 * sobre el valor base del consumo.
 * </p>
 *
 * @author jvale
 * @version 1.0
 */
public class Consumption {

    private String clientId;
    private String propertyId;
    private String companyId;
    private double kWhMonth;
    private boolean contribution;
    private double amountToPay;

    /**
     * Crea un consumo asociado a una propiedad.
     *
     * <p>
     * El cliente y la empresa comercializadora se obtienen
     * a partir de la propiedad asociada.
     * </p>
     *
     * @param property propiedad asociada al consumo
     * @param kWhMonth consumo mensual en kilovatios-hora
     * @param company empresa comercializadora de energía
     */
    public Consumption(Property property, double kWhMonth,
            ElectricCompany company) {

        if (kWhMonth < 0) {
            throw new IllegalArgumentException(
                    "El consumo no puede ser negativo.");
        }

        this.clientId = property.getClientId();
        this.propertyId = property.getPropertyId();
        this.companyId = property.getElectricCompanyId();
        this.kWhMonth = kWhMonth;

        int stratum = property.getStratum();

        this.contribution = stratum == 5 || stratum == 6;

        this.amountToPay = calculateAmountToPay(property, company);
    }

    /**
     * Calcula el valor aproximado a pagar por el consumo mensual.
     *
     * <p>
     * Primero se obtiene la tarifa correspondiente al estrato
     * de la propiedad. Luego se multiplica por el consumo mensual.
     * Finalmente, si el estrato es 5 o 6, se agrega una contribución
     * del 20 %.
     * </p>
     *
     * @param property propiedad asociada al consumo
     * @param company empresa comercializadora de energía
     * @return valor aproximado a pagar
     */
    private double calculateAmountToPay(Property property,
            ElectricCompany company) {

        double tariff = getTariffByStratum(
                property.getStratum(), company);

        double baseAmount = kWhMonth * tariff;

        if (contribution) {
            baseAmount = baseAmount * 1.20;
        }

        return baseAmount;
    }

    /**
     * Obtiene la tarifa correspondiente al estrato.
     *
     * @param stratum estrato de la propiedad
     * @param company empresa comercializadora
     * @return tarifa correspondiente al estrato
     */
    private double getTariffByStratum(int stratum,
            ElectricCompany company) {

        switch (stratum) {
        case 1:
            return company.getTariffStratum1();

        case 2:
            return company.getTariffStratum2();

        case 3:
            return company.getTariffStratum3();

        case 4:
            return company.getTariffStratum4();

        case 5:
            return company.getTariffStratum5();

        case 6:
            return company.getTariffStratum6();

        default:
            throw new IllegalArgumentException(
                    "El estrato debe estar entre 1 y 6.");
        }
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
     * Obtiene el identificador de la propiedad.
     *
     * @return identificador de la propiedad
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * Obtiene el identificador de la compañía eléctrica.
     *
     * @return identificador de la compañía eléctrica
     */
    public String getElectricCompanyId() {
        return companyId;
    }

    /**
     * Obtiene el consumo mensual.
     *
     * @return consumo mensual en kWh
     */
    public double getKWhMonth() {
        return kWhMonth;
    }

    /**
     * Obtiene si el consumo tiene contribución.
     *
     * @return true si aplica contribución, false en caso contrario
     */
    public boolean getContribution() {
        return contribution;
    }

    /**
     * Obtiene el valor aproximado a pagar.
     *
     * @return valor aproximado del consumo
     */
    public double getAmountToPay() {
        return amountToPay;
    }
}
