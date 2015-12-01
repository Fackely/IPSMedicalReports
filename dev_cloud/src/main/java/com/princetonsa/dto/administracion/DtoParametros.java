package com.princetonsa.dto.administracion;

import java.io.Serializable;

/**
 * Encaps&uacute;la los estados de los parametros que se quieren validar
 * @author Cristhian Murillo
 */
@SuppressWarnings("serial")
public class DtoParametros implements Serializable{
	
	
	/**
	 * Indica si se tienen definidos o no consecutivos disponibles 
	 * para el registro de detalles de faltante sobrante 
	 */
	private boolean tieneDefinidoConsecutivoFaltanteSobrante;
	
	
	
	public DtoParametros()
	{
		this.tieneDefinidoConsecutivoFaltanteSobrante = false;
	}


	
	
	public boolean isTieneDefinidoConsecutivoFaltanteSobrante() {
		return tieneDefinidoConsecutivoFaltanteSobrante;
	}


	public void setTieneDefinidoConsecutivoFaltanteSobrante(
			boolean tieneDefinidoConsecutivoFaltanteSobrante) {
		this.tieneDefinidoConsecutivoFaltanteSobrante = tieneDefinidoConsecutivoFaltanteSobrante;
	}
	
	
}
