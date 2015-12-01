package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.dao.SalarioMinimoDao;
import com.princetonsa.dao.sqlbase.SqlBaseSalarioMinimoDao;

/**
 * @author rcancino
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class OracleSalarioMinimoDao implements SalarioMinimoDao 
{//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Ingresar un salario
	 */
	private static String ingresarSalarioStr="INSERT INTO salario_minimo(codigo,fecha_inicial,fecha_final,salario) values(seq_salario_minimo.nextval,?,?,?)";

	/**
	 * @see com.princetonsa.dao.SalarioMinimoDao#insertar(java.sql.Connection, double, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertar(
		Connection con,
		double salario,
		String fechaInicial,
		String fechaFinal)
		throws SQLException
	{
		return SqlBaseSalarioMinimoDao.insertar(con,salario,fechaInicial,fechaFinal, ingresarSalarioStr);
	}

	/**
	 * @see com.princetonsa.dao.SalarioMinimoDao#existeCruzeSalario(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public boolean existeCruzeSalario(
		Connection con,
		String fechaInicial,
		String fechaFinal)
		throws SQLException
	{
		return SqlBaseSalarioMinimoDao.existeCruzeSalario(con,fechaInicial,	fechaFinal);
	}
	/**
	 * @see com.princetonsa.dao.SalarioMinimoDao#consultar(java.sql.Connection)
	 */
	public ResultadoCollectionDB consultar(Connection con) throws SQLException
	{
		return SqlBaseSalarioMinimoDao.consultar(con);
	}
	/**
	 * @see com.princetonsa.dao.SalarioMinimoDao#modificar(java.sql.Connection, int, double, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificar(
		Connection con,
		int codigo,
		double salario,
		String fechaInicial,
		String fechaFinal)
		throws SQLException
	{
		return SqlBaseSalarioMinimoDao.modificar(con,codigo,salario,fechaInicial,fechaFinal);
	}
	
	public ResultadoCollectionDB consultarVigente (Connection con) throws SQLException
	{
		return SqlBaseSalarioMinimoDao.consultarVigente(con);
	}

}
