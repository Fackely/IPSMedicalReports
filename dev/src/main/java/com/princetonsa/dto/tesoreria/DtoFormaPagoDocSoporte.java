package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoFormaPagoDocSoporte implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String seleccionado;
	private ArrayList<DtoEntidadesFinancieras> listadoDtoEntidadesFinancieras;
	private DtoCuadreCaja dtoCuadreCaja;
	private String formaPago;
	private int consecutivoFormaPago;
	private int codigoTipoDetalleFormaPago;
	
	
	/**
	 * Atributo que indica si la forma de pago puede ser entregada en el Cierre de Turno de Caja
	 */
	private boolean registrarEnCierreTurno ;
	
	
	/*
	 * Es necesario tener en cuenta las formas de pago que pueden ser trasladadas a otras
	 * cajas de recaudo. Este atributo solo aplica cuando se desea realizar una Solicitud 
	 * de Traslado a Caja de Recaudo - Anexo 903
	 */
	private String indicativoTrasladoCajaRecaudo;
	
	/**
	 * Atributo que indica si la forma de pago requiere traslado a 
	 * caja de recaudo.
	 */
	private Character reqTrasladoCajaRecaudo;
	
	/**
	 * Atributo que indica si se ha seleccionado la forma de pago
	 * para que sea registrada en el traslado a caja de recaudo.
	 */
	private String registrarTraslado;
	
	public DtoFormaPagoDocSoporte() {
		
		listadoDtoEntidadesFinancieras = new ArrayList<DtoEntidadesFinancieras>();
		seleccionado = ConstantesBD.acronimoSi;
		dtoCuadreCaja = new DtoCuadreCaja();
		formaPago = "";
		consecutivoFormaPago = ConstantesBD.codigoNuncaValido;
		indicativoTrasladoCajaRecaudo = ConstantesBD.acronimoNo;
		registrarEnCierreTurno = false;
		this.reqTrasladoCajaRecaudo = ConstantesBD.acronimoNoChar;
		this.registrarTraslado = ConstantesBD.acronimoSi;
	}

	public String getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(String seleccionado) {
		
		this.seleccionado = seleccionado;
	}

	public ArrayList<DtoEntidadesFinancieras> getListadoDtoEntidadesFinancieras() {
		return listadoDtoEntidadesFinancieras;
	}

	public void setListadoDtoEntidadesFinancieras(
			ArrayList<DtoEntidadesFinancieras> listadoDtoEntidadesFinancieras) {
		this.listadoDtoEntidadesFinancieras = listadoDtoEntidadesFinancieras;
	}

	public DtoCuadreCaja getDtoCuadreCaja() {
		return dtoCuadreCaja;
	}

	public void setDtoCuadreCaja(DtoCuadreCaja dtoCuadreCaja) {
		this.dtoCuadreCaja = dtoCuadreCaja;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public int getConsecutivoFormaPago() {
		return consecutivoFormaPago;
	}

	public void setConsecutivoFormaPago(int consecutivoFormaPago) {
		this.consecutivoFormaPago = consecutivoFormaPago;
	}

	public void setCodigoTipoDetalleFormaPago(int codigoTipoDetalleFormaPago) {
		this.codigoTipoDetalleFormaPago = codigoTipoDetalleFormaPago;
	}

	public int getCodigoTipoDetalleFormaPago() {
		return codigoTipoDetalleFormaPago;
	}

	public void setIndicativoTrasladoCajaRecaudo(
			String indicativoTrasladoCajaRecaudo) {
		this.indicativoTrasladoCajaRecaudo = indicativoTrasladoCajaRecaudo;
	}

	public String getIndicativoTrasladoCajaRecaudo() {
		return indicativoTrasladoCajaRecaudo;
	}

	public void setRegistrarEnCierreTurno(boolean registrarEnCierreTurno) {
		this.registrarEnCierreTurno = registrarEnCierreTurno;
	}

	public boolean isRegistrarEnCierreTurno() {
		return registrarEnCierreTurno;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo reqTrasladoCajaRecaudo
	 * @return retorna la variable reqTrasladoCajaRecaudo 
	 * @author Yennifer Guerrero 
	 */
	public Character getReqTrasladoCajaRecaudo() {
		return reqTrasladoCajaRecaudo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo reqTrasladoCajaRecaudo
	 * @param valor para el atributo reqTrasladoCajaRecaudo 
	 * @author Yennifer Guerrero
	 */
	public void setReqTrasladoCajaRecaudo(Character reqTrasladoCajaRecaudo) {
		this.reqTrasladoCajaRecaudo = reqTrasladoCajaRecaudo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo registrarTraslado
	 * @return retorna la variable registrarTraslado 
	 * @author Yennifer Guerrero 
	 */
	public String getRegistrarTraslado() {
		
		if (this.reqTrasladoCajaRecaudo.equals(ConstantesBD.acronimoSiChar)) {
			this.registrarTraslado = ConstantesBD.acronimoSi;
		}
		
		return registrarTraslado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo registrarTraslado
	 * @param valor para el atributo registrarTraslado 
	 * @author Yennifer Guerrero
	 */
	public void setRegistrarTraslado(String registrarTraslado) {
		this.registrarTraslado = registrarTraslado;
	}
}
