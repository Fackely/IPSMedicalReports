package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoModulo;
public interface NewMenuDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public ArrayList<DtoModulo> cargar(Connection con, String loginUsuario);
	
}