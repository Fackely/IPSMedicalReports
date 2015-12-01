
package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.ControlAnticiposContrato;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones relacionadas a la 
 * la entidad {@link ControlAnticiposContrato}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IControlAnticiposContratoDAO {

	/**
	 * M�todo que determina si un contrato espec�fico requiere Anticipo
	 * 
	 * @param contrato
	 * @return
	 */
	public ArrayList<ControlAnticiposContrato> determinarContratoRequiereAnticipo (int contrato);
	
}
