package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITiposContratoMundo;
import com.servinte.axioma.orm.TiposContrato;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposContratoServicio;

/**
 * Esta clase se encarga de implementar los métodos de
 * negocio para la entidad tipos_contrato
 * 
 * @author Angela Maria Aguirre
 * @since 4/11/2010
 */
public class TiposContratoServicio implements ITiposContratoServicio {
	
	
	ITiposContratoMundo mundo;
	
	public TiposContratoServicio(){
		mundo = FacturacionFabricaMundo.crearTiposContratoMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los tipos de contrato
	 * registrados en el sistema
	 *  
	 * @return ArrayList<TiposContrato>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposContrato> consultarTiposContrato(){
		return mundo.consultarTiposContrato();
	}
}
