/*
 * Created on 24/05/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.sies.mundo.SiEsConstantes;
import com.sies.mundo.UtilidadSiEs;

import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * 
 * SiEs
 * @author Juan David Ramírez L.
 *
 */
public class SqlBaseRestriccionesDao
{
    /**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRestriccionesDao.class);

	/*************************Asignar Restricción a Persona********************/
	
	/**
	 * String que contiene la consulta de las restricciones que estan en la base de datos
	 */
	private static String listadoRestriccionesEnfermeraStr=
			"SELECT " +
				"codigoRestriccion AS codigo, " +
				"descrrestriccion AS descripcion, " +
				"text AS texto, " +
				"true AS es_restriccion," +
				"'' AS color_letra, " +
				"'' AS color_fondo, " +
				"'' AS simbolo, " +
				ValoresPorDefecto.getValorFalseParaConsultas()+" AS por_defecto, " +
				"null AS es_festivo " +
			"FROM " +
				"restriccion_enfermera " +
			"UNION " +
			"( " +
				"SELECT " +
					"t.codigoturno AS codigo, " +
					"t.descripcion AS descripcion, " +
					"false AS texto, " +
					"false AS es_restriccion," +
					"t.color_letra AS color_letra, " +
					"t.color_fondo AS color_fondo, " +
					"t.simbolo AS simbolo, " +
					"tt.por_defecto AS por_defecto, " +
					"t.es_festivo AS es_festivo " +
				"FROM " +
					"turno t " +
				"INNER JOIN " +
					"tipos_turno tt " +
				"ON(tt.acronimo=t.tipo_turno AND t.activo=" +ValoresPorDefecto.getValorTrueParaConsultas()+") " +
			")" +
			"ORDER BY " +
				"es_restriccion, simbolo, codigo";

	/**
	 * String que contiene la consulta de las restricciones que estan en la base de datos
	 */
	private static String listadoRestriccionesCategoriaStr="SELECT codigoRestriccion AS codigo," +
			" descrrestriccion AS descripcion, " +
			"text AS texto from restriccion_categoria";

	/**
	 * String que contiene la consulta de las restricciones que esten asociadas a la Persona dada
	 * y que tengan en fecha fin null
	 */
	private static String consultarRestriccionesEnfermeraStr=
			"SELECT " +
				"er.codigoAsociacion AS codigoAsociacion, " +
				"er.codigoMedico AS codigoMedico, " +
				"res.codigoRestriccion AS codigo, " +
				"res.descrrestriccion AS descripcion, " +
				"res.text AS texto, " +
				"er.valorrestriccion AS valor, " +
				"true AS es_restriccion," +
				"null AS todos_dias " +
			"FROM " +
				"restriccion_enfermera res " +
			"INNER JOIN " +
				"enfermera_restriccion er " +
			"ON" +
				"(er.codigoRestriccion=res.codigorestriccion AND er.codigoMedico=?) " +
			"WHERE fecha_fin IS NULL " +
			"UNION " +
			"SELECT " +
				"etr.codigo AS codigoAsociacion, " +
				"etr.medico AS codigoMedico, " +
				"etr.turno AS codigo, " +
				"tur.descripcion AS descripcion, " +
				"false AS texto, " +
				"etr.valor AS valor," +
				"false AS es_restriccion, " +
				"todos_dias AS todos_dias " +
			"FROM " +
				"enfermera_t_restriccion etr " +
			"INNER JOIN " +
				"turno tur " +
			"ON" +
				"(tur.codigoturno=etr.turno AND etr.medico=?) " +
			"WHERE fecha_fin IS NULL";
	
	
	/**
	 * String que contiene la consulta SQL para asociar una restricción a una Persona
	 */
	private static String insertarRestriccionEnfermeraStr="INSERT INTO enfermera_restriccion (codigoAsociacion, codigorestriccion, codigoMedico, fecha_inicio, valorrestriccion) VALUES (?, ?, ?,CURRENT_DATE, ? )";

	/**
	 * String que contiene la consulta SQL para asociar una restriccion a una Persona
	 */
	private static String insertarRestriccionTurnoEnfermeraStr="INSERT INTO enfermera_t_restriccion (codigo, turno, medico, fecha_inicio, valor, todos_dias) VALUES (?, ?, ?, CURRENT_DATE, ?, ?)";

	
	
	/**
	 * String que contiene la consulta SQL para actualizar a la fecha actual, la finalización de la 
	 * asociación de la restricción a esa Persona
	 */	
	private static String actualizarRestriccionEnfermeraStr="UPDATE enfermera_restriccion SET fecha_fin=CURRENT_DATE WHERE (codigoRestriccion= ? AND codigoMedico=? AND fecha_fin IS null)";
	
	/**
	 * String que contiene la consulta SQL para actualizar a la fecha actual, la finalización de la 
	 * asociación de la restricción a esa Persona
	 */	
	private static String actualizarRestriccionTurnoEnfermeraStr="UPDATE enfermera_t_restriccion SET fecha_fin=CURRENT_DATE WHERE (turno=? AND medico=? AND fecha_fin IS null)";
		
	
	
	/**
	 * Muestra todas las restricciones que se encuentran en el sistema
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static Collection consultarRestricciones(Connection con, int tipo) 
	{
	    try {
	    	if(tipo==SiEsConstantes.TIPO_RESTRICCION_ENFERMERA)
			{
	    		//System.out.println("listadoRestriccionesEnfermeraStr "+listadoRestriccionesEnfermeraStr);
	    		PreparedStatement consultarRestriccionesStatement = con.prepareStatement(listadoRestriccionesEnfermeraStr);
	    		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarRestriccionesStatement.executeQuery()));
			}
	    	else if(tipo==SiEsConstantes.TIPO_RESTRICCION_CATEGORIA)
	    	{
	    		PreparedStatement consultarRestriccionesStatement = con.prepareStatement(listadoRestriccionesCategoriaStr);
	    		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarRestriccionesStatement.executeQuery()));
	    	}
	    	logger.error("No especificó un tipo tipo de consulta válido");
		}
	    catch (SQLException e)
		{
			logger.error("Error consultando el listado de las restricciones "+e);
		}
		return null;
	}
	
	
	

	/**
	 * Método que retorna un Collection con las restricciones que se encuentra asociadas a la
	 * persona con el código dado y que tienen null en fecha fin
	 * @param con Conexión con la BD
	 * @param codigo Código de la persona a consultar
	 * @return
	 */
	public static Collection consultarRestriccionEnfermera(Connection con, int codigo) 
	{
	    try
	    {
			PreparedStatement consultarRestriccionesEnfermeraStatement=con.prepareStatement(consultarRestriccionesEnfermeraStr);
			consultarRestriccionesEnfermeraStatement.setInt(1,codigo);
			consultarRestriccionesEnfermeraStatement.setInt(2,codigo);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarRestriccionesEnfermeraStatement.executeQuery()));
		}
	    catch (SQLException e)
	    {
		    logger.error("Error consultando las restricciones de la persona con código: "+codigo+": "+e);
		}
		return null;
	
	}

	/**
	 * (Juan David)
	 * Método para Ingresar una restricción de una Persona
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoMedico
	 * @param valor
	 * @param esRestriccion
	 * @param todosDias
	 * @return
	 */
	public static int insertarRestriccionPersona(Connection con, int codigoRestriccion, int codigoMedico, String valor, boolean esRestriccion, boolean todosDias)
	{
	    try
		{
	    	//System.out.println("codigoAsociacion "+codigoAsociacion);
			PreparedStatement insertarResEnfSt;
			if(esRestriccion)
			{
				insertarResEnfSt=con.prepareStatement(insertarRestriccionEnfermeraStr);
				insertarResEnfSt.setInt(1,UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_enfermera_restriccion"));
				//System.out.println(insertarRestriccionEnfermeraStr);
			}
			else
			{
				insertarResEnfSt=con.prepareStatement(insertarRestriccionTurnoEnfermeraStr);
				//System.out.println(insertarRestriccionTurnoEnfermeraStr);
				insertarResEnfSt.setBoolean(5, todosDias);
				insertarResEnfSt.setInt(1,UtilidadSiEs.obtenerSiguienteValorSecuencia(con, "seq_enfermera_t_restriccion"));
			}
			insertarResEnfSt.setInt(2,codigoRestriccion);
			insertarResEnfSt.setInt(3,codigoMedico);
			insertarResEnfSt.setString(4,valor);
			
			return insertarResEnfSt.executeUpdate();
		} catch (SQLException e)
		{
			logger.error("Error insertando la Restricción en la Persona: "+e);
			return 0;
		}	    
	}

	/**
	 * Metodo ejecuta la consulta de actualización de la restricción a la Persona, donde la fecha de 
	 * finalización de la asociación de convierte a la fecha actual del sistema
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoMedico
	 * @param esRestriccion
	 * @return
	 */
	public static int actualizarRestriccionEnfermera(Connection con, int codigoRestriccion, int codigoMedico, boolean esRestriccion)
	{
	    try
		{
			PreparedStatement actualizarRestriccionEnfermeraSt;
			if(esRestriccion)
			{
				actualizarRestriccionEnfermeraSt=con.prepareStatement(actualizarRestriccionEnfermeraStr);
			}
			else
			{
				actualizarRestriccionEnfermeraSt=con.prepareStatement(actualizarRestriccionTurnoEnfermeraStr);
			}
			actualizarRestriccionEnfermeraSt.setInt(1,codigoRestriccion);
			actualizarRestriccionEnfermeraSt.setInt(2,codigoMedico);
			
			return actualizarRestriccionEnfermeraSt.executeUpdate();
		}
	    catch (SQLException e)
		{
			logger.error("Error actualizando la Restricción en la Persona: "+e);
			return 0;
		}	    
	}
}
