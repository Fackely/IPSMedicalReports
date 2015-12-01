package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.HistoricoDescuentoOdontologicoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHistoricoDescuentoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologicoAtencion;

public class PostgresqlHistoricoDescuentoOdontologicoDao  implements HistoricoDescuentoOdontologicoDao{

	@Override
	public ArrayList<DtoHistoricoDescuentoOdontologico> cargar(
			DtoHistoricoDescuentoOdontologico dtoWhere) {
		
		return SqlBaseHistoricoDescuentoOdontologicoDao.cargar(dtoWhere);
	}

	@Override
	public double guardar(DtoHistoricoDescuentoOdontologico dto) {
		
		return SqlBaseHistoricoDescuentoOdontologicoDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoHistoricoDescuentoOdontologico dtoNuevo,
			DtoHistoricoDescuentoOdontologico dtoWhere) {
		
		return SqlBaseHistoricoDescuentoOdontologicoDao.modificar(dtoNuevo, dtoWhere);
	}
	
	@Override
	public ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> cargarAtencion(
			DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) {
		
		return SqlBaseHistoricoDescuentoOdontologicoDao.cargarAtencion(dtoWhere);
	}

	@Override
	public double guardarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dto) {
		
		return SqlBaseHistoricoDescuentoOdontologicoDao.guardarAtencion(dto);
	}

	@Override
	public boolean modificarAtencion(
			DtoHistoricoDescuentoOdontologicoAtencion dtoNuevo,
			DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) {
		
		return SqlBaseHistoricoDescuentoOdontologicoDao.modificarAtencion(dtoNuevo, dtoWhere);
	}

	
}
