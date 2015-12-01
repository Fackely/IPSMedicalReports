/*
 * Creado el 12/04/2005
 * Juan David Ram�rez L�pez
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;


/**
 * @author Juan David Ram�rez
 * 
 * CopyRight Princeton S.A.
 * 12/04/2005
 */
public class SqlBaseDiasFestivos
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseDiasFestivos.class);
	
	/**
	 * Sentencia SQL para ingresar un d�a festivo
	 */
	private static String ingresarDiaFestivoStr="INSERT INTO dias_festivos (fecha, descripcion, tipo) VALUES(?,?,?)";

	/**
	 * Sentencia SQL para modificar un d�a festivo
	 */
	private static String modificarDiaFestivoStr="UPDATE dias_festivos SET descripcion=?, tipo=? WHERE fecha=?";

	/**
	 * Sentencia SQL para eliminar un d�a festivo
	 */
	private static String eliminarDiaFestivoStr="DELETE FROM dias_festivos WHERE fecha=?";
	
	/**
	 * Sentencia SQL para hacer un listado de los d�as festivos para determinado a�o
	 */
	private static String listarDiasFestivosPorAnioStr="";
	
	/**
	 * Sentencia SQL para consultar un d�a festivo
	 */
	private static String consultarDiaFestivoStr="SELECT df.fecha AS fecha, df.descripcion AS descripcion, df.tipo AS codigotipo, td.descripcion AS tipo FROM dias_festivos df INNER JOIN tipos_dia_festivo td ON(df.tipo=td.codigo) WHERE df.fecha=?";
	
	/**
	 * M�todo para inserar un d�a festivo
	 * @param con
	 * @param fecha
	 * @param descripcion
	 * @param tipo
	 * @return n�mero de elementos ingresados
	 */
	public static int insertar(Connection con, String fecha, String descripcion, int tipo)
	{
		try
		{
			PreparedStatementDecorator insertatStm= new PreparedStatementDecorator(con.prepareStatement(ingresarDiaFestivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertatStm.setString(1,fecha);
			insertatStm.setString(2,descripcion);
			insertatStm.setInt(3,tipo);
			return insertatStm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error insertando el d�a festivo : "+e);
			return 0;
		}
	}
	
	/**
	 * M�todo para misdificar un d�a festivo
	 * @param con
	 * @param fecha
	 * @param descripcion
	 * @param tipo
	 * @return n�mero de elementos modificados
	 */
	public static int modificar(Connection con, String fecha, String descripcion, int tipo)
	{
		try
		{
			PreparedStatementDecorator insertatStm= new PreparedStatementDecorator(con.prepareStatement(modificarDiaFestivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertatStm.setString(1,descripcion);
			insertatStm.setInt(2,tipo);
			insertatStm.setString(3,fecha);
			return insertatStm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error modificando el d�a festivo : "+e);
			return 0;
		}
	}

	/**
	 * M�todo para eliminar un d�a festivo
	 * @param con
	 * @param fecha
	 * @return numero de elementos eliminados
	 */
	public static int eliminar(Connection con, String fecha)
	{
		try
		{
			PreparedStatementDecorator elim= new PreparedStatementDecorator(con.prepareStatement(eliminarDiaFestivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			elim.setString(1, fecha);
			return elim.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error eliminando un d�a festivo : "+e);
			return 0;
		}
	}
	
	/**
	 * M�todo para listar todos los d�as desftivos parametrizados para determinado a�o
	 * @param con Conexion con la BD
	 * @param anio A�o que se quiere consultar
	 * @return Collection con el listado de todos los d�as festivos
	 */
	public static Collection listar(Connection con, String anio)
	{
		if(anio==null || anio.equals(""))
		{
			return new ArrayList();
		}
		try
		{
			
			listarDiasFestivosPorAnioStr = "SELECT "+anio+" || '-' || to_char(df.fecha, 'MM-DD') AS fechaVariable, df.descripcion AS descripcion, t.descripcion AS tipo FROM administracion.dias_festivos df  INNER JOIN administracion.tipos_dia_festivo t ON(t.codigo=df.tipo) WHERE to_char(df.fecha, 'YYYY')='"+anio+"' OR df.tipo="+ConstantesBD.codigoTipoDiaFestivoEstatico+" GROUP BY "+anio+" || '-' || to_char(df.fecha, 'MM-DD'), df.descripcion, t.descripcion";
			logger.info("listarDiasFestivosPorAnioStr----------->"+listarDiasFestivosPorAnioStr);
			
			PreparedStatementDecorator listadoStm= new PreparedStatementDecorator(con.prepareStatement(listarDiasFestivosPorAnioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//listadoStm.setString(1, anio);
			//listadoStm.setString(2, anio);
			//listadoStm.setString(3, anio);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(listadoStm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error listando los d�as festivos : "+e);
			return null;
		}
	}

	/**
	 * M�todo para cargar un d�a festivo
	 * @param con Conexi�n con la BD
	 * @param fecha Fecha del d�a festivo que se desea cargar
	 * @param incluirDomingos true para incluir los domingos como festivos
	 * @return true si carg� el d�a festivo
	 */
	public static Collection cargar(Connection con, String fecha, boolean incluirDomingos)
	{
		try
		{
		    fecha=UtilidadFecha.conversionFormatoFechaABD(fecha);
		    String cadenaGenerada=consultarDiaFestivoStr;
		    if (incluirDomingos)
		    {
		        cadenaGenerada+=" UNION SELECT ? AS fecha, 'Domingo' AS descripcion, '"+ConstantesBD.codigoTipoDiaFestivoEstatico+"' AS codigoTipo, (SELECT descripcion FROM tipos_dia_festivo WHERE codigo="+ConstantesBD.codigoTipoDiaFestivoEstatico+") AS tipo WHERE EXTRACT(DOW FROM to_date(?, 'YYYY-MM-DD')) = 0";
		    }
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadenaGenerada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			stm.setString(1, fecha);
			if(incluirDomingos)
			{
			    stm.setString(2, fecha);
			    stm.setString(3, fecha);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error cargando el d�a festivo ("+fecha+"): "+e);
			return null;
		}
	}

}
