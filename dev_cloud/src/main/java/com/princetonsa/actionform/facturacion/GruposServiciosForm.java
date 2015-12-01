/*
 * Ene 18, 2006
 *
 */
package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Grupos de Servicios
 */
public class GruposServiciosForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GruposServiciosForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto que almacena los registros de grupos servicios
	 */
	private HashMap gruposMap = new HashMap();
	
	/**
	 * Número de registros del mapa gruposMap
	 */
	private int numGrupos;
	
	/**
	 * Posición del registro dentro del mapa gruposMap
	 * que va a ser eliminado
	 */
	private int pos;
	
	/**
	 * Columna por la cual se ordenará
	 */
	private String indice;
		
	/**
	 * Última columna por la cual se ordenó
	 */
	private String ultimoIndice;
	
	/**
	 * HashMap tipos_salas
	 */
	private HashMap tiposSalas = new HashMap();
	
	/**
	 * HashMap tipos_monto
	 */
	private HashMap tiposMontos = new HashMap();
	
		
	/**
	 * reset de los datos de la forma
	 *
	 */
	
	public void reset()
	{
		this.estado="";
		this.gruposMap = new HashMap();
		this.numGrupos = 0;
		this.pos = -1;
		this.indice = "";
		this.ultimoIndice = "";
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
		
		if(this.estado.equals("guardar"))
		{
			String auxS0 = "";
			
			/*
			//se verifican los acrónimo [Códigos] como requeridos
			for(int i=0;i<this.numGrupos;i++)
			{
				auxS0 = this.getGruposMap("acronimo_"+i) + ""; 
				if(auxS0.equals(""))
					errores.add("acronimo requerido",new ActionMessage("errors.required","El código del registro Nº "+(i+1)));
			}
			*/
			
			//se verifican las descripciones como requeridas
			for(int i=0;i<this.numGrupos;i++)
			{
				auxS0 = this.getGruposMap("descripcion_"+i) + ""; 
				if(auxS0.equals(""))
					errores.add("descripción requerida",new ActionMessage("errors.required","La descripción del registro Nº "+(i+1)));
			}
			
			// sección para validar que no hayan códigos repetidos
			/*
			String auxS1 = "";
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			for(int i=0;i<this.numGrupos;i++)
			{
				auxS0=this.getGruposMap("acronimo_"+i)+"";
				
				for(int j=this.numGrupos-1;j>i;j--)
				{
					
					auxS1=this.getGruposMap("acronimo_"+j)+"";
					//se compara
					if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")
						&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("códigos iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								"códigos","en los registros Nº "+descripcion));
				}
				
				descripcion = "";
				codigosComparados.put(i+"",auxS0);
				
			}
			*/
			if(!errores.isEmpty())
				this.estado = "empezarIngresar";
			
		}
		return errores;
	}
	
	
	
	

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
	 * @return Returns the gruposMap.
	 */
	public HashMap getGruposMap() {
		return gruposMap;
	}
	/**
	 * @param gruposMap The gruposMap to set.
	 */
	public void setGruposMap(HashMap gruposMap) {
		this.gruposMap = gruposMap;
	}
	/**
	 * @return Retorna un elemento del mapa gruposMap.
	 */
	public Object getGruposMap(String key) {
		return gruposMap.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa gruposMap.
	 */
	public void setGruposMap(String key,Object obj) {
		this.gruposMap.put(key,obj);
	}
	/**
	 * @return Returns the numGrupos.
	 */
	public int getNumGrupos() {
		return numGrupos;
	}
	/**
	 * @param numGrupos The numGrupos to set.
	 */
	public void setNumGrupos(int numGrupos) {
		this.numGrupos = numGrupos;
	}
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
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
	 * Método setTiposSalas
	 * @param tiposSalas
	 */
	public void setTiposSalas(HashMap tiposSalas) {
		this.tiposSalas = tiposSalas;
	}

	/**
	 * Método getTiposSalas
	 * @return HashMap tiposSalas
	 */
	public HashMap getTiposSalas() {
		return tiposSalas;
	}
	
	public Object getTiposSalas(String key) {
		return tiposSalas.get(key);
	}
	public void setTiposSalas(String key,Object value) {
		this.tiposSalas.put(key, value);
	}

	/**
	 * @return the tiposMontos
	 */
	public HashMap getTiposMontos() {
		return tiposMontos;
	}

	/**
	 * @param tiposMontos
	 */
	public void setTiposMontos(HashMap tiposMontos) {
		this.tiposMontos = tiposMontos;
	}
	
	public Object getTiposMontos(String key) {
		return tiposMontos.get(key);
	}
	public void setTiposMontos(String key,Object value) {
		this.tiposMontos.put(key, value);
	}
}
