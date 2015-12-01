/*
 * Creado el Jun 13, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.HashMap;

public interface NivelAtencionDao {

	/**
	 * Metodo para cargar los niveles de servcicio
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap cargarInformacion(Connection con, int codigoInstitucionInt);

	/**
	 * Metodo para insertar Niveles de Servicios.
	 * @param con
	 * @param tipoOperacion
	 * @param codigo
	 * @param descripcion
	 * @param Activo 
	 * @return
	 */
	public int insertar(Connection con, int tipoOperacion, int codigo, String descripcion, boolean Activo, int institucion);

	/**
	 * Metodo para eliminar un nivel de servicio. 
	 * @param con
	 * @param nroReg
	 * @return
	 */
	public int eliminar(Connection con, int nroReg);

}
