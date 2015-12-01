package com.servinte.axioma.orm;

// Generated 21/07/2011 03:06:42 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;


/**
 * Egresos generated by hbm2java
 */
public class Egresos implements java.io.Serializable {

	private int cuenta;
	private Personas personas;
	private Diagnosticos diagnosticosByFkEgresosDiagPrinc;
	private Diagnosticos diagnosticosByFkEgresosDiagnosticor1;
	private Diagnosticos diagnosticosByFkEgresosDiagnosticor3;
	private Diagnosticos diagnosticosByFkEgresosDiagnosticor2;
	private TipoMonitoreo tipoMonitoreo;
	private Evoluciones evolucionesByEvolucionGeneroMotivoRev;
	private Medicos medicos;
	private Diagnosticos diagnosticosByFkEgresosDiagMuerte;
	private Evoluciones evolucionesByEvolucion;
	private Cuentas cuentas;
	private Usuarios usuarios;
	private CausasExternas causasExternas;
	private Diagnosticos diagnosticosByFkEgresosDiagnosticoC;
	private Boolean estadoSalida;
	private Integer destinoSalida;
	private String otroDestinoSalida;
	private Date fechaEgreso;
	private String horaEgreso;
	private Date fechaReversionEgreso;
	private String horaReversionEgreso;
	private Date fechaGrabacion;
	private String horaGrabacion;
	private String motivoReversion;
	private Boolean mostrarMotivoRevEpicrisis;
	private boolean esAutomatico;
	private Long resProc;
	private String acompanadoPor;
	private String remitidoA;
	private String placaNro;
	private String conductorAmbulancia;
	private String quienRecibe;
	private String observaciones;
	private Integer evolucionGeneroMotivoRev;
	private Integer causaExterna;
	
	public Egresos() {
	}

	public Egresos(Diagnosticos diagnosticosByFkEgresosDiagPrinc,
			Diagnosticos diagnosticosByFkEgresosDiagnosticor1,
			Diagnosticos diagnosticosByFkEgresosDiagnosticor3,
			Diagnosticos diagnosticosByFkEgresosDiagnosticor2,
			Diagnosticos diagnosticosByFkEgresosDiagMuerte, Cuentas cuentas,
			boolean esAutomatico) {
		this.diagnosticosByFkEgresosDiagPrinc = diagnosticosByFkEgresosDiagPrinc;
		this.diagnosticosByFkEgresosDiagnosticor1 = diagnosticosByFkEgresosDiagnosticor1;
		this.diagnosticosByFkEgresosDiagnosticor3 = diagnosticosByFkEgresosDiagnosticor3;
		this.diagnosticosByFkEgresosDiagnosticor2 = diagnosticosByFkEgresosDiagnosticor2;
		this.diagnosticosByFkEgresosDiagMuerte = diagnosticosByFkEgresosDiagMuerte;
		this.cuentas = cuentas;
		this.esAutomatico = esAutomatico;
	}

	public Egresos(Personas personas,
			Diagnosticos diagnosticosByFkEgresosDiagPrinc,
			Diagnosticos diagnosticosByFkEgresosDiagnosticor1,
			Diagnosticos diagnosticosByFkEgresosDiagnosticor3,
			Diagnosticos diagnosticosByFkEgresosDiagnosticor2,
			TipoMonitoreo tipoMonitoreo,
			Evoluciones evolucionesByEvolucionGeneroMotivoRev, Medicos medicos,
			Diagnosticos diagnosticosByFkEgresosDiagMuerte,
			Evoluciones evolucionesByEvolucion, Cuentas cuentas,
			Usuarios usuarios, CausasExternas causasExternas,
			Diagnosticos diagnosticosByFkEgresosDiagnosticoC,
			Boolean estadoSalida, Integer destinoSalida,
			String otroDestinoSalida, Date fechaEgreso, String horaEgreso,
			Date fechaReversionEgreso, String horaReversionEgreso,
			Date fechaGrabacion, String horaGrabacion, String motivoReversion,
			Boolean mostrarMotivoRevEpicrisis, boolean esAutomatico,
			Long resProc, String acompanadoPor, String remitidoA,
			String placaNro, String conductorAmbulancia, String quienRecibe,
			String observaciones) {
		this.personas = personas;
		this.diagnosticosByFkEgresosDiagPrinc = diagnosticosByFkEgresosDiagPrinc;
		this.diagnosticosByFkEgresosDiagnosticor1 = diagnosticosByFkEgresosDiagnosticor1;
		this.diagnosticosByFkEgresosDiagnosticor3 = diagnosticosByFkEgresosDiagnosticor3;
		this.diagnosticosByFkEgresosDiagnosticor2 = diagnosticosByFkEgresosDiagnosticor2;
		this.tipoMonitoreo = tipoMonitoreo;
		this.evolucionesByEvolucionGeneroMotivoRev = evolucionesByEvolucionGeneroMotivoRev;
		this.medicos = medicos;
		this.diagnosticosByFkEgresosDiagMuerte = diagnosticosByFkEgresosDiagMuerte;
		this.evolucionesByEvolucion = evolucionesByEvolucion;
		this.cuentas = cuentas;
		this.usuarios = usuarios;
		this.causasExternas = causasExternas;
		this.diagnosticosByFkEgresosDiagnosticoC = diagnosticosByFkEgresosDiagnosticoC;
		this.estadoSalida = estadoSalida;
		this.destinoSalida = destinoSalida;
		this.otroDestinoSalida = otroDestinoSalida;
		this.fechaEgreso = fechaEgreso;
		this.horaEgreso = horaEgreso;
		this.fechaReversionEgreso = fechaReversionEgreso;
		this.horaReversionEgreso = horaReversionEgreso;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.motivoReversion = motivoReversion;
		this.mostrarMotivoRevEpicrisis = mostrarMotivoRevEpicrisis;
		this.esAutomatico = esAutomatico;
		this.resProc = resProc;
		this.acompanadoPor = acompanadoPor;
		this.remitidoA = remitidoA;
		this.placaNro = placaNro;
		this.conductorAmbulancia = conductorAmbulancia;
		this.quienRecibe = quienRecibe;
		this.observaciones = observaciones;
	}

	public int getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}

	public Personas getPersonas() {
		return this.personas;
	}

	public void setPersonas(Personas personas) {
		this.personas = personas;
	}

	public Diagnosticos getDiagnosticosByFkEgresosDiagPrinc() {
		return this.diagnosticosByFkEgresosDiagPrinc;
	}

	public void setDiagnosticosByFkEgresosDiagPrinc(
			Diagnosticos diagnosticosByFkEgresosDiagPrinc) {
		this.diagnosticosByFkEgresosDiagPrinc = diagnosticosByFkEgresosDiagPrinc;
	}

	public Diagnosticos getDiagnosticosByFkEgresosDiagnosticor1() {
		return this.diagnosticosByFkEgresosDiagnosticor1;
	}

	public void setDiagnosticosByFkEgresosDiagnosticor1(
			Diagnosticos diagnosticosByFkEgresosDiagnosticor1) {
		this.diagnosticosByFkEgresosDiagnosticor1 = diagnosticosByFkEgresosDiagnosticor1;
	}

	public Diagnosticos getDiagnosticosByFkEgresosDiagnosticor3() {
		return this.diagnosticosByFkEgresosDiagnosticor3;
	}

	public void setDiagnosticosByFkEgresosDiagnosticor3(
			Diagnosticos diagnosticosByFkEgresosDiagnosticor3) {
		this.diagnosticosByFkEgresosDiagnosticor3 = diagnosticosByFkEgresosDiagnosticor3;
	}

	public Diagnosticos getDiagnosticosByFkEgresosDiagnosticor2() {
		return this.diagnosticosByFkEgresosDiagnosticor2;
	}

	public void setDiagnosticosByFkEgresosDiagnosticor2(
			Diagnosticos diagnosticosByFkEgresosDiagnosticor2) {
		this.diagnosticosByFkEgresosDiagnosticor2 = diagnosticosByFkEgresosDiagnosticor2;
	}

	public TipoMonitoreo getTipoMonitoreo() {
		return this.tipoMonitoreo;
	}

	public void setTipoMonitoreo(TipoMonitoreo tipoMonitoreo) {
		this.tipoMonitoreo = tipoMonitoreo;
	}

	public Evoluciones getEvolucionesByEvolucionGeneroMotivoRev() {
		return this.evolucionesByEvolucionGeneroMotivoRev;
	}

	public void setEvolucionesByEvolucionGeneroMotivoRev(
			Evoluciones evolucionesByEvolucionGeneroMotivoRev) {
		this.evolucionesByEvolucionGeneroMotivoRev = evolucionesByEvolucionGeneroMotivoRev;
	}

	public Medicos getMedicos() {
		return this.medicos;
	}

	public void setMedicos(Medicos medicos) {
		this.medicos = medicos;
	}

	public Diagnosticos getDiagnosticosByFkEgresosDiagMuerte() {
		return this.diagnosticosByFkEgresosDiagMuerte;
	}

	public void setDiagnosticosByFkEgresosDiagMuerte(
			Diagnosticos diagnosticosByFkEgresosDiagMuerte) {
		this.diagnosticosByFkEgresosDiagMuerte = diagnosticosByFkEgresosDiagMuerte;
	}

	public Evoluciones getEvolucionesByEvolucion() {
		return this.evolucionesByEvolucion;
	}

	public void setEvolucionesByEvolucion(Evoluciones evolucionesByEvolucion) {
		this.evolucionesByEvolucion = evolucionesByEvolucion;
	}

	public Cuentas getCuentas() {
		return this.cuentas;
	}

	public void setCuentas(Cuentas cuentas) {
		this.cuentas = cuentas;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public CausasExternas getCausasExternas() {
		return this.causasExternas;
	}

	public void setCausasExternas(CausasExternas causasExternas) {
		this.causasExternas = causasExternas;
	}

	public Diagnosticos getDiagnosticosByFkEgresosDiagnosticoC() {
		return this.diagnosticosByFkEgresosDiagnosticoC;
	}

	public void setDiagnosticosByFkEgresosDiagnosticoC(
			Diagnosticos diagnosticosByFkEgresosDiagnosticoC) {
		this.diagnosticosByFkEgresosDiagnosticoC = diagnosticosByFkEgresosDiagnosticoC;
	}

	public Boolean getEstadoSalida() {
		return this.estadoSalida;
	}

	public void setEstadoSalida(Boolean estadoSalida) {
		this.estadoSalida = estadoSalida;
	}

	public Integer getDestinoSalida() {
		return this.destinoSalida;
	}

	public void setDestinoSalida(Integer destinoSalida) {
		this.destinoSalida = destinoSalida;
	}

	public String getOtroDestinoSalida() {
		return this.otroDestinoSalida;
	}

	public void setOtroDestinoSalida(String otroDestinoSalida) {
		this.otroDestinoSalida = otroDestinoSalida;
	}

	public Date getFechaEgreso() {
		return this.fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public String getHoraEgreso() {
		return this.horaEgreso;
	}

	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

	public Date getFechaReversionEgreso() {
		return this.fechaReversionEgreso;
	}

	public void setFechaReversionEgreso(Date fechaReversionEgreso) {
		this.fechaReversionEgreso = fechaReversionEgreso;
	}

	public String getHoraReversionEgreso() {
		return this.horaReversionEgreso;
	}

	public void setHoraReversionEgreso(String horaReversionEgreso) {
		this.horaReversionEgreso = horaReversionEgreso;
	}

	public Date getFechaGrabacion() {
		return this.fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return this.horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public String getMotivoReversion() {
		return this.motivoReversion;
	}

	public void setMotivoReversion(String motivoReversion) {
		this.motivoReversion = motivoReversion;
	}

	public Boolean getMostrarMotivoRevEpicrisis() {
		return this.mostrarMotivoRevEpicrisis;
	}

	public void setMostrarMotivoRevEpicrisis(Boolean mostrarMotivoRevEpicrisis) {
		this.mostrarMotivoRevEpicrisis = mostrarMotivoRevEpicrisis;
	}

	public boolean isEsAutomatico() {
		return this.esAutomatico;
	}

	public void setEsAutomatico(boolean esAutomatico) {
		this.esAutomatico = esAutomatico;
	}

	public Long getResProc() {
		return this.resProc;
	}

	public void setResProc(Long resProc) {
		this.resProc = resProc;
	}

	public String getAcompanadoPor() {
		return this.acompanadoPor;
	}

	public void setAcompanadoPor(String acompanadoPor) {
		this.acompanadoPor = acompanadoPor;
	}

	public String getRemitidoA() {
		return this.remitidoA;
	}

	public void setRemitidoA(String remitidoA) {
		this.remitidoA = remitidoA;
	}

	public String getPlacaNro() {
		return this.placaNro;
	}

	public void setPlacaNro(String placaNro) {
		this.placaNro = placaNro;
	}

	public String getConductorAmbulancia() {
		return this.conductorAmbulancia;
	}

	public void setConductorAmbulancia(String conductorAmbulancia) {
		this.conductorAmbulancia = conductorAmbulancia;
	}

	public String getQuienRecibe() {
		return this.quienRecibe;
	}

	public void setQuienRecibe(String quienRecibe) {
		this.quienRecibe = quienRecibe;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return valor de evolucionGeneroMotivoRev
	 */
	public Integer getEvolucionGeneroMotivoRev() {
		return evolucionGeneroMotivoRev;
	}

	/**
	 * @param evolucionGeneroMotivoRev el evolucionGeneroMotivoRev para asignar
	 */
	public void setEvolucionGeneroMotivoRev(Integer evolucionGeneroMotivoRev) {
		this.evolucionGeneroMotivoRev = evolucionGeneroMotivoRev;
	}

	/**
	 * @return valor de causaExterna
	 */
	public Integer getCausaExterna() {
		return causaExterna;
	}

	/**
	 * @param causaExterna el causaExterna para asignar
	 */
	public void setCausaExterna(Integer causaExterna) {
		this.causaExterna = causaExterna;
	}

}
