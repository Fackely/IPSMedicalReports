package com.servinte.axioma.dto.facturacion;

import java.util.Date;
import java.util.List;

/**
 * Dto con los datos necesarios para crear historico de cargos antes
 * de la Distribucion de Cuenta
 * @author hermorhu
 * @created 24-Nov-2012 
 */
public class InfoCreacionHistoricoCargosDto {

	private String tipoDistribucion;
	private String usuario;
	private Date fecha;
	private String hora;
	private int codigoIngreso;
	private String descripcion;
	private int codigoPaciente;
	private String claseDistribucion;
	private List<Integer> numeroSolicitudes;
	
	/**
	 * 
	 */
	public InfoCreacionHistoricoCargosDto() {
		super();
	}

	/**
	 * @return the tipoDistribucion
	 */
	public String getTipoDistribucion() {
		return tipoDistribucion;
	}

	/**
	 * @param tipoDistribucion the tipoDistribucion to set
	 */
	public void setTipoDistribucion(String tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the codigoIngreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return the claseDistribucion
	 */
	public String getClaseDistribucion() {
		return claseDistribucion;
	}

	/**
	 * @param claseDistribucion the claseDistribucion to set
	 */
	public void setClaseDistribucion(String claseDistribucion) {
		this.claseDistribucion = claseDistribucion;
	}

	/**
	 * @return the numeroSolicitudes
	 */
	public List<Integer> getNumeroSolicitudes() {
		return numeroSolicitudes;
	}

	/**
	 * @param numeroSolicitudes the numeroSolicitudes to set
	 */
	public void setNumeroSolicitudes(List<Integer> numeroSolicitudes) {
		this.numeroSolicitudes = numeroSolicitudes;
	}

 }
