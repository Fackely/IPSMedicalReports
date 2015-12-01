/** 
 * 
 */
package com.princetonsa.dao.oracle.facturacion;

import com.princetonsa.dao.facturacion.CalculoValorCobrarPacienteDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCalculoValorCobrarPacienteDao;
import com.servinte.axioma.dto.facturacion.DtoInfoMontoCobroDetallado;

/**
 * @author armando
 *
 */
public class OracleCalculoValorCobrarPacienteDao  implements CalculoValorCobrarPacienteDao
{

	@Override
	public DtoInfoMontoCobroDetallado obtenerInfoMontoCobroServicioArticulo(int codigoDetalleMonto,int codigoServicio, int codigoArticulo) 
	{
		return SqlBaseCalculoValorCobrarPacienteDao.obtenerInfoMontoCobroServicioArticulo(codigoDetalleMonto,codigoServicio,codigoArticulo);
	}

}
