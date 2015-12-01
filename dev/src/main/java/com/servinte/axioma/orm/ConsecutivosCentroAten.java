package com.servinte.axioma.orm;

// Generated Jan 18, 2011 11:29:29 AM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;

/**
 * ConsecutivosCentroAten generated by hbm2java
 */
public class ConsecutivosCentroAten implements java.io.Serializable {

	private long codigoPk;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;
	private String nombre;
	private Integer anioVigencia;
	private BigDecimal valor;
	private Date fechaModifica;
	private String horaModifica;
	private char activo;
	private char idAnual;

	public ConsecutivosCentroAten() {
	}

	public ConsecutivosCentroAten(long codigoPk, CentroAtencion centroAtencion,
			Usuarios usuarios, String nombre, BigDecimal valor,
			Date fechaModifica, String horaModifica, char activo, char idAnual) {
		this.codigoPk = codigoPk;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.nombre = nombre;
		this.valor = valor;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.activo = activo;
		this.idAnual = idAnual;
	}

	public ConsecutivosCentroAten(long codigoPk, CentroAtencion centroAtencion,
			Usuarios usuarios, String nombre, Integer anioVigencia,
			BigDecimal valor, Date fechaModifica, String horaModifica,
			char activo, char idAnual) {
		this.codigoPk = codigoPk;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.nombre = nombre;
		this.anioVigencia = anioVigencia;
		this.valor = valor;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.activo = activo;
		this.idAnual = idAnual;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getAnioVigencia() {
		return this.anioVigencia;
	}

	public void setAnioVigencia(Integer anioVigencia) {
		this.anioVigencia = anioVigencia;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return this.horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public char getActivo() {
		return this.activo;
	}

	public void setActivo(char activo) {
		this.activo = activo;
	}

	public char getIdAnual() {
		return this.idAnual;
	}

	public void setIdAnual(char idAnual) {
		this.idAnual = idAnual;
	}

}
