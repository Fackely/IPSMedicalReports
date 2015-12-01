/*
 * @(#)RespuestaInsercionPersona.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

/**
 * Esta clase permite manejar la respuesta de la inserci�n de persona. La
 * clase fue creada exclusivamente para manejar el caso de pacientes sin
 * n�mero de identificaci�n, bajando al m�nimo el n�mero de cambios por hacer
 * si en alg�n momento esa tabla cambia (En ese caso, hay que cambiar la base
 * de datos, todos los JavaScripts de validacion de paciente / usuario / medico
 * ) y la clase de inserci�n de persona. No va a haber necesidad de cambiar 
 * ninguna de las clases propietarias de Paciente, M�dico o Usuario
 *
 * @version 1.0, Oct 21, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class RespuestaInsercionPersona {

	/**
	 * C�digo con el que queda insertada la persona en el sistema
	 */
	private int codigoPersona;
	
	/**
	 * Indica si la operaci�n de cambio de identificaci�n tuvo o no �xito.
	 */
	private boolean salioBien;

	/**
	 * Indica si el paciente necesita el cambio de su identificaci�n.
	 */
	private boolean necesitaCambioIdentificacion;

	/**
	 * Texto con la nueva identificaci�n del paciente.
	 */
	private String nuevaIdentificacion;

	/**
	 * Constructor para RespuestaInsercionPersona.
	 * @param salioBien Indica si la inserci�n termino exitosamente
	 * @param necesitaCambioIdentificacion Indica si por alguna raz�n hay que cambiar la identificacion ingresada por el usuario
	 * @param nuevaIdentificacion Contiene la nueva identificacion de la persona, en caso en que tenga que ser cambiada
	 */
	public RespuestaInsercionPersona(boolean salioBien, boolean necesitaCambioIdentificacion, String nuevaIdentificacion, int codigoPersona) 
	{
		this.salioBien=	salioBien;
		this.necesitaCambioIdentificacion=necesitaCambioIdentificacion;
		this.nuevaIdentificacion=nuevaIdentificacion;
		this.codigoPersona=codigoPersona;
	}

	/**
	 * Dice si se necesita efectuar un cambio en la identificaci�n.
	 * @return <b>true</b> si se debe cambiar la identificaci�n, <b>false</b> si no
	 */
	public boolean isNecesitaCambioIdentificacion() {
		return necesitaCambioIdentificacion;
	}

	/**
	 * Retorna la nueva identificaci�n.
	 * @return la nueva identificaci�n
	 */
	public String getNuevaIdentificacion() {
		return nuevaIdentificacion;
	}

	/**
	 * Dice si salio bien el cambio de la identificaci�n.
	 * @return <b>true</b> si el cambio de identificaci�n fue exitoso, <b>false</b> si no
	 */
	public boolean isSalioBien() {
		return salioBien;
	}

	/**
	 * Establece el valor de necesitaCambioIdentificacion.
	 * @param necesitaCambioIdentificacion el valor a establecer
	 */
	public void setNecesitaCambioIdentificacion (boolean necesitaCambioIdentificacion) {
		this.necesitaCambioIdentificacion = necesitaCambioIdentificacion;
	}

	/**
	 * Establece el valor con la nueva identificaci�n.
	 * @param nuevaIdentificacion el valor de la nueva identificaci�n a establecer
	 */
	public void setNuevaIdentificacion(String nuevaIdentificacion) {
		this.nuevaIdentificacion = nuevaIdentificacion;
	}

	/**
	 * Establece el valor de salioBien.
	 * @param salioBien el valor a establecer
	 */
	public void setSalioBien(boolean salioBien) {
		this.salioBien = salioBien;
	}

	/**
	 * @return
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}

	/**
	 * @param i
	 */
	public void setCodigoPersona(int i) {
		codigoPersona = i;
	}

}