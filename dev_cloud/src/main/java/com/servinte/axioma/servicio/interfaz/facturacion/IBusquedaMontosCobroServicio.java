/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.facturacion;


import java.util.Collection;

import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.princetonsa.dto.facturacion.DtoFiltroBusquedaMontosCobro;

/**
 * @author axioma
 *
 */
public interface IBusquedaMontosCobroServicio
{
	/**
	 * 
	 * @param dtoFiltro
	 * @return
	 */
	public DtoBusquedaMontosCobro consultarMontosCobro(DtoFiltroBusquedaMontosCobro dtoFiltro);

	/**
	 * 
	 * @param clasificacionSocioEconomica
	 * @param convenio
	 * @param fechaAperturaCuenta
	 * @param naturalezaPaciente
	 * @param tipoAfiliado
	 * @param tipoPaciente
	 * @param viaIngreso
	 * @return
	 */
	public DtoBusquedaMontosCobro consultarMontosCobro(int clasificacionSocioEconomica,int convenio,String fechaAperturaCuenta,int naturalezaPaciente,String tipoAfiliado,String tipoPaciente, int viaIngreso); 
	
}
