
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
	 * Método que determina si un contrato específico requiere Anticipo
	 * 
	 * @param contrato
	 * @return
	 */
	public ArrayList<ControlAnticiposContrato> determinarContratoRequiereAnticipo (int contrato);
	
}
