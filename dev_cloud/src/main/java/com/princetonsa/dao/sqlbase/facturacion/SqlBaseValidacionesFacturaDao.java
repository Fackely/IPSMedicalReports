package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseValidacionesFacturaDao 
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseValidacionesFacturaDao.class);
	
	/**
	 * Implementacion del metodo para buscar el ultimo
	 * consecutico de dactura utilizado dentro de la empresainstitucion
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public static double obtenerSiguientePosibleNumeroFacturaMultiempresa(Connection con, double empresaInstitucion)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
	    try 
	    {
	    	String consulta="SELECT getSigNumFacturaMultiEmp(?) as maxConsecutivo";
	    	pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	    	pst.setDouble(1, empresaInstitucion);
	    	rs=pst.executeQuery();
	    	
		    if (rs.next()){
		        return rs.getInt("maxConsecutivo");
		    }
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerSiguientePosibleNumeroFacturaMultiempresa",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSiguientePosibleNumeroFacturaMultiempresa", e);
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
	    return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionDerechosConvenioIngreso( Connection con, String idIngreso, int codigoConvenio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			String consulta="SELECT " +
				"vd.sub_cuenta " +
			"FROM " +
				"verificaciones_derechos vd " +
			"WHERE " +
				"vd.ingreso=? " +
				"and vd.convenio=? " +
				"and vd.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoRechazado+"', '"+ConstantesIntegridadDominio.acronimoEstadoAceptado+"')";

			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(idIngreso));
			pst.setInt(2, codigoConvenio);
			rs=pst.executeQuery();
		    if (rs.next()){
		        return true;
		    }
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeVerificacionDerechosConvenioIngreso",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeVerificacionDerechosConvenioIngreso", e);
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
	    return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param facturado
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector obtenerContratosSubCuentas(Connection con, String idIngreso, Vector subCuentas, String facturado)
	{
		Vector contratos= new Vector();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try 
	    {
			String consulta="SELECT " +
						"sc.contrato as contrato " +
					"FROM " +
						"sub_cuentas sc " +
					"WHERE " +
						"sc.ingreso=? " +
						"and sc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") ";
			
			if(!UtilidadTexto.isEmpty(facturado)){
				consulta+="and sc.facturado = '"+facturado+"' ";
			}
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(idIngreso));
			rs=pst.executeQuery();
			while (rs.next()){
		        contratos.add(rs.getInt("contrato")+"");
			}
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerContratosSubCuentas",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerContratosSubCuentas", e);
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
	    return contratos;
	}
	
	/**
	 * metodo que obtiene el listado de solicitudes de interconsulta - procedimientos - evoluciones que tienen asociado
	 * un servicio que requiere interpretacion 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @param tipoFacturacion 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSolicitudesHCInvalidas( Connection con, Vector cuentas, Vector subCuentas, boolean validarInterpretadas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=" SELECT " +
								"s.numero_solicitud AS numerosolicitud, " +
								"s.tipo AS codigotiposolicitud, " +
								"getnomtiposolicitud(s.tipo) AS nombretiposolicitud, " +
								"s.estado_historia_clinica AS codigoestadomedico, " +
								"getestadosolhis(s.estado_historia_clinica) AS nombreestadomedico, " +
								"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas, " +
								"dc.convenio as codigoconvenio, " +
								"getnombreconvenio(dc.convenio) as nombreconvenio " +
							"FROM " +
								"solicitudes s   " +
								"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
								"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
								"INNER JOIN cuentas c ON (c.id=s.cuenta) "+
								"INNER JOIN servicios ser ON (ser.codigo=dc.servicio) " +
							"WHERE   " +
								"dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
								"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND ssc.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
								"AND s.estado_historia_clinica <>"+ConstantesBD.codigoEstadoHCInterpretada+" "+
								"AND s.tipo in("+ConstantesBD.codigoTipoSolicitudInterconsulta+","+
						        				ConstantesBD.codigoTipoSolicitudProcedimiento+", " +
						        				ConstantesBD.codigoTipoSolicitudEvolucion+") "+
						    	"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") ";
			
			consulta+=validarInterpretadas?"AND ser.requiere_interpretacion= '"+ConstantesBD.acronimoSi+"' ":" ";
			consulta+="	order by s.consecutivo_ordenes_medicas, dc.convenio " ;
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
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
			logger.error("############## SQLException ERROR obtenerSolicitudesHCInvalidas",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesHCInvalidas", e);
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
	 * 
	 * @param con
	 * @param cuentas
	 * @param convenios
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap obtenerSolicitudesHCInvalidasMedicamentos( Connection con, Vector cuentas, Vector subCuentas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String	consulta= " SELECT " +
					"s.numero_solicitud AS numerosolicitud, " +
					"s.tipo AS codigotiposolicitud, " +
					"getnomtiposolicitud(s.tipo) AS nombretiposolicitud, " +
					"s.estado_historia_clinica AS codigoestadomedico, " +
					"getestadosolhis(s.estado_historia_clinica) AS nombreestadomedico, " +
					"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas, " +
					"dc.convenio as codigoconvenio, " +
					"getnombreconvenio(dc.convenio) as nombreconvenio " +
				"FROM " +
					"solicitudes s   " +
					"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
					"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
					"INNER JOIN cuentas c ON (c.id=s.cuenta) "+
					"INNER JOIN articulo art ON (art.codigo=dc.articulo) " +
				"WHERE   " +
					"dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
					"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
					"AND ssc.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
					"AND s.estado_historia_clinica <>"+ConstantesBD.codigoEstadoHCAdministrada+" "+
					//SE TIENEN EN CUENTA LOS CARGOS DE MATERIALES ESPECIALES, POR ESA RAZON SE COLOCA EL TIPO DE SOLICITUD CIRUGIA
					"AND s.tipo in("+ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+", "+ConstantesBD.codigoTipoSolicitudCirugia+") " +
					"AND dc.articulo IS NOT NULL "+
			    	"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
					"	order by s.consecutivo_ordenes_medicas, dc.convenio " ;
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
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
			rs.close();
			mapa.put("numRegistros", cont+"");
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerSolicitudesHCInvalidasMedicamentos",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesHCInvalidasMedicamentos", e);
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
	 * metodo apra obtener las solicitudes q no tienen pool
	 * @param con
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap obtenerSolicitudesSinPool(Connection con, Vector cuentas, Vector subCuentas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"SELECT " +
									"administracion.getnombremedico(h.medico) as nombremedico, " +
									"sol.consecutivo_ordenes_medicas as numerosolicitud, " +
									"getnomtiposolicitud(sol.tipo) as nombretiposolicitud " +
								"from " +
									"solicitudes sol " +
									"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
									"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
									"INNER JOIN sol_cirugia_por_servicio cir ON(cir.numero_solicitud=sol.numero_solicitud) " +
									"INNER JOIN det_cx_honorarios h ON(h.cod_sol_cx_servicio=cir.codigo) " +
								"WHERE " +
									"sol.cuenta in (" +UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
									"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
									"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
									"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
									"AND h.pool IS NULL " +
									"AND sol.tipo="+ConstantesBD.codigoTipoSolicitudCirugia +" " +
									"and sol.estado_historia_clinica not in("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCAnulada+") " +
									"and sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
									"and dc.articulo is null " +//para no tener encuenta los medicamentos especiales de cirugia
									//xplanner 49989
									"AND h.cobrable='"+ConstantesBD.acronimoSi+"' "+
									
								"UNION " +
								"SELECT " +
										"administracion.getnombremedico(sol.codigo_medico_responde) as nombremedico, " +
										"sol.consecutivo_ordenes_medicas as numerosolicitud, " +
										"getnomtiposolicitud(sol.tipo) as nombretiposolicitud " +
								"from " +
										"solicitudes sol " +
										"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
										"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
								"WHERE " +
										"sol.cuenta in (" +UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
										"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
										"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
										"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
										"and sol.tipo NOT IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+","+ConstantesBD.codigoTipoSolicitudCirugia+", "+ConstantesBD.codigoTipoSolicitudPaquetes+") " +
										"and sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
										"and sol.estado_historia_clinica not in("/*Comentado x tarea 138395 +ConstantesBD.codigoEstadoHCSolicitada+", "*/+ConstantesBD.codigoEstadoHCAnulada+","+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") " +
										"and sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
										
										"AND sol.pool IS NULL " +
										//Se agrega por tarea 143743
										"AND sol.codigo_medico_responde IS NOT NULL "+
									//parte de laboratorios	
									"UNION " +
										"SELECT " +
												"administracion.getnombremedico(sol.codigo_medico_responde) as nombremedico, " +
												"sol.consecutivo_ordenes_medicas as numerosolicitud, " +
												"getnomtiposolicitud(sol.tipo) as nombretiposolicitud " +
										"from " +
												"solicitudes sol " +
												"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
												"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
												"INNER JOIN cuentas c ON(c.id=sol.cuenta) " +
										"WHERE " +
												"sol.cuenta in (" +UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
												"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
												"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
												"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
												"and sol.tipo NOT IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+","+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudCita+", "+ConstantesBD.codigoTipoSolicitudPaquetes+") " +
												"and sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
												"and sol.estado_historia_clinica in("+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") " +
												"and sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
												"and (" +
														"(" +
															"sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" " +
															"and esSolCargoSolicitud(sol.numero_solicitud)<=0" +
														") " +
														"or " +
														"( " +
															"sol.estado_historia_clinica in ("+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") " +
															"and (esSolCargoSolicitud(sol.numero_solicitud)<=0 and esSolCargoEnProceso(sol.numero_solicitud)<=0)" +
														")" +
												") " +
												//xplanner--> Debe permitirse facturar cuenta de consulta externa con citas de procedimientos [id=39900]
												"and ( c.via_ingreso<>"+ConstantesBD.codigoViaIngresoConsultaExterna+" and sol.tipo<>"+ConstantesBD.codigoTipoSolicitudProcedimiento+" )"+
												
												"AND sol.pool IS NULL ";
			
	    	pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
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
			logger.error("############## SQLException ERROR obtenerSolicitudesSinPool",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesSinPool", e);
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
	 * Mï¿½todo que busca las solicitudes cuyos mï¿½dicos no esten en ningï¿½n pool 
	 * @param con
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap obtenerSolicitudesSinMedicoEnPool (Connection con, Vector cuentas, Vector subCuentas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"SELECT " +
					"administracion.getnombremedico(codigoMedico) as nombreMedico, " +
					"consecutivo_ordenes_medicas AS numeroSolicitud " +
				"FROM ( " +
						"SELECT " +
							"CASE WHEN sol.codigo_medico_responde IS NULL THEN (SELECT distinct(a.codigo_medico) FROM cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) WHERE numero_solicitud=sol.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1) ELSE sol.codigo_medico_responde END as codigoMedico," +
							"sol.numero_solicitud as numero_solicitud, " +
							"sol.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
							"sol.tipo as tipo, " +
							"sol.centro_costo_solicitado as centro_costo_solicitado, " +
							"sol.estado_historia_clinica as estado_historia_clinica " +
						"from " +
							"solicitudes sol " +
							"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
							"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
						"WHERE " +
							"sol.cuenta in (" +UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
							"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
							"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
							"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
							"and sol.tipo NOT IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+","+ConstantesBD.codigoTipoSolicitudCirugia+", "+ConstantesBD.codigoTipoSolicitudPaquetes+")" +
							"and sol.estado_historia_clinica  not in ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") "+
							//parte de las validaciones para laboratorios, procedimientos
						"UNION "+
						"SELECT " +
							"CASE WHEN sol.codigo_medico_responde IS NULL THEN (SELECT distinct(a.codigo_medico) FROM cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) WHERE numero_solicitud=sol.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1) ELSE sol.codigo_medico_responde END as codigoMedico," +
							"sol.numero_solicitud as numero_solicitud, " +
							"sol.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
							"sol.tipo as tipo, " +
							"sol.centro_costo_solicitado as centro_costo_solicitado, " +
							"sol.estado_historia_clinica as estado_historia_clinica " +
						"from " +
							"solicitudes sol " +
							"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
							"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
						"WHERE " +
							"sol.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
							"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
							"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
							"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
							"and sol.tipo not IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+","+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudCita+","+ConstantesBD.codigoTipoSolicitudPaquetes+") " +
							"and sol.estado_historia_clinica  in ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") "+		
							"and (" +
									"(" +
										"sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" " +
										"and esSolCargoSolicitud(sol.numero_solicitud)<=0" +
									") " +
									"or " +
									"( " +
										"sol.estado_historia_clinica in ("+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") " +
										"and (esSolCargoSolicitud(sol.numero_solicitud)<=0 and esSolCargoEnProceso(sol.numero_solicitud)<=0)" +
									")" +
								")"+
						"UNION " +
						//parte de cirugias
						"SELECT " +
							"h.medico as codigoMedico, " +
							"sol.numero_solicitud as numero_solicitud, " +
							"sol.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
							"sol.tipo as tipo, " +
							"sol.centro_costo_solicitado as centro_costo_solicitado, " +
							"sol.estado_historia_clinica as estado_historia_clinica " +
						"from " +
							"solicitudes sol " +
							"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
							"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
							"INNER JOIN sol_cirugia_por_servicio cir ON(cir.numero_solicitud=sol.numero_solicitud) " +
							"INNER JOIN det_cx_honorarios h ON(h.cod_sol_cx_servicio=cir.codigo) " +
						"where " +
							"sol.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
							"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
							"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
							"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
							"and dc.articulo is null " +//para no tener cncuenta los materiales especiales
					  ") tempo " +
			      "WHERE " +
			      "tempo.tipo NOT IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+") " +
			      "and tempo.centro_costo_solicitado<>" + ConstantesBD.codigoCentroCostoExternos + " " +
			      "and esSolicitudTotalEstado(numero_solicitud,"+ConstantesBD.codigoEstadoFInactiva+")<> '"+ConstantesBD.acronimoNo+"' " +
			      "and tempo.estado_historia_clinica not in("+ConstantesBD.codigoEstadoHCAnulada+") " +
			      "and (" +
			      		"SELECT count(1) " +
			      			"from participaciones_pooles pp " +
			      			"INNER JOIN pooles pool ON (pp.pool=pool.codigo) " +
			      		"where " +
			      			"pp.medico=tempo.codigoMedico " +
			      			"and pool.activo=1 " +
			      			"and " +
			      			"(" +
			      				"pp.fecha_ingreso<CURRENT_DATE or (pp.fecha_ingreso=CURRENT_DATE and pp.hora_ingreso<="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") " +
			      			") " +
			      			"and (pp.fecha_retiro IS NULL or pp.fecha_retiro>CURRENT_DATE)" +
			      ")<1";
			
	    	pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
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
			logger.error("############## SQLException ERROR obtenerSolicitudesSinMedicoEnPool",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesSinMedicoEnPool", e);
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
	 * Mï¿½todo que busca las solicitudes de cirugï¿½a las cuales no tienen pool asignado
     * @param con Conexiï¿½n con la fuente de datos
	 * @param cuentas 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap obtenerSolicitudesCxSinPool(Connection con, Vector cuentas, Vector subCuentas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT DISTINCT " +
					"sol.numero_solicitud AS numerosolicitud, " +
					"sol.consecutivo_ordenes_medicas AS orden, " +
					"scs.servicio AS servicio, " +
					"getNombreServicio(scs.servicio, 0) AS nombreservicio, " +
					"ta.codigo_asocio || ' - ' || ta.nombre_asocio   AS asocio " +
				"FROM " +
					"solicitudes sol " +
					"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
					"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
					"INNER JOIN sol_cirugia_por_servicio scs on(scs.numero_solicitud=sol.numero_solicitud) " +
					"INNER JOIN det_cx_honorarios dch ON(dch.cod_sol_cx_servicio=scs.codigo) " +
					"INNER JOIN tipos_asocio ta ON(ta.codigo=dch.tipo_asocio) " +
				"WHERE "+
					"sol.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
					"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
					"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
					"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
					"AND dch.pool IS NULL " +
					"AND dc.articulo IS NULL " +//para no tener encuenta los materiales especiales
					"AND sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
					//xplanner 49989
					"AND dch.cobrable='"+ConstantesBD.acronimoSi+"'";
	    	pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
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
			logger.error("############## SQLException ERROR obtenerSolicitudesCxSinPool",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesCxSinPool", e);
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
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param facturado
	 * @return
	 */
	public static InfoDatosInt obtenerNaturalezaPacienteSubCuenta(Connection con, String idIngreso, double subCuenta, String facturado)
	{
		InfoDatosInt nat= new InfoDatosInt();
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			nat.setCodigo(ConstantesBD.codigoNuncaValido);
		    nat.setNombre("");
		    String consulta="SELECT " +
								"coalesce(sc.naturaleza_paciente,"+ConstantesBD.codigoNuncaValido+") as naturaleza, " +
								"coalesce(getnomnatpacientes(sc.naturaleza_paciente), '') as nombrenat " +
							"FROM " +
								"sub_cuentas sc " +
							"WHERE " +
								"sc.ingreso=? " +
								"and sc.sub_cuenta ="+(long)subCuenta+" ";
			
		    if(!UtilidadTexto.isEmpty(facturado)){
		    	consulta+="and sc.facturado = '"+facturado+"' ";
		    }
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(idIngreso));
			rs=pst.executeQuery();
			if (rs.next()){	
				nat.setCodigo(rs.getInt("naturaleza"));
				nat.setNombre((rs.getString("nombrenat")!=null)?rs.getString("nombrenat"):"");
			}	
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerNaturalezaPacienteSubCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerNaturalezaPacienteSubCuenta", e);
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
	    return nat;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static DtoVerficacionDerechos obtenerValorCuotaVerificacion(Connection con, String idIngreso, int codigoConvenio)
	{
		DtoVerficacionDerechos dto=new DtoVerficacionDerechos();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			String consulta="select coalesce(cuota_verificacion, "+ConstantesBD.codigoNuncaValido+") as cuota,coalesce(porcentaje_cobertura, "+ConstantesBD.codigoNuncaValido+") as porcentaje FROM verificaciones_derechos WHERE ingreso=? and convenio=?";
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(idIngreso));
			pst.setInt(2, codigoConvenio);
			rs=pst.executeQuery();
			if (rs.next())
			{	
				
				dto.setValor(rs.getDouble("cuota"));
				dto.setPorcetaje(rs.getDouble("porcentaje"));
			}    
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerValorCuotaVerificacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerValorCuotaVerificacion", e);
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
	    return dto;
	}
	
	/**
	 * verificar si el codigo de una factura ya se encuentra
	 * insertado.
	 * @param con Connection, conexion con la fuente de datos
	 * @param codigoFact int, codigo de la factura.
	 * @return int, 0 si no existe
	 * @see com.princetonsa.dao.FacturaBORRAMEDao#existeCodigoFactura(Connection, int)
	 * @author jarloc
	 */
	public static int existeCodigoFactura (Connection con, int codigoFact)
	{
		String existeFacturaStr = "SELECT COUNT(*) AS codigo FROM facturas WHERE consecutivo_factura = ?";
	    try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(existeFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoFact);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
			    return rs.getInt("codigo");
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error SQL en existeCodigoFactura "+e.toString());
			return 0;
		}
	    return 0;   
	}
	
	/**
	 * Consultar el consecutivo de la primera factura
	 * generada por el sistema
	 * @param con Connection, conexion con la fuente de datos	 
	 * @return int, -1 si hay error
	 * @see com.princetonsa.dao.FacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
	 * @author jarloc
	 */
	public static int primeraFacturaGeneradaSistema (Connection con)
	{
		String codigoPrimeraFactGeneradaPorElSistemaStr = "SELECT case when min(consecutivo_factura) is null then -1 else min(consecutivo_factura) end AS consecutivo from facturas where tipo_factura_sistema ="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
	    try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(codigoPrimeraFactGeneradaPorElSistemaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
			    return rs.getInt("consecutivo");
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error SQL en primeraFacturaGeneradaSistema "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}
	    return ConstantesBD.codigoNuncaValido;   
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int esCuentaValidaProcesoFacturacion(Connection con, int codigoCuenta) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int cuenta = 0;
		try
		{
			String cadena="SELECT c.cuenta as id from cuentas_proceso_fact c where c.cuenta=? and (c.estado='"+ConstantesBD.codigoEstadoCuentaActiva+"' OR  c.estado='"+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"' OR (c.estado='"+ConstantesBD.codigoEstadoCuentaAsociada+"' AND getcuentafinal(c.cuenta) IS NULL))";
			pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoCuenta);
			rs = pst.executeQuery();
			if(rs.next()){
				cuenta = rs.getInt("id");
			}
			return cuenta;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esCuentaValidaProcesoFacturacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esCuentaValidaProcesoFacturacion", e);
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
		return cuenta;
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean facturaTieneAjustesPendientes(Connection con, int codigoFactura)
	{
		String sentencia="SELECT count(1) as ajustes from ajus_fact_empresa a inner join ajustes_empresa ae on (a.codigo=ae.codigo) where a.factura=? and ae.estado = "+ConstantesBD.codigoEstadoCarteraGenerado;
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("ajustes")==0)
		        {
		            return false;
		        }
		        else
		        {
		            return true;
		        }
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean facturaTieneCastigoCartera(Connection con, int codigoFactura)
	{
		String sentencia="SELECT count(1) as valortempo from ajus_fact_empresa a inner join ajustes_empresa ae on (a.codigo=ae.codigo) where a.factura=? and ae.estado = "+ConstantesBD.codigoEstadoCarteraAprobado +" and ae.castigo_cartera = "+ValoresPorDefecto.getValorTrueParaConsultas() ;
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("...---castigo--->"+sentencia+"--"+codigoFactura);
			ps.setInt(1, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("valortempo")==0)
		        {
		            return false;
		        }
		        else
		        {
		            return true;
		        }
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean facturaTienePagosPendientes(Connection con, int numeroFactura)
	{
		String sentencia="SELECT count(1) as pagos from aplicacion_pagos_factura apf inner join cartera.aplicacion_pagos_empresa ape on (apf.aplicacion_pagos=ape.codigo) where ape.estado="+ConstantesBD.codigoEstadoAplicacionPagosPendiente+" and apf.factura=?";
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("pagos")==0)
		        {
		            return false;
		        }
		        else
		        {
		            return true;
		        }
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Aplicando Ajustes
	 * @param con
	 * @param numeroFactura
	 * @param consulta
	 * @return
	 */
	public static boolean facturaTieneSaldoPendiente(Connection con, int numeroFactura, String consulta) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				if (rs.getDouble("saldo")>0)
		        {
		            return true;
		        }
		        else
		        {
		            return false;
		        }
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean facturaTieneAjustesPendientesDiferentesAjusteActual(Connection con, int codigoFactura, double codigoAjuste) 
	{
		String sentencia="SELECT count(1) as ajustes from ajus_fact_empresa a " +
											"inner join ajustes_empresa ae on (a.codigo=ae.codigo) " +
											"where a.factura=? and ae.codigo <> ? and ae.estado = "+ConstantesBD.codigoEstadoCarteraGenerado;
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			ps.setDouble(2,codigoAjuste);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("ajustes")==0)
		        {
		            return false;
		        }
		        else
		        {
		            return true;
		        }
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean esFacturaCerrada(Connection con, int numeroFactura) 
	{
		String sentencia="SELECT factura_cerrada as factura_cerrada from facturas where codigo=?";
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				return rs.getBoolean("factura_cerrada");
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean esFacturaExterna(Connection con, int numeroFactura) 
	{
		String sentencia="SELECT tipo_factura_sistema as factura_sistema from facturas where codigo=?";
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				return (!rs.getBoolean("factura_sistema"));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Metodo implementado para consultar los pagos
	 * realizados a una factura, que no se encuentren
	 * en estado pendiente y que esten en un rango de
	 * fecha 
	 * @param con Connection
	 * @param numeroFactura int
	 * @param fecha String 
	 * @return ResultSet
	 * @author jarloc
	 */
	public static ArrayList facturaTienePagos (Connection con, int numeroFactura, String fecha)
	{	    
	    PreparedStatementDecorator ps;
	    String query = "select apf.factura as codigo_factura,f.consecutivo_factura as  consecutivo_factura " +
	    		"from aplicacion_pagos_factura apf " +
	    		"inner join facturas f on(apf.factura=f.codigo) " +
	    		"inner join cartera.aplicacion_pagos_empresa ape on(apf.aplicacion_pagos=ape.codigo) "+
	    		"where to_char(ape.fecha_aplicacion,'yyyy-mm') <= ? and ape.estado = "+ConstantesBD.codigoEstadoCarteraGenerado+" order by consecutivo_factura";

	    try 
	    {
            ps =  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            //ps.setInt(1,numeroFactura);
            ps.setString(1,fecha);
            logger.info("consulta pagos->"+query);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            
            ArrayList array = new ArrayList ();
    		InfoDatos factura = new InfoDatos();
    		while(rs.next())
    		{				
    			factura= new InfoDatos(rs.getInt("codigo_factura"),rs.getInt("consecutivo_factura")+"");
    			array.add(factura);
    		}
    		return array;
        } 
	    catch (SQLException e) 
	    {            
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * Metodo para consultar si una factura tiene ajustes
	 * aprobados o anulados, en un rango de fecha.
	 * @param con Connection
	 * @param numeroFactura int 
	 * @param fecha String 
	 * @return ResultSet
	 * @author jarloc
	 */
	public static ArrayList facturaAjustesAprobadosAnulados(Connection con, int numeroFactura,String fecha)
	{
		String sentencia="SELECT af.factura as cod_factura,f.consecutivo_factura as  consecutivo_factura "+ 
						"from ajustes_empresa ae "+
						"inner join ajus_fact_empresa af on (af.codigo = ae.codigo) "+
						"inner join facturas f on(f.codigo=af.factura) "+
						"where to_char(ae.fecha_ajuste, 'yyyy-mm') <= ? and ae.estado = "+ConstantesBD.codigoEstadoCarteraGenerado+" order by consecutivo_factura";	
		
		try 
		{
		    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.setInt(1, numeroFactura);
			ps.setString(1,fecha);
			logger.info("consulta ajustes->"+sentencia);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());		
			
			ArrayList array = new ArrayList ();
			InfoDatos factura;
			while(rs.next())
			{
				factura= new InfoDatos(rs.getInt("cod_factura"),rs.getInt("consecutivo_factura")+"");
				logger.info("");
				array.add(factura);
			}
			return array;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * Metodo que obtiene las solicitudes de cx que no estan liquidadas
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap obtenerSolicitudesCxSinLiquidacion( Connection con, Vector cuentas, Vector subCuentas)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT " +
					"s.numero_solicitud AS numerosolicitud, " +
					"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas, " +
					"subcu.convenio as codigoconvenio, " +
					"getnombreconvenio(subcu.convenio) as nombreconvenio " +
				"FROM " +
					"solicitudes s " +
					"INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud=s.numero_solicitud) " +
					"INNER JOIN cuentas c ON (c.id=s.cuenta) " +
					"LEFT OUTER JOIN sub_cuentas subcu ON(sc.sub_cuenta=subcu.sub_cuenta) "+
					"left outer JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud and ssc.sub_cuenta=sc.sub_cuenta) " +
					"left outer JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
				"WHERE   " +
					"sc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
					"AND (ssc.eliminado='"+ConstantesBD.acronimoNo+"' or ssc.eliminado is null) "+
					"AND s.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
					"AND dc.codigo_detalle_cargo is null " +
					"AND s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+" " +
					"order by consecutivoordenesmedicas ";
				
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
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
			logger.error("############## SQLException ERROR obtenerSolicitudesCxSinLiquidacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesCxSinLiquidacion", e);
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
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean viaIngresoPermiteCorteFactura(Connection con, int codigoCuenta) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String sentencia="SELECT coalesce(vi.corte_facturacion, '"+ConstantesBD.acronimoNo+"') as permite from " +
			"cuentas c INNER JOIN vias_ingreso vi ON (vi.codigo=c.via_ingreso) where c.id=?";
			pst =  con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoCuenta);
			rs=pst.executeQuery();
			if (rs.next())
			{
				String permite=rs.getString("permite");
				return UtilidadTexto.getBoolean(permite);
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR viaIngresoPermiteCorteFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR viaIngresoPermiteCorteFactura", e);
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
		return false;
	}
	
	
	/**
	 * Metodo que evalua los posibles errores de las solicitudes PYP, hasta el momento solo se verifica que las solicitudes 
	 * esten cubiertas por subcuenta
	 * @param con
	 * @param cuentas
	 * @parma subCuentas
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector analisisSolicitudesPYP(Connection con, Vector cuentas, Vector subCuentas) 
	{
		Vector consecutivosVector= new Vector();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String analisisSolicitudesPYPStr= 	"SELECT " +
					"s.numero_solicitud AS numeroSolicitud, " +
					"s.consecutivo_ordenes_medicas AS consecutivo, " +
					"dc.convenio as codigoconvenio " +
				"FROM " +
					"det_cargos dc " +
					"INNER JOIN solicitudes_subcuenta ssc on(dc.cod_sol_subcuenta=ssc.codigo) " +
					"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
					"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
				"WHERE " +
					"dc.sub_cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") " +
					"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
					"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
					"AND s.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
					"AND dc.estado not in("+ConstantesBD.codigoEstadoFExento+","+ConstantesBD.codigoEstadoFInactiva+","+ConstantesBD.codigoEstadoFAnulada+") " +
					"AND s.pyp="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
					"AND dc.facturado='"+ConstantesBD.acronimoNo+"'";

			pst = con.prepareStatement(analisisSolicitudesPYPStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			while (rs.next())
			{
				if(!Utilidades.esSolicitudPypCubiertaXConvenio(con, rs.getString("numeroSolicitud"), rs.getInt("codigoconvenio")))
				{	
					consecutivosVector.add((rs.getString("consecutivo")!=null)?rs.getString("consecutivo"):"");
				}	
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR analisisSolicitudesPYP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR analisisSolicitudesPYP", e);
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
		return consecutivosVector;
	}
	
	/**
	 * metodo que evalua si todas las solicitudes son de pyp para no cobrarle nada al paciente
	 * @param con
	 * @param cuentas
	 * @param subCuenta
	 * @return
	 */
	public static boolean existenUnicamenteSolicitudesPYP(Connection con, Vector cuentas, double subCuenta) 
	{
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String consulta="SELECT " +
					"count(1) as solicitudes_NO_pyp " +
				"FROM " +
					"det_cargos dc " +
					"INNER JOIN solicitudes_subcuenta ssc on(dc.cod_sol_subcuenta=ssc.codigo) " +
					"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
					"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
				"WHERE " +
					"dc.sub_cuenta = "+(long)subCuenta+" " +
					"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
					"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
					"AND s.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" " +
					"AND dc.estado not in("+ConstantesBD.codigoEstadoFExento+","+ConstantesBD.codigoEstadoFInactiva+","+ConstantesBD.codigoEstadoFAnulada+") " +
					"AND (s.pyp<>"+ValoresPorDefecto.getValorTrueParaConsultas()+" or s.pyp is null) " +
					"AND dc.facturado='"+ConstantesBD.acronimoNo+"'";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if (rs.next()){
				if(rs.getInt("solicitudes_NO_pyp")>0){
					return false;
				}	
				else{	
					return true;
				}	
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existenUnicamenteSolicitudesPYP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existenUnicamenteSolicitudesPYP", e);
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
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean existenSolicitudesNoFacturadas( Connection con, Vector cuentas, Vector subCuentas)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=" SELECT " +
					"dc.solicitud AS numerosolicitud " +
				"FROM " +
					"solicitudes_subcuenta ssc " +
					"INNER JOIN det_cargos dc ON (dc.solicitud=ssc.solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
					"INNER JOIN solicitudes s ON(s.numero_solicitud=dc.solicitud) " +
				"WHERE   " +
					"dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
					"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
					"AND s.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
					"AND dc.estado = "+ConstantesBD.codigoEstadoFCargada+" " +
					"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
					"AND dc.cantidad_cargada>0 " +
					"AND s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+" " +
					"AND coalesce(dc.valor_unitario_dcto,0)<coalesce(valor_unitario_cargado,0) " +
					//no debe validar los cargos paquetizados
					"AND dc.paquetizado='N' "+
				"UNION " +
				" SELECT " +
					"dc.solicitud AS numerosolicitud " +
				"FROM " +
					"solicitudes_subcuenta ssc " +
					"INNER JOIN det_cargos dc ON (dc.solicitud=ssc.solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
					"INNER JOIN solicitudes s ON(s.numero_solicitud=dc.solicitud) " +
				"WHERE   " +
					"dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
					"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
					"AND s.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
					"AND dc.estado = "+ConstantesBD.codigoEstadoFPendiente+" "+
					"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
					"AND s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+" "+
					//no debe validar los cargos paquetizados
					"AND dc.paquetizado='N' ";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs= pst.executeQuery();
			if(rs.next())
			{
				return true;
			}	
			else
			{
				//no encontro solicitudes entonces debemos verificar las de cx
				HashMap mapaCx=obtenerSolicitudesCxSinLiquidacion(con, cuentas, subCuentas);
				if(Integer.parseInt(mapaCx.get("numRegistros")+"")>0)
				{
					return true;
				}
				return false;
			}	
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existenSolicitudesNoFacturadas",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existenSolicitudesNoFacturadas", e);
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
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public static Vector<String> obtenerRangoInicialFinalFacturaMultiempresa( Connection con, double empresaInstitucion )
	{
		
		Vector<String> vector= new Vector<String>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"SELECT " +
				"coalesce(rgo_inic_fact, "+ConstantesBD.codigoNuncaValido+") as rangoinicial, " +
				"coalesce(rgo_fin_fact, "+ConstantesBD.codigoNuncaValido+") as rangofinal, " +
				"getdescempresainstitucion(codigo) as descempresa " +
			"FROM empresas_institucion WHERE codigo=? ";
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, empresaInstitucion);
			rs=pst.executeQuery();
			if(rs.next())
			{
				vector.add(rs.getDouble("rangoinicial")+"");
				vector.add(rs.getDouble("rangofinal")+"");
				vector.add((rs.getString("descempresa") != null)?rs.getString("descempresa"):"");
			}	
			else
			{
				vector.add(ConstantesBD.codigoNuncaValido+"");
				vector.add(ConstantesBD.codigoNuncaValido+"");
				vector.add("");
			}	
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerRangoInicialFinalFacturaMultiempresa",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerRangoInicialFinalFacturaMultiempresa", e);
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
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param incremento
	 * @return
	 */
	public static boolean incrementarConsecutivoFacturaMultiempresa(Connection con, double empresaInstitucion,int incremento) 
	{
		String cadena="UPDATE empresas_institucion SET " +
						"valor_consecutivo_fact = convertiranumero(coalesce((valor_consecutivo_fact+"+incremento+"), rgo_inic_fact)||'') " +
						"where codigo=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,empresaInstitucion);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}	
		}
		catch(SQLException e)
		{
			logger.info("Error en incrementarConsecutivoFacturaMultiempresa: "+e);
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subcuenta
	 * @param cubierta
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String obtenerSolicitudesSinAutorizacion( Connection con, Vector cuentas, double subCuenta, boolean cubierta)
	{
		String consecutivos="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT tabla.consecutivoordenesmedicas from " +
						"(" +
							"(	SELECT " +
									"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
								"FROM " +
									"solicitudes s   " +
									"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
									"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
									"INNER JOIN cuentas c ON (c.id=s.cuenta) "+
								"WHERE   " +
									"dc.sub_cuenta = "+(long)subCuenta+" "+
									"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
									"AND ssc.cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
									"AND dc.estado in ("+ConstantesBD.codigoEstadoFCargada+") " +
									"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " + ///con esto solo cargamos el encabezado de ese paquete
									"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
									"AND dc.servicio_cx is null " +
									"AND dc.cantidad_cargada>0 ";
							
					if(cubierta)
					{	
						consulta+=	"AND dc.cubierto='"+ConstantesBD.acronimoSi+"' "+
									"AND dc.requiere_autorizacion='"+ConstantesBD.acronimoSi+"' "+
									"AND (dc.nro_autorizacion is null or dc.nro_autorizacion='') ";
					}
					else
					{
						consulta+=	"AND dc.cubierto='"+ConstantesBD.acronimoNo+"' "+
									"AND (dc.nro_autorizacion is null or dc.nro_autorizacion='') ";
					}
					consulta+=") ";
			
			//TENEMOS QUE UNIRLE LA SUMA DE TODOS LOS ASOCIO DE LAS CX
			consulta+=			"UNION " +
								"(" +
								"SELECT " +
									"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas " +
									"FROM " +
										"solicitudes s   " +
										"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
										"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
										"INNER JOIN cuentas c ON (c.id=s.cuenta) "+
									"WHERE   " +
										"dc.sub_cuenta="+(long)subCuenta+" " +
										"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
										"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
										"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
										"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
										"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " + ///con esto solo cargamos el encabezado de ese paquete
										"AND dc.servicio_cx is not null "+
										"AND dc.cantidad_cargada>0 ";
							
					if(cubierta)
					{	
						consulta+=	"AND dc.cubierto='"+ConstantesBD.acronimoSi+"' "+
									"AND dc.requiere_autorizacion='"+ConstantesBD.acronimoSi+"' "+
									"AND (dc.nro_autorizacion is null or dc.nro_autorizacion='') ";
					}
					else
					{
						consulta+=	"AND dc.cubierto='"+ConstantesBD.acronimoNo+"' "+
									"AND (dc.nro_autorizacion is null or dc.nro_autorizacion='') ";
					}
					consulta+=") " +
					" ) tabla order by tabla.consecutivoordenesmedicas ";

			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			boolean ponerComa=false;
			while(rs.next()){
				if(ponerComa){
					consecutivos += ",";
				}
				consecutivos += (rs.getString(1)!=null)?rs.getString(1):"";
				ponerComa = true;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerSolicitudesSinAutorizacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSolicitudesSinAutorizacion", e);
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
		return consecutivos;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoContrato
	 * @return
	 */
	public static boolean esPacienteCapitadoVigente(Connection con, int codigoPersona, int codigoContrato, int ingreso)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT " +
				"consecutivo " +
			"from " +
				"usuario_x_convenio " +
			"where " +
				"persona=? " +
				"and contrato=? " +
				"and to_char((select fecha_ingreso from ingresos where id=?), 'YYYY-MM-DD') between to_char(fecha_inicial, 'YYYY-MM-DD') and to_char(fecha_final, 'YYYY-MM-DD') ";
			
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPersona);
			pst.setInt(2, codigoContrato);
			pst.setInt(3, ingreso);
			rs=pst.executeQuery();
			if(rs.next())
			{
				return true;
			}	
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esPacienteCapitadoVigente",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esPacienteCapitadoVigente", e);
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
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static Vector<String> obtenerCuentasActivaPacFactura(Connection con, String consecutivoFactura, int institucion)
	{
		Vector<String> vector= new Vector<String>();
		String consulta="SELECT " +
							"c.id " +
						"FROM " +
							"cuentas c, " +
							"facturas f " +
						"WHERE " +
							"c.codigo_paciente=f.cod_paciente " +
							"AND f.consecutivo_factura=? " +
							"and f.institucion=? " +
							"AND c.estado_cuenta=?";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(consecutivoFactura));
			ps.setInt(2, institucion);
			ps.setInt(3, ConstantesBD.codigoEstadoCuentaActiva);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{	
				vector.add(rs.getString(1));
			}	
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static Vector<String> obtenerIngresosAbiertosPacFactura(BigDecimal codigoFactura)
	{
		Vector<String> vector= new Vector<String>();
		String consulta="SELECT " +
							"i.consecutivo " +
						"FROM " +
							"ingresos i, " +
							"facturas f " +
						"WHERE " +
							"i.codigo_paciente=f.cod_paciente " +
							"AND f.codigo=? " +
							"AND i.estado=? " +
							"AND i.id<>(select c.id_ingreso FROM cuentas c WHERE c.id=f.cuenta) ";
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n*****************************************************************************************************");
			logger.info("obtenerIngresosAbiertosPacFactura-->"+consulta+" ->fact->"+codigoFactura+" ABI");
			logger.info("*****************************************************************************************************\n\n");
			ps.setInt(1, codigoFactura.intValue());
			ps.setString(2, ConstantesIntegridadDominio.acronimoEstadoAbierto);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{	
				vector.add(rs.getString(1));
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param consulta 
	 * @return
	 */
	public static boolean generaCargoEnSolicitud(Connection con, int numeroSolicitud, String consulta)
	{
		PreparedStatement pst=null;
		ResultSet rs= null;
		try
		{
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			
			rs=pst.executeQuery();
			
			boolean resultado=false;
			if(rs.next()){	
				resultado=rs.getBoolean(1);
			}
			return resultado;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR generaCargoEnSolicitud",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR generaCargoEnSolicitud", e);
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
		return false;
	}
	
	/*
	 * Validacion Anexo 712 para verificar si la factura a generarle cuenta de cobro si es preglosa, ésta debe estar respondida
	 */
	public static boolean validarPreglosaRespondida (Connection con, String numeroFactura)
	{
		boolean resultado=true;
		boolean cumple=false;
		boolean cumple2=false;
		
		String cadena=	"SELECT " +
							"rg.glosa_sistema AS glosasistema, " +
							"rg.estado, " +
							"ag.fue_auditada AS indicativo, " +
							"ag.codigo_factura AS codigofactura " +
						"FROM " +
							"registro_glosas rg " +
						"INNER JOIN " +
							"auditorias_glosas ag ON (ag.glosa=rg.codigo) " +
						"INNER JOIN " +
							"facturas f ON (f.codigo=ag.codigo_factura) " +
						"WHERE " +
							"ag.fue_auditada='"+ConstantesIntegridadDominio.acronimoIndicativoPreglosa +"' " +
						"AND " +
							"f.consecutivo_factura="+numeroFactura;
		
		String cadena2=	"SELECT " +
							"rg.glosa_sistema AS glosasistema, " +
							"rg.estado, " +
							"ag.fue_auditada AS indicativo, " +
							"ag.codigo_factura AS codigofactura " +
						"FROM " +
							"registro_glosas rg " +
						"INNER JOIN " +
							"auditorias_glosas ag ON (ag.glosa=rg.codigo) " +
						"INNER JOIN " +
							"facturas f ON (f.codigo=ag.codigo_factura) " +
						"WHERE " +
							"rg.estado='"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"' " +
						"AND " +
							"f.consecutivo_factura="+numeroFactura;
		
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeQuery().next())
		        cumple=true; 
			
			ps=  new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeQuery().next())
		        cumple2=true;
							
			if (cumple&&!cumple2)
				resultado=false;
			
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR / guardarEncabezado / "+e);
		}
		return resultado;	
	}

	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static boolean puedoAnularFacturaXGlosa(BigDecimal codigoFactura) 
	{
		boolean retorna= true;
		String consultaStr="SELECT " +
								"ag.codigo " +
							"from " +
								"glosas.auditorias_glosas ag " +
								"INNER JOIN glosas.registro_glosas rg ON(rg.codigo=ag.glosa) " +
							"WHERE " +
								"ag.codigo_factura=? " +
								"and rg.estado not in ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+"', '"+ConstantesIntegridadDominio.acronimoEstadoGlosaAnulada+"')";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			logger.info("\n\npuedoAnularFacturaXGlosa---->"+consultaStr+"\n\n");
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura.intValue());
			if(ps.executeQuery().next())
			{
				retorna= false;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}	
		catch (SQLException e) 
		{
			logger.error("ERROR / puedoAnularFacturaXGlosa / "+e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static boolean puedoAnularFacturaXReclamaciones(BigDecimal codigoFactura) 
	{
		boolean retorna= true;
		String consultaStr="select count(1) " +
							" from manejopaciente.reclamaciones_acc_eve_fact " +
							" where CODIGO_FACTURA=? and estado in ('"+ConstantesIntegridadDominio.acronimoRadicado+"','"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"')";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura.intValue());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt(1)>0)
					retorna= false;
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}	
		catch (SQLException e) 
		{
			logger.error("ERROR / puedoAnularFacturaXReclamaciones / "+e);
		}
		return retorna;
	}

	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentasSeleccionadasVector
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> obtenerMedicoRespondeSinTipoLiquidacion(	Connection con, 
																				Vector cuentas, 
																				Vector subCuentas) 
	{
		ArrayList<String> listaMedicos= new ArrayList<String>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"SELECT DISTINCT " +
						"codigoMedico as codigomedico, " +
						"administracion.getnombremedico(codigoMedico) as nombremedico " +
					"FROM ( " +
							"SELECT " +
								"CASE WHEN sol.codigo_medico_responde IS NULL THEN (SELECT distinct(a.codigo_medico) FROM cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) WHERE numero_solicitud=sol.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1) ELSE sol.codigo_medico_responde END as codigoMedico, " +
								"sol.numero_solicitud as numero_solicitud, " +
								"sol.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
								"sol.tipo as tipo, " +
								"sol.centro_costo_solicitado as centro_costo_solicitado, " +
								"sol.estado_historia_clinica as estado_historia_clinica " +
							"from " +
								"solicitudes sol " +
								"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
								"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
							"WHERE " +
								"sol.cuenta in (" +UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
								"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
								"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
								"and sol.tipo NOT IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+","+ConstantesBD.codigoTipoSolicitudCirugia+", "+ConstantesBD.codigoTipoSolicitudPaquetes+")" +
								"and sol.estado_historia_clinica  not in ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") "+
								//parte de las validaciones para laboratorios, procedimientos
							"UNION "+
							"SELECT " +
								"CASE WHEN sol.codigo_medico_responde IS NULL THEN (SELECT distinct(a.codigo_medico) FROM cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) WHERE numero_solicitud=sol.numero_solicitud "+ValoresPorDefecto.getValorLimit1()+" 1) ELSE sol.codigo_medico_responde END as codigoMedico, " +
								"sol.numero_solicitud as numero_solicitud, " +
								"sol.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
								"sol.tipo as tipo, " +
								"sol.centro_costo_solicitado as centro_costo_solicitado, " +
								"sol.estado_historia_clinica as estado_historia_clinica " +	
							"from " +
								"solicitudes sol " +
								"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
								"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
							"WHERE " +
								"sol.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
								"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
								"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
								"and sol.tipo not IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+","+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudCita+","+ConstantesBD.codigoTipoSolicitudPaquetes+") " +
								"and sol.estado_historia_clinica  in ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") "+		
								"and (" +
										"(" +
											"sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" " +
											"and esSolCargoSolicitud(sol.numero_solicitud)<=0" +
										") " +
										"or " +
										"( " +
											"sol.estado_historia_clinica in ("+ConstantesBD.codigoEstadoHCEnProceso+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+") " +
											"and (esSolCargoSolicitud(sol.numero_solicitud)<=0 and esSolCargoEnProceso(sol.numero_solicitud)<=0)" +
										")" +
									")"+
							"UNION " +
							//parte de cirugias
							"SELECT " +
								"h.medico as codigoMedico, " +
								"sol.numero_solicitud as numero_solicitud, " +
								"sol.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
								"sol.tipo as tipo, " +
								"sol.centro_costo_solicitado as centro_costo_solicitado, " +
								"sol.estado_historia_clinica as estado_historia_clinica " +
							"from " +
								"solicitudes sol " +
								"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = sol.numero_solicitud) " +
								"INNER JOIN det_cargos dc ON (dc.solicitud=sol.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
								"INNER JOIN sol_cirugia_por_servicio cir ON(cir.numero_solicitud=sol.numero_solicitud) " +
								"INNER JOIN det_cx_honorarios h ON(h.cod_sol_cx_servicio=cir.codigo) " +
							"where " +
								"sol.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
								"and dc.sub_cuenta in ("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(subCuentas, false)+") "+
								"and ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado in ("+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFCargada+") " +
								"and dc.articulo is null " +//para no tener cncuenta los materiales especiales
						  ") tempo " +
				      "WHERE " +
				      "tempo.tipo NOT IN ("+ ConstantesBD.codigoTipoSolicitudMedicamentos + ","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudEstancia+") " +
				      "and tempo.centro_costo_solicitado<>" + ConstantesBD.codigoCentroCostoExternos + " " +
				      //"and esSolicitudTotalEstado(tempo.numero_solicitud,"+ConstantesBD.codigoEstadoFInactiva+")<> '"+ConstantesBD.acronimoNo+"' " +
				      "and tempo.estado_historia_clinica not in("+ConstantesBD.codigoEstadoHCAnulada+") ";
			
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			rs= pst.executeQuery();
			
			while(rs.next())
			{
				if(!existeTipoLiquidacionXMedico(con, rs.getInt("codigomedico")))
				{
					listaMedicos.add((rs.getString("nombremedico")!=null)?rs.getString("nombremedico"):"");
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerMedicoRespondeSinTipoLiquidacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerMedicoRespondeSinTipoLiquidacion", e);
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
		return listaMedicos;
	}

	/**
	 * 
	 * @param int1
	 * @return
	 */
	private static boolean existeTipoLiquidacionXMedico(Connection con, int codigoMedico) 
	{
		boolean retorna=false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT coalesce(tipo_liquidacion,'') from administracion.medicos where codigo_medico=? ";
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
	    	pst.setInt(1, codigoMedico);
			rs= pst.executeQuery();
			
			if(rs.next())
			{
				retorna= !UtilidadTexto.isEmpty(rs.getString(1));
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeTipoLiquidacionXMedico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeTipoLiquidacionXMedico", e);
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
		return retorna;
	}
	
	
	/**
	 * 
	 * @param codigoMedico
	 * @return
	 */
	public static String obtenerTipoLiquidacionPool(int codigoMedico) 
	{
		String resultado="";
		String cadenaConsulta="select coalesce(m.tipo_liquidacion, '') as tipoliquidacion from administracion.medicos m where m.codigo_medico="+codigoMedico;
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				resultado = rs.getString("tipoliquidacion");
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("ERROR en consulta del Cargo del Usuario >> cadena >> "+cadenaConsulta );
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		
		
		return resultado;
	}
	
}