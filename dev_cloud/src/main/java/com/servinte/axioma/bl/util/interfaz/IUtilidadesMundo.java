package com.servinte.axioma.bl.util.interfaz;

import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que contiene todos los servicios de lógica de negocio del las utilidades 
 * de negocio tranversales a toda la apliación
 * 
 * @author ricruico
 * @version 1.0
 * @created 27-jun-2012 02:24:01 p.m.
 */
public interface IUtilidadesMundo {

	
	/**
	 * Método que permite verificar si existe un consecutivo disponible
	 * 
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	boolean existeConsecutivo(String nombreConsecutivo, int institucion) throws IPSException;
	
	/**
	 * Método que permite obtener un consecutivo disponible
	 * 
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	Integer obtenerConsecutivo(String nombreConsecutivo, int institucion) throws IPSException;
	
	/**
	 * Método utilizado para cargar la información de un paciente
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
