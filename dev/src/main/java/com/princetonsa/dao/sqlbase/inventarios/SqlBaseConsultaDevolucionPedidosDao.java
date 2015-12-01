package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseConsultaDevolucionPedidosDao
{
	private static Logger logger = Logger.getLogger(SqlBaseEquivalentesDeInventarioDao.class);
	
	private static String [] indicesMap={"codigo_","nombre_","centro_atencion_"};
	
	private static String [] indicesMapE={"codigo_","descripcion_"};
	
	private static String [] indicesMapD={"codigo_","esQx_","fecha_","descripcion_","centroCostoSolicitado_","centroCostoSolicitante_","valor_","noma_","nomc_"};
	
	private static String [] indicesMapDD={"codigod_","es_qx_","fecha_","hora_","descripcione_","centro_costo_solicitado_","centro_costo_solicitante_","valor_","usuariod_","usuarior_","codigoa_","descripciona_","conc_","forma_farmaceutica_","unidad_medida_","pedido_","cantidad_","cantidadrecibida_","descripcionm_","observaciones_","ff_","um_","noma_","nomc_"};
	
	private static String consultaStr="SELECT cc.codigo, cc.nombre, cc.centro_atencion FROM centros_costo cc ORDER BY cc.nombre ";
	
	private static String consultaStrE="SELECT ed.codigo, ed.descripcion FROM estados_devolucion ed";
	
	private static String consultaStrM="SELECT md.codigo, md.descripcion FROM mot_devolucion_inventario md ORDER BY md.descripcion ";
	
	private static String consultaStrDp="SELECT dv.codigo, dv.es_qx, dv.fecha, ed.descripcion, " +
												"p.centro_costo_solicitado, p.centro_costo_solicitante, " +
												"(sum(drp.cantidadrecibida * drp.costo_unitario)) AS valor, " +
												"getnombrealmacen(p.centro_costo_solicitante) AS noma, " +
												"getnombrealmacen(p.centro_costo_solicitado) AS nomc " +
										"FROM " +
											"devolucion_pedidos dv " +
													"INNER JOIN estados_devolucion ed ON (dv.estado = ed.codigo) " +
													"INNER JOIN mot_devolucion_inventario mdi ON (dv.motivo = mdi.codigo) " +
													"INNER JOIN detalle_devol_pedido ddp ON (ddp.devolucion = dv.codigo) " +
													"INNER JOIN pedido p ON (p.codigo = ddp.pedido) " +
													"INNER JOIN recepciones_pedidos rp ON (rp.devolucion = dv.codigo) " +
													"INNER JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo) " +
										"WHERE " +
												"dv.codigo = ? " +
										"GROUP BY dv.codigo,dv.es_qx,dv.fecha,ed.descripcion,p.centro_costo_solicitado,p.centro_costo_solicitante ORDER BY dv.codigo ";
	
	public static String cadena;
	
	private static String consultaStrDD="SELECT dv.codigo AS codigod, " +
												"dv.es_qx, " +
												"to_char(dv.fecha, 'DD/MM/YYYY') AS fecha," +
												"dv.hora, " +
												"ed.descripcion AS descripcione, " +
												"p.centro_costo_solicitado, " +
												"p.centro_costo_solicitante, " +
												"getnombrealmacen(p.centro_costo_solicitante) AS noma, " +
												"getnombrealmacen(p.centro_costo_solicitado) AS nomc, " +
												"dv.usuario AS usuariod, " +
												"rp.usuario AS usuarior, " +
												"a.codigo AS codigoa, " +
												"a.descripcion AS descripciona, " +
												"a.concentracion AS conc, " +
												"a.forma_farmaceutica, " +
												"ff.nombre AS ff, " +
												"a.unidad_medida, " +
												"um.nombre AS um, " +
												"ddp.pedido, " +
												"ddp.cantidad, " +
												"drp.cantidadrecibida, " +
												"mdi.descripcion AS descripcionm, " +
												"dv.observaciones, " +
												"(drp.cantidadrecibida * drp.costo_unitario) AS valor " +
										"FROM " +
										"devolucion_pedidos dv " +
												"INNER JOIN estados_devolucion ed ON (dv.estado = ed.codigo) " +
												"INNER JOIN mot_devolucion_inventario mdi ON (dv.motivo = mdi.codigo) " +
												"INNER JOIN detalle_devol_pedido ddp ON (ddp.devolucion = dv.codigo) " +
												"INNER JOIN pedido p ON (p.codigo = ddp.pedido) " +
												"INNER JOIN recepciones_pedidos rp ON (rp.devolucion = dv.codigo) " +
												"INNER JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo) " +
												"INNER JOIN articulo a ON (ddp.articulo = a.codigo) " +
												"LEFT OUTER JOIN forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo AND a.institucion=ff.institucion) " +
												"LEFT OUTER JOIN unidad_medida um ON (a.unidad_medida=um.acronimo) " +
										"WHERE " +
											"dv.codigo = ? " +
										"GROUP BY dv.codigo,dv.es_qx,dv.fecha,dv.hora,ed.descripcion,p.centro_costo_solicitado,p.centro_costo_solicitante,dv.usuario,rp.usuario,a.codigo,a.descripcion,a.concentracion,a.forma_farmaceutica,ff.nombre,a.unidad_medida,um.nombre,ddp.pedido,ddp.cantidad,drp.cantidadrecibida,mdi.descripcion,dv.observaciones,drp.costo_unitario ORDER BY a.descripcion ";
	
	public static String cadena2;
	
	public static HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion)
	{
		HashMap<String, Object> resultadosCC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosCC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosCC.put("INDICES",indicesMap);
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
	
	public static HashMap<String, Object> consultaDevoluciones (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String check, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe)
	{
		HashMap<String, Object> resultadosDN = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		Statement pst2;

		cadena = "SELECT dv.codigo AS codigo, " +
						"dv.es_qx AS es_qx, " +
						"to_char(dv.fecha, 'DD/MM/YYYY') as fecha," +
						"ed.descripcion AS descripcion, " +
						"p.centro_costo_solicitado AS centro_costo_solicitado, " +
						"p.centro_costo_solicitante AS centro_costo_solicitante, " +
						"(sum(drp.cantidadrecibida * drp.costo_unitario)) AS valor, " +
						"getnombrealmacen(p.centro_costo_solicitante) AS noma, " +
						"getnombrealmacen(p.centro_costo_solicitado) AS nomc " +
				"FROM " +
					"devolucion_pedidos dv " +
							"INNER JOIN estados_devolucion ed ON (dv.estado = ed.codigo) " +
							"INNER JOIN mot_devolucion_inventario mdi ON (dv.motivo = mdi.codigo) " +
							"INNER JOIN detalle_devol_pedido ddp ON (ddp.devolucion = dv.codigo) " +
							"INNER JOIN pedido p ON (p.codigo = ddp.pedido) " +
							"INNER JOIN recepciones_pedidos rp ON (rp.devolucion = dv.codigo) " +
							"INNER JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo) " +
							"INNER JOIN centros_costo cc ON (p.centro_costo_solicitante = cc.codigo) " +
							"INNER JOIN articulo a ON (ddp.articulo = a.codigo) " +
							"INNER JOIN usuarios u ON (dv.usuario = u.login) " +
							"INNER JOIN personas per ON (per.codigo = u.codigo_persona) " +
				"WHERE ";
		
		if(!numeroDevolucion.equals("")){
			try{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrDp, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				pst.setInt(1, Utilidades.convertirAEntero(numeroDevolucion));
				
				resultadosDN = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				
				resultadosDN.put("INDICES",indicesMapD);
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en consulta de las Devoluciones con Numero");
			}
		}
		else{
			cadena += " cc.centro_atencion = "+centroAtencion;
			
			if(!almacen.equals("-1")){
				cadena += " AND p.centro_costo_solicitado = "+almacen;
			}
			
			if(!centroCosto.equals("-1")){
				cadena += " AND p.centro_costo_solicitante = "+centroCosto;
			}
			
			if(!fechaIni.equals("") && !fechaFin.equals("")){
				cadena += " AND dv.fecha BETWEEN TO_DATE('"+UtilidadFecha.conversionFormatoFechaAAp(fechaIni)+"','DD/MM/YYYY') AND TO_DATE('"+UtilidadFecha.conversionFormatoFechaAAp(fechaFin)+"','DD/MM/YYYY')";
			}
			
			if(!estadoDevolucion.equals("-1")){
				cadena += " AND dv.estado = "+estadoDevolucion;
			}
			
			if(check.equals("S")){
				cadena += " AND dv.es_qx = 'S' ";
			}
			
			if(!motivoDevolucion.equals("-1")){
				cadena += " AND dv.motivo = '"+motivoDevolucion+"' ";
			}
			
			if(!usuarioDevuelve.equals("")){
				cadena += " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%"+usuarioDevuelve+"%') ";
			}
			
			if(!usuarioRecibe.equals("")){
				cadena += " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%"+usuarioRecibe+"%') ";
			}
			
			cadena += " GROUP BY dv.codigo,dv.es_qx,dv.fecha,ed.descripcion,p.centro_costo_solicitado,p.centro_costo_solicitante ORDER BY dv.codigo ";
			logger.info("CONSULTA FINAL DE DEVOLUCION PEDIDOS>>>>>>>>>>>>>>"+cadena);
			try{
				pst2 = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				
				resultadosDN = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery(cadena)), true, true);
				
				resultadosDN.put("INDICES",indicesMapD);
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en consulta de las Devoluciones sin Numero");
			}
		}
		return resultadosDN;
	}
	
	public static HashMap<String, Object> consultaDetalleDevoluciones (Connection con, String codigoDevolucion)
	{
		logger.info("CONSULTAAAAA DETAALLEEEE>>>>>>>>>>>>>"+consultaStrDD);
		HashMap<String, Object> resultadosDD = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrDD, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setString(1, codigoDevolucion);
			
			resultadosDD = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosDD.put("INDICES",indicesMapDD);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los Detalles de las Devoluciones");
		}
		cadena2 = "SELECT dv.codigo AS codigod, dv.es_qx AS es_qx, dv.fecha AS fecha, dv.hora AS hora, ed.descripcion AS descripcione, " +
						"p.centro_costo_solicitado AS centro_costo_solicitado, p.centro_costo_solicitante AS centro_costo_solicitante, " +
						"dv.usuario AS usuariod, rp.usuario AS usuarior, a.codigo AS codigoa, a.descripcion AS descripciona, a.concentracion AS concentracion, " +
						"ff.nombre AS forma_farmaceutica, um.nombre AS unidad_medida, ddp.pedido AS pedido, ddp.cantidad AS cantidad, " +
						"getnombrealmacen(p.centro_costo_solicitante) AS noma, " +
						"getnombrealmacen(p.centro_costo_solicitado) AS nomc, " +
						"drp.cantidadrecibida AS cantidadrecibida, mdi.descripcion AS descripcionm, dv.observaciones AS observaciones, " +
						"(drp.cantidadrecibida * drp.costo_unitario) AS valor " +
					"FROM " +
					"devolucion_pedidos dv " +
							"INNER JOIN estados_devolucion ed ON (dv.estado = ed.codigo) " +
							"INNER JOIN mot_devolucion_inventario mdi ON (dv.motivo = mdi.codigo) " +
							"INNER JOIN detalle_devol_pedido ddp ON (ddp.devolucion = dv.codigo) " +
							"INNER JOIN pedido p ON (p.codigo = ddp.pedido) " +
							"INNER JOIN recepciones_pedidos rp ON (rp.devolucion = dv.codigo) " +
							"INNER JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo) " +
							"INNER JOIN articulo a ON (ddp.articulo = a.codigo) " +
							"LEFT OUTER JOIN forma_farmaceutica ff ON (a.forma_farmaceutica=ff.acronimo AND a.institucion=ff.institucion) " +
							"LEFT OUTER JOIN unidad_medida um ON (a.unidad_medida=um.acronimo) " +
					"WHERE " +
						"dv.codigo = " + codigoDevolucion +
					" GROUP BY dv.codigo,dv.es_qx,dv.fecha,dv.hora,ed.descripcion,p.centro_costo_solicitado,p.centro_costo_solicitante,dv.usuario,rp.usuario,a.codigo,a.descripcion,a.concentracion,a.forma_farmaceutica,ff.nombre,a.unidad_medida,um.nombre,ddp.pedido,ddp.cantidad,drp.cantidadrecibida,mdi.descripcion,dv.observaciones,drp.costo_unitario ORDER BY a.descripcion ";
		return resultadosDD;
	}
}