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
 * Esta clase permite manejar la respuesta de la inserción de persona. La
 * clase fue creada exclusivamente para manejar el caso de pacientes sin
 * número de identificación, bajando al mínimo el número de cambios por hacer
 * si en algún momento esa tabla cambia (En ese caso, hay que cambiar la base
 * de datos, todos los JavaScripts de validacion de paciente / usuario / medico
 * ) y la clase de inserción de persona. No va a haber necesidad de cambiar 
 * ninguna de las clases propietarias de Paciente, Médico o Usuario
 *
 * @version 1.0, Oct 21, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class RespuestaInsercionPersona {

	/**
	 * Código con el que queda insertada la persona en el sistema
	 */
	private int codigoPersona;
	
	/**
	 * Indica si la operación de cambio de identificación tuvo o no éxito.
	 */
	private boolean salioBien;

	/**
	 * Indica si el paciente necesita el cambio de su identificación.
	 */
	private boolean necesitaCambioIdentificacion;

	/**
	 * Texto con la nueva identificación del paciente.
	 */
	private String nuevaIdentificacion;

	/**
	 * Constructor para RespuestaInsercionPersona.
	 * @param salioBien Indica si la inserción termino exitosamente
	 * @param necesitaCambioIdentificacion Indica si por alguna razón hay que cambiar la identificacion ingresada por el usuario
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
	 * Dice si se necesita efectuar un cambio en la identificación.
	 * @return <b>true</b> si se debe cambiar la identificación, <b>false</b> si no
	 */
	public boolean isNecesitaCambioIdentificacion() {
		return necesitaCambioIdentificacion;
	}

	/**
	 * Retorna la nueva identificación.
	 * @return la nueva identificación
	 */
	public String getNuevaIdentificacion() {
		return nuevaIdentificacion;
	}

	/**
	 * Dice si salio bien el cambio de la identificación.
	 * @return <b>true</b> si el cambio de identificación fue exitoso, <b>false</b> si no
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
	 * Establece el valor con la nueva identificación.
	 * @param nuevaIdentificacion el valor de la nueva identificación a establecer
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