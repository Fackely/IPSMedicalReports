/*
 * Created on 31-mar-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;

/**
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseAntecedentesVariosDao
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAntecedentesVariosDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar un antecedente vario
	 */
	private static final String modificarAntecedenteVarioStr="update antecedentes_varios set descripcion=?, login_usuario =? where codigo=? and codigo_paciente=?" ;

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar en la tabla guia (antecedentes_paciente) una entrada, en una base de datos Genérica.
	 */
	private static final String insertarAntecedentePacienteStr="insert into antecedentes_pacientes (codigo_paciente) values(?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para seleccionar un antecedente vario
	 */
	private static final String seleccionarAntecedenteVarioStr="SELECT av.codigo   AS codigo_i,  av.tipo          AS codigoTipo_i,  a.descripcion         AS tipo_s,  av.descripcion   AS descripcion_s,  av.fecha         AS fecha_d,  av.hora          AS hora_t,  av.login_usuario AS usuario_s  FROM antecedentes_varios av,  antecedentes a WHERE av.codigo = ? AND av.tipo = a.id" ;
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para mirar si existe una entrada en la tabla antecedentes_pacientes para esta paciente en particular
	 */
	private static final String existeAntecedentePacienteStr="SELECT count(1)  as numResultados from antecedentes_pacientes where codigo_paciente=?";

	/**
	* Implementaci&oacute;n de modificar un Antecedente Vario para una Base de datos Genérica	 
	* @see com.princetonsa.dao.AntecedentesVariosDao#modificar(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static int modificar(Connection con,int idAntecedente,String descripcion,String usuario,PersonaBasica paciente)	throws SQLException {
		PreparedStatementDecorator modificarStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarAntecedenteVarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarStatement.setString(1,descripcion);
		modificarStatement.setString(2,usuario);
		modificarStatement.setInt(3,idAntecedente);
		modificarStatement.setInt(4,paciente.getCodigoPersona());
		return modificarStatement.executeUpdate(); 		
	}

	public static int insertarAntecedentePaciente(Connection con, PersonaBasica paciente){
		try{
			PreparedStatementDecorator insertarAntecedentePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedentePacienteStatement.setInt(1, paciente.getCodigoPersona());
			return insertarAntecedentePacienteStatement.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error insertando antecedente persona: "+e);
			return 0;
		}
	}
	
	/**
	 * Implementaci&oacute;n de cargar un Antecedente Vario para una Base de datos Genérica
	 * @see com.princetonsa.dao.AntecedentesVariosDao#cargar(java.sql.Connection, int)
	 */
	public static ResultSetDecorator cargar(Connection con, int idAntecedente) throws SQLException {
		PreparedStatementDecorator cargarStatement =  new PreparedStatementDecorator(con.prepareStatement(seleccionarAntecedenteVarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarStatement.setInt(1,idAntecedente);
		return new ResultSetDecorator(cargarStatement.executeQuery());
	}

	public static boolean existeAntecedentePaciente(Connection con, int codigoPaciente) throws SQLException
	{
		PreparedStatementDecorator existeAntecedentePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(existeAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeAntecedentePacienteStatement.setInt(1, codigoPaciente);

		ResultSetDecorator rs=new ResultSetDecorator(existeAntecedentePacienteStatement.executeQuery());
		if (rs.next())
		{

			if (rs.getInt("numResultados")<1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			throw new SQLException ("Error Desconocido en la busqueda de un Antecedente general para una paciente");
		}
	}
	/** 
	 * Implementaci&oacute;n de insertar un Antecedente Vario para una Base de datos Genérica
	 * @see com.princetonsa.dao.AntecedentesVariosDao#insertar(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public static int insertar(Connection con,int tipo,String descripcion,String login,PersonaBasica paciente, String insertarAntecedenteVarioStr) throws SQLException {
		int resp=0;
		con = verificarConexion(con);

		//Si este paciente NO tiene antecedentes previos (de cualquier tipo)
		//debemos insertar una entrada en la tabla guia antecedentes_paciente
		if (!existeAntecedentePaciente(con,paciente.getCodigoPersona()))
		{
			resp=SqlBaseAntecedentesVariosDao.insertarAntecedentePaciente(con, paciente);
		}
		
		PreparedStatementDecorator insertarAntecedenteStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedenteVarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarAntecedenteStatement.setInt(1, tipo);
		insertarAntecedenteStatement.setString(2, descripcion);
		insertarAntecedenteStatement.setString(3, login);
		insertarAntecedenteStatement.setInt(4,paciente.getCodigoPersona());
	
		resp =  insertarAntecedenteStatement.executeUpdate();
		
		if (resp<1)
		{
			return 0; 
		}
		else
		{
			PreparedStatementDecorator buscarCodigoAntecedenteStatement= new PreparedStatementDecorator(con.prepareStatement("SELECT max(codigo) as codigoAntecedente from antecedentes_varios where tipo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarCodigoAntecedenteStatement.setInt(1, tipo);
			
			ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoAntecedenteStatement.executeQuery());
			rs.next();			
			return rs.getInt("codigoAntecedente");
		}
		
	}
	/**
	 * Permite verificar si la conexion esta abierta.<br>
	 * Si no lo esta se crea una nueva 
	 * @param con Una referencia a a una conexi&oacute;n a la base de datos
	 * @return Una conexi&oacute;n activa con la base de datos
	 * @throws SQLException
	 */
	public static Connection verificarConexion(Connection con) throws SQLException{
		if (con == null || con.isClosed()) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			return  myFactory.getConnection();
		}		
		return con;
	}


}
