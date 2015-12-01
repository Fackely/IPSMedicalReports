/*
 * Created on 19/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.FormasPagoDao;
import com.princetonsa.dao.sqlbase.SqlBaseFormasPagoDao;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OracleFormasPagoDao implements FormasPagoDao 
{
	/**
	 * Metodo que carga todas las formas de pago que tiene una institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarFormasPago(Connection con, int institucion)
	{
		return SqlBaseFormasPagoDao.cargarFormasPago(con,institucion);
	}
	
	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivo)
	{
		return SqlBaseFormasPagoDao.eliminarRegistro(con,consecutivo);
	}
	

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, String codigo, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo)
	{
		return SqlBaseFormasPagoDao.existeModificacion(con,consecutivo,codigo,descripcion,tipo,activo, cuentaContable, indicativoConsignacion, trasladoCajaRecaudo, reqTrasladoCajaRecaudo);
	}
	
	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator cargarFormaPago(Connection con, int consecutivo)
	{
		return SqlBaseFormasPagoDao.cargarFormaPago(con,consecutivo);
	}
	

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo)
	{
		return SqlBaseFormasPagoDao.modificarRegistro(con,consecutivo,codigo,descripcion,tipo,activo, cuentaContable, indicativoConsignacion, trasladoCajaRecaudo, reqTrasladoCajaRecaudo);
	}
	

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo, int cuentaContable, String indicativoConsignacion, String trasladoCajaRecaudo, String reqTrasladoCajaRecaudo)
	{
		return SqlBaseFormasPagoDao.insertarRegistro(con,codigo,codigoInstitucionInt,descripcion,tipo,activo,cuentaContable, indicativoConsignacion, trasladoCajaRecaudo, reqTrasladoCajaRecaudo);
	}
}
