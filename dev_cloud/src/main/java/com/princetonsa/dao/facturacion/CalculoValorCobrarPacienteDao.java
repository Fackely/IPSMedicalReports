package com.princetonsa.dao.facturacion;

import com.servinte.axioma.dto.facturacion.DtoInfoMontoCobroDetallado;

public interface CalculoValorCobrarPacienteDao 
{

	public abstract DtoInfoMontoCobroDetallado obtenerInfoMontoCobroServicioArticulo(int codigoDetalleMonto,int codigoServicio,int codigoArticulo);

}
