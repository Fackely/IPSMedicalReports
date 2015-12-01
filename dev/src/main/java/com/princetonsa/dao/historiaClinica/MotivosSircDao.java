package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface MotivosSircDao
{
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarMotivoSirc(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarMotivoSirc(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarMotivoSirc(Connection con,HashMap vo);

	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract boolean eliminarRegistro(Connection con, String consecutivo);
}
