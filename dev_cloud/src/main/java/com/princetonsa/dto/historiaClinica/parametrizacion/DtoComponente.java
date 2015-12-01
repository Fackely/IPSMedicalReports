/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Data Transfer Object: Componente Parametrizable
 * @author Sebastián Gómez R.
 *
 */
public class DtoComponente extends DtoElementoParam implements Serializable
{
	private InfoDatosInt tipo; //define el tipo de componente
	
	/**
	 * Bandera que indica si se ingresó información del componente
	 */
	private boolean ingresoInformacion;
	
	
	/**
	 * Resetea los datos del DTO
	 */
	public void clean()
	{
		this.tipo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.ingresoInformacion = false;
	}
	
	/**
	 * Constructor del DTO
	 *
	 */
	public DtoComponente()
	{
		super.clean();
		this.clean();
	}

	
	/**
	 * @return the tipo
	 */
	public int getCodigoTipo() {
		return tipo.getCodigo();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setCodigoTipo(int tipo) {
		this.tipo.setCodigo(tipo);
	}
	
	/**
	 * @return the tipo
	 */
	public String getNombreTipo() {
		return tipo.getNombre();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setNombreTipo(String tipo) {
		this.tipo.setNombre(tipo);
	}

	/**
	 * @return the ingresoInformacion
	 */
	public boolean isIngresoInformacion() {
		return ingresoInformacion;
	}

	/**
	 * @param ingresoInformacion the ingresoInformacion to set
	 */
	public void setIngresoInformacion(boolean ingresoInformacion) {
		this.ingresoInformacion = ingresoInformacion;
	}
	
}
