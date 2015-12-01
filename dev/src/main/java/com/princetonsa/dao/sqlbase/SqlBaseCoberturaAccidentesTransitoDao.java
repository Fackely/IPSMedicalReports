/*
 * SqlBaseCoberturaAccidentesTransitoDao.java 
 * Autor			:  mdiaz
 * Creado el	:  25-nov-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 25-nov-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class SqlBaseCoberturaAccidentesTransitoDao {

	private static Logger logger=Logger.getLogger(SqlBaseCoberturaAccidentesTransitoDao.class);
	
	/**
	 * Sentencia para insertar la cobertura de accidentes
	 */
	private static final String insertarStr="INSERT INTO cob_accidentes_transito (institucion, responsable, cobertura) VALUES (?, (SELECT CASE WHEN max(responsable)+1 IS NULL THEN 1 ELSE max(responsable)+1 END AS responsable FROM cob_accidentes_transito WHERE institucion=?), ?)";
	
	/**
	 * Sentencia para borrar registro de cobertura
	 */
	private static final String borrarStr="DELETE FROM cob_accidentes_transito WHERE institucion = ?"; 
	
	/**
	 * Sentencia para listar de coberturas
	 */
	private static final String listarStr="SELECT institucion AS institucion, responsable AS responsable, cobertura AS cobertura FROM cob_accidentes_transito WHERE institucion = ? ORDER BY responsable";
	
	/**
	 * Método para insertar responsables en la BD
	 * @param con
	 * @param codInstitucion
	 * @param cobertura
	 * @return numero de registrs insertados
	 */
	public static int insertar(Connection con, int codInstitucion, double[] cobertura)
	{
		int i=0, resultado=0;
		try
		{
			PreparedStatementDecorator insertar= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for(i=0;i<cobertura.length;i++)
			{
				insertar.setInt(1, codInstitucion);
				insertar.setInt(2, codInstitucion);
				insertar.setDouble(3, cobertura[i]);
				resultado+=insertar.executeUpdate();
			}
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando la cobertura "+cobertura[i]+": "+e);
			return resultado;
		}
	}
	
	/**
	 * Método para eliminar la cobertura de accidentes de transito
	 * @param con
	 * @param codInstitucion
	 * @return numero de elementos eliminados
	 */
	public static int eliminar(Connection con, int codInstitucion)
	{
		try
		{
			PreparedStatementDecorator borrar= new PreparedStatementDecorator(con.prepareStatement(borrarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			borrar.setInt(1, codInstitucion);
			return borrar.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("error eliminando las coberturas para la institucion "+codInstitucion+": "+e);
			return 0;
		}
	}

	/**
	 * Método para listar la cobertura de accidentes para una institución dada
	 * @param con
	 * @param institucion
	 * @return Mapa con los datos de la cobertura para la institución dada
	 */
	public static HashMap listar(Connection con, int institucion)
	{
		try
		{
			PreparedStatementDecorator listado= new PreparedStatementDecorator(con.prepareStatement(listarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listado.setInt(1, institucion);
			String[] columnas=
			{
					"institucion",
					"responsable",
					"cobertura"
			};
			return UtilidadBD.resultSet2HashMap(columnas, new ResultSetDecorator(listado.executeQuery()), true, true).getMapa();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las coberturas de tránsito "+e);
			return null;
			
		}
	}


}
