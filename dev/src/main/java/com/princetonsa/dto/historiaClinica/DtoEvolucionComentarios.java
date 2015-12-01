package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

public class DtoEvolucionComentarios implements Serializable
{

	/**
	 * 
	 */
	private String consecutivo;
	
	private String fecha;
	
	private String hora;
	
	private String valor;
	
	private UsuarioBasico profesional;
	
	
	/**
	 * 
	 */
	public void clean()
	{
		this.consecutivo="";
		this.fecha="";
		this.hora="";
		this.valor="";
		this.profesional = new UsuarioBasico();
	}
	
	
	/**
	 * 
	 */
	public DtoEvolucionComentarios()
	{
		this.clean();
	}

	
	/**
	 * 
	 * @return
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * 
	 * @param consecutivo
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * 
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * 
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * 
	 * @return
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * 
	 * @param hora
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * 
	 * @return
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * 
	 * @param valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * 
	 * @return
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}

	/**
	 * 
	 * @param profesional
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}
	
	
	
}
