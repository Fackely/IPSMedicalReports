/*
 * DevolucionPedidosDao.java 
 * Autor			:  mdiaz
 * Creado el	:  16-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.pedidos.DetalleDevolucionPedidos;
import com.princetonsa.mundo.pedidos.DevolucionPedidos;


/**
 * descripcion de esta clase
 *
 * @version 1.0, 16-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public interface DevolucionPedidosDao 
{
	
	  public int getCodigoDevolucionDisponible(Connection con);
	  
	  public int getSiguienteCodigoDevolucionDisponible(Connection con);
	  
	  public String getNombreCentroCosto(Connection con, int codCentroCosto);

	  public int insertar(Connection con, DevolucionPedidos devolucionPedidos);
					
   		public int insertarDetalle(Connection con, int codigoDevolucion, DetalleDevolucionPedidos detalleDevolucion);
	  
	  public ResultSetDecorator buscar(Connection con, String[] selectedColumns, String from, String where, String orderBy);
	  
	  
		/**
		 * Método que consulta la fecha y hora de un pedido
		 * @param con
		 * @param codigoPedido
		 * @return
		 */
		public String consultarFechaHoraDespacho(Connection con,String codigoPedido);
		
		/**
		 * Método implementado para realizar la busqueda avanzada de despachos
		 * @param con
		 * @param campos
		 * @return
		 */
		public HashMap consultarDespachos(Connection con,HashMap campos);
				
}
	

