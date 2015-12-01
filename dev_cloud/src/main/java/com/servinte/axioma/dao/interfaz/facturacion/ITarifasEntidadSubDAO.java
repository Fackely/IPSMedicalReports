package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.TarifasEntidadSub;;

/**
 * Esta clase se encarga de definir los metodos de
 * negocio para la entidad TarifasEntidadSub
 * 
 * @author Diana Ruiz
 * @since 20/01/2012
 */
public interface ITarifasEntidadSubDAO {
	
	/**
	 * 
	 * Implementacion del metodo persist de la super clase TarifasEntidadSubHome
	 * 
	 * @param TarifasEntidadSub tarifasEntidadSub
	 * @return boolean
	 * 
	 * @author Diana Ruiz
	 *
	 */
	public boolean sincronizarTarifasEntidadSub(TarifasEntidadSub tarifasEntidadSub);
	

}
