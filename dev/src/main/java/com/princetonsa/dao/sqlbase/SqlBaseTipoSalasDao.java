/*
 * Created on Sep 1, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez R
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Tipos de Salas
 */
public class SqlBaseTipoSalasDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTipoSalasDao.class);
	
	/**
	 * Cadena usada para consultar todos los tipos de salas según la institucion
	 */
	private static final String cargarTiposSalasStr="SELECT " +
													"codigo As codigo," +
													"descripcion As nombre," +
													"es_quirurgica AS quirurgica, " +
													"case when es_urgencias = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as urgencias, " +
													"CASE WHEN getUsoTipoSala(codigo,institucion) > 0 THEN 1 ELSE 0 END AS es_usada "+
													"FROM tipos_salas WHERE institucion=? ORDER BY descripcion";
	 
	/**
	 * Cadena usada parea actualizar los datos de un registro en tipos_salas
	 */
	private static final String actualizarTipoSalaStr=" UPDATE tipos_salas " +
													  " SET descripcion=?," +
													  " es_quirurgica=?, " +
													  " es_urgencias = ?" +
													  " WHERE codigo=?";
	
	/**
	 * Cadena usada para borrar un registro en la tabla tipos_salas
	 */
	private static final String eliminarTipoSalaStr=" DELETE FROM tipos_salas WHERE codigo=?";
	
	/**
	 * Cadena usada para cargar un registro de la tabla tipo_salas
	 */
	private static final String cargarTipoSalaStr="SELECT " +
												"codigo AS codigo," +
												"descripcion As nombre," +
												"es_quirurgica AS quirurgica, " +
												"case when es_urgencias ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as urgencias, " +
												"CASE WHEN getUsoTipoSala(codigo,institucion) > 0 THEN 1 ELSE 0 END AS es_usada "+
												"FROM tipos_salas WHERE codigo=?";
	
	private static final String consultaTipoSalasGruposServicios = 
		"SELECT " +
			"gs.codigo AS cod, " +
			"gs.descripcion AS descripcion, " +
			"gs.tipo_sala_standar AS tipoSalaStandar " +
		"FROM grupos_servicios gs " +
		"WHERE tipo_sala_standar=";
	
	/**
	 * Método usado para cargar todos los tipos de salas existentes
	 * según la institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarTiposSalas(Connection con,int institucion)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"nombre",
				"quirurgica",
				"urgencias",
				"es_usada"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarTiposSalasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("-->"+cargarTipoSalaStr);
			pst.setInt(1,institucion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposSalas de SqlBaseTipoSalasDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para ingresar al sistema un nuevo tipo de sala
	 * @param con
	 * @param descripcion
	 * @param quirurgica
	 * @param institucion
	 * @param consulta Query
	 * @return
	 */
	public static int insertarTipoSala(Connection con,String descripcion,boolean quirurgica,boolean urgencias,int institucion,String insertarTipoSalaStr)
	{
		try
		{
			int resp=0; //variable usada para manejar el resultado de la transacción
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarTipoSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,descripcion);
			pst.setBoolean(2,quirurgica);
			pst.setBoolean(3,urgencias);
			pst.setInt(4,institucion);
			
			resp=pst.executeUpdate();
			if(resp>0)
				//se toma el consecutivo insertado
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_tipos_salas");
			else
				return -1;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarTipoSala de SqlBaseTipoSalasDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para actualizar los datos de un registro
	 * en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param quirurgica
	 * @return
	 */
	public static int actualizarTipoSala(Connection con,int codigo,String descripcion,boolean quirurgica, boolean urgencias)
	{
		/*
		logger.info("===> Codigo tipo sala: "+codigo);
		logger.info("===> Descripcion tipo sala: "+descripcion);
		HashMap resultados = consultaTipoSalasGruposServicios(con, codigo);
		logger.info("===> resultados: "+resultados);
		if(Integer.parseInt(resultados.get("numRegistros")+"")==0)
		{
			logger.info("===> Resultados Vacío");
		}
		else
		{
			logger.info("===> Resultados Llenos Hay que hacer la validación");
		}
		*/
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarTipoSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,descripcion);
			pst.setBoolean(2,quirurgica);
			pst.setBoolean(3,urgencias);
			pst.setInt(4,codigo);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarTipoSala de SqlBaseTipoSalasDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para eliminar un registro en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarTipoSala(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarTipoSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarTipoSala de SqlBaseTipoSalasDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para cargar los datos de un tipo de sala
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarTipoSala(Connection con,int codigo)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"nombre",
				"quirurgica",
				"urgencias",
				"es_usada"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarTipoSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTipoSala de SqlBaseTipoSalasDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método encargado de consultar los datos tipo salas que estén usados en grupo servicios
	 * @param Connection con
	 * @param int codigo
	 * @return HashMap
	 */
	public static HashMap consultaTipoSalasGruposServicios (Connection connection, int codigo)
	{
		//logger.info("===> Entre a consultaTipoSalasGruposServicios ");
		String cadena = consultaTipoSalasGruposServicios;
		cadena+=codigo;
		logger.info("===> Cadena consultaTipoSalasGruposServicios: "+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	        ps.close();
			return mapaRetorno;
					
		}
		catch (SQLException e)
		{
			logger.error("===> Problema consultando datos Tipos Salas Grupos Servicios "+e);
			e.printStackTrace();
		}
		return null;
	}
	
}
