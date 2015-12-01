package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.UsuariosAutorizarAnulacionFacturasDao;

public class UsuariosAutorizarAnulacionFacturas 
{

	
	/**
	 * 
	 */
	private UsuariosAutorizarAnulacionFacturasDao objetoDao;
	
	/**
	 * 
	 */
	public UsuariosAutorizarAnulacionFacturas()
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
			objetoDao=myFactory.getUsuariosAutorizarAnulacionFacturasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}

	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarUsuariosAutorizados(Connection con, String centroAtencion, String codigoInstitucion) 
	{
		return objetoDao.consultarUsuariosAutorizados(con, centroAtencion, codigoInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param usuarioAutorizado
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertarUsuarios(Connection con, String centroAtencion, String usuarioAutorizado, String codigoInstitucion, String loginUsuario) 
	{
		return objetoDao.insertarUsuarios(con, centroAtencion, usuarioAutorizado, codigoInstitucion, loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param usuarioAutorizado
	 * @return
	 */
	public boolean modificarUsuario(Connection con, int codigoPk, String usuarioAutorizado, String loginUsuario) 
	{
		return objetoDao.modificarUsuario(con, codigoPk, usuarioAutorizado, loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @return
	 */
	public boolean eliminarUsuario(Connection con, int codigoPk) 
	{
		return objetoDao.eliminarUsuario(con, codigoPk);
	}
	
	
	
}
