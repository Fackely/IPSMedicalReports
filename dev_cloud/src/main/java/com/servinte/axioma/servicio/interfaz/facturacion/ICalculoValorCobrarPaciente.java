package com.servinte.axioma.servicio.interfaz.facturacion;

import com.servinte.axioma.dto.facturacion.DtoInfoCobroPaciente;
import com.servinte.axioma.fwk.exception.IPSException;

public interface ICalculoValorCobrarPaciente 
{
	
	public DtoInfoCobroPaciente valorCobrarAPaciente(int codigoSubCuenta) throws IPSException;

}
