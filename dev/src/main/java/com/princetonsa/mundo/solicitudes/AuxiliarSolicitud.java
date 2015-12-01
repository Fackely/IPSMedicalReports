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
 * Esta clase permite manejar información básica de una solicitud,
 * teniendo en cuenta únicamente los atributos importantes al 
 * momento de mostrar, maneja la misma información de la 
 * evolución, agregando el Código del Tipo de Solicitud 
 * 
 *	@version 1.0, Mar 8, 2004
 */

public class AuxiliarSolicitud extends AuxiliarEvolucion
{
	/**
	 * Número de la solicitud a la que se está 
	 * refiriendo AuxiliarSolicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * Código del tipo de solicitud al que se está 
	 * refiriendo AuxiliarSolicitud
	 */
	private int codigoTipoSolicitud;
	
	/**
	 * Código que se refiere al estado de la historia
	 * clínica
	 */
	private int codigoEstadoHistoriaClinica;
	
	/**
	 * Método constructor de la clase AuxiliarSolicitud
	 */
	public AuxiliarSolicitud ()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia todos los datos de AuxiliarSolicitud
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
