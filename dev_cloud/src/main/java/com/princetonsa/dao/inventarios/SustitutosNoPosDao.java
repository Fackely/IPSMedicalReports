package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.inventarios.SustitutosNoPosForm;
import com.princetonsa.mundo.inventarios.SustitutosNoPos;


public interface SustitutosNoPosDao
{
	/**
	 * Metodo de consulta de articulos sustitutos
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaSus (Connection con, int codigoArtPpal);
	
	/**
	 * Metodo de consulta de clase y grupo articulo principal
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaCG (Connection con, int codigoArtPpal);
	
		
	/**
	 * 
	 */
	public boolean eliminarSustitutosNoPos(Connection con, String sustitutosNoPos);
	
	/**
	 * 
	 */
	public boolean modificarSustitutosNoPos(Connection con, SustitutosNoPos sustitutosNoPos);
	
	/**
	 * 
	 */
	public boolean insertarSustitutosNoPos(Connection con, SustitutosNoPos sustitutosNoPos);
}