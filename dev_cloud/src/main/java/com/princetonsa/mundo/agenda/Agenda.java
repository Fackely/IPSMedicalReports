/*
* @(#)Agenda.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.mundo.agenda;

import java.io.Serializable;


import java.sql.Connection;

import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import java.util.HashMap;

import util.InfoDatosInt;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.dao.AgendaDao;
import com.princetonsa.dao.DaoFactory;

/**
* Esta clase encapsula los atributos y la funcionalidad de la Agenda
*
* @version 1.0, Sep 12, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class Agenda implements Serializable
{
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/** El DAO usado por el objeto <code>Agenda</code> para acceder a la fuente de datos. */
	private static AgendaDao agendaDao = null;

	/** Código del consultorio asignado asignado a la agenda */
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

	/** Identificación del médico asignado a la agenda */
	private int ii_codigoMedico;

	/** Código de la unidad de consulta asignada a la agenda */
	private int ii_unidadConsulta;

	/** Identificación del usuario que generó/canceló la agenda */
	private String is_codigoUsuario;

	/** Fecha de finalización de la generación/cancelación de la agenda */
	private String is_fechaFin;

	/** Fecha de inicio de la generación de la agenda */
	private String is_fechaInicio;

	/** Hora de finalización del ítem de agenda a consultar/eliminar */
	private String is_horaFin;

	/** Hora de inicio del ítem de agenda a consultar/eliminar */
	private String is_horaInicio;

	private String parcial;
	
	private String descripcion;
	
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    
    /**
     * Centros de atencion si se selecciona la opcion todos
     */
    private String centrosAtencion;
    
    /**
     * Unidades de agenda si se selecciona la opcion todos
     */
    private String unidadesAgenda;
    
	/**
	* Construye un item de agenda sin ningún atributo especificado.
	*/
	public Agenda()
	{
		clean();
		init();
	}

	/** Inicializa los atributos del objeto a valores vacíos (inválidos) */
	public void clean()
	{
		ii_codigoMedico		= -1;
		ii_consultorio		= -1;
		ii_diaSemana		= -1;
		ii_unidadConsulta	= -1;
		centroAtencion = -1;

		is_codigoUsuario	= "";
		is_fechaFin			= "";
		is_fechaInicio		= "";
		is_horaFin			= "";
		is_horaInicio		= "";
		parcial="";
		descripcion = "";
		
	}

	/**
	* Elimina un conjunto de ítems de agenda, reutilizando una conexión existente
	* @param ac_con Conexión abierta con una fuente de datos
	* @return Número de items de agenda eliminados
	*/
	public HashMap cancelarAgenda(Connection ac_con)throws Exception
	{
		HashMap respuesta = new HashMap();
		respuesta.put("cancelados","-1");
		respuesta.put("numRegistros","0");
		if(!is_fechaInicio.equals("") && !is_fechaFin.equals("") )
		{
			return
				agendaDao.cancelarAgenda(
					ac_con,
					is_fechaInicio,
					is_fechaFin,
					is_horaInicio,
					is_horaFin,
					ii_unidadConsulta,
					ii_consultorio,
					ii_diaSemana,
					ii_codigoMedico,
					centroAtencion,
					centrosAtencion,
					unidadesAgenda
				);
		}
		else
			return respuesta;
	}

	/**
	* Obtiene los datos de un ítem de agenda de consulta
	* @param ac_con		Conexión abierta con una fuente de datos
	* @param ai_codigo	Código único del ítem de agenda a cunsultas
	* @return Contedor de datos del ítem de agenda especificado
	*/
	public static HashMap detalleAgenda(
		Connection ac_con,
		int ai_codigo
	)throws Exception
	{
		return
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getAgendaDao().detalleAgenda(
				ac_con, ai_codigo
			);
	}

	/**
	* Genera un agenda en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con Conexión abierta con una fuente de datos
	* @return Número de items de agenda generados
	*/
	public int generarAgenda(Connection ac_con, HashMap horarioConsultoriosMap)throws Exception
	{
		if(!is_fechaInicio.equals("") && !is_fechaFin.equals("") && !is_codigoUsuario.equals("") )
		{			
			InfoDatosInt resultado=
				agendaDao.generarAgenda(
					ac_con,
					is_fechaInicio,
					is_fechaFin,
					ii_unidadConsulta,
					ii_consultorio,
					ii_diaSemana,
					ii_codigoMedico,
					is_codigoUsuario,
					centroAtencion,
					horarioConsultoriosMap,
					centrosAtencion,
					unidadesAgenda
				);
				setParcial(resultado.getNombre());
				setDescripcion(resultado.getDescripcion());
				return resultado.getCodigo();				
		}
		else
			return -1;
	}

	
	/**
	 * Consulta los Horarios de Atencion que no poseen Consultorios
	 * @param Connection con	 
	 * */
	public HashMap getHorariosAtencionSinConsultorios(Connection ac_con)
	{		
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		if(!is_fechaInicio.equals("") && !is_fechaFin.equals("") && !is_codigoUsuario.equals("") && ii_consultorio == -1)
		{			
			mapa= agendaDao.getHorariosAtencionSinConsultorios(
							ac_con,
							is_fechaInicio,
							is_fechaFin,
							ii_unidadConsulta,					
							ii_diaSemana,
							ii_codigoMedico,					
							centroAtencion,
							unidadesAgenda,
							centrosAtencion
						);
			
			for(int i = 0; i < Integer.parseInt(mapa.get("numRegistros").toString()); i++)
			{
				mapa.put("mapaconsultorio_"+i,UtilidadesConsultaExterna.consultarConsultoriosLibresHorarioAtencion(
						ac_con,
						Integer.parseInt(mapa.get("centro_atencion_"+i).toString()),
						Integer.parseInt(mapa.get("dia_"+i).toString()),
						mapa.get("hora_inicio_"+i).toString(),
						mapa.get("hora_fin_"+i).toString()));				
			}
		}
		
		return mapa;
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
	* @return Código del día de la semana de este agenda
	*/
	public int getCodigoDiaSemana()
	{
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
	* Obtiene el código del usuario que generó/canceló la agenda
	* @return Identificación del usuario que generó/canceló esta agenda
	*/
	public String getCodigoUsuario()
	{
		return is_codigoUsuario;
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

	/** Inicializa el acceso a bases de datos de este objeto */
	public void init()
	{
		/* Obtengo el DAO que encapsula las operaciones de BD de este objeto */
		if(agendaDao == null)
			agendaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getAgendaDao();
	}

	/**
	* Lista todos los ítems de agenda de consulta solicitados
	* @param ac_con		Conexión abierta con una fuente de datos
	 * @param institucion @todo
	* @return <code>Collection</code> de los ítems de agenda
	*/
	public Collection listarAgenda(Connection ac_con, int institucion)throws Exception
	{
		if(!is_fechaInicio.equals("") && !is_fechaFin.equals("") )
		{
			return
				agendaDao.listarAgenda(
					ac_con,
					AgendaDao.LISTAR_TODOS,
					-1,
					-1,
					is_fechaInicio,
					is_fechaFin,
					is_horaInicio,
					is_horaFin,
					ii_unidadConsulta,
					ii_consultorio,
					ii_diaSemana,
					ii_codigoMedico,
					institucion,
					centroAtencion,
					true, // En este caso no importa este parámetro, se le puede poner cualquier valor
					false,
					centrosAtencion,
					unidadesAgenda
				);
		}
		else
			return null;
	}

	/**
	* Lista todos los ítems de agenda de consulta solicitados con cupo diponible
	* @param ac_con				Conexión abierta con una fuente de datos
	 * @param ai_codigoPaciente	Código del paciente para el cual se reservará la cita
	 * @param institucion @todo
	 * @param esReserva @todo
	 * @para disponibles (solo listar los cupos disponibles)
	 * @return <code>Collection</code> de los ítems de agenda
	*/
	public Collection listarAgendaDisponible(
		Connection	ac_con,
		int			ai_codigoPaciente,
		int			ai_sexoPaciente, int institucion, boolean esReserva, boolean disponibles
	)throws Exception
	{
		return
			agendaDao.listarAgenda(
				ac_con,
				AgendaDao.LISTAR_CUPOS_DISPONIBLES,
				ai_codigoPaciente,
				ai_sexoPaciente,
				is_fechaInicio,
				is_fechaFin,
				is_horaInicio,
				is_horaFin,
				ii_unidadConsulta,
				ii_consultorio,
				ii_diaSemana,
				ii_codigoMedico,
				institucion,
				centroAtencion,
				esReserva,
				disponibles,
				centrosAtencion,
				unidadesAgenda
			);
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

	/**
	* Establece la fecha de finalización de la generación/cancelación de la agenda
	* @param as_fechaFin Fecha de finalización de la generación/cancelación de esta agenda
	*/
	public void setFechaFin(String as_fechaFin)
	{
		/* El formato de fecha que se espera es dd/MM/yyyy */
		if(as_fechaFin != null && validarFecha(as_fechaFin = as_fechaFin.trim(), false) )
			is_fechaFin = as_fechaFin;
	}

	/**
	* Establece la fecha de inicio de la generación/cancelación de la agenda
	* @param as_fechaInicio Fecha de inicio de la generación/cancelación de esta agenda
	*/
	public void setFechaInicio(String as_fechaInicio)
	{
		/* El formato de fecha que se espera es dd/MM/yyyy */
		if(as_fechaInicio != null && validarFecha(as_fechaInicio = as_fechaInicio.trim(), true) )
			is_fechaInicio = as_fechaInicio;
	}

	/**
	* Establece la hora de finalización del ítem de agenda a eliminar/consultar
	* @param as_horaFin Hora de finalización del ítem de agenda a eliminar/consultar
	*/
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null && validarHora(as_horaFin = as_horaFin.trim(), false) )
			is_horaFin = as_horaFin;
	}

	/**
	* Establece la hora de inicio del ítem de agenda a eliminar
	* @param as_horaInicio Hora de inicio del ítem de agenda a eliminar
	*/
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null && validarHora(as_horaInicio = as_horaInicio.trim(), true) )
			is_horaInicio = as_horaInicio;
	}

	/**
	* Valida si cadena corresponde a un formato de fecha u hora válido
	* @param as_fecha			Fecha/Hora a validar
	* @param ab_esFechaInicio	Indica si la fecha a validar es una fecha de inicio o de
	*							finalización
	* @return true si la hora es válida, false de lo contrario
	*/
	private boolean validarFecha(String as_fecha, boolean ab_esFechaInicio)
	{
		boolean				lb_resp;
		Date				ld_d;
		SimpleDateFormat	lsdf_sdf;

		try
		{
			/* Iniciar variables */
			lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy");

			/* Exije una interpretación estricta del formato de fecha/hora esperado */
			lsdf_sdf.setLenient(false);

			/* Interpretar la fecha/hora a validar */
			ld_d = lsdf_sdf.parse(as_fecha);

			/* Validar la hora */
			if(ab_esFechaInicio)
			{
				/* La hora de inicio debe ser menor que la hora de finalización */
				if(is_fechaFin.equals("") )
					lb_resp = true;
				else
					lb_resp = !ld_d.after(lsdf_sdf.parse(is_fechaFin) );
			}
			else
			{
				/* La hora de finalización debe ser menor que la hora de inicio */
				if(is_fechaInicio.equals("") )
					lb_resp = true;
				else
					lb_resp = !lsdf_sdf.parse(is_fechaInicio).after(ld_d);
			}
		}
		catch(Exception le_e)
		{
			lb_resp = false;
		}

		return lb_resp;
	}

	/**
	* Valida si la hora a asignar, ya sea hora de inicio o finalización es una hora válida
	* @param as_hora			Hora a validar
	* @param ab_esHoraInicio	Indica si la hora a validar es una hora de inicio o de finalización
	* @return true si la hora es válida, false de lo contrario
	*/
	private boolean validarHora(String as_hora, boolean ab_esHoraInicio)
	{
		boolean				lb_resp;
		Date				ld_d;
		SimpleDateFormat	lsdf_sdf;

		/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
		lsdf_sdf = new SimpleDateFormat("H:mm");

		/* Exije una interpretación estricta del formato de hora esperado */
		lsdf_sdf.setLenient(false);

		try
		{
			/* Interpretar la hora a validar */
			ld_d = lsdf_sdf.parse(as_hora);

			/* Validar la hora */
			if(ab_esHoraInicio)
			{
				/* La hora de inicio debe ser menor que la hora de finalización */
				if(is_horaFin.equals("") )
					lb_resp = true;
				else
					lb_resp = !ld_d.after(lsdf_sdf.parse(is_horaFin) );
			}
			else
			{
				/* La hora de finalización debe ser menor que la hora de inicio */
				if(is_horaInicio.equals("") )
					lb_resp = true;
				else
					lb_resp = !lsdf_sdf.parse(is_horaInicio).after(ld_d);
			}
		}
		catch(Exception lpe_e)
		{
			lb_resp = false;
		}

		return lb_resp;
	}
	
	public Collection listarCitasOcupadasAEliminar(Connection con)
	{
		if(!is_fechaInicio.equals("") && !is_fechaFin.equals("") )
				{
					return
						agendaDao.listarCitasOcupadasAEliminar(
							con,
							is_horaInicio,
							is_horaFin,
							is_fechaInicio,
							
							is_fechaFin,							
							ii_unidadConsulta,
							ii_consultorio,
							ii_diaSemana,
							ii_codigoMedico,
							centroAtencion,
							centrosAtencion,
							unidadesAgenda
						);
				}
				else
					return null;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap listarCitasNoAtendidasAEliminar(Connection con) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		if(!is_fechaInicio.equals("") && !is_fechaFin.equals("") )
		{
			mapa=agendaDao.listarCitasNoAtendidasAEliminar(con,is_horaInicio,is_horaFin,is_fechaInicio,is_fechaFin,ii_unidadConsulta,ii_consultorio,ii_diaSemana,ii_codigoMedico,centroAtencion,centrosAtencion,unidadesAgenda);
		}
		return mapa;
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

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the centrosAtencion
	 */
	public String getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(String centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the unidadesAgenda
	 */
	public String getUnidadesAgenda() {
		return unidadesAgenda;
	}

	/**
	 * @param unidadesAgenda the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(String unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}

	

}