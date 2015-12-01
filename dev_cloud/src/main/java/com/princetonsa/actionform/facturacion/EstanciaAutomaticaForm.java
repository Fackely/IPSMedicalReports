/*
 * Created on Aug 19, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EstanciaAutomaticaForm extends ActionForm {
	
	/**
	 * Variable que maneja el estado del controlador
	 */
	private String estado;
	
	/**
	 * Variable usada para manejar el estado de la JSP
	 */
	private String estadoPagina;
	
	/**
	 * variable que almacena codigo-nombre del centro de Costo
	 */
	private String centroCosto;
	
	/**
	 * variable que almacena el rango de fechas para la estancia inicial
	 * y final
	 */
	private String fechaInicialEstancia;
	private String fechaFinalEstancia;
	
	/**
	 * Objeto donde se almacenan las cuentas de hospitalizacion
	 * a las cuales se les va a generar la solicitud de estancia
	 */
	HashMap cuentasEstancia=new HashMap();
	
	/**
	 * Cadena que almacena la ruta del archivo de Inconsistencias
	 */
	private String pathArchivoInconsistencias;
	
	/**
	 * Fechas usadas para validar las fechas parametrizadas en la
	 * estancia por Paciente
	 */
	private String fechaAdmision;
	private String fechaOrdenSalida;
	
	/**
	 * Centro de Atención
	 */
	private int codigoCentroAtencion;
	
	/**
	 * Mapa donde se almacena el archivo de inconsistencias
	 */
	private HashMap inconsistencias = new HashMap();
	
	/**
	 * Numero de lineas del mapa inconsistencias
	 */
	private int numInconsistencias;
	
	//*****ATRIBUTOS USADOS PARA LA CONSULTA LOG DE ESTANCIA AUTOMATICA******
	/**
	 * Parámetros búsqueda
	 */
	private int anio;
	private int mes;
	private String usuario;
	private int tipoGeneracion;
	/**
	 * Objeto donde se almacenan los registros LOG de la Estancia Automatica
	 */
	HashMap datosLog=new HashMap();
	/**
	 * variable para almacenar el tamaño de los registros
	 */
	private int tamanoDatosLog;
	/**
	 * Parámetros usados para la ordenación del listado
	 */
	private String indice;
	private String ultimoIndice;
	
	private String indicativoGeneracionEstancia;

	//***************************************************************************
	
	/**
	 * Mensaje de error al generar la estancia automática
	 */
	private String mensajeError ;
	

	
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
		if(this.estado.equals("generarArea"))
		{
			if(this.fechaInicialEstancia.equals(""))
				errores.add("sin fecha Estancia", new ActionMessage("errors.required","La fecha de generación estancia"));
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaInicialEstancia))
					errores.add("fecha generación estancia", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicialEstancia()));
				else
				{
					//si la fecha estancia es mayor o igual a la fecha actual
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicialEstancia())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>=0)
						errores.add("fecha generación estancia", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "generación estancia", "del sistema"));
				}
			}
			//se revisa si existieron errores
			if(!errores.isEmpty())
			{
				this.estado="buscarArea";
				this.estadoPagina="";
				this.pathArchivoInconsistencias="";
			}
		}
		else if(this.estado.equals("generarPaciente"))
		{
			//System.out.print("\n fecha de orden de salida -->"+this.fechaOrdenSalida);
			//System.out.print("\n fecha admision -->"+this.fechaAdmision);
			//System.out.print("\n COMPARETO -->"+this.fechaAdmision.compareTo(this.fechaOrdenSalida));
			
			if(this.fechaOrdenSalida!=null&&this.fechaAdmision.compareTo(this.fechaOrdenSalida)==0)
				errores.add("Paciente sin estancia", new ActionMessage("error.facturacion.estanciaAutomatica.sinEstancia",UtilidadFecha.conversionFormatoFechaAAp(this.fechaAdmision)));
			else	
				//***SE VALIDAN LOS RANGOS DE LAS FECHAS*****
				errores=this.revisionFechas(errores);
		}
		else if(this.estado.equals("consultar"))
		{
			if(this.anio==0)
				errores.add("sin Año", new ActionMessage("errors.required","El año"));
			else if(this.anio<1700)
				errores.add("Año inválido", new ActionMessage("errors.invalid","El año"));
			if(this.mes==0)
				errores.add("sin Mes", new ActionMessage("errors.required","El mes"));
			if(this.mes>12)
				errores.add("Mes inválido", new ActionMessage("errors.invalid","El mes"));
			if(!errores.isEmpty())
			{
				if(tipoGeneracion==ConstantesBD.codigoTipoEstanciaPorArea)
					estado="buscarConsulta";
				else
					estado="empezarConsulta";
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
		
		if(!this.fechaInicialEstancia.equals(""))
		{
			resp1=1;
			if(UtilidadFecha.validarFecha(this.getFechaInicialEstancia()))
				resp1=2;
			else
				errores.add("fecha inicial estancia", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicialEstancia()));
		}
		
		if(!this.fechaFinalEstancia.equals(""))
		{
			resp2=1;
			if(UtilidadFecha.validarFecha(this.getFechaFinalEstancia()))
				resp2=2;
			else
				errores.add("fecha final estancia", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinalEstancia()));
		}
		
		//revisar si las fechas son válidas
		if(resp1==2&&resp2==2)
		{
			//si la fecha inicial es mayor ó igual a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicialEstancia())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>=0)
			{
				errores.add("fecha inicial estancia", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de estancia", "del sistema"));
			}
			
			//si la fecha final es mayor ó igual a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinalEstancia())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>=0)
			{
				errores.add("fecha final estancia", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final de estancia", "del sistema"));
			}
			
			//si la fecha inicial es mayor a la fecha final
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicialEstancia())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinalEstancia()))>0)
			{
				errores.add("fecha inicial mayor a la fecha final", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial de estancia", "final de estancia"));
			}
			
			//si la fecha inicial es menor a la fecha de admision
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicialEstancia())).compareTo(this.fechaAdmision)<0)
			{
				errores.add("fecha inicial menor a la fecha Admision", new ActionMessage("errors.fechaAnteriorIgualActual", "inicial de estancia", "de admisión del paciente: "+UtilidadFecha.conversionFormatoFechaAAp(this.fechaAdmision)));
			}
			
			//validación para el caso en que se tenga una fecha de orden de salida
			if(this.fechaOrdenSalida!=null&&!this.fechaOrdenSalida.equals(""))
			{
				//si la fecha inicial es mayor o igual a la fecha de orden de salida
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicialEstancia())).compareTo(this.fechaOrdenSalida)>=0)
				{
					errores.add("fecha inicial mayor o igual a la fecha de orden de salida", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de estancia", "de orden de salida: "+UtilidadFecha.conversionFormatoFechaAAp(this.fechaOrdenSalida)));
				}
				//si la fecha final es mayor o igual a la fecha de orden de salida
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinalEstancia())).compareTo(this.fechaOrdenSalida)>=0)
				{
					errores.add("fecha inicial mayor a la fecha de orden de salida", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de estancia", "de orden de salida: "+UtilidadFecha.conversionFormatoFechaAAp(this.fechaOrdenSalida)));
				}
			}
			
			
		}
		else
		{
			//caso en el que falte alguna fecha del rango
			if(resp1==0&&resp2>0)
			{
				errores.add("La fecha inicial de estancia", new ActionMessage("errors.required", "La fecha inicial de estancia"));
			}
			
			if(resp2==0&&resp1>0)
			{
				errores.add("La fecha final de estancia", new ActionMessage("errors.required", "La fecha final de estancia"));
			}
			
			if(resp1==0&&resp2==0)
			{
				errores.add("El rango de fechas de estancia", new ActionMessage("errors.required", "El rango de fechas de estancia"));
			}
		}
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.estadoPagina="";
		this.centroCosto="";
		this.fechaInicialEstancia="";
		this.fechaFinalEstancia="";
		this.cuentasEstancia=new HashMap();
		this.pathArchivoInconsistencias="";
		this.fechaAdmision="";
		this.fechaOrdenSalida="";
		this.codigoCentroAtencion = 0;
		this.anio=0;
		this.mes=0;
		this.usuario="";
		this.tipoGeneracion=-1;
		this.datosLog=new HashMap();
		this.tamanoDatosLog=0;
		this.indice="";
		this.ultimoIndice="";
		
		this.inconsistencias = new HashMap();
		this.numInconsistencias = 0;
		
		this.mensajeError = "";
		this.indicativoGeneracionEstancia="";
		
	}
	
	//****************METODOS GETTERS & SETTERS ********************************
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
	 * @return Returns the centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @param centroCosto The centroCosto to set.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}
	/**
	 * @return Returns the fechaFinalEstancia.
	 */
	public String getFechaFinalEstancia() {
		return fechaFinalEstancia;
	}
	/**
	 * @param fechaFinalEstancia The fechaFinalEstancia to set.
	 */
	public void setFechaFinalEstancia(String fechaFinalEstancia) {
		this.fechaFinalEstancia = fechaFinalEstancia;
	}
	/**
	 * @return Returns the fechaInicialEstancia.
	 */
	public String getFechaInicialEstancia() {
		return fechaInicialEstancia;
	}
	/**
	 * @param fechaInicialEstancia The fechaInicialEstancia to set.
	 */
	public void setFechaInicialEstancia(String fechaInicialEstancia) {
		this.fechaInicialEstancia = fechaInicialEstancia;
	}
	/**
	 * @return Returns the cuentasEstancia.
	 */
	public HashMap getCuentasEstancia() {
		return cuentasEstancia;
	}
	/**
	 * @param cuentasEstancia The cuentasEstancia to set.
	 */
	public void setCuentasEstancia(HashMap cuentasEstancia) {
		this.cuentasEstancia = cuentasEstancia;
	}
	
	
	/**
	 * @return Returns the estadoPagina.
	 */
	public String getEstadoPagina() {
		return estadoPagina;
	}
	/**
	 * @param estadoPagina The estadoPagina to set.
	 */
	public void setEstadoPagina(String estadoPagina) {
		this.estadoPagina = estadoPagina;
	}
	/**
	 * @return Returns the pathArchivoInconsistencias.
	 */
	public String getPathArchivoInconsistencias() {
		return pathArchivoInconsistencias;
	}
	/**
	 * @param pathArchivoInconsistencias The pathArchivoInconsistencias to set.
	 */
	public void setPathArchivoInconsistencias(String pathArchivoInconsistencias) {
		this.pathArchivoInconsistencias = pathArchivoInconsistencias;
	}
	/**
	 * @return Returns the fechaAdmision.
	 */
	public String getFechaAdmision() {
		return fechaAdmision;
	}
	/**
	 * @param fechaAdmision The fechaAdmision to set.
	 */
	public void setFechaAdmision(String fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}

	/**
	 * @return Returns the fechaOrdenSalida.
	 */
	public String getFechaOrdenSalida() {
		return fechaOrdenSalida;
	}
	/**
	 * @param fechaOrdenSalida The fechaOrdenSalida to set.
	 */
	public void setFechaOrdenSalida(String fechaOrdenSalida) {
		this.fechaOrdenSalida = fechaOrdenSalida;
	}
	/**
	 * @return Returns the anio.
	 */
	public int getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}
	/**
	 * @return Returns the mes.
	 */
	public int getMes() {
		return mes;
	}
	/**
	 * @param mes The mes to set.
	 */
	public void setMes(int mes) {
		this.mes = mes;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the tipoGeneracion.
	 */
	public int getTipoGeneracion() {
		return tipoGeneracion;
	}
	/**
	 * @param tipoGeneracion The tipoGeneracion to set.
	 */
	public void setTipoGeneracion(int tipoGeneracion) {
		this.tipoGeneracion = tipoGeneracion;
	}
	/**
	 * @return Returns the datosLog.
	 */
	public HashMap getDatosLog() {
		return datosLog;
	}
	/**
	 * @param datosLog The datosLog to set.
	 */
	public void setDatosLog(HashMap datosLog) {
		this.datosLog = datosLog;
	}
	/**
	 * @return Returns the tamanoDatosLog.
	 */
	public int getTamanoDatosLog() {
		return tamanoDatosLog;
	}
	/**
	 * @param tamanoDatosLog The tamanoDatosLog to set.
	 */
	public void setTamanoDatosLog(int tamanoDatosLog) {
		this.tamanoDatosLog = tamanoDatosLog;
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
	 * @return Returns the inconsistencias.
	 */
	public HashMap getInconsistencias() {
		return inconsistencias;
	}

	/**
	 * @param inconsistencias The inconsistencias to set.
	 */
	public void setInconsistencias(HashMap inconsistencias) {
		this.inconsistencias = inconsistencias;
	}
	
	/**
	 * @return Retorna elemento del mapa inconsistencias.
	 */
	public Object getInconsistencias(String key) {
		return inconsistencias.get(key);
	}

	/**
	 * @param Asigna elemento al mapa inconsistencias.
	 */
	public void setInconsistencias(String key,Object obj) {
		this.inconsistencias.put(key,obj);
	}

	/**
	 * @return Returns the numInconsistencias.
	 */
	public int getNumInconsistencias() {
		return numInconsistencias;
	}

	/**
	 * @param numInconsistencias The numInconsistencias to set.
	 */
	public void setNumInconsistencias(int numInconsistencias) {
		this.numInconsistencias = numInconsistencias;
	}

	/**
	 * @return Returns the mensajeError.
	 */
	public String getMensajeError() {
		return mensajeError;
	}

	/**
	 * @param mensajeError The mensajeError to set.
	 */
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the indicativoGeneracionEstancia
	 */
	public String getIndicativoGeneracionEstancia() {
		return indicativoGeneracionEstancia;
	}

	/**
	 * @param indicativoGeneracionEstancia the indicativoGeneracionEstancia to set
	 */
	public void setIndicativoGeneracionEstancia(String indicativoGeneracionEstancia) {
		this.indicativoGeneracionEstancia = indicativoGeneracionEstancia;
	}
}
