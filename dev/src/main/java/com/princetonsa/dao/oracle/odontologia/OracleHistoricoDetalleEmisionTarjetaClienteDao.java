package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.HistoricoDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHistoricoDetalleEmisionTarjetaClienteDao;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleEmisionTarjetaCliente;

public class OracleHistoricoDetalleEmisionTarjetaClienteDao implements HistoricoDetalleEmisionTarjetaClienteDao {

	@Override
	public ArrayList<DtoHistoricoDetalleEmisionTarjetaCliente> cargar(
			DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseHistoricoDetalleEmisionTarjetaClienteDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseHistoricoDetalleEmisionTarjetaClienteDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoHistoricoDetalleEmisionTarjetaCliente dto) {
		
		return SqlBaseHistoricoDetalleEmisionTarjetaClienteDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoHistoricoDetalleEmisionTarjetaCliente dtoNuevo,
			DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseHistoricoDetalleEmisionTarjetaClienteDao.modificar(dtoNuevo, dtoWhere);
	}

}
