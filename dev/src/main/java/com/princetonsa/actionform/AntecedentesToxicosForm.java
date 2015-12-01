/*
 * @(#)AntecedenteToxicosForm.java
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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;



/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos especificos de los antecedentes toxicos. Y adicionalmente hace el
 * manejo de reset de la forma y de validación de errores de datos de entrada.
 * 
 * @version 2.0, Noviembre 26, 2003
 * @author <a href="mailto:liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
@SuppressWarnings("rawtypes")
public class AntecedentesToxicosForm extends ValidatorForm 
{	
	/** *  */
	private static final long serialVersionUID = 1L;

	/**
	 * Estado actual dentro del flujo de antecedentes tóxicos
	 */
	private String estado;
	
	/**
	 * Lista de todas los toxicos predefinidas que tienen un dato para ser grabado en la bd
	 * Se guardan asi: 
	 * codigo_[i] = código del antecedente tóxico predefinido en la base de
	 * datos 
	 * actual_[codigo] = true o false o null dependiendo si el antecedente
	 * tóxico es actual, no es actual, o no se tiene.
	 * tiempo_[codigo] = si es actual, indica el tiempo durante el cual se ha
	 * mantenido el antecedente, si no es actual, indica el cuanto tiempo hace
	 * que se dejó.
	 * cantidad_[codigo] = cantidad de consumo del antecedente
	 * frecuencia_[codigo] = frecuencia de consumo del antecedente
	 * observaciones_[codigo] = observaciones actuales del antecedente
	 */	
	private Map antecedentesToxicos = new HashMap();
	
	/**
	 * Número de antecedentes tóxicos
	 */
	private int numAntecedentesToxicos = 0;
	
	/**
	 * Número de antecedentes predefinidos
	 */
	private int numPredefinidos = 0;
	
	/**
	 * Lista de todas los toxicos predefinidas que tienen un dato para ser grabado en la bd
	 * Se guardan de la misma forma que los predefinidos
	 */	
	private Map antecedentesToxicosOtros = new HashMap();
	
	/**
	 * Número de antecedentes tóxicos otros
	 */
	private int numAntecedentesToxicosOtros = 0;	
	
	/**
	 * Número de antecedentes tóxicos otros nuevos
	 */
	private int numOtrosNuevos = 0;
	
	/**
	 * Observaciones generales para antecedentes toxicos
	 */
	private String observaciones;
	
	/**
	 * Observaciones generales para antecedentes tóxicos previamente ingresadas.
	 */
	private String observacionesAnteriores;
	
	private String ocultarCabezotes;
	
	private String operacionExistosa = ConstantesBD.acronimoNo;
	
	private HashMap encabezadoImpresion;
	
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que estó siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	@SuppressWarnings("unchecked")
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors(); 
		
		//--validar que no se inserten repetidos. Entre los nuevos. 
		if( estado.equals("finalizar") )
		{
			Vector v = new Vector();
			
			//-- Barriendo los ya Registrados.
			String fin = this.getAntecedenteToxico("limSuperior")+"";
	
			//-Establecer los limites de los nuevos.
			int nTO = this.numAntecedentesToxicosOtros+1;
			int nTON = this.numOtrosNuevos+1;
			int nT = this.numAntecedentesToxicos+1;
	
			//-- Comparacion con las parametrizadas  
			if ( UtilidadCadena.noEsVacio(fin) )
			{
				int fini = Integer.parseInt(fin);
				for (int i=nT; i<(nT+fini); i++)  
				{
					String nom = "nomTipoAnt_"+i;
					nom = this.getAntecedenteToxico(nom)+"";
										  
					//-- Los Antecedentes Nuevos A Ingresar.
					for(int j=nTON; j<nTO; j++)
					{
					  String nom2 = this.getAntecedenteToxicoOtro("nombre_"+j)+"";
					
						if ( 
							 UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && 
						  	 nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) 		
						   )
							{
							  v.add(nom.trim().toLowerCase());
							  errores.add("duplicado",  new ActionMessage("error.antecedentes.duplicados",nom));
							} 
					}					
				}
				
			}
			
			
			v.clear();
			//-- comparar con las Otras insertadas desde la aplicacion.
			for(int i=1; i<nTON; i++)
			{
				String nom = this.getAntecedenteToxicoOtro("nombre_"+i)+"";
				
				
				//-- Los Antecedentes Nuevos A Ingresar.
				for(int j=nTON; j<nTO; j++)
				{
				  String nom2 = this.getAntecedenteToxicoOtro("nombre_"+j)+"";

				    if ( 
						 UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && 
					  	 nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) 		
					    )
						{
						  v.add(nom.trim().toLowerCase());
						  errores.add("duplicado",  new ActionMessage("error.antecedentes.duplicados",nom));
						} 
				}					
			}	
			
			v.clear();
			//-- Verificar que entre los nuevos no hallan Duplicados.   
			//-- (Los Antecedentes Nuevos A Ingresar).
			for(int i=nTON; i<nTO; i++)
			{
				String nom = this.getAntecedenteToxicoOtro("nombre_"+i)+"";
				
				for(int j=nTON; j<nTO; j++)
				{
				  String nom2 = this.getAntecedenteToxicoOtro("nombre_"+j)+"";
	
				    if ( 
						 UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && (j!=i) && 
					  	 nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) 		
					    )
						{
						  v.add(nom.trim().toLowerCase());
						  errores.add("duplicado",  new ActionMessage("error.antecedentes.nuevosDuplicados",nom));
						} 
				}
			}	
			
			
						
		}	

		if ( errores.isEmpty() ) { return null; }
		else 					 { return errores; }
		
	}

	/**
	 * Borra la información del form
	 */
	public void reset ()
	{
		this.numPredefinidos = 0;
		this.numOtrosNuevos = 0;
		this.antecedentesToxicos.clear();
		this.numAntecedentesToxicos = 0;
		this.antecedentesToxicosOtros.clear();
		this.numAntecedentesToxicosOtros = 0;
		this.observaciones = "";
		this.observacionesAnteriores ="";
		this.operacionExistosa = ConstantesBD.acronimoNo;
		this.encabezadoImpresion = new HashMap(); 
	}
	
	/**
	 * Retorna el antecedente tóxico dada su llave
	 * @param String, llave bajo la cual se almaceno la información
	 * @return Object, objeto almacenado en el map
	 */
	public Object getAntecedenteToxico(String key)
	{
		return this.antecedentesToxicos.get(key);
	}
	
	/**
	 * Almacena un valor de antecedente tóxico en el map, bajo la llave dada.
	 * @param Object, value
	 * @param String, key
	 */
	@SuppressWarnings("unchecked")
	public void setAntecedenteToxico(String key, Object value)
	{
		this.antecedentesToxicos.put(key, value);		
	}	

	/**
	 * Retorna el antecedente tóxico otro dada su llave
	 * @param String, llave bajo la cual se almaceno la información
	 * @return Object, objeto almacenado en el map
	 */
	public Object getAntecedenteToxicoOtro(String key)
	{
		return this.antecedentesToxicosOtros.get(key);
	}
	
	/**
	 * Almacena un valor de antecedente tóxico otro en el map, bajo la llave
	 * dada.
	 * @param Object, value
	 * @param String, key
	 */
	@SuppressWarnings("unchecked")
	public void setAntecedenteToxicoOtro(String key, Object value)
	{
		this.antecedentesToxicosOtros.put(key, value);		
	}	

	/**
	 * Retorna el número de antecedentes tóxicos
	 * @return int, numAntecedentesTóxicos
	 */
	public int getNumAntecedentesToxicos()
	{
		return numAntecedentesToxicos;
	}

	/**
	 * Asigna el número de antecedentes tóxicos
	 * @param int, numAntecedentesToxicos 
	 */
	public void setNumAntecedentesToxicos(int numAntecedentesToxicos)
	{
		this.numAntecedentesToxicos = numAntecedentesToxicos;
	}

	/**
	 * Retorna el nómero de antecedentes tóxicos otros
	 * @return int, numAntecedentesTóxicosOtros
	 */
	public int getNumAntecedentesToxicosOtros()
	{
		return numAntecedentesToxicosOtros;
	}

	/**
	 * Asigna el número de antecedentes tóxicos otros
	 * @param int, numAntecedentesToxicosOTros
	 */
	public void setNumAntecedentesToxicosOtros(int numAntecedentesToxicosOtros)
	{
		this.numAntecedentesToxicosOtros = numAntecedentesToxicosOtros;
	}

	/**
	 * Retorna el estado actual dentro del flujo de antecedentes tóxicos
	 * @return String
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado actual dentro del flujo de antecedentes tóxicos
	 * @param estado 
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Retorna las observaciones generales para antecedentes toxicos
	 * @return String, observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones generales para antecedentes toxicos
	 * @param String, observaciones 
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * Retorna las observaciones generales para antecedentes tóxicos previamente
	 * ingresadas.
	 * @return String, observacionesAnteriores
	 */
	public String getObservacionesAnteriores()
	{
		return observacionesAnteriores;
	}

	/**
	 * Asigna las observaciones generales para antecedentes tóxicos previamente
	 * ingresadas.
	 * @param String, observacionesAnteriores 
	 */
	public void setObservacionesAnteriores(String observacionesAnteriores)
	{
		this.observacionesAnteriores = observacionesAnteriores;
	}

	/**
	 * Retorna la lista de todas los toxicos predefinidas que tienen un dato
	 * para ser grabado en la bd
	 * @return Map
	 */
	public Map getAntecedentesToxicosOtros()
	{
		return antecedentesToxicosOtros;
	}

	/**
	 * Retorna el número de antecedentes predefinidos
	 * @return int
	 */
	public int getNumPredefinidos()
	{
		return numPredefinidos;
	}

	/**
	 * Asigna el número de antecedentes predefinidos
	 * @param numPredefinidos The numPredefinidos to set
	 */
	public void setNumPredefinidos(int numPredefinidos)
	{
		this.numPredefinidos = numPredefinidos;
	}

	/**
	 * Retorna el número de antecedentes tóxicos otros nuevos
	 * @return int
	 */
	public int getNumOtrosNuevos()
	{
		return numOtrosNuevos;
	}

	/**
	 * Asigna el número de antecedentes tóxicos otros nuevos
	 * @param numOtrosNuevos
	 */
	public void setNumOtrosNuevos(int numOtrosNuevos)
	{
		this.numOtrosNuevos = numOtrosNuevos;
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

	/**
	 * @return the operacionExistosa
	 */
	public String getOperacionExistosa() {
		return operacionExistosa;
	}

	/**
	 * @param operacionExistosa the operacionExistosa to set
	 */
	public void setOperacionExistosa(String operacionExistosa) {
		this.operacionExistosa = operacionExistosa;
	}

	/**
	 * @return the encabezadoImpresion
	 */
	public HashMap getEncabezadoImpresion() {
		return encabezadoImpresion;
	}

	/**
	 * @param encabezadoImpresion the encabezadoImpresion to set
	 */
	public void setEncabezadoImpresion(HashMap encabezadoImpresion) {
		this.encabezadoImpresion = encabezadoImpresion;
	}
	
	/**
	 * @return the encabezadoImpresion
	 */
	public Object getEncabezadoImpresion(String key) {
		return encabezadoImpresion.get(key);
	}

	/**
	 * @param encabezadoImpresion the encabezadoImpresion to set
	 */
	@SuppressWarnings("unchecked")
	public void setEncabezadoImpresion(String key, Object value) {
		this.encabezadoImpresion.put(key, value);
	}
}