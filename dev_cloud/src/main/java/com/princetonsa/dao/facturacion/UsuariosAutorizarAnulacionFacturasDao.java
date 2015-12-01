package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface UsuariosAutorizarAnulacionFacturasDao 
{

	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	HashMap consultarUsuariosAutorizados(Connection con, String centroAtencion,	String codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param usuarioAutorizado
	 * @param codigoInstitucion
	 * @return
	 */
	boolean insertarUsuarios(Connection con, String centroAtencion,	String usuarioAutorizado, String codigoInstitucion, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param usuarioAutorizado
	 * @return
	 */
	boolean modificarUsuario(Connection con, int codigoPk, String usuarioAutorizado, String loginUsuario);

	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @return
	 */
	boolean eliminarUsuario(Connection con, int codigoPk);

	
	
}
