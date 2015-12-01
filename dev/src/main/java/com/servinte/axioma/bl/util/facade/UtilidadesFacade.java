package com.servinte.axioma.bl.util.facade;

import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.bl.util.impl.UtilidadesMundo;
import com.servinte.axioma.bl.util.interfaz.IUtilidadesMundo;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase Fachada que provee todos los servicios de lógica de negocio del las utilidades 
 * de negocio tranversales a toda la apliación
 * 
 * @author ricruico
 * @version 1.0
 * @created 27-jun-2012 02:24:01 p.m.
 */
public class UtilidadesFacade {

	/**
	 * Método utilizado para verificar si exise o no un consecutivo disponible
	 * 
	 * @param nombreConsecutivo
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeConsecutivo(String nombreConsecutivo, int codigoInstitucion) throws IPSException{
		IUtilidadesMundo utilidadesMundo= new UtilidadesMundo();
		return utilidadesMundo.existeConsecutivo(nombreConsecutivo, codigoInstitucion);
	}
	
	/**
	 * Método utilizado para verificar si exise o no un consecutivo disponible
	 * 
	 * @param nombreConsecutivo
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public Integer obtenerConsecutivo(String nombreConsecutivo, int codigoInstitucion) throws IPSException{
		IUtilidadesMundo utilidadesMundo= new UtilidadesMundo();
		return utilidadesMundo.obtenerConsecutivo(nombreConsecutivo, codigoInstitucion);
	}
	
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
	public PersonaBasica cargarPaciente(int codigoPaciente, int codigoInstitucion, int codigoCentroAtencion) throws IPSException{
		IUtilidadesMundo utilidadesMundo= new UtilidadesMundo();
		return utilidadesMundo.cargarPaciente(codigoPaciente, codigoInstitucion, codigoCentroAtencion);
	}
}
