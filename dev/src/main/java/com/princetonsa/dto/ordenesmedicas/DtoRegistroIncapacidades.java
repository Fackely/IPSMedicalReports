package com.princetonsa.dto.ordenesmedicas;

import java.io.Serializable;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;

import util.ConstantesBD;
import util.UtilidadFecha;


public class DtoRegistroIncapacidades implements Serializable
{
	private int codigoPk;
	
	private int codigoPaciente;
	
	private int ingreso;
	
	private String fechaInicioIncapacidad;
	
	private String fechaFinIncapacidad;
	
	private String diasIncapacidad;
	
	private int tipoIncapacidad;
	
	private String prorroga;
	
	private int codigoMedico;
	
	private int especialidad;
	
	private String estado;
	
	private String motivoAnulacion;
	
	private String consecutivo;
	
	private String anioConsecutivo;
	
	private String observaciones;
	
	private int valoracion;
	
	private int evolucion;
	
	private String acronimoDiagnostico;
	
	private int tipoCie;
	
	private String activo;
	
	private DtoInfoFechaUsuario grabacion;
	
	private String diagnostico;
	
	/**
	 * 
	 */
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.fechaInicioIncapacidad = UtilidadFecha.getFechaActual();
		this.fechaFinIncapacidad = new String("");
		this.diasIncapacidad = new String("");
		this.tipoIncapacidad = ConstantesBD.codigoNuncaValido;
		this.prorroga = new String(ConstantesBD.acronimoNo);
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.especialidad = ConstantesBD.codigoNuncaValido;
		this.estado = new String("");
		this.motivoAnulacion = new String("");
		this.consecutivo = new String("");
		this.anioConsecutivo = new String("");
		this.observaciones = new String("");
		this.valoracion = ConstantesBD.codigoNuncaValido;
		this.evolucion = ConstantesBD.codigoNuncaValido;
		this.acronimoDiagnostico = new String("");
		this.tipoCie = ConstantesBD.codigoNuncaValido;
		this.grabacion = new DtoInfoFechaUsuario();
		this.activo = ConstantesBD.acronimoSi;
		this.diagnostico =  "";
	}
	
	public void set(DtoRegistroIncapacidades incapacidad)
	{
		this.codigoPk = incapacidad.getCodigoPk();
		this.codigoPaciente = incapacidad.getCodigoPaciente();
		this.ingreso = incapacidad.getIngreso();
		this.fechaInicioIncapacidad = incapacidad.getFechaInicioIncapacidad();
		this.fechaFinIncapacidad = incapacidad.getFechaFinIncapacidad();
		this.diasIncapacidad = incapacidad.getDiasIncapacidad();
		this.tipoIncapacidad = incapacidad.getTipoIncapacidad();
		this.prorroga = incapacidad.getProrroga();
		this.codigoMedico = incapacidad.getCodigoMedico();
		this.especialidad = incapacidad.getEspecialidad();
		this.estado =  incapacidad.getEstado();
		this.motivoAnulacion = incapacidad.getMotivoAnulacion();
		this.consecutivo = incapacidad.getConsecutivo();
		this.anioConsecutivo = incapacidad.getAnioConsecutivo();
		this.observaciones = incapacidad.getObservaciones();
		this.valoracion = incapacidad.getValoracion();
		this.evolucion = incapacidad.getEvolucion();
		this.acronimoDiagnostico = incapacidad.getAcronimoDiagnostico();
		this.tipoCie = incapacidad.getTipoCie();
		this.grabacion = incapacidad.getGrabacion();
		this.activo = incapacidad.getActivo();
		this.diagnostico = incapacidad.getDiagnostico();
	}
	
	
	/**
	 * 
	 */
	public DtoRegistroIncapacidades()
	{
		this.clean();
	}
	
	public DtoRegistroIncapacidades(DtoRegistroIncapacidades incapacidad)
	{
		this.set(incapacidad);
	}


	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}


	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	/**
	 * @return the fechaInicioIncapacidad
	 */
	public String getFechaInicioIncapacidad() {
		return fechaInicioIncapacidad;
	}


	/**
	 * @param fechaInicioIncapacidad the fechaInicioIncapacidad to set
	 */
	public void setFechaInicioIncapacidad(String fechaInicioIncapacidad) {
		this.fechaInicioIncapacidad = fechaInicioIncapacidad;
	}


	/**
	 * @return the fechaFinIncapacidad
	 */
	public String getFechaFinIncapacidad() {
		return fechaFinIncapacidad;
	}


	/**
	 * @param fechaFinIncapacidad the fechaFinIncapacidad to set
	 */
	public void setFechaFinIncapacidad(String fechaFinIncapacidad) {
		this.fechaFinIncapacidad = fechaFinIncapacidad;
	}


	/**
	 * @return the tipoIncapacidad
	 */
	public int getTipoIncapacidad() {
		return tipoIncapacidad;
	}


	/**
	 * @param tipoIncapacidad the tipoIncapacidad to set
	 */
	public void setTipoIncapacidad(int tipoIncapacidad) {
		this.tipoIncapacidad = tipoIncapacidad;
	}


	/**
	 * @return the prorroga
	 */
	public String getProrroga() {
		return prorroga;
	}


	/**
	 * @param prorroga the prorroga to set
	 */
	public void setProrroga(String prorroga) {
		this.prorroga = prorroga;
	}


	/**
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}


	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}


	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}


	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the motivoAnulacion
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}


	/**
	 * @param motivoAnulacion the motivoAnulacion to set
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}


	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}


	/**
	 * @return the anioConsecutivo
	 */
	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}


	/**
	 * @param anioConsecutivo the anioConsecutivo to set
	 */
	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}


	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}


	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	/**
	 * @return the valoracion
	 */
	public int getValoracion() {
		return valoracion;
	}


	/**
	 * @param valoracion the valoracion to set
	 */
	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}


	/**
	 * @return the evolucion
	 */
	public int getEvolucion() {
		return evolucion;
	}


	/**
	 * @param evolucion the evolucion to set
	 */
	public void setEvolucion(int evolucion) {
		this.evolucion = evolucion;
	}


	/**
	 * @return the acronimoDiagnostico
	 */
	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}


	/**
	 * @param acronimoDiagnostico the acronimoDiagnostico to set
	 */
	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}


	/**
	 * @return the tipoCie
	 */
	public int getTipoCie() {
		return tipoCie;
	}


	/**
	 * @param tipoCie the tipoCie to set
	 */
	public void setTipoCie(int tipoCie) {
		this.tipoCie = tipoCie;
	}


	/**
	 * @return the diasIncapacidad
	 */
	public String getDiasIncapacidad() {
		return diasIncapacidad;
	}


	/**
	 * @param diasIncapacidad the diasIncapacidad to set
	 */
	public void setDiasIncapacidad(String diasIncapacidad) {
		this.diasIncapacidad = diasIncapacidad;
	}

	/**
	 * @return the grabacion
	 */
	public DtoInfoFechaUsuario getGrabacion() {
		return grabacion;
	}

	/**
	 * @param grabacion the grabacion to set
	 */
	public void setGrabacion(DtoInfoFechaUsuario grabacion) {
		this.grabacion = grabacion;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the diagnostico
	 */
	public String getDiagnostico() {
		return diagnostico;
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
}
