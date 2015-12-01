/*
 * @(#)CentrosCostoViaIngresoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;


/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de los Centros de Costo X Vias de Ingreso
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. May 15, 2006
 * @author cperalta 
 */
public class CentrosCostoViaIngresoForm extends ValidatorForm 
{
	/**
	 * Atributo que almacena el listado de 
	 * centros de costo por via de ingreso
	 * existentes en el sistema.
	 */
	private ArrayList<DtoCentroCostoViaIngreso> listaCentroCostoViaIngreso;
	
	/**
	 * Mapa con los centros de costo
	 */
	private HashMap mapaCentrosCosto;
	
	/**
	 * Mapa con los centros de costo  que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaCentrosCostoNoModificado;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";
	
	/**
	 * Offset para el pager 
	 */
	private int offset;
	
	/**
	 * Posicion del mapa
	 */
	private int posicionMapa;
	
	/**
	 * String con mensajes opcionales
	 */
	private String mensaje="";
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	
	/** Indica que el proceso fue exitoso * */
	private boolean mensajeExitoso;
	
	
	/**
	 * Valor select
	 */
	private HashMap valorSelect;
	/**
	 * Reset los valores de la forma
	 */
	public void reset()
	{
	    this.linkSiguiente = "";
	    this.mapaCentrosCosto = new HashMap();
	    this.mapaCentrosCostoNoModificado = new HashMap();
	    this.posicionMapa = 0;
	    this.patronOrdenar = "";
	    this.ultimoPatron = "";
	    this.offset = 0;
	    this.valorSelect = new HashMap();
	    this.listaCentroCostoViaIngreso= new ArrayList<DtoCentroCostoViaIngreso>();
	    this.mensajeExitoso = false;
	}
	
	/**
	 * Reset unico para el mensaje 
	 */
	public void resetMensaje()
	{
		this.mensaje = "";
		this.mensajeExitoso = false;
	}
	
	/**
	 * Reset para los mapas de la forma
	 */
	public void resetMapa()
	{
	    this.mapaCentrosCosto = new HashMap();
	    this.mapaCentrosCostoNoModificado = new HashMap();
	}

	
	
	
	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron=ultimoPatron;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
	}

	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje=mensaje;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset=offset;
	}

	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}

	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa=posicionMapa;
	} 
	
	/**
	 * @return Returns the mapaCentrosCosto.
	 */
	public HashMap getMapaCentrosCosto()
	{
		return mapaCentrosCosto;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCentrosCosto to set.
	 */
	public void setMapaCentrosCosto(HashMap mapaCentrosCosto)
	{
		this.mapaCentrosCosto= mapaCentrosCosto;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCosto(String key, Object value) 
	{
		mapaCentrosCosto.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCosto(String key) 
	{
		return mapaCentrosCosto.get(key);
	}
	
	/**
	 * @return Returns the mapaCentrosCosto.
	 */
	public HashMap getMapaCentrosCostoNoModificado()
	{
		return mapaCentrosCostoNoModificado;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCentrosCostoNoModificado to set.
	 */
	public void setMapaCentrosCostoNoModificado(HashMap mapaCentrosCostoNoModificado)
	{
		this.mapaCentrosCostoNoModificado= mapaCentrosCostoNoModificado;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCostoNoModificado(String key, Object value) 
	{
		mapaCentrosCostoNoModificado.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCostoNoModificado(String key) 
	{
		return mapaCentrosCostoNoModificado.get(key);
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
		
		if(estado.equals("guardar"))
		{
			
			//VALIDAMOS LA VIA DE INGRESO
		    for(int k = 0 ; k < Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString()) ; k++)
		    {
		    	if(Integer.parseInt(this.getMapaCentrosCosto("codigoviaingreso_"+k).toString()) == -1)
		    	{
		    		errores.add("Via Ingreso requerido", new ActionMessage("errors.required"," La Vía de Ingreso"));
		    	}
		    }
			//VALIDAMOS QUE AL SELECCIONAR UNA VIA DE INGRESO EL CENTRO DE COSTO ES REQUERIDO
		    for(int k = 0 ; k < Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString()) ; k++)
		    {
		    	//Si alguna via de ingreso se ha seleccionado
		    	if(Integer.parseInt(this.getMapaCentrosCosto("codigoviaingreso_"+k).toString()) != -1)
		    	{
		    			if(Integer.parseInt(this.getMapaCentrosCosto("codigocentrocosto_"+k).toString()) == -1)
		    			{
		    				errores.add("Centro Costo requerido", new ActionMessage("errors.required"," El Centro de Costo"));
		    			}
		    		
		    	}
		    }
		    
			//VALIDAMOS EL TIPO PACIENTE REQUERIDO
		    for(int k = 0 ; k < Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString()) ; k++)
		    {
		    	if(this.getMapaCentrosCosto("tipopaciente_"+k).toString().equals("-1"))
		    	{
		    		errores.add("Tipo Paciente requerido", new ActionMessage("errors.required"," Tipo Paciente"));
		    	}
		    }
		    
		    
		    //Comparamos si existe la misma via de ingreso con el mismo tipo paciente con el mismo centro de costo mas de una vez
			String auxS1 = "";
			String auxS0 = "";
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			int numregistros = Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString());
			for(int i = 0 ; i < numregistros ; i++)
			{
				auxS0 = this.getMapaCentrosCosto("codigoviaingreso_"+i).toString().trim()+"-"+this.getMapaCentrosCosto("tipopaciente_"+i).toString().trim()+"-"+this.getMapaCentrosCosto("codigocentrocosto_"+i).toString().trim();
				for(int j = (numregistros-1) ; j > i ; j--)
				{
					
					auxS1 = this.getMapaCentrosCosto("codigoviaingreso_"+j).toString().trim()+"-"+this.getMapaCentrosCosto("tipopaciente_"+j).toString().trim()+"-"+this.getMapaCentrosCosto("codigocentrocosto_"+j).toString().trim();
					//Se realiza la comparacion compara
					if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("via ing - tipo paciente - centro costo iguales", new ActionMessage("error.centrosCosto.igualViaIngresoCentroCosto", descripcion));
				}
				descripcion = "";
				codigosComparados.put(i+"",auxS0.toUpperCase());
			}
		    
		}
		return errores;
	}

	/**
	 * @return the valorSelect
	 */
	public HashMap getValorSelect() {
		return valorSelect;
	}

	/**
	 * @param valorSelect the valorSelect to set
	 */
	public void setValorSelect(HashMap valorSelect) {
		this.valorSelect = valorSelect;
	}

	public ArrayList<DtoCentroCostoViaIngreso> getListaCentroCostoViaIngreso() {
		return listaCentroCostoViaIngreso;
	}

	public void setListaCentroCostoViaIngreso(
			ArrayList<DtoCentroCostoViaIngreso> listaCentroCostoViaIngreso) {
		this.listaCentroCostoViaIngreso = listaCentroCostoViaIngreso;
	}

	public boolean isMensajeExitoso() {
		return mensajeExitoso;
	}

	public void setMensajeExitoso(boolean mensajeExitoso) {
		this.mensajeExitoso = mensajeExitoso;
	}

	


}