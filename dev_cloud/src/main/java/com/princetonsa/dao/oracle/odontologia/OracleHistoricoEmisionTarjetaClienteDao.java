package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.HistoricoEmisionTarjetaClienteDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHistoricoEmisionTarjetaClienteDao;
import com.princetonsa.dto.odontologia.DtoHistoricoEmisionTarjetaCliente;

public class OracleHistoricoEmisionTarjetaClienteDao implements HistoricoEmisionTarjetaClienteDao {

	@Override
	public ArrayList<DtoHistoricoEmisionTarjetaCliente> cargar(
			DtoHistoricoEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseHistoricoEmisionTarjetaClienteDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoHistoricoEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseHistoricoEmisionTarjetaClienteDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoHistoricoEmisionTarjetaCliente dto) {
		
		return SqlBaseHistoricoEmisionTarjetaClienteDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoHistoricoEmisionTarjetaCliente dtoNuevo,
			DtoHistoricoEmisionTarjetaCliente dtoWhere) {
		
		return SqlBaseHistoricoEmisionTarjetaClienteDao.modificar(dtoNuevo, dtoWhere);
	}

}
