package com.servinte.axioma.dao.interfaz.odontologia.contrato;

import java.util.List;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.FirmasContOtrsiempr;


/**
 * 
 * @author axioma
 *
 */
public interface IFirmaContratoMultiEmpresaDAO extends IBaseDAO<FirmasContOtrsiempr> {

	
	
	/**
	 * CARGAR FIRMAS POR INSTITUCION 
	 * @author Edgar Carvajal Ruiz
	 * @param firmas
	 * @return
	 */
	 public List<FirmasContOtrsiempr> cargarFirmasPorInstucion(FirmasContOtrsiempr firmas);
	
	
}
