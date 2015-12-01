package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;


public class SqlBaseActividadesProgramasPYPDao 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseActividadesProgramasPYPDao.class);
	
	
	/**
	 * cadena para consultar las actividad programas pyp
	 */
	private static String cadenaConsutaActividadesPrograma="SELECT ap.codigo as codigo,ap.actividad as actividad,CASE WHEN a.articulo IS NULL THEN '(' || getcodigoespecialidad(a.servicio) || '-' || a.servicio || ') ' || getnombreservicio(a.servicio,"+ConstantesBD.codigoTarifarioCups+") ELSE getdescarticulo(a.articulo) END AS descripcion,CASE WHEN a.articulo IS NULL THEN getnombreservicio(a.servicio,"+ConstantesBD.codigoTarifarioCups+") ELSE getdescarticulosincodigo(a.articulo) END AS descripcion from pyp.actividades_programa_pyp ap inner join actividades_pyp a on(ap.actividad=a.consecutivo) where ap.programa=? and a.institucion=? order by 4";
	
	/**
	 * cadena para consultar una actividad programas pyp
	 */
	private static String cadenaConsutaActividadPrograma="SELECT ap.codigo as codigo,ap.institucion as institucion,ap.programa||'"+ConstantesBD.separadorSplit+"'||ps.descripcion as programa,ap.actividad as actividad,apyp.servicio as servicio,ap.embarazo as embarazo,ap.semanas_gestacion as semgestacion,ap.requerido as requerido,ap.permitir_ejecutar as permitirejecutar,ap.archivo as descarchivo,ap.activo as activo,case when ap.finalidad_consulta is null then '' else ap.finalidad_consulta end as finalidadconsulta,case when ap.finalidad_servicio is null then ''  else ap.finalidad_servicio||'' end  as finalidadservicio from actividades_programa_pyp ap inner join programas_salud_pyp ps on(ap.programa=ps.codigo and ap.institucion=ps.institucion)  inner join actividades_pyp apyp on(ap.actividad=apyp.consecutivo) ";
	
	/**
	 * cadena para actualizar
	 */
	private static String cadenaUpdateActividadPrograma="UPDATE actividades_programa_pyp set actividad=?,embarazo=?,semanas_gestacion=?,requerido=?,archivo=?,activo=?,finalidad_consulta=?,finalidad_servicio=?,permitir_ejecutar=? where codigo=?";

	/**
	 * 
	 */
	private static String eliminarActiProgPYPGrupEtareoGeneral="DELETE FROM acti_prog_pup_grup_eta WHERE codigo_act_prog_pyp=?";
	
	/**
	 * 
	 */
	private static String eliminarActiProgPYPMetasCumpGeneral="DELETE FROM act_prog_pyp_met_cum WHERE codigo_act_prog_pyp=?";
	
	/**
	 * 
	 */
	private static String eliminarActiProgPYPViasIngreso="DELETE FROM acti_prog_pyp_vias_ing WHERE codigo_act_prog_pyp=?";
	
	
	/**
	 * 
	 */
	private static String eliminarActividadesProgramaPYP="DELETE FROM actividades_programa_pyp WHERE codigo=?";
	
	/**
	 * 
	 */
	private static String cadenaConsultarDiagnosticos="select dp.acronimo,dp.tipo_cie as cie,d.nombre,'BD' as tiporegistro from diag_act_prog_pyp dp inner join diagnosticos d on(dp.acronimo=d.acronimo and dp.tipo_cie=d.tipo_cie) where dp.codigo=?";
	
	/**
	 * 
	 */
	private static String cadenaEliminarDiagnosticos="delete from diag_act_prog_pyp where codigo=? and acronimo=? and tipo_cie=?";
	
	/**
	 * 
	 */
	private static String cadenaInsertaDiagnosticos="insert into diag_act_prog_pyp (codigo,acronimo,tipo_cie) values(?,?,?)";
	

	/**
	 * 
	 * @param con
	 * @param institucion , String institucion
	 * @param institucion2 
	 * @return
	 */
	public static HashMap consultarActivadesProgramasPYP(Connection con, String programa, String institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsutaActividadesPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * from pyp.actividades_programa_pyp ap inner join actividades_pyp a on(ap.actividad=a.consecutivo) where ap.programa=? and a.institucion=? order by 4
			 */
			
			ps.setString(1,programa);
			ps.setInt(2,Utilidades.convertirAEntero(institucion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[consultarActivadesProgramasPYP] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @param embarazo
	 * @param semanasGestacion
	 * @param requerido
	 * @param archivo
	 * @param activo
	 * @param sentencia
	 * @param finalidadServicio 
	 * @param finalidadConsulta 
	 * @return
	 */
	public static boolean insertarActividadPrograma(Connection con, int institucion, String programa, String actividad, boolean embarazo, String semanasGestacion, boolean requerido, String archivo, boolean activo, String finalidadConsulta, String finalidadServicio, String sentencia, boolean permitirEjecutar) 
	{
		PreparedStatementDecorator ps = null;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO actividades_programa_pyp
			 * (codigo,
			 * institucion,
			 * programa,
			 * actividad,
			 * embarazo,
			 * semanas_gestacion,
			 * requerido,
			 * archivo,
			 * activo,
			 * finalidad_consulta,
			 * finalidad_servicio,
			 * permitir_ejecutar) VALUES('seq_act_prog_pyp'),?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1,institucion);
			ps.setString(2,programa);
			ps.setDouble(3,Utilidades.convertirADouble(actividad));
			ps.setBoolean(4,embarazo);
			if(!embarazo)
			{
	        	ps.setNull(5,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(5,Utilidades.convertirADouble(semanasGestacion));
			}
			ps.setBoolean(6,requerido);
			ps.setString(7,archivo);
			ps.setBoolean(8,activo);
			if(finalidadConsulta.trim().equals(""))
			{
	        	ps.setNull(9,Types.VARCHAR);
			}
			else
			{
				ps.setString(9,finalidadConsulta);
			}
			if(finalidadServicio.trim().equals(""))
			{
	        	ps.setNull(10,Types.INTEGER);
			}
			else
			{
				ps.setInt(10,Utilidades.convertirAEntero(finalidadServicio));
			}		
			ps.setBoolean(11, permitirEjecutar);
			return ps.executeUpdate()>0;
		} catch (SQLException e) {
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[insertarActividadPrograma] -->");
			e.printStackTrace();
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				logger.error("ERROR cerrando el PreparedStament - insertarActividadPrograma : " + e);
			}
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarActivadProgramaPYP(Connection con, String programa, String actividad, int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena=cadenaConsutaActividadPrograma+" where ap.programa=? and ap.actividad=? and ap.institucion=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,programa);
			ps.setDouble(2,Utilidades.convertirADouble(actividad));
			ps.setInt(3,institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[consultarActivadesProgramasPYP] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}



	/**
	 * 
	 * @param con 
	 * @param codigo
	 * @param actividad
	 * @param embarazo
	 * @param semanasGestacion
	 * @param requerido
	 * @param archivo
	 * @param activo
	 * @param finalidadServicio 
	 * @param finalidadConsulta 
	 * @return
	 */
	public static boolean modifcarActividadPrograma(Connection con, String codigo, String actividad, boolean embarazo, String semanasGestacion, boolean requerido, String archivo, boolean activo, String finalidadConsulta, String finalidadServicio, boolean permitirEjecutar) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateActividadPrograma,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE actividades_programa_pyp set 
			 * actividad=?,
			 * embarazo=?,
			 * semanas_gestacion=?,
			 * requerido=?,
			 * archivo=?,
			 * activo=?,
			 * finalidad_consulta=?,
			 * finalidad_servicio=?,
			 * permitir_ejecutar=? 
			 * where codigo=?
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(actividad));
			ps.setBoolean(2,embarazo);
			if(semanasGestacion.trim().equals(""))
			{
	        	ps.setNull(3,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(3,Utilidades.convertirADouble(semanasGestacion));
			}
			ps.setBoolean(4,requerido);
			ps.setString(5,archivo);
			ps.setBoolean(6,activo);
			if(finalidadConsulta.trim().equals("")||finalidadConsulta.trim().equals("null"))
			{
	        	ps.setNull(7,Types.VARCHAR);
			}
			else
			{
				ps.setString(7,finalidadConsulta);
			}
			if(finalidadServicio.trim().equals("")||finalidadServicio.trim().equals("null"))
			{
	        	ps.setNull(8,Types.INTEGER);
			}
			else
			{
				ps.setInt(8,Utilidades.convertirAEntero(finalidadServicio));
			}			
			ps.setBoolean(9, permitirEjecutar);
			ps.setDouble(10,Utilidades.convertirADouble(codigo));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[insertarActividadPrograma] -->");
			e.printStackTrace();
		}
		return false;

	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarActividad(Connection con, String codigo) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarActiProgPYPGrupEtareoGeneral,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarActiProgPYPMetasCumpGeneral,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarActiProgPYPViasIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps =  new PreparedStatementDecorator(con.prepareStatement("DELETE FROM diag_act_prog_pyp where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarActividadesProgramaPYP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			transaccion=true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[eliminarActividad] -->");
			e.printStackTrace();
			transaccion=false;
			
		}
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		return transaccion;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarViasIngresoActividadPrograma(Connection con, String codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena="select via_ingreso as viaingreso,ocupacion as ocupacion,solicitar as solicitar,programar as programar,ejecutar as ejecutar,'BD' as tiporegistro from acti_prog_pyp_vias_ing where codigo_act_prog_pyp=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			logger.info("CONSULTA----------------------->"+cadena+" ->"+codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[cargarViasIngresoActividadPrograma] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}


	

	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @return
	 */
	public static boolean eliminarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="DELETE FROM acti_prog_pyp_vias_ing where codigo_act_prog_pyp =? and via_ingreso=? and ocupacion=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigoAP));
			ps.setInt(2,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(3,Utilidades.convertirAEntero(ocupacion));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[eliminarRegistroViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public static boolean existeModificacionViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="SELECT codigo_act_prog_pyp FROM acti_prog_pyp_vias_ing where codigo_act_prog_pyp =? and via_ingreso=? and ocupacion=? and solicitar=? and programar=? and ejecutar=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1,Utilidades.convertirALong(codigoAP));
			ps.setInt(2,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(3,Utilidades.convertirAEntero(ocupacion));
			ps.setBoolean(4,solicitar);
			ps.setBoolean(5,programar);
			ps.setBoolean(6,ejecutar);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			return !rs.next();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[existeModificacionViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public static boolean modificarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion, boolean solicitar,boolean programar,boolean ejecutar) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="UPDATE acti_prog_pyp_vias_ing SET solicitar=?,programar=?,ejecutar=? where codigo_act_prog_pyp =? and via_ingreso=? and ocupacion=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBoolean(1,solicitar);
			ps.setBoolean(2,programar);
			ps.setBoolean(3,ejecutar);
			ps.setDouble(4,Utilidades.convertirADouble(codigoAP));
			ps.setInt(5,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(6,Utilidades.convertirAEntero(ocupacion));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[modificarRegistroViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigoAP
	 * @param viaIngreso
	 * @param ocupacion
	 * @param accion
	 * @return
	 */
	public static boolean insertarRegistroViaIngreso(Connection con, String codigoAP, String viaIngreso, String ocupacion,boolean solicitar,boolean programar,boolean ejecutar) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="INSERT INTO acti_prog_pyp_vias_ing(codigo_act_prog_pyp,via_ingreso,ocupacion,solicitar,programar,ejecutar) values(?,?,?,?,?,?)";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigoAP));
			ps.setInt(2,Utilidades.convertirAEntero(viaIngreso));
			ps.setInt(3,Utilidades.convertirAEntero(ocupacion));
			ps.setBoolean(4,solicitar);
			ps.setBoolean(5,programar);
			ps.setBoolean(6,ejecutar);
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[insertarRegistroViaIngreso] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen 
	 * @return
	 */
	public static HashMap cargarGruposEtareos(Connection con, String codigo, String regimen) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena="SELECT grupo_etareo as grupoetareo,frecuencia as frecuencia,tipo_frecuencia as tipofrecuencia,'BD' as tiporegistro from acti_prog_pup_grup_eta where codigo_act_prog_pyp="+codigo+" and regimen='"+regimen+"'";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[cargarGruposEtareos] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @return
	 */
	public static boolean eliminarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="DELETE FROM acti_prog_pup_grup_eta where codigo_act_prog_pyp =? and regimen=? and grupo_etareo=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,regimen);
			ps.setDouble(3,Utilidades.convertirADouble(grupoEtareo));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[eliminarRegistroGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public static boolean existeModificacionGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="SELECT codigo_act_prog_pyp FROM acti_prog_pup_grup_eta where codigo_act_prog_pyp =? and regimen=? and grupo_etareo=? ";
			if(frecuencia.trim().equals(""))
			{
				cadena=cadena+" and frecuencia is null";
			}
			else
			{
				cadena=cadena+" and frecuencia = "+frecuencia;
			}
			if(tipoFrecuencia.trim().equals(""))
			{
				cadena=cadena+" and tipo_frecuencia is null";
			}
			else
			{
				cadena=cadena+" and tipo_frecuencia = "+tipoFrecuencia;
			}
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,regimen);
			ps.setDouble(3,Utilidades.convertirADouble(grupoEtareo));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			return !rs.next();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[existeModificacionGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public static boolean modificarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="UPDATE acti_prog_pup_grup_eta SET frecuencia=?, tipo_frecuencia=? where codigo_act_prog_pyp =? and regimen=? and grupo_etareo=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(frecuencia.trim().equals(""))
			{
				ps.setNull(1,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(1,Utilidades.convertirADouble(frecuencia));
			}
			if(tipoFrecuencia.trim().equals(""))
			{
				ps.setNull(2,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(2,Utilidades.convertirADouble(tipoFrecuencia));
			}
			ps.setDouble(3,Utilidades.convertirADouble(codigo));
			ps.setString(4,regimen);
			ps.setDouble(5,Utilidades.convertirADouble(grupoEtareo));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[modificarRegistroGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;	
	}



	/**
	 * 
	 * @param con 
	 * @param codigo
	 * @param regimen
	 * @param grupoEtareo
	 * @param frecuencia
	 * @param tipoFrecuencia 
	 * @return
	 */
	public static boolean insertarRegistroGrupoEtareo(Connection con, String codigo, String regimen, String grupoEtareo, String frecuencia, String tipoFrecuencia) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="INSERT INTO acti_prog_pup_grup_eta(codigo_act_prog_pyp,regimen,grupo_etareo,frecuencia,tipo_frecuencia) values(?,?,?,?,?)";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,regimen);
			ps.setDouble(3,Utilidades.convertirADouble(grupoEtareo));
			if(frecuencia.trim().equals(""))
			{
				ps.setNull(4,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(4,Utilidades.convertirADouble(frecuencia));
			}
			if(tipoFrecuencia.trim().equals(""))
			{
				ps.setNull(5,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(5,Utilidades.convertirADouble(tipoFrecuencia));
			}
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[insertarRegistroGrupoEtareo] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarMetas(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena=" SELECT regimen as regimen,meta_cumplimiento as meta,'BD' as tiporegistro  from act_prog_pyp_met_cum where codigo_act_prog_pyp =?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[cargarMetas] -->");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @return
	 */
	public static boolean eliminarRegistroMeta(Connection con, String codigo, String regimen) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="DELETE FROM act_prog_pyp_met_cum where codigo_act_prog_pyp =? and regimen=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,regimen);
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[eliminarRegistroMeta] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con 
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public static boolean existeModificacionMetas(Connection con, String codigo, String regimen, String meta) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="SELECT codigo_act_prog_pyp FROM act_prog_pyp_met_cum where codigo_act_prog_pyp =? and regimen=? ";
			if(meta.trim().equals(""))
			{
				cadena=cadena+"  and meta_cumplimiento is null ";
			}
			else
			{
				cadena=cadena+"  and meta_cumplimiento = "+meta;
			}
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,regimen);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			return !rs.next();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[existeModificacionMetas] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public static boolean modificarRegistroMetas(Connection con, String codigo, String regimen, String meta)
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="UPDATE act_prog_pyp_met_cum SET meta_cumplimiento=? where codigo_act_prog_pyp =? and regimen=?";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(meta.trim().equals(""))
			{
				ps.setNull(1,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(1,Utilidades.convertirADouble(meta));	
			}
			ps.setDouble(2,Utilidades.convertirADouble(codigo));
			ps.setString(3,regimen);
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[modificarRegistroMetas] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param regimen
	 * @param meta
	 * @return
	 */
	public static boolean insertarRegistroMeta(Connection con, String codigo, String regimen, String meta) 
	{
		PreparedStatementDecorator ps;
		try 
		{
			String cadena="INSERT INTO act_prog_pyp_met_cum(codigo_act_prog_pyp,regimen,meta_cumplimiento) values(?,?,?)";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,regimen);
			if(meta.trim().equals(""))
			{
				ps.setNull(3,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(3,Utilidades.convertirADouble(meta));	
			}
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[insertarRegistroMeta] -->");
			e.printStackTrace();
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public static HashMap consultarDiagnosticosActProPYP(Connection con, String codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultarDiagnosticos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[consultarDiagnosticosActProPYP] -->");
			e.printStackTrace();
		}
		return mapa;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public static boolean guardarDiagnostico(Connection con, String codigo, String acronimo, String cie) {
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertaDiagnosticos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,acronimo);
			ps.setInt(3,Utilidades.convertirAEntero(cie));
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[guardarDiagnostico] -->");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public static boolean eliminarDiagnostico(Connection con, String codigo, String acronimo, String cie) {
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarDiagnosticos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,Utilidades.convertirADouble(codigo));
			ps.setString(2,acronimo);
			ps.setInt(3,Utilidades.convertirAEntero(cie));
			return ps.executeUpdate()>0;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN SqlBaseActividadesProgramasPYPDao[eliminarDiagnostico] -->");
			e.printStackTrace();
		}
		return false;
	}

}
