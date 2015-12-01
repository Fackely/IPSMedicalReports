package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;

/**
 * @author axioma
 *
 */
public class SqlBaseFormatoJustServNoposDao {
	
	/**
	 * Objeto para manejar log de la clase
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseFormatoJustServNoposDao.class);
	
	
	
	private static String cadenaConsultaParametrizacionSecciones="SELECT " +
																		"codigo, " +
																		"etiqueta " +
																	"FROM " +
																		" secciones_jus " +
																	"WHERE " +
																		"mostrar='S' AND servicio='S' " +
																		"AND tipo_jus = 'SERV'" +
																	"ORDER BY " +
																		"orden";
	
	private static String cadenaConsultaParametrizacionCamposHijos1="SELECT " +
																		"codigo as codigocampo, " +
																		"etiqueta as etiquetacampo,";
	
	private static String cadenaConsultaParametrizacionCamposHijos2="campo_padre  as campopadre, " +
																		"getCodigoParamJusCampo(codigo) as codigoparamjuscampo "+
																	"FROM " +
																		"campos_jus " +
																	"WHERE " +
																		"campo_padre=? AND servicio='S' AND tipo_jus = 'SERV'";
	
	private static String cadenaConsultaParametrizacionFormulario1="SELECT " +
																		"param.requerido, " +
																		"param.tipo_html AS tipo," +
																		"param.mostrar, ";
																		
	
	private static String cadenaConsultaParametrizacionFormulario2="param.campo, " +
																		"getobtenerEtiquetaCampo(campo, 'S') as etiquetacampo, " +
																		"getNombreCampoJus(campo) as nombre," +
																		"param.codigo as codigoparametrizacion, " +
																		"campo.tamanio as tamanio, " +
																		"CASE " +
																			"WHEN (SELECT COUNT (*) " +
																				"FROM acciones_campos ac " +
																				"WHERE ac.campo_accion        = campo.codigo " +
																				"AND ac.SERVICIO_CAMPO_ACCION = campo.servicio)>0 " +
																			"THEN 'S' " +
																			"ELSE 'N' " +
																		"END AS tieneaccion " +
																	"FROM " +
																		"parametrizacion_jus param " +
																	"INNER JOIN campos_jus campo " +
																		"ON campo.codigo=param.campo "+
																	"WHERE " +
																		"param.mostrar='S' AND param.seccion=? AND param.servicio='"+ConstantesBD.acronimoSi+"' " +
																				"AND param.tipo_jus = 'SERV' " +
																				"AND campo.servicio = '"+ConstantesBD.acronimoSi+"' " +
																				
																	" ORDER BY " +
																		"param.orden ";
	
	private static String cadenaConsultaServicio= "SELECT descripcion FROM referencias_servicio WHERE servicio=? AND tipo_tarifario=?";
	
	private static String cadenaConsultaDatosPaciente="SELECT " +
															"s.fecha_solicitud as fecha," +
															"vi.nombre||' - '||getnombrealmacen(s.centro_costo_solicitante) as servsolicitadoen, " +
															"c.id_ingreso as ingreso," +
															"coalesce(getconsecutivoingreso(c.id_ingreso), '') AS ingconsecutivo, " +
															"p.tipo_identificacion || ' ' || p.numero_identificacion as documentopaciente," +
															"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombrepaciente," +
															"p.fecha_nacimiento as nacimiento " +
														"FROM " +
															"solicitudes s " +
														"INNER JOIN " +
															"cuentas c ON c.id=s.cuenta " +
														"INNER JOIN " +
															"vias_ingreso vi  ON c.via_ingreso=vi.codigo " +
														"INNER JOIN " +
															"personas p ON c.codigo_paciente=p.codigo " +
														"WHERE " +
															"s.numero_solicitud=? ";
	
	private static String cadenaConsultaDatosPacienteXOrden = "SELECT " +
																 "oa.fecha as fecha, " +
																 "vi.nombre||' - '||getnombrealmacen(oa.centro_costo_solicita) as servsolicitadoen, " +
																 "c.id_ingreso as ingreso," +
																 "coalesce(getconsecutivoingreso(c.id_ingreso), '') AS ingconsecutivo, " +
																 "p.tipo_identificacion || ' ' || p.numero_identificacion as documentopaciente," +
																 "p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombrepaciente," +
																 "p.fecha_nacimiento as nacimiento " +
															  "FROM ordenes_ambulatorias oa " +
															     "INNER JOIN cuentas c ON (c.id=oa.cuenta_solicitante) " +
															     "INNER JOIN personas p ON c.codigo_paciente=p.codigo  " +
																 "INNER JOIN vias_ingreso vi ON vi.codigo = c.via_ingreso  " +
																 "LEFT JOIN egresos eg ON eg.cuenta = c.id  " +
															  "WHERE oa.codigo = ? ";
																 
	private static String cadenaConsultaDatosMedico="SELECT " +
															"m.numero_registro as registromedico," +
															"p.tipo_identificacion || ' ' || p.numero_identificacion as documentomedico," +
															"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombremedico, " +
															"LOWER(getespecialidadesmedico(u.login, ', ')) as especialidad,m.firma_digital, " +
															"CASE " +
																"WHEN jss.ORDEN_AMBULATORIA is null " +
																"THEN '" +ConstantesBD.acronimoNo+"' " +
																"ELSE '" +ConstantesBD.acronimoSi+"' "
															+ "END as es_orden_ambulatoria " +
														"FROM " +
															"justificacion_serv_sol jss " +
														"INNER JOIN " +
															"justificacion_serv_fijo jsf ON jss.codigo = jsf.justificacion_serv_sol " +
														"INNER JOIN " +
															"medicos m ON jsf.profesional_responsable = m.codigo_medico " +
														"INNER JOIN " +
															"personas p ON jsf.profesional_responsable = p.codigo " +		
														"INNER JOIN " +
															"usuarios u ON jsf.profesional_responsable = u.codigo_persona ";	

	
	private static String cadenaConsultaDatosFijos = "SELECT " +
															"jsr.estado as acronimo_estado, " +
															"getintegridaddominio(jsr.estado) as estado, " +
															"jsr.cantidad, " +
															"getconveniosubcuenta(jsr.sub_cuenta) As sub_cuenta, " +
															"jss.consecutivo as codigo, " +
															"jsf.observacion as observacion " +
														"FROM " +
															"justificacion_serv_sol jss " +
														"INNER JOIN " +
															"justificacion_serv_fijo jsf ON jss.codigo=jsf.justificacion_serv_sol " +
														"INNER JOIN " +
															"justificacion_serv_resp jsr ON (jss.codigo=jsr.justificacion_serv_sol ";
	
	private static String cadenaConsultaDiagnosticos = "SELECT " +
															"jsd.acronimo_dx as acronimo," +
															"jsd.tipo_cie, " +
															"jsd.tipo_dx, " +
															"d.nombre " +
														"FROM " +
															"justificacion_serv_sol jss " +
														"INNER JOIN " +
															"justificacion_serv_dx jsd ON jsd.justificacion_serv_sol = jss.codigo " +
														"INNER JOIN " +
															"diagnosticos d ON d.acronimo = jsd.acronimo_dx AND d.tipo_cie = jsd.tipo_cie " +
														"WHERE " +
															"jss.consecutivo=?";
	
	private static String cadenaConsultaDatosParam = "SELECT " +
															"jsp.valor as valor, " +
															"cj.nombre as nombre" +
														"FROM " +
															"justificacion_serv_param jsp" +
														"INNER JOIN " +
															"campos_jus cj ON (cj.codigo = jsp.campo AND cj.servicio='S') " +
														"WHERE " +
															"jsp.justificacion_serv_sol=?";
	
	private static String cadenaConsultaDatosServicio = "SELECT getNombreServicio(jss.servicio, ?) as nomservicio, jsr.cantidad FROM justificacion_serv_sol jss INNER JOIN justificacion_serv_resp jsr ON (jss.codigo=jsr.justificacion_serv_sol) ";
	
	private static String cadenaConsultaNumJust = "SELECT consecutivo as num FROM justificacion_serv_sol WHERE servicio=?";
	
	/**
	 * Sentencia SQL para insertar un registro en la tabla padre de las justificaciones de servicios con un numero de solicitud
	 */
	private static String insertarJustServSol = "INSERT INTO justificacion_serv_sol(" +
													"consecutivo, " +
													"solicitud, " +
													"servicio, " +
													"usuario_modifica, " +
													"fecha_modifica, " +
													"hora_modifica, " +
													"fecha, " +
													"codigo, " +
													"institucion) " +
												"VALUES (" +
													"?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",CURRENT_DATE,?,?)";
	
	/**
	 * 
	 * Sentencia SQL para insertar un registro en la tabla padre de las justificaciones de servicios con una orden ambulatoria
	 */
	private static String insertarJustServSolOrdenAmbulatoria = "INSERT INTO justificacion_serv_sol(" +
																	"consecutivo, " +
																	"orden_ambulatoria, " +
																	"servicio, " +
																	"usuario_modifica, " +
																	"fecha_modifica, " +
																	"hora_modifica, " +
																	"fecha, " +
																	"codigo, " +
																	"institucion) " +
																"VALUES (" +
																	"?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",CURRENT_DATE,?,?)";
	
	private static String consultarCodJustServSol = "SELECT codigo FROM justificacion_serv_sol WHERE consecutivo=?";
	
	private static String insertarJustServResp = "INSERT INTO justificacion_serv_resp(justificacion_serv_sol, sub_cuenta, estado, cantidad, codigo) VALUES (?,?,?,?,?)";
	
	private static String actualizarJustServResp = "UPDATE justificacion_serv_resp SET estado=?, cantidad=? WHERE justificacion_serv_sol=?";
	
	private static String insertarJustServDx = "INSERT INTO justificacion_serv_dx(justificacion_serv_sol, acronimo_dx, tipo_cie, tipo_dx, codigo) VALUES (?,?,?,?,?)";
	
	private static String eliminarJustServDx = "DELETE FROM justificacion_serv_dx WHERE justificacion_serv_sol=?";
	
	private static String insertarJustServFijo = "INSERT INTO justificacion_serv_fijo(justificacion_serv_sol, observacion, profesional_responsable, codigo) VALUES (?,?,?,?)";
	
	private static String actualizarJustServFijo = "UPDATE justificacion_serv_fijo SET observacion=? WHERE justificacion_serv_sol=?";
	
	private static String insertarJustServParam = "INSERT INTO justificacion_serv_param(justificacion_serv_sol, valor, etiqueta_seccion, campo, etiqueta_campo, parametrizacion_jus, institucion, codigo) VALUES (?,?,?,?,?,?,?,?)";
	
	private static String eliminarJustServParam = "DELETE FROM justificacion_serv_param WHERE justificacion_serv_sol=?";
	
	private static String [] indicesMap2={"sub_cuenta_","nombre_"};

	private static String consultaStr2="SELECT distinct " +
											"jsr.sub_cuenta, " +
											"getObtenerNombreSubCuenta(jsr.sub_cuenta) AS nombre " +
										"FROM " +
										 	"justificacion_serv_resp jsr " +
										"INNER JOIN " +
											"justificacion_serv_sol jss ON (jsr.justificacion_serv_sol=jss.codigo) " +
										"WHERE " +
											"jss.solicitud = ?";
	
	private static String insertarResponsable = "INSERT INTO justificacion_serv_resp(justificacion_serv_sol, sub_cuenta, estado, cantidad) VALUES (?,?,?,?)";
	
	///*********************************************///////////////////
	
	private static String [] indicesMapDistri={"codigo_"};
	
	private static String justificacionDistri="SELECT jsr.codigo " +
												"FROM justificacion_serv_resp jsr " +
														"INNER JOIN justificacion_serv_sol jss ON (jsr.justificacion_serv_sol=jss.codigo) " +
												"WHERE jsr.sub_cuenta=? AND jss.solicitud=? AND jss.servicio=? ";
	
	private static String borrarJustificacionDisti="DELETE FROM justificacion_serv_resp WHERE codigo=? ";
	
	private static String consultaJustificacionAnterior="SELECT codigo FROM justificacion_serv_sol WHERE solicitud=? AND servicio=? ";
	
	private static String insertarJustificacionDisti="INSERT INTO justificacion_serv_resp (estado, justificacion_serv_sol, sub_cuenta, cantidad) " +
														"VALUES (?,?,?,?) ";
	
	private static String insertarJusPStr="INSERT INTO jus_pendiente_servicios VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	private static String consultaJusPStr="SELECT * FROM jus_pendiente_servicios WHERE numero_solicitud=? AND servicio=? ";
	
	private static String [] indicesMapCons={"sub_cuenta_","codigo_","pos_"};
	
	private static String consultaNoPosArtConv="SELECT sb.sub_cuenta, c.codigo, (SELECT espos FROM servicios WHERE codigo=?) AS pos " +
												"FROM sub_cuentas sb " +
														"INNER JOIN convenios c ON (sb.convenio=c.codigo) " +
												"WHERE sb.sub_cuenta=? AND c.requiere_justificacion_serv = 'S' ";
	
	////////*************************************//////////////////
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @return
	 */
	public static boolean ingresarResponsable(Connection con, String numeroSolicitud, int subcuenta, int cantidad)
	{
		try
		{
			// Consultamos el codigo de la solicitud de justificacion
			int codJustServSol = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator ps;
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCodJustServSol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				codJustServSol = rs.getInt("codigo");
			
			// Se ingresa el responsable
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarResponsable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO justificacion_serv_sol(justificacion_serv_sol, sub_cuenta, estado, cantidad) VALUES (?,?,?,?)
			 */
			
			ps.setInt(1, codJustServSol);
			ps.setInt(2, subcuenta);
			ps.setString(3, "JUSTI");
			ps.setInt(4, cantidad);
			ps.executeUpdate();
			
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa justificacion
	 * @param solicitud
	 * @return
	 */
	public static String actualizarJustificacion(Connection con, int institucion, String usuarioModifica, HashMap justificacion, int solicitud, int servicio)
	{
		try
		{
			
			PreparedStatementDecorator ps;
			
			// Consultamos el codigo de la solicitud de justificacion
			int codJustServSol = ConstantesBD.codigoNuncaValido;
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCodJustServSol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, justificacion.get(servicio+"_numjustificacion").toString());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				codJustServSol = rs.getInt("codigo");
			
			// actualizacón de datos en la tabla justificacion_serv_resp (RESPONSABLE)
			ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarJustServResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE justificacion_serv_resp SET estado=?, cantidad=? WHERE justificacion_serv_sol=?
			 */
			
			ps.setString(1, justificacion.get(servicio+"_acronimo_estado").toString());
			ps.setDouble(2, Utilidades.convertirADouble((justificacion.get(servicio+"_cantidad").toString())));
			ps.setDouble(3, Utilidades.convertirADouble(codJustServSol+""));
			ps.executeUpdate();
			
			//Eliminamos los registros de diagnosticos para esa solicitud de justificacion
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
			ps.executeUpdate();
			
			// Actualizacion de datos en la tabla justificacion_serv_dx (DIAGNOSTICOS)
			String[] vector;
			// dx de complicacion
			if (justificacion.containsKey(servicio+"_diagnosticoComplicacion")){
				vector = justificacion.get(servicio+"_diagnosticoComplicacion").toString().split(ConstantesBD.separadorSplit);
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO justificacion_serv_dx(justificacion_serv_sol, acronimo_dx, tipo_cie, tipo_dx, codigo) VALUES (?,?,?,?,?)
				 */
				
				ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
				ps.setString(2, vector[0]);
				ps.setInt(3, Utilidades.convertirAEntero(vector[1]));
				ps.setString(4, "COMP");
				ps.setInt(5, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_dx"));
				ps.executeUpdate();
			}
			// dx principal
			vector = justificacion.get(servicio+"_diagnosticoPrincipal").toString().split(ConstantesBD.separadorSplit);
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codJustServSol);
			ps.setString(2, vector[0]);
			ps.setInt(3, Utilidades.convertirAEntero(vector[1]));
			ps.setString(4, "PRIN");
			ps.setDouble(5, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_dx")+""));
			ps.executeUpdate();
			//dx relacionados
			for (int h=0; h<Utilidades.convertirAEntero(justificacion.get(servicio+"_numDiagRela").toString()); h++){
				vector = justificacion.get(servicio+"_diagnosticoRelacionado_"+h).toString().split(ConstantesBD.separadorSplit);
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO justificacion_serv_dx(justificacion_serv_sol, acronimo_dx, tipo_cie, tipo_dx, codigo) VALUES (?,?,?,?,?)
				 */
				
				ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
				ps.setString(2, vector[0]);
				ps.setInt(3, Utilidades.convertirAEntero(vector[1]));
				ps.setString(4, "RELA");
				ps.setDouble(5, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_dx")+""));
				ps.executeUpdate();
			}
			
			// Actualizacion de datos en la tabla justificacion_serv_fijo (CAMPOS FIJOS)
			ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarJustServFijo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, justificacion.get(servicio+"_descjustificacion").toString());
			ps.setDouble(2, Utilidades.convertirADouble(codJustServSol+""));
			ps.executeUpdate();
			
			// Eliminamos toda la informacion de los campos parametrizables
			ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarJustServParam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
			ps.executeUpdate();
			
			// Insercion de la tabla justificacion_serv_param (CAMPOS PARAMETRIZABLES)
			HashMap seccionesMap=(HashMap) justificacion.get(servicio+"_mapasecciones");
			int numRegistrosSecciones=Integer.parseInt(seccionesMap.get("numRegistros").toString());
			for(int i=0;i<numRegistrosSecciones;i++){
				int numRegistrosXSeccion=Integer.parseInt(justificacion.get(servicio+"_numRegistrosXSec_"+seccionesMap.get("codigo_"+i).toString()).toString());;
				String codigoSeccion=seccionesMap.get("codigo_"+i).toString();
				for(int j=0;j<numRegistrosXSeccion;j++){
					if(!justificacion.get(servicio+"_tipo_"+codigoSeccion+"_"+j).toString().equals("LABE")){
						if(justificacion.get(servicio+"_tipo_"+codigoSeccion+"_"+j).toString().equals("CHEC")){
							for (int h=0; h<Utilidades.convertirAEntero(justificacion.get(servicio+"_numhijos_"+seccionesMap.get("codigo_"+i).toString()+"_"+j).toString()); h++){
								ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServParam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								
								/**
								 * INSERT INTO justificacion_serv_param(
								 * justificacion_serv_sol, 
								 * valor, 
								 * etiqueta_seccion, 
								 * campo, 
								 * etiqueta_campo, 
								 * parametrizacion_jus, 
								 * institucion, 
								 * codigo) VALUES (?,?,?,?,?,?,?,?)
								 */
								
								ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
								ps.setString(2, justificacion.get(servicio+"_valorcampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString());
								ps.setString(3, justificacion.get(servicio+"_etiguetaseccion_"+seccionesMap.get("codigo_"+i).toString()).toString());
								ps.setDouble(4, Utilidades.convertirADouble(justificacion.get(servicio+"_codigocampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString()));
								ps.setString(5, justificacion.get(servicio+"_etiquetacampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString());
								ps.setDouble(6, Utilidades.convertirADouble(justificacion.get(servicio+"_codigoparamjuscampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString()));
								ps.setInt(7, institucion);
								ps.setDouble(8, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_param")+""));
								ps.executeUpdate();
							}
						}else{
							String cadena="INSERT INTO justificacion_serv_param " +
									"(" +
									"codigo, " +
									"justificacion_serv_sol, " +
									"valor, " +
									"etiqueta_seccion," +
									"campo," +
									"etiqueta_campo," +
									"parametrizacion_jus," +
									"institucion" +
									") " +
								" VALUES" +
									"(	" +
									" "+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_param")+","+
									" "+codJustServSol+"," +
									" '"+justificacion.get(servicio+"_valorcampo_"+codigoSeccion+"_"+j)+"'," +
									" '"+justificacion.get(servicio+"_etiquetaseccion_"+codigoSeccion+"_"+j)+"'," +
									" "+justificacion.get(servicio+"_codigocampo_"+codigoSeccion+"_"+j).toString()+"," +
									" '"+justificacion.get(servicio+"_etiquetacampo_"+codigoSeccion+"_"+j)+"'," +
									" "+justificacion.get(servicio+"_codigoparametrizacion_"+codigoSeccion+"_"+j)+"," +
									" "+institucion+" " +
									" ) " +
									"" ;
							logger.info("sentencia insercion datos parametrizables >>>>>> \n "+cadena);
							PreparedStatementDecorator ingresojusparam5= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ingresojusparam5.executeUpdate();
						}
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa justificacion
	 * @param solicitud
	 * @return
	 */
	public static String ingresarJustificacion(Connection con, int institucion, String usuarioModifica, HashMap justificacion, int solicitud, int ordenAmbulatoria, int servicio, int profesional_resp)
	{
		// obtener consecutivo para la solicitud de justificacion de servicio
		String consecutivoJust = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, institucion);
		
		boolean transaccionExitosa=true;
		UtilidadBD.iniciarTransaccion(con);
		PreparedStatementDecorator ps;
		String sqlAux="";
		
		try
		{
			//Insercion datos justificación en la tabla justificacion_serv_sol (SOLICITUD)
			if(solicitud!=ConstantesBD.codigoNuncaValido)
				sqlAux = insertarJustServSol;
			else
				sqlAux = insertarJustServSolOrdenAmbulatoria;
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(sqlAux,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, consecutivoJust.toString().replace("'", " "));
			if(solicitud!=ConstantesBD.codigoNuncaValido)
				ps.setInt(2, solicitud);
			else
				ps.setInt(2, ordenAmbulatoria);
			ps.setInt(3, Utilidades.convertirAEntero(justificacion.get(servicio+"_servicio").toString()));
			ps.setString(4, usuarioModifica.toString().replace("'", " "));
			ps.setDouble(5, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_sol")+""));
			ps.setInt(6, institucion);
			
			
			logger.info("Param 1 - "+consecutivoJust);
			if(solicitud!=ConstantesBD.codigoNuncaValido)
				logger.info("Param 2 - "+solicitud);
			else
				logger.info("Param 2 - "+ordenAmbulatoria);
			logger.info("Param 3 - "+Utilidades.convertirAEntero(justificacion.get(servicio+"_servicio").toString()));
			logger.info("Param 4 - "+usuarioModifica);
			logger.info("Param 5 - "+UtilidadBD.obtenerUltimoValorSecuencia(con, "seq_justificacion_serv_sol"));
			logger.info("Param 6 - "+institucion);
			logger.info("Consulta - "+insertarJustServSol);
			
			
			if(ps.executeUpdate()>0){
				//se da por utilizado el consecutivo
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, institucion, consecutivoJust, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			} 
			else
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, institucion, consecutivoJust, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				transaccionExitosa=false;
			}
			
			//Consultamos el codigo de la solicitud de justificacion
			int codJustServSol = ConstantesBD.codigoNuncaValido;
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCodJustServSol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, consecutivoJust.toString().replace("'", " "));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				codJustServSol = rs.getInt("codigo");
			
			//Inserción de datos en la tabla justificacion_serv_resp (RESPONSABLE)
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServResp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO justificacion_serv_resp(justificacion_serv_sol, sub_cuenta, estado, cantidad, codigo) VALUES (?,?,?,?,?)
			 */
			
			ps.setLong(1, Utilidades.convertirALong(codJustServSol+""));
			ps.setInt(2, Utilidades.convertirAEntero(justificacion.get(servicio+"_subcuenta").toString()));
			ps.setString(3, "JUSTI");
			ps.setDouble(4, Utilidades.convertirADouble(justificacion.get(servicio+"_cantidad").toString()));
			ps.setLong(5, Utilidades.convertirALong(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_resp")+""));
			if(ps.executeUpdate()<=0)
				transaccionExitosa=false;

			//Insercion de datos en la tabla justificacion_serv_dx (DIAGNOSTICOS)
			String[] vector;
			// dx principal
			if (!justificacion.get(servicio+"_diagnosticoPrincipal").equals("")){
				vector = justificacion.get(servicio+"_diagnosticoPrincipal").toString().split(ConstantesBD.separadorSplit);
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codJustServSol);
				ps.setString(2, vector[0].toString().replace("'", " "));
				ps.setInt(3, Utilidades.convertirAEntero(vector[1]));
				ps.setString(4, "PRIN");
				ps.setInt(5, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_dx"));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
			}
			// dx de complicacion
			if (justificacion.containsKey(servicio+"_diagnosticoComplicacion")){
				vector = justificacion.get(servicio+"_diagnosticoComplicacion").toString().split(ConstantesBD.separadorSplit);
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO justificacion_serv_dx(justificacion_serv_sol, acronimo_dx, tipo_cie, tipo_dx, codigo) VALUES (?,?,?,?,?)
				 */
				
				ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
				ps.setString(2, vector[0].toString().replace("'", " "));
				ps.setInt(3, Utilidades.convertirAEntero(vector[1]));
				ps.setString(4, "COMP");
				ps.setDouble(5, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_dx")+""));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
			}
			// dx relacionados
			for (int h=0; h<Utilidades.convertirAEntero(justificacion.get(servicio+"_numDiagRela").toString()); h++){
				vector = justificacion.get(servicio+"_diagnosticoRelacionado_"+h).toString().split(ConstantesBD.separadorSplit);
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServDx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO justificacion_serv_dx(justificacion_serv_sol, acronimo_dx, tipo_cie, tipo_dx, codigo) VALUES (?,?,?,?,?)
				 */
				
				ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
				ps.setString(2, vector[0].toString().replace("'", " "));
				ps.setInt(3, Utilidades.convertirAEntero(vector[1]));
				ps.setString(4, "RELA");
				ps.setDouble(5, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_dx")+""));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
			}
			
			//Insercion de datos en la tabla justificacion_serv_fijo (CAMPOS FIJOS)
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServFijo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO justificacion_serv_fijo(justificacion_serv_sol, observacion, profesional_responsable, codigo) VALUES (?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
			ps.setString(2, justificacion.get(servicio+"_descjustificacion").toString().replace("'", " "));
			ps.setInt(3, profesional_resp);
			ps.setDouble(4, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_fijo")+""));
			if(ps.executeUpdate()<=0)
				transaccionExitosa=false;
			
			
			//Insercion de la tabla justificacion_serv_param (CAMPOS PARAMETRIZABLES)
			/*for (int h=0; h<Utilidades.convertirAEntero(justificacion.get(servicio+"_numhijos_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo).toString()); h++){
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServParam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO justificacion_serv_param(
				 * justificacion_serv_sol, 
				 * valor, 
				 * etiqueta_seccion, 
				 * campo, 
				 * etiqueta_campo, 
				 * parametrizacion_jus, 
				 * institucion, 
				 * codigo) VALUES (?,?,?,?,?,?,?,?)
				 */
				
				/*ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
				ps.setString(2, justificacion.get(servicio+"_valorcampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString());
				ps.setString(3, justificacion.get(servicio+"_etiguetaseccion_"+ConstantesBD.JusSeccionPaciente).toString());
				ps.setDouble(4, Utilidades.convertirADouble(justificacion.get(servicio+"_codigocampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString()));
				ps.setString(5, justificacion.get(servicio+"_etiquetacampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString());
				ps.setDouble(6, Utilidades.convertirADouble(justificacion.get(servicio+"_codigoparamjuscampohijo_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString()));
				ps.setInt(7, institucion);
				ps.setDouble(8, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_param")+""));
				if(ps.executeUpdate()<=0)
					transaccionExitosa=false;
			}*/
			HashMap seccionesMap=(HashMap) justificacion.get(servicio+"_mapasecciones");
			int numRegistrosSecciones=Integer.parseInt(seccionesMap.get("numRegistros").toString());
			for(int i=0;i<numRegistrosSecciones;i++){
				int numRegistrosXSeccion=Integer.parseInt(justificacion.get(servicio+"_numRegistrosXSec_"+seccionesMap.get("codigo_"+i).toString()).toString());;
				String codigoSeccion=seccionesMap.get("codigo_"+i).toString();
				for(int j=0;j<numRegistrosXSeccion;j++){
					if(!justificacion.get(servicio+"_tipo_"+codigoSeccion+"_"+j).toString().equals("LABE")
							&&UtilidadTexto.getBoolean(justificacion.get(servicio+"_mostrar_"+codigoSeccion+"_"+j))){
						if(justificacion.get(servicio+"_tipo_"+codigoSeccion+"_"+j).toString().equals("CHEC")){
							for (int h=0; h<Utilidades.convertirAEntero(justificacion.get(servicio+"_numhijos_"+seccionesMap.get("codigo_"+i).toString()+"_"+j).toString()); h++){
								ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJustServParam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								
								/**
								 * INSERT INTO justificacion_serv_param(
								 * justificacion_serv_sol, 
								 * valor, 
								 * etiqueta_seccion, 
								 * campo, 
								 * etiqueta_campo, 
								 * parametrizacion_jus, 
								 * institucion, 
								 * codigo) VALUES (?,?,?,?,?,?,?,?)
								 */
								
								ps.setDouble(1, Utilidades.convertirADouble(codJustServSol+""));
								ps.setString(2, 
									justificacion.get(servicio+"_valorcampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h)!=null?
											justificacion.get(servicio+"_valorcampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString().replace("'", " "):null);
								ps.setString(3, justificacion.get(servicio+"_etiguetaseccion_"+seccionesMap.get("codigo_"+i).toString()).toString().replace("'", " "));
								ps.setDouble(4, Utilidades.convertirADouble(justificacion.get(servicio+"_codigocampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString()));
								ps.setString(5, justificacion.get(servicio+"_etiquetacampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString().replace("'", " "));
								ps.setDouble(6, Utilidades.convertirADouble(justificacion.get(servicio+"_codigoparamjuscampohijo_"+seccionesMap.get("codigo_"+i).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h).toString()));
								ps.setInt(7, institucion);
								ps.setDouble(8, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_param")+""));
								ps.executeUpdate();
							}
						}else{
							String cadena="INSERT INTO justificacion_serv_param " +
									"(" +
									"codigo, " +
									"justificacion_serv_sol, " +
									"valor, " +
									"etiqueta_seccion," +
									"campo," +
									"etiqueta_campo," +
									"parametrizacion_jus," +
									"institucion" +
									") " +
								" VALUES" +
									"(	" +
									" "+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_serv_param")+","+
									" "+codJustServSol+"," +
									" "+(justificacion.get(servicio+"_valorcampo_"+codigoSeccion+"_"+j)!=null?
											("'"+justificacion.get(servicio+"_valorcampo_"+codigoSeccion+"_"+j).toString().replace("'", " ")+"'"):null)+"," +
									" '"+justificacion.get(servicio+"_etiquetaseccion_"+codigoSeccion+"_"+j).toString().replace("'", " ")+"'," +
									" "+justificacion.get(servicio+"_codigocampo_"+codigoSeccion+"_"+j).toString().toString().replace("'", " ")+"," +
									" '"+justificacion.get(servicio+"_etiquetacampo_"+codigoSeccion+"_"+j).toString().replace("'", " ")+"'," +
									" "+justificacion.get(servicio+"_codigoparametrizacion_"+codigoSeccion+"_"+j).toString().replace("'", " ")+"," +
									" "+institucion+" " +
									" ) " +
									"" ;
							logger.info("sentencia insercion datos parametrizables >>>>>> \n "+cadena);
							PreparedStatementDecorator ingresojusparam5= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ingresojusparam5.executeUpdate();
						}
					}
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if(transaccionExitosa){
			UtilidadBD.finalizarTransaccion(con);
			return consecutivoJust;
		}
		else{
			UtilidadBD.abortarTransaccion(con);
			return "";
		}
	}
	
	
	public static HashMap consultar(Connection con, FormatoJustServNopos fjsn, PersonaBasica paciente, UsuarioBasico usuario, boolean nueva)
	{
		HashMap formulario=new HashMap();
		formulario.put("numRegistros", 0);
		
		HashMap seccionesMap=cargarSeccionesFormularioParametrizado(con);
		formulario.put("mapasecciones",seccionesMap);
		//logger.info("seccionesMap---");
		//Utilidades.imprimirMapa(seccionesMap);
		
		HashMap mapaSeccionX=new HashMap();
		HashMap camposHijos=new HashMap();
		
		//Cargando datos parametrizables del formulario
		for(int i=0;i<Utilidades.convertirAEntero(seccionesMap.get("numRegistros").toString()); i++)
		{
			if (nueva==true)
				mapaSeccionX=cargarCamposSeccionX(con, seccionesMap.get("codigo_"+i).toString(), ConstantesBD.codigoNuncaValido, false);
			else{
				if(fjsn.isProvieneOrdenAmbulatoria())
					mapaSeccionX=cargarCamposSeccionX(con, seccionesMap.get("codigo_"+i).toString(), fjsn.getCodigoOrden(), true);
			else
					mapaSeccionX=cargarCamposSeccionX(con, seccionesMap.get("codigo_"+i).toString(), Utilidades.convertirAEntero(fjsn.getSolicitud().toString()), false);
			}
			
			String codSeccion= seccionesMap.get("codigo_"+i).toString();
			
			for(int j=0;j<Utilidades.convertirAEntero(mapaSeccionX.get("numRegistros").toString()); j++){
				
				formulario.put("requerido_"+codSeccion+"_"+j, mapaSeccionX.get("requerido_"+j));
				formulario.put("tipo_"+codSeccion+"_"+j, mapaSeccionX.get("tipo_"+j));
				formulario.put("mostrar_"+codSeccion+"_"+j, mapaSeccionX.get("mostrar_"+j));
				formulario.put("etiquetacampo_"+codSeccion+"_"+j, mapaSeccionX.get("etiquetacampo_"+j).toString().replaceAll("\n", " "));
				formulario.put("codigocampo_"+codSeccion+"_"+j, mapaSeccionX.get("campo_"+j));
				formulario.put("codigoparametrizacion_"+codSeccion+j,mapaSeccionX.get("codigoparametrizacion_"+j));
				formulario.put("tamanio_"+codSeccion+"_"+j,mapaSeccionX.get("tamanio_"+j).toString());
				
				formulario.put("tieneaccion_"+ codSeccion.toString() + "_"+ j,
						mapaSeccionX.get("tieneaccion_"+j));
				
				if (!mapaSeccionX.get("valor_"+j).toString().equals("")){
					//formulario.put(mapaSeccionX.get("nombre_"+j), mapaSeccionX.get("valor_"+j));
					formulario.put("valorcampo_"+codSeccion+"_"+j, mapaSeccionX.get("valor_"+j));
				}
				if (mapaSeccionX.get("tipo_"+j).toString().equals("CHEC")||mapaSeccionX.get("tipo_"+j).toString().equals("RADI")){
					
					if (nueva==true)
						camposHijos = cargarCamposHijosChec(con, Utilidades.convertirAEntero(mapaSeccionX.get("campo_"+j).toString()), ConstantesBD.codigoNuncaValido, false);
					else
						camposHijos = cargarCamposHijosChec(con, Utilidades.convertirAEntero(mapaSeccionX.get("campo_"+j).toString()), fjsn.isProvieneOrdenAmbulatoria() ? fjsn.getCodigoOrden() : Utilidades.convertirAEntero(fjsn.getSolicitud().toString()), true);
					
					for(int g=0;g<Utilidades.convertirAEntero(camposHijos.get("numRegistros").toString()); g++){
						formulario.put("etiquetacampohijo_"+codSeccion+"_"+j+"_"+g, camposHijos.get("etiquetacampo_"+g).toString().replaceAll("\n", " "));
						formulario.put("codigocampohijo_"+codSeccion+"_"+j+"_"+g, camposHijos.get("codigocampo_"+g));
						formulario.put("codigoparamjuscampohijo_"+codSeccion+"_"+j+"_"+g, camposHijos.get("codigoparamjuscampo_"+g));
						formulario.put("campopadre_"+codSeccion+"_"+j+"_"+g, camposHijos.get("campopadre_"+g));
						if (nueva==true){
							if(mapaSeccionX.get("tipo_"+j).toString().equals("CHEC")){
								formulario.put("valorcampohijo_"+codSeccion+"_"+j+"_"+g, "false");
							}else{
								formulario.put("valorcampohijo_"+codSeccion+"_"+j+"_"+g, "");
							}
						}
						else
							formulario.put("valorcampohijo_"+codSeccion+"_"+j+"_"+g, camposHijos.get("valor_"+g));
					}	
					formulario.put("numHijos_"+codSeccion+"_"+j, camposHijos.get("numRegistros"));
				}
			}
			formulario.put("etiquetaseccion_"+codSeccion,  seccionesMap.get("etiqueta_"+i).toString().replaceAll("\n", " "));
			formulario.put("seccion_"+codSeccion, codSeccion);
			formulario.put("numRegistrosXSec_"+codSeccion, mapaSeccionX.get("numRegistros"));
		}
		
		if (nueva==true){
			// Consultar datos fijos del formulario
			formulario = CargarDatosPaciente(paciente, formulario);
			formulario = CargarDatosMedico(usuario, formulario);
			formulario = CargarDatosServicio(con, formulario, fjsn.getServicio(), fjsn.getCantServicio());
			
			//UtilidadBD.cambiarUsoFinalizadoConsecutivo(nombreConsecutivo, institucion, valor, usado, finalizado);
			
			//formulario.put("numjustificacion", UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios,usuario.getCodigoInstitucionInt()));
			//formulario.put("numjustificacion", "");
			formulario.put("numjustificacion", UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, usuario.getCodigoInstitucionInt()));
			//ConsecutivoDisponible(ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, usuario.getCodigoInstitucionInt()));
		}

		else
		{
			formulario = consultarDatosFijosPaciente(con, fjsn.isProvieneOrdenAmbulatoria() ? String.valueOf(fjsn.getCodigoOrden()) : fjsn.getSolicitud(), formulario, fjsn.isProvieneOrdenAmbulatoria());
			formulario = consultarDatosFijosServicio(con, fjsn.isProvieneOrdenAmbulatoria() ? String.valueOf(fjsn.getCodigoOrden()) : fjsn.getSolicitud(), formulario, fjsn.isProvieneOrdenAmbulatoria());
			formulario.put("numjustificacion", consultarNumJustificacion(con, fjsn.isProvieneOrdenAmbulatoria() ? fjsn.getCodigoOrden() : Utilidades.convertirAEntero(fjsn.getSolicitud().toString()), Utilidades.convertirAEntero(fjsn.getServicio().toString()),fjsn.isProvieneOrdenAmbulatoria()));
			formulario = consultarDatosFijosMedico(con, fjsn.isProvieneOrdenAmbulatoria() ? fjsn.getCodigoOrden() : Utilidades.convertirAEntero(fjsn.getSolicitud()), formulario, fjsn.isProvieneOrdenAmbulatoria());
			formulario = cargarDatosFijos(con, formulario, fjsn.getSubcuenta());
		}

		return formulario;
	}


	private static int consultarNumJustificacion(Connection con, int codigoSolicitudOrden, int servicio, boolean esOrdenAmbulatoria) {
		int num = ConstantesBD.codigoNuncaValido;
		
		String consultaNumJust = "";
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null; 
		
		try
		{
			consultaNumJust += cadenaConsultaNumJust;
			consultaNumJust += esOrdenAmbulatoria ? " AND orden_ambulatoria = ?" : " AND solicitud = ?";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaNumJust,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, servicio);
			ps.setInt(2, codigoSolicitudOrden);
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
				num = rs.getInt("num");
		
		}catch (SQLException e){
            logger.error("ERROR SQLException consultarNumJustificacion: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarNumJustificacion: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
		}
				if(rs != null){
					rs.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return num;
	}


	private static HashMap consultarDatosFijosServicio(Connection con, String codigoSolicitudOrden, HashMap mapa, boolean esOrdenAmbulatoria) {
		HashMap mapaAux = new HashMap();
		
		String consultaDatosServicio = "";
		
		PreparedStatementDecorator ps = null;
		
		try
		{
			consultaDatosServicio = cadenaConsultaDatosServicio;
			consultaDatosServicio += esOrdenAmbulatoria ? " WHERE orden_ambulatoria = ?" : " WHERE solicitud = ?"; 
					
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDatosServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ConstantesBD.codigoTarifarioCups);
			ps.setInt(2, Utilidades.convertirAEntero(codigoSolicitudOrden));
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			mapa.put("nomservicio", mapaAux.get("nomservicio_0"));
			mapa.put("cantservicio",mapaAux.get("cantidad_0"));
			
		}catch (SQLException e){
            logger.error("ERROR SQLException consultarDatosFijosServicio: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarDatosFijosServicio: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
		}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		
		return mapa;
	}


	public static HashMap consultarDiagnosticos(Connection con, int numJustificacion){
		HashMap mapa = new HashMap();
		HashMap mapaAux = new HashMap();
		mapaAux.put("numRegistros", 0);
		String dxComplicacion="";
		HashMap dxDefinitivos = new HashMap();
		int countRelacionados=0;
		String seleccionados="";
		int i=0;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDiagnosticos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numJustificacion+"");
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(i=0; i< Utilidades.convertirAEntero(mapaAux.get("numRegistros").toString());i++)
			{
				if (mapaAux.get("tipo_dx_"+i).toString().equals("RELA")){
					dxDefinitivos.put("relacionado_"+countRelacionados, mapaAux.get("acronimo_"+i)+ConstantesBD.separadorSplit+mapaAux.get("tipo_cie_"+i)+ConstantesBD.separadorSplit+mapaAux.get("nombre_"+i));
					dxDefinitivos.put("checkbox_"+countRelacionados, "true");
					seleccionados = seleccionados + "'" + mapaAux.get("acronimo_"+i) + "',";
					countRelacionados++;
				}
				else if (mapaAux.get("tipo_dx_"+i).toString().equals("COMP"))
					dxComplicacion = mapaAux.get("acronimo_"+i)+ConstantesBD.separadorSplit+mapaAux.get("tipo_cie_"+i)+ConstantesBD.separadorSplit+mapaAux.get("nombre_"+i);
					else if (mapaAux.get("tipo_dx_"+i).toString().equals("PRIN"))
						dxDefinitivos.put("principal", mapaAux.get("acronimo_"+i)+ConstantesBD.separadorSplit+mapaAux.get("tipo_cie_"+i)+ConstantesBD.separadorSplit+mapaAux.get("nombre_"+i));
			}
			seleccionados = seleccionados+"'-1'";
			
			dxDefinitivos.put("seleccionados", seleccionados);
			mapa.put("diagnosticoComplicacion", dxComplicacion);
			mapa.put("diagnosticosDefinitivos", dxDefinitivos);
			mapa.put("numDiagnosticosDefinitivos",countRelacionados);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}


	private static HashMap cargarDatosFijos(Connection con, HashMap mapa, String subcuenta) {
		HashMap mapaAux = new HashMap();
		try
		{
			logger.info("******************************************* "+Utilidades.convertirAEntero(mapa.get("numjustificacion").toString()));
			logger.info("******************************************* "+subcuenta);
		
			String cadena = cadenaConsultaDatosFijos;
			
			if (subcuenta.equals(""))
				cadena += ") WHERE jss.consecutivo='"+mapa.get("numjustificacion").toString()+"' ";
			else
				cadena += ") WHERE jss.consecutivo='"+mapa.get("numjustificacion").toString()+"' AND jsr.sub_cuenta='"+subcuenta+"' ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("Param1 - "+Utilidades.convertirAEntero(mapa.get("numjustificacion").toString()));
			logger.info("Param2 - "+subcuenta);
			logger.info("---------->> "+cadena);
			
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			mapa.put("descjustificacion", mapaAux.get("observacion_0"));
			mapa.put("estado", mapaAux.get("estado_0"));
			//mapa.put("numjustificacion", mapaAux.get("codigo_0"));
			mapa.put("cantidad", mapaAux.get("cantidad_0"));
			mapa.put("convenio", mapaAux.get("sub_cuenta_0"));
			mapa.put("acronimo_estado", mapaAux.get("acronimo_estado_0"));
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}


	private static HashMap consultarDatosFijosMedico(Connection con, int numJustificacion, HashMap mapa, boolean esOrdenAmbulatoria) {
		HashMap mapaAux = new HashMap();
		
		String consultaDatosMedicos = "";
		
		PreparedStatementDecorator ps = null;
		
		try
		{
			consultaDatosMedicos += cadenaConsultaDatosMedico;
			consultaDatosMedicos += esOrdenAmbulatoria ? " WHERE jss.orden_ambulatoria = ?" : " WHERE jss.solicitud = ?"; 
			
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDatosMedicos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numJustificacion);
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			logger.info("Param1 -> "+numJustificacion);
			logger.info("Consulta Medico -> "+cadenaConsultaDatosMedico);
			
			mapa.put("nombremedico", mapaAux.get("nombremedico_0"));
			mapa.put("especialidad",mapaAux.get("especialidad_0"));
			mapa.put("documentomedico",mapaAux.get("documentomedico_0"));
			mapa.put("registromedico", mapaAux.get("registromedico_0"));
			mapa.put("firmadigital", mapaAux.get("firma_digital_0"));
			mapa.put("esOrdenAmbulatoria", mapaAux.get("es_orden_ambulatoria_0"));
			
		}catch (SQLException e){
            logger.error("ERROR SQLException consultarDatosFijosMedico: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarDatosFijosMedico: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
		}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @param mapa
	 * @return
	 */
	private static HashMap consultarDatosFijosPaciente(Connection con, String codigoSolicitudOrden, HashMap mapa, boolean esOrdenAmbulatoria) {
		HashMap mapaAux = new HashMap();
		
		PreparedStatementDecorator ps = null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(esOrdenAmbulatoria ? cadenaConsultaDatosPacienteXOrden : cadenaConsultaDatosPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoSolicitudOrden));
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			System.out.println(cadenaConsultaDatosPaciente +"--"+codigoSolicitudOrden);
			
			mapa.put("nombrepaciente", mapaAux.get("nombrepaciente_0"));
			mapa.put("documentopaciente",mapaAux.get("documentopaciente_0"));
			mapa.put("ingreso",mapaAux.get("ingreso_0"));
			mapa.put("ingconsecutivo",mapaAux.get("ingconsecutivo_0"));
			mapa.put("fecha", mapaAux.get("fecha_0"));
			mapa.put("servsolicitadoen", mapaAux.get("servsolicitadoen_0"));
			logger.info(mapaAux.get("nacimiento_0"));
			logger.info(mapaAux.get("fecha_0").toString());
			mapa.put("edad", UtilidadFecha.calcularEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(mapaAux.get("nacimiento_0").toString()), UtilidadFecha.conversionFormatoFechaAAp(mapaAux.get("fecha_0").toString())));
		
		}catch (SQLException e){
            logger.error("ERROR SQLException consultarDatosFijosPaciente: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarDatosFijosPaciente: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
		}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		
		return mapa;
	}

	/**
	 * Metodo que carga los campos hijos de los posibles check de un campo 
	 * @param mapaSeccionX
	 * @return
	 */
		private static HashMap cargarCamposHijosChec(Connection con, int campo, int codigoSolicitudOrden, boolean isOrdenAmbulatoria) {
			
			HashMap mapa=new HashMap();
			mapa.put("numRegistros","0");
			
			String consultaParametrizacionCamposHijos = "";
			
			PreparedStatementDecorator ps = null;
			
			try
			{
				consultaParametrizacionCamposHijos += cadenaConsultaParametrizacionCamposHijos1;
				consultaParametrizacionCamposHijos += isOrdenAmbulatoria ? "getValorCampoJusParamXOrden(codigo, ?) as valor, " : "getValorCampoJusParam(codigo, ?) as valor, ";
				consultaParametrizacionCamposHijos += cadenaConsultaParametrizacionCamposHijos2;
				
				ps= new PreparedStatementDecorator(con.prepareStatement(consultaParametrizacionCamposHijos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoSolicitudOrden);
				ps.setInt(2, campo);
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}catch (SQLException e){
	            logger.error("ERROR SQLException cargarCamposHijosChec: ", e);
	            
	        }catch(Exception ex){
				logger.error("ERROR Exception cargarCamposHijosChec: ", ex);
			
	        }finally{
				try{
					if(ps != null){
						ps.close();
			}
				}catch (SQLException se) {
					logger.error("###########  Error close PreparedStatement", se);
				}
			}
			
			return mapa;
		}
		
	/**
	 * Metodo que carga el nombre del servicio No Pos
	 */
		private static HashMap CargarDatosServicio(Connection con, HashMap formulario, String servicio, int cantidad) {
			
			HashMap mapa=new HashMap();
			mapa.put("numRegistros","0");
			
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(servicio));
				ps.setInt(2, ConstantesBD.codigoTarifarioCups);
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				while (rs.next())
					formulario.put("nomservicio", rs.getObject("descripcion"));
				
				formulario.put("cantservicio", cantidad);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			return formulario;
		}	
	

	/**
	 * Metodo que carga los datos fijos de la seccion informacion del paciente, tomando los datos en sesion
	 * @return
	 */
	private static HashMap CargarDatosPaciente(PersonaBasica pacienteCargado, HashMap mapa) {
		logger.info("vvvvvvvvvvv");
		logger.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
		logger.info("Id Ingreso del Paciente : " + pacienteCargado.getCodigoIngreso());
		logger.info("Consecutivo Ingreso del Paciente : " + pacienteCargado.getConsecutivoIngreso());
		
		mapa.put("nombrepaciente", pacienteCargado.getApellidosNombresPersona());
		mapa.put("documentopaciente",pacienteCargado.getCodigoTipoIdentificacionPersona()+" "+pacienteCargado.getNumeroIdentificacionPersona());
		//mapa.put("ingreso",pacienteCargado.getConsecutivoIngreso());
		mapa.put("ingreso",pacienteCargado.getCodigoIngreso());
		mapa.put("ingconsecutivo", pacienteCargado.getConsecutivoIngreso());
		mapa.put("fecha", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("edad", UtilidadFecha.calcularEdadDetallada(pacienteCargado.getFechaNacimiento(), UtilidadFecha.getFechaActual()));
		String centroCosto=pacienteCargado.getArea();
		
		if(centroCosto.contains("-")){
			if(centroCosto!=null && centroCosto.split("-").length>=1 && centroCosto.split("-")[1]!=null){
				if(centroCosto.split("-")[0]!=null){
					centroCosto=centroCosto.split("-")[0];
				}
			}
		}
		
		mapa.put("servsolicitadoen",pacienteCargado.getUltimaViaIngreso()+" "+centroCosto );
		return mapa;
	}
	
	/**
	 * Metodo que carga los datos fijos de la seccion firmas, tomando los datos en sesion
	 * @return
	 */
	private static HashMap CargarDatosMedico(UsuarioBasico usuario, HashMap mapa) {
		mapa.put("nombremedico", usuario.getNombreUsuario());
		mapa.put("especialidad", usuario.getEspecialidadesMedico());
		mapa.put("documentomedico", usuario.getCodigoTipoIdentificacion()+" "+usuario.getNumeroIdentificacion());
		mapa.put("registromedico", usuario.getNumeroRegistroMedico());
		mapa.put("firmadigital", usuario.getFirmaDigital());
		return mapa;
	}


/**
 * Metodo para cargar los campos de cada una de las secciones visibles segun la parametrizacion 
 * @param con
 * @param object
 * @return
 */	
	private static HashMap cargarCamposSeccionX(Connection con, String codigoSeccion, int codigoSolicitudOrden, boolean isProvieneOrdenAmbulatoria) {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String consultaParametrizacionFormulario= "";
		
		PreparedStatementDecorator ps = null;
		
		try
		{
			consultaParametrizacionFormulario += cadenaConsultaParametrizacionFormulario1;
			consultaParametrizacionFormulario += isProvieneOrdenAmbulatoria ? "getValorCampoJusParamXOrden(campo, ?) as valor," : "getValorCampoJusParam(campo, ?) as valor,";
			consultaParametrizacionFormulario += cadenaConsultaParametrizacionFormulario2;
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaParametrizacionFormulario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoSolicitudOrden);
			ps.setInt(2, Utilidades.convertirAEntero(codigoSeccion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}catch (SQLException e){
            logger.error("ERROR SQLException cargarCamposSeccionX: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception cargarCamposSeccionX: ", ex);
			
        }finally{
			try{
				if(ps != null){
					ps.close();
		}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return mapa;
	}

/**
 * 
 * Metodo para cargar las secciones parametrizadas del formulario
 * @param con
 * @return
 */
	private static HashMap cargarSeccionesFormularioParametrizado(Connection con) {
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaParametrizacionSecciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> subCuentas(Connection con, int numeroSolicitud)
	{
		HashMap<String, Object> mapaP = new HashMap<String, Object>();
		PreparedStatementDecorator pst2;
		
		try{
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaStr2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst2.setInt(1, numeroSolicitud);
			
			mapaP = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst2.executeQuery()),true, true);
			
			mapaP.put("INDICES",indicesMap2);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de las subcuentas asociadas a la solicitud " + e);
		}		
		return mapaP;
	}
	
	/**
	 * Metodo para revisar la existencia de juistificaciones y modificarlas dependiendo la Distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public static int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String servicio, String usuario)
	{
		HashMap mapaCD = new HashMap();
		HashMap mapaID = new HashMap();
		HashMap mapaCC = new HashMap();
		PreparedStatementDecorator ps, psb, psi, psc, psp, psc2;
		logger.info("\n\nENTRO 11111");
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(justificacionDistri,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(subCuenta));
			ps.setInt(2, Utilidades.convertirAEntero(numSol));
			ps.setInt(3, Utilidades.convertirAEntero(servicio));
			
			mapaCD = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			mapaCD.put("INDICES",indicesMapDistri);
			
			if(Utilidades.convertirAEntero(mapaCD.get("numRegistros").toString())>0)
			{
				for(int i=0; i<Utilidades.convertirAEntero(mapaCD.get("numRegistros").toString()); i++)
				{
					try
					{
						psb= new PreparedStatementDecorator(con.prepareStatement(borrarJustificacionDisti,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						psb.setDouble(1, Utilidades.convertirADouble(mapaCD.get("codigo_"+i)+""));
						psb.executeUpdate();
					}
					catch (SQLException e)
					{
						logger.info("ERROR. Eliminando Justificaciones para distribucion de la cuenta>>>>>>>>>"+e);
					}
				}
				try
				{
					psc= new PreparedStatementDecorator(con.prepareStatement(consultaJustificacionAnterior,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					psc.setInt(1, Utilidades.convertirAEntero(numSol));
					psc.setInt(2, Utilidades.convertirAEntero(servicio));
					
					mapaID = UtilidadBD.cargarValueObject(new ResultSetDecorator(psc.executeQuery()), true, true);
					
					mapaID.put("INDICES",indicesMapDistri);
					
				}
				catch (SQLException e)
				{
					logger.info("ERROR. Tomando el codigo de jus_serv_sol para distribucion de la cuenta>>>>>>>>>"+e);
				}
				try
				{
					psi= new PreparedStatementDecorator(con.prepareStatement(insertarJustificacionDisti,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * INSERT INTO justificacion_serv_resp (estado, justificacion_serv_sol, subcuenta, cantidad) 
					 */
					
					psi.setString(1, "JUSTI");
					psi.setLong(2, Utilidades.convertirALong(mapaID.get("codigo_0")+""));
					psi.setLong(3, Utilidades.convertirALong(subCuenta));
					psi.setDouble(4, Utilidades.convertirADouble(cantidad));
					if(psi.executeUpdate()>0){
						return 1;
					}
				}
				catch (SQLException e)
				{
					logger.info("ERROR. Insertando nueva justificacion para distribucion de la cuenta>>>>>>>>>"+e);
				}
			}
			else
			{
				try
				{
					psc2= new PreparedStatementDecorator(con.prepareStatement(consultaJusPStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
					psc2.setInt(1, Utilidades.convertirAEntero(numSol));
					psc2.setInt(2, Utilidades.convertirAEntero(servicio));
					
					mapaCC = UtilidadBD.cargarValueObject(new ResultSetDecorator(psc2.executeQuery()), true, true);
					
					if(Utilidades.convertirAEntero(mapaCC.get("numRegistros").toString()) <= 0)
					{
						try
						{
							psp= new PreparedStatementDecorator(con.prepareStatement(insertarJusPStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							/**
							 * INSERT INTO jus_pendiente_servicios VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
							 */
							
							psp.setInt(1, Utilidades.convertirAEntero(numSol));
							psp.setInt(2, Utilidades.convertirAEntero(servicio));
							psp.setString(3, usuario);
							
							int resultado=psp.executeUpdate();
							if(resultado>0){
								return 2;
							}
						}
						catch (SQLException e)
						{
							logger.info("ERROR. Insertando justificacion pendiente para distribucion de la cuenta>>>>>>>>>"+e);
						}
					}
					else
					{
						logger.info("\n\n\n\nYA EXISTE UNA JUSTIFICACION PENDIENTE CON NUMERO SOL >>>>"+numSol+">>>>Y SERVICIO >>>>>>"+servicio);
					}
				}
				catch (SQLException e)
				{
					logger.info("ERROR. Consultando justificacion pendiente para distribucion de la cuenta>>>>>>>>>"+e);
				}
			}
		}
		catch (SQLException e)
		{
			logger.info("ERROR. Consulta existencia de justificacion para distribucion de la cuenta>>>>>>>>>"+e);
		}			
		return 0;
	}
	
	/**
	 * Metodo para validar Convenio y Articulo NoPos
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public static boolean validarArtConvJustificacion(Connection con, String servicio, String subCuenta)
	{
		HashMap<String, Object> resultadosNP = new HashMap<String, Object>();
		PreparedStatementDecorator ps, ps2;
		ResultSetDecorator rs;
		String natu="";
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaNoPosArtConv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(servicio));
			ps.setLong(2, Utilidades.convertirALong(subCuenta));
		
			resultadosNP = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
			
			resultadosNP.put("INDICES",indicesMapCons);
			
			if(Utilidades.convertirAEntero(resultadosNP.get("numRegistros").toString())>0 && resultadosNP.get("pos_0").equals(false))
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en validacion de servicio NOPOS sin requiere jus dif med");
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public static boolean existeJustificacion(Connection con, String numeroSolicitud, String servicio)
	{
		String consulta = "SELECT codigo FROM justificacion_serv_sol WHERE solicitud=? AND servicio=?";
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(numeroSolicitud));
			ps.setInt(2, Utilidades.convertirAEntero(servicio));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.error(e+" - ERROR en consulta si existe justificación ");
		}
		return false;
	}

	/**
	 * Metodo para consultar una justificacion historica
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public static HashMap<String, Object> consultarJustificacionHistorica(Connection con, String numeroSolicitud, String servicio)
	{
		HashMap justificacionHistorica = new HashMap();
		String consulta = "SELECT " +
								"das.descripcion as valor," +
								"das.numero_solicitud," +
								"asol.nombre as etiqueta, " +
								"getNombreServicio(das.servicio, "+ConstantesBD.codigoTarifarioCups+") AS servicio, " +
								"sol.tipo " +
							"FROM " +
								"desc_atr_sol_int_proc das " +
							"INNER JOIN " +
								"atributos_solicitud asol ON (asol.codigo = das.atributo) " +
							"INNER JOIN " +
								"solicitudes sol ON (sol.numero_solicitud = das.numero_solicitud)" +
							"WHERE " +
								"das.numero_solicitud = "+numeroSolicitud+" " +
								"AND das.servicio = "+servicio;

		logger.info("consultarJustificacionHistorica / "+consulta);

		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			justificacionHistorica=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (Exception e) {
			logger.info("ERROR! consultarJustificacionHistorica / "+e);
		}
		return justificacionHistorica;
	}
}