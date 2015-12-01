package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;
import com.princetonsa.dao.odontologia.AliadoOdontologicoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseAliadoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;

public class OracleAliadoOdontologicoDao implements AliadoOdontologicoDao
{
	
	/**
	 * 
	 */
	public boolean modificar(DtoAliadoOdontologico dtoNuevo, DtoAliadoOdontologico dtoWhere)
	{
		return SqlBaseAliadoOdontologicoDao.modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 */
	public double guardar(DtoAliadoOdontologico dto) 
	{
		return SqlBaseAliadoOdontologicoDao.guardar(dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoAliadoOdontologico> cargar(DtoAliadoOdontologico dto) 
	{
		return SqlBaseAliadoOdontologicoDao.cargar(dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminar(DtoAliadoOdontologico dto) 
	{
		return SqlBaseAliadoOdontologicoDao.eliminar(dto);
	}

	@Override
	public ArrayList<DtoAliadoOdontologico> cargarDetalle(
			DtoAliadoOdontologico dtoWhere) {
		
		return null;
	}

	

}
