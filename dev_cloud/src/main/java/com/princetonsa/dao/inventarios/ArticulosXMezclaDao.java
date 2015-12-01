/**
 * Juan David Ramírez 31/05/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * @author Juan David Ramírez
 *
 */
public interface ArticulosXMezclaDao
{

	/**
	 * Método para consultar lkas opciones de los selectores de la funcionalidad
	 * @param con
	 * @param codigoInstitucionInt
	 * @param tipoConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int codigoInstitucion, int tipoConsulta);

	/**
	 * Método para ingresar o modificar los datos de
	 * los artículos por mezcla
	 * @param con
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public int ingresarModificar(Connection con, Vector articulosIngresados, Vector articulosEliminados, int mezcla);

	/**
	 * Método para ingresar o modificar los datos de
	 * los artículos por mezcla
	 * @param con
	 * @param codigoMezcla @todo
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public HashMap consultar(Connection con, int codigoInstitucion, int codigoMezcla);

}
