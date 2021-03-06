package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TopesFacturacion generated by hbm2java
 */
public class TopesFacturacion implements java.io.Serializable {

	private int codigo;
	private TiposRegimen tiposRegimen;
	private EstratosSociales estratosSociales;
	private TiposMonto tiposMonto;
	private Instituciones instituciones;
	private Double topeEvento;
	private Double topeAnioCalendario;
	private Date vigenciaInicial;
	private Set facturases = new HashSet(0);

	public TopesFacturacion() {
	}

	public TopesFacturacion(int codigo, TiposRegimen tiposRegimen,
			EstratosSociales estratosSociales, TiposMonto tiposMonto,
			Date vigenciaInicial) {
		this.codigo = codigo;
		this.tiposRegimen = tiposRegimen;
		this.estratosSociales = estratosSociales;
		this.tiposMonto = tiposMonto;
		this.vigenciaInicial = vigenciaInicial;
	}

	public TopesFacturacion(int codigo, TiposRegimen tiposRegimen,
			EstratosSociales estratosSociales, TiposMonto tiposMonto,
			Instituciones instituciones, Double topeEvento,
			Double topeAnioCalendario, Date vigenciaInicial, Set facturases) {
		this.codigo = codigo;
		this.tiposRegimen = tiposRegimen;
		this.estratosSociales = estratosSociales;
		this.tiposMonto = tiposMonto;
		this.instituciones = instituciones;
		this.topeEvento = topeEvento;
		this.topeAnioCalendario = topeAnioCalendario;
		this.vigenciaInicial = vigenciaInicial;
		this.facturases = facturases;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public TiposRegimen getTiposRegimen() {
		return this.tiposRegimen;
	}

	public void setTiposRegimen(TiposRegimen tiposRegimen) {
		this.tiposRegimen = tiposRegimen;
	}

	public EstratosSociales getEstratosSociales() {
		return this.estratosSociales;
	}

	public void setEstratosSociales(EstratosSociales estratosSociales) {
		this.estratosSociales = estratosSociales;
	}

	public TiposMonto getTiposMonto() {
		return this.tiposMonto;
	}

	public void setTiposMonto(TiposMonto tiposMonto) {
		this.tiposMonto = tiposMonto;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Double getTopeEvento() {
		return this.topeEvento;
	}

	public void setTopeEvento(Double topeEvento) {
		this.topeEvento = topeEvento;
	}

	public Double getTopeAnioCalendario() {
		return this.topeAnioCalendario;
	}

	public void setTopeAnioCalendario(Double topeAnioCalendario) {
		this.topeAnioCalendario = topeAnioCalendario;
	}

	public Date getVigenciaInicial() {
		return this.vigenciaInicial;
	}

	public void setVigenciaInicial(Date vigenciaInicial) {
		this.vigenciaInicial = vigenciaInicial;
	}

	public Set getFacturases() {
		return this.facturases;
	}

	public void setFacturases(Set facturases) {
		this.facturases = facturases;
	}

}
