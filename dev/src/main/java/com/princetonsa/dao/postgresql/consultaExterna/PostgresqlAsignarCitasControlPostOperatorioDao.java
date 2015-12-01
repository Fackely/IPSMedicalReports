package com.princetonsa.dao.postgresql.consultaExterna;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.consultaExterna.AsignarCitasControlPostOperatorioDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseAsignarCitasControlPostOperatorioDao;

/**
 * @author Mauricio Jllo
 * Fecha Mayo de 2008
 */

public class PostgresqlAsignarCitasControlPostOperatorioDao implements AsignarCitasControlPostOperatorioDao
{

	/**
	 * 
	 */
	public HashMap consultarCitasReservadas(Connection con, int codigoPaciente)
    {
        return SqlBaseAsignarCitasControlPostOperatorioDao.consultarCitasReservadas(con, codigoPaciente);
    }
	
	/**
	 * 
	 */
	public int insertarSubCuenta(Connection con, String usuario, HashMap datosSubCuenta) 
	{
		try
		{
			String cadena="select nextval('seq_sub_cuentas') ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return SqlBaseAsignarCitasControlPostOperatorioDao.insertarSubCuenta(con, rs.getInt(1), usuario, datosSubCuenta);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 */
	public boolean controlPostOperatorio(Connection con, int idIngreso)
    {
        return SqlBaseAsignarCitasControlPostOperatorioDao.controlPostOperatorio(con, idIngreso);
    }
	
}