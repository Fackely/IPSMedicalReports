package com.princetonsa.dao.sqlbase.facturacion;


import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.mundo.facturacion.TrasladoSolicitudesPorTransplantes;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;





/**
 * SqlBaseTrasladoSolicitudesPorTransplantesDao
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseTrasladoSolicitudesPorTransplantesDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseTrasladoSolicitudesPorTransplantesDao.class);

	//----------------------------
	//-------indices--------------
	public static final String [] indicesListado = TrasladoSolicitudesPorTransplantes.indicesListado;
	
	public static final String [] indicesListadoReceptores = TrasladoSolicitudesPorTransplantes.indicesListadoReceptores;
	
	public static final String [] indicesTraslado = TrasladoSolicitudesPorTransplantes.indicesTraslado;
	
	/*----------------------------------------------------------------------------------------
	 *                      ATRIBUTOS TRASLADO SOLICITUDES POR TRANSPLANTE
	 -----------------------------------------------------------------------------------------*/
	/**
	 * busca las solicitudes de un paciente donante
	 */
	private static final String strCadenaConsultaSolicitudes=" SELECT" +
																		" to_char(s.fecha_solicitud,'dd/mm/yyyy') As fecha_solicitud7, " +
																		" s.consecutivo_ordenes_medicas As numero_orden8, " +
																		" case when dc.servicio is null then getdescripcionarticulo(dc.articulo) else getobtenercodigocupsserv (dc.servicio,"+ConstantesBD.codigoTarifarioCups+") ||' - '||getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") end As serv_articulo9, " +
																		" getestadosolhis(s.estado_historia_clinica) As estado_his_cli10, " +
																		" getnomcentrocosto(s.centro_costo_solicitado) As cen_cos_eje11, " +
																		" getnomcentrocosto(s.centro_costo_solicitante) As cen_cos_sol12, " +
																		" sum(dc.cantidad_cargada) as cantidad13, " +
																		" getnomtiposolicitud(s.tipo) as tiposolicitud14," +
																		" case when dc.servicio is null then dc.articulo else dc.servicio end As cod_serv_art21," +
																		" s.tipo As tipo_sol22," +
																		" dc.solicitud As solicitud25," +
																		" s.centro_costo_solicitado As cent_costo_solicitado26," +																																				
																		//" dc.nro_autorizacion = null As autorizacion27, "+																		
																		" coalesce(s.codigo_medico,"+ConstantesBD.codigoNuncaValido+") As codigo_medico28, " +
																		" coalesce(s.pool,"+ConstantesBD.codigoNuncaValido+") As pool29," +
																		" c.id As cuenta30," +
																		" c.id_ingreso As ingreso31," +
																		" case when dc.servicio is null then 'art' else 'serv' end As tipo_cargo32," +
																		" s.centro_costo_solicitante As cent_costo_solicitante33, " +
																		" CASE WHEN ( dc.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+" OR dc.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudMedicamentos+") THEN getcentcostoprinsolmed(dc.solicitud) end As cent_cost_ppal34, " +
																		" CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN getmedicoresponde(s.numero_solicitud) ELSE  coalesce(s.codigo_medico_responde,"+ConstantesBD.codigoNuncaValido+") END As codigo_medico_responde35, " +
																		" dc.codigo_detalle_cargo AS codigo_detalle_cargo36 " +
																	" FROM det_cargos dc " +
																	" INNER JOIN solicitudes s on (s.numero_solicitud=dc.solicitud)" +
																	" INNER JOIN cuentas c on(c.id=s.cuenta) " ;
	/**
	 * busca los ingresos de con tipo de transplante Receptor
	 */			
	private static final String strCadenaConsultaReceptores=" SELECT " +
																	" i.id As id_ingreso0, " +
																	" i.consecutivo As consecutivo1, " +
																	" to_char(i.fecha_ingreso,'dd/mm/yyyy') As fecha_ingreso2, " +
																	" getnombreviaingreso(c.via_ingreso) As via_ingreso3, " +
																	" getnombrepersona(c.codigo_paciente) As paciente4, " +
																	" getidpaciente(c.codigo_paciente) As identificacion5," +
																	" c.id As cuenta7," +
																	" c.codigo_paciente As codigo_persona8 " +
																" FROM ingresos i " +
																" INNER JOIN cuentas c ON (c.id_ingreso=i.id) " +
																" WHERE i.transplante='"+ConstantesIntegridadDominio.acronimoIndicativoReceptor+"'" +
																	" AND c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+","+ConstantesBD.codigoEstadoCuentaAsociada+")" +
																	" AND i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'" +
																	" AND i.centro_atencion=?";
	
	private static final String strCadenaActualizacionEstadoFacturacion=" UPDATE det_cargos SET estado=?"+
																						" WHERE codigo_detalle_cargo in(" +
																															"( select codigo_detalle_cargo from det_cargos where cargo_padre=?) "+
																														"UNION" +	
																															 " ( select codigo_detalle_cargo from det_cargos where  solicitud=?  " +
																															 	"AND codigo_factura is null " +
																						 										"AND tipo_distribucion='"+ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad+"'" +
																																"AND estado<>"+ConstantesBD.codigoEstadoFacturacionAnulada+")" +
																															" )" ;
																														
																						 									

	
	
	/**
	 * Cadena encargada de insertar un traslado en la tabla 
	 * (tras_sol_transplante)
	 */
	private static final String strCadenaInsercionTraslado = " INSERT INTO tras_sol_transplante (consecutivo,donante,receptor,institucion) VALUES " +
																								"(?,?,?,?)";
	/**
	 * encargado de insertar el detalle del traslado.
	 * en l atabla ( det_tras_sol_transplante )
	 */
	private static final String strCadenaInsercionDetTraslado = " INSERT INTO det_tras_sol_transplante (id_traslado,solicitud_trasladada,solicitud_generada," +
																									"fecha_traslado, hora_traslado,usuario_traslado) VALUES " +
																									" (?,?,?,?,?,?) ";
	
	private static final String strCadenaConsultaTraslado = "SELECT consecutivo FROM  tras_sol_transplante WHERE donante=? AND receptor=? AND institucion=?";
	
	
	
	/*----------------------------------------------------------------------------------------
	 *                              FIN TRASLADO SOLICITUDES POR TRANSPLANTE
	 -----------------------------------------------------------------------------------------*/
	

	/*----------------------------------------------------------------------------------------
	 *                           				METODOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo encargado de consultar si existe un translado
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- donante0
	 * -- receptor1
	 * -- institucion2
	 * @return codigoTranslado (-1 si no existe)
	 */
	public static int  existeTraslado (Connection connection, HashMap datos)
	{
		logger.info("\n entro a existeTraslado --> "+datos);
		String cadena=strCadenaConsultaTraslado;
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//donante
			ps.setInt(1, Utilidades.convertirAEntero(datos.get(indicesTraslado[0])+""));
			//receptor
			ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesTraslado[1])+""));
			//institucion
			ps.setInt(3, Utilidades.convertirAEntero(datos.get(indicesTraslado[2])+""));
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando si existe el translado "+e);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo encargado insertar el detalle del traslado de solitides
	 * en la tabla ( det_tras_sol_transplante )
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEYS' DEL MAPA DATOS
	 * ---------------------------
	 * -- idTraslado3
	 * -- solicitudTrasladada4
	 * -- solicitudGenerada5
	 * -- usuarioTraslado6
	 * @return false/true
	 */
	public static boolean insertarDetTraslado (Connection connection, HashMap datos)
	{
		logger.info("\n entro a insertarDetTraslado --> "+datos);
		String cadena=strCadenaInsercionDetTraslado;
	
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO det_tras_sol_transplante (id_traslado,solicitud_trasladada,solicitud_generada," +
				"fecha_traslado, hora_traslado,usuario_traslado) VALUES (?,?,?,?,?,?) 
			 */
			
			//id_traslado
			ps.setDouble(1,Utilidades.convertirADouble(datos.get(indicesTraslado[3])+""));
			//solicitud_trasladada
			ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesTraslado[4])+""));
			//solicitud_generada
			ps.setInt(3, Utilidades.convertirAEntero(datos.get(indicesTraslado[5])+""));
			//fecha traslado
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora traslado
			ps.setString(5, UtilidadFecha.getHoraActual());
			//usuario traslado
			ps.setString(6, datos.get(indicesTraslado[6])+"");
			
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			logger.info("\n problema insertando el detalle del translado "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de insertar los datos del traslado en la tabla ( tras_sol_transplante )
	 * @param connection
	 * @param datos
	 * -----------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------
	 * -- donante0
	 * -- receptor1
	 * -- institucion2
	 * @return false/true
	 */
	public static int insertarEncabTraslado (Connection connection, HashMap datos)
	{
		logger.info("\n entro a insertarEncabTraslado --> "+datos);
		String cadena=strCadenaInsercionTraslado;
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tras_sol_transplante (consecutivo,donante,receptor,institucion) VALUES (?,?,?,?)
			 */
			
			//consecutivo
			ps.setDouble(1,Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_tras_sol_transplante")+""));
			//donante
			ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesTraslado[0])+""));
			//receptor
			ps.setInt(3, Utilidades.convertirAEntero(datos.get(indicesTraslado[1])+""));
			//institucion
			ps.setInt(4, Utilidades.convertirAEntero(datos.get(indicesTraslado[2])+""));
			
			if (ps.executeUpdate()>0)
				return existeTraslado(connection, datos);
		}
		catch (SQLException e) 
		{
			logger.info("\n problema insertando el translado "+e);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de cambia el estado de actualizar
	 * el estado de la facturacion en la tabla det_cargos
	 * @param connection
	 * @param solicitud
	 * @param estado
	 * @return false/true
	 * 
	 */
	public static boolean actualizarEstadoFacturacion (Connection connection, String solicitud, String estado,String codigoDetalleCargo)
	{
		logger.info("\n  entre a actualizarEstadoFacturacion --> "+solicitud+" estado-->"+estado+" codigoDetalleCargo-->"+codigoDetalleCargo);
		String cadena = strCadenaActualizacionEstadoFacturacion;
		logger.info("\n cadena -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//estado
			ps.setInt(1, Utilidades.convertirAEntero(estado));
			//codigoDetalleCargo
			ps.setInt(2, Utilidades.convertirAEntero(codigoDetalleCargo));
			//solicitud
			ps.setInt(3, Utilidades.convertirAEntero(solicitud));
			
			
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando el estado de facturacion"+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de consultar los pacientes receptores
	 * @param connection
	 * @param criterios
	 * ------------------------
	 * KEY'S DEL MAPA CRITERIOS
 	 * ------------------------
 	 * -- centroAtencion6
 	 * @return MAPA
 	 * ----------------------------------------
 	 * KEY'S DEL MAPA QUE RETORNA
 	 * ----------------------------------------
 	 * -- idingreso0_
 	 * -- consecutivo1_
 	 * -- fechaIngreso2_
 	 * -- viaIngreso3_
 	 * -- paciente4
 	 * -- identificacion5_
 	 */
	public static HashMap consultarPacientesReceptores (Connection connection, HashMap criterios)
	{
		logger.info("\n  entre a consultarPacientesReceptores --> "+criterios);
		String cadena = strCadenaConsultaReceptores;
		logger.info("\n cadena -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//centro de atencion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indicesListadoReceptores[6])+""));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultado los pacientes Receptores "+e);
		}
		
		return null;
	}
	
	
	
	/**
	 * Metodo encargado de consultar los Servicios o los articulos
	 * de las solicitudes de un paciente.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * ------------------------------
	 * KEY'S DEL MAPA  CRITERIOS
	 * ------------------------------
	 * -- fechaInicial1
	 * -- fechaFinal2
	 * -- servicio3
	 * -- articuloMed4
	 * -- cCostoEje5
	 * -- cCostoSol6
	 * -- ingreso15
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- fechaSolicitud7_
	 * -- numeroOrden8_
	 * -- servArticulo9_
	 * -- estadoHisCli10_
	 * -- cenCosEje11_
	 * -- cenCosSol12_
	 * -- cantidad13_
	 * -- tiposolicitud14_
	 */
	public static HashMap consultarServiciosArticulos (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a consultarServiciosArticulos -->"+criterios );
		
		String cadena = strCadenaConsultaSolicitudes;
		
		String where=" WHERE dc.codigo_factura is null " +
							" AND dc.estado<>2 AND (dc.articulo is not null or dc.servicio is not null) " +
							" AND dc.paquetizado='"+ConstantesBD.acronimoNo+"'" +
							" AND dc.cantidad_cargada>0 " +
							" AND s.estado_historia_clinica not in ("+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCAnulada+") " +
							" AND dc.tipo_distribucion='"+ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad+"'" +
							" AND getnumrespsolicitud(dc.solicitud)=1 " +
							//se pones esta validacion de que no salgan las Cirugias ni paquetes por tarea 42261
							" AND dc.tipo_solicitud not in (14,15) " +
							// se pone esta validacion para que no aparescan solicitudes paquetizadas
							" AND esSolicitudPaquetizada(dc.solicitud)='"+ConstantesBD.acronimoNo+"'" ;
		
						
		
		String group=" GROUP BY s.fecha_solicitud, " +
						"s.consecutivo_ordenes_medicas, " + 
						"dc.servicio, " +
						"dc.articulo, " +
						"s.estado_historia_clinica, " +
						"s.centro_costo_solicitado, " +
						"s.codigo_medico, " +
						"s.tipo, " +
						"dc.tipo_solicitud, " +
						"dc.solicitud, " +
						"s.centro_costo_solicitado, " +
						"dc.nro_autorizacion, " +
						"s.codigo_medico, " +
						"s.pool, " +
						"c.id, " +
						"c.id_ingreso, " +
						"dc.servicio, " +
						"s.centro_costo_solicitante, " +
						"s.numero_solicitud, " +
						"s.codigo_medico_responde, " +
						"dc.codigo_detalle_cargo ";
		
		
		
		String order= " ORDER BY numero_orden8 ";
		
		//ingreso
		if (UtilidadCadena.noEsVacio(criterios.get(indicesListado[15])+""))
			where+=" AND  c.id_ingreso="+criterios.get(indicesListado[15]);
		//Valida por un rango de fechas
		if (UtilidadCadena.noEsVacio(criterios.get(indicesListado[1])+"") && UtilidadCadena.noEsVacio(criterios.get(indicesListado[2])+"") )
			where+=" AND s.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesListado[1])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesListado[2])+"")+"'" ;
		
		//servicio
		if (UtilidadCadena.noEsVacio(criterios.get(indicesListado[3])+""))
			where+=" AND dc.servicio="+criterios.get(indicesListado[3]);
			
		//Articulo/Medicamento
		if (UtilidadCadena.noEsVacio(criterios.get(indicesListado[4])+""))
			where+=" AND dc.articulo="+criterios.get(indicesListado[4]);
		
		//filtro por centro de costo ejecuta
		if (UtilidadCadena.noEsVacio(criterios.get(indicesListado[5])+""))
			where+=" AND s.centro_costo_solicitado="+criterios.get(indicesListado[5]);
		
		//filtro por centro de costo solicita
		if (UtilidadCadena.noEsVacio(criterios.get(indicesListado[6])+""))
			where+=" AND s.centro_costo_solicitante="+criterios.get(indicesListado[6]);
		
		////////////////////////////////////////////////////////////////////////
		//se arma la cadena con el where y el gruop by y el order
		cadena+=where+group+order;
		/////////////////////////////////////////////////////////////////////////
		
		logger.info("\n cadena -->"+cadena);
		try 
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		ps.close();
		return mapaRetorno;
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los servicios o articulos "+e);
		}
		
		return null;
	}
	

	/*----------------------------------------------------------------------------------------
	 *                           			 FIN METODOS
	 -----------------------------------------------------------------------------------------*/

	
	
	
}