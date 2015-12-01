package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.TiposTratamientosOdontologicosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseTiposTratamientosOdontologicosDao;
import com.princetonsa.mundo.historiaClinica.TiposTratamientosOdontologicos;

public class OracleTiposTratamientosOdontologicosDao implements TiposTratamientosOdontologicosDao
{
	/**
	 * Metodo de consulta de los tipos de Tratmaiento Odontologicos
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaTiposT (Connection con, int codigoInstitucion)
	{
		return SqlBaseTiposTratamientosOdontologicosDao.consultaTiposT(con, codigoInstitucion);
	}
	
	public boolean eliminarTipoT(Connection con, String tipoT)
	{
		return SqlBaseTiposTratamientosOdontologicosDao.eliminar(con, tipoT);
	}
	
	public boolean insertarTiposT(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos){
		return SqlBaseTiposTratamientosOdontologicosDao.insertar(con, tiposTratamientosOdontologicos);
	}
	
	public boolean modificarTiposT(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos){
		return SqlBaseTiposTratamientosOdontologicosDao.modificar(con, tiposTratamientosOdontologicos);
	}
}