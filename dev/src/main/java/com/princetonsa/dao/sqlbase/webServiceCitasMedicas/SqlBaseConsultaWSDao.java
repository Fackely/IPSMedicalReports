

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dao.sqlbase.webServiceCitasMedicas;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ConstantesBD;


/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class SqlBaseConsultaWSDao
{

	private static String consultaCita="select count(1) as numero from cita where 1=1 ";
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static int obtenerCitasAntendiasPortal(Connection con, String fechaInicial, String fechaFinal)
	{
		String cadena=consultaCita+" and estado_cita = "+ConstantesBD.codigoEstadoCitaAtendida+" and webservice='"+ConstantesBD.acronimoSi+"' and fecha_gen between '"+fechaInicial+"' and '"+fechaFinal+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static int obtenerCitasSolicitadasPortal(Connection con, String fechaInicial, String fechaFinal)
	{
		String cadena=consultaCita+" and webservice='"+ConstantesBD.acronimoSi+"' and fecha_gen between '"+fechaInicial+"' and '"+fechaFinal+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static int obtenerTotalCitas(Connection con, String fechaInicial, String fechaFinal)
	{
		String cadena=consultaCita+" and fecha_gen between '"+fechaInicial+"' and '"+fechaFinal+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

}
