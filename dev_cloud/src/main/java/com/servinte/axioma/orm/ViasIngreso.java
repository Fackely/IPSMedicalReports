package com.servinte.axioma.orm;

// Generated 30/06/2011 05:03:01 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ViasIngreso generated by hbm2java
 */
public class ViasIngreso implements java.io.Serializable {

	private int codigo;
	private Convenios convenios;
	private String nombre;
	private char reciboAutomatico;
	private String identificador;
	private char responsablePaciente;
	private char verificacionDerechos;
	private String usuarioModifica;
	private Date fechaModifica;
	private String horaModifica;
	private char corteFacturacion;
	private Character validarCierreNotaEnfer;
	private Character validarEpicrisisFinali;
	private Set requisitosPacConvenios = new HashSet(0);
	private Set soporteFacturases = new HashSet(0);
	private Set autorizacionesEntSubMontoses = new HashSet(0);
	private Set nivelAutorizacions = new HashSet(0);
	private Set cargoViaIngresoServicios = new HashSet(0);
	private Set historicoFiltroDistribucions = new HashSet(0);
	private Set garantiaPacientes = new HashSet(0);
	private Set excepcionesTarifases = new HashSet(0);
	private Set excepTarifasContratos = new HashSet(0);
	private Set tiposPacientes = new HashSet(0);
	private Set filtroDistribucions = new HashSet(0);
	private Set logConsolidadoFacturacions = new HashSet(0);
	private Set detParamEntidSubcontratadas = new HashSet(0);
	private Set informeInconsistenciases = new HashSet(0);
	private Set estanciaViaIngCentroCostos = new HashSet(0);
	private Set centroCostoViaIngresos = new HashSet(0);
	private Set cuentases = new HashSet(0);
	private Set horasReprocesos = new HashSet(0);
	private Set detalleCoberturas = new HashSet(0);
	private Set recargosTarifases = new HashSet(0);
	private Set descComConvconts = new HashSet(0);
	private Set logExCoberturasEntSubs = new HashSet(0);
	private Set ingresosEstancias = new HashSet(0);
	private Set exCoberturasEntidadSubs = new HashSet(0);
	private Set exepParaCobXConvconts = new HashSet(0);
	private Set viaIngresoEntSubs = new HashSet(0);
	private Set paquetesConvenios = new HashSet(0);
	private Set detalleMontos = new HashSet(0);
	private Set facturases = new HashSet(0);

	public ViasIngreso() {
	}

	public ViasIngreso(int codigo, char reciboAutomatico,
			char responsablePaciente, char verificacionDerechos,
			String usuarioModifica, Date fechaModifica, String horaModifica,
			char corteFacturacion) {
		this.codigo = codigo;
		this.reciboAutomatico = reciboAutomatico;
		this.responsablePaciente = responsablePaciente;
		this.verificacionDerechos = verificacionDerechos;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.corteFacturacion = corteFacturacion;
	}

	public ViasIngreso(int codigo, Convenios convenios, String nombre,
			char reciboAutomatico, String identificador,
			char responsablePaciente, char verificacionDerechos,
			String usuarioModifica, Date fechaModifica, String horaModifica,
			char corteFacturacion, Character validarCierreNotaEnfer,
			Character validarEpicrisisFinali, Set requisitosPacConvenios,
			Set soporteFacturases, Set autorizacionesEntSubMontoses,
			Set nivelAutorizacions, Set cargoViaIngresoServicios,
			Set historicoFiltroDistribucions, Set garantiaPacientes,
			Set excepcionesTarifases, Set excepTarifasContratos,
			Set tiposPacientes, Set filtroDistribucions,
			Set logConsolidadoFacturacions, Set detParamEntidSubcontratadas,
			Set informeInconsistenciases, Set estanciaViaIngCentroCostos,
			Set centroCostoViaIngresos, Set cuentases, Set horasReprocesos,
			Set detalleCoberturas, Set recargosTarifases, Set descComConvconts,
			Set logExCoberturasEntSubs, Set ingresosEstancias,
			Set exCoberturasEntidadSubs, Set exepParaCobXConvconts,
			Set viaIngresoEntSubs, Set paquetesConvenios, Set detalleMontos,
			Set facturases) {
		this.codigo = codigo;
		this.convenios = convenios;
		this.nombre = nombre;
		this.reciboAutomatico = reciboAutomatico;
		this.identificador = identificador;
		this.responsablePaciente = responsablePaciente;
		this.verificacionDerechos = verificacionDerechos;
		this.usuarioModifica = usuarioModifica;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.corteFacturacion = corteFacturacion;
		this.validarCierreNotaEnfer = validarCierreNotaEnfer;
		this.validarEpicrisisFinali = validarEpicrisisFinali;
		this.requisitosPacConvenios = requisitosPacConvenios;
		this.soporteFacturases = soporteFacturases;
		this.autorizacionesEntSubMontoses = autorizacionesEntSubMontoses;
		this.nivelAutorizacions = nivelAutorizacions;
		this.cargoViaIngresoServicios = cargoViaIngresoServicios;
		this.historicoFiltroDistribucions = historicoFiltroDistribucions;
		this.garantiaPacientes = garantiaPacientes;
		this.excepcionesTarifases = excepcionesTarifases;
		this.excepTarifasContratos = excepTarifasContratos;
		this.tiposPacientes = tiposPacientes;
		this.filtroDistribucions = filtroDistribucions;
		this.logConsolidadoFacturacions = logConsolidadoFacturacions;
		this.detParamEntidSubcontratadas = detParamEntidSubcontratadas;
		this.informeInconsistenciases = informeInconsistenciases;
		this.estanciaViaIngCentroCostos = estanciaViaIngCentroCostos;
		this.centroCostoViaIngresos = centroCostoViaIngresos;
		this.cuentases = cuentases;
		this.horasReprocesos = horasReprocesos;
		this.detalleCoberturas = detalleCoberturas;
		this.recargosTarifases = recargosTarifases;
		this.descComConvconts = descComConvconts;
		this.logExCoberturasEntSubs = logExCoberturasEntSubs;
		this.ingresosEstancias = ingresosEstancias;
		this.exCoberturasEntidadSubs = exCoberturasEntidadSubs;
		this.exepParaCobXConvconts = exepParaCobXConvconts;
		this.viaIngresoEntSubs = viaIngresoEntSubs;
		this.paquetesConvenios = paquetesConvenios;
		this.detalleMontos = detalleMontos;
		this.facturases = facturases;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public char getReciboAutomatico() {
		return this.reciboAutomatico;
	}

	public void setReciboAutomatico(char reciboAutomatico) {
		this.reciboAutomatico = reciboAutomatico;
	}

	public String getIdentificador() {
		return this.identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public char getResponsablePaciente() {
		return this.responsablePaciente;
	}

	public void setResponsablePaciente(char responsablePaciente) {
		this.responsablePaciente = responsablePaciente;
	}

	public char getVerificacionDerechos() {
		return this.verificacionDerechos;
	}

	public void setVerificacionDerechos(char verificacionDerechos) {
		this.verificacionDerechos = verificacionDerechos;
	}

	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
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

	public char getCorteFacturacion() {
		return this.corteFacturacion;
	}

	public void setCorteFacturacion(char corteFacturacion) {
		this.corteFacturacion = corteFacturacion;
	}

	public Character getValidarCierreNotaEnfer() {
		return this.validarCierreNotaEnfer;
	}

	public void setValidarCierreNotaEnfer(Character validarCierreNotaEnfer) {
		this.validarCierreNotaEnfer = validarCierreNotaEnfer;
	}

	public Character getValidarEpicrisisFinali() {
		return this.validarEpicrisisFinali;
	}

	public void setValidarEpicrisisFinali(Character validarEpicrisisFinali) {
		this.validarEpicrisisFinali = validarEpicrisisFinali;
	}

	public Set getRequisitosPacConvenios() {
		return this.requisitosPacConvenios;
	}

	public void setRequisitosPacConvenios(Set requisitosPacConvenios) {
		this.requisitosPacConvenios = requisitosPacConvenios;
	}

	public Set getSoporteFacturases() {
		return this.soporteFacturases;
	}

	public void setSoporteFacturases(Set soporteFacturases) {
		this.soporteFacturases = soporteFacturases;
	}

	public Set getAutorizacionesEntSubMontoses() {
		return this.autorizacionesEntSubMontoses;
	}

	public void setAutorizacionesEntSubMontoses(Set autorizacionesEntSubMontoses) {
		this.autorizacionesEntSubMontoses = autorizacionesEntSubMontoses;
	}

	public Set getNivelAutorizacions() {
		return this.nivelAutorizacions;
	}

	public void setNivelAutorizacions(Set nivelAutorizacions) {
		this.nivelAutorizacions = nivelAutorizacions;
	}

	public Set getCargoViaIngresoServicios() {
		return this.cargoViaIngresoServicios;
	}

	public void setCargoViaIngresoServicios(Set cargoViaIngresoServicios) {
		this.cargoViaIngresoServicios = cargoViaIngresoServicios;
	}

	public Set getHistoricoFiltroDistribucions() {
		return this.historicoFiltroDistribucions;
	}

	public void setHistoricoFiltroDistribucions(Set historicoFiltroDistribucions) {
		this.historicoFiltroDistribucions = historicoFiltroDistribucions;
	}

	public Set getGarantiaPacientes() {
		return this.garantiaPacientes;
	}

	public void setGarantiaPacientes(Set garantiaPacientes) {
		this.garantiaPacientes = garantiaPacientes;
	}

	public Set getExcepcionesTarifases() {
		return this.excepcionesTarifases;
	}

	public void setExcepcionesTarifases(Set excepcionesTarifases) {
		this.excepcionesTarifases = excepcionesTarifases;
	}

	public Set getExcepTarifasContratos() {
		return this.excepTarifasContratos;
	}

	public void setExcepTarifasContratos(Set excepTarifasContratos) {
		this.excepTarifasContratos = excepTarifasContratos;
	}

	public Set getTiposPacientes() {
		return this.tiposPacientes;
	}

	public void setTiposPacientes(Set tiposPacientes) {
		this.tiposPacientes = tiposPacientes;
	}

	public Set getFiltroDistribucions() {
		return this.filtroDistribucions;
	}

	public void setFiltroDistribucions(Set filtroDistribucions) {
		this.filtroDistribucions = filtroDistribucions;
	}

	public Set getLogConsolidadoFacturacions() {
		return this.logConsolidadoFacturacions;
	}

	public void setLogConsolidadoFacturacions(Set logConsolidadoFacturacions) {
		this.logConsolidadoFacturacions = logConsolidadoFacturacions;
	}

	public Set getDetParamEntidSubcontratadas() {
		return this.detParamEntidSubcontratadas;
	}

	public void setDetParamEntidSubcontratadas(Set detParamEntidSubcontratadas) {
		this.detParamEntidSubcontratadas = detParamEntidSubcontratadas;
	}

	public Set getInformeInconsistenciases() {
		return this.informeInconsistenciases;
	}

	public void setInformeInconsistenciases(Set informeInconsistenciases) {
		this.informeInconsistenciases = informeInconsistenciases;
	}

	public Set getEstanciaViaIngCentroCostos() {
		return this.estanciaViaIngCentroCostos;
	}

	public void setEstanciaViaIngCentroCostos(Set estanciaViaIngCentroCostos) {
		this.estanciaViaIngCentroCostos = estanciaViaIngCentroCostos;
	}

	public Set getCentroCostoViaIngresos() {
		return this.centroCostoViaIngresos;
	}

	public void setCentroCostoViaIngresos(Set centroCostoViaIngresos) {
		this.centroCostoViaIngresos = centroCostoViaIngresos;
	}

	public Set getCuentases() {
		return this.cuentases;
	}

	public void setCuentases(Set cuentases) {
		this.cuentases = cuentases;
	}

	public Set getHorasReprocesos() {
		return this.horasReprocesos;
	}

	public void setHorasReprocesos(Set horasReprocesos) {
		this.horasReprocesos = horasReprocesos;
	}

	public Set getDetalleCoberturas() {
		return this.detalleCoberturas;
	}

	public void setDetalleCoberturas(Set detalleCoberturas) {
		this.detalleCoberturas = detalleCoberturas;
	}

	public Set getRecargosTarifases() {
		return this.recargosTarifases;
	}

	public void setRecargosTarifases(Set recargosTarifases) {
		this.recargosTarifases = recargosTarifases;
	}

	public Set getDescComConvconts() {
		return this.descComConvconts;
	}

	public void setDescComConvconts(Set descComConvconts) {
		this.descComConvconts = descComConvconts;
	}

	public Set getLogExCoberturasEntSubs() {
		return this.logExCoberturasEntSubs;
	}

	public void setLogExCoberturasEntSubs(Set logExCoberturasEntSubs) {
		this.logExCoberturasEntSubs = logExCoberturasEntSubs;
	}

	public Set getIngresosEstancias() {
		return this.ingresosEstancias;
	}

	public void setIngresosEstancias(Set ingresosEstancias) {
		this.ingresosEstancias = ingresosEstancias;
	}

	public Set getExCoberturasEntidadSubs() {
		return this.exCoberturasEntidadSubs;
	}

	public void setExCoberturasEntidadSubs(Set exCoberturasEntidadSubs) {
		this.exCoberturasEntidadSubs = exCoberturasEntidadSubs;
	}

	public Set getExepParaCobXConvconts() {
		return this.exepParaCobXConvconts;
	}

	public void setExepParaCobXConvconts(Set exepParaCobXConvconts) {
		this.exepParaCobXConvconts = exepParaCobXConvconts;
	}

	public Set getViaIngresoEntSubs() {
		return this.viaIngresoEntSubs;
	}

	public void setViaIngresoEntSubs(Set viaIngresoEntSubs) {
		this.viaIngresoEntSubs = viaIngresoEntSubs;
	}

	public Set getPaquetesConvenios() {
		return this.paquetesConvenios;
	}

	public void setPaquetesConvenios(Set paquetesConvenios) {
		this.paquetesConvenios = paquetesConvenios;
	}

	public Set getDetalleMontos() {
		return this.detalleMontos;
	}

	public void setDetalleMontos(Set detalleMontos) {
		this.detalleMontos = detalleMontos;
	}

	public Set getFacturases() {
		return this.facturases;
	}

	public void setFacturases(Set facturases) {
		this.facturases = facturases;
	}

}
