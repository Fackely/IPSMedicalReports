/*
 * Ago 04, 2009
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.dto.administracion.DtoConceptosRetencion;

/**
 * 
 * DTO: que representa los conceptosd e retencion de un tercero
 * @author Sebastián Gómez R.
 *
 */
public class DtoConceptoRetencionTercero  extends DtoConceptosRetencion implements Serializable  
{
	private String consecutivoConceptoTercero;
	private InfoDatosInt tercero;
	private InfoDatosInt tipoAplicacion;
	private boolean indicativoAgenteRetenedor;
	
	//Atributos destinados al calculo de la retencion
	private String valorRetencion;
	private String valorBaseGravable;
	private boolean calculoRetencionExitoso;
	private boolean cancelarRetencion;
	
	public void clean()
	{
		this.consecutivoConceptoTercero = "";
		this.tercero = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tipoAplicacion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.indicativoAgenteRetenedor = false;
		
		this.valorRetencion = "";
		this.valorBaseGravable = "";
		this.calculoRetencionExitoso = false;
		this.cancelarRetencion = false;
	}
	
	/**
	 * Constructor
	 */
	public DtoConceptoRetencionTercero()
	{
		super.clean();
		this.clean();
	}

	/**
	 * @return the consecutivoConceptoTercero
	 */
	public String getConsecutivoConceptoTercero() {
		return consecutivoConceptoTercero;
	}

	/**
	 * @param consecutivoConceptoTercero the consecutivoConceptoTercero to set
	 */
	public void setConsecutivoConceptoTercero(String consecutivoConceptoTercero) {
		this.consecutivoConceptoTercero = consecutivoConceptoTercero;
	}

	/**
	 * @return the tercero
	 */
	public InfoDatosInt getTercero() {
		return tercero;
	}

	/**
	 * @param tercero the tercero to set
	 */
	public void setTercero(InfoDatosInt tercero) {
		this.tercero = tercero;
	}

	/**
	 * @return the tipoAplicacion
	 */
	public InfoDatosInt getTipoAplicacion() {
		return tipoAplicacion;
	}

	/**
	 * @param tipoAplicacion the tipoAplicacion to set
	 */
	public void setTipoAplicacion(InfoDatosInt tipoAplicacion) {
		this.tipoAplicacion = tipoAplicacion;
	}

	/**
	 * @return the indicativoAgenteRetenedor
	 */
	public boolean isIndicativoAgenteRetenedor() {
		return indicativoAgenteRetenedor;
	}

	/**
	 * @param indicativoAgenteRetenedor the indicativoAgenteRetenedor to set
	 */
	public void setIndicativoAgenteRetenedor(boolean indicativoAgenteRetenedor) {
		this.indicativoAgenteRetenedor = indicativoAgenteRetenedor;
	}

	/**
	 * @return the valorRetencion
	 */
	public String getValorRetencion() {
		return valorRetencion;
	}

	/**
	 * @param valorRetencion the valorRetencion to set
	 */
	public void setValorRetencion(String valorRetencion) {
		this.valorRetencion = valorRetencion;
	}

	/**
	 * @return the valorBaseGravable
	 */
	public String getValorBaseGravable() {
		return valorBaseGravable;
	}

	/**
	 * @param valorBaseGravable the valorBaseGravable to set
	 */
	public void setValorBaseGravable(String valorBaseGravable) {
		this.valorBaseGravable = valorBaseGravable;
	}

	/**
	 * @return the calculoRetencionExitoso
	 */
	public boolean isCalculoRetencionExitoso() {
		return calculoRetencionExitoso;
	}

	/**
	 * @param calculoRetencionExitoso the calculoRetencionExitoso to set
	 */
	public void setCalculoRetencionExitoso(boolean calculoRetencionExitoso) {
		this.calculoRetencionExitoso = calculoRetencionExitoso;
	}

	/**
	 * @return the cancelarRetencion
	 */
	public boolean isCancelarRetencion() {
		return cancelarRetencion;
	}

	/**
	 * @param cancelarRetencion the cancelarRetencion to set
	 */
	public void setCancelarRetencion(boolean cancelarRetencion) {
		this.cancelarRetencion = cancelarRetencion;
	}
	
}
