package com.princetonsa.dto.epicrisis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * 
 * @author wilson
 *
 */
public class DtoCirugiaEpicrisis implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 */
	private String fechaCx;
	
	/**
	 * 
	 */
	private String horaCx;
	
	/**
	 * 
	 */
	private String especialidadOrdena;
	
	/**
	 * 
	 */
	private String centroCostoSolicitado;
	
	/**
	 * 
	 */
	private String fechaSolicitud;
	
	/**
	 * 
	 */
	private String horaSolicitud;
	
	/**
	 * 
	 */
	private String duracionFinalCx;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> diagnosticosPreoperatorio;
	
	/**
	 * 
	 */
	private ArrayList<DtoServiciosCirugiaEpicrisis> servicios;

	/**
	 * 
	 */
	private String descripcionQx;
	
	/**
	 * 
	 */
	private String fechaIngresoSala;
	
	/**
	 * 
	 */
	private String horaIngresoSala;
	
	/**
	 * 
	 */
	private String fechaSalidaSala;
	
	/**
	 * 
	 */
	private String horaSalidaSala;
	
	/**
	 * 
	 */
	private String salidaSalaPaciente;
	
	/**
	 * 
	 */
	private ArrayList<DtoNotasCirugiaEpicrisis> notasEnfermeria= new ArrayList<DtoNotasCirugiaEpicrisis>();
	
	/**
	 * 
	 */
	private ArrayList<DtoNotasCirugiaEpicrisis> notasRecuperacion= new ArrayList<DtoNotasCirugiaEpicrisis>();
	
	/**
	 * 
	 *
	 */
	public DtoCirugiaEpicrisis() 
	{
		this.numeroSolicitud=ConstantesBD.codigoNuncaValido;
		this.fechaCx="";
		this.horaCx="";
		this.centroCostoSolicitado="";
		this.fechaSolicitud="";
		this.horaSolicitud="";
		this.duracionFinalCx="";
		this.diagnosticosPreoperatorio= new HashMap<Object, Object>();
		this.diagnosticosPreoperatorio.put("numRegistros", 0);
		this.servicios= new ArrayList<DtoServiciosCirugiaEpicrisis>();
		this.descripcionQx="";
		this.fechaIngresoSala="";
		this.horaIngresoSala="";
		this.fechaSalidaSala="";
		this.horaSalidaSala="";
		this.salidaSalaPaciente="";
		this.notasEnfermeria=new ArrayList<DtoNotasCirugiaEpicrisis>();
		this.notasRecuperacion=new ArrayList<DtoNotasCirugiaEpicrisis>();
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the centroCostoSolicitado
	 */
	public String getCentroCostoSolicitado() {
		return centroCostoSolicitado;
	}

	/**
	 * @param centroCostoSolicitado the centroCostoSolicitado to set
	 */
	public void setCentroCostoSolicitado(String centroCostoSolicitado) {
		this.centroCostoSolicitado = centroCostoSolicitado;
	}

	/**
	 * @return the diagnosticosPreoperatorio
	 */
	public HashMap<Object, Object> getDiagnosticosPreoperatorio() {
		return diagnosticosPreoperatorio;
	}

	/**
	 * @param diagnosticosPreoperatorio the diagnosticosPreoperatorio to set
	 */
	public void setDiagnosticosPreoperatorio(
			HashMap<Object, Object> diagnosticosPreoperatorio) {
		this.diagnosticosPreoperatorio = diagnosticosPreoperatorio;
	}

	/**
	 * @return the duracionFinalCx
	 */
	public String getDuracionFinalCx() {
		return duracionFinalCx;
	}

	/**
	 * @param duracionFinalCx the duracionFinalCx to set
	 */
	public void setDuracionFinalCx(String duracionFinalCx) {
		this.duracionFinalCx = duracionFinalCx;
	}

	/**
	 * @return the especialidadOrdena
	 */
	public String getEspecialidadOrdena() {
		return especialidadOrdena;
	}

	/**
	 * @param especialidadOrdena the especialidadOrdena to set
	 */
	public void setEspecialidadOrdena(String especialidadOrdena) {
		this.especialidadOrdena = especialidadOrdena;
	}

	/**
	 * @return the fechaCx
	 */
	public String getFechaCx() {
		return fechaCx;
	}

	/**
	 * @param fechaCx the fechaCx to set
	 */
	public void setFechaCx(String fechaCx) {
		this.fechaCx = fechaCx;
	}

	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return the horaCx
	 */
	public String getHoraCx() {
		return horaCx;
	}

	/**
	 * @param horaCx the horaCx to set
	 */
	public void setHoraCx(String horaCx) {
		this.horaCx = horaCx;
	}

	/**
	 * @return the horaSolicitud
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * @param horaSolicitud the horaSolicitud to set
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return the servicios
	 */
	public ArrayList<DtoServiciosCirugiaEpicrisis> getServicios() {
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(ArrayList<DtoServiciosCirugiaEpicrisis> servicios) {
		this.servicios = servicios;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDxPrincipalPreoperatorio()
	{
		String dx="";
		if(!UtilidadTexto.isEmpty(this.getDiagnosticosPreoperatorio().get("numRegistros")+""))
		{	
			for(int w=0; w<Integer.parseInt(this.getDiagnosticosPreoperatorio().get("numRegistros")+""); w++)
			{
				if(UtilidadTexto.getBoolean(this.getDiagnosticosPreoperatorio().get("principal_"+w)+""))
				{
					dx=this.getDiagnosticosPreoperatorio().get("acronimo_"+w)+" "+this.getDiagnosticosPreoperatorio().get("nombrediagnostico_"+w);
				}
			}
		}
		return dx;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDxRelacionadosPreoperatorio()
	{
		String dx="";
		if(!UtilidadTexto.isEmpty(this.getDiagnosticosPreoperatorio().get("numRegistros")+""))
		{	
			for(int w=0; w<Integer.parseInt(this.getDiagnosticosPreoperatorio().get("numRegistros")+""); w++)
			{
				if(!UtilidadTexto.getBoolean(this.getDiagnosticosPreoperatorio().get("principal_"+w)+""))
				{
					dx+=this.getDiagnosticosPreoperatorio().get("acronimo_"+w)+" "+this.getDiagnosticosPreoperatorio().get("nombrediagnostico_"+w)+", ";
				}
			}
		}
		return dx;
	}

	/**
	 * @return the descripcionQx
	 */
	public String getDescripcionQx() {
		return descripcionQx;
	}

	/**
	 * @param descripcionQx the descripcionQx to set
	 */
	public void setDescripcionQx(String descripcionQx) {
		this.descripcionQx = descripcionQx;
	}

	/**
	 * @return the fechaIngresoSala
	 */
	public String getFechaIngresoSala() {
		return fechaIngresoSala;
	}

	/**
	 * @param fechaIngresoSala the fechaIngresoSala to set
	 */
	public void setFechaIngresoSala(String fechaIngresoSala) {
		this.fechaIngresoSala = fechaIngresoSala;
	}

	/**
	 * @return the horaIngresoSala
	 */
	public String getHoraIngresoSala() {
		return horaIngresoSala;
	}

	/**
	 * @param horaIngresoSala the horaIngresoSala to set
	 */
	public void setHoraIngresoSala(String horaIngresoSala) {
		this.horaIngresoSala = horaIngresoSala;
	}

	/**
	 * @return the salidaSalaPaciente
	 */
	public String getSalidaSalaPaciente() {
		return salidaSalaPaciente;
	}

	/**
	 * @param salidaSalaPaciente the salidaSalaPaciente to set
	 */
	public void setSalidaSalaPaciente(String salidaSalaPaciente) {
		this.salidaSalaPaciente = salidaSalaPaciente;
	}

	/**
	 * @return the fechaSalidaSala
	 */
	public String getFechaSalidaSala() {
		return fechaSalidaSala;
	}

	/**
	 * @param fechaSalidaSala the fechaSalidaSala to set
	 */
	public void setFechaSalidaSala(String fechaSalidaSala) {
		this.fechaSalidaSala = fechaSalidaSala;
	}

	/**
	 * @return the horaSalidaSala
	 */
	public String getHoraSalidaSala() {
		return horaSalidaSala;
	}

	/**
	 * @param horaSalidaSala the horaSalidaSala to set
	 */
	public void setHoraSalidaSala(String horaSalidaSala) {
		this.horaSalidaSala = horaSalidaSala;
	}

	/**
	 * @return the notasEnfermeria
	 */
	public ArrayList<DtoNotasCirugiaEpicrisis> getNotasEnfermeria() {
		return notasEnfermeria;
	}

	/**
	 * @param notasEnfermeria the notasEnfermeria to set
	 */
	public void setNotasEnfermeria(
			ArrayList<DtoNotasCirugiaEpicrisis> notasEnfermeria) {
		this.notasEnfermeria = notasEnfermeria;
	}

	/**
	 * @return the notasRecuperacion
	 */
	public ArrayList<DtoNotasCirugiaEpicrisis> getNotasRecuperacion() {
		return notasRecuperacion;
	}

	/**
	 * @param notasRecuperacion the notasRecuperacion to set
	 */
	public void setNotasRecuperacion(
			ArrayList<DtoNotasCirugiaEpicrisis> notasRecuperacion) {
		this.notasRecuperacion = notasRecuperacion;
	}

}
