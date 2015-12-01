package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturacion.ServiciosGruposEsteticos;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * 
 * @author axioma
 *
 */

public class SqlBaseServiciosGruposEsteticosDao
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBaseServiciosGruposEsteticosDao.class);
	
	
	/**
	 * Cadena para la eliminacion
	 */
	
	
	
	private static final String cadenaConsultaStr=  "SELECT " +
																" p.codigo as codigo," +
																" p.codigo as codigoantiguo," +
																" p.institucion as institucion," +
																" p.descripcion as descripcion," +
																" p.activo as activo," +
																" 'BD' as tiporegistro " +
													" from servicios_grupos_esteticos p " +
													"where p.institucion=?";
	
	
	
	/**
	 * Cadena para la Eliminacion
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM servicios_grupos_esteticos WHERE institucion=? and codigo=?";
	
	
	
	/**
	 * Cadena de modificacion, permite modificar codigo
	 * */ 
	private static final String cadenaModificacionTStr = "UPDATE servicios_grupos_esteticos SET codigo=?, descripcion=?, activo=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? and institucion=?";
	
	
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO servicios_grupos_esteticos (codigo, institucion, descripcion, activo, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";

	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final String cadenaEliminacionServicio="DELETE FROM serv_esteticos WHERE institucion=? and servicio=?";
	
	/*private static final String cadenaModificacionServicio="UPDATE serv_esteticos WHERE institucion=?, servicio=? and grupos_estetico=?";*/
	
	private static final String cadenaInsertarServicio="INSERT INTO serv_esteticos (institucion, servicio, grupos_esteticos) VALUES (?,?,?)";
	
	private static final String cadenaConsultaServicio=  "SELECT " +
																" e.institucion as institucion," +
																" e.servicio as codigo_servicio," +
																" s.especialidad ||' - '||e.servicio||' '|| getnombreservicio(e.servicio,0) as descripcion_servicio," +
																" e.grupos_esteticos as gruposesteticos," +
																" 'BD' as tiporegistro " +
																" from serv_esteticos e " +
																" inner join servicios s ON (s.codigo=e.servicio) " +
																"where e.institucion=?";
	
	
	
	
	
	
	/**
	 * consulta los servicios por grupos esteticos existentes
	 * @param con
	 * @param institucion
	 * @return
	 */
	
	public static HashMap consultarServiciosEsteticosExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaServicio;
		
		try
		{
			if(vo.containsKey("codigo"))
			{
				cadena+=" and e.grupos_esteticos='"+vo.get("codigo")+"'";
			}
			cadena+=" ORDER BY 3 ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
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
			 * INSERT INTO servicios_grupos_esteticos (
			 * codigo, 
			 * institucion, 
			 * descripcion, 
			 * activo, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, vo.get("codigo")+"");
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(3, vo.get("descripcion")+"");
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("activo")+"");
			ps.setString(5, vo.get("usuario_modifica")+"");
			ps.setDate(6, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(7, vo.get("hora_modifica")+"");
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionTStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE servicios_grupos_esteticos SET 
			 * codigo=?, 
			 * descripcion=?, 
			 * activo=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?  
			 * WHERE codigo=? and institucion=?
			 */
			
			ps.setString(1, vo.get("codigo")+"");
			ps.setString(2, vo.get("descripcion")+"");
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, vo.get("activo")+"");
			ps.setString(4, vo.get("usuario_modifica")+"");
			ps.setDate(5, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(6, vo.get("hora_modifica")+"");
			ps.setString(7, vo.get("codigoOld")+"");
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("institucion")+""));
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
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean eliminarServicio(Connection con, int institucion, int servicio)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, servicio);
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/*public static boolean modificarServicio(Connection con, HashMap vo)
	{
	  		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("institucion")+"");
			ps.setString(2, vo.get("servicio")+"");
			ps.setString(3, vo.get("grupos_esteticos")+"");
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}*/
	
	
	public static boolean insertarServicio(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("servicio")+""));
			ps.setString(3, vo.get("grupos_esteticos")+"");
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public static HashMap consultarGruposEsteticosExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena2= cadenaConsultaStr;
		
		try
		{
			if(vo.containsKey("codigo"))
			{
				cadena2+=" and p.codigo='"+vo.get("codigo")+"'";
			}
			cadena2+=" ORDER BY p.descripcion ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
		
		
	}
	
	


}
