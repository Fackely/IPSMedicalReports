/*
 * Creado el 23-sep-2005
 * por Julian Montoya
 */
package com.princetonsa.actionform.hojaOftalmologica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */			
@SuppressWarnings("rawtypes")
public class AntecedentesOftalmologicosForm extends ValidatorForm 
{

		
	 /** *  */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	//-----------------Sección Antecedentes Personales Oftalmologicos  
	
   /**
    * Mapa sirve para guardar datos de los listados 
    * de los tipos de enfermedades Oftalmologicas 
    */
	private HashMap mapa=new HashMap();


	/**
	 * Coleccion para almacenar los tipos de enfermedades oftalmologicas
	 */
	private Collection listadoEnferPerso; 

	/**
	 * Coleccion para almacenar las enfermedades registradas de un paciente especifico
	 */
	private Collection listadoEnferOftaPersoPaciente; 

	/**
	 * Coleccion para almacenar los procedimientos oftalmologicos quirurgicos del paciente 
	 */
	private Collection listadoEnferOftaPersoQuirurPaciente; 

	/**
	 * Campo Tem para almacenar la informacion del area de texto emergente en el jsp
	 */
	
	private String temp;

	
	/**
	 * Coleccion para almacenar los tipos de enfermedades oftalmologicas familiares registradas
	 */
	private Collection listadoEnferFam; 

	/**
	 * Coleccion para almacenar los antecedentes oftalmologicos de los familiares   
	 */
	private Collection listadoEnferOftaFam; 

	/**
	 * coleccion para traer los tipos de parentesco 
	 */
	private Collection listadoParentesco;
	
	/**
	 * coleccion para traer los tipos de parentesco registrados anteriormente 
	 * que padecen algun tipo de enfermedad oftalmologica. 
	 */
	private Collection listadoEnferFamPadece;

	/**
	 * Campo que almacena el codigo del paciente 
	 * Para identificar si tiene antecedentes o no ???
	 */
	
	private int codAuxPaciente;
	
	/**
	 * Campo para almacenar Información sobre otra
	 * enfermedad oftalmologica digitada por el usuario 
	 */
	private String otroEnferPerso;
	
	/**
	 * Campo que permite registrar desde cuando se sufre
	 * la enfermedad descrita en otroEnferPerso
	 */
	private String otroDesdeCuando;
	
	/**
	 * Campo que permite registrar el tratamiento
	 * descrita en otroEnferPerso.
	 */
	private String otroTratamiento;

	/**
	 * Campo para guardar las observaciones 
	 * medicas de los antecedentes personales
	 * medicos.   
	 */
	private String observacionesPersonales;
	
	/**
	 * Campo para guardar las nuevas observaciones 
	 * medicas de los antecedentes personales medicos.   
	 */
	private String observacionesPersonalesNueva;
	
	
	/** Campo para guardar las observaciones 
	 *  medicas de los antecedentes familiares
	 *  medicos.   
	 */
	private String observacionesFamiliares;
	
	/**
	 * Campo para guardar las nuevas observaciones 
	 * medicas de los antecedentes familiares medicos.   
	 */
	private String observacionesFamiliaresNueva;
	
	/**
	 * variable Utilizada para redireccionar a otras funcionalidades 
	 */
	private String paginaSiguiente;

	
	/**
     * Metodo reset para inicializar las variables 
     */
	public void reset()
	{
	  this.codAuxPaciente = -1;
	  this.mapa = new HashMap();	

  	  listadoEnferPerso = new ArrayList(); 
	  listadoEnferOftaPersoPaciente = new ArrayList();  
	  listadoEnferOftaPersoQuirurPaciente = new ArrayList();  
	  listadoEnferFam  = new ArrayList(); 
	  listadoEnferOftaFam  = new ArrayList(); 
	  listadoParentesco = new ArrayList(); 
	  listadoEnferFamPadece  = new ArrayList();
	  
	  otroEnferPerso = "";
	  otroDesdeCuando = "";
	  otroTratamiento = "";
	  observacionesPersonales  = "";
	  observacionesPersonalesNueva  = "";
	  observacionesFamiliares  = "";
	  observacionesFamiliaresNueva  = "";
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
		ActionErrors errores = new ActionErrors();
		
		errores=super.validate(mapping,request);

	/*	Connection con=null;
		try
		{
		   con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
		}
		catch(SQLException e)
		{
			logger.warn("No se pudo abrir la conexión"+e.toString());
		}
			
		HttpSession session=request.getSession();
		PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			
			try
			{
				UtilidadBD.closeConnection(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error cerrando la conexión : "+e1);
			}*/

		
		if(!errores.isEmpty())
		{
			if(estado.equals("salir"))
				this.setEstado("empezar");
		}
		return errores;
	}

	
	
	//------------------METODOS BASICOS SET's Y GET's

	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaCompleto()
	{
		return mapa;
	}
	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapaCompleto(HashMap mapaAntecedentes)
	{
		this.mapa = mapaAntecedentes;
	}
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	@SuppressWarnings("unchecked")
	public void setMapa(String key, Object value)
	{
		this.mapa.put(key, value);
	}

	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna listadoEnferPerso.
	 */
	public Collection getListadoEnferPerso() {
		return listadoEnferPerso;
	}
	/**
	 * @param Asigna listadoEnferPerso.
	 */
	public void setListadoEnferPerso(Collection listadoEnferPerso) {
		this.listadoEnferPerso = listadoEnferPerso;
	}
	/**
	 * @return Retorna temp.
	 */
	public String getTemp() {
		return temp;
	}
	/**
	 * @param Asigna temp.
	 */
	public void setTemp(String temp) {
		this.temp = temp;
	}
	/**
	 * @return Retorna codAuxPaciente.
	 */
	public int getCodAuxPaciente() {
		return codAuxPaciente;
	}
	/**
	 * @param Asigna codAuxPaciente.
	 */
	public void setCodAuxPaciente(int codAuxPaciente) {
		this.codAuxPaciente = codAuxPaciente;
	}
	/**
	 * @return Retorna otroDesdeCuando.
	 */
	public String getOtroDesdeCuando() {
		return otroDesdeCuando;
	}
	/**
	 * @param Asigna otroDesdeCuando.
	 */
	public void setOtroDesdeCuando(String otroDesdeCuando) {
		this.otroDesdeCuando = otroDesdeCuando;
	}
	/**
	 * @return Retorna otroEnferPerso.
	 */
	public String getOtroEnferPerso() {
		return otroEnferPerso;
	}
	/**
	 * @param Asigna otroEnferPerso.
	 */
	public void setOtroEnferPerso(String otroEnferPerso) {
		this.otroEnferPerso = otroEnferPerso;
	}
	/**
	 * @return Retorna otroTratamiento.
	 */
	public String getOtroTratamiento() {
		return otroTratamiento;
	}
	/**
	 * @param Asigna otroTratamiento.
	 */
	public void setOtroTratamiento(String otroTratamiento) {
		this.otroTratamiento = otroTratamiento;
	}
	/**
	 * @return Retorna listadoEnferOftaPersoPaciente.
	 */
	public Collection getListadoEnferOftaPersoPaciente() {
		return listadoEnferOftaPersoPaciente;
	}
	/**
	 * @param Asigna listadoEnferOftaPersoPaciente.
	 */
	public void setListadoEnferOftaPersoPaciente(Collection listadoEnferOftaPersoPaciente) {
		this.listadoEnferOftaPersoPaciente = listadoEnferOftaPersoPaciente;
	}
	/**
	 * @return Retorna listadoEnferOftaPersoQuirurPaciente.
	 */
	public Collection getListadoEnferOftaPersoQuirurPaciente() {
		return listadoEnferOftaPersoQuirurPaciente;
	}
	/**
	 * @param Asigna listadoEnferOftaPersoQuirurPaciente.
	 */
	public void setListadoEnferOftaPersoQuirurPaciente(
			Collection listadoEnferOftaPersoQuirurPaciente) {
		this.listadoEnferOftaPersoQuirurPaciente = listadoEnferOftaPersoQuirurPaciente;
	}
	/**
	 * @return Retorna observacionesPersonales.
	 */
	public String getObservacionesPersonales() {
		return observacionesPersonales;
	}
	/**
	 * @param Asigna observacionesPersonales.
	 */
	public void setObservacionesPersonales(String observacionesPersonales) {
		this.observacionesPersonales = observacionesPersonales;
	}
	/**
	 * @return Retorna observacionesPersonalesNueva.
	 */
	public String getObservacionesPersonalesNueva() {
		return observacionesPersonalesNueva;
	}
	/**
	 * @param Asigna observacionesPersonalesNueva.
	 */
	public void setObservacionesPersonalesNueva(
			String observacionesPersonalesNueva) {
		this.observacionesPersonalesNueva = observacionesPersonalesNueva;
	}
	/**
	 * @return Retorna listadoEnferFam.
	 */
	public Collection getListadoEnferFam() {
		return listadoEnferFam;
	}
	/**
	 * @param Asigna listadoEnferFam.
	 */
	public void setListadoEnferFam(Collection listadoEnferFam) {
		this.listadoEnferFam = listadoEnferFam;
	}
	/**
	 * @return Retorna listadoEnferOftaFam.
	 */
	public Collection getListadoEnferOftaFam() {
		return listadoEnferOftaFam;
	}
	/**
	 * @param Asigna listadoEnferOftaFam.
	 */
	public void setListadoEnferOftaFam(Collection listadoEnferOftaFam) {
		this.listadoEnferOftaFam = listadoEnferOftaFam;
	}
	/**
	 * @return Retorna observacionesFamiliares.
	 */
	public String getObservacionesFamiliares() {
		return observacionesFamiliares;
	}
	/**
	 * @param Asigna observacionesFamiliares.
	 */
	public void setObservacionesFamiliares(String observacionesFamiliares) {
		this.observacionesFamiliares = observacionesFamiliares;
	}
	/**
	 * @return Retorna observacionesFamiliaresNueva.
	 */
	public String getObservacionesFamiliaresNueva() {
		return observacionesFamiliaresNueva;
	}
	/**
	 * @param Asigna observacionesFamiliaresNueva.
	 */
	public void setObservacionesFamiliaresNueva(
			String observacionesFamiliaresNueva) {
		this.observacionesFamiliaresNueva = observacionesFamiliaresNueva;
	}
	/**
	 * @return Retorna listadoParentesco.
	 */
	public Collection getListadoParentesco() {
		return listadoParentesco;
	}
	/**
	 * @param Asigna listadoParentesco.
	 */
	public void setListadoParentesco(Collection listadoParentesco) {
		this.listadoParentesco = listadoParentesco;
	}
	/**
	 * @return Retorna listadoEnferFamPadece.
	 */
	public Collection getListadoEnferFamPadece() {
		return listadoEnferFamPadece;
	}
	/**
	 * @param Asigna listadoEnferFamPadece.
	 */
	public void setListadoEnferFamPadece(Collection listadoEnferFamPadece) {
		this.listadoEnferFamPadece = listadoEnferFamPadece;
	}
	
	/**
	 * @return Retorna paginaSiguiente.
	 */
	public String getPaginaSiguiente() {
		return paginaSiguiente;
	}
	/**
	 * @param Asigna paginaSiguiente.
	 */
	public void setPaginaSiguiente(String paginaSiguiente) {
		this.paginaSiguiente = paginaSiguiente;
	}
	
}
