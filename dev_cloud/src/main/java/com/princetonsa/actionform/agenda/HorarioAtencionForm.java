/*
* @(#)HorarioAtencionForm.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.actionform.agenda;

import java.io.Serializable;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.ConstantesBD;


/**
* ActionForm, tiene la función de bean dentro de la forma, que contiene todos
* los datos necesarios de un horario de atención. Adicionalmente hace el manejo
* de limpieza de la la forma y de validación de datos de entrada.
*
* @version 1.0, Sep 4, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class HorarioAtencionForm extends ValidatorForm implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;


	/** Identificador único del horario de atención en la forma */
	private int ii_codigo;

	/** Código del consultorio asignado el horario de atención en la forma */
	private int ii_consultorio;

	/** Código del día de la semana asignado al horario de atención en la forma
	* 1 - Lunes
	* 2 - Martes
	* 3 - Miércoles
	* 4 - Jueves
	* 5 - Viernes
	* 6 - Sábado
	* 7 - Domingo
	*/
	private int ii_diaSemana;
	
	/**
	 * HashMap del dias de la semana
	 * */
	private HashMap diasSemanaMap;

	/** Tiempo de duración (en minutos) de una consulta en este horario de atención en la forma */
	private int ii_duracionConsulta;

	/** Código del médico al cual será asignado el horario de atención en la forma */
	private int ii_medico;

	/** Máximo número de pacientes que se pueden atender por sesión de consulta en la foema */
	private int ii_pacientesSesion;

	/** Código de la unidad de consulta asignada al horario de atención en la forma */
	private int ii_unidadAgenda;

	/** Námero múximo de sesiones en este horario de atención */
	private int ii_totalSesiones;	

	/** Nombre del consultorio asignado al horario de atención en la forma */
	private String is_consultorio;

	/** Día de la semana asignado al horario de atención en la forma */
	private String is_diaSemana;

	/** 
	 * Estado actual del flujo 
	 * */
	private String is_estado;	

	/**
	* Hora de finalización del horario de atención para el día de la semana especificado en la forma
	*/
	private String is_horaFin;

	/** Hora de inicio del horario de atención para el día de la semana especificado en la forma */
	private String is_horaInicio;

	/** Nombre completo del médico asignado al horario de atención en la forma */
	private String is_nombreMedico;

	/** Unidad de consulta asignada al horario de atención en la forma */
	private String is_unidadConsulta;

	/** Listado de horarios de atención para listar */
	private Vector iv_horarios;
	
	private String temporal="false";

	/**
	 * Variables para la ordenacion del listado de horarios
	 */
	private String columna;
	private String ultimaPropiedad;

	/**
	 * Variables para la paginacion del listado de horarios
	 */
	private int offset;
	private String linkSiguiente;
	
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
    private int codigoCentroAtencion;
    /**
     * Nombre del Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario
     * se utiliza para la consulta  
     */
    private String nombreCentroAtencion;
    
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
	 * Mostrar la ruta JSP
	 */
	private boolean mostrarRutaJsp = false;
		
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);

	private String tipoAtencion;
	private String estadoAnt;

	private String parametroMultiplo;
	private String func;
	
	private int diaSemanaSel;
  	
	private int codConsultorio;
	
	private String xmlHorarioAtencion;
	
	private String horaIniHorarios;
	private String minIniHorarios;
	private String horaFinHorarios;
	private String minFinHorarios;
	
	public String getXmlHorarioAtencion() {
		return xmlHorarioAtencion;
	}

	public void setXmlHorarioAtencion(String xmlHorarioAtencion) {
		this.xmlHorarioAtencion = xmlHorarioAtencion;
	}

	public int getCodConsultorio() {
		return codConsultorio;
	}

	public void setCodConsultorio(int codConsultorio) {
		this.codConsultorio = codConsultorio;
	}

	private ArrayList<DtoHorarioAtencion> listaHorarios;
	
	public int getDiaSemanaSel() {
		return diaSemanaSel;
	}

	public void setDiaSemanaSel(int diaSemanaSel) {
		this.diaSemanaSel = diaSemanaSel;
	}

	public ArrayList<DtoHorarioAtencion> getListaHorarios() {
		return listaHorarios;
	}

	public void setListaHorarios(ArrayList<DtoHorarioAtencion> listaHorarios) {
		this.listaHorarios = listaHorarios;
	}

	public String getTipoAtencion() {
		return tipoAtencion;
	}

	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}
	
    //----------------------------------
	//Cambio Funcionalidades Consulta Ext --Anexo 810
	private ArrayList<HashMap<String,Object>> profesionaleSaludUniAgen;
	private String indexCodUniAgen;
	private HashMap<String,Object> datosUnidadAgenda;
	private String estadoEliminar;
	//----------------------------------
    
	private String estadoTransaccion; 
	
    //-------------------------------------------------------------------------
    
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return Returns the temporal.
	 */
	public String getTemporal()
	{
		return temporal;
	}

	/**
	 * @param temporal The temporal to set.
	 */
	public void setTemporal(String temporal)
	{
		this.temporal=temporal;
	}

	/**
	* Obtiene el código del horario de atención de la forma
	* @return Identificador único de este horario de atención de la forma
	*/
	public int getCodigo()
	{
		return ii_codigo;
	}

	/**
	* Obtiene el código del consultorio del horario de atención de la forma
	* @return Código del consulorio de este horario de atención de la forma
	*/
	public int getCodigoConsultorio()
	{
		return ii_consultorio;
	}

	/**
	* Obtiene el código del día de la semana del horario de atención de la forma
	* @return Código del día de la semana de este horario de atención de la forma
	*/
	public int getCodigoDiaSemana()
	{
		return ii_diaSemana;
	}

	/**
	* Obtiene el código del médico del horario de atencio de la forma
	* @return Código del médico asignado a este horario de atención de la forma
	*/
	public int getCodigoMedico()
	{
		return ii_medico;
	}

	
	/**
	* Obtiene el consultorio del horario de atención de la forma
	* @return Nombre del consultorio de este horario de atención
	*/
	public String getConsultorio()
	{
		return is_consultorio;
	}

	/**
	* Obtiene el día de la semana del horario de atención de la forma
	* @return Día de la semana al cual fue asignado este horario de atención
	*/
	public String getDiaSemana()
	{
		return is_diaSemana;
	}

	/**
	* Obtiene el tiempo de duración (en minutos) de una consulta en este horario de atención de la
	* forma
	* @return Tiempo en minutos que dura un sesión (consulta) en este horario de atención de la
	* forma
	*/
	public int getDuracionConsulta()
	{
		return ii_duracionConsulta;
	}

	/**
	* Retorna el estado actual del flujo
	* @return Estado actual del flujo
	*/
	public String getEstado()
	{
		return is_estado;
	}

	/**
	* Obtiene la hora de finalización del horario de atención para el día de la semana de la forma
	* @return Hora de finalización de este horario de atención de la forma
	*/
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/**
	* Obtiene la hora de inicio del horario de atención para el día de la semana de la forma
	* @return Hora de inicio de este horario de atención de la forma
	*/
	public String getHoraInicio()
	{
		return is_horaInicio;
	}

	/**
	* Obtiene la lista de horarios de atención a listar
	* @return <code>Collection</code> de horarios de atención
	*/
	public Collection getItems()
	{
		return iv_horarios;
	}

	/**
	* Obtiene el nombre del médico asignado al horario de atención de la forma
	* @return Nombre del médico asignado a este horario de atención medico
	*/
	public String getMedico()
	{
		return is_nombreMedico;
	}

	/**
	* Obtiene el número máximo de pacientes que se pueden atender por sesión o consulta de la forma
	* @return Número máximo de pacientes que se pueden atender por sesión o consulta en este
	* horario de atención de la forma
	*/
	public int getPacientesSesion()
	{
		return ii_pacientesSesion;
	}

	/**
	* Obtiener el número de horarios de atención a listar
	* @return <code>Integer</code> número de horarios de atención a listar
	*/
	public Integer getSize()
	{
		return new Integer(size() );
	}
	
	

	/**
	* Obtiene el número máximo de sesiones en este horario de antención de la forma
	* @return Número máximo de sesiones en este horario de atención de la forma
	*/
	public int getTotalSesiones()
	{
		if(
			ii_totalSesiones	< 1		&&
			ii_duracionConsulta	> 0		&&
			!is_horaInicio.equals("")	&&
			!is_horaFin.equals("")
		)
		{
			/* Calcular el número total de sesiones */
			SimpleDateFormat lsdf_sdf;

			/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
			lsdf_sdf = new SimpleDateFormat("H:mm");

			/* Exije una interpretación estricta del formato de hora esperado */
			lsdf_sdf.setLenient(false);

			try
			{
				setTotalSesiones(
					new Long(
						(
							(
								(
									lsdf_sdf.parse(is_horaFin).getTime() -
									lsdf_sdf.parse(is_horaInicio).getTime()
								) / 60000
							) //+ 1 MT 2516
						) / ii_duracionConsulta
					).intValue()
				);
			}
			catch(Exception lpe_e)
			{
			}
		}

		return ii_totalSesiones;
	}

	/**
	* Obtiene la unidad de consulta del horario de atención de la forma
	* @return Nombre de la unidad de consulta de este horario de atención forma
	*/
	public String getUnidadConsulta()
	{
		return is_unidadConsulta;
	}
	
	private int multiploMinGenCita; 
	
	private int tipoReporte;	
	
	private ArrayList<HashMap> listDiaSemana;
	
	private String horaIniAsignar;
	private String consultorioAsignar;
	private String diaAsignar;
	
	private String consultorioAsignado;
	private String profesionalAsignado;
	private HashMap<String,Object> diasSemanaBusqueda;
	
	/** Limpia la forma */
	public void reset()
	{		 
		ii_codigo			= -1;
		ii_consultorio		= -1;
		ii_diaSemana		= -1;
		ii_duracionConsulta	= 0;
		ii_medico			= -1;
		ii_pacientesSesion	= 1;
		ii_unidadAgenda	= -1;
		ii_totalSesiones	= 0;

		is_consultorio		= "";
		is_diaSemana		= "";
		is_estado			= "";
		is_horaFin			= "";
		is_horaInicio		= "";
		is_nombreMedico		= "";
		is_unidadConsulta	= "";
		this.nombreCentroAtencion = "";
		this.temporal="false";
		iv_horarios = new Vector();
		this.diasSemanaMap = new HashMap();
		this.columna = "";
		this.ultimaPropiedad = "";
		this.offset  = 0;
		this.linkSiguiente = "";
		this.unidadAgendaMap=new HashMap();
		this.centrosAtencionAutorizados=new HashMap();
		this.centrosAtencionAutorizados.put("numRegistros", "0");
		this.unidadesAgendaAutorizadas=new HashMap();
		this.unidadesAgendaAutorizadas.put("numRegistros", "0");
		
		//Cambio Funcionalidades Consulta Ext --Anexo 810
		this.profesionaleSaludUniAgen = new ArrayList<HashMap<String,Object>>();
		this.indexCodUniAgen = "";
		this.datosUnidadAgenda = new HashMap<String,Object>();
		this.estadoEliminar = ConstantesBD.acronimoNo;
		//-----------------------------------------------
		this.codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.estadoTransaccion = ConstantesBD.acronimoNo;
		this.tipoAtencion="";
		this.estadoAnt="";
		this.multiploMinGenCita=ConstantesBD.codigoNuncaValido;
		this.parametroMultiplo="";
//		this.func="";
		this.tipoReporte=0;
		this.listDiaSemana= new  ArrayList<HashMap>();
		this.listaHorarios= new ArrayList<DtoHorarioAtencion>();
		this.diaSemanaSel= ConstantesBD.codigoNuncaValido;
		this.codConsultorio= ConstantesBD.codigoNuncaValido;
		this.xmlHorarioAtencion="";
		this.horaIniHorarios="";
		this.horaFinHorarios="";
		this.minIniHorarios="";
		this.minFinHorarios="";
		this.horaIniAsignar="";
		this.consultorioAsignar="";
		this.diaAsignar="";
		
		this.consultorioAsignado = ConstantesBD.acronimoSi;
		this.profesionalAsignado = ConstantesBD.acronimoSi;
	}
	
	/** Limpia la forma */
	public void resetAsignar()
	{		 
		ii_codigo			= -1;
		ii_consultorio		= -1;
		ii_diaSemana		= -1;
		ii_duracionConsulta	= 0;
		ii_medico			= -1;
		ii_pacientesSesion	= 1;
		ii_unidadAgenda	= -1;
		ii_totalSesiones	= 0;

		is_consultorio		= "";
		is_diaSemana		= "";
		is_estado			= "";
		is_horaFin			= "";
		is_horaInicio		= "";
		is_nombreMedico		= "";
		is_unidadConsulta	= "";
		this.nombreCentroAtencion = "";
		this.temporal="false";
		iv_horarios = new Vector();
		this.diasSemanaMap = new HashMap();
		this.columna = "";
		this.ultimaPropiedad = "";
		this.offset  = 0;
		this.linkSiguiente = "";
		this.unidadAgendaMap=new HashMap();
		this.centrosAtencionAutorizados=new HashMap();
		this.centrosAtencionAutorizados.put("numRegistros", "0");
		this.unidadesAgendaAutorizadas=new HashMap();
		this.unidadesAgendaAutorizadas.put("numRegistros", "0");
		
		//Cambio Funcionalidades Consulta Ext --Anexo 810
		this.profesionaleSaludUniAgen = new ArrayList<HashMap<String,Object>>();
		this.indexCodUniAgen = "";
		this.datosUnidadAgenda = new HashMap<String,Object>();
		this.estadoEliminar = ConstantesBD.acronimoNo;
		//-----------------------------------------------
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.estadoTransaccion = ConstantesBD.acronimoNo;
		this.tipoAtencion="";
		this.estadoAnt="";
		this.multiploMinGenCita=ConstantesBD.codigoNuncaValido;
		this.parametroMultiplo="";
		this.func="";
		this.listDiaSemana= new  ArrayList<HashMap>();
		this.listaHorarios= new ArrayList<DtoHorarioAtencion>();
		this.horaIniHorarios="";
		this.horaFinHorarios="";
		this.minIniHorarios="";
		this.minFinHorarios="";
	}

	public String getDiaAsignar() {
		return diaAsignar;
	}

	public void setDiaAsignar(String diaAsignar) {
		this.diaAsignar = diaAsignar;
	}

	public String getHoraIniAsignar() {
		return horaIniAsignar;
	}

	public void setHoraIniAsignar(String horaIniAsignar) {
		this.horaIniAsignar = horaIniAsignar;
	}

	public String getConsultorioAsignar() {
		return consultorioAsignar;
	}

	public void setConsultorioAsignar(String consultorioAsignar) {
		this.consultorioAsignar = consultorioAsignar;
	}

	public String getHoraIniHorarios() {
		return horaIniHorarios;
	}

	public void setHoraIniHorarios(String horaIniHorarios) {
		this.horaIniHorarios = horaIniHorarios;
	}

	public String getMinIniHorarios() {
		return minIniHorarios;
	}

	public void setMinIniHorarios(String minIniHorarios) {
		this.minIniHorarios = minIniHorarios;
	}

	public String getHoraFinHorarios() {
		return horaFinHorarios;
	}

	public void setHoraFinHorarios(String horaFinHorarios) {
		this.horaFinHorarios = horaFinHorarios;
	}

	public String getMinFinHorarios() {
		return minFinHorarios;
	}

	public void setMinFinHorarios(String minFinHorarios) {
		this.minFinHorarios = minFinHorarios;
	}

	public ArrayList<HashMap> getListDiaSemana() {
		return listDiaSemana;
	}

	public void setListDiaSemana(ArrayList<HashMap> listDiaSemana) {
		this.listDiaSemana = listDiaSemana;
	}

	public int getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getParametroMultiplo() {
		return parametroMultiplo;
	}

	public void setParametroMultiplo(String parametroMultiplo) {
		this.parametroMultiplo = parametroMultiplo;
	}

	public int getMultiploMinGenCita() {
		return multiploMinGenCita;
	}

	public void setMultiploMinGenCita(int multiploMinGenCita) {
		this.multiploMinGenCita = multiploMinGenCita;
	}

	public String getEstadoAnt() {
		return estadoAnt;
	}

	public void setEstadoAnt(String estadoAnt) {
		this.estadoAnt = estadoAnt;
	}

	/**
	* Establece el código del horario de atención de la forma
	* @param ai_codigo Identificador único de este horario de atención de la forma
	*/
	public void setCodigo(int ai_codigo)
	{
		ii_codigo = ai_codigo;
	}

	/**
	* Establece el código del consultorio del horario de atención
	* @param ai_consultorio Código del consultorio a asignar a este horario de atención
	*/
	public void setCodigoConsultorio(int ai_consultorio)
	{
		ii_consultorio = ai_consultorio;
	}

	/**
	* Establece el código del día de la semana del horario de atención
	* @param ai_diaSemana Código del día de la semana a asignar a este horario de atención
	*/
	public void setCodigoDiaSemana(int ai_diaSemana)
	{
		ii_diaSemana = ai_diaSemana;
	}

	/**
	* Establece el código del médico del horario de atencio
	* @param as_codigoMedico Código del médico a asignar a este horario de atención
	*/
	public void setCodigoMedico(int ai_medico)
	{
		ii_medico = ai_medico;
	}

	/**
	* Establece la unidad de consulta del horario de atención
	* @param ai_unidadConsulta Código de la unidad de consulta a asignar a este horario de atención
	*/
	public void setCodigoUnidadConsulta(int ai_unidadConsulta)
	{
		ii_unidadAgenda = ai_unidadConsulta;
	}

	/**
	* Establece el consultorio del horario de atención
	* @param as_consultorio Nombre del consultorio de este horario de atención
	*/
	public void setConsultorio(String as_consultorio)
	{
		if(as_consultorio != null)
			is_consultorio = as_consultorio.trim();
	}

	/**
	* Establece el día de la semana del horario de atención
	* @param as_diaSemana Día de la semana al cual fue asignado este horario de atención
	*/
	public void setDiaSemana(String as_diaSemana)
	{
		if(as_diaSemana != null)
			is_diaSemana = as_diaSemana.trim();
	}

	/**
	* Establece el tiempo de duración (en minutos) de una consulta en este horario de atención
	* @param ai_duracionConsulta Tiempo en minutos de una consulta o sesión de este horario de
	* atención
	*/
	public void setDuracionConsulta(int ai_duracionConsulta)
	{
		if(ai_duracionConsulta > 0)
			ii_duracionConsulta = ai_duracionConsulta;
	}

	/**
	* Asigna el estado actual del flujo
	* @param as_estado el estado a asignar
	*/
	public void setEstado(String as_estado)
	{
		if(as_estado != null)
			is_estado = as_estado.trim();
	}

	/**
	* Establece la hora de finalización del horario de atención para el día de la semana
	* @param as_horaFin Hora de finalización de este horario de atención
	*/
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null)
			is_horaFin = as_horaFin.trim();
	}

	/**
	* Establece la hora de inicio del horario de atención para el día de la semana
	* @param as_horaInicio Hora de inicio de este horario de atención
	*/
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null)
			is_horaInicio = as_horaInicio.trim();
	}

	/**
	* Establece el conjuto de horarios de atención para listar
	* @param ac_horarios <code>Collection<code> de horarios de atención
	*/
	public void setItems(Collection ac_horarios)
	{
		if(ac_horarios != null)
			iv_horarios = new Vector(ac_horarios);
		else
			iv_horarios = new Vector(0);
	}

	/**
	* Establece el nombre del médico asignado al horario de atención e la forma
	* @param as_nombreMedico Nombre del médico asignado a este horario de atención
	*/
	public void setMedico(String as_nombreMedico)
	{
		if(as_nombreMedico != null)
			is_nombreMedico = as_nombreMedico.trim();
	}

	/**
	* Establece el número máximo de pacientes que se pueden atender por sesión de consulta
	* @param ai_pacientesSesion Número máximo de pacientes que se pueden asignar a una
	* consulta en este horario de atención
	*/
	public void setPacientesSesion(int ai_pacientesSesion)
	{
		//if(ai_pacientesSesion > 0)
			ii_pacientesSesion = ai_pacientesSesion;
	}

	/**
	* Establece el número máximo de sesiones en este horario de atención
	* @param ai_totalSesiones Número máximo de sesiones que se pueden asignar a este horario de
	* atención
	*/
	public void setTotalSesiones(int ai_totalSesiones)
	{
		if(ai_totalSesiones > 0)
			ii_totalSesiones = ai_totalSesiones;
	}

	/**
	* Establece la unidad de consulta del horario de atención de la forma
	* @param as_unidadConsulta Nombre de la unidad de consulta de este horario de atención
	*/
	public void setUnidadConsulta(String as_unidadConsulta)
	{
		if(as_unidadConsulta != null)
			is_unidadConsulta = as_unidadConsulta.trim();
	}

	/**
	* Obtiener el número de horarios de atención a listar
	* @return Número de horarios de atención a listar
	*/
	public int size()
	{
		return iv_horarios == null ? 0 : iv_horarios.size();
	}

	/**
	* Valida las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validación encontrados. Si no se encontraron errores de validación,
	* retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este
	* momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al
	* validar este formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping aam_m, HttpServletRequest hsr_r)
	{
		ActionErrors lae_errors;

		lae_errors = null;
		
		boolean indica;
			
		if(is_estado.equals("guardarNuevoHorarioAtencion") ||is_estado.equals("guardarHorarioAtencionModificado"))
		{
			
			lae_errors = new ActionErrors();

			//Validaciones para los dias de la semana
			indica = false;
			diasSemanaMap.put("haySeleccion",ConstantesBD.acronimoNo);
			
			for(int i = 0; i< Integer.parseInt(diasSemanaMap.get("numRegistros").toString()) && !indica ; i++)
			{
				if(diasSemanaMap.get("seleccionado_"+i).equals(ConstantesBD.acronimoSi))
				{
					indica = true;
					diasSemanaMap.put("haySeleccion",ConstantesBD.acronimoSi);			
				}
			}
			
			if((ii_consultorio == ConstantesBD.codigoNuncaValido && ii_medico != ConstantesBD.codigoNuncaValido) && !indica)				
				lae_errors.add("descripcion",new ActionMessage("errors.required","Debe Seleccionar el o los Dias de  la semana. Dia "));				
			
			if((ii_medico == ConstantesBD.codigoNuncaValido && ii_consultorio != ConstantesBD.codigoNuncaValido) && !indica)				
				lae_errors.add("descripcion",new ActionMessage("errors.required","Debe Seleccionar el o los Dias de  la semana. Dia"));

			if((ii_consultorio == ConstantesBD.codigoNuncaValido && ii_medico == ConstantesBD.codigoNuncaValido) && !indica)
				lae_errors.add("descripcion",new ActionMessage("errors.required","Debe Seleccionar Dia(s) de  la semana o Debe Seleccionar Consultorio y Profesional de la Salud sin Dia(s) de Semana. Dia"));
		

			if(this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
			{
				if( this.ii_duracionConsulta < 1 || this.ii_duracionConsulta > 240 )
				{
					lae_errors.add("rangoDuracionConsulta", new ActionMessage("errors.range", "La duración de la consulta", "1", "240"));
				}
			}
			
			/* El código de la unidad de consulta debe ser válido */
			if(ii_unidadAgenda < 0)
				lae_errors.add("codigoUnidadAgenda",new ActionMessage("errors.seleccionInvalida", "unidad de agenda"));		

			/* La hora de finalización debe estar presente*/
			if(is_horaFin.equals(""))
				lae_errors.add("horaFin",new ActionMessage("error.horarioAtencion.horaFin","Hora de Inicio","Duración de la Consulta","Total Sesiones por Atender"));

			//Se valida que el número de pacientes por consulta sea mayor a cero
			if(ii_pacientesSesion<1)
			{
				lae_errors.add("pacientesPorConsulta",new ActionMessage("errors.integerMayorQue", "El nómero de pacientes por consulta", "0"));
			}

			if(this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
			{
				/* Realizar la validación especificada en el archivo validation.xml */
				lae_errors.add(super.validate(aam_m, hsr_r) );
			}

			/* Validar esto sí y solo si no se han encontrado errores */
			if(lae_errors.isEmpty() && !is_horaInicio.equals("") && !is_horaFin.equals("") )
			{
				Date				ld_horaInicio;
				Date				ld_horaFin;
				SimpleDateFormat	lsdf_sdf;

				/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
				lsdf_sdf = new SimpleDateFormat("H:mm");

				/* Exije una interpretación estricta del formato de hora esperado */
				lsdf_sdf.setLenient(false);

				try
				{
					/* Obtener la hora de inicio */
					ld_horaInicio = lsdf_sdf.parse(is_horaInicio);
				}
				catch(ParseException lpe_horaInicio)
				{
					lae_errors.add("horaInicio",new ActionMessage("errors.formatoHoraInvalido", "Hora de inicio"));
					ld_horaInicio = null;
				}

				try
				{
					/* Obtener la hora de finalización */
					ld_horaFin = lsdf_sdf.parse(is_horaFin);
				}
				catch(ParseException lpe_horaFin)
				{
					lae_errors.add("horaFin",new ActionMessage("errors.formatoHoraInvalido", "Hora de finalización"));
					ld_horaFin = null;
				}

				
					if(ld_horaFin != null && ld_horaInicio != null)
					{
						int ld_duracionTotal = 0;
						int ld_totalSesiones = 0;
						String horaInicioTemp = is_horaInicio;
						
						/*  Se cambia la validación de menor-igual ya que si se deja menor-igual siempre contaría un minuto de más. MT 666 y 665. Cristhian Murillo */
						//while(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(horaInicioTemp, is_horaFin))
						while(UtilidadFecha.esHoraMenorQueOtraReferencia(horaInicioTemp, is_horaFin))
						{
							horaInicioTemp = UtilidadFecha.incrementarMinutosAHora(horaInicioTemp, 1);
							ld_duracionTotal ++;
						}
									
						//logger.info("ld_duracionTotal: "+ld_duracionTotal);
						//logger.info("ii_duracionConsulta: "+ii_duracionConsulta);
						if(this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
						{
							ld_totalSesiones = (ld_duracionTotal / ii_duracionConsulta);
						}
							//logger.info("ld_totalSesiones: "+ld_totalSesiones);
							//logger.info("ii_totalSesiones: "+ii_totalSesiones);
							
							/* La hora de inicio debe ser anterior a la hora de finalización */
	
								if(ld_horaFin.before(ld_horaInicio)){
									lae_errors.add("horaFin",new ActionMessage("errors.horaSuperiorA","de inicio","de finalización"));
								}
							/*
								El número de sesiones que se espera realizar debe poderse llevar a cabo en
								el horario especificado
							*/
								else if(ld_totalSesiones != ii_totalSesiones)
								{
									if(this.tipoAtencion.equals(ConstantesIntegridadDominio.acronimoTipoAtencionGeneral))
									{
										lae_errors.add("totalSesiones",new ActionMessage("error.horarioAtencion.totalSesiones"));
									}
								}
				}

				/* Si no hay errores limpiar el contenedor */
				if(lae_errors.isEmpty() )
					lae_errors = null;
			}
		}

		return lae_errors;
	}

	public String getColumna() {
		return columna;
	}

	public void setColumna(String columna) {
		this.columna = columna;
	}

	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}

	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
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
	 * @return Retorna the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion()
	{
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion)
	{
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the diasSemanaMap
	 */
	public HashMap getDiasSemanaMap() {
		return diasSemanaMap;
	}

	/**
	 * @param diasSemanaMap the diasSemanaMap to set
	 */
	public void setDiasSemanaMap(HashMap diasSemanaMap) {
		this.diasSemanaMap = diasSemanaMap;
	}
	
	/**
	 * @return the diasSemanaMap
	 */
	public Object getDiasSemanaMap(String key) {
		return diasSemanaMap.get(key);
	}

	/**
	 * @param diasSemanaMap the diasSemanaMap to set
	 */
	public void setDiasSemanaMap(String key, Object value) {
		this.diasSemanaMap.put(key, value);
	}

	/**
	 * @return the ii_unidadAgenda
	 */
	public int getUnidadAgenda() {
		return ii_unidadAgenda;
	}

	/**
	 * @param ii_unidadAgenda the ii_unidadAgenda to set
	 */
	public void setUnidadAgenda(int ii_unidadAgenda) {
		this.ii_unidadAgenda = ii_unidadAgenda;
	}

	/**
	 * @return the unidadAgendaMap
	 */
	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(HashMap unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}	
	
	/**
	 * @return the unidadAgendaMap
	 */
	public Object getUnidadAgendaMap(String llave) {
		return unidadAgendaMap.get(llave);
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
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
	 * @return the profesionaleSaludUniAgen
	 */
	public ArrayList<HashMap<String, Object>> getProfesionaleSaludUniAgen() {
		return profesionaleSaludUniAgen;
	}

	/**
	 * @param profesionaleSaludUniAgen the profesionaleSaludUniAgen to set
	 */
	public void setProfesionaleSaludUniAgen(
			ArrayList<HashMap<String, Object>> profesionaleSaludUniAgen) {
		this.profesionaleSaludUniAgen = profesionaleSaludUniAgen;
	}

	/**
	 * @return the indexCodUniAgen
	 */
	public String getIndexCodUniAgen() {
		return indexCodUniAgen;
	}

	/**
	 * @param indexCodUniAgen the indexCodUniAgen to set
	 */
	public void setIndexCodUniAgen(String indexCodUniAgen) {
		this.indexCodUniAgen = indexCodUniAgen;
	}

	/**
	 * @return the datosUnidadAgenda
	 */
	public HashMap<String, Object> getDatosUnidadAgenda() {
		return datosUnidadAgenda;
	}

	/**
	 * @param datosUnidadAgenda the datosUnidadAgenda to set
	 */
	public void setDatosUnidadAgenda(HashMap<String, Object> datosUnidadAgenda) {
		this.datosUnidadAgenda = datosUnidadAgenda;
	}

	/**
	 * @return the estadoTransaccion
	 */
	public String getEstadoTransaccion() {
		return estadoTransaccion;
	}

	/**
	 * @param estadoTransaccion the estadoTransaccion to set
	 */
	public void setEstadoTransaccion(String estadoTransaccion) {
		this.estadoTransaccion = estadoTransaccion;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the estadoEliminar
	 */
	public String getEstadoEliminar() {
		return estadoEliminar;
	}

	/**
	 * @param estadoEliminar the estadoEliminar to set
	 */
	public void setEstadoEliminar(String estadoEliminar) {
		this.estadoEliminar = estadoEliminar;
	}
	
	/**
	 * @return the mostrarRutaJsp
	 */
	public boolean isMostrarRutaJsp() {
		return mostrarRutaJsp;
	}
	
	/**
	 * @param mostrarRutaJsp the mostrarRutaJsp to set
	 */
	public void setMostrarRutaJsp(boolean mostrarRutaJsp) {
		this.mostrarRutaJsp = mostrarRutaJsp;
	}

	public String getConsultorioAsignado() {
		return consultorioAsignado;
	}

	public void setConsultorioAsignado(String consultorioAsignado) {
		this.consultorioAsignado = consultorioAsignado;
	}

	public String getProfesionalAsignado() {
		return profesionalAsignado;
	}

	public void setProfesionalAsignado(String profesionalAsignado) {
		this.profesionalAsignado = profesionalAsignado;
	}

	public HashMap<String, Object> getDiasSemanaBusqueda() {
		return diasSemanaBusqueda;
	}

	public void setDiasSemanaBusqueda(HashMap<String, Object> diasSemanaBusqueda) {
		this.diasSemanaBusqueda = diasSemanaBusqueda;
	}
	
	public Object getDiasSemanaBusqueda(String key) {
		return diasSemanaBusqueda.get(key);
	}

	public void setDiasSemanaBusqueda(String key, Object value) {
		this.diasSemanaBusqueda.put(key, value);
	}

}