package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.dto.odontologia.DtoGenerarAgenda;

@SuppressWarnings("serial")
public class GenerarAgendaOdontologicaForm extends ValidatorForm {
	
	/**
	 * Manejo de versiones
	 */

	/**
	 * 
	 */
	private String estado;

	/**
	 * dto generar agenda odontologica  
	 */
	private DtoGenerarAgenda generarAgenda;
	
	private int codigoActividadAutorizacion;
	private ArrayList<HashMap<String,Object>> listCentroAtencion;
	private ArrayList<HashMap> listUnidadesAgendaXUsuario;
	private ArrayList<DtoConsultorios> listConsultorios;
	private ArrayList<HashMap<String, Object>> listProfesionalesActivos;
	private ArrayList<HashMap> listDiaSemana;
	
	// Atributos Formulario
	private String centroAtencion;
	private String nombreCentroAtencion;
	private String fechaInicial;
	private String fechaFinal;
	private String unidadAgenda;
	private String consultorio;
	private String diaSemana;
	private String profesionalSalud;
		
	private String paginaLinkSiguiente;
	private String paginaLinkSiguienteExcep;
	private String mostrarResumenGenAgenOdon;
	private int posAgenOdonExcep;

	
	private String mostrarLinkExHorario;
	
	private String xmlProfesionales;
	private String xmlUnidadesAgenda;
	private String xmlAgendaOdon;
	private String xmlParametros;
	
	private String horaIniAgenda;
	private String minIniAgenda;
	private String horaFinAgenda;
	private String minFinAgenda;
	
	private int multiploMinGenCita;
	
	private String consultorioAsignar;
	private int centroAtencionAsignar;
	private String fechaInicialAsignar;
	private String fechaFinalAsignar;
	private String unidadAgendaAsignar;
	private String diaSemanaAsignar;
	private String profesionalAsignar;
	
	private ArrayList<DtoExcepcionesHorarioAtencion> excepcionesHorario;
	
	private Date fechaInicialDate;
	private Date fechaFinalDate;
	
	/** * Contiene un listado de advertencias a mostrar */
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();

	private boolean agendaGenerada;
	
	public void reset()
	{
		this.generarAgenda = new DtoGenerarAgenda();
		this.codigoActividadAutorizacion = ConstantesBD.codigoNuncaValido;
		this.listCentroAtencion = new ArrayList<HashMap<String,Object>>();
		this.listUnidadesAgendaXUsuario = new  ArrayList<HashMap>();
		this.listConsultorios = new  ArrayList<DtoConsultorios>();
		this.listProfesionalesActivos = new ArrayList<HashMap<String, Object>>();
		this.listDiaSemana = new  ArrayList<HashMap>();
		
		// atributos formulario
		this.centroAtencion = "-1";
		this.nombreCentroAtencion="";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.unidadAgenda = "";
		this.consultorio = "";
		this.diaSemana = "";
		this.profesionalSalud = "";
		this.fechaInicialDate = null;
		this.fechaFinalDate = null;
		
		this.paginaLinkSiguiente = "" ;
		this.paginaLinkSiguienteExcep = "";
		this.mostrarResumenGenAgenOdon = "";
		this.posAgenOdonExcep = ConstantesBD.codigoNuncaValido;
		this.mostrarLinkExHorario = ConstantesBD.acronimoNo;
		
		this.xmlProfesionales="";
		this.xmlUnidadesAgenda="";
		this.xmlAgendaOdon="";
		this.xmlParametros="";
		
		this.horaIniAgenda="";
		this.minIniAgenda="";
		this.horaFinAgenda="";
		this.minFinAgenda="";
		
		this.multiploMinGenCita=ConstantesBD.codigoNuncaValido;
		
		this.consultorioAsignar="";		
		this.centroAtencionAsignar=ConstantesBD.codigoNuncaValido;
		this.fechaInicialAsignar="";
		this.fechaFinalAsignar="";
		this.diaSemanaAsignar="";
		this.unidadAgendaAsignar="";
		this.profesionalAsignar="";
		this.excepcionesHorario= new ArrayList<DtoExcepcionesHorarioAtencion>();
		this.listaAdvertencias = new ArrayList<String>();

		this.agendaGenerada = false;
	}
	
	public ArrayList<DtoExcepcionesHorarioAtencion> getExcepcionesHorario() {
		return excepcionesHorario;
	}

	public void setExcepcionesHorario(
			ArrayList<DtoExcepcionesHorarioAtencion> excepcionesHorario) {
		this.excepcionesHorario = excepcionesHorario;
	}

	public void resetAsignar()
	{
		this.generarAgenda = new DtoGenerarAgenda();
		this.codigoActividadAutorizacion = ConstantesBD.codigoNuncaValido;
		this.listCentroAtencion = new ArrayList<HashMap<String,Object>>();
		this.listUnidadesAgendaXUsuario = new  ArrayList<HashMap>();
		this.listConsultorios = new  ArrayList<DtoConsultorios>();
		this.listProfesionalesActivos = new ArrayList<HashMap<String, Object>>();
		this.listDiaSemana = new  ArrayList<HashMap>();
		
		// atributos formulario
		this.centroAtencion = "-1";
		this.nombreCentroAtencion="";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.unidadAgenda = "";
		this.consultorio = "";
		this.diaSemana = "";
		this.profesionalSalud = "";
		
		this.paginaLinkSiguiente = "" ;
		this.paginaLinkSiguienteExcep = "";
		this.mostrarResumenGenAgenOdon = "";
		this.posAgenOdonExcep = ConstantesBD.codigoNuncaValido;
		this.mostrarLinkExHorario = ConstantesBD.acronimoNo;
		
		this.xmlProfesionales="";
		this.xmlUnidadesAgenda="";
		this.xmlAgendaOdon="";
		this.xmlParametros="";
		
		this.horaIniAgenda="";
		this.minIniAgenda="";
		this.horaFinAgenda="";
		this.minFinAgenda="";
		
		this.multiploMinGenCita=ConstantesBD.codigoNuncaValido;
	}

	public int getCentroAtencionAsignar() {
		return centroAtencionAsignar;
	}

	public void setCentroAtencionAsignar(int centroAtencionAsignar) {
		this.centroAtencionAsignar = centroAtencionAsignar;
	}

	public String getFechaInicialAsignar() {
		return fechaInicialAsignar;
	}

	public void setFechaInicialAsignar(String fechaInicialAsignar) {
		this.fechaInicialAsignar = fechaInicialAsignar;
	}

	public String getFechaFinalAsignar() {
		return fechaFinalAsignar;
	}

	public void setFechaFinalAsignar(String fechaFinalAsignar) {
		this.fechaFinalAsignar = fechaFinalAsignar;
	}

	public String getUnidadAgendaAsignar() {
		return unidadAgendaAsignar;
	}

	public void setUnidadAgendaAsignar(String unidadAgendaAsignar) {
		this.unidadAgendaAsignar = unidadAgendaAsignar;
	}

	public String getDiaSemanaAsignar() {
		return diaSemanaAsignar;
	}

	public void setDiaSemanaAsignar(String diaSemanaAsignar) {
		this.diaSemanaAsignar = diaSemanaAsignar;
	}

	public String getProfesionalAsignar() {
		return profesionalAsignar;
	}

	public void setProfesionalAsignar(String profesionalAsignar) {
		this.profesionalAsignar = profesionalAsignar;
	}

	public String getConsultorioAsignar() {
		return consultorioAsignar;
	}


	public void setConsultorioAsignar(String consultorioAsignar) {
		this.consultorioAsignar = consultorioAsignar;
	}


	public int getMultiploMinGenCita() {
		return multiploMinGenCita;
	}


	public void setMultiploMinGenCita(int multiploMinGenCita) {
		this.multiploMinGenCita = multiploMinGenCita;
	}


	public String getHoraIniAgenda() {
		return horaIniAgenda;
	}

	public void setHoraIniAgenda(String horaIniAgenda) {
		this.horaIniAgenda = horaIniAgenda;
	}

	public String getMinIniAgenda() {
		return minIniAgenda;
	}

	public void setMinIniAgenda(String minIniAgenda) {
		this.minIniAgenda = minIniAgenda;
	}

	public String getHoraFinAgenda() {
		return horaFinAgenda;
	}

	public void setHoraFinAgenda(String horaFinAgenda) {
		this.horaFinAgenda = horaFinAgenda;
	}

	public String getMinFinAgenda() {
		return minFinAgenda;
	}

	public void setMinFinAgenda(String minFinAgenda) {
		this.minFinAgenda = minFinAgenda;
	}

	public String getXmlProfesionales() {
		return xmlProfesionales;
	}

	public void setXmlProfesionales(String xmlProfesionales) {
		this.xmlProfesionales = xmlProfesionales;
	}

	public String getXmlUnidadesAgenda() {
		return xmlUnidadesAgenda;
	}

	public void setXmlUnidadesAgenda(String xmlUnidadesAgenda) {
		this.xmlUnidadesAgenda = xmlUnidadesAgenda;
	}

	public String getXmlAgendaOdon() {
		return xmlAgendaOdon;
	}

	public void setXmlAgendaOdon(String xmlAgendaOdon) {
		this.xmlAgendaOdon = xmlAgendaOdon;
	}

	public String getXmlParametros() {
		return xmlParametros;
	}

	public void setXmlParametros(String xmlParametros) {
		this.xmlParametros = xmlParametros;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	public String getMostrarLinkExHorario() {
		return mostrarLinkExHorario;
	}

	public void setMostrarLinkExHorario(String mostrarLinkExHorario) {
		this.mostrarLinkExHorario = mostrarLinkExHorario;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
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
	 * @return the listCentroAtencion
	 */
	public ArrayList<HashMap<String,Object>> getListCentroAtencion() {
		return listCentroAtencion;
	}

	/**
	 * @param listCentroAtencion the listCentroAtencion to set
	 */
	public void setListCentroAtencion(
			ArrayList<HashMap<String,Object>> listCentroAtencion) {
		this.listCentroAtencion = listCentroAtencion;
	}

	/**
	 * @return the listUnidadesAgendaXUsuario
	 */
	public ArrayList<HashMap> getListUnidadesAgendaXUsuario() {
		return listUnidadesAgendaXUsuario;
	}

	/**
	 * @param listUnidadesAgendaXUsuario the listUnidadesAgendaXUsuario to set
	 */
	public void setListUnidadesAgendaXUsuario(
			ArrayList<HashMap> listUnidadesAgendaXUsuario) {
		this.listUnidadesAgendaXUsuario = listUnidadesAgendaXUsuario;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}

	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}

	/**
	 * @return the consultorio
	 */
	public String getConsultorio() {
		return consultorio;
	}

	/**
	 * @param consultorio the consultorio to set
	 */
	public void setConsultorio(String consultorio) {
		this.consultorio = consultorio;
	}

	/**
	 * @return the diaSemana
	 */
	public String getDiaSemana() {
		return diaSemana;
	}

	/**
	 * @param diaSemana the diaSemana to set
	 */
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	/**
	 * @return the profesionalSalud
	 */
	public String getProfesionalSalud() {
		return profesionalSalud;
	}

	/**
	 * @param profesionalSalud the profesionalSalud to set
	 */
	public void setProfesionalSalud(String profesionalSalud) {
		this.profesionalSalud = profesionalSalud;
	}

	/**
	 * @return the listConsultorios
	 */
	public ArrayList<DtoConsultorios> getListConsultorios() {
		return listConsultorios;
	}

	/**
	 * @param listConsultorios the listConsultorios to set
	 */
	public void setListConsultorios(ArrayList<DtoConsultorios> listConsultorios) {
		this.listConsultorios = listConsultorios;
	}

	/**
	 * @return the listProfesionalesActivos
	 */
	public ArrayList<HashMap<String, Object>> getListProfesionalesActivos() {
		return listProfesionalesActivos;
	}

	/**
	 * @param listProfesionalesActivos the listProfesionalesActivos to set
	 */
	public void setListProfesionalesActivos(
			ArrayList<HashMap<String, Object>> listProfesionalesActivos) {
		this.listProfesionalesActivos = listProfesionalesActivos;
	}

	/**
	 * @return the listDiaSemana
	 */
	public ArrayList<HashMap> getListDiaSemana() {
		return listDiaSemana;
	}

	/**
	 * @param listDiaSemana the listDiaSemana to set
	 */
	public void setListDiaSemana(ArrayList<HashMap> listDiaSemana) {
		this.listDiaSemana = listDiaSemana;
	}

	
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validaciï¿½n encontrados. Si no se
	* encontraron errores de validaciï¿½n, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que estï¿½ siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		Log4JManager.info(fechaInicial);
		Log4JManager.info(fechaFinal);
		
		ActionErrors errores = new ActionErrors();
		/*if(this.estado.equals("generarAgenOdon") || this.estado.equals("listarConsulta"))
		{
			//listar			
		}*/
		if(this.estado.equals("consultaGrafica"))
		{
			// validacion de las fechas
			boolean fechaInicialValida = true;
			
			// se valida que las fecha inicial y la final contengan datos
			if(this.fechaInicial.equals("")){
				errores.add("fechaInicio",new ActionMessage("errors.required", "El Campo Fecha Inicial "));
				fechaInicialValida = false;
			}
			
			// se valida que la fecha inicial y la final esten en el formato adecuado
			if(fechaInicialValida)
			{
				if(!UtilidadFecha.validarFecha(this.fechaInicial)){
					errores.add("fechaInicio",new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicial"));
					fechaInicialValida = false;
				}				
			}
		}
		
		if(errores.isEmpty())
			return null;
		else
			return errores;
	}

	/**
	 * @return the generarAgenda
	 */
	public DtoGenerarAgenda getGenerarAgenda() {
		return generarAgenda;
	}

	/**
	 * @param generarAgenda the generarAgenda to set
	 */
	public void setGenerarAgenda(DtoGenerarAgenda generarAgenda) {
		this.generarAgenda = generarAgenda;
	}

	/**
	 * @return the paginaLinkSiguiente
	 */
	public String getPaginaLinkSiguiente() {
		return paginaLinkSiguiente;
	}

	/**
	 * @param paginaLinkSiguiente the paginaLinkSiguiente to set
	 */
	public void setPaginaLinkSiguiente(String paginaLinkSiguiente) {
		this.paginaLinkSiguiente = paginaLinkSiguiente;
	}

	/**
	 * @return the mostrarResumenGenAgenOdon
	 */
	public String getMostrarResumenGenAgenOdon() {
		return mostrarResumenGenAgenOdon;
	}

	/**
	 * @param mostrarResumenGenAgenOdon the mostrarResumenGenAgenOdon to set
	 */
	public void setMostrarResumenGenAgenOdon(String mostrarResumenGenAgenOdon) {
		this.mostrarResumenGenAgenOdon = mostrarResumenGenAgenOdon;
	}

	/**
	 * @return the posAgenOdonExcep
	 */
	public int getPosAgenOdonExcep() {
		return posAgenOdonExcep;
	}

	/**
	 * @param posAgenOdonExcep the posAgenOdonExcep to set
	 */
	public void setPosAgenOdonExcep(int posAgenOdonExcep) {
		this.posAgenOdonExcep = posAgenOdonExcep;
	}

	/**
	 * @return the paginaLinkSiguienteExcep
	 */
	public String getPaginaLinkSiguienteExcep() {
		return paginaLinkSiguienteExcep;
	}

	/**
	 * @param paginaLinkSiguienteExcep the paginaLinkSiguienteExcep to set
	 */
	public void setPaginaLinkSiguienteExcep(String paginaLinkSiguienteExcep) {
		this.paginaLinkSiguienteExcep = paginaLinkSiguienteExcep;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaInicialDate
	 *
	 * @return retorna la variable fechaInicialDate
	 */
	public Date getFechaInicialDate() {
		return fechaInicialDate;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaInicialDate
	 * @param fechaInicialDate es el valor para el atributo fechaInicialDate 
	 */
	public void setFechaInicialDate(Date fechaInicialDate) {
		this.fechaInicialDate = fechaInicialDate;
		
		this.fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicialDate);
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaFinalDate
	 *
	 * @return retorna la variable fechaFinalDate
	 */
	public Date getFechaFinalDate() {
		return fechaFinalDate;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaFinalDate
	 * @param fechaFinalDate es el valor para el atributo fechaFinalDate 
	 */
	public void setFechaFinalDate(Date fechaFinalDate) {
		this.fechaFinalDate = fechaFinalDate;
		
		this.fechaFinal = UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinalDate);
	}

	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}

	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}

	/**
	 * Método que se encarga de obtener el valor del atributo agendaGenerada
	 * 
	 * @return retorna la variable agendaGenerada
	 */
	public boolean isAgendaGenerada() {
		return agendaGenerada;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo agendaGenerada
	 * 
	 * @param agendaGenerada es el valor para el atributo agendaGenerada
	 */
	public void setAgendaGenerada(boolean agendaGenerada) {
		this.agendaGenerada = agendaGenerada;
	}
	
}
