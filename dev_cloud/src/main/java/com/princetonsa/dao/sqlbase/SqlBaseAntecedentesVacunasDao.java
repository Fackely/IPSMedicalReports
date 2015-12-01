
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;

/**
 * SqlBaseAntecedentesVacunasDao
 */
@SuppressWarnings("rawtypes")
public class SqlBaseAntecedentesVacunasDao {

	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseAntecedentesVacunasDao.class);

	
	/**
	 * Funcion para cosultar informacion 
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public static HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		int nroConsulta = 0, codigoPaciente=0; 
		String consulta = "";
		
		if ( !UtilidadCadena.noEsVacio(mapaParam.get("nroConsulta")+"") ) 
			{
			return new HashMap(); 
			}
		else 
			{ 
			nroConsulta = Integer.parseInt(mapaParam.get("nroConsulta")+""); 
			}
		
		if ( !UtilidadCadena.noEsVacio(mapaParam.get("codigoPaciente")+"") ) 
		{
			return new HashMap(); 
		}
		else 
		{ 
			codigoPaciente = Integer.parseInt(mapaParam.get("codigoPaciente")+""); 
		}
		
		switch (nroConsulta)
		{
			case 1:  //---Consultar los tipos de vacunas (tipos_inmunizacion) 
			{
				consulta = "  SELECT codigo as codigo_tipo, nombre as nombre_tipo, numero_dosis as numero_dosis, numero_refuerzos as numero_refuerzos " +
						   "		 FROM tipos_inmunizacion " +
						   "			  ORDER BY codigo";
			}
			break;
			case 2:  //---Consultar las observaciones de los antecedentes de vacunas del paciente 
			{
				consulta = " SELECT observaciones, fecha, hora FROM antecedentes_vacunas " +
							"	WHERE codigo_paciente="+codigoPaciente;
			}
			break;
			case 3:  //---Consultar las dosis,refuerzos y comentarios de las vacunas del paciente 
			{
				consulta = " SELECT * FROM " + 
					 		"	(" +
					 		"	SELECT apid.tipo_inmunizacion AS tipo_inmunizacion, apid.numero_dosis||'' AS numero_dosis, apid.fecha AS texto, apid.fecha_grabacion as fecha, apid.hora_grabacion as hora " + 
							"		FROM ant_ped_inmu_dosis apid " +
							"			WHERE apid.codigo_paciente=" +codigoPaciente+"  "+ 
							"	UNION ALL" +
							" 	SELECT apio.tipo_inmunizacion AS tipo_inmunizacion, '-2' AS numero_dosis, apio.observacion AS texto, apio.fecha_grabacion as fecha, apio.hora_grabacion as hora " +
							"		FROM ant_ped_inmu_obser apio " +
							"			WHERE apio.codigo_paciente=" +codigoPaciente+
							"	)x " +
							"  ORDER BY x.tipo_inmunizacion";
			}
			break;
			 
			default :
			{
				logger.warn(" [ERROR] No esta indicando el número de la consulta, el rango normal es [1-3]"+ nroConsulta + "\n\n" );	
				return new HashMap();
			}
			
		}
		
		try
		{
			logger.info("consyulta->"+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarInformacion de SqlBaseAntecedentesVacunasDao: "+e); 
			return null;
		}
	}
	
	/**
	 * Método para insertar/modificar los antecedentes vacunas e insertar si es necesari
	 * en antecedentes pacientes
	 * @param con
	 * @param codigoPaciente
	 * @param observaciones
	 * @param esInsertar
	 * @return
	 */
	public static int insertarModificarAntecedenteVacuna(Connection con, int codigoPaciente, String observaciones, boolean esInsertar)
	{
		PreparedStatementDecorator ps;
		String consultaStr="";
		int resp=-1;
		
		try
		{
			if (esInsertar)
				{
				//-------Se verifica si existe antecedente paciente para insertar -------//
				if(!SqlBaseAntecedentesVariosDao.existeAntecedentePaciente(con, codigoPaciente))
				{
					resp=insertarAntecedentePaciente(con, codigoPaciente);
				}
				
				//--------Se inserta el antecedente de vacuna --------------------//
				consultaStr="INSERT INTO antecedentes_vacunas (codigo_paciente, observaciones) VALUES (?, ?)";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoPaciente);
				ps.setString(2, observaciones);
				}
			else
				{
				consultaStr="UPDATE antecedentes_vacunas SET observaciones=? " +
							"	 WHERE codigo_paciente = ? ";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, observaciones);
				ps.setInt(2, codigoPaciente);
				
				}
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/modificación de datos en antecedentesVacunas : SqlBaseAntecedentesVacunasDao "+e.toString() );
				resp = -1;
		}
		return resp;	
	}
	
	/**
	 * Método que insertar el antecedente paciente
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static int insertarAntecedentePaciente(Connection con, int paciente)
		{
		String insertarStr="INSERT INTO antecedentes_pacientes (codigo_paciente) VALUES (?)";
		try
		{
			PreparedStatementDecorator insertarAntecedentePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedentePacienteStatement.setInt(1, paciente);
			return insertarAntecedentePacienteStatement.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error insertando antecedente paciente en SqlAntecedentesVacunas: "+e);
			return -1;
		}
	}
	
	/**
	 * Método que inserta la aclaración de la dosis o el refuerzo de la vacuna, ya que se insertan
	 * en la misma tabla, sino que el refuerzo se inserta -1 en numero_dosis
	 * @param con
	 * @param codigoPaciente
	 * @param codigoTipoInmunizacion 
	 * @param dosis (Si es igual -1 es refuerzo sino es una aclaración de dosis)
	 * @param texto (Puede ser la aclaración o el refuerzo)
	 * @param loginUsuario
	 * @param datosMedico
	 * @return
	 */
	public static int insertarDosisRefuerzo(Connection con, int codigoPaciente, int codigoTipoInmunizacion, int dosis, String texto, String loginUsuario, String datosMedico)
	{
		PreparedStatementDecorator ps;
		int resp=-1;
		String cad = "";
						
		try
			{
				cad = " INSERT INTO ant_ped_inmu_dosis (codigo_paciente, tipo_inmunizacion, numero_dosis, fecha, login_usuario, datos_medico, fecha_grabacion, hora_grabacion) " +
					  "				VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoPaciente);				
				ps.setInt(2, codigoTipoInmunizacion);
				ps.setInt(3, dosis);
				ps.setString(4, texto);
				ps.setString(5, loginUsuario);
				ps.setString(6, datosMedico);
													
				resp = ps.executeUpdate();
					
			}
		catch(SQLException e)
			{
					logger.warn(" Error en la inserción de Dosis/Refuerzo : SqlBaseAntecedentesVacunasDao "+e.toString() );
					resp = -1;
			}
			return resp;
	}
	
	/**
	 * Método que inserta o modifica el comentario agragado a la vacuna
	 * @param con
	 * @param codigoPaciente
	 * @param codigoTipoInmunizacion
	 * @param comentarioVacuna
	 * @param loginUsuario
	 * @param datosMedico
	 * @return
	 */
	public static int insertarModificarComentarioVacuna(Connection con, int codigoPaciente, int codigoTipoInmunizacion, String comentarioVacuna, String loginUsuario, String datosMedico)
	{
		PreparedStatementDecorator ps;
		String consultaStr="";
		int resp=-1;
		
		try
		{
			//-------Se verifica si existe comentarios para la vacuna del paciente -------//
				if(!existeComentarioVacunaPaciente (con, codigoPaciente, codigoTipoInmunizacion))
				{
				//--------Se inserta el antecedente de vacuna --------------------//
				consultaStr="INSERT INTO ant_ped_inmu_obser (codigo_paciente, tipo_inmunizacion, observacion, login_usuario, datos_medico, fecha_grabacion, hora_grabacion) " +
							"		VALUES (?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoPaciente);
				ps.setInt(2, codigoTipoInmunizacion);
				ps.setString(3, comentarioVacuna);
				ps.setString(4, loginUsuario);
				ps.setString (5, datosMedico);
				
				}
			else
				{
				consultaStr="UPDATE ant_ped_inmu_obser SET observacion=?, login_usuario=?, datos_medico=?, fecha_grabacion = CURRENT_DATE, hora_grabacion = "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" " +
							"	 WHERE codigo_paciente = ? AND tipo_inmunizacion = ? ";
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, comentarioVacuna);
				ps.setString(2, loginUsuario);
				ps.setString (3, datosMedico);
				ps.setInt(4, codigoPaciente);
				ps.setInt(5, codigoTipoInmunizacion);
				}
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/modificación de datos en antecedentesVacunas : SqlBaseAntecedentesVacunasDao "+e.toString() );
				resp = -1;
		}
		return resp;	
	}

	/**
	 * Método que verifica si existe observación para la vacuna del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoTipoInmunizacion
	 * @return
	 */
	private static boolean existeComentarioVacunaPaciente(Connection con, int codigoPaciente, int codigoTipoInmunizacion) 
	{
		String consultaStr="SELECT count(1) AS numResultados from ant_ped_inmu_obser " +
						   "		WHERE codigo_paciente=? AND tipo_inmunizacion = ?";

		try
		{
		if(con==null || con.isClosed())
		{
		con=UtilidadBD.abrirConexion();
		}
		} catch (SQLException e1)
		{
		logger.warn("No se pudo realizar la conexión (SqlBaseAntecedentesVacunasDao)"+e1.toString());
		}
		
		try
		{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setInt(1, codigoPaciente);
		ps.setInt(2, codigoTipoInmunizacion);
		
		ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
		if(resultado.next())
		{
		return resultado.getInt("numResultados") > 0;
		}
		else
		{
		return false;
		}
		}
		catch (SQLException e)
		{
		logger.error("Error en existeComentarioVacunaPaciente SqlBaseAntecedentesVacunasDao : "+e);
		return false;
		}
	}
}
