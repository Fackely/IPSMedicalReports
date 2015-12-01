package com.princetonsa.dto.facturacion;

import java.io.Serializable;

/**
 * Esta clase almacea las inconsistencias generadas en el proceso rips de entidades subcontratadas
 * @author Fabián Becerra
 */
public class DtoInconsistenciasRipsEntidadesSub implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el nombre del archivo
	 */
	private String nombreArchivo;
	
	/**
	 * Atributo que almacena el tipo de inconsistencia
	 */
	private String observaciones;
	
	/**
	 * Atributo que almacena el campo donde se generó la inconsistencia
	 */
	private String campo;
	
	/**
	 * Atributo que almacena el número de registro del archivo donde
	 * se generó la inconsistencia
	 */
	private int numeroFila;

	
	private String procesado;
	private String autorizado;
	
	/**
	 * Atributo que almacena el código del servicio o medicamento 
	 * que se encuentra en el registro del archivo plano
	 */
	private String codigoServicioMed;
	
	/**
	 * Atributo que almacena el nombre del servicio o medicamento
	 * que se encuentra en el registro del archivo plano
	 */
	private String nombreServicioMed;
	
	
	
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo observaciones
	 * 
	 * @param  valor para el atributo observaciones 
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo observaciones
	 * 
	 * @return  Retorna la variable observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo nombreArchivo
	 * 
	 * @param  valor para el atributo nombreArchivo 
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreArchivo
	 * 
	 * @return  Retorna la variable nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo campo
	 * 
	 * @param  valor para el atributo campo 
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo campo
	 * 
	 * @return  Retorna la variable campo
	 */
	public String getCampo() {
		return campo;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo numeroFila
	 * 
	 * @param  valor para el atributo numeroFila 
	 */
	public void setNumeroFila(int numeroFila) {
		this.numeroFila = numeroFila;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo numeroFila
	 * 
	 * @return  Retorna la variable numeroFila
	 */
	public int getNumeroFila() {
		return numeroFila;
	}

	public void setProcesado(String procesado) {
		this.procesado = procesado;
	}

	public String getProcesado() {
		return procesado;
	}

	public void setAutorizado(String autorizado) {
		this.autorizado = autorizado;
	}

	public String getAutorizado() {
		return autorizado;
	}

	public void setCodigoServicioMed(String codigoServicioMed) {
		this.codigoServicioMed = codigoServicioMed;
	}

	public String getCodigoServicioMed() {
		return codigoServicioMed;
	}

	public void setNombreServicioMed(String nombreServicioMed) {
		this.nombreServicioMed = nombreServicioMed;
	}

	public String getNombreServicioMed() {
		return nombreServicioMed;
	}

	
	

}
