/*
 * @(#)SqlBaseRequisitosPacienteDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para los requisitos de paciente
 *
 * @version 1.0, Nov 22 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseRequisitosPacienteDao 
{
    
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseRequisitosPacienteDao.class);

	/**
	 * Consulta la info de los requisitos del paciente según la institución
	 */
	private final static String consultaStandarRequisitosPacienteStr= "SELECT " +
		"codigo, " +
		"descripcion, " +
		"CASE WHEN activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS activo, " +
		ValoresPorDefecto.getValorFalseParaConsultas() + " AS puedoborrar, " +
		"tipo_requisito " +
		"FROM requisitos_paciente WHERE institucion= ? ORDER BY descripcion ";
	
	/**
	 * Consulta la info de los requisitos de la radicacion según la institución
	 */
	private final static String consultaStandarRequisitosRadicacionStr= "SELECT " +
		"codigo, " +
		"descripcion, " +
		"CASE WHEN activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS activo, " +
		ValoresPorDefecto.getValorFalseParaConsultas() + " AS puedoborrar, " +
		"tipo_requisito " +
		"FROM requisitos_radicacion WHERE institucion= ? ORDER BY descripcion ";
	
	/**
	 * Hace la modificación de los datos de los requisitos del paciente
	 */
	private final static String modificarRequisitoPacienteStr=		"UPDATE requisitos_paciente SET " +
																							"descripcion = ?, tipo_requisito = ?, activo = ? WHERE codigo = ?";
	
	/**
	 * Hace la modificación de los datos de los requisitos de la radicación
	 */
	private final static String modificarRequisitoRadicacionStr=		"UPDATE requisitos_radicacion SET " +
																							"descripcion = ?,tipo_requisito = ?, activo = ? WHERE codigo = ?";
	
	/**
	 * Cadena constante con el <i>statement</i> 
	 * necesario para insertar un requisito para un
	 * convenio 
	 */
	private final static String insertarRequisitoPacienteConvenioStr="insert into requisitos_pac_convenio (requisito_paciente, convenio, via_ingreso) values (?,?,?)";
		
	/**
	 * Cadena constante con el <i>statement</i> 
	 * necesario para insertar un requisito radicacion  para un
	 * convenio  
	 */
	private final static String insertarRequisitoRadicacionConvenioStr="insert into requisitos_rad_convenio (requisito_radicacion, convenio) values (?,?)";
		
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 * modificar un requisito en una subcuenta
	 */
	private final static String modificarRequisitoPacienteSubCuentaStr="UPDATE requisitos_pac_subcuenta set cumplido=? where subcuenta=? and requisito_paciente=? ";
	
	/**
	 * Cadena constante con el <i>statement</i>
	 * necesario para eliminar un requisito para un
	 * convenio. No tiene eliminación de varios 
	 * elementos por requerimientos de Log 
	 */
	private final static String eliminarRequisitoPacienteConvenioStr="DELETE from requisitos_pac_convenio where requisito_paciente=? and convenio=? and via_ingreso = ?";
	
	/**
	 * Cadena constante con el <i>statement</i>
	 * necesario para eliminar un requisito radicacion para un
	 * convenio. No tiene eliminación de varios 
	 * elementos por requerimientos de Log 
	 */
	private final static String eliminarRequisitoRadicacionConvenioStr="DELETE from requisitos_rad_convenio where requisito_radicacion=? and convenio=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 * consultar todos los requisitos dado el convenio
	 */
	private final static String consultarRequisitoPacienteConvenioStr="SELECT " +
		"rpc.requisito_paciente as codigoRequisito, " +
		"rp.descripcion as requisito, " +
		"rp.tipo_requisito as tipoRequisito, " +
		"rpc.convenio as codigoConvenio, " +
		"con.nombre as convenio, " + 
		ValoresPorDefecto.getValorTrueParaConsultas() + " as vieneBD, " + 
		ValoresPorDefecto.getValorFalseParaConsultas() + " as eliminar, " +
		"rpc.via_ingreso as viaIngreso, " +
		"getnombreviaingreso(rpc.via_ingreso) as nombreViaIngreso " +
		"from convenios con " +
		"INNER JOIN requisitos_pac_convenio rpc ON(con.codigo=rpc.convenio) " +
		"INNER JOIN requisitos_paciente rp ON (rpc.requisito_paciente=rp.codigo) " +
		"where rpc.convenio=? ORDER BY requisito ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 * consultar todos los requisitos radicacion dado el convenio
	 */
	private final static String consultarRequisitoRadicacionConvenioStr="SELECT " +
		"rrc.requisito_radicacion as codigoRequisito, " +
		"rr.descripcion as requisito, " +
		"rr.tipo_requisito AS tipoRequisito, "+
		"rrc.convenio as codigoConvenio, " +
		"con.nombre as convenio, " + 
		ValoresPorDefecto.getValorTrueParaConsultas() + " as vieneBD, " + 
		ValoresPorDefecto.getValorFalseParaConsultas() + " as eliminar " +
		"from convenios con " +
		"INNER JOIN requisitos_rad_convenio rrc ON(con.codigo=rrc.convenio) " +
		"INNER JOIN requisitos_radicacion rr ON (rrc.requisito_radicacion=rr.codigo) " +
		"where rrc.convenio=? ORDER BY requisito ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 */
	private final static String obtenerRequisitosNoUtilizadosPorConvenioStr="SELECT " +
		"codigo as codigoReqNoUsado, " +
		"descripcion as reqNoUsado," +
		"tipo_requisito AS tipoReqNoUsado " +
		"from requisitos_paciente " +
		"where " +
		//"codigo NOT IN (select requisito_paciente from requisitos_pac_convenio where convenio=?) and " +
		"institucion=? and " +
		"activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " " +
		"ORDER BY reqNoUsado ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 */
	private final static String obtenerRequisitosRadicacionNoUtilizadosPorConvenioStr="SELECT " +
		"codigo as codigoReqNoUsado, " +
		"descripcion as reqNoUsado," +
		"tipo_requisito AS tipoReqNoUsado " +
		"from requisitos_radicacion " +
		"where " +
		"codigo NOT IN (select requisito_radicacion from requisitos_rad_convenio where convenio=?) and " +
		"institucion=? and " +
		"activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " " +
		"ORDER BY reqNoUsado ";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para
	 * cargar los requisitos ya llenados y los que no (sirve
	 * sobre todo para la modificacion)
	 */
	private final static String cargarRequisitosPacienteXSubCuentaModificacionStr="SELECT req.codigo as codigoRequisito, req.descripcion as requisito, rps.cumplido  from requisitos_pac_subcuenta rps INNER JOIN requisitos_paciente req ON (rps.requisito_paciente=req.codigo) where rps.subcuenta=? ORDER BY req.descripcion";

	
	/**
	 * Inserta un requisito para el paciente dependiendo de la institución
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int  insertar(		Connection con,
												String descripcion,
												String tipoRequisito,
												boolean activo,
												int codigoInstitucion,
												String insertarRequisitoStr)
	{
	   int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarRequisitoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,descripcion);
					ps.setString(2,tipoRequisito);
					ps.setBoolean(3,activo);
					ps.setInt(4, codigoInstitucion);
	
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBaseRequisitosPacienteDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 * Inserta un requisito para las radicaciones dependiendo de la institución
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int  insertarRequisitosRadicacion(		Connection con,
																				String descripcion,
																				String tipoRequisito,
																				boolean activo,
																				int codigoInstitucion,
																				String insertarRequisitoRadicacionStr)
	{
	   int resp=0;
		try
			{
					if (con == null || con.isClosed()) 
					{
							DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							con = myFactory.getConnection();
					}
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarRequisitoRadicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1,descripcion);
					ps.setString(2,tipoRequisito);
					ps.setBoolean(3,activo);
					ps.setInt(4, codigoInstitucion);
	
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos requisitos radicacion: SqlBaseRequisitosPacienteDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	
	/**
	 * Consulta la info de los requisitos de un paciente según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ResultSetDecorator cargarRequisitos(Connection con, int codigoInstitucion)
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaStandarRequisitosPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			logger.info("Select de los requisitos=> "+consultaStandarRequisitosPacienteStr+", institucion=> "+codigoInstitucion);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de requisitos: SqlBaseRequisitosPacienteDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Consulta la info de los requisitos de radicacion según la institución
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ResultSetDecorator cargarRequisitosRadicacion(Connection con, int codigoInstitucion)
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaStandarRequisitosRadicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de requisitos de radicacion: SqlBaseRequisitosPacienteDao "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Modifica un requisito de paciente dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public static int modificar(			Connection con, 
													String descripcion,
													String tipoRequisito,
													boolean activo, 
													int codigo) 
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarRequisitoPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, descripcion);
				ps.setString(2, tipoRequisito);
				ps.setBoolean(3, activo);
				ps.setInt(4, codigo);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseRequisitosPacienteDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Modifica un requisito de radicaion dado su codigo
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public static int modificarRequisitosRadicacion(		Connection con, 
																				String descripcion,
																				String tipoRequisito,
																				boolean activo, 
																				int codigo) 
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarRequisitoRadicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, descripcion);
				ps.setString(2, tipoRequisito);
				ps.setBoolean(3, activo);
				ps.setInt(4, codigo);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos los requisitos radicacion: SqlBaseRequisitosPacienteDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * Implementación del método que permite insertar 
	 * un nuevo requisito exigido por un convenio para
	 * una base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @param viaIngreso 
	 * @return
	 * @throws SQLException
	 */
	public static int insertarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio, int viaIngreso) throws SQLException
	{
	    int llaves[]={codigoRequisitoPaciente, codigoConvenio, viaIngreso};
	    return UtilidadBD.updateGenerico(con, llaves, insertarRequisitoPacienteConvenioStr);
	}
	
	/**
	 * Implementación del método que permite insertar 
	 * un nuevo requisito radicacion exigido por un convenio para
	 * una base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public static int insertarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException
	{
	    int llaves[]={codigoRequisitoRadicacion, codigoConvenio};
	    return UtilidadBD.updateGenerico(con, llaves, insertarRequisitoRadicacionConvenioStr);
	}
	
	
	
	/**
	 * Implementación del método que permite eliminar un 
	 * requisito exigido por un convenio para una base de 
	 * datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @param viaIngreso 
	 * @return
	 * @throws SQLException
	 */
	public static int eliminarRequisitoPacienteConvenio (Connection con, int codigoRequisitoPaciente, int codigoConvenio, int viaIngreso) throws SQLException
	{
	    int llaves[]={codigoRequisitoPaciente, codigoConvenio,viaIngreso};
	    return UtilidadBD.updateGenerico(con, llaves, eliminarRequisitoPacienteConvenioStr);
	}
	
	/**
	 * Implementación del método que permite eliminar un 
	 * requisito radicacion exigido por un convenio para una base de 
	 * datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public static int eliminarRequisitoRadicacionConvenio (Connection con, int codigoRequisitoRadicacion, int codigoConvenio) throws SQLException
	{
	    int llaves[]={codigoRequisitoRadicacion, codigoConvenio};
	    return UtilidadBD.updateGenerico(con, llaves, eliminarRequisitoRadicacionConvenioStr);
	}
	
	/**
	 * Implementación del método que consulta todos los 
	 * requisitos de paciente para un convenio para una
	 * base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultarRequisitoPacienteConvenio(Connection con, int codigoConvenio) throws SQLException
	{
	    return UtilidadBD.ejecucionGenericaResultSetDecorator(con, codigoConvenio, 1, consultarRequisitoPacienteConvenioStr);
	}
	
	/**
	 * Implementación del método que consulta todos los 
	 * requisitos radicacion para un convenio para una
	 * base de datos Genérica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultarRequisitoRadicacionConvenio(Connection con, int codigoConvenio) throws SQLException
	{
	    return UtilidadBD.ejecucionGenericaResultSetDecorator(con, codigoConvenio, 1, consultarRequisitoRadicacionConvenioStr);
	}
	
	/**
	 * Implementación del método que consulta los
	 * requisitos no utilizados por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator obtenerRequisitosNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException
	{
	    PreparedStatementDecorator obtenerRequisitosNoUtilizadosPorConvenioStatement = new PreparedStatementDecorator(con.prepareStatement(obtenerRequisitosNoUtilizadosPorConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    //obtenerRequisitosNoUtilizadosPorConvenioStatement.setInt(1, codigoConvenio);
	    obtenerRequisitosNoUtilizadosPorConvenioStatement.setString(1, codigoInstitucion);
	    return new ResultSetDecorator(obtenerRequisitosNoUtilizadosPorConvenioStatement.executeQuery());
	}
	
	/**
	 * Implementación del método que consulta los
	 * requisitos radicacion no utilizados por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator obtenerRequisitosRadicacionNoUtilizadosPorConvenio (Connection con, int codigoConvenio, String codigoInstitucion) throws SQLException
	{
	    PreparedStatementDecorator obtenerRequisitosRadicacionNoUtilizadosPorConvenioStatement = new PreparedStatementDecorator(con.prepareStatement(obtenerRequisitosRadicacionNoUtilizadosPorConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    obtenerRequisitosRadicacionNoUtilizadosPorConvenioStatement.setInt(1, codigoConvenio);
	    obtenerRequisitosRadicacionNoUtilizadosPorConvenioStatement.setString(2, codigoInstitucion);
	    return new ResultSetDecorator(obtenerRequisitosRadicacionNoUtilizadosPorConvenioStatement.executeQuery());
	}
	



	

	/**
	 * Implementación del método que carga los requisitos previos
	 * llenados por un paciente en una subcuenta.
	 * 
	 * @see com.princetonsa.dao.RequisitosDao#cargarRequisitosPacienteXCuentaModificacion(con, idCuenta) throws SQLException
	 */
	public static ResultSetDecorator cargarRequisitosPacienteXSubCuentaModificacion(Connection con, int idCuenta) throws SQLException
	{
		return UtilidadBD.ejecucionGenericaResultSetDecorator(con, idCuenta, 1, cargarRequisitosPacienteXSubCuentaModificacionStr);
	}

	
	/**
	 * Adición de Sebatsián
	 * Implementación del método que modifica un requisito de una SUBCUENTA previo
	 * llenado por un paciente para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.RequisitosDao#modificarRequisitoPacienteCuenta (Connection , int , int,  boolean ) throws SQLException
	 */
	public static int modificarRequisitoPacienteSubCuenta (Connection con, int idSubCuenta, int codigoRequisito, boolean cumplido)
	{
		try{
	    PreparedStatementDecorator modificarRequisitoPacienteCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(modificarRequisitoPacienteSubCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    modificarRequisitoPacienteCuentaStatement.setBoolean(1, cumplido);
	    modificarRequisitoPacienteCuentaStatement.setInt(2, idSubCuenta);
	    modificarRequisitoPacienteCuentaStatement.setInt(3, codigoRequisito);
	    return modificarRequisitoPacienteCuentaStatement.executeUpdate();
		}
	catch(SQLException e){
			logger.warn(e+" Error en la modificación de requisitos dle paciente por subcuenta: SqlBaseRequisitosPacienteDao "+e.toString());
			return -1;
			}
	    
	}
	
	
	
	/**
	 * Método que consulta los requisitos del paciente por convenio segun el tipo
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarRequisitosPacienteXConvenio(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT " +
				"rp.codigo AS codigo, " +
				"rp.descripcion AS descripcion " +
				"FROM requisitos_pac_convenio rpc " +
				"INNER JOIN requisitos_paciente rp ON(rp.codigo=rpc.requisito_paciente) " +
				"WHERE " +
				"rpc.convenio = "+campos.get("codigoConvenio")+" AND " +
				"rpc.via_ingreso = "+campos.get("codigoViaIngreso")+" AND " +
				"rp.tipo_requisito = '"+campos.get("tipoRequisito")+"' and " +
				"rp.activo = "+(UtilidadTexto.getBoolean(campos.get("activo").toString())?ValoresPorDefecto.getValorTrueParaConsultas():ValoresPorDefecto.getValorFalseParaConsultas())+" AND " +
				"rp.institucion = "+campos.get("institucion")+" ORDER BY rp.descripcion ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("descripcion", rs.getObject("descripcion"));
				resultados.add(elemento);
			}
			
			return resultados;

		}
		catch(SQLException e)
		{
			logger.error("Error en cargarRequisitosPacienteXConvenio: "+e);
			 
		}
		
		return resultados;
	}
	
	
}
