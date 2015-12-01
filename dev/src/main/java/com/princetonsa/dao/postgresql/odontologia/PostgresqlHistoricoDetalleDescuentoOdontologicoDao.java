package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.HistoricoDetalleDescuentoOdontologico;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHistoricoDetalleDescuentoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleDescuentoOdontologico;

public class PostgresqlHistoricoDetalleDescuentoOdontologicoDao implements HistoricoDetalleDescuentoOdontologico {

	@Override
	public ArrayList<DtoHistoricoDetalleDescuentoOdontologico> cargar(
			DtoHistoricoDetalleDescuentoOdontologico dtoWhere) {
		
		return SqlBaseHistoricoDetalleDescuentoOdontologicoDao.cargar(dtoWhere);
	}

	@Override
	public double guardar(DtoHistoricoDetalleDescuentoOdontologico dto) {
		
		return SqlBaseHistoricoDetalleDescuentoOdontologicoDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoHistoricoDetalleDescuentoOdontologico dtoNuevo,
			DtoHistoricoDetalleDescuentoOdontologico dtoWhere) {
		
		return SqlBaseHistoricoDetalleDescuentoOdontologicoDao.modificar(dtoNuevo, dtoWhere);
	}

	
}
