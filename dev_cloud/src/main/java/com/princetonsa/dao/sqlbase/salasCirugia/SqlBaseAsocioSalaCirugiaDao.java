package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com  
 * */

public class SqlBaseAsocioSalaCirugiaDao
{
	
	/**
	 * Cadena de Consulta de Asocios
	 * */
	private static final String strConsultaAsocioSalaCirugia = "SELECT " +
				"ta.codigo," +
				"ta.institucion," +
				"ta.codigo_asocio AS codigoasocio," +			
				"ta.codigo_asocio AS codigoasocioold," +
				"ta.nombre_asocio AS nombreasocio," +
				"ta.tipos_servicio AS tiposservicio," +
				"ts.nombre AS nombretiposervicio," +
				"ta.participa_cir AS participacir," +
				"'"+ConstantesBD.acronimoSi+"' AS estabd, " +
				"coalesce(ta.centro_costo_ejecuta, "+ConstantesBD.codigoNuncaValido+") AS ccejecuta " +
			"FROM " +
				"tipos_asocio ta " +
			"INNER JOIN " +
				"tipos_servicio ts ON (ts.acronimo = ta.tipos_servicio) " +
			"WHERE " +
				"institucion = ?  AND ta.tipos_servicio <> '"+ConstantesBD.codigoServicioProcedimiento+"' ";
	
	
	/**
	 * Cadena de Actualizacion de Asocios
	 * */
	private static final String strActualizacionAsocioSalaCirugia = " UPDATE " +
				"tipos_asocio " +
			"SET " +
				"nombre_asocio = ?, " +
				"tipos_servicio = ?, " +
				"centro_costo_ejecuta = ?, " +
				"participa_cir = ? " +
			"WHERE " +
				"codigo_asocio = ? AND institucion = ? ";	
	
	
	/**
	 * Cadena de Eliminacion de Asocios
	 * */
	private static final String srtEliminacionAsocioSalaCirugia = "DELETE FROM tipos_asocio WHERE institucion = ? AND codigo_asocio = ?";
	
	
	/**
	 * Cadena de Consulta de Tipos de Servicio
	 * */
	private static final String strConsultaTiposServicios = "SELECT " +
			"acronimo || '', " +
			"nombre " +
			"FROM  " +
			"tipos_servicio " +
			"WHERE acronimo IN ('"+ConstantesBD.codigoServicioHonorariosCirugia+"','"+ConstantesBD.codigoServicioSalaCirugia+"','"+ConstantesBD.codigoServicioMaterialesCirugia+"') ";
	
	
	
	/**
	 * Vector de Indices del Mapa
	 * */
	private static final String [] indices_mapa = {"codigo_","institucion_","codigoasocio_","codigoasocioold_","nombreasocio_","tiposservicio_","nombretiposervicio_","participacir_","esUsado_","estabd_","ccejecuta_"};
	
	
	
	//-------------------Metodos
	
	/**
	 * Consulta la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param int institucion 
	 * */
	public static HashMap consultaAsocioSalaCirugia(Connection con, int institucion)
	{
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros","0");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaAsocioSalaCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
			respuesta.put("INDICES_MAP",indices_mapa);
			
			return respuesta;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return respuesta;		
	}
	
	
	/**
	 * Consulta la informacion de los asocios de Cirugia por medio del acronimo
	 * @param Connection con
	 * @param int institucion 
	 * @param String codigoAsocio
	 * */
	public static HashMap consultaAsocioSalaCirugia(Connection con, int institucion,String codigoAsocio)
	{
		HashMap respuesta = new HashMap();
		respuesta.put("numRegistros","0");
		
		String cadena = strConsultaAsocioSalaCirugia +" AND ta.codigo_asocio = ? ";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			ps.setString(2,codigoAsocio);
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
			respuesta.put("INDICES_MAP",indices_mapa);
			
			return respuesta;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return respuesta;		
	}
	
	
	
	/**
	 * Actualiza la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarAsocioSalaCirugia(Connection con, HashMap parametros)
	{				
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizacionAsocioSalaCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			/**
			 * UPDATE " +
			"tipos_asocio " +
			"SET " +
			"nombre_asocio = ?, " +
			"tipos_servicio = ?," +
			"participa_cir = ? " +
			"WHERE codigo_asocio = ? " +	
			"AND institucion = ? 
			 */
			
			ps.setString(1,parametros.get("nombreasocio")+"");
			ps.setString(2,parametros.get("tiposservicio")+"");
			if(Utilidades.convertirAEntero(parametros.get("ccejecuta")+"")>=0)
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("ccejecuta")+""));
			else
				ps.setObject(3,null);
			ps.setString(4,parametros.get("participacir")+"");
			
			ps.setString(5,parametros.get("codigoasocioold")+"");
			ps.setInt(6,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			
			if(ps.executeUpdate()>0)
				return true; 			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;		
	}
	
	
	
	/**
	 * Inserta informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean insertarAsocioSalaCirugia(Connection con, HashMap parametros,String cadenaSql)
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tipos_asocio(
			 * codigo,
			 * institucion,
			 * codigo_asocio,
			 * nombre_asocio,
			 * tipos_servicio,
			 * participa_cir,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) VALUES ('seq_tipos_asocio'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ps.setString(2,parametros.get("codigoasocio")+"");
			ps.setString(3,parametros.get("nombreasocio")+"");
			ps.setString(4,parametros.get("tiposservicio")+"");
			if(Utilidades.convertirAEntero(parametros.get("ccejecuta")+"")>=0)
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get("ccejecuta")+""));
			else
				ps.setObject(5,null);
			ps.setString(6,parametros.get("participacir")+"");			
			ps.setString(7,parametros.get("usuariomodifica")+"");
			ps.setDate(8,Date.valueOf(parametros.get("fechamodifica")+""));
			ps.setString(9,parametros.get("horamodifica")+"");			
			
			if(ps.executeUpdate()>0)
				return true; 			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;		
	}
	
	/**
	 * Elimina informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param int institucion
	 * @param String codigoAsocio
	 * */
	public static boolean eliminaAsocioSalaCirugia(Connection con,int institucion,String codigoAsocio)
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(srtEliminacionAsocioSalaCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			ps.setString(2,codigoAsocio);						
			
			if(ps.executeUpdate()>0)
				return true; 			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;		
	}
	
	
	/**
	 * Consulta de Tipos de Servicio
	 * @param Connection con
	 * */
	public static ArrayList<HashMap<String,Object>> consultaTiposServicios(Connection con)
	{
		ArrayList<HashMap<String,Object>> respuesta = new ArrayList<HashMap<String,Object>>();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs ;
			HashMap mapa;
			
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				mapa = new HashMap();
				mapa.put("acronimo",rs.getObject(1));
				mapa.put("nombre",rs.getObject(2));
				
				respuesta.add(mapa);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return respuesta;
	}
}