/*
 * Created on 19/06/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.Answer;
import util.ConstantesBD;

/**
 * @author wrios
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseCensoCamas1Dao 
{
    /**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private static Logger logger = Logger.getLogger(SqlBaseCensoCamas1Dao.class);

	private static final String listarCamasStr = "SELECT c.codigo as codCama, c.numero_cama as numCama, c.descripcion as descripcionCama, c.estado as estadoCama, ec.nombre as nombreEstadoCama, c.centro_costo as codCentroCostoCama, cc.nombre as nombreCentroCostoCama, c.habitacion  "
																		+ "FROM camas1 c, centros_costo cc, estados_cama ec "
																		+ "WHERE c.centro_costo = cc.codigo "
																		+ "AND ec.codigo = c.estado "
																		+ "AND c.institucion=? "
																		+ "ORDER BY c.centro_costo ASC, c.numero_cama ASC ";

	private static final String listarCamasPorCCostoStr = "SELECT c.codigo as codCama, c.numero_cama as numCama, c.descripcion as descripcionCama, c.estado as estadoCama, ec.nombre as nombreEstadoCama, c.centro_costo as codCentroCostoCama, cc.nombre as nombreCentroCostoCama, c.habitacion "
																		+ "FROM camas1 c, centros_costo cc, estados_cama ec "
																		+ "WHERE c.centro_costo = cc.codigo "
																		+ "AND ec.codigo = c.estado "
																		+ "AND c.centro_costo = ? " 
																		+ "AND c.institucion= ? "
																		+ "ORDER BY c.centro_costo ASC, c.numero_cama ASC ";

	
    
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una cama en un objeto Answer desde la base de datos Genérica.
	 */
	private static final String cargarCamaCensoCamasStr="SELECT c.descripcion, c.numero_cama as numeroCama, c.estado as estado, c.centro_costo as codigoCentroCosto, c.tipo_usuario_cama as codigoTipoUsuarioCama, tuc.nombre as tipoUsuarioCama, c.habitacion FROM camas1 c, tipos_usuario_cama tuc WHERE c.codigo=? and c.tipo_usuario_cama = tuc.codigo and c.institucion=? ";
	
	/**
	 * Listado de camas (No se reutilizo Camas1 debido a que se necesitaban estos mismos alias creados con
	 * antelacion por Liliana)
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator listarCamas(Connection con, int codigoInstitucion) throws SQLException
	{
		ResultSetDecorator resultado;
		try{
			PreparedStatementDecorator listarCamasStmnt =  new PreparedStatementDecorator(con.prepareStatement(listarCamasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listarCamasStmnt.setInt(1, codigoInstitucion);
			resultado=new ResultSetDecorator(listarCamasStmnt.executeQuery());
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error listando camas: "+e);
			return null;
		}		
	}
    
	/**
	 * Metodo que lista las camas por centro de costo e institucion
	 * (No se reutilizo Camas1 debido a que se necesitaban estos mismos alias creados con
	 * antelacion por Liliana)
	 * @param con
	 * @param centroCosto
	 * @param codigoInstitucion
	 * @return
	 */
    public static ResultSetDecorator listarCamasCentroCosto(Connection con, int centroCosto, int codigoInstitucion)
	{
		ResultSetDecorator resultado;
		try{
			PreparedStatementDecorator listarCamasStmnt =  new PreparedStatementDecorator(con.prepareStatement(listarCamasPorCCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listarCamasStmnt.setInt(1, centroCosto);
			listarCamasStmnt.setInt(2, codigoInstitucion);
			resultado=new ResultSetDecorator(listarCamasStmnt.executeQuery());
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error listando camas por centro de costo: "+e);
			return null;
		}		
	}
    
  
	
	
	/**
	 * Dada la identificacion de una cama, carga los datos correspondientes desde la base de datos Genérica.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param codigoCama el código de la cama que se desea cargar
	 * @return un <code>Answer</code> con los datos pedidos y una conexión abierta con la base de datos Genérica
	 */
	public static Answer cargarCamaCensoCamas (Connection con, String codigoCama, int codigoInstitucion) throws SQLException
	{
		PreparedStatementDecorator cargarCamaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCamaCensoCamasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCamaStatement.setString(1, codigoCama);
		cargarCamaStatement.setInt(2, codigoInstitucion);
		return new Answer(new ResultSetDecorator(cargarCamaStatement.executeQuery()), con);
	}

}