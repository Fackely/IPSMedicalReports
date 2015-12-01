
package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.ControlAnticiposContrato;

/**
 * Define la l&oacute;gica de negocio relacionada con
 * la entidad {@link ControlAnticiposContrato}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IControlAnticiposContratoMundo {

	/**
	 * M�todo que determina si un contrato espec�fico requiere Anticipo
	 * 
	 * @param contrato
	 * @return
	 */
	public ArrayList<ControlAnticiposContrato> determinarContratoRequiereAnticipo (int contrato);
	
}
