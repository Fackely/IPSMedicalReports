package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoArticuloEspecificoMundo;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontoArticuloEspecificoServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoArticuloEspecificoServicio implements
		IMontoArticuloEspecificoServicio {
	
	IMontoArticuloEspecificoMundo mundo;
	
	public MontoArticuloEspecificoServicio(){
		mundo = FacturacionFabricaMundo.crearMontoArticuloEspecificoMundo();
	}
	
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un art�culo espec�fico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarArticuloEspecifico(int id){
		return mundo.eliminarArticuloEspecifico(id);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de buscar un registro de
	 * de art�culos espec�ficos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoArticuloEspecifico> buscarMontoArticuloEspecifico(
			DTOBusquedaMontoArticuloEspecifico dto){
		return mundo.buscarMontoArticuloEspecifico(dto);
	}

}
