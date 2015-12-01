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
 * los datos de los antecedentes morbidos. Y adicionalmente hace el manejo de
 * reset de la forma y de validación de errores de datos de entrada.
 * @version 1.0, Agosto 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesMorbidosForm  extends ValidatorForm
{
	/**
	 * Estado actual dentro del flujo de la funcionalidad
	 */
	public String estado;
	
	/**
	 * Map para manejar los antecedentes mórbidos módicos y toda su información
	 */
	private final Map antecedentesMedicos = new HashMap();	
	
	private int numAntMorbidosMedicos = 0;
	
	private final Map antecedentesMedicosOtros = new HashMap();
	
	private int numAntMorbidosMedicosOtros = 0;
	
	private final Map antecedentesQuirurgicos = new HashMap();
	
	private int numAntMorbidosQuirurgicos = 0;
	
	private int numAntMorbidosQuirurgicosBD = 0;
	
	private String observacionesAnteriores;
	
	private String observacionesNuevas;
	
	private String ocultarCabezotes;
	
	/**
	* Valida  las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que estó siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if( estado.equals("adicionarMedico") || estado.equals("finalizar") )
		{
			for(int i=1; i<=this.numAntMorbidosMedicosOtros; i++)
			{
				String nomAntOtro = "nombre_"+i;
				if( this.getAntecedenteMedicoOtro(nomAntOtro) == null  || this.getAntecedenteMedicoOtro(nomAntOtro).equals("") )
					if( estado.equals("finalizar") && getAntecedenteMedicoOtro("chequeado_"+i) != null && !getAntecedenteMedicoOtro("chequeado_"+i).equals("false") )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "antecedente mórbido médico"));									
			}
		}
		else
		if( estado.equals("adicionarQuirurgico") || estado.equals("finalizar") )
		{
			int tam = this.numAntMorbidosQuirurgicos;
			
			if( estado.equals("adicionarQuirurgico") )
				tam += 1;
			
			for(int i=1; i<=tam; i++)
			{
				String nomAntQ = "nombre_"+i;
				if( this.getAntecedenteQuirurgico(nomAntQ) == null || this.getAntecedenteQuirurgico(nomAntQ).equals("") )
				{
					if( estado.equals("adicionarMedicamento") )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "antecedente quirúrgico"));
					else
					if( noVacio((String)this.getAntecedenteQuirurgico("fecha_"+i)) || noVacio((String)this.getAntecedenteQuirurgico("causa_"+i)) || noVacio((String)this.getAntecedenteQuirurgico("complicaciones_"+i)) || noVacio((String)this.getAntecedenteQuirurgico("recomendaciones_"+i)) || noVacio((String)this.getAntecedenteQuirurgico("observaciones_"+i)) )
						errores.add("El nombre no puede ser vacio ", new ActionMessage("errors.nombreVacio", "antecedente quirúrgico"));			
				}
			}
		}		
		
		//--------------------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------------------------------------
		//-Verificar repetidos en la seccion.(Antecedentes MÃ©dicos).
		 if ( estado.equals("finalizar") )
		 {
			 Vector v = new Vector();
			 
			 	//-- Verificar que entre los nuevos no esten repetidos.
				for(int i=1; i<=this.numAntMorbidosMedicosOtros; i++)
				{
					String nom = "nombre_"+i;
					nom = this.getAntecedenteMedicoOtro(nom)+"";
					for(int j=1; j<=this.numAntMorbidosMedicosOtros; j++)
					{
						String nom2 = "nombre_"+j;
						nom2 = this.getAntecedenteMedicoOtro(nom2)+"";
						if (
							UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && (i!=j) &&
							nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) 		
						   )
						{
							v.add(nom.trim().toLowerCase());
							errores.add("repetido", new ActionMessage("error.antecedentes.nuevosDuplicados", nom));			
						}
					}		
				} 
				
				//-- Verificar entre los nuevos con los ya registrados. 
				int tam = this.getNumAntMorbidosMedicos();
				v.clear();
				for( int i=1; i<=tam; i++ )
				{
					String nom = (String)this.getAntecedenteMedico("nom_"+i);
					
					for(int j=1; j<=this.numAntMorbidosMedicosOtros; j++)
					{
						String nom2 = "nombre_"+j;
						nom2 = this.getAntecedenteMedicoOtro(nom2)+"";

						if (
							UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && 
							nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) 		
						   )
						{
							v.add(nom.trim().toLowerCase());
							errores.add("repetido", new ActionMessage("error.antecedentes.duplicados", nom));			
						}
						
					}
					
				}	
			
		 
		 }	 
		//--------------------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------------------------------------------------------------------------------------
		 //-Validar que no hayan repetidos. en la seccion de (Morbidos Quirurgicos) 
		if ( estado.equals("adicionarQuirurgico") || estado.equals("finalizar") )
		{
			Vector v = new Vector();

			int limite = 0;
			if (estado.equals("finalizar")) limite = this.getNumAntMorbidosQuirurgicos();
			else limite = this.getNumAntMorbidosQuirurgicos() + 1;  
			
			for(int i=(this.getNumAntMorbidosQuirurgicosBD()+1); i<=limite; i++)
			{
				String nom = "nombre_"+i;
				nom = this.getAntecedenteQuirurgico(nom)+"";
				for(int j=(this.getNumAntMorbidosQuirurgicosBD()+1); j<=limite; j++)
				{
					String nom2 = "nombre_"+j;
					nom2 = this.getAntecedenteQuirurgico(nom2)+"";

					if (
						UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && (i!=j) &&
						nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) && (j!=i)		
					   )
					{
						v.add(nom.trim().toLowerCase());
						errores.add("repetido", new ActionMessage("error.antecedentes.quirur.duplicados", "MÃ³rbido QuirÃºrgico" , nom));			
					}
				}
			}	
		    
		    v.clear();
		    //-- Verificar que entre los nuevos y los ya registrados para que no existan repetidos.
			
	
		    for(int i=1; i<(this.getNumAntMorbidosQuirurgicosBD()+1); i++)
			{
				String nom = "nombre_"+i;
				nom = this.getAntecedenteQuirurgico(nom)+"";
				
				for(int j=(this.getNumAntMorbidosQuirurgicosBD()+1); j<=limite; j++)
				{
					String nom2 = "nombre_"+j;
					nom2 = this.getAntecedenteQuirurgico(nom2)+"";

					if (
						UtilidadCadena.noEsVacio(nom) && UtilidadCadena.noEsVacio(nom2) && 
						nom.trim().toLowerCase().equals(nom2.trim().toLowerCase()) && !v.contains(nom.trim().toLowerCase()) 		
					   )
					{
						v.add(nom.trim().toLowerCase());
						errores.add("repetido", new ActionMessage("error.antecedentes.quirur.duplicadosRegistrados", "MÃ³rbido QuirÃºrgico" , nom));			
					}
				}		
			} 
		}
		 
		return errores;
	}

	public void reset()
	{
		this.antecedentesMedicos.clear();
		this.numAntMorbidosMedicos = 0;
		this.antecedentesMedicosOtros.clear();
		this.numAntMorbidosMedicosOtros = 0;
		this.antecedentesQuirurgicos.clear();
		this.numAntMorbidosQuirurgicos = 0;
		this.numAntMorbidosQuirurgicosBD = 0;
		this.observacionesAnteriores = "";
		this.observacionesNuevas = "";
	}
		
	/**
	 * Retorna el estado actual dentro del flujo de la funcionalidad
	 * @return String, estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado actual dentro del flujo de la funcionalidad
	 * @param String, estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Retorna el antecedente módico dada su llave
	 * @param String, llave bajo la cual se almaceno la información
	 * @return Object, objeto almacenado en el map
	 */
	public Object getAntecedenteMedico(String key)
	{
		return this.antecedentesMedicos.get(key);
	}
	
	/**
	 * Almacena un valor de antecedente módico en el map, bajo la llave dada.
	 * @param Object, value
	 * @param String, key
	 */
	public void setAntecedenteMedico(String key, Object value)
	{
		this.antecedentesMedicos.put(key, value);		
	}	
	
	/**
	* Retorna el antecedente médico dada su llave
	* @param String, llave bajo la cual se almaceno la información
	* @return Object, objeto almacenado en el map
	*/
   public Object getAntecedenteMedicoOtro(String key)
   {
	   return this.antecedentesMedicosOtros.get(key);
   }
	
   /**
	* Almacena un valor de antecedente médico en el map, bajo la llave dada.
	* @param Object, value
	* @param String, key
	*/
   public void setAntecedenteMedicoOtro(String key, Object value)
   {
	   this.antecedentesMedicosOtros.put(key, value);		
   }
	
	/**
	 * Retorna el antecedente quirurgico dada su llave
	 * @param String, llave bajo la cual se almaceno la información
	 * @return Object, objeto almacenado en el map
	 */
	public Object getAntecedenteQuirurgico(String key)
	{
		return this.antecedentesQuirurgicos.get(key);
	}
	
	/**
	 * Almacena un valor de antecedente quirurgico en el map, bajo la llave
	 * dada.
	 * @param Object, value
	 * @param String, key
	 */
	public void setAntecedenteQuirurgico(String key, Object value)
	{
		this.antecedentesQuirurgicos.put(key, value);
	}
	
	/**
	 * Returns the numAntMorbidosMedicos.
	 * @return int
	 */
	public int getNumAntMorbidosMedicos()
	{
		return numAntMorbidosMedicos;
	}

	/**
	 * Sets the numAntMorbidosMedicos.
	 * @param numAntMorbidosMedicos The numAntMorbidosMedicos to set
	 */
	public void setNumAntMorbidosMedicos(int numAntMorbidosMedicos)
	{
		this.numAntMorbidosMedicos = numAntMorbidosMedicos;
	}

	/**
	 * Returns the numAntMorbidosQuirurgicos.
	 * @return int
	 */
	public int getNumAntMorbidosQuirurgicos()
	{
		return numAntMorbidosQuirurgicos;
	}

	/**
	 * Sets the numAntMorbidosQuirurgicos.
	 * @param numAntMorbidosQuirurgicos The numAntMorbidosQuirurgicos to set
	 */
	public void setNumAntMorbidosQuirurgicos(int numAntMorbidosQuirurgicos)
	{
		this.numAntMorbidosQuirurgicos = numAntMorbidosQuirurgicos;
	}

	/**
	 * Returns the numAntMorbidosMedicosOtros.
	 * @return int
	 */
	public int getNumAntMorbidosMedicosOtros()
	{
		return numAntMorbidosMedicosOtros;
	}

	/**
	 * Sets the numAntMorbidosMedicosOtros.
	 * @param numAntMorbidosMedicosOtros The numAntMorbidosMedicosOtros to set
	 */
	public void setNumAntMorbidosMedicosOtros(int numAntMorbidosMedicosOtros)
	{
		this.numAntMorbidosMedicosOtros = numAntMorbidosMedicosOtros;
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
	 * Sets the observacionesAnteriores.
	 * @param observacionesAnteriores The observacionesAnteriores to set
	 */
	public void setObservacionesAnteriores(String observacionesAnteriores)
	{
		this.observacionesAnteriores = observacionesAnteriores;
	}

	/**
	 * Returns the observacionesNuevas.
	 * @return String
	 */
	public String getObservacionesNuevas()
	{
		return observacionesNuevas;
	}

	/**
	 * Sets the observacionesNuevas.
	 * @param observacionesNuevas The observacionesNuevas to set
	 */
	public void setObservacionesNuevas(String observacionesNuevas)
	{
		this.observacionesNuevas = observacionesNuevas;
	}

	/**
	 * Returns the numAntMorbidosQuirurgicosBD.
	 * @return int
	 */
	public int getNumAntMorbidosQuirurgicosBD()
	{
		return numAntMorbidosQuirurgicosBD;
	}

	/**
	 * Sets the numAntMorbidosQuirurgicosBD.
	 * @param numAntMorbidosQuirurgicosBD The numAntMorbidosQuirurgicosBD to set
	 */
	public void setNumAntMorbidosQuirurgicosBD(int numAntMorbidosQuirurgicosBD)
	{
		this.numAntMorbidosQuirurgicosBD = numAntMorbidosQuirurgicosBD;
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
