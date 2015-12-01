package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.ITiposContratoDAO;
import com.servinte.axioma.orm.TiposContrato;
import com.servinte.axioma.orm.delegate.facturacion.TiposContratoDelegate;

/**
 * Esta clase se encarga de implementar los métodos de
 * negocio para la entidad tipos_contrato
 * 
 * @author Angela Maria Aguirre
 * @since 4/11/2010
 */
public class TiposContratoHibernateDAO implements ITiposContratoDAO {
	
	TiposContratoDelegate delegate;
	
	public TiposContratoHibernateDAO() {
		delegate = new TiposContratoDelegate();
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
		return delegate.consultarTiposContrato();
	}

}
