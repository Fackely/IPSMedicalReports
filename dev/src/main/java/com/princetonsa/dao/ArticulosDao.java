/*
 * Created on Nov 28, 2003
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;


/**
 * @author rcancino
 *
 * Princeton S.A
 */
public interface ArticulosDao{

	/**
	 * Listado de Artículos
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public Collection listarArticulos(Connection con);

	/**
	 * @param con
	 * @param criteriosBusqueda
	 * @return
	 */
	public Collection buscarArticulos(Connection con, String[] criteriosBusqueda,String clase,String grupo,String subgrupo,String codigo,String descripcion,String naturaleza,String minsalud,String formaFarmaceutica,String unidadMedida,String concentracion, boolean estado);

}
