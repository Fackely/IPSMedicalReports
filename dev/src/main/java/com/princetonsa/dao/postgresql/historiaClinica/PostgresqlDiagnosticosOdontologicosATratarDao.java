package com.princetonsa.dao.postgresql.historiaClinica;



import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.DiagnosticosOdontologicosATratarDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseDiagnosticosOdontologicosATratarDao;






public class PostgresqlDiagnosticosOdontologicosATratarDao implements DiagnosticosOdontologicosATratarDao
{

	
	/**
	 * 
	 */
	public HashMap consultarDiagnosticosATratar(int institucion) 
	{
		return SqlBaseDiagnosticosOdontologicosATratarDao.consultarDiagnosticosATratar(institucion);
	}

	/**
	 * 
	 */
	public HashMap consultarDiagnosticosATratarEspecifico(Connection con,int codigo) 
	{
		return SqlBaseDiagnosticosOdontologicosATratarDao.consultarDiagnosticosATratarEspecifico(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertar(Connection con, HashMap vo) 
	{
		return SqlBaseDiagnosticosOdontologicosATratarDao.insertar(con,vo);
	}


	/**
	 * 
	 */
	public boolean modificar(Connection con, HashMap vo) 
	{
		return SqlBaseDiagnosticosOdontologicosATratarDao.modificar(con,vo);
	}
	
}
