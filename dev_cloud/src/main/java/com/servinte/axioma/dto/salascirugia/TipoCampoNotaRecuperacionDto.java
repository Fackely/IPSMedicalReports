/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class TipoCampoNotaRecuperacionDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5731111584205176891L;

	private List<CampoNotaRecuperacionDto>camposNotaRecuperacion=new ArrayList<CampoNotaRecuperacionDto>();

	private String nombreTipo;
	
	private int codigoTipo;
	
	
	public List<CampoNotaRecuperacionDto> getCamposNotaRecuperacion() {
		return camposNotaRecuperacion;
	}

	public void setCamposNotaRecuperacion(
			List<CampoNotaRecuperacionDto> camposNotaRecuperacion) {
		this.camposNotaRecuperacion = camposNotaRecuperacion;
	}

	public String getNombreTipo() {
		return nombreTipo;
	}

	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

	public int getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(int codigoTipo) {
		this.codigoTipo = codigoTipo;
	}
	
	
}
