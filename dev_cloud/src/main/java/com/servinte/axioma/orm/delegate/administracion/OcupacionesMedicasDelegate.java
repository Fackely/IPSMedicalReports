package com.servinte.axioma.orm.delegate.administracion;

import java.util.List;

import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.orm.OcupacionesMedicasHome;

/**
 * 
 * Esta clase se encarga de ejecutar los métodos de negocio de la 
 * entidad Ocupaciones Médicas
 * 
 */
public class OcupacionesMedicasDelegate extends OcupacionesMedicasHome {
	
	/**
	 * 
	 * Este Método se encarga de consultar las ocupaciones médicas 
	 * existentes en el sistema
	 * 
	 * @return List<OcupacionesMedicas>
	 */
	public List<OcupacionesMedicas> listarOcupacionesMedicas() {	
		return sessionFactory.getCurrentSession().createCriteria(OcupacionesMedicas.class).list();
	}
}