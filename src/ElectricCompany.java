/**
 * Representa una compañía eléctrica registrada utilizada por Calculator_Solar_app.
 *
 * <p>
 * Una compañía contiene la información necesaria para relacionar
 * una nombre, apeliido y zona.
 * </p>
 *
 * @author jvale
 * @version 1.0
 */
public class ElectricCompany {
    private static int idCounter = 1; 

    private String companyId;
    private String companyName;
    private String ZoneId; 

    private double tariffStratum1;
    private double tariffStratum2;
    private double tariffStratum3;
    private double tariffStratum4;
    private double tariffStratum5;
    private double tariffStratum6;
/*Crea un objeto de tipo ElectricCompany  con la información proporcionada 
* 
*@param companyName nombre de la compañía eléctrica
*@param ZoneId identificador de la zona asociada a la compañía eléctrica 
*@param tariffStratum1 tarifa para el estrato 1
*@param tariffStratum2 tarifa para el estrato 2
*@param tariffStratum3 tarifa para el estrato 3
*@param tariffStratum4 tarifa para el estrato 4
*@param tariffStratum5 tarifa para el estrato 5
*@param tariffStratum6 tarifa para el estrato 6
*
*/

public ElectricCompany(String companyName, String ZoneId, double tariffStratum1, double tariffStratum2,
            double tariffStratum3, double tariffStratum4, double tariffStratum5, double tariffStratum6) {
        this.companyId = String.format("E%03d", idCounter);
        idCounter++;
        this.companyName = companyName;
        this.ZoneId = ZoneId;
        this.tariffStratum1 = tariffStratum1;
        this.tariffStratum2 = tariffStratum2;
        this.tariffStratum3 = tariffStratum3;
        this.tariffStratum4 = tariffStratum4;
        this.tariffStratum5 = tariffStratum5;
        this.tariffStratum6 = tariffStratum6;
    }
/*Obtiene el identificador de la compañía eléctrica
*
*@return identificador de la compañía eléctrica
*
 */
    public String getCompanyId() {
        return companyId;
    }
/*Obtiene el nombre de la compañía eléctrica
*
*@return nombre de la compañía eléctrica
*
 */
    public String getCompanyName() {
        return companyName;
    }
/*Obtiene el identificador de la zona asociada a la compañía eléctrica
*
*@return identificador de la zona asociada a la compañía eléctrica
*
 */
    public String getZoneId() {
        return ZoneId;
    }
/*Obtiene la tarifa para el estrato 1
*
*@return tarifa para el estrato 1
*
 */
    public double getTariffStratum1() {
        return tariffStratum1;
    }
/*Obtiene la tarifa para el estrato 2
*
*@return tarifa para el estrato 2
*
 */
    public double getTariffStratum2() {
        return tariffStratum2;
    }
/*Obtiene la tarifa para el estrato 3
*
*@return tarifa para el estrato 3
*
 */
    public double getTariffStratum3() {
        return tariffStratum3;
    }
/*Obtiene la tarifa para el estrato 4
*
*@return tarifa para el estrato 4
*
 */
    public double getTariffStratum4() {
        return tariffStratum4;
    }
/*Obtiene la tarifa para el estrato 5
*
*@return tarifa para el estrato 5
*
 */
    public double getTariffStratum5() {
        return tariffStratum5;
    }
/*Obtiene la tarifa para el estrato 6
*
*@return tarifa para el estrato 6
*
 */
    public double getTariffStratum6() {
        return tariffStratum6;
    }
}