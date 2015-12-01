package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.HallazgoVsProgramaServicioDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseHallazgoVsProgramaServicioDao;
import com.princetonsa.dto.odontologia.DtoHallazgoVsProgramaServicio;

public class OracleHallazgoVsProgramaServicioDao implements HallazgoVsProgramaServicioDao{

	@Override
	public ArrayList<DtoHallazgoVsProgramaServicio> cargar(
			DtoHallazgoVsProgramaServicio dtoWhere) {
		
		return  SqlBaseHallazgoVsProgramaServicioDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoHallazgoVsProgramaServicio dtoWhere) {
		
		
		return  SqlBaseHallazgoVsProgramaServicioDao.eliminar(dtoWhere);
		
	}

	@Override
	public double guardar(DtoHallazgoVsProgramaServicio dto) {
		
		return  SqlBaseHallazgoVsProgramaServicioDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoHallazgoVsProgramaServicio dtoNuevo,
			DtoHallazgoVsProgramaServicio dtoWhere) {
		
		return  SqlBaseHallazgoVsProgramaServicioDao.modificar(dtoNuevo, dtoWhere);
	}

}
