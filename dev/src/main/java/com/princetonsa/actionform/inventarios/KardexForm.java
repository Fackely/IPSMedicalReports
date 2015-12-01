/*
 * Creado el 27-dic-2005
 * por Joan López
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author Joan López
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class KardexForm extends ActionForm 
{
	 private Logger logger=Logger.getLogger(KardexForm.class);
	/**
     * estado del workflow
     */
    private String estado;    
    /**
     * código del almacén
     */
    int codAlmacen;
    
    private String centroAtencion;
    
    /**
     * Variable que indica si el detalle es por lote o no.
     */
    private boolean porLote;
    
    /**
     * Lote por el cual se consulta el kardex.
     */
    private String lote;
    
    /**
     * Fecha Vencimiento por el cual se consulta el kardex.
     */
    private String fechaVencimiento;
    
    /**
     * Codigo del articulo para consultar por lote.
     */
    private int codigoArticulo;
    
    /**
     * nombre del almacen
     */
    private String nomAlmacen;
    
    /**
     * nombre del almacen
     */
    private String nomCentroAtencion;
    /**
     * fecha del rango inicial
     */
    private String fechaInicial;
    /**
     * fecha del rango final
     */
    private String fechaFinal;
    /**
     * listado de los articulos
     */
    private HashMap mapaArticulos;
    /**
     * indice del articulo seleccionado
     */
    int indexSeleccionado;
    /**
	 * Clase del artículo.
	 */
	private String clase;
	/**
	 * Grupo del articulo
	 */
	private String grupo;
	/**
	 * Subgrupo del artículo
	 */
	private String subgrupo;
	
	/**
	 * 
	 */
	private String articulo;
	
	/**
	 * 
	 */
	private String archivoGenerado;
	
	/**
	 * 
	 */
	private String mensajeAdvertencia;
	
	/**
	 * Nombre Completo del Artículo
	 */
	private String nombreArticulo;
	
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {  
    	porLote=false;
    	this.clase="0";
    	this.grupo="0";
    	this.subgrupo="0";
    	this.codAlmacen=ConstantesBD.codigoNuncaValido;    	
    	this.nomAlmacen="";
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.articulo="-1";
    	this.centroAtencion="";
    	this.mapaArticulos=new HashMap();
    	this.mapaArticulos.put("calculoCosto",0+"");
    	this.mapaArticulos.put("calculoCantidad",0+"");
    	this.mapaArticulos.put("calculoValorTotalInicial",0+"");
    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
    	this.nomCentroAtencion="";
    	this.archivoGenerado="";
    	this.mensajeAdvertencia="";
    	this.nombreArticulo = "";
    }
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{	    
	    ActionErrors errores = new ActionErrors();
	    if(estado.equals("generarBusqueda"))
	    {
	    	if(this.codAlmacen==ConstantesBD.codigoNuncaValido)
	    	{
	    		errores.add("falta campo", new ActionMessage("errors.required","El Almacén"));
	    	}
	    	if(this.clase.equals("0") && this.articulo.equals("-1"))
	    	{
	    			errores.add("falta campo", new ActionMessage("errors.required","El Artículo ó Clase-Grupo-SubGrupo"));
	    	}
	    	if(!this.clase.equals("0"))
	    	{
	    		if(this.grupo.equals("0"))
	    		{
	    			errores.add("falta campo", new ActionMessage("errors.required","El Grupo"));
	    		}
	    		if(this.subgrupo.equals("0"))
	    		{
	    			errores.add("falta campo", new ActionMessage("errors.required","El SubGrupo"));
	    		}
	    	}
	    		
	    	boolean fechaInicialCorrecta=true;
	    	boolean fechaFinalCorrecta=true;
	    	if(this.fechaInicial.equals(""))
	    	{
	    		errores.add("falta campo", new ActionMessage("errors.required","La Fecha Inicial"));
	    		fechaInicialCorrecta=false;
	    	}
	    	else
	    	{
	    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
	    		{
	    			errores.add("falta campo", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.fechaInicial,"Sistema "+UtilidadFecha.getFechaActual()));
	    			fechaInicialCorrecta=false;
	    		}
	    		if(!UtilidadFecha.validarFecha(this.fechaInicial))
	    		{
	    			logger.info("Entré a Validar si la Fecha Inicial viene");
	    			errores.add("falta campo", new ActionMessage("errors.required","El Formato de la Fecha Inicial debe ser DD/MM/AAAA"));
	    			fechaInicialCorrecta=false;
	    		}
	    	}
	    	if(this.fechaFinal.equals(""))
	    	{
	    		errores.add("falta campo", new ActionMessage("errors.required","La Fecha Final"));
	    		fechaFinalCorrecta=false;
	    	}
	    	else 
	    	{
	    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
	    		{
	    			errores.add("falta campo", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.fechaFinal,"Sistema "+UtilidadFecha.getFechaActual()));
	    			fechaFinalCorrecta=false;
	    		}
	    		if(!this.fechaInicial.equals(""))
		    	{
		    		if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,this.fechaInicial))
		    		{
		    			errores.add("falta campo", new ActionMessage("errors.fechaAnteriorIgualActual","Final "+this.fechaFinal,"Inicial "+this.fechaInicial));
		    			fechaFinalCorrecta=false;
		    		}
		    	}
	    		if(!UtilidadFecha.validarFecha(this.fechaFinal))
	    		{
	    			logger.info("Entré a Validar si la Fecha Final viene");
	    			errores.add("falta campo", new ActionMessage("errors.required","El Formato de la Fecha Final debe ser DD/MM/AAAA"));
	    			fechaInicialCorrecta=false;
	    		}
	    	}
	    	if(fechaInicialCorrecta&&fechaFinalCorrecta)
	    	{
		    	if(UtilidadFecha.numeroDiasEntreFechas(this.fechaInicial,this.fechaFinal)>90)
		    	{
					errores.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","para Kardex"));
		    	}
	    	}
	    }
	    
	    
	    if(estado.equals("generarArchivoPlano"))
	    {
	    	if(this.codAlmacen==ConstantesBD.codigoNuncaValido)
	    	{
	    		errores.add("falta campo", new ActionMessage("errors.required","El Almacén"));
	    	}
	    	
	    	boolean fechaInicialCorrecta=true;
	    	boolean fechaFinalCorrecta=true;
	    	if(this.fechaInicial.equals(""))
	    	{
	    		errores.add("falta campo", new ActionMessage("errors.required","La Fecha Inicial"));
	    		fechaInicialCorrecta=false;
	    	}
	    	else
	    	{
	    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial,UtilidadFecha.getFechaActual()))
	    		{
	    			errores.add("falta campo", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.fechaInicial,"Sistema "+UtilidadFecha.getFechaActual()));
	    			fechaInicialCorrecta=false;
	    		}
	    	}
	    	if(this.fechaFinal.equals(""))
	    	{
	    		errores.add("falta campo", new ActionMessage("errors.required","La Fecha Final"));
	    		fechaFinalCorrecta=false;
	    	}
	    	else 
	    	{
	    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal,UtilidadFecha.getFechaActual()))
	    		{
	    			errores.add("falta campo", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.fechaFinal,"Sistema "+UtilidadFecha.getFechaActual()));
	    			fechaFinalCorrecta=false;
	    		}
	    		if(!this.fechaInicial.equals(""))
		    	{
		    		if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaFinal,this.fechaInicial))
		    		{
		    			errores.add("falta campo", new ActionMessage("errors.fechaAnteriorIgualActual","Final "+this.fechaFinal,"Incial "+this.fechaInicial));
		    			fechaFinalCorrecta=false;
		    		}
		    	}
	    	}
	    	if(fechaInicialCorrecta&&fechaFinalCorrecta)
	    	{
		    	//las fechas deben pertenecer al mismo anio
	    		if(!this.fechaInicial.split("/")[2].equals(this.fechaFinal.split("/")[2]))
	    		{
	    			errores.add("errors.notEspecific", new ActionMessage("errors.notEspecific", "Las Fechas deben pertenecer al mismo Año"));
	    		}
	    	}
	    }
	    
	    return errores;
	}
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna codAlmacen.
	 */
	public int getCodAlmacen() {
		return codAlmacen;
	}
	/**
	 * @param Asigna codAlmacen.
	 */
	public void setCodAlmacen(int codAlmacen) {
		this.codAlmacen = codAlmacen;
	}	
	/**
	 * @return Retorna fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param Asigna fechaFinal.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Retorna fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param Asigna fechaInicial.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Retorna mapaArticulos.
	 */
	public HashMap getMapaArticulos() {
		return mapaArticulos;
	}
	/**
	 * @param Asigna mapaArticulos.
	 */
	public void setMapaArticulos(HashMap mapaArticulos) {
		this.mapaArticulos = mapaArticulos;
	}
	/**
	 * @return Retorna mapaArticulos.
	 */
	public Object getMapaArticulos(String key) {
		return mapaArticulos.get(key);
	}
	/**
	 * @param Asigna mapaArticulos.
	 */
	public void setMapaArticulos(String key,Object value) {
		this.mapaArticulos.put(key, value);
	}
	/**
	 * @return Retorna nomAlmacen.
	 */
	public String getNomAlmacen() {
		return nomAlmacen;
	}
	/**
	 * @param Asigna nomAlmacen.
	 */
	public void setNomAlmacen(String nomAlmacen) {
		this.nomAlmacen = nomAlmacen;
	}
	/**
	 * @return Retorna indexSeleccionado.
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}
	/**
	 * @param Asigna indexSeleccionado.
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}
	public String getArticulo() {
		return articulo;
	}
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	public String getNomCentroAtencion() {
		return nomCentroAtencion;
	}
	public void setNomCentroAtencion(String nomCentroAtencion) {
		this.nomCentroAtencion = nomCentroAtencion;
	}
	public String getCentroAtencion()
	{
		return centroAtencion;
	}
	public void setCentroAtencion(String centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
	public boolean isPorLote()
	{
		return porLote;
	}
	public void setPorLote(boolean porLote)
	{
		this.porLote = porLote;
	}
	public int getCodigoArticulo()
	{
		return codigoArticulo;
	}
	public void setCodigoArticulo(int codigoArticulo)
	{
		this.codigoArticulo = codigoArticulo;
	}
	public String getFechaVencimiento()
	{
		return fechaVencimiento;
	}
	public void setFechaVencimiento(String fechaVencimiento)
	{
		this.fechaVencimiento = fechaVencimiento;
	}
	public String getLote()
	{
		return lote;
	}
	public void setLote(String lote)
	{
		this.lote = lote;
	}
	/**
	 * @return the archivoGenerado
	 */
	public String getArchivoGenerado() {
		return archivoGenerado;
	}
	/**
	 * @param archivoGenerado the archivoGenerado to set
	 */
	public void setArchivoGenerado(String archivoGenerado) {
		this.archivoGenerado = archivoGenerado;
	}
	/**
	 * @return the mensajeAdvertencia
	 */
	public String getMensajeAdvertencia() {
		return mensajeAdvertencia;
	}
	/**
	 * @param mensajeAdvertencia the mensajeAdvertencia to set
	 */
	public void setMensajeAdvertencia(String mensajeAdvertencia) {
		this.mensajeAdvertencia = mensajeAdvertencia;
	}
	/**
	 * @return the nombreArticulo
	 */
	public String getNombreArticulo() {
		return nombreArticulo;
	}
	/**
	 * @param nombreArticulo the nombreArticulo to set
	 */
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}
}
