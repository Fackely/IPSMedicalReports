/*
 * Mayo 31, 2006
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadCadena;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Destinos Triage
 */
public class DestinoTriageForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Mapa donde se almacenan los destinos Triage
	 */
	private HashMap destinosMap = new HashMap();
	
	/**
	 * Número de registros en destinosMap
	 */
	private int numRegistros;
	
	/**
	 * Variable que apunta a la posicion de un registro
	 * dentro de destinosMap
	 */
	private int posicion;
	
	//****ATRIBUTOS DE ORDENACIÓN************
	private String indice;
	private String ultimoIndice;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.destinosMap = new HashMap();
		this.numRegistros = 0;
		this.posicion = -1;
		
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
			//*******VALIDAR REQUERIDOS*****************
			String aux1 = "";
			String aux2 = "";
			for(int i=0;i<this.numRegistros;i++)
			{
				//se valida el código
				aux1 = this.getDestinosMap("codigo_"+i).toString();
				if(aux1.equals(""))
					errores.add("código requerido",new ActionMessage("errors.required","El código en el registro Nº "+(i+1)));
				else if(UtilidadCadena.tieneCaracteresEspecialesNumericoRips(aux1))
					errores.add("código requerido",new ActionMessage("errors.caracteresInvalidos","El código en el registro Nº "+(i+1)));
					
				
				//se valida destino
				aux2 = this.getDestinosMap("nombre_"+i).toString();
				if(aux2.equals(""))
					errores.add("nombre requerido",new ActionMessage("errors.required","El destino en el registro Nº "+(i+1)));
				else if(UtilidadCadena.tieneCaracteresEspecialesGeneral(aux2))
					errores.add("código requerido",new ActionMessage("errors.caracteresInvalidos","El destino en el registro Nº "+(i+1)));
			}
			
			//*****VALIDAR REPETIDOS*******************
			
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			for(int i=0;i<this.numRegistros;i++)
			{
				aux1=this.getDestinosMap("codigo_"+i).toString();
				
				for(int j=this.numRegistros-1;j>i;j--)
				{
					
					aux2=this.getDestinosMap("codigo_"+j).toString();
					//se compara
					if(aux1.compareToIgnoreCase(aux2)==0&&!aux1.equals("")
						&&!aux2.equals("")&&!codigosComparados.containsValue(aux1))
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
				codigosComparados.put(i+"",aux1);
				
			}
			
			
			if(!errores.isEmpty())
				this.estado = "empezar";
		}
		
		return errores;
	}

	/**
	 * @return Returns the destinosMap.
	 */
	public HashMap getDestinosMap() {
		return destinosMap;
	}

	/**
	 * @param destinosMap The destinosMap to set.
	 */
	public void setDestinosMap(HashMap destinosMap) {
		this.destinosMap = destinosMap;
	}
	
	/**
	 * @return Retorna un elemento del mapa destinosMap.
	 */
	public Object getDestinosMap(String key) {
		return destinosMap.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa destinosMap.
	 */
	public void setDestinosMap(String key,Object obj) {
		this.destinosMap.put(key,obj);
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
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return Returns the posicion.
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion The posicion to set.
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
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
	
	
	
	
}
