package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * 
 * @author wilson
 *
 */
public class DtoReingresoSalidaHospiDia implements Serializable
{
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String tipo;
	
	/**
	 * 
	 */
	private InfoDatosInt paciente;
	
	/**
	 * 
	 */
	private String cuenta;
	
	/**
	 * 
	 */
	private String ingreso;
	
	/**
	 * 
	 */
	private String loginUsuarioIngreso;
	
	/**
	 * 
	 */
	private String fechaIngreso;
	
	/**
	 * 
	 */
	private String horaIngreso;
	
	/**
	 * 
	 */
	private String observacionesIngreso;
	
	/**
	 * 
	 */
	private String loginUsuarioSalida;
	
	/**
	 * 
	 */
	private String fechaSalida;
	
	/**
	 * 
	 */
	private String horaSalida;
	
	/**
	 * 
	 */
	private String observacionesSalida;
	
	
	/**
	 * 
	 */
	private String numeroSolicitudSalida;
	
	/**
	 * 
	 */
	private InfoDatosInt servicioSalida;
	
	/**
	 * 
	 */
	private int institucion;

	/**
	 * Constructor  vacio
	 *
	 */
	public DtoReingresoSalidaHospiDia() 
	{
		this.codigo="";
		this.cuenta="";
		this.fechaIngreso="";
		this.fechaSalida="";
		this.horaIngreso="";
		this.horaSalida="";
		this.ingreso="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.loginUsuarioIngreso="";
		this.loginUsuarioSalida="";
		this.numeroSolicitudSalida="";
		this.observacionesIngreso="";
		this.observacionesSalida="";
		this.paciente= new InfoDatosInt();
		this.servicioSalida=new InfoDatosInt();
		this.tipo="";
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
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * @return the fechaSalida
	 */
	public String getFechaSalida() {
		return fechaSalida;
	}

	/**
	 * @param fechaSalida the fechaSalida to set
	 */
	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	/**
	 * @return the horaIngreso
	 */
	public String getHoraIngreso() {
		return horaIngreso;
	}

	/**
	 * @param horaIngreso the horaIngreso to set
	 */
	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	/**
	 * @return the horaSalida
	 */
	public String getHoraSalida() {
		return horaSalida;
	}

	/**
	 * @param horaSalida the horaSalida to set
	 */
	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the loginUsuarioIngreso
	 */
	public String getLoginUsuarioIngreso() {
		return loginUsuarioIngreso;
	}

	/**
	 * @param loginUsuarioIngreso the loginUsuarioIngreso to set
	 */
	public void setLoginUsuarioIngreso(String loginUsuarioIngreso) {
		this.loginUsuarioIngreso = loginUsuarioIngreso;
	}

	/**
	 * @return the loginUsuarioSalida
	 */
	public String getLoginUsuarioSalida() {
		return loginUsuarioSalida;
	}

	/**
	 * @param loginUsuarioSalida the loginUsuarioSalida to set
	 */
	public void setLoginUsuarioSalida(String loginUsuarioSalida) {
		this.loginUsuarioSalida = loginUsuarioSalida;
	}

	/**
	 * @return the numeroSolicitudSalida
	 */
	public String getNumeroSolicitudSalida() {
		return numeroSolicitudSalida;
	}

	/**
	 * @param numeroSolicitudSalida the numeroSolicitudSalida to set
	 */
	public void setNumeroSolicitudSalida(String numeroSolicitudSalida) {
		this.numeroSolicitudSalida = numeroSolicitudSalida;
	}

	/**
	 * @return the observacionesIngreso
	 */
	public String getObservacionesIngreso() {
		return observacionesIngreso;
	}

	/**
	 * @param observacionesIngreso the observacionesIngreso to set
	 */
	public void setObservacionesIngreso(String observacionesIngreso) {
		this.observacionesIngreso = observacionesIngreso;
	}

	/**
	 * @return the observacionesSalida
	 */
	public String getObservacionesSalida() {
		return observacionesSalida;
	}

	/**
	 * @param observacionesSalida the observacionesSalida to set
	 */
	public void setObservacionesSalida(String observacionesSalida) {
		this.observacionesSalida = observacionesSalida;
	}

	/**
	 * @return the paciente
	 */
	public InfoDatosInt getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(InfoDatosInt paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the servicioSalida
	 */
	public InfoDatosInt getServicioSalida() {
		return servicioSalida;
	}

	/**
	 * @param servicioSalida the servicioSalida to set
	 */
	public void setServicioSalida(InfoDatosInt servicioSalida) {
		this.servicioSalida = servicioSalida;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
}