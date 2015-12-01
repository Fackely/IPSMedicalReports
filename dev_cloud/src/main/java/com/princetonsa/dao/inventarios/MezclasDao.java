package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public interface MezclasDao 
{
	/**
	 * Metodo que inserta una mezcla
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param codTipoMezcla
	 * @param activo
	 * @param codInstitucion
	 * @throws SQLException
	 */
	public void insertarMezcla(Connection con, String codigo, String nombre, int codTipoMezcla, String activo, int codInstitucion) throws SQLException;
	
	/**
	 * Método para actualizar la información de una mezcla
	 * @param con
	 * @param consecutivo
	 * @param nombre
	 * @param codTipoMezcla
	 * @param activo
	 * @param codInstitucion
	 * @throws SQLException
	 */
	public void actualizarMezcla(Connection con, int consecutivo, String nombre, int codTipoMezcla, String activo) throws SQLException;

	/**
	 * Método para eliminar una mezcla
	 * @param con
	 * @param consecutivo
	 * @throws SQLException
	 */
	public void eliminarMezcla(Connection con, int consecutivo) throws SQLException;
	
	/**
	 * Método para consultar la información de una mezcla
	 * @param con
	 * @param consecutivo
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMezcla(Connection con, int consecutivo) throws SQLException;
	
	/**
	 * Método para consultar las mezclas de una institución especificada
	 * @param con
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMezclasInst(Connection con, int codInstitucion) throws SQLException;
}
