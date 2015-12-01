package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;

import com.princetonsa.action.inventarios.SeccionesAction;


public class SustitutosNoPosForm extends ValidatorForm
{
	
	Logger logger = Logger.getLogger(SeccionesAction.class);
		
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
	 * 
	 */
	private String dosificacion;
	
	/**
	 * 
	 */
	private String dosisDiaria;
	
	/**
	 * 
	 */
	private String tiempoTratamiento;
	
	/**
	 * 
	 */
	private String numDosisEquivalentes;
	
	/**
	 * 
	 */
	private String subgrupo;
	
	/**
	 * 
	 */
	private HashMap resultadosMap;
	
	/**
	 * 
	 */
	private HashMap resultadosCGMap;
	
	/**
	 * 
	 */
	private HashMap informacionArtPrin;
	
	/**
	 * 
	 */
	private String articuloSustituto;
	
	/**
	 * 
	 */
	private String articuloPrincipal;
	
	/**
	 * Variable para manejar los poUp de seleccion de articulo y articulo No Pos
	 */
	private String esArtNoPos;
	
	/**
	 * 
	 */
	private String indexMap;
	
	/**
	 * 
	 */
	private String codNueArtSus;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * 
	 */
	private String nomModSus;

	public String getNomModSus() {
		return nomModSus;
	}



	public void setNomModSus(String nomModSus) {
		this.nomModSus = nomModSus;
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
        
        if(this.estado.equals("insertar"))
        {
        	if(UtilidadTexto.isEmpty(this.getInformacionArtPrin("codigoArticuloSus").toString()))
        		errores.add("Campo Código Articulo Sustituto requerido",new ActionMessage("errors.required", "El Código del Articulo Sustituto "));
        	
        	if(UtilidadTexto.isEmpty(this.getInformacionArtPrin("descripcionArticuloSus").toString()))
        		errores.add("Campo Descripcion Articulo Sustituto requerido",new ActionMessage("errors.required", "La Descripcion del Articulo Sustituto "));
        	
        	if(UtilidadTexto.isEmpty(this.getInformacionArtPrin("dosificacionNuevo").toString()))
        		errores.add("Campo Dosificacion requerido",new ActionMessage("errors.required", "La Dosificacion del Sustituto "));
        	
        	if(UtilidadTexto.isEmpty(this.getInformacionArtPrin("dosisDiariaNuevo").toString()))
        		errores.add("Campo Dosis Diaria requerido",new ActionMessage("errors.required", "La Dosis Diaria del Sustituto "));
        	
        	if(UtilidadTexto.isEmpty(this.getInformacionArtPrin("tiempoTratamientoNuevo").toString()))
        		errores.add("Campo Tiempo de Tratamiento requerido",new ActionMessage("errors.required", "El Tiempo de Tratamiento del Sustituto "));
        	
        	if(UtilidadTexto.isEmpty(this.getInformacionArtPrin("numDosisEquivalentesNuevo").toString()))
        		errores.add("Campo # de Dosis Equivalentes requerido",new ActionMessage("errors.required", "El # de Dosis Equivalentes del Sustituto "));
        		
        	if(!errores.isEmpty())
        		this.setEstado("guardarNuevo");
        }
        if(this.estado.equals("guardarModificacion"))
        {
        	if(UtilidadTexto.isEmpty(this.getDosificacion()))
        		errores.add("Campo Dosificacion requerido",new ActionMessage("errors.required", "La Dosificacion del Sustituto "));
        	
           	if(UtilidadTexto.isEmpty(this.getDosisDiaria()))
        		errores.add("Campo Dosis Diaria requerido",new ActionMessage("errors.required", "La Dosis Diaria del Sustituto "));
           	
           	if(UtilidadTexto.isEmpty(this.getTiempoTratamiento()))
        		errores.add("Campo Tiempo de Tratamiento requerido",new ActionMessage("errors.required", "El Tiempo de Tratamiento del Sustituto "));
        	
        	if(UtilidadTexto.isEmpty(this.getNumDosisEquivalentes()))
        		errores.add("Campo # de Dosis Equivalentes requerido",new ActionMessage("errors.required", "El # de Dosis Equivalentes del Sustituto "));
        		
        	if(!errores.isEmpty())
        		this.setEstado("modificarRegistro");
        }
        return errores;        
    }



	/**
	 * 
	 */
	public void reset( int codigoInstitucion)
	{
		
		this.patronOrdenar="";		
		this.dosificacion="";
		this.dosisDiaria="";
		this.tiempoTratamiento="";
		this.numDosisEquivalentes="";		
		this.resultadosMap= new HashMap();
		resultadosMap.put("numRegistros", 0);
		this.resultadosCGMap= new HashMap();
		resultadosCGMap.put("numRegistros", 0);
		this.esArtNoPos = "";
		this.informacionArtPrin = new HashMap();
		informacionArtPrin.put("numRegistros", 0);
		this.ultimoPatron="";
		
	}
	
	

	public String getDosificacion() {
		return dosificacion;
	}

	public void setDosificacion(String dosificacion) {
		this.dosificacion = dosificacion;		
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	public String getEsArtNoPos() {
		return esArtNoPos;
	}


	public void setEsArtNoPos(String esArtNoPos) {
		this.esArtNoPos = esArtNoPos;
	}

	public HashMap getInformacionArtPrin() {
		return informacionArtPrin;
	}

	public void setInformacionArtPrin(HashMap informacionArtPrin) {
		this.informacionArtPrin = informacionArtPrin;
	}
	
	public Object getInformacionArtPrin(String key) {
		return informacionArtPrin.get(key);
	}


	public void setInformacionArtPrin(String key, Object value) {
		this.informacionArtPrin.put(key, value);
	}


	public HashMap getResultadosMap() {
		return resultadosMap;
	}


	public void setResultadosMap(HashMap resultados) {
		this.resultadosMap = resultados;
	}
	
	public Object getResultadosMap(String key) {
		return resultadosMap.get(key);
	}


	public void setResultadosMap(String key, Object value) {
		this.resultadosMap.put(key, value);
	}

	public String getArticuloSustituto() {
		return articuloSustituto;
	}


	public void setArticuloSustituto(String articuloSustituto) {
		this.articuloSustituto = articuloSustituto;
	}

	public String getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}



	public String getArticuloPrincipal() {
		return articuloPrincipal;
	}



	public void setArticuloPrincipal(String articuloPrincipal) {
		this.articuloPrincipal = articuloPrincipal;
	}



	public String getDosisDiaria() {
		return dosisDiaria;
	}



	public void setDosisDiaria(String dosisDiaria) {
		this.dosisDiaria = dosisDiaria;
	}



	public String getNumDosisEquivalentes() {
		return numDosisEquivalentes;
	}



	public void setNumDosisEquivalentes(String numDosisEquivalentes) {
		this.numDosisEquivalentes = numDosisEquivalentes;
	}



	public String getTiempoTratamiento() {
		return tiempoTratamiento;
	}



	public void setTiempoTratamiento(String tiempoTratamiento) {
		this.tiempoTratamiento = tiempoTratamiento;
	}



	public String getCodNueArtSus() {
		return codNueArtSus;
	}



	public void setCodNueArtSus(String codNueArtSus) {
		this.codNueArtSus = codNueArtSus;
	}



	public HashMap getResultadosCGMap() {
		return resultadosCGMap;
	}



	public void setResultadosCGMap(HashMap resultadosCGMap) {
		this.resultadosCGMap = resultadosCGMap;
	}
	
	public Object getResultadosCGMap(String key) {
		return resultadosCGMap.get(key);
	}


	public void setResultadosCGMap(String key, Object value) {
		this.resultadosCGMap.put(key, value);
	}



	public String getSubgrupo() {
		return subgrupo;
	}



	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}
}