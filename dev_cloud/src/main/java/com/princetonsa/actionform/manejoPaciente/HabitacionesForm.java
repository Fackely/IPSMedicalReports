/*
 * @(#)HabitacionesForm.java
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
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion de habitaciones
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Abril 30, 200
 * @author Julián Pacheco jpacheco@princetonsa.com
 */

public class HabitacionesForm extends ValidatorForm 
{
	/**
	 * Estado en el que se encuentra el proceso.  
	 */
	private String estado;
	
	/**
	 * Centro de Atencion
	 */
	private int centroAtencion;
	
	/**
	 * Mapa de habitaciones
	 */
	private HashMap habitacionesMap;
	
	/**
	 * Mapa de habitaciones que almacena las datos de consulta
	 * */
	//private HashMap habitacionesConsultaMap;
	
	/**
	 * Mapa de eliminacion de habitaciones 
	 */
	private HashMap habitacionesEliminadosMap;
	
	/**
	 * mapa para mostrar el select de los n centros de atencion
	 */
	private HashMap centrosAntencionTagMap;
	
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
	private int indice;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	

//	--------------------------------METODOS---------------------------------------	
	
	/**
	 * resetea los atributos del form
	 *
	 */
	public void reset(int codCentroAtencion)
	{
		this.centroAtencion=codCentroAtencion;
		this.habitacionesMap= new HashMap();
		this.habitacionesMap.put("numRegistros", 0);
		//this.habitacionesConsultaMap= new HashMap();
		//this.habitacionesConsultaMap.put("numRegistros", 0);
		this.habitacionesEliminadosMap= new HashMap();
		this.habitacionesEliminadosMap.put("numRegistros", 0);
		this.centrosAntencionTagMap=new HashMap();
		this.indice=-1;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.linkSiguiente="";
		
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
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
	    ActionErrors errores= new ActionErrors();
	    errores=super.validate(mapping,request);
	    if(estado.equals("guardar"))			
		{					 	
			int numReg=Integer.parseInt(this.habitacionesMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(UtilidadTexto.isEmpty(this.habitacionesMap.get("piso_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Piso del registro N° "+(i+1)));
				}
				if(UtilidadTexto.isEmpty(this.habitacionesMap.get("codigotipohabitac_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El tipo de habitación del registro N° "+(i+1)));
				}
				if(UtilidadTexto.isEmpty(this.habitacionesMap.get("codigohabitac_"+i).toString()))
				{
					errores.add("codigohabitac",new ActionMessage("errors.required","El Código Habitación del registro N° "+(i+1)));
				}
				else if(UtilidadCadena.tieneCaracteresEspecialesNumeroId(this.habitacionesMap.get("codigohabitac_"+i).toString()))
				{
					errores.add("", new ActionMessage("errors.caracteresInvalidos","El código de la habitación del registro N° "+(i+1)+": "+this.habitacionesMap.get("codigohabitac_"+i)));
				}
				else if(this.habitacionesMap.get("codigohabitac_"+i).toString().trim().length()>8)
				{
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El número de caracteres del código de la habitación del registro N° "+(i+1),"8"));
				}
				if(UtilidadTexto.isEmpty(this.habitacionesMap.get("nombre_"+i).toString()))
				{
					errores.add("",new ActionMessage("errors.required","El Nombre de la Habitación del registro N° "+(i+1)));
				}
				else if(UtilidadCadena.tieneCaracteresEspecialesTextoRips(this.habitacionesMap.get("nombre_"+i).toString()))
				{
					errores.add("", new ActionMessage("errors.caracteresInvalidos","El nombre de la habitación del registro N° "+(i+1)+": "+this.habitacionesMap.get("nombre_"+i)));
				}
				else if(this.habitacionesMap.get("nombre_"+i).toString().trim().length()>128)
				{
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El número de caracteres del nombre de la habitación del registro N° "+(i+1),"128"));
				}
				
				if(errores.isEmpty())
				{
					if(this.habitacionesMap.get("estabd_"+i).toString().trim().equals(ConstantesBD.acronimoNo))
					{
						for(int j=0; j<numReg; j++)
						{
							if(this.habitacionesMap.get("codigohabitac_"+j).toString().trim().equals(this.habitacionesMap.get("codigohabitac_"+i).toString().trim()) && (j!=i))
							{
								errores.add("codigohabitac",new ActionMessage("errors.yaExiste","El Código de Habitación "+this.habitacionesMap.get("codigohabitac_"+i)));							
							}							
						}
					}	
				}
			}
		}
	    return errores;
	}
	
//	--------------------------------------------Fin Validate--------------------------------------------------------
	
//	---------------------------------Getters and Setters-----------------------------

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
	 * @return the habitacionesMap
	 */
	public HashMap getHabitacionesMap() {
		return habitacionesMap;
	}

	/**
	 * @param habitacionesMap the habitacionesMap to set
	 */
	public void setHabitacionesMap(HashMap habitacionesMap) {
		this.habitacionesMap = habitacionesMap;
	}
	
	/**
	 * @return the habitacionesMap
	 */
	public Object getHabitacionesMap(Object key) {
		return habitacionesMap.get(key);
	}
	
	/**
	 * @param habitacionesMap the habitacionesMap to set
	 */
	public void setHabitacionesMap(Object key, Object values) {
		this.habitacionesMap.put(key, values);
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
	 * @return the habitacionesEliminadosMap
	 */
	public HashMap getHabitacionesEliminadosMap() {
		return habitacionesEliminadosMap;
	}

	/**
	 * @param habitacionesEliminadosMap the habitacionesEliminadosMap to set
	 */
	public void setHabitacionesEliminadosMap(HashMap habitacionesEliminadosMap) {
		this.habitacionesEliminadosMap = habitacionesEliminadosMap;
	}
	
	/** 
	 * @param key
	 * @return
	 */
	public Object getHabitacionesEliminadosMap(Object key) {
		return habitacionesEliminadosMap.get(key);
	}

	/**
	 * @param key
	 * @param values
	 */
	public void setHabitacionesEliminadosMap(Object key, Object values) {
		this.habitacionesEliminadosMap.put(key, values);
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
	 * @return the habitacionesConsultaMap
	 */
/*
	public HashMap getHabitacionesConsultaMap() {
		return habitacionesConsultaMap;
	}
*/
	/**
	 * @param habitacionesConsultaMap the habitacionesConsultaMap to set
	 */
	/*public void setHabitacionesConsultaMap(HashMap habitacionesConsultaMap) {
		this.habitacionesConsultaMap = habitacionesConsultaMap;
	}*/
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	/*public void setHabitacionesConsultaMap(String key, Object value){
		this.habitacionesConsultaMap.put(key, value);
	}*/
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	/*public Object getHabitacionesConsultaMap(String key){
		return habitacionesConsultaMap.get(key);
	}*/	
	
	
}
