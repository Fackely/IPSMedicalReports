/*
 * Creado  15/09/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.postgresql;



import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.PedidosInsumosDao;
import com.princetonsa.dao.sqlbase.SqlBasePedidosInsumosDao;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para un el <code>Pedido de Insumos</code>.
 *
 * @version 1.0, 15/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
@SuppressWarnings("unchecked")
public class PostgresqlPedidosInsumosDao implements PedidosInsumosDao
{

	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PostgresqlPedidosInsumosDao.class);
	/**
	 * String para insertar un pedido.
	 */
	private final static String insertarPedidoStr="insert into pedido " +
																			"(codigo, " +
																			"estado, " +
																			"fecha_grabacion, " +
																			"hora_grabacion, " +
																			"fecha, " +
																			"hora, " +
																			"centro_costo_solicitante, " +
																			"centro_costo_solicitado, " +
																			"urgente, " +
																			"observaciones_generales, " +
																			"usuario, " +
																			"es_qx, " +
																			"entidad_subcontratada, " +
																			"auto_por_subcontratacion) " +
																			"values (NEXTVAL('seq_pedido'),?,?,?,?,?,?,?,?,?,?,?,?,?)";


																					
	
	
	
	/**
	 * Carga el Listado de todos los centros de costo.
	 * @param con, Connection, conexión abierta con una fuente de datos.
	 * @return ResulSet list.
	 */
    public ResultSetDecorator listadoCentrosCosto(Connection con)
	{
		return SqlBasePedidosInsumosDao.listadoCentrosCosto(con);
	}
    
    /**
	 * Carga el Listado de los centros de costo que sean de tipo Subalmacen.
	 * @param con, Connection, conexión abierta con una fuente de datos.
	 * @return ResulSet list.
	 */
	public ResultSetDecorator listadoCentrosCostoSubalmacen(Connection con)
	{
	    return SqlBasePedidosInsumosDao.listadoCentrosCostoSubalmacen(con);
	}
	
	/**
	 * Carga el ultimo codigo pedido  (table= pedido))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public int cargarUltimoCodigoSequence(Connection con)
	{
	    return SqlBasePedidosInsumosDao.cargarUltimoCodigoSequence(con);
	}

	/**
	 * Metodo para insertar un Pedido (tabla = <code>pedido</code>).
	 * @param con, Connection con la fuente de datos.
	 * @param fecha, fecha de grabación.
	 * @param hora, hora de grabación.
	 * @param centroCostoSolicitante, centro de costo que solicita.
	 * @param centroCostoSolicitado, centro de costo que despecha.
	 * @param urgente,  true ó false.
	 * @param obsevacionesGenerales, observaciones del pedido.
	 * @param usuario, usuario que realza el pedido.
	 * @return int, 1 efectivo de lo contrario 0.
	 */
	public int insertarAdmin(Connection con, String fecha, 
											        String hora, 
											        int centroCostoSolicitante,
											        int centroCostoSolicitado,
											        boolean urgente,
											        String obsevacionesGenerales,
											        String usuario,
											        boolean terminarPedido,
											        String fechaPedido,
											        String horaPedido,
											        String entidadSubcontratada,
											        String esQuirurgico,
											        String autoPorSub)
	{
	    return SqlBasePedidosInsumosDao.insertarAdmin(con, 
	            											fecha,
												            hora,
												            centroCostoSolicitante,
												            centroCostoSolicitado,
												            urgente,
												            obsevacionesGenerales,
												            usuario,
												            terminarPedido,
												            insertarPedidoStr,
												            fechaPedido,
												            horaPedido,
												            entidadSubcontratada,
												            esQuirurgico,
												            autoPorSub);
	    
	}
	
	/**
	 * Metodo para insertar el detalle del pedido.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPedido, codigo del pedido.
	 * @param codigoArticulo, codigo del articulo.
	 * @param cantidad, cantidad del pedido de un articulo.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public int insertarDetallePedido(Connection con, int codigoPedido, int codigoArticulo, int cantidad)
	{
	    return SqlBasePedidosInsumosDao.insertarDetallePedido(con, codigoPedido, codigoArticulo, cantidad); 
	}
	
	
	/**
	 * Metodo para realizar la consulta de un pedido realizado.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPedido, Codigo del pedido a consultar.
	 * @return ResultSet.
	 */
	public ResultSetDecorator listarPedidoInsumo(Connection con, int codigoPedido)
	{
	    return SqlBasePedidosInsumosDao.listarPedidoInsumo(con, codigoPedido); 
	}
	
	
	
	
	public int modificarPedido(Connection con, int codigoPedido, int centroCosto, int farmacia, boolean urgente, String observacionesGenerales, int estado, String motivoDevolucion,  String usuario){
		return SqlBasePedidosInsumosDao.modificarPedido(con, codigoPedido, centroCosto, farmacia, urgente, observacionesGenerales, estado, motivoDevolucion,  usuario );
	}
	
	
	public int modificarDetallePedido(Connection con, int codigoPedido, int codigoArticulo, int cantidad){
		return SqlBasePedidosInsumosDao.modificarDetallePedido(con, codigoPedido, codigoArticulo, cantidad);
	}
	
	
	public int eliminarDetallePedido(Connection con, int codigoPedido, int codigoArticulo){
		return SqlBasePedidosInsumosDao.eliminarDetallePedido(con, codigoPedido, codigoArticulo);
	}
	
	/**
	 * Método implementado para consultar los datos generales de un pedido
	 * de insumos
	 * @param con
	 * @param pedido
	 * @return
	 */
	public HashMap cargarDatosGeneralesPedido(Connection con,int pedido)
	{
		return SqlBasePedidosInsumosDao.cargarDatosGeneralesPedido(con,pedido);
		
	}
	
	/**
	 * Método usado para consultar el detalle de artículos del 
	 * pedido de insumos
	 * @param con
	 * @param pedido
	 * @return
	 */
	public HashMap cargarDetallePedido(Connection con,int pedido)
	{
		return SqlBasePedidosInsumosDao.cargarDetallePedido(con,pedido);
			
	}
	
	/**
	 * Método implementado para cargar los datos de anulación de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public HashMap cargarDatosAnulacion(Connection con,int codigoPedido)
	{
		return SqlBasePedidosInsumosDao.cargarDatosAnulacion(con,codigoPedido);
	}
	
	/**
	 * Método que consulta los datos adicionales de la peticion de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public HashMap cargarDatosPeticionPedido(Connection con,String codigoPedido)
	{
		return SqlBasePedidosInsumosDao.cargarDatosPeticionPedido(con, codigoPedido);
	}

}
