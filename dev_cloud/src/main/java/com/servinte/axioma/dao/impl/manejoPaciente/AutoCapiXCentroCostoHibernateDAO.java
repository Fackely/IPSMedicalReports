package com.servinte.axioma.dao.impl.manejoPaciente;

import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutoCapiXCentroCostoDAO;
import com.servinte.axioma.orm.AutoCapiXCentroCosto;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutoCapiXCentroCostoDelegate;

/**
 * Esta clase se encarga de implementar los metodos de
 * negocio para la entidad AutorizacionCapitacionXCentroCosto
 * 
 * @author Diana Ruiz
 * @since 14/02/2012
 */
public class AutoCapiXCentroCostoHibernateDAO implements IAutoCapiXCentroCostoDAO{

	
	AutoCapiXCentroCostoDelegate autoCapiXCentroCostoDelegate;
	
	public AutoCapiXCentroCostoHibernateDAO() {
		autoCapiXCentroCostoDelegate=new AutoCapiXCentroCostoDelegate();
	}
		
	/**
	 * Implementacion del metodo persist de la super clase TarifasEntidadSubHome
	 * @param TarifasEntidadSub tarifasEntidadSub
	 * @return boolean 
	 * @author Diana Ruiz
	 */
	
	public boolean sincronizarAutoCapiXCentroCosto(AutoCapiXCentroCosto autoCapiXCentroCosto) {
		return autoCapiXCentroCostoDelegate.sincronizarAutoCapiXCentroCosto(autoCapiXCentroCosto);
	}	

}
