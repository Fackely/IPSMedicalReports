package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;

public interface IMontoServicioEspecificoServicio {
	
	/** 
	 * Este método se encarga de consultar todos los
	 * servicios específicos relacionados al detalle del monto de cobro
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoServicioEspecifico> obtenerServiciosPorDetalleID(int idDetalle,int codigoTarifario);
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un servicio específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id);

}
