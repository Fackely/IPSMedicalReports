/*
 * @(#)ReporteProcedimientosEsteticosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2007. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.facturacion.ReporteProcedimientosEsteticosAction;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion del reporte procedimientos esteticos
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Junio 4, 2007
 * @author Julian Pacheco jpacheco@princetonsa.com
 * @author ANDRES E SILVA MONSALVE
 */
public class ReporteProcedimientosEsteticosForm extends ValidatorForm 
{

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Para manejar la fecha inicial del rango de Reporte Procedimientos Esteticos.
	 */
	private String fechaInicial;
	
	/**
	 * Para manejar la fecha final del rango de Reporte Procedimientos Esteticos
	 */
	private String fechaFinal;
	
	/**
	 * Maneja los grupos esteticos por servicios
	 */
	private String grupoEstetico;
	
	/**
	 * Maneja los centros de atencion
	 */
	private String centroAtencion;
	
	/**
	 * Tipo de reporte utilizado en el reporte  procedimientos Esteticos
	 */
	private String tipoReporte;
	
	/**
	 * 
	 */
	private int indice;
	
	/**
	 * Institucion 
	 */
	private String institucion;
	
	/**
	 * Consulta
	 */
	private HashMap consulta;
	
	/**
	 * Detalle
	 */
	private HashMap detalle;
	
	/**
	 * Detalle del cargo de la cirugia
	 */
	private ArrayList<HashMap<String, Object>> detalleCargo;
	
	/**
	 * Detalle de los materiales especiales de la cirugia
	 */
	private ArrayList<HashMap<String, Object>> materialesEspeciales;
	
	//atributos usados para la paginacion 
	private String linkSiguiente;
	private int offset;
	
	//atributos usados para la ordenacion
	private String columna;
	private String ultimaColumna;

	private Logger logger =  Logger.getLogger(ReporteProcedimientosEsteticosForm.class);
	
	
	//	--------------------------------METODOS--------------------------------------
	
	/**
     * resetea los atributos del form
	 * @param centroAtencion 
	 * @param institucion 
     *
     */
    public void reset(String centroAtencion, String institucion)
    {
    	this.indice=-1;
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.grupoEstetico="";
    	this.centroAtencion=centroAtencion;
    	this.tipoReporte="";
    	this.institucion = institucion;
    	this.consulta = new HashMap();
    	this.detalle = new HashMap();
    	this.detalleCargo = new ArrayList<HashMap<String,Object>>();
    	this.materialesEspeciales = new ArrayList<HashMap<String,Object>>();
    	    	
    	this.linkSiguiente = "";
    	this.offset = 0;
    	
    	this.columna = "";
    	this.ultimaColumna = "";
    }
    
    
    /**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no recorded
     * error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
    */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        //errores=super.validate(mapping,request);
        
        logger.info("\n\nValor del estado=============>       "+estado);
        
        
        if(this.estado.equals("consultar"))
        {
        	logger.info("\n\n   ***************  SI ENTRO AL VALIDATE  *************** \n");
        	if(this.getFechaFinal().equals(""))
        	{
        		errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
        		logger.info("\n\n   ***************  Cargo Error Fecha Inicial   *************** \n");
        	}
        	if(this.getFechaFinal().equals(""))
        	{
        		errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
        		logger.info("\n\n   ***************  Cargo Error Fecha Final   *************** \n");
        	}
        	
        	//(this.grupoEstetico==ConstantesBD.codigoNuncaValido)
        	if(!this.getFechaInicial().equals("") || !this.getFechaFinal().equals(""))
			{
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicial()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.getFechaInicial()));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinal()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final"+this.getFechaFinal()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.getFechaInicial(), "Actual "+UtilidadFecha.getFechaActual()));
					}
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinal(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.getFechaFinal(), "Actual "+UtilidadFecha.getFechaActual()));
					}
					
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicial(),this.getFechaFinal()))
					{
						errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final "+this.getFechaFinal(), "Inicial "+this.fechaInicial));
					}
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal())>=2)
					{
						errores.add("", new ActionMessage("errors.rangoMayorSesentaDias", "PARA CONSULTAR PROCEDIMIENTOS ESTÉTICOS"));
					}
				}
			}
        	
        }
        return errores;
		
    }
//-----------------Fin Validate----------------------------------

//  --------------------------Getters and Setters-----------------------------

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
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}


	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}


	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial; 
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * @return the grupoEstetico
	 */
	public String getGrupoEstetico() {
		return grupoEstetico;
	}


	/**
	 * @param grupoEstetico the grupoEstetico to set
	 */
	public void setGrupoEstetico(String grupoEstetico) {
		this.grupoEstetico = grupoEstetico;
	}


	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}


	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}


	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public String getInstitucion() {
		return institucion;
	}


	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}


	public HashMap getConsulta() {
		return consulta;
	}


	public void setConsulta(HashMap consulta) {
		this.consulta = consulta;
	}
	
	public Object getConsulta(String key) {
		return consulta.get(key);
	}


	public void setConsulta(String key,Object obj) {
		this.consulta.put(key, obj);
	}


	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	public int getOffset() {
		return offset;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}


	public String getColumna() {
		return columna;
	}


	public void setColumna(String columna) {
		this.columna = columna;
	}


	public String getUltimaColumna() {
		return ultimaColumna;
	}


	public void setUltimaColumna(String ultimaColumna) {
		this.ultimaColumna = ultimaColumna;
	}


	public HashMap getDetalle() {
		return detalle;
	}


	public void setDetalle(HashMap detalle) {
		this.detalle = detalle;
	}
	
	public Object getDetalle(String key) {
		return detalle.get(key);
	}


	public void setDetalle(String key,Object obj) {
		this.detalle.put(key,obj);
	}


	public ArrayList<HashMap<String, Object>> getDetalleCargo() {
		return detalleCargo;
	}


	public void setDetalleCargo(ArrayList<HashMap<String, Object>> detalleCargo) {
		this.detalleCargo = detalleCargo;
	}


	/**
	 * @return the materialesEspeciales
	 */
	public ArrayList<HashMap<String, Object>> getMaterialesEspeciales() {
		return materialesEspeciales;
	}


	/**
	 * @param materialesEspeciales the materialesEspeciales to set
	 */
	public void setMaterialesEspeciales(
			ArrayList<HashMap<String, Object>> materialesEspeciales) {
		this.materialesEspeciales = materialesEspeciales;
	}
	
	/**
	 * Método para saber el número de los materiales especiales
	 * @return
	 */
	public int getNumMaterialesEspeciales()
	{
		return this.materialesEspeciales.size();
	}
	
	/**
	 * Método para obtener el total de los materiales especiales
	 * @return
	 */
	public String getTotalMaterialesEspeciales()
	{
		double total = 0;
		for(HashMap elemento:this.materialesEspeciales)
			total += Utilidades.convertirADouble(elemento.get("valorDouble")+"", true);
		
		return UtilidadTexto.formatearValores(total);
	}
 	
}
