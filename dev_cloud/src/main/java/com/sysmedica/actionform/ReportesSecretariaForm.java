package com.sysmedica.actionform;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.sysmedica.util.CalendarioEpidemiologico;

import util.UtilidadFecha;

public class ReportesSecretariaForm extends ValidatorForm {
	
	private Logger logger = Logger.getLogger(ReportesSecretariaForm.class);

	private boolean morbilidad;
	private boolean mortalidad;
	private boolean brotes;
	private boolean sivim;
	private boolean sisvan;
	private int semanaEpidemiologica;
	private int anyo;
	
	private boolean generoArchivoMorbilidad;
	private boolean generoArchivoMortalidad;
	private boolean generoArchivoBrotes;
	private boolean generoArchivoSivim;
	private boolean generoArchivoSisvan;
	
	private Collection coleccion;
	
	private String estado;
	
	private boolean huboError = false;
	
	public void reset() {
    	
    	morbilidad = false;
    	mortalidad = false;
    	brotes = false;
    	sivim = false;
    	sisvan = false;
    	semanaEpidemiologica = 0;
    	anyo = 0;
    	huboError = false;
    	
    	try {
    		coleccion.clear();
    	}
    	catch (NullPointerException npe) {
    		
    	}
    }
	
	/**
     * Metodo para validar los atributos provenientes del formulario
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
        	reset();
        }
        else if (estado.equals("consultar") || (estado.equals("busqueda"))) {
        	
        	String fechaActual = UtilidadFecha.getFechaActual();
        //	int anyoActual = Integer.parseInt(fechaActual.split("/")[2]);
        	int numeroSemanaActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[0];
        	int anyoActual = CalendarioEpidemiologico.obtenerNumeroSemana(fechaActual)[1];
        	
        	if (semanaEpidemiologica>0) {
	        	if (anyo<1900) {
	        		errores.add("Valor de año no valido", new ActionMessage("error.epidemiologia.anyoBusqueda"));
	        		
	        		reset();
	        		
	        		huboError=true;
	        	}
	        	if (semanaEpidemiologica>=numeroSemanaActual && anyo>=anyoActual) {
	        		errores.add("La semana epidemiologica seleccionada es superior o igual a la actual", new ActionMessage("error.epidemiologia.numerosemananovalido"));
	        		
	        		reset();
	        		huboError=true;
	        	}
	        	if (anyo>anyoActual) {
	        		errores.add("La semana epidemiologica seleccionada es superior o igual a la actual", new ActionMessage("error.epidemiologia.numerosemananovalido"));
	        		
	        		reset();
	        		huboError=true;
	        	}
	        	
	        	
        	}
        }
        
        return errores;
    }
	
	public int getAnyo() {
		return anyo;
	}
	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}
	public boolean isBrotes() {
		return brotes;
	}
	public void setBrotes(boolean brotes) {
		this.brotes = brotes;
	}
	public boolean isMorbilidad() {
		return morbilidad;
	}
	public void setMorbilidad(boolean morbilidad) {
		this.morbilidad = morbilidad;
	}
	public boolean isMortalidad() {
		return mortalidad;
	}
	public void setMortalidad(boolean mortalidad) {
		this.mortalidad = mortalidad;
	}
	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}
	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}
	public boolean isSisvan() {
		return sisvan;
	}
	public void setSisvan(boolean sisvan) {
		this.sisvan = sisvan;
	}
	public boolean isSivim() {
		return sivim;
	}
	public void setSivim(boolean sivim) {
		this.sivim = sivim;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isGeneroArchivoBrotes() {
		return generoArchivoBrotes;
	}

	public void setGeneroArchivoBrotes(boolean generoArchivoBrotes) {
		this.generoArchivoBrotes = generoArchivoBrotes;
	}

	public boolean isGeneroArchivoMorbilidad() {
		return generoArchivoMorbilidad;
	}

	public void setGeneroArchivoMorbilidad(boolean generoArchivoMorbilidad) {
		this.generoArchivoMorbilidad = generoArchivoMorbilidad;
	}

	public boolean isGeneroArchivoMortalidad() {
		return generoArchivoMortalidad;
	}

	public void setGeneroArchivoMortalidad(boolean generoArchivoMortalidad) {
		this.generoArchivoMortalidad = generoArchivoMortalidad;
	}

	public boolean isGeneroArchivoSisvan() {
		return generoArchivoSisvan;
	}

	public void setGeneroArchivoSisvan(boolean generoArchivoSisvan) {
		this.generoArchivoSisvan = generoArchivoSisvan;
	}

	public boolean isGeneroArchivoSivim() {
		return generoArchivoSivim;
	}

	public void setGeneroArchivoSivim(boolean generoArchivoSivim) {
		this.generoArchivoSivim = generoArchivoSivim;
	}

	public Collection getColeccion() {
		return coleccion;
	}

	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}

	public boolean isHuboError() {
		return huboError;
	}

	public void setHuboError(boolean huboError) {
		this.huboError = huboError;
	}
	
	
}
