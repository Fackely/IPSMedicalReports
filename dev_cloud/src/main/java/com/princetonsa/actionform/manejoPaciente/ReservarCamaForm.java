/*
 * @(#)ReservarCamaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */

package com.princetonsa.actionform.manejoPaciente;



import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;



/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion de reservar cama
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Mayo 28, 2007
 * @author Julián Pacheco jpacheco@princetonsa.com
 */
public class ReservarCamaForm extends ValidatorForm 
{
	/**
	 * Estado en el que se encuentra el proceso.  
	 */
	private String estado;
		
	
	//--------Atributos de la Reserva
	/**
	 * HashMap reservaCamaMap
	 * */
	private HashMap reservarMap;
	
	/**
	 * HashMap reservaActivaMap almacen la informacion basica de la reserva
	 * */
	private HashMap reservaActivaMap;	
	//----------------------------------------			

	/**
	 * centro de atencion
	 */
	private int centroAtencion;	
	
	/**
	 * mapa para mostrar el select de los n centros de atencion
	 */
	private HashMap centrosAntencionTagMap;
	
	
	/**----------------------------------------------------------
	 * Atributo que indica de donce ha sido llamada la aplicacion
	 ----------------------------------------------------------**/
	private String origenLlamado;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	//--------- Atributos para el paciente
	private ArrayList<HashMap<String, Object>> tiposIdentificacion = new ArrayList<HashMap<String,Object>>(); //arreglo paera almacenar los tipos de identificacion
	private ArrayList<HashMap<String, Object>> sexos = new ArrayList<HashMap<String,Object>>(); //arreglo para almacenar los sexos
	
	private int codigoPaciente;
	private String anioHistoriaClinica;
	private String numeroHistoriaClinica;
	private String codigoTipoIdentificacion;
	private String nombreTipoIdentificacion;
	private String numeroIdentificacion;
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String codigoSexo;
	private String fechaNacimiento;
	private String telefono;
	private String observaciones;
	private boolean identificacionAutomatica;
	private String tipoPaciente;
	
	
//	--------------------------------METODOS---------------------------------------	
	/**
	 * resetea los atributos del form
	 *
	 */
	public void reset(int codCentroAtencion)
	{
		
		this.reservarMap = new HashMap();		
		
		this.centrosAntencionTagMap= new HashMap();
		this.centrosAntencionTagMap.put("numRegistros", 0);
		this.reservaActivaMap = new HashMap();		
		this.origenLlamado="";
		this.centroAtencion=codCentroAtencion;	
		
		//Atributos del paciente -------------------------
		this.tiposIdentificacion = new ArrayList<HashMap<String,Object>>();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		this.codigoPaciente = 0;
		this.anioHistoriaClinica = "";
		this.numeroHistoriaClinica = "";
		this.codigoTipoIdentificacion = "";
		this.nombreTipoIdentificacion = "";
		this.numeroIdentificacion = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.codigoSexo = "";
		this.fechaNacimiento = "";
		this.telefono = "";
		this.observaciones = "";
		this.identificacionAutomatica = false;
		this.tipoPaciente="";
		
	}
    
    
    
//  -----------------------------------------Validate--------------------------------------------------------    
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
	    
	    if(this.estado.equals("guardar"))
	    {
	    	
	    	//Validacion de los datos del paciente ------------------------------
	    	if(this.tipoPaciente.equals(""))
	    		errores.add("",new ActionMessage("errors.required","El tipo de paciente"));
	    	
	    	if(this.primerApellido.equals(""))
	    		errores.add("",new ActionMessage("errors.required","El primer apellido"));
	    	
	    	if(this.primerNombre.equals(""))
	    		errores.add("",new ActionMessage("errors.required","El primer nombre"));
	    	
	    	if(this.codigoSexo.equals(""))
	    		errores.add("",new ActionMessage("errors.required","El sexo"));
	    	
	    	if(this.fechaNacimiento.equals(""))
	    		errores.add("",new ActionMessage("errors.required","La fecha de nacimiento"));
	    	else
	    	{
	    		if(!UtilidadFecha.validarFecha(this.fechaNacimiento))
	    			errores.add("",new ActionMessage("errors.formatoFechaInvalido","de nacimiento"));
	    		else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaNacimiento, UtilidadFecha.getFechaActual()))
	    			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","de nacimiento","actual"));
	    	}
	    	//--------------------------------------------------------------------
	    	
	    	
	    	
	    	
	    	
	    	
	    	if(!this.getReservarMap("fechaOcupacion").toString().equals(""))
	    	{
	    		if(!UtilidadFecha.validarFecha(this.getReservarMap("fechaOcupacion").toString()))
	    			errores.add("fecha ocupacion invalida",new ActionMessage("errors.formatoFechaInvalido","de ocupación"));
	    		else if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getReservarMap("fechaOcupacion").toString(), UtilidadFecha.getFechaActual()))
					errores.add("fecha ocupacion menor que fecha actual",new ActionMessage("errors.fechaAnteriorIgualActual","de ocupación","actual"));
	    		
	    	}
	    	
	    	if(this.getReservarMap("codigoCama").equals(""))
	    		errores.add("La cama es requerida",new ActionMessage("errors.required","La cama"));
	    }
	    
	    return errores;
	}	
//--------------------------------------------Fin Validate--------------------------------------------------------
	
	
	
	
//---------------------------------Getters and Setters------------------------		

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}



	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	
	
	/**
     * inicializa los tags de la forma
     * @param codigoInstitucionInt
     */
    public void inicializarTags(int codigoInstitucion) 
    {
		this.centrosAntencionTagMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion); 
	}
    
	
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
	 * @return the centrosAntencionTagMap
	 */
	public HashMap getCentrosAntencionTagMap() {
		return centrosAntencionTagMap;
	}

	/**
	 * @param centrosAntencionTagMap the centrosAntencionTagMap to set
	 */
	public void setCentrosAntencionTagMap(HashMap centrosAntencionTagMap) {
		this.centrosAntencionTagMap = centrosAntencionTagMap;
	}
	
	/**
	 * @return the centrosAntencionTagMap
	 */
	public Object getCentrosAntencionTagMap(Object key) {
		return centrosAntencionTagMap.get(key);
	}

	/**
	 * @param centrosAntencionTagMap the centrosAntencionTagMap to set
	 */
	public void setCentrosAntencionTagMap(Object value, Object key) {
		this.centrosAntencionTagMap.put(key, value);
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	/**
	 * @return the reservarMap
	 */
	public HashMap getReservarMap() {
		return reservarMap;
	}


	/**
	 * @param reservarMap the reservarMap to set
	 */
	public void setReservarMap(HashMap reservarMap) {
		this.reservarMap = reservarMap;
	}
	
	
	/**
	 * @return the reservarMap
	 */
	public Object getReservarMap(String key) {
		return reservarMap.get(key);
	}


	/**
	 * @param reservarMap the reservarMap to set
	 */
	public void setReservarMap(String key, Object value) {
		this.reservarMap.put(key, value);
	}



	/**
	 * @return the reservaActivaMap
	 */
	public HashMap getReservaActivaMap() {
		return reservaActivaMap;
	}



	/**
	 * @param reservaActivaMap the reservaActivaMap to set
	 */
	public void setReservaActivaMap(HashMap reservaActivaMap) {
		this.reservaActivaMap = reservaActivaMap;
	}
	
	
	/**
	 * @return the reservaActivaMap
	 */
	public Object getReservaActivaMap(String key) {
		return reservaActivaMap.get(key);
	}


	/**
	 * @param reservaActivaMap the reservaActivaMap to set
	 */
	public void setReservaActivaMap(String key, Object value) {
		this.reservaActivaMap.put(key, value);
	}



	/**
	 * @return the codigoSexo
	 */
	public String getCodigoSexo() {
		return codigoSexo;
	}



	/**
	 * @param codigoSexo the codigoSexo to set
	 */
	public void setCodigoSexo(String codigoSexo) {
		this.codigoSexo = codigoSexo;
	}



	/**
	 * @return the codigoTipoIdentificacion
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}



	/**
	 * @param codigoTipoIdentificacion the codigoTipoIdentificacion to set
	 */
	public void setCodigoTipoIdentificacion(String codigoTipoIdentificacion) {
		this.codigoTipoIdentificacion = codigoTipoIdentificacion;
	}



	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}



	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}



	/**
	 * @return the nombreTipoIdentificacion
	 */
	public String getNombreTipoIdentificacion() {
		return nombreTipoIdentificacion;
	}



	/**
	 * @param nombreTipoIdentificacion the nombreTipoIdentificacion to set
	 */
	public void setNombreTipoIdentificacion(String nombreTipoIdentificacion) {
		this.nombreTipoIdentificacion = nombreTipoIdentificacion;
	}



	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}



	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}



	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}



	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}



	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}



	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}



	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}



	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}



	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}



	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}



	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}



	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}



	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	/**
	 * @return the tiposIdentificacion
	 */
	public ArrayList<HashMap<String, Object>> getTiposIdentificacion() {
		return tiposIdentificacion;
	}



	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacion(
			ArrayList<HashMap<String, Object>> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}



	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}



	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}



	/**
	 * @return the identificacionAutomatica
	 */
	public boolean isIdentificacionAutomatica() {
		return identificacionAutomatica;
	}



	/**
	 * @param identificacionAutomatica the identificacionAutomatica to set
	 */
	public void setIdentificacionAutomatica(boolean identificacionAutomatica) {
		this.identificacionAutomatica = identificacionAutomatica;
	}



	/**
	 * @return the anioHistoriaClinica
	 */
	public String getAnioHistoriaClinica() {
		return anioHistoriaClinica;
	}



	/**
	 * @param anioHistoriaClinica the anioHistoriaClinica to set
	 */
	public void setAnioHistoriaClinica(String anioHistoriaClinica) {
		this.anioHistoriaClinica = anioHistoriaClinica;
	}



	/**
	 * @return the numeroHistoriaClinica
	 */
	public String getNumeroHistoriaClinica() {
		return numeroHistoriaClinica;
	}



	/**
	 * @param numeroHistoriaClinica the numeroHistoriaClinica to set
	 */
	public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
		this.numeroHistoriaClinica = numeroHistoriaClinica;
	}



	/**
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}



	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}



	public String getOrigenLlamado() {
		return origenLlamado;
	}



	public void setOrigenLlamado(String origenLlamado) {
		this.origenLlamado = origenLlamado;
	}



	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}



	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
}