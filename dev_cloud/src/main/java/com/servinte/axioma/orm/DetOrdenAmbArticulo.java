package com.servinte.axioma.orm;

// Generated 23/06/2012 05:39:04 PM by Hibernate Tools 3.4.0.CR1

/**
 * DetOrdenAmbArticulo generated by hbm2java
 */
public class DetOrdenAmbArticulo implements java.io.Serializable {

	private DetOrdenAmbArticuloId id;
	private ViasAdministracion viasAdministracion;
	private TiposFrecuencia tiposFrecuencia;
	private OrdenesAmbulatorias ordenesAmbulatorias;
	private Contratos contratos;
	private UnidosisXArticulo unidosisXArticulo;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private Articulo articulo;
	private String dosis;
	private String observaciones;
	private Long frecuencia;
	private Long cantidad;
	private Boolean medicamento;
	private Integer duracionTratamiento;
	private String cubierto;

	public DetOrdenAmbArticulo() {
	}

	public DetOrdenAmbArticulo(DetOrdenAmbArticuloId id,
			OrdenesAmbulatorias ordenesAmbulatorias, Articulo articulo) {
		this.id = id;
		this.ordenesAmbulatorias = ordenesAmbulatorias;
		this.articulo = articulo;
	}

	public DetOrdenAmbArticulo(DetOrdenAmbArticuloId id,
			ViasAdministracion viasAdministracion,
			TiposFrecuencia tiposFrecuencia,
			OrdenesAmbulatorias ordenesAmbulatorias, Contratos contratos,
			UnidosisXArticulo unidosisXArticulo,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub,
			Articulo articulo, String dosis, String observaciones,
			Long frecuencia, Long cantidad, Boolean medicamento,
			Integer duracionTratamiento, String cubierto) {
		this.id = id;
		this.viasAdministracion = viasAdministracion;
		this.tiposFrecuencia = tiposFrecuencia;
		this.ordenesAmbulatorias = ordenesAmbulatorias;
		this.contratos = contratos;
		this.unidosisXArticulo = unidosisXArticulo;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.articulo = articulo;
		this.dosis = dosis;
		this.observaciones = observaciones;
		this.frecuencia = frecuencia;
		this.cantidad = cantidad;
		this.medicamento = medicamento;
		this.duracionTratamiento = duracionTratamiento;
		this.cubierto = cubierto;
	}

	public DetOrdenAmbArticuloId getId() {
		return this.id;
	}

	public void setId(DetOrdenAmbArticuloId id) {
		this.id = id;
	}

	public ViasAdministracion getViasAdministracion() {
		return this.viasAdministracion;
	}

	public void setViasAdministracion(ViasAdministracion viasAdministracion) {
		this.viasAdministracion = viasAdministracion;
	}

	public TiposFrecuencia getTiposFrecuencia() {
		return this.tiposFrecuencia;
	}

	public void setTiposFrecuencia(TiposFrecuencia tiposFrecuencia) {
		this.tiposFrecuencia = tiposFrecuencia;
	}

	public OrdenesAmbulatorias getOrdenesAmbulatorias() {
		return this.ordenesAmbulatorias;
	}

	public void setOrdenesAmbulatorias(OrdenesAmbulatorias ordenesAmbulatorias) {
		this.ordenesAmbulatorias = ordenesAmbulatorias;
	}

	public Contratos getContratos() {
		return this.contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public UnidosisXArticulo getUnidosisXArticulo() {
		return this.unidosisXArticulo;
	}

	public void setUnidosisXArticulo(UnidosisXArticulo unidosisXArticulo) {
		this.unidosisXArticulo = unidosisXArticulo;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
	}

	public Articulo getArticulo() {
		return this.articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public String getDosis() {
		return this.dosis;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Long getFrecuencia() {
		return this.frecuencia;
	}

	public void setFrecuencia(Long frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Long getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public Boolean getMedicamento() {
		return this.medicamento;
	}

	public void setMedicamento(Boolean medicamento) {
		this.medicamento = medicamento;
	}

	public Integer getDuracionTratamiento() {
		return this.duracionTratamiento;
	}

	public void setDuracionTratamiento(Integer duracionTratamiento) {
		this.duracionTratamiento = duracionTratamiento;
	}

	public String getCubierto() {
		return this.cubierto;
	}

	public void setCubierto(String cubierto) {
		this.cubierto = cubierto;
	}

}
