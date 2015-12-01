/*
 * @(#)SqlBaseDespachoPedidosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.IdentificadoresExcepcionesSql;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para el despacho de pedidos
 *
 * @version 1.0, Septiembre 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseDespachoPedidosDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseDespachoPedidosDao.class);
	
	/**
	 *  Listado de los pedidos en estado TERMINADO 
	 */
	private final static String listadoPedidosStr =	"SELECT " +
																	"CASE WHEN pe.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'urgente' ELSE ' ' END  AS identificadorPrioridad, " +
																	"to_char(pe.fecha_grabacion,'dd/mm/yyyy') AS fechaGrabacionPedido, " +
																	"pe.hora_grabacion AS horaGrabacionPedido, " +
																	"to_char(pe.fecha,'dd/mm/yyyy') AS fechaPedido, " +
																	"pe.hora AS horaPedido, " +
																	"pe.codigo AS numeroPedido, " +
																	"pe.usuario As usuarioSolicitante, " +
																	"pe.centro_costo_solicitante AS codigoCentroCostoSolicitante, " +
																	"cc.nombre ||' ['|| getnomcentroatencion(cc.centro_atencion) ||'] ' AS centroCostoSolicitante " +
																	"FROM " +
																	"pedido pe, " +
																	"centros_costo cc " +
																	"WHERE " +
																	"pe.centro_costo_solicitante=cc.codigo " +
																	"AND pe.estado = "+ConstantesBD.codigoEstadoPedidoTerminado+" " +
																	"AND pe.centro_costo_solicitado = ? " +
																	"AND pe.es_qx = '"+ConstantesBD.acronimoNo+"' " +
																	"AND pe.auto_por_subcontratacion = '" + ConstantesBD.acronimoNo +"' "+
																	"ORDER BY  pe.fecha ASC, pe.hora ASC ";
	/**
	 * Parte 1 del Detalle de un pedido dado su numeroPedido
	 */
	private final static String detallePedidoPart1Str=		"SELECT " +
																				"detp.articulo AS codigoArticulo, " +
																				"detp.articulo " +
																				"||'-'|| vista.descripcion " +
																				"||'-'|| CASE WHEN vista.concentracion IS NULL THEN ' ' ELSE vista.concentracion END  " +
																				"||'-'|| CASE WHEN getNomFormaFarmaceutica(vista.forma_farmaceutica) IS NULL THEN ' ' ELSE getNomFormaFarmaceutica(vista.forma_farmaceutica) END AS infoArticulo, " +
																				"CASE WHEN getNomUnidadMedida(vista.unidad_medida) IS NULL THEN ' ' ELSE getNomUnidadMedida(vista.unidad_medida) END AS unidadMedida, " +
																				"detp.cantidad AS cantidadPedido, " +
																				"gettotalexisarticulosxalmacen(?,detp.articulo,?) AS existencias "+
																				"FROM " +
																				"detalle_pedidos detp, " +
																				"view_articulos vista " +
																				"WHERE " +
																				"detp.articulo=vista.codigo " +
																				"AND detp.pedido=? ORDER BY codigoArticulo ";
			
	/**
	 * Parte 2 del Detalle de un pedido dado su numeroPedido
	 */
	private final static String detallePedidoPart2Str= 	"SELECT " +
																				"to_char(pe.fecha,'dd/mm/yyyy') ||'-'|| pe.hora AS fechaHoraPedido, " +
																				"pe.codigo AS numeroPedido, " +
																				"pe.usuario AS usuarioSolicitante, " +
																				"pe.observaciones_generales AS observacionesGenerales, " +
																				"to_char(pe.fecha_grabacion,'dd/mm/yyyy') ||'-'|| pe.hora_grabacion AS fechaHoraGrabacion, " +
																				"cc.nombre AS centroCostoSolicitante, " +
																				"cc.codigo AS codCentroCostoSolicitante, " +
																				"CASE WHEN pe.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'urgente' ELSE 'no' END  AS identificadorPrioridad, " +
																				"est.descripcion AS estadoPedido, " +
																				"ccs.nombre AS farmacia, " +
																				"to_char(desp.fecha,'dd/mm/yyyy') ||'-'|| desp.hora AS fechaHoraDespacho " +
																				"FROM " +
																				"pedido pe " +
																				"INNER JOIN centros_costo cc ON(pe.centro_costo_solicitante=cc.codigo)  " +
																				"INNER JOIN estados_pedido est ON(pe.estado=est.codigo) " +
																				"INNER JOIN centros_costo ccs ON(pe.centro_costo_solicitado=ccs.codigo) " +
																				"LEFT OUTER JOIN despacho_pedido desp ON (pe.codigo=desp.pedido) " +
																				"WHERE " +
																				"pe.codigo=? ";
	
	/**
	 * Cambia el estado del pedido a DESPACHADO
	 */
	private final static String cambiarEstadoPedidoStr= 	"UPDATE pedido " +
																					"SET estado = "+ConstantesBD.codigoEstadoPedidoDespachado+" " +
																					"WHERE codigo=? ";
	
	/**
	 * Inserta el despacho Básico de un pedido
	 */
	private final static String insertarDespachoBasicoStr= "INSERT INTO despacho_pedido (pedido,fecha,hora,usuario,contabilizado,consecutivo) VALUES (?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?, ?, ?) ";
	
	/**
	 * Inserta el detalle del despacho
	 */
	private final static String insertarDetalleDespachoStr= "INSERT INTO detalle_despacho_pedido(pedido,articulo,cantidad,costo_unitario,lote,fecha_vencimiento,tipo_despacho,almacen_consignacion,proveedor_compra,proveedor_catalogo) VALUES( ? , ? , ? ,? , ? , ?, ?, ?, ?, ?) ";
	
	/**
	 * Carga el resumen
	 */
	private final static String resumenStr=	"SELECT " +
																"detp.articulo AS codigoArticulo, " +
																"detp.articulo ||'-'|| vista.descripcion   " +
																"||'-'|| CASE WHEN vista.concentracion IS NULL THEN ' ' ELSE vista.concentracion END  " +
																"||'-'|| CASE WHEN getNomFormaFarmaceutica(vista.forma_farmaceutica) IS NULL THEN ' ' ELSE getNomFormaFarmaceutica(vista.forma_farmaceutica) END AS infoArticulo, " +
																"CASE WHEN getNomUnidadMedida(vista.unidad_medida) IS NULL THEN ' ' ELSE getNomUnidadMedida(vista.unidad_medida) END AS unidadMedida, " +
																"detp.cantidad AS cantidadPedido, " +
																"detdes.cantidad AS cantidadDespachada," +
																"case when detdes.lote is null then ' ' else detdes.lote end as lote," +
																"case when detdes.fecha_vencimiento is null then ' ' else to_char(detdes.fecha_vencimiento,'dd/mm/yyyy') end as fechavencimiento " +
																"FROM  " +
																"detalle_pedidos detp, " +
																"view_articulos vista, " +
																"detalle_despacho_pedido detdes " +
																"WHERE " +
																"detp.articulo=vista.codigo " +
																"AND detdes.pedido=detp.pedido " +
																"AND detdes.articulo=detp.articulo  " +
																"AND detp.pedido=? ORDER BY codigoArticulo ";
	
	/**
	 * Carga el Listado de los pedidos en estado TERMINADO filtrados deacuerdo a la farmacia
	 * solicitada en el pedido versus el centro de costo del usuario que ingresa a la opción  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param centroCostoUser, int, codigo del centro costo del user
	 * @return ResulSet list
	 */
	public static ResultSetDecorator listadoPedidos(Connection con, int centroCostoUser)
	{
		try
		{
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoPedidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("-->"+listadoPedidosStr+"\n-->"+centroCostoUser);
			listadoStatement.setInt(1,centroCostoUser);
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de los pedidos: SqlBaseDespachoPedidosDao ",e);
			return null;
		}
	}

	/**
	 * Carga el detalle de un pedido Part 1  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @param almacen
	 * @param institucion
	 * @return ResulSet list
	 */
	public static ResultSetDecorator detallePedidoPart1(Connection con, int numeroPedido, int almacen,int institucion)
	{
		try
		{
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(detallePedidoPart1Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listadoStatement.setInt(1,almacen);
			listadoStatement.setInt(2,institucion);
			listadoStatement.setInt(3,numeroPedido);
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle de los pedidos: SqlBaseDespachoPedidosDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Carga el detalle de un pedido Part 2  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @return ResulSet list
	 */
	public static ResultSetDecorator detallePedidoPart2(Connection con, int numeroPedido)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(detallePedidoPart2Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+detallePedidoPart2Str+"-----"+numeroPedido);
			ps.setInt(1,numeroPedido);
			return new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle de los pedidos: SqlBaseDespachoPedidosDao ",e);
			return null;
		}
	}
	
	/**
	 * Cambia el estado del pedido a DESPACHADO
	 * @param con
	 * @param numeroPedido
	 * @return
	 */
	public static int cambiarEstadoPedido(Connection con, int numeroPedido)
	{
		int resp=0;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoPedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroPedido);
			resp=ps.executeUpdate();
			return resp;
		}
		catch(SQLException e) 
		{
			logger.warn(e+" Error en la consulta del detalle de los pedidos: SqlBaseDespachoPedidosDao "+e.toString());
			return 0;
		}
	}
	
	/**
	 * Inserta un despacho de pedido
	 * @param con, Connection
	 * @param numeroPedido, int
	 * @param usuario, String
	 * @return int (0 -ultimoCodigoSequence) 
	 */
	public static int insertarDespachoBasico (Connection con, int numeroPedido,	String usuario)
	{
	    int resp=0;
	    
	    try
		{		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDespachoBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "inventarios.seq_despacho_pedido");
			
			ps.setInt(1,numeroPedido);
			ps.setString(2,usuario);
			ps.setString(3,ConstantesBD.acronimoNo);
			ps.setInt(4,valorseq);
			return ps.executeUpdate();
		}
	    catch(SQLException e)
		  {
	    		if(e.getSQLState().equals(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente))
	    			return Integer.parseInt(IdentificadoresExcepcionesSql.codigoExcepcionSqlRegistroExistente)*-1;
		      logger.warn(e+" Error en la inserción de datos: SqlBaseDespachoPedidosDao "+e.toString() );
		      resp=0;
		  }
	    return resp;
	}

	/**
	 * Inserta el DETALLE del despacho de pedidos
	 * @param con, Connection
	 * @param numeroPedido, int
	 * @param articulo, int
	 * @param cantidad, int
	 * @param costo
	 * @param fechaVencimiento 
	 * @param lote 
	 * @param proveedorCatalogo 
	 * @param proveedorCompra 
	 * @param almacenConsignacion 
	 * @return
	 */
	public static int insertarDetalleDespachoPedido	(Connection con,int  numeroPedido,int articulo,int cantidad,float costo, String lote, String fechaVencimiento,String tipoDespacho, String almacenConsignacion, String proveedorCompra, String proveedorCatalogo)
	{
	    int resp=0;
	    
	    try
		{		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroPedido);
			ps.setInt(2,articulo);
			ps.setInt(3,cantidad);
			ps.setFloat(4,costo);
			if(lote.trim().equals("")||lote.trim().equalsIgnoreCase("null"))
	        {
	        	ps.setObject(5, null);
	        	ps.setObject(6, null);
	        }
	        else
	        {
	        	ps.setString(5, lote);
	        	if(fechaVencimiento.trim().equals("")||fechaVencimiento.trim().equalsIgnoreCase("null"))
	        	{
	        		ps.setObject(6, null);
	        	}
	        	else
	        	{
	        		ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento));	
	        	}
	        }    
			if(UtilidadTexto.isEmpty(tipoDespacho))
				ps.setObject(7, null);
			else
				ps.setString(7,tipoDespacho);

			if(UtilidadTexto.isEmpty(almacenConsignacion))
				ps.setObject(8, null);
			else
				ps.setInt(8,Utilidades.convertirAEntero(almacenConsignacion));
			
			if(UtilidadTexto.isEmpty(proveedorCompra))
				ps.setObject(9, null);
			else
				ps.setString(9,proveedorCompra);
				
			if(UtilidadTexto.isEmpty(proveedorCatalogo))
				ps.setObject(10, null);
			else
				ps.setString(10,proveedorCatalogo);
				
			resp=ps.executeUpdate();
			
		}
	    catch(SQLException e)
		  {
		      logger.warn(e+" Error en la inserción de datos: SqlBaseDespachoPedidosDao "+e.toString() );
		      resp=0;
		  }
	    return resp;
	}
	
	/**
	 * Carga el resumen del despacho de pedidos  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @return ResulSet list
	 */
	public static ResultSetDecorator resumen(Connection con, int numeroPedido)
	{
		try
		{
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(resumenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listadoStatement.setInt(1,numeroPedido);
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en el resumen del despacho de pedidos: SqlBaseDespachoPedidosDao "+e.toString());
			return null;
		}
	}
}
