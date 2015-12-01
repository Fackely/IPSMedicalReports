/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class NotaEnfermeriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1996291525484243311L;

	/**
	 * Codigo de la nota enfermeria
	 */
	private int codigo;
	/**
	 * Numero de solicitud relacionado a la nota de enfermeria
	 */
	private int numeroSolicitud;
	/**
	 * Contenido de la nota de enfermeria
	 */
	private String descripcion;
	/**
	 * Fecha de generacion de la nota de enfermeria
	 */
	private Date fecha;
	/**
	 * Hora de generacion de la nota de enfermeria
	 */
	private String hora;
	/**
	 * Nombre de Responsable que genero la nota
	 */
	private String enfermera;

	/**
	 * Cdigo de Responsable que genera la nota
	 */
	private int codEnfermera;
	/**
	 * Codigo de la institucion del profesional que genra la nota
	 */
	private int institucion;
	/**
	 * Fecha de ¿grabacion? de la nota de enfermeria
	 */
	private Date fechaGrabacion;
	/**
	 * Hora de ¿grabacion? de la nota de enfermeria
	 */
	private String horaGrabacion;
	/**
	 * Especialidades del medico
	 */
	private String especialidadesMedico;
	/**
	 * Datos del medico (nombre y registro medico)
	 */
	private String datosMedico;
	
	public NotaEnfermeriaDto() {
	}

	public NotaEnfermeriaDto(int codigo, int numeroSolicitud, String descripcion, Date fecha, String hora, Date fechaGrabacion, String horaGrabacion, String enfermera, String especialidadesMedico, String datosMedico) {
		this.codigo = codigo;
		this.numeroSolicitud = numeroSolicitud;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.hora = hora;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.enfermera = enfermera;
		this.especialidadesMedico= especialidadesMedico;
		this.datosMedico = datosMedico;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getEnfermera() {
		return enfermera;
	}

	public void setEnfermera(String enfermera) {
		this.enfermera = enfermera;
	}

	public int getCodEnfermera() {
		return codEnfermera;
	}

	public void setCodEnfermera(int codEnfermera) {
		this.codEnfermera = codEnfermera;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public Date getFechaGrabacion() {
		return fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public String getEspecialidadesMedico() {
		return especialidadesMedico;
	}

	public void setEspecialidadesMedico(String especialidadesMedico) {
		this.especialidadesMedico = especialidadesMedico;
	}

	public String getDatosMedico() {
		return datosMedico;
	}

	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}
	
	
	
}
