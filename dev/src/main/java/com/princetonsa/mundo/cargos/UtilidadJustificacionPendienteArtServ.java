package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.UtilidadJustificacionPendienteArtServDao;
import com.servinte.axioma.fwk.exception.IPSException;

public class UtilidadJustificacionPendienteArtServ
{
	/**
	 * Para hacer logs de debug / warn / error de la validacion e insercion justificacion Pendiente.
	 */
	private static Logger logger = Logger.getLogger(UtilidadJustificacionPendienteArtServ.class);
	
	/**
	 * 
	 * */
	private static UtilidadJustificacionPendienteArtServDao getUtilidadJustificacionPendienteArtServDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao();
	}
	
	/**
	 * 
	 */
	public static boolean validarNOPOS(Connection con, int numeroSolicitud, int codigoArticulo, boolean esArticulo, boolean esmaterialqx, int codigoConvenio) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().validarNOPOS(con, numeroSolicitud, codigoArticulo, esArticulo, esmaterialqx, codigoConvenio);
	}
	
	/**
	 * 
	 */
	public static boolean insertarJusNP(Connection con, int numeroSolicitud, int codigoArticulo, int cantidad, String usuario, boolean esArticulo, boolean actualizarCantidad, int subcuenta, String tipoJus)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().insertarJusNP(con, numeroSolicitud, codigoArticulo, cantidad, usuario, esArticulo, actualizarCantidad, subcuenta, tipoJus);
	}
	
	/**
	 * 
	 */
	public static boolean eliminarJusNoposPendiente(Connection con, int solicitud, int codigo, boolean esArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().eliminarJusNoposPendiente(con, solicitud, codigo, esArticulo);
	}
	
	/**
	 * 
	 */
	public static boolean justificacionPendienteCirugiaDYT(Connection con, int numeroSolicitud, int codigoServicio, String usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().justificacionPendienteCirugiaDYT(con, numeroSolicitud, codigoServicio, usuario);
	}
	
	/**
	 * Obtiene los responsables de una justificación pendiente
	 * 		keys del mapa
	 * 		- subcuenta
	 * 		- cantidad		
	 * @param con
	 * @param numeroSolicitud
	 * @param codigo
	 * @param esArticulo
	 * @return
	 */
	public static HashMap obtenerResponsablesJusPendiente(Connection con, int numeroSolicitud, int codigo, boolean esArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().obtenerResponsablesJusPendiente(con, numeroSolicitud, codigo, esArticulo);
	}

	/**
	 * Elimina los responsables de una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param string
	 * @param b
	 * @return
	 */
	public static double eliminarResponsablesJustificacionPendiente(Connection con, int numeroSolicitud, int codigo, boolean esArticulo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().eliminarResponsablesJustificacionPendiente(con, numeroSolicitud, codigo, esArticulo);
	}

	/**
	 * Inserta una responsable para una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param subCuenta
	 * @param cantidad
	 * @param codigoJustificacionPendiente
	 */
	public static boolean insertarResponsableJustificacionPendiente(Connection con, double codigoJustificacionPendiente, String subCuenta, String cantidad, boolean esArticulo) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().insertarResponsableJustificacionPendiente(con, codigoJustificacionPendiente, subCuenta, cantidad, esArticulo);
	}
	
	/**
	 * Consulta si existe una justificación de orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param esArticulo
	 * @return
	 */
	public static boolean existeJustificacionDeOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, boolean esArticulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().existeJustificacionDeOrdenAmbulatoria(con, ordenAmbulatoria, codigo, esArticulo);
	}
	
	/**
	 * Actualiza un numero de solicitud en una justificacion de orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	public static boolean actualizarSolicitudJusOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, int solicitud, boolean esArticulo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().actualizarSolicitudJusOrdenAmbulatoria(con, ordenAmbulatoria, codigo, solicitud, esArticulo);
	}
	
	/**
	 * Ingresar un nuevo registro de justificacion no pos segun una ya ingresada por medio de una orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	public static boolean ingresarJustificacionSegunOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, int solicitud, boolean esArticulo, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadJustificacionPendienteArtServDao().ingresarJustificacionSegunOrdenAmbulatoria(con, ordenAmbulatoria, codigo, solicitud, esArticulo, institucion);
	}
}