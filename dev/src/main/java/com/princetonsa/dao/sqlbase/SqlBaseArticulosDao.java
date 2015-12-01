/*
 * Created on Apr 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

/**
 * @author wrios
 *
 */
public class SqlBaseArticulosDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseArticulosDao.class);
	
	private static final String listarArticulosStr = "SELECT  a.codigo AS codigo," +
															" a.minsalud AS minsalud," +
															" a.descripcion AS descripcion" +
															", a.naturaleza AS naturaleza" +
															", a.forma_farmaceutica AS forma_farmaceutica" +
															", a.concentracion AS concentracion" +
															", a.clase AS clase" +
															", a.grupo AS grupo" +
															", a.subgrupo AS subgrupo" +
															", a.estado AS estado" +
															", a.unidad_medida AS unidad_medida " +
															"FROM articulo a " +
															"ORDER BY a.codigo ASC";

	private static final String buscarArticulosStr = "SELECT   a.codigo AS codigo" +
															", a.minsalud AS minsalud" +
															", a.descripcion AS descripcion" +
															", a.naturaleza AS naturaleza" +
															", a.forma_farmaceutica AS forma_farmaceutica" +
															", a.concentracion AS concentracion" +
															", a.clase AS clase" +
															", a.grupo AS grupo" +
															", a.subgrupo AS subgrupo" +
															", a.estado AS estado" +
															", a.unidad_medida AS unidad_medida" +
															" FROM articulo a WHERE ";
	
	public static Collection listarArticulos(Connection con) 
	{
			try
			{
				PreparedStatementDecorator listarArticulosStmnt =  new PreparedStatementDecorator(con.prepareStatement(listarArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(listarArticulosStmnt.executeQuery()));
			}
			catch (SQLException e)
			{
				logger.error(" "+e);
				return null;
			}
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.ArticulosDao#buscarArticulos(java.sql.Connection, java.lang.String[])
	 */
	public static Collection buscarArticulos(Connection con, String[] criteriosBusqueda,String clase,String grupo,String subgrupo,String codigo,String descripcion,String naturaleza,String minsalud,String formaFarmaceutica,String unidadMedida,String concentracion, boolean estado)
	{
		boolean existeDescripcion=false,existeAsignacion=false;
		String buscarArticulosStr2=new String(buscarArticulosStr);
		for(int i=0;i< criteriosBusqueda.length;i++){

			if(criteriosBusqueda[i].equals("claseArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("clase LIKE %"+clase+"% ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("grupoArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("grupo LIKE %"+grupo+"% ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("subgrupoArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("subgrupo LIKE %"+subgrupo+"% ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("codigoArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("codigo LIKE %"+codigo+"% ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("descripcionArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("UPPER(descripcion) LIKE UPPER('%"+descripcion+"%') ");
				existeDescripcion=true;
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("naturalezaArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("naturaleza LIKE '%"+naturaleza+"%' ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("minsaludArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("minsalud LIKE '%"+minsalud+"%' ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("formaFarmaceuticaArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
					
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("forma_farmaceutica LIKE '%"+formaFarmaceutica+"%' ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("unidadMedidaArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("unidad_medida LIKE '%"+unidadMedida+"%' ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("concentracionArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("UPPER(concentracion) LIKE UPPER('%"+concentracion+"%') ");
				existeAsignacion=true;
			}
			if(criteriosBusqueda[i].equals("estadoArticulo")){
				if(existeAsignacion){
					buscarArticulosStr2=buscarArticulosStr2.concat(" AND ");
				}
				buscarArticulosStr2=buscarArticulosStr2.concat("estado="+estado+" ");
				existeAsignacion=true;
			}
			
		}
		if(existeDescripcion){
			buscarArticulosStr2=buscarArticulosStr2.concat("GROUP BY descripcion, codigo , clase ,grupo,subgrupo,naturaleza,minsalud,forma_farmaceutica,unidad_medida,concentracion,estado");
		}else{
			buscarArticulosStr2=buscarArticulosStr2.concat("GROUP BY codigo, descripcion , clase ,grupo,subgrupo,naturaleza,minsalud,forma_farmaceutica,unidad_medida,concentracion,estado");
		}
		PreparedStatementDecorator buscarArticulosStmnt;
		try
		{
			buscarArticulosStmnt =  new PreparedStatementDecorator(con.prepareStatement(buscarArticulosStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(buscarArticulosStmnt.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en la búsqueda de artículos "+e);
			return null;
		}
	}

}
