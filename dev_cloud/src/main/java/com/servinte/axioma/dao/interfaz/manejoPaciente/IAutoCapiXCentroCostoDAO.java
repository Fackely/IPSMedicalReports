package com.servinte.axioma.dao.interfaz.manejoPaciente;

import com.servinte.axioma.orm.AutoCapiXCentroCosto;

/**
 * Esta clase se encarga de definir los metodos de
 * negocio para la entidad AutorizacionCapitacionXCentroCosto
 * 
 * @author Diana Ruiz
 * @since 14/02/2012
 */
public interface IAutoCapiXCentroCostoDAO {
	
	/**
	 * Implementacion del metodo merge de la super clase AutoCapiXCentroCostoHome
	 * @param AutoCapiXCentroCosto autoCapiXCentroCosto
	 * @return boolean
	 * @author Diana Ruiz
	 */
	public boolean sincronizarAutoCapiXCentroCosto(AutoCapiXCentroCosto autoCapiXCentroCosto);

}
