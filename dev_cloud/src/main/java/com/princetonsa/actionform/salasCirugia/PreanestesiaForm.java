/*
 * Creado en Oct 27, 2005
 */
package com.princetonsa.actionform.salasCirugia;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PreanestesiaForm  extends ValidatorForm
{
	 /**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
		
 /**
  * Fecha de la Preanestesia
  */
	private String fechaPreanestesia;
	
/**
 * Hora de la Preanestesia 
 */
	private String horaPreanestesia;

/**
 * Observaciones Generales de la Preanestesia
 */
private String observacionesGrales;

/**
* Campo para registrar las observaciones generales nuevas de la Preanestesia 
*/

private String observacionesGralesNueva;

/**
 * Mapa de la preanestesia
 */
private HashMap mapa = new HashMap();

/**
 * Mapa para almacenar los examenes Fisicos
 */
private HashMap mapaExamenFisico = new HashMap();

/**
 * Mapa para almacenar las conclusiones parametrizadas en la preanestesia 
 */
private HashMap mapaConclusion = new HashMap();

/**
 * Mapa para guardar los tipos de exámenes de laboratorio
 */
private HashMap mapaTipoExamenesLab = new HashMap();

//---------------------------------------------Tipos parametrizados en la Preanestesia------------------------------------------------------//
/**
 * Colección para traer los tipos de Exámenes de Laboratorio parametrizados para la institución
 */
private Collection listadoTiposExamenesLab;

/**
 * Colección para traer los tipos de Exámenes físicos de Preanestesia parametrizados para la institución de tipo text
 */
private Collection listadoTiposExamFisicosText;

/**
 * Colección para traer los tipos de Exámenes físicos de Preanestesia parametrizados para la institución de tipo textArea
 */
private Collection listadoTiposExamFisicosArea;

/**
 * Colección para traer los tipos de Conclusiones de Preanestesia parametrizados para la institución
 */
private Collection listadoTiposConclusiones;

//------------------------------Campos de información general de la petición--------------------------------------------//
/**
 * Número de la petición de cirugía
 */
	private int peticionCirugia;
	
/**
 * Fecha de la petición de cirugía
 */
	private String fechaPeticion;
	
/**
 * Hora de la petición de cirugía
 */
	private String horaPeticion;
	
/**
 * Estado de la petición de cirugía
 */
	private String nombreEstadoPeticion;
	
/**
 * Código de la petición de cirugía
 */
	private int codigoEstadoPeticion;
	
/**
 * Fecha estimada de la cirugía
 */
	private String fechaCirugia;
	
/**
 * Duración aproximada de la cirugía
 */
	private String duracionCirugia;
	
/**
 * Nombre completo del profesional que solicita
 */
	private String nombreSolicitante;
	
/**
 * Para almacenar el tipo de anestesia 
 */
	private int tipoAnestesia;
	
//-----------------------------------Otros atributos de la clase--------------------------------------------//
  /**
   * Campo para saber si se debe ocultar el cabezote superior e inferior
   */
	private int ocultarCabezote;
	
 /**
  * Campo para guardar el código del estado de la orden de cirugía
  */
	private int estadoSolicitud;
	
	/**
	 * Campo para indicar si se debe cargar la fecha y hora de preanestesia y no permitir su modicación
	 */
	private boolean cargarFechaHoraPreanestesia;
	
	/**
	 * Campo que indica si debe mostrar el menu de Hoja de Anestesia
	 * */
	private HashMap infoHojaAnestesiaMap = new HashMap();

//---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//

/**
 * Este método inicializa los atributos de la clase con valores vacíos
 */
public void reset()
{
	this.peticionCirugia=0;
	this.fechaPreanestesia=UtilidadFecha.getFechaActual();
	this.horaPreanestesia=UtilidadFecha.getHoraActual();
	this.observacionesGrales ="";
	this.observacionesGralesNueva = "";
  	this.fechaPeticion = "";
  	this.horaPeticion = "";
  	this.fechaCirugia = "";
  	this.nombreEstadoPeticion = "";
  	this.duracionCirugia = "";
  	this.nombreSolicitante = "";
  	this.cargarFechaHoraPreanestesia=false;
  	this.tipoAnestesia = 0;  	
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
	boolean errorHora=false;
	
	if(estado.equals("salir"))
	{
		//Fecha actual y patrón de fecha a utilizar en las validaciones
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		
		//Validación de la Hora de Preanestesia
		if (this.horaPreanestesia.trim().equals(""))
		{
			errorHora=true;
			errores.add("Campo Hora Preanestesia vacio", new ActionMessage("errors.required","El campo Hora Preanestesia"));
		}
		else
		{
			if(!UtilidadFecha.validacionHora(this.horaPreanestesia).puedoSeguir)
			{
				errorHora=true;
				errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de Preanestesia "+this.horaPreanestesia));
			}
			else if(this.horaPreanestesia.compareTo("00:00")<=0)
			{
				errorHora=true;
				errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "de Preanestesia ", "00:00"));
			}
		}
		
		//Validación de la Fecha de Preanestesia
		if (this.fechaPreanestesia.trim().equals(""))
		{
			errores.add("Campo Fecha Preanestesia vacio", new ActionMessage("errors.required","El campo Fecha Preanestesia"));
		}
		else
		{
			if(!UtilidadFecha.validarFecha(this.fechaPreanestesia))
			{
					errores.add("Fecha Preanestesia", new ActionMessage("errors.formatoFechaInvalido", " Preanestesia"));							
			}
			else
			{		
				boolean tieneErroresFecha=false;
				 
				//Fecha preanestesia
				Date fechaPreanestesia = null;
								
				try 
				{
					fechaPreanestesia = dateFormatter.parse(this.fechaPreanestesia);
				}	
				catch (java.text.ParseException e) 
				{
					tieneErroresFecha=true;
				}
			
				if (!tieneErroresFecha)
				{
					// Validar que la fecha hora preanestesia no sea superior a la fecha hora actual
					if((UtilidadFecha.conversionFormatoFechaABD(fechaPreanestesia)+horaPreanestesia).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+UtilidadFecha.getHoraActual())>0)
					{
						if((UtilidadFecha.conversionFormatoFechaABD(fechaPreanestesia)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
						{
							errores.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "preanestesia", "actual"));
						}
						else if(horaPreanestesia.compareTo(UtilidadFecha.getHoraActual())>0 && !errorHora)
						{
							errores.add("Hora", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "preanestesia", "actual"));
						}
					}
					
					//Validar que la fecha hora preanestesia no sea menor a la fecha hora de la petición
					if((UtilidadFecha.conversionFormatoFechaABD(fechaPreanestesia)+horaPreanestesia).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaPeticion())+this.getHoraPeticion())<0)
					{
						if((UtilidadFecha.conversionFormatoFechaABD(fechaPreanestesia)).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaPeticion()))<0)
						{
							errores.add("fecha", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "preanestesia", "petición"));
						}
						else if(horaPreanestesia.compareTo(this.getHoraPeticion())<0 && !errorHora)
						{
							errores.add("Hora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "preanestesia", "petición"));
						}
					}
					
				}//if	no tiene errores fecha
				
			}//else formato fecha
		}//else fecha preanestesia != de vacío
		
	}//estado=salir
	
	if(!errores.isEmpty())
	{
		if(estado.equals("salir"))
			this.setEstado("empezar");
	}
	
	return errores;
	
}

//-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//
	
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
		this.estado = estado;
	}
/**
 * @return Returns the peticionCirugia.
 */
public int getPeticionCirugia()
{
	return peticionCirugia;
}
/**
 * @param peticionCirugia The peticionCirugia to set.
 */
public void setPeticionCirugia(int peticionCirugia)
{
	this.peticionCirugia = peticionCirugia;
}
/**
 * @return Returns the fechaPreanestesia.
 */
public String getFechaPreanestesia()
{
	return fechaPreanestesia;
}
/**
 * @param fechaPreanestesia The fechaPreanestesia to set.
 */
public void setFechaPreanestesia(String fechaPreanestesia)
{
	this.fechaPreanestesia = fechaPreanestesia;
}
/**
 * @return Returns the horaPreanestesia.
 */
public String getHoraPreanestesia()
{
	return horaPreanestesia;
}
/**
 * @param horaPreanestesia The horaPreanestesia to set.
 */
public void setHoraPreanestesia(String horaPreanestesia)
{
	this.horaPreanestesia = horaPreanestesia;
}
/**
 * @return Returns the mapa.
 */
public HashMap getMapa()
{
	return mapa;
}
/**
 * @param mapa The mapa to set.
 */
public void setMapa(HashMap mapa)
{
	this.mapa = mapa;
}
/**
 * @return Returns the observacionesGrales.
 */
public String getObservacionesGrales()
{
	return observacionesGrales;
}
/**
 * @param observacionesGrales The observacionesGrales to set.
 */
public void setObservacionesGrales(String observacionesGrales)
{
	this.observacionesGrales = observacionesGrales;
}
/**
 * @return Returns the observacionesGralesNueva.
 */
public String getObservacionesGralesNueva()
{
	return observacionesGralesNueva;
}
/**
 * @param observacionesGralesNueva The observacionesGralesNueva to set.
 */
public void setObservacionesGralesNueva(String observacionesGralesNueva)
{
	this.observacionesGralesNueva = observacionesGralesNueva;
}
/**
 * @return Returns the listadoTiposConclusiones.
 */
public Collection getListadoTiposConclusiones()
{
	return listadoTiposConclusiones;
}
/**
 * @param listadoTiposConclusiones The listadoTiposConclusiones to set.
 */
public void setListadoTiposConclusiones(Collection listadoTiposConclusiones)
{
	this.listadoTiposConclusiones = listadoTiposConclusiones;
}
/**
 * @return Returns the listadoTiposExamenesLab.
 */
public Collection getListadoTiposExamenesLab()
{
	return listadoTiposExamenesLab;
}
/**
 * @param listadoTiposExamenesLab The listadoTiposExamenesLab to set.
 */
public void setListadoTiposExamenesLab(Collection listadoTiposExamenesLab)
{
	this.listadoTiposExamenesLab = listadoTiposExamenesLab;
}
/**
 * @return Returns the listadoTiposExamFisicosText.
 */
public Collection getListadoTiposExamFisicosText()
{
	return listadoTiposExamFisicosText;
}
/**
 * @param listadoTiposExamFisicosText The listadoTiposExamFisicosText to set.
 */
public void setListadoTiposExamFisicosText(Collection listadoTiposExamFisicos)
{
	this.listadoTiposExamFisicosText = listadoTiposExamFisicos;
}
/**
 * @return Returns the listadoTiposExamFisicosArea.
 */
public Collection getListadoTiposExamFisicosArea()
{
	return listadoTiposExamFisicosArea;
}

/**
 * @param listadoTiposExamFisicosArea The listadoTiposExamFisicosArea to set.
 */
public void setListadoTiposExamFisicosArea(
		Collection listadoTiposExamFisicosArea)
{
	this.listadoTiposExamFisicosArea = listadoTiposExamFisicosArea;
}

/**
 * @return Retorna mapaExamenFisico.
 */
public HashMap getMapaExamenFisico() {
	return mapaExamenFisico;
}

/**
 * @param Asigna mapaExamenFisico.
 */
public void setMapaExamenFisico(HashMap mapaExamenFisico) {
	this.mapaExamenFisico = mapaExamenFisico;
}

/**
 * @return Returna la propiedad del mapa mapa.
 */
public Object getMapaExamenFisico(String key)
{
	return mapaExamenFisico.get(key);
}
/**
 * @param Asigna la propiedad al mapa
 */
public void setMapaExamenFisico(String key, Object value)
{
	this.mapaExamenFisico.put(key, value);
}

/**
 * @return Returns the mapaConclusion.
 */
public HashMap getMapaConclusion()
{
	return mapaConclusion;
}
/**
 * @param mapaConclusion The mapaConclusion to set.
 */
public void setMapaConclusion(HashMap mapaConclusion)
{
	this.mapaConclusion = mapaConclusion;
}

/**
 * @return Returna la propiedad del mapa mapaConclusion.
 */
public Object getMapaConclusion(String key)
{
	return mapaConclusion.get(key);
}
/**
 * @param Asigna la propiedad al mapaConclusion
 */
public void setMapaConclusion(String key, Object value)
{
	this.mapaConclusion.put(key, value);
}

/**
 * @return Returns the codigoEstadoPeticion.
 */
public int getCodigoEstadoPeticion()
{
	return codigoEstadoPeticion;
}
/**
 * @param codigoEstadoPeticion The codigoEstadoPeticion to set.
 */
public void setCodigoEstadoPeticion(int codigoEstadoPeticion)
{
	this.codigoEstadoPeticion = codigoEstadoPeticion;
}
/**
 * @return Returns the duracionCirugia.
 */
public String getDuracionCirugia()
{
	return duracionCirugia;
}
/**
 * @param duracionCirugia The duracionCirugia to set.
 */
public void setDuracionCirugia(String duracionCirugia)
{
	this.duracionCirugia = duracionCirugia;
}
/**
 * @return Returns the fechaCirugia.
 */
public String getFechaCirugia()
{
	return fechaCirugia;
}
/**
 * @param fechaCirugia The fechaCirugia to set.
 */
public void setFechaCirugia(String fechaCirugia)
{
	this.fechaCirugia = fechaCirugia;
}
/**
 * @return Returns the fechaPeticion.
 */
public String getFechaPeticion()
{
	return fechaPeticion;
}
/**
 * @param fechaPeticion The fechaPeticion to set.
 */
public void setFechaPeticion(String fechaPeticion)
{
	this.fechaPeticion = fechaPeticion;
}
/**
 * @return Returns the horaPeticion.
 */
public String getHoraPeticion()
{
	return horaPeticion;
}
/**
 * @param horaPeticion The horaPeticion to set.
 */
public void setHoraPeticion(String horaPeticion)
{
	this.horaPeticion = horaPeticion;
}
/**
 * @return Returns the nombreEstadoPeticion.
 */
public String getNombreEstadoPeticion()
{
	return nombreEstadoPeticion;
}
/**
 * @param nombreEstadoPeticion The nombreEstadoPeticion to set.
 */
public void setNombreEstadoPeticion(String nombreEstadoPeticion)
{
	this.nombreEstadoPeticion = nombreEstadoPeticion;
}
/**
 * @return Returns the nombreSolicitante.
 */
public String getNombreSolicitante()
{
	return nombreSolicitante;
}
/**
 * @param nombreSolicitante The nombreSolicitante to set.
 */
public void setNombreSolicitante(String nombreSolicitante)
{
	this.nombreSolicitante = nombreSolicitante;
}
/**
 * @return Returns the ocultarCabezote.
 */
public int getOcultarCabezote()
{
	return ocultarCabezote;
}
/**
 * @param ocultarCabezote The ocultarCabezote to set.
 */
public void setOcultarCabezote(int ocultarCabezote)
{
	this.ocultarCabezote = ocultarCabezote;
}
/**
 * @return Returns the estadoSolicitud.
 */
public int getEstadoSolicitud()
{
	return estadoSolicitud;
}
/**
 * @param estadoSolicitud The estadoSolicitud to set.
 */
public void setEstadoSolicitud(int estadoSolicitud)
{
	this.estadoSolicitud = estadoSolicitud;
}
	/**
	 * @return Returns the cargarFechaHoraPreanestesia.
	 */
	public boolean isCargarFechaHoraPreanestesia()
	{
		return cargarFechaHoraPreanestesia;
	}
	/**
	 * @param cargarFechaHoraPreanestesia The cargarFechaHoraPreanestesia to set.
	 */
	public void setCargarFechaHoraPreanestesia(
			boolean cargarFechaHoraPreanestesia)
	{
		this.cargarFechaHoraPreanestesia = cargarFechaHoraPreanestesia;
	}
	
	/**
	 * @return Returns the mapaTipoExamenesLab.
	 */
	public HashMap getMapaTipoExamenesLab()
	{
		return mapaTipoExamenesLab;
	}
	/**
	 * @param mapaTipoExamenesLab The mapaTipoExamenesLab to set.
	 */
	public void setMapaTipoExamenesLab(HashMap mapaTipoExamenesLab)
	{
		this.mapaTipoExamenesLab = mapaTipoExamenesLab;
	}

	/**
	 * @return Returna la propiedad del mapa mapaTipoExamenesLab.
	 */
	public Object getMapaTipoExamenesLab(String key)
	{
		return mapaTipoExamenesLab.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapaTipoExamenesLab
	 */
	public void setMapaTipoExamenesLab(String key, Object value)
	{
		this.mapaTipoExamenesLab.put(key, value);
	}

	/**
	 * @return Retorna tipoAnestesia.
	 */
	public int getTipoAnestesia() {
		return tipoAnestesia;
	}

	/**
	 * @param Asigna tipoAnestesia.
	 */
	public void setTipoAnestesia(int tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}

	public void initInfoMapa()
	{
		if(this.infoHojaAnestesiaMap ==  null)
			this.infoHojaAnestesiaMap = new HashMap();
	}	
	
	/**
	 * @return the mostrarMenuHojaAnestesia
	 */
	public String getMostrarMenuHojaAnestesia() {
		initInfoMapa();
		if(this.infoHojaAnestesiaMap.containsKey("mostrarMenuHojaAnestesia"))
			return infoHojaAnestesiaMap.get("mostrarMenuHojaAnestesia").toString();
		else
			return ConstantesBD.acronimoNo;		
	}
	
	/**
	 * @param mostrarMenuHojaAnestesia the mostrarMenuHojaAnestesia to set
	 */
	public void setMostrarMenuHojaAnestesia(String mostrarMenuHojaAnestesia) {
		initInfoMapa();
		this.infoHojaAnestesiaMap.put("mostrarMenuHojaAnestesia",mostrarMenuHojaAnestesia);
	}

	/**
	 * @param mostrarMenuHojaAnestesia the mostrarMenuHojaAnestesia to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		initInfoMapa();
		this.infoHojaAnestesiaMap.put("numeroSolicitud",numeroSolicitud);
	}
	
	/**
	 * @return the mostrarMenuHojaAnestesia
	 */
	public String getNumeroSolicitud() {
		initInfoMapa();
		if(this.infoHojaAnestesiaMap.containsKey("numeroSolicitud"))
			return infoHojaAnestesiaMap.get("numeroSolicitud").toString();
		else
			return ConstantesBD.codigoNuncaValido+"";		
	}

	/**
	 * @return the infoHojaAnestesiaMap
	 */
	public HashMap getInfoHojaAnestesiaMap() {
		return infoHojaAnestesiaMap;
	}

	/**
	 * @param infoHojaAnestesiaMap the infoHojaAnestesiaMap to set
	 */
	public void setInfoHojaAnestesiaMap(HashMap infoHojaAnestesiaMap) {
		this.infoHojaAnestesiaMap = infoHojaAnestesiaMap;
	}

}
