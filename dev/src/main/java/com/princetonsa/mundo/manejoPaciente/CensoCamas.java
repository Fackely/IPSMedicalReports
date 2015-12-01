package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.CensoCamasDao;





/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class CensoCamas
{
	/**
	 *Atributos Censo de Camas 
	 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(CensoCamas.class);
	
	
	/*-----------------------------------------------------------
	 *         				METODOS CENSO DE CAMAS
	 ------------------------------------------------------------*/
	

	
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static CensoCamasDao censoCamasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCensoCamasDao();
	}
	
	
	
	/**
	 * Metodo Principal encargado de consultar el censo de camas
	 * el HashMap parametros puede contener los siguentes parametros
	 * institucion --> Requerido
	 * convenio --> Opcional
	 * centroatencion --> Opcional
	 * centrocosto --> Opcional
	 * piso --> Opcional
	 * estado --> Opcional
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @return mapa
	 * -------------------------------------------------------
	 * 			KEY'S QUE CONTIENE EL HASHMAP CENSOCAMAS
	 * --------------------------------------------------------
	 * El mapa de salida contiene los siguientes key's
	 * nombrehabitacion, cama, estadocama, nombretipohabitacion,
	 * habitacioncama, tipousuariocama, sexocama, restriccioncama
	 * cuenta, fecha, diasestancia, hora, diagnostico, convenio,
	 * nombrepac, identificacionpac, tipoidentificacionpac, 
	 * sexopac, fechanacimientopac, edadpac, restriccioncama
	 *  
	 */
	public static HashMap consultaCenso (Connection connection,HashMap parametros)
	{
		return censoCamasDao().consultaCenso(connection, parametros);
	}
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas reservadas
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public static String ConsultaFechaOcupacionReserva (Connection connection, int codigoCama)
	{
		return censoCamasDao().ConsultaFechaOcupacionReserva(connection, codigoCama);
	}
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas en
	 * estados diferentes a resercama. 
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public static String ConsultaFechaOcupacionOtrosEst(Connection connection, int cuenta)
	{
		return censoCamasDao().ConsultaFechaOcupacionOtrosEst(connection, cuenta);
	}
	
	/**
	 *Metodo que consulta informacion estadistica y lo inserta en una tabla para
	 *luego ser consultada por otra funcionalidad. 
	 */
	public static boolean corrrerProcesoAutomatico ()
	{
		return censoCamasDao().corrrerProcesoAutomatico();
	}
	
}
