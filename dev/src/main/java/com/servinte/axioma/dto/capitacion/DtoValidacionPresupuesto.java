package com.servinte.axioma.dto.capitacion;

import com.servinte.axioma.common.ErrorMessage;

/**
 *  Dto Utilizado para la validación de presupesto
 *  para Autorizaciones de capitación subcontratada
 * 
 * @version 1.0, Agu 29, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class DtoValidacionPresupuesto {

	/**
	 * Atributo utilizado para devolver si se cumple con
	 * la valdiación o no
	 */
	private boolean valido;
	
	/**
	 * Atributo utilizado para devolver el mensaje de validación 
	 * generado
	 */
	private String mensaje;
	
	/**
	 * Atributo utilizado para devolver el código de la validación
	 * generado
	 */
	private Integer codigoValidacion;
	
	/**
	 * Atributo utilizado para asignar el nombre del servicio / Merdicamento Insumo de la validación 
	 * 
	 */
	private String nombreServicioArticulo;
	
	/**
	 * Atributo utilizado para asignar la fecha de la validación 
	 * 
	 */
	private String fechaValidacion;
	
	/**
	 * Atributo utilizado para asignar el convenio de la validación 
	 * 
	 */
	private String convenioValidacion;
	
	/**
	 * Atributo utilizado para asignar el contrato de la validación 
	 * 
	 */
	private String contratoValidacion;
	
	/**
	 * Atributo utilizado para asignar el año de la validación 
	 * 
	 */
	private String anioValidacion;
	
	/**
	 * Atributo utilizado para asignar el mes de la validación 
	 * 
	 */
	private String mesValidacion;
	
	/**
	 * Atributo para almacenar los errores de las validaciones
	 */
	private ErrorMessage mensajeError;
	
	/**
	 * Constructor de la clase
	 */
	public DtoValidacionPresupuesto() {
		
	}


	/**
	 * Constructor de la clase para inicializar los atributos
	 * @param valido
	 * @param mensaje
	 */
	public DtoValidacionPresupuesto(boolean valido, String mensaje, Integer codigoValidacion, 
			String nombreServicioArticulo, String fechaValidacion, String convenioValidacion, 
			String contratoValidacion, String anioValidacion, String mesValidacion) {
		this.valido = valido;
		this.mensaje = mensaje;
		this.codigoValidacion = codigoValidacion;
		this.nombreServicioArticulo = nombreServicioArticulo;
		this.fechaValidacion = fechaValidacion;
		this.convenioValidacion = convenioValidacion;
		this.contratoValidacion = contratoValidacion;
		this.anioValidacion = anioValidacion;
		this.mesValidacion = mesValidacion;
	}


	/**
	 * @return the valido
	 */
	public boolean isValido() {
		return valido;
	}


	/**
	 * @param valido the valido to set
	 */
	public void setValido(boolean valido) {
		this.valido = valido;
	}


	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}


	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	/**
	 * @return the codigoValidacion
	 */
	public Integer getCodigoValidacion() {
		return codigoValidacion;
	}


	/**
	 * @param codigoValidacion the codigoValidacion to set
	 */
	public void setCodigoValidacion(Integer codigoValidacion) {
		this.codigoValidacion = codigoValidacion;
	}


	/**
	 * @return the nombreServicioArticulo
	 */
	public String getNombreServicioArticulo() {
		return nombreServicioArticulo;
	}


	/**
	 * @param nombreServicioArticulo the nombreServicioArticulo to set
	 */
	public void setNombreServicioArticulo(String nombreServicioArticulo) {
		this.nombreServicioArticulo = nombreServicioArticulo;
	}


	/**
	 * @return the fechaValidacion
	 */
	public String getFechaValidacion() {
		return fechaValidacion;
	}


	/**
	 * @param fechaValidacion the fechaValidacion to set
	 */
	public void setFechaValidacion(String fechaValidacion) {
		this.fechaValidacion = fechaValidacion;
	}


	/**
	 * @return the convenioValidacion
	 */
	public String getConvenioValidacion() {
		return convenioValidacion;
	}


	/**
	 * @param convenioValidacion the convenioValidacion to set
	 */
	public void setConvenioValidacion(String convenioValidacion) {
		this.convenioValidacion = convenioValidacion;
	}


	/**
	 * @return the contratoValidacion
	 */
	public String getContratoValidacion() {
		return contratoValidacion;
	}


	/**
	 * @param contratoValidacion the contratoValidacion to set
	 */
	public void setContratoValidacion(String contratoValidacion) {
		this.contratoValidacion = contratoValidacion;
	}


	/**
	 * @return the anioValidacion
	 */
	public String getAnioValidacion() {
		return anioValidacion;
	}


	/**
	 * @param anioValidacion the anioValidacion to set
	 */
	public void setAnioValidacion(String anioValidacion) {
		this.anioValidacion = anioValidacion;
	}


	/**
	 * @return the mesValidacion
	 */
	public String getMesValidacion() {
		return mesValidacion;
	}


	/**
	 * @param mesValidacion the mesValidacion to set
	 */
	public void setMesValidacion(String mesValidacion) {
		this.mesValidacion = mesValidacion;
	}


	public ErrorMessage getMensajeError() {
		return mensajeError;
	}


	public void setMensajeError(ErrorMessage mensajeError) {
		this.mensajeError = mensajeError;
	}

	
}
