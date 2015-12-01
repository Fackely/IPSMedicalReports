/*
* @(#)AgendaForm.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.actionform.agenda;

import java.io.Serializable;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.agenda.AgendaAction;


import util.ConstantesBD;
import util.UtilidadFecha;

/**
* ActionForm, tiene la función de bean dentro de la forma, que contiene todos los datos necesarios
* para generación/cancelación de una agenda de consultas. Adicionalmente hace el manejo de limpieza
* de la la forma y de validación de datos de entrada.
*
* @version 1.0, Sep 17, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class AgendaForm extends ValidatorForm implements Serializable
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/** Contenedor de datos de un ítem de agenda de consulta */
	private HashMap idb_item;

	/** Item del código de la agenda de consulta */
	private int ii_codigoAgenda;

	/** Código del médico asignado a la agenda */
	private int ii_codigoMedico;

	/** Código del consultorio asignado al ítem de la agenda de consulta */
	private int ii_consultorio;

	/** Código del día de la semana utilizado para generar/cancelar la agenda
	* 1 - Lunes
	* 2 - Martes
	* 3 - Miércoles
	* 4 - Jueves
	* 5 - Viernes
	* 6 - Sábado
	* 7 - Domingo
	*/
	private int ii_diaSemana;

	/** Código de la unidad de consulta asignada a la agenda */
	private int ii_unidadConsulta;

	/** Identificación del usuario que generó/canceló la agenda */
	private String is_codigoUsuario;

	/** Estado actual del flujo */
	private String estado;

	/** Fecha de finalización de la generación/cancelación de la agenda */
	private String is_fechaFin;

	/** Fecha de inicio de la generación/cancelación de la agenda */
	private String is_fechaInicio;

	/** Hora de finalización del ítem de agenda a consultar/eliminar */
	private String is_horaFin;

	/** Hora de inicio del ítem de agenda a consultar/eliminar */
	private String is_horaInicio;

	/** Listado de ítems de agenda de consultas para listar */
	private Vector iv_agendas = new Vector(0);
	
	private Vector citasAEliminar;
	
	private String Telefono;
	
	private String parcial;
	
	private String estadoAnterior="";
	
	private HashMap citasNoAtendidas;
	
	/**
	 * Variable usada para almacenar el número de registros cancelados
	 */
	private String cancelados = "";
	
	/**
	 * Se utiliza para saber si se entró a generar agenda o eliminar agenda
	 */
	private String estadoInicial="";
	
	/**
	 * Codigo de la actividad de autorizacion
	 */
	private int codigoActividadAutorizacion = ConstantesBD.codigoNuncaValido;
	
	
	/**
	 * Boolean que indica si se tiene agenda 
	 * para una unidad de consulta específica en estado activo,
	 * y permitiendo mostrar el mensaje en la vista de 
	 * "No existe agenda para cancelar con los parametros dados"
	**/
	private boolean existeValoresUnidadConsulta;
	
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    
    
    /**
     * Mapa de Horarios Sin Consultorio 
     * */
    private HashMap horaAtencionConMap; 
    
    /**
     * Estado del Horario de Atencion Sin Consultorios
     * */
    private String estadoHorarioConsul;
    
    
    /** * Se selecciono el domingo para generar la agenda */
    private boolean diaDomingo; 
    
    

	/**
	* Obtiene el código del ítem de agenda a sobre la cual se reservará la cita
	* @return Código del ítem agenda de esta cita
	*/
	public int getCodigoAgenda()
	{
		return ii_codigoAgenda;
	}

	/**
	* Obtiene el código del consultorio de la agenda
	* @return Código del consulorio de esta agenda
	*/
	public int getCodigoConsultorio()
	{
		return ii_consultorio;
	}

	/**
	* Obtiene el código del día de la semana de la agenda a generar/cancelar
	* @return Código del día de la semana de esta agenda
	*/
	public int getCodigoDiaSemana()
	{
		/*  MT 1233  */
		if(this.isDiaDomingo()){
			this.ii_diaSemana = 7;
		}
		
		return ii_diaSemana;
	}

	/**
	* Obtiene el código del médico de la agenda
	* @return Código del médico asignado a esta agenda
	*/
	public int getCodigoMedico()
	{
		return ii_codigoMedico;
	}

	/**
	* Obtiene la unidad de consulta de la agenda
	* @return Código de la unidad de consulta de esta agenda
	*/
	public int getCodigoUnidadConsulta()
	{
		return ii_unidadConsulta;
	}

	/**
	* Obtiene el código del usuario que generó/canceló la agenda o reservó la cita
	* @return Identificación del usuario que generó/canceló esta agenda o reserva la cita
	*/
	public String getCodigoUsuario()
	{
		return is_codigoUsuario;
	}

	/**
	* Retorna el estado actual del flujo
	* @return Estado actual del flujo
	*/
	public String getEstado()
	{
		return estado;
	}

	/**
	* Obtiene la fecha de finalización de la generación/cancelación de la agenda
	* @return Fecha de consulta de esta agenda
	*/
	public String getFechaFin()
	{
		return is_fechaFin;
	}

	/**
	* Obtiene la fecha de inicio de la generación/cancelación de la agenda
	* @return Fecha de consulta de esta agenda
	*/
	public String getFechaInicio()
	{
		return is_fechaInicio;
	}

	/**
	* Obtiene la hora de finalización del ítem de agenda a eliminar/consultar
	* @return Hora de finalización del ítem de agenda a eliminar/consultar
	*/
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/**
	* Obtiene la hora de inicio del ítem de agenda a eliminar/consultar
	* @return Hora de inicio del ítem de agenda a eliminar/consultar
	*/
	public String getHoraInicio()
	{
		return is_horaInicio;
	}

	/**
	* Obtiene la lista de ítems de agenda de consulta a listar
	* @return <code>Collection</code> de ítems de agenda de consulta
	*/
	public Collection getItems()
	{
		return iv_agendas;
	}

	public Collection getItemsAEliminar()
	{
		return this.citasAEliminar;
	}

	/**
	 * Listado de centros de atencion y unidades de agenda autorizados para el usuario
	 */
	private HashMap unidadAgendaMap;
	
	/**
	 * Centros de atencion validos para el usuario
	 */
	private HashMap centrosAtencionAutorizados;
	
	/**
	 * Unidades de agenda validas para el usuario
	 */
	private HashMap unidadesAgendaAutorizadas;
	
	/**
	* Obtiene el contenedor de datos de un ítem de agenda de consulta
	* @return <code>HashMap</code> con los datos de un ítem de agenda de consulta
	*/
	public HashMap getItem()
	{
		return idb_item;
	}

	/**
	* Obtiener el número de ítems de agenda de consulta a listar
	* @return Número de ítems de agenda de consulta a listar
	*/
	public Integer getSize()
	{
		return new Integer(size() );
	}

	public Integer getSizeEliminar()
	{
		return new Integer(sizeEliminar() );
	}
	
	

	/** Limpia la forma */
	public void reset()
	{
		idb_item			= null;

		ii_codigoAgenda		= -1;
		ii_codigoMedico		= -1;
		ii_consultorio		= -1;
		ii_diaSemana		= -1;
		ii_unidadConsulta	= -1;
		centroAtencion		= -1;

		is_codigoUsuario			= "";
		estado						= "";
		estadoHorarioConsul 		= "";
		is_fechaFin					= "";
		is_fechaInicio				= "";
		is_horaFin					= "";
		is_horaInicio				= "";

		iv_agendas = new Vector();
		existeValoresUnidadConsulta=false;
		estadoInicial	="";
		this.cancelados = "";
		this.horaAtencionConMap = new HashMap();
		this.citasNoAtendidas=new HashMap();
		this.citasNoAtendidas.put("numRegistros", "0");
		this.unidadAgendaMap=new HashMap();
		this.unidadAgendaMap.put("numRegistros", "0");
		this.centrosAtencionAutorizados=new HashMap();
		this.centrosAtencionAutorizados.put("numRegistros", "0");
		this.unidadesAgendaAutorizadas=new HashMap();
		this.unidadesAgendaAutorizadas.put("numRegistros", "0");
		this.codigoActividadAutorizacion = ConstantesBD.codigoNuncaValido;
		this.diaDomingo = false;
	}

	/**
	* Establece el código del ítem de agenda a la que pertence la cita
	* @param ai_codigoAgenda Código del item de agenda a la que pertence esta cita
	*/
	public void setCodigoAgenda(int ai_codigoAgenda)
	{
		ii_codigoAgenda = ai_codigoAgenda;
	}

	/**
	* Establece el código del consultorio de la agenda
	* @param ai_consultorio Código del consultorio a asignar a esta agenda
	*/
	public void setCodigoConsultorio(int ai_consultorio)
	{
		ii_consultorio = ai_consultorio;
	}

	/**
	* Establece el código del día de la semana de la agenda a generar/cancelar
	* @param ai_diaSemana Código del día de la semana a asignar a esta agenda
	*/
	public void setCodigoDiaSemana(int ai_diaSemana)
	{
		ii_diaSemana = ai_diaSemana;
	}

	/**
	* Establece el código del médico de la agenda
	* @param as_codigoMedico Código del médico a asignar a esta agenda
	*/
	public void setCodigoMedico(int ai_codigoMedico)
	{
		ii_codigoMedico = ai_codigoMedico;
	}

	/**
	* Establece la unidad de consulta de la agenda
	* @param ai_unidadConsulta Código de la unidad de consulta a asignar a esta agenda
	*/
	public void setCodigoUnidadConsulta(int ai_unidadConsulta)
	{
		ii_unidadConsulta = ai_unidadConsulta;
	}

	/**
	* Establece el código del usuario que generó la agenda
	* @param as_codigoUsuario Identificación del usuario que generó esta agenda
	*/
	public void setCodigoUsuario(String as_codigoUsuario)
	{
		if(as_codigoUsuario != null)
			is_codigoUsuario = as_codigoUsuario.trim();
	}

	/** Asigna el estado actual del flujo */
	public void setEstado(String as_estado)
	{
		if(as_estado != null)
			estado = as_estado.trim();
	}

	/**
	* Establece la fecha de finalización de la generación/cancelación de la agenda
	* @param as_fechaFin Fecha de finalización de la generación/cancelación de esta agenda
	*/
	public void setFechaFin(String as_fechaFin)
	{
		if(as_fechaFin != null)
			is_fechaFin = as_fechaFin.trim();
	}

	/**
	* Establece la fecha de inicio de la generación/cancelación de la agenda
	* @param as_fechaInicio Fecha de inicio de la generación/cancelación de esta agenda
	*/
	public void setFechaInicio(String as_fechaInicio)
	{
		if(as_fechaInicio != null)
			is_fechaInicio = as_fechaInicio.trim();
	}

	/**
	* Establece la hora de finalización del ítem de agenda a eliminar/consultar
	* @param as_horaFin Hora de finalización del ítem de agenda a eliminar/consultar
	*/
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null)
			is_horaFin = as_horaFin.trim();
	}

	/**
	* Establece la hora de inicio del ítem de agenda a eliminar
	* @param as_horaInicio Hora de inicio del ítem de agenda a eliminar
	*/
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null)
			is_horaInicio = as_horaInicio.trim();
	}

	/**
	* Establece el contenedor de datos de un ítem de agenda de consulta
	* @param adb_item <code>HashMap<code> contenedor de datos de un ítem de agenda de consulta
	*/
	public void setItem(HashMap adb_item)
	{
		idb_item = adb_item;
	}

	/**
	* Establece el conjuto de ítems de agenda de consulta a listar
	* @param ac_agendas <code>Collection<code> de ítems de agenda de consulta a listar
	*/
	public void setItems(Collection ac_agendas)
	{
		iv_agendas = (ac_agendas != null) ? new Vector(ac_agendas) : new Vector(0);
	}

	/**
	 * 
	 * @param citasAEliminar
	 */
	public void setItemsAEliminar(Collection citasAEliminar)
	{
		this.citasAEliminar = (citasAEliminar != null) ? new Vector(citasAEliminar) : new Vector(0);
	}

	/**
	* Obtener el número de ítems de agenda de consulta a listar
	* @return Número de ítems de agenda de consulta a listar
	*/
	public int size()
	{
		return iv_agendas == null ? 0 : iv_agendas.size();
	}

	public int sizeEliminar()
	{
		return citasAEliminar == null ? 0 : citasAEliminar.size();
	}


	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validación encontrados. Si no se
	* encontraron errores de validación, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors;
		boolean fechaInicialEsDiaHoy=false;

		errors = null;
		if(
			estado.equals("generarAgenda")	||
			estado.equals("cancelarAgenda")	||
			estado.equals("listarAgenda")
		)
		{
			errors = new ActionErrors();

			/* Realizar la validación especificada en el archivo validation.xml */
			errors.add(super.validate(mapping, request) );
			/* Validar esto sí y solo si no se han encontrado errores */
			if(errors.isEmpty() )
			{
				boolean fechaInicialValida = true, fechaFinalValida = true;
				
				if(!UtilidadFecha.validarFecha(is_fechaInicio))
				{
					errors.add(
							"fechaInicio",
							new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicial")
						);
					fechaInicialValida = false;
				}
				
				if(!UtilidadFecha.validarFecha(is_fechaFin))
				{
					errors.add(
							"fechaInicio",
							new ActionMessage("errors.formatoFechaInvalido", "Fecha Final")
						);
					fechaFinalValida = false;
				}
				
				

				if(fechaInicialValida&& fechaFinalValida)
				{
					//Lo primero que vamos a revisar es si la fecha
					//inicial corresponde al dia de hoy o anterior
					String fechaHoy=UtilidadFecha.getFechaActual();
					if(fechaHoy.equals(is_fechaInicio))
						fechaInicialEsDiaHoy=true;
					
					/*
					 * Esto es un error
					 * Solaqmente se valida la fecha inicial contra la fecha de hoy
					 * para saber si fechaInicialEsDiaHoy=true
					 */
					/*if (ld_fechaFin.equals(ld_fechaInicio))
					{
						fechaInicialEsDiaHoy=true;
					}*/
					if(!estado.equals("listarAgenda") )
					{
						
						
						
						/* La fecha inicial debe ser mayor o igual a la fecha actual */
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(is_fechaInicio, fechaHoy))
							errors.add(
									"fechaInicio",
									new ActionMessage(
										"errors.fechaAnteriorIgualActual",
										"inicial",
										"actual"
									)
								);
					}

					/* La fecha de inicio debe ser anterior a la fecha de finalización */
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(is_fechaInicio, is_fechaFin) )
						errors.add(
							"fechaFin",
							new ActionMessage(
								"errors.fechaAnteriorIgualActual",
								"final",
								"inicial"
							)
						);
				}
			}
			
			//-----Se valida que no se genere una agenda para un rango de fechas mayor de tres meses-----//
			if (errors.isEmpty())
				{
				int nroMeses = UtilidadFecha.numeroMesesEntreFechas(is_fechaInicio, is_fechaFin,true);
				if (nroMeses > 3)
					{
					if (estado.equals("generarAgenda"))
						errors.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "GENERAR AGENDA"));
					else if (estado.equals("cancelarAgenda"))
						errors.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "CANCELAR AGENDA"));
					}
				}
			
			/* Validar esto sí y solo si no se han encontrado errores */
			if(errors.isEmpty() && !is_horaInicio.equals("") && !is_horaFin.equals("") )
			{
				boolean horaInicialValida = true, horaFinalValida = true;
				
				if(!UtilidadFecha.validacionHora(is_horaInicio).puedoSeguir)
				{
					errors.add(
							"horaInicio",
							new ActionMessage("errors.formatoHoraInvalido", "de inicio")
						);
					horaInicialValida = false;
				}
				
				if(!UtilidadFecha.validacionHora(is_horaFin).puedoSeguir)
				{
					errors.add(
							"horaFin",
							new ActionMessage("errors.formatoHoraInvalido", "de finalización")
						);
					horaFinalValida = false;
				}
				
				

				/* La hora de inicio debe ser anterior a la hora de finalización */
				if(horaInicialValida && horaFinalValida)
				{
					if(!UtilidadFecha.compararFechas(is_fechaFin, is_horaFin, is_fechaInicio, is_horaInicio).isTrue())
					{
						errors.add("horaFin",new ActionMessage("errors.fechaHoraAnteriorIgualActual", "final", "inicial"));
					}
				}
				
				//Para el caso de generar agenda, si el día es el mismo
				//la hora inicial debe ser superior que la hora
				if (estado.equals("cancelarAgenda")&&fechaInicialEsDiaHoy)
				{
				
					String horaActual=UtilidadFecha.getHoraActual();
					if(UtilidadFecha.esHoraMenorQueOtraReferencia(is_horaInicio, horaActual))
					{
						//No puedo cancelar una agenda que ya pasó
						errors.add("cancelandoRangoHorasYaPaso", new ActionMessage("error.agenda.horaInicial"));
					}
					
				}
			}

			if (!is_horaInicio.equals("") && is_horaFin.equals(""))
			{
				errors.add("horaFinalRequeridaSiExisteHoraInicial", new ActionMessage("errors.required", "Si se llena la hora inicial la hora final"));
			}
			
			/* Si no hay errores limpiar el contenedor */
			if(errors.isEmpty() )
				errors = null;
		}

		return errors;
	}
	/**
	 * @return
	 */
	public String getTelefono()
	{
		return Telefono;
	}

	/**
	 * @param string
	 */
	public void setTelefono(String string)
	{
		Telefono= string;
	}


	/**
	 * @return
	 */
	public String getParcial()
	{
		return parcial;
	}

	/**
	 * @param string
	 */
	public void setParcial(String string)
	{
		parcial= string;
	}

	/**
	 * @return
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}

	/**
	 * @param string
	 */
	public void setEstadoAnterior(String string)
	{
		estadoAnterior= string;
	}

	/**
	 * Retorna Boolean que indica si se tiene agenda 
	 * para una unidad de consulta específica en estado activo,
	 * y permitiendo mostrar el mensaje en la vista de 
	 * "No existe agenda para cancelar con los parametros dados"
	**/
	public boolean getExisteValoresUnidadConsulta() {
		return existeValoresUnidadConsulta;
	}

	/**
	 * Asigna  Boolean que indica si se tiene agenda 
	 * para una unidad de consulta específica en estado activo,
	 * y permitiendo mostrar el mensaje en la vista de 
	 * "No existe agenda para cancelar con los parametros dados"
	**/
	public void setExisteValoresUnidadConsulta(boolean b) {
		existeValoresUnidadConsulta = b;
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	public String getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	/**
	 * @return the cancelados
	 */
	public String getCancelados() {
		return cancelados;
	}

	/**
	 * @param cancelados the cancelados to set
	 */
	public void setCancelados(String cancelados) {
		this.cancelados = cancelados;
	}

	/**
	 * @return the horaAtencionConMap
	 */
	public HashMap getHoraAtencionConMap() {
		return horaAtencionConMap;
	}

	/**
	 * @param horaAtencionConMap the horaAtencionConMap to set
	 */
	public void setHoraAtencionConMap(HashMap horaAtencionConMap) {
		this.horaAtencionConMap = horaAtencionConMap;
	}
	
	/**
	 * @return the horaAtencionConMap
	 */
	public Object getHoraAtencionConMap(String key) {
		return horaAtencionConMap.get(key);
	}

	/**
	 * @param horaAtencionConMap the horaAtencionConMap to set
	 */
	public void setHoraAtencionConMap(String key, Object value) {
		this.horaAtencionConMap.put(key, value);
	}

	/**
	 * @return the estadoHorarioConsul
	 */
	public String getEstadoHorarioConsul() {
		return estadoHorarioConsul;
	}

	/**
	 * @param estadoHorarioConsul the estadoHorarioConsul to set
	 */
	public void setEstadoHorarioConsul(String estadoHorarioConsul) {
		this.estadoHorarioConsul = estadoHorarioConsul;
	}

	public HashMap getCitasNoAtendidas() {
		return citasNoAtendidas;
	}

	public void setCitasNoAtendidas(HashMap citasNoAtendidas) {
		this.citasNoAtendidas = citasNoAtendidas;
	}
	public Object getCitasNoAtendidas(String key) {
		return citasNoAtendidas.get(key);
	}

	public void setCitasNoAtendidas(String key,Object value) {
		this.citasNoAtendidas.put(key, value);
	}

	/**
	 * @return the centrosAtencionMap
	 */
	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setUnidadAgendaMap(HashMap centrosAtencionMap) {
		this.unidadAgendaMap = centrosAtencionMap;
	}

	/**
	 * @return the centrosAtencionMap
	 */
	public Object getUnidadAgendaMap(String llave) {
		return unidadAgendaMap.get(llave);
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setUnidadAgendaMap(String llave, Object obj) {
		this.unidadAgendaMap.put(llave, obj);
	}

	/**
	 * @return the centrosAtencionAutorizados
	 */
	public HashMap getCentrosAtencionAutorizados() {
		return centrosAtencionAutorizados;
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(HashMap centrosAtencionAutorizados) {
		this.centrosAtencionAutorizados = centrosAtencionAutorizados;
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public Object getCentrosAtencionAutorizados(String llave) {
		return centrosAtencionAutorizados.get(llave);
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(String llave, Object obj) {
		this.centrosAtencionAutorizados.put(llave, obj);
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public HashMap getUnidadesAgendaAutorizadas() {
		return unidadesAgendaAutorizadas;
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(HashMap unidadesAgendaAutorizadas) {
		this.unidadesAgendaAutorizadas = unidadesAgendaAutorizadas;
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public Object getUnidadesAgendaAutorizadas(String llave) {
		return unidadesAgendaAutorizadas.get(llave);
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(String llave, Object obj) {
		this.unidadesAgendaAutorizadas.put(llave, obj);
	}

	/**
	 * @return the codigoActividadAutorizacion
	 */
	public int getCodigoActividadAutorizacion() {
		return codigoActividadAutorizacion;
	}

	/**
	 * @param codigoActividadAutorizacion the codigoActividadAutorizacion to set
	 */
	public void setCodigoActividadAutorizacion(int codigoActividadAutorizacion) {
		this.codigoActividadAutorizacion = codigoActividadAutorizacion;
	}

	/**
	 * @return valor de diaDomingo
	 */
	public boolean isDiaDomingo() {
		return diaDomingo;
	}

	/**
	 * @param diaDomingo el diaDomingo para asignar
	 */
	public void setDiaDomingo(boolean diaDomingo) {
		this.diaDomingo = diaDomingo;
	}
	
	
}