/*
 * @(#)OracleAntecedentesVariosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ValoresPorDefecto;

import com.princetonsa.dao.AntecedentesVariosDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesVariosDao;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Clase que implementa toda la funcionalidad de los antecedentes varios con respecto a la Base de Datos Oracle
 *
 * @version 1.0, Apr 15, 2003
 * @author <a href="mailto:diego@PrincetonSA.com">Diego Andr&eacute;s Ram&iacute;rez Arag&oacute;n</a>
 */
public class OracleAntecedentesVariosDao implements AntecedentesVariosDao {//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un antecedente vario
	 */
	private static final String insertarAntecedenteVarioStr="insert into antecedentes_varios (codigo,fecha, hora,tipo, descripcion,  login_usuario,codigo_paciente) values (seq_antecedentes_varios.nextval, current_date, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?, ?,?,?)";
	
	/** 
	 * Implementaci&oacute;n de insertar un Antecedente Vario para una Base de datos Oracle
	 * @see com.princetonsa.dao.AntecedentesVariosDao#insertar(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int insertar(Connection con,int tipo,String descripcion,String login,PersonaBasica paciente) throws SQLException {
	    return SqlBaseAntecedentesVariosDao.insertar(con,tipo, descripcion, login, paciente, insertarAntecedenteVarioStr) ;
	}

	/**
	* Implementaci&oacute;n de modificar un Antecedente Vario para una Base de datos Oracle	 
	* @see com.princetonsa.dao.AntecedentesVariosDao#modificar(java.sql.Connection, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	public int modificar(Connection con,int idAntecedente,String descripcion,String usuario,PersonaBasica paciente)	throws SQLException {
		con = SqlBaseAntecedentesVariosDao.verificarConexion(con);
		return SqlBaseAntecedentesVariosDao.modificar(con, idAntecedente, descripcion, usuario, paciente);
	}

	/**
	 * Implementaci&oacute;n de cargar un Antecedente Vario para una Base de datos Oracle
	 * @see com.princetonsa.dao.AntecedentesVariosDao#cargar(java.sql.Connection, int)
	 */
	public ResultSetDecorator cargar(Connection con, int idAntecedente) throws SQLException
	{
		con = SqlBaseAntecedentesVariosDao.verificarConexion(con);
		return SqlBaseAntecedentesVariosDao.cargar(con, idAntecedente);
	}
	
	public boolean existeAntecedentePaciente(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesVariosDao.existeAntecedentePaciente(con, codigoPaciente);
	}
}