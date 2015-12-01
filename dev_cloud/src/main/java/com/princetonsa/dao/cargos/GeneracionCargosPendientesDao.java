/*
 * @(#)GeneracionCargosPendientesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 
 *
 */
package com.princetonsa.dao.cargos;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 *  Interfaz para el acceder a la fuente de datos de 
 *  la generaci�n de cargos PENDIENTES
 *
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */

public interface GeneracionCargosPendientesDao 
{
	/**
	 * Carga el listado de solicitudes pendientes x cuenta y responsable 
	 * @param con
	 * @param codigoCuenta
	 * @return 
	 */
	public HashMap obtenerSolicitudesCargoPendiente(Connection con, Vector codigosCuenta, Vector subCuentas);
	
	/**
	 * Carga los datos del detalle de una solicitud de valoraci�n inicial de urgencias o valoraci�n inicial de hospitalizaci�n
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarValoracionPendiente(Connection con, int numeroSolicitud);
	
	/**
	 * Modifica el tipo de recargo de una valoraci�n inicial de hospitalizaci�n o urgencias
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param numero de solicitud
	 * @param tipo de recargo
	 * @return 
	 */
	public boolean modificarTipoRecargo(	Connection con, 
											int numeroSolicitud,
											int tipoRecargo	); 
	
	/**
	 * 
	 * Carga los datos del detalle de una solicitud de procedimientos - interconsulta - evolucion cobrable - cargos directos, 
	 *  
	 * @param con
	 * @param numeroSolicitud
	 * @param tipoSolicitud, (interconsulta - procedimiento)
	 * @return
	 */
	public HashMap cargarSolicitudesInterProcEvolCargosDirectos(	Connection con, 
																	int numeroSolicitud, 
																	int tipoSolicitud,
																	String esPortatil);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap obtenerSolicitudMedicamentosPendientesXResponsable( Connection con, int numeroSolicitud, double subCuenta);
}
