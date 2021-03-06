package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;

/**
 * LogControlAnticiposContrato generated by hbm2java
 */
public class LogControlAnticiposContrato implements java.io.Serializable {

	private long codigo;
	private Usuarios usuarios;
	private long controlAnticiposContrato;
	private BigDecimal valorAnticipoContConvAnt;
	private BigDecimal valorAnticipoContConv;
	private BigDecimal valorAntResPreContPac;
	private BigDecimal valorAntRecConvenio;
	private BigDecimal valorAntUtiFacPac;
	private Long numTotalPacientes;
	private BigDecimal valorMaxPac;
	private Long numPacAtendidos;
	private Long numPacXAtender;
	private Date fechaModifica;
	private String horaModifica;
	private Character eliminado;
	private char reqAntContPre;

	public LogControlAnticiposContrato() {
	}

	public LogControlAnticiposContrato(long codigo, Usuarios usuarios,
			long controlAnticiposContrato, BigDecimal valorAnticipoContConvAnt,
			BigDecimal valorAnticipoContConv, Date fechaModifica,
			String horaModifica, char reqAntContPre) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.controlAnticiposContrato = controlAnticiposContrato;
		this.valorAnticipoContConvAnt = valorAnticipoContConvAnt;
		this.valorAnticipoContConv = valorAnticipoContConv;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.reqAntContPre = reqAntContPre;
	}

	public LogControlAnticiposContrato(long codigo, Usuarios usuarios,
			long controlAnticiposContrato, BigDecimal valorAnticipoContConvAnt,
			BigDecimal valorAnticipoContConv, BigDecimal valorAntResPreContPac,
			BigDecimal valorAntRecConvenio, BigDecimal valorAntUtiFacPac,
			Long numTotalPacientes, BigDecimal valorMaxPac,
			Long numPacAtendidos, Long numPacXAtender, Date fechaModifica,
			String horaModifica, Character eliminado, char reqAntContPre) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.controlAnticiposContrato = controlAnticiposContrato;
		this.valorAnticipoContConvAnt = valorAnticipoContConvAnt;
		this.valorAnticipoContConv = valorAnticipoContConv;
		this.valorAntResPreContPac = valorAntResPreContPac;
		this.valorAntRecConvenio = valorAntRecConvenio;
		this.valorAntUtiFacPac = valorAntUtiFacPac;
		this.numTotalPacientes = numTotalPacientes;
		this.valorMaxPac = valorMaxPac;
		this.numPacAtendidos = numPacAtendidos;
		this.numPacXAtender = numPacXAtender;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.eliminado = eliminado;
		this.reqAntContPre = reqAntContPre;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public long getControlAnticiposContrato() {
		return this.controlAnticiposContrato;
	}

	public void setControlAnticiposContrato(long controlAnticiposContrato) {
		this.controlAnticiposContrato = controlAnticiposContrato;
	}

	public BigDecimal getValorAnticipoContConvAnt() {
		return this.valorAnticipoContConvAnt;
	}

	public void setValorAnticipoContConvAnt(BigDecimal valorAnticipoContConvAnt) {
		this.valorAnticipoContConvAnt = valorAnticipoContConvAnt;
	}

	public BigDecimal getValorAnticipoContConv() {
		return this.valorAnticipoContConv;
	}

	public void setValorAnticipoContConv(BigDecimal valorAnticipoContConv) {
		this.valorAnticipoContConv = valorAnticipoContConv;
	}

	public BigDecimal getValorAntResPreContPac() {
		return this.valorAntResPreContPac;
	}

	public void setValorAntResPreContPac(BigDecimal valorAntResPreContPac) {
		this.valorAntResPreContPac = valorAntResPreContPac;
	}

	public BigDecimal getValorAntRecConvenio() {
		return this.valorAntRecConvenio;
	}

	public void setValorAntRecConvenio(BigDecimal valorAntRecConvenio) {
		this.valorAntRecConvenio = valorAntRecConvenio;
	}

	public BigDecimal getValorAntUtiFacPac() {
		return this.valorAntUtiFacPac;
	}

	public void setValorAntUtiFacPac(BigDecimal valorAntUtiFacPac) {
		this.valorAntUtiFacPac = valorAntUtiFacPac;
	}

	public Long getNumTotalPacientes() {
		return this.numTotalPacientes;
	}

	public void setNumTotalPacientes(Long numTotalPacientes) {
		this.numTotalPacientes = numTotalPacientes;
	}

	public BigDecimal getValorMaxPac() {
		return this.valorMaxPac;
	}

	public void setValorMaxPac(BigDecimal valorMaxPac) {
		this.valorMaxPac = valorMaxPac;
	}

	public Long getNumPacAtendidos() {
		return this.numPacAtendidos;
	}

	public void setNumPacAtendidos(Long numPacAtendidos) {
		this.numPacAtendidos = numPacAtendidos;
	}

	public Long getNumPacXAtender() {
		return this.numPacXAtender;
	}

	public void setNumPacXAtender(Long numPacXAtender) {
		this.numPacXAtender = numPacXAtender;
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

	public Character getEliminado() {
		return this.eliminado;
	}

	public void setEliminado(Character eliminado) {
		this.eliminado = eliminado;
	}

	public char getReqAntContPre() {
		return this.reqAntContPre;
	}

	public void setReqAntContPre(char reqAntContPre) {
		this.reqAntContPre = reqAntContPre;
	}

}
