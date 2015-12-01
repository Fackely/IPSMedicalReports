package com.servinte.axioma.orm;

// Generated 30/06/2011 05:03:01 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Ingresos generated by hbm2java
 */
public class Ingresos implements java.io.Serializable {

	private int id;
	private Ingresos ingresos;
	private Usuarios usuariosByUsuarioPreingresoGen;
	private CentroAtencion centroAtencion;
	private Pacientes pacientes;
	private Usuarios usuariosByUsuarioModifica;
	private PacEntidadesSubcontratadas pacEntidadesSubcontratadas;
	private Instituciones instituciones;
	private Usuarios usuariosByUsuarioPreingresoPen;
	private Date fechaEgreso;
	private String estado;
	private Date fechaIngreso;
	private String horaIngreso;
	private Date fechaModifica;
	private String horaModifica;
	private String consecutivo;
	private String anioConsecutivo;
	private char cierreManual;
	private Character reaperturaAutomatica;
	private String preingreso;
	private Date fechaPreingresoPen;
	private String horaPreingresoPen;
	private Date fechaPreingresoGen;
	private String horaPreingresoGen;
	private String transplante;
	private String controlPostOperatorioCx;
	private String tipoIngreso;
	private String migrado;
	private String horaEgreso;
	private Set hisRequisitosPacSubcuentas = new HashSet(0);
	private Set odontogramas = new HashSet(0);
	private Set documentosGarantias = new HashSet(0);
	private Set logPlanTratamientos = new HashSet(0);
	private Set logPlanTratamientos_1 = new HashSet(0);
	private Set reingresoSalidaHospiDias = new HashSet(0);
	private Set informeAtencionIniUrgs = new HashSet(0);
	private Set cierreIngresoses = new HashSet(0);
	private Set subCuentases = new HashSet(0);
	private Set encuestaCalidads = new HashSet(0);
	private Set notasAdministrativases = new HashSet(0);
	private Set hisConfPlanTratamientos = new HashSet(0);
	private Set registroEventoCatastroficos = new HashSet(0);
	private Set ingresoses = new HashSet(0);
	private Set consentimientoInfoOdontos = new HashSet(0);
	private Set presupuestoPacientes = new HashSet(0);
	private Set movimientosAbonoses = new HashSet(0);
	private Set registroAccidentesTransitos = new HashSet(0);
	private Set informeInconsistenciases = new HashSet(0);
	private Set cuentases = new HashSet(0);
	private Set autorizacioneses = new HashSet(0);
	private Set peticionQxes = new HashSet(0);
	private Set logPresupuestoOdontos = new HashSet(0);
	private Set ordenesAmbulatoriases = new HashSet(0);
	private Set trasSolTransplantesForDonante = new HashSet(0);
	private Set asociosCuentas = new HashSet(0);
	private Set verificacionesDerechoses = new HashSet(0);
	private Set inactivacionesCargoses = new HashSet(0);
	private Set reclamacionesAccEveFacts = new HashSet(0);
	private Set trasSolTransplantesForReceptor = new HashSet(0);
	private Set planTratamientos = new HashSet(0);
	private Set presupuestoOdontologicos = new HashSet(0);
	private Set historicoSubcuentases = new HashSet(0);
	private Set notaAclaratorias = new HashSet(0);
	private Set historicoDistribucions = new HashSet(0);
	private DistribucionIngreso distribucionIngreso;
	private Set pacEntidadesSubcontratadases = new HashSet(0);
	private Set logDistribucionCuentas = new HashSet(0);

	public Ingresos() {
	}

	public Ingresos(int id, CentroAtencion centroAtencion, Pacientes pacientes,
			Usuarios usuariosByUsuarioModifica, Instituciones instituciones,
			String estado, Date fechaIngreso, String horaIngreso,
			Date fechaModifica, String horaModifica, String consecutivo,
			char cierreManual, String tipoIngreso) {
		this.id = id;
		this.centroAtencion = centroAtencion;
		this.pacientes = pacientes;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.instituciones = instituciones;
		this.estado = estado;
		this.fechaIngreso = fechaIngreso;
		this.horaIngreso = horaIngreso;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.consecutivo = consecutivo;
		this.cierreManual = cierreManual;
		this.tipoIngreso = tipoIngreso;
	}

	public Ingresos(int id, Ingresos ingresos,
			Usuarios usuariosByUsuarioPreingresoGen,
			CentroAtencion centroAtencion, Pacientes pacientes,
			Usuarios usuariosByUsuarioModifica,
			PacEntidadesSubcontratadas pacEntidadesSubcontratadas,
			Instituciones instituciones,
			Usuarios usuariosByUsuarioPreingresoPen, Date fechaEgreso,
			String estado, Date fechaIngreso, String horaIngreso,
			Date fechaModifica, String horaModifica, String consecutivo,
			String anioConsecutivo, char cierreManual,
			Character reaperturaAutomatica, String preingreso,
			Date fechaPreingresoPen, String horaPreingresoPen,
			Date fechaPreingresoGen, String horaPreingresoGen,
			String transplante, String controlPostOperatorioCx,
			String tipoIngreso, String migrado, String horaEgreso,
			Set hisRequisitosPacSubcuentas, Set odontogramas,
			Set logPlanTratamientos, Set documentosGarantias, Set logPlanTratamientos_1,
			Set reingresoSalidaHospiDias, Set informeAtencionIniUrgs,
			Set cierreIngresoses, Set subCuentases, Set encuestaCalidads,
			Set notasAdministrativases, Set hisConfPlanTratamientos,
			Set registroEventoCatastroficos, Set ingresoses,
			Set consentimientoInfoOdontos, Set presupuestoPacientes,
			Set movimientosAbonoses, Set registroAccidentesTransitos,
			Set informeInconsistenciases, Set cuentases, Set autorizacioneses,
			Set peticionQxes, Set logPresupuestoOdontos,
			Set ordenesAmbulatoriases, Set trasSolTransplantesForDonante,
			Set asociosCuentas, Set verificacionesDerechoses,
			Set inactivacionesCargoses, Set reclamacionesAccEveFacts,
			Set trasSolTransplantesForReceptor, Set planTratamientos,
			Set presupuestoOdontologicos, Set historicoSubcuentases,
			Set notaAclaratorias, Set historicoDistribucions,
			DistribucionIngreso distribucionIngreso,
			Set pacEntidadesSubcontratadases) {
		this.id = id;
		this.ingresos = ingresos;
		this.usuariosByUsuarioPreingresoGen = usuariosByUsuarioPreingresoGen;
		this.centroAtencion = centroAtencion;
		this.pacientes = pacientes;
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
		this.pacEntidadesSubcontratadas = pacEntidadesSubcontratadas;
		this.instituciones = instituciones;
		this.usuariosByUsuarioPreingresoPen = usuariosByUsuarioPreingresoPen;
		this.fechaEgreso = fechaEgreso;
		this.estado = estado;
		this.fechaIngreso = fechaIngreso;
		this.horaIngreso = horaIngreso;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.consecutivo = consecutivo;
		this.anioConsecutivo = anioConsecutivo;
		this.cierreManual = cierreManual;
		this.reaperturaAutomatica = reaperturaAutomatica;
		this.preingreso = preingreso;
		this.fechaPreingresoPen = fechaPreingresoPen;
		this.horaPreingresoPen = horaPreingresoPen;
		this.fechaPreingresoGen = fechaPreingresoGen;
		this.horaPreingresoGen = horaPreingresoGen;
		this.transplante = transplante;
		this.controlPostOperatorioCx = controlPostOperatorioCx;
		this.tipoIngreso = tipoIngreso;
		this.migrado = migrado;
		this.horaEgreso = horaEgreso;
		this.hisRequisitosPacSubcuentas = hisRequisitosPacSubcuentas;
		this.odontogramas = odontogramas;
		this.documentosGarantias = documentosGarantias;
		this.logPlanTratamientos = logPlanTratamientos;
		this.logPlanTratamientos_1 = logPlanTratamientos_1;
		this.reingresoSalidaHospiDias = reingresoSalidaHospiDias;
		this.informeAtencionIniUrgs = informeAtencionIniUrgs;
		this.cierreIngresoses = cierreIngresoses;
		this.subCuentases = subCuentases;
		this.encuestaCalidads = encuestaCalidads;
		this.notasAdministrativases = notasAdministrativases;
		this.hisConfPlanTratamientos = hisConfPlanTratamientos;
		this.registroEventoCatastroficos = registroEventoCatastroficos;
		this.ingresoses = ingresoses;
		this.consentimientoInfoOdontos = consentimientoInfoOdontos;
		this.presupuestoPacientes = presupuestoPacientes;
		this.movimientosAbonoses = movimientosAbonoses;
		this.registroAccidentesTransitos = registroAccidentesTransitos;
		this.informeInconsistenciases = informeInconsistenciases;
		this.cuentases = cuentases;
		this.autorizacioneses = autorizacioneses;
		this.peticionQxes = peticionQxes;
		this.logPresupuestoOdontos = logPresupuestoOdontos;
		this.ordenesAmbulatoriases = ordenesAmbulatoriases;
		this.trasSolTransplantesForDonante = trasSolTransplantesForDonante;
		this.asociosCuentas = asociosCuentas;
		this.verificacionesDerechoses = verificacionesDerechoses;
		this.inactivacionesCargoses = inactivacionesCargoses;
		this.reclamacionesAccEveFacts = reclamacionesAccEveFacts;
		this.trasSolTransplantesForReceptor = trasSolTransplantesForReceptor;
		this.planTratamientos = planTratamientos;
		this.presupuestoOdontologicos = presupuestoOdontologicos;
		this.historicoSubcuentases = historicoSubcuentases;
		this.notaAclaratorias = notaAclaratorias;
		this.historicoDistribucions = historicoDistribucions;
		this.distribucionIngreso = distribucionIngreso;
		this.pacEntidadesSubcontratadases = pacEntidadesSubcontratadases;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Ingresos getIngresos() {
		return this.ingresos;
	}

	public void setIngresos(Ingresos ingresos) {
		this.ingresos = ingresos;
	}

	public Usuarios getUsuariosByUsuarioPreingresoGen() {
		return this.usuariosByUsuarioPreingresoGen;
	}

	public void setUsuariosByUsuarioPreingresoGen(
			Usuarios usuariosByUsuarioPreingresoGen) {
		this.usuariosByUsuarioPreingresoGen = usuariosByUsuarioPreingresoGen;
	}

	public CentroAtencion getCentroAtencion() {
		return this.centroAtencion;
	}

	public void setCentroAtencion(CentroAtencion centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public Pacientes getPacientes() {
		return this.pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

	public Usuarios getUsuariosByUsuarioModifica() {
		return this.usuariosByUsuarioModifica;
	}

	public void setUsuariosByUsuarioModifica(Usuarios usuariosByUsuarioModifica) {
		this.usuariosByUsuarioModifica = usuariosByUsuarioModifica;
	}

	public PacEntidadesSubcontratadas getPacEntidadesSubcontratadas() {
		return this.pacEntidadesSubcontratadas;
	}

	public void setPacEntidadesSubcontratadas(
			PacEntidadesSubcontratadas pacEntidadesSubcontratadas) {
		this.pacEntidadesSubcontratadas = pacEntidadesSubcontratadas;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Usuarios getUsuariosByUsuarioPreingresoPen() {
		return this.usuariosByUsuarioPreingresoPen;
	}

	public void setUsuariosByUsuarioPreingresoPen(
			Usuarios usuariosByUsuarioPreingresoPen) {
		this.usuariosByUsuarioPreingresoPen = usuariosByUsuarioPreingresoPen;
	}

	public Date getFechaEgreso() {
		return this.fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getHoraIngreso() {
		return this.horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
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

	public String getConsecutivo() {
		return this.consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getAnioConsecutivo() {
		return this.anioConsecutivo;
	}

	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	public char getCierreManual() {
		return this.cierreManual;
	}

	public void setCierreManual(char cierreManual) {
		this.cierreManual = cierreManual;
	}

	public Character getReaperturaAutomatica() {
		return this.reaperturaAutomatica;
	}

	public void setReaperturaAutomatica(Character reaperturaAutomatica) {
		this.reaperturaAutomatica = reaperturaAutomatica;
	}

	public String getPreingreso() {
		return this.preingreso;
	}

	public void setPreingreso(String preingreso) {
		this.preingreso = preingreso;
	}

	public Date getFechaPreingresoPen() {
		return this.fechaPreingresoPen;
	}

	public void setFechaPreingresoPen(Date fechaPreingresoPen) {
		this.fechaPreingresoPen = fechaPreingresoPen;
	}

	public String getHoraPreingresoPen() {
		return this.horaPreingresoPen;
	}

	public void setHoraPreingresoPen(String horaPreingresoPen) {
		this.horaPreingresoPen = horaPreingresoPen;
	}

	public Date getFechaPreingresoGen() {
		return this.fechaPreingresoGen;
	}

	public void setFechaPreingresoGen(Date fechaPreingresoGen) {
		this.fechaPreingresoGen = fechaPreingresoGen;
	}

	public String getHoraPreingresoGen() {
		return this.horaPreingresoGen;
	}

	public void setHoraPreingresoGen(String horaPreingresoGen) {
		this.horaPreingresoGen = horaPreingresoGen;
	}

	public String getTransplante() {
		return this.transplante;
	}

	public void setTransplante(String transplante) {
		this.transplante = transplante;
	}

	public String getControlPostOperatorioCx() {
		return this.controlPostOperatorioCx;
	}

	public void setControlPostOperatorioCx(String controlPostOperatorioCx) {
		this.controlPostOperatorioCx = controlPostOperatorioCx;
	}

	public String getTipoIngreso() {
		return this.tipoIngreso;
	}

	public void setTipoIngreso(String tipoIngreso) {
		this.tipoIngreso = tipoIngreso;
	}

	public String getMigrado() {
		return this.migrado;
	}

	public void setMigrado(String migrado) {
		this.migrado = migrado;
	}

	public String getHoraEgreso() {
		return this.horaEgreso;
	}

	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

	public Set getHisRequisitosPacSubcuentas() {
		return this.hisRequisitosPacSubcuentas;
	}

	public void setHisRequisitosPacSubcuentas(Set hisRequisitosPacSubcuentas) {
		this.hisRequisitosPacSubcuentas = hisRequisitosPacSubcuentas;
	}

	public Set getOdontogramas() {
		return this.odontogramas;
	}

	public void setOdontogramas(Set odontogramas) {
		this.odontogramas = odontogramas;
	}

	public Set getDocumentosGarantias() {
		return this.documentosGarantias;
	}

	public void setDocumentosGarantias(Set documentosGarantias) {
		this.documentosGarantias = documentosGarantias;
	}

	public Set getLogPlanTratamientos() {
		return this.logPlanTratamientos;
	}

	public void setLogPlanTratamientos(Set logPlanTratamientos) {
		this.logPlanTratamientos = logPlanTratamientos;
	}

	public Set getLogPlanTratamientos_1() {
		return this.logPlanTratamientos_1;
	}

	public void setLogPlanTratamientos_1(Set logPlanTratamientos_1) {
		this.logPlanTratamientos_1 = logPlanTratamientos_1;
	}

	public Set getReingresoSalidaHospiDias() {
		return this.reingresoSalidaHospiDias;
	}

	public void setReingresoSalidaHospiDias(Set reingresoSalidaHospiDias) {
		this.reingresoSalidaHospiDias = reingresoSalidaHospiDias;
	}

	public Set getInformeAtencionIniUrgs() {
		return this.informeAtencionIniUrgs;
	}

	public void setInformeAtencionIniUrgs(Set informeAtencionIniUrgs) {
		this.informeAtencionIniUrgs = informeAtencionIniUrgs;
	}

	public Set getCierreIngresoses() {
		return this.cierreIngresoses;
	}

	public void setCierreIngresoses(Set cierreIngresoses) {
		this.cierreIngresoses = cierreIngresoses;
	}

	public Set getSubCuentases() {
		return this.subCuentases;
	}

	public void setSubCuentases(Set subCuentases) {
		this.subCuentases = subCuentases;
	}

	public Set getEncuestaCalidads() {
		return this.encuestaCalidads;
	}

	public void setEncuestaCalidads(Set encuestaCalidads) {
		this.encuestaCalidads = encuestaCalidads;
	}

	public Set getNotasAdministrativases() {
		return this.notasAdministrativases;
	}

	public void setNotasAdministrativases(Set notasAdministrativases) {
		this.notasAdministrativases = notasAdministrativases;
	}

	public Set getHisConfPlanTratamientos() {
		return this.hisConfPlanTratamientos;
	}

	public void setHisConfPlanTratamientos(Set hisConfPlanTratamientos) {
		this.hisConfPlanTratamientos = hisConfPlanTratamientos;
	}

	public Set getRegistroEventoCatastroficos() {
		return this.registroEventoCatastroficos;
	}

	public void setRegistroEventoCatastroficos(Set registroEventoCatastroficos) {
		this.registroEventoCatastroficos = registroEventoCatastroficos;
	}

	public Set getIngresoses() {
		return this.ingresoses;
	}

	public void setIngresoses(Set ingresoses) {
		this.ingresoses = ingresoses;
	}

	public Set getConsentimientoInfoOdontos() {
		return this.consentimientoInfoOdontos;
	}

	public void setConsentimientoInfoOdontos(Set consentimientoInfoOdontos) {
		this.consentimientoInfoOdontos = consentimientoInfoOdontos;
	}

	public Set getPresupuestoPacientes() {
		return this.presupuestoPacientes;
	}

	public void setPresupuestoPacientes(Set presupuestoPacientes) {
		this.presupuestoPacientes = presupuestoPacientes;
	}

	public Set getMovimientosAbonoses() {
		return this.movimientosAbonoses;
	}

	public void setMovimientosAbonoses(Set movimientosAbonoses) {
		this.movimientosAbonoses = movimientosAbonoses;
	}

	public Set getRegistroAccidentesTransitos() {
		return this.registroAccidentesTransitos;
	}

	public void setRegistroAccidentesTransitos(Set registroAccidentesTransitos) {
		this.registroAccidentesTransitos = registroAccidentesTransitos;
	}

	public Set getInformeInconsistenciases() {
		return this.informeInconsistenciases;
	}

	public void setInformeInconsistenciases(Set informeInconsistenciases) {
		this.informeInconsistenciases = informeInconsistenciases;
	}

	public Set getCuentases() {
		return this.cuentases;
	}

	public void setCuentases(Set cuentases) {
		this.cuentases = cuentases;
	}

	public Set getAutorizacioneses() {
		return this.autorizacioneses;
	}

	public void setAutorizacioneses(Set autorizacioneses) {
		this.autorizacioneses = autorizacioneses;
	}

	public Set getPeticionQxes() {
		return this.peticionQxes;
	}

	public void setPeticionQxes(Set peticionQxes) {
		this.peticionQxes = peticionQxes;
	}

	public Set getLogPresupuestoOdontos() {
		return this.logPresupuestoOdontos;
	}

	public void setLogPresupuestoOdontos(Set logPresupuestoOdontos) {
		this.logPresupuestoOdontos = logPresupuestoOdontos;
	}

	public Set getOrdenesAmbulatoriases() {
		return this.ordenesAmbulatoriases;
	}

	public void setOrdenesAmbulatoriases(Set ordenesAmbulatoriases) {
		this.ordenesAmbulatoriases = ordenesAmbulatoriases;
	}

	public Set getTrasSolTransplantesForDonante() {
		return this.trasSolTransplantesForDonante;
	}

	public void setTrasSolTransplantesForDonante(
			Set trasSolTransplantesForDonante) {
		this.trasSolTransplantesForDonante = trasSolTransplantesForDonante;
	}

	public Set getAsociosCuentas() {
		return this.asociosCuentas;
	}

	public void setAsociosCuentas(Set asociosCuentas) {
		this.asociosCuentas = asociosCuentas;
	}

	public Set getVerificacionesDerechoses() {
		return this.verificacionesDerechoses;
	}

	public void setVerificacionesDerechoses(Set verificacionesDerechoses) {
		this.verificacionesDerechoses = verificacionesDerechoses;
	}

	public Set getInactivacionesCargoses() {
		return this.inactivacionesCargoses;
	}

	public void setInactivacionesCargoses(Set inactivacionesCargoses) {
		this.inactivacionesCargoses = inactivacionesCargoses;
	}

	public Set getReclamacionesAccEveFacts() {
		return this.reclamacionesAccEveFacts;
	}

	public void setReclamacionesAccEveFacts(Set reclamacionesAccEveFacts) {
		this.reclamacionesAccEveFacts = reclamacionesAccEveFacts;
	}

	public Set getTrasSolTransplantesForReceptor() {
		return this.trasSolTransplantesForReceptor;
	}

	public void setTrasSolTransplantesForReceptor(
			Set trasSolTransplantesForReceptor) {
		this.trasSolTransplantesForReceptor = trasSolTransplantesForReceptor;
	}

	public Set getPlanTratamientos() {
		return this.planTratamientos;
	}

	public void setPlanTratamientos(Set planTratamientos) {
		this.planTratamientos = planTratamientos;
	}

	public Set getPresupuestoOdontologicos() {
		return this.presupuestoOdontologicos;
	}

	public void setPresupuestoOdontologicos(Set presupuestoOdontologicos) {
		this.presupuestoOdontologicos = presupuestoOdontologicos;
	}

	public Set getHistoricoSubcuentases() {
		return this.historicoSubcuentases;
	}

	public void setHistoricoSubcuentases(Set historicoSubcuentases) {
		this.historicoSubcuentases = historicoSubcuentases;
	}

	public Set getNotaAclaratorias() {
		return this.notaAclaratorias;
	}

	public void setNotaAclaratorias(Set notaAclaratorias) {
		this.notaAclaratorias = notaAclaratorias;
	}
	
	public Set getHistoricoDistribucions() {
		return this.historicoDistribucions;
	}

	public void setHistoricoDistribucions(Set historicoDistribucions) {
		this.historicoDistribucions = historicoDistribucions;
	}

	public DistribucionIngreso getDistribucionIngreso() {
		return this.distribucionIngreso;
	}

	public void setDistribucionIngreso(DistribucionIngreso distribucionIngreso) {
		this.distribucionIngreso = distribucionIngreso;
	}

	public Set getPacEntidadesSubcontratadases() {
		return this.pacEntidadesSubcontratadases;
	}

	public void setPacEntidadesSubcontratadases(Set pacEntidadesSubcontratadases) {
		this.pacEntidadesSubcontratadases = pacEntidadesSubcontratadases;
	}

	/**
	 * @return the logDistribucionCuentas
	 */
	public Set getLogDistribucionCuentas() {
		return logDistribucionCuentas;
	}

	/**
	 * @param logDistribucionCuentas the logDistribucionCuentas to set
	 */
	public void setLogDistribucionCuentas(Set logDistribucionCuentas) {
		this.logDistribucionCuentas = logDistribucionCuentas;
	}

}
