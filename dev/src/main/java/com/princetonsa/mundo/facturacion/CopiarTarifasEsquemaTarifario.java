package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CopiarTarifasEsquemaTarifarioDao;
import com.servinte.axioma.fwk.exception.IPSException;

public class CopiarTarifasEsquemaTarifario 
{

	
	private CopiarTarifasEsquemaTarifarioDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public CopiarTarifasEsquemaTarifario()
	{
		init(System.getProperty("TIPOBD"));
	}


	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getCopiarTarifasEsquemaTarifarioDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}


	/**
	 * 
	 * @param con
	 * @param tarifarioOrigen
	 * @param institucion 
	 * @return
	 */
	public HashMap<String, Object> obtenerTarifario(Connection con, String tarifarioOrigen, String institucion) 
	{
		return objetoDao.obtenerTarifario(con,tarifarioOrigen,institucion);
	}

	
	/**
	 * 
	 * @param con
	 * @param tarifarioOrigen
	 * @param institucion 
	 * @param tarifarioDestino 
	 * @return
	 */
	public HashMap<String, Object> consultarTarifas(Connection con, String tarifarioOrigen, String esinventario, String tarifarioOficial) 
	{
		return objetoDao.consultarTarifas(con, tarifarioOrigen, esinventario, tarifarioOficial);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 * @throws IPSException 
	 */
	public boolean insertarInventarioPorcentaje(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException 
	{
		return objetoDao.insertarInventarioPorcentaje(con, vo, porcentaje, chequeo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarInventario(Connection con, HashMap vo) 
	{
		return objetoDao.insertarInventario(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarTarifasIss(Connection con, HashMap vo) 
	{
		return objetoDao.insertarTarifasIss(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarTarifasSoat(Connection con, HashMap vo) 
	{
		return objetoDao.insertarTarifasSoat(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 * @throws IPSException 
	 */
	public boolean insertarTarifasIssValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException 
	{
		return objetoDao.insertarTarifasIssValor(con, vo, porcentaje, chequeo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 * @throws IPSException 
	 */
	public boolean insertarTarifasSoatValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException 
	{
		return objetoDao.insertarTarifasSoatValor(con, vo, porcentaje, chequeo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cantidadEsquema
	 * @param unidades
	 * @return
	 * @throws IPSException 
	 */
	public boolean insertarTarifasIssUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException 
	{
		return objetoDao.insertarTarifasIssUnidades(con, vo, cantidadEsquema, unidades);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cantidadEsquema
	 * @param unidades
	 * @return
	 * @throws IPSException 
	 */
	public boolean insertarTarifasSoatUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException 
	{
		return objetoDao.insertarTarifasSoatUnidades(con, vo, cantidadEsquema, unidades);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean existeTarifaInventarios(Connection con, int codigoEsquematarifario, int codigoArticulo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCopiarTarifasEsquemaTarifarioDao().existeTarifaInventarios(con, codigoEsquematarifario, codigoArticulo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean existeTarifaServicios(Connection con, int codigoEsquematarifario, int codigoServicio, int codigoTarifario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCopiarTarifasEsquemaTarifarioDao().existeTarifaServicios(con, codigoEsquematarifario, codigoServicio, codigoTarifario);
	}
	
}
