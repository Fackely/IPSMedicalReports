package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseConsultaDevolucionInventarioPacienteDao
{
	private static Logger logger = Logger.getLogger(SqlBaseConsultaDevolucionInventarioPacienteDao.class);
	
	private static String [] indicesMapCC={"codigo_","nombre_","centro_atencion_"};
	
	private static String [] indicesMapE={"codigo_","descripcion_"};
	
	private static String [] indicesMapLI={"descripcion_","idIng_","fecha_ingreso_","hora_ingreso_","estado_","fecha_egreso_","hora_egreso_","idcue_","nomEst_","nomVia_"};
	
	private static String [] indicesMapIC={"numdev_","fechadev_","estadodev_","centrocdev_","nomFarmacia_","valor_","cpdv_","campod_"};
	
	private static String [] indicesMapApsIC={"codigod_","campod_","fecha_","hora_","descripcione_","nombre_","nom_almacen","valor_","usuariod_","usuarior_","codigoa_","descripciona_","concentracion_","forma_farmaceutica_","unidad_medida_","pedido_","numero_solicitud_","cantidad_","cantidadrecibida_","descripcionm_","observaciones_"};
	
	private static String consultaStrCC="SELECT cc.codigo, cc.nombre, cc.centro_atencion FROM centros_costo cc WHERE cc.tipo_area=1";
	
	private static String consultaStrE="SELECT ed.codigo, ed.descripcion FROM estados_devolucion ed";
	
	private static String consultaStrM="SELECT md.codigo, md.descripcion FROM mot_devolucion_inventario md";
	
	private static String consultaStrLI="SELECT ca.descripcion, i.id AS idIng, i.fecha_ingreso, i.hora_ingreso, i.estado, " +
												"i.fecha_egreso, i.consecutivo as consingreso, i.hora_egreso, c.id AS idcue, ec.nombre AS nomEst, vi.nombre AS nomVia " +
										"FROM ingresos i INNER JOIN centro_atencion ca ON (i.centro_atencion = ca.consecutivo) " +
												"INNER JOIN cuentas c ON (c.id_ingreso = i.id)  " +
												"INNER JOIN estados_cuenta ec ON (c.estado_cuenta = ec.codigo) " +
												"INNER JOIN vias_ingreso vi ON (c.via_ingreso = vi.codigo) " +
										"WHERE i.codigo_paciente = ? ORDER BY i.fecha_ingreso";
	
	public static String consultaStrIC="";
	
	public static String consultaStrAsIC="";
	
	public static String cadena="";
	
	
	public static HashMap<String, Object> consultaCentroCosto (Connection con)
	{
		HashMap<String, Object> resultadosCC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrCC, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosCC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosCC.put("INDICES",indicesMapCC);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Centros de Costo");
		}
		return resultadosCC;
	}
	
	public static HashMap<String, Object> consultaEstadosDevolucion (Connection con)
	{
		HashMap<String, Object> resultadosED = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrE, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosED = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosED.put("INDICES",indicesMapE);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Estados Devolucion");
		}
		return resultadosED;
	}
	
	public static HashMap<String, Object> consultaMotivosDevolucion (Connection con)
	{
		HashMap<String, Object> resultadosMD = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrM, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosMD = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosMD.put("INDICES",indicesMapE);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los Motivos de la Devolucion");
		}
		return resultadosMD;
	}
	
	public static HashMap<String, Object> consultaListadoIngresos (Connection con, int codigoPaciente)
	{
		HashMap<String, Object> resultadosLI = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			logger.info("--->"+consultaStrLI);
			logger.info("--->"+codigoPaciente);
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrLI, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1, codigoPaciente);
			
			resultadosLI = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosLI.put("INDICES",indicesMapLI);
			pst.close();
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los Ingresos del Paciente");
		}
		return resultadosLI;
	}
	
	public static HashMap<String, Object> consultaDetalleIC (Connection con, String codigoCuenta)
	{
		HashMap<String, Object> resultadosLI = new HashMap<String, Object>();		
		Statement pst;
		
		/**
		 * Modificación de consulta según incidencia 6088
		 * Autor: Alejandro Aguirre Luna
		 * Usuario: aleagulu
		 * Fecha: 15/01/2013 
		 * Descripción: La consulta anterior (la comenatada despues de esta) no
		 * 				estaba mostrando los registros con estado 'Generada'. 
		 * 				Se hizo una reestructuración de la cnosulta, entre los 
		 * 				cambios más significativos esta la no enunciación de
		 * 				de la relación entre el campo codigo de las tablas
		 * 				detalle_recep_medicamentos y detalle_devol_med. Al 
		 * 				igual que en los campos codigo de las tablas 
		 * 				detalle_recep_pedidos y detalle_devol_pedido.   
		 */
		
		//Declaración de consulta en la variable consultaStrIC
		consultaStrIC = "SELECT dvm.codigo      	 AS numdev," +
				" dvm.fecha                      	 AS fechadev," +
				" getEstadoDevolucion(dvm.estado)    AS estadodev," +
				" cc.nombre                          AS centrocdev," +
				" 'Solicitud'                        AS campod," +
				" getnombrealmacen(dvm.farmacia)     AS nom_farmacia," +
				" SUM(ddm.cantidad * detdes.costo_unitario) AS valor" +
				" FROM cuentas c" +
				" INNER JOIN solicitudes s ON (s.cuenta = c.id)" +
				" INNER JOIN detalle_devol_med ddm ON (s.numero_solicitud = ddm.numero_solicitud)" +
				" INNER JOIN devolucion_med dvm ON (ddm.devolucion = dvm.codigo)" +
				" INNER JOIN centros_costo cc ON (dvm.centro_costo_devuelve = cc.codigo)" +
				" INNER JOIN inventarios.despacho des ON s.numero_solicitud = des.numero_solicitud" +
				" INNER JOIN inventarios.detalle_despachos detdes ON detdes.despacho = des.orden" +
				" WHERE c.id =" + codigoCuenta +
				" GROUP BY dvm.codigo," +
					"dvm.fecha," +
					"dvm.estado," +
					"cc.nombre," +
					"c.codigo_paciente," +
					"dvm.farmacia" +
				" UNION ALL" +
				" SELECT DISTINCT dvp.codigo        AS numdev," +
				" dvp.fecha                         AS fechadev," +
				" getEstadoDevolucion(dvp.estado)   AS estadodev," +
				" cc.nombre                         AS centrocdev," +
				" 'Pedido'                          AS campod," +
				" getnombrealmacen(p.centro_costo_solicitado)   AS nom_farmacia," +
				" (SUM(drp.cantidadrecibida * drp.costo_unitario)) AS valor" +
				" FROM cuentas c" +
				" INNER JOIN solicitudes s" + " ON (s.cuenta = c.id)" +
				" INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = s.numero_solicitud)" +
				" INNER JOIN peticion_qx pqx ON (sc.codigo_peticion = pqx.codigo)" +
				" INNER JOIN pedidos_peticiones_qx ppqx ON (ppqx.peticion = pqx.codigo)" +
				" INNER JOIN pedido p ON (ppqx.pedido = p.codigo)" +
				" INNER JOIN detalle_devol_pedido ddp ON (ddp.pedido = p.codigo)" +
				" INNER JOIN devolucion_pedidos dvp ON (ddp.devolucion = dvp.codigo)" +
				" INNER JOIN centros_costo cc ON (p.centro_costo_solicitante = cc.codigo)" +
				" INNER JOIN detalle_recep_pedidos drp ON (drp.articulo=ddp.articulo)" +
				" WHERE c.id =" + codigoCuenta +
				" GROUP BY " +
					"dvp.codigo," +
					"dvp.fecha," +
					"dvp.estado," +
					"cc.nombre," +
					"p.centro_costo_solicitado ";
		
		//Consulta anterior
		/*
		consultaStrIC="SELECT	 DISTINCT dvm.codigo AS numdev, dvm.fecha AS fechadev, edv.descripcion AS estadodev, cc.nombre AS centrocdev, 'Solicitud' AS campod, " +
								"getnombrealmacen(dvm.farmacia) AS nom_farmacia, " +
								"(sum(drm.cantidadrecibida * drm.costo_unitario)) AS valor " +
						"FROM	 cuentas c " +
								"INNER JOIN solicitudes s ON (s.cuenta = c.id) " +
								"INNER JOIN detalle_devol_med ddm ON (s.numero_solicitud = ddm.numero_solicitud) " +
								"INNER JOIN devolucion_med dvm ON (ddm.devolucion = dvm.codigo) " +
								"INNER JOIN recepciones_medicamentos rm ON (rm.devolucion = dvm.codigo) " +	
								"INNER JOIN detalle_recep_medicamentos drm ON (drm.codigo = ddm.codigo AND drm.articulo=ddm.articulo) " +
								"INNER JOIN centros_costo cc ON (dvm.centro_costo_devuelve = cc.codigo) " +
								"INNER JOIN estados_devolucion edv ON (dvm.estado = edv.codigo) ";
		
		consultaStrIC += "WHERE	 c.id = " + codigoCuenta + " GROUP BY dvm.codigo,dvm.fecha,edv.descripcion,cc.nombre,c.codigo_paciente,dvm.farmacia " + 
						"UNION ALL " + 
						"SELECT	 DISTINCT dvp.codigo AS numdev, dvp.fecha AS fechadev, edv.descripcion AS estadodev, cc.nombre AS centrocdev, 'Pedido' AS campod, " + 
								"getnombrealmacen(p.centro_costo_solicitado) AS nom_farmacia, " + 
								"(sum(drp.cantidadrecibida * drp.costo_unitario)) AS valor " + 
						"FROM	 cuentas c " + 
								"INNER JOIN solicitudes s ON (s.cuenta = c.id) " + 
								"INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = s.numero_solicitud) " + 
								"INNER JOIN peticion_qx pqx ON (sc.codigo_peticion = pqx.codigo) " + 
								"INNER JOIN pedidos_peticiones_qx ppqx ON (ppqx.peticion = pqx.codigo) " + 
								"INNER JOIN pedido p ON (ppqx.pedido = p.codigo) " + 
								"INNER JOIN detalle_devol_pedido ddp ON (ddp.pedido = p.codigo) " + 
								"INNER JOIN devolucion_pedidos dvp ON (ddp.devolucion = dvp.codigo) " + 
								"INNER JOIN recepciones_pedidos rp ON (rp.devolucion = dvp.codigo) " + 
								"INNER JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo AND drp.articulo=ddp.articulo) " + 
								"INNER JOIN centros_costo cc ON (p.centro_costo_solicitante = cc.codigo) " + 
								"INNER JOIN estados_devolucion edv ON (dvp.estado = edv.codigo) " + 
						"WHERE	 c.id = " + codigoCuenta + " AND dvp.es_qx='S' GROUP BY dvp.codigo,dvp.fecha,edv.descripcion,cc.nombre,c.codigo_paciente,p.centro_costo_solicitado ";
		 */
		try{
			pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			String nuevaConsulta=consultaStrIC+"ORDER BY numdev";
			
			resultadosLI = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery(nuevaConsulta)), true, true);
			
			resultadosLI.put("INDICES",indicesMapIC);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta del Detalle de Ingresos");
		}
		return resultadosLI;
	}
	
	public static HashMap<String, Object> consultaDetalleAIC (Connection con, String codigoDevolucion, String tipoDevolucion)
	{
		HashMap<String, Object> resultadosLI = new HashMap<String, Object>();
		
		Statement pst;
		
		if(tipoDevolucion.equals("Solicitud")){
			consultaStrAsIC="";
			consultaStrAsIC="SELECT dvm.codigo AS codigod, dvm.fecha AS fecha, dvm.hora AS hora, ed.descripcion AS descripcione, " +
									"'Solicitud' AS campod, cc.nombre AS nombre, (SELECT cci.nombre FROM centros_costo cci WHERE s.centro_costo_solicitante = cci.codigo) AS nom_almacen, " +
									"dvm.usuario AS usuariod, rm.usuario AS usuarior, a.codigo AS codigoa, a.descripcion AS descripciona, a.concentracion AS concentracion, " +
									"ff.nombre AS forma_farmaceutica, a.unidad_medida AS unidad_medida, ddm.numero_solicitud AS numero_solicitud, ddm.cantidad AS cantidad, " +
									"drm.cantidadrecibida AS cantidadrecibida, mdi.descripcion AS descripcionm, dvm.observaciones AS observaciones, " +
									"getNombrePersona(per.codigo) AS nombrepersona, getNombreTipoIdentificacion(per.tipo_identificacion)||' '||per.numero_identificacion AS tiponumeroid, " +
									"ing.consecutivo AS numingreso, ing.fecha_ingreso AS fechaingreso, vi.nombre AS viaingreso, " +
									"(drm.cantidadrecibida * drm.costo_unitario) AS valor " +
								"FROM " +
								"devolucion_med dvm " +
										"INNER JOIN estados_devolucion ed ON (dvm.estado = ed.codigo) " +
										"INNER JOIN mot_devolucion_inventario mdi ON (dvm.motivo = mdi.codigo) " +
										"INNER JOIN detalle_devol_med ddm ON (ddm.devolucion = dvm.codigo) " +
										"INNER JOIN solicitudes s ON (s.numero_solicitud = ddm.numero_solicitud) " +
										"INNER JOIN cuentas cue ON (cue.id = s.cuenta) " +
										"INNER JOIN personas per ON (per.codigo = cue.codigo_paciente) " +
										"INNER JOIN ingresos ing ON (ing.id = cue.id_ingreso) " +
										"INNER JOIN vias_ingreso vi ON (vi.codigo = cue.via_ingreso) " +
										//MT6088  se cambia inner a left join ya que para las devoluciones generadas no hay datos de recepción
										"LEFT OUTER JOIN recepciones_medicamentos rm ON (rm.devolucion = dvm.codigo) " +
										"LEFT OUTER JOIN detalle_recep_medicamentos drm ON (drm.codigo = ddm.codigo AND drm.articulo=ddm.articulo) " +
										"INNER JOIN articulo a ON (ddm.articulo = a.codigo) " +
										"LEFT OUTER JOIN forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo AND a.institucion=ff.institucion) " +
										"INNER JOIN centros_costo cc ON (s.centro_costo_solicitado = cc.codigo) " +
								"WHERE " +
									"dvm.codigo = " + codigoDevolucion + " " +
								"ORDER BY a.descripcion ";
			
			try{
				pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				
				resultadosLI = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery(consultaStrAsIC)), true, true);
				
				resultadosLI.put("INDICES",indicesMapApsIC);
				logger.info("consulta solicitud : "+consultaStrAsIC+" codigoDevolucion: "+codigoDevolucion);
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en consulta del Detalle de Articulos de Ingresos de devol sol");
			}
		}
		else{
			if(tipoDevolucion.equals("Pedido")){
				consultaStrAsIC="";
				consultaStrAsIC="SELECT dvp.codigo AS codigod, dvp.fecha AS fecha, dvp.hora AS hora, ed.descripcion AS descripcione, " +
									"'Pedido' AS campod, cc.nombre AS nombre, (SELECT cci.nombre FROM centros_costo cci WHERE p.centro_costo_solicitante = cci.codigo) AS nom_almacen, " +
									"dvp.usuario AS usuariod, rp.usuario AS usuarior, a.codigo AS codigoa, a.descripcion AS descripciona, a.concentracion AS concentracion, " +
									"ff.nombre AS forma_farmaceutica, a.unidad_medida AS unidad_medida, ddp.pedido AS pedido, ddp.cantidad AS cantidad, " +
									"drp.cantidadrecibida AS cantidadrecibida, mdi.descripcion AS descripcionm, dvp.observaciones AS observaciones, " +
									"getNombrePersona(per.codigo) AS nombrepersona, getNombreTipoIdentificacion(per.tipo_identificacion)||' '||per.numero_identificacion AS tiponumeroid, " +
									"ing.consecutivo AS numingreso, ing.fecha_ingreso AS fechaingreso, vi.nombre AS viaingreso, " +
									"(drp.cantidadrecibida * drp.costo_unitario) AS valor " +
								"FROM " +
								"devolucion_pedidos dvp " +
										"INNER JOIN estados_devolucion ed ON (dvp.estado = ed.codigo) " +
										"INNER JOIN mot_devolucion_inventario mdi ON (dvp.motivo = mdi.codigo) " +
										"INNER JOIN detalle_devol_pedido ddp ON (ddp.devolucion = dvp.codigo) " +
										"INNER JOIN pedido p ON (p.codigo = ddp.pedido) " +
										"INNER JOIN pedidos_peticiones_qx ppqx ON (ppqx.pedido = p.codigo) " +
										"INNER JOIN peticion_qx pqx ON (pqx.codigo = ppqx.peticion) " + 
										"INNER JOIN solicitudes_cirugia sc ON (sc.codigo_peticion = pqx.codigo) " +
										"INNER JOIN solicitudes s ON (s.numero_solicitud = sc.numero_solicitud) " + 
										"INNER JOIN cuentas cue ON (cue.id = s.cuenta) " +
										"INNER JOIN personas per ON (per.codigo = cue.codigo_paciente) " +
										"INNER JOIN ingresos ing ON (ing.id = cue.id_ingreso) " +
										"INNER JOIN vias_ingreso vi ON (vi.codigo = cue.via_ingreso) " +
										//MT6088  se cambia inner a left join ya que para las devoluciones generadas no hay datos de recepción
										"LEFT OUTER JOIN recepciones_pedidos rp ON (rp.devolucion = dvp.codigo) " +
										"LEFT OUTER JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo AND drp.articulo=ddp.articulo) " +
										"INNER JOIN articulo a ON (ddp.articulo = a.codigo) " +
										"LEFT OUTER JOIN forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo AND a.institucion=ff.institucion) " +
										"INNER JOIN centros_costo cc ON (p.centro_costo_solicitado = cc.codigo) " +
								"WHERE " +
									"dvp.codigo = " + codigoDevolucion + " " +
								"ORDER BY a.descripcion ";
				try{
					pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					
					resultadosLI = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery(consultaStrAsIC)), true, true);
					
					resultadosLI.put("INDICES",indicesMapApsIC);
					logger.info("consulta pedido"+consultaStrAsIC+" codigoDevolucion: "+codigoDevolucion);
				}
				catch (SQLException e)
				{
					logger.error(e+" Error en consulta del Detalle de Articulos de Ingresos de devol ped");
				}
			}
		}
		return resultadosLI;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroDevolucion
	 * @param centroAtencion
	 * @param almacen
	 * @param centroCosto
	 * @param fechaIni
	 * @param fechaFin
	 * @param estadoDevolucion
	 * @param tipoDevolucion
	 * @param motivoDevolucion
	 * @param usuarioDevuelve
	 * @param usuarioRecibe
	 * @param sql 
	 * @return
	 */
	public static HashMap<String, Object> consultaDevR (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String tipoDevolucion, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe, String sql)
	{
		HashMap<String, Object> resultadosLI = new HashMap<String, Object>();
		cadena=sql;
		logger.info("SQL / "+sql);
		try {
			Statement pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadosLI = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery(sql)), true, true);
			resultadosLI.put("INDICES",indicesMapIC);
		}
		catch (SQLException e)
		{
			logger.error(e+"ERROR en  consultaDevR ", e);
		}
		return resultadosLI;
	}
}