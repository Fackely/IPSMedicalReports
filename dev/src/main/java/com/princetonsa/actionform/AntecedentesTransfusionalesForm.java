package com.princetonsa.actionform;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos de los antecedentes transfusionales. Y adicionalmente hace el
 * manejo de reset de la forma y de validación de errores de datos de entrada.
 * @version 1.0, Septiembre 2, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesTransfusionalesForm extends ValidatorForm
{
	/**
	 * Estado actual dentro del flujo de la funcionalidad
	 */	
	private String estado;
	
	/**
	 * Map para manejar las transfusiones
	 */
	private final Map transfusiones = new HashMap();	
	
	/**
	 * Número de transfusiones almacenadas en la base de datos
	 */
	private int numTransfusionesBD;
	
	/**
	 * Número total de transfusiones
	 */
	private int numTransfusiones;
	
	/**
	 * Observaciones generales previamente ingresadas
	 */
	private String observacionesAnteriores;
	
	/**
	 * Observaciones generales nuevas
	 */
	private String observacionesNuevas;
	
	private String ocultarCabezotes;
	
	/**
	* Valida  las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que está siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if( estado.equals("adicionarTransfusion") || estado.equals("finalizar") )
		{
			int tam = this.numTransfusiones;
			
			if( estado.equals("adicionarTransfusion") )
				tam += 1;
			
			for(int i=1; i<=tam; i++)
			{
				String nomT = "componente_"+i;
				if( this.getTransfusion(nomT) == null || this.getTransfusion(nomT).equals("") )
				{
					if( estado.equals("adicionarTransfusion") )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "antecedente transfusional"));
					else
					if( noVacio((String)this.getTransfusion("fecha_"+i)) || noVacio((String)this.getTransfusion("causa_"+i)) || noVacio((String)this.getTransfusion("lugar_"+i)) || noVacio((String)this.getTransfusion("edad_"+i)) || noVacio((String)this.getTransfusion("donante_"+i)) || noVacio((String)this.getTransfusion("observaciones_"+i)) )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "antecedente transfusional"));
				}
			}
		}
		return errores;
	}
	
	public void reset()
	{
		this.transfusiones.clear();
		this.numTransfusionesBD = 0;
		this.numTransfusiones = 0;
		this.observacionesAnteriores = "";
		this.observacionesNuevas = "";
	}
	
	/**
	 * Retorna el estado actual dentro del flujo de la funcionalidad
	 * @return 	String, estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado actual dentro del flujo de la funcionalidad
	 * @param 	String, estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * Retorna la transfusion dada su llave
	 * @param String, llave bajo la cual se almaceno la información
	 * @return Object, objeto almacenado en el map
	 */
	public Object getTransfusion(String key)
	{
		return this.transfusiones.get(key);
	}
	
	/**
	 * Almacena un valor de  transfusion en el map, bajo la llave dada.
	 * @param Object, value
	 * @param String, key
	 */
	public void setTransfusion(String key, Object value)
	{
		this.transfusiones.put(key, value);		
	}	

	/**
	 * Retorna el número de transfusiones almacenadas en la base de datos
	 * @return 	int, número de transfusiones en la bd
	 */
	public int getNumTransfusionesBD()
	{
		return numTransfusionesBD;
	}

	/**
	 * Asigna el número de transfusiones almacenadas en la base de datos
	 * @param int, número de transfusiones en la bd
	 */
	public void setNumTransfusionesBD(int numMedicamentosBD)
	{
		this.numTransfusionesBD = numMedicamentosBD;
	}

	/**
	 * Retorna el número total de transfusiones
	 * @return 	int, número de transfusiones
	 */
	public int getNumTransfusiones()
	{
		return numTransfusiones;
	}

	/**
	 * Asigna el número total de transfusiones
	 * @param int, número de transfusiones
	 */
	public void setNumTransfusiones(int numMedicamentos)
	{
		this.numTransfusiones = numMedicamentos;
	}

	/**
	 * Retorna las observaciones generales previamente ingresadas
	 * @return	 String, observaciones
	 */
	public String getObservacionesAnteriores()
	{
		return observacionesAnteriores;
	}

	/**
	 * Asigna las observaciones generales previamente ingresadas
	 * @param 	String, observaciones anteriores
	 */
	public void setObservacionesAnteriores(String observacionesAnteriores)
	{
		this.observacionesAnteriores = observacionesAnteriores;
	}

	/**
	 * Retorna las observaciones generales nuevas
	 * @return 	String, observaciones nuevas
	 */
	public String getObservacionesNuevas()
	{
		return observacionesNuevas;
	}

	/**
	 * Asgina las observaciones generales nuevas
	 * @param 	String, observaciones nuevas
	 */
	public void setObservacionesNuevas(String observacionesNuevas)
	{
		this.observacionesNuevas = observacionesNuevas;
	}

	private boolean noVacio(String valor)
	{
		if( valor != null && !valor.equals("") )
			return true;
		else
			return false; 
	}
	
	/**
	 * @return Returns the ocultarCabezotes.
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}
	/**
	 * @param ocultarCabezotes The ocultarCabezotes to set.
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}
}
