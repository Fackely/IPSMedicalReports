package com.servinte.axioma.mundo.interfaz.facturacion;

import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;

public interface IValidacionTipoCobroPacienteMundo
{
	public DtoValidacionTipoCobroPaciente validarTipoCobroPacienteServicioConvenioContrato(int codigoConvenio);
}
