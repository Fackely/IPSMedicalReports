/*
 *Sep 05/2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.Utilidades;

/**
 * @author Sebastián Gómez
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Salas
 */
public class SqlBaseSalasDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSalasDao.class);
	
	/**
	 * Cadena que consulta los registros de la tabla salas
	 */
	private static final String cargarSalasStr="SELECT "+ 
		"sal.consecutivo AS consecutivo,"+
		"sal.codigo AS codigo,"+
		"sal.tipo_sala AS tipo,"+
		"sal.activo AS activo,"+ 
		"sal.descripcion AS descripcion," +
		"coalesce(sal.medico||'', '') AS medico, " +
		"administracion.getnombremedico(sal.medico) AS nombremedico, " +
		"ts.es_quirurgica AS quirurgica," +
		"ts.descripcion AS descTipoSala, "+
		"CASE WHEN getUsoSala(sal.consecutivo) > 0 THEN 1 ELSE 0 END AS es_usada "+
			"FROM salas sal " +
				"INNER JOIN tipos_salas ts ON (sal.tipo_sala=ts.codigo AND sal.institucion=ts.institucion)" +
					"WHERE sal.institucion=?" +
					"  AND sal.centro_atencion = ? " +
					" ORDER BY sal.descripcion";
	
	/**
	 * Cadena que actualiza los datos de un registro de la tabla salas
	 */
	private static final String actualizarSalaStr="UPDATE salas SET codigo=?,tipo_sala=?,activo=?,descripcion=?,medico=? WHERE consecutivo=?";
	
	/**
	 * Cadena que elimina un registro de la tabla salas
	 */
	private static final String eliminarSalaStr="DELETE FROM salas WHERE consecutivo=?";
	
	/**
	 * Cadena que consulta un registros de la tabla salas
	 * por el consecutivo
	 */
	private static final String cargarSalaStr="SELECT "+ 
		"consecutivo AS consecutivo,"+
		"codigo AS codigo,"+
		"tipo_sala AS tipo,"+
		"activo AS activo,"+ 
		"descripcion AS descripcion, "+
		"CASE WHEN getUsoSala(consecutivo) > 0 THEN 1 ELSE 0 END AS es_usada "+
		"FROM salas WHERE consecutivo=?";
	
	/**
	 * Cadena que consulta los rangos de disponibilidad de la sala 
	 */
	private static final String cargarDisponibilidadSalaStr = "SELECT 1 AS consecutivo, " +
																															  "codigo_sala AS codigo_sala, " +
																															  "rango_inicial AS rango_inicial, " +
																															  "rango_final	AS rango_final " +
																												" FROM disponibilidad_salas " +
																													" WHERE codigo_sala = ?";
	
	/**
	 * Consulta sql para actualizar un rango de disponibilidad de una sala
	 */
	private static final String actualizarDisponibilidadSalaStr="UPDATE disponibilidad_salas SET rango_inicial = ?, rango_final = ? " +
																														" WHERE codigo_sala = ? AND rango_inicial = ? AND rango_final = ?";
	
	/**
	 * Cadena sql para insertar un rango de disponibilidad para la sala
	 */
	private static final String insertarDisponibilidadSalaStr = "INSERT INTO disponibilidad_salas " +
																																		"(codigo_sala, " +
																																		"rango_inicial," +
																																		" rango_final) " +
																																		" VALUES (?, ?, ?)";
	
	
	
	
	//******************************************************* Métodos de la Clase********************************************//
	/**
	 * Método usado para cargar el listado de salas existentes
	 * por institucion
	 * @param con
	 * @param institucion
	 * @param centroAtencion 
	 * @return
	 */
	public static HashMap cargarSalas(Connection con,int institucion, int centroAtencion)
	{
		//columnas del listado
		String[] columnas={
				"consecutivo",
				"codigo",
				"tipo",
				"activo",
				"descripcion",
				"medico",
				"nombremedico",
				"quirurgica",
				"descTipoSala",
				"es_usada"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarSalasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			pst.setInt(2,centroAtencion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSalas de SqlBaseSalasDao: "+e);
			return null;
		}
		
	}
	
	/**
	 * Método para insertar una nueva sala
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param centroAtencion 
	 * @param insertarSalasStr
	 * @return
	 */
	public static int insertarSala(Connection con,String codigo,int institucion,
			int tipoSala,boolean activo,String descripcion,String insertarSalaStr, int centroAtencion, String medico)
	{
		logger.info("========================================================= ");		
		logger.info("======================== SALA INSERTAR");
		logger.info("== Codigo: " + codigo);
		logger.info("== Institucion: " + institucion);
		logger.info("== TipoSala : " + tipoSala);
		logger.info("== Activo: " + activo);
		logger.info("== Descripcion: " + descripcion);
		logger.info("== Centro Atencion: " + centroAtencion);
		logger.info("== Medico: "+ medico);
		logger.info("== Insertar: " + insertarSalaStr);
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setInt(2,institucion);
			pst.setInt(3,tipoSala);
			pst.setBoolean(4,activo);
			pst.setString(5,descripcion);
			pst.setInt(6,centroAtencion);
			if(medico.isEmpty() || medico.equals(ConstantesBD.codigoNuncaValido+"")) {
				logger.info("== OJOOOO MEDICO VACIOOO");
				pst.setNull(7, Types.INTEGER);
			}
			else	
				pst.setInt(7, Utilidades.convertirAEntero(medico));
			
			int resp=pst.executeUpdate();
			if(resp>0)
				//se toma el consecutivo insertado
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_salas");
			else
				return 0;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarSalas de SqlBaseSalasDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para actualizar los datos de una sala
	 * @param con
	 * @param codigo
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param consecutivo
	 * @param centroAtencion 
	 * @return
	 */
	public static String actualizarSala(Connection con,String codigo,int tipoSala,boolean activo,String descripcion,int consecutivo, int centroAtencion, String medico)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setInt(2,tipoSala);
			pst.setBoolean(3,activo);
			pst.setString(4,descripcion);
			if(medico.isEmpty() || medico.equals(ConstantesBD.codigoNuncaValido+""))
				pst.setNull(5, Types.INTEGER);
			else	
				pst.setInt(5, Utilidades.convertirAEntero(medico));
			pst.setInt(6,consecutivo);
			
			return String.valueOf(pst.executeUpdate());
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarSala de SqlBaseSalasDao: "+e);
			return "-1";
		}
		
	}
	
	/**
	 * Método usado para eliminar una sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static int eliminarSala(Connection con,int consecutivo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivo);
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarSala de SqlBaseSalasDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para cargar una sala de las tablas existentes
	 * de acuerdo a su consecutivo interno de Axioma
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap cargarSala(Connection con,int consecutivo)
	{
		//columnas del listado
		String[] columnas={
				"consecutivo",
				"codigo",
				"tipo",
				"activo",
				"descripcion",
				"es_usada"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivo);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSala de sqlBaseSalasDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para cargar la disponiblidad de la sala
	 * @param con
	 * @param consecutivoSala
	 * @return HashMap con la disponibilidad de la sala
	 */
	public static HashMap cargarDisponibilidadSala(Connection con, String consecutivoSala)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(cargarDisponibilidadSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cargarDisponibilidadSalaStr-->"+cargarDisponibilidadSalaStr+"-->"+Utilidades.convertirAEntero(consecutivoSala));
			statement.setInt(1, Utilidades.convertirAEntero(consecutivoSala));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
			statement.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando la disponibilidad de la sal : "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para actualizar un rango de disponibilidad para una sala específica
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param rangoInicialAnterior
	 * @param rangoFinalAnterior
	 * @return 
	 */
	public static int actualizarDisponibilidadSala (Connection con, String codigoSala, String rangoInicial, String rangoFinal, String rangoInicialAnterior, String rangoFinalAnterior)
	{
		try
		{
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDisponibilidadSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, rangoInicial);
			pst.setString(2, rangoFinal);
			pst.setString(3, codigoSala);
			pst.setString(4, rangoInicialAnterior);
			pst.setString(5, rangoFinalAnterior);
			
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarDisponibilidadSala de SqlBaseSalasDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método para insertar un nuevo rango de disponibilidad de la sala
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @return
	 */
	public static String insertarDisponibilidadSala(Connection con, String codigoSala, String rangoInicial, String rangoFinal)
	{
		logger.info("\n insertarDisponibilidadSala codigoSala -->"+codigoSala+"  rangoInicial -->"+rangoInicial+"  rangoFinal -->"+rangoFinal);

		try
		{
			
			//-Primero se debe verificar que para ese horario no se inserten datos para el mismo horario para la sala.  
			String vr = String.valueOf(verificarHorario( con, codigoSala, rangoInicial, rangoFinal));
			
			logger.info("\n verificar que para ese horario no se inserten datos para el mismo horario para la sala.  "+vr);
			if (Integer.parseInt(vr) == 0)
			{
			
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDisponibilidadSalaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1,codigoSala);
				pst.setString(2, rangoInicial);
				pst.setString(3, rangoFinal);
				
				int resp=pst.executeUpdate();
				if(resp>0)
					return codigoSala;
				else
					return "0";
			}		
			else
			{
				return "1"; 
			}		
					
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarDisponibilidadSala de SqlBaseSalasDao: "+e);
			return "-1";
		}
	}
	
	/**
	 * Metodo para saber Si se pùede programar la sala con ese horario determinado
	 * @param nroSala
	 * @param horaInicioProgramacion
	 * @param horaFinProgramacion
	 * @return 0 Si no hay un horario entonces si se puede registrar 
	 * 		  -1 Si Hubo probelmas con la base de datos	
	 * 		   1 Si hay registros para esa hora para esa sala. 	
	 */
	public static int verificarHorario(Connection con, String nroSala, String horaInicioProgramacion, String horaFinProgramacion)
	{
		
		logger.info("\n entre a verificarHorario nroSala -->"+nroSala+"  horaInicioProgramacion-->"+horaInicioProgramacion+"  horaFinProgramacion -->"+horaFinProgramacion);
		PreparedStatementDecorator ps = null;
		
		//----consulta para saber si en sala-fecha-hora hay algo ya registrado
		String cad =  "  	SELECT codigo_sala FROM disponibilidad_salas ds 										" +
	  						"						WHERE codigo_sala = ?	 										" +
							"		 	    		  AND (															" +			
							"							    (ds.rango_inicial >= ? AND ds.rango_inicial <= ?)		" +
							"								 OR														" +		
							"								(ds.rango_final > ? AND ds.rango_final <= ?)			" +
							"								 OR														" +		
							"								(? >= ds.rango_inicial AND ? < ds.rango_final)			" +
							"								 OR														" +
							"								(? >= ds.rango_inicial AND ? <= ds.rango_final)			" +
							"				     		   )														";
		try
		{	
			logger.info("\n consulta de verificarHorario "+cad);
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, nroSala);

				ps.setString(2, horaInicioProgramacion);
				ps.setString(3, horaFinProgramacion);
				ps.setString(4, horaInicioProgramacion);
				ps.setString(5, horaFinProgramacion);
			
				ps.setString(6, horaInicioProgramacion);
				ps.setString(7, horaInicioProgramacion);
				ps.setString(8, horaFinProgramacion);
				ps.setString(9, horaFinProgramacion);

				ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
				
				if(resultado.next()) { return 1;}  //-Si encontro un registro que se troca en el horario.
				else { return 0; }
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del horario de disponibilidad para una Sala (verificarCronograma) : SqlSalasDao "+e.toString());
			return -1;
		}
	}
	
	
	

	/**
	 * Metodo para eliminar el rango de una sala especifica
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @return
	 */
	public static int accionEliminarRango(Connection con, String codigoSala, String rangoInicial, String rangoFinal) 
	{
		try
		{
			
			String consulta = " DELETE FROM disponibilidad_salas WHERE codigo_sala = ? AND rango_inicial = ? AND rango_final = ? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoSala);
			pst.setString(2,rangoInicial);
			pst.setString(3,rangoFinal);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error Eliminando Rangos De disponibilidad de Salas  "+e);
			return -2;
		}
	}
	
}
