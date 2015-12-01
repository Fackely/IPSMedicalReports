package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

@SuppressWarnings("serial")
public class ConsultarImprimirAutorizacionesEntSubcontratadasForm extends ValidatorForm {

	
	@SuppressWarnings("unused")
	private static final String SERIAL = "serial";

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ConsultarImprimirAutorizacionesEntSubcontratadasForm.class);
	
	private String estado;
	private ArrayList<DtoAutorizacionEntSubContratada> autorizacionesEntSubContratadas;
	private String opcionListadoAutorizacion;
	@SuppressWarnings("rawtypes")
	private HashMap parametrosFiltros;
    private String propiedadOrdenar;
	private String ultimaPropiedad;
	private int posAutorizacion;
	private String estadoAutorizaciondetalle;
	@SuppressWarnings("rawtypes")
	private HashMap parametrosBusqueda;
	@SuppressWarnings("rawtypes")
	private HashMap entidadesSubcontratadas;
	private String linkSiguiente;
	/** * lista que contiene los nombres de los reportes de las autorzaciones **/
	private ArrayList<String> listaNombresReportes;
	
	public ConsultarImprimirAutorizacionesEntSubcontratadasForm()
	{
		this.reset();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void reset()
	{
		this.estado=new String("");
		this.opcionListadoAutorizacion=new String("");
		this.autorizacionesEntSubContratadas=new ArrayList<DtoAutorizacionEntSubContratada>();
		this.parametrosFiltros=new HashMap();
		this.parametrosFiltros.put("operacionExitosa",ConstantesBD.acronimoNo);
		this.propiedadOrdenar=new String("");
		this.ultimaPropiedad=new String("fechaAutorizacion_");
		this.posAutorizacion=ConstantesBD.codigoNuncaValido;
		this.estadoAutorizaciondetalle=new String("");
		this.linkSiguiente=new String("");
		this.parametrosBusqueda=new HashMap();		
		this.entidadesSubcontratadas=new HashMap();
		this.entidadesSubcontratadas.put("numRegistros","0");
		this.listaNombresReportes= new ArrayList<String>();
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


	public void setAutorizacionesEntSubContratadas(ArrayList<DtoAutorizacionEntSubContratada> autorizacionesEntSubContratadas) {
		this.autorizacionesEntSubContratadas = autorizacionesEntSubContratadas;
	}


	public String getOpcionListadoAutorizacion() {
		return opcionListadoAutorizacion;
	}


	public void setOpcionListadoAutorizacion(String opcionListadoAutorizacion) {
		this.opcionListadoAutorizacion = opcionListadoAutorizacion;
	}


	@SuppressWarnings("rawtypes")
	public HashMap getParametrosFiltros() {
		return parametrosFiltros;
	}


	@SuppressWarnings("rawtypes")
	public void setParametrosFiltros(HashMap parametrosFiltros) {
		this.parametrosFiltros = parametrosFiltros;
	}

	public Object getParametrosFiltros(String key) {
		return parametrosFiltros.get(key);
	}

	@SuppressWarnings("unchecked")
	public void setParametrosFiltros(String key, Object value) {
		this.parametrosFiltros.put(key,value);
	}
	
	
	public String getFechaAutorizacionFiltro() {
		return this.parametrosFiltros.get("fechaAutorizacionFiltro").toString();
	}

	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("rawtypes")
	public HashMap getParametrosBusqueda() {
		return parametrosBusqueda;
	}


	@SuppressWarnings("rawtypes")
	public void setParametrosBusqueda(HashMap parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
	}
     
	public Object getParametrosBusqueda(String key) {
		return parametrosBusqueda.get(key);
	}

	@SuppressWarnings("unchecked")
	public void setParametrosBusqueda(String key, Object value) {
		this.parametrosBusqueda.put(key,value);
	}


	@SuppressWarnings("rawtypes")
	public HashMap getEntidadesSubcontratadas() {
		return entidadesSubcontratadas;
	}


	@SuppressWarnings("rawtypes")
	public void setEntidadesSubcontratadas(HashMap entidadesSubcontratadas) {
		this.entidadesSubcontratadas = entidadesSubcontratadas;
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}
	
	
}
