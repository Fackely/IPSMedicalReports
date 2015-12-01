package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.EquivalentesDeInventarioDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseEquivalentesDeInventarioDao;

public class OracleEquivalentesDeInventarioDao implements
		EquivalentesDeInventarioDao {

	
	/**
	 * Metodo de consulta de articulos equivalentes
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaEquivalentes (Connection con, int codigoArtPpal)
	{
		return SqlBaseEquivalentesDeInventarioDao.consultaEquivalentes(con, codigoArtPpal);
	}
	
	/**
	 * Metodo de insercion de un nuevo articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @param cantidadArtEquivalente
	 * @param usuarioModifica
	 * @return
	 */
	public boolean insertarArticuloEquivalente (Connection con, int codigoArtPpal, int codigoArtEquivalente, int cantidadArtEquivalente, String usuarioModifica)
	{
		return SqlBaseEquivalentesDeInventarioDao.insertarArticuloEquivalente(con, codigoArtPpal, codigoArtEquivalente, cantidadArtEquivalente, usuarioModifica);
	}
	
	/**
	 * Metodo que elimina un registro de articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @return
	 */
	public boolean eliminarArticuloEquivalente (Connection con,int codigoArtPpal, int codigoArtEquivalente)
	{
		return SqlBaseEquivalentesDeInventarioDao.eliminarArticuloEquivalente(con, codigoArtPpal, codigoArtEquivalente);
	}
	
	/**
	 * Metodo para modificar un registro de articulo equivalente
	 * @param con
	 * @param codigoArtPpal
	 * @param codigoArtEquivalente
	 * @param cantidadArtEquivalente
	 * @param usuarioModifica
	 * @return
	 */
	
	public boolean modificarArticuloEquivalente (Connection con,int codigoArtPpal, int codigoArtEquivalente, int cantidadArtEquivalente, String usuarioModifica)
	{
		return SqlBaseEquivalentesDeInventarioDao.modificarArticuloEquivalente(con, codigoArtPpal, codigoArtEquivalente, cantidadArtEquivalente, usuarioModifica);
		
	}
	
	/**
	 * Metodo de Consulta los Campos de Articulo Principal para filtrar la Busqueda de Equivalentes
	 * @param con
	 * @param codgigoArtPpal
	 */
	public HashMap<String, Object> consultaCamposBusqueda (Connection con, int codArtPrincipal)
	{
		return SqlBaseEquivalentesDeInventarioDao.consultaCamposBusquedaEquivalente(con, codArtPrincipal);
	}
	
	/**
	 * Metodo de Cosnutla de Datos Adicionales Articulo Equivalente
	 * @param con
	 * @param codArtPrincipal
	 * @return
	 */
	public HashMap<String, Object> consultaDatosAd (Connection con, int codigo)
	{
		return SqlBaseEquivalentesDeInventarioDao.consultaDatosAd(con, codigo);
	}

	/**
	 * Metodo para consultar articulos equivalentes
	 * @param con
	 * @param codigoPpal
	 * @param codigoEquivalente
	 * @return
	 */
	public HashMap consultarArticulo(Connection con, int codigoPpal,int codigoEquivalente)
	{
		return SqlBaseEquivalentesDeInventarioDao.consultarArticulo(con, codigoPpal, codigoEquivalente);
	}
}
