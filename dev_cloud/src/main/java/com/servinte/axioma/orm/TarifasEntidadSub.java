package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TarifasEntidadSub generated by hbm2java
 */
public class TarifasEntidadSub implements java.io.Serializable {

	private long codigoDetalleCargo;
	private EntidadesSubcontratadas entidadesSubcontratadas;
	private Articulo articuloByArtPrincipal;
	private Solicitudes solicitudes;
	private EstadosSolFact estadosSolFact;
	private Usuarios usuarios;
	private Pedido pedido;
	private ContratosEntidadesSub contratosEntidadesSub;
	private EsquemasTarifarios esquemasTarifarios;
	private Articulo articuloByArticulo;
	private AutorizacionesEntidadesSub autorizacionesEntidadesSub;
	private Date fecha;
	private String hora;
	private BigDecimal valorUnitario;
	private String vieneDespacho;
	private Date fechaModifica;
	private String horaModifica;
	private String observaciones;
	private Set erroresTarifasEntSubs = new HashSet(0);

	public TarifasEntidadSub() {
	}

	public TarifasEntidadSub(long codigoDetalleCargo,
			EstadosSolFact estadosSolFact, Usuarios usuarios, Date fecha,
			String hora, String vieneDespacho, Date fechaModifica,
			String horaModifica) {
		this.codigoDetalleCargo = codigoDetalleCargo;
		this.estadosSolFact = estadosSolFact;
		this.usuarios = usuarios;
		this.fecha = fecha;
		this.hora = hora;
		this.vieneDespacho = vieneDespacho;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
	}

	public TarifasEntidadSub(long codigoDetalleCargo,
			EntidadesSubcontratadas entidadesSubcontratadas,
			Articulo articuloByArtPrincipal, Solicitudes solicitudes,
			EstadosSolFact estadosSolFact, Usuarios usuarios, Pedido pedido,
			ContratosEntidadesSub contratosEntidadesSub,
			EsquemasTarifarios esquemasTarifarios, Articulo articuloByArticulo,
			AutorizacionesEntidadesSub autorizacionesEntidadesSub, Date fecha,
			String hora, BigDecimal valorUnitario, String vieneDespacho,
			Date fechaModifica, String horaModifica, String observaciones,
			Set erroresTarifasEntSubs) {
		this.codigoDetalleCargo = codigoDetalleCargo;
		this.entidadesSubcontratadas = entidadesSubcontratadas;
		this.articuloByArtPrincipal = articuloByArtPrincipal;
		this.solicitudes = solicitudes;
		this.estadosSolFact = estadosSolFact;
		this.usuarios = usuarios;
		this.pedido = pedido;
		this.contratosEntidadesSub = contratosEntidadesSub;
		this.esquemasTarifarios = esquemasTarifarios;
		this.articuloByArticulo = articuloByArticulo;
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
		this.fecha = fecha;
		this.hora = hora;
		this.valorUnitario = valorUnitario;
		this.vieneDespacho = vieneDespacho;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.observaciones = observaciones;
		this.erroresTarifasEntSubs = erroresTarifasEntSubs;
	}

	public long getCodigoDetalleCargo() {
		return this.codigoDetalleCargo;
	}

	public void setCodigoDetalleCargo(long codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	public EntidadesSubcontratadas getEntidadesSubcontratadas() {
		return this.entidadesSubcontratadas;
	}

	public void setEntidadesSubcontratadas(
			EntidadesSubcontratadas entidadesSubcontratadas) {
		this.entidadesSubcontratadas = entidadesSubcontratadas;
	}

	public Articulo getArticuloByArtPrincipal() {
		return this.articuloByArtPrincipal;
	}

	public void setArticuloByArtPrincipal(Articulo articuloByArtPrincipal) {
		this.articuloByArtPrincipal = articuloByArtPrincipal;
	}

	public Solicitudes getSolicitudes() {
		return this.solicitudes;
	}

	public void setSolicitudes(Solicitudes solicitudes) {
		this.solicitudes = solicitudes;
	}

	public EstadosSolFact getEstadosSolFact() {
		return this.estadosSolFact;
	}

	public void setEstadosSolFact(EstadosSolFact estadosSolFact) {
		this.estadosSolFact = estadosSolFact;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Pedido getPedido() {
		return this.pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public ContratosEntidadesSub getContratosEntidadesSub() {
		return this.contratosEntidadesSub;
	}

	public void setContratosEntidadesSub(
			ContratosEntidadesSub contratosEntidadesSub) {
		this.contratosEntidadesSub = contratosEntidadesSub;
	}

	public EsquemasTarifarios getEsquemasTarifarios() {
		return this.esquemasTarifarios;
	}

	public void setEsquemasTarifarios(EsquemasTarifarios esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}

	public Articulo getArticuloByArticulo() {
		return this.articuloByArticulo;
	}

	public void setArticuloByArticulo(Articulo articuloByArticulo) {
		this.articuloByArticulo = articuloByArticulo;
	}

	public AutorizacionesEntidadesSub getAutorizacionesEntidadesSub() {
		return this.autorizacionesEntidadesSub;
	}

	public void setAutorizacionesEntidadesSub(
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) {
		this.autorizacionesEntidadesSub = autorizacionesEntidadesSub;
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

	public BigDecimal getValorUnitario() {
		return this.valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public String getVieneDespacho() {
		return this.vieneDespacho;
	}

	public void setVieneDespacho(String vieneDespacho) {
		this.vieneDespacho = vieneDespacho;
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

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Set getErroresTarifasEntSubs() {
		return this.erroresTarifasEntSubs;
	}

	public void setErroresTarifasEntSubs(Set erroresTarifasEntSubs) {
		this.erroresTarifasEntSubs = erroresTarifasEntSubs;
	}

}
