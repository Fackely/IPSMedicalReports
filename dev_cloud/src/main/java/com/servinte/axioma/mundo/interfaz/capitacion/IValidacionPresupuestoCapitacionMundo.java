package com.servinte.axioma.mundo.interfaz.capitacion;

import com.servinte.axioma.dto.capitacion.DtoValidacionPresupuesto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;

/**
 *  Interfaz que define la l�gica de negocio relacionada con la
 *  Validaci�n de Presupuesto de Capitaci�n para Autorizaciones de
 *  Capitaci�n Subcontratada
 * 
 * @version 1.0, Agu 29, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public interface IValidacionPresupuestoCapitacionMundo {

	
	/**
	 * M�todo utilizado para la validaci�n de presupuesto de capitaci�n 
	 * para la autorizaci�n de servicios
	 * @param dtoServicio
	 * @param codigoConvenio
	 * @param nombreConvenio
	 * @param codigoContrato
	 * @param numeroContrato
	 * @return DtoValidacionPresupuesto el dto con el booleano si pasa o no la validaci�n
	 * @author Ricardo Ruiz
	 */
	public DtoValidacionPresupuesto validarPresupuestoCapitacionServicio(ServicioAutorizacionOrdenDto dtoServicio, 
										Integer codigoConvenio, String nombreConvenio, Integer codigoContrato, 
										String numeroContrato);
	
	
	/**
	 * M�todo utilizado para la validaci�n de presupuesto de capitaci�n 
	 * para la autorizaci�n de articulos
	 * 
	 * @param dtoArticulo
	 * @param codigoConvenio
	 * @param nombreConvenio
	 * @param codigoContrato
	 * @param numeroContrato
	 * @return DtoValidacionPresupuesto el dto con el booleano si pasa o no la validaci�n
	 * @author Ricardo Ruiz
	 */

	public DtoValidacionPresupuesto validarPresupuestoCapitacionArticulo(MedicamentoInsumoAutorizacionOrdenDto dtoArticulo,
										Integer codigoConvenio, String nombreConvenio, Integer codigoContrato, 
										String numeroContrato);
}
