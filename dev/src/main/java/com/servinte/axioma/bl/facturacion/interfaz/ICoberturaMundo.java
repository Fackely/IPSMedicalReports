/**
 * 
 */
package com.servinte.axioma.bl.facturacion.interfaz;

import com.servinte.axioma.fwk.exception.IPSException;

import util.facturacion.InfoCobertura;

/**
 * Interface que provee los servicios de Negocio correspondientes a la
 * información de cobertura
 * 
 * @author diego
 *
 */
public interface ICoberturaMundo {

	/**
	 * Método implementado para validar la cobertura de un servicio por flujo de entidades subcontratadas
	 * 
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return InfoCoberturaDto
	 */
	InfoCobertura validacionCoberturaServicioEntidadSub(long codigoContrato,
			int codigoViaIngreso,String tipoPaciente,int codigoServicio,
			Integer codigoNaturalezaPaciente, int codigoInstitucion) throws IPSException;
	
	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un 
	 * contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return InfoCoberturaDto
	 */
	InfoCobertura validacionCoberturaArticuloEntidadSub(long codigoContrato, 
			int codigoViaIngreso, String tipoPaciente, int codigoArticulo, 
			Integer codigoNaturalezaPaciente, int codigoInstitucion) throws IPSException;
	
}
