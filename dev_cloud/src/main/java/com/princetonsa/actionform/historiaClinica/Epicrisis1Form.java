package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.epicrisis.DtoNotasAclaratoriasEpicrisis;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1Form extends ValidatorForm  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * 
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> centrosAtencionMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> ingresosMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> criteriosBusquedaMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> fechasHorasMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> detalleMap;
	
	/**
	 * 
	 */
	private int indice;
	
	/**
	 * cuentas del ingreso seleccionado
	 */
	private Vector<String> cuentasIngreso;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
    /**
     * 
     */
    private HashMap<Object, Object> cuadroTextoMap;
    
    /**
     * 
     */
    private HashMap<Object, Object> notasAclaratorias;
    
    /**
     * 
     */
    private DtoNotasAclaratoriasEpicrisis dtoNotaAclaratoria;
    
    /**
     * 
     */
    private boolean esConsultaNotasAclaratorias;
    
    /**
     * 
     */
    private int indiceNotaAclaratoria;
    
    /**
     * 
     */
    private boolean egresoMedico;
    
    ////////////////////////////////////////////////PARTE DE ANTECEDENTES////////////////////////////////////////////////
    
    /**
     * 
     */
    private String antecedentesAlergias;
    
    /**
     * 
     */
    private String antecedentesFamiliares;
    
    /**
     * 
     */
    private String antecedentesFamiliaresOculares;
    
    /**
     * 
     */
    private String antecedentesMedicamentos;
    
    /**
     * 
     */
    private String antecedentesMedicosYQx;
    
    /**
     * 
     */
    private String antecedentesOdontologicos;
    
    /**
     * 
     */
    private String antecedentesPediatricos;
    
    /**
     * 
     */
    private String antecedentesPersonalesOculares;
    
    /**
     * 
     */
    private String antecedentesToxicos;
    
    /**
     * 
     */
    private String antecedentesTransfuncionales;
    
    /**
     * 
     */
    private String antecedentesVarios;
    
    /**
     * 
     */
    private HashMap<Object, Object> antecedentesVacunas;
    
    /**
     * 
     */
    private HashMap<Object, Object> antecedentesGineco;
    
    /**
     * 
     */
    private ArrayList<Object> historicosGineco;
    
   /**
     * 
     */
	private HashMap<String, String> abrirSecciones;
	
	/**
	 * 
	 */ 
	private Vector<String> antecedentesAmostrar= new Vector<String>();

	
	/**
	 * 
	 */
	private HashMap<Object, Object> valoracionesInicialesAmostrar= new HashMap<Object, Object>();
	
	///////////////////////////////////////////////////////////////FIN PARTE DE ANTECEDENTES*///////////////////////////////////////////////////
	
	
	/**
	 * 
	 */
	private HashMap<Object,Object> mapaMedicoElaboraEpicrisis;
	
	/**
	 * 
	 */
	private boolean finalizada;
	
	/**
	 * 
	 */
	private String contenidoEncabezado;
	
	/**
	 * Indica si la impresión se ejecuta automaticamente
	 */
	private boolean imprimirAutomaticamente;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset(int institucion, int centroAtencion)
    {
    	this.centroAtencion= centroAtencion;
    	this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(institucion);
    	this.ingresosMap= new HashMap<Object, Object>();
    	this.indice=ConstantesBD.codigoNuncaValido;
    	this.criteriosBusquedaMap= new HashMap<Object, Object>();
    	this.cuentasIngreso= new Vector<String>();
    	this.linkSiguiente="";
    	this.finalizada=false;
    	this.egresoMedico=false;
    	this.contenidoEncabezado="";
    	this.resetCuadroTexto();
    	this.resetDetalleBusqueda();
    	this.resetAntecedentes();
    	this.resetAbrirSecciones();
    	this.resetMedicoElaboraEpicrisis();
    	this.imprimirAutomaticamente=false;
    }
    
    /**
     * 
     *
     */
    private void resetAbrirSecciones() 
    {
    	this.abrirSecciones= new HashMap<String, String>();
    	this.setAbrirSecciones("datosidentificacion", ConstantesBD.acronimoNo);
    	this.setAbrirSecciones("ingresoppal", ConstantesBD.acronimoNo);
    	this.setAbrirSecciones("ingreso", ConstantesBD.acronimoNo);
    	this.setAbrirSecciones("valoraciones", ConstantesBD.acronimoNo);
    	this.setAbrirSecciones("evolucion", ConstantesBD.acronimoNo);
    	this.setAbrirSecciones("notas", ConstantesBD.acronimoNo);
    	this.setAbrirSecciones("medico", ConstantesBD.acronimoNo);
	}

    /**
     * 
     *
     */
    public void resetCuadroTexto()
    {
    	this.cuadroTextoMap= new HashMap<Object, Object>();
    	this.cuadroTextoMap.put("numRegistros", 0);
    }
    
    
	/**
     * 
     *
     */
    public void resetDetalleBusqueda()
    {
    	this.fechasHorasMap=new HashMap<Object, Object>();
    	this.fechasHorasMap.put("numRegistros", ConstantesBD.codigoNuncaValido);
    	this.detalleMap=new HashMap<Object, Object>();
    	this.esConsultaNotasAclaratorias=false;
    }
    
    /**
     * 
     *
     */
    public void resetNotasAclaratorias()
    {
    	this.notasAclaratorias= new HashMap<Object, Object>();
    	this.notasAclaratorias.put("numRegistros", 0);
    	this.dtoNotaAclaratoria= new DtoNotasAclaratoriasEpicrisis();
    	this.indiceNotaAclaratoria=ConstantesBD.codigoNuncaValido;
    }
   
    /**
     * 
     *
     */
    public void resetAntecedentes()
    {
    	this.antecedentesAlergias="";
    	this.antecedentesFamiliares="";
    	this.antecedentesFamiliaresOculares="";
    	this.antecedentesMedicosYQx="";
    	this.antecedentesOdontologicos="";
    	this.antecedentesPediatricos="";
    	this.antecedentesPersonalesOculares="";
    	this.antecedentesToxicos="";
    	this.antecedentesTransfuncionales="";
    	this.antecedentesVarios="";
    	this.antecedentesMedicamentos="";
    	this.antecedentesVacunas= new HashMap<Object, Object>();
    	this.antecedentesVacunas.put("numRegistros", 0);
    	this.antecedentesGineco= new HashMap<Object, Object>();
    	this.antecedentesGineco.put("numRegistros", 0);
    	
    	this.antecedentesAmostrar= new Vector<String>();
		this.valoracionesInicialesAmostrar= new HashMap<Object, Object>();
		this.valoracionesInicialesAmostrar.put("numRegistros", "0");
    }
    
    /**
     * 
     *
     */
    public void resetMedicoElaboraEpicrisis()
    {
    	this.mapaMedicoElaboraEpicrisis= new HashMap<Object, Object>();
    	this.mapaMedicoElaboraEpicrisis.put("numRegistros", 0);
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
        
        if(estado.equals("busquedaAvanzada"))
        {
        	boolean seleccionoValInicial=false;
        	for (int w=0; w<Integer.parseInt(this.getValoracionesInicialesAmostrar("numRegistros").toString()); w++) 
        	{
        		if(UtilidadTexto.getBoolean(this.getValoracionesInicialesAmostrar("seleccionadaval_"+w)+""))
        			seleccionoValInicial=true;
			}
        	
        	boolean seleccionoTipoEvol=false;
        	for(int w=0; w<Integer.parseInt(this.getCriteriosBusquedaMap("numRegistros").toString()); w++)
        	{
        		if(UtilidadTexto.getBoolean(this.getCriteriosBusquedaMap("seleccionado_"+w)+""))
        			seleccionoTipoEvol=true;
        	}
        	
        	if(!seleccionoTipoEvol && !seleccionoValInicial)
        		errores.add("", new ActionMessage("errors.required", "Al menos un Tipo de Evolucion ó un Dato del Ingreso"));
        	
        	if(errores.isEmpty() && seleccionoTipoEvol)
        		errores=validarFechas(errores);
        	
        	if(!errores.isEmpty())
        		this.setEstado("continuarDefinirEpicrisis");
        		
        }
        
        ///////////////parte de notas aclaratorias
        else if(estado.equals("guardarNuevaNotaAclaratoria") || estado.equals("guardarModificarNotaAclaratoria"))
        {
        	if(UtilidadTexto.isEmpty(dtoNotaAclaratoria.getNota()))
        	{
        		errores.add("", new ActionMessage("errors.required", "La nota aclaratoria"));
        	}
        	/*if(!errores.isEmpty())
        		this.setEstado("continuarNotasAclaratorias);*/
        }
        
        else if(estado.equals("busquedaConsultaEpicrisis"))
        {
        	errores=validarFechas(errores);
        	if(!errores.isEmpty())
        		this.setEstado("continuarConsultaEpicrisis");
        }
        
        else if(estado.equals("finalizar"))
        {
        	if(!Epicrisis1.esValoracionInicialEnEpicrisis(this.valoracionesInicialesAmostrar))
        	{
        		errores.add("", new ActionMessage("errors.required", "La información de la(s) valoracion(es) inicial(es)"));
        		this.setEstado("continuarDefinirEpicrisis");
        	}
        }
        
        if(estado.equals("definirEpicrisis") || estado.equals("continuarDefinirEpicrisis") || estado.equals("guardar") || estado.equals("finalizar") || estado.equals("enviarAEpicrisis"))
        {	
        	if(this.getContenidoEncabezado().length()>9900)
        	{
        		errores.add("", new ActionMessage("error.historiaClinica.tamanioTextoExcedido", "de texto de la Epicrisis con fecha y hora "+(ingresosMap.get("fechaingreso_"+indice)+" "+ingresosMap.get("horaingreso_"+indice))+", debe ser máximo 10000"));
        		if(!errores.isEmpty())
            		this.setEstado("continuarDefinirEpicrisis");
        	}
	        for(int w=0; w< Utilidades.convertirAEntero(this.getCuadroTextoMap("numRegistros")+""); w++)
			{
	        	if((this.getCuadroTextoMap("contenido_"+w)+"").length()>9900)
	        	{
	        		errores.add("", new ActionMessage("error.historiaClinica.tamanioTextoExcedido", "de texto de la Epicrisis con fecha y hora "+cuadroTextoMap.get("fecha_"+w)+" "+cuadroTextoMap.get("hora_"+w)+", debe ser máximo 10000"));
	        		if(!errores.isEmpty())
	            		this.setEstado("continuarDefinirEpicrisis");
	        	}
			}
        }
        
        return errores;
    }

    /**
     * 
     * @param errores
     * @return
     */
    public ActionErrors validarFechas(ActionErrors errores)
    {
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getCriteriosBusquedaMap("fechainicial")+""))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
    	}
    	if(!UtilidadFecha.esFechaValidaSegunAp(this.getCriteriosBusquedaMap("fechafinal")+""))
    	{
    		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Final"));
    	}
    	//si no existen errores entonces se valida que la fecha inicio y final <= fecha sistema
    	if(errores.isEmpty())
    	{
    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechainicial")+"", UtilidadFecha.getFechaActual()))
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual "+UtilidadFecha.getFechaActual()));
    		}
    		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechafinal")+"", UtilidadFecha.getFechaActual()))
    		{
    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual "+UtilidadFecha.getFechaActual()));
    		}
    		if(errores.isEmpty())
    		{
    			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechainicial")+"", this.getCriteriosBusquedaMap("fechafinal")+""))
        		{
        			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Final"));
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
	 * @return the centrosAtencionMap
	 */
	public HashMap<Object, Object> getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setCentrosAtencionMap(HashMap<Object, Object> centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	/**
	 * @return the ingresosMap
	 */
	public HashMap<Object, Object> getIngresosMap() {
		return ingresosMap;
	}

	/**
	 * @param ingresosMap the ingresosMap to set
	 */
	public void setIngresosMap(HashMap<Object, Object> ingresosMap) {
		this.ingresosMap = ingresosMap;
	}
	
	/**
	 * @return the ingresosMap
	 */
	public Object getIngresosMap(Object key) {
		return ingresosMap.get(key);
	}

	/**
	 * @param ingresosMap the ingresosMap to set
	 */
	public void setIngresosMap(Object key, Object value) {
		this.ingresosMap.put(key, value);
	}

	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}

	/**
	 * @return the criteriosBusquedaMap
	 */
	public HashMap<Object, Object> getCriteriosBusquedaMap() {
		return criteriosBusquedaMap;
	}

	/**
	 * @param criteriosBusquedaMap the criteriosBusquedaMap to set
	 */
	public void setCriteriosBusquedaMap(HashMap<Object, Object> criteriosBusquedaMap) {
		this.criteriosBusquedaMap = criteriosBusquedaMap;
	}

	/**
	 * @return the criteriosBusquedaMap
	 */
	public Object getCriteriosBusquedaMap(Object key) {
		return criteriosBusquedaMap.get(key);
	}

	/**
	 * @param criteriosBusquedaMap the criteriosBusquedaMap to set
	 */
	public void setCriteriosBusquedaMap(Object key, Object value) {
		this.criteriosBusquedaMap.put(key, value);
	}

	/**
	 * @return the cuentasIngreso
	 */
	public Vector<String> getCuentasIngreso() {
		return cuentasIngreso;
	}

	/**
	 * @param cuentasIngreso the cuentasIngreso to set
	 */
	public void setCuentasIngreso(Vector<String> cuentasIngreso) {
		this.cuentasIngreso = cuentasIngreso;
	}

	/**
	 * @return the fechasHorasMap
	 */
	public HashMap<Object, Object> getFechasHorasMap() {
		return fechasHorasMap;
	}

	/**
	 * @param fechasHorasMap the fechasHorasMap to set
	 */
	public void setFechasHorasMap(HashMap<Object, Object> fechasHorasMap) {
		this.fechasHorasMap = fechasHorasMap;
	}
	
	/**
	 * @return the fechasHorasMap
	 */
	public Object getFechasHorasMap(Object key) {
		return fechasHorasMap.get(key);
	}

	/**
	 * @param fechasHorasMap the fechasHorasMap to set
	 */
	public void setFechasHorasMap(Object key, Object value) {
		this.fechasHorasMap.put(key, value);
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
	 * @return the detalleMap
	 */
	public HashMap<Object, Object> getDetalleMap() {
		return detalleMap;
	}

	/**
	 * @param detalleMap the detalleMap to set
	 */
	public void setDetalleMap(HashMap<Object, Object> detalleMap) {
		this.detalleMap = detalleMap;
	}
	
	/**
	 * @return the detalleMap
	 */
	public Object getDetalleMap(Object key) {
		return detalleMap.get(key);
	}

	/**
	 * @param detalleMap the detalleMap to set
	 */
	public void setDetalleMap(Object key, Object value) {
		this.detalleMap.put(key, value);
	}

	/**
	 * @return the cuadroTextoMap
	 */
	public HashMap<Object, Object> getCuadroTextoMap() {
		return cuadroTextoMap;
	}

	/**
	 * @param cuadroTextoMap the cuadroTextoMap to set
	 */
	public void setCuadroTextoMap(HashMap<Object, Object> cuadroTextoMap) {
		this.cuadroTextoMap = cuadroTextoMap;
	}
	
	/**
	 * @return the cuadroTextoMap
	 */
	public Object getCuadroTextoMap(Object key) {
		return cuadroTextoMap.get(key);
	}

	/**
	 * @param cuadroTextoMap the cuadroTextoMap to set
	 */
	public void setCuadroTextoMap(Object key, Object value) {
		this.cuadroTextoMap.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public int getNumRegCuadroTexto()
	{
		if(!UtilidadTexto.isEmpty(this.getCuadroTextoMap("numRegistros")+""))
		{
			return Integer.parseInt(this.getCuadroTextoMap("numRegistros")+"");
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the dtoNotasAclaratorias
	 */
	public DtoNotasAclaratoriasEpicrisis getDtoNotaAclaratoria() {
		return dtoNotaAclaratoria;
	}

	/**
	 * @param dtoNotasAclaratorias the dtoNotasAclaratorias to set
	 */
	public void setDtoNotaAclaratoria(
			DtoNotasAclaratoriasEpicrisis dtoNotasAclaratorias) {
		this.dtoNotaAclaratoria = dtoNotasAclaratorias;
	}

	/**
	 * @return the notasAclaratorias
	 */
	public HashMap<Object, Object> getNotasAclaratorias() {
		return notasAclaratorias;
	}

	/**
	 * @param notasAclaratorias the notasAclaratorias to set
	 */
	public void setNotasAclaratorias(HashMap<Object, Object> notasAclaratorias) {
		this.notasAclaratorias = notasAclaratorias;
	}
	
	/**
	 * @return the notasAclaratorias
	 */
	public Object getNotasAclaratorias(Object key) {
		return notasAclaratorias.get(key);
	}

	/**
	 * @param notasAclaratorias the notasAclaratorias to set
	 */
	public void setNotasAclaratorias(Object key, Object value) {
		this.notasAclaratorias.put(key, value);
	}

	/**
	 * @return the indiceNotaAclaratoria
	 */
	public int getIndiceNotaAclaratoria() {
		return indiceNotaAclaratoria;
	}

	/**
	 * @param indiceNotaAclaratoria the indiceNotaAclaratoria to set
	 */
	public void setIndiceNotaAclaratoria(int indiceNotaAclaratoria) {
		this.indiceNotaAclaratoria = indiceNotaAclaratoria;
	}

	/**
	 * @return the esConsultaNotasAclaratorias
	 */
	public boolean getEsConsultaNotasAclaratorias() {
		return esConsultaNotasAclaratorias;
	}

	/**
	 * @param esConsultaNotasAclaratorias the esConsultaNotasAclaratorias to set
	 */
	public void setEsConsultaNotasAclaratorias(boolean esConsultaNotasAclaratorias) {
		this.esConsultaNotasAclaratorias = esConsultaNotasAclaratorias;
	}

	/**
	 * @return the antecedentesVacunas
	 */
	public HashMap<Object, Object> getAntecedentesVacunas() {
		return antecedentesVacunas;
	}

	/**
	 * @param antecedentesVacunas the antecedentesVacunas to set
	 */
	public void setAntecedentesVacunas(HashMap<Object, Object> antecedentesVacunas) {
		this.antecedentesVacunas = antecedentesVacunas;
	}
	
	/**
	 * @return the antecedentesVacunas
	 */
	public Object getAntecedentesVacunas(Object key) {
		return antecedentesVacunas.get(key);
	}

	/**
	 * @param antecedentesVacunas the antecedentesVacunas to set
	 */
	public void setAntecedentesVacunas(Object key, Object value) {
		this.antecedentesVacunas.put(key, value);
	}

	/**
	 * @return the antecedentesGineco
	 */
	public HashMap<Object, Object> getAntecedentesGineco() {
		return antecedentesGineco;
	}

	/**
	 * @param antecedentesGineco the antecedentesGineco to set
	 */
	public void setAntecedentesGineco(HashMap<Object, Object> antecedentesGineco) {
		this.antecedentesGineco = antecedentesGineco;
	}
	
	/**
	 * @return the antecedentesGineco
	 */
	public Object getAntecedentesGineco(Object key) {
		return antecedentesGineco.get(key);
	}

	/**
	 * @param antecedentesGineco the antecedentesGineco to set
	 */
	public void setAntecedentesGineco(Object key, Object value) {
		this.antecedentesGineco.put(key, value);
	}

	/**
	 * @return the historicosGineco
	 */
	public ArrayList<Object> getHistoricosGineco() {
		return historicosGineco;
	}

	/**
	 * @param historicosGineco the historicosGineco to set
	 */
	public void setHistoricosGineco(ArrayList<Object> historicosGineco) {
		this.historicosGineco = historicosGineco;
	}

	/**
	 * @return the abrirSecciones
	 */
	public HashMap<String, String> getAbrirSecciones() {
		return abrirSecciones;
	}

	/**
	 * @param abrirSecciones the abrirSecciones to set
	 */
	public void setAbrirSecciones(HashMap<String, String> abrirSecciones) {
		this.abrirSecciones = abrirSecciones;
	}

	/**
	 * @return the abrirSecciones
	 */
	public String getAbrirSecciones(String key) {
		return abrirSecciones.get(key);
	}

	/**
	 * @param abrirSecciones the abrirSecciones to set
	 */
	public void setAbrirSecciones(String key, String value) {
		this.abrirSecciones.put(key, value);
	}

	/**
	 * @return the mapaMedicoElaboraEpicrisis
	 */
	public HashMap<Object, Object> getMapaMedicoElaboraEpicrisis() {
		return mapaMedicoElaboraEpicrisis;
	}

	/**
	 * @param mapaMedicoElaboraEpicrisis the mapaMedicoElaboraEpicrisis to set
	 */
	public void setMapaMedicoElaboraEpicrisis(
			HashMap<Object, Object> mapaMedicoElaboraEpicrisis) {
		this.mapaMedicoElaboraEpicrisis = mapaMedicoElaboraEpicrisis;
	}

	/**
	 * @return the mapaMedicoElaboraEpicrisis
	 */
	public Object getMapaMedicoElaboraEpicrisis(Object key) {
		return mapaMedicoElaboraEpicrisis.get(key);
	}

	/**
	 * @param mapaMedicoElaboraEpicrisis the mapaMedicoElaboraEpicrisis to set
	 */
	public void setMapaMedicoElaboraEpicrisis(Object key, Object value) {
		this.mapaMedicoElaboraEpicrisis.put(key, value);
	}

	/**
	 * @return the antecedentesAlergias
	 */
	public String getAntecedentesAlergias() {
		return antecedentesAlergias;
	}

	/**
	 * @param antecedentesAlergias the antecedentesAlergias to set
	 */
	public void setAntecedentesAlergias(String antecedentesAlergias) {
		this.antecedentesAlergias = antecedentesAlergias;
	}

	/**
	 * @return the antecedentesFamiliares
	 */
	public String getAntecedentesFamiliares() {
		return antecedentesFamiliares;
	}

	/**
	 * @param antecedentesFamiliares the antecedentesFamiliares to set
	 */
	public void setAntecedentesFamiliares(String antecedentesFamiliares) {
		this.antecedentesFamiliares = antecedentesFamiliares;
	}

	/**
	 * @return the antecedentesFamiliaresOculares
	 */
	public String getAntecedentesFamiliaresOculares() {
		return antecedentesFamiliaresOculares;
	}

	/**
	 * @param antecedentesFamiliaresOculares the antecedentesFamiliaresOculares to set
	 */
	public void setAntecedentesFamiliaresOculares(
			String antecedentesFamiliaresOculares) {
		this.antecedentesFamiliaresOculares = antecedentesFamiliaresOculares;
	}

	/**
	 * @return the antecedentesMedicamentos
	 */
	public String getAntecedentesMedicamentos() {
		return antecedentesMedicamentos;
	}

	/**
	 * @param antecedentesMedicamentos the antecedentesMedicamentos to set
	 */
	public void setAntecedentesMedicamentos(String antecedentesMedicamentos) {
		this.antecedentesMedicamentos = antecedentesMedicamentos;
	}

	/**
	 * @return the antecedentesMedicosYQx
	 */
	public String getAntecedentesMedicosYQx() {
		return antecedentesMedicosYQx;
	}

	/**
	 * @param antecedentesMedicosYQx the antecedentesMedicosYQx to set
	 */
	public void setAntecedentesMedicosYQx(String antecedentesMedicosYQx) {
		this.antecedentesMedicosYQx = antecedentesMedicosYQx;
	}

	/**
	 * @return the antecedentesOdontologicos
	 */
	public String getAntecedentesOdontologicos() {
		return antecedentesOdontologicos;
	}

	/**
	 * @param antecedentesOdontologicos the antecedentesOdontologicos to set
	 */
	public void setAntecedentesOdontologicos(String antecedentesOdontologicos) {
		this.antecedentesOdontologicos = antecedentesOdontologicos;
	}

	/**
	 * @return the antecedentesPediatricos
	 */
	public String getAntecedentesPediatricos() {
		return antecedentesPediatricos;
	}

	/**
	 * @param antecedentesPediatricos the antecedentesPediatricos to set
	 */
	public void setAntecedentesPediatricos(String antecedentesPediatricos) {
		this.antecedentesPediatricos = antecedentesPediatricos;
	}

	/**
	 * @return the antecedentesPersonalesOculares
	 */
	public String getAntecedentesPersonalesOculares() {
		return antecedentesPersonalesOculares;
	}

	/**
	 * @param antecedentesPersonalesOculares the antecedentesPersonalesOculares to set
	 */
	public void setAntecedentesPersonalesOculares(
			String antecedentesPersonalesOculares) {
		this.antecedentesPersonalesOculares = antecedentesPersonalesOculares;
	}

	/**
	 * @return the antecedentesToxicos
	 */
	public String getAntecedentesToxicos() {
		return antecedentesToxicos;
	}

	/**
	 * @param antecedentesToxicos the antecedentesToxicos to set
	 */
	public void setAntecedentesToxicos(String antecedentesToxicos) {
		this.antecedentesToxicos = antecedentesToxicos;
	}

	/**
	 * @return the antecedentesTransfuncionales
	 */
	public String getAntecedentesTransfuncionales() {
		return antecedentesTransfuncionales;
	}

	/**
	 * @param antecedentesTransfuncionales the antecedentesTransfuncionales to set
	 */
	public void setAntecedentesTransfuncionales(String antecedentesTransfuncionales) {
		this.antecedentesTransfuncionales = antecedentesTransfuncionales;
	}

	/**
	 * @return the antecedentesVarios
	 */
	public String getAntecedentesVarios() {
		return antecedentesVarios;
	}

	/**
	 * @param antecedentesVarios the antecedentesVarios to set
	 */
	public void setAntecedentesVarios(String antecedentesVarios) {
		this.antecedentesVarios = antecedentesVarios;
	}

	/**
	 * @return the antecedentesAmostrar
	 */
	public Vector<String> getAntecedentesAmostrar() {
		return antecedentesAmostrar;
	}

	/**
	 * @param antecedentesAmostrar the antecedentesAmostrar to set
	 */
	public void setAntecedentesAmostrar(Vector<String> antecedentesAmostrar) {
		this.antecedentesAmostrar = antecedentesAmostrar;
	}

	/**
	 * @param finalizada the finalizada to set
	 */
	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}
	
	/**
	 * @return the finalizada
	 */
	public boolean getFinalizada() {
		return finalizada;
	}

	/**
	 * @return the valoracionesInicialesAmostrar
	 */
	public HashMap<Object, Object> getValoracionesInicialesAmostrar() 
	{
		return valoracionesInicialesAmostrar;
	}

	/**
	 * @param valoracionesInicialesAmostrar the valoracionesInicialesAmostrar to set
	 */
	public void setValoracionesInicialesAmostrar(HashMap<Object, Object> valoracionesInicialesAmostrar) 
	{
		this.valoracionesInicialesAmostrar = valoracionesInicialesAmostrar;
	}
	
	/**
	 * @return the valoracionesInicialesAmostrar
	 */
	public Object getValoracionesInicialesAmostrar(Object key) 
	{
		return valoracionesInicialesAmostrar.get(key);
	}

	/**
	 * @param valoracionesInicialesAmostrar the valoracionesInicialesAmostrar to set
	 */
	public void setValoracionesInicialesAmostrar(Object key, Object value) 
	{
		this.valoracionesInicialesAmostrar.put(key, value);
	}

	/**
	 * @return the egresoMedico
	 */
	public boolean isEgresoMedico() {
		return egresoMedico;
	}

	/**
	 * @param egresoMedico the egresoMedico to set
	 */
	public void setEgresoMedico(boolean egresoMedico) {
		this.egresoMedico = egresoMedico;
	}
	
	/**
	 * @return the egresoMedico
	 */
	public boolean getEgresoMedico() {
		return egresoMedico;
	}

	/**
	 * @return the contenidoEncabezado
	 */
	public String getContenidoEncabezado() {
		return contenidoEncabezado;
	}

	/**
	 * @param contenidoEncabezado the contenidoEncabezado to set
	 */
	public void setContenidoEncabezado(String contenidoEncabezado) {
		this.contenidoEncabezado = contenidoEncabezado;
	}
	
	/**
	 * @return the imprimirAutomaticamente
	 */
	public boolean isImprimirAutomaticamente() {
		return imprimirAutomaticamente;
	}

	/**
	 * @param imprimirAutomaticamente the imprimirAutomaticamente to set
	 */
	public void setImprimirAutomaticamente(boolean imprimirAutomaticamente) {
		this.imprimirAutomaticamente = imprimirAutomaticamente;
	}

	
}
