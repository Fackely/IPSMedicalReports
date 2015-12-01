package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetalleServicioHonorariosDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleServicioHonorarioDao;
import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.servinte.axioma.fwk.exception.IPSException;

public class PostgresqlDetalleServicioHonorarioDao implements DetalleServicioHonorariosDao {

	@Override
	public ArrayList<DtoDetalleServicioHonorarios> cargar(
			DtoDetalleServicioHonorarios dtoWhere) throws IPSException {
		
		return SqlBaseDetalleServicioHonorarioDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoDetalleServicioHonorarios dtoWhere) {
		
		return SqlBaseDetalleServicioHonorarioDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoDetalleServicioHonorarios dto) {
		
		return SqlBaseDetalleServicioHonorarioDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoDetalleServicioHonorarios dtoNuevo,
			DtoDetalleServicioHonorarios dtoWhere ,boolean siVacioUpdateNull ) {
		
		return SqlBaseDetalleServicioHonorarioDao.modificar(dtoNuevo,dtoWhere , siVacioUpdateNull);
	}

	@Override
	public boolean existeDetalleServicio(double codigoHonorario, int Servicio,
			int especialidad, double codigoNotIn) {
		
		return SqlBaseDetalleServicioHonorarioDao.existeDetalleServicio(codigoHonorario, Servicio, especialidad, codigoNotIn);
	}

}
