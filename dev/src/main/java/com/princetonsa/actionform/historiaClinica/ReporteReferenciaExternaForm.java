package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.historiaClinica.ReporteReferenciaExterna;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * Anexo 679
 * Creado el 12 de Septiembre de 2008
 * @author Ing. Felipe Perez
 * @mail lfperez@princetonsa.com
 */

public class ReporteReferenciaExternaForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Loggers de la clase ReporteReferenciaExternaForm
	 */
	Logger logger = Logger.getLogger(ReporteReferenciaExternaForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	//---------------operacion true ------------------------------------------
	public boolean isOperacionTrue() {
		return operacionTrue;
	}
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	//--------------------------------------------------------------------------
	//-------ruta --------------------------------------------------------------
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	//----------------------------------------------------------------------------
	//-------------url archivo----------------------------------------------------
	public String getUrlArchivo() {
		return urlArchivo;
	}
	public void setUrlArchivo(String urlArchivo) {
		this.urlArchivo = urlArchivo;
	}
	//-----------------------------------------------------------------------------
	//--------existe archivo ------------------------------------------------------
	public boolean isExisteArchivo() {
		return existeArchivo;
	}
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}
	//------------------------------------------------------------------------------
	
	
	/*---------------------------------------------------------
	 * 			  FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	/// Indices De Criterios-------------------
	
	String [] indicesCriterios=ReporteReferenciaExterna.indicesCriterios;

	
	/*---------------------------------------------------
	 * 	 ATRIBUTOS PARA EL REPORTE REFERENCIA Externa DE HISTORIA CLINICA
	 ---------------------------------------------------*/
	/**
	 * maneja los estados del action
	 */
	private String estado="";

	/**
	 * Almacena los datos de reporte referencia externa
	 */
	private HashMap criterios = new HashMap ();
	
	/**
	 * Almacena lod centros de atencion a ser mostrados en 
	 * la jsp
	 */
	private ArrayList<HashMap<String, Object>> centrosAtencion= new ArrayList<HashMap<String, Object>>();
	
	/**
	 * Atributo que le indica a la vista si se
	 * genero el archivo plano
	 */
	private boolean operacionTrue;
	
	/**
	 * Atributo que indica donde se almaceno el archivo,
	 * este es para mostrar la ruta excata donde se genero
	 * el archivo dentro del sistema de directorios del
	 * servidor 
	 */
	
	private String ruta;
	
	/**
	 * atributo que indica la direccion para poder
	 * descargar el archivo
	 */
	private String urlArchivo;
	
	/**
	 * Atributo que almacena si el archivo
	 * .ZIP si se genero
	 */
	private boolean existeArchivo=false; 

	/**
	 * Almacena las instituciones sirc
	 */
	private ArrayList<HashMap<String, Object>> institucionesSirc = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Almacena la fecha inicial de la referencia externa
	 */
	private ArrayList<HashMap<String, Object>> fechaInicial = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Controla si se generaron errores en validate para no mostrar el mensaje
	 */
	private boolean errores;
	
	/**
	 * mensaje para validar si hay datos para mostrar en el reporte de archivo plano
	 * @return
	 */
	private boolean mensaje;
	
	/*---------------------------------------------------
	 * 	 FIN ATRIBUTOS PARA EL REPORTE REFERENCIA Externa DE HISTORIA CLINICA
	 ---------------------------------------------------*/
	//---------------------------------------------------*/
	
	//----------------------------------------------------
		public ArrayList<HashMap<String, Object>> fechaInicial() {
			return fechaInicial;
		}
		public void setfechaInicial(
				ArrayList<HashMap<String, Object>> fechaInicial) {
			this.fechaInicial = fechaInicial;
		}
		//-------------------------------------------------
	
	//----------------------------------------------------
	public ArrayList<HashMap<String, Object>> getInstitucionesSirc() {
		return institucionesSirc;
	}
	public void setInstitucionesSirc(
			ArrayList<HashMap<String, Object>> institucionesSirc) {
		this.institucionesSirc = institucionesSirc;
	}
	//-------------------------------------------------
	
	//-----------centro atencion-------------------
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	//-------------------------------
	
	//------------criterios de busqueda--------------------------
	public HashMap getCriterios() {
		return criterios;
	}
	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}
	public Object getCriterios(String key) {
		return criterios.get(key);
	}
	public void setCriterios(String key,Object value) {
		this.criterios.put(key, value);
	}
	//-----------------------------------------------------------
	//--------------------estado------------------------------------
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	//--------------------------------------------------------

	
	/*--------------------------------------------------------
	 * METODOS ADICIONALES
	 --------------------------------------------------------*/
	public void reset ()
	{
		this.criterios = new HashMap ();
		this.urlArchivo="";
		this.errores = false;
	}
	
	public void resetMensaje ()
	{
		this.mensaje= false;
	}
	
	public void setMensaje(boolean mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isMensaje() {
		return mensaje;
	}
	
	/**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}
	/*--------------------------------------------------------
	 * FIN METODOS ADICIONALES
	 --------------------------------------------------------*/
	/**
	 * Control de Errores y Validaciones
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (estado.equals("generar"))
		{
			setOperacionTrue(false);
			boolean fecha=true;
			if(!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[0])+"") && 
				(this.getCriterios(indicesCriterios[0])+"").equals(ConstantesBD.codigoNuncaValido+"" ))
				errores.add("centroAtencion", new ActionMessage("errors.required", "ES NECESARIO SELECCIONAR UN CENTRO DE ATENCIÓN, "));
			
			/*
			 * Validamos el centro de atención
			 * El usuario DEBE de seleccionar uno en especial o la opción de TODOS
			 */
			if(!UtilidadCadena.noEsVacio(this.getCriterios("centroAtencion")+"") || 
			  (this.getCriterios("centroAtencion")+"").equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("centroAtencion", new ActionMessage("errors.required","ES NECESARIO SELECCIONAR UN CENTRO DE ATENCIÓN, "));
				this.errores = true;
			}
			
			/*
			 * Validamos el Campo Tipo de Salida que es Requerido
			 */
			if(!UtilidadCadena.noEsVacio(this.getCriterios("tipoSalida")+"") || 
				(this.getCriterios("tipoSalida")+"").equals(ConstantesBD.codigoNuncaValido+""))
			{
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo de Salida "));
				this.errores = true;
			}
			
			/*
			 * 1)Validacion: Requerido fecha incial, posicion de memoria fecha inicial=2
			 */
			if (!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[2])+""))
			{
				fecha=false;
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Inicial"));
			}
			
			/*
			 * 2)Validacion: La fecha inicial sea menor a la fecha actual del sistema
			 */
			else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriterios(indicesCriterios[2])+"",UtilidadFecha.getFechaActual()))
				{
					fecha=false;
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
							this.getCriterios(indicesCriterios[2]), "Actual "+UtilidadFecha.getFechaActual()));
				}
			
			/*
			 * 3)Validacion: Requerido fecha final, posicion de memoria fecha final=3
			 */
			if (!UtilidadCadena.noEsVacio(this.getCriterios(indicesCriterios[3])+""))
			{
				fecha=false;
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Final"));
			}
			
			/*
			 * 4)Validacion: La fecha final sea menor o igual a la fecha actual
			 */
			else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriterios(indicesCriterios[3])+"",UtilidadFecha.getFechaActual()))
			{
				fecha=false;
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+
						this.getCriterios(indicesCriterios[3]), "Actual "+UtilidadFecha.getFechaActual()));
			}
			
			//5) Validacion: La fecha final debe ser mayor a la fecha incial
			logger.info("\n *** Fecha Final *** -->"+ this.getCriterios(indicesCriterios[3]));
			logger.info("\n *** Fecha Inicial *** -->"+ this.getCriterios(indicesCriterios[2]));
			
			if(!(this.getCriterios(indicesCriterios[2])+"").equals("") && !(this.getCriterios(indicesCriterios[3])+"").equals(""))
			{
				logger.info("\n *** Entré a validar si la fecha viene null ***");
				logger.info("\n *** Aqui vamos a validar las fechas Fecha Final*** -->"+ this.getCriterios(indicesCriterios[3]));
				logger.info("\n *** Aqui vamos a validar las fechas Fecha Inicial*** -->"+ this.getCriterios(indicesCriterios[2]));
				if (UtilidadFecha.numeroMesesEntreFechasExacta(this.getCriterios(indicesCriterios[2])+"", 
						this.getCriterios(indicesCriterios[3])+"") == -1)
				{
					logger.info("\n *** Entré a validar las fechas inicial y final ***");
					logger.info("\n *** Aqui vamos a validar las fechas Fecha Final*** -->"+ this.getCriterios(indicesCriterios[3]));
					logger.info("\n *** Aqui vamos a validar las fechas Fecha Inicial*** -->"+ this.getCriterios(indicesCriterios[2]));
					fecha=false;
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
							this.getCriterios(indicesCriterios[2]), "Final "+this.getCriterios(indicesCriterios[3])+""));
				}
			}
			
			if (fecha)
			{
				logger.info(" El rango de fechas es: --> "+UtilidadFecha.numeroDiasEntreFechas(this.getCriterios(indicesCriterios[2])+"", 
						this.getCriterios(indicesCriterios[3])+""));
				logger.info(" El rango de fechas es: --> "+UtilidadFecha.numeroMesesEntreFechasExacta(this.getCriterios(indicesCriterios[2])+"", 
						this.getCriterios(indicesCriterios[3])+""));
				/*if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getCriterios(indicesCriterios[2])+"", 
				 * this.getCriterios(indicesCriterios[3])+"")>1)
				 */
				if(UtilidadFecha.numeroDiasEntreFechas(this.getCriterios(indicesCriterios[2])+"", this.getCriterios(indicesCriterios[3])+"")>92)
					//En donde el numero final al cual se compara, es el numero de meses del rango.
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "3 Meses"));
			}
		}
		
		return errores;
	}
}