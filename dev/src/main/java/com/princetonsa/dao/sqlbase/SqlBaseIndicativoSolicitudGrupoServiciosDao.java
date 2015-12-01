package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

public class SqlBaseIndicativoSolicitudGrupoServiciosDao
{
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseIndicativoSolicitudGrupoServiciosDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsultaGruposServiciosInstitucion="SELECT " +
																		" codigo as codigo," +
																		" descripcion as descripcion," +
																		" acronimo as acronimo," +
																		" activo as activo," +
																		" institucion as institucion," +
																		" tipo " +
																	" from grupos_servicios " +
																	" where institucion=? and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+""; 

	/**
	 * 
	 */
	private static String cadenaConsultaServiciosGrupo="SELECT " +
															" s.codigo as codigo," +
															" s.especialidad as especialidad," +
															" getcodigopropservicio2(s.codigo,0)||' '||rs.descripcion as servicio," +
															" s.toma_muestra as tomamuestra," +
															" s.respuesta_multiple as respuestamultiple," +
															" s.tipo_servicio as tiposervicio " +
														" from servicios s " +
														" inner join referencias_servicio rs on(rs.servicio =s.codigo and rs.tipo_tarifario=0) " +
														" where grupo_servicio = ? ";
	
	/**
	 * 
	 */
	private static String cadenaUpdateServicio="UPDATE servicios SET toma_muestra=?,respuesta_multiple=? where codigo=? ";
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param tipo
	 */
	public static HashMap consultarGruposServiciosInstitucion(Connection con, String institucion, String tipo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=cadenaConsultaGruposServiciosInstitucion;
		if(!tipo.trim().equals(""))
		{
			cadena+=" and upper(tipo)=upper('"+tipo+"')";
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" ORDER BY descripcion ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR CONSULTADO LOS GRUPOS DE SERVICIOS POR INSTITUCION");
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param grupoServicio
	 * @param tipoServicio
	 * @return
	 */
	public static HashMap consultarServiciosGrupoServicioTipo(Connection con, String grupoServicio, String tipoServicio)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=cadenaConsultaServiciosGrupo;
		if(!tipoServicio.trim().equals(""))
		{
			cadena+=" and s.tipo_servicio='"+tipoServicio+"'";
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by rs.descripcion ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, grupoServicio);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR CONSULTADO LOS SERVICIOS DE UN GRUPO DE SERVICIOS");
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tomaMuestra
	 * @param respuestaMultiple
	 * @return
	 */
	public static boolean actualizarServicioProcedimiento(Connection con, String codigoServicio, String tomaMuestra, String respuestaMultiple)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, tomaMuestra);
			ps.setString(2, respuestaMultiple);
			ps.setString(3, codigoServicio);
			ps.executeUpdate();
			ps.close();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("ERROR ACTUALIZANDO LOS SERVICIOS");
			e.printStackTrace();
		}
		return false;
	}

}
