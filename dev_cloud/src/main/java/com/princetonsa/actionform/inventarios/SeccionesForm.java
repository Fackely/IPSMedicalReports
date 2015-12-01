package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * Clase para el manejo de secciones y subsecciones x almacen
 * Date: 2008-01-16
 * @author garias@princetonsa.com - lgchavez@princetonsa.com
 */
public class SeccionesForm extends ValidatorForm 
{
	
	/************************************************/
	//atributos para el uso del pager
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * codigo del centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * 
	 * */
	private String nombreCentroAtencion;
		
	/**
	 * 
	 * */
	private String nombreAlmacen;
	
	/**
	 * 
	 */
	private int almacen;
	
	/**
	 * 
	 */
	private HashMap almacenesMap;
	
	/**
	 * 
	 */
	private String codigoSeccion;
	
	/**
	 * 
	 */
	private String descripcionSeccion;
	
	/**
	 * 
	 */
	private HashMap seccionesMap;
	
	/**
	 * 
	 */
	private HashMap subseccionesMap;
	
	/**
	 * 
	 */
	private int codigoPk;
	
	/**
	 * 
	 */
	private String codigoSubseccion;
	
	/**
	 * 
	 */
	private String descripcionSubseccion;
	
	/**
	 * 
	 */
	private int codigoPkSeccion;
	
	
	
	/**
	 * 
	 */
	private String codigoSubseccionTemp;
	
	
	/**
	 * 
	 */
	private HashMap subSeccionNuevoMap;

	ResultadoBoolean mensajeInsercion=null;
	
	/**
	 * 
	 */
	public void reset( int codigoInstitucion, int centroAtencion)
	{
		this.centroAtencion=centroAtencion;
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.almacen=ConstantesBD.codigoNuncaValido;
		this.almacenesMap=UtilidadInventarios.listadoAlmacensActivos(codigoInstitucion, false);
		this.codigoSeccion="";
		this.descripcionSeccion="";
		this.seccionesMap= new HashMap();
		this.seccionesMap.put("numRegistros", "0");
		this.codigoPk=ConstantesBD.codigoNuncaValido;
		this.codigoPkSeccion=ConstantesBD.codigoNuncaValido;
		this.subseccionesMap=new HashMap();
		this.subseccionesMap.put("numRegistros", "0");
		this.descripcionSubseccion ="";
		this.codigoSubseccion = "";
		this.codigoSubseccionTemp = "";
		this.subSeccionNuevoMap=new HashMap();
		this.subSeccionNuevoMap.put("numRegistros", "0");
		
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
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("guardarNuevo"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigoSeccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.required", "El código de la sección"));
        	else if(!UtilidadTexto.isNumber(this.getCodigoSeccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.integer", "El código de la sección"));
        	else if(Integer.parseInt( this.getCodigoSeccion())<=0)
        		errores.add("codigo requerido",new ActionMessage("errors.integerMayorQue", "El código de la sección", "0"));
        	
        	if(UtilidadTexto.isEmpty(this.getDescripcionSeccion()))
        		errores.add("desc requerido",new ActionMessage("errors.required", "La descripción de la sección"));
        	
        	int registroArticulo = Utilidades.convertirAEntero(seccionesMap.get("numRegistros").toString());
        	
        	for(int x=0;x<registroArticulo;x++)
        	{
        		
        		
        		if((seccionesMap.get("codseccion_"+x).toString()).equals(getCodigoSeccion()))
        		{
        			errores.add("codigo ya existe",new ActionMessage("errors.yaExiste", "El Codigo Sección "));
        		}
        	}
        	
        	if(!errores.isEmpty())
        		this.setEstado("nuevoRegistro");
        	
        }
        if(this.estado.equals("guardarModificacion"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigoSeccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.required", "El código de la sección"));
        	else if(!UtilidadTexto.isNumber(this.getCodigoSeccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.integer", "El código de la sección"));
        	else if(Integer.parseInt( this.getCodigoSeccion())<=0)
        		errores.add("codigo requerido",new ActionMessage("errors.integerMayorQue", "El código de la sección", "0"));
        	
        	if(UtilidadTexto.isEmpty(this.getDescripcionSeccion()))
        		errores.add("desc requerido",new ActionMessage("errors.required", "La descripción de la sección"));
        	
        	int registroArticulo = Utilidades.convertirAEntero(seccionesMap.get("numRegistros").toString());
        	
        	
        	if(!errores.isEmpty())
        		this.setEstado("modificarRegistro");
        }
        
        
        if(this.estado.equals("guardarNuevoSubseccion"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigoSubseccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.required", "El código de la Subsección"));
        	else if(!UtilidadTexto.isNumber(this.getCodigoSubseccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.integer", "El código de la Subsección"));
        	else if(Integer.parseInt( this.getCodigoSubseccion())<=0)
        		errores.add("codigo requerido",new ActionMessage("errors.integerMayorQue", "El código de la Subsección", "0"));
        	
        	if(UtilidadTexto.isEmpty(this.getDescripcionSubseccion()))
        		errores.add("desc requerido",new ActionMessage("errors.required", "La descripción de la Subsección"));
        	
        	int registroArticulo = Utilidades.convertirAEntero(subseccionesMap.get("numRegistros").toString());
        	
        	for(int x=0;x<registroArticulo;x++)
        	{
        		if((subseccionesMap.get("codigo_subseccion_"+x).toString()).equals(getCodigoSubseccion()))
        		{
        			errores.add("codigo ya existe",new ActionMessage("errors.yaExiste", "El Codigo Subsección "));        			
        		}
        	}
        	
        	if(!errores.isEmpty())
        		this.setEstado("nuevoRegistroSubseccion");
        	
        }
       
        
        if(this.estado.equals("guardarModificacionSubseccion"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigoSubseccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.required", "El código de la Subsección"));
        	else if(!UtilidadTexto.isNumber(this.getCodigoSubseccion()))
        		errores.add("codigo requerido",new ActionMessage("errors.integer", "El código de la Subsección"));
        	else if(Integer.parseInt( this.getCodigoSubseccion())<=0)
        		errores.add("codigo requerido",new ActionMessage("errors.integerMayorQue", "El código de la Subsección", "0"));
        	
        	if(UtilidadTexto.isEmpty(this.getDescripcionSubseccion()))
        		errores.add("desc requerido",new ActionMessage("errors.required", "La descripción de la Subsección"));
        	
        	int registroArticulo = Utilidades.convertirAEntero(subseccionesMap.get("numRegistros").toString());
        	
        	if(!errores.isEmpty())
        		this.setEstado("modificarRegistroSubseccion");
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
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the almacenesMap
	 */
	public HashMap getAlmacenesMap() {
		return almacenesMap;
	}

	/**
	 * @param almacenesMap the almacenesMap to set
	 */
	public void setAlmacenesMap(HashMap almacenesMap) {
		this.almacenesMap = almacenesMap;
	}

	/**
	 * @return the almacen
	 */
	public int getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen the almacen to set
	 */
	public void setAlmacen(int almacen) {
		this.almacen = almacen;
	}

	/**
	 * @return the centrosAtencionMap
	 */
	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	/**
	 * @return the codigoSeccion
	 */
	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	/**
	 * @param codigoSeccion the codigoSeccion to set
	 */
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	/**
	 * @return the descripcionSeccion
	 */
	public String getDescripcionSeccion() {
		return descripcionSeccion;
	}

	/**
	 * @param descripcionSeccion the descripcionSeccion to set
	 */
	public void setDescripcionSeccion(String descripcionSeccion) {
		this.descripcionSeccion = descripcionSeccion;
	}

	/**
	 * @return the seccionesMap
	 */
	public HashMap getSeccionesMap() {
		return seccionesMap;
	}

	/**
	 * @param seccionesMap the seccionesMap to set
	 */
	public void setSeccionesMap(HashMap seccionesMap) {
		this.seccionesMap = seccionesMap;
	}
	
	/**
	 * @param seccionesMap the seccionesMap to set
	 */
	public void setSeccionesMap(String key,Object obj) {
		this.seccionesMap.put(key,obj);
	}

	
	
	/**
	 * @return the seccionesMap
	 */
	public Object getSeccionesMap(String key) {
		return seccionesMap.get(key);
	}
	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the subseccionesMap
	 */
	public HashMap getSubseccionesMap() {
		return subseccionesMap;
	}

	/**
	 * @param subseccionesMap the subseccionesMap to set
	 */
	public void setSubseccionesMap(HashMap subseccionesMap) {
		this.subseccionesMap = subseccionesMap;
	}

	/**
	 * @param subseccionesMap the subseccionesMap to set
	 */
	public void setSubseccionesMap(String key,Object obj) {
		
		this.subseccionesMap.put(key, obj);
	}

	/**
	 * @return the subseccionesMap
	 */
	public Object getSubseccionesMap(String key) {
		return subseccionesMap.get(key);
	}
	
	
	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the nombreAlmacen
	 */
	public String getNombreAlmacen() {
		return nombreAlmacen;
	}

	/**
	 * @param nombreAlmacen the nombreAlmacen to set
	 */
	public void setNombreAlmacen(String nombreAlmacen) {
		this.nombreAlmacen = nombreAlmacen;
	}

	/**
	 * @return the descripcionSubseccion
	 */
	public String getDescripcionSubseccion() {
		return descripcionSubseccion;
	}

	/**
	 * @param descripcionSubseccion the descripcionSubseccion to set
	 */
	public void setDescripcionSubseccion(String descripcionSubseccion) {
		this.descripcionSubseccion = descripcionSubseccion;
	}

	/**
	 * @return the codigoSubseccion
	 */
	public String getCodigoSubseccion() {
		return codigoSubseccion;
	}

	/**
	 * @param codigoSubseccion the codigoSubseccion to set
	 */
	public void setCodigoSubseccion(String codigoSubseccion) {
		this.codigoSubseccion = codigoSubseccion;
	}

	/**
	 * @return the codigoPkSeccion
	 */
	public int getCodigoPkSeccion() {
		return codigoPkSeccion;
	}

	/**
	 * @param codigoPkSeccion the codigoPkSeccion to set
	 */
	public void setCodigoPkSeccion(int codigoPkSeccion) {
		this.codigoPkSeccion = codigoPkSeccion;
	}

	
	/**
	 * @return the codigoSubseccionTemp
	 */
	public String getCodigoSubseccionTemp() {
		return codigoSubseccionTemp;
	}

	/**
	 * @param codigoSubseccionTemp the codigoSubseccionTemp to set
	 */
	public void setCodigoSubseccionTemp(String codigoSubseccionTemp) {
		this.codigoSubseccionTemp = codigoSubseccionTemp;
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
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public HashMap getSubSeccionNuevoMap() {
		return subSeccionNuevoMap;
	}

	public void setSubSeccionNuevoMap(HashMap subSeccionNuevoMap) {
		this.subSeccionNuevoMap = subSeccionNuevoMap;
	}
	
	public void setSubSeccionNuevoMap(String key, Object value) {
		this.subSeccionNuevoMap.put(key, value);
	}
	
}