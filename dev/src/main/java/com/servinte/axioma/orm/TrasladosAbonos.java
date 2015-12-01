package com.servinte.axioma.orm;

// Generated Jul 22, 2010 12:52:36 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TrasladosAbonos generated by hbm2java
 */
public class TrasladosAbonos implements java.io.Serializable {

	private long codigoPk;
	private CentroAtencion centroAtencion;
	private Usuarios usuarios;
	private Date fecha;
	private String hora;
	private char contabilizado;
	private String tipoComprobante;
	private int numeroComprobante;
	private BigDecimal consecutivo;
	private Set destinoTrasladosAbonosPacs = new HashSet(0);
	private Set origenTrasladosAbonosPacs = new HashSet(0);

	public TrasladosAbonos() {
	}

	public TrasladosAbonos(long codigoPk, Usuarios usuarios, Date fecha,
			String hora, char contabilizado, int numeroComprobante,
			BigDecimal consecutivo) {
		this.codigoPk = codigoPk;
		this.usuarios = usuarios;
		this.fecha = fecha;
		this.hora = hora;
		this.contabilizado = contabilizado;
		this.numeroComprobante = numeroComprobante;
		this.consecutivo = consecutivo;
	}

	public TrasladosAbonos(long codigoPk, CentroAtencion centroAtencion,
			Usuarios usuarios, Date fecha, String hora, char contabilizado,
			String tipoComprobante, int numeroComprobante,
			BigDecimal consecutivo, Set destinoTrasladosAbonosPacs,
			Set origenTrasladosAbonosPacs) {
		this.codigoPk = codigoPk;
		this.centroAtencion = centroAtencion;
		this.usuarios = usuarios;
		this.fecha = fecha;
		this.hora = hora;
		this.contabilizado = contabilizado;
		this.tipoComprobante = tipoComprobante;
		this.numeroComprobante = numeroComprobante;
		this.consecutivo = consecutivo;
		this.destinoTrasladosAbonosPacs = destinoTrasladosAbonosPacs;
		this.origenTrasladosAbonosPacs = origenTrasladosAbonosPacs;
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

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public char getContabilizado() {
		return this.contabilizado;
	}

	public void setContabilizado(char contabilizado) {
		this.contabilizado = contabilizado;
	}

	public String getTipoComprobante() {
		return this.tipoComprobante;
	}

	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public int getNumeroComprobante() {
		return this.numeroComprobante;
	}

	public void setNumeroComprobante(int numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}

	public BigDecimal getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Set getDestinoTrasladosAbonosPacs() {
		return this.destinoTrasladosAbonosPacs;
	}

	public void setDestinoTrasladosAbonosPacs(Set destinoTrasladosAbonosPacs) {
		this.destinoTrasladosAbonosPacs = destinoTrasladosAbonosPacs;
	}

	public Set getOrigenTrasladosAbonosPacs() {
		return this.origenTrasladosAbonosPacs;
	}

	public void setOrigenTrasladosAbonosPacs(Set origenTrasladosAbonosPacs) {
		this.origenTrasladosAbonosPacs = origenTrasladosAbonosPacs;
	}

}
