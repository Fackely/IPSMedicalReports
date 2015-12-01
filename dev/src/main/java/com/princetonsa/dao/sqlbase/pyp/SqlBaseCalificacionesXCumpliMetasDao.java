/*
 * Creado en Aug 8, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * SqlBase de calificaciones por cumplimiento de metas
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Aug 9, 2006
 */
public class SqlBaseCalificacionesXCumpliMetasDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCalificacionesXCumpliMetasDao.class);
	

	/**
	 * Método que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * régimen seleccionado y la institución actual
	 * @param con
	 * @param tipoRegimen
	 * @param codigoInstitucion
	 */
	public static HashMap consultarCalificacionesXRegimen(Connection con, String tipoRegimen, int codigoInstitucion)
	{
		String consultaStr="SELECT codigo AS codigo, meta AS meta, meta AS metaAnt, rango_inicial AS rango_inicial," +
											"				rango_inicial AS rango_inicialAnt, rango_final AS rango_finalAnt, tipo_calificacion AS tipo_calificacionAnt, activo AS activoAnt," +
											"				rango_final AS rango_final, tipo_calificacion AS tipo_calificacion," +
											"				activo AS activo, 1 AS esta_grabado " +
											"		FROM pyp.califica_x_cumpli_metas " +
											"			WHERE tipo_regimen=? AND institucion=?" +
											"					ORDER BY codigo";

		try
		{
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setString(1,tipoRegimen);
		ps.setInt(2, codigoInstitucion);
		
		HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		ps.close();
		return mapaRetorno;
		}
		catch (SQLException e)
		{
		logger.error("Error en consultarCalificacionesXRegimen:  en SqlBaseCalificacionesXCumpliMetasDao"+e.toString());
		e.printStackTrace();
		return null;
		}
	}
	
	/**
	 *	Método que elimina el registro seleccionado de la tabla
	 * @param con
	 * @param codigoCalificacion
	 * @return
	 */
	public static int eliminarCalificacionXCumplimiento(Connection con, int codigoCalificacion)
	{
		PreparedStatementDecorator ps;
		int resp=ConstantesBD.codigoNuncaValido;
		
		try
		{
			String	consultaStr="DELETE FROM pyp.califica_x_cumpli_metas " +
												"		WHERE codigo = ?" ;
			
					
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCalificacion);
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en eliminarCalificacionXCumplimiento : SqlBaseCalificacionesXCumpliMetasDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
	/**
	 * Método que guarda o actualiza las calificaciones de cumplimieto por metas
	 * registradas en un tipo de régimen y una institución
	 * @param con
	 * @param codigo
	 * @param tipoRegimen
	 * @param meta
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param tipoCalificacion
	 * @param activo
	 * @param codigoInstitucion
	 * @param esInsertar
	 * @return
	 */
	public static int insertarModificarCalificacionsXCumplimiento(Connection con, int codigo, String tipoRegimen, String meta, String rangoInicial, String rangoFinal, int tipoCalificacion, boolean activo, int codigoInstitucion, boolean esInsertar)
	{
		PreparedStatementDecorator ps;
		String consultaStr="";
		int resp=-1;
		
		try
		{
			if (esInsertar)
				{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				codigo=myFactory.incrementarValorSecuencia(con, "seq_califica_x_cumpli_metas");
				
				consultaStr="INSERT INTO pyp.califica_x_cumpli_metas (codigo, tipo_regimen, meta, rango_inicial, rango_final, tipo_calificacion, activo, institucion) " +
																" VALUES (?, ?, ?, ?, ?, ?, ?, ? )" ;
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
				ps.setString(2, tipoRegimen);
				ps.setDouble(3, Utilidades.convertirADouble(meta));
				ps.setDouble(4, Utilidades.convertirADouble(rangoInicial));
				ps.setDouble(5, Utilidades.convertirADouble(rangoFinal));
				ps.setInt(6, tipoCalificacion);
				ps.setBoolean(7, activo);
				ps.setInt(8, codigoInstitucion);
				}
			else
				{
				consultaStr="UPDATE pyp.califica_x_cumpli_metas SET meta=?, rango_inicial=?, rango_final=?, tipo_calificacion=?, activo = ?, institucion = ?" +
										"				 WHERE codigo = ? ";

				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(meta));
				ps.setDouble(2, Utilidades.convertirADouble(rangoInicial));
				ps.setDouble(3, Utilidades.convertirADouble(rangoFinal));
				ps.setInt(4, tipoCalificacion);
				ps.setBoolean(5, activo);
				ps.setInt(6, codigoInstitucion);
				ps.setDouble(7, Utilidades.convertirADouble(codigo+""));
				}
									
			resp = ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción/modificación de datos en insertarModificarCalificacionsXCumplimiento : SqlBaseCalificacionesXCumpliMetasDao "+e.toString() );
				resp = ConstantesBD.codigoNuncaValido;
		}
		return resp;
	}
	
}
