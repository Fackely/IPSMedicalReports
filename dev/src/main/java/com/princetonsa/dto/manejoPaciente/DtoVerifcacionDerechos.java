/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dto.manejoPaciente
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.InfoDatosString;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoVerifcacionDerechos implements Serializable{

	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * SubCuenta asociada
	 */
	private String subCuenta;
	
	/**
	 * Id del ingreso
	 */
	private String idIngreso;
	
	/**
	 * Convenio
	 */
	private int convenio;
	
	/**
	 * estado
	 */
	private InfoDatosString estado;
	
	/**
	 * tipo
	 */
	private InfoDatosString tipo;
	
	/**
	 * Numero
	 */
	private String numero;
	
	/**
	 * Persona solicita
	 */
	private String personaSolicita;
	
	/**
	 * fecha solicitud
	 */
	private String fechaSolicitud;
	
	/**
	 * hora solicitud
	 */
	private String horaSolicitud;
	
	/**
	 * persona contactada
	 */
	private String personaContactada;
	
	/**
	 * fecha de verificacion
	 */
	private String fechaVerificacion;
	
	/**
	 * Hora de verificacione
	 */
	private String horaVerificacion;
	
	/**
	 * Porcentaje de cobertura
	 */
	private String porcentajeCobertura;
	
	/**
	 * Couta verificacion
	 */
	private String cuotaVerificacion;
	
	/**
	 * observaciones
	 */
	private String observaciones;
	
	/**
	 * login del usuario
	 */
	private String loginUsuario;
	
	/**
	 * Verificar si se debe eliminar
	 */
	private boolean eliminar;
	
	/**
	 * Constructor
	 *
	 */
	public DtoVerifcacionDerechos() 
	{
		this.subCuenta = "";
		this.estado = new InfoDatosString("","");
		this.tipo = new InfoDatosString("","");
		this.numero = "";
		this.personaSolicita = "";
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.personaContactada = "";
		this.fechaVerificacion = "";
		this.horaVerificacion = "";
		this.porcentajeCobertura = "";
		this.cuotaVerificacion = "";
		this.observaciones = "";
		this.loginUsuario = "";
		this.idIngreso = "";
		this.convenio = 0;
		this.eliminar = false;
	}

	/**
	 * @return the cuotaVerificacion
	 */
	public String getCuotaVerificacion() {
		return cuotaVerificacion;
	}

	/**
	 * @param cuotaVerificacion the cuotaVerificacion to set
	 */
	public void setCuotaVerificacion(String cuotaVerificacion) {
		this.cuotaVerificacion = cuotaVerificacion;
	}

	/**
	 * @return the estado
	 */
	public String getCodigoEstado() {
		return estado.getCodigo();
	}
	
	/**
	 * @return the estado
	 */
	public String getDescripcionEstado() {
		return estado.getNombre();
	}

	/**
	 * @param estado the estado to set
	 */
	public void setCodigoEstado(String estado) {
		this.estado.setCodigo(estado);
	}
	
	/**
	 * @param estado the estado to set
	 */
	public void setDescripcionEstado(String estado) {
		this.estado.setNombre(estado);
	}

	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return the fechaVerificacion
	 */
	public String getFechaVerificacion() {
		return fechaVerificacion;
	}

	/**
	 * @param fechaVerificacion the fechaVerificacion to set
	 */
	public void setFechaVerificacion(String fechaVerificacion) {
		this.fechaVerificacion = fechaVerificacion;
	}

	/**
	 * @return the horaSolicitud
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * @param horaSolicitud the horaSolicitud to set
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return the horaVerificacion
	 */
	public String getHoraVerificacion() {
		return horaVerificacion;
	}

	/**
	 * @param horaVerificacion the horaVerificacion to set
	 */
	public void setHoraVerificacion(String horaVerificacion) {
		this.horaVerificacion = horaVerificacion;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the personaContactada
	 */
	public String getPersonaContactada() {
		return personaContactada;
	}

	/**
	 * @param personaContactada the personaContactada to set
	 */
	public void setPersonaContactada(String personaContactada) {
		this.personaContactada = personaContactada;
	}

	/**
	 * @return the personaSolicita
	 */
	public String getPersonaSolicita() {
		return personaSolicita;
	}

	/**
	 * @param personaSolicita the personaSolicita to set
	 */
	public void setPersonaSolicita(String personaSolicita) {
		this.personaSolicita = personaSolicita;
	}

	/**
	 * @return the porcentajeCobertura
	 */
	public String getPorcentajeCobertura() {
		return porcentajeCobertura;
	}

	/**
	 * @param porcentajeCobertura the porcentajeCobertura to set
	 */
	public void setPorcentajeCobertura(String porcentajeCobertura) {
		this.porcentajeCobertura = porcentajeCobertura;
	}

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the tipo
	 */
	public String getCodigoTipo() {
		return tipo.getCodigo();
	}
	
	/**
	 * @return the tipo
	 */
	public String getDescripcionTipo() {
		return tipo.getNombre();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setCodigoTipo(String tipo) {
		this.tipo.setCodigo(tipo);
	}
	
	/**
	 * @param tipo the tipo to set
	 */
	public void setDescripcionTipo(String tipo) {
		this.tipo.setNombre(tipo);
	}

	/**
	 * @return the estado
	 */
	public InfoDatosString getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(InfoDatosString estado) {
		this.estado = estado;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return the tipo
	 */
	public InfoDatosString getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(InfoDatosString tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
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
	 * @return the idIngreso
	 */
	public String getIdIngreso() {
		return idIngreso;
	}

	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * @return the eliminar
	 */
	public boolean isEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(boolean eliminar) {
		this.eliminar = eliminar;
	}

}
