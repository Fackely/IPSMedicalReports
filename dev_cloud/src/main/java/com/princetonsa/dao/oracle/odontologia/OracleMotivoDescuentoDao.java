package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;
import com.princetonsa.dao.odontologia.MotivosDescuentosDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseMotivosDescuentosOdonDao;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;

public class OracleMotivoDescuentoDao implements MotivosDescuentosDao
{
	
	/**
	 * 
	 */
	public boolean modificar(DtoMotivoDescuento dtoNuevo, DtoMotivoDescuento dtoWhere)
	{
		return SqlBaseMotivosDescuentosOdonDao.modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 */
	public double guardar(DtoMotivoDescuento dto) 
	{
		return SqlBaseMotivosDescuentosOdonDao.guardar(dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoMotivoDescuento> cargar(DtoMotivoDescuento dto) 
	{
		return SqlBaseMotivosDescuentosOdonDao.cargar(dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminar(DtoMotivoDescuento dto) 
	{
		return SqlBaseMotivosDescuentosOdonDao.eliminar(dto);
	}
	
	
	
	@Override
	public int validarExistenciaMotivos(double codigoPkMotivo) {
		
		return SqlBaseMotivosDescuentosOdonDao.validarExistenciaMotivos(codigoPkMotivo);
	}

}
