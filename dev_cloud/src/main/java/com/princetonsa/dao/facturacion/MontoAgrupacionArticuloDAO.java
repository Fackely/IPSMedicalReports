/**
 * 
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface MontoAgrupacionArticuloDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int insertarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto,int TIPO_BD);

	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto);
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de agrupaci�n
	 * de art�culos por el id del detalle relacionado
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticuloPorDetalleID(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto);


	/**
	 * 
	 * Este M�todo se encarga de actualizar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int actualizarMontoAgrupacionArticulo(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto);

	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOBusquedaMontoAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionArticulo> buscarMontoAgrupacionArticuloPorDetalleID(Connection conn,
			DTOBusquedaMontoAgrupacionArticulo dto);

}
