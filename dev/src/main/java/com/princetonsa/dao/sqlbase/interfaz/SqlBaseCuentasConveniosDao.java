package com.princetonsa.dao.sqlbase.interfaz;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseCuentasConveniosDao
{
	private static Logger logger = Logger.getLogger(SqlBaseCuentasConveniosDao.class);

	/**
	 * Metodo que inserta la información de una cuenta de un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @param valor
	 * @throws SQLException
	 */
	public static void insertarCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		String insertarCuentaRegimenStr = 
			"insert into cuenta_regimen (" +
			"cod_tipo_cuenta, " +
			"acr_tipo_regimen, " +
			"cod_institucion, " +
			"valor, " +
			"rubro_presupuestal, " +
			"cuenta_contable_cxc_cap "+
			") " +
			"values (?, ?, ?, ?, ?, ?) ";		

        PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarCuentaRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        insertarStatement.setInt(1, codTipoCuenta);
        insertarStatement.setString(2, acronimoTipoRegimen);
        insertarStatement.setInt(3, codInstitucion);
        insertarStatement.setString(4, valor);
  
        if (!UtilidadTexto.isEmpty(capitacion))
        	insertarStatement.setDouble(5, Utilidades.convertirAEntero(capitacion));
        else
        	insertarStatement.setNull(5, Types.DOUBLE);
                
        if(rubro.equals(""))
		{
        	insertarStatement.setNull(6, Types.NUMERIC);
		}
		else
		{	
			insertarStatement.setDouble(6,Utilidades.convertirADouble(rubro));
		}
        
        insertarStatement.executeUpdate();
	}

	/**
	 * Actualiza la información de una cuenta de un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @param valor
	 * @param rubro 
	 * @throws SQLException
	 */
	public static void actualizarCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		String actualizarCuentaRegimenStr =
			"update cuenta_regimen set " +
			"valor=?, rubro_presupuestal=?," +
			"cuenta_contable_cxc_cap=? " +
			"where cod_tipo_cuenta=? and acr_tipo_regimen=? and cod_institucion=?";

        PreparedStatementDecorator actualizarStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarCuentaRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        actualizarStatement.setString(1, valor);
        if(rubro.equals(""))
		{
        	actualizarStatement.setNull(2, Types.NUMERIC);
		}
		else
		{	
			actualizarStatement.setDouble(2,Utilidades.convertirADouble(rubro));
		}
        if (!UtilidadTexto.isEmpty(capitacion))
        	actualizarStatement.setDouble(3, Utilidades.convertirAEntero(capitacion));
        else
        	actualizarStatement.setNull(3, Types.DOUBLE);
        
        actualizarStatement.setInt(4, codTipoCuenta);
        actualizarStatement.setString(5, acronimoTipoRegimen);
        actualizarStatement.setInt(6, codInstitucion);
        
        actualizarStatement.executeUpdate();
	}

	/**
	 * Consulta si existe una cuenta dada para un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion) throws SQLException
	{
		String existenCuentaRegimenStr = "select count(*) as existeCuenta from cuenta_regimen where cod_tipo_cuenta=? and acr_tipo_regimen=? and cod_institucion=?";
		
		PreparedStatementDecorator existeStatement= new PreparedStatementDecorator(con.prepareStatement(existenCuentaRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        existeStatement.setInt(1, codTipoCuenta);
        existeStatement.setString(2, acronimoTipoRegimen);
        existeStatement.setInt(3, codInstitucion);
        
        ResultSetDecorator rs = new ResultSetDecorator(existeStatement.executeQuery());
        if(rs.next())
        {
            int existeCuenta = rs.getInt("existeCuenta");
            if(existeCuenta>0)
                return true;
            else
                return false;
        }
        else
        {
            String mensajeError = "Hubo problemas consultando si existe cuenta";
            logger.warn(mensajeError);
            throw new SQLException(mensajeError);
        }
	}
	
	/**
	 * Método que consulta las cuentas pertenecientes a un tipo de regimen y de la institucion indicada
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @return
	 */
	public static HashMap consultarCuentasRegimen(Connection con, String acronimoTipoRegimen, int codInstitucion) throws SQLException
	{
		String consultaCuentasRegimenStr = 	"select " +
				"cod_tipo_cuenta as codTipoCuenta, " +
				"tipo_cuenta.nombre as nomTipoCuenta, " +
				"tipo_cuenta.orden, " +
				"acr_tipo_regimen as acronimoTipoRegimen, " +
				"cod_institucion as codInstitucion, " +
				"valor as valor, " +
				"rubro_presupuestal as rubro, " +
				"cuenta_contable_cxc_cap as capitacion " +
				"from cuenta_regimen, tipo_cuenta where cuenta_regimen.cod_tipo_cuenta = tipo_cuenta.codigo and " +
				"acr_tipo_regimen = ? and cod_institucion = ? ";
				

	

		
		PreparedStatementDecorator consultaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaCuentasRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		consultaStatement.setString(1, acronimoTipoRegimen);
		consultaStatement.setInt(2, codInstitucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(consultaStatement.executeQuery()));
		consultaStatement.close();
		return mapaRetorno;
	}
	
	/**
	 * Metodo que inserta la información de una cuenta de un convenio especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @param valor
	 * @param rubro 
	 * @throws SQLException
	 */
	public static void insertarCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		String insertarCuentaConvenioStr = 
			"insert into cuenta_convenio (" +
			"cod_tipo_cuenta, " +
			"cod_convenio, " +
			"cod_institucion, " +
			"valor, " +
			"rubro_presupuestal," +
			"cuenta_contable_cxc_cap) " +
			"values (?, ?, ?, ?, ?, ?)";		

        PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarCuentaConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        insertarStatement.setInt(1, codTipoCuenta);
        insertarStatement.setInt(2, codConvenio);
        insertarStatement.setInt(3, codInstitucion);
        insertarStatement.setString(4, valor);
        if(rubro.equals(""))
		{
        	insertarStatement.setNull(5, Types.NUMERIC);
		}
		else
		{	
			insertarStatement.setDouble(5,Utilidades.convertirADouble(rubro));
		}
        if (!UtilidadTexto.isEmpty(capitacion))
        	insertarStatement.setDouble(6, Utilidades.convertirAEntero(capitacion));
        else
        	insertarStatement.setNull(6, Types.DOUBLE);
        
        insertarStatement.executeUpdate();
	}

	/**
	 * Actualizar la información de una cuenta convenio especificado 
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @param valor
	 * @param rubro 
	 * @throws SQLException
	 */
	public static void actualizarCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		String actualizarCuentaConvenioStr =
			"update cuenta_convenio set " +
			"valor=?, rubro_presupuestal=?, cuenta_contable_cxc_cap=? " +
			"where cod_tipo_cuenta=? and cod_convenio=? and cod_institucion=?";

        PreparedStatementDecorator actualizarStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarCuentaConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        actualizarStatement.setString(1, valor);
        if(rubro.equals(""))
		{
        	actualizarStatement.setNull(2, Types.NUMERIC);
		}
		else
		{	
			actualizarStatement.setDouble(2,Utilidades.convertirADouble(rubro));
		}
        if (!UtilidadTexto.isEmpty(capitacion))
        	actualizarStatement.setDouble(3, Utilidades.convertirAEntero(capitacion));
        else
        	actualizarStatement.setNull(3, Types.DOUBLE);
        
        actualizarStatement.setInt(4, codTipoCuenta);
        actualizarStatement.setInt(5, codConvenio);
        actualizarStatement.setInt(6, codInstitucion);
        actualizarStatement.executeUpdate();
	}

	/**
	 * Consulta si existe una cuenta dada para un convenio especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static boolean existeCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion) throws SQLException
	{
		String existenCuentaConvenioStr = "select count(*) as existeCuenta from cuenta_convenio where cod_tipo_cuenta=? and cod_convenio=? and cod_institucion=?";
		
		PreparedStatementDecorator existeStatement= new PreparedStatementDecorator(con.prepareStatement(existenCuentaConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        existeStatement.setInt(1, codTipoCuenta);
        existeStatement.setInt(2, codConvenio);
        existeStatement.setInt(3, codInstitucion);
        
        ResultSetDecorator rs = new ResultSetDecorator(existeStatement.executeQuery());
        if(rs.next())
        {
            int existeCuenta = rs.getInt("existeCuenta");
            if(existeCuenta>0)
                return true;
            else
                return false;
        }
        else
        {
            String mensajeError = "Hubo problemas consultando si existe cuenta";
            logger.warn(mensajeError);
            throw new SQLException(mensajeError);
        }
	}

	/**
	 * Método que consulta las cuentas pertenecientes a un tipo de convenio y de la institucion indicada
	 * @param con
	 * @param codConvenio
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCuentasConvenio(Connection con, int codConvenio, int codInstitucion) throws SQLException
	{
		String consultaCuentasRegimenStr = "select " +
				"cod_tipo_cuenta as codTipoCuenta, " +
				"tipo_cuenta.nombre as nomTipoCuenta, " +
				"tipo_cuenta.orden, " +
				"cod_convenio as codConvenio, " +
				"cod_institucion as codInstitucion, " +
				"valor as valor, " +
				"rubro_presupuestal as rubro," +
				"cuenta_contable_cxc_cap as capitacion " +
				"from cuenta_convenio, tipo_cuenta where cuenta_convenio.cod_tipo_cuenta = tipo_cuenta.codigo and " +
				"cod_convenio = ? and cod_institucion = ? ";
				

		PreparedStatementDecorator consultaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaCuentasRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		consultaStatement.setInt(1, codConvenio);
		consultaStatement.setInt(2, codInstitucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(consultaStatement.executeQuery()));
		consultaStatement.close();
		return mapaRetorno;
		
	}	
}
