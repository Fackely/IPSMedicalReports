package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * @author Mauricio Jaramillo Henao
 * Fecha: Agosto de 2008
 */

public class ConsultaSaldosCierresInventariosForm extends ValidatorForm
{

	private String estado;
	
    /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtro
     */
    private String codigoCentroAtencion;
	
    /**
     * HashMap de los Almacenes
     */
    private HashMap almacenes;
    
    /**
     * Codigo del Almacen seleccionado para realizar el filtrado
     */
    private String almacen;
    
    /**
     * Anio del cierre que se esta realizando
     */
    private String anioCierre;
    
    /**
     * Mes del cierre que se esta realizando
     */
    private String mesCierre;
    
    /**
     * String para almacenar el código de la clase seleccionada
     */
    private String clase;
    
    /**
     * String para almacenar el nombre de la clase seleccionada
     */
    private String descripcionClase;
    
    /**
     * String para almacenar el código del grupo seleccionado
     */
    private String grupo;
    
    /**
     * String para almacenar el nombre del grupo seleccionado
     */
    private String descripcionGrupo;
    
    /**
     * String para almacenar el código del subgrupo seleccionado
     */
    private String subGrupo;
    
    /**
     * String para almacenar el nombre del subgrupo seleccionado
     */
    private String descripcionSubGrupo;
	
    /**
     * String para almacenar el codigo del articulo buscado por la busqueda generica
     */
    private String codArticulo;
    
    /**
     * String para almacenar la descripción del articulo buscado por la busqueda generica
     */
    private String desArticulo;
    
    /**
     * HashMap para almacenar los resultados arrojados para la Consulta de Saldos de Cierre de Inventarios
     */
    private HashMap consultaSaldosCierresInventarios;
    
	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Patron para realizar el ordenamiento
     */
    private String patronOrdenar;
	
    /**
     * Ultimo patron por el que se realizo el ordenamiento
     */
    private String ultimoPatron;
    
    /**
     * String que maneja la institución del usuario activo en sesión
     */
    private String institucion;
    
	/**
	 * Metodo reset
	 */
	public void reset()
	{
		this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.almacenes = new HashMap();
    	this.almacenes.put("numRegistros", "0");
    	this.almacen = "";
    	this.anioCierre = "";
        this.mesCierre = "";
        this.clase = "";
        this.descripcionClase = "";
        this.grupo = "";
        this.descripcionGrupo = "";
        this.subGrupo = "";
        this.descripcionSubGrupo = "";
        this.codArticulo = "";
        this.desArticulo = "";
        this.consultaSaldosCierresInventarios = new HashMap();
    	this.consultaSaldosCierresInventarios.put("numRegistros", "0");
    	this.maxPageItems = 20;
        this.linkSiguiente = "";
        this.patronOrdenar = "";
    	this.ultimoPatron = "";
    	this.institucion = "";
	}
	
	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			//Validamos el Centro de Atencion
			if(this.codigoCentroAtencion.trim().equals("") || this.codigoCentroAtencion.trim().equals("null"))
				errores.add("centroAtencion", new ActionMessage("errors.required", "El Centro de Atención "));
			//Validamos el Año de Cierre y Mes de Cierre
			if(this.anioCierre.trim().equals("") || this.anioCierre.trim().equals("null"))
				errores.add("anioCierre", new ActionMessage("errors.required", "El Año de Cierre "));
			if(this.mesCierre.trim().equals("") || this.mesCierre.trim().equals("null"))
				errores.add("mesCierre", new ActionMessage("errors.required", "El Mes de Cierre "));
			
			//Validamos si el Año de Cierre y Mes de Cierre son válidos 
			if(!UtilidadTexto.isEmpty(this.anioCierre) && !UtilidadTexto.isEmpty(this.mesCierre))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp("01/"+this.mesCierre+"/2008"))
				{
					errores.add("", new ActionMessage("errors.formatoMesInvalido", this.mesCierre));
					centinelaErrorFechas = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp("01/01/"+this.anioCierre+""))
				{
					errores.add("", new ActionMessage("errors.formatoAnoInvalido", this.anioCierre));
					centinelaErrorFechas = true;
				}
				if(!centinelaErrorFechas)
				{
					int anioActual, mesActual;
				    String[] fechaTemp = UtilidadFecha.getFechaActual().split("/");
				    anioActual = Integer.parseInt(fechaTemp[2]);
				    mesActual = Integer.parseInt(fechaTemp[1]);
				    //Validación de que el Año de Cierre y el Mes de Cierre no sean mayores a la fecha del Sistema
				    if(Integer.parseInt(this.anioCierre) > anioActual)
				        errores.add("fechaInvalida", new ActionMessage("error.cierre.fechaCierreInvalido"));
				    else if(Integer.parseInt(this.anioCierre) == anioActual && Integer.parseInt(this.mesCierre) > mesActual)
				        errores.add("fechaInvalida", new ActionMessage("error.cierre.fechaCierreInvalido"));
				    else
				    {
				    	//Si la Fecha es Válida. Validamos que exista Cierre Mensual para los meses Consecutivos al Año y Mes ingresado
				    	/*for(int i=1; i<Utilidades.convertirAEntero(this.getMesCierre()); i++)
				    	{
				    		if(!UtilidadInventarios.existeCierreInventarioParaFecha("01/"+i+"/"+this.getAnioCierre(), Utilidades.convertirAEntero(this.institucion)))
				    		{
				    			i = Utilidades.convertirAEntero(this.getMesCierre());
				    			errores.add("mesesAnterioresSinCierre", new ActionMessage("error.cierre.mesesAnterioresSinCierre", this.getMesCierre(), this.anioCierre));
				    		}
				    	}*/
				    }
				}
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
	 * @return the centroAtencion
	 */
	public HashMap getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(HashMap centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroAtencion(String key) {
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value) {
		this.centroAtencion.put(key, value);
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	
	/**
	 * @return the almacen
	 */
	public String getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen the almacen to set
	 */
	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	/**
	 * @return the almacenes
	 */
	public HashMap getAlmacenes() {
		return almacenes;
	}

	/**
	 * @param almacenes the almacenes to set
	 */
	public void setAlmacenes(HashMap almacenes) {
		this.almacenes = almacenes;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getAlmacenes(String key){
		return almacenes.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setAlmacenes(String key, Object value){
		this.almacenes.put(key, value);
	}

	/**
	 * @return the anioCierre
	 */
	public String getAnioCierre() {
		return anioCierre;
	}

	/**
	 * @param anioCierre the anioCierre to set
	 */
	public void setAnioCierre(String anioCierre) {
		this.anioCierre = anioCierre;
	}

	/**
	 * @return the mesCierre
	 */
	public String getMesCierre() {
		return mesCierre;
	}

	/**
	 * @param mesCierre the mesCierre to set
	 */
	public void setMesCierre(String mesCierre) {
		this.mesCierre = mesCierre;
	}

	/**
	 * @return the clase
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the subGrupo
	 */
	public String getSubGrupo() {
		return subGrupo;
	}

	/**
	 * @param subGrupo the subGrupo to set
	 */
	public void setSubGrupo(String subGrupo) {
		this.subGrupo = subGrupo;
	}

	/**
	 * @return the descripcionClase
	 */
	public String getDescripcionClase() {
		return descripcionClase;
	}

	/**
	 * @param descripcionClase the descripcionClase to set
	 */
	public void setDescripcionClase(String descripcionClase) {
		this.descripcionClase = descripcionClase;
	}

	/**
	 * @return the descripcionGrupo
	 */
	public String getDescripcionGrupo() {
		return descripcionGrupo;
	}

	/**
	 * @param descripcionGrupo the descripcionGrupo to set
	 */
	public void setDescripcionGrupo(String descripcionGrupo) {
		this.descripcionGrupo = descripcionGrupo;
	}

	/**
	 * @return the descripcionSubGrupo
	 */
	public String getDescripcionSubGrupo() {
		return descripcionSubGrupo;
	}

	/**
	 * @param descripcionSubGrupo the descripcionSubGrupo to set
	 */
	public void setDescripcionSubGrupo(String descripcionSubGrupo) {
		this.descripcionSubGrupo = descripcionSubGrupo;
	}

	/**
	 * @return the codArticulo
	 */
	public String getCodArticulo() {
		return codArticulo;
	}

	/**
	 * @param codArticulo the codArticulo to set
	 */
	public void setCodArticulo(String codArticulo) {
		this.codArticulo = codArticulo;
	}

	/**
	 * @return the consultaSaldosCierresInventarios
	 */
	public HashMap getConsultaSaldosCierresInventarios() {
		return consultaSaldosCierresInventarios;
	}

	/**
	 * @param consultaSaldosCierresInventarios the consultaSaldosCierresInventarios to set
	 */
	public void setConsultaSaldosCierresInventarios(HashMap consultaSaldosCierresInventarios) {
		this.consultaSaldosCierresInventarios = consultaSaldosCierresInventarios;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getConsultaSaldosCierresInventarios(String key){
		return consultaSaldosCierresInventarios.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaSaldosCierresInventarios(String key, Object value){
		this.consultaSaldosCierresInventarios.put(key, value);
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
	 * @return the desArticulo
	 */
	public String getDesArticulo() {
		return desArticulo;
	}

	/**
	 * @param desArticulo the desArticulo to set
	 */
	public void setDesArticulo(String desArticulo) {
		this.desArticulo = desArticulo;
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

	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	
}