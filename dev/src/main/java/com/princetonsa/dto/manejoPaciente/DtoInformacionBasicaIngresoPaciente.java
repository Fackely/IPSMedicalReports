package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Usuarios;

/**
* Clase qué contiene las variables basicas para la creacion de una nueva cuenta a un paciente
* @author Cristhian Murillo
*/
public class DtoInformacionBasicaIngresoPaciente implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private String acronimoSi;
	private String acronimoNo;
	private int codigoTipoPersonaNatural;
	private int codigoTipoPersonaJuridica;
	private int codigoCuentaActiva;
	private int codigoCuentaCerrada;
	private String acronimoGrupoPoblacionalOtros;
	private int codigoInstitucion;
	private int tipoMovimientoSaldoInicialMigrado;
	private int tipoMovimientoAjusteMigracionPositivo;
	private int tipoMovimientoAjusteMigracionNegativo;
	/** * Nombre para manejar el consecutivo para ingresos del paciente  */
	private String nombreConsecutivoIngresos;
	private String estadoIngresoAbierto;
	private String estadoIngresoCerrado;
	private String tipoIngresoOdontologia;
	private String aronimoTipoPacienteAmbulatorios;
	private int origenAdmisionConsultaExterna;
	private int viaIngresoConsultaExterna;
	private int naturalezaPacienteNinguno;
	private int contanteMontoCobroCien;
	private int contanteMontoCobroCero;
	private Character acronimoEstadoCivilDesconocido;
	private Integer codigoTipoSangreDesconocido;
	private String acronimoEstadoPlanTratamientoActivo;
	private String acronimoEstadoPlanTratamientoTerminado;
	private String acronimoEstadoPresupuestoContratado;
	private String acronimoEstadoPresupuestoTerminado;
	private String acronimoIndicativoPlanTratamiento;
	private String acronimoSeccionHallazgoOtro;
	private String acronimoSeccionHallazgoBoca;
	private String acronimoSeccionHallazgoPlan;
	private Object acronimoAplicaABoca;
	private String acronimoIndicativoPresupuestos;
	private int codigoNuncaValido;
	
	private CentroAtencion CentroAtencion;
	private Usuarios usuario;
	private Instituciones institucion;
	private Date fechaActual;
	private String horaActual;
	private Pacientes paciente;
	private Ingresos ingreso;
	private CentrosCosto areaCentroCosto;
	private CentrosCosto ccostoCentroCosto;
	private Convenios convenio;
	private Contratos contrato;
	private int prioridad;


	
	/**
	* Constructor que inicia con los valores por defecto
	*
	*/
	public DtoInformacionBasicaIngresoPaciente()
	{
		this.acronimoSi="S";
		this.acronimoNo="N";
		this.codigoTipoPersonaNatural=1;
		this.codigoTipoPersonaJuridica=2;
		this.codigoCuentaActiva = 0;
		this.codigoCuentaCerrada = 4;
		this.acronimoGrupoPoblacionalOtros="OGPO";
		this.codigoInstitucion=2;
		this.tipoMovimientoSaldoInicialMigrado=16;
		this.tipoMovimientoAjusteMigracionPositivo=17;
		this.tipoMovimientoAjusteMigracionNegativo=18;
		this.nombreConsecutivoIngresos="consecutivo_ingreso";
		this.estadoIngresoAbierto="ABI";
		this.estadoIngresoCerrado="CER";
		this.tipoIngresoOdontologia="ODON";
		this.aronimoTipoPacienteAmbulatorios="A";
		this.origenAdmisionConsultaExterna=2;
		this.viaIngresoConsultaExterna=4;
		this.naturalezaPacienteNinguno=0;
		this.contanteMontoCobroCien=-2;
		this.contanteMontoCobroCero=ConstantesBD.codigoNuncaValido;
		this.acronimoEstadoCivilDesconocido='D';
		this.codigoTipoSangreDesconocido=9;
		this.acronimoEstadoPlanTratamientoActivo="ACT";
		this.acronimoEstadoPlanTratamientoTerminado="TER";
		this.acronimoEstadoPresupuestoContratado="CCOT";
		this.acronimoEstadoPresupuestoTerminado="CTER";
		this.acronimoIndicativoPlanTratamiento="INI";
		this.acronimoSeccionHallazgoOtro="AOTR";
		this.acronimoSeccionHallazgoBoca="BOCA";
		this.acronimoSeccionHallazgoPlan="DET";
		this.acronimoAplicaABoca="BOC";
		this.acronimoIndicativoPresupuestos="PRESU";
		this.codigoNuncaValido=ConstantesBD.codigoNuncaValido;
		
		
		this.CentroAtencion = new CentroAtencion();
		this.usuario 		= new Usuarios();
		this.institucion 	= new Instituciones();
		this.fechaActual	= UtilidadFecha.getFechaActualTipoBD();
		this.horaActual		= UtilidadFecha.getHoraActual();
		this.paciente		= new Pacientes();
		this.ingreso		= new Ingresos();
		this.areaCentroCosto 	= new CentrosCosto();
		this.ccostoCentroCosto	= new CentrosCosto();
		this.convenio		= new Convenios();
		this.contrato		= new Contratos();
		this.prioridad		= 1;
		
	}



	public String getAcronimoSi() {
		return acronimoSi;
	}



	public void setAcronimoSi(String acronimoSi) {
		this.acronimoSi = acronimoSi;
	}



	public String getAcronimoNo() {
		return acronimoNo;
	}



	public void setAcronimoNo(String acronimoNo) {
		this.acronimoNo = acronimoNo;
	}



	public int getCodigoTipoPersonaNatural() {
		return codigoTipoPersonaNatural;
	}



	public void setCodigoTipoPersonaNatural(int codigoTipoPersonaNatural) {
		this.codigoTipoPersonaNatural = codigoTipoPersonaNatural;
	}



	public int getCodigoTipoPersonaJuridica() {
		return codigoTipoPersonaJuridica;
	}



	public void setCodigoTipoPersonaJuridica(int codigoTipoPersonaJuridica) {
		this.codigoTipoPersonaJuridica = codigoTipoPersonaJuridica;
	}



	public int getCodigoCuentaActiva() {
		return codigoCuentaActiva;
	}



	public void setCodigoCuentaActiva(int codigoCuentaActiva) {
		this.codigoCuentaActiva = codigoCuentaActiva;
	}



	public int getCodigoCuentaCerrada() {
		return codigoCuentaCerrada;
	}



	public void setCodigoCuentaCerrada(int codigoCuentaCerrada) {
		this.codigoCuentaCerrada = codigoCuentaCerrada;
	}



	public String getAcronimoGrupoPoblacionalOtros() {
		return acronimoGrupoPoblacionalOtros;
	}



	public void setAcronimoGrupoPoblacionalOtros(
			String acronimoGrupoPoblacionalOtros) {
		this.acronimoGrupoPoblacionalOtros = acronimoGrupoPoblacionalOtros;
	}



	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}



	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}



	public int getTipoMovimientoSaldoInicialMigrado() {
		return tipoMovimientoSaldoInicialMigrado;
	}



	public void setTipoMovimientoSaldoInicialMigrado(
			int tipoMovimientoSaldoInicialMigrado) {
		this.tipoMovimientoSaldoInicialMigrado = tipoMovimientoSaldoInicialMigrado;
	}



	public int getTipoMovimientoAjusteMigracionPositivo() {
		return tipoMovimientoAjusteMigracionPositivo;
	}



	public void setTipoMovimientoAjusteMigracionPositivo(
			int tipoMovimientoAjusteMigracionPositivo) {
		this.tipoMovimientoAjusteMigracionPositivo = tipoMovimientoAjusteMigracionPositivo;
	}



	public int getTipoMovimientoAjusteMigracionNegativo() {
		return tipoMovimientoAjusteMigracionNegativo;
	}



	public void setTipoMovimientoAjusteMigracionNegativo(
			int tipoMovimientoAjusteMigracionNegativo) {
		this.tipoMovimientoAjusteMigracionNegativo = tipoMovimientoAjusteMigracionNegativo;
	}



	public String getNombreConsecutivoIngresos() {
		return nombreConsecutivoIngresos;
	}



	public void setNombreConsecutivoIngresos(String nombreConsecutivoIngresos) {
		this.nombreConsecutivoIngresos = nombreConsecutivoIngresos;
	}



	public String getEstadoIngresoAbierto() {
		return estadoIngresoAbierto;
	}



	public void setEstadoIngresoAbierto(String estadoIngresoAbierto) {
		this.estadoIngresoAbierto = estadoIngresoAbierto;
	}



	public String getEstadoIngresoCerrado() {
		return estadoIngresoCerrado;
	}



	public void setEstadoIngresoCerrado(String estadoIngresoCerrado) {
		this.estadoIngresoCerrado = estadoIngresoCerrado;
	}



	public String getTipoIngresoOdontologia() {
		return tipoIngresoOdontologia;
	}



	public void setTipoIngresoOdontologia(String tipoIngresoOdontologia) {
		this.tipoIngresoOdontologia = tipoIngresoOdontologia;
	}



	public String getAronimoTipoPacienteAmbulatorios() {
		return aronimoTipoPacienteAmbulatorios;
	}



	public void setAronimoTipoPacienteAmbulatorios(
			String aronimoTipoPacienteAmbulatorios) {
		this.aronimoTipoPacienteAmbulatorios = aronimoTipoPacienteAmbulatorios;
	}



	public int getOrigenAdmisionConsultaExterna() {
		return origenAdmisionConsultaExterna;
	}



	public void setOrigenAdmisionConsultaExterna(int origenAdmisionConsultaExterna) {
		this.origenAdmisionConsultaExterna = origenAdmisionConsultaExterna;
	}



	public int getViaIngresoConsultaExterna() {
		return viaIngresoConsultaExterna;
	}



	public void setViaIngresoConsultaExterna(int viaIngresoConsultaExterna) {
		this.viaIngresoConsultaExterna = viaIngresoConsultaExterna;
	}



	public int getNaturalezaPacienteNinguno() {
		return naturalezaPacienteNinguno;
	}



	public void setNaturalezaPacienteNinguno(int naturalezaPacienteNinguno) {
		this.naturalezaPacienteNinguno = naturalezaPacienteNinguno;
	}



	public int getContanteMontoCobroCien() {
		return contanteMontoCobroCien;
	}



	public void setContanteMontoCobroCien(int contanteMontoCobroCien) {
		this.contanteMontoCobroCien = contanteMontoCobroCien;
	}



	public int getContanteMontoCobroCero() {
		return contanteMontoCobroCero;
	}



	public void setContanteMontoCobroCero(int contanteMontoCobroCero) {
		this.contanteMontoCobroCero = contanteMontoCobroCero;
	}



	public Character getAcronimoEstadoCivilDesconocido() {
		return acronimoEstadoCivilDesconocido;
	}



	public void setAcronimoEstadoCivilDesconocido(
			Character acronimoEstadoCivilDesconocido) {
		this.acronimoEstadoCivilDesconocido = acronimoEstadoCivilDesconocido;
	}



	public Integer getCodigoTipoSangreDesconocido() {
		return codigoTipoSangreDesconocido;
	}



	public void setCodigoTipoSangreDesconocido(Integer codigoTipoSangreDesconocido) {
		this.codigoTipoSangreDesconocido = codigoTipoSangreDesconocido;
	}



	public String getAcronimoEstadoPlanTratamientoActivo() {
		return acronimoEstadoPlanTratamientoActivo;
	}



	public void setAcronimoEstadoPlanTratamientoActivo(
			String acronimoEstadoPlanTratamientoActivo) {
		this.acronimoEstadoPlanTratamientoActivo = acronimoEstadoPlanTratamientoActivo;
	}



	public String getAcronimoEstadoPlanTratamientoTerminado() {
		return acronimoEstadoPlanTratamientoTerminado;
	}



	public void setAcronimoEstadoPlanTratamientoTerminado(
			String acronimoEstadoPlanTratamientoTerminado) {
		this.acronimoEstadoPlanTratamientoTerminado = acronimoEstadoPlanTratamientoTerminado;
	}



	public String getAcronimoEstadoPresupuestoContratado() {
		return acronimoEstadoPresupuestoContratado;
	}



	public void setAcronimoEstadoPresupuestoContratado(
			String acronimoEstadoPresupuestoContratado) {
		this.acronimoEstadoPresupuestoContratado = acronimoEstadoPresupuestoContratado;
	}



	public String getAcronimoEstadoPresupuestoTerminado() {
		return acronimoEstadoPresupuestoTerminado;
	}



	public void setAcronimoEstadoPresupuestoTerminado(
			String acronimoEstadoPresupuestoTerminado) {
		this.acronimoEstadoPresupuestoTerminado = acronimoEstadoPresupuestoTerminado;
	}



	public String getAcronimoIndicativoPlanTratamiento() {
		return acronimoIndicativoPlanTratamiento;
	}



	public void setAcronimoIndicativoPlanTratamiento(
			String acronimoIndicativoPlanTratamiento) {
		this.acronimoIndicativoPlanTratamiento = acronimoIndicativoPlanTratamiento;
	}



	public String getAcronimoSeccionHallazgoOtro() {
		return acronimoSeccionHallazgoOtro;
	}



	public void setAcronimoSeccionHallazgoOtro(String acronimoSeccionHallazgoOtro) {
		this.acronimoSeccionHallazgoOtro = acronimoSeccionHallazgoOtro;
	}



	public String getAcronimoSeccionHallazgoBoca() {
		return acronimoSeccionHallazgoBoca;
	}



	public void setAcronimoSeccionHallazgoBoca(String acronimoSeccionHallazgoBoca) {
		this.acronimoSeccionHallazgoBoca = acronimoSeccionHallazgoBoca;
	}



	public String getAcronimoSeccionHallazgoPlan() {
		return acronimoSeccionHallazgoPlan;
	}



	public void setAcronimoSeccionHallazgoPlan(String acronimoSeccionHallazgoPlan) {
		this.acronimoSeccionHallazgoPlan = acronimoSeccionHallazgoPlan;
	}



	public Object getAcronimoAplicaABoca() {
		return acronimoAplicaABoca;
	}



	public void setAcronimoAplicaABoca(Object acronimoAplicaABoca) {
		this.acronimoAplicaABoca = acronimoAplicaABoca;
	}



	public String getAcronimoIndicativoPresupuestos() {
		return acronimoIndicativoPresupuestos;
	}



	public void setAcronimoIndicativoPresupuestos(
			String acronimoIndicativoPresupuestos) {
		this.acronimoIndicativoPresupuestos = acronimoIndicativoPresupuestos;
	}



	public int getCodigoNuncaValido() {
		return codigoNuncaValido;
	}



	public void setCodigoNuncaValido(int codigoNuncaValido) {
		this.codigoNuncaValido = codigoNuncaValido;
	}



	public CentroAtencion getCentroAtencion() {
		return CentroAtencion;
	}



	public void setCentroAtencion(CentroAtencion centroAtencion) {
		CentroAtencion = centroAtencion;
	}



	public Usuarios getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuarios usuario) {
		this.usuario = usuario;
	}



	public Instituciones getInstitucion() {
		return institucion;
	}



	public void setInstitucion(Instituciones institucion) {
		this.institucion = institucion;
	}



	public Date getFechaActual() {
		return fechaActual;
	}



	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}



	public String getHoraActual() {
		return horaActual;
	}



	public void setHoraActual(String horaActual) {
		this.horaActual = horaActual;
	}



	public Pacientes getPaciente() {
		return paciente;
	}



	public void setPaciente(Pacientes paciente) {
		this.paciente = paciente;
	}



	public Ingresos getIngreso() {
		return ingreso;
	}



	public void setIngreso(Ingresos ingreso) {
		this.ingreso = ingreso;
	}



	public CentrosCosto getAreaCentroCosto() {
		return areaCentroCosto;
	}



	public void setAreaCentroCosto(CentrosCosto areaCentroCosto) {
		this.areaCentroCosto = areaCentroCosto;
	}



	public CentrosCosto getCcostoCentroCosto() {
		return ccostoCentroCosto;
	}



	public void setCcostoCentroCosto(CentrosCosto ccostoCentroCosto) {
		this.ccostoCentroCosto = ccostoCentroCosto;
	}



	public Convenios getConvenio() {
		return convenio;
	}



	public void setConvenio(Convenios convenio) {
		this.convenio = convenio;
	}



	public Contratos getContrato() {
		return contrato;
	}



	public void setContrato(Contratos contrato) {
		this.contrato = contrato;
	}



	public int getPrioridad() {
		return prioridad;
	}



	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}


}


