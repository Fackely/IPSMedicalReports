package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * 
 * @author armando
 *
 */
public class SqlBaseTiposProgamaDao 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTiposProgamaDao.class);
			
	/**
	 * 
	 */
	private static String cadenaConsultaTiposPrograma="select codigo as codigo,institucion as institucion,descripcion as descripcion,activo as activo,'BD' as tiporegistro from tipos_programa_pyp where institucion=?";
	
	/**
	 * 
	 */
	private static String cadenaInsertaTipoPrograma="INSERT INTO tipos_programa_pyp (codigo,institucion,descripcion,activo) VALUES (?,?,?,?)";
	
	/**
	 * 
	 */
	private static String cadenaUpdateTipoPrograma="UPDATE tipos_programa_pyp SET descripcion=?,activo=? WHERE codigo=? and institucion=?";
	
	/**
	 * 
	 */
	private static String cadenaDeleteTipoPrograma="DELETE FROM tipos_programa_pyp WHERE codigo=? and institucion=?";
	
	
	/**
	 * Cadena para consultar si un registro fue modificado o no.
	 */
	private static final String cadenaExisteModificacion="select codigo from tipos_programa_pyp where codigo=? and institucion=? and descripcion=? and activo=?";
	
	/**
	 * 
	 */
	private static final String consultarTipoPrograma="select codigo as codigo,institucion as institucion,descripcion as descripcion,activo as activo from tipos_programa_pyp where codigo=? and institucion=?";
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	public static HashMap cargarInfomacionBD(Connection con, int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaTiposPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[cargarInfomacionBD] -->");
			e.printStackTrace();
		}
		//ps.set
		return (HashMap)mapa.clone();
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertaTipoPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setInt(2,institucion);
			ps.setString(3,descripcion);
			ps.setBoolean(4,activo);
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[insertarRegistro] -->");
			e.printStackTrace();
		}
		return false;
		
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateTipoPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,descripcion);
			ps.setBoolean(2,activo);
			ps.setDouble(3,Utilidades.convertirADouble(codigo));
			ps.setInt(4,institucion);
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[modificarRegistro] -->");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public static boolean existeModificacion(Connection con, String codigo, int institucion, String descripcion, boolean activo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaExisteModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setDouble(1,Utilidades.convertirADouble(codigo));
	        ps.setInt(2,institucion);
	        ps.setString(3,descripcion);
	        ps.setBoolean(4,activo);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.error("ERROR EN SqlBaseTiposProgamaDao[existeModificacion] -->");
	        return false;
	    }
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarTipoPrograma(Connection con, String codigo, int institucion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consultarTipoPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setDouble(1,Utilidades.convertirADouble(codigo));
	        ps.setInt(2,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	    	logger.error("ERROR EN SqlBaseTiposProgamaDao[cargarTipoPrograma] -->");
		   return null;
	    }
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, String codigo, int institucion) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDeleteTipoPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setInt(2,institucion);
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseTiposProgamaDao[eliminarRegistro] -->");
			e.printStackTrace();
		}
		return false;

	}

}
