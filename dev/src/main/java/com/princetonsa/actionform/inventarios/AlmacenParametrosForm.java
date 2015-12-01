package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.Utilidades;
import util.ConstantesBD;


public class AlmacenParametrosForm extends ValidatorForm  
{
	//-- Atributos 
	/**
	 * HashMap almacenParametros
	 * */	
	private HashMap almacenParametrosMap;
	
	/**
	 * HashMap Eliminado almacenParametros
	 * */	
	private HashMap almacenParametrosEliminadosMap;
	
	/**
	 * HashMap centros de atencion
	 * */	
	private HashMap centroAtencionMap;
	
	/**
	 * Almacen el consecutivo del mapa de centro de atencion
	 * */
	private String centroAtencion;
	
	/**
	 * Index del Registro Eliminado
	 * */
	private int  indexEliminado;
	
	/**
	 * linkSiguiente del pager
	 * */
	private String linkSiguiente;
	
	/**
	 * Patron por el cual se ordenara
	 * **/
	private String patronOrdenar;
	
	/**
	 * */
	private String ultimoPatron;
	
	/**
	 * estado de la forma
	 * */
	private String estado;
	
	/**
	 * HashMap con los centros de costo principal
	 */
	private HashMap centroCostoPrincipal;
	
	//-- Fin Atributos
	
	//-- Metodos

	/**
	 * Inializa los atributos de la clase
	 * */
	public void reset()
	{
		this.almacenParametrosMap = new HashMap();
		this.centroAtencionMap = new HashMap();
		this.almacenParametrosEliminadosMap = new HashMap();
		this.indexEliminado=ConstantesBD.codigoNuncaValido;
		this.almacenParametrosMap.put("numRegistros", "0");
		this.centroAtencionMap.put("numRegistros","0");
		this.almacenParametrosEliminadosMap.put("numRegistros","0");
		this.centroCostoPrincipal = new HashMap();
		this.centroCostoPrincipal.put("numRegistros", "0");
		this.centroAtencion="";
		this.linkSiguiente="";
		this.patronOrdenar="";
	}
	
	/**
	 * Inicializa los centros de Atencion 
	 * @param int codigoInstitucion
	 * */
	public void inicializarCentroAtencion(int codigoInstitucion)
	{
		this.centroAtencionMap = Utilidades.obtenerCentrosAtencion(codigoInstitucion);		
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
        
        /* con la tarea de xplanner2008 id=42494, se solicita que no haga validaciones, que guarde la informacion que se ingresa.
         * Por mutuo acuerdo con German se decidio volver a hacer requerido el centro de costo principal
         */
        if(this.estado.equals("guardar"))
        {
        	for(int i=0; i<Utilidades.convertirAEntero(this.almacenParametrosMap.get("numRegistros")+""); i++)
        	{
        		if((this.almacenParametrosMap.get("centrocostoprincipal_"+i)+"").trim().equals("") || (this.almacenParametrosMap.get("centrocostoprincipal_"+i)+"").trim().equals("null"))
        			errores.add("mesCorte", new ActionMessage("errors.required","El Centro de Costo Principal del Almacén "+this.almacenParametrosMap.get("codigo_"+i)+" "));
        	}
        }
        
        return errores;
    }

	/**
	 * @return the almacenParametrosMap
	 */
	public HashMap getAlmacenParametrosMap() {
		return almacenParametrosMap;
	}

	/**
	 * @param almacenParametrosMap the almacenParametrosMap to set
	 */
	public void setAlmacenParametrosMap(HashMap almacenParametrosMap) {
		this.almacenParametrosMap = almacenParametrosMap;
	}
	
	/**
	 * @param String key
	 * @return the Object almacenParametrosMap
	 */
	public Object getAlmacenParametrosMap(String key) {
		return almacenParametrosMap.get(key);
	}

	/**
	 * @param String key
	 * @param object value 
	 */
	public void setAlmacenParametrosMap(String key, Object value) {
		this.almacenParametrosMap.put(key, value);
	}


	/**
	 * @return the centroAtencionMap
	 */
	public HashMap getCentroAtencionMap() {
		return centroAtencionMap;
	}

	/**
	 * @param centroAtencionMap the centroAtencionMap to set
	 */
	public void setCentroAtencionMap(HashMap centroAtencionMap) {
		this.centroAtencionMap = centroAtencionMap;
	}
	
	/**
	 * @param String value
	 * @return the Object centroAtencionMap
	 */
	public Object getCentroAtencionMap(String key) {
		return centroAtencionMap.get(key);
	}

	/**
	 * @param String key
	 * @param Object value 
	 */
	public void setCentroAtencionMap(String key, Object value) {
		this.centroAtencionMap.put(key, value);
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
	 * @return the indexEliminado
	 */
	public int getIndexEliminado() {
		return indexEliminado;
	}

	/**
	 * @param indexEliminado the indexEliminado to set
	 */
	public void setIndexEliminado(int indexEliminado) {
		this.indexEliminado = indexEliminado;
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

	/**
	 * @return the ultimoPatro
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatro the ultimoPatro to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the almacenParametrosEliminadosMap
	 */
	public HashMap getAlmacenParametrosEliminadosMap() {
		return almacenParametrosEliminadosMap;
	}

	/**
	 * @param almacenParametrosEliminadosMap the almacenParametrosEliminadosMap to set
	 */
	public void setAlmacenParametrosEliminadosMap(
			HashMap almacenParametrosEliminadosMap) {
		this.almacenParametrosEliminadosMap = almacenParametrosEliminadosMap;
	}  
	
	/**
	 * @return the almacenParametrosEliminadosMap
	 */
	public Object getAlmacenParametrosEliminadosMap(String key) {
		return almacenParametrosEliminadosMap.get(key);
	}

	/**
	 * @param almacenParametrosEliminadosMap the almacenParametrosEliminadosMap to set
	 */
	public void setAlmacenParametrosEliminadosMap(String key, Object value) {
		this.almacenParametrosEliminadosMap.put(key, value);
	}

	/**
	 * @return the centroCostoPrincipal
	 */
	public HashMap getCentroCostoPrincipal() {
		return centroCostoPrincipal;
	}

	/**
	 * @param centroCostoPrincipal the centroCostoPrincipal to set
	 */
	public void setCentroCostoPrincipal(HashMap centroCostoPrincipal) {
		this.centroCostoPrincipal = centroCostoPrincipal;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroCostoPrincipal(String key) 
	{
		return centroCostoPrincipal.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroCostoPrincipal(String key, Object value) 
	{
		this.centroCostoPrincipal.put(key, value);
	}
	
	// -- Fin Metodos
}
