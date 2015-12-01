/*
 * @(#)NotaAclaratoria.java
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
 * Esta clase encapsula los atributos de una nota aclaratoria de una epicrisis.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class NotaAclaratoria {

	/**
	 * N�mero consecutivo de esta nota aclaratoria.
	 */
	private int numero;

	/**
	 * Texto de esta nota.
	 */
	private String nota;

	/**
	 * Fecha en la que se realiz� esta nota.
	 */
	private String fecha;

	/**
	 * Hora en la que se realiz� esta nota.
	 */
	private String hora;

	/**
	 * M�dico que escribi� esta nota.
	 */
	private UsuarioBasico medico;

	/**
	 * Crea un nuevo objeto <code>NotaAclaratoria</code>
	 */
	public NotaAclaratoria() {
		this.clean();
	}

	/**
	 * Retorna la fecha de esta nota..
	 * @return la fecha de esta nota
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Retorna la hora de esta nota.
	 * @return la hora de esta nota
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Retorna la hora de esta nota en formato hh:mm (Cinco caracteres).
	 * @return la hora de esta nota en formato hh:mm (Cinco caracteres)
	 */
	public String getHoraCincoCaracteres ()
	{
		if (hora!=null)
		{
			return hora.substring(0, 5);
		}
		else
		{
			return "";
		}
	}

	/**
	 * Retorna el m�dico que escribe esta nota.
	 * @return el m�dico que escribe esta nota
	 */
	public UsuarioBasico getMedico() {
		return medico;
	}

	/**
	 * Retorna el texto de esta nota.
	 * @return el texto de esta nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * Retorna el n�mero consecutivo de esta nota.
	 * @return el n�mero consecutivo de esta nota
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Establece la fecha de esta nota.
	 * @param fecha la fecha a establecer
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Establece la hora de esta nota.
	 * @param hora la hora a establecer
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * Establece el m�dico que escribe esta nota.
	 * @param medico el m�dico a establecer
	 */
	public void setMedico(UsuarioBasico medico) {
		this.medico = medico;
	}

	/**
	 * Establece el texto de esta nota.
	 * @param nota el texto a establecer
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * Establece el n�mero consecutivo de esta nota.
	 * @param numero el n�mero consecutivo a establecer
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * Este m�todo inicializa en valores vac�os, -mas no nulos- los atributos de este objeto.
	 */
	public void clean() {
		this.numero = -1;
		this.nota = "";
		this.fecha = "";
		this.hora = "";
		this.medico = null;
	}

}