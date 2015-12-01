package com.princetonsa.dao.sqlbase.cargos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

public class SqlBaseUtilidadJustificacionPendienteArtServDao
{
	private static Logger logger = Logger.getLogger(SqlBaseUtilidadJustificacionPendienteArtServDao.class);
	
	private static String [] indicesMapNP={"requiere_justificacion_art_","req_just_art_nopos_dif_med_","solicitud_","es_pos_","codigo_"};
	
	private static String [] indicesMapNPS={"requiere_justificacion_art_","req_just_art_nopos_dif_med_","solicitud_","es_pos_","codigo_"};
	
	/**
	 * 
	 */
	private static String consultaStrNP = "SELECT DISTINCT " +
												"c.requiere_justificacion_art, " +
												"c.req_just_art_nopos_dif_med, " +
												"dtc.solicitud, " +
												"na.es_pos, " +
												"a.codigo " +
											"FROM " +
												"det_cargos dtc " +
												"INNER JOIN convenios c ON (dtc.convenio = c.codigo) " +
												"INNER JOIN articulo a ON (dtc.articulo = a.codigo) " + 
												"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo) " +
											"WHERE " +
												"c.req_just_art_nopos_dif_med = '"+ConstantesBD.acronimoSi+"' " +
												"AND dtc.solicitud = ? " +
												"AND na.es_pos= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "+
												"AND a.codigo = ? ";
	
	/**
	 * 
	 */
	private static String consultaStrNPmqx = "SELECT DISTINCT " +
												"c.requiere_justificacion_art, " +
												"c.req_just_art_nopos_dif_med, " +
												"dmqx.numero_solicitud, " +
												"na.es_pos, " +
												"a.codigo " +
											"FROM " +
												"det_materiales_qx dmqx " +
												"INNER JOIN materiales_qx mqx ON (dmqx.numero_solicitud = mqx.numero_solicitud) " +
												"INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = mqx.numero_solicitud) " +
												"INNER JOIN sub_cuentas subc ON (subc.sub_cuenta = sc.sub_cuenta) " +														
												"INNER JOIN convenios c ON (subc.convenio = c.codigo) " +
												"INNER JOIN articulo a ON (dmqx.articulo = a.codigo) " + 
												"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo) " +
											"WHERE " +
												"c.req_just_art_nopos_dif_med = '"+ConstantesBD.acronimoSi+"' " +
												"AND dmqx.numero_solicitud = ? " +
												"AND na.es_pos= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "+
												"AND a.codigo = ?";
	
	/**
	 * 
	 */
	private static String consultaStrNP2 = "SELECT DISTINCT " +
												"c.requiere_justificacion_art, " +
												"c.req_just_art_nopos_dif_med, " +
												"dtc.solicitud, " +
												"na.es_pos, " +
												"a.codigo " +
											"FROM " +
												"det_cargos dtc " +
												"INNER JOIN convenios c ON (dtc.convenio = c.codigo) " +
												"INNER JOIN articulo a ON (dtc.articulo = a.codigo) " + 
												"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo) " +
											"WHERE " +
												"c.requiere_justificacion_art = '"+ConstantesBD.acronimoSi+"' " +
												"AND dtc.solicitud = ? " +
												"AND na.es_pos= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "+
												"AND a.codigo = ?";
	
	/**
	 * 
	 */
	private static String consultaStrNP2mqx = "SELECT DISTINCT " +
													"c.requiere_justificacion_art, " +
													"c.req_just_art_nopos_dif_med, " +
													"dmqx.numero_solicitud, " +
													"na.es_pos, " +
													"a.codigo " +
												"FROM " +
													"det_materiales_qx dmqx " +
													"INNER JOIN materiales_qx mqx ON (dmqx.numero_solicitud = mqx.numero_solicitud) " +
													"INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = mqx.numero_solicitud) " +
													"INNER JOIN sub_cuentas subc ON (subc.sub_cuenta = sc.sub_cuenta) " +														
													"INNER JOIN convenios c ON (subc.convenio = c.codigo) " +
													"INNER JOIN articulo a ON (dmqx.articulo = a.codigo) " + 
													"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo) " +
												"WHERE " +
													"c.requiere_justificacion_art = '"+ConstantesBD.acronimoSi+"' " +
													"AND dmqx.numero_solicitud = ? " +
													"AND na.es_pos= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' "+
													"AND a.codigo = ?";
	
	/**
	 * 
	 */
	private static String insertarJusPStr = "INSERT INTO jus_pendiente_articulos (codigo, numero_solicitud,articulo, fecha_modifica, hora_modifica, usuario_modifica, cantidad, tipo_jus) VALUES (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	
	/**
	 * 
	 */
	private static String insertarResponsableJusPendienteArt = "INSERT INTO det_jus_pendiente_articulos (jus_pendiente_articulos,sub_cuenta,cantidad) VALUES (?,?,?)";
	
	/**
	 * 
	 */
	private static String insertarResponsableJusPendienteServ = "INSERT INTO det_jus_pendiente_servicios (jus_pendiente_servicios,sub_cuenta,cantidad) VALUES (?,?,?)";
	
	/**
	 * 
	 */
	private static String consultaStrNPS = "SELECT DISTINCT * " +
												"FROM " +
													"det_cargos dtc " +
													"INNER JOIN convenios c ON (dtc.convenio = c.codigo) " +
													"INNER JOIN servicios s ON (dtc.servicio = s.codigo) " +
												"WHERE " +
													"c.requiere_justificacion_serv = '"+ConstantesBD.acronimoSi+"' " +
													"AND dtc.solicitud = ? " +
													"AND s.espos = "+ValoresPorDefecto.getValorFalseParaConsultas()+" " +
													"AND s.codigo = ? ";
	
	
	private static String insertarJusPStrS="INSERT INTO inventarios.jus_pendiente_servicios (codigo, numero_solicitud,servicio, fecha_modifica, hora_modifica, usuario_modifica, cantidad) VALUES (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	private static String consultaStrNaturalezaArt = "SELECT " +
														"na.es_medicamento AS naturalezaa " +
													"FROM " +
														"articulo a " +
														"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo AND a.institucion = na.institucion) " +
													"WHERE " +
														"a.codigo = ?";
	
	private static String consultaStrSubCuenta="SELECT sub_cuenta FROM solicitudes_cirugia WHERE numero_solicitud = ? ";
	
	private static String consultaStrConv="SELECT convenio from sub_cuentas	WHERE sub_cuenta = ? ";
	
	private static String consultaStrNPC="SELECT * FROM convenios c WHERE c.requiere_justificacion_serv = 'S' AND c.codigo=? ";
	
	private static String consultaStrNPCS="SELECT * FROM servicios s WHERE s.espos = "+ValoresPorDefecto.getValorFalseParaConsultas()+" AND s.codigo = ? ";
	
	private static String consultaValidacionExistenciaA="SELECT * FROM jus_pendiente_articulos WHERE numero_solicitud=? AND articulo=?";
	
	private static String consultaValidacionExistenciaS="SELECT * FROM jus_pendiente_servicios WHERE numero_solicitud=? AND servicio=?";
	
	private static String consultaExisteJusDiligenciadaA="SELECT * FROM justificacion_art_sol WHERE numero_solicitud=? AND articulo=?";
	
	private static String consultaExisteJusDiligenciadaS="SELECT * FROM justificacion_serv_sol WHERE solicitud=? AND servicio=?";
	
	private static String actualizarCantidadJusPendienteA="UPDATE jus_pendiente_articulos SET cantidad=? WHERE numero_solicitud=? AND articulo=?";
	
	private static String actualizarCantidadJusPendienteS="UPDATE jus_pendiente_servicios SET cantidad=? WHERE numero_solicitud=? AND servicio=?";
	
	private static String actualizarCantidadJusDiligenciadaA="UPDATE justificacion_art_resp SET cantidad=? WHERE justificacion_art_sol=(SELECT codigo FROM justificacion_art_sol WHERE numero_solicitud=? AND articulo=?)";
	
	private static String actualizarCantidadJusDiligenciadaS="UPDATE justificacion_serv_resp SET cantidad=? WHERE justificacion_art_sol=(SELECT codigo FROM justificacion_serv_sol WHERE solicitud=? AND servicio=?)";
	
	/**
	 * Consultar los responsables de una justificación pendiente de articulos
	 */
	private static String consultarResponsablesJusPendienteArt = "SELECT " +
																	"djpa.jus_pendiente_articulos as codigo, " +
																	"djpa.sub_cuenta as subcuenta, " +
																	"djpa.cantidad as cantidad " +
																"FROM " +
																	"jus_pendiente_articulos jpa " +
																"INNER JOIN " +
																	"det_jus_pendiente_articulos djpa ON (djpa.jus_pendiente_articulos = jpa.codigo) " +
																"WHERE " +
																	"jpa.numero_solicitud=? " +
																	"AND jpa.articulo=?";
	
	/**
	 * Consultar los responsables de una justificación pendiente de Servicios
	 */
	private static String consultarResponsablesJusPendienteServ = "SELECT " +
																	"djps.jus_pendiente_servicios as codigo, " +
																	"djps.sub_cuenta as subcuenta, " +
																	"djps.cantidad as cantidad " +
																"FROM " +
																	"jus_pendiente_servicios jps " +
																"INNER JOIN " +
																	"det_jus_pendiente_servicios djps ON (djps.jus_pendiente_servicios = jps.codigo) " +
																"WHERE " +
																	"jps.numero_solicitud=? " +
																	"AND jps.servicio=?";
	
	
	/**
	 * Metodo para validar e insertar una justificacion pendiente de un servicio DYT
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param usuario
	 * @return
	 */
	public static boolean justificacionPendienteServicioCirugiaDYT(Connection con, int numeroSolicitud, int codigoServicio, String usuario)
	{
		logger.info("ENTTROOOOOOOOO111111111");
		HashMap<String, Object> resultadosNP = new HashMap<String, Object>();
		HashMap<String, Object> resultadosNP2 = new HashMap<String, Object>();
		ResultSetDecorator rs, rs2;
		int convenio=0, sub_cuenta=0, aux1=0, aux2=0;
		PreparedStatementDecorator ps, ps2, ps3, ps4, ps5;
		try
		{
			ps5= new PreparedStatementDecorator(con.prepareStatement(consultaStrSubCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps5.setInt(1, numeroSolicitud);
			
			rs2=new ResultSetDecorator(ps5.executeQuery());
			if(rs2.next()){
				sub_cuenta = rs2.getInt("sub_cuenta");
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta de la Sub Cuenta");
			return false;
		}
		if(sub_cuenta>0){
			logger.info("ENTTROOOOOOOOO2222222222");
			try
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaStrConv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, sub_cuenta);
				
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next()){
					convenio = rs.getInt("convenio");
				}
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en la consulta del convenio");
				return false;
			}
		}
		else{
			logger.error("Error. La consulta de la Sub Cuenta arrojo un codigo no valido.");
			return false;
		}
		if(convenio > 0){
			logger.info("ENTTROOOOOOOOO333333333333");
			try
			{
				ps2= new PreparedStatementDecorator(con.prepareStatement(consultaStrNPC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps2.setInt(1, convenio);
			
				resultadosNP = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery()),true, true);
				
				if(Utilidades.convertirAEntero(resultadosNP.get("numRegistros").toString())>0)
				{
					aux1=1;
				}
				
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en la consulta del convenio no POS");
				return false;
			}
		}
		else{
			logger.error("Error. La consulta del convenio arrojo un codigo no valido.");
			return false;
		}
		if(aux1 == 1){
			logger.info("ENTTROOOOOOOOO4444444444");
			try
			{
				ps3= new PreparedStatementDecorator(con.prepareStatement(consultaStrNPCS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps3.setInt(1, codigoServicio);
			
				resultadosNP2 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps3.executeQuery()),true, true);
				
				if(Utilidades.convertirAEntero(resultadosNP2.get("numRegistros").toString())>0)
				{
					aux2=1;
				}
			}
			catch (SQLException e)
			{
				logger.error(e+" Error en la consulta de Servicio No Pos");
				return false;
			}
		}
		if(aux2 == 1){
			logger.info("ENTTROOOOOOOOO55555555555");
			try
			{
				logger.error("AQUI ESTÁ EL ERROR, solamente pasan 3 parámetros");
				ps4= new PreparedStatementDecorator(con.prepareStatement(insertarJusPStrS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				/* codigo,
				 * numero_solicitud,
				 * servicio,
				 *  fecha_modifica,
				 *  hora_modifica,
				 *  usuario_modifica,
				 *  cantidad
				 */
				int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "inventarios.seq_jus_pendiente_servicios");
				ps4.setInt(1, codigo);
				ps4.setInt(2, numeroSolicitud);
				ps4.setInt(3, codigoServicio);
				ps4.setString(4, usuario);
				//solo se puede guardar por defecto una sola cirugia en DYT
				ps4.setString(5, "1");
				
				if(ps4.executeUpdate()>0){
					return true;
				}
				
			}
			catch (SQLException e)
			{
				logger.error(e+" Error insertando la justificacion pendiente de una servicio en Cirugias DYT");
			}
		}
		return false;
	}
	

	/**
	 * Metodo que valida el convenio y el articulo o servicio no pos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param esArticulo
	 * @return
	 */
	public static boolean validarNOPOS(Connection con, int numeroSolicitud, int codigoArticulo, boolean esArticulo, boolean materialesqx, int codigoConvenio) throws BDException
	{
		HashMap<String, Object> resultadosNP = new HashMap<String, Object>();
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		String natu = "";
		String consulta = "";
		boolean resultado=false;
		try{
			Log4JManager.info("############## Inicio validarNOPOS");
			if(esArticulo)
			{
				pst = con.prepareStatement(consultaStrNaturalezaArt,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, codigoArticulo);
				rs=pst.executeQuery();
				if(rs.next()){
					natu = rs.getString("naturalezaa");
				}
				if(natu.equals(ConstantesBD.acronimoNo)){
					if(materialesqx){
						if(codigoConvenio<=0){
							consulta = consultaStrNPmqx;
						}
						else{
							consulta = consultaStrNPmqx+" AND c.codigo = "+codigoConvenio+" ";
						}
					}
					else{
						if(codigoConvenio<=0){
							consulta = consultaStrNP;
						}
						else{
							consulta = consultaStrNP+" AND c.codigo = "+codigoConvenio+" ";
						}
					}
					
					pst2 = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst2.setInt(1, numeroSolicitud);
					pst2.setInt(2, codigoArticulo);
					rs2=pst2.executeQuery();
					int cont=0;
					resultadosNP.put("numRegistros","0");
					ResultSetMetaData rsm=rs2.getMetaData();
					while(rs2.next())
					{
						for(int i=1;i<=rsm.getColumnCount();i++)
						{
							String alias=rsm.getColumnLabel(i).toLowerCase();
							int index=alias.indexOf("_");
							while(index>0)
							{
								index=alias.indexOf("_");
								try{
									String caracter=alias.charAt(index+1)+"";
									{
										alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
									}
								}
								catch(IndexOutOfBoundsException e)
								{
									break;
								}
							}
							resultadosNP.put(alias+"_"+cont, rs2.getObject(rsm.getColumnLabel(i))==null?"":rs2.getObject(rsm.getColumnLabel(i)));
						}
						cont++;
					}
					resultadosNP.put("numRegistros", cont+"");
					resultadosNP.put("INDICES",indicesMapNP);
					if(Integer.parseInt(resultadosNP.get("numRegistros").toString())>0){
						resultado= true;
					}
				}
				else
				{
					if(materialesqx){
						if(codigoConvenio<=0){
							consulta = consultaStrNP2mqx;
						}
						else{
							consulta = consultaStrNP2mqx+" AND c.codigo = "+codigoConvenio+" ";
						}
					}
					else
					{
						if(codigoConvenio<=0){
							consulta = consultaStrNP2;
						}
						else{
							consulta = consultaStrNP2+" AND c.codigo = "+codigoConvenio+" ";
						}
					}
					pst2 = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst2.setInt(1, numeroSolicitud);
					pst2.setInt(2, codigoArticulo);
					rs2=pst2.executeQuery();
					int cont=0;
					resultadosNP.put("numRegistros","0");
					ResultSetMetaData rsm=rs2.getMetaData();
					while(rs2.next())
					{
						for(int i=1;i<=rsm.getColumnCount();i++)
						{
							String alias=rsm.getColumnLabel(i).toLowerCase();
							int index=alias.indexOf("_");
							while(index>0)
							{
								index=alias.indexOf("_");
								try{
									String caracter=alias.charAt(index+1)+"";
									{
										alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
									}
								}
								catch(IndexOutOfBoundsException e)
								{
									break;
								}
							}
							resultadosNP.put(alias+"_"+cont, rs2.getObject(rsm.getColumnLabel(i))==null?"":rs2.getObject(rsm.getColumnLabel(i)));
						}
						cont++;
					}
					resultadosNP.put("numRegistros", cont+"");
					resultadosNP.put("INDICES",indicesMapNP);
					if(Utilidades.convertirAEntero(resultadosNP.get("numRegistros").toString())>0){
						resultado= true;
					}
				}
			}
			else
			{
				if(codigoConvenio<=0){
					consulta = consultaStrNPS;
				}
				else{
					consulta = consultaStrNPS+" AND c.codigo = "+codigoConvenio+" ";
				}
				pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, numeroSolicitud);
				pst.setInt(2, codigoArticulo);
				rs=pst.executeQuery();
				int cont=0;
				resultadosNP.put("numRegistros","0");
				ResultSetMetaData rsm=rs.getMetaData();
				while(rs.next())
				{
					for(int i=1;i<=rsm.getColumnCount();i++)
					{
						String alias=rsm.getColumnLabel(i).toLowerCase();
						int index=alias.indexOf("_");
						while(index>0)
						{
							index=alias.indexOf("_");
							try{
								String caracter=alias.charAt(index+1)+"";
								{
									alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
								}
							}
							catch(IndexOutOfBoundsException e)
							{
								break;
							}
						}
						resultadosNP.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
					}
					cont++;
				}
				resultadosNP.put("numRegistros", cont+"");
				resultadosNP.put("INDICES",indicesMapNPS);
				if(Utilidades.convertirAEntero(resultadosNP.get("numRegistros").toString())>0){
					resultado=true;
				}
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin validarNOPOS");
		return resultado;
	}
	
	/**
	 * Metodo para la insercion de una justificacion pendiente de un servicio o articulo nopos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param usuario
	 * @param esArticulo
	 * @return
	 */
	public static boolean insertarJusP(Connection con, int numeroSolicitud, int codigoArticulo, int cantidad, String usuario, boolean esArticulo, boolean actualizarCant, int subcuenta, String tipo_jus)
	{
		String sqlExisteJusPendiente = "";
		//String sqlInsertarJusPendiente = "";
		String sqlExisteJusDiligenciada = "";
		String sqlActualizarCantJusPendiente = "";
		String sqlActualizarCantJusDiligenciada = "";
		PreparedStatementDecorator ps;
		boolean insertarJusPendiente = false;
		boolean actualizarCantidadJusPendiente = false;
		boolean actualizarCantidadJusDiligenciada = false;
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		boolean exito=false;
		
		if(esArticulo == true){
			// Sentencias Necesarias si la Justificación es de Articulos
			sqlExisteJusPendiente = consultaValidacionExistenciaA;
			sqlExisteJusDiligenciada = consultaExisteJusDiligenciadaA;
			sqlActualizarCantJusPendiente = actualizarCantidadJusPendienteA;
			sqlActualizarCantJusDiligenciada = actualizarCantidadJusDiligenciadaA;
		} else {
			// Sentencias Necesarias si la Justificación es de Servicios
			sqlExisteJusPendiente = consultaValidacionExistenciaS;
			sqlExisteJusDiligenciada = consultaExisteJusDiligenciadaS;
			sqlActualizarCantJusPendiente = actualizarCantidadJusPendienteS;
			sqlActualizarCantJusDiligenciada = actualizarCantidadJusDiligenciadaS;
		}
		
		
		if (actualizarCant){
			
			//****************** Flujo para actualizar las cantidades si la 
			//****************** justificación pendiente o diligenciada ya existe
			
			// Verificar si ya hay una justificación pendiente
			if(existeJustificacionPendiente(con, sqlExisteJusPendiente, numeroSolicitud, codigoArticulo)){
				
				// Si hay una justificacion pendiente se puede actualizar la cantidad 
				actualizarCantidadJusPendiente = true;
				
			} else {
				
				// Verificar si ya hay una justificacion diligenciada
				if(existeJustificacionDiligenciada(con, sqlExisteJusDiligenciada, numeroSolicitud, codigoArticulo)){
					
					// Se actualiza la cantidad de la justificación diligenciada
					actualizarCantidadJusDiligenciada = true;
					
				} else {
					
					// Se puede insertar una justificación pendiente
					insertarJusPendiente = true;
				}
			}
		} else {

			// Verificar si ya hay una justificación pendiente
			if(!existeJustificacionPendiente(con, sqlExisteJusPendiente, numeroSolicitud, codigoArticulo)){
				
				// Se puede insertar una justificación pendiente
				insertarJusPendiente = true;
				
			}
		}
		
		if (insertarJusPendiente)
			exito = insertarJusPendiente(con, numeroSolicitud, codigoArticulo, usuario, cantidad, esArticulo, subcuenta, tipo_jus);
		
		if (actualizarCantidadJusPendiente)
			exito = actualizarCantidadJusPendiente(con, sqlActualizarCantJusPendiente, numeroSolicitud, codigoArticulo, usuario, cantidad);
		
		if (actualizarCantidadJusDiligenciada)
			
			
			
			
			
			
			
			exito = actualizarCantidadJusDiligenciada(con, sqlActualizarCantJusDiligenciada, numeroSolicitud, codigoArticulo, usuario, cantidad);
		
		return exito;
	}
	
	/**
	 * Consulta si hay una justificación pendiente
	 */
	public static boolean existeJustificacionPendiente(Connection con, String sql, int numero_solicitud, int codigoServArt){
		PreparedStatementDecorator ps;
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numero_solicitud);
			ps.setInt(2, codigoServArt);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			resultados = UtilidadBD.cargarValueObject(rs,true, true);
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			logger.info("Error verificando si ya hay una justificación pendiente | "+e);
		}
		if(Utilidades.convertirAEntero(resultados.get("numRegistros").toString())>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Consulta si hay una justificación diligenciada
	 */
	public static boolean existeJustificacionDiligenciada(Connection con, String sql, int numero_solicitud, int codigoServArt){
		PreparedStatementDecorator ps;
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numero_solicitud);
			ps.setInt(2, codigoServArt);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
		}
		catch (SQLException e)
		{
			logger.info("Error verificando si ya hay una justificación diligenciada | "+e);
		}
		if(Utilidades.convertirAEntero(resultados.get("numRegistros").toString())>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Insetar una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param numero_solicitud
	 * @param usuario
	 * @return
	 */
	public static boolean insertarJusPendiente (Connection con, int numeroSolicitud, int codigoArticulo, String usuario, int cantidad, boolean esArticulo, int subcuenta, String tipoJus){
		logger.info("Llega a insertar la justificación "+esArticulo);
		boolean exito=false;
		PreparedStatementDecorator ps;
		String sqlInsertarJusPendiente="";
		String sqlInsertarDetJusPendiente="";
		
		int codigo = ConstantesBD.codigoNuncaValido;
		
		if(esArticulo){
			// Sentencias Necesarias si la Justificación es de Articulos
			sqlInsertarJusPendiente = insertarJusPStr;
			sqlInsertarDetJusPendiente = insertarResponsableJusPendienteArt;
			codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "inventarios.seq_jus_pendiente_articulos");
		} else {
			// Sentencias Necesarias si la Justificación es de Servicios
			sqlInsertarJusPendiente = insertarJusPStrS;
			sqlInsertarDetJusPendiente = insertarResponsableJusPendienteServ;
			codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "inventarios.seq_jus_pendiente_servicios");
		}
		
		logger.info(sqlInsertarJusPendiente);
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sqlInsertarJusPendiente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			/* codigo,
			 * numero_solicitud,
			 * servicio,
			 *  fecha_modifica,
			 *  hora_modifica,
			 *  usuario_modifica,
			 *  cantidad
			 */
			ps.setInt(1, codigo);
			ps.setInt(2, numeroSolicitud);
			ps.setInt(3, codigoArticulo);
			ps.setString(4, usuario);
			ps.setInt(5, cantidad);
			if(esArticulo){
				if(tipoJus.isEmpty())
					ps.setNull(6, Types.VARCHAR);
				else
					ps.setString(6, tipoJus);
			}
			
			if(ps.executeUpdate()>0){
				ps.close();
				try
				{
					logger.info("Subcuenta: "+subcuenta);
					ps= new PreparedStatementDecorator(con.prepareStatement(sqlInsertarDetJusPendiente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
					ps.setDouble(1, codigo);
					ps.setInt(2, subcuenta);
					ps.setInt(3, cantidad);
					
					if(ps.executeUpdate()>0)
					{
						exito = true;
					}
					ps.close();
				}
				catch (SQLException e) {
					logger.info("Error insertando el responsable de la justificación pendiente / "+e);
				}	
			}
			else
			{
				ps.close();
			}
		}
		catch (SQLException e) {
			logger.info("Error insertando la justificación pendiente / "+e);
		}
		return exito;
	}
	
	/**
	 * Actualizar la cantidad de una justificacion pendiente
	 * @param con
	 * @param sql
	 * @param numero_solicitud
	 * @param codigoServArt
	 * @return
	 */
	public static boolean actualizarCantidadJusPendiente (Connection con, String sql, int numero_solicitud, int codigoServArt, String usuario, int cantidad){
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, cantidad);
			ps.setInt(2, numero_solicitud);
			ps.setInt(3, codigoServArt);
			
			if(ps.executeUpdate()>0)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			logger.info("Error actualizando la cantidad de la justificacion pendiente / "+e);
			return false;
		}
	}
	
	/**
	 * Actualizar la cantidad de una justificacion diligenciada
	 * @param con
	 * @param sql
	 * @param numero_solicitud
	 * @param codigoServArt
	 * @return
	 */
	public static boolean actualizarCantidadJusDiligenciada (Connection con, String sql, int numero_solicitud, int codigoServArt, String usuario, int cantidad){
		PreparedStatementDecorator ps;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, cantidad);
			ps.setInt(2, numero_solicitud);
			ps.setInt(3, codigoServArt);
			
			if(ps.executeUpdate()>0)
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			logger.info("Error actualizando la cantidad de la justificacion diligenciada / "+e);
			return false;
		}
	}
	
	/**
	 * Eliminar Justificación pendiente
	 * @param con
	 * @param solicitud
	 * @param codigo
	 * @param esArticulo
	 * @return
	 */
	public static boolean eliminarJusNoposPendiente(Connection con, int solicitud, int codigo, boolean esArticulo){
		String sentencia="";
		if (esArticulo)
			sentencia="SELECT codigo FROM jus_pendiente_articulos WHERE numero_solicitud="+solicitud+" AND articulo="+codigo;
		else
			sentencia="SELECT codigo FROM jus_pendiente_servicios WHERE numero_solicitud="+solicitud+" AND servicio="+codigo;
		try {
			logger.info("eliminarJusNoposPendiente / Paso 1 / "+sentencia);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next()){
				double codigoJusPendiente = rs.getDouble("codigo");
				
				if (esArticulo)
					sentencia="DELETE FROM det_jus_pendiente_articulos WHERE jus_pendiente_articulos="+codigoJusPendiente;
				else
					sentencia="DELETE FROM det_jus_pendiente_servicios WHERE jus_pendiente_servicios="+codigoJusPendiente;
				
				logger.info("eliminarJusNoposPendiente / Paso 2 / "+sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(ps.executeUpdate()>0){
					if (esArticulo)
						sentencia="DELETE FROM jus_pendiente_articulos WHERE codigo="+codigoJusPendiente;
					else
						sentencia="DELETE FROM jus_pendiente_servicios WHERE codigo="+codigoJusPendiente;
					
					logger.info("eliminarJusNoposPendiente / Paso 3 / "+sentencia);
					ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					if(ps.executeUpdate()<0)
						return false;
				} else 
					return false;	
			} else 
				return true;
		}
		catch (SQLException e)
		{
			logger.info("Error eliminando la justificación pendiente / "+e);
			return false;
		}
			
		return true;
	}
	
	/**
	 * Obtiene los responsables de una justificación pendiente
	 * 		keys del mapa
	 * 		- subcuenta
	 * 		- cantidad
	 * @param con
	 * @param numeroSolicitud
	 * @param codigo
	 * @param esArticulo
	 * @return
	 */
	public static HashMap obtenerResponsablesJusPendiente(Connection con, int solicitud, int codigo, boolean esArticulo){
		PreparedStatementDecorator ps;
		HashMap<String, Object> responsables = new HashMap<String, Object>();
		String sql = "";
		if(esArticulo)
			sql = consultarResponsablesJusPendienteArt;
		else
			sql = consultarResponsablesJusPendienteServ;
		
		logger.info("SQL / obtenerResponsablesJusPendiente / "+sql);
		logger.info("Parametro 1 = "+solicitud);
		logger.info("Parametro 2 = "+codigo);
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, solicitud);
			ps.setInt(2, codigo);
			responsables = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
		}
		catch (SQLException e)
		{
			logger.info("Error en obtenerResponsablesJusPendiente / "+e);
		}
		
		return responsables;
	}

	/**
	 * Elimina los responsables de una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param string
	 * @param b
	 * @return
	 */
	public static double eliminarResponsablesJustificacionPendiente(Connection con, int solicitud, int codigo,boolean esArticulo) throws BDException{
		double codigoJusPendiente=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		try {
			Log4JManager.info("############## Inicio eliminarResponsablesJustificacionPendiente");
			String sentencia="";
			String sentencia2="";
			if (esArticulo){
				sentencia="SELECT codigo FROM jus_pendiente_articulos WHERE numero_solicitud="+solicitud+" AND articulo="+codigo;
			}
			else{
				sentencia="SELECT codigo FROM jus_pendiente_servicios WHERE numero_solicitud="+solicitud+" AND servicio="+codigo;
			}
			pst = con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs =pst.executeQuery();
			
			if (rs.next()){
				codigoJusPendiente = rs.getDouble("codigo");
				if (esArticulo){
					sentencia2="DELETE FROM det_jus_pendiente_articulos WHERE jus_pendiente_articulos="+codigoJusPendiente;
				}
				else{
					sentencia2="DELETE FROM det_jus_pendiente_servicios WHERE jus_pendiente_servicios="+codigoJusPendiente;
				}
				pst2 =  con.prepareStatement(sentencia2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.executeUpdate();
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin eliminarResponsablesJustificacionPendiente");
		return codigoJusPendiente;
	}

	/**
	 * Inserta una responsable para una justificación pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param subCuenta
	 * @param cantidad
	 * @param codigoJustificacionPendiente
	 */
	public static boolean insertarResponsableJustificacionPendiente(Connection con, double codigoJustificacionPendiente, String subCuenta, String cantidad, boolean esArticulo) throws BDException {
		
		PreparedStatement pst=null;
		boolean resultado=false;
		try {
			Log4JManager.info("############## Inicio insertarResponsableJustificacionPendiente");
			String sentencia="";
			if (esArticulo){
				sentencia="INSERT INTO det_jus_pendiente_articulos VALUES ("+codigoJustificacionPendiente+", "+subCuenta+", "+cantidad+")";
			}
			else{
				sentencia="INSERT INTO det_jus_pendiente_servicios VALUES ("+codigoJustificacionPendiente+", "+subCuenta+", "+cantidad+")";
			}
			pst = con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			if(pst.executeUpdate()>0){
				resultado=true;
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarResponsableJustificacionPendiente");	
		return resultado;
	}

	/**
	 * Consulta si existe una justificación de orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param esArticulo
	 * @return
	 */
	public static boolean existeJustificacionDeOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, boolean esArticulo) {
		PreparedStatementDecorator ps;
		String sentencia="";
		if (esArticulo)
			sentencia="SELECT codigo FROM justificacion_art_sol WHERE orden_ambulatoria="+ordenAmbulatoria+" AND articulo="+codigo;
		else
			sentencia="SELECT codigo FROM justificacion_serv_sol WHERE orden_ambulatoria="+ordenAmbulatoria+" AND servicio="+codigo;
		try {
			logger.info("existeJustificacionDeOrdenAmbulatoria / "+sentencia);
			ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next()){
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.info("Error en existeJustificacionDeOrdenAmbulatoria / "+e);
		}	
		return false;
	}

	/**
	 * Actualiza un numero de solicitud en una justificacion de orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	public static boolean actualizarSolicitudJusOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, int solicitud, boolean esArticulo) {
		PreparedStatement ps=null;
		PreparedStatement psActualizarOrden=null;
		String sentencia="";
		try{
			if (esArticulo){
				//sentencia="UPDATE justificacion_art_sol SET numero_solicitud="+solicitud+" WHERE orden_ambulatoria="+ordenAmbulatoria+" AND articulo="+codigo;
				
				Integer codigoJustificacion=consultarCodigoJustificacionOrdenAmb(con, ordenAmbulatoria, codigo);
				
				if(codigoJustificacion!=null){	
					StringBuffer insertarSolJust=new StringBuffer("INSERT INTO INVENTARIOS.SOL_X_JUST_ART_SOL (NUMERO_SOLICITUD,CODIGO_JUSTIFICACION) ");
						insertarSolJust.append("values (")
						.append(solicitud)
						.append(", ")
						.append(codigoJustificacion)
						.append(") ");
					sentencia=insertarSolJust.toString();
					
					ps =  con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					
					boolean ejecucionExitosa=false;
					
					if (ps.executeUpdate()>0){
						ejecucionExitosa = true;
					}
					
					if(ejecucionExitosa){
						StringBuffer actualizarOrdenJust=new StringBuffer("UPDATE INVENTARIOS.ORD_AMB_JUST_ART_SOL SET MIGRADO = ");
						actualizarOrdenJust.append("'")
							.append(ConstantesBD.acronimoSi)
							.append("'")
							.append(" WHERE CODIGO_ORDEN = ")
							.append(ordenAmbulatoria)
							.append(" AND CODIGO_JUSTIFICACION = ")
							.append(codigoJustificacion);
						
						sentencia=actualizarOrdenJust.toString();
						
						psActualizarOrden =  con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						
						if (psActualizarOrden.executeUpdate()>0){
							return true;
						}
					}
					
				}else{
					logger.info("Error en actualizarSolicitudJusOrdenAmbulatoria / ");
					throw new RuntimeException("Error en actualizarSolicitudJusOrdenAmbulatoria");
				}
			}
			else{
				sentencia="UPDATE justificacion_serv_sol SET solicitud="+solicitud+" WHERE orden_ambulatoria="+ordenAmbulatoria+" AND servicio="+codigo;
				logger.info("actualizarSolicitudJusOrdenAmbulatoria / "+sentencia);
				ps =  con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				
				if (ps.executeUpdate()>0){
					return true;
				}
			}
		}catch (SQLException e)
		{
			logger.info("Error en actualizarSolicitudJusOrdenAmbulatoria / ",e);
			throw new RuntimeException(e);
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				if(psActualizarOrden!=null){
					psActualizarOrden.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				Log4JManager.error("ERROR CERRANDO PS - RS " , e);
			}
		}
		return false;
	}
	
	/**
	 * Permite consultar el codigo de una justificacion no pos de un articulo generada desde ordenes ambulatorias 
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param codigoArticulo
	 * @return
	 * @author jeilones
	 * @created 3/12/2012
	 */
	public static Integer consultarCodigoJustificacionOrdenAmb(Connection con,int codigoOrden,int codigoArticulo){
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			StringBuffer consultarJustificacion=new StringBuffer("SELECT JUST.CODIGO AS CODIGO FROM INVENTARIOS.JUSTIFICACION_ART_SOL JUST ")
				.append("INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL ORD_X_JUST ")
				.append("ON ORD_X_JUST.CODIGO_JUSTIFICACION = JUST.CODIGO ")
				.append("WHERE ORD_X_JUST.CODIGO_ORDEN = ? ")
				.append("AND JUST.ARTICULO = ? ");
			
			ps=con.prepareStatement(consultarJustificacion.toString());
			
			ps.setInt(1, codigoOrden);
			ps.setInt(2, codigoArticulo);
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				
				int codigoJustificacion=rs.getInt("CODIGO");
				
				return codigoJustificacion;
			}
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * Ingresar un nuevo registro de justificacion no pos segun una ya ingresada por medio de una orden ambulatoria
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo del servicio o articulo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	public static boolean ingresarJustificacionSegunOrdenAmbulatoria(Connection con, int ordenAmbulatoria, int codigo, int solicitud, boolean esArticulo, int institucion, int bd){
		boolean transaccionExitosa = true;
		int codigoJustificacion = codigoJustificacionSegunOrdenAmbulatoria(con, ordenAmbulatoria, codigo, esArticulo); 
		String sentencia="";
		PreparedStatementDecorator ps;
		
		if (!esArticulo){
			try {
				
				UtilidadBD.iniciarTransaccion(con);
				
				// obtener consecutivo para la solicitud de justificacion de servicio
				String consecutivoJust = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, institucion);
				
				//Insercion datos justificación en la tabla justificacion_serv_sol (SOLICITUD)
				int codJustServSolActual = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_sol");
				sentencia = "INSERT INTO justificacion_serv_sol " +
								"(codigo, consecutivo, solicitud, servicio, usuario_modifica, fecha_modifica, hora_modifica, fecha, institucion) " +
							"SELECT " +
								codJustServSolActual+" AS codigo, '"+consecutivoJust+"' AS consecutivo, "+solicitud+", jss.servicio, jss.usuario_modifica, jss.fecha_modifica, jss.hora_modifica, jss.fecha, jss.institucion FROM justificacion_serv_sol jss WHERE jss.orden_ambulatoria="+ordenAmbulatoria+" AND jss.servicio="+codigo;
				logger.info(sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(ps.executeUpdate()>0){
					//se da por utilizado el consecutivo
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, institucion, consecutivoJust, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				} else {
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, institucion, consecutivoJust, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					transaccionExitosa=false;
				}
				
				//Consultamos el codigo de la solicitud de justificacion ya ingresada en la orden ambulatoria
				int codJustServSol = ConstantesBD.codigoNuncaValido;
				sentencia = "SELECT codigo FROM justificacion_serv_sol WHERE orden_ambulatoria="+ordenAmbulatoria+" AND servicio="+codigo;
				logger.info(sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
				if (rs.next())
					codJustServSol = rs.getInt("codigo");
				
				//Inserción de datos en la tabla justificacion_serv_resp (RESPONSABLE)
				sentencia = "INSERT INTO justificacion_serv_resp " +
								"(codigo, justificacion_serv_sol, sub_cuenta, estado, cantidad) " +
							"SELECT ";
							if(bd==DaoFactory.POSTGRESQL)
								sentencia += "nextval('seq_justificacion_serv_resp') ";
							if(bd==DaoFactory.ORACLE)
								sentencia += "seq_justificacion_serv_resp.nextval ";
							sentencia+=" AS codigo, "+codJustServSolActual+" AS justificacion_serv_sol, jsr.sub_cuenta, jsr.estado, jsr.cantidad FROM justificacion_serv_resp jsr WHERE jsr.justificacion_serv_sol="+codJustServSol;
				logger.info(sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;

				//Insercion de datos en la tabla justificacion_serv_dx (DIAGNOSTICOS)
				sentencia = "INSERT INTO justificacion_serv_dx " +
								"(codigo, justificacion_serv_sol, acronimo_dx, tipo_cie, tipo_dx) " +
							"SELECT ";
							if(bd==DaoFactory.POSTGRESQL)
								sentencia += "nextval('seq_justificacion_serv_dx') ";
							if(bd==DaoFactory.ORACLE)
								sentencia += "seq_justificacion_serv_dx.nextval ";
							sentencia += " AS codigo, "+codJustServSolActual+" AS justificacion_serv_sol, jsd.acronimo_dx, jsd.tipo_cie, jsd.tipo_dx FROM justificacion_serv_dx jsd WHERE jsd.justificacion_serv_sol="+codJustServSol;
				logger.info(sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
				
				//Insercion de datos en la tabla justificacion_serv_fijo (CAMPOS FIJOS)
				sentencia = "INSERT INTO justificacion_serv_fijo " +
								"(codigo, justificacion_serv_sol, observacion, profesional_responsable) " +
							"SELECT ";
							if(bd==DaoFactory.POSTGRESQL)
								sentencia += "nextval('seq_justificacion_serv_fijo') ";
							if(bd==DaoFactory.ORACLE)
								sentencia += "seq_justificacion_serv_fijo.nextval ";
							sentencia += " AS codigo, "+codJustServSolActual+" AS justificacion_serv_sol, jsf.observacion, jsf.profesional_responsable FROM justificacion_serv_fijo jsf WHERE jsf.justificacion_serv_sol="+codJustServSol;
				logger.info(sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
				
				//Insercion de la tabla justificacion_serv_param (CAMPOS PARAMETRIZABLES)
				sentencia = "INSERT INTO justificacion_serv_param " +
								"(codigo, justificacion_serv_sol, valor, etiqueta_seccion, campo, etiqueta_campo, parametrizacion_jus, institucion) " +
							"SELECT ";
							if(bd==DaoFactory.POSTGRESQL)
								sentencia += "nextval('seq_justificacion_serv_param') ";
							if(bd==DaoFactory.ORACLE)
								sentencia += "seq_justificacion_serv_param.nextval ";
							sentencia += " AS codigo, "+codJustServSolActual+" AS justificacion_serv_sol, jsp.valor, jsp.etiqueta_seccion, jsp.campo, jsp.etiqueta_campo, jsp.parametrizacion_jus, jsp.institucion FROM justificacion_serv_param jsp WHERE jsp.justificacion_serv_sol="+codJustServSol;
				logger.info(sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
			}
			catch (SQLException e)
			{
				logger.info("Error en ingresarJustificacionSegunOrdenAmbulatoria / "+e);
			}
		}
		
		if(transaccionExitosa)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
			
		return transaccionExitosa;
	}


	/**
	 * Consultar el codigo de la justificacion no pos segun el numero de orden
	 * @param con
	 * @param ordenAmbulatoria
	 * @param codigo
	 * @param solicitud
	 * @param esArticulo
	 * @return
	 */
	private static int codigoJustificacionSegunOrdenAmbulatoria(Connection con,int ordenAmbulatoria, int codigo, boolean esArticulo) {
		PreparedStatementDecorator ps;
		String sentencia="";
		int codigoJustificacion = ConstantesBD.codigoNuncaValido;
		if (!esArticulo){
				sentencia="SELECT codigo FROM justificacion_art_sol WHERE orden_ambulatoria="+ordenAmbulatoria+" AND articulo="+codigo;
			try {
				logger.info("codigoJustificacionSegunOrdenAmbulatoria / "+sentencia);
				ps =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
				
				if (rs.next()){
					codigoJustificacion = rs.getInt("codigo");
				}
			}
			catch (SQLException e)
			{
				logger.info("Error en codigoJustificacionSegunOrdenAmbulatoria / "+e);
			}
		}
		return codigoJustificacion;
	}
	
	
	
}