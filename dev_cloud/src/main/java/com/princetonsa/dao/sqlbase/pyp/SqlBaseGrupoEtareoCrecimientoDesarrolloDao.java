
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de Grupo Etareo De Crecimiento y Desarrollo
 * @version 1.0  28 /jul/ 2006
 */
public class SqlBaseGrupoEtareoCrecimientoDesarrolloDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseGrupoEtareoCrecimientoDesarrolloDao.class);
	
	
	/**
	 * Cadena con el statement necesario para consultar los grupos etareos existentes para la institucion
	 */
	private final static String consultarGruposEtareosStr = " SELECT gecd.codigo as codigointerno, " +
															" gecd.consecutivo as codigo, " +
															" gecd.descripcion as descripcion, " +
															" gecd.unidad_medida as codigounidadmedida, " +
															" umf.nombre as nombreunidadmedida, " +
															" gecd.rango_inicial as rangoinicial, " +
															" gecd.rango_final as rangofinal, " +
															" case when gecd.sexo is null then -2 else gecd.sexo end as codigosexo, " +
															" case when gecd.sexo is null then 'Ambos' else s.nombre end as nombresexo, " +
															" case when gecd.activo = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' then 'true' else 'false' end as activo, " +
															" 'si' as existebd, " +
															" 'false' as eseliminado " +
															" FROM grup_etareo_creci_desa gecd " +
															" INNER JOIN unidad_medida_fechas umf ON(gecd.unidad_medida=umf.codigo) " +
															" LEFT OUTER JOIN sexo s ON(gecd.sexo=s.codigo) " +
															" WHERE gecd.institucion = ? " +
															" ORDER BY gecd.rango_inicial ASC, gecd.rango_final ASC ";
	
	/**
	 * Cadena con el statement necesario para modificar los grupos etareos
	 */
	private final static String modificarGrupoEtareoStr = " UPDATE grup_etareo_creci_desa " +
														  " SET consecutivo = ? ," +
														  " descripcion = ?, " +
														  " unidad_medida = ?," +
														  " rango_inicial = ?, " +
														  " rango_final = ?, " +
														  " sexo = ?, " +
														  " activo = ?" +
														  " WHERE codigo = ? " ;
	
	/**
	 * Cadena con el statement necesario para eliminar un grupo etareo
	 */
	private final static String eliminarGrupoEtareoStr = " DELETE FROM grup_etareo_creci_desa " +
    												     " WHERE codigo = ? ";
	
	
	
	/**
	 * devuelve el codigo del asocio máximo
	 */
	private final static String maxCodigoInsertadoStr = " SELECT max(codigo) as mayor " +
														" FROM grup_etareo_creci_desa gecd " +
														" WHERE gecd.institucion = ? ";
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun la institucion del usuario cargado en session
	 * @param con
	 * @param institucion
	 * @return HashMap
	 * @throws SQLException
	 */
	public static HashMap consultarGruposEtareos (Connection con, int institucion) throws SQLException
	{
		try
		{
			logger.info("Consulta "+consultarGruposEtareosStr);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarGruposEtareosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarGruposEtareos : [SqlBaseGrupoEtareoCrecimientoDesarrolloDao] "+e.toString() );
			return null;
		}	    
	}
	
	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static boolean eliminarGrupoEtareo(Connection con, int codigoInterno) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInterno);
			ps.executeUpdate();
			return  true;
			
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en eliminarGrupoEtareo : [SqlBaseGrupoEtareoCrecimientoDesarrolloDao] "+e.toString() );
			return false;
		}
		
	}
	
	/**
	 * Metodo par modificar un grupo estareo
	 * @param con
	 * @param codigoInterno
	 * @param codigo
	 * @param descripcion
	 * @param unidadMedida
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param codigoSexo
	 * @param activo
	 * @return
	 */
	public static boolean modificarGrupoEtareo(Connection con, int codigoInterno, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo) 
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}

			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE grup_etareo_creci_desa " +
														  " SET consecutivo = ? ," +
														  " descripcion = ?, " +
														  " unidad_medida = ?," +
														  " rango_inicial = ?, " +
														  " rango_final = ?, " +
														  " sexo = ?, " +
														  " activo = ?" +
														  " WHERE codigo = ?
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setString(2, descripcion);
			ps.setDouble(3, Utilidades.convertirADouble(unidadMedida+""));
			ps.setDouble(4, Utilidades.convertirADouble(rangoInicial+""));
			ps.setDouble(5, Utilidades.convertirADouble(rangoFinal+""));
			
			if(codigoSexo==ConstantesBD.codigoSexoAmbos)
				ps.setNull(6,Types.INTEGER);
			else
				ps.setInt(6, codigoSexo);
			if(activo.equals("true"))
			{
				ps.setBoolean(7, true);
			}
			else
			{
				ps.setBoolean(7, false);
			}
			ps.setDouble(8, Utilidades.convertirADouble(codigoInterno+""));
			
			if(ps.executeUpdate() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos [SqlBaseGrupoEtareoCrecimientoDesarrolloDao]: "+e.toString());
			return false;			
		}	
	}
	
	
	/**
	 * Método para insertar un grupo estare de crecimiento y desarrollo
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param unidadMedida
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param codigoSexo
	 * @param activo
	 * @param insertarGrupoEtareoStr
	 * @return
	 */
	public static int insertarGrupoEtareo(Connection con, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo, int institucion, String insertarGrupoEtareoStr)
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO grup_etareo_creci_desa " +
											     " (codigo, " +
											     " consecutivo, " +
											     " descripcion, " +
											     " unidad_medida, " +
											     " rango_inicial, " +
											     " rango_final, " +
											     " sexo, " +
											     " activo, " +
											     " institucion) " +
											     " VALUES ('seq_grup_etareo_creci_desa'), ?, ?, ?, ?, ?, ?, ?, ?) 
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setString(2, descripcion);
			ps.setDouble(3, Utilidades.convertirADouble(unidadMedida+""));
			ps.setDouble(4, Utilidades.convertirADouble(rangoInicial+""));
			ps.setDouble(5, Utilidades.convertirADouble(rangoFinal+""));
			if(codigoSexo==ConstantesBD.codigoSexoAmbos)
				ps.setNull(6,Types.INTEGER);
			else
				ps.setInt(6, codigoSexo);
			if(activo.equals("true"))
			{
				ps.setBoolean(7, true);
			}
			else
			{
				ps.setBoolean(7, false);
			}
			ps.setInt(8, institucion);
			if(ps.executeUpdate()>0)
			{
				return maxCodigoInsertadoStr(con, institucion);
			}
			else
			{
				return ConstantesBD.codigoNuncaValido;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos [SqlBaseGrupoEtareoCrecimientoDesarrolloDao]: "+e.toString() );
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * metodo que devuelve el maximo código insertado
	 * @param con
	 * @return
	 */
	public static int maxCodigoInsertadoStr( Connection con , int institucion)
	{
	    try
	    {
	        PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(maxCodigoInsertadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1, institucion);
	        ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	           return rs.getInt("mayor");
	        else
	            return ConstantesBD.codigoNuncaValido;
	    }
	    catch(SQLException e)
		{
		    logger.warn(e+" Error en la consulta de la maxCodigoInsertadoStr [SqlBaseGrupoEtareoCrecimientoDesarrolloDao]: "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}
	}
}