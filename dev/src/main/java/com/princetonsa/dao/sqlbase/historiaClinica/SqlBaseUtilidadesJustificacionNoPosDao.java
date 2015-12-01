package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.DtoJustificacionNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamOpcionesCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamSeccionesJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoResponsableJustificacionNoPos;
import com.princetonsa.mundo.atencion.Diagnostico;

public class SqlBaseUtilidadesJustificacionNoPosDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseUtilidadesJustificacionNoPosDao.class);
	
	private static String consultaParametrizacionSecciones = "SELECT " +
																"codigo AS codigo, " +
																"columnas AS columnas, " +
																"etiqueta AS etiqueta, " +
																"orden AS orden, " +
																"mostrar AS mostrar, " +
																"mostrar_etiqueta AS mostrar_etiqueta," +
																"seccion_padre AS seccion_padre " +
															"FROM " +
																"secciones_jus " +
															"WHERE " +
																"institucion = ? " +
																"AND tipo_jus = ? " +
															"ORDER BY " +
																"orden ";
	
	private static String  consultaParametrizacionCampos = "SELECT " +
																"pj.codigo AS codigo_parametrizacion, " +
																"pj.requerido AS requerido, " +
																"pj.mostrar AS mostrar, " +
																"pj.campo AS codigo_campo, " +
																"pj.orden AS orden, " +
																"pj.tipo_html AS tipo_html, " +
																"cj.columnas AS columnas, " +
																"cj.tipo AS tipo, " +
																"cj.etiqueta AS etiqueta, " +
																"COALESCE(cj.campo_padre||'', '') AS campo_padre, " +
																"cj.nombre AS nombre, " +
																"cj.valor_por_defecto AS valor_por_defecto, " +
																"cj.tamanio AS tamanio, " +
																"cj.longitud AS longitud, " +
																"cj.alineacion AS alineacion, " +
																"cj.fijo AS fijo, " +
																"CASE " +
																	//"WHEN ac.CODIGO is not null " +
																	"WHEN (SELECT count (*) from acciones_campos ac where ac.campo_accion = cj.codigo and ac.SERVICIO_CAMPO_ACCION = cj.servicio)>0 "+
																	"THEN '" +ConstantesBD.acronimoSi+"' "+
																	"ELSE '" +ConstantesBD.acronimoNo+"' "+
																"END as tieneaccion "+
															"FROM " +
																"parametrizacion_jus pj " +
															"INNER JOIN " +
																"campos_jus cj ON (cj.codigo = pj.campo) " +
															"WHERE " +
																"pj.institucion = ? " +
																"AND pj.tipo_jus = ? " +
																"AND pj.seccion = ? " +
																"AND pj.mostrar = 'S' " +
															"ORDER BY " +
																"pj.orden";
	
	private static String consultaParametrizacionOpcionesCampo = "SELECT " +
																"codigo AS codigo, " +
																"opcion AS opcion, " +
																"valor AS valor, " +
																"mostrar_seccion AS mostrar_seccion " +
															"FROM " +
																"opciones_jus " +
															"WHERE " +
																"institucion = ? " +
																"AND campo_jus = ? ";
	
	private static String insertarJusArtSol = "INSERT INTO justificacion_art_sol(" +
																"consecutivo, " +
																//"numero_solicitud, " +
																"articulo, " +
																"grupo_terapeutico, " +
																"bibliografia, " +
																"usuario_modifica, " +
																"fecha_modifica, " +
																"hora_modifica, " +
																"fecha, " +
																"codigo, " +
																"institucion," +
																"tipo_jus) " +
															"VALUES " +
																"(?," +
																//"?," +
																"?,inventarios.getGrupoArticulo(?),?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",CURRENT_DATE,?,?,?)";
	
	private static String actualizarJusArtSol = "UPDATE justificacion_art_sol SET " +
																"bibliografia=?, " +
																"usuario_modifica=?, " +
																"fecha_modifica=CURRENT_DATE, " +
																"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" "+
															"WHERE " +
																"codigo=? ";
	
	private static String insertarJusArtSolOrdenAmbulatoria = "INSERT INTO justificacion_art_sol(" +
																"consecutivo, " +
																//"orden_ambulatoria, " +
																"articulo, " +
																"grupo_terapeutico, " +
																"bibliografia, " +
																"usuario_modifica, " +
																"fecha_modifica, " +
																"hora_modifica, " +
																"fecha, " +
																"codigo, " +
																"institucion," +
																"tipo_jus) " +
															"VALUES " +
																"(?," +
																//"?," +
																"?,inventarios.getGrupoArticulo(?),?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",CURRENT_DATE,?,?,?)";
	
	private static String insertarJusArtDx = "INSERT INTO justificacion_art_dx(" +
																"justificacion_art_sol, " +
																"acronimo_dx, " +
																"tipo_cie, " +
																"tipo_dx, " +
																"codigo) " +
															"VALUES " +
																"(?,?,?,?,?)";
	
	private static String eliminarJusArtDx = "DELETE FROM justificacion_art_dx WHERE justificacion_art_sol=? ";
	
	private static String insertarJusArtFijo = "INSERT INTO justificacion_art_fijo(" +
																"codigo," +
																"justificacion_art_sol, " +
																"observacion, " +
																"medicamento_sustituto, " +
																"profesional_responsable," +
																"cantidad_sustituto) " +
															"VALUES " +
																"(?,?,?,?,?,?)";
	
	private static String actualizarJusArtFijo = "UPDATE justificacion_art_fijo SET " +
																"observacion=?,medicamento_sustituto=?,cantidad_sustituto=? " +
															"WHERE " +
																"justificacion_art_sol=? ";
	
	//private static String consultarCodJusArtSol = "SELECT codigo FROM justificacion_art_sol WHERE consecutivo=?";
	
	private static String insertarJusArtParam = "INSERT INTO justificacion_art_param(" +
																"codigo, " +
																"justificacion_art_sol," +
																"valor," +
																"campo," +
																"etiqueta_campo," +
																"parametrizacion_jus," +
																"institucion," +
																"opcion) " +
															"VALUES " +
																"(?,?,?,?,?,?,?,?)";
	
	private static String insertarAsoSolJusArt = "INSERT INTO INVENTARIOS.SOL_X_JUST_ART_SOL(" +
			                                                    "numero_solicitud, " +
			                                                    "codigo_justificacion) " +
	                                                        "VALUES " +
			                                                    "(?,?)";
	
	private static String insertarAsoOrdJusArt = "INSERT INTO INVENTARIOS.ORD_AMB_JUST_ART_SOL(" +
                                                                "codigo_orden, " +
                                                                "codigo_justificacion) " +
                                                            "VALUES " +
                                                                "(?,?)";
	
	private static String eliminarJusArtParam = "DELETE FROM justificacion_art_param WHERE justificacion_art_sol=? ";
	
	private static String insertarJusArtResp = "INSERT INTO justificacion_art_resp(" +
																"codigo, " +
																"estado," +
																"justificacion_art_sol," +
																"subcuenta," +
																"cantidad) " +
															"VALUES " +
																"(?,?,?,?,?)";
	
	private static String actualizarJusArtResp = "UPDATE justificacion_art_resp SET estado=?, cantidad=? WHERE justificacion_art_sol=? AND subcuenta=?";
	
	private static String existeJustificacionArt = "SELECT codigo FROM justificacion_art_sol WHERE articulo=? AND numero_solicitud=? AND institucion=? AND tipo_jus=?";
	
	private static String existeJustificacionNoPosArt = "SELECT codigo " +
			                                            "FROM INVENTARIOS.justificacion_art_sol jas " +
			                                            "INNER JOIN INVENTARIOS.sol_x_just_art_sol sjas " +
			                                            "ON(jas.codigo=sjas.codigo_justificacion) " +
			                                            "WHERE jas.articulo=? AND sjas.numero_solicitud=? " +
			                                            "AND jas.institucion=?";
	
	
	private static String existeJustificacionArtXOrdenAmbulatoria = "SELECT codigo " +
																	"FROM justificacion_art_sol " +
																	"WHERE articulo = ? " +
																		"AND orden_ambulatoria = ? " +
																		"AND institucion = ? " +
																		"AND tipo_jus = ? ";
	
	private static String consultarJustificacionArt = "SELECT " +
			                                                    "JAS.codigo, " +
			                                                    "JAS.articulo, " +
			                                                    "JAS.frecuencia, " +
			                                                    "JAS.tipo_frecuencia, " +
			                                                    "JAS.concentracion, " +
			                                                    "JAS.tiempo_tratamiento, " +
			                                                    "JAS.grupo_terapeutico, " +
			                                                    "JAS.tiempo_respuesta, " +
			                                                    "JAS.registro_invima, " +
			                                                    "JAS.efecto_deseado, " +
			                                                    "JAS.efectos_secundarios, " +
			                                                    "JAS.bibliografia, " +
			                                                    "JAS.fecha_modifica, " +
			                                                    "JAS.hora_modifica, " +
			                                                    "JAS.consecutivo, " +
			                                                    "JAS.dosis, " +
			                                                    "JAS.unidosis, " +
			                                                    "JAS.forma_farmaceutica," +
			                                                    "JAS.usuario_modifica," +
			                                                    "JAS.dosificacion," +
			                                                    "JAS.documentosadj," +
			                                                    "JAS.fecha," +
			                                                    "JAS.institucion," +
			                                                    "JAS.orden_ambulatoria, " +
			                                                    "getdescarticulosincodigo(articulo) AS nombre_articulo " +
		                                               "FROM " +
			                                                    "INVENTARIOS.justificacion_art_sol JAS "; 
//			                                         
//	
	private static String consultarJustificacionArtDx = "SELECT " +
																"codigo," +
																"acronimo_dx, " +
																"tipo_cie, " +
																"tipo_dx, " +
																"getnombrediagnostico(acronimo_dx, tipo_cie) AS nombre " +
															"FROM " +
																"justificacion_art_dx " +
															"WHERE " +
																"justificacion_art_sol=? ";
															
	private static String consultarJustificacionArtFijo = "SELECT " +
																"codigo," +
																"observacion," +
																"medicamento_sustituto, " +
																"profesional_responsable, " +
																"cantidad_sustituto " +
															"FROM " +
																"justificacion_art_fijo "+
															"WHERE " +
																"justificacion_art_sol=? ";
	
	private static String consultarJustificacionArtParamCampos = "SELECT " +
																"jap.codigo, " +
																"jap.valor, " +
																"jap.seccion, " +
																"jap.etiqueta_seccion, " +
																"jap.campo, " +
																"jap.etiqueta_campo, " +
																"jap.parametrizacion_jus, " +
																"jap.institucion, " +
																"pj.tipo_html "+
															"FROM " +
																"justificacion_art_param jap "+
															"INNER JOIN " +
																"parametrizacion_jus pj ON (pj.codigo = jap.parametrizacion_jus) "+
															"WHERE " +
																"jap.justificacion_art_sol=? " +
																"AND jap.opcion IS NULL";
	
	private static String  consultarJustificacionArtOpcionesCampo = "SELECT " +
																"jap.codigo, " +
																"jap.valor, " +
																"jap.seccion, " +
																"jap.etiqueta_seccion, " +
																"jap.campo, " +
																"jap.etiqueta_campo, " +
																"jap.parametrizacion_jus, " +
																"jap.institucion," +
																"jap.opcion " +
															"FROM " +
																"justificacion_art_param jap "+
															"INNER JOIN " +
																"opciones_jus oj ON (oj.codigo = jap.opcion) "+
															"WHERE " +
																"jap.justificacion_art_sol=? " +
																"AND jap.campo=? " +
																"AND parametrizacion_jus IS NULL ";
	
	private static String consultarJustificacionArtResp = "SELECT " +
																"jap.codigo, " +
																"jap.estado AS acronimo_estado, " +
																"getintegridaddominio(jap.estado) AS estado, " +
																"jap.subcuenta, " +
																"jap.cantidad, " +
																"c.nombre AS convenio," +
																"tr.nombre AS tipo_usuario " +
															"FROM " +
																"justificacion_art_resp jap " +
															"INNER JOIN " +
																"sub_cuentas sc ON (sc.sub_cuenta=jap.subcuenta) " +
															"INNER JOIN " +
																"convenios c ON (c.codigo = sc.convenio) " +
															"INNER JOIN " +
																"tipos_regimen tr ON (tr.acronimo = c.tipo_regimen) "+
															"WHERE " +
																"jap.justificacion_art_sol=? ";
	
	private static String consultarDatosPaciente = "SELECT " +
																" TO_CHAR(s.fecha_solicitud , 'DD/MM/YYYY')  AS fecha, " +
																"getnombrealmacen(c.area) AS servsolicitadoen, " +
																"vi.nombre AS via_ingreso, " +
																"getconsecutivoingreso(c.id_ingreso) as ingreso," +
																"p.tipo_identificacion || ' ' || p.numero_identificacion AS documentopaciente," +
																"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS nombrepaciente," +
																"to_char(p.fecha_nacimiento, 'YYYY-MM-DD') AS nacimiento " +
															"FROM " +
																"solicitudes s " +
															"INNER JOIN " +
																"cuentas c ON c.id=s.cuenta " +
															"INNER JOIN vias_ingreso vi " +
																"ON vi.codigo=c.via_ingreso " +
															"INNER JOIN " +
																"personas p ON c.codigo_paciente=p.codigo " +
															"WHERE " +
																"s.numero_solicitud=? ";
	
	private static String consultarDatosPacienteXOrden = "SELECT " +
			 												"TO_CHAR(oa.fecha, 'DD/MM/YYYY')  AS fecha, " + 
			 												"getnombrealmacen(c.area) AS servsolicitadoen, " + 
			 												"vi.nombre AS via_ingreso, " + 
			 												"getconsecutivoingreso(c.id_ingreso) as ingreso, " +
			 												"p.tipo_identificacion || ' ' || p.numero_identificacion AS documentopaciente, " +
			 												"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS nombrepaciente, " +
			 												"to_char(p.fecha_nacimiento, 'YYYY-MM-DD') AS nacimiento " + 
			 											"FROM ordenes_ambulatorias oa " + 
			 												"INNER JOIN cuentas c " +
			 												"ON (c.id=oa.cuenta_solicitante) " +
			 												"INNER JOIN vias_ingreso vi " + 
			 												"ON (vi.codigo=c.via_ingreso) " + 
			 												"INNER JOIN personas p " +
			 												"ON (c.codigo_paciente=p.codigo) " + 
			 											"WHERE " + 
			 												"oa.codigo = ? ";
	
	private static String consultarDatosMedico = "SELECT " +
																"m.numero_registro as registromedico," +
																"p.tipo_identificacion || ' ' || p.numero_identificacion as documentomedico," +
																"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombremedico, " +
																"getespecialidadesmedico(u.login, ', ') as especialidad, " +
																"m.firma_digital AS firmadigital " +
															"FROM " +
																"medicos m " +
															"INNER JOIN " +
																"personas p ON (p.codigo = m.codigo_medico) " +		
															"INNER JOIN " +
																"usuarios u ON (u.codigo_persona = m.codigo_medico) " +	
															"WHERE " +
																"m.codigo_medico=? ";
	
	/**
	 * Método para cargar la parametrización del formato de Justificación No Pos
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public static DtoParamJusNoPos cargarParametrizacion(Connection con, int institucion, String tipoJustificacion)
	{
		DtoParamJusNoPos dto = new DtoParamJusNoPos();
		HashMap mapSecciones = new HashMap();
		PreparedStatementDecorator ps =  null;
		
		// Consultar secciones Parametrizadas
		try
		{
			/*logger.info("SQL / consultaParametrizacionSecciones / "+consultaParametrizacionSecciones);
			logger.info("institucion = "+institucion);
			logger.info("tipo_jus = "+tipoJustificacion);*/
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaParametrizacionSecciones));
			ps.setInt(1, institucion);
			ps.setString(2, tipoJustificacion);
			mapSecciones = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
		}
		catch (SQLException e)
		{
			logger.error("ERROR en consultaParametrizacionSecciones / "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		
		for (int i=0; i<Utilidades.convertirAEntero(mapSecciones.get("numRegistros")+""); i++){
			
			HashMap mapCampos = new HashMap();
			DtoParamSeccionesJusNoPos dtoSeccion = new DtoParamSeccionesJusNoPos();
			dtoSeccion.setCodigo(mapSecciones.get("codigo_"+i)+"");
			dtoSeccion.setColumnas(mapSecciones.get("columnas_"+i)+"");
			dtoSeccion.setEtiqueta(mapSecciones.get("etiqueta_"+i)+"");
			dtoSeccion.setMostrar(mapSecciones.get("mostrar_"+i)+"");
			dtoSeccion.setOrden(mapSecciones.get("orden_"+i)+"");
			dtoSeccion.setMostrarEtiqueta(mapSecciones.get("mostrar_etiqueta_"+i)+"");
			dtoSeccion.setSeccionPadre(mapSecciones.get("seccion_padre_"+i)+"");
			
			// Consultar Campos Parametrizados para la sección
			try
			{
				/*logger.info("SQL / consultaParametrizacionCampos / "+consultaParametrizacionCampos);
				logger.info("institucion = "+institucion);
				logger.info("tipo_jus = "+tipoJustificacion);
				logger.info("seccion = "+mapSecciones.get("codigo_"+i));*/
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaParametrizacionCampos));
				ps.setInt(1, institucion);
				ps.setString(2, tipoJustificacion);
				ps.setInt(3, Utilidades.convertirAEntero(dtoSeccion.getCodigo()));
				mapCampos = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			}
			catch (SQLException e)
			{
				logger.error("ERROR en consultaParametrizacionCampos / "+e);
			}finally{
				try {
					if(ps!=null){
						ps.close();
					}
					
				} catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
				}
			}
			
			for (int j=0; j<Utilidades.convertirAEntero(mapCampos.get("numRegistros")+""); j++){
				
				DtoParamCamposJusNoPos dtoCampo = new DtoParamCamposJusNoPos();
				dtoCampo.setCodigoParam(mapCampos.get("codigo_parametrizacion_"+j)+"");
				dtoCampo.setRequerido(mapCampos.get("requerido_"+j)+"");
				dtoCampo.setMostrar(mapCampos.get("mostrar_"+j)+"");
				dtoCampo.setCodigo(mapCampos.get("codigo_campo_"+j)+"");
				dtoCampo.setOrden(mapCampos.get("orden_"+j)+"");
				dtoCampo.setColumnas(mapCampos.get("columnas_"+j)+"");
				dtoCampo.setEtiqueta(mapCampos.get("etiqueta_"+j)+"");
				dtoCampo.setValor(mapCampos.get("valor_por_defecto_"+j)+"");
				dtoCampo.setTipo(mapCampos.get("tipo_"+j)+"");
				dtoCampo.setTipoHtml(mapCampos.get("tipo_html_"+j)+"");
				dtoCampo.setNombre(mapCampos.get("nombre_"+j)+"");
				dtoCampo.setTamanio(mapCampos.get("tamanio_"+j)+"");
				dtoCampo.setLongitud(mapCampos.get("longitud_"+j)+"");
				dtoCampo.setAlineacion(mapCampos.get("alineacion_"+j)+"");
				dtoCampo.setFijo(mapCampos.get("fijo_"+j)+"");
				dtoCampo.setTieneAccion(UtilidadTexto.getBoolean(mapCampos.get("tieneaccion_"+j)));
				
				if (dtoCampo.getTipoHtml().equals("RADI") || dtoCampo.getTipoHtml().equals("SELE") || dtoCampo.getTipoHtml().equals("CHEC")){
					HashMap mapOpcionesCampo = new HashMap();
					
					// Consultar opciones parametrizadas para el campo
					try
					{
						/*logger.info("SQL / consultaParametrizacionOpcionesCampo / "+consultaParametrizacionOpcionesCampo);
						logger.info("institucion = "+institucion);
						logger.info("tipo_jus = "+tipoJustificacion);
						logger.info("seccion = "+mapSecciones.get("codigo_"+i));-*/
						ps =  new PreparedStatementDecorator(con.prepareStatement(consultaParametrizacionOpcionesCampo));
						ps.setInt(1, institucion);
						ps.setInt(2, Utilidades.convertirAEntero(dtoCampo.getCodigo()));
						mapOpcionesCampo = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
					}
					catch (SQLException e)
					{
						logger.error("ERROR en consultaParametrizacionOpcionesCampo / "+e);
					}finally{
						try {
							if(ps!=null){
								ps.close();
							}
							
						} catch(SQLException sqlException){
							logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
						}
					}
					
					for (int x=0; x<Utilidades.convertirAEntero(mapOpcionesCampo.get("numRegistros")+""); x++){
						DtoParamOpcionesCamposJusNoPos dtoOpcion = new DtoParamOpcionesCamposJusNoPos();
						
						dtoOpcion.setCodigo(mapOpcionesCampo.get("codigo_"+x)+"");
						dtoOpcion.setMostrarSeccion(mapOpcionesCampo.get("mostrar_seccion_"+x)+"");
						dtoOpcion.setOpcion(mapOpcionesCampo.get("opcion_"+x)+"");
						dtoOpcion.setValor(mapOpcionesCampo.get("valor_"+x)+"");
						
						// Añadimos la opcion al array
						dtoCampo.getOpciones().add(dtoOpcion);
					}
				}
				
				// Añadimos el campo al array
				dtoSeccion.getCampos().add(dtoCampo);
			}
			
			// Añadimos la seccion al array
			dto.getSecciones().add(dtoSeccion);
			
		}

		return dto;
	}
	
	/**
	 * devuelve la informacion de la justificacion de la solicitud
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param codigoSolicitud
	 * @return [consecutivoJustificacion, estadoJustificacion, nombreEstadoJustificacion, esOrdenAmbulatoria]
	 * @throws SQLException 
	 */
	public String[] consultarInformacionJustificacion(Connection con,String codigoArticulo,String codigoSolicitud) throws SQLException,Exception{
		PreparedStatementDecorator ps5 = null;
		try{
			HashMap mapa1 = new HashMap();
			mapa1.put("numRegistros", 0);
			String consulta = " SELECT "
					+ "jas.consecutivo, "
					+ "jar.estado, " 
					+ "CASE " +
						"WHEN jas.ORDEN_AMBULATORIA is null " +
						"THEN '" +ConstantesBD.acronimoNo+"' " +
						"ELSE '" +ConstantesBD.acronimoSi+"' "
					+ "END as es_orden_ambulatoria "
					+ "from "
					+ "justificacion_art_sol jas "
					+ "inner join "
					+ "justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "
					+ "where " + "jas.articulo=" + codigoArticulo + " and "
					+ "jas.numero_solicitud=" + codigoSolicitud;
				ps5 = new PreparedStatementDecorator(con.prepareStatement(consulta));
				mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps5
						.executeQuery()));
			logger.info("[Consulta justificacion_art_sol]" + consulta);
			
			return new String[]{mapa1
					.get("consecutivo_0").toString(),mapa1.get("estado_0").toString(),ValoresPorDefecto
					.getIntegridadDominio(ConstantesIntegridadDominio.acronimoJustificado).toString(),mapa1.get("es_orden_ambulatoria_0").toString()};
		}catch (SQLException e) {
			// TODO: handle exception
			logger.error("ERROR en consultarInformacionJustificacion / "+e);
			 throw e;
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR en consultarInformacionJustificacion / "+e);
			throw e;
		}finally{
			try {
				if(ps5!=null){
					ps5.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método para guardar la justificacion No Pos en la BD 
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @param usuario
	 * @param esModificacion
	 * @return
	 */
	public static boolean guardarJustificacion(Connection con, DtoJustificacionNoPos dtoJus, String tipoJustificacion){
		
		boolean transaccionExitosa = true;
		int codigoJustificacion = ConstantesBD.codigoNuncaValido;
		String sql;
		PreparedStatementDecorator ps = null;
		PreparedStatementDecorator insertarSolicitudUOrdenXJust=null;
		if(tipoJustificacion.equals("INSU")){

			UtilidadBD.iniciarTransaccion(con);
			
			// obtenemos el consecutivo para la justificacion de articulos
			String consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos, Utilidades.convertirAEntero(dtoJus.getInstitucion()));
			logger.info("valor del consecutivo >> "+consecutivo);
			logger.info("solicitud >> "+dtoJus.getSolicitud());
			
			try {
				//****** Insercion datos justificación en la tabla justificacion_art_sol
				if(dtoJus.getSolicitud().equals(""))
					sql = insertarJusArtSolOrdenAmbulatoria;
				else
					sql = insertarJusArtSol;
				
				codigoJustificacion=Utilidades.convertirAEntero(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_sol")+"");
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(sql));
				ps.setString(1, consecutivo);
				/*if(dtoJus.getSolicitud().equals(""))
					ps.setInt(2,  Utilidades.convertirAEntero(dtoJus.getOrdenAmbulatoria()));
				else
					ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getSolicitud()));*/
				ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getArticulo()));
				ps.setInt(3, Utilidades.convertirAEntero(dtoJus.getArticulo()));
				ps.setString(4, dtoJus.getBibliografia().replace("'", ""));
				ps.setString(5, dtoJus.getUsuarioModifica());
				ps.setDouble(6, codigoJustificacion);
				ps.setInt(7, Utilidades.convertirAEntero(dtoJus.getInstitucion()));
				ps.setString(8, ConstantesIntegridadDominio.acronimoInsumo);
				
				if(ps.executeUpdate()>0){
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos, Utilidades.convertirAEntero(dtoJus.getInstitucion()), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				} else {
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos, Utilidades.convertirAEntero(dtoJus.getInstitucion()), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					transaccionExitosa=false;
				}
				
				/***/
				
				StringBuffer insertarSolJust=null;
				String sentenciaJustificacionSolicitudOrden="";
				if(dtoJus.getSolicitud().equals("")){
					insertarSolJust=new StringBuffer("INSERT INTO INVENTARIOS.ORD_AMB_JUST_ART_SOL (CODIGO_ORDEN,CODIGO_JUSTIFICACION) ");
					insertarSolJust.append("values (")
					.append(dtoJus.getOrdenAmbulatoria())
					.append(", ")
					.append(codigoJustificacion)
					.append(") ");
				}else{
					insertarSolJust=new StringBuffer("INSERT INTO INVENTARIOS.SOL_X_JUST_ART_SOL (NUMERO_SOLICITUD,CODIGO_JUSTIFICACION) ");
					insertarSolJust.append("values (")
					.append(dtoJus.getSolicitud())
					.append(", ")
					.append(codigoJustificacion)
					.append(") ");
					
				}
				
				sentenciaJustificacionSolicitudOrden=insertarSolJust.toString();
				insertarSolicitudUOrdenXJust=new PreparedStatementDecorator(con.prepareStatement(sentenciaJustificacionSolicitudOrden,
						ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				
				if(insertarSolicitudUOrdenXJust.executeUpdate()!=1){
					transaccionExitosa=false;
				}
				
				/***/
				
			} catch (SQLException e) {
				logger.error("ERROR / guardarJustificacion / "+e);
				transaccionExitosa=false;
			}finally{
				try{
					if(ps!=null){
						ps.close();
					}
					if(insertarSolicitudUOrdenXJust!=null){
						insertarSolicitudUOrdenXJust.close();
					}
				}catch (SQLException e) {
					Log4JManager.error("ERROR CERRANDO PS - RS " , e);
				}
			}
			
			/*if(transaccionExitosa){
				try {
					//****** Consultamos el codigo de la solicitud de justificacion
					ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCodJusArtSol));
					ps.setString(1, consecutivo);
					
					ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
					if (rs.next())
						codigoJustificacion = rs.getInt("codigo");
					
					logger.info("codJusArtSol - "+codigoJustificacion);
					
					
				} catch (SQLException e) {
					logger.info("ERROR / guardarJustificacion / "+e);
					transaccionExitosa=false;
				}
			}	*/
		
			if(transaccionExitosa){
				try {
					//Insercion de datos en la tabla justificacion_art_dx
					for(int c=0; c<dtoJus.getDiagnosticos().size(); c++){
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtDx));
						ps.setInt(1, codigoJustificacion);
						ps.setString(2, dtoJus.getDiagnosticos().get(c).getAcronimo());
						ps.setInt(3, dtoJus.getDiagnosticos().get(c).getTipoCIE());
						ps.setString(4, dtoJus.getDiagnosticos().get(c).getNombreTipoDiagnostico());
						ps.setInt(5, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_dx"));
						if(ps.executeUpdate()<=0)
							transaccionExitosa=false;
					}
				} catch (SQLException e) {
					logger.info("ERROR / guardarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try{
						if(ps!=null){
							ps.close();
						}
					}catch (SQLException e) {
						Log4JManager.error("ERROR CERRANDO PS - RS " , e);
					}
				}
			}	
			
			if(transaccionExitosa){
				try {
					//Insercion de datos en la tabla justificacion_art_fijo
					ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtFijo));
					ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_fijo"));
					ps.setInt(2, codigoJustificacion);
					ps.setString(3, dtoJus.getJustificacion().replace("'", ""));
					ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getArticuloSustituto()));
					ps.setInt(5, Utilidades.convertirAEntero(dtoJus.getProfesionalResponsable()));
					ps.setInt(6, Utilidades.convertirAEntero(dtoJus.getCantidadSustituto()));
					if(ps.executeUpdate()<=0)
						transaccionExitosa=false;
				} catch (SQLException e) {
					logger.info("ERROR / guardarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try{
						if(ps!=null){
							ps.close();
						}
					}catch (SQLException e) {
						Log4JManager.error("ERROR CERRANDO PS - RS " , e);
					}
				}
			}
			
			if(transaccionExitosa){
				try {
					//Insercion de datos en la tabla justificacion_art_resp
					ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtResp));
					ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_resp"));
					ps.setString(2, dtoJus.getEstado());
					ps.setInt(3, codigoJustificacion);
					ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getSubcuenta()));
					ps.setInt(5, Utilidades.convertirAEntero(dtoJus.getCantidad()));
					if(ps.executeUpdate()<=0)
						transaccionExitosa=false;
				} catch (SQLException e) {
					logger.info("ERROR / guardarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try{
						if(ps!=null){
							ps.close();
						}
					}catch (SQLException e) {
						Log4JManager.error("ERROR CERRANDO PS - RS " , e);
					}
				}
			}
			
			if(transaccionExitosa){
				try {
					//Insercion de datos en la tabla justificacion_serv_param
					for(int c=0; c<dtoJus.getCamposParam().size(); c++){
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtParam));
						ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_param"));
						ps.setInt(2, codigoJustificacion);
						ps.setString(3, dtoJus.getCamposParam().get(c).getValor().replace("'", ""));
						ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getCodigo().replace("'", "")));
						ps.setString(5, dtoJus.getCamposParam().get(c).getEtiqueta().replace("'", ""));
						ps.setInt(6, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getCodigoParam().replace("'", "")));
						ps.setInt(7, Utilidades.convertirAEntero(dtoJus.getInstitucion().replace("'", "")));
						ps.setNull(8, Types.INTEGER);
						
						if(ps.executeUpdate()<=0)
							transaccionExitosa=false;
						
						if(dtoJus.getCamposParam().get(c).getTipoHtml().equals("CHEC")){
							for(int op=0; op<dtoJus.getCamposParam().get(c).getOpciones().size(); op++){
								if(dtoJus.getCamposParam().get(c).getOpciones().get(op).getSeleccionado().equals(ConstantesBD.acronimoSi)){
									ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtParam));
									ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_param"));
									ps.setInt(2, codigoJustificacion);
									ps.setString(3, ConstantesBD.acronimoSi);
									ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getCodigo().replace("'", "")));
									ps.setString(5, dtoJus.getCamposParam().get(c).getOpciones().get(op).getOpcion().replace("'", ""));
									ps.setNull(6, Types.INTEGER);
									ps.setInt(7, Utilidades.convertirAEntero(dtoJus.getInstitucion().replace("'", "")));
									ps.setInt(8, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getOpciones().get(op).getCodigo().replace("'", "")));
									
									if(ps.executeUpdate()<=0)
										transaccionExitosa=false;
								}	
							}
						}
							
					}
				} catch (SQLException e) {
					logger.info("ERROR / guardarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try{
						if(ps!=null){
							ps.close();
						}
					}catch (SQLException e) {
						Log4JManager.error("ERROR CERRANDO PS - RS " , e);
					}
				}
			}
			
		}
		
		if(transaccionExitosa)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return transaccionExitosa;
	}


	/**
	 * Metodo para insertar la asociacion de solicitudes - justificacion No Pos en la BD
	 *  
	 * @param con
	 * @param numerosolicitud
	 * @param codigoJustificacion
	*/
	
	//Solicitud Justificacion guardado
	
	
	public static void insertarAsocioSolicitudJustificacion(Connection con, int numeroSolicitud, int codigoAsocioJustificacion, int cantidadSolicitada)
	{
		String sql="";
		String actualizarCantidadSolicitada="";
		PreparedStatement ps1=null;
		PreparedStatement ps2=null;
		UtilidadBD.iniciarTransaccion(con);

		try{
			sql = insertarAsoSolJusArt;
			ps1 =  con.prepareStatement(sql);
			ps1.setInt(1, numeroSolicitud);
			ps1.setInt(2, codigoAsocioJustificacion);
			ps1.executeUpdate();				
			
			actualizarCantidadSolicitada="UPDATE inventarios.justificacion_art_resp SET cantidad=cantidad+"+cantidadSolicitada+" WHERE JUSTIFICACION_ART_SOL=?";
			
			ps2 =  con.prepareStatement(actualizarCantidadSolicitada);
			
			ps2.setInt(1, codigoAsocioJustificacion);
			
			ps2.executeUpdate();
		}catch (SQLException e) {
			logger.error("ERROR / guardarAsocioJustificacion / "+e);
			
			
			throw new RuntimeException(e);
		}finally{
			if(ps1!=null){
				try {
					ps1.close();
				} catch (SQLException sqlException) {
					// TODO Auto-generated catch block
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
				}
			}
			if(ps2!=null){
				try {
					ps2.close();
				} catch (SQLException sqlException) {
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
				}
			}
		}
	}

	/**
	 * Metodo para insertar la asociacion de Ordenes - justificacion No Pos en la BD
	 *  
	 * @param con
	 * @param cantidadSolicitada 
	 * @param numerosolicitud
	 * @param codigoJustificacion
	*/
	
	//Orden Justificacion guardado
	
	
	public static void insertarAsocioOrdenJustificacion(Connection con, int numeroOrden, int codigoAsocioJustificacion, int cantidadSolicitada)
	{
		String sql="";
		String actualizarCantidadSolicitada="";
		PreparedStatement ps1=null;
		PreparedStatement ps2=null;
		UtilidadBD.iniciarTransaccion(con);

		try{
			sql = insertarAsoOrdJusArt;
			ps1 =  con.prepareStatement(sql);
			ps1.setInt(1, numeroOrden);
			ps1.setInt(2, codigoAsocioJustificacion);
			ps1.executeUpdate();
			
			actualizarCantidadSolicitada="UPDATE inventarios.justificacion_art_resp SET cantidad=cantidad+"+cantidadSolicitada+" WHERE JUSTIFICACION_ART_SOL=?";
			
			ps2 =  con.prepareStatement(actualizarCantidadSolicitada);
			
			ps2.setInt(1, codigoAsocioJustificacion);
			
			ps2.executeUpdate();
		}catch (SQLException e) {
			logger.error("ERROR / guardarAsocioOrdenesJustificacion / "+e);
			
			throw new RuntimeException(e);
		}finally{
			
			if(ps1!=null){
				try {
					ps1.close();
				} catch (SQLException sqlException) {
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					
				}
			}
			if(ps2!=null){
				try {
					ps2.close();
				} catch (SQLException sqlException) {
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
				}
			}
			
		}
	}
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/Orden Ambulatoria
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @param codigo
	 * @param ordenAmbulatoria
	 * @return
	 */
	public static boolean existeJustificacionOrdenAmbulatoria (Connection con, int institucion, String tipoJustificacion, String codigoArticulo, String codigoOrdenAmbulatoria){
		
		boolean existeJustificacionOrdenAmbulatoria = false;
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try {
			if(tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoInsumo)) {
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(existeJustificacionArtXOrdenAmbulatoria));
				ps.setInt(1, Utilidades.convertirAEntero(codigoArticulo));
				ps.setInt(2, Utilidades.convertirAEntero(codigoOrdenAmbulatoria));
				ps.setInt(3, institucion);
				ps.setString(4, tipoJustificacion);
				
				rs =new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
					existeJustificacionOrdenAmbulatoria = true;
			}	
			
		} catch(SQLException e){
			Log4JManager.error("ERROR -- SQLException existeJustificacionOrdenAmbulatoria"+e);
		
		} catch (Exception ex){
			Log4JManager.error("ERROR -- Exception existeJustificacionOrdenAmbulatoria", ex);
			
		}finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return existeJustificacionOrdenAmbulatoria;
	}
	
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/solicitud general
	 * @param con
	 * codigoArticulo
	 * numero solicitud
	 * @param institucion
	 * @return
	 */
	public static boolean existeJustificacion(Connection con, int institucion, String codigo, String solicitud) {
		boolean existeJustificacion = false;
		ResultSetDecorator rs =  null;
		PreparedStatementDecorator ps = null;
		try {
//			if(tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoMedicamento) || tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoInsumo)) {
				
				 ps =  new PreparedStatementDecorator(con.prepareStatement(existeJustificacionNoPosArt));
				ps.setInt(1, Utilidades.convertirAEntero(codigo));
				ps.setInt(2, Utilidades.convertirAEntero(solicitud));
				ps.setInt(3, institucion);
				//ps.setString(4, tipoJustificacion);
				rs =new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
					existeJustificacion = true;
		//	}	
			
		} catch(SQLException e){
			logger.info("ERROR / validar Justificacion/ "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return existeJustificacion;
	}
	
	
	/**
	 * Método que consulta si existe o no una justificación No Pos para un articulo/solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public static boolean existeJustificacion(Connection con, int institucion, String tipoJustificacion, String codigo, String solicitud) {
		boolean existeJustificacion = false;
		ResultSetDecorator rs = null;

		PreparedStatementDecorator ps = null;
		try {
			if(tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoMedicamento) || tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoInsumo)) {
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(existeJustificacionArt));
				ps.setInt(1, Utilidades.convertirAEntero(codigo));
				ps.setInt(2, Utilidades.convertirAEntero(solicitud));
				ps.setInt(3, institucion);
				ps.setString(4, tipoJustificacion);
				rs =new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
					existeJustificacion = true;
			}	
			
		} catch(SQLException e){
			logger.info("ERROR / guardarJustificacion / "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return existeJustificacion;
	}
	
	/**
	 * Metodo que consulta el codigo de una justificacion no pos si existe o no para un articulo/orden-solicitud
	 * @param con
	 * @param institucion
	 * @param tipoJustificacion
	 * @return
	 */
	public static int consultarCodigoJustificacion(Connection con, int institucion, String tipoJustificacion, String codigoArt,String codigoOrden, String numeroSolicitud) {
		int retornoCodJustificacion = ConstantesBD.codigoNuncaValido;
		ResultSetDecorator rs=null;
		PreparedStatementDecorator ps=null;
		try {
			if(tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoMedicamento) || tipoJustificacion.equals(ConstantesIntegridadDominio.acronimoInsumo)) {
				
				StringBuffer consultarJustificacion=new StringBuffer("SELECT JUST.CODIGO AS CODIGO FROM INVENTARIOS.JUSTIFICACION_ART_SOL JUST ");
				if(numeroSolicitud!=null){
					consultarJustificacion.append("INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SOL_X_JUST ")
						.append("ON SOL_X_JUST.CODIGO_JUSTIFICACION = JUST.CODIGO ");
				}else{
					if(codigoOrden!=null){
						consultarJustificacion.append("INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL ORD_X_JUST ")
							.append("ON ORD_X_JUST.CODIGO_JUSTIFICACION = JUST.CODIGO ");
					}
				}
				
				consultarJustificacion.append("WHERE JUST.ARTICULO = ? ");
				
				if(numeroSolicitud!=null){
					consultarJustificacion.append("AND SOL_X_JUST.NUMERO_SOLICITUD = ? ");
				}else{
					if(codigoOrden!=null){
						consultarJustificacion.append("AND ORD_X_JUST.CODIGO_ORDEN = ? ");
					}
				}
				
				consultarJustificacion.append("AND JUST.institucion= ? AND JUST.tipo_jus=? ");
				
				ps=new PreparedStatementDecorator(con.prepareStatement(consultarJustificacion.toString()));
				
				ps.setInt(1, Utilidades.convertirAEntero(codigoArt));
				if(numeroSolicitud!=null){
					ps.setInt(2,  Utilidades.convertirAEntero(numeroSolicitud));
				}else{
					if(codigoOrden!=null){
						ps.setInt(2,  Utilidades.convertirAEntero(codigoOrden));
					}
				}
				ps.setInt(3, institucion);
				ps.setString(4, tipoJustificacion);
				
				
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next()){
					
					retornoCodJustificacion=rs.getInt("CODIGO");
				}
				
			}	
			
		} catch(SQLException e){
			logger.info("ERROR / guardarJustificacion / "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return retornoCodJustificacion;
	}

	/**
	 * Método que consulta una justificación No Pos
	 * @param con
	 * @param tipoJustificacion
	 * @param dtoParam
	 * @return
	 */
	public static DtoJustificacionNoPos consultarJustificacion(Connection con, String tipoJustificacion, DtoParamJusNoPos dtoParam) {
		DtoJustificacionNoPos dtoJus = new DtoJustificacionNoPos();
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try {
			
			String cadenaJustificacionArt = consultarJustificacionArt;

			cadenaJustificacionArt += !dtoParam.getSolicitud().isEmpty() ? "INNER JOIN INVENTARIOS.sol_x_just_art_sol SJAS ON JAS.codigo=SJAS.codigo_justificacion "
					                                                     : "INNER JOIN INVENTARIOS.ord_amb_just_art_sol OAJASON JAS.codigo=OAJAS.codigo_justificacion ";
			cadenaJustificacionArt += "WHERE JAS.articulo=? AND JAS.tipo_jus=? ";
			cadenaJustificacionArt += !dtoParam.getSolicitud().isEmpty() ? " AND SJAS.numero_solicitud = ? " : " AND OAJAS.codigo_orden = ? ";
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaJustificacionArt));
			ps.setInt(1, Utilidades.convertirAEntero(dtoParam.getCodigoArticulo()));
			ps.setString(2, tipoJustificacion);
			ps.setInt(3, !dtoParam.getSolicitud().isEmpty() ? Utilidades.convertirAEntero(dtoParam.getSolicitud()) : Utilidades.convertirAEntero(dtoParam.getOrdenAmbulatoria()));
			
			rs =new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next()){
				dtoJus.setBibliografia(rs.getString("bibliografia"));
				dtoJus.setConsecutivo(rs.getString("consecutivo"));
				dtoJus.setCodigo(rs.getString("codigo"));
				dtoJus.setArticulo(rs.getString("articulo"));
				dtoJus.setNombreArticulo(rs.getString("nombre_articulo"));
				dtoJus.setOrdenAmbulatoria(rs.getString("orden_ambulatoria"));
			}
			
			String cadenaDatosPaciente = !dtoParam.getSolicitud().isEmpty() ? consultarDatosPaciente : consultarDatosPacienteXOrden;  
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaDatosPaciente ));
			ps.setInt(1, !dtoParam.getSolicitud().isEmpty() ? Utilidades.convertirAEntero(dtoParam.getSolicitud()) : Utilidades.convertirAEntero(dtoParam.getOrdenAmbulatoria()));
			
			rs =new ResultSetDecorator(ps.executeQuery());
			if (rs.next()){
				dtoJus.setFechaJustificacion(rs.getString("fecha"));
				dtoJus.setSolicitadoEn(rs.getString("via_ingreso")+" - "+rs.getString("servsolicitadoen"));
				dtoJus.setIngreso(rs.getString("ingreso"));
				dtoJus.setIdPaciente(rs.getString("documentopaciente"));
				dtoJus.setNombrePaciente(rs.getString("nombrepaciente"));
				dtoJus.setFechaNacimientoPaciente(rs.getString("nacimiento"));
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarJustificacionArtDx));
			ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
			rs =new ResultSetDecorator(ps.executeQuery());
			while (rs.next()){
				Diagnostico diag = new Diagnostico();
				diag.setAcronimo(rs.getString("acronimo_dx"));
				diag.setTipoCIE(rs.getInt("tipo_cie"));
				diag.setNombre(rs.getString("nombre"));
				if(rs.getString("tipo_dx").equals("PRIN"))
					diag.setPrincipal(true);
				if(rs.getString("tipo_dx").equals("COMP"))
					diag.setComplicacion(true);
				dtoJus.getDiagnosticos().add(diag);
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarJustificacionArtResp));
			ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
			
			logger.info("Konsulta SQL + "+consultarJustificacionArtResp);
			logger.info("Param 1 = "+dtoJus.getCodigo());
			
			rs =new ResultSetDecorator(ps.executeQuery());
			while (rs.next()){
				DtoResponsableJustificacionNoPos resp = new DtoResponsableJustificacionNoPos();
				resp.setSubcuenta(rs.getString("subcuenta"));
				resp.setCantidad(rs.getString("cantidad"));
				resp.setAcronimoEstado(rs.getString("acronimo_estado"));
				resp.setEstado(rs.getString("estado"));
				resp.setConvenio(rs.getString("convenio"));
				resp.setTipoUsuario(rs.getString("tipo_usuario"));
				dtoJus.getResponsables().add(resp);
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarJustificacionArtFijo));
			ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
			rs =new ResultSetDecorator(ps.executeQuery());
			if (rs.next()){
				dtoJus.setJustificacion(rs.getString("observacion"));
				dtoJus.setProfesionalResponsable(rs.getString("profesional_responsable"));
				dtoJus.setArticuloSustituto(rs.getString("medicamento_sustituto"));
				dtoJus.setCantidadSustituto(rs.getString("cantidad_sustituto"));
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarDatosMedico));
			ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getProfesionalResponsable()));
			rs =new ResultSetDecorator(ps.executeQuery());
			if (rs.next()){
				dtoJus.setRegistroMedico(rs.getString("registromedico"));
				dtoJus.setIdMedico(rs.getString("documentomedico"));
				dtoJus.setNombreMedico(rs.getString("nombremedico"));
				dtoJus.setEspecialidadMedico(rs.getString("especialidad"));
				dtoJus.setFirmaDigital(rs.getString("firmadigital"));
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarJustificacionArtParamCampos));
			ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
			rs =new ResultSetDecorator(ps.executeQuery());
			while (rs.next()){
				DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
				campo.setCodigo(rs.getString("campo"));
				campo.setValor(rs.getString("valor"));
				campo.setTipoHtml(rs.getString("tipo_html"));
				dtoJus.getCamposParam().add(campo);
			}
			
			for(int op=0; op<dtoJus.getCamposParam().size(); op++){
				if (dtoJus.getCamposParam().get(op).getTipoHtml().equals("CHEC")){
					ps =  new PreparedStatementDecorator(con.prepareStatement(consultarJustificacionArtOpcionesCampo));
					ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
					ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(op).getCodigo()));
					rs =new ResultSetDecorator(ps.executeQuery());
					while (rs.next()){
						DtoParamOpcionesCamposJusNoPos opcion = new DtoParamOpcionesCamposJusNoPos();
						opcion.setCodigo(rs.getString("opcion"));
						dtoJus.getCamposParam().get(op).getOpciones().add(opcion);
					}
				}
			}
			
			
		} catch(SQLException e){
            logger.error("ERROR SQLException consultarJustificacion: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarJustificacion: ", ex);
		
        }finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
			}
		}
		return dtoJus;
	}

	/**
	 * Método para actualizar una justificación No Pos
	 * @param con
	 * @param dtoJus
	 * @param tipoJustificacion
	 * @return
	 */
	public static boolean actualizarJustificacion(Connection con, DtoJustificacionNoPos dtoJus, String tipoJustificacion) {
		boolean transaccionExitosa = true;
		PreparedStatementDecorator ps = null;
		
		if(tipoJustificacion.equals("INSU")){

			UtilidadBD.iniciarTransaccion(con);
			
			try {
				//****** actualizacion datos justificación en la tabla justificacion_art_sol
				ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarJusArtSol));
				ps.setString(1, dtoJus.getBibliografia());
				ps.setString(2, dtoJus.getUsuarioModifica());
				ps.setInt(3, Utilidades.convertirAEntero(dtoJus.getCodigo()));
				
				ps.executeUpdate();
				
			} catch (SQLException e) {
				logger.info("ERROR / actualizarJustificacion / "+e);
				transaccionExitosa=false;
			}finally{
				try {
					if(ps!=null){
						ps.close();
					}
					
				} catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
				}
			}
		
			if(transaccionExitosa){
				try {
					//eliminamos los datos de la tabla justificacion_art_dx
					ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarJusArtDx));
					ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
					ps.executeUpdate();
				} catch (SQLException e) {
					logger.info("ERROR / actualizarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try {
						if(ps!=null){
							ps.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					}
				}
			}
			
			if(transaccionExitosa){
				try {
					//Insercion de datos en la tabla justificacion_art_dx
					for(int c=0; c<dtoJus.getDiagnosticos().size(); c++){
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtDx));
						ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
						ps.setString(2, dtoJus.getDiagnosticos().get(c).getAcronimo());
						ps.setInt(3, dtoJus.getDiagnosticos().get(c).getTipoCIE());
						ps.setString(4, dtoJus.getDiagnosticos().get(c).getNombreTipoDiagnostico());
						ps.setInt(5, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_dx"));
						
						ps.executeUpdate();
					}
				} catch (SQLException e) {
					logger.info("ERROR / actualizarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try {
						if(ps!=null){
							ps.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					}
				}
			}	
			
			if(transaccionExitosa){
				try {
					//actualización de datos en la tabla justificacion_art_fijo
					ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarJusArtFijo));
					ps.setString(1, dtoJus.getJustificacion());
					ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getArticuloSustituto()));
					ps.setInt(3, Utilidades.convertirAEntero(dtoJus.getCantidadSustituto()));
					ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getCodigo()));
					ps.executeUpdate();
						
				} catch (SQLException e) {
					logger.info("ERROR / actualizarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try {
						if(ps!=null){
							ps.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					}
				}
			}
			
			if(transaccionExitosa){
				try {
					//actualización de datos en la tabla justificacion_art_resp
					ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarJusArtResp));
					ps.setString(1, dtoJus.getEstado());
					ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getCantidad()));
					ps.setInt(3, Utilidades.convertirAEntero(dtoJus.getCodigo()));
					ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getSubcuenta()));
					
					logger.info("SQL / "+actualizarJusArtResp);
					logger.info("1 - "+dtoJus.getEstado());
					logger.info("2 - "+Utilidades.convertirAEntero(dtoJus.getCantidad()));
					logger.info("3 - "+Utilidades.convertirAEntero(dtoJus.getCodigo()));
					logger.info("4 - "+Utilidades.convertirAEntero(dtoJus.getSubcuenta()));
					
					ps.executeUpdate();
						
				} catch (SQLException e) {
					logger.info("ERROR / actualizarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try {
						if(ps!=null){
							ps.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					}
				}
			}
			
			if(transaccionExitosa){
				try {
					//eliminamos los datos de la tabla justificacion_art_param
					ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarJusArtParam));
					ps.setInt(1, Utilidades.convertirAEntero(dtoJus.getCodigo()));
					ps.executeUpdate();
					
				} catch (SQLException e) {
					logger.info("ERROR / actualizarJustificacion / "+e);
					transaccionExitosa=false;
				}finally{
					try {
						if(ps!=null){
							ps.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					}
				}
			}
			
			if(transaccionExitosa){
				try {
					//Insercion de datos en la tabla justificacion_serv_param
					for(int c=0; c<dtoJus.getCamposParam().size(); c++){
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtParam));
						ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_param"));
						ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getCodigo()));
						ps.setString(3, dtoJus.getCamposParam().get(c).getValor());
						ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getCodigo()));
						ps.setString(5, dtoJus.getCamposParam().get(c).getEtiqueta());
						ps.setInt(6, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getCodigoParam()));
						ps.setInt(7, Utilidades.convertirAEntero(dtoJus.getInstitucion()));
						ps.setNull(8, Types.INTEGER);
						
						if(ps.executeUpdate()<=0)
							transaccionExitosa=false;
						
						if(dtoJus.getCamposParam().get(c).getTipoHtml().equals("CHEC")){
							for(int op=0; op<dtoJus.getCamposParam().get(c).getOpciones().size(); op++){
								if(dtoJus.getCamposParam().get(c).getOpciones().get(op).getSeleccionado().equals(ConstantesBD.acronimoSi)){
									ps =  new PreparedStatementDecorator(con.prepareStatement(insertarJusArtParam));
									ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_justificacion_art_param"));
									ps.setInt(2, Utilidades.convertirAEntero(dtoJus.getCodigo()));
									ps.setString(3, ConstantesBD.acronimoSi);
									ps.setInt(4, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getCodigo()));
									ps.setString(5, dtoJus.getCamposParam().get(c).getOpciones().get(op).getOpcion());
									ps.setNull(6, Types.INTEGER);
									ps.setInt(7, Utilidades.convertirAEntero(dtoJus.getInstitucion()));
									ps.setInt(8, Utilidades.convertirAEntero(dtoJus.getCamposParam().get(c).getOpciones().get(op).getCodigo()));
									
									if(ps.executeUpdate()<=0)
										transaccionExitosa=false;
								}	
							}
						}
							
					}
				} catch (SQLException e) {
					transaccionExitosa=false;
					logger.info("ERROR / actualizarJustificacion / "+e);
				}finally{
					try {
						if(ps!=null){
							ps.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
					}
				}
			}
			
		}
		
		if(transaccionExitosa)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return transaccionExitosa;
	}
}
