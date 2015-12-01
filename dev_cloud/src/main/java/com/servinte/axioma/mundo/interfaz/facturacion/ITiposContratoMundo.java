package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposContrato;

/**
 * Esta clase se encarga de definir los métodos de
 * negocio para la entidad tipos_contrato
 * 
 * @author Angela Maria Aguirre
 * @since 4/11/2010
 */
public interface ITiposContratoMundo {
	

	/**
	 * 
	 * Este Método se encarga de consultar los tipos de contrato
	 * registrados en el sistema
	 *  
	 * @return ArrayList<TiposContrato>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposContrato> consultarTiposContrato();

}
