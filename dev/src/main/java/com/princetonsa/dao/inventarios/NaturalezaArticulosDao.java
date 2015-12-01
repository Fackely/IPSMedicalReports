package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface NaturalezaArticulosDao {
	
	/**
	 * Consulta naturaleza de articulos existentes por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */

	public abstract HashMap consultarNaturalezaArticulosExistentes(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	
	public abstract boolean insertar(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public abstract boolean modificar(Connection con,HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public abstract boolean eliminarRegistro(Connection con, int institucion, String acronimo);
	
	/**
	 * 
	 * @param acronimoNaturaleza
	 * @param pos 
	 * @param posNoPos 
	 * @return
	 */
	public abstract boolean esMedicamento(String acronimoNaturaleza);
}
