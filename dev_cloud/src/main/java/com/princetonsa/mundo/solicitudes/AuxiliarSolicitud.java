/*
 * @(#)AuxiliarSolicitud.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.solicitudes;

import com.princetonsa.mundo.atencion.AuxiliarEvolucion;

/**
 * Esta clase permite manejar informaci�n b�sica de una solicitud,
 * teniendo en cuenta �nicamente los atributos importantes al 
 * momento de mostrar, maneja la misma informaci�n de la 
 * evoluci�n, agregando el C�digo del Tipo de Solicitud 
 * 
 *	@version 1.0, Mar 8, 2004
 */

public class AuxiliarSolicitud extends AuxiliarEvolucion
{
	/**
	 * N�mero de la solicitud a la que se est� 
	 * refiriendo AuxiliarSolicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * C�digo del tipo de solicitud al que se est� 
	 * refiriendo AuxiliarSolicitud
	 */
	private int codigoTipoSolicitud;
	
	/**
	 * C�digo que se refiere al estado de la historia
	 * cl�nica
	 */
	private int codigoEstadoHistoriaClinica;
	
	/**
	 * M�todo constructor de la clase AuxiliarSolicitud
	 */
	public AuxiliarSolicitud ()
	{
		this.clean();
	}
	
	/**
	 * M�todo que limpia todos los datos de AuxiliarSolicitud
	 */
	public void clean ()
	{
		this.numeroSolicitud=0;
		this.codigoTipoSolicitud=0;
		this.codigoEstadoHistoriaClinica=0;
		super.clean();
	}
	
	/**
	 * @return
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * @return
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param i
	 */
	public void setCodigoTipoSolicitud(int i) {
		codigoTipoSolicitud = i;
	}

	/**
	 * @param i
	 */
	public void setNumeroSolicitud(int i) {
		numeroSolicitud = i;
	}

    /**
     * @return Returns the codigoEstadoHistoriaClinica.
     */
    public int getCodigoEstadoHistoriaClinica()
    {
        return codigoEstadoHistoriaClinica;
    }
    /**
     * @param codigoEstadoHistoriaClinica The codigoEstadoHistoriaClinica to set.
     */
    public void setCodigoEstadoHistoriaClinica(int codigoEstadoHistoriaClinica)
    {
        this.codigoEstadoHistoriaClinica = codigoEstadoHistoriaClinica;
    }
}
