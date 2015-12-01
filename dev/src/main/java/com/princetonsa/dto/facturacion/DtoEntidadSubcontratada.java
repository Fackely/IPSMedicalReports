/*
 * Jun 24/09
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * DTO Data transfer Object para el manejo de las entidades subcontratads
 * @author Sebastián Gómez R.
 */
public class DtoEntidadSubcontratada implements Serializable
{
	/**
	 * Atributos
	 */
	private static final long serialVersionUID = 1L;
	private long codigoPk;
	private String consecutivo;
	private String codigo;
	private String razonSocial;
	private String codigoMinsalud;
	private String direccion;
	private String telefono;
	private String direccionotra;
	private String telefonootra;
	private String descripcionEntidad;
	private String prioridad;
	private boolean respOtrosUsua;
	private Integer nroPrioridad;
	/** * Usada para indicar si es valida para realizar alguna accion sobre esa entidad  */
	private boolean valida;
	private ArrayList<DtoEntidadSubcontratada> agrupaListadoEntidSubCobertu;
	private Integer viaIngreso;
	private boolean permiteEstanciaPaciente;
	private long contratoEntSub;
	private String tipotarifa;
	
	
	/**
	 * Constructor
	 */
	public DtoEntidadSubcontratada()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValidoLong;
		this.consecutivo = "";
		this.codigo = "";
		this.razonSocial = "";
		this.codigoMinsalud = "";
		this.direccion="";
		this.telefono="";
		this.direccionotra="";
		this.telefonootra="";
		this.descripcionEntidad="";
		this.prioridad="";
		this.respOtrosUsua = false;
		this.nroPrioridad = null;
		this.valida			 				= false;
		this.agrupaListadoEntidSubCobertu 	= new ArrayList<DtoEntidadSubcontratada>();
		this.viaIngreso 					= null;
		this.permiteEstanciaPaciente        = false;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the codigoMinsalud
	 */
	public String getCodigoMinsalud() {
		return codigoMinsalud;
	}

	/**
	 * @param codigoMinsalud the codigoMinsalud to set
	 */
	public void setCodigoMinsalud(String codigoMinsalud) {
		this.codigoMinsalud = codigoMinsalud;
	}

	public boolean isRespOtrosUsua() {
		return respOtrosUsua;
	}

	public void setRespOtrosUsua(boolean respOtrosUsua) {
		this.respOtrosUsua = respOtrosUsua;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the prioridad
	 */
	public String getPrioridad() {
		return prioridad;
	}

	/**
	 * @param prioridad the prioridad to set
	 */
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public long getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public boolean isValida() {
		return valida;
	}

	public void setValida(boolean valida) {
		this.valida = valida;
	}
	
	public Integer getNroPrioridad() {
		return nroPrioridad;
	}

	public void setNroPrioridad(Integer nroPrioridad) {
		this.nroPrioridad = nroPrioridad;
	}

	public void setAgrupaListadoEntidSubCobertu(
			ArrayList<DtoEntidadSubcontratada> agrupaListadoEntidSubCobertu) {
		this.agrupaListadoEntidSubCobertu = agrupaListadoEntidSubCobertu;
	}

	public ArrayList<DtoEntidadSubcontratada> getAgrupaListadoEntidSubCobertu() {
		return agrupaListadoEntidSubCobertu;
	}

	public Integer getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(Integer viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the permiteEstanciaPaciente
	 */
	public boolean isPermiteEstanciaPaciente() {
		return permiteEstanciaPaciente;
	}

	/**
	 * @param permiteEstanciaPaciente the permiteEstanciaPaciente to set
	 */
	public void setPermiteEstanciaPaciente(boolean permiteEstanciaPaciente) {
		this.permiteEstanciaPaciente = permiteEstanciaPaciente;
	}

	public void setDireccionotra(String direccionotra) {
		this.direccionotra = direccionotra;
	}

	public String getDireccionotra() {
		return direccionotra;
	}

	public void setTelefonootra(String telefonootra) {
		this.telefonootra = telefonootra;
	}

	public String getTelefonootra() {
		return telefonootra;
	}

	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}

	public String getDescripcionEntidad() {
		return descripcionEntidad;
	}
	
	/**
	 * @return contratoEntSub
	 */
	public long getContratoEntSub() {
		return contratoEntSub;
	}
	
	/**
	 * @param contratoEntSub
	 */
	public void setContratoEntSub(long contratoEntSub) {
		this.contratoEntSub = contratoEntSub;
	}
	
	/**
	 * @return tipoTarifaEntSub
	 */
	public String getTipotarifa() {
		return tipotarifa;
	}
	
	/**
	 * @param tipoTarifaEntSub
	 */
	public void setTipotarifa(String tipotarifa) {
		this.tipotarifa = tipotarifa;
	}

}
