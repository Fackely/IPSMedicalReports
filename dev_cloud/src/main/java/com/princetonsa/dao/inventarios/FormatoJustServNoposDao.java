package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * Interfaz de justificacion de servicios no pos
 * @author axioma
 *
 */
public interface FormatoJustServNoposDao
{
	/**
	 * 
	 * @param con
	 * @param formatoJust
	 * @param paciente
	 */
	public HashMap consultar(Connection con, FormatoJustServNopos formatoJust, PersonaBasica paciente, UsuarioBasico usuario, boolean nueva);
	
	/**
	 * 
	 * @param con
	 * @param numJustificacion
	 * @return
	 */
	public HashMap consultarDiagnosticos(Connection con, int numJustificacion);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @return
	 */
	public boolean ingresarResponsable(Connection con, String numeroSolicitud, int subcuenta, int cantidad);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public boolean existeJustificacion(Connection con, String numeroSolicitud, String servicio);
	
	/**
	 * Metodo para ingresar una justificación No Pos de Servicios.
	 * Los campos solicitud y ordenAmbulatoria son excluyentes!
	 * @param con
	 * @param institucion
	 * @param usuarioModifica
	 * @param justificacion
	 * @param solicitud
	 * @param ordenAmbulatoria
	 * @param servicio
	 * @param profesional
	 * @return
	 */
	public String ingresarJustificacion(Connection con, int institucion, String usuarioModifica, HashMap justificacion, int solicitud, int ordenAmbulatoria, int servicio, int profesional);
	
	/**
	 * 
	 * @param con
	 * @param mapa justificacion
	 * @param solicitud
	 * @return
	 */
	public String actualizarJustificacion(Connection con, int institucion, String usuarioModifica, HashMap justificacion, int solicitud, int servicio);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<String, Object> subCuentas (Connection con, int numeroSolicitud);
	
	/**
	 * Metodo para revisar existencias de justificaciones y modificarlas segun distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String servicio, String usuario);
	
	/**
	 * Metodo para validar Convenio y Articulo No Pos
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public boolean validarArtConvJustificacion(Connection con, String servicio, String subCuenta);

	/**
	 * Metodo para consultar una justificacion historica
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public HashMap<String, Object> consultarJustificacionHistorica(Connection con, String numeroSolicitud, String servicio);
}
