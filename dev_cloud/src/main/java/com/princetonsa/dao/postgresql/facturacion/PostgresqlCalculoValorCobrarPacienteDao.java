/**
 * 
 */
package com.princetonsa.dao.postgresql.facturacion;

import com.princetonsa.dao.facturacion.CalculoValorCobrarPacienteDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCalculoValorCobrarPacienteDao;
import com.servinte.axioma.dto.facturacion.DtoInfoMontoCobroDetallado;

/** 
 * @author armando
 *
 */
public class PostgresqlCalculoValorCobrarPacienteDao implements CalculoValorCobrarPacienteDao{

	@Override
	public DtoInfoMontoCobroDetallado obtenerInfoMontoCobroServicioArticulo(int codigoDetalleMonto,int codigoServicio, int codigoArticulo) 
	{
		return SqlBaseCalculoValorCobrarPacienteDao.obtenerInfoMontoCobroServicioArticulo(codigoDetalleMonto,codigoServicio,codigoArticulo);
	}
}
