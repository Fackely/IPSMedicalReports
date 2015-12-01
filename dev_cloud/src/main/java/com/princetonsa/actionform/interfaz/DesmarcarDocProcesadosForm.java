package com.princetonsa.actionform.interfaz;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;

import util.ConstantesBD;

public class DesmarcarDocProcesadosForm extends ValidatorForm{

	private Logger logger = Logger.getLogger(DesmarcarDocProcesadosForm.class);
	
	private String estado;
	private HashMap parametrosBusqueda;
	private HashMap parametrosFiltros;
	private String motivoDesmaracacion;
	private String fechaControl;
	private ArrayList<DtoDocumentoDesmarcar> mvtosVentas;
	private ArrayList<DtoDocumentoDesmarcar> mvtosRecaudos;
	private ArrayList<DtoDocumentoDesmarcar> mvtosAjustes;
	private ArrayList<DtoDocumentoDesmarcar> mvtosServicios;
	private int contDocsaDesMarcar;
	
	
	public DesmarcarDocProcesadosForm()
	{
		this.reset();
		
	}
		
	public void reset()
	{
	 this.estado=new String("");
	 this.motivoDesmaracacion=new String("");
	 this.fechaControl=new String("");
	 this.parametrosFiltros= new HashMap();
	 this.parametrosFiltros.put("operacionExitosa","");
	 this.parametrosBusqueda=new HashMap();
	 this.mvtosAjustes=new ArrayList<DtoDocumentoDesmarcar>();
	 this.mvtosRecaudos=new ArrayList<DtoDocumentoDesmarcar>();
	 this.mvtosServicios=new ArrayList<DtoDocumentoDesmarcar>();
	 this.mvtosVentas=new ArrayList<DtoDocumentoDesmarcar>();
	 this.contDocsaDesMarcar=0;
	 
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getParametrosBusqueda() {
		return parametrosBusqueda;
	}

	public void setParametrosBusqueda(HashMap parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
	}

	public HashMap getParametrosFiltros() {
		return parametrosFiltros;
	}

	public void setParametrosFiltros(HashMap parametrosFiltros) {
		this.parametrosFiltros = parametrosFiltros;
	}
	
	public Object getParametrosFiltros(String key) {
		return parametrosFiltros.get(key);
	}

	public void setParametrosFiltros(String key, Object value) {
		this.parametrosFiltros.put(key,value);
	}

	public ArrayList<DtoDocumentoDesmarcar> getMvtosVentas() {
		return mvtosVentas;
	}

	public void setMvtosVentas(ArrayList<DtoDocumentoDesmarcar> mvtosVentas) {
		this.mvtosVentas = mvtosVentas;
	}

	public ArrayList<DtoDocumentoDesmarcar> getMvtosRecaudos() {
		return mvtosRecaudos;
	}

	public void setMvtosRecaudos(ArrayList<DtoDocumentoDesmarcar> mvtosRecaudos) {
		this.mvtosRecaudos = mvtosRecaudos;
	}

	public ArrayList<DtoDocumentoDesmarcar> getMvtosAjustes() {
		return mvtosAjustes;
	}

	public void setMvtosAjustes(ArrayList<DtoDocumentoDesmarcar> mvtosAjustes) {
		this.mvtosAjustes = mvtosAjustes;
	}

	public ArrayList<DtoDocumentoDesmarcar> getMvtosServicios() {
		return mvtosServicios;
	}

	public void setMvtosServicios(ArrayList<DtoDocumentoDesmarcar> mvtosServicios) {
		this.mvtosServicios = mvtosServicios;
	}

	public String getMotivoDesmaracacion() {
		return motivoDesmaracacion;
	}

	public void setMotivoDesmaracacion(String motivoDesmaracacion) {
		this.motivoDesmaracacion = motivoDesmaracacion;
	}

	public String getFechaControl() {
		return fechaControl;
	}

	public void setFechaControl(String fechaControl) {
		this.fechaControl = fechaControl;
	}

	/**
	 * @return the contDocsaDesMarcar
	 */
	public int getContDocsaDesMarcar() {
		return contDocsaDesMarcar;
	}

	/**
	 * @param contDocsaDesMarcar the contDocsaDesMarcar to set
	 */
	public void setContDocsaDesMarcar(int contDocsaDesMarcar) {
		this.contDocsaDesMarcar = contDocsaDesMarcar;
	}

	
	
	
}
