package com.servinte.axioma.mundo.interfaz.tesoreria;

import com.servinte.axioma.orm.DetallePagosRc;

/**
 * Define la l&oacute;gica de negocio relacionada con los DetallesPagosRc
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.DetallePagosRcMundo
 */


public interface IDetallePagosRcMundo {

	/**
	 * Retorna un DetallePagosRc espec&iacute;fico
	 * 
	 * @param codigoDetalle
	 * @return DetallePagosRc
	 */
	public DetallePagosRc findById(int codigoDetalle);
	
	
}
