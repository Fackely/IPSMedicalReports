package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;


import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.dao.odontologia.ProgramaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseProgramaDao;


public class PostgresqlProgramasDao implements ProgramaDao{
	
	@Override
	public ArrayList<DtoPrograma> cargar(DtoPrograma dto) {
		return	SqlBaseProgramaDao.cargar(dto);
		 
	}

	@Override
	/**
	 * 
	 */
	public boolean eliminar(DtoPrograma dto) {
		return SqlBaseProgramaDao.eliminar(dto);
	}

	/**
	 * 
	 */
	public double guardar(DtoPrograma dto) {
		return SqlBaseProgramaDao.guardar(dto);
	}

	@Override
	/**
	 * 
	 */
	public boolean modificar(DtoPrograma dto) {
		return SqlBaseProgramaDao.modificar(dto);
	}

	@Override
	public ArrayList<DtoPrograma> cargarConsultaAvanzada(DtoPrograma dto, boolean incluirInactivos) {
		
		return SqlBaseProgramaDao.cargarAvanzado(dto, incluirInactivos);
	}

	@Override
	public String obtenerEspeciliadadPrograma(double programa) {
		return SqlBaseProgramaDao.obtenerEspeciliadadPrograma(programa);
	}
}
