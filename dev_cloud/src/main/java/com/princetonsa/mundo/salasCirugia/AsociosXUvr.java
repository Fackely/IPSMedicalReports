package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.AsociosXUvrDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class AsociosXUvr {
	
	/**
	 * Objeto para los logs
	 */
	private Logger logger=Logger.getLogger(AsociosXUvr.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private AsociosXUvrDao objetoDao;
	
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
			objetoDao=myFactory.getAsociosXUvrDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * Constructor de la clase
	 */
	public AsociosXUvr()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	public boolean actualizarDetCodigosAsociosXUvr(Connection con,HashMap<String, Object> vo) 
	{
		return objetoDao.actualizarDetCodigosAsociosXUvr(con, vo);
	}

	public HashMap consultarAsociosXUvrTipoSala(Connection con,	int esquemaTarifario, int convenio, int codigoAsocioXUvr) 
	{
		return objetoDao.consultarAsociosXUvrTipoSala(con, esquemaTarifario, convenio, codigoAsocioXUvr);
	}

	public HashMap consultarDetalleAsociosXUvr(Connection con,int codigoAsocioUvr, int tipoAsocio, int esquemaTarifario,int convenio,String tipoServicio,int tipoAnestesia,String ocupacion,String especialidad,String tipoEspecialista,int tipoLiquidacion) 
	{
		return objetoDao.consultarDetalleAsociosXUvr(con, codigoAsocioUvr, tipoAsocio, esquemaTarifario, convenio,tipoServicio,tipoAnestesia,ocupacion,especialidad,tipoEspecialista,tipoLiquidacion);
	}

	public HashMap consultarVigenciasXConvenio(Connection con, int convenio) 
	{
		return objetoDao.consultarVigenciasXConvenio(con, convenio);
	}

	public boolean insertarAsociosUvrMaestro(Connection con, HashMap vo,boolean guardarXTipoAsocio) 
	{
		return objetoDao.insertarAsociosUvrMaestro(con, vo,guardarXTipoAsocio);
	}

	public int insertarDetalleAsocioXUvr(Connection con, HashMap vo) 
	{
		return objetoDao.insertarDetalleAsocioXUvr(con, vo);
	}
	
	public boolean insertarAsociosXUvrXTipoAsocio(Connection con,HashMap vo,int codigoAsocioUvr)
	{
		return objetoDao.insertarAsociosXUvrXTipoAsocio(con, vo, codigoAsocioUvr);
	}
	
	/**
	 * Metodo para la actualizacion de los detalles de asocios por uvr
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarDetalle(Connection con,HashMap vo)
	{
		return objetoDao.modificarDetalle(con, vo);
	}
	
	/**
	 * Metodo para la eliminacion del detalle de asocios por uvr
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarDetalle(Connection con,int codigo)
	{
		return objetoDao.eliminarDetalle(con, codigo);
	}
	
	/**
	 * Metodo que elimina un registro del segundo maestro que es asocio por uvr por tipo de asocio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAsocioXUvrTipoAsocio(Connection con,int codigo)
	{
		return objetoDao.eliminarAsocioXUvrTipoAsocio(con, codigo);
	}

	/**
	 * Metodo que elimina un asocio por uvr cuando este viene por convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAsocioUvrXConvenio(Connection con,int codigo)
	{
		return objetoDao.eliminarAsocioUvrXConvenio(con, codigo);
	}
	
	/**
	 * Metodo que modifica las vigencias por convenio
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarVigencias(Connection con,HashMap vo)
	{
		return objetoDao.modificarVigencias(con, vo); 
	}
	
	/**
	 * Metodo encargado de mopdificar asocios por sala
	 * @param connection
	 * @param tipoAsocio
	 * @param tipoSala
	 * @param codigo
	 * @return
	 */
	public boolean modificarAsociosUvrSala (Connection connection,String tipoAsocio, String tipoSala, String codigo,String liquidarPor)
	{
		return objetoDao.modificarAsociosUvrSala(connection, tipoAsocio, tipoSala, codigo,liquidarPor);
	}
}
