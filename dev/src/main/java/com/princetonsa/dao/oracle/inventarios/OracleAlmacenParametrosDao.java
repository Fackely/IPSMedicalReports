package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.AlmacenParametrosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseAlmacenParametrosDao;
import com.princetonsa.mundo.inventarios.AlmacenParametros;

public class OracleAlmacenParametrosDao implements AlmacenParametrosDao {
	
	/**
	 * Consulta registros en base al codigo del centro de costos (subalmacen)
	 * @param Connection con
	 * @param int codigo del centro de costos (subalmacen)
	 * @parm int centro atencion
	 * @param int institucion (*)
	 * @return HashMap
	 * */
	public HashMap consultarAlmacenParametros(Connection con, int codigo, int centroAtencion, int institucion)
	{
		return SqlBaseAlmacenParametrosDao.consultarAlmacenParametros(con, codigo, centroAtencion, institucion);		
	}
	
	/**
	 * Consulta centro de Costos
	 * @param Connection con
	 * @param int institucion
	 * @return HashMap
	 * */
	public HashMap consultarCentroCostos(Connection con, int institucion, int centroAtencion)
	{
		return SqlBaseAlmacenParametrosDao.consultarCentroCostos(con, institucion, centroAtencion);
	}
	
		
	/**
	 * Inserta un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public boolean insertarAlmacenParametros(Connection con, AlmacenParametros almacenParametros)
	{
		return SqlBaseAlmacenParametrosDao.insertarAlmacenParametros(con, almacenParametros);
	}
	
	/**
	 * Elimina un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public boolean eliminarAlmacenParametros(Connection con, int codigo, int institucion )
	{
		return SqlBaseAlmacenParametrosDao.eliminarAlmacenParametros(con, codigo, institucion);
	}
	
	/**
	 * Modifica un registro de la base de datos
	 * @param Connection con
	 * @param AlmacenParametros almacenParametros 
	 * */
	public boolean modificarAlmacenParametros(Connection con, AlmacenParametros almacenParametros)
	{
		return SqlBaseAlmacenParametrosDao.modificarAlmacenParametros(con, almacenParametros);
	}
	
	/**
	 * Inicializa la tabla almacen parametros si esta se encuentra vacia
	 * @param Connection con
	 * @param int institucion
	 * @param int opc 1 iniciar tabla, 2 completar tabla
	 * */
	public void iniciarTableAlmacenParametros(Connection con, int institucion, int opc)
	{
		SqlBaseAlmacenParametrosDao.iniciarTableAlmacenParametros(con, institucion, opc);				
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean manejaExistenciasNegativas(Connection con, int codigoAlmacen, int codigoInstitucion)
	{
		return SqlBaseAlmacenParametrosDao.manejaExistenciasNegativas(con, codigoAlmacen, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */	
	public boolean manejaExistenciasNegativasCentroAten(Connection con, int centroCosto, int codigoInstitucion)
	{
		return SqlBaseAlmacenParametrosDao.manejaExistenciasNegativasCentroAten(con, centroCosto, codigoInstitucion);
	}

	

	/**
	 * @see com.princetonsa.dao.inventarios.AlmacenParametrosDao#afectaCostoPromedio(java.sql.Connection, int, int)
	 */
	@Override
	public boolean afectaCostoPromedio(Connection con, int codigoAlmacen, int codigoInstitucion) {

		return SqlBaseAlmacenParametrosDao.afectaCostoPromedio(con, codigoAlmacen, codigoInstitucion);
	}
}