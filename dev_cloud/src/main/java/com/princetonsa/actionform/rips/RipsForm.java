/*
 * Created on Jun 10, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.actionform.rips;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * @author sebacho
 *
 * Formulario para el manejo de la creación de archivos rips
 */
public class RipsForm extends ValidatorForm {
	
	/**
	 * estado del formulario
	 */
	private String estado;
	/**
	 * <code>fechaInicial</code> fecha de elaboracion inicial para la elaboración 
	 * de la información
	 */
	private String fechaInicial;
	
	/**
	 * <code>fechaFinal</code> fecha de elaboracion final para la elaboración 
	 * de la información
	 */
	private String fechaFinal;
	
	/**
	 * <code>numeroCuentaCobro</code> para la cual se elaborará la información  
	 * 
	 */
	private String numeroCuentaCobro;
	

	/**
	 * <code>Convenio</code> para la cual se elaborará la información  
	 * 
	 */
	private String convenio;
	
	/**
	 * fecha en la cual se envían los archivos RIPS a las EPS
	 */
	private String fechaRemision;
	
	/**
	 * nombre de cada uno de los archivos a generar
	 */
	private String numeroRemision;
	
	/**
	 * objeto para manejar la selección de los archivos
	 */
	private HashMap seleccion=new HashMap();
	
	/**
	 * indicador para saber si la generación de archivos es por factura o por cuenta de cobro
	 */
	private boolean esFactura;
	
	/**
	 * objeto que almacena los resultados de la generación de RIPS
	 */
	private HashMap resultados=new HashMap();
	
	/**
	 * Variable donde se almacena el prefijo del archivo que se desea consultar
	 */
	private String archivo;
	
	/**
	 * Objeto usado para almacenar el contenido de un archivo
	 */
	private HashMap contenidoArchivo= new HashMap();
	
	/**
	 * 
	 * Variable indicador de inconsistencias durante la
	 * generación de los archivos RIPS
	 */
	private boolean huboInconsistencias;
	
	/**
	 * Variable para almacenar el path donde se generaron los archivos RIPS
	 */
	private String pathGeneracion;
	
	/**
	 * Codigo Tarifario Oficial con el cual se generará los RIPS
	 */
	private int tipoCodigo;
	
	//******DATOS PARA LA OPCIÓN POR RANGOS DE RIPS CONSULTORIOS************
	
	/**
	 * Variable para indicar cual opción rips se está usando "paciente" o "convenio"
	 * 
	 */
	private String opcionRips;	
	/**
	 * Variable para guardar los registros de la búsqueda por rangos
	 */
	private HashMap registrosRangos=new HashMap();
	
	/**
	 * Variable para almacenar el número de factura
	 */
	private String numeroFactura;
	
	/**
	 * Variable para almacenar la fecha de la factura
	 */
	private String fechaFactura;
	
	/**
	 * Variable usada para la paginación del listado
	 */
	private String linkSiguiente;
	
	/**
	 * Datos para la ordenación
	 * 
	 */
	private String indice;
	private String ultimoIndice;
	
	/**
	 * Variables usadas para el popUp de la informaciónRips de la cita
	 * 
	 */
	private int tipoSolicitud;
	private int numeroSolicitud;
	/**
	 * objeto para almacenar temporalmente los datos RIPS de un registro
	 */
	private HashMap datosRips=new HashMap();
	/**
	 * posición actual del hashmap registrosRangos donde se está ingresando la información RIPS
	 */
	private int posicionHashMap;
	/**
	 * variable que me permite saber si el registro tiene un servicio asociado
	 */
	private boolean tieneServicio;
	/**
	 * 
	 * Variable para almacenar el codigo CUPS del servicio que se va a consultar
	 */
	private String codigoCups;
	
	/**
	 * Variable para almacenar la descripcion del servicio que se va a consultar
	 */
	private String nombreServicio;
	
	/**
	 * Variable para almacenar el estado de la búsqueda del servicio
	 */
	private String estadoBusquedaServicio;
	/**
	 * 
	 * Variable usada para registros 'Otros' que necesitan relacionar la posicion padre
	 */
	private int posicionPadre;
	/**
	 * Variable para indicar el tipo de registro que se está manejando
	 * 1=> Cita
	 * 2=> No cita, con servicio asociado
	 * 3=> solicitud sin servicio o no solicitud u otro.
	 */
	private int tipoRegistro;
	
	/**
	 * Variable que indica la opción de búsqueda del servicio
	 * "codigo" => busqueda por código CUPS
	 * "nombre" => búsqueda por nombre del servicio
	 */
	private String opcionBusquedaServicio;
	
	
	
	/**
	 * Variable de numero de envio, utilizada para la consulta de facturas en la tabla de interfaz ax_rips
	 */
	private String numeroEnvio;
	
	
	
	/**
	 * Variable que guarda el numero de la factura
	 */
	private HashMap nroFacturaAx;
	
	
	private ArrayList<HashMap> resultadoArray;
	
	
	/**
	 * Variable de Path para la generacion de archivos forecat
	 */
	private String pathGeneracionForecat;
	
	
	/**
	 * Mapa de recepcion de resultados de generacion de archivos forecat
	 */
	private HashMap<String, Object> resultadosForecat;
	
	
	/**
	 * Variable que se maneja como bandera de informacion referente a la generacion  o no de archivos forecat 
	 */
	private boolean generoArchvsForecat;
	
	/**
	 * Para guardar si se genero el .zip o no??
	 */
	private boolean zip;
	
	/**
	 * String para capturar el nombre del Archivo Backup
	 */
	private String backupArchivo;
	
	/**
	 * Para almacenar el NIt de la entidad asociada al convenio
	 */
	private String nitEntidad;
	
	
	//**************************************************
	/**
	 * @return Returns the esFactura.
	 */
	public boolean isEsFactura() {
		return esFactura;
	}
	/**
	 * @param esFactura The esFactura to set.
	 */
	public void setEsFactura(boolean esFactura) {
		this.esFactura = esFactura;
	}
	
	/**
	 * Limpia el Formulario
	 */
	public void reset()
	{
		this.estado="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.fechaRemision="";
		this.numeroCuentaCobro="";
		this.numeroRemision="";
		this.convenio="0-0";
		this.seleccion=new HashMap();
		this.esFactura=false;
		this.resultados=new HashMap();
		this.archivo="";
		this.contenidoArchivo=new HashMap();
		this.huboInconsistencias=false;
		this.registrosRangos=new HashMap();
		this.linkSiguiente="";
		this.indice="";
		this.ultimoIndice="";
		this.tipoSolicitud=0;
		this.numeroSolicitud=0;
		this.datosRips=new HashMap();
		this.posicionHashMap=0;
		this.tieneServicio=false;
		this.codigoCups="";
		this.posicionPadre=-1;
		this.tipoRegistro=0;
		this.nombreServicio="";
		this.estadoBusquedaServicio="";
		this.numeroFactura="";
		this.fechaFactura="";
		this.pathGeneracion="";
		this.opcionBusquedaServicio="";
		this.opcionRips="";
		this.tipoCodigo = -1;
		this.numeroEnvio = "";
		this.nroFacturaAx = new HashMap();
		this.pathGeneracionForecat = "";
		this.resultadosForecat = new HashMap<String, Object>();
		this.generoArchvsForecat = false;
		this.backupArchivo = "";
		this.zip = false;
		this.resultadoArray = new ArrayList<HashMap>();
		this.nitEntidad="";
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
			
			
			// ****************************************************************************************************************
			// verificar que el campo de numero de envio no este tramitado y por ende los campos esten activos para su tramite.
			// ****************************************************************************************************************
			if (this.numeroEnvio.equals("")){
				//*****VALIDACIÓN CONVENIOS**********
				if(this.convenio.equals("0-0"))
					errores.add("sin convenio", new ActionMessage("errors.required","El Convenio"));
				
				//****VALIDACION DE TIPO CODIGO********
				if(this.tipoCodigo<0)
					errores.add("sin convenio", new ActionMessage("errors.required","El Tipo Código"));
				
				
				//*****VALIDACIÓN DE CUENTA DE COBRO**********
				if(!esFactura)
				{
					if(this.numeroCuentaCobro.equals(""))
						errores.add("sin numero de cuenta de cobro", new ActionMessage("errors.required","La cuenta de cobro"));
				}
				
				//****VALIDACIÓN FECHAS*************
				//indicadores de validación
				if(esFactura)
				{
					errores=this.revisionFechas(errores);
				}
				
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
				{
					errores.add("El número de remisión", new ActionMessage("errors.required", "El número de remisión"));
				}
			}
			else
			{
				// asignar el numero de remision igual al numero de envio
				this.setNumeroRemision(this.getNumeroEnvio());
				// como fecha de remision asignar la fecha actual del sistema
				this.setFechaRemision(UtilidadFecha.getFechaActual());
			}
			
			
		}
		else if(estado.equals("busquedaRangos"))
		{
			//******VALIDACIÓN DEL CONVENIO************************
			if(this.convenio.equals("0-0"))
				errores.add("sin convenio", new ActionMessage("errors.required","El Convenio"));
			//******VALIDACIÓN DE LOS RANGOS DE LAS FECHAS**********
			errores=this.revisionFechas1(errores);
			
			if(!errores.isEmpty())
				this.estado="empezar";
		}
		else if(estado.equals("generarConsultorios"))
		{
			//*******VALIDACIÓN DEL  NÚMERO DE LA FACTURA********
			if(this.numeroFactura.equals(""))
				errores.add("sin número de factura", new ActionMessage("errors.required","El número de la factura"));
			//*****VALIDACIÓN DE LA FECHA DE FACTURA*********
			//se valida que se haya ingresado
			if(this.fechaFactura.equals(""))
			{
				errores.add("sin fecha de factura", new ActionMessage("errors.required","La fecha de la factura"));
			}
			else
			{
				//se valida que tenga un formato válido
				if(!UtilidadFecha.validarFecha(this.fechaFactura))
				{
					errores.add("fecha factura", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFactura()));
				}
				else
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFactura())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
						errores.add("fecha factura", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "fecha factura", "actual"));
				}
			}
			//********VALIDACIÓN FECHA DE REMISIÓN*****************
			if(!fechaRemision.equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaRemision()))
					errores.add("fecha remisión", new ActionMessage("errors.formatoFechaInvalido",this.getFechaRemision()));
			}
			else
			{
				errores.add("La fecha de remisión", new ActionMessage("errors.required", "La fecha de remisión"));
			}
			
			if(!errores.isEmpty())
				this.estado="busquedaRangos";
		}
		else if(estado.equals("resultadoConsulta"))
		{
			int errorConv=0;
			int errorFecha=0;
			//VALIDACIÓN CONVENIO*************************
			if(this.convenio.equals("0-0"))
				errorConv=1;
			//VALIDACIÓN FECHA DE LA FACTURA**************
			if(this.fechaFactura.equals(""))
				errorFecha=1;
			
			//si hay fecha de factura se debe validar que sea válida
			if(errorFecha==0)
			{
				//se valida que tenga un formato válido
				if(!UtilidadFecha.validarFecha(this.fechaFactura))
					errores.add("fecha factura", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFactura()));
			}
			
			//se valida si los campos están vacíos
			if(errorConv==1&&errorFecha==1)
				errores.add("sin campos", new ActionMessage("errors.required","Alguno de los campos de búsqueda "));
			
			if(!errores.isEmpty())
				this.estado="consultar";
		}
		
		return errores;
	}
	
	/**
	 * Método para la revisión de los rangos de las fechas
	 * @param errores
	 * @return
	 */
	private ActionErrors revisionFechas1(ActionErrors errores) {
		int resp1=0;
		int resp2=0;
		
		if(!this.fechaInicial.equals(""))
		{
			resp1=1;
			if(UtilidadFecha.validarFecha(this.getFechaInicial()))
				resp1=2;
			else
				errores.add("fecha inicial de generación", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
		}
		
		if(!this.fechaFinal.equals(""))
		{
			resp2=1;
			if(UtilidadFecha.validarFecha(this.getFechaFinal()))
				resp2=2;
			else
				errores.add("fecha final de generación", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
		}
		
		//revisar si las fechas son válidas
		if(resp1==2&&resp2==2)
		{
			//si la fecha inicial es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha inicial de generación", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de generación", "del sistema"));
			}
			
			//si la fecha final es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha final de generación", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final de generación", "del sistema"));
			}
			
			//si la fecha inicial es mayor a la fecha final
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal()))>0)
			{
				errores.add("fecha inicial mayor a la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de generación", "final de generación"));
			}
		}
		else
		{
			//caso en el que falte alguna fecha del rango
			if(resp1==0&&resp2>0)
			{
				errores.add("La fecha inicial de generación", new ActionMessage("errors.required", "La fecha inicial de generación"));
			}
			
			if(resp2==0&&resp1>0)
			{
				errores.add("La fecha final de generación", new ActionMessage("errors.required", "La fecha final de generación"));
			}
			
			if(resp1==0&&resp2==0)
			{
				errores.add("El rango de fechas de generación", new ActionMessage("errors.required", "El rango de fechas de generación"));
			}
		}
		return errores;
	}

	/**
	 * Método para la revisión de los rangos de las fechas
	 * @param errores
	 * @return
	 */
	private ActionErrors revisionFechas(ActionErrors errores) {
		int resp1=0;
		int resp2=0;
		
		if(!this.fechaInicial.equals(""))
		{
			resp1=1;
			if(UtilidadFecha.validarFecha(this.getFechaInicial()))
				resp1=2;
			else
				errores.add("fecha elaboracion inicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
		}
		
		if(!this.fechaFinal.equals(""))
		{
			resp2=1;
			if(UtilidadFecha.validarFecha(this.getFechaFinal()))
				resp2=2;
			else
				errores.add("fecha elaboracion final", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
		}
		
		//revisar si las fechas son válidas
		if(resp1==2&&resp2==2)
		{
			//si la fecha inicial es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha elaboración inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de elaboración", "del sistema"));
			}
			
			//si la fecha final es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha elaboración final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final de elaboración", "del sistema"));
			}
			
			//si la fecha inicial es mayor a la fecha final
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal()))>0)
			{
				errores.add("fecha inicial mayor a la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de elaboración", "final de elaboración"));
			}
		}
		else
		{
			//caso en el que falte alguna fecha del rango
			if(resp1==0&&resp2>0)
			{
				errores.add("La Fecha Elaboración Inicial", new ActionMessage("errors.required", "La fecha inicial de elaboración"));
			}
			
			if(resp2==0&&resp1>0)
			{
				errores.add("La Fecha Elaboración Final", new ActionMessage("errors.required", "La fecha final de elaboración"));
			}
			
			if(resp1==0&&resp2==0)
			{
				errores.add("El rango de fechas de elaboración", new ActionMessage("errors.required", "El rango de fechas de elaboración"));
			}
		}
		return errores;
	}
	
	/**
	 * @return the nitEntidad
	 */
	public String getNitEntidad() {
		return nitEntidad;
	}
	/**
	 * @param nitEntidad the nitEntidad to set
	 */
	public void setNitEntidad(String nitEntidad) {
		this.nitEntidad = nitEntidad;
	}
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Returns the nombreConvenio.
	 */
	public String getConvenio() {
		return convenio;
	}
	/**
	 * @param nombreConvenio The nombreConvenio to set.
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the numeroCuentaCobro.
	 */
	public String getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}
	/**
	 * @param numeroCuentaCobro The numeroCuentaCobro to set.
	 */
	public void setNumeroCuentaCobro(String numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}
	/**
	 * @return Returns the fechaRemision.
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}
	/**
	 * @param fechaRemision The fechaRemision to set.
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}
	/**
	 * @return Returns the numeroRemision.
	 */
	public String getNumeroRemision() {
		return numeroRemision;
	}
	/**
	 * @param numeroRemision The numeroRemision to set.
	 */
	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}
	/**
	 * @return Returns the seleccion.
	 */
	public HashMap getSeleccion() {
		return seleccion;
	}
	/**
	 * @param seleccion The seleccion to set.
	 */
	public void setSeleccion(HashMap seleccion) {
		this.seleccion = seleccion;
	}
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getSeleccion(String key) 
	{
		return seleccion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setSeleccion(String key, Object value) 
	{
	    seleccion.put(key, value);
	}
	
	/**
	 * @return Returns the resultados.
	 */
	public HashMap getResultados() {
		return resultados;
	}
	/**
	 * @param resultados The resultados to set.
	 */
	public void setResultados(HashMap resultados) {
		this.resultados = resultados;
	}
	
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getResultados(String key) 
	{
		return resultados.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setResultados(String key, Object value) 
	{
	    resultados.put(key, value);
	}
	/**
	 * @return Returns the archivo.
	 */
	public String getArchivo() {
		return archivo;
	}
	/**
	 * @param archivo The archivo to set.
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}
	/**
	 * @return Returns the contenidoArchivo.
	 */
	public HashMap getContenidoArchivo() {
		return contenidoArchivo;
	}
	/**
	 * @param contenidoArchivo The contenidoArchivo to set.
	 */
	public void setContenidoArchivo(HashMap contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}
	/**
	 * @return Returns the huboInconsistencias.
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}
	/**
	 * @param huboInconsistencias The huboInconsistencias to set.
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}
	
	/**
	 * @return Returns the registrosRangos.
	 */
	public HashMap getRegistrosRangos() {
		return registrosRangos;
	}
	
	/**
	 * @param registrosRangos The registrosRangos to set.
	 */
	public void setRegistrosRangos(HashMap registrosRangos) {
		this.registrosRangos = registrosRangos;
	}
	
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getRegistrosRangos(String key) 
	{
		return registrosRangos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setRegistrosRangos(String key, Object value) 
	{
	    registrosRangos.put(key, value);
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the tipoSolicitud.
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}
	/**
	 * @param tipoSolicitud The tipoSolicitud to set.
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	/**
	 * @return Returns the datosRips.
	 */
	public HashMap getDatosRips() {
		return datosRips;
	}
	/**
	 * @param datosRips The datosRips to set.
	 */
	public void setDatosRips(HashMap datosRips) {
		this.datosRips = datosRips;
	}
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getDatosRips(String key) 
	{
		return datosRips.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setDatosRips(String key, Object value) 
	{
	    datosRips.put(key, value);
	}
	/**
	 * @return Returns the posicionHashMap.
	 */
	public int getPosicionHashMap() {
		return posicionHashMap;
	}
	/**
	 * @param posicionHashMap The posicionHashMap to set.
	 */
	public void setPosicionHashMap(int posicionHashMap) {
		this.posicionHashMap = posicionHashMap;
	}
	/**
	 * @return Returns the tieneServicio.
	 */
	public boolean isTieneServicio() {
		return tieneServicio;
	}
	/**
	 * @param tieneServicio The tieneServicio to set.
	 */
	public void setTieneServicio(boolean tieneServicio) {
		this.tieneServicio = tieneServicio;
	}
	/**
	 * @return Returns the codigoCups.
	 */
	public String getCodigoCups() {
		return codigoCups;
	}
	/**
	 * @param codigoCups The codigoCups to set.
	 */
	public void setCodigoCups(String codigoCups) {
		this.codigoCups = codigoCups;
	}
	/**
	 * @return Returns the posicionPadre.
	 */
	public int getPosicionPadre() {
		return posicionPadre;
	}
	/**
	 * @param posicionPadre The posicionPadre to set.
	 */
	public void setPosicionPadre(int posicionPadre) {
		this.posicionPadre = posicionPadre;
	}
	/**
	 * @return Returns the tipoRegistro.
	 */
	public int getTipoRegistro() {
		return tipoRegistro;
	}
	/**
	 * @param tipoRegistro The tipoRegistro to set.
	 */
	public void setTipoRegistro(int tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	/**
	 * @return Returns the nombreServicio.
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}
	/**
	 * @param nombreServicio The nombreServicio to set.
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	/**
	 * @return Returns the estadoBusquedaServicio.
	 */
	public String getEstadoBusquedaServicio() {
		return estadoBusquedaServicio;
	}
	/**
	 * @param estadoBusquedaServicio The estadoBusquedaServicio to set.
	 */
	public void setEstadoBusquedaServicio(String estadoBusquedaServicio) {
		this.estadoBusquedaServicio = estadoBusquedaServicio;
	}
	/**
	 * @return Returns the fechaFactura.
	 */
	public String getFechaFactura() {
		return fechaFactura;
	}
	/**
	 * @param fechaFactura The fechaFactura to set.
	 */
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	/**
	 * @return Returns the numeroFactura.
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}
	/**
	 * @param numeroFactura The numeroFactura to set.
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	/**
	 * @return Returns the pathGeneracion.
	 */
	public String getPathGeneracion() {
		return pathGeneracion;
	}
	/**
	 * @param pathGeneracion The pathGeneracion to set.
	 */
	public void setPathGeneracion(String pathGeneracion) {
		this.pathGeneracion = pathGeneracion;
	}
	/**
	 * @return Returns the opcionBusquedaServicio.
	 */
	public String getOpcionBusquedaServicio() {
		return opcionBusquedaServicio;
	}
	/**
	 * @param opcionBusquedaServicio The opcionBusquedaServicio to set.
	 */
	public void setOpcionBusquedaServicio(String opcionBusquedaServicio) {
		this.opcionBusquedaServicio = opcionBusquedaServicio;
	}
	/**
	 * @return Returns the opcionRips.
	 */
	public String getOpcionRips() {
		return opcionRips;
	}
	/**
	 * @param opcionRips The opcionRips to set.
	 */
	public void setOpcionRips(String opcionRips) {
		this.opcionRips = opcionRips;
	}
	/**
	 * @return Returns the tipoCodigo.
	 */
	public int getTipoCodigo() {
		return tipoCodigo;
	}
	/**
	 * @param tipoCodigo The tipoCodigo to set.
	 */
	public void setTipoCodigo(int tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}
	/**
	 * @return the numeroEnvio
	 */
	public String getNumeroEnvio() {
		return numeroEnvio;
	}
	/**
	 * @param numeroEnvio the numeroEnvio to set
	 */
	public void setNumeroEnvio(String numeroEnvio) {
		this.numeroEnvio = numeroEnvio;
	}
	
	/**
	 * @return the pathGeneracionForecat
	 */
	public String getPathGeneracionForecat() {
		return pathGeneracionForecat;
	}
	/**
	 * @param pathGeneracionForecat the pathGeneracionForecat to set
	 */
	public void setPathGeneracionForecat(String pathGeneracionForecat) {
		this.pathGeneracionForecat = pathGeneracionForecat;
	}
	/**
	 * @return the resultadosForecat
	 */
	public HashMap<String, Object> getResultadosForecat() {
		return resultadosForecat;
	}
	
	
	/**
	 * @return the Key resultadosForecat
	 */
	public Object getResultadosForecat(String key) {
		return resultadosForecat.get("numRegsitros");
	}
	
	/**
	 * @param resultadosForecat the resultadosForecat to set
	 */
	public void setResultadosForecat(HashMap<String, Object> resultadosForecat) {
		this.resultadosForecat = resultadosForecat;
	}
	/**
	 * @return the generoArchvsForecat
	 */
	public boolean isGeneroArchvsForecat() {
		return generoArchvsForecat;
	}
	/**
	 * @param generoArchvsForecat the generoArchvsForecat to set
	 */
	public void setGeneroArchvsForecat(boolean generoArchvsForecat) {
		this.generoArchvsForecat = generoArchvsForecat;
	}
	public String getBackupArchivo() {
		return backupArchivo;
	}
	public void setBackupArchivo(String backupArchivo) {
		this.backupArchivo = backupArchivo;
	}
	/**
	 * @return the zip
	 */
	public boolean isZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}
	/**
	 * @return the nroFacturaAx
	 */
	public HashMap getNroFacturaAx() {
		return nroFacturaAx;
	}
	
	public Object getNroFacturaAx(String key) {
		return nroFacturaAx.get(key);
	}
	
	/**
	 * @param nroFacturaAx the nroFacturaAx to set
	 */
	public void setNroFacturaAx(HashMap nroFacturaAx) {
		this.nroFacturaAx = nroFacturaAx;
	}
	

	public void setNroFacturaAx(String key, Object values) {
		this.nroFacturaAx.put(key, values);
	}
	
	/**
	 * @return the resultadoArray
	 */
	public ArrayList<HashMap> getResultadoArray() {
		return resultadoArray;
	}
	/**
	 * @param resultadoArray the resultadoArray to set
	 */
	public void setResultadoArray(ArrayList<HashMap> resultadoArray) {
		this.resultadoArray = resultadoArray;
	}
	
	
}
