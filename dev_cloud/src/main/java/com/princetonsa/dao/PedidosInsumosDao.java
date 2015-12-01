/*
 * Creado  15/09/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase para manejar
 *
 * @version 1.0, 15/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
@SuppressWarnings("unchecked")
public interface PedidosInsumosDao
{
    /**
	 * Carga el Listado de las solicitudes de medicamentos para un paciente determinado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoPaciente, int, codigo del paciente
	 * @return ResulSet list
	 */
	public ResultSetDecorator listadoCentrosCosto(Connection con);
	
	/**
	 * Carga el Listado de los centros de costo que sean de tipo Subalmacen.
	 * @param con, Connection, conexi�n abierta con una fuente de datos.
	 * @return ResulSet list.
	 */
	public ResultSetDecorator listadoCentrosCostoSubalmacen(Connection con);
	
		
	/**
	 * Carga el ultimo codigo pedido  (table= pedido))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public int cargarUltimoCodigoSequence(Connection con);
	
	/**
	 * Metodo para insertar un Pedido (tabla = <code>pedido</code>).
	 * @param esQuirurgico2 
	 * @param con, Connection con la fuente de datos.
	 * @param fecha, fecha de grabaci�n.
	 * @param hora, hora de grabaci�n.
	 * @param centroCostoSolicitante, centro de costo que solicita.
	 * @param centroCostoSolicitado, centro de costo que despecha.
	 * @param urgente,  true � false.
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
											        String autoPorSub);

	
	/**
	 * Metodo para insertar el detalle del pedido.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPedido, codigo del pedido.
	 * @param codigoArticulo, codigo del articulo.
	 * @param cantidad, cantidad del pedido de un articulo.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public  int insertarDetallePedido(Connection con, int codigoPedido, int codigoArticulo, int cantidad);
	
	
	public int modificarPedido(Connection con, int codigoPedido, int centroCosto, int farmacia, boolean urgente, String observacionesGenerales, int estado, String motivoDevolucion,  String usuario);
	
	
	public int modificarDetallePedido(Connection con, int codigoPedido, int codigoArticulo, int cantidad);
	
	
	public int eliminarDetallePedido(Connection con, int codigoPedido, int codigoArticulo);
	
	
	
	
	/**
	 * Metodo para realizar la consulta de un pedido realizado.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPedido, Codigo del pedido a consultar.
	 * @return ResultSet.
	 */
	public ResultSetDecorator listarPedidoInsumo(Connection con, int codigoPedido);
	
	/**
	 * M�todo implementado para consultar los datos generales de un pedido
	 * de insumos
	 * @param con
	 * @param pedido
	 * @return
	 */
	public HashMap cargarDatosGeneralesPedido(Connection con,int pedido);
	
	/**
	 * M�todo usado para consultar el detalle de art�culos del 
	 * pedido de insumos
	 * @param con
	 * @param pedido
	 * @return
	 */
	public HashMap cargarDetallePedido(Connection con,int pedido);
	
	/**
	 * M�todo implementado para cargar los datos de anulaci�n de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public HashMap cargarDatosAnulacion(Connection con,int codigoPedido);
	
	/**
	 * M�todo que consulta los datos adicionales de la peticion de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public HashMap cargarDatosPeticionPedido(Connection con,String codigoPedido);
}
