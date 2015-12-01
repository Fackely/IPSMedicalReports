/*
 * @(#)InfoEvolucion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

/**
 * Clase utilitaria, almacena los datos b�sicos de una evoluci�n que se muestran
 * en una p�gina de consulta de datos hist�ricos de evoluciones.
 *
 * @version Jun 10, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class InfoEvolucion {

	/**
	 * N�mero de la solicitud de esta evoluci�n.
	 */
	private int numeroSolicitud;	

	/**
	 * Fecha en la que fue realizada la evoluci�n.
	 */
	private String fechaEvolucion;	

	/**
	 * Hora en la que fue realizada la evoluci�n.
	 */
	private String horaEvolucion;	

	/**
	 * Centro de costo al que pertenece el m�dico que hace esta evoluci�n.
	 */
	private String centroCosto;	

	/**
	 * Nombre del m�dico que hace esta evoluci�n.
	 */
	private String nombreMedico;	

	/**
	 * Crea un nuevo objeto <code>InfoEvolucion</code>.
	 */
	public InfoEvolucion () {

		this.numeroSolicitud = -1;
		this.fechaEvolucion = "";
		this.horaEvolucion = "";
		this.centroCosto = "";
		this.nombreMedico = "";

	}

	/**
	 * Crea un nuevo objeto <code>InfoEvolucion</code>.
	 * @param numeroSolicitud n�mero de solicitud
	 * @param fechaEvolucion fecha de la evoluci�n
	 * @param horaEvolucion hora de la evoluci�n
	 * @param centroCosto centro de costo donde se hace esta evoluci�n
	 * @param nombreMedico m�dico que hace esta evoluci�n
	 */
	public InfoEvolucion (int numeroSolicitud, String fechaEvolucion, String horaEvolucion, String centroCosto, String nombreMedico) {

		this.numeroSolicitud = numeroSolicitud;
		this.fechaEvolucion = fechaEvolucion;
		//-----------------------------------------------------------------------------------
		//se Modifico por tarea  68490
		//this.horaEvolucion = horaEvolucion.substring(0, horaEvolucion.lastIndexOf(':'));
		this.horaEvolucion = horaEvolucion;
		//-----------------------------------------------------------------------------------
		this.centroCosto = centroCosto;
		this.nombreMedico = nombreMedico;

	}

	/**
	 * Retorna el centro de costo del m�dico que hizo esta evoluci�n.
	 * @return el centro de costo del m�dico que hizo esta evoluci�n
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * Retorna la fecha en que fue realizada esta evoluci�n. 
	 * @return la fecha en que fue realizada esta evoluci�n
	 */
	public String getFechaEvolucion() {
		return fechaEvolucion;
	}

	/**
	 * Retorna la hora en que fue realizada esta evoluci�n. 
	 * @return la hora en que fue realizada esta evoluci�n
	 */
	public String getHoraEvolucion() {
		return horaEvolucion;
	}

	/**
	 * Retorna el nombre del m�dico que realiz� esta evoluci�n.
	 * @return el nombre del m�dico que realiz� esta evoluci�n
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * Retorna el n�mero de solicitud de esta evoluci�n.
	 * @return el n�mero de solicitud de esta evoluci�n
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

}