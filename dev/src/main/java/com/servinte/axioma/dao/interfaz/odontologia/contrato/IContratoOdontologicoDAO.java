package com.servinte.axioma.dao.interfaz.odontologia.contrato;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.ContratoOdontologico;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public interface IContratoOdontologicoDAO  extends IBaseDAO<ContratoOdontologico> {

	
	
	/**
	 * CONSULTA AVANZADA DE CONTRATO ODONTOLOGICO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoContrato
	 * @return
	 */
	public ContratoOdontologico consultarAvanzadaContratoOdon(ContratoOdontologico dtoContrato );
	
	
	
}
