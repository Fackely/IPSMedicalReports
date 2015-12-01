package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.UsuariosAutorizarAnulacionFacturasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseUsuariosAutorizarAnulacionFacturasDao;

public class PostgresqlUsuariosAutorizarAnulacionFacturasDao implements
		UsuariosAutorizarAnulacionFacturasDao 
		
{

	
	/**
	 * 
	 */
	public HashMap consultarUsuariosAutorizados(Connection con,	String centroAtencion, String codigoInstitucion) 
	{
		return SqlBaseUsuariosAutorizarAnulacionFacturasDao.consultarUsuariosAutorizados(con, centroAtencion, codigoInstitucion);
	}
	
	/**
	 * 
	 */
	public boolean insertarUsuarios(Connection con, String centroAtencion, String usuarioAutorizado, String codigoInstitucion, String loginUsuario) 
	{
		return SqlBaseUsuariosAutorizarAnulacionFacturasDao.insertarUsuarios(con, centroAtencion, usuarioAutorizado, codigoInstitucion, loginUsuario);
	}
	
	/**
	 * 
	 */
	public boolean eliminarUsuario(Connection con, int codigoPk) 
	{
		return SqlBaseUsuariosAutorizarAnulacionFacturasDao.eliminarUsuario(con, codigoPk);
	}

	/**
	 * 
	 */
	public boolean modificarUsuario(Connection con, int codigoPk, String usuarioAutorizado, String loginUsuario) 
	{
		return SqlBaseUsuariosAutorizarAnulacionFacturasDao.modificarUsuario(con, codigoPk, usuarioAutorizado, loginUsuario);
	}
	
		
}
