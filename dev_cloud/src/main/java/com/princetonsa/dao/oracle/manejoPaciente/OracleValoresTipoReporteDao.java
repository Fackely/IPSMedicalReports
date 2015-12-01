package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ValoresTipoReporteDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseValoresTipoReporteDao;

public class OracleValoresTipoReporteDao implements ValoresTipoReporteDao 
{

	
	/**
	 * 
	 */
	public HashMap consultarInfoTipoReporte(Connection con, String codigoReporte) 
	{
		return SqlBaseValoresTipoReporteDao.consultarInfoTipoReporte(con, codigoReporte);
	}

	/**
	 * 
	 */
	public HashMap consultarParametrizacion(Connection con, String codigoReporte) 
	{
		return SqlBaseValoresTipoReporteDao.consultarParametrizacion(con, codigoReporte);
	}

	/**
	 * 
	 */
	public boolean eliminarRegistro(Connection con, String codigo) 
	{
		return SqlBaseValoresTipoReporteDao.eliminarRegistro(con, codigo);
	}

	/**
	 * 
	 */
	public boolean insertar(Connection con, HashMap vo) 
	{
		return SqlBaseValoresTipoReporteDao.insertar(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificar(Connection con, HashMap vo) 
	{
		return SqlBaseValoresTipoReporteDao.modificar(con, vo);
	}
	
	/**
	 * Método implementado para verificar si existe parametrizacion de los valores
	 * de un tipo de reporte específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existeParametrizacionValoresTipoReporte(Connection con,HashMap campos)
	{
		return SqlBaseValoresTipoReporteDao.existeParametrizacionValoresTipoReporte(con, campos);
	}
	
	
}
