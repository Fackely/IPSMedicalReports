/*
 * Dic 05, 2007
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author Sebastián Gómez
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad DESPACHO PEDIDOS QUIRÚRGICOS
 *
 */
public class SqlBaseDespachoPedidoQxDao 
{
	 /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger( SqlBaseDespachoPedidoQxDao.class);
	
	
	/**
	 * Seccion SELECT para la consulta de peticiones
	 */
	private static final String consultarPeticionesStr_SELECT = "SELECT "+
		"p.codigo AS peticion, "+
		"CASE WHEN sc.fecha_inicial_cx IS NOT NULL THEN sc.fecha_inicial_cx || '' ELSE coalesce(p.fecha_cirugia||'','') END AS fecha_cirugia, "+ 
		"getNomEstadoPeticion(p.estado_peticion) AS estado, "+ 
		"tienePedidosUrgPet(p.codigo,?,"+ConstantesBD.codigoEstadoPedidoTerminado+") AS urgente, " +
		"p.paciente AS codigo_paciente, "+
		"per.tipo_identificacion || ' ' || per.numero_identificacion AS numero_id, "+
		"per.primer_nombre || ' ' || coalesce(per.segundo_nombre,'') || ' ' || per.primer_apellido || ' ' || coalesce(per.segundo_apellido,'') As paciente, "+
		"coalesce(s.consecutivo_ordenes_medicas||'','') AS orden, "+
		"CASE WHEN s.codigo_medico IS NOT NULL THEN getnombrepersona(s.codigo_medico) ELSE getnombrepersona(p.solicitante) END AS solicitante, "+
		"coalesce(getnombreespecialidad(s.especialidad_solicitante),'') AS especialidad, "+
		"coalesce(getconsecutivoingreso(cu.id_ingreso), '') AS ingreso "+
		"FROM peticion_qx p "+ 
		"INNER JOIN personas per ON(per.codigo=p.paciente) "+
		"LEFT OUTER JOIN solicitudes_cirugia sc ON(sc.codigo_peticion=p.codigo) "+ 
		"LEFT OUTER JOIN solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) "+
		"LEFT OUTER JOIN cuentas cu ON(s.cuenta=cu.id)";
	
	/**
	 * Sección WHERE para la consulta de peticiones por paciente
	 */
	private static final String consultarPeticionesStr_WHERE_01 = " WHERE p.paciente=? ";
	
	/**
	 * Sección WHERE para la consulta de peticiones por 
	 */
	private static final String consultarPeticionesStr_WHERE_02 = " WHERE getCCPedidoPeticion(p.codigo) = ? ";
	
	/**
	 * Seccion WHERE para la consulta de peticiones
	 */
	private static final String consultarPeticionesStr_WHERE_03 = " AND "+ 
		"( "+   
			"p.estado_peticion IN ("+ConstantesBD.codigoEstadoPeticionPendiente+","+ConstantesBD.codigoEstadoPeticionProgramada+","+ConstantesBD.codigoEstadoPeticionReprogramada+","+ConstantesBD.codigoEstadoPeticionAtendida+")  "+ 
			//Nota* Se quita esta validación por tarea xplanner3 : 51291 => "(p.estado_peticion = "+ConstantesBD.codigoEstadoPeticionAtendida+" AND esSolicitudTotalPendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"') "+  
		") AND "+ 
		"tienePedidoTerminadoPet(p.codigo,?) = '"+ConstantesBD.acronimoSi+"' ORDER BY fecha_cirugia";
	
	
	
	/**
	 * Cadena que consulta todos los articulos de todos los pedidos de la peticion
	 */
	private static final String consultaArticulosPedidosPeticionStr = "SELECT "+ 
		"a.codigo AS codigos_art, "+
		"a.codigo || '-' || a.descripcion ||' CONC:'|| coalesce(a.concentracion,'')  ||' F.F:'|| coalesce(getNomFormaFarmaceutica(a.forma_farmaceutica),'') || ' NAT:' || coalesce(getnomnaturalezaarticulo(a.naturaleza),'') AS articulo, "+ 
		"sum(dp.cantidad) AS total_pedido, "+
		"getNomUnidadMedida(a.unidad_medida) AS unidad_medida_articulo, "+
		"sc.numero_solicitud AS sol, " +
		"na.es_pos AS pos, " +
		"p.centro_costo_solicitado AS centro_costo_solicitado " +
		"FROM pedidos_peticiones_qx ppq "+ 
		"INNER JOIN pedido p ON(p.codigo=ppq.pedido) "+ 
		"INNER JOIN detalle_pedidos dp ON(dp.pedido=p.codigo) "+ 
		"INNER JOIN articulo a ON(a.codigo=dp.articulo) "+
		"INNER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " +
		"INNER JOIN peticion_qx pqx ON (ppq.peticion=pqx.codigo) " +
		"LEFT OUTER JOIN solicitudes_cirugia sc ON (sc.codigo_peticion=pqx.codigo) " +
		"WHERE ppq.peticion = ? AND p.centro_costo_solicitado = ? " + 
		"AND p.auto_por_subcontratacion = '" + ConstantesBD.acronimoNo +"' ";
	
	/**
	 * Cadena que consulta los pedidos de una peticion
	 */
	private static final String consultaPedidosPeticionStr = "SELECT "+ 
		"p.codigo AS codigo_pedido, " +
		"getnombreusuario2(p.usuario) As usuario_solicitante, " +
		"to_char(p.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha_pedido, " +
		"p.hora AS hora_pedido, " +
		"getnomcentrocosto(p.centro_costo_solicitante) AS centro_costo_solicita, "+ 
		"CASE WHEN p.urgente = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as urgente, "+ 
		"p.estado, "+
		"'"+ConstantesBD.acronimoSi+"' AS chequeado "+ 
		"FROM pedidos_peticiones_qx ppq "+ 
		"INNER JOIN pedido p ON(p.codigo=ppq.pedido) "+ 
		"WHERE ppq.peticion = ? AND p.centro_costo_solicitado = ? " +
		"AND p.auto_por_subcontratacion = '" + ConstantesBD.acronimoNo +"' ";
	
	/**
	 * Cadena que consulta los articulos del pedido
	 */
	private static final String consultaArticulosPedidoPeticionStr = "SELECT "+ 
		"dp.articulo AS codigo_articulo, "+
		"'' AS lote, "+ //no aplica
		"'' AS fecha_vencimiento, "+ // no aplica
		"dp.cantidad AS cantidad "+ 
		"FROM detalle_pedidos dp "+ 
		"WHERE dp.pedido = ?";
	
	/**
	 * Cadena que consulta los artpículos despachados
	 */
	private static final String consultaArticulosDespachoPedidoPeticionStr = "SELECT "+ 
		"ddd.articulo AS codigo_articulo, "+
		"coalesce(ddd.lote,'') AS lote, "+
		"coalesce(to_char(ddd.fecha_vencimiento,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_vencimiento, "+ 
		"ddd.cantidad AS cantidad "+ 
		"FROM detalle_despacho_pedido ddd "+ 
		"WHERE ddd.pedido = ?";

	
	
	/**
	 * Método que consulta las peticiones para realizar el despacho de pedidos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarPeticiones(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		resultados.put("numRegistros", "0");
		
		String[] indices = {"peticion_","fechaCirugia_","estado_","urgente_","orden_","solicitante_","especialidad_","numeroId_","paciente_","codigoPaciente_"};
		try
		{
			String consulta = consultarPeticionesStr_SELECT;
			
			if(!campos.get("codigoPaciente").toString().equals(""))
				consulta += consultarPeticionesStr_WHERE_01;
			else
				consulta += consultarPeticionesStr_WHERE_02;
			
			consulta += consultarPeticionesStr_WHERE_03;
			
			logger.info("\n\n\nconsulta=> "+consulta+", codigoPaciente: "+campos.get("codigoPaciente")+", codigoCentroCosto: "+campos.get("codigoCentroCosto")+", codigoCentroCostoUsuario: "+campos.get("codigoCentroCostoUsuario")+"\n\n\n");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoCentroCostoUsuario")+""));
			if(!campos.get("codigoPaciente").toString().equals(""))
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
			else
				pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoCentroCosto")+"")); 
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("codigoCentroCostoUsuario")+""));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPeticiones: "+e);
		}
		
		resultados.put("INDICES_MAPA", indices);
		return resultados;
	}
	
	/**
	 * Método que consultas los articulos de todos los pedidos de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static HashMap consultaArticulosPedidosPeticion(Connection con,int numeroPeticion,int codigoFarmacia,String listadoPedidos)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros", "0");
		
		try
		{
			String consulta = consultaArticulosPedidosPeticionStr;
			
			if(!listadoPedidos.equals(""))
				consulta += " AND p.codigo IN ("+listadoPedidos+")";
			else
				consulta += " AND p.estado = "+ConstantesBD.codigoEstadoPedidoTerminado+" ";
				
			consulta += " GROUP BY a.codigo,a.descripcion,a.concentracion,a.forma_farmaceutica,a.naturaleza,a.unidad_medida,sc.numero_solicitud,na.es_pos, p.centro_costo_solicitado "+ 
				"ORDER BY a.descripcion";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			pst.setInt(2,codigoFarmacia);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaArticulosPedidosPeticion: "+e);
		}
		
		return resultados;
	}
	
	/**
	 * Método implementado para cargar los pedidos de la peticion con su detalle
	 * @param con
	 * @param numeroPeticion
	 * @param codigoFarmacia
	 * @return
	 */
	public static HashMap consultaPedidosPeticion(Connection con,int numeroPeticion,int codigoFarmacia,String listadoPedidos)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros","0");
		try
		{
			String consulta = consultaPedidosPeticionStr;
			
			if(listadoPedidos.equals(""))
				consulta += " AND p.estado = "+ConstantesBD.codigoEstadoPedidoTerminado;
			else
				consulta += " AND p.codigo IN ("+listadoPedidos+") ";
			
			consulta += " ORDER BY p.codigo";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			pst.setInt(2,codigoFarmacia);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			//Se consultan los artículos de cada pedido 
			if(!resultados.isEmpty()){
				for(int i=0;i<Utilidades.convertirAEntero(resultados.get("numRegistros").toString());i++)
				{
					//Si vienen un listado de pedidos específico, quiere decir que se desea consultar el despacho
					if(listadoPedidos.equals(""))
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaArticulosPedidoPeticionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					else
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaArticulosDespachoPedidoPeticionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(resultados.get("codigoPedido_"+i).toString()));
					
					resultados.put("articulos_"+i, UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
				}
			}			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaPedidosPeticion: "+e);
		}
		return resultados;
	}
	
	
}
