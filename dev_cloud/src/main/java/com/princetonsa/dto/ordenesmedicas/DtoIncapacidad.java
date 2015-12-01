/**
 * 
 */
package com.princetonsa.dto.ordenesmedicas;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class DtoIncapacidad implements Serializable{
	
	private String centroAtencion;
	
	private int noIngreso;
	
	private String fechaHoraAtencion;
	
	private String fechaHoraGeneracion;
	
	private String usuarioGeneracion;
	
	private int ingreso;
	
	private int codCentroAtencion;
	
	private String fechaInicialGeneracion;
	
	private String fechaFinalGeneracion;
	
	private int viaIngreso;
	
	private int convenio;
	
	private String nomViaIngreso;
	
	private String nomConvenio;
	
	private String nomPaciente;
	
	private String idenPaciente;
	
	private int codigoPaciente;
	
	private int codigoPk;
	
	private int prioridad;
	
	
	public DtoIncapacidad(){
		this.clear();
	}
	
	public DtoIncapacidad(DtoIncapacidad incapacidad){
		this.setThis(incapacidad);
	}
	
	public void clear(){
		this.centroAtencion = new String("");
		this.noIngreso = ConstantesBD.codigoNuncaValido;
		this.fechaHoraAtencion = new String("");
		this.fechaHoraGeneracion = new String("");
		this.usuarioGeneracion = new String("");
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.codCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.fechaInicialGeneracion = new String("");
		this.fechaFinalGeneracion = new String("");
		this.viaIngreso = ConstantesBD.codigoNuncaValido;
		this.convenio = ConstantesBD.codigoNuncaValido;
		this.nomViaIngreso = new String("");
		this.nomConvenio = new String("");
		this.nomPaciente = new String("");
		this.idenPaciente = new String("");
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.prioridad = ConstantesBD.codigoNuncaValido;
	}

	public void setThis(DtoIncapacidad incapacidad){
		this.centroAtencion = incapacidad.getCentroAtencion();
		this.noIngreso = incapacidad.getNoIngreso();
		this.fechaHoraAtencion = incapacidad.getFechaHoraAtencion();
		this.fechaHoraGeneracion = incapacidad.getFechaHoraGeneracion();
		this.usuarioGeneracion = incapacidad.getUsuarioGeneracion();
		this.ingreso = incapacidad.getIngreso();
		this.codCentroAtencion = incapacidad.getCodCentroAtencion();
		this.fechaInicialGeneracion = incapacidad.getFechaInicialGeneracion();
		this.fechaFinalGeneracion = incapacidad.getFechaFinalGeneracion();
		this.viaIngreso = incapacidad.getViaIngreso();
		this.convenio = incapacidad.getConvenio();
		this.nomViaIngreso = incapacidad.getNomViaIngreso();
		this.nomConvenio = incapacidad.getNomConvenio();
		this.nomPaciente = incapacidad.getNomPaciente();
		this.idenPaciente = incapacidad.getIdenPaciente();
		this.codigoPaciente = incapacidad.getCodigoPaciente();
		this.codigoPk = incapacidad.getCodigoPk();
		this.prioridad = incapacidad.getPrioridad();
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the noIngreso
	 */
	public int getNoIngreso() {
		return noIngreso;
	}

	/**
	 * @param noIngreso the noIngreso to set
	 */
	public void setNoIngreso(int noIngreso) {
		this.noIngreso = noIngreso;
	}

	/**
	 * @return the fechaHoraAtencion
	 */
	public String getFechaHoraAtencion() {
		return fechaHoraAtencion;
	}

	/**
	 * @param fechaHoraAtencion the fechaHoraAtencion to set
	 */
	public void setFechaHoraAtencion(String fechaHoraAtencion) {
		this.fechaHoraAtencion = fechaHoraAtencion;
	}

	/**
	 * @return the fechaHoraGeneracion
	 */
	public String getFechaHoraGeneracion() {
		return fechaHoraGeneracion;
	}

	/**
	 * @param fechaHoraGeneracion the fechaHoraGeneracion to set
	 */
	public void setFechaHoraGeneracion(String fechaHoraGeneracion) {
		this.fechaHoraGeneracion = fechaHoraGeneracion;
	}

	/**
	 * @return the usuarioGeneracion
	 */
	public String getUsuarioGeneracion() {
		return usuarioGeneracion;
	}

	/**
	 * @param usuarioGeneracion the usuarioGeneracion to set
	 */
	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the codCentroAtencion
	 */
	public int getCodCentroAtencion() {
		return codCentroAtencion;
	}

	/**
	 * @param codCentroAtencion the codCentroAtencion to set
	 */
	public void setCodCentroAtencion(int codCentroAtencion) {
		this.codCentroAtencion = codCentroAtencion;
	}

	/**
	 * @return the fechaInicialGeneracion
	 */
	public String getFechaInicialGeneracion() {
		return fechaInicialGeneracion;
	}

	/**
	 * @param fechaInicialGeneracion the fechaInicialGeneracion to set
	 */
	public void setFechaInicialGeneracion(String fechaInicialGeneracion) {
		this.fechaInicialGeneracion = fechaInicialGeneracion;
	}

	/**
	 * @return the fechaFinalGeneracion
	 */
	public String getFechaFinalGeneracion() {
		return fechaFinalGeneracion;
	}

	/**
	 * @param fechaFinalGeneracion the fechaFinalGeneracion to set
	 */
	public void setFechaFinalGeneracion(String fechaFinalGeneracion) {
		this.fechaFinalGeneracion = fechaFinalGeneracion;
	}

	/**
	 * @return the viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the nomViaIngreso
	 */
	public String getNomViaIngreso() {
		return nomViaIngreso;
	}

	/**
	 * @param nomViaIngreso the nomViaIngreso to set
	 */
	public void setNomViaIngreso(String nomViaIngreso) {
		this.nomViaIngreso = nomViaIngreso;
	}

	/**
	 * @return the nomConvenio
	 */
	public String getNomConvenio() {
		return nomConvenio;
	}

	/**
	 * @param nomConvenio the nomConvenio to set
	 */
	public void setNomConvenio(String nomConvenio) {
		this.nomConvenio = nomConvenio;
	}

	/**
	 * @return the nomPaciente
	 */
	public String getNomPaciente() {
		return nomPaciente;
	}

	/**
	 * @param nomPaciente the nomPaciente to set
	 */
	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	/**
	 * @return the idenPaciente
	 */
	public String getIdenPaciente() {
		return idenPaciente;
	}

	/**
	 * @param idenPaciente the idenPaciente to set
	 */
	public void setIdenPaciente(String idenPaciente) {
		this.idenPaciente = idenPaciente;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the prioridad
	 */
	public int getPrioridad() {
		return prioridad;
	}

	/**
	 * @param prioridad the prioridad to set
	 */
	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}
}
