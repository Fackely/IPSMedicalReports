/*
 * Enero 2, 2008
 */
package com.princetonsa.actionform.manejoPaciente;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *	Lectura Planos Entidades Subcontratadas 
 */
public class LecturaPlanosEntidadesForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(LecturaPlanosEntidadesForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	//******************PARÁMETROS DE CAPTURA (inicio de flujo)***********************
	private String entidadSubcontratada;
	private ArrayList<HashMap<String, Object>> entidadesSubcontradas = new ArrayList<HashMap<String,Object>>();
	private int codigoManual;
	private ArrayList<HashMap<String, Object>> codigosManuales = new ArrayList<HashMap<String,Object>>();
	private String ubicacionPlanosEntidadesSubcontratadas; //define la forma de capturar los planos Cliente o Servidor
	private String directorioArchivos; //para la opcion por servidor
	//para la opcion por cliente -----------------------------------------------------------------------
	private HashMap archivos = new HashMap(); 
	private String tipoArchivo, nombreArchivo;
	private FormFile archivo;
	//-----------------------------------------------------------------------------------------------------
	private String numeroFactura;
	private String validacionNumeroCarnet;
	//*********************************************************************************
	//**************PARÁMETROS DE SALIDA DE PROCESO (fin flujo)************************
	private String pathInconsistencias;
	private HashMap contenidoArchivoInconsistencias = new HashMap();
	private int numeroIngresosRegistrados;
	//*********************************************************************************
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		
		//Parámetros de captura
		this.entidadSubcontratada = "";
		this.entidadesSubcontradas = new ArrayList<HashMap<String,Object>>();
		this.codigoManual = ConstantesBD.codigoNuncaValido;
		this.codigosManuales = new ArrayList<HashMap<String,Object>>();
		this.ubicacionPlanosEntidadesSubcontratadas = "";
		this.directorioArchivos = "";
		this.archivos = new HashMap();
		this.tipoArchivo = "";
		this.nombreArchivo = "";
		this.archivo = null;
		this.numeroFactura = "";
		this.validacionNumeroCarnet = ConstantesBD.acronimoNo;
		
		//Parámetros de salida
		this.pathInconsistencias = "";
		this.contenidoArchivoInconsistencias = new HashMap();
		this.numeroIngresosRegistrados = 0;
		
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
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("ejecutar"))
		{
			if(this.entidadSubcontratada.equals(""))
			{
				errores.add("",new ActionMessage("errors.required","La Entidad Subcontratada"));
			}
			else
			{
				boolean activa = false;
				for(HashMap<String, Object> elemento:this.entidadesSubcontradas)
				{
					if(elemento.get("consecutivo").toString().equals(this.entidadSubcontratada)&&UtilidadTexto.getBoolean(elemento.get("activo")))
					{
						activa = true;
					}
				}
				if(!activa)
				{
					errores.add("", new ActionMessage("errors.seleccion","entidad subcontratada activa"));
				}
			}
			
			if(this.codigoManual==ConstantesBD.codigoNuncaValido)
				errores.add("",new ActionMessage("errors.required","La Codificación Manual de Archivos"));
			
			//Validaciones de la ubicacion del archivo por Servidor
			if(this.ubicacionPlanosEntidadesSubcontratadas.equals(ConstantesIntegridadDominio.acronimoServidor))
			{
				if(this.directorioArchivos.equals(""))
					errores.add("",new ActionMessage("errors.required","La selección del directorio de archivos"));
				else
				{
					File file = new File(this.directorioArchivos);
					
					if(file.exists())
					{
						if(!file.isDirectory())
							errores.add("",new ActionMessage("errors.invalid","El directorio de archivos "));
					}
					else 
						errores.add("",new ActionMessage("errors.noExiste","El directorio de archivos"));
					
				}
			}
			
			if(!errores.isEmpty())
				this.estado = "empezar";
				
		}
		
		
		return errores;
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
	 * @return the codigoManual
	 */
	public int getCodigoManual() {
		return codigoManual;
	}

	/**
	 * @param codigoManual the codigoManual to set
	 */
	public void setCodigoManual(int codigoManual) {
		this.codigoManual = codigoManual;
	}

	


	/**
	 * @return the entidadSubcontratada
	 */
	public String getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	/**
	 * @param entidadSubcontratada the entidadSubcontratada to set
	 */
	public void setEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}

	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}

	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	/**
	 * @return the validacionNumeroCarnet
	 */
	public String getValidacionNumeroCarnet() {
		return validacionNumeroCarnet;
	}

	/**
	 * @param validacionNumeroCarnet the validacionNumeroCarnet to set
	 */
	public void setValidacionNumeroCarnet(String validacionNumeroCarnet) {
		this.validacionNumeroCarnet = validacionNumeroCarnet;
	}

	/**
	 * @return the entidadesSubcontradas
	 */
	public ArrayList<HashMap<String, Object>> getEntidadesSubcontradas() {
		return entidadesSubcontradas;
	}

	/**
	 * @param entidadesSubcontradas the entidadesSubcontradas to set
	 */
	public void setEntidadesSubcontradas(
			ArrayList<HashMap<String, Object>> entidadesSubcontradas) {
		this.entidadesSubcontradas = entidadesSubcontradas;
	}

	/**
	 * @return the codigosManuales
	 */
	public ArrayList<HashMap<String, Object>> getCodigosManuales() {
		return codigosManuales;
	}

	/**
	 * @param codigosManuales the codigosManuales to set
	 */
	public void setCodigosManuales(
			ArrayList<HashMap<String, Object>> codigosManuales) {
		this.codigosManuales = codigosManuales;
	}

	

	/**
	 * @return the directorioArchivos
	 */
	public String getDirectorioArchivos() {
		return directorioArchivos;
	}

	/**
	 * @param directorioArchivos the directorioArchivos to set
	 */
	public void setDirectorioArchivos(String directorioArchivos) {
		this.directorioArchivos = directorioArchivos;
	}

	/**
	 * @return the ubicacionPlanosEntidadesSubcontratadas
	 */
	public String getUbicacionPlanosEntidadesSubcontratadas() {
		return ubicacionPlanosEntidadesSubcontratadas;
	}

	/**
	 * @param ubicacionPlanosEntidadesSubcontratadas the ubicacionPlanosEntidadesSubcontratadas to set
	 */
	public void setUbicacionPlanosEntidadesSubcontratadas(
			String ubicacionPlanosEntidadesSubcontratadas) {
		this.ubicacionPlanosEntidadesSubcontratadas = ubicacionPlanosEntidadesSubcontratadas;
	}

	

	/**
	 * @return the contenidoArchivoInconsistencias
	 */
	public HashMap getContenidoArchivoInconsistencias() {
		return contenidoArchivoInconsistencias;
	}

	/**
	 * @param contenidoArchivoInconsistencias the contenidoArchivoInconsistencias to set
	 */
	public void setContenidoArchivoInconsistencias(
			HashMap contenidoArchivoInconsistencias) {
		this.contenidoArchivoInconsistencias = contenidoArchivoInconsistencias;
	}
	
	/**
	 * @return elemento del mapa contenidoArchivoInconsistencias
	 */
	public Object getContenidoArchivoInconsistencias(String key) {
		return contenidoArchivoInconsistencias.get(key);
	}

	/**
	 * @param Asigna elemento al mapa contenidoArchivoInconsistencias to set
	 */
	public void setContenidoArchivoInconsistencias(String key,Object obj) {
		this.contenidoArchivoInconsistencias.put(key,obj);
	}
	

	/**
	 * @return the pathInconsistencias
	 */
	public String getPathInconsistencias() {
		return pathInconsistencias;
	}

	/**
	 * @param pathInconsistencias the pathInconsistencias to set
	 */
	public void setPathInconsistencias(String pathInconsistencias) {
		this.pathInconsistencias = pathInconsistencias;
	}

	/**
	 * @return the numeroIngresosRegistrados
	 */
	public int getNumeroIngresosRegistrados() {
		return numeroIngresosRegistrados;
	}

	/**
	 * @param numeroIngresosRegistrados the numeroIngresosRegistrados to set
	 */
	public void setNumeroIngresosRegistrados(int numeroIngresosRegistrados) {
		this.numeroIngresosRegistrados = numeroIngresosRegistrados;
	}

	/**
	 * @return the archivos
	 */
	public HashMap getArchivos() {
		return archivos;
	}

	/**
	 * @param archivos the archivos to set
	 */
	public void setArchivos(HashMap archivos) {
		this.archivos = archivos;
	}
	
	/**
	 * @return Elemento del mapa archivos
	 */
	public Object getArchivos(String key) {
		return archivos.get(key);
	}

	/**
	 * @param Asigna elemento al mapa archivos 
	 */
	public void setArchivos(String key,Object obj) {
		this.archivos.put(key, obj);
	}

	/**
	 * @return the tipoArchivo
	 */
	public String getTipoArchivo() {
		return tipoArchivo;
	}

	/**
	 * @param tipoArchivo the tipoArchivo to set
	 */
	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	/**
	 * @return the archivo
	 */
	public FormFile getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(FormFile archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
}
