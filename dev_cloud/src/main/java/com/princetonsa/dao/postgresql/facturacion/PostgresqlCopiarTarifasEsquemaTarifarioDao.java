package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CopiarTarifasEsquemaTarifarioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCopiarTarifasEsquemaTarifarioDao;
import com.servinte.axioma.fwk.exception.IPSException;

public class PostgresqlCopiarTarifasEsquemaTarifarioDao implements
		CopiarTarifasEsquemaTarifarioDao {

	/**
	 * 
	 */
	public HashMap<String, Object> obtenerTarifario(Connection con, String tarifarioOrigen, String institucion) 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.obtenerTarifario(con,tarifarioOrigen,institucion);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarTarifas(Connection con, String tarifarioOrigen, String esinventario, String tarifarioOficial) 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.consultarTarifas(con, tarifarioOrigen, esinventario, tarifarioOficial);
	}
	
	/**
	 * 
	 */
	public boolean insertarInventarioPorcentaje(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarInventarioPorcentaje(con, vo, porcentaje, chequeo);
	}
	
	/**
	 * 
	 */
	public boolean insertarInventario(Connection con, HashMap vo) 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarInventario(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean insertarTarifasIss(Connection con, HashMap vo) 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarTarifasIss(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean insertarTarifasSoat(Connection con, HashMap vo) 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarTarifasSoat(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean insertarTarifasIssValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarTarifasIssValor(con, vo, porcentaje, chequeo);
	}
	
	/**
	 * 
	 */
	public boolean insertarTarifasSoatValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarTarifasSoatValor(con, vo, porcentaje, chequeo);
	}
	
	/**
	 * 
	 */
	public boolean insertarTarifasIssUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException 
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarTarifasIssUnidades(con, vo, cantidadEsquema, unidades);
	}
	
	/**
	 * 
	 */
	public boolean insertarTarifasSoatUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.insertarTarifasSoatUnidades(con, vo, cantidadEsquema, unidades);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public boolean existeTarifaInventarios(Connection con, int codigoEsquematarifario, int codigoArticulo)
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.existeTarifaInventarios(con, codigoEsquematarifario, codigoArticulo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public boolean existeTarifaServicios(Connection con, int codigoEsquematarifario, int codigoServicio, int codigoTarifario)
	{
		return SqlBaseCopiarTarifasEsquemaTarifarioDao.existeTarifaServicios(con, codigoEsquematarifario, codigoServicio, codigoTarifario);
	}
	
}
