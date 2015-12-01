/*
 * @(#)AntecedenteAlergiasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.actionform;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


import util.UtilidadCadena;



/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos especificos de los antecedentes de alergias. Y adicionalmente hace el
 * manejo de reset de la forma y de validación de errores de datos de entrada.
 * @version 1.0, Ago 1, 2003
 * @author <a href="mailto:sandra@PrincetonSA.com">Sandra Moya</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesAlergiasForm extends ValidatorForm 
{
	/**
	 * Arreglo con los codigos de todas las categorias de alergias que existen
	 */
	private int[] categorias;
	
	private boolean existeAntecedente = false; 
	private String itemActual = "";
	private String estado = "";
	/**
	 * Lista de todas las alergias predefinidas que han sido chequeadas en la pagina despues
	 * de haberse cargado el form, es decir que no han sido guardados en la bd.
	 * [cod_categoria] = [total = numero de alergias predefinidas para la categoria 'cod_categoria'] 
	 * cod_[cod_categoria]_[contador] = [codigo_alergia], donde 0<= 'contador' < total  
	 * Se guardan asi: [cod_categoria]_[codigo_alergia]= [codigo_alergia]-[nombre_alergia]-[cod_categoria] or null
	 */	
	private Map alePredef = new HashMap();
	
	/**
	 * Lista de todas las alergias predefinidas que se le han seleccionado al paciente previamente, 
	 * es decir que ya se guardaron en la base de datos.
	 * Se guardan asi: [cod_categoria]_[codigo_alergia]= [codigo_alergia]-[nombre_alergia]-[cod_categoria]
	 */
	private Map alePredefCarga = new HashMap();
	
	/**
	 * Lista con los comentarios ingresados para los tipos de alergias 'predefinidos' despues de haberse cargado el form,
	 * es decir que no han sido guardados a la bd.
	 * Se guardan asi: [cod_categoria]_[codigo_alergia]= [comentario] or null
	 */
	private Map alePredefObs = new HashMap();
	
	/**
	 * Lista con los comentarios ingresados previamente para los tipos de alergias 'predefinidas', 
	 * es decir que ya se guardaron en la bd.
	 * Se guardan asi: [cod_categoria]_[codigo_alergia]= [comentario] 
	 */	
	private Map alePredefObsCarga = new HashMap();
	
	/**
	 * Lista de todas las alergias 'adicionales' que han sido chequeadas en la pagina despues
	 * de haberse cargado el form, es decir que no han sido guardados en la bd. 
	 * Se guardan asi:
	 * [cod_categoria] = [numero de alergias adicionales para la categoria 'cod_categoria'] 
	 * [cod_categoria]_[contador] = [nombre alergia adicional] or null
	 */	
	private Map aleAdic = new HashMap();

	
	/**
	 * Lista de todas las alergias 'adicionales' que se le han seleccionado al paciente previamente, 
	 * es decir que ya se guardaron en la base de datos.
	 * Se guardan asi: 
	 * [cod_categoria] = [total = numero de alergias adicionales para la categoria con codigo: 'cod_categoria']
	 * cod_[cod_categoria]_[contador] = [codigo_alergia], donde 0<= 'contador' < total   
	 * [cod_categoria]_[codigo_alergia] = [nombre_alergia]
	 */	
	private Map aleAdicCarga = new HashMap();

	/**
	 * Lista con los comentarios ingresados para los tipos de alergias 'adicionales' despues de haberse cargado el form,
	 * es decir que no han sido guardados a la bd.
	 * Se guardan asi: 
	 * [cod_categoria]_[contador]= [comentario] or null. Se usa para las observaciones de alergias que no tienen comentarios previos guardados en la bd
	 * nueva_[cod_categoria]_[codigo_alergia] = [comentario]  or null. Se usa para las observaciones de alergias que ya tienen comentarios previos guardados en la bd.
	 */	
	private Map aleAdicObs = new HashMap();
	
	/**
	 * Lista con los comentarios ingresados previamente para los tipos de alergias 'adicionales', 
	 * es decir que ya se guardaron en la bd.
	 * Se guardan asi: [cod_categoria]_[codigo_alergia]= [comentario] 
	 */	
	private Map aleAdicObsCarga = new HashMap();
	
	/**
	 * Observaciones generales para antecedentes alergias
	 */
	private String observaciones;
	private String observacionesAnteriores;
	
	private String ocultarCabezotes;
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP,
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
		//Si hasta ahora va a cargar no se valida nada
		if(this.estado.equals("cargar")) return null;				
		if(this.estado.equals("continuar")) return null;
		if(this.estado.equals("resumenAtenciones")) return null;
		
		//En cualquier otro caso: 'grabar' o 'continuar' se debe validar			
		int cat, numAleAdic = 0, numAlePredef = 0, codigoAle;
		String nombre = "";
		for(int i=0; i<this.getCategorias().length; i++)
		{			
			cat = this.getCategorias()[i];		
			if (this.getAleAdic().get(""+cat) != null)	
				numAleAdic = Integer.parseInt((String)this.getAleAdic().get(""+cat));
			if (this.getAlePredef().get(""+cat) != null)				
				numAlePredef = Integer.parseInt((String)this.getAlePredef().get(""+cat));
							
			for (int j=0; j<numAleAdic; j++)
			{	
				if(this.getAleAdic().get(cat+"_"+j) != null)
				{						
					nombre = (String)this.getAleAdic().get(cat+"_"+j);
					//Si no tiene nombre
					if(nombre.trim().equals(""))
					{
						//Debe quitarla de las alergias adicionales (removerla del map), cubre el caso de deschequeada						
						this.getAleAdic().remove(cat+"_"+j);
						this.getAleAdicObs().remove(cat+"_"+j);
					}		
				}					
			}
			for (int j=0; j<numAlePredef; j++)
			{					
				codigoAle = Integer.parseInt((String)this.getAlePredef().get("cod_"+cat+"_"+j));
				//Si esta deschequeada la alergia debe removerse del map
				if (request.getParameter("alePredef(" + cat + "_" + codigoAle+")") == null) {						
					this.getAlePredef().remove(cat+"_"+codigoAle);				
				}
			}							
		}

		//-------------------------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------------------------
		//--------------------------SECCION (Alergias/Infecciones a Sustancias del medio ambiente)---------------------------------
		//-------------------------------------------------------------------------------------------------------------------------
		//--- Verificar si existen repetidos para  
		ActionErrors errors = super.validate(mapping, request);	
		
		for(int i=0; i<this.getCategorias().length; i++)
		{			
			cat = this.getCategorias()[i];
			numAleAdic = Integer.parseInt((String)this.getAleAdic().get(""+cat));
			numAlePredef = Integer.parseInt((String)this.getAlePredef().get(""+cat));
			Vector v = new Vector();


			//--Verificar Repetidos Entre Los Nuevos.  
			for (int j=0; j<numAleAdic; j++)
			{	
				nombre = this.getAleAdic().get(cat+"_"+j)+"";
				for (int l=0; l<numAleAdic; l++)
				{	
					String nombre1 = this.getAleAdic().get(cat+"_"+l)+"";
					if( UtilidadCadena.noEsVacio(nombre) &&	UtilidadCadena.noEsVacio(nombre1) &&
						nombre1.trim().toLowerCase().equals(nombre.trim().toLowerCase()) && !v.contains(nombre.trim().toLowerCase())  && j!=l )
					{					
						v.add(nombre.trim().toLowerCase());
						errors.add("parentescoRepetidos", new ActionMessage("error.antecedentes.alergia.duplicados", nombre));										
					}
				}
			}
			
			//-------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------
			//--Verificar Repetidos Entre Los Nuevos Con los Ya Registrados.
			v.clear();
			for (int j=0; j<numAlePredef; j++)
			{
				nombre = (String)this.getAlePredef().get("nom_"+cat+"_"+j);
				for (int l=0; l<numAleAdic; l++)
				{	
					String nombre1 = this.getAleAdic().get(cat+"_"+l)+"";
					if( UtilidadCadena.noEsVacio(nombre) &&	UtilidadCadena.noEsVacio(nombre1) &&
						nombre1.trim().toLowerCase().equals(nombre.trim().toLowerCase()) && !v.contains(nombre.trim().toLowerCase())  )
						{					
						  v.add(nombre.trim().toLowerCase());
						  errors.add("alergiaDuplicada", new ActionMessage("error.antecedentes.alergia.duplicadosRegistrados", nombre));										
						}
				}		
			}
			//-------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------
			
		}
		
		
		//-------------------------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------------------------
		if (!errors.isEmpty())
		{
			return errors;
		}
		//-------------------------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------------------------
		//-- Verificar que los antecedentes nuevos no esten repetidos. (Entre los nuevos)
		/*Vector v = new Vector();
		for (int i=1; i<=this.numAntAdicionales; i++)
		{
			String nombre = request.getParameter("antAdicional(nombre_"+i+")");
			for (int j=1; j<=this.numAntAdicionales; j++)
			{
				String nombre1 = request.getParameter("antAdicional(nombre_"+j+")");
				if( UtilidadCadena.noEsVacio(nombre) && UtilidadCadena.noEsVacio(nombre1) && 
					nombre1.trim().toLowerCase().equals(nombre.trim().toLowerCase()) && !v.contains(nombre)  && i!=j )
				{
				  v.add(nombre);
				  errors.add("parentescoRepetidos", new ActionMessage("error.antecedentes.familiares.nuevosDuplicados", nombre));										
				}
			}
		}*/
		//-------------------------------------------------------------------------------------------------------------------------
		//-------------------------------------------------------------------------------------------------------------------------
		
		
		return null;		
	}

	public void reset (){
		this.existeAntecedente = false;
		this.itemActual = "";
		//limpiando todos los campos
		this.alePredef.clear();
		this.alePredefCarga.clear();
		this.alePredefObs.clear();
		this.alePredefObsCarga.clear();
		this.aleAdic.clear();
		this.aleAdicCarga.clear();
		this.aleAdicObs.clear();
		this.aleAdicObsCarga.clear();
		this.observaciones = "";
		this.observacionesAnteriores ="";
		}


	/**
	 * Retorna la descripción de la conducta a seguir
	 * @return String
	 */

	/**
	 * Asigna la descripción de la conducta a seguir
	 * @param descConductaASeguir The descConductaSeguir to set
	 */

	/**
	 * @return
	 */
	public Map getAleAdic() {
		return aleAdic;
	}

	/**
	 * @return
	 */
	public Map getAleAdicCarga() {
		return aleAdicCarga;
	}

	/**
	 * @return
	 */
	public Map getAleAdicObs() {
		return aleAdicObs;
	}

	/**
	 * @return
	 */
	public Map getAleAdicObsCarga() {
		return aleAdicObsCarga;
	}

	/**
	 * @return
	 */
	public Map getAlePredef() {
		return alePredef;
	}

	/**
	 * @return
	 */
	public Map getAlePredefCarga() {
		return alePredefCarga;
	}

	/**
	 * @return
	 */
	public Map getAlePredefObs() {
		return alePredefObs;
	}

	/**
	 * @return
	 */
	public Map getAlePredefObsCarga() {
		return alePredefObsCarga;
	}

	/**
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @return
	 */
	public String getObservacionesAnteriores() {
		return observacionesAnteriores;
	}

	/**
	 * @param map
	 */
	public void setAleAdic(Map map) {
		aleAdic = map;
	}

	/**
	 * @param map
	 */
	public void setAleAdicCarga(Map map) {
		aleAdicCarga = map;
	}

	/**
	 * @param map
	 */
	public void setAleAdicObs(Map map) {
		aleAdicObs = map;
	}

	/**
	 * @param map
	 */
	public void setAleAdicObsCarga(Map map) {
		aleAdicObsCarga = map;
	}

	/**
	 * @param map
	 */
	public void setAlePredef(Map map) {
		alePredef = map;
	}

	/**
	 * @param map
	 */
	public void setAlePredefCarga(Map map) {
		alePredefCarga = map;
	}

	/**
	 * @param map
	 */
	public void setAlePredefObs(Map map) {
		alePredefObs = map;
	}

	/**
	 * @param map
	 */
	public void setAlePredefObsCarga(Map map) {
		alePredefObsCarga = map;
	}

	/**
	 * @param string
	 */
	public void setObservaciones(String string) {
		observaciones = string;
	}

	/**
	 * @param string
	 */
	public void setObservacionesAnteriores(String string) {
		observacionesAnteriores = string;
	}

	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}


	/**
	 * @return
	 */
	public String getItemActual() {
		return itemActual;
	}

	/**
	 * @param string
	 */
	public void setItemActual(String string) {
		itemActual = string;
	}

	/**
	 * @return
	 */
	public boolean isExisteAntecedente() {
		return existeAntecedente;
	}

	/**
	 * @param b
	 */
	public void setExisteAntecedente(boolean b) {
		existeAntecedente = b;
	}

	/**
	 * @return
	 */
	public int[] getCategorias() {
		return categorias;
	}

	/**
	 * @param is
	 */
	public void setCategorias(int[] is) {
		categorias = is;
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