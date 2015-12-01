package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.AjustesXInventarioFisico;
import com.princetonsa.mundo.inventarios.ComparativoUltimoConteo;

/**
 * Interfaz de AjustesXInventarioFisico
 * @author garias@princetonsa.com
 */
public interface AjustesXInventarioFisicoDao {

	/**
	 * 
	 * @param con
	 * @param axif
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, AjustesXInventarioFisico axif);
	
	/**
	 * 
	 * @param con
	 * @param axif
	 * @return
	 */
	public int registrarAjuste(Connection con, AjustesXInventarioFisico axif);
	
	/**
	 * 
	 * @param con
	 * @param axif
	 * @param paramAlmacenConsig 
	 * @return
	 */
	public HashMap transaccionesValidas(Connection con, AjustesXInventarioFisico axif, String paramAlmacenConsig);
	
	/**
	 * 
	 * @param con
	 * @param codConteo
	 * @return codPreparacion
	 */
	public int consultarCodPrep(Connection con, int codConteo);
	
	/**
	 * 
	 * @param con
	 * @param codPreparacion
	 * @return
	 */
	public boolean finalizarPreparacion (Connection con, int codAlmacen, int codArticulo, int codLote);
	
	/**
	 * 
	 * @param con
	 * @param tipo de ajuste
	 */
	public String consultarDescripcionAjuste(Connection con, int tipAjuste);

	public String consultarParametroAlmacenConsignacion(Connection con, int codigoAlmacen);
	
}	