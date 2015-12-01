package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseMezclasDao
{
	/**
	 * Metodo que inserta una mezcla
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param codTipoMezcla
	 * @param activo
	 * @param codInstitucion
	 * @throws SQLException
	 */
	public static void insertarMezcla(Connection con, String codigo, String nombre, int codTipoMezcla, String activo, int codInstitucion) throws SQLException
	{		
		String insertarMezclaStr = 
			"insert into mezcla (" +
			"consecutivo, " +
			"codigo, " +
			"nombre, " +
			"cod_tipo_mezcla, "+
			"activo, " +
			"cod_institucion) " +
			"values (?, ?, ?, ?, ?, ?)";
		
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_mezcla");
        PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(insertarMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        sentencia.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
        sentencia.setString(2, codigo);
        sentencia.setString(3, nombre);
        sentencia.setDouble(4, Utilidades.convertirADouble(codTipoMezcla+""));
        sentencia.setBoolean(5, UtilidadTexto.getBoolean(activo));
        sentencia.setInt(6, codInstitucion);
        sentencia.executeUpdate();
	}
	
	/**
	 * Método para actualizar la información de una mezcla
	 * @param con
	 * @param consecutivo
	 * @param nombre
	 * @param codTipoMezcla
	 * @param activo
	 * @param codInstitucion
	 * @throws SQLException
	 */
	public static void actualizarMezcla(Connection con, int consecutivo, String nombre, int codTipoMezcla, String activo) throws SQLException
	{
		String actualizarMezclaStr = 
			"update mezcla set " +
			"nombre=?, " +
			"cod_tipo_mezcla=?, "+
			"activo=? " +
			"where consecutivo=?";
		
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(actualizarMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setString(1, nombre);
		sentencia.setDouble(2, Utilidades.convertirADouble(codTipoMezcla+""));
		sentencia.setBoolean(3, UtilidadTexto.getBoolean(activo));
		sentencia.setDouble(4, Utilidades.convertirADouble(consecutivo+""));
		sentencia.executeUpdate();
	}

	/**
	 * Método para eliminar una mezcla
	 * @param con
	 * @param consecutivo
	 * @throws SQLException
	 */
	public static void eliminarMezcla(Connection con, int consecutivo) throws SQLException
	{
		String eliminarMezclaStr =
			"delete from mezcla where consecutivo=?";
		
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(eliminarMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
		sentencia.executeUpdate();
	}
	
	/**
	 * Método para consultar la información de una mezcla
	 * @param con
	 * @param consecutivo
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarMezcla(Connection con, int consecutivo) throws SQLException
	{
		String consultarMezclaStr =
			"select " +
			"consecutivo as consecutivo, " +
			"mezcla.codigo as codigo, " +
			"mezcla.nombre as nombre, " +
			"cod_tipo_mezcla as codTipo, " +
			"tipo_mezcla.nombre as nomTipo, " +
			"activo as activo, " +
			"cod_institucion as codInstitucion " +
			"from mezcla, tipo_mezcla where consecutivo=? and tipo_mezcla.codigo = mezcla.cod_tipo_mezcla";
		
		
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarMezclaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setDouble(1, Utilidades.convertirADouble(consecutivo+""));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(sentencia.executeQuery()));
		sentencia.close();
		return mapaRetorno;
	}
	
	/**
	 * Método para consultar las mezclas de una institución especificada
	 * @param con
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarMezclasInst(Connection con, int codInstitucion) throws SQLException
	{
		String consultarMezclasInstStr =
			"select " +
			"consecutivo as consecutivo, " +
			"mezcla.codigo as codigo, " +
			"mezcla.nombre as nombre, " +
			"cod_tipo_mezcla as codTipo, " +
			"tipo_mezcla.nombre as nomTipo, " +
			"activo as activo, " +
			"cod_institucion as codInstitucion " +
			"from mezcla, tipo_mezcla where cod_institucion=? and tipo_mezcla.codigo = mezcla.cod_tipo_mezcla";
			
		
		PreparedStatementDecorator sentencia= new PreparedStatementDecorator(con.prepareStatement(consultarMezclasInstStr+" order by nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		sentencia.setInt(1, codInstitucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(sentencia.executeQuery()));
		sentencia.close();
		return mapaRetorno;
	}
}