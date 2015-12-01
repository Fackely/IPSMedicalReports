package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoAutorizacionSaldoMora implements Serializable
{
	private double codigoPk;
	private String fecha;
	private String hora;
	private int viaIngreso;
	private String tiposPaciente;
	private String horasVigencia;
	private String personaAutoriza;
	private double motivo;
	private String observaciones;
	private int codigoPaciente;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private int ingreso;
	private String fechaIngreso;
	private String horaIngreso;
	private int institucion;
	private int centroAtencion;
	
	//Elementos Adicionales para la consulta
	private String fechaFinal;
	private String tipoId;
	private String idPaciente;
	private String nombrePaciente;
	private String nombreViaIngreso;
	private String descripcionTipoPaciente;
	private String descripcionMotivo;
	
	public void reset()
	{
		this.codigoPk=0;
		this.fecha="";
		this.hora="";
		this.viaIngreso=ConstantesBD.codigoNuncaValido;
		this.tiposPaciente="";
		this.horasVigencia="";
		this.personaAutoriza="";
		this.motivo=ConstantesBD.codigoNuncaValidoDouble;
		this.observaciones="";
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;;
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.fechaIngreso="";
		this.institucion=ConstantesBD.codigoNuncaValido;;
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.fechaFinal="";
		this.tipoId="";
		this.idPaciente="";
		this.nombrePaciente="";
		this.nombreViaIngreso="";
		this.descripcionTipoPaciente="";
		this.descripcionMotivo="";
	}

	public DtoAutorizacionSaldoMora()
	{
		this.reset();
	}
	
	public double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public int getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public String getTiposPaciente() {
		return tiposPaciente;
	}

	public void setTiposPaciente(String tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	public String getHorasVigencia() {
		return horasVigencia;
	}

	public void setHorasVigencia(String horasVigencia) {
		this.horasVigencia = horasVigencia;
	}

	public String getPersonaAutoriza() {
		return personaAutoriza;
	}

	public void setPersonaAutoriza(String personaAutoriza) {
		this.personaAutoriza = personaAutoriza;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getHoraIngreso() {
		return horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public double getMotivo() {
		return motivo;
	}

	public void setMotivo(double motivo) {
		this.motivo = motivo;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getTipoId() {
		return tipoId;
	}

	public void setTipoId(String tiposId) {
		this.tipoId = tiposId;
	}

	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getNombrePaciente() {
		return nombrePaciente;
	}

	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	public String getDescripcionTipoPaciente() {
		return descripcionTipoPaciente;
	}

	public void setDescripcionTipoPaciente(String descripcionTipoPaciente) {
		this.descripcionTipoPaciente = descripcionTipoPaciente;
	}

	public String getDescripcionMotivo() {
		return descripcionMotivo;
	}

	public void setDescripcionMotivo(String descripcionMotivo) {
		this.descripcionMotivo = descripcionMotivo;
	}	
	
	
}