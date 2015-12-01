package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseNaturalezaArticulosDao {
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBaseNaturalezaArticulosDao.class);
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaStr= "SELECT " +
															"acronimo as acronimo, nombre as nombre," +
															"case when es_pos='"+ValoresPorDefecto.getValorTrueParaConsultas()+"' or es_pos='"+ConstantesBD.acronimoSi+"' then '"+ConstantesBD.acronimoSi+"' when es_pos='"+ValoresPorDefecto.getValorFalseParaConsultas()+"' or es_pos='"+ConstantesBD.acronimoNo+"' then '"+ConstantesBD.acronimoNo+"' else '' end as espos, codigo_rips as codigorips," + 
															"codigo_interfaz as codigo_interfaz, institucion as institucion, " + 
															"'"+ConstantesBD.acronimoSi+"' as tiporegistro," +
															"coalesce(es_medicamento, '"+ConstantesBD.acronimoSi+"') as esmedicamento " +
															"FROM naturaleza_articulo ";
	
	/**
	 *Cadena para la eliminacion 
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM naturaleza_articulo WHERE institucion=? and acronimo=?";
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE naturaleza_articulo SET nombre=?, es_pos=?, codigo_rips=?, codigo_interfaz=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?, es_medicamento=? WHERE acronimo=?";
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO naturaleza_articulo (acronimo, nombre, es_pos, codigo_rips, codigo_interfaz, institucion, usuario_modifica, fecha_modifica, hora_modifica, es_medicamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	

	/**
	 * consulta naturaleza de articulos existentes
	 * @param con
	 * @param institucion
	 * @return
	 */
	
	public static HashMap consultarNaturalezaArticulosExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr;
		
		try
		{
			if(vo.containsKey("acronimo")&&!UtilidadTexto.isEmpty(vo.get("acronimo")+""))
			{
				cadena+=" WHERE acronimo='"+vo.get("acronimo")+"'";
			}
			cadena+=" ORDER BY acronimo ";
			logger.info(cadena);
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
	 * Insertar
	 * */
	
	public static boolean insertar(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO naturaleza_articulo (
			 * acronimo, 
			 * nombre, 
			 * es_pos, 
			 * codigo_rips, 
			 * codigo_interfaz, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica, 
			 * es_medicamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, vo.get("acronimo")+"");
			ps.setString(2, vo.get("nombre")+"");
			if(UtilidadTexto.isEmpty(vo.get("es_pos")+""))
				ps.setNull(3, Types.CHAR);
			else
			{
				//me toco hacer esto porque no hicieron el cambio en codigo y me dieron solamente 30 min para solucionarlo
				if(UtilidadTexto.getBoolean(vo.get("es_pos")+""))
					ps.setString(3, ValoresPorDefecto.getValorTrueCortoParaConsultas());
				else
					ps.setString(3, ValoresPorDefecto.getValorFalseCortoParaConsultas());
			}
			if(UtilidadTexto.isEmpty(vo.get("codigo_rips")+""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, vo.get("codigo_rips")+"");
			if(UtilidadTexto.isEmpty(vo.get("codigo_interfaz")+""))
				ps.setNull(5, Types.CHAR);
			else
				ps.setString(5, vo.get("codigo_interfaz")+"");
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(7, vo.get("usuario_modifica")+"");
			ps.setDate(8, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(9, vo.get("hora_modifica")+"");
			ps.setString(10, vo.get("esmedicamento")+"");
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Modificar
	 * */
	public static boolean modificar(Connection con, HashMap vo)
	{
	  		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE naturaleza_articulo SET 
			 * nombre=?, 
			 * es_pos=?, 
			 * codigo_rips=?, 
			 * codigo_interfaz=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?, 
			 * es_medicamento=? 
			 * WHERE acronimo=?
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			if(UtilidadTexto.isEmpty(vo.get("es_pos")+""))
				ps.setNull(2, Types.CHAR);
			else
			{
				if(UtilidadTexto.getBoolean(vo.get("es_pos")+""))
					ps.setString(2, ValoresPorDefecto.getValorTrueParaConsultas());
				else
					ps.setString(2, ValoresPorDefecto.getValorFalseParaConsultas());
			}	
			ps.setString(3, vo.get("codigo_rips")+"");
			ps.setString(4, vo.get("codigo_interfaz")+"");
			ps.setString(5, vo.get("usuario_modifica")+"");
			ps.setDate(6, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(7, vo.get("hora_modifica")+"");
			ps.setString(8, vo.get("esmedicamento")+"");
			ps.setString(9, vo.get("acronimo")+"");
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Eliminar
	 * */
	public static boolean eliminarRegistro(Connection con, int institucion, String acronimo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2, acronimo);
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
	 * @param acronimoNaturaleza
	 * @return
	 */
	public static boolean esMedicamento(String acronimoNaturaleza)
	{
		Connection con=UtilidadBD.abrirConexion();
		String consulta="SELECT coalesce(es_medicamento, '"+ConstantesBD.acronimoSi+"') from naturaleza_articulo where acronimo='"+acronimoNaturaleza+"'";
		logger.info("esMedicamento-->"+consulta);
		boolean retorna=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				retorna= UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		logger.info("esMed-->"+retorna);
		return retorna;
	}
	
}
