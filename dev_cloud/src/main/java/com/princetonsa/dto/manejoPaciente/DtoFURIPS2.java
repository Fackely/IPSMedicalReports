package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoFURIPS2 implements Serializable 
{
	/**
	 * 
	 */
	public String[] nombresCampos=	{"",
									/*1*/"Número  factura o Número de cuenta de cobro",
									/*2*/"Número del consecutivo de la reclamación",
									/*3*/"Tipo de Servicios",
									/*4*/"Código Servicio",
									/*5*/"Descripción del Insumo",
									/*6*/"Cantidad de Servicios",
									/*7*/"Valor Unitario",
									/*8*/"Valor total facturado",
									/*9*/"Valor total reclamado al fosyga"
									};
	
	/**
	 * 
	 */
	private boolean esDesplazado;
	
	/**
	 * 
	 */
	private String numeroIdPaciente;
	
	/**
	 * 
	 */
	private String tipoIdPaciente;
	
	/**
	 * 
	 */
	private String nombresPaciente;
	
	/**
	 * 
	 */
	private String idCuenta;
	
	/**
	 * 
	 */
	private String naturalezaEvento;
	
	/**
	 * 
	 */
	private boolean esServicio;
	
	/**
	 * 
	 */
	private String nombreViaIngreso;
	
	/**
	 * 
	 */
	private String idFactura;
	
	/**
	 * 
	 */
	private String idCuentaCobro;
	
	/**
	 * 
	 */
	private boolean esInstitucionPublica;
	
	/**
	 * 
	 */
	private String numeroFacturaNumeroCXC;
	
	/**
	 * 
	 */
	private String numeroConsecutivoReclamacion;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * 
	 */
	private String codigoServicio;
	
	/**
	 * 
	 */
	private String descripcionInsumo;
	
	/**
	 * 
	 */
	private String cantidadServicio;
	
	/**
	 * 
	 */
	private String valorUnitario;
	
	/**
	 * 
	 */
	private String valorTotalFacgturado;

	/**
	 * 
	 */
	private String valorTotalReclamadoFosyga;
	
	
	/**
	 * 
	 */
	public DtoFURIPS2() 
	{
		super();
		this.numeroFacturaNumeroCXC = "";
		this.numeroConsecutivoReclamacion = "";
		this.tipoServicio = "";
		this.codigoServicio = "";
		this.descripcionInsumo = "";
		this.valorUnitario = "";
		this.valorTotalFacgturado = "";
		this.valorTotalFacgturado = "";
		this.valorTotalReclamadoFosyga="";
		this.esServicio = false;
		this.idCuenta = "";
		this.naturalezaEvento = "";
		this.nombreViaIngreso = "";
		this.nombresPaciente = "";
		this.numeroIdPaciente = "";
		this.tipoIdPaciente = "";
		this.esDesplazado=false;
		this.idCuentaCobro="";
		this.esInstitucionPublica=false;
		this.idFactura="";
		this.cantidadServicio="";
	}

	/**
	 * @return the numeroIdPaciente
	 */
	public String getNumeroIdPaciente() {
		return numeroIdPaciente;
	}

	/**
	 * @param numeroIdPaciente the numeroIdPaciente to set
	 */
	public void setNumeroIdPaciente(String numeroIdPaciente) {
		this.numeroIdPaciente = numeroIdPaciente;
	}

	/**
	 * @return the tipoIdPaciente
	 */
	public String getTipoIdPaciente() {
		return tipoIdPaciente;
	}

	/**
	 * @param tipoIdPaciente the tipoIdPaciente to set
	 */
	public void setTipoIdPaciente(String tipoIdPaciente) {
		this.tipoIdPaciente = tipoIdPaciente;
	}

	/**
	 * @return the nombresPaciente
	 */
	public String getNombresPaciente() {
		return nombresPaciente;
	}

	/**
	 * @param nombresPaciente the nombresPaciente to set
	 */
	public void setNombresPaciente(String nombresPaciente) {
		this.nombresPaciente = nombresPaciente;
	}

	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return the naturalezaEvento
	 */
	public String getNaturalezaEvento() {
		return naturalezaEvento;
	}

	/**
	 * @param naturalezaEvento the naturalezaEvento to set
	 */
	public void setNaturalezaEvento(String naturalezaEvento) {
		this.naturalezaEvento = naturalezaEvento;
	}

	/**
	 * @return the esServicio
	 */
	public boolean isEsServicio() {
		return esServicio;
	}

	/**
	 * @param esServicio the esServicio to set
	 */
	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}

	/**
	 * @return the nombreViaIngreso
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	/**
	 * @param nombreViaIngreso the nombreViaIngreso to set
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	
	/**
	 * @return the esDesplazado
	 */
	public boolean isEsDesplazado() {
		return esDesplazado;
	}

	/**
	 * @param esDesplazado the esDesplazado to set
	 */
	public void setEsDesplazado(boolean esDesplazado) {
		this.esDesplazado = esDesplazado;
	}

	

	/**
	 * @return the idFactura
	 */
	public String getIdFactura() {
		return idFactura;
	}

	/**
	 * @param idFactura the idFactura to set
	 */
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}

	/**
	 * @return the idCuentaCobro
	 */
	public String getIdCuentaCobro() {
		return idCuentaCobro;
	}

	/**
	 * @param idCuentaCobro the idCuentaCobro to set
	 */
	public void setIdCuentaCobro(String idCuentaCobro) {
		this.idCuentaCobro = idCuentaCobro;
	}

	/**
	 * @return the esInstitucionPublica
	 */
	public boolean isEsInstitucionPublica() {
		return esInstitucionPublica;
	}

	/**
	 * @param esInstitucionPublica the esInstitucionPublica to set
	 */
	public void setEsInstitucionPublica(boolean esInstitucionPublica) {
		this.esInstitucionPublica = esInstitucionPublica;
	}

	public String[] getNombresCampos() {
		return nombresCampos;
	}

	public void setNombresCampos(String[] nombresCampos) {
		this.nombresCampos = nombresCampos;
	}

	public String getNumeroFacturaNumeroCXC() {
		return numeroFacturaNumeroCXC;
	}

	public void setNumeroFacturaNumeroCXC(String numeroFacturaNumeroCXC) {
		this.numeroFacturaNumeroCXC = numeroFacturaNumeroCXC;
	}

	public String getNumeroConsecutivoReclamacion() {
		return numeroConsecutivoReclamacion;
	}

	public void setNumeroConsecutivoReclamacion(String numeroConsecutivoReclamacion) {
		this.numeroConsecutivoReclamacion = numeroConsecutivoReclamacion;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getDescripcionInsumo() {
		return descripcionInsumo;
	}

	public void setDescripcionInsumo(String descripcionInsumo) {
		this.descripcionInsumo = descripcionInsumo;
	}

	public String getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public String getValorTotalFacgturado() {
		return valorTotalFacgturado;
	}

	public void setValorTotalFacgturado(String valorTotalFacgturado) {
		this.valorTotalFacgturado = valorTotalFacgturado;
	}

	public String getValorTotalReclamadoFosyga() {
		return valorTotalReclamadoFosyga;
	}

	public void setValorTotalReclamadoFosyga(String valorTotalReclamadoFosyga) {
		this.valorTotalReclamadoFosyga = valorTotalReclamadoFosyga;
	}

	public String getCantidadServicio() {
		return cantidadServicio;
	}

	public void setCantidadServicio(String cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}

	
	
	
}
