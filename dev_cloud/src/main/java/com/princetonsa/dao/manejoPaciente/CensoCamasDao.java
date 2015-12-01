package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;




/**
 * @author Jhony Alexande Duque A.
 * jduque@princetonsa.com
 */
public interface CensoCamasDao
{
	/**
	 * Metodo Principal encargado de consultar el censo de camas
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @return mapa
	 */
	public HashMap consultaCenso (Connection connection,HashMap parametros);
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas reservadas
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public  String ConsultaFechaOcupacionReserva (Connection connection, int codigoCama);
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas en
	 * estados diferentes a resercama. 
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public String ConsultaFechaOcupacionOtrosEst(Connection connection, int cuenta);
	
	/**
	 *Metodo que consulta informacion estadistica y lo inserta en una tabla para
	 *luego ser consultada por otra funcionalidad. 
	 */
	public boolean corrrerProcesoAutomatico ();
	
}