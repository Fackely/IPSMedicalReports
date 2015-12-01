package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;

import util.InfoDatosInt;

public class DtoAntecedentesOdontologicosAnt implements Serializable{

	private int codPaciente;
	private String profesionalResponsable;
	private String fechaModificacion;
	private String horaModificacion;
	private String observaciones;
	
	private ArrayList habitos;
    private ArrayList habitosOtros;
    private ArrayList traumatismos;
    private ArrayList traumatismosOtros;
    private ArrayList tratamientosPrevios;
	
    private ArrayList<InfoDatosInt> secciones;
	
	
	public DtoAntecedentesOdontologicosAnt()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.profesionalResponsable= new String("");
		this.fechaModificacion= new String("");
	    this.horaModificacion = new String("");
		this.observaciones = new String("");
		this.habitos = null;
	    this.habitosOtros = null;
	    this.traumatismos = null;
	    this.traumatismosOtros = null;
	    this.tratamientosPrevios = null;
	}
	
	/**
	 * @return the profesionalResponsable
	 */
	public String getProfesionalResponsable() {
		return profesionalResponsable;
	}
	/**
	 * @param profesionalResponsable the profesionalResponsable to set
	 */
	public void setProfesionalResponsable(String profesionalResponsable) {
		this.profesionalResponsable = profesionalResponsable;
	}
	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}
	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
		
	}
	
	
	public ActionErrors validate(ActionErrors errores) {
		
		return null;
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
	 * @return the codPaciente
	 */
	public int getCodPaciente() {
		return codPaciente;
	}

	/**
	 * @param codPaciente the codPaciente to set
	 */
	public void setCodPaciente(int codPaciente) {
		this.codPaciente = codPaciente;
	}

	/**
	 * @return the habitos
	 */
	public ArrayList getHabitos() {
		if(habitos == null)
		{
			habitos = new ArrayList();
		}
		return habitos;
	}

	/**
	 * @param habitos the habitos to set
	 */
	public void setHabitos(ArrayList habitos) {
		this.habitos = habitos;
	}

	/**
	 * @return the habitosOtros
	 */
	public ArrayList getHabitosOtros() {
		if(habitosOtros == null)
		{
			habitosOtros = new ArrayList();
		}
		return habitosOtros;
	}

	/**
	 * @param habitosOtros the habitosOtros to set
	 */
	public void setHabitosOtros(ArrayList habitosOtros) {
		this.habitosOtros = habitosOtros;
	}

	/**
	 * @return the traumatismos
	 */
	public ArrayList getTraumatismos() {
		if(traumatismos == null)
		{
			traumatismos = new ArrayList();
		}
		return traumatismos;
	}

	/**
	 * @param traumatismos the traumatismos to set
	 */
	public void setTraumatismos(ArrayList traumatismos) {
		this.traumatismos = traumatismos;
	}

	/**
	 * @return the traumatismosOtros
	 */
	public ArrayList getTraumatismosOtros() {
		if(traumatismosOtros == null)
		{
			traumatismosOtros = new ArrayList();
		}
		return traumatismosOtros;
	}

	/**
	 * @param traumatismosOtros the traumatismosOtros to set
	 */
	public void setTraumatismosOtros(ArrayList traumatismosOtros) {
		this.traumatismosOtros = traumatismosOtros;
	}

	/**
	 * @return the tratamientosPrevios
	 */
	public ArrayList getTratamientosPrevios() {
		if(tratamientosPrevios == null)
		{
			tratamientosPrevios = new ArrayList();
		}
		return tratamientosPrevios;
	}

	/**
	 * @param tratamientosPrevios the tratamientosPrevios to set
	 */
	public void setTratamientosPrevios(ArrayList tratamientosPrevios) {
		this.tratamientosPrevios = tratamientosPrevios;
	}

	/**
	 * @return the secciones
	 */
	public ArrayList<InfoDatosInt> getSecciones() {
		if(secciones == null)
		{
			secciones = new ArrayList<InfoDatosInt>();
		}
		return secciones;
	}

	/**
	 * @param secciones the secciones to set
	 */
	public void setSecciones(ArrayList<InfoDatosInt> secciones) {
		this.secciones = secciones;
	}
}
