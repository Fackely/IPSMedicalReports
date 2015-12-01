package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DescuentoOdontologicoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDescuentoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;

public class PostgresqlDescuentoOdontologicoDao implements DescuentoOdontologicoDao  {

	@Override
	public ArrayList<DtoDescuentosOdontologicos> cargar(
			DtoDescuentosOdontologicos dtoWhere) {
		
		return SqlBaseDescuentoOdontologicoDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoDescuentosOdontologicos dtoWhere) {
		
		return SqlBaseDescuentoOdontologicoDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoDescuentosOdontologicos dto) {
		
		return SqlBaseDescuentoOdontologicoDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoDescuentosOdontologicos dtoNuevo,
			DtoDescuentosOdontologicos dtoWhere) {
		
		return SqlBaseDescuentoOdontologicoDao.modificar(dtoNuevo, dtoWhere);
	}
	
	@Override
	public boolean existeCruceFechas(DtoDescuentosOdontologicos dto,
			double codigoPkNotIn, int centroAtencion) {
		
		return SqlBaseDescuentoOdontologicoDao.existeCruceFechas(dto, codigoPkNotIn, centroAtencion);
	}

	
	@Override
	public ArrayList<DtoDescuentoOdontologicoAtencion> cargarAtencion(
			DtoDescuentoOdontologicoAtencion dtoWhere) {
		
		return SqlBaseDescuentoOdontologicoDao.cargarAtencion(dtoWhere);
	}

	@Override
	public double guardarAtencion(DtoDescuentoOdontologicoAtencion dto) {
		
		return SqlBaseDescuentoOdontologicoDao.guardarAtencion(dto);
	}

	@Override
	public boolean modificarAtencion(DtoDescuentoOdontologicoAtencion dtoNuevo,
			DtoDescuentoOdontologicoAtencion dtoWhere) {
		
		return SqlBaseDescuentoOdontologicoDao.modificarAtencion(dtoNuevo, dtoWhere);
	}
	
	@Override
	public boolean eliminarAtencion(DtoDescuentoOdontologicoAtencion dtoWhere) {
		
		return SqlBaseDescuentoOdontologicoDao.eliminarAtencion(dtoWhere);
	}
	
}
