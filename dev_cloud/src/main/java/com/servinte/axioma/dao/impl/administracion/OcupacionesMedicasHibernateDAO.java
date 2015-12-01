package com.servinte.axioma.dao.impl.administracion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.administracion.IOcupacionesMedicasDAO;
import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.orm.delegate.administracion.OcupacionesMedicasDelegate;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de la 
 * entidad Ocupaciones M�dicas
 * 
 * @author Angela Maria Aguirre
 * @since 23/09/2010
 */
public class OcupacionesMedicasHibernateDAO implements IOcupacionesMedicasDAO {
	
	OcupacionesMedicasDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public OcupacionesMedicasHibernateDAO(){
		delegate = new OcupacionesMedicasDelegate();
	}
	

	/**
	 * 
	 * Este M�todo se encarga de consultar las ocupaciones m�dicas 
	 * existentes en el sistema
	 * 
	 * @return List<OcupacionesMedicas>
	 */
	@Override
	public List<OcupacionesMedicas> listarOcupacionesMedicas() {
		return delegate.listarOcupacionesMedicas();
	}

}
