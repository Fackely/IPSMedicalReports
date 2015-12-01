package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * Dto que guarda informacion de anulacion de peticiones 
 * @author wilgomcr
 * @created 13/09/2012
 */
public class AnulacionPeticionQxDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8200980986093869148L;

	/**Atributo que almacena el codigo de la Peticion*/
	private int codigoPeticion;
	
	/**Atributo que almacena el login del usuario que anula la Peticion*/
	private String usuario;
	
	/**Atributo que almacena la fecha de la Peticion*/
	private Date fecha;
	
	/**Atributo que almacena la hora de la Peticion*/
	private String hora;
	
	/**Atributo que almacena el codigo del motivo de anulacion de la peticion*/
	private int motivoAnulacion;
	
	/**Atributo que almacena el comentario de anulacion de la peticion*/
	private String comentarioAnulacion;

	
	/**
	 * @return codigoPeticion
	 */
	public int getCodigoPeticion() {
		return codigoPeticion;
	}

	/**
	 * @param codigoPeticion
	 */
	public void setCodigoPeticion(int codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}

	/**
	 * @return usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return motivoAnulacion
	 */
	public int getMotivoAnulacion() {
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion
	 */
	public void setMotivoAnulacion(int motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return comentarioAnulacion
	 */
	public String getComentarioAnulacion() {
		return comentarioAnulacion;
	}

	/**
	 * @param comentarioAnulacion
	 */
	public void setComentarioAnulacion(String comentarioAnulacion) {
		this.comentarioAnulacion = comentarioAnulacion;
	}
}
