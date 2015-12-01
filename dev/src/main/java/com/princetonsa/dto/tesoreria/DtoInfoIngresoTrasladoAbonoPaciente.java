/**
 * 
 */
package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Encaps&uacute;la la respuesta a la informaci&oacute;n de un paciente en el momento
 * de hacer un traslado de abonos
 * 
 * @author Cristhian Murillo
 */
public class DtoInfoIngresoTrasladoAbonoPaciente implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	int idIngreso;
	Object idIngresoObj;
	Integer centroAtencion;
	Object centroAtencionObj;
	String nombreCentroAtencion;
	double abonoDisponible;
	double abonoTrasladar;
	String estadoIngreso;
	int codigoPaciente;
	
	private String tipoId;
	private String numeroId;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	
	
	/**
	 *Atributo utilizado para guardar la fecha de egreso del ultimo ingreso del paciente 
	 */
	private Date fechaEgreso;
	
	/**
	 *Atributo utilizado para guardar la hora de egreso del ultimo ingreso del paciente 
	 */
	private String horaEgreso;
	
	
	public DtoInfoIngresoTrasladoAbonoPaciente(){
		this.idIngreso			= ConstantesBD.codigoNuncaValido;
		this.idIngresoObj		= null;
		this.centroAtencion		= ConstantesBD.codigoNuncaValido;	
		this.centroAtencionObj	= null;
		this.abonoDisponible	= ConstantesBD.codigoNuncaValidoDouble;
		this.abonoTrasladar		= ConstantesBD.codigoNuncaValidoDouble;
		this.estadoIngreso		= "";
		this.nombreCentroAtencion = "";
		this.codigoPaciente		= ConstantesBD.codigoNuncaValido;
		
		this.tipoId				= "";
		this.numeroId			= "";
		this.primerNombre		= "";
		this.segundoNombre		= "";
		this.primerApellido	  	= "";
		this.segundoApellido	= "";
	}


	public int getIdIngreso() {
		return idIngreso;
	}


	public Integer getCentroAtencion() {
		return centroAtencion;
	}


	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}


	public double getAbonoDisponible() {
		return abonoDisponible;
	}


	public String getEstadoIngreso() {
		return estadoIngreso;
	}


	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}


	public void setCentroAtencion(Integer centroAtencion) {
		if(centroAtencion != null){
			this.centroAtencion = centroAtencion;
		}
	}


	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}


	public void setAbonoDisponible(double abonoDisponible) {
		this.abonoDisponible = abonoDisponible;
	}


	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}


	public double getAbonoTrasladar() {
		return abonoTrasladar;
	}


	public void setAbonoTrasladar(double abonoTrasladar) {
		this.abonoTrasladar = abonoTrasladar;
	}


	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public String getTipoId() {
		return tipoId;
	}


	public String getNumeroId() {
		return numeroId;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	
	public Object getIdIngresoObj() {
		return idIngresoObj;
	}


	public void setIdIngresoObj(Object idIngresoObj) {
		this.idIngresoObj = idIngresoObj;
		if(idIngresoObj instanceof Integer){
			setIdIngreso(Integer.parseInt(idIngresoObj.toString()));
		}
	}


	public Object getCentroAtencionObj() {
		return centroAtencionObj;
	}


	public void setCentroAtencionObj(Object centroAtencionObj) {
		this.centroAtencionObj = centroAtencionObj;
		if(centroAtencionObj instanceof Integer){
			setCentroAtencion(Integer.parseInt(centroAtencionObj.toString()));
		}
	}


	/**
	 * @return the fechaEgreso
	 */
	public Date getFechaEgreso() {
		return fechaEgreso;
	}


	/**
	 * @param fechaEgreso the fechaEgreso to set
	 */
	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}


	/**
	 * @return the horaEgreso
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}


	/**
	 * @param horaEgreso the horaEgreso to set
	 */
	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}
	
}
