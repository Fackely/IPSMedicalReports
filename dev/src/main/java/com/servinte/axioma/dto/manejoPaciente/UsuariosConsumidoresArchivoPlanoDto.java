package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;

/**
 * @author ricruico
 *
 */
public class UsuariosConsumidoresArchivoPlanoDto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8484794605205150972L;
	
	private String nombreArchivoGenerado;
	private String rangoMeses;
	private String nombrePaciente;
	private String nombreTipoIdentificacion;
	private String numeroIdentificacion;
	private String nombreGrupoServicio;
	private double cantidadAutorizadaServ;
	private double valorAutorizadoServ;
	private double valorFacturadoServ;
	private long cantidadAutorizada;
	private long cantidadIngresos;
	private String autorizado;
	

	public UsuariosConsumidoresArchivoPlanoDto(){
	this.reset();
		}
	public void reset()
	{
				
	}
	
	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}
	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}
	/**
	 * @return the nombreTipoIdentificacion
	 */
	public String getNombreTipoIdentificacion() {
		return nombreTipoIdentificacion;
	}
	/**
	 * @param nombreTipoIdentificacion the nombreTipoIdentificacion to set
	 */
	public void setNombreTipoIdentificacion(String nombreTipoIdentificacion) {
		this.nombreTipoIdentificacion = nombreTipoIdentificacion;
	}
	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	/**
	 * @return the cantidadAutorizada
	 */
	public long getCantidadAutorizada() {
		return cantidadAutorizada;
	}
	/**
	 * @param cantidadAutorizada the cantidadAutorizada to set
	 */
	public void setCantidadAutorizada(long cantidadAutorizada) {
		this.cantidadAutorizada = cantidadAutorizada;
	}
	/**
	 * @return the cantidadIngresos
	 */
	public long getCantidadIngresos() {
		return cantidadIngresos;
	}
	/**
	 * @param cantidadIngresos the cantidadIngresos to set
	 */
	public void setCantidadIngresos(long cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}
	/**
	 * @return the NombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}
	/**
	 * @param NombrePaciente the NombrePaciente to set
	 */
	public void setNombrePaciente(String NombrePaciente) {
		this.nombrePaciente = NombrePaciente;
	}
	/**
	 * @return the rangoMeses
	 */
	public String getRangoMeses() {
		return rangoMeses;
	}
	/**
	 * @param rangoMeses the rangoMeses to set
	 */
	public void setRangoMeses(String rangoMeses) {
		this.rangoMeses = rangoMeses;
	}
	/**
	 * @return the nombreGrupoServicio
	 */
	public String getNombreGrupoServicio() {
		return nombreGrupoServicio;
	}
	/**
	 * @param nombreGrupoServicio the nombreGrupoServicio to set
	 */
	public void setNombreGrupoServicio(String nombreGrupoServicio) {
		this.nombreGrupoServicio = nombreGrupoServicio;
	}
	/**
	 * @return the cantidadAutorizadaServ
	 */
	public double getCantidadAutorizadaServ() {
		return cantidadAutorizadaServ;
	}
	/**
	 * @param cantidadAutorizadaServ the cantidadAutorizadaServ to set
	 */
	public void setCantidadAutorizadaServ(double cantidadAutorizadaServ) {
		this.cantidadAutorizadaServ = cantidadAutorizadaServ;
	}
	/**
	 * @return the valorAutorizadoServ
	 */
	public double getValorAutorizadoServ() {
		return valorAutorizadoServ;
	}
	/**
	 * @param valorAutorizadoServ the valorAutorizadoServ to set
	 */
	public void setValorAutorizadoServ(double valorAutorizadoServ) {
		this.valorAutorizadoServ = valorAutorizadoServ;
	}
	/**
	 * @return the valorFacturadoServ
	 */
	public double getValorFacturadoServ() {
		return valorFacturadoServ;
	}
	/**
	 * @param valorFacturadoServ the valorFacturadoServ to set
	 */
	public void setValorFacturadoServ(double valorFacturadoServ) {
		this.valorFacturadoServ = valorFacturadoServ;
	}
	/**
	 * @return the autorizado
	 */
	public String getAutorizado() {
		return autorizado;
	}
	/**
	 * @param autorizado the autorizado to set
	 */
	public void setAutorizado(String autorizado) {
		this.autorizado = autorizado;
	}
	




}