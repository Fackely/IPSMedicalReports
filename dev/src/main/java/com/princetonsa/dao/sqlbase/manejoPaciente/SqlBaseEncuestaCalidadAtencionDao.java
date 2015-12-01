package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.EncuestaCalidadAtencion;

/**
 * 
 * Date: 2008-01-16
 * @author garias@princetonsa.com - lgchavez@princetonsa.com
 */
public class SqlBaseEncuestaCalidadAtencionDao 
{
	//*********************** Atributos
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseEncuestaCalidadAtencionDao.class);
	
	
	/**
	 *  Cadena para consultar todas las secciones
	 */
	private static final String cadenaConsultaStr = "SELECT " +
														"	getnomcentroatencion(i.centro_atencion) as centroatencion," +
														"	i.centro_atencion as codigo_centroatencion," +
														"	case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1  " +
														"	then getnombreviaingreso(c.via_ingreso)||' / '||getnombretipopaciente(c.tipo_paciente) " +
														"	else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  " +
														"	i.consecutivo as noingreso," +
														"	i.estado as estadoing," +
														"	i.id as ingresoid," +
														"	i.fecha_ingreso as fechaing," +
														"	i.fecha_egreso as fechaegr," +
														"	c.via_ingreso || '@@@@@' || c.tipo_paciente as codviaingresotipopac" +
														" FROM " +
														"	ingresos i " +
														"		INNER JOIN cuentas c ON (i.id=c.id_ingreso) " +
														" WHERE i.estado IN ('ABI','CER') and i.codigo_paciente=?" +
														" and getcuentafinalasocio(i.id,c.id) is null " +
														" ORDER BY i.fecha_ingreso " +
														" "; 
	
	private static final String cadenaConsultaEncuestasStr = "SELECT " +
																"	ec.codigo as codigo," +
																"	ec.ingreso as ingreso," +
																"	ec.area as area,  " +
																"	ec.fecha_modifica as fecha ," +
																"	ec.hora_modifica as hora," +
																"	ec.calificacion as calificacion," +
																"	ec.motivo_calificacion as motivo," +
																"	ec.usuario_modifica as usuariom," +
																"	eco.observaciones " +
																" FROM " +
																"	encuesta_calidad ec" +
																"	left outer join encuesta_calidad_observ eco on (ec.codigo=eco.encuesta_calidad) " +
																" WHERE " +
																"	ec.ingreso=? ";
																
	

	
	/**
	 *  Vector de indices de la consulta de ingresos
	 */
	private static final String [] indicesMap = {	"centroAtencion_",
													"noingreso_",
													"estadoing_",
													"fechaing_",
													"fechaegr_" ,
													"viaingresotipopac_",
													"codigo_centroatencion_",
													"codviaingresotipopac_"
													};

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultar(Connection con, EncuestaCalidadAtencion encuesta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(encuesta.getPaciente()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("INDICES_MAPA", indicesMap);
			logger.info("\n\n\n\n Consulta Ingresos >>>"+cadenaConsultaStr+"	paciente >>>"+encuesta.getPaciente());
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de ingresos >>>"+e+"\n\n\n Sentencia"+cadenaConsultaStr);
		}
		return mapa;
	}
	
	
	
	public static HashMap consultarEncuestas(Connection con, int ingreso, String area) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String cadena =cadenaConsultaEncuestasStr;
		
		if(area.equals(""))
		{
			cadena+= " AND area is null";
		}
		else
		{
			cadena+=" AND area="+area ;
		}
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,ingreso);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n Consulta encuestas >>>"+cadenaConsultaEncuestasStr+"	ingreso >>>"+ingreso+" area>>>>"+area);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de encuestas >>>"+e+"\n\n\n Sentencia"+cadenaConsultaEncuestasStr);
		}
		return mapa;
	}
	
	
	
	public static int guardarEncuestas(Connection con, HashMap encuesta) 
	{
		int y=0;
		
		logger.info("\n\n\n\n\n\n   mapa encuesta >>>>>"+encuesta+"\n\n\n\n\n");
		
		String cadenaInsertar=" INSERT INTO " +
										" encuesta_calidad (" +
										"	codigo," +
										"	ingreso," +
										"	area ,  " +
										"	fecha_modifica," +
										"	hora_modifica," +
										"	calificacion," +
										"	motivo_calificacion," +
										"	usuario_modifica" +
										")" +
										"VALUES (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,? ) ";
										
		 
		
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_encuesta_calidad"));
			ps.setInt(2, Utilidades.convertirAEntero(encuesta.get("ingreso")+""));
			if(UtilidadTexto.isEmpty(encuesta.get("area")+""))
				ps.setNull(3, Types.INTEGER);
			else
				ps.setInt(3, Utilidades.convertirAEntero(encuesta.get("area")+""));
			ps.setString(4, encuesta.get("calificacion_0")+"");
			ps.setString(5, encuesta.get("motivo_0")+"");
			ps.setString(6, encuesta.get("usuario")+"");
			
			
			y=ps.executeUpdate();
			logger.info("\n\n\n\n insertar encuestas >>>"+cadenaInsertar);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la insercion de encuestas >>>"+e+"\n\n\n Sentencia"+cadenaInsertar);
		}
		
		if (y==1)
		{
			y=0;
			
			if (encuesta.containsKey("observaciones") && !encuesta.get("observaciones").toString().equals(""))
			{
				HashMap mapa=new HashMap();
				mapa.put("numRegistros","0");
				String cadenaConsultaultimo="";
				if((encuesta.get("area")+"").equals(""))
				{	
					cadenaConsultaultimo="select max(codigo) as codigo " +
													"from encuesta_calidad " +
													"	where ingreso="+encuesta.get("ingreso")+" and area is null";
				}
				else
				{
					cadenaConsultaultimo="select max(codigo) as codigo " +
													"from encuesta_calidad " +
													"	where ingreso="+encuesta.get("ingreso")+" and area="+encuesta.get("area");
				}
				try
				{
					PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaultimo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery()));
				}
				catch (SQLException e)
				{
					logger.info("\n\n\n Error en la consulta de encuestas >>>"+e+"\n\n\n Sentencia"+cadenaConsultaultimo);
				}
				
				logger.info("cadena consulta >>>"+cadenaConsultaultimo);
				
				
				String cadenaInsertarob=" INSERT INTO " +
											" encuesta_calidad_observ (" +
											"	codigo," +
											"	encuesta_calidad," +
											"	observaciones ,  " +
											"	fecha_modifica," +
											"	hora_modifica," +
											"	usuario_modifica" +
											")" +
											"VALUES (" +
											""+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_encuesta_calidad_observ")+","+
											""+mapa.get("codigo_0")+","+
											"'"+encuesta.get("observaciones")+"',"+
											" CURRENT_DATE ,"+
											" "+ValoresPorDefecto.getSentenciaHoraActualBD()+","+
											"'"+encuesta.get("usuario")+"'"+
											")";
				 			
				try
				{
				PreparedStatementDecorator psob= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarob,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				y=psob.executeUpdate();
				logger.info("\n\n\n\n insertar observaciones >>>"+cadenaInsertarob);
				}
				catch (SQLException e)
				{
				logger.info("\n\n\n Error en la insercion de observaciones >>>"+e+"\n\n\n Sentencia"+cadenaInsertarob);
				}
			}
		}
		
		
		return y;
	}
	
	
	
	
	public static int modificarEncuestas(Connection con, HashMap encuesta) 
	{
		int y=0;
		
		logger.info("\n\n\n\n\n\n   mapa encuesta >>>>>"+encuesta+"\n\n\n\n\n");
			String cadenaInsertarob=" INSERT INTO " +
										" encuesta_calidad_observ (" +
										"	codigo," +
										"	encuesta_calidad," +
										"	observaciones ,  " +
										"	fecha_modifica," +
										"	hora_modifica," +
										"	usuario_modifica" +
										")" +
										"VALUES (" +
										""+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_encuesta_calidad_observ")+","+
										""+encuesta.get("codigo_0")+","+
										"'"+encuesta.get("observaciones")+"',"+
										" CURRENT_DATE ,"+
										" "+ValoresPorDefecto.getSentenciaHoraActualBD()+","+
										"'"+encuesta.get("usuario")+"'"+
										")";
			 			
			try
			{
			PreparedStatementDecorator psob= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarob,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			y=psob.executeUpdate();
			logger.info("\n\n\n\n insertar observaciones >>>"+cadenaInsertarob);
			}
			catch (SQLException e)
			{
			logger.info("\n\n\n Error en la insercion de observaciones >>>"+e+"\n\n\n Sentencia"+cadenaInsertarob);
			}
		
		return y;
	}			
}