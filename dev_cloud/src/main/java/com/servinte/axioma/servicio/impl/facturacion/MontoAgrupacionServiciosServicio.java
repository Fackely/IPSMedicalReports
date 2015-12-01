package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionServiciosMundo;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoAgrupacionServiciosServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoAgrupacionServiciosServicio implements
		IMontoAgrupacionServiciosServicio {
	
	IMontoAgrupacionServiciosMundo mundo;
	
	public MontoAgrupacionServiciosServicio(){
		mundo = FacturacionFabricaMundo.crearMontoAgrupacionServiciosMundo();
	}
	
	 /** 
	 * Este método se encarga de consultar todos las agrupaciones
	 * de servicios relacionadas a un detalle específico
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoAgrupacionServicio>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionServicio> obtenerServiciosPorDetalleID(int idDetalle){
		return mundo.obtenerServiciosPorDetalleID(idDetalle);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de una agrupación de servicios de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarAgrupacionServicio(int id){
		return mundo.eliminarAgrupacionServicio(id);
	}

}
