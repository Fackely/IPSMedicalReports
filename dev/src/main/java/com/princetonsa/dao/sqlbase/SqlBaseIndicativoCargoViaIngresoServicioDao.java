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

public class SqlBaseIndicativoCargoViaIngresoServicioDao
{

	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseIndicativoCargoViaIngresoServicioDao.class);
			
	/**
	 * 
	 */
	private static String cadenaConsultaGruposServiciosProcedimientosInstitucion="SELECT " +
																		" codigo as codigo," +
																		" descripcion as descripcion," +
																		" acronimo as acronimo," +
																		" activo as activo," +
																		" institucion as institucion " +
																	" from grupos_servicios " +
																	" where codigo in (select distinct grupo_servicio from servicios where tipo_servicio='P') and institucion=?  and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
	
	/**
	 * 
	 */
	private static String cadenaConsultaServiciosGrupo="SELECT " +
															" s.codigo as codigo," +
															" s.especialidad as especialidad," +
															" s.especialidad||'-'||s.codigo||' '||rs.descripcion as servicio," +
															" cvis.cargo_solicitud as cargosolicitud," +
															" cvis.cargo_proceso as cargoproceso," +
															" s.tipo_servicio as tiposervicio " +
														" from cargo_via_ingreso_servicio cvis " +
														" inner join servicios s on(cvis.servicio=s.codigo) " +
														" inner join referencias_servicio rs on(rs.servicio =s.codigo and rs.tipo_tarifario=0) " +
														" where cvis.via_ingreso=? and s.grupo_servicio=? and s.tipo_servicio='P' and tipo_paciente=? " +
														
														" UNION "+
														
														"SELECT " +
															" s.codigo as codigo," +
															" s.especialidad as especialidad," +
															" s.especialidad||'-'||s.codigo||' '||rs.descripcion as servicio," +
															" 'N' as cargosolicitud," +
															" 'N' as cargoproceso," +
															" s.tipo_servicio as tiposervicio " +
														" from servicios s " +
														" inner join referencias_servicio rs on(rs.servicio =s.codigo and rs.tipo_tarifario=0) " +
														" where grupo_servicio = ? and s.tipo_servicio='P' " +
														" and s.codigo not in(select temp.servicio from cargo_via_ingreso_servicio temp where temp.via_ingreso=?) ";
	
	/**
	 * 
	 */
	private static String cadenaUpdateServicio="UPDATE cargo_via_ingreso_servicio set cargo_solicitud=?, cargo_proceso=? where via_ingreso=? and servicio=? and institucion=? and tipo_paciente=?";
	
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarGruposServiciosProcedimientosInstitucion(Connection con, String codigoInstitucion)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=cadenaConsultaGruposServiciosProcedimientosInstitucion;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" ORDER BY descripcion ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR CONSULTADO LOS GRUPOS DE SERVICIOS CON PROCEDIMIENTOS POR INSTITUCION");
			e.printStackTrace();
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param grupoServicio
	 * @param viaIngreso
	 * @return
	 */
	public static HashMap consultarServiciosGrupoServicioViaIngreso(Connection con, String grupoServicio, String viaIngreso, String tipoPaciente)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=cadenaConsultaServiciosGrupo;
		String [] vector;
		vector = tipoPaciente.split(ConstantesBD.separadorSplit);
		try
		{
			logger.info("\n\n\n consultarServiciosGrupoServicioViaIngreso-->"+cadena+" ORDER BY 3 -> via:"+viaIngreso+" grupoServ->"+grupoServicio+" vector0->"+vector[0]+"\n\n\n");
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" ORDER BY 3 ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, viaIngreso);
			ps.setString(2, grupoServicio);
			ps.setString(3, vector[0]);
			ps.setString(4, grupoServicio);
			ps.setString(5, viaIngreso);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.error("ERROR CONSULTADO LOS SERVICIOS DEL GRUPO - VIA INGRESO");
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion
	 * @param cargoSolicitud
	 * @param cargoProceso
	 * @param cadena 
	 * @return
	 */
	public static boolean actualizarServicioProcedimientoViaIngreso(Connection con, String viaIngreso, String servicio, String institucion, String tipoPaciente, String cargoSolicitud, String cargoProceso, String cadenaInsercion)
	{
		try
		{
			if(existeRegistro(con,viaIngreso,servicio,institucion, tipoPaciente))
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, cargoSolicitud);
				ps.setString(2, cargoProceso);
				ps.setString(3, viaIngreso);
				ps.setString(4, servicio);
				ps.setString(5, institucion);
				ps.setString(6, tipoPaciente);
				ps.executeUpdate();
				ps.close();
				return true;
			}
			else
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, viaIngreso);
				ps.setString(2, servicio);
				ps.setString(3, institucion);
				ps.setString(4, cargoSolicitud);
				ps.setString(5, cargoProceso);
				ps.setString(6, tipoPaciente);
				ps.executeUpdate();
				ps.close();
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.error("ERROR ACTUALIZANDO/INSERTANDO LOS SERVICIOS");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion
	 * @return
	 */
	private static boolean existeRegistro(Connection con, String viaIngreso, String servicio, String institucion, String tipoPaciente)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT servicio from cargo_via_ingreso_servicio where via_ingreso=? and servicio=? and institucion=? and tipo_paciente=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, viaIngreso);
			ps.setString(2, servicio);
			ps.setString(3, institucion);
			ps.setString(4, tipoPaciente);
			boolean resultadoBool=((new ResultSetDecorator(ps.executeQuery())).next());
			ps.close();
			return resultadoBool;
		}
		catch (SQLException e)
		{
			logger.error("ERROR VERIFICANDO LA EXISTENCIA DEL REGISTRO");
			e.printStackTrace();
		}
		return false;
	}
	
	

}
