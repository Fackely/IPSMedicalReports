package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SalidaHospitalDiaForm extends ValidatorForm {
	
	/**
	 * Estado dentro de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa que contiene los pacientes registrados como permanencia diurna
	 */
	private HashMap salidaHospitalDiaMap;
	
	/**
	 * 
	 */
	private HashMap tiposIdentificacionTagMap;
	
	/**
	 * 
	 */
	private String codigoServicio;
	
	
	/**
	 * Campo que guarda el servicio de permanencia diurna
	 */
	private String servicio;
	
	/**
	 * Almacena la cuenta que sera guardada en la salida del hospital dia
	 */
	private int cuenta;
	
	/**
	 * Almacena el ingreso del paciente al que se le realizara la salida diaria
	 */
	private int ingreso;
	
	/**
	 * Almacena las observaciones sobre la salida del paciente
	 */
	private String observaciones;
	
	/**
	 * Almacena el numero de solicitud del paciente
	 */
	private int numSolicitud;
	
	/**
	 * 
	 */
	private String tipoIdentificacion;
	
	/**
	 * 
	 */
	private String numeroIdentificacion;
	
	/**
	 * 
	 */
	private String primerNombre;
	
	/**
	 * 
	 */
	private String primerApellido;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private String nombreCentroAtencion;
	
	/**
	 * Se utiliza para la navegacion de pager
	 */
	private String linkSiguiente;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private int indexSeleccionado;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Resetea los atributos de la forma
	 *
	 */
	public void reset(int codigoInstitucion)
	{
		this.salidaHospitalDiaMap=new HashMap();
		this.salidaHospitalDiaMap.put("numRegistros", "0");
		
		this.inicializarTagMap(codigoInstitucion);
		
		this.servicio="";
		this.codigoServicio="";
		this.cuenta=ConstantesBD.codigoNuncaValido;
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.observaciones="";
		this.numSolicitud=ConstantesBD.codigoNuncaValido;
		this.fecha="";
		this.hora="";
		this.nombreCentroAtencion="";
		this.linkSiguiente="";
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.maxPageItems=10;
    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 *
	 */
	public void resetCriteriosBusqueda()
    {
    	this.tipoIdentificacion="";
    	this.numeroIdentificacion="";
    	this.primerNombre="";
    	this.primerApellido="";
    }
	
	/**
	 * 
	 * @param codigoInstitucion
	 */
	public void inicializarTagMap(int codigoInstitucion)
    {
    	this.tiposIdentificacionTagMap= Utilidades.consultarTiposidentificacion(codigoInstitucion);
    }
	
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			if(this.codigoServicio.equals("")||this.codigoServicio.equals("null")||this.codigoServicio==null)
			{
				errores.add("servicio",new ActionMessage("errors.required","El servicio "));
			}
		}
		return errores;
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
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
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
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the cuenta
	 */
	public int getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the numSolicitud
	 */
	public int getNumSolicitud() {
		return numSolicitud;
	}

	/**
	 * @param numSolicitud the numSolicitud to set
	 */
	public void setNumSolicitud(int numSolicitud) {
		this.numSolicitud = numSolicitud;
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
	 * @return the salidaHospitalDiaMap
	 */
	public HashMap getSalidaHospitalDiaMap() {
		return salidaHospitalDiaMap;
	}

	/**
	 * @param salidaHospitalDiaMap the salidaHospitalDiaMap to set
	 */
	public void setSalidaHospitalDiaMap(HashMap salidaHospitalDiaMap) {
		this.salidaHospitalDiaMap = salidaHospitalDiaMap;
	}
	
	/**
	 * @return the mapa
	 */
	public Object getSalidaHospitalDiaMap(Object key) {
		return salidaHospitalDiaMap.get(key);
	}

	/**
	 * @param mapa the mapa to set
	 */
	public void setSalidaHospitalDiaMap(Object key, Object value) {
		this.salidaHospitalDiaMap.put(key, value);
	}

	/**
	 * @return the indexSeleccionado
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}

	/**
	 * @param indexSeleccionado the indexSeleccionado to set
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
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
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the tiposIdentificacionTagMap
	 */
	public HashMap getTiposIdentificacionTagMap() {
		return tiposIdentificacionTagMap;
	}

	/**
	 * @param tiposIdentificacionTagMap the tiposIdentificacionTagMap to set
	 */
	public void setTiposIdentificacionTagMap(HashMap tiposIdentificacionTagMap) {
		this.tiposIdentificacionTagMap = tiposIdentificacionTagMap;
	}


	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

}
