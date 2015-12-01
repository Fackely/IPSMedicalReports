package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturacion.ParamArchivoPlanoIndCalidad;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseParamArchivoPlanoIndCalidadDao
{
	private static Logger logger = Logger.getLogger(SqlBaseParamArchivoPlanoColsanitasDao.class);
	
	private static String [] indicesESMap={"codigo_","codigoe_","codigoi_","acronimo_","descripcion_","nombre_","puedoEliminar_"};
	
	private static String [] indicesCCMap={"codigo_","codigoc_","codigoi_","acronimo_","descripcion_","nombre_","puedoEliminar_"};
	
	private static String [] indicesDXMap={"codigo_","codigod_","codigoi_","acronimo_","descripcion_","nombre_","cie_","puedoEliminar_"};
	
	private static String [] indicesGMap={"codigo_","codigosv_","tad_","tas_","mtad_","mtas_","cero_","nomtad","nomtas"};
	
	private static String [] indicesCODMap={"codigo_","descripcion_","acronimo_"};
	
	private static String [] indicesEspeMap={"codigo_","nombre_","selec_"};
	
	private static String [] indicesSigMap={"acronimo_","cie","nombre_"};

	private static String consultaES="SELECT ice.codigo, icc.codigo AS codigoi, icc.acronimo, icc.descripcion, e.nombre, e.codigo AS codigoe " +
										"FROM ind_calidad_especialidad ice " +
												"INNER JOIN ind_calidad_codigos icc ON (ice.ind_calidad_codigo=icc.codigo) " +
												"INNER JOIN especialidades e ON (ice.especialidad=e.codigo) ";
	
	private static String consultaCodigos="SELECT codigo, descripcion, acronimo FROM ind_calidad_codigos WHERE tipo=? ";
	
	private static String consutlaEspecialidades="SELECT codigo, nombre, '"+ConstantesBD.acronimoNo+"' AS selec FROM especialidades WHERE codigo<>-1 AND codigo<>0 ORDER BY nombre ";
	
	private static String consutlaCentros="SELECT codigo, nombre, '"+ConstantesBD.acronimoNo+"' AS selec FROM centros_costo WHERE centro_atencion=? AND codigo<>-1 AND codigo<>0 ORDER BY nombre ";
	
	private static String consutlaSignos="SELECT codigo, nombre FROM signos_vitales ";
	
	private static String eliminarES="DELETE FROM ind_calidad_especialidad WHERE codigo=?";
	
	private static String eliminarCC="DELETE FROM ind_calidad_cc WHERE codigo=?";
	
	private static String eliminarDX="DELETE FROM ind_calidad_dx WHERE codigo=?";
	
	private static String modificarES="UPDATE ind_calidad_especialidad " +
											"SET ind_calidad_codigo=?, " +
												"especialidad=?, " +
												"institucion=?, " +
												"usuario_modifica=?, " +
												"fecha_modifica=CURRENT_DATE, " +
												"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
											"WHERE " +
												"codigo=? ";
	
	private static String modificarCC="UPDATE ind_calidad_cc " +
											"SET ind_calidad_codigo=?, " +
												"centro_costo=?, " +
												"institucion=?, " +
												"usuario_modifica=?, " +
												"fecha_modifica=CURRENT_DATE, " +
												"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
											"WHERE " +
												"codigo=? ";
	
	private static String modificarDX="UPDATE ind_calidad_dx " +
											"SET ind_calidad_codigo=?, " +
												"acronimo_dx=?, " +
												"tipo_cie_dx=?, " +
												"institucion=?, " +
												"usuario_modifica=?, " +
												"fecha_modifica=CURRENT_DATE, " +
												"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
											"WHERE " +
												"codigo=? ";
	
	private static String consultaCC="SELECT ic.codigo, icc.codigo AS codigoi, icc.acronimo, icc.descripcion, cc.nombre, cc.codigo AS codigoc " +
										"FROM ind_calidad_cc ic " +
												"INNER JOIN ind_calidad_codigos icc ON (ic.ind_calidad_codigo=icc.codigo) " +
												"INNER JOIN centros_costo cc ON (ic.centro_costo=cc.codigo) ";
	
	private static String consultaDX="SELECT id.codigo, icc.codigo AS codigoi, icc.acronimo, icc.descripcion, d.nombre, d.tipo_cie AS cie, d.acronimo AS codigod " +
										"FROM ind_calidad_dx id " +
												"INNER JOIN ind_calidad_codigos icc ON (id.ind_calidad_codigo=icc.codigo) " +
												"INNER JOIN diagnosticos d ON (id.acronimo_dx=d.acronimo AND id.tipo_cie_dx=d.tipo_cie) ";
	
	private static String consultaG="SELECT g.codigo, " +
											"g.tension_arterial_diastolica AS tad, " +
											"g.tension_arterial_sistolica AS tas, " +
											"g.maximo_normal_tad AS mtad, " +
											"g.maximo_normal_tas AS mtas, " +
											"g.reportar_ind_cero AS cero, " +
											"sv.codigo AS codigosv, " +
											"sv.nombre AS nomtad, " +
											"(SELECT nombre FROM signos_vitales WHERE codigo=g.tension_arterial_sistolica) AS nomtas " +
										"FROM ind_generales g " +
												"INNER JOIN signos_vitales sv ON (g.tension_arterial_diastolica=sv.codigo) ";
	
	private static String modificarS="UPDATE ind_generales " +
											"SET reportar_ind_cero=?, " +
												"tension_arterial_diastolica=?, " +
												"maximo_normal_tad=?, " +
												"tension_arterial_sistolica=?, " +
												"maximo_normal_tas=?, " +
												"institucion=?, " +
												"usuario_modifica=?, " +
												"fecha_modifica=CURRENT_DATE, " +
												"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
											"WHERE " +
												"codigo=? ";
	
	private static String insertarS="INSERT INTO ind_generales " +
											"(codigo, " +
												"reportar_ind_cero, " +
												"tension_arterial_diastolica, " +
												"maximo_normal_tad, " +
												"tension_arterial_sistolica, " +
												"maximo_normal_tas, " +
												"institucion, " +
												"usuario_modifica, " +
												"fecha_modifica, " +
												"hora_modifica) " +
											"VALUES (1,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	
	/**
	 * Metodo que consulta los registro existentes de la Seccion Especialdiad
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaES (Connection con)
	{
		HashMap<String, Object> resultadosES = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaES, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosES = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosES.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(resultadosES.get("codigo_"+w)+"",1))
				{
					resultadosES.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultadosES.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
			
			resultadosES.put("INDICES",indicesESMap);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Indicadores por Especialidad.>>>>>"+e);
		}		
		return resultadosES;
	}
	
	/**
	 * Metodo que consulta los registro existentes de la Seccion Centro de Costo
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaCC (Connection con)
	{
		HashMap<String, Object> resultadosCC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaCC, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosCC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosCC.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(resultadosCC.get("codigo_"+w)+"", 2))
				{
					resultadosCC.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultadosCC.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
			
			resultadosCC.put("INDICES",indicesCCMap);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Indicadores por Centro de Costo.>>>>>"+e);
		}		
		return resultadosCC;
	}
	
	/**
	 * Metodo que consulta los registro existentes de la Seccion Generales
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaG (Connection con)
	{
		HashMap<String, Object> resultadosCC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaG, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosCC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);

			resultadosCC.put("INDICES",indicesGMap);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Signos Vitales.>>>>>"+e);
		}		
		return resultadosCC;
	}
	
	/**
	 * Metodo que consulta los registro existentes de la Seccion Diagnostico
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaDX (Connection con)
	{
		HashMap<String, Object> resultadosDX = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaDX, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosDX = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosDX.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(resultadosDX.get("codigo_"+w)+"", 3))
				{
					resultadosDX.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultadosDX.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
			
			resultadosDX.put("INDICES",indicesDXMap);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Indicadores por Diagnostico.>>>>>"+e);
		}		
		return resultadosDX;
	}
	
	/**
	 * Metodo que trae los Codigos de los Indicadores de Calidad segun Tipo de Seccion
	 * @param con
	 * @param tipo
	 * @return
	 */
	public static HashMap<String, Object> consultaCOD (Connection con, String tipo)
	{
		HashMap<String, Object> resultadosCOD = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaCodigos, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst2.setString(1, tipo);
			resultadosCOD = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			resultadosCOD.put("INDICES",indicesCODMap);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los codigos de Indicadores por Especialidad.>>>>>"+e);
		}		
		return resultadosCOD;
	}
	
	/**
	 * Metodo que trae las Especialidades parametrizadas en Axioma
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaEspe (Connection con)
	{
		HashMap<String, Object> resultadosEspe = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consutlaEspecialidades, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			resultadosEspe = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			resultadosEspe.put("INDICES",indicesEspeMap);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de las Especialidades.>>>>>"+e);
		}		
		return resultadosEspe;
	}
	
	/**
	 * Metodo que trae los Centros de Costo parametrizados en Axioma
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultaCentros (Connection con, int centroAtencion)
	{
		HashMap<String, Object> resultadosCent = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consutlaCentros, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst2.setInt(1, centroAtencion);
			resultadosCent = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			resultadosCent.put("INDICES",indicesEspeMap);			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los Centros de Costo.>>>>>"+e);
		}		
		return resultadosCent;
	}
	
	/**
	 * Metodo que trae los Signos Vitales parametrizados en Axioma
	 * @param con
	 * @return
	 */	public static HashMap<String, Object> consultaSignos (Connection con)
	{
		HashMap<String, Object> resultadosDX = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consutlaSignos, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			resultadosDX = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			resultadosDX.put("INDICES",indicesSigMap);			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los Diagnosticos.>>>>>"+e);
		}		
		return resultadosDX;
	}
	
	 /**
	  * Metodo para Insertar un nuevo Registro de la Seccio Especialidad
	  * @param con
	  * @param mundo
	  * @return
	  */
	public static boolean insertarEspecialidad(Connection con, ParamArchivoPlanoIndCalidad mundo, String insertarES)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarES,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			/**
			 * INSERT INTO ind_calidad_especialidad VALUES " +
									"('facturacion.seq_ind_calidad_especialidad')," +
									"?," +
									"?," +
									"?," +
									"?," +
									"CURRENT_DATE," +
									""+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCodigo()));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getEspecialidad()));
			ps.setInt(3, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(4, mundo.getUsuario());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Insertando un nuevo registro de Especialidades.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * Metodo que inserta un nuevo registro de la Seccio Centro de Costo
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean insertarCentro(Connection con, ParamArchivoPlanoIndCalidad mundo, String insertarCC)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarCC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			/**
			 * INSERT INTO ind_calidad_cc VALUES " +
								"('facturacion.seq_ind_calidad_cc')," +
								"?," +
								"?," +
								"?," +
								"?," +
								"CURRENT_DATE," +
								""+ValoresPorDefecto.getSentenciaHoraActualBD()+")
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCodigo()));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getCentroCosto()));
			ps.setInt(3, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(4, mundo.getUsuario());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Insertando un nuevo registro de Centros de Costo.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * Metodo que inserta un nuevo registro de la seccion Diagnosticos
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static boolean insertarDiagnostico(Connection con, ParamArchivoPlanoIndCalidad mundo, String insertarDX)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarDX,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCodigo()));
			ps.setString(2, mundo.getDiagnostico());
			ps.setString(3, mundo.getCie());
			ps.setInt(4, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(5, mundo.getUsuario());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Insertando un nuevo registro de Diagnostico.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * Metodo para Eliminar un registro seleccionado de la Seccion Especialidad
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminar(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarES,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;			
	}
	
	/**
	 * Metodo para eliminar un registro seleccionado de la Seccion CEntro de Costo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarCC(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarCC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;			
	}
	
	/**
	 * Metodo para eliminar un registro seleccionado de la Seccion Diagnostico
	 * @param con
	 * @param codigo
	 * @return
	 */	public static boolean eliminarDX(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDX,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;			
	}
	
	/**
	 * Metodo utilizado para saber si un registro se puede eliminar o no
	 * @param codigo
	 * @param seccion
	 * @return
	 */
	private static boolean puedoEliminar(String codigo, int seccion)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminar=false;
		if(seccion == 1)
		{
			puedoEliminar=eliminar(con, codigo);
		}
		if(seccion == 2)
		{
			puedoEliminar=eliminarCC(con, codigo);
		}
		if(seccion == 3)
		{
			puedoEliminar=eliminarDX(con, codigo);
		}
		if(puedoEliminar)
			retorna= true;
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	/**
	 * Metodo para modificar un registro de la Seccion Especialdiad
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarES(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarES,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE ind_calidad_especialidad " +
								"SET ind_calidad_codigo=?, " +
									"especialidad=?, " +
									"institucion=?, " +
									"usuario_modifica=?, " +
									"fecha_modifica=CURRENT_DATE, " +
									"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
								"WHERE " +
									"codigo=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCodigo()));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getEspecialidad()));
			ps.setInt(3, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(4, mundo.getUsuario());
			ps.setDouble(5, Utilidades.convertirADouble(codigoM));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Modificando una Especialidad>>>>>>>>>"+e);
		}
		return false;
	}
	
	/**
	 * Metodo para Modificar un registro de la Seccion CEntro de Costo
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarCC(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE ind_calidad_cc " +
									"SET ind_calidad_codigo=?, " +
										"centro_costo=?, " +
										"institucion=?, " +
										"usuario_modifica=?, " +
										"fecha_modifica=CURRENT_DATE, " +
										"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
									"WHERE " +
										"codigo=?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCodigo()));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getCentroCosto()));
			ps.setInt(3, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(4, mundo.getUsuario());
			ps.setDouble(5, Utilidades.convertirADouble(codigoM));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Modificando un Centro de Costo>>>>>>>>>"+e);
		}
		return false;
	}
	
	/**
	 * Metodo para modificar un registro de la Seccion Diagnostico
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarDX(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarDX,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE ind_calidad_dx " +
								"SET ind_calidad_codigo=?, " +
									"acronimo_dx=?, " +
									"tipo_cie_dx=?, " +
									"institucion=?, " +
									"usuario_modifica=?, " +
									"fecha_modifica=CURRENT_DATE, " +
									"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
								"WHERE " +
									"codigo=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCodigo()));
			ps.setString(2, mundo.getDiagnostico());
			ps.setInt(3, Utilidades.convertirAEntero(mundo.getCie()));
			ps.setInt(4, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(5, mundo.getUsuario());
			ps.setDouble(6, Utilidades.convertirADouble(codigoM));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Modificando un Diagnostico>>>>>>>>>"+e);
		}
		return false;
	}
	
	/**
	 * Metodo para modificar un registro de la Seccion Generales
	 * @param con
	 * @param mundo
	 * @param codigoM
	 * @return
	 */
	public static boolean modificarS(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM, String esNuevo)
	{
		if(esNuevo.equals("s"))
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * UPDATE ind_generales " +
							"SET reportar_ind_cero=?, " +
								"tension_arterial_diastolica=?, " +
								"maximo_normal_tad=?, " +
								"tension_arterial_sistolica=?, " +
								"maximo_normal_tas=?, " +
								"institucion=?, " +
								"usuario_modifica=?, " +
								"fecha_modifica=CURRENT_DATE, " +
								"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
							"WHERE " +
								"codigo=? 
				 */
				
				if(mundo.getCero().equals("1"))
				{
					ps.setString(1, "S");
				}
				else
				{
					ps.setString(1, "N");
				}
				ps.setInt(2, Utilidades.convertirAEntero(mundo.getTad()));
				ps.setInt(3, Utilidades.convertirAEntero(mundo.getMtad()));
				ps.setInt(4, Utilidades.convertirAEntero(mundo.getTas()));
				ps.setInt(5, Utilidades.convertirAEntero(mundo.getMtas()));
				ps.setInt(6, Utilidades.convertirAEntero(mundo.getInstitucion()));
				ps.setString(7, mundo.getUsuario());
				
				if(ps.executeUpdate()>0)
					return true;
				
			}
			catch (SQLException e)
			{
				logger.info("Error. Creando el nuevo registro de un Signo>>>>>>>>>"+e);
			}
		}
		else
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(mundo.getCero().equals("1"))
				{
					ps.setString(1, "S");
				}
				else
				{
					ps.setString(1, "N");
				}
				ps.setInt(2, Utilidades.convertirAEntero(mundo.getTad()));
				ps.setInt(3, Utilidades.convertirAEntero(mundo.getMtad()));
				ps.setInt(4, Utilidades.convertirAEntero(mundo.getTas()));
				ps.setInt(5, Utilidades.convertirAEntero(mundo.getMtas()));
				ps.setInt(6, Utilidades.convertirAEntero(mundo.getInstitucion()));
				ps.setString(7, mundo.getUsuario());
				ps.setDouble(8, Utilidades.convertirADouble(codigoM));
				
				if(ps.executeUpdate()>0)
					return true;
				
			}
			catch (SQLException e)
			{
				logger.info("Error. Modificando un Signo>>>>>>>>>"+e);
			}
		}
		return false;
	}
}