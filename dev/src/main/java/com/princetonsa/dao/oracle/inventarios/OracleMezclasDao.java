package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.MezclasDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseMezclasDao;

public class OracleMezclasDao implements MezclasDao
{
	public void insertarMezcla(Connection con, String codigo, String nombre, int codTipoMezcla,
			String activo, int codInstitucion) throws SQLException 
	{
		SqlBaseMezclasDao.insertarMezcla(con, codigo, nombre, codTipoMezcla, activo, codInstitucion);
	}

	public void actualizarMezcla(Connection con, int consecutivo,
			String nombre, int codTipoMezcla, String activo) throws SQLException
	{
		SqlBaseMezclasDao.actualizarMezcla(con, consecutivo, nombre, codTipoMezcla, activo);
	}

	public void eliminarMezcla(Connection con, int consecutivo)
			throws SQLException
	{
		SqlBaseMezclasDao.eliminarMezcla(con, consecutivo);
	}

	public HashMap consultarMezcla(Connection con, int consecutivo)
			throws SQLException
	{
		return SqlBaseMezclasDao.consultarMezcla(con, consecutivo);
	}

	public HashMap consultarMezclasInst(Connection con, int codInstitucion)
			throws SQLException
	{
		return SqlBaseMezclasDao.consultarMezclasInst(con, codInstitucion);
	}
}
