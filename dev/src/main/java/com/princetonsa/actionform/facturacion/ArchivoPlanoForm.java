package com.princetonsa.actionform.facturacion;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.agendaProcedimiento.ExamenCondiTomaForm;
import com.princetonsa.dto.interfaz.DtoInterfazAxRips;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class ArchivoPlanoForm extends ValidatorForm 
{
	
	//--------------------Atributos
	private Logger logger = Logger.getLogger(ArchivoPlanoForm.class);
	
	
	//------Action 
	
	//Estado de la forma 
	private  String estado;
	
	//------Fin Action
	
	//-------Archivos Planos
	
	//HasMap de la consulta de Archivos Planos
	private HashMap busquedaArchivosPlanosMap;
	
	//HashMap de Numeros de Envio
	private HashMap numeroEnviosMap;	
	
	//Estructura de interfaz RIPS
	private ArrayList<DtoInterfazAxRips> interfazRips;
	
	//ArrayList Convenios
	private ArrayList<HashMap<String,Object>> convenioArray;
	
	//Cuentas de cobro
	private String numeroCuentaCobro;
	
	//Tipo Consulta. Indica la manera en que se generara el archivo plano Colsanitas. (1. a partir del numero de Envio, 2. a partir del convenio, 3. a partir de la cuenta de cobro)
	private int tipoConsuta;
	
	//HashMap de Nombres de Archivos
	private HashMap nombreArchivosPlanosMap;
	
	//HashMap que contiene la informacion de los archivos Generados
	private HashMap dataFile;
	
	//HashMap que contiene el contenido de un archivo ya generado 
	private HashMap contenidoArchivo;
	
	//--------------------Metodos
	
	
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
    	errores = super.validate(mapping, request);
    	
    	
    	if(this.estado.equals("ingresarNumeroEnvio"))
    	{  	   		
    		//verifica que no exitan numeros de Envio Repetidos
    		int posArray = Integer.parseInt(this.getNumeroEnviosMap("indicadorPos").toString());
    		int numRegistros = Integer.parseInt(this.getNumeroEnviosMap("numRegistros").toString());
    		
    		if(posArray < 0)    			
    			errores.add("descripcion",new ActionMessage("errors.required","Indique el Numero de Envio. Numero Envio "));
    		else
    		{   		
				for(int i = 0; i< numRegistros; i++)
					if(this.getNumeroEnviosMap("indexArray_"+i).equals(posArray))
						errores.add("descripcion",new ActionMessage("errors.invalid","El Numero de Envio Ya fue Seleccionado. Numero de Envio "+(this.getNumeroEnviosMap("numeroEnvio_"+i).toString()+" - "+this.getNumeroEnviosMap("descripcionConvenio_"+i).toString())));						
    		}	
    		
    	}
    	else if(this.estado.equals("generarArchPlanoColsa") || this.estado.equals("preGenerarArchPlanoColsa"))
    	{
    		//se verifica el estado de la interfazRips
    		if(this.getBusquedaArchivosPlanosMap("interfazRips").toString().equals(ConstantesBD.acronimoNo))
    		{      			
    			logger.info("valor convenio > "+this.getBusquedaArchivosPlanosMap("convenio").toString());
    			
    			if(!this.getBusquedaArchivosPlanosMap("convenio").toString().equals(ConstantesBD.codigoNuncaValido+""))
    			{
    				//Tipo de Consutla por Convenio
    				this.tipoConsuta = 2;
    				
    				//validaciones de la fecha final e inicial
    				if(this.getBusquedaArchivosPlanosMap("fechaFinal").equals("") || this.getBusquedaArchivosPlanosMap("fechaInicial").equals(""))
    				{
    					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y Final "));
    				}
    				else
    				{
    					if(!UtilidadFecha.validarFecha(this.getBusquedaArchivosPlanosMap("fechaInicial").toString()))
    						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial "+this.getBusquedaArchivosPlanosMap("fechaInicial").toString()+" "));
    					
    					if(!UtilidadFecha.validarFecha(this.getBusquedaArchivosPlanosMap("fechaFinal").toString()))
    						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final "+this.getBusquedaArchivosPlanosMap("fechaFinal").toString()+" "));
    					
    					
    					if(!UtilidadFecha.compararFechas(this.getBusquedaArchivosPlanosMap("fechaFinal").toString(),"00:00",this.getBusquedaArchivosPlanosMap("fechaInicial").toString(),"00:00").isTrue())
    						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial "+this.getBusquedaArchivosPlanosMap("fechaInicial").toString()," Final "+this.getBusquedaArchivosPlanosMap("fechaFinal").toString()));
    					else
    					{    					
	    					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getBusquedaArchivosPlanosMap("fechaInicial").toString(), "00:00").isTrue())
	    					 	errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getBusquedaArchivosPlanosMap("fechaInicial").toString(),UtilidadFecha.getFechaActual()));
	    					
	    					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getBusquedaArchivosPlanosMap("fechaFinal").toString(), "00:00").isTrue())
	    						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getBusquedaArchivosPlanosMap("fechaFinal").toString(),UtilidadFecha.getFechaActual()));
    					}
    				}    				
    			}
    			else if(getBusquedaArchivosPlanosMap("convenio").equals(ConstantesBD.codigoNuncaValido+""))
    			{
    				//Tipo de Consulta por Numero de Cuenta de Cobro
    				this.tipoConsuta = 3;
    				
    				if(this.getBusquedaArchivosPlanosMap("numeroCuentaCobro").equals(""))
    					errores.add("descripcion",new ActionMessage("errors.required","El Numero de Cuenta de Cobro "));
    			}   			
    		}
    		else
    		{
    			//Tipo de Consulta por Numero de Envio
    			this.tipoConsuta = 1;
    			
    			int numRegistros = Integer.parseInt(this.getNumeroEnviosMap("numRegistros").toString());
    			
    			if (numRegistros <= 0)
    				errores.add("descripcion",new ActionMessage("errors.required","Número de Envío "));   		    			
    		}
    		
    		
    		//validaciones Comunes
			if(this.getBusquedaArchivosPlanosMap("fechaEnvio").toString().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Envio "));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getBusquedaArchivosPlanosMap("fechaEnvio").toString()))				
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Envio "+this.getBusquedaArchivosPlanosMap("fechaEnvio").toString()+" "));				
				else
				{
					if(!UtilidadFecha.compararFechas(this.getBusquedaArchivosPlanosMap("fechaEnvio").toString(),"00:00",UtilidadFecha.getFechaActual(),"00:00").isTrue())
					 	errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","de Envio "+this.getBusquedaArchivosPlanosMap("fechaEnvio").toString(),UtilidadFecha.getFechaActual()));					
				}
				
				
			}
			
			if(!this.getBusquedaArchivosPlanosMap("secuencia").equals(""))
			{
				if(Utilidades.convertirAEntero(this.getBusquedaArchivosPlanosMap("secuencia").toString()) == ConstantesBD.codigoNuncaValido) 
					errores.add("descripcion",new ActionMessage("errors.invalid","Secuencia debe ser Numerica Mayor a Cero. La Secuencia "+this.getBusquedaArchivosPlanosMap("secuencia").toString()+" "));
				else
				{				
					if(Integer.parseInt(this.getBusquedaArchivosPlanosMap("secuencia").toString()) <= 0)
						errores.add("descripcion",new ActionMessage("errors.invalid","Secuencia debe ser Mayor a Cero. La Secuencia "+this.getBusquedaArchivosPlanosMap("secuencia").toString()+" "));
				}
			}
			else
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Secuencia "));
			}
			
			if(this.getBusquedaArchivosPlanosMap("ruta").equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Ruta "));
			else
			{    				
				if(!UtilidadFileUpload.validarExistePath(this.getBusquedaArchivosPlanosMap("ruta").toString()))
					errores.add("descripcion",new ActionMessage("errors.invalid","Verifique que Exista la Ruta. Ruta "));    					
			}
    		
    	}
	
    	return errores;
	}
	
		
	
	
	public void reset()
	{
		this.busquedaArchivosPlanosMap = new HashMap();
		this.numeroEnviosMap = new HashMap();
		this.convenioArray = new ArrayList<HashMap<String,Object>>();
		this.interfazRips = new ArrayList<DtoInterfazAxRips>();
		this.tipoConsuta = 0;	
		this.nombreArchivosPlanosMap = new HashMap();
		this.dataFile = new HashMap();
		this.contenidoArchivo = new HashMap();
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
	 * @return the bsuquedaArchivosPlanosMap
	 */
	public HashMap getBusquedaArchivosPlanosMap() {
		return busquedaArchivosPlanosMap;
	}


	/**
	 * @param bsuquedaArchivosPlanosMap the bsuquedaArchivosPlanosMap to set
	 */
	public void setBusquedaArchivosPlanosMap(HashMap bsuquedaArchivosPlanosMap) {
		this.busquedaArchivosPlanosMap = bsuquedaArchivosPlanosMap;
	}
	
	/**
	 * @return the bsuquedaArchivosPlanosMap
	 */
	public Object getBusquedaArchivosPlanosMap(String key) {
		return busquedaArchivosPlanosMap.get(key);
	}


	/**
	 * @param bsuquedaArchivosPlanosMap the bsuquedaArchivosPlanosMap to set
	 */
	public void setBusquedaArchivosPlanosMap(String key,Object value) {
		this.busquedaArchivosPlanosMap.put(key, value);
	}


	/**
	 * @return the numeroEnviosMap
	 */
	public HashMap getNumeroEnviosMap() {
		return numeroEnviosMap;
	}


	/**
	 * @param numeroEnviosMap the numeroEnviosMap to set
	 */
	public void setNumeroEnviosMap(HashMap numeroEnviosMap) {
		this.numeroEnviosMap = numeroEnviosMap;
	}
	
	/**
	 * @return the numeroEnviosMap
	 */
	public Object getNumeroEnviosMap(String key) {
		return numeroEnviosMap.get(key);
	}


	/**
	 * @param numeroEnviosMap the numeroEnviosMap to set
	 */
	public void setNumeroEnviosMap(String key, Object value) {
		this.numeroEnviosMap.put(key, value);
	}


	/**
	 * @return the convenioArray
	 */
	public ArrayList<HashMap<String, Object>> getConvenioArray() {
		return convenioArray;
	}


	/**
	 * @param convenioArray the convenioArray to set
	 */
	public void setConvenioArray(ArrayList<HashMap<String, Object>> convenioArray) {
		this.convenioArray = convenioArray;
	}


	/**
	 * @return the interfazRips
	 */
	public ArrayList<DtoInterfazAxRips> getInterfazRips() {
		return interfazRips;
	}


	/**
	 * @param interfazRips the interfazRips to set
	 */
	public void setInterfazRips(ArrayList<DtoInterfazAxRips> interfazRips) {
		this.interfazRips = interfazRips;
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
	 * @param tipoConsuta the tipoConsuta to set
	 */
	public void setTipoConsuta(int tipoConsuta) {
		this.tipoConsuta = tipoConsuta;
	}




	/**
	 * @return the tipoConsuta
	 */
	public int getTipoConsuta() {
		return tipoConsuta;
	}




	/**
	 * @return the nombreArchivosPlanosMap
	 */
	public HashMap getNombreArchivosPlanosMap() {
		return nombreArchivosPlanosMap;
	}



	/**
	 * @param nombreArchivosPlanosMap the nombreArchivosPlanosMap to set
	 */
	public void setNombreArchivosPlanosMap(HashMap nombreArchivosPlanosMap) {
		this.nombreArchivosPlanosMap = nombreArchivosPlanosMap;
	}
	
	
	/**
	 * @return the nombreArchivosPlanosMap
	 */
	public Object getNombreArchivosPlanosMap(String key) {
		return nombreArchivosPlanosMap.get(key);
	}



	/**
	 * @param nombreArchivosPlanosMap the nombreArchivosPlanosMap to set
	 */
	public void setNombreArchivosPlanosMap(String key, Object value) {
		this.nombreArchivosPlanosMap.put(key, value);
	}



	/**
	 * @return the dataFile
	 */
	public HashMap getDataFile() {
		return dataFile;
	}

	/**
	 * @param dataFile the dataFile to set
	 */
	public void setDataFile(HashMap dataFile) {
		this.dataFile = dataFile;
	}
	
	/**
	 * @return the dataFile
	 */
	public Object getDataFile(String key) {
		return dataFile.get(key);
	}

	/**
	 * @param dataFile the dataFile to set
	 */
	public void setDataFile(String key, Object value) {
		this.dataFile.put(key, value);
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
	 * @return the contenidoArchivo
	 */
	public Object getContenidoArchivo(String key) {
		return contenidoArchivo.get(key);
	}




	/**
	 * @param contenidoArchivo the contenidoArchivo to set
	 */
	public void setContenidoArchivo(String key, Object value) {
		this.contenidoArchivo.put(key, value);
	}
}