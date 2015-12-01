package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import javax.mail.Session;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseParamArchivoPlanoColsanitasDao;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.UnidadAgendaUsuarioCentro;

public class SqlBaseUnidadAgendaUsuarioCentroDao
{
	private static Logger logger = Logger.getLogger(SqlBaseUnidadAgendaUsuarioCentroDao.class);
	
	private static String [] indicesUniP={"catencion_","codigou_","nombrea_","nombreb_","apea_","apeb_","ccatencion_","uagenda_","cuagenda_","uautorizado_","actividad_","cactividad_","puedoEliminar_"};
	
	private static String [] indicesU={"codigo_","descripcion_","centro_atencion"};
	
	private static String [] indicesUsu={"login_"};
	
	private static String [] indicesAct={"codigo_","nombre_","selec_"};
	
	private static String consultaPrincipal1="SELECT ca.descripcion AS catencion, " +
													"uauc.codigo AS codigou, " +
													"uauc.centro_atencion AS ccatencion, " +
													"uc.descripcion AS uagenda, " +
													"uauc.unidad_agenda AS cuagenda, " +
													"uauc.usuario_autorizado AS uautorizado, " +
													"p.primer_nombre AS nombrea, " +
													"p.segundo_nombre AS nombreb, " +
													"p.primer_apellido AS apea, " +
													"p.segundo_apellido AS apeb " +
												"FROM unid_agenda_usu_caten uauc " +
													"INNER JOIN unidades_consulta uc ON (uauc.unidad_agenda=uc.codigo) " +
													"INNER JOIN centro_atencion ca ON (uauc.centro_atencion=ca.consecutivo) " +
													"INNER JOIN usuarios u ON (uauc.usuario_autorizado=u.login) " +
													"INNER JOIN personas p ON (u.codigo_persona=p.codigo) " +
												"WHERE " +
												"uauc.centro_atencion=? " +
												"AND 1=1 "+
												"ORDER BY uc.descripcion, p.primer_apellido ";
	
	private static String consultaPrincipal="SELECT ca.descripcion AS catencion, " +
													"uauc.codigo AS codigou, " +
													"uauc.centro_atencion AS ccatencion, " +
													"uc.descripcion AS uagenda, " +
													"uauc.unidad_agenda AS cuagenda, " +
													"uauc.usuario_autorizado AS uautorizado, " +
													"aa.nombre AS actividad, " +
													"aa.codigo AS cactividad, " +
													"p.primer_nombre AS nombrea, " +
													"p.segundo_nombre AS nombreb, " +
													"p.primer_apellido AS apea, " +
													"p.segundo_apellido AS apeb " +
											"FROM unid_agenda_usu_caten uauc " +
													"INNER JOIN unid_agenda_act_auto uaaa ON (uauc.codigo=uaaa.unid_agenda_usu_caten) " +
													"INNER JOIN activi_autorizadas aa ON (uaaa.actividad_autorizada=aa.codigo) " +
													"INNER JOIN unidades_consulta uc ON (uauc.unidad_agenda=uc.codigo) " +
													"INNER JOIN centro_atencion ca ON (uauc.centro_atencion=ca.consecutivo) " +
													"INNER JOIN usuarios u ON (uauc.usuario_autorizado=u.login) " +
													"INNER JOIN personas p ON (u.codigo_persona=p.codigo) " +
											"WHERE " +
											"uauc.centro_atencion=? " +
											"AND 1=1 "+
											"ORDER BY uc.descripcion, p.primer_apellido ";
	
	public static String consultaUnidadPrincipal="";
	
	private static String eliminarUniP="DELETE FROM unid_agenda_usu_caten WHERE codigo=?";
	
	private static String eliminarActxUnid="DELETE FROM unid_agenda_act_auto WHERE unid_agenda_usu_caten=?";
	
	private static String eliminarActxUnid2="DELETE FROM unid_agenda_act_auto WHERE unid_agenda_usu_caten=? AND actividad_autorizada=?";
	
	private static String consultaUnidades="SELECT DISTINCT uc.codigo, uc.descripcion AS descripcion, cc.centro_atencion, uc.tipo_atencion AS tipoatencion " +
											"FROM unidades_consulta uc " +
													"INNER JOIN cen_costo_x_un_consulta ccx ON (uc.codigo=ccx.unidad_consulta) " +
													"INNER JOIN centros_costo cc ON (ccx.centro_costo=cc.codigo) " +
											"WHERE cc.centro_atencion=? ORDER BY uc.descripcion ";
	
	private static String consultaUsuarios="SELECT u.login, p.primer_nombre, p.segundo_nombre, p.primer_apellido, p.segundo_apellido " +
											"FROM usuarios u " +
													"INNER JOIN personas p ON (u.codigo_persona=p.codigo) ORDER BY p.primer_apellido ";
	
	private static String consultaActividadesNO="SELECT codigo, nombre, '"+ConstantesBD.acronimoNo+"' AS selec, aplica_odontologia AS aplicaodon FROM activi_autorizadas where es_multa='N'";
	private static String consultaActividadesSI="SELECT codigo, nombre, '"+ConstantesBD.acronimoNo+"' AS selec, aplica_odontologia AS aplicaodon FROM activi_autorizadas ";
	

	
	private static String insertarUnidadPos="INSERT INTO consultaexterna.unid_agenda_usu_caten VALUES " +
											"(nextval('consultaexterna.seq_unid_agenda_usu_caten'), " +
											"?, " +
											"?, " +
											"?, " +
											"?, " +
											"?, " +"CURRENT_DATE, " +
											""+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
	
	private static String insertarUnidadOra="INSERT INTO consultaexterna.unid_agenda_usu_caten VALUES (" +
											"consultaexterna.seq_unid_agenda_usu_caten.nextval, " +
											"?, " +
											"?, " +
											"?, " +
											"?, " +
											"?, " +
											"(select to_date(to_char(sysdate, 'YYYYMMDD'),'YYYYMMDD') from dual),"+
	                                        "substr((select to_char(sysdate, 'hh24:mi:ss') from dual),1,5))";
	
	//private static final String cadenaInsertarOra="VALUES (?, ?, ?, ?, ?, ?, (select to_date(to_char(sysdate, 'YYYYMMDD'),'YYYYMMDD') from dual), substr((select to_char(sysdate, 'hh24:mi:ss') from dual), 1,5), ?)";
	private static String consultarMaxU="SELECT max(codigo) AS codigoU FROM consultaexterna.unid_agenda_usu_caten ";
	
	private static String insertarActivi="INSERT INTO consultaexterna.unid_agenda_act_auto VALUES " +
																			"(?, ?) ";
	
	
																	
	private static String modificarUnidPos="UPDATE consultaexterna.unid_agenda_usu_caten SET " +
										   "centro_atencion=?, " +
										   "unidad_agenda=?, " +
										   "usuario_autorizado=?, " +
										   "institucion=?, " +
										   "usuario_modifica=?, "+
										   "fecha_modifica=CURRENT_DATE, " +
											"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
											"WHERE codigo=? ";
	
	private static String modificarUnidOra="UPDATE consultaexterna.unid_agenda_usu_caten SET " +
										   "centro_atencion=?, " +
										   "unidad_agenda=?, " +
										   "usuario_autorizado=?, " +
										   "institucion=?, " +
										   "usuario_modifica=?, "+
										   "fecha_modifica=(select to_date(to_char(sysdate, 'YYYYMMDD'),'YYYYMMDD') from dual), " +
											"hora_modifica=SUBSTR((SELECT TO_CHAR(sysdate, 'hh24:mi') FROM dual), 1,5) " +
											"WHERE codigo=? ";
	
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultaUA1 (Connection con, int centroAtencion, String usuarioLogin)
	{
		HashMap<String, Object> resultadosUA = new HashMap<String, Object>();		
		PreparedStatementDecorator pst2;		
		String cadena = consultaPrincipal1;
		
		//Se filtra por el usuario
		if(!usuarioLogin.equals(""))
			cadena = cadena.replace("1=1","  u.login =  '"+usuarioLogin+"' ");			
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst2.setInt(1, centroAtencion);
			
			resultadosUA = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			resultadosUA.put("INDICES",indicesUniP);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosUA.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(Utilidades.convertirAEntero(resultadosUA.get("codigou_"+w)+"")))
				{
					resultadosUA.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultadosUA.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Unidad de Agenda por Consulta por CA1.>>>>>"+e);
		}
		return resultadosUA;
	}
	
	/**
	 * Metodo de consulta de los registro de unidad de agenda
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaUA (Connection con, int centroAtencion, String usuarioLogin)
	{
		HashMap<String, Object> resultadosUA = new HashMap<String, Object>();		
		PreparedStatementDecorator pst2;
		String cadena = consultaPrincipal;
		
		//Se filtra por el usuario
		if(!usuarioLogin.equals(""))
			cadena = cadena.replace("1=1","  u.login =  '"+usuarioLogin+"' ");		
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst2.setInt(1, centroAtencion);
			
			resultadosUA = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			resultadosUA.put("INDICES",indicesUniP);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosUA.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(Utilidades.convertirAEntero(resultadosUA.get("codigou_"+w)+"")))
				{
					resultadosUA.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultadosUA.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Unidad de Agenda por Consulta por CA.>>>>>"+e);
		}
		return resultadosUA;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaUnidadPrincipal (Connection con, int centroAtencion, int unidadAgenda, String usuarioAutorizado)
	{
		consultaUnidadPrincipal="SELECT ca.descripcion AS catencion, " +
								"uauc.centro_atencion AS ccatencion, " +
								"uc.descripcion AS uagenda, " +
								"uauc.unidad_agenda AS cuagenda, " +
								"uauc.usuario_autorizado AS uautorizado, " +
								"aa.nombre AS actividad, " +
								"aa.codigo AS cactividad " +
							"FROM unid_agenda_usu_caten uauc " +
								"INNER JOIN unidades_consulta uc ON (uauc.unidad_agenda=uc.codigo) " +
								"INNER JOIN unid_agenda_act_auto uaaa ON (uauc.codigo=uaaa.unid_agenda_usu_caten) " +
								"INNER JOIN centro_atencion ca ON (uauc.centro_atencion=ca.consecutivo) " +
								"INNER JOIN activi_autorizadas aa ON (uaaa.actividad_autorizada=aa.codigo) ";
		
		
		HashMap<String, Object> resultadosUA = new HashMap<String, Object>();
		int aux=0;
		
		PreparedStatementDecorator pst2;
		
		if(centroAtencion != -1 || unidadAgenda != -1 || !usuarioAutorizado.equals(""))
		{
			consultaUnidadPrincipal+="WHERE ";
			if(centroAtencion != -1)
			{
				consultaUnidadPrincipal+="uauc.centro_atencion="+centroAtencion+" ";
				aux=1;
			}
			if(unidadAgenda != -1)
			{
				if(aux==1)
				{
					consultaUnidadPrincipal+="AND uauc.unidad_agenda="+unidadAgenda+" ";
					aux=2;
				}
				else
					consultaUnidadPrincipal+="uauc.unidad_agenda="+unidadAgenda+" ";
			}
			if(!usuarioAutorizado.equals(""))
			{
				if(aux==1 || aux==2)
					consultaUnidadPrincipal+="AND uauc.usuario_autorizado='"+usuarioAutorizado+"' ";
				else
					consultaUnidadPrincipal+="uauc.usuario_autorizado='"+usuarioAutorizado+"' ";
			}
		}
		try{
			
			logger.info("consultaUnidadPrincipal--->"+consultaUnidadPrincipal);
			
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaUnidadPrincipal, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosUA = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			resultadosUA.put("INDICES",indicesUniP);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Unidad de Agenda por Consulta por CA Exterior.>>>>>"+e);
		}		
		return resultadosUA;
	}
	
	/**
	 * Metodo para saber si un Registro se puede Eliminar
	 * @param codigo
	 * @return
	 */
	private static boolean puedoEliminar(int codigo)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminar=eliminar(con, codigo);
		if(puedoEliminar)
			retorna= true;
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	/**
	 * Metodo para Eliminar un Registro
	 * @param con
	 * @param sustitutosNoPos
	 * @return
	 */
	public static boolean eliminar(Connection con, int codigo)
	{
		boolean elimino=false;
		try
		{
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(eliminarActxUnid,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps2.setDouble(1, Utilidades.convertirADouble(codigo+""));
		
			if(ps2.executeUpdate()>0)
				elimino=true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		if(elimino == true)
		{
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarUniP,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			
				if(ps.executeUpdate()>0)
					return true;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return false;			
	}
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultaUnidades (Connection con, int centroAtencion)
	{
		HashMap<String, Object> resultadosU = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaUnidades, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		   pst2.setInt(1, centroAtencion);
			
			resultadosU = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			resultadosU.put("INDICES",indicesU);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Unidades");
		}
		return resultadosU;
	}
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaUsuarios (Connection con)
	{
		HashMap<String, Object> resultadosU = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaUsuarios, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
			
			resultadosU = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			resultadosU.put("INDICES",indicesUsu);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Usuarios");
		}
		return resultadosU;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaActividades (Connection con,UsuarioBasico user)
	{
		HashMap<String, Object> resultadosU = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst2;
		
		try{
			
			 		 
			//UsuarioBasico user=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if (ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(user.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
			{
				pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaActividadesSI, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			
			}  else 
			{
				pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaActividadesNO, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			
			}
			
			resultadosU = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			
			resultadosU.put("INDICES",indicesAct);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Actividades");
		}
		return resultadosU;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param Tipo_DB 
	 * @return
	 */
	public static boolean insertarUnidad(Connection con, UnidadAgendaUsuarioCentro mundo, int Tipo_DB)
	{
		try
		{
			String insertarUnidad=" ";
			switch(Tipo_DB)
			{
			case DaoFactory.ORACLE:
				insertarUnidad=insertarUnidadOra;
				break;
			case DaoFactory.POSTGRESQL:
				insertarUnidad=insertarUnidadPos;
				break;
				default:
					break;
			
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarUnidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("insertarUnidad------------> "+insertarUnidad);
			logger.info("1------------> "+Utilidades.convertirAEntero(mundo.getCentroAtencion()));
			logger.info("2------------> "+Utilidades.convertirAEntero(mundo.getUnidad()));
			logger.info("3------------> "+mundo.getUsuario());
			logger.info("4------------> "+Utilidades.convertirAEntero(mundo.getInstitucion()));
			logger.info("5------------> "+ mundo.getUsuarioM());
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCentroAtencion()));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getUnidad()));
			ps.setString(3, mundo.getUsuario());
			ps.setInt(4, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(5, mundo.getUsuarioM());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Insertando un nuevo registro de Unidades de Agenda.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param unidad
	 * @param actividad
	 * @return
	 */
	public static boolean insertarActividadxUnidad(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		int codigoU=0;
		PreparedStatementDecorator ps2;
		ResultSetDecorator rs2;
		try
		{
			ps2= new PreparedStatementDecorator(con.prepareStatement(consultarMaxU,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			rs2=new ResultSetDecorator(ps2.executeQuery());
			if(rs2.next()){
				codigoU = rs2.getInt("codigoU");
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta del max Codigo");
			return false;
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarActivi,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setDouble(1, Utilidades.convertirADouble(codigoU+""));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getActividad()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Insertando un nuevo registro de Actividad por Unidad.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param Tipo_BD 
	 * @return
	 */
	public static boolean modificar(Connection con, UnidadAgendaUsuarioCentro mundo, int Tipo_BD)
	{
		try
		{
			String modificarUnid="";
			switch(Tipo_BD)
			{
			case DaoFactory.ORACLE:
				modificarUnid=modificarUnidOra;
				break;
			case DaoFactory.POSTGRESQL:
				modificarUnid=modificarUnidPos;
				break;
				
				default:
					break;
			
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarUnid,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			logger.info("modificarUnid------------->  "+modificarUnid+"*****");
			logger.info("1------------->  "+Utilidades.convertirAEntero(mundo.getCentroAtencion()));
			logger.info("2------------->  "+Utilidades.convertirAEntero(mundo.getUnidad()));
			logger.info("3------------->  "+mundo.getUsuario());
			logger.info("4------------->  "+Utilidades.convertirAEntero(mundo.getInstitucion()));
			logger.info("5------------->  "+mundo.getUsuarioM());
			logger.info("6------------->  "+Utilidades.convertirADouble(mundo.getCodigoM()));
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getCentroAtencion()));
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getUnidad()));
			ps.setString(3, mundo.getUsuario());
			ps.setInt(4, Utilidades.convertirAEntero(mundo.getInstitucion()));
			ps.setString(5, mundo.getUsuarioM());
			ps.setDouble(6, Utilidades.convertirADouble(mundo.getCodigoM()));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Modificar un registro de Unidad.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAct
	 * @return
	 */
	public static boolean insertarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarActivi,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("insertarActivi---------------> "+insertarActivi);
			ps.setDouble(1, Utilidades.convertirADouble(mundo.getCodigoM()));
			ps.setInt(2, codigoAct);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("Error. Insertando una Actividad por Unidad en Modificacion.>>>>"+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param codigoAct
	 * @return
	 */
	public static boolean eliminarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct)
	{
		try
		{
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(eliminarActxUnid2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps2.setDouble(1, Utilidades.convertirADouble(mundo.getCodigoM()));
			ps2.setInt(2, codigoAct);
		
			if(ps2.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}