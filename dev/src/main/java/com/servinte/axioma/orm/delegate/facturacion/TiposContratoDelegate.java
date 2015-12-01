package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.hibernate.Criteria;

import com.servinte.axioma.orm.TiposContrato;
import com.servinte.axioma.orm.TiposContratoHome;

/**
 * Esta clase se encarga de manejar las transacciones relacionadas
 * con la entidad tipos_contrato
 * 
 * @author Angela Maria Aguirre
 * @since 4/11/2010
 */
public class TiposContratoDelegate extends TiposContratoHome {
	
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
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TiposContrato.class, "tipoContrato");
		return (ArrayList<TiposContrato>)criteria.list();
	}
	
}
