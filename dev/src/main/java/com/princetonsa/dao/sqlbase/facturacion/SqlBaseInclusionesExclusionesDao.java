package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.Utilidades;

public class SqlBaseInclusionesExclusionesDao {
	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBasePaquetesDao.class);
	
	
	/**
	 * Cadena para la eliminacion
	 */
	
	
	
	private static final String cadenaConsultaStr=  "SELECT " +
																" p.codigo as codigo," +
																" p.institucion as institucion," +
																" p.nombre as nombre," +
																" 'BD' as tiporegistro " +
													" from inclusiones_exclusiones p " +
													"where p.institucion=?";
	
	
	
	/**
	 * 
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM inclusiones_exclusiones WHERE institucion=? and codigo=?";
	
		
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE inclusiones_exclusiones SET usuario_modifica=?, nombre=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? and institucion=?";
	
	
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO inclusiones_exclusiones (codigo, institucion, nombre, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?)";

	
	
	/**
	 * consulta los paquetes existentes
	 * @param con
	 * @param institucion
	 * @return
	 */
	
	public static HashMap consultarIncluExcluExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr;
		
		try
		{
			if(vo.containsKey("codigo"))
			{
				cadena+=" and p.codigo='"+vo.get("codigo")+"'";
			}
			cadena+=" ORDER BY codigo ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
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
	 * @param vo
	 * @return
	 */
	
	public static boolean insertar(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO inclusiones_exclusiones 
			 * (codigo, institucion, nombre, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, vo.get("codigo")+"");
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(3, vo.get("nombre")+"");
			ps.setString(4, vo.get("usuario_modifica")+"");
			ps.setDate(5, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(6, vo.get("hora_modifica")+"");
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
			
			/**
			 * UPDATE inclusiones_exclusiones SET usuario_modifica=?, nombre=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? and institucion=?
			 */
			
			ps.setString(1, vo.get("usuario_modifica")+"");
			ps.setString(2, vo.get("nombre")+"");
			ps.setDate(3, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(4, vo.get("hora_modifica")+"");
			ps.setString(5, vo.get("codigo")+"");
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("institucion")+""));
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
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public static boolean eliminarRegistro(Connection con, int institucion, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2, codigo);
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
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> consultarIncluExcluExistentes(Connection con, int institucion)
	{
		String cadena="select codigo,nombre from inclusiones_exclusiones where institucion=?";
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

}
