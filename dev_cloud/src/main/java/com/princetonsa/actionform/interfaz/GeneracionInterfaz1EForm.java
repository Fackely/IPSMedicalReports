package com.princetonsa.actionform.interfaz;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;


public class GeneracionInterfaz1EForm extends ValidatorForm
{
	/**
	 * 
	 * */
	private String estado;
	
	/**
	 * 
	 * */
	private DtoInterfazS1EInfo dtoInfoInterfaz;
	
	/**
	 * 
	 * */
	private HashMap contenidoArchivo;
	
	/**
	 * 
	 * */
	private String rutaInd = "";
	
	/**
	 * 
	 * */
	private String nombreInd = "";

	
	
	
	public void reset()
	{
		this.dtoInfoInterfaz = new DtoInterfazS1EInfo();
		contenidoArchivo = new HashMap();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DtoInterfazS1EInfo getDtoInfoInterfaz() {
		return dtoInfoInterfaz;
	}

	public void setDtoInfoInterfaz(DtoInterfazS1EInfo dtoInfoInterfaz) {
		this.dtoInfoInterfaz = dtoInfoInterfaz;
	}

	public HashMap getContenidoArchivo() {
		return contenidoArchivo;
	}

	public void setContenidoArchivo(HashMap contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}

	public String getRutaInd() {
		return rutaInd;
	}

	public void setRutaInd(String rutaInd) {
		this.rutaInd = rutaInd;
	}

	public String getNombreInd() {
		return nombreInd;
	}

	public void setNombreInd(String nombreInd) {
		this.nombreInd = nombreInd;
	}
}