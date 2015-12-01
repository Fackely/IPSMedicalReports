/*
 * @(#)ConsultoriosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;

/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion de consultorios
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Mar 12, 2008
 * @author Wilson Rios wrios@princetonsa.com
 */
public class ConsultoriosForm extends ValidatorForm 
{
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * mapa de consultorios
	 */
	private HashMap consultoriosMap;
	
	/**
	 * clon de consultoriosmap al momento de cargarlos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	private HashMap consultoriosEliminadosMap;
	
	/**
	 * mapa para mostrar el select de los n centros de atencion
	 */
	private HashMap centrosAntencionTagMap;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
    /**
     * indice del mapa que se desea eliminar
     */
    private int indexEliminado;
    
    /**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
    
	/**
	 * 
	 */
	private String acronimoTipo;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.centroAtencion=ConstantesBD.codigoNuncaValido;
    	this.consultoriosMap= new HashMap();
    	this.consultoriosMap.put("numRegistros", "0");
    	this.consultoriosEliminadosMap= new HashMap();
    	this.consultoriosEliminadosMap.put("numRegistros", "0");
    	this.centrosAntencionTagMap= new HashMap();
    	this.linkSiguiente="";
    	this.indexEliminado=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.acronimoTipo="";
    }
    
    /**
     * inicializa los tags de la forma
     * @param codigoInstitucionInt
     */
    public void inicializarTags(int codigoInstitucion) 
    {
		this.centrosAntencionTagMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion); 
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
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) 
    {
        ActionErrors errores= new ActionErrors();
        
        //errores=super.validate(mapping,request);
        if(estado.equals("guardar"))
        {
        	int numReg=Integer.parseInt(this.consultoriosMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.consultoriosMap.get("codigoconsultorio_"+i)+"").trim().equals(""))
				{
					errores.add("codigoconsultorio", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					int cod=0;
					boolean entroError=false;
					//debe ser numerico
					try
					{
						cod=Integer.parseInt(this.consultoriosMap.get("codigoconsultorio_"+i)+"");
					}
					catch (Exception e) 
					{
						errores.add("", new ActionMessage("errors.integerMayorQue", "El Codigo del registro "+(i+1), "0"));
						entroError=true;
					}
					if(cod<1)
					{
						errores.add("", new ActionMessage("errors.integerMayorQue", "El Codigo del registro "+(i+1), "0"));
						entroError=true;
					}
					if(!entroError)
					{	
						for(int j=0;j<i;j++)
						{
							if((this.consultoriosMap.get("codigoconsultorio_"+i)+"").equalsIgnoreCase(this.consultoriosMap.get("codigoconsultorio_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.consultoriosMap.get("codigoconsultorio_"+i)));
							}
						}
					}	
				}
				if((this.consultoriosMap.get("descripcion_"+i)+"").trim().equals(""))
				{
					errores.add("desc", new ActionMessage("errors.required","La Descripción del registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.consultoriosMap.get("descripcion_"+i)+"").equalsIgnoreCase(this.consultoriosMap.get("descripcion_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","La descripción "+this.consultoriosMap.get("descripcion_"+i)));
						}
					}
				}
			}
        }
        
        if(errores.isEmpty()){
        	setErrorGuardando(false);
        }
        else{
        	setErrorGuardando(true);
        }
               
        return null;
    }

    private boolean errorGuardando = false;
    
    
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
	 * @return the centrosAntencionTagMap
	 */
	public HashMap getCentrosAntencionTagMap() {
		return centrosAntencionTagMap;
	}

	/**
	 * @param centrosAntencionTagMap the centrosAntencionTagMap to set
	 */
	public void setCentrosAntencionTagMap(HashMap centrosAntencionTagMap) {
		this.centrosAntencionTagMap = centrosAntencionTagMap;
	}

	/**
	 * @return the centrosAntencionTagMap
	 */
	public Object getCentrosAntencionTagMap(Object key) {
		return centrosAntencionTagMap.get(key);
	}

	/**
	 * @param centrosAntencionTagMap the centrosAntencionTagMap to set
	 */
	public void setCentrosAntencionTagMap(Object value, Object key) {
		this.centrosAntencionTagMap.put(key, value);
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
	 * @return the consultoriosMap
	 */
	public HashMap getConsultoriosMap() {
		return consultoriosMap;
	}

	/**
	 * @param consultoriosMap the consultoriosMap to set
	 */
	public void setConsultoriosMap(HashMap consultoriosMap) {
		this.consultoriosMap = consultoriosMap;
	}
    
	/**
	 * @return the consultoriosMap
	 */
	public Object getConsultoriosMap(Object key) {
		return consultoriosMap.get(key);
	}

	/**
	 * @param consultoriosMap the consultoriosMap to set
	 */
	public void setConsultoriosMap(Object key, Object value) {
		this.consultoriosMap.put(key, value);
	}

	
	
	/**
	 * @return the consultoriosNoModificadosMap
	 */
	public Object getConsultoriosEliminadosMap(Object key) {
		return consultoriosEliminadosMap.get(key);
	}

	/**
	 * @param consultoriosNoModificadosMap the consultoriosNoModificadosMap to set
	 */
	public void setConsultoriosEliminadosMap(Object key, Object value) {
		this.consultoriosEliminadosMap.put(key, value);
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
	 * @return the consultoriosEliminadosMap
	 */
	public HashMap getConsultoriosEliminadosMap() {
		return consultoriosEliminadosMap;
	}

	/**
	 * @param consultoriosEliminadosMap the consultoriosEliminadosMap to set
	 */
	public void setConsultoriosEliminadosMap(HashMap consultoriosEliminadosMap) {
		this.consultoriosEliminadosMap = consultoriosEliminadosMap;
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
	 * @return the acronimoTipo
	 */
	public String getAcronimoTipo() {
		return acronimoTipo;
	}

	/**
	 * @param acronimoTipo the acronimoTipo to set
	 */
	public void setAcronimoTipo(String acronimoTipo) {
		this.acronimoTipo = acronimoTipo;
	}

	public void setErrorGuardando(boolean errorGuardando) {
		this.errorGuardando = errorGuardando;
	}

	public boolean isErrorGuardando() {
		return errorGuardando;
	}
    
}