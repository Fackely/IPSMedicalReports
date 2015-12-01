package com.princetonsa.dao.cargos;

import java.sql.Connection;
import java.util.HashMap;

import com.servinte.axioma.fwk.exception.BDException;

public interface UtilidadJustificacionPendienteArtServDao
{
	/**
	 * 
	 */
	public boolean validarNOPOS(Connection con, int numeroSolicitud, int codigoArticulo, boolean esArticulo, boolean esmaterialqx, int codigoConvenio) throws BDException;
	
	/**
	 * 
	 */
	public boolean insertarJusNP(Connection con, int numeroSolicitud, int codigoArticulo, int cantidad, String usuario, boolean esArticulo, boolean actualizarCantidad, int subcuenta, String tipoJust);
	
	/**
	 * 
	 */
	public boolean justificacionPendienteCirugiaDYT(Connection con, int numeroSolicitud, int codigoServicio, String usuario);
	
	/**
	 * 
	 */
	public boolean eliminarJusNoposPendiente(Connection con, int solicitud, int codigo, boolean esArticulo);
	
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
	public HashMap obtenerResponsablesJusPendiente(Connection con, int solicitud, int codigo, boolean esArticulo);

	/**
	 * Elimina los responsables de una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param string
	 * @param b
	 * @return
	 */
	public double eliminarResponsablesJustificacionPendiente(Connection con, int numeroSolicitud, int codigo, boolean esArticulo) throws BDException;
	
	/**
	 * Inserta una responsable para una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param subCuenta
	 * @param cantidad
	 * @param codigoJustificacionPendiente
	 */
	public boolean insertarResponsableJustificacionPendiente(Connection con, double codigoJustificacionPendiente, String subCuenta, String cantidad, boolean esArticulo) throws BDException;

	/**
	 * Consulta si existe una justificación de orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param esArticulo
	 * @return
	 */
	public boolean existeJustificacionDeOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, boolean esArticulo);

	/**
	 * Actualiza un numero de solicitud en una justificacion de orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	public boolean actualizarSolicitudJusOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, int solicitud, boolean esArticulo); 
	
	/**
	 * Ingresar un nuevo registro de justificacion no pos segun una ya ingresada por medio de una orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	public boolean ingresarJustificacionSegunOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, int solicitud, boolean esArticulo, int institucion); 
}