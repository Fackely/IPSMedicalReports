package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

public class ProrrogarAnularAutorizacionesEntSubcontratadasForm extends ValidatorForm{

private static Logger logger = Logger.getLogger(ProrrogarAnularAutorizacionesEntSubcontratadasForm.class);
	
	private String estado;
	private ArrayList<DtoAutorizacionEntSubContratada> autorizacionesEntSubContratadas;
	private String opcionListadoAutorizacion;
	private HashMap parametrosFiltros;
    private String propiedadOrdenar;
	private String ultimaPropiedad;
	private int posAutorizacion;
	private String estadoAutorizaciondetalle;
	private HashMap parametrosBusqueda;
	private HashMap entidadesSubcontratadas;
	private HashMap parametrosProrroga;
	private HashMap parametrosAnulacion;
	private String usuarioSesion;
	private String linkSiguiente;
	
	public ProrrogarAnularAutorizacionesEntSubcontratadasForm()
	{
		this.reset();
	}
	
	
	public void reset()
	{
		this.estado=new String("");
		this.opcionListadoAutorizacion=new String("");
		this.usuarioSesion=new String("");
		this.linkSiguiente=new String("");
		this.autorizacionesEntSubContratadas=new ArrayList<DtoAutorizacionEntSubContratada>();
		this.parametrosFiltros=new HashMap();
		this.parametrosFiltros.put("operacionExitosa",ConstantesBD.acronimoNo);
		this.propiedadOrdenar=new String("");
		this.ultimaPropiedad=new String("fechaAutorizacion_");
		this.posAutorizacion=ConstantesBD.codigoNuncaValido;
		this.estadoAutorizaciondetalle=new String("");
		this.parametrosBusqueda=new HashMap();		
		this.entidadesSubcontratadas=new HashMap();
		this.entidadesSubcontratadas.put("numRegistros","0");
		this.parametrosProrroga=new HashMap();
		this.parametrosAnulacion=new HashMap();
		
		
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void resetArrayAutorizacionesEntSub()
	{
		this.autorizacionesEntSubContratadas=new ArrayList<DtoAutorizacionEntSubContratada>();
	}
	
	public ArrayList<DtoAutorizacionEntSubContratada> getAutorizacionesEntSubContratadas() {
		return autorizacionesEntSubContratadas;
	}


	public void setAutorizacionesEntSubContratadas(
			ArrayList<DtoAutorizacionEntSubContratada> autorizacionesEntSubContratadas) {
		this.autorizacionesEntSubContratadas = autorizacionesEntSubContratadas;
	}


	public String getOpcionListadoAutorizacion() {
		return opcionListadoAutorizacion;
	}


	public void setOpcionListadoAutorizacion(String opcionListadoAutorizacion) {
		this.opcionListadoAutorizacion = opcionListadoAutorizacion;
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
	
	
	public String getFechaAutorizacionFiltro() {
		return this.parametrosFiltros.get("fechaAutorizacionFiltro").toString();
	}

	public void setFechaAutorizacionFiltro(String valor) {
		this.parametrosFiltros.put("fechaAutorizacionFiltro",valor);
	}
	

	public String getPropiedadOrdenar() {
		return propiedadOrdenar;
	}


	public void setPropiedadOrdenar(String propiedadOrdenar) {
		this.propiedadOrdenar = propiedadOrdenar;
	}


	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}


	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}


	public int getPosAutorizacion() {
		return posAutorizacion;
	}


	public void setPosAutorizacion(int posAutorizacion) {
		this.posAutorizacion = posAutorizacion;
	}


	public String getEstadoAutorizaciondetalle() {
		return estadoAutorizaciondetalle;
	}


	public void setEstadoAutorizaciondetalle(String estadoAutorizaciondetalle) {
		this.estadoAutorizaciondetalle = estadoAutorizaciondetalle;
	}


	public HashMap getParametrosBusqueda() {
		return parametrosBusqueda;
	}


	public void setParametrosBusqueda(HashMap parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
	}
    
	

	public HashMap getEntidadesSubcontratadas() {
		return entidadesSubcontratadas;
	}


	public void setEntidadesSubcontratadas(HashMap entidadesSubcontratadas) {
		this.entidadesSubcontratadas = entidadesSubcontratadas;
	}


	public HashMap getParametrosProrroga() {
		return parametrosProrroga;
	}


	public void setParametrosProrroga(HashMap parametrosProrroga) {
		this.parametrosProrroga = parametrosProrroga;
	}


	public HashMap getParametrosAnulacion() {
		return parametrosAnulacion;
	}


	public void setParametrosAnulacion(HashMap parametrosAnulacion) {
		this.parametrosAnulacion = parametrosAnulacion;
	}


	public String getUsuarioSesion() {
		return usuarioSesion;
	}


	public void setUsuarioSesion(String usuarioSesion) {
		this.usuarioSesion = usuarioSesion;
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	
	
}
