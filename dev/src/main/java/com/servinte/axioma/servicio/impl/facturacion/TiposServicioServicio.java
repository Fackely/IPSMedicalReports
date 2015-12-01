package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposServicioMundo;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposServicioServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class TiposServicioServicio implements ITiposServicioServicio {
	
	ITiposServicioMundo mundo;
	
	public TiposServicioServicio(){
		mundo = FacturacionFabricaMundo.crearTipoServicioMundo();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los tipos de servicios
	 * en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposServicio> buscarTiposServicio(){
		return mundo.buscarTiposServicio(); 
	}
	

}
