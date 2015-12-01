package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ServicioHonorariosDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseServicioHonorariosDao;
import com.princetonsa.dto.odontologia.DtoServicioHonorarios;

/**
 * 
 * @author axioma
 *
 */
public class OracleServicioHonorariosDao implements ServicioHonorariosDao 
{
	@Override
	public ArrayList<DtoServicioHonorarios> cargar(	DtoServicioHonorarios dtoWhere) 
	{
		return SqlBaseServicioHonorariosDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoServicioHonorarios dtoWhere) 
	{
		return SqlBaseServicioHonorariosDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoServicioHonorarios dto) 
	{
		return SqlBaseServicioHonorariosDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoServicioHonorarios dtoNuevo,DtoServicioHonorarios dtoWhere) 
	{
		return SqlBaseServicioHonorariosDao.modificar(dtoNuevo, dtoWhere);
	}
	
}
