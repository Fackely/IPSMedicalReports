package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;

/**
 * @author Cristhian Murillo
 */
public class CierrePresupuestoForm extends ValidatorForm 
{
	
	/** * Serial */
	private static final long serialVersionUID = 1L;
	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;
	/** * Posicion de las autorizaciones */
	private int posArray;
	/** * Posicion de las autorizaciones */
	private int posArrayAnterior;
	/** * Parametros para ordenar */
	private String patronOrdenar;
	/** * Parametros para ordenar */
	private String funcionalidadAnterior;
	/** * Parametros para ordenar */
	private String esDescendente;
	/** *Fecha inicio busqueda */
	private Date fechaInicio;
	/** * Fecha fin busqueda */
	private Date fechaFin;
	/** Lista de convenios asociados a la institucion en sesion */
	private ArrayList<Convenios> listaConveniosInstitucion;
	/** Lista de contatos asociados al convenio seleccionado */
	private ArrayList<Contratos> listaContratoConvenio;		
	/** * Convenio  */
	private String codigoConvenio;
	/** * Contrato  */
	private String codigoContrato;
	/** * Obervaciones busqueda */
	private String observaciones;
	/** * Lista de testigos para la aceptacion */
	private ArrayList<DtoUsuarioPersona> listaUsuarios;
	/** * Loguin del usuario seleccionado para la consulta del log */
	private String usuarioSeleccionado;
	/** * Lista de logs a mostrar */
	private ArrayList<DtoLogCierrePresuCapita> listaLogs;
	/** * Lista de logs a mostrar */
	private ArrayList<DtoLogCierrePresuCapita> listaLogsAux;
	/** * Log seleccionado */
	private DtoLogCierrePresuCapita loggySeleccionado;
	/*-------------------------------------------------------*/
	/* RESET's
	/*-------------------------------------------------------*/

	/** * Reset de la forma  */
	public void reset()
	{
		this.patronOrdenar 				= "";
		this.esDescendente				= "";
		this.listaConveniosInstitucion 	= new ArrayList<Convenios>();
		this.listaContratoConvenio		= new ArrayList<Contratos>();
		this.codigoConvenio				= null;
		this.codigoContrato				= null;
		this.fechaInicio				= null;
		this.fechaFin 					= null;
		this.listaUsuarios				= new ArrayList<DtoUsuarioPersona>();
		this.usuarioSeleccionado		= null;
		this.listaLogs					= new ArrayList<DtoLogCierrePresuCapita>();
		this.loggySeleccionado			= new DtoLogCierrePresuCapita();
		this.observaciones				= null;
		this.listaLogsAux				= new ArrayList<DtoLogCierrePresuCapita>();
		//this.funcionalidadAnterior		= "";
	}

	/*-------------------------------------------------------*/
	/* METODOS SETs Y GETs
	/*-------------------------------------------------------*/
	
	/**
	 * @return valor de estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado el estado para asignar
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return valor de posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray el posArray para asignar
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return valor de patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar el patronOrdenar para asignar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return valor de esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente el esDescendente para asignar
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return valor de listaConveniosInstitucion
	 */
	public ArrayList<Convenios> getListaConveniosInstitucion() {
		return listaConveniosInstitucion;
	}

	/**
	 * @param listaConveniosInstitucion el listaConveniosInstitucion para asignar
	 */
	public void setListaConveniosInstitucion(
			ArrayList<Convenios> listaConveniosInstitucion) {
		this.listaConveniosInstitucion = listaConveniosInstitucion;
	}

	/**
	 * @return valor de listaContratoConvenio
	 */
	public ArrayList<Contratos> getListaContratoConvenio() {
		return listaContratoConvenio;
	}

	/**
	 * @param listaContratoConvenio el listaContratoConvenio para asignar
	 */
	public void setListaContratoConvenio(ArrayList<Contratos> listaContratoConvenio) {
		this.listaContratoConvenio = listaContratoConvenio;
	}

	/**
	 * @return valor de codigoConvenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio el codigoConvenio para asignar
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return valor de codigoContrato
	 */
	public String getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato el codigoContrato para asignar
	 */
	public void setCodigoContrato(String codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * @return valor de observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones el observaciones para asignar
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return valor de fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio el fechaInicio para asignar
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return valor de fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * @param fechaFin el fechaFin para asignar
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return valor de listaUsuarios
	 */
	public ArrayList<DtoUsuarioPersona> getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * @param listaUsuarios el listaUsuarios para asignar
	 */
	public void setListaUsuarios(ArrayList<DtoUsuarioPersona> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	/**
	 * @return valor de usuarioSeleccionado
	 */
	public String getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	/**
	 * @param usuarioSeleccionado el usuarioSeleccionado para asignar
	 */
	public void setUsuarioSeleccionado(String usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}

	/**
	 * @return valor de listaLogs
	 */
	public ArrayList<DtoLogCierrePresuCapita> getListaLogs() {
		return listaLogs;
	}

	/**
	 * @param listaLogs el listaLogs para asignar
	 */
	public void setListaLogs(ArrayList<DtoLogCierrePresuCapita> listaLogs) {
		this.listaLogs = listaLogs;
	}

	/**
	 * @return valor de loggySeleccionado
	 */
	public DtoLogCierrePresuCapita getLoggySeleccionado() {
		return loggySeleccionado;
	}

	/**
	 * @param loggySeleccionado el loggySeleccionado para asignar
	 */
	public void setLoggySeleccionado(DtoLogCierrePresuCapita loggySeleccionado) {
		this.loggySeleccionado = loggySeleccionado;
	}

	/**
	 * @return valor de posArrayAnterior
	 */
	public int getPosArrayAnterior() {
		return posArrayAnterior;
	}

	/**
	 * @param posArrayAnterior el posArrayAnterior para asignar
	 */
	public void setPosArrayAnterior(int posArrayAnterior) {
		this.posArrayAnterior = posArrayAnterior;
	}

	public void setListaLogsAux(ArrayList<DtoLogCierrePresuCapita> listaLogsAux) {
		this.listaLogsAux = listaLogsAux;
	}

	public ArrayList<DtoLogCierrePresuCapita> getListaLogsAux() {
		return listaLogsAux;
	}

	public void setFuncionalidadAnterior(String funcionalidadAnterior) {
		this.funcionalidadAnterior = funcionalidadAnterior;
	}

	public String getFuncionalidadAnterior() {
		return funcionalidadAnterior;
	}
		

}
