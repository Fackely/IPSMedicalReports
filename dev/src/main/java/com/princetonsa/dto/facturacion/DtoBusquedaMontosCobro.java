/**
 * 
 */
package com.princetonsa.dto.facturacion;

import java.util.ArrayList;
import java.util.Collection;

import util.ResultadoBoolean;

/**
 * @author axioma
 *
 */
public class DtoBusquedaMontosCobro 
{
	
	private ResultadoBoolean resultado;
	
	private Collection<DTOResultadoBusquedaDetalleMontos> montosCobro;
	
	/**
	 * 
	 */
	public DtoBusquedaMontosCobro()
	{
		this.resultado=new ResultadoBoolean(false);
		this.montosCobro=new ArrayList<DTOResultadoBusquedaDetalleMontos>();
	}

	public ResultadoBoolean getResultado() {
		return resultado;
	}

	public void setResultado(ResultadoBoolean resultado) {
		this.resultado = resultado;
	}

	public Collection<DTOResultadoBusquedaDetalleMontos> getMontosCobro() {
		return montosCobro;
	}

	public void setMontosCobro(Collection<DTOResultadoBusquedaDetalleMontos> montosCobro) {
		this.montosCobro = montosCobro;
	}

	
	

}
