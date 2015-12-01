package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultarActivarCamasReservadasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultarActivarCamasReservadasDao;



/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class PostgresqlConsultarActivarCamasReservadasDao implements ConsultarActivarCamasReservadasDao
{
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
	public HashMap consultarCamasReservadas (Connection connection,HashMap parametros)
	{
		return SqlBaseConsultarActivarCamasReservadasDao.consultarCamasReservadas(connection, parametros);
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
	public HashMap consultaDatosReserva (Connection connection,HashMap parametros)
	{
		return SqlBaseConsultarActivarCamasReservadasDao.consultaDatosReserva(connection, parametros);
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
	 * tipohabitacion, nombrecentroatencioncama.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 */
	public HashMap consultaDatoscama (Connection connection,HashMap parametros)
	{
		return SqlBaseConsultarActivarCamasReservadasDao.consultaDatoscama(connection, parametros);
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
	public  boolean cancelarReserva (Connection connection,HashMap parametros)
	{
		return SqlBaseConsultarActivarCamasReservadasDao.cancelarReserva(connection, parametros);
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
	public boolean logActivacionCamas(Connection connection, HashMap parametros)
	{
		return SqlBaseConsultarActivarCamasReservadasDao.logActivacionCamas(connection, parametros);
	}
}
