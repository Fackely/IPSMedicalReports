/*
 * Created on 15/04/2005
 *
 */
package com.sies.actionform;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;

/**
 * @author karenth
 * 
 * Juan David Ramírez
 */
public class AsociarEnfermeraCategoriaForm extends ActionForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Codigo asignado por el sistema a la enfermera 
	 */ 
	private int codigoPersona;

	/**
	 * Hash Map donde se hacen todas las modificaciones antes de guardar modificaciones
	 */
	private HashMap<String, Object> asignadas;
	
	/**
	 * Hash map donde se guardan lo que hay en las base de datos antes de comenzar,
	 *  para los logs de las modificaciones
	 */
	private HashMap<String, Object> asignadasBD;
	
	/**
	 * Codigo de la categoria
	 */
	private int codigoCategoria;
	
	/**
	 * fecha de inicio de la asociación de la categoria a la enfermera
	 */
	private String fechaInicio;
	
	/**
	 * fecha de finalización de la asociación de la enfermera a la categoria
	 */
	private String fechaFin;

	/**
	 * Fecha de inicio de la asociación para el manejo de validaciones entre fechas
	 */
	private String fechaInicioBD;

	/**
	 * Fecha de fin de la asociación para el manejo de validaciones entre fechas
	 */
	private String fechaFinBD;

	/**
	 * Para guardar el resultado de la consulta 
	 */
	private Collection<HashMap<String, Object>> listado;
	
	/**
	 * Categorias que tiene la enfermera consultada
	 */
	private Collection<HashMap<String, Object>> categoriasEnfermera;
	
	/**
	 * String para almacenar el nombre de la enfermera
	 */
	private String nombrePersona;
	
	/**
	 * Son las diferentes enfermeras que se van asignando
	 */
	private String nuevaAsignacion;
	
	/**
	 * Nombre de la Persona Anterior
	 */
	private String nombrePersonaAnt;
	
	/**
	 * Para manejar los logs de modificación
	 */
	private String log;
	
	/**
	 * Con este parámetro verifico si hubo o no modificaciones
	 * en la BD para mostrar el mensaje
	 */
	private boolean hayModificaciones;
	
	/**
	 * Manejo de estados de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Boolean para cambiar la persona
	 */
	private boolean cambiar;
	
	/**
	 * String para el manejo de un mensaje que indica que la enferme ya fue asignada a una categoria
	 */
	private String mensajeReemplazo;
	
	/**
	 * código de la categoría que se confirma para cambiar
	 */
	private int codigoCatCambiar;
	
	/**
	 * Nombre de la categoría que se confirma para cambiar
	 */
	private String nombreCatCambiar;
	
	/**
	 * Ménsaje que indica el resultado de los procesos
	 */
	private String mensajeResultado; 
	
	/**
	 * Listar las personas pertenencientes a la institución
	 */
	private Collection<HashMap<String, Object>> listadoPersonas;

	/**
	 * Listar las personas pertenencientes a la institución
	 */
	private Collection<HashMap<String, Object>> listadoPersonasBD;
	
	/**
	 * Conservar los criterios de busqeuda para la cancelación
	 */
	private int codigoBusquedaAnterior;
	private String nombreBusquedaAnterior;
	private int categoriaBusquedaAnterior;
	
	private String nombreCategoriaAnterior="";
	
	private boolean eliminarTurnos=false;
	
	private boolean existeCuadroDestino=true;
	
	private boolean existenTurnosGenerados=true;
	
	private String ultimaFechaDestino;
	
	private int indiceElimiarFin;

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
		
		//System.out.println("estado AsociarEnfermeraCategoriaForm ="+estado);
		
		if (estado.equalsIgnoreCase("asignar"))
		{
			if(codigoCategoria==0)
			{
				errores.add("categoria", new ActionMessage("errors.required", "Categoría"));
			}
			if(codigoPersona==0)
			{
				errores.add("persona", new ActionMessage("errors.required", "Persona"));
			}
			String fechaInicioTempo="";
			if(!fechaInicio.trim().equalsIgnoreCase(""))
			{
				fechaInicioTempo=fechaInicio;
			}
			else
			{
				fechaInicioTempo=UtilidadFecha.getFechaActual();
			}
			if(!UtilidadFecha.esFechaValidaSegunAp(fechaInicioTempo))
			{
				errores.add("fecha inicio", new ActionMessage("errors.formatoFechaInvalido", "de Inicio"));
			}
			else
			{
				String fechaActual=UtilidadFecha.getFechaActual();
				if(UtilidadFecha.conversionFormatoFechaABD(fechaInicioTempo).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaActual))<0)
				{
					errores.add("fecha inicio", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de Inicio", "de Actual"));
				}
				if(!fechaFin.trim().equalsIgnoreCase(""))
				{
					if(!UtilidadFecha.esFechaValidaSegunAp(fechaFin))
					{
						errores.add("fecha fin", new ActionMessage("errors.formatoFechaInvalido", "de Fin"));
					}
					else if(UtilidadFecha.conversionFormatoFechaABD(fechaFin).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))<0)
					{
						errores.add("fecha fin", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de Finalización", "Actual"));
					}
					else if(UtilidadFecha.conversionFormatoFechaABD(fechaFin).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicioTempo))<0)
					{
						errores.add("fecha fin", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de Finalización", "de Inicio"));
					}
				}
			}
			if(errores.isEmpty())
			{
				for(int i=0;i<categoriasEnfermera.size();i++)
				{
					int codCategoria=((Integer)asignadas.get("categoria_"+i)).intValue();
					for(int y=0;y<((Integer)asignadas.get("numElementos_"+codCategoria)).intValue();y++)
					{
						if ((asignadas.get("codigo_"+codCategoria+"_"+y)+"").equals(codigoPersona+""))
						{
							mensajeReemplazo="La persona: "+nombrePersona+" ya se encuentra asignada a la categoría: "+((String)asignadas.get("nombreCategoria_"+i+""))+"\nConfirme si desea asignarla a la nueva Categoría ";
							codigoCatCambiar=codCategoria;
							nombreCatCambiar="nombre_"+codCategoria+"_"+y;
							nombreCategoriaAnterior=(String)asignadas.get("nombreCategoria_"+i);
							break;
						}
					}
				}
			}
		}
		else if(estado.equalsIgnoreCase("cancelarAsociacion"))
		{
			if(fechaFin==null || fechaFin.trim().equalsIgnoreCase(""))
			{
				errores.add("fecha fin", new ActionMessage("errors.required", "Fecha"));
			}
			else
			{
				String fechaActualBD=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
				if(!UtilidadFecha.esFechaValidaSegunAp(fechaFin))
				{
					errores.add("fecha fin", new ActionMessage("errors.formatoFechaInvalido", "Fecha"));
				}
				else
				{
					String fechaFinFormatoBD=UtilidadFecha.conversionFormatoFechaABD(fechaFin);
					if(fechaActualBD.compareTo(fechaFinFormatoBD)>0)
					{
						errores.add("fecha fin", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de cancelación", "Actual"));
					}
					else
					{
						if(fechaFinFormatoBD.compareTo(fechaInicioBD)<0)
						{
							errores.add("fecha fin", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de cancelación", UtilidadFecha.conversionFormatoFechaAAp(fechaInicioBD)));
						}
						if(fechaFinBD!=null && !fechaFinBD.equals("") && fechaFinFormatoBD.compareTo(fechaFinBD)>0)
						{
							errores.add("fecha fin", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "de cancelación", UtilidadFecha.conversionFormatoFechaAAp(fechaFinBD)));
						}
					}
				}
				/*
				else if(UtilidadFecha.conversionFormatoFechaABD(fechaFin).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))<0)
				{
					errores.add("fecha fin", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de Finalización", "Actual"));
				}
				else if(UtilidadFecha.conversionFormatoFechaABD(fechaFin).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInicioTempo))<0)
				{
					errores.add("fecha fin", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de Finalización", "de Inicio"));
				}*/
			}
			
		}
			
		return errores;
	}
	
	/**
	 * Reiniciar todos los aributos de la clase
	 */
	public void clean()
	{
		this.codigoPersona=0;
		this.codigoCategoria=0;
		this.fechaInicio="";
		this.fechaFin="";
		this.nuevaAsignacion="";
		this.asignadas=new HashMap<String, Object>();
		this.mensajeReemplazo="";
		this.cambiar=false;
		this.codigoCatCambiar=0;
		this.nombreCatCambiar="";
		this.listado=null;
		this.categoriasEnfermera=null;
		this.hayModificaciones=false;
		this.mensajeResultado=new String();
		this.nombrePersona="";
		this.nombrePersonaAnt="";
		this.codigoBusquedaAnterior=0;
		this.nombreBusquedaAnterior="";
		this.categoriaBusquedaAnterior=0;
		this.eliminarTurnos=false;
		this.nombreCategoriaAnterior="";
		this.ultimaFechaDestino="";
		this.indiceElimiarFin=0;
	}

	/**
	 * @return asignadas
	 */
	public HashMap<String, Object> getAsignadas()
	{
		return asignadas;
	}

	/**
	 * @param asignadas Asigna asignadas
	 */
	public void setAsignadas(HashMap<String, Object> asignadas)
	{
		this.asignadas = asignadas;
	}

	/**
	 * @return asignadasBD
	 */
	public HashMap<String, Object> getAsignadasBD()
	{
		return asignadasBD;
	}

	/**
	 * @param asignadasBD Asigna asignadasBD
	 */
	public void setAsignadasBD(HashMap<String, Object> asignadasBD)
	{
		this.asignadasBD = asignadasBD;
	}

	/**
	 * @return cambiar
	 */
	public boolean getCambiar()
	{
		return cambiar;
	}

	/**
	 * @param cambiar Asigna cambiar
	 */
	public void setCambiar(boolean cambiar)
	{
		this.cambiar = cambiar;
	}

	/**
	 * @return categoriasEnfermera
	 */
	public Collection<HashMap<String, Object>> getCategoriasEnfermera()
	{
		return categoriasEnfermera;
	}

	/**
	 * @param categoriasEnfermera Asigna categoriasEnfermera
	 */
	public void setCategoriasEnfermera(Collection<HashMap<String, Object>> categoriasEnfermera)
	{
		this.categoriasEnfermera = categoriasEnfermera;
	}

	/**
	 * @return codigoCategoria
	 */
	public int getCodigoCategoria()
	{
		return codigoCategoria;
	}

	/**
	 * @param codigoCategoria Asigna codigoCategoria
	 */
	public void setCodigoCategoria(int codigoCategoria)
	{
		this.codigoCategoria = codigoCategoria;
	}

	/**
	 * @return codigoPersona
	 */
	public int getCodigoPersona()
	{
		return codigoPersona;
	}

	/**
	 * @param codigoPersona Asigna codigoPersona
	 */
	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
	}

	/**
	 * @return estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param fechaFin Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param fechaInicio Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return hayModificaciones
	 */
	public boolean getHayModificaciones()
	{
		return hayModificaciones;
	}

	/**
	 * @param hayModificaciones Asigna hayModificaciones
	 */
	public void setHayModificaciones(boolean hayModificaciones)
	{
		this.hayModificaciones = hayModificaciones;
	}

	/**
	 * @return listado
	 */
	public Collection<HashMap<String, Object>> getListado()
	{
		return listado;
	}

	/**
	 * @param listado Asigna listado
	 */
	public void setListado(Collection<HashMap<String, Object>> listado)
	{
		this.listado = listado;
	}

	/**
	 * @return log
	 */
	public String getLog()
	{
		return log;
	}

	/**
	 * @param log Asigna log
	 */
	public void setLog(String log)
	{
		this.log = log;
	}

	/**
	 * @return mensajeReemplazo
	 */
	public String getMensajeReemplazo()
	{
		return mensajeReemplazo;
	}

	/**
	 * @param mensajeReemplazo Asigna mensajeReemplazo
	 */
	public void setMensajeReemplazo(String mensajeReemplazo)
	{
		this.mensajeReemplazo = mensajeReemplazo;
	}

	/**
	 * @return nombreCatCambiar
	 */
	public String getNombreCatCambiar()
	{
		return nombreCatCambiar;
	}

	/**
	 * @param nombreCatCambiar Asigna nombreCatCambiar
	 */
	public void setNombreCatCambiar(String nombreCatCambiar)
	{
		this.nombreCatCambiar = nombreCatCambiar;
	}

	/**
	 * @return nombreEnfermeraAnt
	 */
	public String getNombrePersonaAnt()
	{
		return nombrePersonaAnt;
	}

	/**
	 * @param nombreEnfermeraAnt Asigna nombreEnfermeraAnt
	 */
	public void setNombrePersonaAnt(String nombrePersonaAnt)
	{
		this.nombrePersonaAnt = nombrePersonaAnt;
	}

	/**
	 * @return nombrePersona
	 */
	public String getNombrePersona()
	{
		return nombrePersona;
	}

	/**
	 * @param nombrePersona Asigna nombrePersona
	 */
	public void setNombrePersona(String nombrePersona)
	{
		this.nombrePersona = nombrePersona;
	}

	/**
	 * @return nuevaAsignacion
	 */
	public String getNuevaAsignacion()
	{
		return nuevaAsignacion;
	}

	/**
	 * @param nuevaAsignacion Asigna nuevaAsignacion
	 */
	public void setNuevaAsignacion(String nuevaAsignacion)
	{
		this.nuevaAsignacion = nuevaAsignacion;
	}

	/**
	 * @return mensajeResultado
	 */
	public String getMensajeResultado()
	{
		return mensajeResultado;
	}

	/**
	 * @param mensajeResultado Asigna mensajeResultado
	 */
	public void setMensajeResultado(String mensajeResultado)
	{
		this.mensajeResultado = mensajeResultado;
	}

	/**
	 * @return listadoPersonas
	 */
	public Collection<HashMap<String, Object>> getListadoPersonas()
	{
		return listadoPersonas;
	}

	/**
	 * @param listadoPersonas Asigna listadoPersonas
	 */
	public void setListadoPersonas(Collection<HashMap<String, Object>> listadoPersonas)
	{
		this.listadoPersonas = listadoPersonas;
	}

	/**
	 * @return listadoPersonasBD
	 */
	public Collection<HashMap<String, Object>> getListadoPersonasBD()
	{
		return listadoPersonasBD;
	}

	/**
	 * @param listadoPersonasBD Asigna listadoPersonasBD
	 */
	public void setListadoPersonasBD(Collection<HashMap<String, Object>> listadoPersonasBD)
	{
		this.listadoPersonasBD = listadoPersonasBD;
	}

	/**
	 * @return categoriaBusquedaAnterior
	 */
	public int getCategoriaBusquedaAnterior()
	{
		return categoriaBusquedaAnterior;
	}

	/**
	 * @param categoriaBusquedaAnterior Asigna categoriaBusquedaAnterior
	 */
	public void setCategoriaBusquedaAnterior(int categoriaBusquedaAnterior)
	{
		this.categoriaBusquedaAnterior = categoriaBusquedaAnterior;
	}

	/**
	 * @return codigoBusquedaAnterior
	 */
	public int getCodigoBusquedaAnterior()
	{
		return codigoBusquedaAnterior;
	}

	/**
	 * @param codigoBusquedaAnterior Asigna codigoBusquedaAnterior
	 */
	public void setCodigoBusquedaAnterior(int codigoBusquedaAnterior)
	{
		this.codigoBusquedaAnterior = codigoBusquedaAnterior;
	}

	/**
	 * @return nombreBusquedaAnterior
	 */
	public String getNombreBusquedaAnterior()
	{
		return nombreBusquedaAnterior;
	}

	/**
	 * @param nombreBusquedaAnterior Asigna nombreBusquedaAnterior
	 */
	public void setNombreBusquedaAnterior(String nombreBusquedaAnterior)
	{
		this.nombreBusquedaAnterior = nombreBusquedaAnterior;
	}

	/**
	 * @return eliminarTurnos
	 */
	public boolean getEliminarTurnos()
	{
		return eliminarTurnos;
	}

	/**
	 * @param eliminarTurnos Asigna eliminarTurnos
	 */
	public void setEliminarTurnos(boolean eliminarTurnos)
	{
		this.eliminarTurnos = eliminarTurnos;
	}

	/**
	 * @return nombreCategoriaAnterior
	 */
	public String getNombreCategoriaAnterior()
	{
		return nombreCategoriaAnterior;
	}

	/**
	 * @param nombreCategoriaAnterior Asigna nombreCategoriaAnterior
	 */
	public void setNombreCategoriaAnterior(String nombreCategoriaAnterior)
	{
		this.nombreCategoriaAnterior = nombreCategoriaAnterior;
	}

	/**
	 * @return existeCuadroDestino
	 */
	public boolean getExisteCuadroDestino()
	{
		return existeCuadroDestino;
	}

	/**
	 * @param existeCuadroDestino Asigna existeCuadroDestino
	 */
	public void setExisteCuadroDestino(boolean existeCuadroDestino)
	{
		this.existeCuadroDestino = existeCuadroDestino;
	}

	/**
	 * @return codigoCatCambiar
	 */
	public int getCodigoCatCambiar()
	{
		return codigoCatCambiar;
	}

	/**
	 * @param codigoCatCambiar Asigna codigoCatCambiar
	 */
	public void setCodigoCatCambiar(int codigoCatCambiar)
	{
		this.codigoCatCambiar = codigoCatCambiar;
	}

	/**
	 * @return fechaFinBD
	 */
	public String getFechaFinBD()
	{
		return fechaFinBD;
	}

	/**
	 * @param fechaFinBD Asigna fechaFinBD
	 */
	public void setFechaFinBD(String fechaFinBD)
	{
		this.fechaFinBD = fechaFinBD;
	}

	/**
	 * @return fechaInicioBD
	 */
	public String getFechaInicioBD()
	{
		return fechaInicioBD;
	}

	/**
	 * @param fechaInicioBD Asigna fechaInicioBD
	 */
	public void setFechaInicioBD(String fechaInicioBD)
	{
		this.fechaInicioBD = fechaInicioBD;
	}

	/**
	 * @return existenTurnosGenerados
	 */
	public boolean getExistenTurnosGenerados()
	{
		return existenTurnosGenerados;
	}

	/**
	 * @param existenTurnosGenerados Asigna existenTurnosGenerados
	 */
	public void setExistenTurnosGenerados(boolean existenTurnosGenerados)
	{
		this.existenTurnosGenerados = existenTurnosGenerados;
	}

	/**
	 * @return ultimaFechaDestino
	 */
	public String getUltimaFechaDestino()
	{
		return ultimaFechaDestino;
	}

	/**
	 * @param ultimaFechaDestino Asigna ultimaFechaDestino
	 */
	public void setUltimaFechaDestino(String ultimaFechaDestino)
	{
		this.ultimaFechaDestino = ultimaFechaDestino;
	}

	public int getIndiceElimiarFin()
	{
		return indiceElimiarFin;
	}

	public void setIndiceElimiarFin(int indiceElimiarFin)
	{
		this.indiceElimiarFin = indiceElimiarFin;
	}
}
