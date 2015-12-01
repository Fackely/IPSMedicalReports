package com.princetonsa.mundo.administracion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.TiposMonedaDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class TiposMoneda {
	
	/**
	 * Objeto para manejar los logs de la clase
	 */
	private Logger logger=Logger.getLogger(TiposMoneda.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private TiposMonedaDao objetoDao;
	
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
			objetoDao=myFactory.getTiposMonedaDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public TiposMoneda()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Insertar los tipos de monedas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarTiposMoneda(Connection con, HashMap vo)
	{
		return objetoDao.insertarTiposMoneda(con, vo);
	}
	
	/**
	 * Consultar los tipos de monedas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposMoneda(Connection con)
	{
		return objetoDao.consultarTiposMoneda(con);
	}
	
	/**
	 * Modificar los tipos de monedas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarTiposMoneda(Connection con, HashMap vo)
	{
		return objetoDao.modificarTiposMoneda(con, vo);
	}
	
	/**
	 * Eliminar los tipos de monedas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarTiposMoneda(Connection con, int codigo)
	{
		return objetoDao.eliminarTiposMoneda(con, codigo);
	}

}
