package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.GrupoEspecialArticulosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseGrupoEspecialArticulosDao;

public class OracleGrupoEspecialArticulosDao implements
		GrupoEspecialArticulosDao 
		
{

	
	/**
	 * 
	 */
	public HashMap consultarMotivoEspecifico(Connection con, int codigoGrupo) 
	{
		return SqlBaseGrupoEspecialArticulosDao.consultarMotivoEspecifico(con, codigoGrupo);
	}

	/**
	 * 
	 */
	public HashMap consultarMotivosExistentes(Connection con) 
	{
		return SqlBaseGrupoEspecialArticulosDao.consultarMotivosExistentes(con);
	}

	/**
	 * 
	 */
	public boolean eliminarRegistro(Connection con, int codigo) 
	{
		return SqlBaseGrupoEspecialArticulosDao.eliminarRegistro(con, codigo);
	}

	/**
	 * 
	 */
	public boolean insertar(Connection con, HashMap vo) 
	{
		return SqlBaseGrupoEspecialArticulosDao.insertar(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificar(Connection con, HashMap vo) 
	{
		return SqlBaseGrupoEspecialArticulosDao.modificar(con, vo);
	}

	
}
