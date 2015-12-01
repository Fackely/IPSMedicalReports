/*
 * Created on 19/09/2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

/**
 * @author artotor
 *
 */
public class SqlBaseFormasPagoDao 
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseFormasPagoDao.class);
			
	/**
	 * cadena para eliminar un registro de la tabla formas_pago
	 */
	private static final String eliminarRegistro="DELETE FROM formas_pago where consecutivo=?";
	
	/**
	 * Metodo que realiza la consulta de la BD dada la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarFormasPago(Connection con, int institucion) 
	{
		String consultaFormasPago="SELECT fp.consecutivo as consecutivo,fp.codigo as codigo, fp.descripcion as descripcion,fp.tipo_detalle as tipodetalle,tdfp.descripcion as nomtipodetalle,fp.activo as activo, CASE WHEN fp.cuenta_contable IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE fp.cuenta_contable END AS cuentacontable, fp.ind_consignacion as indicativoconsignacion, traslado_caja_recaudo AS traslado_caja_recaudo, req_traslado_caja_recaudo AS req_traslado_caja_recaudo from formas_pago fp inner join tipos_detalle_forma_pago tdfp on(fp.tipo_detalle=tdfp.codigo) where institucion =? order by codigo";
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultaFormasPago,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta Formas Pagos: SqlBaseFormasPagoDao "+e.toString() );
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
	        logger.warn(e+"Error ELIMINANDO EL REGISTRO: SqlBaseFormasPagoDao "+e.toString() );
	    }
		return false;
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param trasladoCajaRecaudo
	 * @return
	 */
	public static boolean existeModificacion(Connection con, int consecutivo, String codigo, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo) 
	{
		String cadenaExisteModificacion="select consecutivo from formas_pago where consecutivo=? and codigo=? and descripcion=? and tipo_detalle=? and activo=? and ind_consignacion=? AND traslado_caja_recaudo=? AND req_traslado_caja_recaudo=?  ";
		
		if(cuentaContable>0)
		{
			cadenaExisteModificacion+=" and cuenta_contable="+cuentaContable;
		}
		else
		{
			cadenaExisteModificacion+=" and cuenta_contable  is null ";
		}
		
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaExisteModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivo);
	        ps.setString(2,codigo);
	        ps.setString(3,descripcion);
	        ps.setInt(4,tipo);
	        ps.setBoolean(5,activo);
	        ps.setString(6, indicativoConsignacion);
	        ps.setString(7, trasladoCajaRecaudo);
	        ps.setString(8, reqTrasladoCajaRecaudo);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        return (!rs.next());
	    }
	    catch(SQLException e)
	    {
	    	logger.warn(e+"Error revisando la existencia de modificaciones [SqlBaseFormaPagosDaoDao] "+e.toString() );
	        return false;
	    }
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static ResultSetDecorator cargarFormaPago(Connection con, int consecutivo) 
	{
		String consultaFormaPago="SELECT fp.consecutivo as consecutivo,fp.institucion,fp.codigo as codigo, fp.descripcion as descripcion,fp.tipo_detalle as tipodetalle,tdfp.descripcion as nomtipodetalle,fp.activo as activo, CASE WHEN fp.cuenta_contable IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE fp.cuenta_contable END AS cuentacontable , fp.ind_consignacion as indicativoconsignacion, traslado_caja_recaudo AS traslado_caja_recaudo from formas_pago fp inner join tipos_detalle_forma_pago tdfp on(fp.tipo_detalle=tdfp.codigo) where fp.consecutivo=?";
		
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=new PreparedStatementDecorator(con, consultaFormaPago);
	        ps.setInt(1,consecutivo);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta cajas: SqlBaseFormaPagosDao "+e.toString() );
		   return null;
	    }
	}

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param trasladoCajaRecaudo
	 * @return
	 */
	public static boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo) 
	{
		String modificarRegistro="UPDATE formas_pago SET codigo = ?,descripcion=?,tipo_detalle=?,activo=?, cuenta_contable=?, ind_consignacion=?, traslado_caja_recaudo=?, req_traslado_caja_recaudo=? where consecutivo=?";

		try
		{
			PreparedStatementDecorator ps = null;
			ps =  new PreparedStatementDecorator(con.prepareStatement(modificarRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setString(2,descripcion);
			ps.setInt(3,tipo);
			ps.setBoolean(4,activo);
			
			if(cuentaContable>0)
				ps.setInt(5, cuentaContable);
			else
				ps.setObject(5, null);
			
			logger.info("indicativoConsignacion-->"+indicativoConsignacion);
			
			if(UtilidadTexto.isEmpty(indicativoConsignacion))
				ps.setString(6, ConstantesBD.acronimoSi);
			else
				ps.setString(6, indicativoConsignacion);
			
			if(UtilidadTexto.isEmpty(trasladoCajaRecaudo))
			{
				ps.setString(7, ConstantesBD.acronimoSi);
			}
			else
			{
				ps.setString(7, trasladoCajaRecaudo);
			}
			
			if(UtilidadTexto.isEmpty(reqTrasladoCajaRecaudo))
			{
				ps.setString(8, ConstantesBD.acronimoNo);
			}
			else
			{
				ps.setString(8, reqTrasladoCajaRecaudo);
			}
			
			ps.setInt(9,consecutivo);
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la consulta cajas: SqlBaseFormaPagosDao "+e.toString() );
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
	 * @param trasladoCajaRecaudo
	 * @param cadena
	 * @return
	 */
	public static boolean insertarRegistro(Connection con, String codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo) 
	{
		int codigoPk=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_formas_pago");
		String cadena="insert into formas_pago(consecutivo,institucion,codigo,descripcion,tipo_detalle, activo, cuenta_contable, ind_consignacion, traslado_caja_recaudo, req_traslado_caja_recaudo) values(?,?,?,?,?,?,?,?,?,?)";
		try
	    {
	        PreparedStatementDecorator ps = null;
	        ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigoPk);
	        ps.setInt(2,codigoInstitucionInt);
	        ps.setString(3,codigoPk+"");
	        ps.setString(4,descripcion);
	        ps.setInt(5,tipo);
	        ps.setBoolean(6,activo);
	        if(cuentaContable>0)
	        	ps.setInt(7, cuentaContable);
	        else
	        	ps.setObject(7, null);
	        ps.setString(8, indicativoConsignacion);
	        ps.setString(9, trasladoCajaRecaudo);
	        ps.setString(10, reqTrasladoCajaRecaudo);
	        ps.executeUpdate();
	        return true;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la insercion cajas: SqlBaseFormasPagoDao "+e.toString() );
		   return false;
	    }
	}

}
