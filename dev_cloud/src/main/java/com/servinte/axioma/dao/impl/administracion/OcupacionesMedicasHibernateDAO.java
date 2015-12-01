package com.servinte.axioma.dao.impl.administracion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.administracion.IOcupacionesMedicasDAO;
import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.orm.delegate.administracion.OcupacionesMedicasDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de la 
 * entidad Ocupaciones Médicas
 * 
 * @author Angela Maria Aguirre
 * @since 23/09/2010
 */
public class OcupacionesMedicasHibernateDAO implements IOcupacionesMedicasDAO {
	
	OcupacionesMedicasDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public OcupacionesMedicasHibernateDAO(){
		delegate = new OcupacionesMedicasDelegate();
	}
	

	/**
	 * 
	 * Este Método se encarga de consultar las ocupaciones médicas 
	 * existentes en el sistema
	 * 
	 * @return List<OcupacionesMedicas>
	 */
	@Override
	public List<OcupacionesMedicas> listarOcupacionesMedicas() {
		return delegate.listarOcupacionesMedicas();
	}

}
