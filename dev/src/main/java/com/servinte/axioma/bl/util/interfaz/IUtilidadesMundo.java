package com.servinte.axioma.bl.util.interfaz;

import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que contiene todos los servicios de l�gica de negocio del las utilidades 
 * de negocio tranversales a toda la apliaci�n
 * 
 * @author ricruico
 * @version 1.0
 * @created 27-jun-2012 02:24:01 p.m.
 */
public interface IUtilidadesMundo {

	
	/**
	 * M�todo que permite verificar si existe un consecutivo disponible
	 * 
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	boolean existeConsecutivo(String nombreConsecutivo, int institucion) throws IPSException;
	
	/**
	 * M�todo que permite obtener un consecutivo disponible
	 * 
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	Integer obtenerConsecutivo(String nombreConsecutivo, int institucion) throws IPSException;
	
	/**
	 * M�todo utilizado para cargar la informaci�n de un paciente
	 * 
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * 
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	PersonaBasica cargarPaciente(int codigoPaciente, int codigoInstitucion, int codigoCentroAtencion) throws IPSException;
	
}
