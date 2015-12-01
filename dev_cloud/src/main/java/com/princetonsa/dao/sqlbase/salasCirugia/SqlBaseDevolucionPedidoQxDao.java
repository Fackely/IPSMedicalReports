package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class SqlBaseDevolucionPedidoQxDao
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDevolucionPedidoQxDao.class);
	
	/**
	 * Cadena para consultar las peticiones de cirugia de un paciente
	 */
	private static String strConPeticionesCirugia="SELECT " +
													"t.peticion AS peticion,  " +
													"to_char(t.fechapeticion,'DD/MM/YYYY') AS fechapeticion, " +
													"t.horapeticion AS horapeticion, " +
													"t.estadomedico AS estadomedico, " +
													"t.profesionalsolicita AS profesionalsolicita, " +
													"t.solicitud AS solicitud, " +
													"t.ordenmedica AS ordenmedica, " +
													"t.codigopaciente AS codigopaciente, " +
													"t.nombrepaciente AS nombrepaciente, " +
													"t.idpaciente AS idpaciente, " +
													"t.fechanacimiento AS fechanacimiento, " +
													"t.convenio AS convenio,  " +
													"t.centrocostosolicita AS centrocostosolicita, " +
													"t.nombrecentrocostosolicita AS nombrecentrocostosolicita, " +
													"t.centrocostosolicitado AS centrocostosolicitado, " +
													"t.nombrecentrocostosolicitado AS nombrecentrocostosolicitado " +
													"FROM( " +
														"SELECT DISTINCT pq.codigo AS peticion,  " +
														"pq.fecha_peticion AS fechapeticion,  " +
														"pq.hora_peticion AS horapeticion, " +
														"'' AS estadomedico, " +
														"getnombrepersona(pq.solicitante) AS profesionalsolicita, " +
														"'' AS solicitud, " +
														"'' AS ordenmedica, " +
														"pq.paciente AS codigopaciente, " +
														"getnombrepersona(pq.paciente) AS nombrepaciente, " +
														"getidpaciente(pq.paciente) AS idpaciente, " +
														"getfechanacimientopaciente(pq.paciente) AS fechanacimiento,  " +
														"getnombreconvenio(getconvenioxingreso(pq.ingreso)) AS convenio,  " +
														"pq.ingreso AS ingresopaciente, " +
														"p.centro_costo_solicitante AS centrocostosolicita, " +
														"getnomcentrocosto(p.centro_costo_solicitante) AS nombrecentrocostosolicita, " +
														"'' AS centrocostosolicitado, " +
														"'' AS nombrecentrocostosolicitado " +
														"FROM pedidos_peticiones_qx ppq " +
														"INNER JOIN peticion_qx pq ON(pq.codigo=ppq.peticion)  " +
														"INNER JOIN pedido p ON(p.codigo=ppq.pedido) " +
														"WHERE pq.institucion = ?  " +
														"AND pq.codigo NOT IN(SELECT codigo_peticion  " +
														"FROM solicitudes_cirugia WHERE codigo_peticion IS NOT NULL) " +
													"UNION " +
														"SELECT DISTINCT pq.codigo AS peticion,  " +
														"pq.fecha_peticion AS fechapeticion, " + 
														"pq.hora_peticion AS horapeticion, " +
														"getestadosolhis(s.estado_historia_clinica) AS estadomedico, " +
														"getnombrepersona(pq.solicitante) AS profesionalsolicita, " +
														"s.numero_solicitud||'' AS solicitud, " +
														"s.consecutivo_ordenes_medicas||'' AS ordenmedica, " +
														"c.codigo_paciente AS codigopaciente, " +
														"getnombrepersona(c.codigo_paciente) AS nombrepaciente, " +
														"getidpaciente(c.codigo_paciente) AS idpaciente, " +
														"getfechanacimientopaciente(c.codigo_paciente) AS fechanacimiento, " + 
														"getnombreconvenio(getconvenioxingreso(c.id_ingreso)) AS convenio,  " +
														"c.id_ingreso ingresopaciente, " +
														"s.centro_costo_solicitante AS centrocostosolicita, " +
														"getnomcentrocosto(s.centro_costo_solicitante) AS nombrecentrocostosolicita, " +
														"s.centro_costo_solicitado||'' AS centrocostosolicitado, " +
														"getnomcentrocosto(s.centro_costo_solicitado) AS nombrecentrocostosolicitado " +
														"FROM cuentas c  " +
														"INNER JOIN solicitudes s on(s.cuenta = c.id)  " +
														"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = s.numero_solicitud)  " +
														"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=sc.codigo_peticion) " +
														"INNER JOIN peticion_qx pq ON(pq.codigo = sc.codigo_peticion)  " +
														"INNER JOIN materiales_qx mat ON (mat.numero_solicitud = sc.numero_solicitud)  " +
														"INNER JOIN personas p ON(p.codigo=pq.solicitante) " +
														"WHERE sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"')  " +
														"AND (mat.finalizado = '"+ConstantesBD.acronimoSi+"' OR mat.finalizado IS NULL) " +
														"AND s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND s.estado_historia_clinica<>4 " +
													") t WHERE t.ingresopaciente = ? " +
													"ORDER BY fechapeticion DESC, horapeticion DESC ";
	
	/**
	 * Cadena para consultar las devoluciones pendientes asociadas a un pedido
	 */
	private static String strConDevolucionesPendientes="SELECT  " +
														"t.codigo_articulo AS codigo_articulo, " +
														"CASE WHEN  " + 
														"sum(t.cantidad_despachada) - sum(t.cantidad_consumo)  " +
														"-sum(t.cantidad_devolucion_generada) - sum(t.cantidad_devolucion_recibida) > 0 " +
														"THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS devolucion " +
														"FROM( " +
															"SELECT va.codigo AS codigo_articulo, " +
															"ddp.cantidad AS cantidad_despachada, " +
															"0 AS cantidad_consumo, " +
															"0 AS cantidad_devolucion_generada, " +
															"0 AS cantidad_devolucion_recibida, " +
															"pp.peticion AS peticion " +
															"FROM pedidos_peticiones_qx pp  " +
															"INNER JOIN pedido p ON(p.codigo=pp.pedido)  " +
															"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido=p.codigo) " +
															"INNER JOIN articulo va ON(va.codigo=ddp.articulo) " +
															"WHERE p.estado IN ("+ConstantesBD.codigoEstadoPedidoDespachado+","+ConstantesBD.codigoEstadoPedidoTerminado+")  " +
														"UNION ALL " +
															"SELECT dm.articulo AS codigo_articulo, " +
															"0 AS cantidad_despachada, " +
															"dm.cantidad AS cantidad_consumo, " +
															"0 AS cantidad_devolucion_generada, " +
															"0 AS cantidad_devolucion_recibida, " +
															"sc.codigo_peticion AS peticion " +
															"FROM solicitudes_cirugia sc  " +
															"INNER JOIN det_materiales_qx dm ON(dm.numero_solicitud=sc.numero_solicitud)  " +
														"UNION ALL " +
															"SELECT ddp.articulo AS codigo_articulo, " +
															"0 AS cantidad_despachada, " +
															"0 AS cantidad_consumo, " +
															"ddp.cantidad AS cantidad_devolucion_generada, " +
															"0 AS cantidad_devolucion_recibida, " +
															"ppq.peticion As peticion " +
															"FROM pedidos_peticiones_qx ppq " +
															"INNER JOIN pedido p ON(p.codigo=ppq.pedido)  " +
															"INNER JOIN detalle_devol_pedido ddp ON(ddp.pedido=p.codigo) " +
															"INNER JOIN devolucion_pedidos dp ON(dp.codigo=ddp.devolucion) " +
															"WHERE p.estado IN ("+ConstantesBD.codigoEstadoPedidoDespachado+","+ConstantesBD.codigoEstadoPedidoTerminado+") " +
															"AND dp.estado IN ("+ConstantesBD.codigoEstadoDevolucionGenerada+","+ConstantesBD.codigoEstadoDevolucionRecibida+")  " +
														") t  WHERE t.peticion = ? " +
														"GROUP BY t.codigo_articulo ";

	
	
	/**
	 * Cadena para consultar las solicitudes de cirugia de la cuenta de un paciente
	 */
	private static String strConSolicitudesCirugia = "SELECT "+
														"s.numero_solicitud AS solicitud, "+
														"s.consecutivo_ordenes_medicas AS ordenmedica, "+
														"to_char(s.fecha_solicitud, 'DD/MM/YYYY') AS fechasolicitud, "+
														"s.hora_solicitud AS horasolicitud, "+
														"getestadosolhis(s.estado_historia_clinica) AS estadomedico, "+
														"s.centro_costo_solicitante AS codigocentrocostosolicita, "+
														"getnomcentrocosto(s.centro_costo_solicitante) AS centrocostosolicita, "+
														"s.centro_costo_solicitado AS codigocentrocostoejecuta, "+
														"getnomcentrocosto(s.centro_costo_solicitado) AS centrocostoejecuta, "+
														"cu.codigo_paciente AS codigopaciente, "+
														"getnombrepersona(cu.codigo_paciente) AS nombrepaciente, "+
														"getidpaciente(cu.codigo_paciente) AS idpaciente, "+
														"getfechanacimientopaciente(cu.codigo_paciente) AS fechanacimiento, "+
														"getnombreconvenio(getconvenioxingreso(cu.id_ingreso)) AS convenio "+
													"FROM "+
														"cuentas cu "+
														"INNER JOIN solicitudes s ON (s.cuenta = cu.id) "+
														"INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = s.numero_solicitud) "+
														"INNER JOIN materiales_qx mat ON (mat.numero_solicitud = sc.numero_solicitud) "+
													"WHERE "+
														"cu.id = ? "+
														"AND s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" "+
														"AND s.estado_historia_clinica<>4 "+
														"AND mat.finalizado = '"+ConstantesBD.acronimoSi+"' "+
														"AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"') "+
													"ORDER BY "+
														"s.fecha_solicitud DESC, s.hora_solicitud DESC";
	
	/**
	 * Cadena para consultar el detalle de la peticion de cirugia seleccionada
	 */
	private static String strConDetallePeticion = "SELECT " +
													"ser.codigo AS codigoservicio, " +
													"getnombreservicio(ser.codigo,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio " + 
												"FROM peticion_qx pqx " + 				
												"INNER JOIN peticiones_servicio ps ON ( ps.peticion_qx  = pqx.codigo ) " + 	
												"INNER JOIN servicios ser ON ( ser.codigo = ps.servicio ) " + 		
												"WHERE pqx.codigo = ? " +
												"ORDER BY ps.numero_servicio ";  
	
	/**
	 * Cadena para consultar el detalle de los articulos consumidos para cada una de las cirugias de la solicitud seleccionada
	 */
	private static String strConDetalleArticulosSolicitud = "SELECT "+
		"ddp.articulo as codigoarticulo, "+
		"getdescarticulo(ddp.articulo) AS descripcionarticulo, "+ 
		"getunidadmedidaarticulo(ddp.articulo) AS unidadmedida, "+ 
		"sum(ddp.cantidad) as cantidaddespachada, "+
		"getconsumosolicitud(ddp.articulo,sc.numero_solicitud) as cantidadconsumo, "+
		"getcantdevueltasolicitud(ddp.articulo,sc.numero_solicitud) as cantidaddevuelta, "+ 
		"(sum(ddp.cantidad) - getconsumosolicitud(ddp.articulo,sc.numero_solicitud) - getcantdevueltasolicitud(ddp.articulo,sc.numero_solicitud)) AS diferencia "+ 
		"FROM solicitudes_cirugia sc "+ 
		"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion = sc.codigo_peticion) "+ 
		"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido = ppq.pedido) "+ 
		"WHERE sc.numero_solicitud = ? "+ 
		"GROUP BY ddp.articulo,sc.numero_solicitud";
		
	/**
	 * Cadena para consultar el detalle de los articulos consumidos para cada una de las cirugias de la peticion seleccionada
	 */
	private static String strConDetalleArticulosPeticion="SELECT " +
															"t.codigoarticulo AS codigoarticulo, " +
															"t.descripcionarticulo AS descripcionarticulo, " +
															"t.unidadmedida AS unidadmedida, " +
															"t.cantidaddespachada AS cantidaddespachada, " +
															"t.cantidadconsumo AS cantidadconsumo, " +
															"t.cantidaddevuelta AS cantidaddevuelta, " +
															"t.diferencia AS diferencia " +
														"FROM( " +
															"SELECT " +
																"sc.codigo_peticion as peticion, " +
																"ddp.articulo as codigoarticulo, " +
																"getdescarticulo(ddp.articulo) AS descripcionarticulo, " + 
																"getunidadmedidaarticulo(ddp.articulo) AS unidadmedida,  " +
																"sum(ddp.cantidad) as cantidaddespachada, " +
																"getconsumosolicitud(ddp.articulo,sc.numero_solicitud) as cantidadconsumo, " +
																"getcantdevueltasolicitud(ddp.articulo,sc.numero_solicitud) as cantidaddevuelta, " +
																"(sum(ddp.cantidad) - getconsumosolicitud(ddp.articulo,sc.numero_solicitud) - getcantdevueltasolicitud(ddp.articulo,sc.numero_solicitud)) AS diferencia " +  
															"FROM solicitudes_cirugia sc " +
															"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion = sc.codigo_peticion) " +  
															"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido = ppq.pedido) " + 
															"GROUP BY ddp.articulo,sc.codigo_peticion,sc.numero_solicitud,ddp.articulo " +
														"UNION " +
															"SELECT " +
																"ppq.peticion as peticion, " +
																"ddp.articulo as codigoarticulo, " + 
																"getdescarticulo(ddp.articulo) AS descripcionarticulo, " + 
																"getunidadmedidaarticulo(ddp.articulo) AS unidadmedida, " + 
																"sum(ddp.cantidad) as cantidaddespachada, " +
																"0 as cantidadconsumo, " +
																"getcantdevueltapeticion(ddp.articulo,ppq.peticion) as cantidaddevuelta, " +
																"(sum(ddp.cantidad) - getcantdevueltapeticion(ddp.articulo,ppq.peticion)) AS diferencia " + 
															"FROM pedidos_peticiones_qx ppq " +
															"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido = ppq.pedido) " + 
															"WHERE ppq.peticion NOT IN(SELECT codigo_peticion " +
															"FROM solicitudes_cirugia WHERE codigo_peticion IS NOT NULL) " +
															"GROUP BY ddp.articulo,ppq.peticion,ddp.articulo" +
														")t " +
														"WHERE t.peticion = ? "; 
	
	
	/**
	 * Cadena para consultar el detalle de los articulos consumidos para cada una de las cirugias de la solicitud seleccionada
	 */
	private static String strConDetalleArticulosSolicitudFact =	"SELECT "+
																	"dp.articulo AS codigoarticulo, "+
																	"getdescarticulo(dp.articulo) AS descripcionarticulo, "+
																	"getunidadmedidaarticulo(dp.articulo) AS unidadmedida, "+
																	"getcantidadtotaldespachada(dp.articulo, sc.numero_solicitud) AS cantidaddespachada, "+
																	//" sum(dp.cantidad) AS cantidadconsumo, "+
																	"getconsumosolicitud(dp.articulo,sc.numero_solicitud) as cantidadconsumo, "+
																	"getcantdevueltasolicitud(dp.articulo,sc.numero_solicitud) as cantidaddevuelta, " +
																	"(getcantidadtotaldespachada(dp.articulo, sc.numero_solicitud) - getconsumosolicitud(dp.articulo,sc.numero_solicitud) - getcantdevueltasolicitud(dp.articulo,sc.numero_solicitud)) AS diferencia "+
																"FROM  solicitudes_cirugia sc "+ 
																"inner join pedidos_peticiones_qx ppq ON(ppq.peticion = sc.codigo_peticion) "+
																"inner join detalle_pedidos dp ON(dp.pedido = ppq.pedido) "+
																"WHERE "+
																	"sc.numero_solicitud = ? "+
																"GROUP BY dp.articulo, sc.numero_solicitud";
														
	/**
	 * Cadena para Group By y Order By de la consulta de peticiones por Rangos 
	 */
	private static String strGrOrdSolicitudesPorRangos = "ORDER BY t.fechapeticion desc, t.horapeticion desc ";
	
	/**
	 * Cadena para consultar los almacenes asociados al articulo de consumo de la solicitud de cirugia
	 */
	private static String strConAlmacenes = "SELECT "+
												"p.centro_costo_solicitado AS codigoalmacen, "+
												"p.codigo AS numeropedido, "+
												"getnomcentrocosto(p.centro_costo_solicitado) AS nombrealmacen, "+
												"sum(ddp.cantidad) AS netodespachado, "+
												"coalesce(ddp.lote, '') AS lote, "+
												"coalesce(to_char(ddp.fecha_vencimiento, 'DD/MM/YYYY'), '') AS fechavencimiento, " +
												"coalesce(getcantdevueltapedido(ddp.pedido,ddp.articulo), -1) AS netodevolucion "+
											"FROM "+
												"peticion_qx pq "+
												"INNER JOIN pedidos_peticiones_qx ppq ON (ppq.peticion = pq.codigo) "+
												"INNER JOIN pedido p ON (p.codigo = ppq.pedido) "+
												"INNER JOIN detalle_despacho_pedido ddp ON (ddp.pedido = ppq.pedido) "+
											"WHERE "+
												"pq.codigo = ? "+
												"AND ddp.articulo = ? "+
												"AND p.estado = "+ConstantesBD.codigoEstadoPedidoDespachado+" "+
											"GROUP BY "+
												"p.centro_costo_solicitado, p.codigo,ddp.pedido, ddp.articulo, ddp.lote, ddp.fecha_vencimiento "+
											"ORDER BY "+
												"p.centro_costo_solicitado";
	
	/**
	 * Cadena para insertar la devolucion del pedido de la solicitud de cirugia seleccionada en devolucion_med
	 */
	private static String strInsDevolucionPedidos = "INSERT INTO devolucion_pedidos "+
													"(codigo, motivo, fecha, hora, fecha_grabacion, hora_grabacion, usuario, estado, institucion, observaciones, es_qx) "+
													"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	/**
	 * Cadena para insertar el detalle de la devolucion del pedido de la solicitud de la cirugia seleccionada en detalle_devol_med
	 */
	private static String strInsDetalleDevolucionPedidos = "INSERT INTO detalle_devol_pedido "+
															"(codigo, devolucion, pedido, articulo, cantidad, lote, fecha_vencimiento) "+
															"VALUES (?, ?, ?, ?, ?, ?, ?) ";
	
	
	/**
	 * Metodo que arroja el listado de solicitudes
	 * para la cuenta del paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static HashMap listadoSolicitudes(Connection con, int codigoCuenta)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSolicitudesCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoCuenta);
        	logger.info("\n\n====>Consulta Solicitudes Cirugia: "+strConSolicitudesCirugia);
        	logger.info("\n\n====>Codigo de la Cuenta: "+codigoCuenta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE SOLICITUDES DE CIRUGIA PARA LA CUENTA ASIGNADA");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * Metodo que valida si una peticion de cirugia
	 * tiene devoluciones en esta pendiente
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	public static String validarPeticionDevolucionesPendientes(Connection con, int codigoPeticion,String consulta)
	{
		
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoPeticion);
        	logger.info("\n\n====>Consulta Peticiones con Devoluciones Pendientes: "+consulta);
        	logger.info("\n\n====>Codigo de la Peticion: "+codigoPeticion);
        	ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
        	if (rs.next())
        		return rs.getString(1);
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE PETICIONES CON DEVOLUCIONES PENDIENTES");
            e.printStackTrace();
            return "";
        }
        return "";
	}
	
	/**
	 * Metodo que consulta el detalle de una peticion de cirugia
	 * retornando todos los datos en un mapa
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public static HashMap<String, Object> detallePeticion(Connection con, int codigoPeticion)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        
        PreparedStatementDecorator ps = null;
        
        try
        {
        	ps =  new PreparedStatementDecorator(con.prepareStatement(strConDetallePeticion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoPeticion);
            
        	logger.info("\n\n====>Consulta Detalle Servicios de la Peticion de Cirugia: "+strConDetallePeticion);
        	logger.info("\n\n====>Codigo de la Peticion: "+codigoPeticion);
        	
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERRORdetallePeticion: ", e);
            e.printStackTrace();
        
        }catch(Exception ex){
			logger.error("ERROR detallePeticion: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
        return (HashMap<String, Object>)mapa.clone();
	}
	
	/**
	 * Metodo que consulta el consumo de articulos
	 * de cada una de las cirugias que tiene la 
	 * peticion seleccionada
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	
	public static HashMap<String,Object> detallePeticionArticulos(Connection con, int codigoPeticion){
		
		HashMap<String, Object> mapa = new HashMap<String, Object>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try
        {
        	pst =  con.prepareStatement(strConDetalleArticulosPeticion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
        	pst.setInt(1, codigoPeticion);
        	
        	logger.info("detallePeticionArticulos: "+pst);
        	
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
			mapa.put("numRegistros", cont);
        	
        }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR detallePeticionArticulos",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR detallePeticionArticulos", e);
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
        return (HashMap<String, Object>)mapa.clone();
	}
	/**
	 * Metodo que consulta el consumo de articulos
	 * de cada una de las cirugias que tiene la 
	 * solicitud seleccionada
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap detalleSolicitudArticulos(Connection con, int codigoSolicitud, boolean validacionFactura)
	{
		HashMap mapa = new HashMap<String, Object>();
        PreparedStatement pst=null;
        ResultSet rs=null;
		try
        {
        	String consulta=validacionFactura?strConDetalleArticulosSolicitudFact:strConDetalleArticulosSolicitud;
        	pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setInt(1, codigoSolicitud);
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
			logger.error("############## SQLException ERROR detalleSolicitudArticulos",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR detalleSolicitudArticulos", e);
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
        return (HashMap)mapa.clone();
	}
	
	/**
	 * Metodo que consulta las solicitudes de cirugia
	 * segun los criterios especificados en la funcionalidad
	 * por rangos
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap<String,Object> listadoPeticionesPorRangos(Connection con, HashMap vo,String strConSolicitudesPorRangos)
	{
		PreparedStatementDecorator pst = null;
		ResultSet rs = null;
		
		PreparedStatementDecorator pst1 = null;
		ResultSet rs1 = null;
		
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        
        String consulta = strConSolicitudesPorRangos;
        
        //Filtramos la consulta por el centro de costo que devuelve. Como es requerido no se valida
        consulta += "AND t.centrocostosolicita = "+vo.get("centroCostoDevuelve")+" ";
        
        //Filtramos la consulta por el centro de costo que ejecuta. Se requiere validar
        if(!(vo.get("centroCostoEjecuta")+"").equals("") && !(vo.get("centroCostoEjecuta")+"").equals("null"))
			consulta += "AND t.centrocostosolicitado = "+vo.get("centroCostoEjecuta")+" ";
        
        //Filtramos la consulta por la fecha inicial y la fecha final
        if(!(vo.get("fechaInicial")+"").equals("") && !(vo.get("fechaInicial")+"").equals("null") && !(vo.get("fechaFinal")+"").equals("") && !(vo.get("fechaFinal")+"").equals("null"))
        	consulta += "AND to_char(t.fechapeticion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' ";
        
        try
        {
        	pst =  new PreparedStatementDecorator(con.prepareStatement(consulta+strGrOrdSolicitudesPorRangos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	pst.setInt(1, Integer.parseInt(vo.get("centroAtencion")+""));
        	
        	logger.info("\n\n====>Consulta Peticiones Por Rangos: "+consulta+strGrOrdSolicitudesPorRangos);
        	
        	rs = pst.executeQuery();
        	
        	ResultSetMetaData rsm=rs.getMetaData();
        	int cont=0;
        	
        	while(rs.next()){
        		
    			pst1 = new PreparedStatementDecorator(con.prepareStatement(strConDevolucionesPendientes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    			pst1.setInt(1, rs.getInt("peticion"));
        		
            	logger.info("\n\n====>Consulta Devoluciones Pendientes: "+pst1);

            	rs1 = pst1.executeQuery();
            	
            	devPendiente : while(rs1.next()){		
            		if(rs1.getString("devolucion").equals(ConstantesBD.acronimoSi)){
            			for(int i=1;i<=rsm.getColumnCount();i++){
        					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
        				}
        				cont++;
        				break devPendiente;
            		}
            	}
			}
        	
            mapa.put("numRegistros",cont);      
        	
        }catch (SQLException e){
            logger.error("ERROR SQLException listadoSolicitudesPorRangos: ", e);
        
        }catch(Exception ex){
			logger.error("ERROR Exception listadoSolicitudesPorRangos: ", ex);
		
        }finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
				if(rs1 != null){
					rs1.close();
				}
				if(pst1 != null){
					pst1.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
        return (HashMap)mapa.clone();
	}
	
	/**
	 * Metodo que consulta los almacenes por el articulo
	 * seleccionado
	 * @param con
	 * @param codigoPeticion
	 * @param codigoArticulo
	 * @return
	 */
	public static HashMap<String,Object> consultarAlmacenes(Connection con, int codigoPeticion, int codigoArticulo)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        
        PreparedStatementDecorator ps = null;
        
        try
        {
        	ps =  new PreparedStatementDecorator(con.prepareStatement(strConAlmacenes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, codigoPeticion);
        	ps.setInt(2, codigoArticulo);
        	
            logger.info("\n\n====>Consulta Almacenes por Articulo: "+strConAlmacenes);
            logger.info("\n====>Codigo de la Peticion: "+codigoPeticion);
            logger.info("\n====>Codigo del Articulo: "+codigoArticulo);
            
        	mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        
        }catch (SQLException e){
            logger.error("SQLException consultarAlmacenes: ", e);
        
        }catch(Exception ex){
			logger.error("Exception consultarAlmacenes: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
        return (HashMap<String,Object>)mapa.clone();
	}
	
	/**
	 * Metodo que inserta la devolucion
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int devolucionPedidoQx(Connection con, HashMap vo)
	{
		int seqDevolucion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_devolucion_pedidos");
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsDevolucionPedidos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO devolucion_pedidos "+
							"(codigo, motivo, fecha, hora, fecha_grabacion, hora_grabacion, usuario, estado, institucion, observaciones, es_qx) "+
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
			 */
			
			ps.setInt(1, seqDevolucion);
	  		ps.setString(2, vo.get("motivo")+"");
	  		ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(4, UtilidadFecha.getHoraActual(con));
	  		ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(6, UtilidadFecha.getHoraActual(con));
	  		ps.setString(7, vo.get("usuario")+"");
	  		ps.setInt(8, ConstantesBD.codigoEstadoDevolucionGenerada);
	  		ps.setInt(9, Utilidades.convertirAEntero(vo.get("institucion")+""));
	  		
	  		if(UtilidadCadena.noEsVacio(vo.get("observaciones")+""))
	  			ps.setString(10, vo.get("observaciones")+"");
	  		else
	  			ps.setNull(10, Types.VARCHAR);
	  		
	  		ps.setString(11, ConstantesBD.acronimoSi);
	  		
	  		if(ps.executeUpdate()>0)
	  		{
	  			logger.info("SE INSERTO CORRECTAMENTE EL REGISTRO EN devolucion_pedidos");
	  			return seqDevolucion;
	  		}
	  		else
	  		{
	  			logger.info("NO SE GRABO INFORMACION EN devolucion_pedidos");
	  			return ConstantesBD.codigoNuncaValido;
	  		}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("NO SE GRABO INFORMACION EN devolucion_pedidos");
			return ConstantesBD.codigoNuncaValido;			
		}
	}
	
	/**
	 * Metodo que ejecuta la devolucion de la solicitud
	 * de cirugia seleccionada
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean devolverDetallePedidoQx(Connection con, HashMap vo, int codigoDevolucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsDevolucionPedidos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
        	ps =  new PreparedStatementDecorator(con.prepareStatement(strInsDetalleDevolucionPedidos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	
        	/**
        	 * INSERT INTO detalle_devol_pedido "+
															"(codigo, devolucion, pedido, articulo, cantidad, lote, fecha_vencimiento) "+
															"VALUES (?, ?, ?, ?, ?, ?, ?)
        	 */
        	
        	ps.setInt(1, Utilidades.convertirAEntero(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_detalle_devol_pedido")+""));
        	ps.setInt(2, codigoDevolucion);
        	ps.setInt(3, Utilidades.convertirAEntero(vo.get("pedido")+""));
        	ps.setInt(4, Utilidades.convertirAEntero(vo.get("articulo")+""));
        	ps.setInt(5, Utilidades.convertirAEntero(vo.get("cantidad")+""));
        		
        	if(UtilidadCadena.noEsVacio(vo.get("lote")+""))
    	  		ps.setString(6, vo.get("lote")+"");
    	  	else
    	  		ps.setNull(6, Types.VARCHAR);
        		
        	if(UtilidadCadena.noEsVacio(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaVencimiento")+"")))
    	  		ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaVencimiento")+"")));
    	  	else
    	  		ps.setNull(7, Types.DATE);
        	
        	if(ps.executeUpdate()>0)
        	{
	  			logger.info("SE INSERTO CORRECTAMENTE EL REGISTRO EN detalle_devol_pedido");
	  			return true;
        	}
	  		else
	  		{
	  			logger.info("NO SE GRABO INFORMACION EN detalle_devol_pedido");
        		return false;
	  		}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("NO SE GRABO INFORMACION EN detalle_devol_pedido");
			return false;
		}
	}

	/**
	 * Metodo que devuelve el estado de la 
	 * devolucion segun la devolucion insertada
	 * @param con
	 * @param codigoDevolucion
	 * @return
	 */
	public static String consultarEstadoDevolucion(Connection con, String codigoDevolucion)
	{
		String consulta = "SELECT ed.descripcion AS estado FROM devolucion_pedidos dp INNER JOIN estados_devolucion ed ON(dp.estado = ed.codigo) WHERE dp.codigo = ?";
		ResultSetDecorator rs;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoDevolucion));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
			else
				return "";
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.info("ERROR CONSULTANDO EL ESTADO DE LA DEVOLUCION DE PEDIDOS");
			return "";
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static Vector<String> obtenerPedidosPeticionesQx(Connection con, int numeroSolicitud)
	{
		Vector<String> vector= new Vector<String>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT ppq.pedido||'' FROM pedidos_peticiones_qx ppq INNER JOIN solicitudes_cirugia sc ON(ppq.peticion=sc.codigo_peticion) WHERE sc.numero_solicitud =? order by ppq.pedido ";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			rs = pst.executeQuery();
			while(rs.next())
			{
				vector.add((rs.getString(1)!=null)?rs.getString(1):"");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerPedidosPeticionesQx",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerPedidosPeticionesQx", e);
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
	 * Metodo que arroja el listado de peticiones
	 * del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String,Object> listadoPeticiones(Connection con, int codigoIngreso, int codigoInstitucion){
		
		PreparedStatementDecorator pst=null;
		ResultSet rs=null;
		
		PreparedStatementDecorator pst1=null;
		ResultSet rs1=null;
		
		HashMap<String,Object> mapa = new HashMap<String, Object>();
		
        try{          
            
			pst = new PreparedStatementDecorator(con.prepareStatement(strConPeticionesCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1, codigoInstitucion);
            pst.setInt(2, codigoIngreso);
            
        	logger.info("\n\n====>Consulta Peticiones Cirugia: "+pst);
        	
        	rs = pst.executeQuery();
			
        	ResultSetMetaData rsm=rs.getMetaData();
        	int cont=0;
        	
        	while(rs.next()){
        		
    			pst1 = new PreparedStatementDecorator(con.prepareStatement(strConDevolucionesPendientes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    			pst1.setInt(1, rs.getInt("peticion"));
        		
            	logger.info("\n\n====>Consulta Devoluciones Pendientes: "+pst1);

            	rs1 = pst1.executeQuery();
            	
            	devPendiente : while(rs1.next()){		
            		if(rs1.getString("devolucion").equals(ConstantesBD.acronimoSi)){
            			for(int i=1;i<=rsm.getColumnCount();i++){
        					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
        				}
        				cont++;
        				break devPendiente;
            		}
            	}
			}
        	
            mapa.put("numRegistros",cont);
	
        }
        catch (SQLException e){
            logger.error("ERROR listadoPeticiones: ");
            e.printStackTrace();
        
        }catch(Exception e){
			logger.error("ERROR listadoPeticiones: ", e);
		
        }finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
				if(rs1 != null){
					rs1.close();
				}
				if(pst1 != null){
					pst1.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
        return (HashMap<String,Object>)mapa.clone();		
	}
}