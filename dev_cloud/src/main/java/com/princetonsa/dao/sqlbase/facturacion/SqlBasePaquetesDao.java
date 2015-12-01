package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;


import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;


/**
 * Consultas estandar de paquetes 
 * @author axioma
 *
 */

public class SqlBasePaquetesDao 
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBasePaquetesDao.class);
	
	
	/**
	 * Cadena para la eliminacion
	 */
	
	
	
	private static final String cadenaConsultaStr=  "SELECT " +
																" p.codigo_paquete as codigopaquete," +
																" p.codigo_paquete as codigopaqueteoriginal," +
																" p.institucion as institucion," +
																" p.descripcion as descripcion," +
																" p.servicio as servicio," +
																" s.especialidad ||' - '||p.servicio||' '|| getnombreservicio(p.servicio,0) as nomservicio," +
																" p.distribucion_costo as distribucioncosto," +
																" p.cuenta_cont_may_val as cuentacontmayval," +
																" p.cuenta_cont_men_val as cuentacontmenval," +
																" 'BD' as tiporegistro " +
													" from paquetes p " +
													" inner join servicios s ON (s.codigo=p.servicio) " +
													"where p.institucion=?";
	
	
	
	/**
	 * 
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM paquetes WHERE institucion=? and codigo_paquete=?";
	
		
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE paquetes SET usuario_modifica=?, descripcion=?, servicio=?, distribucion_costo=?, cuenta_cont_may_val=?, cuenta_cont_men_val=?, fecha_modifica=?, hora_modifica=?, codigo_paquete=?  WHERE codigo_paquete=? and institucion=?";
	
	
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO paquetes (codigo_paquete, institucion, descripcion, servicio, distribucion_costo, cuenta_cont_may_val, cuenta_cont_men_val, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	
	
	/**
	 * consulta los paquetes existentes
	 * @param con
	 * @param institucion
	 * @return
	 */
	
	public static HashMap consultarPaquetesExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr;
		
		try
		{
			if(vo.containsKey("codigoPaquete"))
			{
				cadena+=" and p.codigo_paquete='"+vo.get("codigoPaquete")+"'";
			}
			cadena+=" ORDER BY codigo_paquete ";
			
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
			 * INSERT INTO paquetes (
			 * codigo_paquete, 
			 * institucion, 
			 * descripcion, 
			 * servicio, 
			 * distribucion_costo, 
			 * cuenta_cont_may_val, 
			 * cuenta_cont_men_val, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, vo.get("codigo_paquete")+"");
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(3, vo.get("descripcion")+"");
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("servicio")+""));
			if(UtilidadTexto.isEmpty(vo.get("distribucion_costo")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("distribucion_costo")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("cuenta_cont_may_val")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("cuenta_cont_may_val")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cuenta_cont_men_val")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("cuenta_cont_men_val")+""));
			
			ps.setString(8, vo.get("usuario_modifica")+"");
			ps.setDate(9, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(10, vo.get("hora_modifica")+"");
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
			 * UPDATE paquetes SET 
			 * usuario_modifica=?, 
			 * descripcion=?, 
			 * servicio=?, 
			 * distribucion_costo=?, 
			 * cuenta_cont_may_val=?, 
			 * cuenta_cont_men_val=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?, 
			 * codigo_paquete=?  
			 * WHERE codigo_paquete=? and institucion=?
			 */
			
			ps.setString(1, vo.get("usuario_modifica")+"");
			ps.setString(2, vo.get("descripcion")+"");
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("servicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("distribucion_costo")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("distribucion_costo")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("cuenta_cont_may_val")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cuenta_cont_may_val")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("cuenta_cont_men_val")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("cuenta_cont_men_val")+""));
			
			ps.setDate(7, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(8, vo.get("hora_modifica")+"");
			ps.setString(9, vo.get("codigo_paquete")+"");
			ps.setString(10, vo.get("codigo_paquete_original")+"");
			ps.setInt(11, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
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
	
	public static boolean eliminarRegistro(Connection con, int institucion, String codigoPaquete)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2, codigoPaquete);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
