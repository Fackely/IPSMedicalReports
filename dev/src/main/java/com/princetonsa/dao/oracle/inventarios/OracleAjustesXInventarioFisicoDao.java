package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.AjustesXInventarioFisicoDao;
import com.princetonsa.dao.inventarios.ComparativoUltimoConteoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseAjustesXInventarioFisicoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseComparativoUltimoConteoDao;
import com.princetonsa.mundo.inventarios.AjustesXInventarioFisico;
import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

/**
 * @author garias@princetonsa.com
 */
public class OracleAjustesXInventarioFisicoDao implements AjustesXInventarioFisicoDao {
	
	/**
	 * 
	 * @param con
	 * @param axif
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, AjustesXInventarioFisico axif)
	{
		return SqlBaseAjustesXInventarioFisicoDao.filtrarArticulos(con, axif);
	}
	
	/**
	 * 
	 * @param con
	 * @param axif
	 * @return
	 */
	public int registrarAjuste(Connection con, AjustesXInventarioFisico axif)
	{
		return SqlBaseAjustesXInventarioFisicoDao.registrarAjuste(con, axif);
	}
	
	/**
	 * 
	 * @param con
	 * @param axif
	 * @return
	 */
	public HashMap transaccionesValidas(Connection con, AjustesXInventarioFisico axif, String paramAlmacenConsig)
	{
		return SqlBaseAjustesXInventarioFisicoDao.transaccionesValidas(con, axif, paramAlmacenConsig);
	}
	
	/**
	 * 
	 * @param con
	 * @param CodConteo
	 * @return CodPreparacion
	 */
	public int consultarCodPrep(Connection con,int codConteo)
	{
		return SqlBaseAjustesXInventarioFisicoDao.consultarCodPrep(con, codConteo);
	}
	
	/**
	 * 
	 * @param con
	 * @param CodAlmacen
	 * @param CodArticulo
	 * @param CodLote
	 * @return CodPreparacion
	 */
	public boolean finalizarPreparacion(Connection con, int codAlmacen, int codArticulo, int codLote)
	{
		return SqlBaseAjustesXInventarioFisicoDao.finalizarPreparacion(con, codAlmacen, codArticulo, codLote);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipo de ajuste
	 */
	public String consultarDescripcionAjuste(Connection con, int tipAjuste)
	{
		return SqlBaseAjustesXInventarioFisicoDao.consultarDescripcionAjuste(con, tipAjuste);
	}
	
	/**
	 * 
	 */
	public String consultarParametroAlmacenConsignacion(Connection con, int codigoAlmacen) 
	{
		return SqlBaseAjustesXInventarioFisicoDao.consultarParametroAlmacenConsignacion(con, codigoAlmacen);
	}
	
} 