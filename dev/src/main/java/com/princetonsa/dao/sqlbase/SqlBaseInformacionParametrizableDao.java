/*
 * Created on Apr 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Random;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * @author wrios
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseInformacionParametrizableDao {

	/**
	 * Log para manejar los problemas de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseInformacionParametrizableDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario 
	 * para buscar los campos parametrizados previamente
	 * por el médico 
	 */
	private final static String buscarCamposParametrizadosPreviamenteStr = " SELECT parasoc.codigo, " +
																		   " parasoc.orden, " +
																		   " parasoc.activo, " +
																		   " parasoc.centro_costo as centrocosto, " +
																		   " secpar.nombre as seccion, " +
																		   " secpar.codigo as codigoSeccion, " +
																		   " tippar.nombre as tipo, " +
																		   " tippar.codigo as codigoTipo, " +
																		   " parasoc.nombre, " +
																		   " funcpar.nombre as funcionalidad, " +
																		   " funcpar.codigo as codigoFuncionalidad, " +
																		   " parasoc.alcance_campo as codigoAlcanceCampo " +
																		   " from param_asociadas parasoc, seccion_parametriza secpar, tipos_parametrizables tippar, fun_param funcpar " +
																		   " where parasoc.tipo=tippar.codigo " +
																		   " and parasoc.seccion=secpar.codigo " +
																		   " and funcpar.codigo=secpar.funcionalidad " +
																		   " and ((parasoc.alcance_campo="+ConstantesBD.codigoAlcanceMedico+" and parasoc.codigo_medico=? and parasoc.institucion=?) or (parasoc.alcance_campo<>"+ConstantesBD.codigoAlcanceMedico+" and parasoc.institucion=?)) " +
																		   " order by funcpar.codigo, secpar.codigo";
	
	/**
	 * Método que inserta un valor especifico para un
	 * campo parametrizado particular 
	 */
	private final static String insertarLlenadoCampoStr="INSERT INTO  info_parametrizada (codigo_tabla, parametrizacion_asociada, valor) VALUES (?, ?, ?)";
	
	/**
	 * Método que actualiza la información sobre un campo
	 * parametrizado
	 */
	private final static String actualizarCampoParametrizadoStr=" UPDATE param_asociadas " +
																" set activo=?, " +
																" alcance_campo=?," +
																" centro_costo =? " +
																" where codigo=?";
	
	/**
	 * Implementación de la búsqueda de los campos que puede llenar un médico en
	 * particular para una funcionalidad particular (datos parametrizados) en una BD 
	 * Genérica
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#buscarSeccionesYCamposDadaFuncionalidad (Connection , int , String , String, int, String ) throws SQLException
	 */
	public static ResultSetDecorator buscarSeccionesYCamposDadaFuncionalidad (Connection con, int idFuncionalidad, int codigoMedico, int codigoCentroCosto, String codigoInstitucion) throws SQLException
	{
		/**
		 * Cadena constante con el <i>statement</i> necesario 
		 * para buscar todas las secciones y campos para una
		 * pareja funcionalidad / médico 
		 */
		String buscarSeccionesYCamposDadaFuncionalidadStr=
		    " SELECT secpar.codigo as codigoSeccion, secpar.nombre as seccion, parasoc.codigo as codigoCampo, parasoc.orden as ordenCampo, parasoc.nombre as campo from seccion_parametriza secpar, param_asociadas parasoc where secpar.codigo=parasoc.seccion and parasoc.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and secpar.funcionalidad =? and parasoc.codigo_medico=? and parasoc.alcance_campo="+ ConstantesBD.campoParametrizableAlcanceMedico +
		    " UNION ALL " +
		    " SELECT secpar.codigo as codigoSeccion, secpar.nombre as seccion, parasoc.codigo as codigoCampo, parasoc.orden as ordenCampo, parasoc.nombre as campo from seccion_parametriza secpar, param_asociadas parasoc where secpar.codigo=parasoc.seccion and parasoc.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and secpar.funcionalidad =? and parasoc.centro_costo=? and parasoc.alcance_campo="+ ConstantesBD.campoParametrizableAlcanceCentroCosto +
		    " UNION ALL " +
		    " SELECT secpar.codigo as codigoSeccion, secpar.nombre as seccion, parasoc.codigo as codigoCampo, parasoc.orden as ordenCampo, parasoc.nombre as campo from seccion_parametriza secpar, param_asociadas parasoc where secpar.codigo=parasoc.seccion and parasoc.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and secpar.funcionalidad =? and parasoc.institucion=? and parasoc.alcance_campo="+ ConstantesBD.campoParametrizableAlcanceInstitucion +
		    " order by codigoSeccion desc";
		
		PreparedStatementDecorator buscarSeccionesYCamposDadaFuncionalidadStatement= new PreparedStatementDecorator(con.prepareStatement(buscarSeccionesYCamposDadaFuncionalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarSeccionesYCamposDadaFuncionalidadStatement.setInt(1, idFuncionalidad);
		buscarSeccionesYCamposDadaFuncionalidadStatement.setInt(2, codigoMedico);
		buscarSeccionesYCamposDadaFuncionalidadStatement.setInt(3, idFuncionalidad);
		buscarSeccionesYCamposDadaFuncionalidadStatement.setInt(4, codigoCentroCosto);
		buscarSeccionesYCamposDadaFuncionalidadStatement.setInt(5, idFuncionalidad);
		buscarSeccionesYCamposDadaFuncionalidadStatement.setString(6, codigoInstitucion);
		return new ResultSetDecorator(buscarSeccionesYCamposDadaFuncionalidadStatement.executeQuery());
	}

	/**
	 * Método que busca los campos parametrizados 
	 * previamente por un médico para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#buscarCamposParametrizadosPreviamente (Connection , int ) 
	 */
	public static ResultSetDecorator buscarCamposParametrizadosPreviamente (Connection con, int codigoMedico, int institucion) throws SQLException
	{
		PreparedStatementDecorator buscarCamposParametrizadosPreviamenteStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCamposParametrizadosPreviamenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarCamposParametrizadosPreviamenteStatement.setInt(1, codigoMedico);
		buscarCamposParametrizadosPreviamenteStatement.setInt(2, institucion);
		buscarCamposParametrizadosPreviamenteStatement.setInt(3, institucion);
		return new ResultSetDecorator(buscarCamposParametrizadosPreviamenteStatement.executeQuery());
	}

	/**
	* Implementación de la inserción de un campo parametrizado en 
	* una BD Genérica, dado que es solo una inserción no tiene
	* transacción ya que si falla la instrucción falla toda la operación
	* (def. transacción)
	* 
	* @see com.princetonsa.dao.InformacionParametrizableDao#public int insertarLlenadoCampo (Connection , int , int , String ) throws SQLException
	*/
	public static int insertarLlenadoCampo (Connection con, int codigoTabla, int parametrizacionAsociada, String valor) throws SQLException
	{		
		PreparedStatementDecorator insertarLlenadoCampoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarLlenadoCampoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarLlenadoCampoStatement.setInt(1, codigoTabla);
		insertarLlenadoCampoStatement.setInt(2, parametrizacionAsociada);
		insertarLlenadoCampoStatement.setString(3, valor);
		return insertarLlenadoCampoStatement.executeUpdate();
	}

	/**
	 * Método que inserta un nuevo campo parametrizado para 
	 * una BD Genérica
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#insertarCampoParametrizado (Connection , int , int , int , String , int , boolean , int , String , int , String ) throws SQLException 
	 */
	public static int insertarCampoParametrizado (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico,boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance, String variableSqlStr) throws SQLException
	{
		PreparedStatementDecorator insertarCampoParametrizadoStatement= new PreparedStatementDecorator(con.prepareStatement(variableSqlStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		Random r=new Random();
		//@todo: super-cambio 2da parte
		insertarCampoParametrizadoStatement.setInt(1, r.nextInt()+orden);
		insertarCampoParametrizadoStatement.setInt(2, codigoSeccion);
		insertarCampoParametrizadoStatement.setInt(3, codigoTipo);
		insertarCampoParametrizadoStatement.setString(4, nombre);
		insertarCampoParametrizadoStatement.setInt(5, codigoMedico);
		insertarCampoParametrizadoStatement.setInt(6, codigoCentroCosto);
		insertarCampoParametrizadoStatement.setString(7, codigoInstitucion);
		insertarCampoParametrizadoStatement.setInt(8, codigoAlcance);

		insertarCampoParametrizadoStatement.setBoolean(9, activo);
		return insertarCampoParametrizadoStatement.executeUpdate();
		//(codigo, orden, seccion, tipo, nombre, tipo_identificacion_medico, numero_identificacion_medico, activo)
	}
	

	/**
	 * Método que actualiza un nuevo campo parametrizado 
	 * para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#actualizarCampoParametrizado (Connection , int , boolean , int , int ) throws SQLException 
	 */
	public static int actualizarCampoParametrizado (Connection con, boolean activo, int codigo, int codigoAlcance, int centroCosto) throws SQLException
	{
		PreparedStatementDecorator actualizarCampoParametrizadoStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarCampoParametrizadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		actualizarCampoParametrizadoStatement.setBoolean(1, activo);
		actualizarCampoParametrizadoStatement.setInt(2, codigoAlcance);
		actualizarCampoParametrizadoStatement.setInt(3, centroCosto);
		actualizarCampoParametrizadoStatement.setInt(4, codigo);
		return actualizarCampoParametrizadoStatement.executeUpdate();
	}

	/**
	 * Implementación de la inserción de un campo parametrizado en 
	 * una BD Genérica, soportando definir el estado de la
	 * transacción.
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#insertarLlenadoCampoTransaccional (Connection , int , int , String , String ) throws SQLException
	 */
	public static int insertarLlenadoCampoTransaccional (Connection con, int codigoTabla, int parametrizacionAsociada, String valor, String estado) throws SQLException
	{
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		try
		{
				if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
				con.setAutoCommit(false);
				
			}

			if (estado.equals("empezar"))
			{
				boolean a=myFactory.beginTransaction(con);
				if(a)
					resp0 = 1;
				else
					resp0 = 0;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			resp1=insertarLlenadoCampo (con, codigoTabla, parametrizacionAsociada, valor);
			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{

			logger.warn("Excepción en insertarLlenadoCampoTransaccional " + e);
			throw e;
		}
		
	}
	
	/**
	 * Implementación de la inserción de un campo parametrizado en 
	 * una BD Genérica.
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#insertarCampoParametrizadoTransaccional (Connection , int , int , int , String , int , boolean , String , int , String , int , String ) throws SQLException
	 */
	public static int insertarCampoParametrizadoTransaccional (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico, boolean activo, String estado, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance, String variableSqlStr) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		int resp0=0, resp1=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				boolean a=myFactory.beginTransaction(con);
				if(a)
					resp0 = 1;
				else
					resp0 = 0;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			resp1=insertarCampoParametrizado (con, orden, codigoSeccion, codigoTipo, nombre, codigoMedico,  activo, codigoCentroCosto, codigoInstitucion, codigoAlcance, variableSqlStr) ;

			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{

			logger.warn("Excepción en insertarCampoParametrizadoTransaccional " + e);
			throw e;
		}
	}

	/**
	 * Implementación de la actualización de un campo 
	 * parametrizado en una BD Genérica, soportando 
	 * definir el estado de la transacción.
	 * 
	 * @see com.princetonsa.dao.InformacionParametrizableDao#actualizarCampoParametrizadoTransaccional (Connection , int , boolean , int , int , String ) throws SQLException
	 */
	public static int actualizarCampoParametrizadoTransaccional (Connection con, boolean activo, int codigo, int codigoAlcance, int centroCosto, String estado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp0=0, resp1=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
				con.setAutoCommit(false);
			}

			if (estado.equals("empezar"))
			{
				boolean a=myFactory.beginTransaction(con);
				if(a)
					resp0 = 1;
				else
					resp0 = 0;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			resp1=actualizarCampoParametrizado (con, activo, codigo, codigoAlcance, centroCosto) ;

			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{

			logger.warn("Excepción en actualizarCampoParametrizadoTransaccional " + e);
			throw e;
		}
	}
	

}
