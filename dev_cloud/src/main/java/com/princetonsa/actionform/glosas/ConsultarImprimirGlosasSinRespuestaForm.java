package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dto.glosas.DtoGlosa;

public class ConsultarImprimirGlosasSinRespuestaForm extends ValidatorForm {
	
	private String estado;
	private String linkSiguiente;
	private HashMap mapaListadoGlosas;
	private String fechaNotificacionInicial;
	private String consecutivoFact;
	private String fechaNotificacionFinal;
	private String convenio;
	private String codigoConvenio;
	private int codigoContrato;
	private String criteriosConsulta;	
	private ResultadoBoolean mensaje;
	private int numMapas;
	private int difechas;
	private int maxCampos;
	private String ruta;
	private String urlArchivo;
	private boolean existeArchivo;
	private boolean operacionTrue;
    private String patronOrdenar;
	private ArrayList<DtoGlosa> glosasSinRespuesta;
    private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
    private ArrayList<HashMap<String, Object>> arregloContratos = new ArrayList<HashMap<String,Object>>();
    private  ArrayList<String> arregloCampos = new ArrayList<String>();
    private String indicativoFueAuditada;
    
    
	
	
	
	/**
	 * Constructor por defecto
	 */
	public ConsultarImprimirGlosasSinRespuestaForm(){
		this.estado=new String("");
        this.patronOrdenar=new String("");
		this.fechaNotificacionInicial=new String("");
		this.consecutivoFact="";
		this.fechaNotificacionFinal=new String("");
		this.linkSiguiente=new String("");
		this.codigoConvenio=new String("");
		this.criteriosConsulta=new String("");
		this.mensaje=new ResultadoBoolean(false);
		this.difechas=ConstantesBD.codigoNuncaValido;
		this.maxCampos=ConstantesBD.codigoNuncaValido;
		this.ruta=new String("");
		this.urlArchivo=new String("");
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.codigoContrato=ConstantesBD.codigoNuncaValido;
		this.mapaListadoGlosas= new HashMap();
		this.mapaListadoGlosas.put("numRegistros", "0");
		this.glosasSinRespuesta=new ArrayList<DtoGlosa>();
		this.arregloConvenios=new ArrayList<HashMap<String,Object>>();
		this.arregloContratos=new ArrayList<HashMap<String,Object>>();
		this.arregloCampos=new ArrayList<String>();
		this.indicativoFueAuditada="";
		
	}
	
	/**
	 * Funcion que permite reiniciar los valores de los atributos
	 */
	public void reset()
	{
		this.estado=new String("");
		this.patronOrdenar=new String("");
		this.fechaNotificacionInicial=new String("");
		this.consecutivoFact="";
		this.fechaNotificacionFinal=new String("");
		this.linkSiguiente=new String("");
		this.codigoConvenio=new String("");
		this.ruta=new String("");
		this.urlArchivo=new String("");
		this.criteriosConsulta=new String("");
		this.mensaje=new ResultadoBoolean(false);
		this.existeArchivo=false;
		this.operacionTrue=false;
		this.codigoContrato=ConstantesBD.codigoNuncaValido;
		this.difechas=ConstantesBD.codigoNuncaValido;
		this.maxCampos=ConstantesBD.codigoNuncaValido;
		this.mapaListadoGlosas= new HashMap();
		this.mapaListadoGlosas.put("numRegistros", "0");
		this.glosasSinRespuesta=new ArrayList<DtoGlosa>();
		this.arregloConvenios=new ArrayList<HashMap<String,Object>>();
		this.arregloContratos=new ArrayList<HashMap<String,Object>>();
		this.arregloCampos=new ArrayList<String>();
		this.indicativoFueAuditada="";
	}

	
	
	/**
	 * Funcion que retorna el estado del FORM
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

    /**
     * 
     * @param estado
     */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaNotificacionInicial() {
		return fechaNotificacionInicial;
	}

	/**
	 * 
	 * @param fechaNotificacionIncial
	 */
	public void setFechaNotificacionInicial(String fechaNotificacionIncial) {
		this.fechaNotificacionInicial = fechaNotificacionIncial;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaNotificacionFinal() {
		return fechaNotificacionFinal;
	}

	/**
	 * 
	 * @param fechaNotificacionFinal
	 */
	public void setFechaNotificacionFinal(String fechaNotificacionFinal) {
		this.fechaNotificacionFinal = fechaNotificacionFinal;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * 
	 * @param codigConvenio
	 */
	public void setCodigoConvenio(String codigConvenio) {
		this.codigoConvenio = codigConvenio;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * 
	 * @param codigoContrato
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaListadoGlosas() {
		return mapaListadoGlosas;
	}
	
	/**
	 * 
	 * @param mapaListadoGlosas
	 */
	public void setMapaListadoGlosas(HashMap mapaListadoGlosas) {
		this.mapaListadoGlosas = mapaListadoGlosas;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoGlosa> getGlosasSinRespuesta() {
		return glosasSinRespuesta;
	}

	/**
	 * 
	 * @param glosasSinRespuesta
	 */
	public void setGlosasSinRespuesta(ArrayList<DtoGlosa> glosasSinRespuesta) {
		this.glosasSinRespuesta = glosasSinRespuesta;
	}

	/**
	 * 
	 * @return
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * 
	 * @param convenio
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
    
	/**
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}
     
	/**
	 * 
	 * @param arregloConvenios
	 */
	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}
    
	/**
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getArregloContratos() {
		return arregloContratos;
	}
    
	/**
	 * 
	 * @param arregloContratos
	 */
	public void setArregloContratos(
			ArrayList<HashMap<String, Object>> arregloContratos) {
		this.arregloContratos = arregloContratos;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumMapas() {
		return numMapas;
	}

	/**
	 * 
	 * @param numMapas
	 */
	public void setNumMapas(int numMapas) {
		this.numMapas = numMapas;
	}
    
	/**
	 * 
	 * @return
	 */
	public int getDifechas() {
		return difechas;
	}
    
	/*
	 * 
	 */
	public void setDifechas(int difechas) {
		this.difechas = difechas;
	}

    /**
     *  
     * @return
     */
	public ArrayList<String> getArregloCampos() {
		return arregloCampos;
	}
   
	/**
	 * 
	 * @param arregloCampos
	 */
	public void setArregloCampos(ArrayList<String> arregloCampos) {
		this.arregloCampos = arregloCampos;
	}
    
	/**
	 * 
	 * @return
	 */
	public int getMaxCampos() {
		return maxCampos;
	}
   
	/**
	 * 
	 * @param maxCampos
	 */
	public void setMaxCampos(int maxCampos) {
		this.maxCampos = maxCampos;
 	}
    
	/**
	 * 
	 * @return
	 */
 	public String getPatronOrdenar() {
		return patronOrdenar;
	}
    
 	/**
 	 * 
 	 * @param patronOrdenar
 	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
    
	/**
	 * 
	 * @return
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	 /**
	  * 
	  * @param linkSiguiente
	  */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
     
	/**
	 * 
	 * @return
	 */
	public String getRuta() {
		return ruta;
	}
    
	/**
	 * 
	 * @param ruta
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	/**
	 * 
	 * @return
	 */
	public String getUrlArchivo() {
		return urlArchivo;
	}
    
	/**
	 * 
	 * @param urlArchivo
	 */
	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
   
	/**
	 * 
	 * @param existeArchivo
	 */
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
   
	/**
	 * 
	 * @return
	 */
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
 
	/**
	 * 
	 * @param operacionTrue
	 */
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	public String getCriteriosConsulta() {
		return criteriosConsulta;
	}

	public void setCriteriosConsulta(String criteriosConsulta) {
		this.criteriosConsulta = criteriosConsulta;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the indicativoFueAuditada
	 */
	public String getIndicativoFueAuditada() {
		return indicativoFueAuditada;
	}

	/**
	 * @param indicativoFueAuditada the indicativoFueAuditada to set
	 */
	public void setIndicativoFueAuditada(String indicativoFueAuditada) {
		this.indicativoFueAuditada = indicativoFueAuditada;
	}

	public String getConsecutivoFact() {
		return consecutivoFact;
	}

	public void setConsecutivoFact(String consecutivoFact) {
		this.consecutivoFact = consecutivoFact;
	}
}
