package com.servinte.axioma.bl.facturacion.interfaz;

import java.util.List;

import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que expone los servicios de Negocio correspondientes a la lógica asociada a los
 * Convenios y los Contratos
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public interface IConvenioContratoMundo {

	/**
	 * Servicio que obtiene la lista de contratos parametrizados
	 * en el sistema que estan asociados al convenio pasado por parámetro
	 * 
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 */
	List<ContratoDto> consultarContratosVigentesPorConvenio(int codigoConvenio) throws IPSException;

	/**
	 * Servicio que obtiene la lista de convenios parametrizados
	 * en el sistema que cumplan con los parámetros de búsqueda
	 * 
	 * @param institucion
	 * @param tipoContrato
	 * @param manejaCapitacionSubcontratada
	 * @param isActivo
	 * @return
	 * @throws IPSException
	 */
	List<ConvenioDto> consultarConveniosPorInstitucion(int institucion, Integer tipoContrato, Character manejaCapitacionSubcontratada, boolean isActivo) throws IPSException;

	/**
	 * Servicio que obtiene la lista de todos convenios parametrizados
	 * en el sistema que cumplan con los parámetros de búsqueda
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 14/11/2012
	 */
	List<ConvenioDto> consultarTodosConveniosPorInstitucion(int codigoInstitucion) throws IPSException;
}