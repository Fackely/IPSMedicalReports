package com.princetonsa.dto.consultaExterna;

import java.io.Serializable;
import java.util.HashMap;

import util.ConstantesBD;

public class DtoMultasCitas implements Serializable{

	
	private int codigoMulta;
	private String estadoMulta;
	private String usuario;
	private int codConvenioCita;
	private String convenioCita;
	private int cita;
	private int codMotivo_Cond_anul;
	private String descripcionMotivo_Cond_Anul;
	private int valor;
	private String valorString;
	private String observaciones;
	private int unidadAgenda;
	private String descripcionUnidadAgenda;
	private String fechaMulta;
	private String horaMulta;
	private String fechaCita;
	private String horaCita;
	private String estadoCita;
	private String profesional;
	private int codProfesional;
	private int codCentroAtencion;
	private String nombreCentroAtencion;
	private boolean condonar;
	private boolean anular;
	private HashMap serviciosCita;
	private String  idPaciente;
    private String nombrePaciente;
    private boolean seleccionado;

	
	public DtoMultasCitas()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoMulta=ConstantesBD.codigoNuncaValido;
		this.valor=ConstantesBD.codigoNuncaValido;
		this.unidadAgenda=ConstantesBD.codigoNuncaValido;
		this.cita=ConstantesBD.codigoNuncaValido;
		this.codMotivo_Cond_anul=ConstantesBD.codigoNuncaValido;
		this.codProfesional=ConstantesBD.codigoNuncaValido;
		this.codCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.codConvenioCita=ConstantesBD.codigoNuncaValido;
		this.convenioCita=new String("");
		this.estadoMulta=new String("");
		this.usuario=new String("");
		this.descripcionMotivo_Cond_Anul=new String("");
		this.descripcionUnidadAgenda=new String("");
		this.observaciones=new String("");
		this.fechaMulta=new String("");
		this.horaMulta=new String("");
		this.fechaCita=new String("");
		this.horaCita=new String("");
		this.profesional=new String("");
		this.estadoCita=new String("");
		this.serviciosCita=new HashMap();
		this.serviciosCita.put("numRegistros","0");
		this.nombreCentroAtencion=new String("");
		this.nombrePaciente=new String("");
		this.idPaciente=new String("");
		this.condonar=false;
		this.anular=false;
		this.valorString = "";
		this.seleccionado = false;
	}

	/**
	 * @return the seleccionado
	 */
	public boolean isSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado the seleccionado to set
	 */
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public int getCodigoMulta() {
		return codigoMulta;
	}

	public void setCodigoMulta(int codigoMulta) {
		this.codigoMulta = codigoMulta;
	}

	public String getEstadoMulta() {
		return estadoMulta;
	}

	public void setEstadoMulta(String estadoMulta) {
		this.estadoMulta = estadoMulta;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getCita() {
		return cita;
	}

	public void setCita(int cita) {
		this.cita = cita;
	}

	public int getCodMotivo_Cond_anul() {
		return codMotivo_Cond_anul;
	}

	public void setCodMotivo_Cond_anul(int codMotivo_Cond_anul) {
		this.codMotivo_Cond_anul = codMotivo_Cond_anul;
	}

	public String getDescripcionMotivo_Cond_Anul() {
		return descripcionMotivo_Cond_Anul;
	}

	public void setDescripcionMotivo_Cond_Anul(String descripcionMotivo_Cond_Anul) {
		this.descripcionMotivo_Cond_Anul = descripcionMotivo_Cond_Anul;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getUnidadAgenda() {
		return unidadAgenda;
	}

	public void setUnidadAgenda(int unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	public String getDescripcionUnidadAgenda() {
		return descripcionUnidadAgenda;
	}

	public void setDescripcionUnidadAgenda(String descripcionUnidadAgenda) {
		this.descripcionUnidadAgenda = descripcionUnidadAgenda;
	}

	public String getFechaMulta() {
		return fechaMulta;
	}

	public void setFechaMulta(String fechaMulta) {
		this.fechaMulta = fechaMulta;
	}

	public String getHoraMulta() {
		return horaMulta;
	}

	public void setHoraMulta(String horaMulta) {
		this.horaMulta = horaMulta;
	}

	public String getEstadoCita() {
		return estadoCita;
	}

	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	public String getProfesional() {
		return profesional;
	}

	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}

	public boolean isCondonar() {
		return condonar;
	}

	public void setCondonar(boolean condonar) {
		this.condonar = condonar;
	}

	public boolean isAnular() {
		return anular;
	}

	public void setAnular(boolean anular) {
		this.anular = anular;
	}

	public String getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(String fechaCita) {
		this.fechaCita = fechaCita;
	}

	public String getHoraCita() {
		return horaCita;
	}

	public void setHoraCita(String horaCita) {
		this.horaCita = horaCita;
	}

	public int getCodProfesional() {
		return codProfesional;
	}

	public void setCodProfesional(int codProfesional) {
		this.codProfesional = codProfesional;
	}

	public int getCodCentroAtencion() {
		return codCentroAtencion;
	}

	public void setCodCentroAtencion(int codCentroAtencion) {
		this.codCentroAtencion = codCentroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public int getCodConvenioCita() {
		return codConvenioCita;
	}

	public void setCodConvenioCita(int codConvenioCita) {
		this.codConvenioCita = codConvenioCita;
	}

	public String getConvenioCita() {
		return convenioCita;
	}

	public void setConvenioCita(String convenioCita) {
		this.convenioCita = convenioCita;
	}

	public HashMap getServiciosCita() {
		return serviciosCita;
	}

	public void setServiciosCita(HashMap serviciosCita) {
		this.serviciosCita = serviciosCita;
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

	/**
	 * @return the valorString
	 */
	public String getValorString() {
		return valorString;
	}

	/**
	 * @param valorString the valorString to set
	 */
	public void setValorString(String valorString) {
		this.valorString = valorString;
	}
	
	
}
