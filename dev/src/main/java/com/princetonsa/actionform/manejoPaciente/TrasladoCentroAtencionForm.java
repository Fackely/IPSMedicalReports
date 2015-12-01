/*
 * Sep 28, 06 
 */
package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *	Traslado Centro de Atención 
 */
public class TrasladoCentroAtencionForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Codigo Institucion
	 */
	private String institucion;
	
	/**
	 * Nuevo centro de atencion
	 */
	private String nuevoCentroAtencion;
	
	/**
	 * centro Atención
	 */
	private String centroAtencion;
	
	/**
	 * Nombre del nuevo Centro de Atencion
	 */
	private String nombreNuevoCentroAtencion;
	
	/**
	 * Nueva área
	 */
	private String nuevaArea;
	
	/**
	 * Nombre de nueva área
	 */
	private String nombreNuevaArea;
	
	/**
	 * Via ingreso del paciente en sesion
	 */
	private String viaIngreso;
	
	/**
	 * cama Reservada
	 */
	private String reserva="";
	
	/**
	 * Tipo Paciente
	 */
	private String tipoPaciente="";
	
	/**
	 * Indice cama Vasia
	 */
	private int key;
	
	//*******ATRIBUTOS DE LA ADMISIÓN DE HOSPITALIZACION*************
	/**
	 * Objeto donde se almacena los datos de la cama actual
	 */
	private HashMap camaActual = new HashMap();
	
	private String fechaTraslado;
	private String horaTraslado;
	private String fechaAdmision;
	private String horaAdmision;
	private String sexo;
	private String edad;
	
	private String datosCama;
	private HashMap camaNueva = new HashMap();
	
	
	private String observaciones;
	//****************************************************************
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.institucion = "";
		this.nuevoCentroAtencion = "";
		this.centroAtencion = "";
		this.nombreNuevoCentroAtencion = "";
		this.nuevaArea = "";
		this.nombreNuevaArea = "";
		this.viaIngreso = "";
		this.key=0;
		
		this.resetHospitalizacion();
	}
	
	public void resetHospitalizacion()
	{
		//atributos de hospitalizacion***************
		this.camaActual = new HashMap();
		this.fechaTraslado = "";
		this.horaTraslado = "";
		this.fechaAdmision = "";
		this.horaAdmision = "";
		this.sexo = "";
		this.edad = "";
		
		this.datosCama = "";
		this.camaNueva = new HashMap();
		this.observaciones = "";
		
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
		
		if(this.estado.equals("guardar"))
		{
			//Sólo se hace revisión para la via de ingreso hospitalizacion
			if(this.viaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
			{
				//validar fecha/hora traslado*************************************
				boolean exitoFecha = true;
				boolean exitoHora = true;
				if(this.fechaTraslado.equals(""))
				{
					errores.add("Fecha Traslado",new ActionMessage("errors.required","La fecha del traslado"));
					exitoFecha = false;
				}
				else if(!UtilidadFecha.validarFecha(this.fechaTraslado))
				{
					errores.add("fecha traslado", new ActionMessage("errors.formatoFechaInvalido",this.getFechaTraslado()));
					exitoFecha = false;
				}
				
				if(this.horaTraslado.equals(""))
				{
					errores.add("Hora Traslado",new ActionMessage("errors.required","La hora del traslado"));
					exitoHora = false;
				}
				else if(!UtilidadFecha.validacionHora(this.horaTraslado).puedoSeguir)
				{
					errores.add("fecha traslado", new ActionMessage("errors.formatoHoraInvalido",this.getHoraTraslado()));
					exitoHora = false;
				}
				if(exitoFecha)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado()))>0)
					{
						errores.add("Fecha Traslado", new ActionMessage("errors.fechaAnteriorIgualActual"," del Traslado ", " de la Admisión"));
					}
					else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaAdmision())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado()))==0&&exitoHora)
					{
						if(!UtilidadFecha.compararFechas(this.fechaTraslado,this.horaTraslado,this.fechaAdmision,this.horaAdmision).isTrue()) 
							errores.add("Fecha Traslado", new ActionMessage("errors.fechaHoraAnteriorIgualActual"," del Traslado ", " de la Admisión"));
					}
					
					if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))>0)
					{
						errores.add("Fecha Traslado mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual"," del Traslado ", " Actual"));
					}
					else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaTraslado().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))==0&&exitoHora)
					{
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),this.fechaTraslado,this.horaTraslado).isTrue()) 
							errores.add("Fecha Traslado", new ActionMessage("errors.fechaHoraPosteriorIgualActual"," del Traslado ", " Actual"));
					}
					
				}
				if(this.datosCama.equals("") && !this.tipoPaciente.equals("C"))
					errores.add("cama requerida",new ActionMessage("errors.required","La cama"));
				
				if(!errores.isEmpty())
					this.estado = "parametrizar";
			}
		}
		
		return errores;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return Returns the nuevoCentroAtencion.
	 */
	public String getNuevoCentroAtencion() {
		return nuevoCentroAtencion;
	}

	/**
	 * @param nuevoCentroAtencion The nuevoCentroAtencion to set.
	 */
	public void setNuevoCentroAtencion(String nuevoCentroAtencion) {
		this.nuevoCentroAtencion = nuevoCentroAtencion;
	}

	/**
	 * @return Returns the nuevaArea.
	 */
	public String getNuevaArea() {
		return nuevaArea;
	}

	/**
	 * @param nuevaArea The nuevaArea to set.
	 */
	public void setNuevaArea(String nuevaArea) {
		this.nuevaArea = nuevaArea;
	}

	/**
	 * @return Returns the viaIngreso.
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return Returns the camaActual.
	 */
	public HashMap getCamaActual() {
		return camaActual;
	}

	/**
	 * @param camaActual The camaActual to set.
	 */
	public void setCamaActual(HashMap camaActual) {
		this.camaActual = camaActual;
	}
	
	/**
	 * @return Retorna un elemento del mapa camaActual.
	 */
	public Object getCamaActual(String key) {
		return camaActual.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa camaActual.
	 */
	public void setCamaActual(String key, Object obj) {
		this.camaActual.put(key,obj);
	}

	/**
	 * @return Returns the fechaAdmision.
	 */
	public String getFechaAdmision() {
		return fechaAdmision;
	}

	/**
	 * @param fechaAdmision The fechaAdmision to set.
	 */
	public void setFechaAdmision(String fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}

	/**
	 * @return Returns the fechaTraslado.
	 */
	public String getFechaTraslado() {
		return fechaTraslado;
	}

	/**
	 * @param fechaTraslado The fechaTraslado to set.
	 */
	public void setFechaTraslado(String fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}

	/**
	 * @return Returns the horaAdmision.
	 */
	public String getHoraAdmision() {
		return horaAdmision;
	}

	/**
	 * @param horaAdmision The horaAdmision to set.
	 */
	public void setHoraAdmision(String horaAdmision) {
		this.horaAdmision = horaAdmision;
	}

	/**
	 * @return Returns the horaTraslado.
	 */
	public String getHoraTraslado() {
		return horaTraslado;
	}

	/**
	 * @param horaTraslado The horaTraslado to set.
	 */
	public void setHoraTraslado(String horaTraslado) {
		this.horaTraslado = horaTraslado;
	}

	/**
	 * @return Returns the datosCama.
	 */
	public String getDatosCama() {
		return datosCama;
	}

	/**
	 * @param datosCama The datosCama to set.
	 */
	public void setDatosCama(String datosCama) {
		this.datosCama = datosCama;
	}

	/**
	 * @return Returns the edad.
	 */
	public String getEdad() {
		return edad;
	}

	/**
	 * @param edad The edad to set.
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}

	/**
	 * @return Returns the sexo.
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo The sexo to set.
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	
           

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the nombreNuevaArea.
	 */
	public String getNombreNuevaArea() {
		return nombreNuevaArea;
	}

	/**
	 * @param nombreNuevaArea The nombreNuevaArea to set.
	 */
	public void setNombreNuevaArea(String nombreNuevaArea) {
		this.nombreNuevaArea = nombreNuevaArea;
	}

	/**
	 * @return Returns the nombreNuevoCentroAtencion.
	 */
	public String getNombreNuevoCentroAtencion() {
		return nombreNuevoCentroAtencion;
	}

	/**
	 * @param nombreNuevoCentroAtencion The nombreNuevoCentroAtencion to set.
	 */
	public void setNombreNuevoCentroAtencion(String nombreNuevoCentroAtencion) {
		this.nombreNuevoCentroAtencion = nombreNuevoCentroAtencion;
	}

	/**
	 * @return Returns the centroAtencion.
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the camaNueva
	 */
	public HashMap getCamaNueva() {
		return camaNueva;
	}

	/**
	 * @param camaNueva the camaNueva to set
	 */
	public void setCamaNueva(HashMap camaNueva) {
		this.camaNueva = camaNueva;
	}
	
	/**
	 * @return the camaNueva
	 */
	public Object getCamaNueva(String key) {
		return camaNueva.get(key);
	}

	/**
	 * @param Asigna elemento al mapa camaNueva the camaNueva to set
	 */
	public void setCamaNueva(String key,Object obj) {
		this.camaNueva.put(key, obj);
	}

	public String getReserva() {
		return reserva;
	}

	public void setReserva(String reserva) {
		this.reserva = reserva;
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

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}
}
