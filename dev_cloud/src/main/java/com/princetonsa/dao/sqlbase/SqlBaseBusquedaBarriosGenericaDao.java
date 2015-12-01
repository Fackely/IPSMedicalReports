/*
 * Jun 15, 2007 
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * 
 * @author Sebastián Gómez R.
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares para la Busqueda de BARRIOS
 *
 */
public class SqlBaseBusquedaBarriosGenericaDao 
{
	/**
    * Objeto para manejar los logs de esta clase
    */
	private static Logger logger = Logger.getLogger(SqlBaseBusquedaBarriosGenericaDao.class);
	
	/**
	 * Método que realiza la consulta de barrios
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consulta(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT " +
				"codigo," +
				"codigo_departamento," +
				"codigo_ciudad," +
				"codigo_barrio," +
				"codigo_pais," +
				"coalesce(codigo_localidad,'') AS codigo_localidad," +
				"descripcion," +
				"coalesce(getdesclocalidad(codigo_pais,codigo_departamento,codigo_ciudad,codigo_localidad),'') as desc_localidad " +
				"FROM barrios " +
				"WHERE " +
				"codigo_ciudad = '"+campos.get("codigoCiudad")+"' AND " +
				"codigo_departamento = '"+campos.get("codigoDepartamento")+"' AND "+
				"codigo_pais = '"+campos.get("codigoPais")+"' ";
			
			
			//Filtro del criterio
			if(!campos.get("criterioBarrio").toString().equals(""))
				consulta += " AND UPPER(descripcion) like UPPER('%"+campos.get("criterioBarrio")+"%') ";
			
			logger.info("CONSULTA DE LOS BARRIOS=> "+consulta);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String,Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("codigoBarrio", rs.getObject("codigo_barrio"));
				elemento.put("nombreBarrio",rs.getObject("descripcion"));
				elemento.put("codigoLocalidad",rs.getObject("codigo_localidad"));
				elemento.put("nombreLocalidad",rs.getObject("desc_localidad"));
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consulta:  "+e);
		}
		
		return resultados;
	}
}
