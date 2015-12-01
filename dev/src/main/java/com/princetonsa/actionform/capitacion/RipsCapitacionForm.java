/*
 * 23 de Abril, 2007
 */
package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * @author Sebastián Gómez R.
 *
 * Formulario para el manejo de la creación de archivos rips
 */
public class RipsCapitacionForm extends ValidatorForm 
{
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * Fecha de la remision de los rips
	 */
	private String fechaRemision = "";
	
	/**
	 * Número de la remisión de los rips
	 */
	private String numeroRemision = "";
	
	/**
	 * Número de la cuenta de cobro
	 */
	private String numeroCuentaCobro;
	
	/**
	 * Código del manual
	 */
	private String tipoCodigo;
	
	/**
	 * mapa donde se seleccionan los archivos que se van a generar
	 */
	private HashMap seleccion = new HashMap();
	
	/**
	 * mapa donde se almacenan los resultados
	 */
	private HashMap resultados = new HashMap();
	
	/**
	 * Variable que indica si hubo inconsistencias en la generación de RIPS
	 */
	private boolean huboInconsistencias;
	
	/**
	 * Ruta de la generación de los RIPS
	 */
	private String pathGeneracion;
	
	/**
	 * mapa donde se almacena el contenido de un archivo generado
	 */
	private HashMap contenidoArchivo = new HashMap();
	
	/**
	 * Prefijo de un archivo
	 */
	private String archivo;
	
//	 String para capturar el nombre del Archivo Backup
	private String backupArchivo;
	
	//****DATOS DE LA CUENTA DE COBRO*************
	private String codigoConvenio;
	private String nombreConvenio;
	private String fechaInicial;
	private String fechaFinal;
	private String fechaElaboracion;
	private String valorCuenta;
	private String contrato;
	private HashMap contratos = new HashMap();
	
	/**
	 * Limpia el Formulario
	 */
	public void reset()
	{
		this.estado="";
		this.fechaRemision="";
		this.numeroCuentaCobro="";
		this.numeroRemision="";
		this.tipoCodigo="";
		this.seleccion=new HashMap();
		this.resultados = new HashMap();
		this.huboInconsistencias = false;
		this.pathGeneracion = "";
		
		//atributos de la cuenta de cobro
		this.codigoConvenio = "";
		this.nombreConvenio = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.fechaElaboracion = "";
		this.valorCuenta = "";
		this.contratos = new HashMap();
		this.contrato = "";
		
		this.contenidoArchivo = new HashMap();
		this.archivo = "";
		this.backupArchivo = "";
		
	}
	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		
		
		if(estado.equals("generar"))
		{
			int bandera=0;
			
			//**************VALIDACIÓN DE LA SELECCIÓN DE ARCHIVOS************
			if((this.getSeleccion("AC")+"").equals("true"))
				bandera=1;
			if((this.getSeleccion("AH")+"").equals("true"))
				bandera=1;
			if((this.getSeleccion("AM")+"").equals("true"))
				bandera=1;
			if((this.getSeleccion("AN")+"").equals("true"))
				bandera=1;
			if((this.getSeleccion("AP")+"").equals("true"))
				bandera=1;
			if((this.getSeleccion("AT")+"").equals("true"))
				bandera=1;
			if((this.getSeleccion("AU")+"").equals("true"))
				bandera=1;
			
			if(bandera==0)
				errores.add("sin selección", new ActionMessage("error.rips.archivosSinSeleccion"));
			
			///*****VALIDACIÓN DE CUENTA DE COBRO**********
			if(this.numeroCuentaCobro.equals(""))
				errores.add("sin numero de cuenta de cobro", new ActionMessage("errors.required","La Cuenta de Cobro"));
			
			//*****VALIDACIÓN CONVENIOS**********
			if(this.codigoConvenio.equals("")||this.nombreConvenio.equals(""))
				errores.add("sin convenio", new ActionMessage("errors.required","El Convenio"));
			
			//***VALIDACION DEL CONTRATO************************
			//xplanner http://xplanner.princeton:9090/xplanner2008/do/view/task?oid=3005
			/*if(this.contrato.equals(""))
				errores.add("sin contrato", new ActionMessage("errors.required","El Contrato"));*/
			
			//****VALIDACION DE TIPO CODIGO********
			if(this.tipoCodigo.equals(""))
				errores.add("sin convenio", new ActionMessage("errors.required","Código del Manual"));
			
			//**********VALIDACIÓN DATOS DE GENERACIÓN RIPS**********
			if(!fechaRemision.equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaRemision()))
					errores.add("fecha remisión", new ActionMessage("errors.formatoFechaInvalido",this.getFechaRemision()));
			}
			else
			{
				errores.add("La fecha de remisión", new ActionMessage("errors.required", "La fecha de remisión"));
			}
			
			if(numeroRemision.equals(""))
				errores.add("El número de remisión", new ActionMessage("errors.required", "El Número de Remisión"));
			
			
		}
		
		return errores;
	}

	/**
	 * @return the codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	

	/**
	 * @return the tipoCodigo
	 */
	public String getTipoCodigo() {
		return tipoCodigo;
	}

	/**
	 * @param tipoCodigo the tipoCodigo to set
	 */
	public void setTipoCodigo(String tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
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
	 * @return the fechaRemision
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}

	/**
	 * @param fechaRemision the fechaRemision to set
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}

	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * @return the numeroCuentaCobro
	 */
	public String getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}

	/**
	 * @param numeroCuentaCobro the numeroCuentaCobro to set
	 */
	public void setNumeroCuentaCobro(String numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}

	/**
	 * @return the numeroRemision
	 */
	public String getNumeroRemision() {
		return numeroRemision;
	}

	/**
	 * @param numeroRemision the numeroRemision to set
	 */
	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}

	/**
	 * @return the seleccion
	 */
	public HashMap getSeleccion() {
		return seleccion;
	}

	/**
	 * @param seleccion the seleccion to set
	 */
	public void setSeleccion(HashMap seleccion) {
		this.seleccion = seleccion;
	}
	
	/**
	 * @return Retorna un elemento del mapa seleccion
	 */
	public Object getSeleccion(String key) {
		return seleccion.get(key);
	}

	/**
	 * @param Asigna elemento al mapa seleccion 
	 */
	public void setSeleccion(String key,Object obj) 
	{
		this.seleccion.put(key,obj);
	}

	/**
	 * @return the fechaElaboracion
	 */
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}

	/**
	 * @param fechaElaboracion the fechaElaboracion to set
	 */
	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}

	/**
	 * @return the resultados
	 */
	public HashMap getResultados() {
		return resultados;
	}

	/**
	 * @param resultados the resultados to set
	 */
	public void setResultados(HashMap resultados) {
		this.resultados = resultados;
	}
	
	/**
	 * @return Retorna elemento del mapa resultados
	 */
	public Object getResultados(String key) {
		return resultados.get(key);
	}

	/**
	 * @param Asigna elemento al mapa resultados 
	 */
	public void setResultados(String key,Object obj) {
		this.resultados.put(key,obj);
	}

	/**
	 * @return the huboInconsistencias
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}

	/**
	 * @param huboInconsistencias the huboInconsistencias to set
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}

	/**
	 * @return the pathGeneracion
	 */
	public String getPathGeneracion() {
		return pathGeneracion;
	}

	/**
	 * @param pathGeneracion the pathGeneracion to set
	 */
	public void setPathGeneracion(String pathGeneracion) {
		this.pathGeneracion = pathGeneracion;
	}

	/**
	 * @return the contratos
	 */
	public HashMap getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(HashMap contratos) {
		this.contratos = contratos;
	}
	
	/**
	 * @return Retorna elemento del mapa contratos
	 */
	public Object getContratos(String key) {
		return contratos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa contratos
	 */
	public void setContratos(String key,Object obj) {
		this.contratos.put(key, obj);
	}

	/**
	 * @return the valorCuenta
	 */
	public String getValorCuenta() {
		return valorCuenta;
	}

	/**
	 * @param valorCuenta the valorCuenta to set
	 */
	public void setValorCuenta(String valorCuenta) {
		this.valorCuenta = valorCuenta;
	}

	/**
	 * @return the contrato
	 */
	public String getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the archivo
	 */
	public String getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the contenidoArchivo
	 */
	public HashMap getContenidoArchivo() {
		return contenidoArchivo;
	}

	/**
	 * @param contenidoArchivo the contenidoArchivo to set
	 */
	public void setContenidoArchivo(HashMap contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}
	
	/**
	 * @return Retorna elemento del mapa contenidoArchivo
	 */
	public Object getContenidoArchivo(String key) {
		return contenidoArchivo.get(key);
	}

	/**
	 * @param Asigna elemento al mapa contenidoArchivo 
	 */
	public void setContenidoArchivo(String key,Object obj) {
		this.contenidoArchivo.put(key,obj);
	}

	/**
	 * @return the backupArchivo
	 */
	public String getBackupArchivo() {
		return backupArchivo;
	}

	/**
	 * @param backupArchivo the backupArchivo to set
	 */
	public void setBackupArchivo(String backupArchivo) {
		this.backupArchivo = backupArchivo;
	}
}
