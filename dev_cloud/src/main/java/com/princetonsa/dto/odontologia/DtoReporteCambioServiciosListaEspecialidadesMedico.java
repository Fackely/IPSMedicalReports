package com.princetonsa.dto.odontologia;

import java.io.Serializable;


/**
 * Esta clase se encarga de almacenar las especialidades de un profesional de la salud
 * despues de la consulta
 * @author Fabian Becerra
 * @author Javier Gonzalez
 * @since 27 Oct 2010
 */
public class DtoReporteCambioServiciosListaEspecialidadesMedico implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	/**
	 * Atributo que almacena el nombre de la especialidad
	 */
	private String nombreEspecialidad;
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor del 
	 * atributo nombreEspecialidad
	 * 
	 * @param  valor del atributo nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}
	
}
