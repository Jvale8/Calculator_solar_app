/**
 * Representa una zona geográfica utilizada por Calculator_Solar_app.
 *
 * <p>
 * Una zona contiene la información necesaria para relacionar
 * una ubicación geográfica con sus horas de sol pico estimadas.
 * </p>
 *
 * @author jvale
 * @version 1.0
 */
public class Zone {
    private static int idCounter = 1;

    private String zoneId;
    private String city;
    private String department;
    private double peakSunHours;

/**
 * Crea una zona con la información proporcionada.
 *
 * @param zoneId identificador único de la zona
 * @param city ciudad asociada a la zona
 * @param department departamento al que pertenece la ciudad
 * @param peakSunHours horas de sol pico estimadas para la zona
 */
public Zone(String city, String department, double peakSunHours) {
    this.zoneId = String.format("Z%03d", idCounter);
    idCounter++;
    this.city = city;
    this.department = department;
    this.peakSunHours = peakSunHours;
}

/**
 * Obtiene el identificador de la zona.
 *
 * @return identificador de la zona
 */
public String getZoneId() {
	return zoneId;
}

/**
 * Obtiene la ciudad de la zona.
 *
 * @return Ciudad de la zona
 */
public String getCity() {
	return city;
}

/**
 * Obtiene el departamento de la zona.
 *
 * @return Departmento de la zona
 */
public String getDepartment() {
	return department;
}

/**
 * Obtiene las horas solares pico de la zona.
 *
 * @return Horas solares pico de la zona
 */
public double getPeakSunHours() {
	return peakSunHours;
}
}