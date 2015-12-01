package com.princetonsa.dao.postgresql.cargos;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.cargos.GeneracionCargosPendientesDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseGeneracionCargosPendientesDao;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlGeneracionCargosPendientesDao implements GeneracionCargosPendientesDao 
{
	/**
	 * carga los datos del detalle de la solicitud de procedimiento 
	 */
	private final static String detalleSolProcedimientoPORTATILStr=			"SELECT  " +
																		"s.numero_solicitud AS numerosolicitud " +
																		",to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud " +
																		", getnomtiposolicitud(s.tipo) AS nombretiposolicitud  " +
																		", s.tipo as codigotiposolicitud " +
																		", case when temprec.tipo_recargo is null then '"+ConstantesBD.codigoTipoRecargoSinRecargo+"' else temprec.tipo_recargo||'' end  AS codigotiporecargo " +
																		", case when temprec.tipo_recargo is null then getnombretiporecargo('"+ConstantesBD.codigoTipoRecargoSinRecargo+"') else getnombretiporecargo(temprec.tipo_recargo)  end  AS nombretiporecargo " +
																		//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion" +
																		", (ser.especialidad ||'-'|| sp.portatil_asociado) AS codigoaxioma " +
																		", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
																	" FROM solicitudes s " +
																		" INNER JOIN sol_procedimientos sp ON (sp.numero_solicitud=s.numero_solicitud) " +
																		" INNER JOIN servicios ser ON (sp.codigo_servicio_solicitado=ser.codigo) " +
																		" LEFT OUTER JOIN ( select rsp1.numero_solicitud as numero_solicitud,rsp1.tipo_recargo as tipo_recargo FROM res_sol_proc rsp1 WHERE rsp1.numero_solicitud=? order by rsp1.fecha_grabacion desc, rsp1.hora_grabacion desc "+ValoresPorDefecto.getValorLimit1()+" 1 ) temprec on(temprec.numero_solicitud=s.numero_solicitud) " +
																	" WHERE " +
																		" s.numero_solicitud=?";
	
	/**
	 * Carga el listado de solicitudes pendientes x cuenta y responsable 
	 * @param con
	 * @param codigoCuenta
	 * @return 
	 */
	public HashMap obtenerSolicitudesCargoPendiente(Connection con, Vector codigosCuenta, Vector subCuentas)
	{
		return SqlBaseGeneracionCargosPendientesDao.obtenerSolicitudesCargoPendiente(con, codigosCuenta, subCuentas);
	}
	
	/**
	 * Carga los datos del detalle de una solicitud de valoración inicial de urgencias o valoración inicial de hospitalización
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarValoracionPendiente(Connection con, int numeroSolicitud)
	{
		return SqlBaseGeneracionCargosPendientesDao.cargarValoracionPendiente(con, numeroSolicitud);
	}
	
	/**
	 * Modifica el tipo de recargo de una valoración inicial de hospitalización o urgencias
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numero de solicitud
	 * @param tipo de recargo
	 * @return 
	 */
	public boolean modificarTipoRecargo(	Connection con, 
											int numeroSolicitud,
											int tipoRecargo	)
	{
		return SqlBaseGeneracionCargosPendientesDao.modificarTipoRecargo(con, numeroSolicitud, tipoRecargo);
	}
	
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
																	String esPortatil)
	{
		return SqlBaseGeneracionCargosPendientesDao.cargarSolicitudesInterProcEvolCargosDirectos(con, numeroSolicitud, tipoSolicitud, esPortatil, detalleSolProcedimientoPORTATILStr);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap obtenerSolicitudMedicamentosPendientesXResponsable( Connection con, int numeroSolicitud, double subCuenta)
	{
		return SqlBaseGeneracionCargosPendientesDao.obtenerSolicitudMedicamentosPendientesXResponsable(con, numeroSolicitud, subCuenta);
	}
}
