package com.servinte.axioma.orm;

// Generated Dec 1, 2010 2:25:26 PM by Hibernate Tools 3.2.4.GA

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * EmisionBonosDesc generated by hbm2java
 */
public class EmisionBonosDesc implements java.io.Serializable {

	private long codigo;
	private Convenios convenios;
	private Usuarios usuarios;
	private Servicios servicios;
	private Instituciones instituciones;
	private Programas programas;
	private String id;
	private long serialInicial;
	private long serialFinal;
	private Date fechaVigenciaInicial;
	private Date fechaVigenciaFinal;
	private BigDecimal valorDescuento;
	private BigDecimal porcentajeDescuentos;
	private Date fechaModifica;
	private String horaModifica;
	private Set bonosConvIngPacs = new HashSet(0);

	public EmisionBonosDesc() {
	}

	public EmisionBonosDesc(long codigo, Convenios convenios,
			Usuarios usuarios, Instituciones instituciones, String id,
			long serialInicial, long serialFinal, Date fechaVigenciaInicial,
			Date fechaVigenciaFinal, Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.convenios = convenios;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.id = id;
		this.serialInicial = serialInicial;
		this.serialFinal = serialFinal;
		this.fechaVigenciaInicial = fechaVigenciaInicial;
		this.fechaVigenciaFinal = fechaVigenciaFinal;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public EmisionBonosDesc(long codigo, Convenios convenios,
			Usuarios usuarios, Servicios servicios,
			Instituciones instituciones, Programas programas, String id,
			long serialInicial, long serialFinal, Date fechaVigenciaInicial,
			Date fechaVigenciaFinal, BigDecimal valorDescuento,
			BigDecimal porcentajeDescuentos, Date fechaModifica,
			String horaModifica, Set bonosConvIngPacs) {
		this.codigo = codigo;
		this.convenios = convenios;
		this.usuarios = usuarios;
		this.servicios = servicios;
		this.instituciones = instituciones;
		this.programas = programas;
		this.id = id;
		this.serialInicial = serialInicial;
		this.serialFinal = serialFinal;
		this.fechaVigenciaInicial = fechaVigenciaInicial;
		this.fechaVigenciaFinal = fechaVigenciaFinal;
		this.valorDescuento = valorDescuento;
		this.porcentajeDescuentos = porcentajeDescuentos;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.bonosConvIngPacs = bonosConvIngPacs;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Programas getProgramas() {
		return this.programas;
	}

	public void setProgramas(Programas programas) {
		this.programas = programas;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getSerialInicial() {
		return this.serialInicial;
	}

	public void setSerialInicial(long serialInicial) {
		this.serialInicial = serialInicial;
	}

	public long getSerialFinal() {
		return this.serialFinal;
	}

	public void setSerialFinal(long serialFinal) {
		this.serialFinal = serialFinal;
	}

	public Date getFechaVigenciaInicial() {
		return this.fechaVigenciaInicial;
	}

	public void setFechaVigenciaInicial(Date fechaVigenciaInicial) {
		this.fechaVigenciaInicial = fechaVigenciaInicial;
	}

	public Date getFechaVigenciaFinal() {
		return this.fechaVigenciaFinal;
	}

	public void setFechaVigenciaFinal(Date fechaVigenciaFinal) {
		this.fechaVigenciaFinal = fechaVigenciaFinal;
	}

	public BigDecimal getValorDescuento() {
		return this.valorDescuento;
	}

	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}

	public BigDecimal getPorcentajeDescuentos() {
		return this.porcentajeDescuentos;
	}

	public void setPorcentajeDescuentos(BigDecimal porcentajeDescuentos) {
		this.porcentajeDescuentos = porcentajeDescuentos;
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

	public Set getBonosConvIngPacs() {
		return this.bonosConvIngPacs;
	}

	public void setBonosConvIngPacs(Set bonosConvIngPacs) {
		this.bonosConvIngPacs = bonosConvIngPacs;
	}

}
