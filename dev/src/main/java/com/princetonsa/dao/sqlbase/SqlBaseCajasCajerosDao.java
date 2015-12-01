/*
 * Created on 15/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
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
public class SqlBaseCajasCajerosDao 
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCajasCajerosDao.class);
	
	/**
	 * Cadena para consultar las cajas de la BD
	 */
	private static final String consultarCajasCajeros="SELECT cc.caja as consecutivocaja,c.codigo as codigocaja,c.descripcion as descripcioncaja,cc.usuario as loginusuario, getNombreUsuario(cc.usuario) as nombreusuario,cc.activo as activo,cc.institucion as institucion from cajas_cajeros cc inner join cajas c on (c.consecutivo=cc.caja) where cc.institucion=? AND c.centro_atencion=? order by c.descripcion,getNombreUsuario(cc.usuario)";
	
	/**
	 * Cadena para eliminar un registro.
	 */
	private static final String eliminarRegistro="DELETE from cajas_cajeros where caja = ? and usuario=?";
	
	
	/**
	 * Cadena para modificar los registros de la tabla caja
	 */
	private static final String modificarRegistro="UPDATE cajas_cajeros SET activo = ? where caja=? and usuario=?";
	
	private static final String insertarRegistro="INSERT INTO cajas_cajeros (caja,usuario,activo,institucion) values (?,?,?,?)";
	
	/**
	 * Cadena para verificar si hay modificacion en un registro
	 */
	private static final String cadenaExisteModificacion="select caja from cajas_cajeros where caja=? and usuario=? and activo=?";
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param centroAtencion
	 * @return
	 */
	public static ResultSetDecorator cargarInformacion(Connection con, int codigoInstitucionInt, int centroAtencion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        StringBuffer cadena=new StringBuffer();
	        
	        cadena.append("SELECT cc.caja as consecutivocaja,c.codigo as codigocaja,c.descripcion as descripcioncaja,cc.usuario as loginusuario, getNombreUsuario(cc.usuario) as nombreusuario,cc.activo as activo,cc.institucion as institucion from cajas_cajeros cc inner join cajas c on (c.consecutivo=cc.caja) where cc.institucion=?");
	        
	        //-------Si es diferente de seleccione --------------------//
	        if(centroAtencion!=ConstantesBD.codigoNuncaValido)
	        {
	        	cadena.append(" AND c.centro_atencion=?");
	        }
	        
	        cadena.append(" order by c.descripcion,getNombreUsuario(cc.usuario)");
	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(cadena.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        ps.setInt(1,codigoInstitucionInt);
	        
	        //-------Si es diferente de seleccione --------------------//
	        if(centroAtencion!=ConstantesBD.codigoNuncaValido)
	        {
	        	ps.setInt(2,centroAtencion);
	        }
	        
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseCajasCajerosDao "+e.toString() );
		   return null;
	    }
	}


	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int consecutivoCaja, String loginUsuario) 
	{
		try
	    {
			PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(eliminarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivoCaja);
	        ps.setString(2,loginUsuario);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error SqlBaseCajasCajerosDao "+e.toString() );
		   return false;
	    }
	}


	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @param activo
	 * @return
	 */
	public static boolean existeModificacion(Connection con, int consecutivoCaja, String loginUsuario, boolean activo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaExisteModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivoCaja);
	        ps.setString(2,loginUsuario);
	        ps.setBoolean(3,activo);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseCajasCajerosDao] "+e.toString() );
		   return false;
	    }
	}


	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @param tempActivo
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, int consecutivoCaja, String loginUsuario, boolean tempActivo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(modificarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setBoolean(1,tempActivo);
	        ps.setInt(2,consecutivoCaja);
	        ps.setString(3,loginUsuario);
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
	 * @param caja
	 * @param loginUsuario
	 * @param activo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, int caja, String loginUsuario, boolean activo, int codigoInstitucionInt) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(insertarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,caja);
	        ps.setString(2,loginUsuario);
	        ps.setBoolean(3,activo);
	        ps.setInt(4,codigoInstitucionInt);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la insercion cajas: SqlBaseCajasDao "+e.toString() );
		   return false;
	    }
	}
}
