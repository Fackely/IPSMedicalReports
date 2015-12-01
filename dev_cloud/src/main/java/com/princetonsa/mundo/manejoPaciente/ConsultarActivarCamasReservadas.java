package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultarActivarCamasReservadasDao;




/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com 
 *
 */
public class ConsultarActivarCamasReservadas
{
	
	/*----------------------------------------------------
	 * 	ATRIBUTOS DE CONSULTAR ACTIVAR CAMAS RESERVADAS
	 ----------------------------------------------------*/
	
	private ConsultarActivarCamasReservadasDao consultarActivarCamasReservadas;
	private Logger logger = Logger.getLogger(ConsultarActivarCamasReservadas.class);
	
	/*----------------------------------------------------
	 * FIN ATRIBUTOS DE CONSULTAR ACTIVAR CAMAS RESERVADAS
	 ----------------------------------------------------*/
	
	
	/*-------------------------------------------------------------
	 * METODOS PARA EL MANEJO DE CONSULTAR ACTIVAR CAMAS RESERVADAS
	 --------------------------------------------------------------*/
	
	/**
	 * Constructor de la clase consultar activar camas reservadas
	 */
	public void ConsultarActivarCamasReservadas ()
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		consultarActivarCamasReservadas = myFactory.getConsultarActivarCamasReservadasDao();
	}
	
	
	/**
	 * Metodo que inicializa el Dao
	 */
	public static ConsultarActivarCamasReservadasDao consultarActivarCamasReservadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarActivarCamasReservadasDao();
	}
	
	
	/**
	 * Metodo encargado de consultar las camas reservadas por diferentes 
	 * criterios de busuqeda; el HashMap parametros puede estar compuesto
	 * por los siguientes Key's:
	 * ---------------------------------------------------------------
	 * 			KEY'S QUE PUEDE CONTENER EL MAPA PARAMETROS
	 * --------------------------------------------------------------
	 * institucion --> Requerido
	 * centroatencion --> Opcional
	 * codigocama --> Opcional
	 * estadoreserva --> Opcional
	 * centrocosto --> Opcional
	 * piso --> Opcional
	 * tipohabitacion --> Opcional
	 * tipoidentificacion --> Opcional
	 * numeroidentificacion --> Opcional
	 * primernombre --> Opcional
	 * segundonombre --> Opcional
	 * primerapellido --> Opcional
	 * segundoapellido --> Opcional
	 * Y al momento de devolver la consulta, el metodo devuele el mapa 
	 * con los siguientes Key's:
	 * ----------------------------------------------------------------
	 * 				KEY'S QUE DEVUELVE LA CONSULTA
	 * ---------------------------------------------------------------
	 * centroatencion, codigopacinte, codigocama, fechareservada, 
	 * estadoreserva, codigoreserva, fechacreacion, nombrepac, 
	 * identificacionpac, tipoidentificacionpac, nombrecama, 
	 * nombrehabitacion, nombrecentrocosto, nombrepiso, nombretipohabitacion.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static HashMap consultarCamasReservadas (Connection connection,HashMap parametros)
	{
		return consultarActivarCamasReservadasDao().consultarCamasReservadas(connection, parametros);
	}
	
	
	/**
	 * Metodo encargado de buscar los datos de una reserva.
	 * El HashMap Parametros lleva los key's.
	 * ----------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * ----------------------------------------------
	 * --institucion --> Requerido
	 * --reservaCamaId --> Opcional
	 * ----------------------------------------------
	 * 			KEY'S DEL MAPA QUE DEVUELVE
	 * ----------------------------------------------
	 * nombrecentroatencionreserva, fechaocupacion,fechareserva,
	 * usuario, estadoreserva, codigocama, codigopaciente.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static HashMap consultaDatosReserva (Connection connection,HashMap parametros)
	{
		return consultarActivarCamasReservadasDao().consultaDatosReserva(connection, parametros);
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de una cama
	 * El HashMap parametros puede llevar los siguiente Key's:
	 * -----------------------------------------------------
	 * 					KEY'S DE PARAMETROS
	 * -----------------------------------------------------
	 * --institucion --> Requerido
	 * --codigocama --> Opcional 
	 * ---------------------------------------------------
	 * 			KEY'S DEL MAPA QUE DEVUELVE
	 * ---------------------------------------------------
	 * codigocama, codigohabitacion, numerocama, tipousuario,
	 * centrocosto, habitacion, nombrecentrocosto, piso,
	 * tipohabitacion, nombrecentroatencioncama,nombretipousuario.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static HashMap consultaDatoscama (Connection connection,HashMap parametros)
	{
		return consultarActivarCamasReservadasDao().consultaDatoscama(connection, parametros);
	}
	
	/**
	 * Metodo que se encarga de canselar una Reserva.
	 * El HashMap parametros contiene los siguientes key's:
	 * ---------------------------------------------------
	 * 					KEYS DE PARAMETROS
	 * ---------------------------------------------------
	 * --institucion --> Requerido
	 * --motivocancelacion --> Requerido
	 * --codigoreserva --> Requerido
	 * --codigocama --> Requerido
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public static boolean cancelarReserva (Connection connection,HashMap parametros)
	{
		return consultarActivarCamasReservadasDao().cancelarReserva(connection, parametros);
	}
	
	/**
	 * Metodo que se encarga de ingresar la informacion a la BD
	 * El Hasmap Parametros contiene los siguientes Key's:
	 * --------------------------------------------------------
	 * 					KEY'S DE PARAMETROS
	 * --------------------------------------------------------
	 * --codigopaciente --> Requerido
	 * --codigocentroatencion --> Requerido
	 * --codigocentrocosto --> Requerido
	 * --codigopiso --> Requerido
	 * --codigohabitacion --> Requerido
	 * --codigocama --> Requerido
	 * --codigoreservacancelada --> Requerido
	 * --codigousuariocancela --> Requerido
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean logActivacionCamas(Connection connection, HashMap parametros)
	{
		return consultarActivarCamasReservadasDao().logActivacionCamas(connection, parametros);
	}
	
	
	/*----------------------------------------------------
	 * FIN ATRIBUTOS DE CONSULTAR ACTIVAR CAMAS RESERVADAS
	 ----------------------------------------------------*/
	
}