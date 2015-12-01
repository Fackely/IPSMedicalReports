/**
 * 
 */
package com.servinte.axioma.servicio.impl.facturacion;

import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IValidacionTipoCobroPacienteMundo;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;


/**
 * @author armando
 *
 */
public class ValidacionTipoCobroPacienteServicio implements IValidacionTipoCobroPacienteServicio
{
	/**
	 * 
	 */
	IValidacionTipoCobroPacienteMundo validationTipoCobroPacienteMundo;

	/**
	 * 
	 */
	public ValidacionTipoCobroPacienteServicio() 
	{
		validationTipoCobroPacienteMundo=FacturacionFabricaMundo.crearValidacionTipoCobroPacienteMundo();
	}

	public DtoValidacionTipoCobroPaciente validarTipoCobroPacienteServicioConvenioContrato(int codigoContrato) {
		return validationTipoCobroPacienteMundo.validarTipoCobroPacienteServicioConvenioContrato(codigoContrato);
	}


}
