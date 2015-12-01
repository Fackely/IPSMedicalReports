/**
 * 
 */
package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.facturacion.MontoAgrupacionArticuloDAO;
import com.princetonsa.dao.sqlbase.facturacion.SQLBaseMontoAgrupacionArticuloDAO;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class PostgresSQLMontoAgrupacionArticulo implements
		MontoAgrupacionArticuloDAO {

	/**
	 * 
	 * Este Método se encarga de insertar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int insertarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto,int TIPO_BD){
		return SQLBaseMontoAgrupacionArticuloDAO.insertarMontoAgrupacionArticulo(conn, dto, TIPO_BD);
	}

	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto){
		return SQLBaseMontoAgrupacionArticuloDAO.eliminarMontoAgrupacionArticulo(conn, dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos por el id del detalle relacionado
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticuloPorDetalleID(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto){
		return SQLBaseMontoAgrupacionArticuloDAO.eliminarMontoAgrupacionArticuloPorDetalleID(conn, dto);
	}


	/**
	 * 
	 * Este Método se encarga de actualizar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int actualizarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto){
		return SQLBaseMontoAgrupacionArticuloDAO.actualizarMontoAgrupacionArticulo(conn, dto);
	}

	/**
	 * 
	 * Este Método se encarga de buscar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionArticulo> buscarMontoAgrupacionArticuloPorDetalleID(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto){
		return SQLBaseMontoAgrupacionArticuloDAO.buscarMontoAgrupacionArticuloPorDetalleID(conn, dto);
	}

}
