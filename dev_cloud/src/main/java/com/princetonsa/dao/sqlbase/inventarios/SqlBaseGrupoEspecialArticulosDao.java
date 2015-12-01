package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseGrupoEspecialArticulosDao 
{
	
	
	/**
	 * 
	 */
	Logger logger = Logger.getLogger(SqlBaseGrupoEspecialArticulosDao.class);
	
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaEspecificoStr=  "SELECT " +
																" codigo as codigo," +
																" descripcion as descripcion," +
																" codigo_pk as codigopk," +
																" 'BD' as tiporegistro " +
													" from grupo_especial_articulos " +
													"where codigo_pk=?";
	
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaStr=  "SELECT " +
																		" codigo as codigo," +
																		" descripcion as descripcion," +
																		" codigo_pk as codigopk," +
																		" 'BD' as tiporegistro " +
																	" from grupo_especial_articulos ";
	
	
	/**
	 * Cadena para la eliminacion
	 */
	private static final String cadenaEliminacionStr="DELETE FROM grupo_especial_articulos WHERE codigo_pk=?";
	
		
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE grupo_especial_articulos SET codigo=?, descripcion=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo_pk=? ";
	
	
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO grupo_especial_articulos (codigo_pk, codigo, descripcion, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?)";
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public static HashMap consultarMotivoEspecifico(Connection con, int codigoGrupo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoGrupo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarMotivosExistentes(Connection con) 
	{
		HashMap mapa=new HashMap();
		String cadena = cadenaConsultaStr;
		
		cadena += " ORDER BY codigo ASC ";
			
		mapa.put("numRegistros","0");
		try
		{			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
			
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int codigo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertar(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigoGrupo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_grupo_especial_articulos");
			
			ps.setInt(1, codigoGrupo);
			ps.setString(2, vo.get("codigo").toString());
			ps.setString(3, vo.get("descripcion").toString());
				
			ps.setString(4, vo.get("usuario_modifica").toString());
			ps.setDate(5, Date.valueOf(vo.get("fecha_modifica").toString()));
			ps.setString(6, vo.get("hora_modifica").toString());
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificar(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("codigo")+"");
			ps.setString(2, vo.get("descripcion")+"");
			ps.setString(3, vo.get("usuario_modifica").toString());
			ps.setDate(4, Date.valueOf(vo.get("fecha_modifica").toString()));
			ps.setString(5, vo.get("hora_modifica").toString());
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigopk").toString()));
			
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	
}
