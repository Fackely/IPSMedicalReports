package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface GrupoEspecialArticulosDao 
{
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	boolean eliminarRegistro(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificar(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertar(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	HashMap consultarMotivoEspecifico(Connection con, int codigoGrupo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap consultarMotivosExistentes(Connection con);

	
	
}
