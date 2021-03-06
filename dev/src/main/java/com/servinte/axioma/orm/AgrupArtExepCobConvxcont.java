package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;

/**
 * AgrupArtExepCobConvxcont generated by hbm2java
 */
public class AgrupArtExepCobConvxcont implements java.io.Serializable {

	private long codigo;
	private ExepParaCobXConvcont exepParaCobXConvcont;
	private Usuarios usuarios;
	private Instituciones instituciones;
	private Integer clase;
	private Integer grupo;
	private Integer subgrupo;
	private String naturaleza;
	private char presentarFacturaCompra;
	private char incluido;
	private char requiereAutorizacion;
	private Integer semanasMinCotizacion;
	private Long cantidadMaxCubXIngreso;
	private Date fechaModifica;
	private String horaModifica;

	public AgrupArtExepCobConvxcont() {
	}

	public AgrupArtExepCobConvxcont(long codigo,
			ExepParaCobXConvcont exepParaCobXConvcont, Usuarios usuarios,
			Instituciones instituciones, char presentarFacturaCompra,
			char incluido, char requiereAutorizacion, Date fechaModifica,
			String horaModifica) {
		this.codigo = codigo;
		this.exepParaCobXConvcont = exepParaCobXConvcont;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.presentarFacturaCompra = presentarFacturaCompra;
		this.incluido = incluido;
		this.requiereAutorizacion = requiereAutorizacion;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public AgrupArtExepCobConvxcont(long codigo,
			ExepParaCobXConvcont exepParaCobXConvcont, Usuarios usuarios,
			Instituciones instituciones, Integer clase, Integer grupo,
			Integer subgrupo, String naturaleza, char presentarFacturaCompra,
			char incluido, char requiereAutorizacion,
			Integer semanasMinCotizacion, Long cantidadMaxCubXIngreso,
			Date fechaModifica, String horaModifica) {
		this.codigo = codigo;
		this.exepParaCobXConvcont = exepParaCobXConvcont;
		this.usuarios = usuarios;
		this.instituciones = instituciones;
		this.clase = clase;
		this.grupo = grupo;
		this.subgrupo = subgrupo;
		this.naturaleza = naturaleza;
		this.presentarFacturaCompra = presentarFacturaCompra;
		this.incluido = incluido;
		this.requiereAutorizacion = requiereAutorizacion;
		this.semanasMinCotizacion = semanasMinCotizacion;
		this.cantidadMaxCubXIngreso = cantidadMaxCubXIngreso;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public ExepParaCobXConvcont getExepParaCobXConvcont() {
		return this.exepParaCobXConvcont;
	}

	public void setExepParaCobXConvcont(
			ExepParaCobXConvcont exepParaCobXConvcont) {
		this.exepParaCobXConvcont = exepParaCobXConvcont;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Integer getClase() {
		return this.clase;
	}

	public void setClase(Integer clase) {
		this.clase = clase;
	}

	public Integer getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
	}

	public Integer getSubgrupo() {
		return this.subgrupo;
	}

	public void setSubgrupo(Integer subgrupo) {
		this.subgrupo = subgrupo;
	}

	public String getNaturaleza() {
		return this.naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public char getPresentarFacturaCompra() {
		return this.presentarFacturaCompra;
	}

	public void setPresentarFacturaCompra(char presentarFacturaCompra) {
		this.presentarFacturaCompra = presentarFacturaCompra;
	}

	public char getIncluido() {
		return this.incluido;
	}

	public void setIncluido(char incluido) {
		this.incluido = incluido;
	}

	public char getRequiereAutorizacion() {
		return this.requiereAutorizacion;
	}

	public void setRequiereAutorizacion(char requiereAutorizacion) {
		this.requiereAutorizacion = requiereAutorizacion;
	}

	public Integer getSemanasMinCotizacion() {
		return this.semanasMinCotizacion;
	}

	public void setSemanasMinCotizacion(Integer semanasMinCotizacion) {
		this.semanasMinCotizacion = semanasMinCotizacion;
	}

	public Long getCantidadMaxCubXIngreso() {
		return this.cantidadMaxCubXIngreso;
	}

	public void setCantidadMaxCubXIngreso(Long cantidadMaxCubXIngreso) {
		this.cantidadMaxCubXIngreso = cantidadMaxCubXIngreso;
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

}
