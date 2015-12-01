/*
 * Junio 12, 2008
 */
package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

import util.InfoDatosString;


/**
 * Data Transfer Object: 
 * Usado para el manejo de las observaciones de pediatría
 * 
 * @author Sebastián Gómez R.
 *
 */
public class DtoObservacionesPediatria implements Serializable
{

	private String consecutivo;
	/**
	 * El tipo es integridad domino y corresponde
	 * a las siguientes constantesIntegridadDominio:
		acronimoValNutricionalMenor2Anios  
		acronimoValNutricionalMayor2Anios  
		acronimoSueniosHabitos
		acronimoTipoAlimentacion 
		acronimoObservacionesDesarrollo  
	 * 
	 */
	private InfoDatosString tipo;
	private String valor;
	private String fecha;
	private String hora;
	private UsuarioBasico usuario;
	
	
	/**
	 * Método que limpia la informacion
	 *
	 */
	public void clean()
	{
		this.consecutivo = "";
		this.tipo = new InfoDatosString();
		this.valor = "";
		this.fecha = "";
		this.hora = "";
		this.usuario = new UsuarioBasico();
	}
	
	/**
	 * Cosntructor
	 *
	 */
	public DtoObservacionesPediatria()
	{
		this.clean();
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
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
	 * @return the tipo
	 */
	public String getCodigoTipo() {
		return tipo.getCodigo();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setCodigoTipo(String tipo) {
		this.tipo.setCodigo(tipo);
	}
	
	/**
	 * @return the tipo
	 */
	public String getNombreTipo() {
		return tipo.getNombre();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setNombreTipo(String tipo) {
		this.tipo.setNombre(tipo);
	}

	/**
	 * @return the usuario
	 */
	public UsuarioBasico getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
