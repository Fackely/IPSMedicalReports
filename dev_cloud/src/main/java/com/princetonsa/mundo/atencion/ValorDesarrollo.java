/*
 * @(#)ValorDesarrollo.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import com.princetonsa.mundo.UsuarioBasico;

import util.InfoDatosInt;

/**
 * Esta clase encapsula los atributos de una valoraci�n pedi�trica del desarrollo de un ni�o.
 * No extiende de InfoDatos ya que este obliga a usar conversiones (costoso)
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class ValorDesarrollo extends InfoDatosInt
{
	/**
	 * M�dico que realizo esta edad de alimentaci�n
	 */
	UsuarioBasico medico=null;

	/**
	 * Edad que ten�a el ni�o cuando este valor de 
	 * desarrollo fu� grabado
	 */
	private String edadTexto="";

	/**
	 * Fecha de grabaci�n de este valor de desarrollo
	 */
	private String fechaGrabacion="";

	/**
	 * Hora de grabaci�n de este valor de desarrollo
	 */
	private String horaGrabacion="";

	/**
	 * Constructor vacio del objeto ValorDesarrollo 
	 */
	public ValorDesarrollo ()
	{
		this.clean();
		this.setMedico(new UsuarioBasico());
		this.setDescripcion("");
		this.setNombre("");
		this.setCodigo(0);
	}

	/**
	 * Constructor que llena todos los datos en el objeto 
	 * ValorDesarrollo 
	 */
	public ValorDesarrollo (int codigo, String nombre, String descripcion)
	{
		this.clean();
		this.setMedico(new UsuarioBasico());
		this.setCodigo(codigo);
		this.setNombre(nombre);
		this.setDescripcion(descripcion);
	}

	/**
	 * M�todo que limpia el valor de desarrollo actual
	 */
	public void clean ()
	{
		super.clean();
		edadTexto="";
		fechaGrabacion="";
		horaGrabacion="";
	}

	/**
	 * @return
	 */
	public String getEdadTexto() {
		return edadTexto;
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
	public String getHoraGrabacionCincoCaracteres() 
	{
		if (horaGrabacion==null)
		{
			return "";
		}
		else
		{
			return horaGrabacion.substring(0, 5);
		}
	}
	/**
	 * @param string
	 */
	public void setEdadTexto(String string) {
		edadTexto = string;
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
	 * @return
	 */
	public UsuarioBasico getMedico() {
		return medico;
	}

	/**
	 * @param basico
	 */
	public void setMedico(UsuarioBasico basico) {
		medico = basico;
	}

}