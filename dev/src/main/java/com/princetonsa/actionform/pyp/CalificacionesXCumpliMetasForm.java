/*
 * Creado en Aug 8, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Form de Calificaciones por cumplimiento de metas
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Aug 8, 2006
 */
public class CalificacionesXCumpliMetasForm extends ValidatorForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Tipo de régimen seleccionado
	 */
	private String tipoRegimen;
	
	/**
	 * Nombre del tipo de régimen
	 */
	private String nombreTipoRegimen;
	
	/**
	 * Mapa para guardar la información de las calificaciones
	 * por cumplimiento de metas por régimen
	 */
	private HashMap mapaCumpliMetas;
	
	/**
	 * Indice o posición del registro que se desea eliminar
	 */
	private int indiceRegEliminar;
	
	//---------Para el ordenamiento y paginación ---------------//
	/**
     * Almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * Almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    /**
     * Número de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Variables para manejar el paginador de la consulta de pacientes del centro de costo
     */
    private String linkSiguiente;
    
    /**
     * Para controlar la página actual
     * del pager.
     */
    private int offset;
    
    /**
     * Para controlar el numero de registros del
     * HashMap.
     */
    private int numRegistros;
	
	
//	---------------------------------------------------FIN DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Método para limpiar los atributos de la clase
	 */
	public void reset()
	{
		this.tipoRegimen="-1";
		this.nombreTipoRegimen="";
		this.mapaCumpliMetas=new HashMap();
		this.mapaCumpliMetas.put("numRegistros", "0");
		this.indiceRegEliminar=ConstantesBD.codigoNuncaValido;
		
		//-----Ordenamiento y paginación --------//
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.maxPageItems = 0;
		
		this.offset=ConstantesBD.codigoNuncaValido;
		this.numRegistros=0;
	}
	
	 /**
	 * Método que elimina un registro del mapa, de acuerdo
	 * al indice del registro a eliminar, realizando el respectivo desplazamiento
	 *
	 */
	public void eliminarRegistroMapa()
	{
		int numRegistros=Integer.parseInt(mapaCumpliMetas.get("numRegistros")+"");
		int indiceEliminado=this.getIndiceRegEliminar();
		
		for(int i=indiceEliminado; i<numRegistros-1; i++)
		{
			String codigo=mapaCumpliMetas.get("codigo_"+(i+1))+"";
			String meta=mapaCumpliMetas.get("meta_"+(i+1))+"";
			String metaAnt=mapaCumpliMetas.get("metaant_"+(i+1))+"";
			String rangoInicial=mapaCumpliMetas.get("rango_inicial_"+(i+1))+"";
			String rangoInicialAnt=mapaCumpliMetas.get("rango_inicialant_"+(i+1))+"";
			String rangoFinal=mapaCumpliMetas.get("rango_final_"+(i+1))+"";
			String rangoFinalAnt=mapaCumpliMetas.get("rango_finalant_"+(i+1))+"";
			String tipoCalificacion=mapaCumpliMetas.get("tipo_calificacion_"+(i+1))+"";
			String tipoCalificacionAnt=mapaCumpliMetas.get("tipo_calificacionant_"+(i+1))+"";
			String activo=mapaCumpliMetas.get("activo_"+(i+1))+"";
			String activoAnt=mapaCumpliMetas.get("activoant_"+(i+1))+"";
			String estaGrabado=mapaCumpliMetas.get("esta_grabado_"+(i+1))+"";
			
			mapaCumpliMetas.put("codigo_"+i, codigo);
			mapaCumpliMetas.put("meta_"+i, meta);
			mapaCumpliMetas.put("metaant_"+i, metaAnt);
			mapaCumpliMetas.put("rango_inicial_"+i, rangoInicial);
			mapaCumpliMetas.put("rango_inicialant_"+i, rangoInicialAnt);
			mapaCumpliMetas.put("rango_final_"+i, rangoFinal);
			mapaCumpliMetas.put("rango_finalant_"+i, rangoFinalAnt);
			mapaCumpliMetas.put("tipo_calificacion_"+i, tipoCalificacion);
			mapaCumpliMetas.put("tipo_calificacionant_"+i, tipoCalificacionAnt);
			mapaCumpliMetas.put("activo_"+i, activo);
			mapaCumpliMetas.put("activoant_"+i, activoAnt);
			mapaCumpliMetas.put("esta_grabado_"+i, estaGrabado);
		}
		//---------El último registro se coloca en null----------------//
		mapaCumpliMetas.put("codigo_"+(numRegistros-1), null);
		mapaCumpliMetas.put("meta_"+(numRegistros-1), null);
		mapaCumpliMetas.put("metaant_"+(numRegistros-1), null);
		mapaCumpliMetas.put("rango_inicial_"+(numRegistros-1), null);
		mapaCumpliMetas.put("rango_inicialant_"+(numRegistros-1), null);
		mapaCumpliMetas.put("rango_final_"+(numRegistros-1), null);
		mapaCumpliMetas.put("rango_finalant_"+(numRegistros-1), null);
		mapaCumpliMetas.put("tipo_calificacion_"+(numRegistros-1), null);
		mapaCumpliMetas.put("tipo_calificacionant_"+(numRegistros-1), null);
		mapaCumpliMetas.put("activo_"+(numRegistros-1), null);
		mapaCumpliMetas.put("activoant_"+(numRegistros-1), null);
		mapaCumpliMetas.put("esta_grabado_"+(numRegistros-1), null);
		
		//---------Se decrementa en 1 el número de registros del mapa ---------//
		mapaCumpliMetas.put("numRegistros", numRegistros-1+"");
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
		
		//-----Si el estado es guardar---//
		if(estado.equals("guardar"))
		{
			if(this.mapaCumpliMetas!=null)
			{
				int nroRegistros=Integer.parseInt(this.mapaCumpliMetas.get("numRegistros")+"");
				
				for(int i=0; i<nroRegistros; i++)
				{
					//--------------------------Se verifica el campo meta -------------------------------------------------//
					if (UtilidadCadena.noEsVacio(this.mapaCumpliMetas.get("meta_"+i)+""))
					{
						int resp=UtilidadCadena.validarPorcentajeString((this.mapaCumpliMetas.get("meta_"+i)+""), 2, ".");
						
						if(resp==1 || resp==4)
						{
							errores.add("Mayor/igual Cero", new ActionMessage("errors.MayorIgualQue", "La Meta ["+(i+1)+"]", "0"));
						}
						
						if(resp==2 || resp==5)
						{
							errores.add("Menor/igual Cien", new ActionMessage("errors.MenorIgualQue", "La Meta ["+(i+1)+"]", "100"));
						}
						
						if(resp==3 || resp==4 || resp==5)
						{
							errores.add("Decimales Meta", new ActionMessage("errors.numDecimales", "La Meta ["+(i+1)+"]", "2"));
						}
					}
					else
					{
						errores.add("Meta vacio", new ActionMessage("errors.required","La Meta ["+(i+1)+"]"));
					}
				
				//--------------------------Se verifica el campo Rango Inicial -------------------------------------------------//
					if (UtilidadCadena.noEsVacio(this.mapaCumpliMetas.get("rango_inicial_"+i)+""))
					{
						int resp=UtilidadCadena.validarPorcentajeString((this.mapaCumpliMetas.get("rango_inicial_"+i)+""), 2, ".");
						
						if(resp==1 || resp==4)
						{
							errores.add("Mayor/igual Cero", new ActionMessage("errors.MayorIgualQue", "El Rango Inicial ["+(i+1)+"]", "0"));
						}
						
						if(resp==2 || resp==5)
						{
							errores.add("Menor/igual Cien", new ActionMessage("errors.MenorIgualQue", "El Rango Inicial ["+(i+1)+"]", "100"));
						}
						
						if(resp==3 || resp==4 || resp==5)
						{
							errores.add("Decimales RangoInicial", new ActionMessage("errors.numDecimales", "El Rango Inicial ["+(i+1)+"]", "2"));
						}
					}
					else
					{
						errores.add("RangoInicial vacio", new ActionMessage("errors.required","El Rango Inicial ["+(i+1)+"]"));
					}
					
				//--------------------------Se verifica el campo Rango Final -------------------------------------------------//
					if (UtilidadCadena.noEsVacio(this.mapaCumpliMetas.get("rango_final_"+i)+""))
					{
						int resp=UtilidadCadena.validarPorcentajeString((this.mapaCumpliMetas.get("rango_final_"+i)+""), 2, ".");
						
						if(resp==1 || resp==4)
						{
							errores.add("Mayor/igual Cero", new ActionMessage("errors.MayorIgualQue", "El Rango Final ["+(i+1)+"]", "0"));
						}
						
						if(resp==2 || resp==5)
						{
							errores.add("Menor/igual Cien", new ActionMessage("errors.MenorIgualQue", "El Rango Final ["+(i+1)+"]", "100"));
						}
						
						if(resp==3 || resp==4 || resp==5)
						{
							errores.add("Decimales RangoFinal", new ActionMessage("errors.numDecimales", "El Rango Final ["+(i+1)+"]", "2"));
						}
						
						//---------------Se verifica que el rango final sea mayor al rango inicial-------------------//
						if (UtilidadCadena.noEsVacio(this.mapaCumpliMetas.get("rango_inicial_"+i)+""))
						{
							float rangoInicial = Float.parseFloat(this.mapaCumpliMetas.get("rango_inicial_"+i)+"");
							float rangoFinal = Float.parseFloat(this.mapaCumpliMetas.get("rango_final_"+i)+"");
							
							//------Si el rango final es menor o igual al rango inicial se muestra el error -------//
							if(rangoFinal <= rangoInicial)
							{
								errores.add("Menor/igual RangoInicial", new ActionMessage("errors.MayorQue", "El Rango Final ["+(i+1)+"] " +rangoFinal+"", " el Rango Inicial "+rangoInicial));
							}
						}
						
					}
					else
					{
						errores.add("RangoFinal vacio", new ActionMessage("errors.required","El Rango Final ["+(i+1)+"]"));
					}
					
					//--------------------------Se verifica el Tipo de Calificación -------------------------------------------------//
					if (UtilidadCadena.noEsVacio(this.mapaCumpliMetas.get("tipo_calificacion_"+i)+""))
					{
						if((this.mapaCumpliMetas.get("tipo_calificacion_"+i)+"").equals("-1"))
						{
							errores.add("TipoCalificacion vacio", new ActionMessage("errors.required","El Tipo de Calificación ["+(i+1)+"]"));
						}
					}
					
				}//for
				
				//------Se verifica que no se ingrese un registro con la misma meta, rango inicial y rango final -----------//
				for(int i=0; i<nroRegistros; i++)
				{
					int c=i+1;
					for(int j=c; j<nroRegistros; j++)
					{
						boolean activo1=UtilidadTexto.getBoolean(this.mapaCumpliMetas.get("activo_"+i));
						boolean activo2=UtilidadTexto.getBoolean(this.mapaCumpliMetas.get("activo_"+j));
						
						if(activo1 && activo2)
						{
							String meta1=this.mapaCumpliMetas.get("meta_"+i)+"";
							String meta2=this.mapaCumpliMetas.get("meta_"+j)+"";
							String rangoInicial1=this.mapaCumpliMetas.get("rango_inicial_"+i)+"";
							String rangoInicial2=this.mapaCumpliMetas.get("rango_inicial_"+j)+"";
							String rangoFinal1=this.mapaCumpliMetas.get("rango_final_"+i)+"";
							String rangoFinal2=this.mapaCumpliMetas.get("rango_final_"+j)+"";
							String tipoCalificacion1=this.mapaCumpliMetas.get("tipo_calificacion_"+i)+"";
							String tipoCalificacion2=this.mapaCumpliMetas.get("tipo_calificacion_"+j)+"";
							
							if(
									UtilidadCadena.noEsVacio(meta1) &&
									UtilidadCadena.noEsVacio(meta2) &&
									UtilidadCadena.noEsVacio(rangoInicial1) &&
									UtilidadCadena.noEsVacio(rangoInicial2) &&
									UtilidadCadena.noEsVacio(rangoFinal1) &&
									UtilidadCadena.noEsVacio(rangoFinal2) &&
									UtilidadCadena.noEsVacio(tipoCalificacion1) &&
									UtilidadCadena.noEsVacio(tipoCalificacion2)
									)
							{
								float m1=Utilidades.convertirAFloat(meta1);
								float m2=Utilidades.convertirAFloat(meta2);
								float ri1=Utilidades.convertirAFloat(rangoInicial1);
								float ri2=Utilidades.convertirAFloat(rangoInicial2);
								float rf1=Utilidades.convertirAFloat(rangoFinal1);
								float rf2=Utilidades.convertirAFloat(rangoFinal2);
								float tc1=Utilidades.convertirAFloat(tipoCalificacion1);
								float tc2=Utilidades.convertirAFloat(tipoCalificacion2);
								if(m1 == m2)
								{
									errores.add("Repetidas metas"+i+" "+j, new ActionMessage("error.pyp.calificacionesXCumpliMetas.registrosRepetidosMetas", (i+1), (j+1)));
								}
								if(tc1 == tc2)
								{
									errores.add("Repetidas Calificaciones"+i+" "+j, new ActionMessage("error.pyp.calificacionesXCumpliMetas.registrosRepetidosCalificacion", (i+1), (j+1)));
								}
								/*
								 Caso 1
								       ri1        rf1
								 -------|----------|---------------
								 -------------|----------|---------
								             ri2        rf2
	
	 							 Caso 2
								             ri1        rf1
								 -------------|----------|---------
								 -------|----------|---------------
								       ri2        rf2
	
	 							 Caso 3
								       ri1              rf1
								 -------|----------------|---------
								 ----------|----------|------------
								          ri2        rf2
	
	  							 Caso 4
								          ri1        rf1
								 ----------|----------|------------
								 -------|----------------|---------
								       ri2              rf2
	
								 
								 * Con esta condicion cumplo todos los casos */
								if( (ri2>=ri1 && ri2<=rf1) || (ri1>=ri2 && ri1<=rf2) ||
									(rf2>=ri1 && rf2<=rf1) || (rf1>=ri2 && rf1<=rf2)
								)
								{
									errores.add("Repetidos Rangos"+i+" "+j, new ActionMessage("error.pyp.calificacionesXCumpliMetas.rangosCruzados", (i+1), (j+1)));
								}
							}// if no vacío
						}// if activo1 && activo2
					}//for
					
				}//for
				
			}//if mapaCumpliMetas != null
		}
		
		
		return errores;
	}
	
//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//

	/**
	 * @return Retorna the estado.
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
		this.estado = estado;
	}

	/**
	 * @return Retorna the mapaCumpliMetas.
	 */
	public HashMap getMapaCumpliMetas()
	{
		return mapaCumpliMetas;
	}

	/**
	 * @param mapaCumpliMetas The mapaCumpliMetas to set.
	 */
	public void setMapaCumpliMetas(HashMap mapaCumpliMetas)
	{
		this.mapaCumpliMetas = mapaCumpliMetas;
	}
	
	/**
	 * @return Retorna mapaCumpliMetas.
	 */
	public Object getMapaCumpliMetas(String key) {
		return mapaCumpliMetas.get( key );
	}

	/**
	 * @param Asigna mapaCumpliMetas.
	 */
	public void setMapaCumpliMetas(String key, String dato) {
		this.mapaCumpliMetas.put(key, dato);
	}

	/**
	 * @return Retorna the tipoRegimen.
	 */
	public String getTipoRegimen()
	{
		return tipoRegimen;
	}

	/**
	 * @param tipoRegimen The tipoRegimen to set.
	 */
	public void setTipoRegimen(String tipoRegimen)
	{
		this.tipoRegimen = tipoRegimen;
	}

	/**
	 * @return Retorna the linkSiguiente.
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
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Retorna the maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna the patronOrdenar.
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
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna the ultimoPatron.
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
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Retorna the indiceRegEliminar.
	 */
	public int getIndiceRegEliminar()
	{
		return indiceRegEliminar;
	}

	/**
	 * @param indiceRegEliminar The indiceRegEliminar to set.
	 */
	public void setIndiceRegEliminar(int indiceRegEliminar)
	{
		this.indiceRegEliminar = indiceRegEliminar;
	}

	/**
	 * @return Retorna the nombreTipoRegimen.
	 */
	public String getNombreTipoRegimen()
	{
		return nombreTipoRegimen;
	}

	/**
	 * @param nombreTipoRegimen The nombreTipoRegimen to set.
	 */
	public void setNombreTipoRegimen(String nombreTipoRegimen)
	{
		this.nombreTipoRegimen = nombreTipoRegimen;
	}

	/**
	 * @return Retorna the offset.
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
		this.offset = offset;
	}

	/**
	 * @return Retorna the numRegistros.
	 */
	public int getNumRegistros()
	{
		return numRegistros;
	}

	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros)
	{
		this.numRegistros = numRegistros;
	}

}
