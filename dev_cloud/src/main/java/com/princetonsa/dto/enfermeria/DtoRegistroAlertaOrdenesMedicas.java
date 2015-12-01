package com.princetonsa.dto.enfermeria;

import java.sql.Date;



public class DtoRegistroAlertaOrdenesMedicas {

	private long codigo; 
	private int tipoSeccionOrden; 
	private String nombreSeccionOrden;
	private long cuenta;
	private boolean activo;
	private String medicoOrdena;
	private Date fechaOrden;
	private String horaOrden;
	private long registroEnfermeria;
	private String usuarioRegistra;
	private Date fechaRegistro;
	private String horaRegistro;
	
	
	
	public DtoRegistroAlertaOrdenesMedicas() {
		
	}

	public DtoRegistroAlertaOrdenesMedicas(long codigo, int tipoSeccionOrden,
			String nombreSeccionOrden, long cuenta, boolean activo,
			String medicoOrdena, Date fechaOrden, String horaOrden,
			long registroEnfermeria, String usuarioRegistra,
			Date fechaRegistro, String horaRegistro) {
		super();
		this.codigo = codigo;
		this.tipoSeccionOrden = tipoSeccionOrden;
		this.nombreSeccionOrden = nombreSeccionOrden;
		this.cuenta = cuenta;
		this.activo = activo;
		this.medicoOrdena = medicoOrdena;
		this.fechaOrden = fechaOrden;
		this.horaOrden = horaOrden;
		this.registroEnfermeria = registroEnfermeria;
		this.usuarioRegistra = usuarioRegistra;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
	}

	public long getCodigo() {
		return codigo;
	}


	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}


	public int getTipoSeccionOrden() {
		return tipoSeccionOrden;
	}


	public void setTipoSeccionOrden(int tipoSeccionOrden) {
		this.tipoSeccionOrden = tipoSeccionOrden;
	}


	/**
	 * @param nombreSeccionOrden the nombreSeccionOrden to set
	 */
	public void setNombreSeccionOrden(String nombreSeccionOrden) {
		this.nombreSeccionOrden = nombreSeccionOrden;
	}


	/**
	 * @return the nombreSeccionOrden
	 */
	public String getNombreSeccionOrden() {
		return nombreSeccionOrden;
	}


	public long getCuenta() {
		return cuenta;
	}


	public void setCuenta(long cuenta) {
		this.cuenta = cuenta;
	}


	public boolean getActivo() {
		return activo;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
	}


	public String getMedicoOrdena() {
		return medicoOrdena;
	}


	public void setMedicoOrdena(String medicoOrdena) {
		this.medicoOrdena = medicoOrdena;
	}


	public Date getFechaOrden() {
		return fechaOrden;
	}


	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}


	public String getHoraOrden() {
		return horaOrden;
	}


	public void setHoraOrden(String horaOrden) {
		this.horaOrden = horaOrden;
	}


	public long getRegistroEnfermeria() {
		return registroEnfermeria;
	}


	public void setRegistroEnfermeria(long registroEnfermeria) {
		this.registroEnfermeria = registroEnfermeria;
	}


	public String getUsuarioRegistra() {
		return usuarioRegistra;
	}


	public void setUsuarioRegistra(String usuarioRegistra) {
		this.usuarioRegistra = usuarioRegistra;
	}


	public Date getFechaRegistro() {
		return fechaRegistro;
	}


	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	public String getHoraRegistro() {
		return horaRegistro;
	}


	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}
	
	
}
