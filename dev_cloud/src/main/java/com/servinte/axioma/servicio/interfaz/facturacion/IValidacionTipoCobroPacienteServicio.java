/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.facturacion;

import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;

/**
 * 
 * @author armando
 *
 */
public interface IValidacionTipoCobroPacienteServicio 
{

	public DtoValidacionTipoCobroPaciente validarTipoCobroPacienteServicioConvenioContrato(int codigoContrato);
}
