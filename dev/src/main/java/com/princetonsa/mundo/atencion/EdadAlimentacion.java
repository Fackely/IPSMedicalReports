/*
 * @(#)EdadAlimentacion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.atencion;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * Esta clase tiene exactamente los mismos datos de Valor de 
 * Desarrollo, pero por claridad  es mejor definir una clase
 * aparte que deje claro que se esta trabajando con una
 * EdadAlimentacion, que no tiene nada que ver con un
 * ValorDesarrollo
 *
 * @version 1.0, Jun 25, 2003
 */
public class EdadAlimentacion extends ValorDesarrollo
{
	/**
	 * Médico que realizo esta edad de alimentación
	 */
	UsuarioBasico medico=null;

	/**
	 * Fecha en la que se grabó esta edad de alimentación
	 */
	String fechaGrabacion="";
	/**
	 * Hora en la que se grabó esta edad de alimentación
	 */
	String horaGrabacion="";
	
	/**
	 * Constructor vacio del objeto EdadAlimentacion 
	 */
	public EdadAlimentacion ()
	{
		this.setMedico(new UsuarioBasico());
		this.setFechaGrabacion("");
		this.setHoraGrabacion("");
		this.setDescripcion("");
		this.setNombre("");
		this.setCodigo(0);
	}

	/**
	 * Constructor que llena todos los datos en el objeto 
	 * EdadAlimentacion 
	 */
	public EdadAlimentacion (int codigo, String nombre, String descripcion, UsuarioBasico medico)
	{
		this.setMedico(medico);
		this.setCodigo(codigo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
	}

	public void setEdadTexto (String edadTexto)
	{
		this.setDescripcion(edadTexto);
	}
	
	public String getEdadTexto ()
	{
		return this.getDescripcion();
	}
	
	/**
	 * @return
	 */
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}

	/**
	 * @return
	 */
	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	/**
	 * @return
	 */
	public UsuarioBasico getMedico() {
		return medico;
	}

	/**
	 * @param string
	 */
	public void setFechaGrabacion(String string) {
		fechaGrabacion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraGrabacion(String string) {
		horaGrabacion = string;
	}

	/**
	 * @param basico
	 */
	public void setMedico(UsuarioBasico basico) {
		medico = basico;
	}

}
