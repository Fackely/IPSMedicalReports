package com.princetonsa.dao.consultaExterna;
 
import java.sql.Connection;
import java.util.HashMap;

public interface MotivosCancelacionCitaDao 
{

	
	/**
	 * 
	 * @param con
	 * @param codigoMotivo
	 * @return
	 */
	boolean eliminarRegistro(Connection con, int codigoMotivo);

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
	 * @param codigoMotivo
	 * @return
	 */
	HashMap consultarMotivoEspecifico(Connection con, int codigoMotivo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap consultarMotivosExistentes(Connection con,HashMap parametros);

}
