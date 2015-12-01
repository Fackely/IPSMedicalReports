package com.servinte.axioma.dao.interfaz.tesoreria;

import com.servinte.axioma.orm.DetallePagosRc;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de DetallePagosRc
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public interface IDetallePagosRcDAO {

	/**
	 * Retorna un DetallePagosRc espec&iacute;fico
	 * 
	 * @param codigoDetalle
	 * @return DetallePagosRc
	 */
	public DetallePagosRc findById(int codigoDetalle);

	/**
	 * Guardar o modificar
	 * @param detallePagos
	 */
	public void attachDirty(DetallePagosRc detallePagos);
	
	
}
