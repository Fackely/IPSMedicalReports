package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseMotivosSircDao
{
	private static Logger logger=Logger.getLogger(SqlBaseMotivosSircDao.class);
	/**
	 * 
	 */
	private static String cadenaConsultarMotivoSirc="SELECT consecutivo as consecutivo,codigo as codigo,descripcion as descripcion,tipo_motivo as tipomotivo,case when activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo,'BD' as tiporegistro " +
							"from motivos_sirc where 1=1 ";


	/**
	 * 
	 */
	private static String cadenaModificarMotivoSirc="UPDATE motivos_sirc set descripcion=?,tipo_motivo=?,activo=? where consecutivo=?";
	
	/**
	 * 
	 */
	private static String cadenaEliminarMotivoSirc="DELETE FROM motivos_sirc where consecutivo=?";

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarMotivoSirc(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena=cadenaConsultarMotivoSirc;
		if(vo.containsKey("institucion"))
		{
			cadena+=" AND institucion="+vo.get("institucion")+" order by tipo_motivo,descripcion";
		}
		else if(vo.containsKey("consecutivo"))
		{
			cadena+=" AND consecutivo="+vo.get("consecutivo");
		}
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}
		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarMotivoSirc(Connection con, HashMap vo)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarMotivoSirc));
			
			/**
			 * UPDATE motivos_sirc set descripcion=?,tipo_motivo=?,activo=? where consecutivo=?
			 */
			
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("tipomotivo")+"");
			ps.setBoolean(3, UtilidadTexto.getBoolean(vo.get("activo")+""));
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("consecutivo")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}
		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarMotivoSirc(Connection con, HashMap vo,String cadena)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			
			/**
			 * INSERT INTO motivos_sirc (consecutivo,codigo,descripcion,tipo_motivo,activo,institucion) values ('seq_motivos_sirc'),?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.setString(2, vo.get("descripcion")+"");
			ps.setString(3, vo.get("tipomotivo")+"");
			ps.setBoolean(4, UtilidadTexto.getBoolean(vo.get("activo")+""));
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("institucion")+""));
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}
		
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, String consecutivo)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			 ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarMotivoSirc));
			ps.setDouble(1, Utilidades.convertirADouble(consecutivo));
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
		return false;
	}

}
