/**
 * 
 */
package com.servinte.axioma.mundo.impl.facturacion;

import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.princetonsa.dto.facturacion.DtoFiltroBusquedaMontosCobro;

/**
 * @author axioma
 *
 */
public interface IBusquedaMontosCobroMundo 
{

	/**
	 * 
	 * @param dtoFiltro
	 * @return
	 */
	public DtoBusquedaMontosCobro consultarMontosCobro(DtoFiltroBusquedaMontosCobro dtoFiltro);


}
