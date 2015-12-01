/*
 * Mayo 04, 2006
 */
package com.princetonsa.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author sebacho
 *
 * Forma para el workflow de la funcionalidad Unidades Funcionales
 */
public class UnidadFuncionalForm extends ValidatorForm 
{

	/**
	 * Estado de la aplicacion
	 */
	private String estado;
	
	/**
	 * Mapa donde se almacena el listado de unidades funcionales
	 */
	private HashMap unidades = new HashMap();
	
	/**
	 * Número de registros en el mapa de unidades
	 */
	private int numUnidades;
	
	/**
	 * Variable que identifica la posición de un registro del listado
	 */
	private int posRegistro;
	
	/**
	 * Campos destinados para la ordenacion
	 * del listado
	 */
	private String indice;
	private String ultimoIndice;
	
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
			//Validación de campos requeridos
			for(int i=0;i<this.numUnidades;i++)
			{
				//validacion código
				if(this.getUnidades("codigo_"+i).toString().trim().equals(""))
					errores.add("codigo requerido",new ActionMessage("errors.required","El código del registro Nº "+(i+1)));
				
				//validación descripcion
				if(this.getUnidades("descripcion_"+i).toString().trim().equals(""))
					errores.add("descripcion requerida",new ActionMessage("errors.required","La descripción del registro Nº "+(i+1)));
				
			}
			
			if(errores.isEmpty())
			{
				// sección para validar que no hayan códigos repetidos
				String auxS1 = "";
				String auxS0 = "";
				HashMap codigosComparados = new HashMap();
				String descripcion = "";
				for(int i=0;i<this.numUnidades;i++)
				{
					auxS0=this.getUnidades("codigo_"+i).toString().trim();
					
					for(int j=(this.getNumUnidades()-1);j>i;j--)
					{
						
						auxS1=this.getUnidades("codigo_"+j).toString().trim();
						//se compara
						if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")
							&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0.toUpperCase()))
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
					codigosComparados.put(i+"",auxS0.toUpperCase());
					
				}
			}
		}
		
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
	    this.estado="";
	    this.numUnidades = 0;
	    this.unidades = new HashMap();
	    this.posRegistro = 0;
	    this.indice = "";
	    this.ultimoIndice = "";
	}

	
	//*******************GETTERS & SETTERS***************************************
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
	 * @return Returns the numUnidades.
	 */
	public int getNumUnidades() {
		return numUnidades;
	}

	/**
	 * @param numUnidades The numUnidades to set.
	 */
	public void setNumUnidades(int numUnidades) {
		this.numUnidades = numUnidades;
	}

	/**
	 * @return Returns the unidades.
	 */
	public HashMap getUnidades() {
		return unidades;
	}

	/**
	 * @param unidades The unidades to set.
	 */
	public void setUnidades(HashMap unidades) {
		this.unidades = unidades;
	}
	
	/**
	 * @return Retorna elemento del mapa unidades.
	 */
	public Object getUnidades(String key) {
		return unidades.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa unidades.
	 */
	public void setUnidades(String key,Object obj) {
		this.unidades.put(key,obj);
	}

	/**
	 * @return Returns the posRegistro.
	 */
	public int getPosRegistro() {
		return posRegistro;
	}

	/**
	 * @param posRegistro The posRegistro to set.
	 */
	public void setPosRegistro(int posRegistro) {
		this.posRegistro = posRegistro;
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

	
	
}
