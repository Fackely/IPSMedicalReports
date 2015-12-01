/*
 * Created on Mar 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.comun.DtoDatosGenericos;
import com.princetonsa.mundo.antecedentes.CategoriaAlergia;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Antecedentes Alergias
 *
 *	@version 1.0, Mar 31, 2004
 */
public class SqlBaseAntecedentesAlergiasDao
{
	
	
		/**
		 * Cadena constante con el <i>statement</i> necesario para consultar si
		 * un paciente tiene un antecedente registrado en el sistema
		 */
		private static final String consultarAntecedentePacienteStr ="SELECT * FROM antecedentes_pacientes WHERE codigo_paciente = ? ";

		/**
		 * Cadena constante con el <i>statement</i> necesario para insertar un
		 * un paciente en la tabla antecedentes_pacientes, que contiene todos los
		 * pacientes con algun antecedente ingresado en el sistema.
		 */		
		private static final String insertarAntecedentePacienteStr ="INSERT INTO antecedentes_pacientes (" +"codigo_paciente) "+"VALUES (?)";	
		
		
		/**
		 * Cadena constante con el <i>statement</i> necesario para consultar 
		 * antecedentes alergias dado el tipo y numero de identificación de un paciente
		 */	
		private static final String consultarAntecedentesAlergiasStr = "" +
		"SELECT observaciones, fecha, hora " +
		"FROM antecedentes_alergias " +
		"WHERE codigo_paciente = ?";
		
		/**
		 * Cadena constante con el <i>statement</i> necesario para consultar 
		 * alergias predefinidas dado el tipo y numero de identificación de un paciente
		 */		
		private static final String consultarAlergiasPredefinidasStr = "" +
		"SELECT categoria_alergia as categoria, ca.nombre as nombreCategoria, tipo_alergia as tipo, ta.nombre as nombreTipo, observaciones, fecha, hora " +
		"FROM ant_alergias_tipos, tipos_alergias ta, categorias_alergias ca " +
		"WHERE codigo_paciente = ? AND ta.categoria_alergia = ca.codigo and ta.codigo = tipo_alergia order by ta.categoria_alergia, tipo_alergia";
		
		/**
		 * Cadena constante con el <i>statement</i> necesario para consultar 
		 * alergias adicionales dado el tipo y numero de identificación de un paciente
		 */	
		private static final String consultarAlergiasAdicionalesStr = "" +
		"SELECT codigo_categoria as categoria, ca.nombre as nombreCategoria, aao.codigo, aao.nombre as nombre, observaciones, aao.fecha as fecha, aao.hora as hora " +
		"FROM ant_alergias_otros aao,  categorias_alergias ca " +
		"WHERE codigo_paciente = ? AND aao.codigo_categoria = ca.codigo order by aao.codigo_categoria, aao.codigo";
		
		
		/**
		 * Implementación de la inserción de antecedentes alergias para
		 * una BD PostgresSQL o Hsqldb (Transaccionalidad se debe manejar en capa más arriba) 
		 * 
		 * @see com.princetonsa.dao.AntecedentesAlergiasDao#insertarAntecedentesAlergias(con, int, String, String, String, String, String, String, String, String, int, int, int, int, String, String)
		 */
		public static int insertarAntecedentesAlergias(Connection con, int codigoPaciente, String observaciones) throws SQLException
		{
			if (con == null || con.isClosed()) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");				
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			String insertarAntecedentesAlergiasStr = "" +
			"INSERT INTO antecedentes_alergias (codigo_paciente, observaciones, fecha, hora) " +
			"VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"','"+UtilidadFecha.getHoraActual()+"')";
			
			PreparedStatementDecorator insertarAntecedentesAlergiasStatement= new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesAlergiasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			insertarAntecedentesAlergiasStatement.setInt(1, codigoPaciente);
			insertarAntecedentesAlergiasStatement.setString(2, observaciones);
		
			//Si ya fue previamente insertado el paciente en la tabla antecedentes_pacientes, se intenta insertar 
			//el registro de antecedentes_alergias
			if(existeAntecedentePaciente(con,codigoPaciente)){
				try
				{																											
					int resp= insertarAntecedentesAlergiasStatement.executeUpdate();
					insertarAntecedentesAlergiasStatement.close();
					return resp;
				}		
				catch (SQLException sql)
				{
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
					logger.warn("Error insertando antecedente alergias para el paciente  "+codigoPaciente+" en la tabla 'antecedentes_alergias'.\nSe lanzó la siguiente excepción:\n"+sql);
					throw sql;
				}					
			}
			return 0;
		}

	/**
	 * Consulta y/o inserción de antecedentes pacientes
	 * (Transaccionalidad se debe manejar en capa más arriba) 
	 */	
	private static  boolean existeAntecedentePaciente(Connection con, int codigoPaciente) throws SQLException
	{
		/*Statement para consultar si el paciente ya tiene un antecedente registrado*/
		PreparedStatement consultarAntecedentePaciente = null;
		/*Statement para insertar en antecedentes_pacientes el paciente actual*/
		PreparedStatement insertarAntecedentePaciente =  null;
		ResultSet rs=null;
		boolean resp = true;
		
		try 
		{		
			consultarAntecedentePaciente= new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedentePaciente= new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarAntecedentePaciente.setInt  ( 1, codigoPaciente);
			//Consulto si el paciente ya tiene antecedente en el sistema
			rs = consultarAntecedentePaciente.executeQuery(); 	
			//Si no tiene antecedente antes se debe insertar
			if (!rs.next() )
			{									
				try 
				{				
					insertarAntecedentePaciente.setInt  ( 1, codigoPaciente);
					resp = (insertarAntecedentePaciente.executeUpdate() == 1)? true : false;
				}
				catch (SQLException sql)
				{
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
					logger.warn("Error insertando el paciente  "+codigoPaciente+" en la tabla 'antecedentes_pacientes'.\n Se lanzó la siguiente excepción:\n"+sql);
					throw sql;
				}
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR existeAntecedentePaciente", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(consultarAntecedentePaciente != null){
					consultarAntecedentePaciente.close();
				}
				if(insertarAntecedentePaciente != null){
					insertarAntecedentePaciente.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}			
		return resp;
	}

	/**
	 * Implementación de la inserción de alergias predefinidas para
	 * una BD PostgresSQL o Hsqldb (Transaccionalidad se maneja en capa más arriba) 
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#insertarAlergiasPredef(con, String, String, int, String)
	 */	
	public static int insertarAlergiasPredef(Connection con, int codigoPaciente, int codigo, String observaciones ) throws SQLException
	{
		int resp=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		
		try
		{	
			if (con == null || con.isClosed()) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			/**
			 * Cadena constante con el <i>statement</i> necesario para insertar 
			 * alergias predefinidas a un paciente
			 */
			String insertarAlergiasPredefinidasStr ="" +
			"INSERT INTO ant_alergias_tipos (codigo_paciente, tipo_alergia, observaciones, fecha, hora) " +
			"VALUES (?, ?, ?, '"+Utilidades.capturarFechaBD()+"','"+UtilidadFecha.getHoraActual()+"')";
			
			pst= con.prepareStatement(insertarAlergiasPredefinidasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigo);
			pst.setString(3, observaciones);			
			resp=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR insertarAlergiasPredef", e);
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
	 * Implementación de la inserción de alergias adicionales para
	 * una BD PostgresSQL o Hsqldb (Transaccionalidad se maneja en capa más arriba) 
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#insertarAlergiasAdic(con, String, String, int, int, String)
	 */	
	public static int insertarAlergiasAdic(Connection con, int codigoPaciente, int categoria, int codigo, String nombre, String observaciones ) throws SQLException
	{
		int resp=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
			try
			{	
				if (con == null || con.isClosed()) 
				{
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
					logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				/**
				 * Cadena constante con el <i>statement</i> necesario para insertar 
				 * alergias adicionales a un paciente
				 */
				String insertarAlergiasAdicionalesStr ="" +
				"INSERT INTO ant_alergias_otros (codigo_paciente, codigo_categoria, codigo, nombre, observaciones, fecha, hora) " +
				"VALUES (?, ?, ?, ?, ?, '"+Utilidades.capturarFechaBD()+"','"+UtilidadFecha.getHoraActual()+"')";
				
				pst= con.prepareStatement(insertarAlergiasAdicionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						
				pst.setInt(1, codigoPaciente);
			
				pst.setInt(2, categoria);
				pst.setInt(3, codigo);					
				pst.setString(4, nombre);				
				pst.setString(5, observaciones);						
																								
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

	/**
	 * Implementación de la carga de antecedentes alergias en una BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAntecedentesAlergias(con, String, String)
	 */
	public static ResultSetDecorator cargarAntecedentesAlergias (Connection con, int codigoPaciente) throws SQLException
	{		
		if (con == null || con.isClosed()) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			try
			{			
				PreparedStatementDecorator cargarAntecedentesAlergiasStatement= new PreparedStatementDecorator(con.prepareStatement(consultarAntecedentesAlergiasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarAntecedentesAlergiasStatement.setInt(1, codigoPaciente);
				
				 return new ResultSetDecorator(cargarAntecedentesAlergiasStatement.executeQuery());	
			} 
			catch (SQLException sql) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("Error consultando antecedentes alergias para el paciente "+codigoPaciente+" en la tabla 'antecedentes_alergias'.\n Se lanzó la siguiente excepción:\n"+sql);
				throw sql;
			}
	}

	/**
		 * Implementación de la carga de alergias predefinidas en una BD PostgresSQL o Hsqldb
		 * 
		 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAlergiasPredef(con, String, String)
		 */	
		public static ResultSetDecorator cargarAlergiasPredef (Connection con, int codigoPaciente) throws SQLException
		{
			if (con == null || con.isClosed()) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			try
			{			
				PreparedStatementDecorator cargarAlergiasPredefinidasStatement= new PreparedStatementDecorator(con.prepareStatement(consultarAlergiasPredefinidasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarAlergiasPredefinidasStatement.setInt(1, codigoPaciente);
							
				return new ResultSetDecorator(cargarAlergiasPredefinidasStatement.executeQuery());
			} 
			catch (SQLException sql) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("Error consultando alergia predefinida para el paciente con "+codigoPaciente+" en la tabla 'ant_alergias_tipos'.\n Se lanzó la siguiente excepción:\n"+sql);
				throw sql;
			}		
		}
	
	/**
	 * Implementación de la carga de alergias adicionales en una BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAlergiasAdic(con, String, String)
	 */	
	public static ResultSetDecorator cargarAlergiasAdic (Connection con, int codigoPaciente) throws SQLException
	{
			if (con == null || con.isClosed()) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
		
			try
			{			
				PreparedStatementDecorator cargarAlergiasAdicionalesStatement= new PreparedStatementDecorator(con.prepareStatement(consultarAlergiasAdicionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarAlergiasAdicionalesStatement.setInt(1, codigoPaciente);
			
						
				return new ResultSetDecorator(cargarAlergiasAdicionalesStatement.executeQuery());
			} 
			catch (SQLException sql) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("Error consultando alergia adicional para el paciente con "+codigoPaciente+" en la tabla 'ant_alergias_otros'.\n Se lanzó la siguiente excepción:\n"+sql);
				throw sql;
			}				
	}
	
	/**
	 * Implementación de la actualizacion de los campos modificables de antecedentes alergias para
	 * una BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#modificarAntecedentesAlergias(con, String, String, String) 
	 */
	public static int modificarAntecedentesAlergias(Connection con, int codigoPaciente, String observaciones) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
			try
			{
				if (con == null || con.isClosed()) 
				{
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
					logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				/**
				 * Cadena constante con el <i>statement</i> necesario para modificar 
				 * antecedentes alergias dado el tipo y numero de identificación de un paciente
				 */	
				String modificarAntecedentesAlergiasStr = "" +
				"UPDATE antecedentes_alergias SET observaciones=?, " +
				"fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "+
				"WHERE codigo_paciente = ?";
				
				pst= con.prepareStatement(modificarAntecedentesAlergiasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(2, codigoPaciente);
			
				pst.setString(1, observaciones);
			
				resp=pst.executeUpdate();
			}
			catch(Exception e)
		    {
			    Log4JManager.error("ERROR modificarAntecedentesAlergias", e);
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
	 * Implementación de la actualizacion de los campos modificables de alergias predefinidas para
	 * una BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#modificarAlergiasPredef(con, String, String, int, String) 
	 */	
	public static int modificarAlergiasPredef(Connection con, int codigoPaciente, int codigo, String observaciones ) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
			try
			{
				if (con == null || con.isClosed()) 
				{
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
					logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
					throw new SQLException ("Error SQL: Conexión cerrada");
				}		
			

				/**
				 * Cadena constante con el <i>statement</i> necesario para modificar 
				 * alergias predefinidas dado el tipo y numero de identificación de un paciente
				 */	
				String modificarAlergiasPredefinidasStr ="" +
				"UPDATE ant_alergias_tipos SET observaciones = ?, " +
				"fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "+
				"WHERE codigo_paciente = ? AND tipo_alergia = ?";
				
				pst= con.prepareStatement(modificarAlergiasPredefinidasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(2, codigoPaciente);
			
				pst.setInt(3, codigo);			
				pst.setString(1, observaciones);
						
				resp=pst.executeUpdate();

			}
			catch(Exception e)
		    {
			    Log4JManager.error("ERROR modificarAlergiasPredef", e);
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
	 * Implementación de la actualizacion de los campos modificables de alergias adicionales para
	 * una BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#modificarAlergiasAdic(con, String, String, int, int, String) 
	 */	
	public static  int modificarAlergiasAdic(Connection con, int codigoPaciente, int categoria, int codigo, String nombre, String observaciones ) throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;			
			try
			{		
				if (con == null || con.isClosed()) 
				{	
					final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
					logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada" +nombre);
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				/**
				 * Cadena constante con el <i>statement</i> necesario para modificar 
				 * alergias adicionales dado el tipo y numero de identificación de un paciente
				 */		
				String modificarAlergiasAdicionalesStr ="" +
				"UPDATE ant_alergias_otros SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' " +
				"WHERE codigo_paciente = ? AND codigo_categoria = ? AND codigo = ?";
				
				pst= con.prepareStatement(modificarAlergiasAdicionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(2, codigoPaciente);
			
				pst.setInt(3, categoria);
				pst.setInt(4, codigo);							
				pst.setString(1, observaciones);
						
				resp=pst.executeUpdate();
			}
			catch(Exception e)
		    {
			    Log4JManager.error("ERROR modificarAlergiasAdic", e);
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
	 * Implementación de la carga de alergias adicionales en una BD PostgresSQL o Hsqldb
	 * 
	 * @see com.princetonsa.dao.AntecedentesAlergiasDao#cargarAlergiasAdic(con, String, String)
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<DtoDatosGenericos> cargarAlergiasAdicNoRs (Connection con, int codigoPaciente) throws SQLException
	{
		ArrayList<DtoDatosGenericos> listaResultado =  new ArrayList<DtoDatosGenericos>();
		PreparedStatementDecorator cargarAlergiasAdicionalesStatement=null; 
		ResultSetDecorator rs_aleAdic =null;
		try
		{	
			if (con == null || con.isClosed()) 
			{
				final Logger logger = Logger.getLogger(SqlBaseAntecedentesAlergiasDao.class);
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			cargarAlergiasAdicionalesStatement= new PreparedStatementDecorator(con.prepareStatement(consultarAlergiasAdicionalesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarAlergiasAdicionalesStatement.setInt(1, codigoPaciente);
		
			rs_aleAdic = new ResultSetDecorator(cargarAlergiasAdicionalesStatement.executeQuery());
			
			
			//----
			int codigoCategoriaAnterior = -2;
			boolean categoriaEncontrada = false;
			int indiceAlergia = -1;
			int codigoCategoria;
			ArrayList alergias = new ArrayList();
			CategoriaAlergia categoriaAlergia = new CategoriaAlergia();
			
			while(rs_aleAdic.next())
			{
				codigoCategoria = rs_aleAdic.getInt("categoria");
				
				if(codigoCategoriaAnterior != codigoCategoria)
				{
				codigoCategoriaAnterior = codigoCategoria;
				
					//Buscando la categoria de la alergia adicional
					for(int i=indiceAlergia+1; i<alergias.size(); i++)
					{
						if(((CategoriaAlergia)alergias.get(i)).getCodigo() == codigoCategoria)
						{
							indiceAlergia = i;
							i= alergias.size();	
							categoriaEncontrada = true;		
						}
					}
					//Si no se encontro la adiciono
					if(!categoriaEncontrada)
					{
						categoriaAlergia = new CategoriaAlergia(codigoCategoria, rs_aleAdic.getString("nombreCategoria"), new ArrayList(), new ArrayList());
						
						alergias.add(categoriaAlergia);
						indiceAlergia = alergias.size()-1; 						
					}
					categoriaEncontrada = false;
				}
				((CategoriaAlergia)alergias.get(indiceAlergia)).getTiposAlergiasAdicionales().add(new InfoDatosBD(rs_aleAdic.getInt("codigo"), rs_aleAdic.getString("nombre"), rs_aleAdic.getString("observaciones") ));
				
				
				DtoDatosGenericos datoGenericos = new DtoDatosGenericos();
				datoGenericos.setDato1(rs_aleAdic.getString("nombre"));
				datoGenericos.setDato2(rs_aleAdic.getString("nombre"));
				
				if(!UtilidadTexto.isEmpty(rs_aleAdic.getString("fecha")+"")){
					datoGenericos.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(rs_aleAdic.getString("fecha")+""));
				}
				if(!UtilidadTexto.isEmpty(rs_aleAdic.getString("hora"))){
					datoGenericos.setHora(rs_aleAdic.getString("hora"));
				}
				
				listaResultado.add(datoGenericos);
			}
			
			//----
			
			
		} 
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarPatologias", e);
	    }
	    finally{
			try{
				if(rs_aleAdic != null){
					rs_aleAdic.close();
				}
				if(cargarAlergiasAdicionalesStatement != null){
					cargarAlergiasAdicionalesStatement.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    return listaResultado;
	}
	
}
