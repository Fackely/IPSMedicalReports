package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;

/**
 * PaquetizacionDetFactura generated by hbm2java
 */
public class PaquetizacionDetFactura implements java.io.Serializable {

	private long codigo;
	private Servicios serviciosByServicioCx;
	private Medicos medicosByCodigoMedico;
	private DetFacturaSolicitud detFacturaSolicitud;
	private Especialidades especialidades;
	private Servicios serviciosByServicio;
	private Medicos medicosByMedicoAsocio;
	private EsquemasTarifarios esquemasTarifarios;
	private Pooles pooles;
	private Integer articulo;
	private double ajusteCreditoMedico;
	private double ajusteDebitoMedico;
	private double valorCargo;
	private double valorIva;
	private double valorRecargo;
	private double valorTotal;
	private double valorDifConsumoValor;
	private double ajustesCredito;
	private double ajustesDebito;
	private Double porcentajeMedico;
	private Double porcentajePool;
	private Double valorAsocio;
	private Integer tipoAsocio;
	private Double valorMedico;
	private Double valorPool;
	private Integer soliPago;
	private int cantidadCargo;
	private int tipoCargo;
	private int tipoSolicitud;
	private Integer solicitud;
	private BigDecimal ajustesDifConsumoDebito;
	private BigDecimal ajustesDifConsumoCredito;

	public PaquetizacionDetFactura() {
	}

	public PaquetizacionDetFactura(long codigo,
			DetFacturaSolicitud detFacturaSolicitud,
			double ajusteCreditoMedico, double ajusteDebitoMedico,
			double valorCargo, double valorIva, double valorRecargo,
			double valorTotal, double valorDifConsumoValor,
			double ajustesCredito, double ajustesDebito, int cantidadCargo,
			int tipoCargo, int tipoSolicitud) {
		this.codigo = codigo;
		this.detFacturaSolicitud = detFacturaSolicitud;
		this.ajusteCreditoMedico = ajusteCreditoMedico;
		this.ajusteDebitoMedico = ajusteDebitoMedico;
		this.valorCargo = valorCargo;
		this.valorIva = valorIva;
		this.valorRecargo = valorRecargo;
		this.valorTotal = valorTotal;
		this.valorDifConsumoValor = valorDifConsumoValor;
		this.ajustesCredito = ajustesCredito;
		this.ajustesDebito = ajustesDebito;
		this.cantidadCargo = cantidadCargo;
		this.tipoCargo = tipoCargo;
		this.tipoSolicitud = tipoSolicitud;
	}

	public PaquetizacionDetFactura(long codigo,
			Servicios serviciosByServicioCx, Medicos medicosByCodigoMedico,
			DetFacturaSolicitud detFacturaSolicitud,
			Especialidades especialidades, Servicios serviciosByServicio,
			Medicos medicosByMedicoAsocio,
			EsquemasTarifarios esquemasTarifarios, Pooles pooles,
			Integer articulo, double ajusteCreditoMedico,
			double ajusteDebitoMedico, double valorCargo, double valorIva,
			double valorRecargo, double valorTotal,
			double valorDifConsumoValor, double ajustesCredito,
			double ajustesDebito, Double porcentajeMedico,
			Double porcentajePool, Double valorAsocio, Integer tipoAsocio,
			Double valorMedico, Double valorPool, Integer soliPago,
			int cantidadCargo, int tipoCargo, int tipoSolicitud,
			Integer solicitud, BigDecimal ajustesDifConsumoDebito,
			BigDecimal ajustesDifConsumoCredito) {
		this.codigo = codigo;
		this.serviciosByServicioCx = serviciosByServicioCx;
		this.medicosByCodigoMedico = medicosByCodigoMedico;
		this.detFacturaSolicitud = detFacturaSolicitud;
		this.especialidades = especialidades;
		this.serviciosByServicio = serviciosByServicio;
		this.medicosByMedicoAsocio = medicosByMedicoAsocio;
		this.esquemasTarifarios = esquemasTarifarios;
		this.pooles = pooles;
		this.articulo = articulo;
		this.ajusteCreditoMedico = ajusteCreditoMedico;
		this.ajusteDebitoMedico = ajusteDebitoMedico;
		this.valorCargo = valorCargo;
		this.valorIva = valorIva;
		this.valorRecargo = valorRecargo;
		this.valorTotal = valorTotal;
		this.valorDifConsumoValor = valorDifConsumoValor;
		this.ajustesCredito = ajustesCredito;
		this.ajustesDebito = ajustesDebito;
		this.porcentajeMedico = porcentajeMedico;
		this.porcentajePool = porcentajePool;
		this.valorAsocio = valorAsocio;
		this.tipoAsocio = tipoAsocio;
		this.valorMedico = valorMedico;
		this.valorPool = valorPool;
		this.soliPago = soliPago;
		this.cantidadCargo = cantidadCargo;
		this.tipoCargo = tipoCargo;
		this.tipoSolicitud = tipoSolicitud;
		this.solicitud = solicitud;
		this.ajustesDifConsumoDebito = ajustesDifConsumoDebito;
		this.ajustesDifConsumoCredito = ajustesDifConsumoCredito;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Servicios getServiciosByServicioCx() {
		return this.serviciosByServicioCx;
	}

	public void setServiciosByServicioCx(Servicios serviciosByServicioCx) {
		this.serviciosByServicioCx = serviciosByServicioCx;
	}

	public Medicos getMedicosByCodigoMedico() {
		return this.medicosByCodigoMedico;
	}

	public void setMedicosByCodigoMedico(Medicos medicosByCodigoMedico) {
		this.medicosByCodigoMedico = medicosByCodigoMedico;
	}

	public DetFacturaSolicitud getDetFacturaSolicitud() {
		return this.detFacturaSolicitud;
	}

	public void setDetFacturaSolicitud(DetFacturaSolicitud detFacturaSolicitud) {
		this.detFacturaSolicitud = detFacturaSolicitud;
	}

	public Especialidades getEspecialidades() {
		return this.especialidades;
	}

	public void setEspecialidades(Especialidades especialidades) {
		this.especialidades = especialidades;
	}

	public Servicios getServiciosByServicio() {
		return this.serviciosByServicio;
	}

	public void setServiciosByServicio(Servicios serviciosByServicio) {
		this.serviciosByServicio = serviciosByServicio;
	}

	public Medicos getMedicosByMedicoAsocio() {
		return this.medicosByMedicoAsocio;
	}

	public void setMedicosByMedicoAsocio(Medicos medicosByMedicoAsocio) {
		this.medicosByMedicoAsocio = medicosByMedicoAsocio;
	}

	public EsquemasTarifarios getEsquemasTarifarios() {
		return this.esquemasTarifarios;
	}

	public void setEsquemasTarifarios(EsquemasTarifarios esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public Pooles getPooles() {
		return this.pooles;
	}

	public void setPooles(Pooles pooles) {
		this.pooles = pooles;
	}

	public Integer getArticulo() {
		return this.articulo;
	}

	public void setArticulo(Integer articulo) {
		this.articulo = articulo;
	}

	public double getAjusteCreditoMedico() {
		return this.ajusteCreditoMedico;
	}

	public void setAjusteCreditoMedico(double ajusteCreditoMedico) {
		this.ajusteCreditoMedico = ajusteCreditoMedico;
	}

	public double getAjusteDebitoMedico() {
		return this.ajusteDebitoMedico;
	}

	public void setAjusteDebitoMedico(double ajusteDebitoMedico) {
		this.ajusteDebitoMedico = ajusteDebitoMedico;
	}

	public double getValorCargo() {
		return this.valorCargo;
	}

	public void setValorCargo(double valorCargo) {
		this.valorCargo = valorCargo;
	}

	public double getValorIva() {
		return this.valorIva;
	}

	public void setValorIva(double valorIva) {
		this.valorIva = valorIva;
	}

	public double getValorRecargo() {
		return this.valorRecargo;
	}

	public void setValorRecargo(double valorRecargo) {
		this.valorRecargo = valorRecargo;
	}

	public double getValorTotal() {
		return this.valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorDifConsumoValor() {
		return this.valorDifConsumoValor;
	}

	public void setValorDifConsumoValor(double valorDifConsumoValor) {
		this.valorDifConsumoValor = valorDifConsumoValor;
	}

	public double getAjustesCredito() {
		return this.ajustesCredito;
	}

	public void setAjustesCredito(double ajustesCredito) {
		this.ajustesCredito = ajustesCredito;
	}

	public double getAjustesDebito() {
		return this.ajustesDebito;
	}

	public void setAjustesDebito(double ajustesDebito) {
		this.ajustesDebito = ajustesDebito;
	}

	public Double getPorcentajeMedico() {
		return this.porcentajeMedico;
	}

	public void setPorcentajeMedico(Double porcentajeMedico) {
		this.porcentajeMedico = porcentajeMedico;
	}

	public Double getPorcentajePool() {
		return this.porcentajePool;
	}

	public void setPorcentajePool(Double porcentajePool) {
		this.porcentajePool = porcentajePool;
	}

	public Double getValorAsocio() {
		return this.valorAsocio;
	}

	public void setValorAsocio(Double valorAsocio) {
		this.valorAsocio = valorAsocio;
	}

	public Integer getTipoAsocio() {
		return this.tipoAsocio;
	}

	public void setTipoAsocio(Integer tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	public Double getValorMedico() {
		return this.valorMedico;
	}

	public void setValorMedico(Double valorMedico) {
		this.valorMedico = valorMedico;
	}

	public Double getValorPool() {
		return this.valorPool;
	}

	public void setValorPool(Double valorPool) {
		this.valorPool = valorPool;
	}

	public Integer getSoliPago() {
		return this.soliPago;
	}

	public void setSoliPago(Integer soliPago) {
		this.soliPago = soliPago;
	}

	public int getCantidadCargo() {
		return this.cantidadCargo;
	}

	public void setCantidadCargo(int cantidadCargo) {
		this.cantidadCargo = cantidadCargo;
	}

	public int getTipoCargo() {
		return this.tipoCargo;
	}

	public void setTipoCargo(int tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	public int getTipoSolicitud() {
		return this.tipoSolicitud;
	}

	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public Integer getSolicitud() {
		return this.solicitud;
	}

	public void setSolicitud(Integer solicitud) {
		this.solicitud = solicitud;
	}

	public BigDecimal getAjustesDifConsumoDebito() {
		return this.ajustesDifConsumoDebito;
	}

	public void setAjustesDifConsumoDebito(BigDecimal ajustesDifConsumoDebito) {
		this.ajustesDifConsumoDebito = ajustesDifConsumoDebito;
	}

	public BigDecimal getAjustesDifConsumoCredito() {
		return this.ajustesDifConsumoCredito;
	}

	public void setAjustesDifConsumoCredito(BigDecimal ajustesDifConsumoCredito) {
		this.ajustesDifConsumoCredito = ajustesDifConsumoCredito;
	}

}
