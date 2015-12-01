package com.princetonsa.actionform.tesoreria;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * Form que contiene los datos espec&iacute;ficos para generar 
 * el Arqueo de Caja (Anexo 226).
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * La informaci&oacute;n com&uacute;n a los procesos de arqueo (Arqueo Caja, Arqueo Entrega Parcial
 * y Cierre Turno de Caja) se encuentra contenida en MovimientosCajaForm.
 *
 * @author Jorge Armando Agudelo Quintero
 * @see MovimientosCajaForm
 *
 */
public class ArqueosCajaForm extends MovimientosCajaForm{
	
	private static final long serialVersionUID = 1L;
	
	private boolean imprimeCuadreCaja;
	
	/*
	 * Constructor del form
	 */
	public ArqueosCajaForm() {
		
	}
	
	/**
	 * Constructor del form. Recibe un objeto de tipo MovimientosCajaForm, del cual herada.
	 * @param forma
	 */
	public ArqueosCajaForm(MovimientosCajaForm forma) {
		this.setListadoCajas(forma.getListadoCajas());
		this.setListadoCajeros(forma.getListadoCajeros());
		this.setListadoTiposArqueo(forma.getListadoTiposArqueo());
		this.setTurnoDeCaja(forma.getTurnoDeCaja());
		this.setFechaUltimoMovimiento(forma.getFechaUltimoMovimiento());
		this.setListadoTiposReporte(forma.getListadoTiposReporte());
		this.setListaNombresReportes(forma.getListaNombresReportes());
	}

	/**
	 * M&eacute;todo encargado de procesar los errores presentados
	 * @param forma
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=super.validate(mapping, request);
		
		return errores;
	}
	
	/**
	 * @return the imprimeCuadreCaja
	 */
	public boolean isImprimeCuadreCaja() {
		return imprimeCuadreCaja;
	}

	/**
	 * @param imprimeCuadreCaja the imprimeCuadreCaja to set
	 */
	public void setImprimeCuadreCaja(boolean imprimeCuadreCaja) {
		this.imprimeCuadreCaja = imprimeCuadreCaja;
	}
	
}