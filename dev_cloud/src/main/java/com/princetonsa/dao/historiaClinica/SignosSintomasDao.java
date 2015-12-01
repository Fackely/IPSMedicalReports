/*
 * Creado el Jun 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface SignosSintomasDao {

	/**
	 * Metodo para cargar la informacion de los signos y sintomas.
	 * @param con
	 * @param institucion 
	 */
	HashMap cargarSignosSintomas(Connection con, int institucion); 

	/**
	 * Metodo para insertar datos de los signos y sintomas. 
	 * @param con
	 * @param consecutivo
	 * @param codigo2 
	 * @param descripcion
	 * @return
	 */
	int insertar(Connection con, int consecutivo, String codigo, String descripcion, int codigoInstitucion);

	/**
	 * Metodo para eliminar un sintoma o diagnostico.
	 * @param con
	 * @param nroRegEliminar
	 * @return
	 */
	int eliminar(Connection con, int nroRegEliminar);

}
