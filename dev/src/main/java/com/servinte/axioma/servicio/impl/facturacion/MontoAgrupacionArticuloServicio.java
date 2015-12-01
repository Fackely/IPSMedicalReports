package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionArticuloMundo;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoAgrupacionArticuloServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 10/09/2010
 */
public class MontoAgrupacionArticuloServicio implements
		IMontoAgrupacionArticuloServicio {
	
	IMontoAgrupacionArticuloMundo mundo;
	
	public MontoAgrupacionArticuloServicio(){
		mundo = FacturacionFabricaMundo.crearMontoAgrupacionArticuloMundo();
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
		return mundo.insertarMontoAgrupacionArticulo(dto);
	}
	

	/**
	 * 
	 * Este Método se encarga de eliminar un registro de agrupación
	 * de artículos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return boolean	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo dto){
		return mundo.eliminarMontoAgrupacionArticulo(dto);
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
		return mundo.actualizarMontoAgrupacionArticulo(dto);
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
		return mundo.buscarMontoAgrupacionArticuloPorDetalleID(dto);
	}

}
