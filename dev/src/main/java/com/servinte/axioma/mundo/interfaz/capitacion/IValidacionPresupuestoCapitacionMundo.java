package com.servinte.axioma.mundo.interfaz.capitacion;

import com.servinte.axioma.dto.capitacion.DtoValidacionPresupuesto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;

/**
 *  Interfaz que define la lógica de negocio relacionada con la
 *  Validación de Presupuesto de Capitación para Autorizaciones de
 *  Capitación Subcontratada
 * 
 * @version 1.0, Agu 29, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public interface IValidacionPresupuestoCapitacionMundo {

	
	/**
	 * Método utilizado para la validación de presupuesto de capitación 
	 * para la autorización de servicios
	 * @param dtoServicio
	 * @param codigoConvenio
	 * @param nombreConvenio
	 * @param codigoContrato
	 * @param numeroContrato
	 * @return DtoValidacionPresupuesto el dto con el booleano si pasa o no la validación
	 * @author Ricardo Ruiz
	 */
	public DtoValidacionPresupuesto validarPresupuestoCapitacionServicio(ServicioAutorizacionOrdenDto dtoServicio, 
										Integer codigoConvenio, String nombreConvenio, Integer codigoContrato, 
										String numeroContrato);
	
	
	/**
	 * Método utilizado para la validación de presupuesto de capitación 
	 * para la autorización de articulos
	 * 
	 * @param dtoArticulo
	 * @param codigoConvenio
	 * @param nombreConvenio
	 * @param codigoContrato
	 * @param numeroContrato
	 * @return DtoValidacionPresupuesto el dto con el booleano si pasa o no la validación
	 * @author Ricardo Ruiz
	 */

	public DtoValidacionPresupuesto validarPresupuestoCapitacionArticulo(MedicamentoInsumoAutorizacionOrdenDto dtoArticulo,
										Integer codigoConvenio, String nombreConvenio, Integer codigoContrato, 
										String numeroContrato);
}
