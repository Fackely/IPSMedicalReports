package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.MotivosSircDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseMotivosSircDao;

public class PostgresqlMotivosSircDao implements MotivosSircDao
{

	/**
	 * 
	 */
	public HashMap consultarMotivoSirc(Connection con, HashMap vo)
	{
		return SqlBaseMotivosSircDao.consultarMotivoSirc(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarMotivoSirc(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO motivos_sirc (consecutivo,codigo,descripcion,tipo_motivo,activo,institucion) values (nextval('seq_motivos_sirc'),?,?,?,?,?)";
		return SqlBaseMotivosSircDao.insertarMotivoSirc(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarMotivoSirc(Connection con, HashMap vo)
	{
		return SqlBaseMotivosSircDao.modificarMotivoSirc(con,vo);
	}
	

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String consecutivo)
	{
		return SqlBaseMotivosSircDao.eliminarRegistro(con,consecutivo);
	}
	

}
