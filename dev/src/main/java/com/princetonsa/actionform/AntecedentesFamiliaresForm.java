package com.princetonsa.actionform;

import  java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import  org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm; 

import util.UtilidadCadena;

/**
 * El Action Form de Antecedentes Familiares, tienen la función de bean dentro
 * de la forma  que contienen todos los datos de los antecedentes familiares.
 * Adicionalmente, hace reset sobre la forma y valida los datos de entrada.
 */
public class AntecedentesFamiliaresForm extends ValidatorForm
{
	/**
 	* Estado que toma de acuerdo al flujo de la funcionalidad
 	*/

	private String estado="";

	/**
	 * Lista de códigos y Nombres de las enfermedads familiares guardadas en la
	 * BD
	 */
	private  Map  antecedentesFamiliares=new HashMap();

	/**
	 * Numero de antecedentes familiares devueltos por la BD
	 */

	private int numAntecedentesFamiliares=0;

	private Map antecedentesFamiliaresOtros = new HashMap();

	private Map antecedentesAdicionales = new HashMap();

	private int numAntAdicionales=0;

	/**
	 * Cantidad de otros antecedentes familiares
	 */
 	private int numAntFamiliaresOtros=0;

	private String observaciones="";

	private String observacionesAnteriores="";

	private boolean existeAntecedente=false;
	
	private String ocultarCabezotes;
	
	/**
	 * Valida las propiedades que han sido establecidas para este request http y
	 * retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	 * validacion encontrados. Si no encontró errores retorna null.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		for( int i=1; i <= this.numAntAdicionales; i++ )
		{
			String nomChequeado = "chequeado_"+i;
			String check = (String)this.getAntAdicional(nomChequeado);
			String nomCheck = "checkbox_"+i;
			
			if( check != null && check.equals("false") )
				this.setAntAdicional(nomCheck, null);
		}
		
		ActionErrors errors = new ActionErrors();	
				
		
		if( errors.isEmpty() && estado.equals("ingresar"))
		{
			for (int i = 1; i <= this.numAntecedentesFamiliares; i++)
			{
				String  parentesco=request.getParameter("antecedenteFamiliar(parentesco_"+i+")");
				String nombre=request.getParameter("antecedenteFamiliar(nombre_"+i+")");
		
				
				if(request.getParameter("antecedenteFamiliar(checkbox_"+i+")")!=null)
				{
					if (request.getParameter("antecedenteFamiliar(parentesco_"+i+")")!=null && parentesco.equals("")) 
						errors.add("parentesco", new ActionMessage("error.antecedentes.familiares.parentesco", nombre));
				}
			}
			for (int i = 1; i <= this.numAntAdicionales; i++)
			{
				String  parentesco=request.getParameter("antAdicional(parentesco_"+i+")");
				String nombre=request.getParameter("antAdicional(nombre_"+i+")");

				if(request.getParameter("antAdicional(checkbox_"+i+")")!=null)
				{
					String nombreAntAdic = (String)getAntAdicional("nombre_"+i);
					
					if( nombreAntAdic == null || nombreAntAdic.equals("") )
						errors.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "antecedente familiar"));
					else
					if (request.getParameter("antAdicional(parentesco_"+i+")")!=null && parentesco.equals("")) 
						errors.add("parentesco", new ActionMessage("error.antecedentes.familiares.parentesco", nombre));										
				}
			}

		}
		
		if( estado.equals("ingresar") )
		{
			//-------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------
			//-- Verificar que los antecedentes nuevos no esten repetidos. (Compararlos con los Viejos).
			Vector v = new Vector();
			for (int i=1; i<=this.numAntecedentesFamiliares; i++)
			{
				String nombre=request.getParameter("antecedenteFamiliar(nombre_I_"+i+")");
				for (int j=1; j<=this.numAntAdicionales; j++)
				{		
					String nombre1 = request.getParameter("antAdicional(nombre_"+j+")");

					if( UtilidadCadena.noEsVacio(nombre) && UtilidadCadena.noEsVacio(nombre1) &&
					 	nombre1.trim().toLowerCase().equals(nombre.trim().toLowerCase()) && !v.contains(nombre.trim().toLowerCase()) )
					{

						v.add(nombre.trim().toLowerCase());
					  errors.add("parentescoRepetidos", new ActionMessage("error.antecedentes.duplicados", nombre));										
					}
				}
			}			
			
			/*v.clear();
			//--Verificar los nuevos con los Nuevos. que se van a registrar juntos. 
			for (int i=1; i<=this.numAntAdicionales; i++) 
			{
				String nombre = request.getParameter("antAdicional(nombre_"+i+")");
				for (int j=1; j<=this.numAntAdicionales; j++)
				{
					String nombre1 = request.getParameter("antAdicional(nombre_"+j+")");
					if( UtilidadCadena.noEsVacio(nombre) && UtilidadCadena.noEsVacio(nombre1) && 
						nombre1.trim().toLowerCase().equals(nombre.trim().toLowerCase()) && !v.contains(nombre.trim().toLowerCase())  && (i!=j)
					  )
					{
					  v.add(nombre.trim().toLowerCase());
					  errors.add("parentescoRepetidos", new ActionMessage("error.antecedentes.nuevosDuplicados", nombre));										
					}
				}
			}*/
			
			v.clear();
			//---------------------------------------------------------------
			//--Estamos verificando los nuevos con los nuevos YA REGISTRADOS.
			for (int i=1; i<=this.numAntAdicionales; i++)
			{		
				String nombre = request.getParameter("antAdicional(nombre_"+i+")");
				
				for (int j=1; j<=this.numAntAdicionales; j++)
				{
					String nombre1 = request.getParameter("antAdicional(nombre_"+j+")");

					  
					if( UtilidadCadena.noEsVacio(nombre) && UtilidadCadena.noEsVacio(nombre1) && (i!=j) && 
						nombre1.trim().toLowerCase().equals(nombre.trim().toLowerCase()) && !v.contains(nombre.trim().toLowerCase()) 
					  )
					{
					
					  v.add(nombre.trim().toLowerCase());
					  errors.add("parentescoRepetidos", new ActionMessage("error.antecedentes.nuevosDuplicados", nombre));										
					}
				}
			}			
			
		}
		return errors;
	}

	public void reset()
		{
			//this.existeAntecedente = false;
			//this.estado = "";
			this.antecedentesFamiliares.clear();
			this.antecedentesFamiliaresOtros.clear();
			this.antecedentesAdicionales.clear();

		//	this.numAntecedentesFamiliares=0;
			this.observaciones="";
			this.observacionesAnteriores="";
		}
		
	/**
	 * Retorna el antecedete familiar dada su llave
	 * @param String llave bajo la cual se almacenó la información
	 * @return Object objeto almacenado en el Map
	 * 
	 *
	 */
	public Object getAntecedenteFamiliar(String key)
	{
		return this.antecedentesFamiliares.get(key);
	}
	
	/**
	 * Retorna el antecedete familiar dada su llave
	 * @param String llave bajo la cual se almacenó la información
	 * @return Object objeto almacenado en el Map
	 * 
	 *
	 */
	public Object getAntecedenteFamiliarOtro(String key)
	{
		return this.antecedentesFamiliaresOtros.get(key);
	}

	public Object getAntAdicional(String key)
	{
		return this.antecedentesAdicionales.get(key);
	}

	/**
	 * Returns the estado.
	 * @return String
	 */
	public String getEstado() 
	{
		return estado;
	}

	/**
	 * Returns the numAntecedentesFamiliares.
	 * @return int
	 */
	public int getNumAntecedentesFamiliares() 
	{
		return numAntecedentesFamiliares;
	}

	/**
	 * Sets the estado.
	 * @param estado The estado to set
	 */
	public void setEstado(String estado) 
	{
		this.estado = estado;
	}

	/**
	 * Sets the numAntecedentesFamiliares.
	 * @param numAntecedentesFamiliares The numAntecedentesFamiliares to set
	 */
	public void setNumAntecedentesFamiliares(int numAntecedentesFamiliares)
	{
		this.numAntecedentesFamiliares = numAntecedentesFamiliares;
	}

	/**
	 * Almacena el valor de un antecedente familiar bajo la llave dada.
	 * @param String key 
	 * @param Object value
	 */
	
	public void setAntecedenteFamiliar(String key,Object value  )
	{
		this.antecedentesFamiliares.put(key,value);
	}
	
	
	public void setAntAdicional(String key,Object value  )
	{
		this.antecedentesAdicionales.put(key,value);
	}
	
	
	public void setAntecedenteFamiliarOtro(String key,Object value  )
	{
		this.antecedentesFamiliaresOtros.put(key,value);
	}
 
	/**
	 * Returns the numAntFamiliaresOtros.
	 * @return int
	 */
	public int getNumAntFamiliaresOtros() 
	{
		return numAntFamiliaresOtros;
	}
	
	/**
	 * Sets the numAntFamiliaresOtros.
	 * @param numAntFamiliaresOtros The numAntFamiliaresOtros to set
	 */
	public void setNumAntFamiliaresOtros(int numAntFamiliaresOtros) 
	{
		this.numAntFamiliaresOtros = numAntFamiliaresOtros;
	}

	/**
	 * Returns the observaciones.
	 * @return String
	 */
	public String getObservaciones() 
	{
		return observaciones;
	}
	
	/**
	 * Returns the observacionesAnteriores.
	 * @return String
	 */
	public String getObservacionesAnteriores() 
	{
		return observacionesAnteriores;
	}
	
	/**
	 * Sets the observaciones.
	 * @param observaciones The observaciones to set
	 */
	public void setObservaciones(String observaciones) 
	{
		this.observaciones = observaciones;
	}
	
	/**
	 * Sets the observacionesAnteriores.
	 * @param observacionesAnteriores The observacionesAnteriores to set
	 */
	public void setObservacionesAnteriores(String observacionesAnteriores) 
	{
		this.observacionesAnteriores = observacionesAnteriores;
	}
	
	/**
	 * Returns the numAntAdicionales.
	 * @return int
	 */
	public int getNumAntAdicionales() 
	{
		return numAntAdicionales;
	}
	
	/**
	 * Sets the numAntAdicionales.
	 * @param numAntAdicionales The numAntAdicionales to set
	 */
	public void setNumAntAdicionales(int numAntAdicionales) 
	{
		this.numAntAdicionales = numAntAdicionales;
	}
	
	/**
	 * Returns the existeAntecedente.
	 * @return boolean
	 */
	public boolean isExisteAntecedente() 
	{
		return existeAntecedente;
	}
	
	/**
	 * Sets the existeAntecedente.
	 * @param existeAntecedente The existeAntecedente to set
	 */
	public void setExisteAntecedente(boolean existeAntecedente) 
	{
		this.existeAntecedente = existeAntecedente;
	}
	
	/**
	 * Returns the antecedentesFamiliares.
	 * @return Map
	 */
	public Map getAntecedentesFamiliares() 
	{
		return antecedentesFamiliares;
	}
	
	/**
	 * Sets the antecedentesFamiliares.
	 * @param antecedentesFamiliares The antecedentesFamiliares to set
	 */
	public void setAntecedentesFamiliares(Map antecedentesFamiliares) 
	{
		this.antecedentesFamiliares = antecedentesFamiliares;
	}
	
	/**
	 * Returns the antecedentesAdicionales.
	 * @return Map
	 */
	public Map getAntecedentesAdicionales() 
	{
		return antecedentesAdicionales;
	}
	
	/**
	 * Returns the antecedentesFamiliaresOtros.
	 * @return Map
	 */
	public Map getAntecedentesFamiliaresOtros() 
	{
		return antecedentesFamiliaresOtros;
	}
	
	/**
	 * Sets the antecedentesAdicionales.
	 * @param antecedentesAdicionales The antecedentesAdicionales to set
	 */
	public void setAntecedentesAdicionales(Map antecedentesAdicionales) 
	{
		this.antecedentesAdicionales = antecedentesAdicionales;
	}
	
	/**
	 * Sets the antecedentesFamiliaresOtros.
	 * @param antecedentesFamiliaresOtros The antecedentesFamiliaresOtros to set
	 */
	public void setAntecedentesFamiliaresOtros(Map antecedentesFamiliaresOtros) 
	{
		this.antecedentesFamiliaresOtros = antecedentesFamiliaresOtros;
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