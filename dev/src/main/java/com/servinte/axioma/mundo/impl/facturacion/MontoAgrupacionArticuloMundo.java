package com.servinte.axioma.mundo.impl.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.MontoAgrupacionArticuloDAO;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionArticuloMundo;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 10/09/2010
 */
public class MontoAgrupacionArticuloMundo implements
		IMontoAgrupacionArticuloMundo {
	
	MontoAgrupacionArticuloDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public MontoAgrupacionArticuloMundo(){
		dao = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getMontoAgrupacionArticuloDAO();
	}

	/**
	 * 
	 * Este Método se encarga de insertar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int insertarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto){
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		int filasAfectadas = dao.insertarMontoAgrupacionArticulo(conn, dto, tipoBD);
		
		return filasAfectadas;
		
	}

	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos por el id del detalle relacinado
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto){
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		boolean eliminacionExitosa = dao.eliminarMontoAgrupacionArticulo(conn, dto);		
		return eliminacionExitosa;
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos por el id del detalle relacionado
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticuloPorDetalleID(
			DTOBusquedaMontoAgrupacionArticulo dto){
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		boolean eliminacionExitosa = dao.eliminarMontoAgrupacionArticuloPorDetalleID(conn, dto);		
		return eliminacionExitosa;
	}

	/**
	 * 
	 * Este Método se encarga de actualizar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int actualizarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto){
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		int filasAfectadas = dao.actualizarMontoAgrupacionArticulo(conn, dto);
		
		return filasAfectadas;
	}

	/**
	 * 
	 * Este Método se encarga de buscar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionArticulo> buscarMontoAgrupacionArticuloPorDetalleID(DTOBusquedaMontoAgrupacionArticulo dto){
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista = dao.buscarMontoAgrupacionArticuloPorDetalleID(conn, dto);
		return lista;
	}

}
