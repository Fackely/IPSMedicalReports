package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseReliquidacionTarifasDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReliquidacionTarifasDao.class);
	
	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap obtenerCuentasReliquidar (Connection con, int codigoCentroAtencion, String codigoPaciente)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String consulta=	"SELECT " +
								"i.consecutivo as numeroingreso, " +
								"getintegridaddominio(i.estado) as nomestadoingreso, " +
								"to_char(i.fecha_ingreso, 'DD/MM/YYYY') ||' '|| substr(i.hora_ingreso, 1,5) as fechahoraingreso, " +
								"CASE WHEN i.fecha_egreso IS NULL THEN '' ELSE to_char(i.fecha_egreso, 'DD/MM/YYYY') ||' '|| substr(i.hora_egreso, 1,5) END as fechahoracierreingreso, " +
								"c.id as numerocuenta, " +
								"coalesce(getDescripEntidadSubXingreso(c.id_ingreso),'') AS entidadsub," +
								"getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
								"getnombreviaingreso(c.via_ingreso) as nomviaingreso,   " +
								"(SELECT count(1) FROM sub_cuentas sc WHERE sc.ingreso=i.id) as numconvenios, " +
								"CASE WHEN (SELECT count(1) FROM sub_cuentas sc WHERE sc.ingreso=i.id)=1 then (SELECT sub_cuenta||'' FROM sub_cuentas sc WHERE sc.ingreso=i.id) else '' end as subcuenta " +
							"FROM " +
								"cuentas c " +
								"INNER JOIN ingresos i ON (i.id=c.id_ingreso) " +
								"INNER JOIN centros_costo cc ON (cc.codigo=c.area) " +
							"WHERE " +
								"cc.centro_atencion=? " +
								"AND i.codigo_paciente="+codigoPaciente+" " +
								"AND i.estado in ( '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
								"AND (c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") OR c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" AND getcuentafinal(c.id) IS NULL) " +
								"ORDER BY c.fecha_apertura DESC, c.hora_apertura DESC";	
									
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCentroAtencion);
			logger.info("llega a obtenerCuentasReliquidar---------->"+consulta+" ----cwentro aten== "+codigoCentroAtencion);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en obtenerCuentasReliquidar");
			e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap obtenerSolicitudesReliquidar(Connection con, String subCuenta, String esServicios)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String consultaStr = 	" SELECT DISTINCT " +
										"s.numero_solicitud AS numerosolicitud, " +
										"s.tipo AS codigotiposolicitud, " +
										"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas," +
										"dc.estado as estadocargo," +
										"dc.servicio as servicio," +
										"getgruposervicio(dc.servicio) as gruposervicio," +
										"dc.articulo as articulo," +
										"getclaseinventarioarticulo(dc.articulo) as clasearticulo," +
										"dc.codigo_detalle_cargo as codigocargo, " +
										"coalesce(dc.cantidad_cargada, "+ConstantesBD.codigoNuncaValido+") as cantidad, " +
										"dc.es_portatil as esportatil " +
									"FROM " +
										"solicitudes s   " +
										"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
										"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
										"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) "+ 
									"WHERE  " +
										"ssc.sub_cuenta= "+Utilidades.convertirALong(subCuenta)+" "+
										"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
										"AND " +
										"(" +
											"s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCRespondida+","+
																			ConstantesBD.codigoEstadoHCInterpretada+","+
																			ConstantesBD.codigoEstadoHCAdministrada+","+
																			ConstantesBD.codigoEstadoHCCargoDirecto+") " +
											" OR (s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCTomaDeMuestra+") AND esSolCargoSolicitud(s.numero_solicitud)>0) "+
											" OR (s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCEnProceso+") AND (esSolCargoSolicitud(s.numero_solicitud)>0 OR esSolCargoEnProceso(s.numero_solicitud)>0)) "+
											" OR s.tipo="+ConstantesBD.codigoTipoSolicitudCita+" "+ 
										") " +
										" AND dc.estado in ("+ConstantesBD.codigoEstadoFCargada+","+ConstantesBD.codigoEstadoFInactiva+", "+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFExento+") " +
	                                    " AND s.tipo<> "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
	                                    //solo toma el padre de los paquetes
	                                    " AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " +
	                                    " AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
	                                    " and dc.servicio_cx is null " +
	                                    " and dc.cantidad_cargada >0 ";
		
		if(esServicios.equals(ConstantesBD.acronimoSi))
			consultaStr+=" and dc.articulo is null and dc.servicio is not null ";
		//" and s.articulo is null and servicio is not null"
		if(esServicios.equals(ConstantesBD.acronimoNo))
		{
			//se coloca el de paquetes porque puede tener componentes de articulos
			consultaStr+=" AND ((dc.articulo is not null and dc.servicio is null) or s.tipo="+ConstantesBD.codigoTipoSolicitudPaquetes+" ) ";
		}	
		try
		{
			PreparedStatementDecorator cargarListado= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\nobtenerSolicitudesReliquidar-->"+consultaStr);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarListado.executeQuery()));  
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de solicitudes");
			e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoDetalleCargoPadre
	 * @param esServicios
	 * @return
	 */
	public static HashMap obtenerComponentesPaqueteReliquidar(Connection con, String subCuenta, double codigoDetalleCargoPadre, String esServicios)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String consultaStr = 	" SELECT DISTINCT " +
										"s.numero_solicitud AS numerosolicitud, " +
										"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas," +
										"dc.estado as estadocargo," +
										"coalesce(dc.cantidad_cargada, "+ConstantesBD.codigoNuncaValido+") as cantidad, " +
										"dc.es_portatil as esportatil " +
									"FROM " +
										"solicitudes s   " +
										"INNER JOIN solicitudes_subcuenta ssc ON (ssc.solicitud = s.numero_solicitud) " +
										"INNER JOIN det_cargos dc ON (dc.solicitud=s.numero_solicitud and dc.cod_sol_subcuenta=ssc.codigo) " +
									"WHERE  " +
										"ssc.sub_cuenta= "+Utilidades.convertirALong(subCuenta)+" "+
										"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
										"AND " +
										"(" +
											"s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCRespondida+","+
																			ConstantesBD.codigoEstadoHCInterpretada+","+
																			ConstantesBD.codigoEstadoHCAdministrada+","+
																			ConstantesBD.codigoEstadoHCCargoDirecto+") " +
											" OR (s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCTomaDeMuestra+") AND esSolCargoSolicitud(s.numero_solicitud)>0) "+
											" OR (s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCEnProceso+") AND (esSolCargoSolicitud(s.numero_solicitud)>0 OR esSolCargoEnProceso(s.numero_solicitud)>0)) "+
											" OR s.tipo="+ConstantesBD.codigoTipoSolicitudCita+" "+ 
										") " +
										" AND dc.estado in ("+ConstantesBD.codigoEstadoFCargada+","+ConstantesBD.codigoEstadoFInactiva+", "+ConstantesBD.codigoEstadoFPendiente+", "+ConstantesBD.codigoEstadoFExento+") " +
	                                    " AND s.tipo<> "+ConstantesBD.codigoTipoSolicitudCirugia+" " +
	                                    //solo tomamos los componentes
	                                    " AND dc.paquetizado='"+ConstantesBD.acronimoSi+"' " +
	                                    " AND dc.cargo_padre="+codigoDetalleCargoPadre+"  " +
	                                    " AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
	                                    " and dc.servicio_cx is null " +
	                                    " and dc.cantidad_cargada >0 ";
		
		logger.info("esServicios---->"+esServicios);
		if(esServicios.equals(ConstantesBD.acronimoSi))
			consultaStr+=" and dc.articulo is null and dc.servicio is not null ";
		//" and s.articulo is null and servicio is not null"
		if(esServicios.equals(ConstantesBD.acronimoNo))
			consultaStr+=" AND dc.articulo is not null and dc.servicio is null ";
		
		try
		{
			PreparedStatementDecorator cargarListado= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n55555555555555555555555555555555555555555555555555555555555555555555555555555555obtenerComponentesPaqueteReliquidar-->"+consultaStr);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarListado.executeQuery()));  
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de solicitudes componentes");
			e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param resultadoExitoso
	 * @param esqTarServOriginal
	 * @param esqTarServNuevo
	 * @param esqTarArtOriginal
	 * @param esqTarArtNuevo
	 * @return
	 */
	public static boolean insertarReliquidacion(	Connection con, 
													String loginUsuario, 
													String resultadoExitoso, 
													int esqTarServOriginal, 
													int esqTarServNuevo,	
													int	esqTarArtOriginal,	
													int esqTarArtNuevo)
	{
		String insertarStr= "INSERT INTO reliquidacion_tarifas (codigo," +					//1
																"fecha_generacion," +		
																"hora_generacion," +		
																"usuario_generacion," +		//2
																"resultado_exitoso, " +		//3
																"esq_tar_serv_original, " +	//4
																"esq_tar_serv_nuevo, " +	//5	
																"esq_tar_art_original, " +	//6
																"esq_tar_art_nuevo) " +		//7
																"VALUES (?, current_date, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?, ?)"; 
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reliquidacion_tarifas")+""));
			ps.setString(2, loginUsuario);
			ps.setString(3, resultadoExitoso);
			
			if(esqTarServOriginal>0)
				ps.setInt(4, esqTarServOriginal);
			else
				ps.setNull(4, Types.INTEGER);
			
			if(esqTarServNuevo>0)
				ps.setInt(5, esqTarServNuevo);
			else
				ps.setNull(5, Types.INTEGER);
			
			if(esqTarArtOriginal>0)
				ps.setInt(6, esqTarArtOriginal);
			else
				ps.setNull(6, Types.INTEGER);
			
			if(esqTarArtNuevo>0)
				ps.setInt(7, esqTarArtNuevo);
			else
				ps.setNull(7, Types.INTEGER);
			
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			logger.warn("Error en insertarReliquidacion");
			e.printStackTrace();
		}
		return false;
	}
	
}
