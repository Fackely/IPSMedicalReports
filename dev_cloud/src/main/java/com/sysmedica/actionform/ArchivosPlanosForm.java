package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;
import util.UtilidadFecha;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class ArchivosPlanosForm extends ValidatorForm {

	private boolean caracterizacion;
	private boolean talento;
	private boolean notificacionIndividual;
	private boolean notificacionColectiva;
	private boolean datosComplementarios;
	private boolean control;
	
	private int semana;
	private int anyo;
	
	private String estado;
	
	
	/**
     * Metodo para validar los atributos provenientes del formulario
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
        	reset();
        }
        else {
        	
        }
        
        return errores;
    }
    
    public void reset()
    {
    	caracterizacion = false;
    	talento = false;
    	notificacionIndividual = false;
    	notificacionColectiva = false;
    	datosComplementarios = false;
    	control = false;
    	
    	semana = 0;
    	
    	String fechaActual = UtilidadFecha.getFechaActual();
    	
    	anyo = Integer.parseInt(fechaActual.split("/")[2]);
    }
	
	public int getAnyo() {
		return anyo;
	}
	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}
	public boolean isCaracterizacion() {
		return caracterizacion;
	}
	public void setCaracterizacion(boolean caracterizacion) {
		this.caracterizacion = caracterizacion;
	}
	public boolean isControl() {
		return control;
	}
	public void setControl(boolean control) {
		this.control = control;
	}
	public boolean isDatosComplementarios() {
		return datosComplementarios;
	}
	public void setDatosComplementarios(boolean datosComplementarios) {
		this.datosComplementarios = datosComplementarios;
	}
	public boolean isNotificacionColectiva() {
		return notificacionColectiva;
	}
	public void setNotificacionColectiva(boolean notificacionColectiva) {
		this.notificacionColectiva = notificacionColectiva;
	}
	public boolean isNotificacionIndividual() {
		return notificacionIndividual;
	}
	public void setNotificacionIndividual(boolean notificacionIndividual) {
		this.notificacionIndividual = notificacionIndividual;
	}
	public int getSemana() {
		return semana;
	}
	public void setSemana(int semana) {
		this.semana = semana;
	}
	public boolean isTalento() {
		return talento;
	}
	public void setTalento(boolean talento) {
		this.talento = talento;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
