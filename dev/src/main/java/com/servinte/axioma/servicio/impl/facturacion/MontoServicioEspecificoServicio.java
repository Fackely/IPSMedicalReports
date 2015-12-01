package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoServicioEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoServicioEspecificoServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoServicioEspecificoServicio implements
		IMontoServicioEspecificoServicio {

	IMontoServicioEspecificoMundo mundo;
	
	public MontoServicioEspecificoServicio(){
		mundo = FacturacionFabricaMundo.crearMontoServicioEspecificoMundo();
	}
	/** 
	 * Este método se encarga de consultar todos los
	 * servicios específicos relacionados al detalle del monto de cobro
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoServicioEspecifico> obtenerServiciosPorDetalleID(int idDetalle,int codigoTarifario){
		return mundo.obtenerServiciosPorDetalleID(idDetalle,codigoTarifario);
	}
	
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
	public boolean eliminarServicioEspecifico(int id){
		return mundo.eliminarServicioEspecifico(id);
	}
}
