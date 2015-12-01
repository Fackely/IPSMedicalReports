/*
 * Created on 12/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Types;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseCajasDao
{

    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCajasDao.class);
	
	/**
	 * Cadena para consultar las cajas de la BD
	 */
	private static final String consultarCajas="SELECT c.consecutivo as consecutivo,c.codigo as codigo,c.institucion as institucion,c.descripcion as descripcion,c.tipo as tipo,tc.descripcion as descripciontipo,c.activo as activo, c.centro_atencion AS centroatencion, c.valor_base as valor_base from cajas c inner join tipos_caja tc on(c.tipo=tc.codigo) where institucion=? AND centro_atencion=? order by c.codigo";
	
	/**
	 * Cadena para consultar una caja especifica
	 */
	private static final String consultarCaja="SELECT c.consecutivo as consecutivo,c.codigo as codigo,c.institucion as institucion,c.descripcion as descripcion,c.tipo as tipo,tc.descripcion as descripciontipo,c.activo as activo, c.valor_base as valor_base from cajas c inner join tipos_caja tc on(c.tipo=tc.codigo) where consecutivo=?";
	/**
	 * Cadena para eliminar un registro.
	 */
	private static final String eliminarRegistro="DELETE from cajas where consecutivo = ?";
	
	/**
	 * Cadena para modificar los registros de la tabla caja
	 */
	private static final String modificarRegistro="UPDATE cajas SET codigo = ?,descripcion=?,tipo=?,activo=?, valor_base=? where consecutivo=?";

	/**
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static ResultSetDecorator cargarInformacion(Connection con,int institucion, int centroAtencion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultarCajas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ps.setInt(2,centroAtencion);
	        
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseCajasDao "+e.toString() );
		   return null;
	    }
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int consecutivoCaja) 
	{
		try
	    {
			PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivoCaja);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error SqlBaseCajasDao "+e.toString() );
		   return false;
	    }
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param valorBase
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(modificarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigo);
	        ps.setString(2,descripcion);
	        ps.setInt(3,tipo);
	        ps.setBoolean(4,activo);
	        if(valorBase!=null)
	        {
	        	ps.setDouble(5, valorBase);
	        }
	        else
	        {
	        	ps.setNull(5, Types.DOUBLE);
	        }
	        ps.setInt(6,consecutivo);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseCajasDao "+e.toString() );
		   return false;
	    }
	}

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param centroAtencion
	 * @param cadena
	 * @param valorBase
	 * @param consecutivo
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, int codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo,int centroAtencion, String cadena, Double valorBase) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigo);
	        ps.setInt(2,codigoInstitucionInt);
	        ps.setString(3,descripcion);
	        ps.setInt(4,tipo);
	        ps.setBoolean(5,activo);
	        ps.setInt(6,centroAtencion);
	        if(valorBase!=null)
	        {
	        	ps.setDouble(7, valorBase);
	        }
	        else
	        {
	        	ps.setNull(7, Types.DOUBLE);
	        }
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la insercion cajas: SqlBaseCajasDao ",e);
		   return false;
	    }
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param valorBase
	 * @return
	 */
	public static boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase) 
	{
		try
	    {
			String sentencia="select consecutivo from cajas where consecutivo=? and codigo=? and descripcion=? and tipo=? and activo=?";
			if(valorBase==null)
			{
				sentencia+=" AND valor_base IS NULL";
			}
			else
			{
				sentencia+=" AND valor_base = ?";
			}
			PreparedStatementDecorator ps = null;
	        ps=new PreparedStatementDecorator(con, sentencia);
	        ps.setInt(1,consecutivo);
	        ps.setInt(2,codigo);
	        ps.setString(3,descripcion);
	        ps.setInt(4,tipo);
	        ps.setBoolean(5,activo);
	        if(valorBase!=null)
	        {
	        	ps.setDouble(6,valorBase);
	        }
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseCajasDao] "+e.toString() );
	        return false;
	    }
		
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static ResultSetDecorator cargarCaja(Connection con, int consecutivo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consultarCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivo);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseCajasDao "+e.toString() );
		   return null;
	    }
	}

}
