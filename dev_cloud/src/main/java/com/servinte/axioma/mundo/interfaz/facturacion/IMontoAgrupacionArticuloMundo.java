package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;

/**
 * Esta clase se encarga de 
 * @author Angela Maria Aguirre
 * @since 10/09/2010
 */
public interface IMontoAgrupacionArticuloMundo {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int insertarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto);

	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto);
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de agrupaci�n
	 * de art�culos por el id del detalle relacionado
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticuloPorDetalleID(DTOBusquedaMontoAgrupacionArticulo dto);

	/**
	 * 
	 * Este M�todo se encarga de actualizar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public int actualizarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto);

	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de agrupaci�n
	 * de art�culos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return ArrayList<DTOBusquedaMontoAgrupacionArticulo> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionArticulo> buscarMontoAgrupacionArticuloPorDetalleID(DTOBusquedaMontoAgrupacionArticulo dto);
	

}
