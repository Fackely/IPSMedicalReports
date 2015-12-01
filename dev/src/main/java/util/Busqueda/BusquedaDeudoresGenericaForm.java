/*
 * Junio 25, 2009
 */
package util.Busqueda;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author Sebastián Gómez Rivillas
 * 
 * Form que contiene todos los datos específicos para generar la
 * busqueda de deudores genérica
 *
 */
public class BusquedaDeudoresGenericaForm extends ValidatorForm 
{
	/**
    * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(BusquedaDeudoresGenericaForm.class);
	
	/**
    * estado de la accion
    */
   private String estado;
   
   //***************PARÁMETROS DEL LLAMADO*******************
   /**
    * Para saber si se debe hacer submit
    */
   private boolean hacerSubmit = false;
   /**
    * Nombre de la forma de la jso que llama
    */
   private String nombreForma = "";
   /**
    * Estado para el submit
    */
   private String estadoSubmit = "";
   /**
    * Hidden donde se almacena el codigo dell deudor seleccionado
    */
   private String idHiddenCodigo = "";
   
   /**
    * Hidden donde se almacena el tipo dell deudor seleccionado
    */
   private String idHiddenTipo = "";
   /**
    * Hidden donde se almacena la descripcion dell deudor seleccionado
    */
   private String idHiddenDescripcion = "";
   
   /**
    * Hidden donde se almacena si el paciente es no deudor
    */
   private String idHiddenPacienteNoDeudor;
   
   /**
    * Indica si el paciente cargado no se encuentra
    * registrado como deudor.
    */
   private boolean pacienteNoDeudor;
   
  
/**
    * Div donde se dibuja los datos del deudor elegido
    */
   private String idDiv = "";
   
   /**
    * Para saber si ya viene el tipo deudor definido
    */
   private boolean vieneTipoDeudor;
   
   /**
    * Para saber si la busqueda es para agregar un nuevo deudor o no
    */
   private boolean nuevoDeudor;
   
   /**
    * Para saber si solo se deben buscar deudores activos
    */
   private boolean deudoresActivos;
   //*********************************************************
   
 //************ATRIBUTOS DE LA BUSQUEDA******************************
   private String tipoDeudor;
   private String codigoTipoIdentificacion;
   private String numeroIdentificacion;
   private String primerApellido;
   private String primerNombre;
   private String descripcion;
   private ArrayList<HashMap<String, Object>> tiposIdentificacion = new ArrayList<HashMap<String,Object>>();
  //******************************************************************
   
   private HashMap<String, Object> listado = new HashMap<String, Object>();
   private int maxPageItems;
   private String linkSiguiente;
   private String indice;
   private String ultimoIndice;
   
   
   /**
    * Reset
 * @param request 
    */
    public void reset(HttpServletRequest request)
    {
    	this.estado = "";
    	this.tipoDeudor = request.getParameter("tipoDeudor")==null?"":request.getParameter("tipoDeudor");
    	this.codigoTipoIdentificacion = "";
    	this.numeroIdentificacion = "";
    	this.primerApellido = "";
    	this.primerNombre = "";
    	this.descripcion = "";
    	this.tiposIdentificacion = new ArrayList<HashMap<String,Object>>();
    	this.listado = new HashMap<String, Object>();
    	this.maxPageItems = 10;
    	this.linkSiguiente = "";
    	this.indice = "";
    	this.ultimoIndice = "";
    	this.nuevoDeudor = UtilidadTexto.getBoolean(request.getParameter("nuevoDeudor"));
    	this.deudoresActivos = UtilidadTexto.getBoolean(request.getParameter("deudoresActivos"));
    	this.pacienteNoDeudor = false;
    	
    	if(!this.tipoDeudor.equals(""))
		{
			this.setVieneTipoDeudor(true);
		}
		else
		{
			this.setVieneTipoDeudor(false);
		}
    }
   /**
 * @return the deudoresActivos
 */
public boolean isDeudoresActivos() {
	return deudoresActivos;
}
/**
 * @param deudoresActivos the deudoresActivos to set
 */
public void setDeudoresActivos(boolean deudoresActivos) {
	this.deudoresActivos = deudoresActivos;
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
       
       if(this.estado.equals("buscar"))
       {
    	   if(this.tipoDeudor.equals(""))
    	   {
    		   errores.add("",new ActionMessage("errors.required","El tipo deudor"));
    		   this.codigoTipoIdentificacion = "";
    		   this.estado = "empezar";
    	   }
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
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}
	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}
	/**
	 * @return the codigoTipoIdentificacion
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}
	/**
	 * @param codigoTipoIdentificacion the codigoTipoIdentificacion to set
	 */
	public void setCodigoTipoIdentificacion(String codigoTipoIdentificacion) {
		this.codigoTipoIdentificacion = codigoTipoIdentificacion;
	}
	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}
	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}
	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the hacerSubmit
	 */
	public boolean isHacerSubmit() {
		return hacerSubmit;
	}
	/**
	 * @param hacerSubmit the hacerSubmit to set
	 */
	public void setHacerSubmit(boolean hacerSubmit) {
		this.hacerSubmit = hacerSubmit;
	}
	/**
	 * @return the nombreForma
	 */
	public String getNombreForma() {
		return nombreForma;
	}
	/**
	 * @param nombreForma the nombreForma to set
	 */
	public void setNombreForma(String nombreForma) {
		this.nombreForma = nombreForma;
	}
	/**
	 * @return the estadoSubmit
	 */
	public String getEstadoSubmit() {
		return estadoSubmit;
	}
	/**
	 * @param estadoSubmit the estadoSubmit to set
	 */
	public void setEstadoSubmit(String estadoSubmit) {
		this.estadoSubmit = estadoSubmit;
	}
	
	/**
	 * @return the idDiv
	 */
	public String getIdDiv() {
		return idDiv;
	}
	/**
	 * @param idDiv the idDiv to set
	 */
	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}
	/**
	 * @return the tiposIdentificacion
	 */
	public ArrayList<HashMap<String, Object>> getTiposIdentificacion() {
		return tiposIdentificacion;
	}
	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacion(
			ArrayList<HashMap<String, Object>> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}
	/**
	 * @return the vieneTipoDeudor
	 */
	public boolean isVieneTipoDeudor() {
		return vieneTipoDeudor;
	}
	/**
	 * @param vieneTipoDeudor the vieneTipoDeudor to set
	 */
	public void setVieneTipoDeudor(boolean vieneTipoDeudor) {
		this.vieneTipoDeudor = vieneTipoDeudor;
	}
	/**
	 * @return the listado
	 */
	public HashMap<String, Object> getListado() {
		return listado;
	}
	/**
	 * @param listado the listado to set
	 */
	public void setListado(HashMap<String, Object> listado) {
		this.listado = listado;
	}
	
	/**
	 * @return the listado
	 */
	public Object getListado(String key) {
		return listado.get(key);
	}
	/**
	 * @param listado the listado to set
	 */
	public void setListado(String key,Object obj) 
	{
		this.listado.put(key,obj);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumRegistros()
	{
		return Utilidades.convertirAEntero(this.getListado("numRegistros")+"", true);
	}
	
	/**
	 * Método que asigna el nýumero de registros del listado
	 * @param numRegistros
	 */
	public void setNumRegistros(int numRegistros)
	{
		this.setListado("numRegistros", numRegistros);
	}
	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return the idHiddenCodigo
	 */
	public String getIdHiddenCodigo() {
		return idHiddenCodigo;
	}
	/**
	 * @param idHiddenCodigo the idHiddenCodigo to set
	 */
	public void setIdHiddenCodigo(String idHiddenCodigo) {
		this.idHiddenCodigo = idHiddenCodigo;
	}
	/**
	 * @return the idHiddenDescripcion
	 */
	public String getIdHiddenDescripcion() {
		return idHiddenDescripcion;
	}
	/**
	 * @param idHiddenDescripcion the idHiddenDescripcion to set
	 */
	public void setIdHiddenDescripcion(String idHiddenDescripcion) {
		this.idHiddenDescripcion = idHiddenDescripcion;
	}
	/**
	 * @return the nuevoDeudor
	 */
	public boolean isNuevoDeudor() {
		return nuevoDeudor;
	}
	/**
	 * @param nuevoDeudor the nuevoDeudor to set
	 */
	public void setNuevoDeudor(boolean nuevoDeudor) {
		this.nuevoDeudor = nuevoDeudor;
	}
	/**
	 * @return the idHiddenTipo
	 */
	public String getIdHiddenTipo() {
		return idHiddenTipo;
	}
	/**
	 * @param idHiddenTipo the idHiddenTipo to set
	 */
	public void setIdHiddenTipo(String idHiddenTipo) {
		this.idHiddenTipo = idHiddenTipo;
	}
	
	public void setIdHiddenPacienteNoDeudor(String idHiddenPacienteNoDeudor) {
		this.idHiddenPacienteNoDeudor = idHiddenPacienteNoDeudor;
	}
	public String getIdHiddenPacienteNoDeudor() {
		return idHiddenPacienteNoDeudor;
	}
	
	public boolean isPacienteNoDeudor() {
		return pacienteNoDeudor;
	}
	 
	public void setPacienteNoDeudor(boolean pacienteNoDeudor) {
		this.pacienteNoDeudor = pacienteNoDeudor;
	}
}
