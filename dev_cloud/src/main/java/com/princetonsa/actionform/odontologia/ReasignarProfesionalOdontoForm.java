/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dto.odontologia.DtoReasignarProfesionalOdonto;
import com.princetonsa.mundo.Usuario;

/**
 * @author Jairo Andrés Gómez
 * noviembre 9 / 2009
 *
 */
public class ReasignarProfesionalOdontoForm extends ValidatorForm{
//	*************** Declaracion de variables ***************

	private String estado;
	private String estadoAnterior;
	private ResultadoBoolean mensaje;
	private DtoReasignarProfesionalOdonto busquedaServicios;
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	private ArrayList<HashMap> unidadesAgenda;
	private ArrayList<HashMap<String, Object>> profesionalesSalud; 
	private int nuevoProfesionalSalud;
	
	/*
	 * variable para el llamado asincrono de cargar los profesionales de la salud y las unidades de agenda
	 */
	private int index;
	
	/*
	 * Atributo pager
	 */
	private String linkSiguiente;
	
	/*
	 * Atributo para almacenar el resultado de la busqueda de Servicios
	 */
	private ArrayList<DtoReasignarProfesionalOdonto> arrayResultado;
	
	/*
	 * Atributo para checkear todos los registro
	 */
	private String seleccionaTodos;
	
	/*
	 * Atributos utilizados para hacer el rompimiento
	 */
	private String centroAtencionAnterior;
	private String profesionalSaludAnterior;
	private String profesionalSaludAnteriorFinal;
	
	/*
	 * Atributo utilizado para la busqueda de logs
	 */
	private ArrayList<Usuario> usuariosProceso;
	
	/*
	 * Atributos para ordenar
	 */
	private String patronOrdenar;
	private String esDescendente;
	
//	************ Fin Declaracion de variables **************
	
	/**
	 * Metodo que inicializa todas las variables.
	 */
	public void reset() {
		this.estado = "";
		this.estadoAnterior = "";
		this.mensaje = new ResultadoBoolean(false);
		this.busquedaServicios = new DtoReasignarProfesionalOdonto();
		this.centrosAtencion = new ArrayList<HashMap<String,Object>>();
		this.unidadesAgenda = new ArrayList<HashMap>();
		this.profesionalesSalud = new ArrayList<HashMap<String, Object>>();
		this.nuevoProfesionalSalud = ConstantesBD.codigoNuncaValido;
		
//		variable para el llamado asincrono de cargar los profesionales de la salud y las unidades de agenda
		this.index = ConstantesBD.codigoNuncaValido;
		
//		 * Atributo pager
		this.linkSiguiente = "";
		
		this.arrayResultado = new ArrayList<DtoReasignarProfesionalOdonto>();
		
//		Atributo para checkear todos los registro
		this.seleccionaTodos = ConstantesBD.acronimoNo;
		
//		 * Atributos utilizados para hacer el rompimiento
		this.centroAtencionAnterior = "";
		this.profesionalSaludAnterior = "";
		this.profesionalSaludAnteriorFinal = "";
		
//		 * Atributo utilizado para la busqueda de logs
		this.usuariosProceso = new ArrayList<Usuario>();
		
//		 * Atributos para ordenar
		this.patronOrdenar = "";
		this.esDescendente = "";
	}
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		return errores;
	}

//	*************** Declaracion de Metodos Get y Set ***************
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the busquedaServicios
	 */
	public DtoReasignarProfesionalOdonto getBusquedaServicios() {
		return busquedaServicios;
	}

	/**
	 * @param busquedaServicios the busquedaServicios to set
	 */
	public void setBusquedaServicios(DtoReasignarProfesionalOdonto busquedaServicios) {
		this.busquedaServicios = busquedaServicios;
	}

	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String,Object>> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(ArrayList<HashMap<String,Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the unidadesAgenda
	 */
	public ArrayList<HashMap> getUnidadesAgenda() {
		return unidadesAgenda;
	}

	/**
	 * @param unidadesAgenda the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(ArrayList<HashMap> unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the profesionalesSalud
	 */
	public ArrayList<HashMap<String, Object>> getProfesionalesSalud() {
		return profesionalesSalud;
	}

	/**
	 * @param profesionalesSalud the profesionalesSalud to set
	 */
	public void setProfesionalesSalud(
			ArrayList<HashMap<String, Object>> profesionalesSalud) {
		this.profesionalesSalud = profesionalesSalud;
	}

	/**
	 * @return the arrayResultado
	 */
	public ArrayList<DtoReasignarProfesionalOdonto> getArrayResultado() {
		return arrayResultado;
	}

	/**
	 * @param arrayResultado the arrayResultado to set
	 */
	public void setArrayResultado(
			ArrayList<DtoReasignarProfesionalOdonto> arrayResultado) {
		this.arrayResultado = arrayResultado;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the seleccionaTodos
	 */
	public String getSeleccionaTodos() {
		return seleccionaTodos;
	}

	/**
	 * @param seleccionaTodos the seleccionaTodos to set
	 */
	public void setSeleccionaTodos(String seleccionaTodos) {
		this.seleccionaTodos = seleccionaTodos;
	}

	/**
	 * @return the nuevoProfesionalSalud
	 */
	public int getNuevoProfesionalSalud() {
		return nuevoProfesionalSalud;
	}

	/**
	 * @param nuevoProfesionalSalud the nuevoProfesionalSalud to set
	 */
	public void setNuevoProfesionalSalud(int nuevoProfesionalSalud) {
		this.nuevoProfesionalSalud = nuevoProfesionalSalud;
	}

	/**
	 * @return the centroAtencionAnterior
	 */
	public String getCentroAtencionAnterior() {
		return centroAtencionAnterior;
	}

	/**
	 * @param centroAtencionAnterior the centroAtencionAnterior to set
	 */
	public void setCentroAtencionAnterior(String centroAtencionAnterior) {
		this.centroAtencionAnterior = centroAtencionAnterior;
	}

	/**
	 * @return the profesionalSaludAnterior
	 */
	public String getProfesionalSaludAnterior() {
		return profesionalSaludAnterior;
	}

	/**
	 * @param profesionalSaludAnterior the profesionalSaludAnterior to set
	 */
	public void setProfesionalSaludAnterior(String profesionalSaludAnterior) {
		this.profesionalSaludAnterior = profesionalSaludAnterior;
	}

	/**
	 * @return the usuariosProceso
	 */
	public ArrayList<Usuario> getUsuariosProceso() {
		return usuariosProceso;
	}

	/**
	 * @param usuariosProceso the usuariosProceso to set
	 */
	public void setUsuariosProceso(ArrayList<Usuario> usuariosProceso) {
		this.usuariosProceso = usuariosProceso;
	}

	/**
	 * @return the profesionalSaludAnteriorFinal
	 */
	public String getProfesionalSaludAnteriorFinal() {
		return profesionalSaludAnteriorFinal;
	}

	/**
	 * @param profesionalSaludAnteriorFinal the profesionalSaludAnteriorFinal to set
	 */
	public void setProfesionalSaludAnteriorFinal(
			String profesionalSaludAnteriorFinal) {
		this.profesionalSaludAnteriorFinal = profesionalSaludAnteriorFinal;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the estadoAnterior
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	/**
	 * @param estadoAnterior the estadoAnterior to set
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}	
	
//	************* Fin Declaracion de Metodos Get y Set *************
}
