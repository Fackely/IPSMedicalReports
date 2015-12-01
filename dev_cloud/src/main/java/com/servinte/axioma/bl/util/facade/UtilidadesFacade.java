package com.servinte.axioma.bl.util.facade;

import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.bl.util.impl.UtilidadesMundo;
import com.servinte.axioma.bl.util.interfaz.IUtilidadesMundo;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase Fachada que provee todos los servicios de l�gica de negocio del las utilidades 
 * de negocio tranversales a toda la apliaci�n
 * 
 * @author ricruico
 * @version 1.0
 * @created 27-jun-2012 02:24:01 p.m.
 */
public class UtilidadesFacade {

	/**
	 * M�todo utilizado para verificar si exise o no un consecutivo disponible
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
	 * M�todo utilizado para verificar si exise o no un consecutivo disponible
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
	public PersonaBasica cargarPaciente(int codigoPaciente, int codigoInstitucion, int codigoCentroAtencion) throws IPSException{
		IUtilidadesMundo utilidadesMundo= new UtilidadesMundo();
		return utilidadesMundo.cargarPaciente(codigoPaciente, codigoInstitucion, codigoCentroAtencion);
	}
}
