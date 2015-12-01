package com.princetonsa.dao.sqlbase.cargos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseGeneracionCargosPendientesDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseGeneracionCargosPendientesDao.class);
	
	/**
	 * carga los datos del detalle de la solicitud de interconsulta,  
	 */
	private final static String detalleSolInterconsultaStr=		"SELECT  " +
																	"s.numero_solicitud AS numerosolicitud " +
																	", to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud " +
																	", getnomtiposolicitud(s.tipo) AS nombretiposolicitud  " +
																	", s.tipo as codigotiposolicitud " +
																	", vc.tipo_recargo AS codigotiporecargo" +
																	", tr.nombre AS nombretiporecargo" +
																	//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion" +
																	", (ser.especialidad ||'-'|| si.codigo_servicio_solicitado) AS codigoaxioma " +
																	", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
																" FROM solicitudes s " +
																	" INNER JOIN valoraciones_consulta vc ON (vc.numero_solicitud=s.numero_solicitud) " +
																	" INNER JOIN tipos_recargo tr ON (tr.codigo= vc.tipo_recargo) " +
																	" INNER JOIN solicitudes_inter si ON (si.numero_solicitud=s.numero_solicitud) " +
																	" INNER JOIN servicios ser ON (si.codigo_servicio_solicitado=ser.codigo) " +
																" WHERE " +
																	" s.numero_solicitud=?";
	
	/**
	 * carga los datos del detalle de la solicitud de cita 
	 */
	private final static String detalleSolCitaStr = 			"SELECT "+
																	"s.numero_solicitud AS numerosolicitud " +
																	", to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud " +
																	", getnomtiposolicitud(s.tipo) AS nombretiposolicitud  " +
																	", s.tipo as codigotiposolicitud " +
																	", 0 AS codigotiporecargo" +
																	", 0 AS nombretiporecargo" +
																	//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion" +
																	", (ser.especialidad ||'-'|| sc.codigo_servicio_solicitado) AS codigoaxioma " +
																	", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
																"FROM solicitudes s "+
																	" INNER JOIN solicitudes_consulta sc ON (sc.numero_solicitud=s.numero_solicitud) " +
																	" INNER JOIN servicios ser ON (sc.codigo_servicio_solicitado=ser.codigo) " +
																"WHERE "+
																	" s.numero_solicitud=?";
	
	/**
	 * carga los datos del detalle de la solicitud de cargos directos 
	 */
	private final static String detalleSolCargosDirectosStr=		"SELECT  " +
																		"s.numero_solicitud AS numerosolicitud " +
																		", to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud " +
																		", getnomtiposolicitud(s.tipo) AS nombretiposolicitud  " +
																		", s.tipo as codigotiposolicitud " +
																		", coalesce(cd.tipo_recargo, "+ConstantesBD.codigoTipoRecargoSinRecargo+") AS codigotiporecargo" +
																		", tr.nombre AS nombretiporecargo" +
																		//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion" +
																		", (ser.especialidad ||'-'|| cd.servicio_solicitado) AS codigoaxioma " +
																		", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
																	" FROM solicitudes s " +
																		" INNER JOIN cargos_directos cd ON (cd.numero_solicitud=s.numero_solicitud) " +
																		" INNER JOIN servicios ser ON (cd.servicio_solicitado=ser.codigo) " +
																		" LEFT OUTER JOIN tipos_recargo tr ON (tr.codigo= cd.tipo_recargo) " +
																	" WHERE " +
																		" s.numero_solicitud=?";

	
	/**
	 * carga los datos del detalle de la solicitud de procedimiento 
	 */
	private final static String detalleSolProcedimientoStr=			"SELECT  " +
																		"s.numero_solicitud AS numerosolicitud " +
																		",to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud " +
																		", getnomtiposolicitud(s.tipo) AS nombretiposolicitud  " +
																		", s.tipo as codigotiposolicitud " +
																		", case when temprec.tipo_recargo is null then '"+ConstantesBD.codigoTipoRecargoSinRecargo+"' else temprec.tipo_recargo||'' end  AS codigotiporecargo " +
																		", case when temprec.tipo_recargo is null then getnombretiporecargo('"+ConstantesBD.codigoTipoRecargoSinRecargo+"') else getnombretiporecargo(temprec.tipo_recargo)  end  AS nombretiporecargo " +
																		//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion" +
																		", (ser.especialidad ||'-'|| sp.codigo_servicio_solicitado) AS codigoaxioma " +
																		", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
																	" FROM solicitudes s " +
																		" INNER JOIN sol_procedimientos sp ON (sp.numero_solicitud=s.numero_solicitud) " +
																		" INNER JOIN servicios ser ON (sp.codigo_servicio_solicitado=ser.codigo) " +
																		" LEFT OUTER JOIN ( select v.numero_solicitud as numero_solicitud, v.tipo_recargo as tipo_recargo FROM facturacion.view_recargo_solicitud v WHERE v.numero_solicitud=? "+ValoresPorDefecto.getValorLimit1()+" 1 ) temprec on(temprec.numero_solicitud=s.numero_solicitud) " +
																	" WHERE " +
																		" s.numero_solicitud=?";
	
	/**
	 * Carga los datos del detalle de la solicitud de evolución, 
	 */
	private final static String detalleSolEvolucionStr=	"SELECT " +
																" s.numero_solicitud AS numerosolicitud " +
																",to_char(s.fecha_solicitud,'DD/MM/YYYY') AS fechasolicitud" +
																", getnomtiposolicitud(s.tipo) AS nombretiposolicitud  " +
																", s.tipo as codigotiposolicitud " +
																//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion" +
																", tr.nombre AS nombretiporecargo" +
																", ev.recargo AS codigotiporecargo" +
																", ser.especialidad || '-' || getServicioEvolucionVInicial(s.numero_solicitud, " + ValoresPorDefecto.getValorTrueParaConsultas() + ") AS codigoaxioma " +
																", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
														"FROM 	" +
																"solicitudes_evolucion solev " +
																"INNER JOIN evoluciones ev ON (solev.evolucion=ev.codigo) " +
																"INNER JOIN solicitudes s ON (s.numero_solicitud=solev.numero_solicitud) " +
																"INNER JOIN tipos_recargo tr ON(tr.codigo=ev.recargo) " +
																"LEFT OUTER JOIN servicios ser ON (convertiranumero(getServicioEvolucionVInicial(s.numero_solicitud, " + ValoresPorDefecto.getValorFalseParaConsultas() + "))=ser.codigo) " +
														"WHERE solev.numero_solicitud=?";
	
	/**
	 * Carga el listado de solicitudes pendientes x cuentas y responsable 
	 * @param con
	 * @param codigosCuenta
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap obtenerSolicitudesCargoPendiente(Connection con, Vector codigosCuenta, Vector subCuentas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			mapa.put("numRegistros", "0");
			String consultaStr = 	" SELECT DISTINCT " +
											"s.numero_solicitud AS numerosolicitud, " +
											"to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud, " +
											"s.tipo AS codigotiposolicitud, " +
											"getnomtiposolicitud(s.tipo) AS nombretiposolicitud, " +
											"s.centro_costo_solicitado AS codigocentrocostosolicitado, " +
											"s.centro_costo_solicitante AS codigocentrocostosolicitante, " +
											"cc.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
											"getnomcentrocosto(s.centro_costo_solicitado) AS nombrecentrocostosolicitado, " +
											"s.estado_historia_clinica AS codigoestadomedico, " +
											"getestadosolhis(s.estado_historia_clinica) AS nombreestadomedico, " +
											"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas, " +
											"dc.convenio as codigoconvenio, " +
											"getnombreconvenio(dc.convenio) as nombreconvenio, " +
											"dc.es_portatil as esportatil " +
										"FROM " +
											"solicitudes s   " +
											"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
											"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
											"INNER JOIN cuentas c ON (c.id=s.cuenta) "+ 
											"INNER JOIN centros_costo cc ON (cc.codigo=s.centro_costo_solicitante) "+ 
										"WHERE   " +
											"dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
											"AND ssc.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(codigosCuenta, false)+") "+
											"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' " +
											"AND " +
											"(" +
												"s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCRespondida+","+
																				ConstantesBD.codigoEstadoHCInterpretada+","+
																				ConstantesBD.codigoEstadoHCAdministrada+","+
																				ConstantesBD.codigoEstadoHCCargoDirecto+") " +
												" OR ((s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCTomaDeMuestra+") AND esSolCargoSolicitud(s.numero_solicitud)>0)) "+
												" OR ((s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCEnProceso+") AND (esSolCargoSolicitud(s.numero_solicitud)>0 OR esSolCargoEnProceso(s.numero_solicitud)>0))) "+
												" OR s.tipo="+ConstantesBD.codigoTipoSolicitudCita+" "+ 
											") " +
											" AND dc.estado= "+ConstantesBD.codigoEstadoFPendiente+" " +
		                                    " AND s.tipo<> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+
		                                	" AND (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva+"" +
													" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+"" +
													" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" " +
													" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+") " +
											"	order by consecutivoordenesmedicas, dc.convenio " ;
										
			pst = con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();  
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerSolicitudesCargoPendiente",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesCargoPendiente", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return mapa;
	}

	/**
	 * Carga los datos del detalle de una solicitud de valoración inicial de urgencias o valoración inicial de hospitalización
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarValoracionPendiente(Connection con, int numeroSolicitud)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String detalleSolicitudValoracionesStr=		" SELECT " +
														" to_char(v.fecha_valoracion, 'DD/MM/YYYY') AS fechavaloracion" +
														", ts.nombre AS nombretipovaloracion" +
														", vc.tipo_recargo AS codigotiporecargo" +
														", tr.nombre AS nombretiporecargo" +
														", vc.tipo_recargo AS codigotiporecargobackup" +
														", tr.nombre AS nombretiporecargobackup" +
														//", CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END AS numeroautorizacion " +
														", s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
													" FROM " +
														" solicitudes s " +
														"INNER JOIN valoraciones v ON (v.numero_solicitud= s.numero_solicitud) " +
														"INNER JOIN tipos_solicitud ts ON (ts.codigo=s.tipo) " +
														"INNER JOIN valoraciones_consulta vc ON (vc.numero_solicitud=s.numero_solicitud) " +
														"INNER JOIN tipos_recargo tr ON (tr.codigo= vc.tipo_recargo) " +
													" WHERE " +
														"s.numero_solicitud=? ";
		
		try
		{
			PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolicitudValoracionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarDetalle.setInt(1, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle : SqlBaseGeneracionCargosPendientesDao "+e.toString());
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	
	/**
	 * Modifica el tipo de recargo de una valoración inicial de hospitalización o urgencias
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numero de solicitud
	 * @param tipo de recargo
	 * @return 
	 */
	public static boolean modificarTipoRecargo(	Connection con, 
												int numeroSolicitud,
												int tipoRecargo	) 
	{
		String modificarTipoRecargoStr= "UPDATE valoraciones_consulta SET tipo_recargo=? WHERE numero_solicitud=? ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTipoRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipoRecargo);
			ps.setInt(2, numeroSolicitud);
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseGeneracionCargosPendientesDao "+e.toString());			
		}	
		return false;
	}

	/**
	 * 
	 * Carga los datos del detalle de una solicitud de procedimientos - interconsulta - evolucion cobrable - cargos directos, 
	 * en la vista se carga su servicio correspondiente 
	 * y los errores de generacion del cargo mediante los tags
	 *  
	 * @param con
	 * @param numeroSolicitud
	 * @param detalleSolProcedimientoPORTATILStr Sentencia SQL
	 * @param tipoSolicitud, (interconsulta - procedimiento)
	 * @return
	 */
	public static HashMap cargarSolicitudesInterProcEvolCargosDirectos(	Connection con, 
																		int numeroSolicitud, 
																		int tipoSolicitud,
																		String esPortatil,
																		String detalleSolProcedimientoPORTATILStr)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
			{	
				PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolInterconsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDetalle.setInt(1, numeroSolicitud);
				logger.info("detalleSolInterconsultaStr-->"+detalleSolInterconsultaStr+" numeroSol->"+numeroSolicitud);
				mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
			}
			else if (tipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
			{
				PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDetalle.setInt(1, numeroSolicitud);
				logger.info("detalleSolCitaStr-->"+detalleSolCitaStr+" -->numersoSol->"+numeroSolicitud);
				mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
			}
			else if (tipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento && !UtilidadTexto.getBoolean(esPortatil))
			{	
				PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDetalle.setInt(1, numeroSolicitud);
				cargarDetalle.setInt(2, numeroSolicitud);
				logger.info("detalleSolProcedimientoStr-->"+detalleSolProcedimientoStr+" -->numersoSol->"+numeroSolicitud);
				mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
			}
			else if (tipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento && UtilidadTexto.getBoolean(esPortatil))
			{	
				PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolProcedimientoPORTATILStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDetalle.setInt(1, numeroSolicitud);
				cargarDetalle.setInt(2, numeroSolicitud);
				logger.info("detalleSolProcedimientoPORTATILStr-->"+detalleSolProcedimientoPORTATILStr+" -->numersoSol->"+numeroSolicitud);
				mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
			}
			else if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudEvolucion)
			{
				PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDetalle.setInt(1, Utilidades.convertirAEntero(numeroSolicitud+""));
				logger.info("detalleSolEvolucionStr-->"+detalleSolEvolucionStr+" -->numersoSol->"+numeroSolicitud);
				mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
			}
			else if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios
					||tipoSolicitud==ConstantesBD.codigoTipoSolicitudEstancia)
			{
			    PreparedStatementDecorator cargarDetalle= new PreparedStatementDecorator(con.prepareStatement(detalleSolCargosDirectosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarDetalle.setInt(1, numeroSolicitud);
				logger.info("detalleSolCargosDirectosStr-->"+detalleSolCargosDirectosStr+" -->numersoSol->"+numeroSolicitud);
				mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarDetalle.executeQuery()), false, false);
			}
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle : SqlBaseGeneracionCargosPendientesDao "+e.toString());
			e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta
	 * @return
	 */
	public static HashMap obtenerSolicitudMedicamentosPendientesXResponsable( Connection con, int numeroSolicitud, double subCuenta)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String consulta= " SELECT " +
							" a.codigo AS codigo, " +
							" a.descripcion AS descripcion, " +
							" CASE WHEN getconcentracionarticulo(a.codigo) IS NULL THEN '' ELSE getconcentracionarticulo(a.codigo)||'' END AS concentracion, " +
							" CASE WHEN getformafarmaceuticaarticulo(a.codigo) IS NULL THEN '' ELSE getformafarmaceuticaarticulo(a.codigo)||'' END AS formafarmaceutica, " +
							" CASE WHEN getunidadmedidaarticulo(a.codigo) IS NULL THEN '' ELSE getunidadmedidaarticulo(a.codigo)||'' END AS unidadmedida, " +
							" CASE WHEN ds.dosis IS NULL THEN '' ELSE  ds.dosis||'' END AS dosis, " +
							" CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE getumxunidosisarticulo(ds.unidosis_articulo) END as unidosis, " +
							" CASE WHEN ds.frecuencia IS NULL THEN '' ELSE ds.frecuencia||'' END AS frecuencia, " +
							" CASE WHEN ds.tipo_frecuencia IS NULL THEN '' ELSE ds.tipo_frecuencia||'' END as tipofrecuencia, " +
							" CASE WHEN ds.via IS NULL THEN '' ELSE ds.via||'' END AS via, " +
							" coalesce(ds.observaciones, '') AS observaciones, " +
							" getDespacho(a.codigo, ds.numero_solicitud) as unidadesdespachadas, " +
							" CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END AS unidadesconsumidasxfarmacia, " +
							" a.naturaleza AS naturaleza, " +
							" edc.error as errorcargo " +
						"FROM " +
								"solicitudes s " +
								"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud) " +
								"left outer join detalle_solicitudes ds on (dc.solicitud=ds.numero_solicitud and ds.articulo=dc.articulo) " +
								"inner join articulo a on (dc.articulo=a.codigo) " +
								"left outer join errores_det_cargos edc ON (edc.codigo_detalle_cargo=dc.codigo_detalle_cargo) " +
						"WHERE " +
								"dc.solicitud=? " +
								"AND dc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.sub_cuenta=? " +
								"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
								"AND " +
								"(" +
									"s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCAdministrada+","+
																	ConstantesBD.codigoEstadoHCCargoDirecto+") " +
								") " +
								" AND s.tipo in (" +ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") "+
								" AND dc.estado= "+ConstantesBD.codigoEstadoFPendiente+" " +
								" AND dc.facturado= '"+ConstantesBD.acronimoNo+"' "+
						" ORDER BY codigo ";
								
		logger.info("obtenerSolicitudMedicamentosPendientesXResponsable-->"+consulta+" subCuenta->"+subCuenta+" numeroSol->"+numeroSolicitud);
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setLong(2, (long)subCuenta);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en obtenerTotalAdminFarmaciaXResponsable : SqlBaseCargoDao "+e.toString() );
			e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa; 
	}
	
}
