package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.ServiciosViaAccesoDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ServiciosViaAcceso {
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ServiciosViaAccesoDao objetoDao;
	
	/**
	 * inicializa el acceso a la base de datos obteniendo el respectivo DAO
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getServiciosViaAccesoDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public ServiciosViaAcceso()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	public HashMap consultarServiciosViaAcceso(Connection con) 
	{
		return objetoDao.consultarServiciosViaAcceso(con);
	}

	public boolean eliminarServiciosViaAcceso(Connection con, int codigo) 
	{
		return objetoDao.eliminarServiciosViaAcceso(con, codigo);
	}

	public boolean insertarServiciosViaAcceso(Connection con,HashMap vo)
	{
		return objetoDao.insertarServiciosViaAcceso(con, vo);
	}

}
