package com.princetonsa.dao.postgresql.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.facturacion.HonorariosPoolDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseHonorariosPool;
import com.princetonsa.dto.odontologia.DtoAgrupHonorariosPool;
import com.princetonsa.dto.odontologia.DtoHonorarioPoolServicio;
import com.princetonsa.dto.odontologia.DtoHonorariosPool;

public class PostgresqlHonorariosPoolDao implements HonorariosPoolDao {
	
	@Override
	public ArrayList<DtoAgrupHonorariosPool> cargarAgrupacionHonorariosPool(DtoAgrupHonorariosPool dto) {
			return SqlBaseHonorariosPool.cargarAgrupacionHonorariosPool(dto);
	}

	@Override
	public ArrayList<DtoHonorariosPool> cargarHonorariosPool(DtoHonorariosPool dto) {
		return SqlBaseHonorariosPool.cargarHonorariosPool(dto);
	}

	@Override
	public ArrayList<DtoHonorarioPoolServicio> cargarHonorariosPoolServ(DtoHonorarioPoolServicio dto, int institucion) {
		return SqlBaseHonorariosPool.cargarHonorariosPoolServ(dto, institucion);
	}

	@Override
	public double guardarGruposHonorarioPool(Connection con, DtoAgrupHonorariosPool dto) {
		return SqlBaseHonorariosPool.guardarGruposHonorarioPool(con, dto);
	}

	@Override
	public double guardarHonorariosPool(DtoHonorariosPool dto) {
		return SqlBaseHonorariosPool.guardarHonorariosPool(dto);
	}

	@Override
	public double guardarHonoriarioPoolServicio(Connection con, DtoHonorarioPoolServicio dto) {
		return SqlBaseHonorariosPool.guardarHonoriarioPoolServicio(con, dto);	
	}

	@Override
	public boolean eliminarAgrupacionHonorario(DtoAgrupHonorariosPool dto) {
		return SqlBaseHonorariosPool.eliminarAgrupacionHonorario(dto);
	}

	@Override
	public boolean eliminarHonorariosPoolServicio(DtoHonorarioPoolServicio dto) {
		return SqlBaseHonorariosPool.eliminarHonorariosPoolServicio(dto);
	}

	@Override
	public boolean modicarAgrupacionServicios(DtoAgrupHonorariosPool dto) {
		return SqlBaseHonorariosPool.modicarAgrupacionServicios(dto);
	}

	@Override
	public boolean modicarHonorarioPoolServicios(DtoHonorarioPoolServicio dto) {
		return SqlBaseHonorariosPool.modicarHonorarioPoolServicios(dto);
	}
	
	@Override
	public boolean modificarHonorarioPool(DtoHonorariosPool dto) {
		return SqlBaseHonorariosPool.modificarHonorarioPool(dto);
	}
	
	@Override
	public boolean eliminarHonorarioPool(BigDecimal codigoPk) {
		return SqlBaseHonorariosPool.eliminarHonorarioPool(codigoPk);
	}

	@Override
	public boolean existeHonorarioPool(int pool, int convenioEXCLUYENTE,
			int esquemaEXCLUYENTE, int centroAtencion, BigDecimal codigoPkNOTIN) {
		return SqlBaseHonorariosPool.existeHonorarioPool(pool, convenioEXCLUYENTE, esquemaEXCLUYENTE, centroAtencion, codigoPkNOTIN);
	}
}
