package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaSaldosCierresInventariosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaSaldosCierresInventariosDao;

/**
 * @author Mauricio Jaramillo Henao
 * Fecha: Agosto de 2008
 */

public class OracleConsultaSaldosCierresInventariosDao implements ConsultaSaldosCierresInventariosDao
{

	/**
	 * 
	 */
	public HashMap consultarSaldosCierresInventarios(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaSaldosCierresInventariosDao.consultarSaldosCierresInventarios(con, criterios);
	}
	
	/**
	 * 
	 */
	public String consultarCondicionesSaldosCierresInventarios(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaSaldosCierresInventariosDao.consultarCondicionesSaldosCierresInventarios(con, criterios);
	}
	
}