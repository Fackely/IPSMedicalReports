/*
 * Created on Nov 28, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.Collection;


import com.princetonsa.dao.ArticulosDao;
import com.princetonsa.dao.sqlbase.SqlBaseArticulosDao;

/**
 * @author rcancino
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OracleArticulosDao implements ArticulosDao {

	public Collection listarArticulos(Connection con)
	{
		return SqlBaseArticulosDao.listarArticulos(con);
	}

	public Collection buscarArticulos(Connection con, String[] criteriosBusqueda,String clase,String grupo,String subgrupo,String codigo,String descripcion,String naturaleza,String minsalud,String formaFarmaceutica,String unidadMedida,String concentracion, boolean estado) 
	{
		return SqlBaseArticulosDao.buscarArticulos(con,  criteriosBusqueda, clase, grupo, subgrupo, codigo, descripcion, naturaleza, minsalud, formaFarmaceutica, unidadMedida, concentracion, estado);
	}

}
