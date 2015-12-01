package com.servinte.axioma.orm;

// Generated Sep 13, 2010 1:53:02 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * DetalleMonto generated by hbm2java
 */
public class DetalleMonto implements java.io.Serializable {

	private static final long serialVersionUID = 4689951696615701012L;
	private int detalleCodigo;
	private TiposAfiliado tiposAfiliado;
	private ViasIngreso viasIngreso;
	private EstratosSociales estratosSociales;
	private MontosCobro montosCobro;
	private TiposPaciente tiposPaciente;
	private TiposMonto tiposMonto;
	private NaturalezaPacientes naturalezaPacientes;
	private Usuarios usuarios;
	private Date fechaRegistro;
	private String horaRegistro;
	private String tipoDetalle;
	private Boolean activo;
	private Set montoAgrupacionServicioses = new HashSet(0);
	private Set subCuentases = new HashSet(0);
	private Set montoArticuloEspecificos = new HashSet(0);
	private Set histoDetalleMontos = new HashSet(0);
	private Set montoServicioEspecificos = new HashSet(0);
	private Set autorizacionesEntSubMontoses = new HashSet(0);
	private DetalleMontoGeneral detalleMontoGeneral;

	public DetalleMonto() {
	}

	public DetalleMonto(int detalleCodigo, TiposAfiliado tiposAfiliado,
			ViasIngreso viasIngreso, EstratosSociales estratosSociales,
			MontosCobro montosCobro, TiposPaciente tiposPaciente,
			TiposMonto tiposMonto, Usuarios usuarios, Date fechaRegistro,
			String horaRegistro, String tipoDetalle) {
		this.detalleCodigo = detalleCodigo;
		this.tiposAfiliado = tiposAfiliado;
		this.viasIngreso = viasIngreso;
		this.estratosSociales = estratosSociales;
		this.montosCobro = montosCobro;
		this.tiposPaciente = tiposPaciente;
		this.tiposMonto = tiposMonto;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.tipoDetalle = tipoDetalle;
	}

	public DetalleMonto(int detalleCodigo, TiposAfiliado tiposAfiliado,
			ViasIngreso viasIngreso, EstratosSociales estratosSociales,
			MontosCobro montosCobro, TiposPaciente tiposPaciente,
			TiposMonto tiposMonto, NaturalezaPacientes naturalezaPacientes,
			Usuarios usuarios, Date fechaRegistro, String horaRegistro,
			String tipoDetalle, Boolean activo, Set montoAgrupacionServicioses,
			Set subCuentases, Set montoArticuloEspecificos,
			Set histoDetalleMontos, Set montoServicioEspecificos,
			Set autorizacionesEntSubMontoses,
			DetalleMontoGeneral detalleMontoGeneral) {
		this.detalleCodigo = detalleCodigo;
		this.tiposAfiliado = tiposAfiliado;
		this.viasIngreso = viasIngreso;
		this.estratosSociales = estratosSociales;
		this.montosCobro = montosCobro;
		this.tiposPaciente = tiposPaciente;
		this.tiposMonto = tiposMonto;
		this.naturalezaPacientes = naturalezaPacientes;
		this.usuarios = usuarios;
		this.fechaRegistro = fechaRegistro;
		this.horaRegistro = horaRegistro;
		this.tipoDetalle = tipoDetalle;
		this.activo = activo;
		this.montoAgrupacionServicioses = montoAgrupacionServicioses;
		this.subCuentases = subCuentases;
		this.montoArticuloEspecificos = montoArticuloEspecificos;
		this.histoDetalleMontos = histoDetalleMontos;
		this.montoServicioEspecificos = montoServicioEspecificos;
		this.autorizacionesEntSubMontoses=autorizacionesEntSubMontoses;
		this.detalleMontoGeneral = detalleMontoGeneral;
	}

	public int getDetalleCodigo() {
		return this.detalleCodigo;
	}

	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}

	public TiposAfiliado getTiposAfiliado() {
		return this.tiposAfiliado;
	}

	public void setTiposAfiliado(TiposAfiliado tiposAfiliado) {
		this.tiposAfiliado = tiposAfiliado;
	}

	public ViasIngreso getViasIngreso() {
		return this.viasIngreso;
	}

	public void setViasIngreso(ViasIngreso viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	public EstratosSociales getEstratosSociales() {
		return this.estratosSociales;
	}

	public void setEstratosSociales(EstratosSociales estratosSociales) {
		this.estratosSociales = estratosSociales;
	}

	public MontosCobro getMontosCobro() {
		return this.montosCobro;
	}

	public void setMontosCobro(MontosCobro montosCobro) {
		this.montosCobro = montosCobro;
	}

	public TiposPaciente getTiposPaciente() {
		return this.tiposPaciente;
	}

	public void setTiposPaciente(TiposPaciente tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	public TiposMonto getTiposMonto() {
		return this.tiposMonto;
	}

	public void setTiposMonto(TiposMonto tiposMonto) {
		this.tiposMonto = tiposMonto;
	}

	public NaturalezaPacientes getNaturalezaPacientes() {
		return this.naturalezaPacientes;
	}

	public void setNaturalezaPacientes(NaturalezaPacientes naturalezaPacientes) {
		this.naturalezaPacientes = naturalezaPacientes;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getHoraRegistro() {
		return this.horaRegistro;
	}

	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	public String getTipoDetalle() {
		return this.tipoDetalle;
	}

	public void setTipoDetalle(String tipoDetalle) {
		this.tipoDetalle = tipoDetalle;
	}

	public Boolean getActivo() {
		return this.activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Set getMontoAgrupacionServicioses() {
		return this.montoAgrupacionServicioses;
	}

	public void setMontoAgrupacionServicioses(Set montoAgrupacionServicioses) {
		this.montoAgrupacionServicioses = montoAgrupacionServicioses;
	}

	public Set getSubCuentases() {
		return this.subCuentases;
	}

	public void setSubCuentases(Set subCuentases) {
		this.subCuentases = subCuentases;
	}

	public Set getMontoArticuloEspecificos() {
		return this.montoArticuloEspecificos;
	}

	public void setMontoArticuloEspecificos(Set montoArticuloEspecificos) {
		this.montoArticuloEspecificos = montoArticuloEspecificos;
	}

	public Set getHistoDetalleMontos() {
		return this.histoDetalleMontos;
	}

	public void setHistoDetalleMontos(Set histoDetalleMontos) {
		this.histoDetalleMontos = histoDetalleMontos;
	}

	public Set getMontoServicioEspecificos() {
		return this.montoServicioEspecificos;
	}

	public void setMontoServicioEspecificos(Set montoServicioEspecificos) {
		this.montoServicioEspecificos = montoServicioEspecificos;
	}

	public DetalleMontoGeneral getDetalleMontoGeneral() {
		return this.detalleMontoGeneral;
	}

	public void setDetalleMontoGeneral(DetalleMontoGeneral detalleMontoGeneral) {
		this.detalleMontoGeneral = detalleMontoGeneral;
	}

	/**
	 * @return the autorizacionesEntSubMontoses
	 */
	public Set getAutorizacionesEntSubMontoses() {
		return autorizacionesEntSubMontoses;
	}

	/**
	 * @param autorizacionesEntSubMontoses the autorizacionesEntSubMontoses to set
	 */
	public void setAutorizacionesEntSubMontoses(Set autorizacionesEntSubMontoses) {
		this.autorizacionesEntSubMontoses = autorizacionesEntSubMontoses;
	}

}
