/*
 * @(#)AdmisionUrgenciasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.actionform;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * <code>ValidatorForm</code> para el ingreso de datos de <code>AdmisionUrgencias</code>
 *
 * @version 1.0, Mar 10, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class AdmisionUrgenciasForm extends ValidatorForm 
{
	/**
	 * Para hacer los logs de la aplicaciÛn
	 */
	protected Logger logger = Logger.getLogger(AdmisionUrgenciasForm.class);
	/**
	 * Es el codigo del centro de costo de las cama de observacion y medicos de urgencias
	 */
	private String centroCosto = "";
	/**
	 * Boolean que indica si se puede modificar informacion de cama de observaciÛn
	 */
	private boolean modificarCama = false;
	/**
	 * Boolean que indica si se deben mostrar datos de Cama de observaciÛn
	 */
	private boolean mostrarDatosCama = false;
	/**
	 * Campo que determina si es una forma de ingreso o de modificacion de datos
	 */
	private String accion = "";

	/**
	 * Fecha en la que se efect√∫a esta admisi√≥n, formato dd/MM/yyyy
	 */
	private String fecha = "";

	/**
	 * Hora en la que se efect√∫a esta admisi√≥n, formato HH:mm
	 */
	private String hora = "";

	/**
	 * Pareja codigo-nombre del origen de esta admisi√≥n.
	 */
	private String origen_cn = "";

	/**
	 * Pareja codigo-nombre de la causa externa de esta admisi√≥n.
	 */
	private String causaExterna_cn = "";

	/**
	 * N√∫mero de autorizaci√≥n de esta admisi√≥n.
	 */
	private String numeroAutorizacion = "";

	/**
	 * Pareja tipoId-numeroId que identifican el m√©dico que efect√∫a esta admisi√≥n.
	 */
	private String medico_cn = "";

	/**
	 * Pareja codigo-nombre que identifican la cama de observaci√≥n.
	 */
	private String cama_cn = "";

	/**
	 * Fecha en la que se efect√∫a el ingreso a una cama de observacion, formato dd/MM/yyyy.
	 */
	private String fechaObservacion = "";

	/**
	 * Hora en la que se efect√∫a el ingreso a una cama de observacion, formato HH:mm.
	 */
	private String horaObservacion = "";

	/**
	 * Fecha en la que se efect˙a el egreso de observacion, formato dd/MM/yyyy.
	 */
	private String fechaEgresoObservacion = "";

	/**
	 * Hora en la que se efect˙a el egreso de observacion, formato HH:mm.
	 */
	private String horaEgresoObservacion = "";
	
	private boolean pacienteReferido;

	/**
	 * consecutivo triage
	 */
	private String consecutivoTriage="";
	
	/**
	 * consecutivo fecha triage
	 */
	private String consecutivoFechaTriage="";
	
	/**
	 * CÛdigo del tipo de evento
	 */
	private String codigoTipoEvento;
	
	/**
	 * Variable que indica si debo abrir referencia
	 */
	private boolean deboAbrirReferencia;
	
	
	/**
	 * Returns the accion.
	 * @return String
	 */
	public String getAccion() {		
		return accion;
	}

	/**
	 * Retorna el codigo-nombre de la causa externa de esta admisi√≥n.
	 * @return el codigo-nombre de la causa externa de esta admisi√≥n
	 */
	public String getCausaExterna_cn() {
		return causaExterna_cn;
	}

	/**
	 * Retorna la fecha en que se efectu√≥ esta admisi√≥n.
	 * @return la fecha en que se efectu√≥ esta admisi√≥n
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Retorna la hora en que se efectu√≥ esta admisi√≥n.
	 * @return la hora en que se efectu√≥ esta admisi√≥n
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Retorna el tipoId-numeroId del m√©dico que efect√∫a esta admisi√≥n.
	 * @return el tipoId-numeroId del m√©dico que efect√∫a esta admisi√≥n
	 */
	public String getMedico_cn() {
		return medico_cn;
	}

	/**
	 * Retorna el codigo-nombre del origen de esta admisi√≥n.
	 * @return el codigo-nombre del origen de esta admisi√≥n
	 */
	public String getOrigen_cn() {
		return origen_cn;
	}

	/**
	 * Retorna la cama.
	 * @return String
	 */
	public String getCama_cn() {
		return cama_cn;
	}

	/**
	 * Retorna la fechaObservacion.
	 * @return String
	 */
	public String getFechaObservacion() {
		return fechaObservacion;
	}

	/**
	 * Retorna la horaObservacion.
	 * @return String
	 */
	public String getHoraObservacion() {
		return horaObservacion;
	}

	/**
	 * Sets the accion.
	 * @param accion The accion to set
	 */
	public void setAccion(String accion) {				
		this.accion = accion;
	}

	/**
	 * Establece la causa externa.
	 * @param causaExterna la causa externa a establecer
	 */
	public void setCausaExterna_cn(String causaExterna) {
		this.causaExterna_cn = causaExterna;
	}

	/**
	 * Establece el c√≥digo de origen.
	 * @param codigoOrigen el c√≥digo de origen a establecer
	 */
	final public void setOrigen_cn(String codigoOrigen) {
		this.origen_cn = codigoOrigen;
	}

	/**
	 * Establece la fecha.
	 * @param fecha la fecha a establecer
	 */
	final public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Establece la hora.
	 * @param hora la hora a establecer
	 */
	final public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * Establece el m√©dico.
	 * @param medico_cn el m√©dico a establecer
	 */
	public void setMedico_cn(String medico_cn) {
		this.medico_cn = medico_cn;
	}

	/**
	 * Establece la cama.
	 * @param cama la cama
	 */
	public void setCama_cn(String cama_cn) {

		if (cama_cn != null) {

			cama_cn = cama_cn.trim();
//			if (!cama_cn.equals("") && !cama_cn.equals("ninguna")) {				
				this.cama_cn = cama_cn;
//			}
//			else {
//				this.cama_cn = null;
//			}

		}

		else {
			this.cama_cn = null;
		}

	}

	/**
	 * Establece la fechaObservacion.
	 * @param fechaObservacion la fechaObservacion
	 */
	public void setFechaObservacion(String fechaObservacion) {

		if (fechaObservacion != null) {

			fechaObservacion = fechaObservacion.trim();
			if (!fechaObservacion.equals("")) {
				this.fechaObservacion = fechaObservacion;
			}
			else {
				this.fechaObservacion = null;
			}

		}

		else {
			this.fechaObservacion = null;
		}

	}

	/**
	 * Establece la horaObservacion.
	 * @param horaObservacion la horaObservacion
	 */
	public void setHoraObservacion(String horaObservacion) {

		if (horaObservacion != null) {

			horaObservacion = horaObservacion.trim();
			if (!horaObservacion.equals("")) {
				this.horaObservacion = horaObservacion;
			}
			else {
				this.horaObservacion = null;
			}

		}

		else {
			this.horaObservacion = null;
		}

	}

	/**
	 * Este m√©todo inicializa en valores nulos los atributos de la admisi√≥n.
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param request el <i>servlet request</i> que est√° siendo procesado en este momento
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {

		if (mapping == null || request == null); // NOP para evitar el warning "The argument mapping / request is never read"

		//this.accion = null;
		this.fecha = null;
		this.hora = null;
		this.medico_cn = null;
		this.numeroAutorizacion = null;
		this.causaExterna_cn = null;
		this.origen_cn = null;
		this.cama_cn = null;
		this.fechaObservacion = null;
		this.horaObservacion = null;
		this.modificarCama = false;
		this.mostrarDatosCama = false;	
		this.pacienteReferido = false;
		
		this.deboAbrirReferencia = false;

	}

	/**
	 * Valida las propiedades que han sido establecidas para este request HTTP,
	 * y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	 * validaci√≥n encontrados. Si no se encontraron errores de validaci√≥n, retorna
	 * <code>null</code>.
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param request el <i>servlet request</i> que est√° siendo procesado en este momento
	 * @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	 * o <code>null</code> si no se encontraron errores.
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request) {

		// Primero validamos usando las validaciones est√°ndar definidas en validation.xml
		ActionErrors errors = new ActionErrors();

		// Fecha actual y patr√≥n de fecha a utilizar en las validaciones
		String fechaActual = UtilidadFecha.getFechaActual();
		String horaActual = UtilidadFecha.getHoraActual();
		
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");

		
		
		
		// Fecha de la admisi√≥n
		try {
			Date fechaAdmision = dateFormatter.parse(this.fecha);
		}	catch (java.text.ParseException e) {
				errors.add("fecha", new ActionMessage("errors.formatoFechaInvalido", "de admisiÛn"));
		}

		// Fecha y Hora de la admisi√≥n
		try {
			Date fechaHoraAdmision = dateTimeFormatter.parse(this.fecha + ":" + this.hora);
		}	catch (java.text.ParseException e) {
				errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de admisiÛn"));
		}

		// Validar que la fecha ingresada no sea superior a la fecha actual
		if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fecha, fechaActual)) {
			errors.add("fecha", new ActionMessage("errors.fechaPosteriorIgualActual", "de admisiÛn", "actual"));
		}

		// Validar que si la fecha ingresada es igual a la fecha actual, la hora ingresada no sea superior a la hora actual
		else if (!UtilidadFecha.compararFechas(fechaActual, horaActual, this.fecha, this.hora).isTrue()) {
			errors.add("hora", new ActionMessage("errors.horaSuperiorA", "de admisiÛn", "actual"));
		}
		//Verificamos que si hay datos de egreso de observaciÛn, se debe tener todos los datos de ingreso a observaciÛn
		if ((this.fechaEgresoObservacion != null && this.horaEgresoObservacion != null)&& (!this.fechaEgresoObservacion.equals("") && !this.horaEgresoObservacion.equals(""))) {			
			if ((this.fechaObservacion == null || this.horaObservacion == null || this.cama_cn == null) || (this.horaObservacion != null && this.fechaObservacion.equals("")) || (this.horaObservacion != null && this.horaObservacion.equals("")) || (this.cama_cn != null && this.cama_cn.equals(""))) {
				errors.add("fechaObservacion", new ActionMessage("errors.datosFaltantes.observacion"));
			}
		}

		logger.info("validate fechaObservacion: "+this.fechaObservacion);
		logger.info("validate horaObservacion: "+this.horaObservacion);
		
		// Las siguientes validaciones las efectuamos ˙nicamente si se entrÛ a observaciÛn			
		if (this.fechaObservacion != null && this.horaObservacion != null) 
		{
			
			this.cama_cn=this.cama_cn.equals("-1")?"":this.cama_cn;
			
			
			//Si no hay cama pero hay fecha / hora observacion la cama es requerida
			/**if(!this.fechaObservacion.equals("")&&!this.horaObservacion.equals("")&&this.cama_cn.equals(""))
				errors.add("Cama requerida",new ActionMessage("errors.required","La cama"));**/
			
			if((this.fechaObservacion.equals("")||this.horaObservacion.equals("")))
			{
				if(!this.cama_cn.equals(""))
					errors.add("Cama requerida",new ActionMessage("errors.required","Debido a que existe cama seleccionada, la fecha/hora ingreso observaciÛn"));
				else
					errors.add("Fecha/hora observacion requerida",new ActionMessage("errors.required","La fecha/hora ingreso observaciÛn"));
			}

			// Fecha de la admisiÛn a observaciÛn
			try {
				Date fechaObservacion = dateFormatter.parse(this.fechaObservacion);
			}	catch (java.text.ParseException e) {
					errors.add("fechaObservacion", new ActionMessage("errors.formatoFechaInvalido", "de ingreso a observaciÛn"));
			}

			// Hora de la admisiÛn a observaciÛn
			try {
				Date fechaHoraObservacion = dateTimeFormatter.parse(this.fechaObservacion + ":" + this.horaObservacion);
			}	catch (java.text.ParseException e) {
					errors.add("horaObservacion", new ActionMessage("errors.formatoHoraInvalido", "de ingreso a observaciÛn"));
			}

			// Validar que la fecha de observaciÛn, si fue ingresada, no sea superior a la fecha actual
			if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaObservacion, fechaActual)) {
				errors.add("fechaObservacion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de ingreso a observaciÛn", "actual ("+fechaActual+")"));
			}

			// Validar que si la fecha de observacion ingresada es igual a la fecha actual, la hora de observaci√≥n ingresada no sea superior a la hora actual
			else if (!UtilidadFecha.compararFechas(fechaActual, horaActual, this.fechaObservacion, this.horaObservacion).isTrue()) {
				errors.add("horaObservacion", new ActionMessage("errors.horaSuperiorA", "de ingreso a observaciÛn", "actual ("+fechaActual+" - "+horaActual+")"));
			}

			// Validar que la fecha de observaciÛn sea mayor o igual que la fecha de admisi√≥n
			if (UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaObservacion, this.fecha)) {
				errors.add("fechaObservacion", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de ingreso a observaciÛn", "de admisiÛn ("+this.fecha+")"));
			}

			// Validar que si la fecha de observacion ingresada es igual a la fecha de admisi√≥n, la hora de observaci√≥n sea mayor o igual que la hora de admisi√≥n
			else if (!UtilidadFecha.compararFechas(this.fechaObservacion, this.horaObservacion, this.fecha, this.hora).isTrue()) {
				errors.add("horaObservacion", new ActionMessage("errors.horaSuperiorA", "de ingreso a observaciÛn", "de admisiÛn a urgencias ("+this.fecha+" - "+this.hora+")"));
			}
			//Las siguientes son las validaciones para la fecha y hora de egreso de observaciÛn
			if ((this.fechaEgresoObservacion != null && this.horaEgresoObservacion != null)&& (!this.fechaEgresoObservacion.equals("") && !this.horaEgresoObservacion.equals(""))) {
				// Fecha egreso de observaciÛn
				Date fechaEgresoObservacion = null;
				try {
					fechaEgresoObservacion = dateFormatter.parse(this.fechaEgresoObservacion);
				}	catch (java.text.ParseException e) {
						errors.add("fechaEgresoObservacion", new ActionMessage("errors.formatoFechaInvalido", "de Egreso de Observacion"));
				}

				// Hora de la admisiÛn a observaciÛn
				Date fechaHoraEgresoObservacion = null;
				try {
					fechaHoraEgresoObservacion = dateTimeFormatter.parse(this.fechaEgresoObservacion + ":" + this.horaEgresoObservacion);
				}	catch (java.text.ParseException e) {
						errors.add("horaEgresoObservacion", new ActionMessage("errors.formatoHoraInvalido", "de Egreso de observaciÛn"));
				}

				// Validar que la fecha de egreso de observaciÛn, si fue ingresada, no sea superior a la fecha actual
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual, this.fechaEgresoObservacion) ) {
					errors.add("fechaEgresoObservacion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de egreso de observaciÛn", "actual"));
				}

				// Validar que si la fecha de egreso de observacion ingresada es igual a la fecha actual, la hora de egreso de observaciÛn ingresada no sea superior a la hora actual
				else if (!UtilidadFecha.compararFechas(this.fechaEgresoObservacion, this.horaEgresoObservacion, fechaActual, horaActual).isTrue()) 
				{
					errors.add("horaEgresoObservacion", new ActionMessage("errors.horaSuperiorA", "de egreso de observaciÛn", "de ingreso a observaciÛn"));
				}

				// Validar que la fecha de egreso de observaciÛn sea mayor o igual que la fecha de ingreso a observacion
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaEgresoObservacion, this.fechaObservacion)) {
					errors.add("fechaEgresoObservacion", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de egreso de observaciÛn", "de ingreso a observaciÛn"));
				}

				// Validar que si la fecha de observacion ingresada es igual a la fecha de ingreso a observaciÛn, la hora de egreso de observaciÛn sea mayor o igual que la hora de ingreso a observaciÛn
				else if (!UtilidadFecha.compararFechas(this.fechaObservacion, this.horaObservacion, this.fechaEgresoObservacion, this.horaEgresoObservacion).isTrue()) {
					errors.add("horaEgresoObservacion", new ActionMessage("errors.horaInferiorAlaObservacion.observacion.egreso"));
				}
				
			}
		}
		else
			errors.add("Fecha/hora observacion requerida",new ActionMessage("errors.required","La fecha/hora ingreso observaciÛn"));
		
		// Por convenci√≥n, si no se encuentran errores, retornamos null
		if (errors.isEmpty()) {
			return null;
		}
		else {
			return errors;
		}

	}

	/**
	 * @return
	 */
	public boolean isModificarCama() {
		return modificarCama;
	}

	/**
	 * @param b
	 */
	public void setModificarCama(boolean b) {
		modificarCama = b;
	}

	/**
	 * @return
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param string
	 */
	public void setCentroCosto(String string) {
		centroCosto = string;
	}

	/**
	 * @return
	 */
	public String getFechaEgresoObservacion() {
		return fechaEgresoObservacion;
	}

	/**
	 * @return
	 */
	public String getHoraEgresoObservacion() {
		return horaEgresoObservacion;
	}

	/**
	 * @param string
	 */
	public void setFechaEgresoObservacion(String string) {
		fechaEgresoObservacion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraEgresoObservacion(String string) {
		horaEgresoObservacion = string;
	}

	/**
	 * @return
	 */
	public boolean isMostrarDatosCama() {
		return mostrarDatosCama;
	}

	/**
	 * @param b
	 */
	public void setMostrarDatosCama(boolean b) {
		mostrarDatosCama = b;
	}

	/**
	 * @return Returns the pacienteReferido.
	 */
	public boolean isPacienteReferido() {
		return pacienteReferido;
	}

	/**
	 * @param pacienteReferido The pacienteReferido to set.
	 */
	public void setPacienteReferido(boolean pacienteReferido) {
		this.pacienteReferido = pacienteReferido;
	}

	/**
	 * @return Returns the consecutivoFechaTriage.
	 */
	public String getConsecutivoFechaTriage() {
		return consecutivoFechaTriage;
	}

	/**
	 * @param consecutivoFechaTriage The consecutivoFechaTriage to set.
	 */
	public void setConsecutivoFechaTriage(String consecutivoFechaTriage) {
		this.consecutivoFechaTriage = consecutivoFechaTriage;
	}

	/**
	 * @return Returns the consecutivoTriage.
	 */
	public String getConsecutivoTriage() {
		return consecutivoTriage;
	}

	/**
	 * @param consecutivoTriage The consecutivoTriage to set.
	 */
	public void setConsecutivoTriage(String consecutivoTriage) {
		this.consecutivoTriage = consecutivoTriage;
	}

	/**
	 * @return the codigoTipoEvento
	 */
	public String getCodigoTipoEvento() {
		return codigoTipoEvento;
	}

	/**
	 * @param codigoTipoEvento the codigoTipoEvento to set
	 */
	public void setCodigoTipoEvento(String codigoTipoEvento) {
		this.codigoTipoEvento = codigoTipoEvento;
	}

	/**
	 * @return the deboAbrirReferencia
	 */
	public boolean isDeboAbrirReferencia() {
		return deboAbrirReferencia;
	}

	/**
	 * @param deboAbrirReferencia the deboAbrirReferencia to set
	 */
	public void setDeboAbrirReferencia(boolean deboAbrirReferencia) {
		this.deboAbrirReferencia = deboAbrirReferencia;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	

}