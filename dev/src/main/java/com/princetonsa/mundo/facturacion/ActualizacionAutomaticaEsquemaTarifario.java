package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ActualizacionAutomaticaEsquemaTarifarioDao;

public class ActualizacionAutomaticaEsquemaTarifario 
{

	
	/**
	 * 
	 */
	private ActualizacionAutomaticaEsquemaTarifarioDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public ActualizacionAutomaticaEsquemaTarifario()
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
			objetoDao=myFactory.getActualizacionAutomaticaEsquemaTarifarioDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
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
	public boolean insertarServicio(Connection con, HashMap vo)
	{
		return objetoDao.insertarServicio(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaInventarios(Connection con) 
	{
		return objetoDao.consultaInventarios(con);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaServicios(Connection con) 
	{
		return objetoDao.consultaServicios(con);
	}

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @param contrato
	 * @param empresa
	 * @param tipoConvenio
	 * @param esquemaServicios
	 * @param esquemaInventarios
	 * @return
	 */
	public HashMap<String, Object> consultaConveniosVigentes(Connection con, String convenio, String contrato, String empresa, String tipoConvenio, String esquemaServicios, String esquemaInventarios) 
	{
		return objetoDao.consultaConveniosVigentes(con, convenio, contrato, empresa, tipoConvenio, esquemaServicios, esquemaInventarios);
	}
	
	
}
