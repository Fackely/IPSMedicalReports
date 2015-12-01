package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;


/**
 * Contiene los parametros de busqueda para los pacientes de convenio odontologico
 * @author Cristhian Murillo
 *
 */
public class DtoBusquedaEmisionBonos implements Serializable{

	private static final long serialVersionUID = 1L;

	private int codigoConvenios;
	private int codigoContrato;
	private int codInstituciones;
	private long numeroSerial;
	private BigDecimal valorDescuento;
	private BigDecimal porcentajeDescuentos;
	private Date fecha;
	
	
	
	/**
	 * Constructor d ela clase
	 */
	public DtoBusquedaEmisionBonos(){
	
		this.codigoConvenios		= ConstantesBD.codigoNuncaValido;
		this.codigoContrato			= ConstantesBD.codigoNuncaValido;
		this.codInstituciones		= ConstantesBD.codigoNuncaValido;
		this.numeroSerial			= ConstantesBD.codigoNuncaValidoLong;
		this.valorDescuento			= null;
		this.porcentajeDescuentos	= null;
		this.fecha					= new Date();
	}



	public int getCodigoConvenios() {
		return codigoConvenios;
	}



	public void setCodigoConvenios(int codigoConvenios) {
		this.codigoConvenios = codigoConvenios;
	}



	public int getCodInstituciones() {
		return codInstituciones;
	}



	public void setCodInstituciones(int codInstituciones) {
		this.codInstituciones = codInstituciones;
	}



	public long getNumeroSerial() {
		return numeroSerial;
	}



	public void setNumeroSerial(long numeroSerial) {
		this.numeroSerial = numeroSerial;
	}



	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}



	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}



	public BigDecimal getPorcentajeDescuentos() {
		return porcentajeDescuentos;
	}



	public void setPorcentajeDescuentos(BigDecimal porcentajeDescuentos) {
		this.porcentajeDescuentos = porcentajeDescuentos;
	}



	public Date getFecha() {
		return fecha;
	}



	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}



	public int getCodigoContrato() {
		return codigoContrato;
	}



	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	

	
}
