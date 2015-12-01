package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetalleAgrupacionHonorarioDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleAgrupacionHonorarioDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleServicioHonorarioDao;

import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;

public class OracleDetalleAgrupacionHonorarioDao implements DetalleAgrupacionHonorarioDao {

	@Override
	public ArrayList<DtoDetalleAgrupacionHonorarios> cargar(
			DtoDetalleAgrupacionHonorarios dtoWhere) {
		
		return SqlBaseDetalleAgrupacionHonorarioDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoDetalleAgrupacionHonorarios dtoWhere) {
		
		return SqlBaseDetalleAgrupacionHonorarioDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoDetalleAgrupacionHonorarios dto) {
		
		return SqlBaseDetalleAgrupacionHonorarioDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoDetalleAgrupacionHonorarios dtoNuevo,
			DtoDetalleAgrupacionHonorarios dtoWhere, boolean siVacioUpdateNull) {
		
		return SqlBaseDetalleAgrupacionHonorarioDao.modificar(dtoNuevo,dtoWhere, siVacioUpdateNull);
	}

	/**
	 * 
	 * @param codigoHonorario
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @return
	 */
	public boolean existeDetalleAgrupacion(double codigoHonorario, int grupoServicio, String tipoServicio, int especialidad,  double codigoNotIn)
	{
		return SqlBaseDetalleAgrupacionHonorarioDao.existeDetalleAgrupacion(codigoHonorario, grupoServicio, tipoServicio, especialidad, codigoNotIn);
	}
}
