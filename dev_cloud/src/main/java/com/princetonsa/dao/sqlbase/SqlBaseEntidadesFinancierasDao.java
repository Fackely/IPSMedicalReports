/*
 * Created on 20/09/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
/**
 * @author artotor
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseEntidadesFinancierasDao 
{


	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseEntidadesFinancierasDao.class);
			
	/**
	 * Cadena para consultar las formas de pago que tiene definida una institucion.
	 */
	private static final String consultaEntidadesFinancieras="SELECT ef.consecutivo as consecutivo,ef.codigo as codigo,ef.institucion as institucion,ef.tercero as codigotercero,t.numero_identificacion as identificaciontercero,t.descripcion as descripciontercero,t.numero_identificacion ||'   '||t.descripcion as tercero,ef.tipo as codigotipo,tef.descripcion as descripciontipo,ef.activo as activo from entidades_financieras ef inner join tipos_ent_financieras tef on(ef.tipo=tef.codigo) inner join terceros t on(ef.tercero=t.codigo and ef.institucion=t.institucion) where ef.institucion=? order by ef.codigo";
	
	/**
	 * cadena para cargar una entidad financiera determinada.
	 */
	private static final String consultarEntidadFinanciera="SELECT ef.consecutivo as consecutivo,ef.codigo as codigo,ef.institucion as institucion,ef.tercero as codigotercero,t.numero_identificacion as identificaciontercero,t.descripcion as descripciontercero,t.numero_identificacion ||'   '||t.descripcion as tercero,ef.tipo as codigotipo,tef.descripcion as descripciontipo,ef.activo as activo from entidades_financieras ef inner join tipos_ent_financieras tef on(ef.tipo=tef.codigo) inner join terceros t on(ef.tercero=t.codigo and ef.institucion=t.institucion) where ef.consecutivo=?";
	
	/**
	 * cadena para eliminar un registro de la tabla formas_pago
	 */
	private static final String eliminarRegistro="DELETE FROM entidades_financieras where consecutivo=?";
	
	/**
	 * Cadena para consultar si un registro fue modificado o no.
	 */
	private static final String cadenaExisteModificacion="select consecutivo from entidades_financieras where consecutivo=? and codigo=? and tercero=? and tipo=? and activo=?";
	

	/**
	 * Cadena para modificar los registros de la tabla caja
	 */
	private static final String modificarRegistro="UPDATE entidades_financieras SET codigo = ?,tercero=?,tipo=?,activo=? where consecutivo=?";
	
	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarEntidadesFinancieras(Connection con, int institucion) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultaEntidadesFinancieras,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta : SqlBaseEntidadesFinancierasDao "+e.toString() );
		   return null;
	    }
	}


	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int consecutivo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(eliminarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivo);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error ELIMINANDO EL REGISTRO: SqlBaseEntidadesFinancierasDao "+e.toString() );
	    }
		return false;
	}


	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public static boolean existeModificacion(Connection con, int consecutivo, String codigo, int codigoTercero, int tipo, boolean activo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaExisteModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivo);
	        ps.setString(2,codigo);
	        ps.setInt(3,codigoTercero);
	        ps.setInt(4,tipo);
	        ps.setBoolean(5,activo);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseEntidadesFinancierasDao] "+e.toString() );
	        return false;
	    }
	}


	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, int consecutivo, String codigo, int codigoTercero, int tipo, boolean activo) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(modificarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,codigo);
	        ps.setInt(2,codigoTercero);
	        ps.setInt(3,tipo);
	        ps.setBoolean(4,activo);
	        ps.setInt(5,consecutivo);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseEntidadesFinancierasDao "+e.toString() );
		   return false;
	    }
	}


	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param codigoTercero
	 * @param tipo
	 * @param activo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, String codigo, int codigoInstitucionInt, int codigoTercero, int tipo, boolean activo, String cadena) 
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con, cadena);
	        ps.setString(1,codigo);
	        ps.setInt(2,codigoInstitucionInt);
	        ps.setInt(3,codigoTercero);
	        ps.setInt(4,tipo);
	        ps.setBoolean(5,activo);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en la insercion Entidades Financieras: SqlBaseEntidadesFinancierasoDao ",e);
		   return false;
	    }	}


	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static ResultSetDecorator cargarEntidadFinanciera(Connection con, int consecutivo)
	{
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultarEntidadFinanciera,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivo);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error Consultando: SqlBaseEntidadesFinancierasDao "+e.toString() );
	    }
		return null;
	}

}
