/*
 * Created on 01-abr-2004
 *
 * To change the template for this generated file go to
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;


/**
 * @author juanda
 *
 */
public class SqlBaseAntecedentesFamiliaresDao
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAntecedentesFamiliaresDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar 
	 * una entrada en la tabla ant_familiares_tipos (Esto corresponde a un
	 * antecedente con un nombre predefinido en la bd).
	*/
	private static final String insertarFamiliaresTiposStr = "INSERT INTO ant_familiares_tipos " +
																					"(codigo_paciente,  " +
																					"tipo_enfermedad_familiar, " +
																					"observaciones, " +
																					"parentesco) " +
																					"VALUES (?,?,?,?)";

	
	private static final String consultarAntecedentePacienteStr = "SELECT * " +
																									"FROM antecedentes_pacientes " +
																									"WHERE codigo_paciente = ? ";

	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes " +
																								"(codigo_paciente)" +
																								"VALUES (?)";

	@SuppressWarnings("unused")
	private static final String cargarFamiliaresTiposStr = "SELECT "+ 
		"a.tipo_enfermedad_familiar As tipo_enfermedad_familiar, "+
		"t.nombre As nombre, "+ 
		"a.observaciones As observaciones, "+ 
		"a.parentesco "+ 
		"FROM ant_familiares_tipos a "+ 
		"INNER JOIN tipos_enf_familiares t ON(a.tipo_enfermedad_familiar=t.codigo) "+  
		"WHERE a.codigo_paciente = ?";

	private static final String cargarFamiliaresOtros = "SELECT codigo, " +
																				"nombre, " +
																				"observaciones, " +
																				"parentesco,  " +
																				"fecha,  " +
																				"hora  " +
																				"FROM  ant_familiares_otros  " +
																				"WHERE codigo_paciente=? ";

	private static final String cargarFamiliaresStr = "SELECT observaciones, fecha, hora " +
																			"FROM  antecedentes_familiares  " +
																			"WHERE codigo_paciente = ? ";


	public static int insertarFamiliaresTipos(Connection con, int codigoPaciente, int tipoEnfFamiliar, String observaciones, String parentesco) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
		try
		{		
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}

			pst = con.prepareStatement(insertarFamiliaresTiposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);

			pst.setInt(1, codigoPaciente);
			pst.setInt(2,tipoEnfFamiliar);
			pst.setString(3, observaciones);
			pst.setString(4, parentesco);			
			resp=pst.executeUpdate();

		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarPatologias", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}		
		return resp;
	}

	public static int insertarFamiliaresOtros(Connection con, int codigoPaciente, int codigo, String nombre, String observaciones, String parentesco) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
		try
		{		
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			String insertarFamiliaresOtrosStr = "INSERT INTO ant_familiares_otros " +
			"(codigo_paciente,  " +
			"codigo," +
			"nombre, " +
			"observaciones, " +
			"parentesco, " +
			"fecha, " +
			"hora) " +
			"VALUES (?,?,?,?,?,'"+Utilidades.capturarFechaBD()+"','"+UtilidadFecha.getHoraActual()+"')";
			
			pst= con.prepareStatement(insertarFamiliaresOtrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);

			pst.setInt(1, codigoPaciente);
			pst.setInt(2,codigo);
			pst.setString(3, nombre);
			pst.setString(4, observaciones);
			pst.setString(5, parentesco);
			
			resp=pst.executeUpdate();

		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR insertarFamiliaresOtros", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}	
		return resp;
	}
	
	/**
	* Implementación de la inserción de antecedentes alergias para una BD
	* Genérica (Transaccionalidad se debe manejar en capa más arriba)
	* @see com.princetonsa.dao.
	* AntecedentesAlergiasDao#insertarAntecedentesAlergias(con, int, String,
	* String, String, String, String, String, String, String, int, int, int,
	* int, String, String)
	*/
	public static int insertarFamiliares(Connection con, int codigoPaciente, String observacionesGenerales) throws SQLException
	{
		if (con == null || con.isClosed()) 
		{				
			throw new SQLException ("Error SQL: Conexión cerrada");
		}

		if(existeAntecedentePaciente(con, codigoPaciente))
		{
			try
			{
				String insertarFamiliaresStr = "INSERT INTO antecedentes_familiares " +
				"(codigo_paciente,  " +
				"observaciones, fecha, hora) " +
				"VALUES ( ? , ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"' )";
				
				PreparedStatementDecorator InsertarAntecedentesFamiliares= new PreparedStatementDecorator(con.prepareStatement(insertarFamiliaresStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				InsertarAntecedentesFamiliares.setInt(1,codigoPaciente);
				InsertarAntecedentesFamiliares.setString(2,observacionesGenerales);
				int resp= InsertarAntecedentesFamiliares.executeUpdate();
				InsertarAntecedentesFamiliares.close();
				return resp;
			}		
			catch (SQLException sql)
			{
				logger.warn("Error en insertar familiares"+sql.toString());
				throw sql;
			}					
		}
		return 0;
	}
	
	/**
	 * Consulta y/o inserción de antecedentes pacientes (Transaccionalidad se
	 * debe manejar en capa más arriba)
	 */
	private static boolean existeAntecedentePaciente(Connection con, int codigoPaciente) throws SQLException
	{			
		PreparedStatement consultarAntecedentePaciente = null;
		PreparedStatement insertarAntecedentePaciente = null;
		ResultSet rs=null;
		boolean resp = true;
		
		try 
		{		
			consultarAntecedentePaciente= new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedentePaciente= new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarAntecedentePaciente.setInt(1, codigoPaciente);
			rs = consultarAntecedentePaciente.executeQuery(); 	
			
			if (!rs.next())
			{										
				try 
				{				
					insertarAntecedentePaciente.setInt(1, codigoPaciente);
															
					resp = (insertarAntecedentePaciente.executeUpdate() == 1)? true : false;
				}
				catch (SQLException sql)
				{
					logger.warn("error insertando el paciente en la tabla ant_pac"+ sql.toString());
					throw sql;
				}
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarPatologias", e);
	    }
	    finally{
			try{
				if(insertarAntecedentePaciente != null){
					insertarAntecedentePaciente.close();
				}
				if(consultarAntecedentePaciente != null){
					consultarAntecedentePaciente.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}	
		return resp;
	}

	public static ResultSetDecorator cargarFamiliaresTipos(Connection con, int codigoPaciente)throws SQLException
	{
		if (con == null || con.isClosed()) 
		{			
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		
		try	
		{
			String cad = " SELECT aft.tipo_enfermedad_familiar, tipos.nombre as nombre,					 " +
						 " 		  aft.observaciones as observaciones, aft.parentesco as parentesco,		 " +
						 " 		  aft.fecha as fecha, aft.hora as hora			 						 " +
				   		 "		  FROM tipos_enf_familiares tipos										 " +
				   		 " 			   LEFT OUTER JOIN ant_familiares_tipos aft 						 " +
				   		 "			    ON  ( aft.tipo_enfermedad_familiar = tipos.codigo AND aft.codigo_paciente = "  + codigoPaciente +  " ) "; 
			
			//PreparedStatementDecorator cargarAntecedentesFamiliaresTiposStatement= new PreparedStatementDecorator(con.prepareStatement(cargarFamiliaresTiposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			PreparedStatementDecorator cargarAntecedentesFamiliaresTiposStatement= new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			//cargarAntecedentesFamiliaresTiposStatement.setInt(1,codigoPaciente);
													
			ResultSetDecorator  familiaresTipos_rs=new ResultSetDecorator(cargarAntecedentesFamiliaresTiposStatement.executeQuery());
			return familiaresTipos_rs;
									
		}
		catch(SQLException e)
		{
			logger.warn(e.getStackTrace());
			throw e;
		}		
	}
	
	public static ResultSetDecorator cargarFamiliaresOtros(Connection con, int codigoPaciente)throws SQLException
	{
		if (con == null || con.isClosed()) 
		{
			throw new SQLException ("Error SQL: Conexión cerrada");
		}	
						
		try	
		{
			PreparedStatementDecorator cargarAntecedentesFamiliaresOtrosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarFamiliaresOtros,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			cargarAntecedentesFamiliaresOtrosStatement.setInt(1,codigoPaciente);
									
			ResultSetDecorator  familiaresOtros_rs=new ResultSetDecorator(cargarAntecedentesFamiliaresOtrosStatement.executeQuery());
			return familiaresOtros_rs;
									
		}
		catch(SQLException e)
		{
			logger.warn(e.getStackTrace());
			throw e;
		}
	}
	
	public static ResultSetDecorator cargarFamiliares(Connection con, int codigoPaciente)throws SQLException
	{
		if (con == null || con.isClosed()) 
		{
			throw new SQLException ("Error SQL: Conexión cerrada");
		}					
		try	
		{
			PreparedStatementDecorator cargarAntecedentesFamiliaresStatement= new PreparedStatementDecorator(con.prepareStatement(cargarFamiliaresStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			cargarAntecedentesFamiliaresStatement.setInt(1,codigoPaciente);
									
			ResultSetDecorator  familiares_rs=new ResultSetDecorator(cargarAntecedentesFamiliaresStatement.executeQuery());
			return familiares_rs;
									
		}
		catch(SQLException e)
		{
			logger.warn(e.getStackTrace());
			throw e;
		}
	}

	public static int modificarFamiliaresTipos(Connection con, int codigoPaciente, int tipoEnfFamiliar, String observaciones) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
		try
		{	
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}

			String modificarFamiliaresTiposStr = "UPDATE ant_familiares_tipos  " +
			"SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' " +
			"WHERE codigo_paciente = ? " +
			"AND tipo_enfermedad_familiar = ? ";
			
			pst= con.prepareStatement(modificarFamiliaresTiposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, observaciones);
			pst.setInt(2, codigoPaciente);
			pst.setInt(3, tipoEnfFamiliar);

			resp=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarFamiliaresTipos", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}

	public static int modificarFamiliaresOtros(Connection con, int codigoPaciente, int codigo, String observaciones) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}

			String modificarFamiliaresOtrosStr =  "UPDATE ant_familiares_otros  " +
			"SET observaciones = ? " +
			",fecha='"+Utilidades.capturarFechaBD()+
			"', hora='"+UtilidadFecha.getHoraActual()+"'"+
			"WHERE  codigo_paciente=? " +
			"AND codigo=? ";
			
			pst= con.prepareStatement(modificarFamiliaresOtrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, observaciones);
			pst.setInt(2, codigoPaciente);
			pst.setInt(3, codigo);

			resp=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarFamiliaresOtros", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}

	public static int modificarFamiliares(Connection con, int codigoPaciente, String observacionesGenerales) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}

			String modificarFamiliaresStr = "UPDATE antecedentes_familiares  " +
			"SET observaciones=?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' " +
			"WHERE codigo_paciente = ? ";

			
			pst= con.prepareStatement(modificarFamiliaresStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);			
			pst.setInt(2, codigoPaciente);
			pst.setString(1, observacionesGenerales);
							
			resp= pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarFamiliares", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}	
		return resp;
	}

}
