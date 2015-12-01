package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ParamArchivoPlanoColsanitasDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ParamArchivoPlanoColsanitas {
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ParamArchivoPlanoColsanitasDao objetoDao;
	
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
			objetoDao=myFactory.getParamArchivoPlanoColsanitasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public ParamArchivoPlanoColsanitas()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	public HashMap consultarArchivoPlanoColsanitas(Connection con, int convenio) {
		return objetoDao.consultarArchivoPlanoColsanitas(con, convenio);
	}

	public boolean eliminarArchivoPlanoColsanitas(Connection con, int convenio) {
		return objetoDao.eliminarArchivoPlanoColsanitas(con, convenio);
	}

	public boolean insertarArchivoPlanoColsanitas(Connection con,
			HashMap<String, Object> vo) {
		return objetoDao.insertarArchivoPlanoColsanitas(con, vo);
	}

	public boolean modificarArchivoPlanoColsanitas(Connection con, HashMap vo) {
		return objetoDao.modificarArchivoPlanoColsanitas(con, vo);
	}

}
