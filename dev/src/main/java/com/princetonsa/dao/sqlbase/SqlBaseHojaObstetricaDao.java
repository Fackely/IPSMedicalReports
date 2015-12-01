/*
 * Creado en Jun 15, 2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.mundo.UsuarioBasico;


/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class SqlBaseHojaObstetricaDao
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseHojaObstetricaDao.class);
	
	/**
	 *  Sentencia SQL para insertar una hoja obstétrica
	 */
	private final static String insertarHojaObstetricaStr = 	"INSERT INTO hoja_obstetrica " +
																		"( " +
																		"codigo, " +
																		"paciente, " +
																		"embarazo, " +
																		"fecha_orden, " +
																		"fur, " +
																		"fpp, " +
																		"fecha_ultrasonido, " +
																		"fpp_ultrasonido, " +
																		"edad_gestacional, " +
																		"edad_parto, " +
																		"fin_embarazo, " +
																		"datos_medico, " +
																		"login_usuario, " +
																		"institucion, " +
																		"fecha_grabacion, " +
																		"hora_grabacion, " +
																		"observaciones_grales," +
																		"confiable," +
																		"vigente_antitetanica," +
																		"dosis_antitetanica, " +
																		"mes_gestacion_antitetanica," +
																		"antirubeola," +
																		"fecha_terminacion," +
																		"motivo_finalizacion," +
																		"edad_gestacional_finalizar," +
																		"peso," +
																		"talla," +
																		"embarazo_deseado," +
																		"embarazo_planeado) " +
																		"VALUES " +
																		"( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
																		") ";
	
	private static String insertarHojaDesdeValoracionStr="INSERT INTO hoja_obstetrica(codigo, paciente, embarazo, fecha_orden, fur, fpp, edad_gestacional, fin_embarazo, institucion, login_usuario, datos_medico, fecha_grabacion, hora_grabacion) VALUES(?, ?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")";
	
	/**
	 *  Sentencia SQL para insertar el resumen gestacional a una hoja obstétrica
	 */
	private final static String insertarResumenGestacionalStr = 	"INSERT INTO resumen_gestacional " +
																														"( " +
																														"codigo, " +
																														"hoja_obstetrica, " +
																														"edad_gestacional, " +
																														"fecha, " +
																														"hora, " +
																														"datos_medico " +
																														") " +
																														"VALUES " +
																														"( ?, ?, ?, ?, ?, ?" +
																														") ";
	
	/**
	 *  Sentencia SQL para insertar el histórico del exámen de laboratorio
	 */
	private final static String insertarHistoricoExamenLabStr = 	"INSERT INTO historico_examenes_lab " +
																														"( " +
																														"codigo, " +
																														"hoja_obstetrica, " +
																														"edad_gestacional, " +
																														"datos_medico, " +
																														"fecha, " +
																														"hora " +
																														") " +
																														"VALUES " +
																														"( ?, ?, ?, ?, ?, ?" +
																														") ";
	
	/**
	 *  Sentencia SQL para insertar el detalle del resumen gestacional
	 */
	private final static String insertarDetalleResumenGestacionalStr = 	"INSERT INTO detalle_resumen_gest " +
																																	"( " +
																																	"codigo_resumen_gest, " +
																																	"tipo_resumen_gest, " +
																																	"valor " +
																																	") " +
																																	"VALUES " +
																																	"( ?, ?, ?)";

	/**
	 *  Sentencia SQL para insertar el detalle del exámen de laboratorio
	 */
	private final static String insertarDetalleExamenLabStr = 	"INSERT INTO detalle_examen_lab " +
																													"( " +
																													"codigo,"+
																													"codigo_hist_examen_lab, " +
																													"tipo_examen_lab, " +
																													"resultado, " +
																													"observaciones"+
																													") " +
																													"VALUES " +
																													"( ?, ?, ?, ?, ?)";
	
	/**
	 *  Sentencia SQL para insertar el archivo adjunto del exámen de laboratorio
	 */
	private final static String insertarAdjuntoExamenLabStr = 	"INSERT INTO adjuntos_examenes_lab " +
																													"( " +
																													"detalle_examen_lab,"+
																													"nombre_original, " +
																													"nombre_archivo " +
																													") " +
																													"VALUES " +
																													"( ?, ?, ?)";
	
	/**
	 *  Sentencia SQL para insertar el histórico de los ultrasonidos
	 */
	private final static String insertarHistoricoUltrasonidoStr = 	"INSERT INTO historico_ultrasonidos " +
																														"( " +
																														"codigo, " +
																														"hoja_obstetrica, " +
																														"datos_medico, " +
																														"fecha, " +
																														"hora " +
																														") " +
																														"VALUES " +
																														"( ?, ?, ?, ?, ?" +
																														") ";
	
	/**
	 *  Sentencia SQL para insertar el detalle del resumen gestacional
	 */
	private final static String insertarDetalleUltrasonidoStr = 	"INSERT INTO detalle_ultrasonido " +
																																	"( " +
																																	"codigo_histo_ultrasonido, " +
																																	"ultrasonido_institucion, " +
																																	"valor " +
																																	") " +
																																	"VALUES " +
																																	"( ?, ?, ?)";
	
	/**
	 *  Sentencia SQL para insertar el archivo adjunto del exámen de laboratorio
	 */
	private final static String insertarAdjuntoUltrasonidoStr = 	"INSERT INTO adjuntos_ultrasonido " +
																													"( " +
																													"codigo_histo_ultrasonido,"+
																													"nombre_original, " +
																													"nombre_archivo " +
																													") " +
																													"VALUES " +
																													"( ?, ?, ?)";
	
			
	/**
	 * Sentencia para ingresar un ebmbarazo en los antecedentes gineco-obstetricos
	 */
	private static String insertarEmbarazoStr="INSERT INTO ant_gineco_embarazo ( codigo_paciente, codigo, meses_gestacion, trabajo_parto, fin_embarazo, duracion_trabajo_parto, tiempo_ruptura_membranas, legrado) VALUES (?, ?, ?, -1, "+ValoresPorDefecto.getValorFalseParaConsultas()+", ?, ?, ?)";

	/**
	 * Sentencia para consultar los campos de la hoja obstétrica que generan log
	 */
	private static String cargarDatosLogStr="SELECT fur AS fur, fpp AS fpp, fpp_ultrasonido AS fppUltrasonido, edad_parto AS edadParto, fin_embarazo AS finEmbarazo, edad_gestacional AS edadGestacional FROM hoja_obstetrica where codigo=?";
	
	/**
	 * Sentencia para ingresar un antecedente totalmente vacío
	 */
	private static String insertarAntecedenteStr="INSERT INTO ant_gineco_obste(codigo_paciente, edad_menarquia, edad_menopausia, fecha, hora, usuario) VALUES(?, -1, -1, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";
	
	/**
	 * Sentencia para actualizar el detalle embarazo gineco-obstétrico
	 */
	private final static String modificarAntGinecoEmbarazoStr=	"UPDATE " +
																														"ant_gineco_embarazo SET " +
																														"fecha_terminacion= ?, " +
																														"trabajo_parto= ?, " +
																														"otro_trabajo_parto= ?, " +
																														"meses_gestacion= ?,"+
																														"duracion_trabajo_parto= ?, " +
																														"tiempo_ruptura_membranas= ?, " +
																														"legrado= ? " +
																														" WHERE " +
																														" codigo = ? AND"+
																														" codigo_paciente = ?";
	
	/**
	 *  Sentencia SQL para insertar el histórico del antecedente gineco_obstétrico
	 */
		private final static String insertarAntecedenteGinecoHistoStr =	"INSERT INTO ant_gineco_histo " +
																																"( " +
																																"codigo_paciente, " +
																																"codigo, " +
																																"concepto_menstruacion, " +
																																"fecha_ultima_regla, " +
																																"fecha_creacion," +
																																"hora_creacion " +
																																") " +
																																"VALUES " +
																																"( ?, (SELECT count(codigo)+1 FROM ant_gineco_histo WHERE  codigo_paciente=?), ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")";
		
		/**
		 * Sentencia para actualizar la hoja obstétrica
		 */
		private final static String actualizarHojaObstetricaStr=	"UPDATE " +
																		"hoja_obstetrica SET " +
																		"fecha_orden= ?, " +
																		"fur= ?, " +
																		"fpp= ?, " +
																		"fecha_ultrasonido= ?,"+
																		"fpp_ultrasonido= ?,"+
																		"edad_gestacional= ?,"+
																		"edad_parto= ?,"+
																		"fin_embarazo= ?,"+
																		"observaciones_grales= ?,"+
																		"confiable= ?,"+
																		"vigente_antitetanica= ?,"+
																		"dosis_antitetanica= ?,"+
																		"mes_gestacion_antitetanica= ?,"+
																		"antirubeola= ?," +
																		"fecha_terminacion = ?," +
																		"motivo_finalizacion= ?," +
																		"edad_gestacional_finalizar = ?," +
																		"peso = ?, " +
																		"talla = ?, " +
																		"embarazo_deseado = ? ," +
																		"embarazo_planeado = ? "+
																		" 	WHERE embarazo = ? AND paciente = ?";
	
	/**
	 * String para consultar el historico de los examenes de laboratorio
	 */
	private static String consultarHistoricoExamenesLabStr=
							"SELECT " +
								"de.codigo AS tipoExamenLab, " +
								"tel.nombre AS nombreExamen, " +
								"de.resultado AS resultado, " +
								"de.observaciones AS observaciones, " +
								"hex.codigo AS codigo_histo_examen, " +
								"to_char(hex.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, " +
								"hex.hora AS hora, " +
								"hex.datos_medico AS datos_medico " +
							"FROM " +
								"hoja_obstetrica hoja " +
							"INNER JOIN " +
								"historico_examenes_lab hex " +
									"ON (hex.hoja_obstetrica=hoja.codigo) " +
							"INNER JOIN " +
								"detalle_examen_lab de " +
									"ON (de.codigo_hist_examen_lab=hex.codigo) " +
							"INNER JOIN " +
								"examen_lab_institucion elin " +
									"ON (elin.codigo=de.tipo_examen_lab) " +
							"INNER JOIN " +
								"tipos_examen_lab tel " +
									"ON (tel.codigo=elin.tipo_examen_lab) " +
							"WHERE " +
								"hoja.paciente=? " +
								"AND hoja.embarazo=? " +
								"AND elin.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"ORDER BY hex.fecha,hex.hora";
	
	private static String actualizarEmbarazoFinalizadoStr="UPDATE ant_gineco_embarazo SET fin_embarazo="+ValoresPorDefecto.getValorTrueParaConsultas()+" WHERE codigo_paciente=? AND codigo=?";
	/**
	 * Sentencia para consultar el número de registros 
	 */
	private final static String consultaRegistrosExamenesLabStr="SELECT  count(1) AS registros FROM hoja_obstetrica h INNER JOIN resumen_gestacional r ON (r.hoja_obstetrica=h.codigo) WHERE h.paciente=? and h.embarazo=?";
	
	/** 
	 *Sentencia para consultar los dos últimos historicos de los examenes de laboratorio
	 */
	private final static String consultarUltimosHistoricosExamenesLabStr="SELECT  dhl.tipo_examen_lab as tipo_examen_lab, dhl.resultado as resultado, dhl.observaciones as observaciones, to_char(hexl.fecha,'"+ConstantesBD.formatoFechaAp+"') as fecha, hexl.hora as hora, hexl.edad_gestacional as edad_gestacional, dhl.codigo as detalle_examen_lab, hexl.codigo AS codigo_historico FROM (select hex.fecha,hex.hora,hex.codigo,hex.edad_gestacional from hoja_obstetrica hoja INNER JOIN historico_examenes_lab hex ON (hex.hoja_obstetrica=hoja.codigo) WHERE hoja.paciente=? AND hoja.embarazo=? ORDER BY hex.fecha,hex.hora OFFSET ?-2)hexl,detalle_examen_lab dhl WHERE dhl.codigo_hist_examen_lab=hexl.codigo ";
		
	/** 
	 *Sentencia para consultar los tipos de resumen gestacional parametrizados a la institución
	 */
	private final static String consultarTiposResumenGestacionalStr="SELECT  rgins.codigo AS codigo, rg.nombre AS nombre FROM tipos_resumen_gestacional rg INNER JOIN resumen_gest_institucion rgins ON(rgins.tipo_resumen_gestacional=rg.codigo) WHERE rgins.institucion=? AND rgins.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY rg.codigo asc";
	
	/** 
	 *Sentencia para consultar los tipos de exámenes de laboratorio
	 */
	private final static String consultarTiposExamenLaboratorioStr="SELECT  elins.codigo AS codigo, el.nombre  AS nombre FROM tipos_examen_lab el INNER JOIN examen_lab_institucion elins ON(elins.tipo_examen_lab=el.codigo) WHERE elins.institucion=? AND elins.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY el.codigo asc";
	
	/** 
	 *Sentencia para consultar los tipos de ultrasonido por institución
	 */
	private final static String consultarTiposUltrasonidoStr="SELECT  uins.codigo AS codigo, tu.nombre  AS nombre FROM tipos_ultrasonido tu INNER JOIN ultrasonido_institucion uins ON(uins.tipo_ultrasonido=tu.codigo) WHERE uins.institucion=? AND uins.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY tu.codigo asc";
	
	/**
	 *  Sentencia SQL para insertar el detalle de otro exámen de laboratorio
	 */
	private final static String insertarDetalleOtroExamenLabStr = 	"INSERT INTO detalle_otro_examen_lab " +
																													"( " +
																													"codigo,"+
																													"codigo_hist_examen_lab, " +
																													"descripcion_otro, " +
																													"resultado, " +
																													"observaciones"+
																													") " +
																													"VALUES " +
																													"( ?, ?, ?, ?, ?)";
	
	/**
	 *  Sentencia SQL para insertar el archivo adjunto de otro exámen de laboratorio
	 */
	private final static String insertarAdjuntoOtroExamenLabStr = 	"INSERT INTO adjunto_otro_examen_lab " +
																															"( " +
																															"detalle_otro_examen_lab,"+
																															"nombre_original, " +
																															"nombre_archivo " +
																															") " +
																															"VALUES " +
																															"( ?, ?, ?)";
	
	
	/**
	 * Método para obtener el codigo de la hoja obstétrica que está asociado a una secuencia
	 * @param con -> conexion
	 * @param secuencia -> string que tiene la secuencia
	 * @return codigoHojaObstetrica
	 */
	public static int obtenerCodigoSecuencia (Connection con, String secuencia)
	{
		String consultaSecuencia="SELECT "+secuencia;
			
		try
		{
			
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia));
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			return 0;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del codigo de secuencia: SqlBaseHojaObstetricaDao "+secuencia+e.toString());
			return 0;
		}
	}
	
	/**
	 * Método para insertar una hoja obstetrica
	 * @param con
	 * @param secuencia -> String
	 * @param codigoPaciente -> int
	 * @param embarazo -> int
	 * @param login -> String
	 * @param institucion -> int
	 * @param datosMedico -> String
	 * @param fechaOrden -> String
	 * @param fur -> String
	 * @param fpp -> String
	 * @param fechaUltrasonido -> String
	 * @param fppUltrasonido -> String
	 * @param edadGestacional -> int
	 * @param edadParto -> int
	 * @param finalizacionEmbarazo -> boolean
	 * @param observacionesGrales -> String
	 * @param confiable
	 * @param vigenteAntitetanica
	 * @param dosisAntitetanica
	 * @param mesesGestacionAntitetanica
	 * @param antirubeola
	 * @param fechaTerminacion
	 * @param motivoFinalizacion
	 * @param egFinalizar
	 * @param peso
	 * @param talla
	 * @param embarazoDeseado
	 * @param embarazoPlaneado
	 *
	 * @return codigoHoja
	 */
	
	public static int insertarHojaObstetrica( Connection con, String secuencia, int codigoPaciente, int embarazo, String login, int institucion, String datosMedico, 
											  String fechaOrden, String fur, String fpp, String fechaUltrasonido, String fppUltrasonido, int edadGestacional,
											  int edadParto, boolean finalizacionEmbarazo, String observacionesGrales, String confiable,String vigenteAntitetanica, String dosisAntitetanica, String mesesGestacionAntitetanica, String antirubeola,
											  String fechaTerminacion, String motivoFinalizacion, int egFinalizar,int peso, int talla, String embarazoDeseado, String embarazoPlaneado)
	{
		int codigoHoja=0;
		int numEmbarazo=0;
		
		try
			{
					
					fechaOrden=UtilidadFecha.conversionFormatoFechaABD(fechaOrden);
					
					numEmbarazo=Utilidades.obtenerUltimoNumeroEmbarazo(con, codigoPaciente);
					
					//Se obtiene el código de la secuencia			
					//codigoHoja=obtenerCodigoSecuencia(con, secuencia);
					
					
					if(numEmbarazo>=embarazo)
					{	
						codigoHoja=actualizarHojaObstetrica(con, codigoPaciente, embarazo, fechaOrden, fur, fpp, fechaUltrasonido, fppUltrasonido, edadGestacional,
															edadParto, finalizacionEmbarazo, observacionesGrales, confiable, vigenteAntitetanica, dosisAntitetanica, mesesGestacionAntitetanica, antirubeola,
															fechaTerminacion, motivoFinalizacion, egFinalizar,peso,talla,embarazoDeseado,embarazoPlaneado);
					}
										
					else
						{
							//Se obtiene el código de la secuencia			
							codigoHoja=obtenerCodigoSecuencia(con, secuencia);
							
							PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarHojaObstetricaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							ps.setInt(1, codigoHoja);
							ps.setInt(2, codigoPaciente);
							ps.setInt(3, embarazo);
							ps.setString(4, fechaOrden);
							if(fur.equals(""))
							{
								ps.setString(5, null);
							}
							else
							{
								fur=UtilidadFecha.conversionFormatoFechaABD(fur);
								ps.setString(5, fur);
							}
							
							if(fpp.equals(""))
							{
								ps.setString(6, null);
							}
							else
							{
								fpp=UtilidadFecha.conversionFormatoFechaABD(fpp);
								ps.setString(6, fpp);
							}
							
							if(fechaUltrasonido.equals(""))
							{
								ps.setString(7, null);
							}
							else
							{
								fechaUltrasonido=UtilidadFecha.conversionFormatoFechaABD(fechaUltrasonido);
								ps.setString(7, fechaUltrasonido);
							}
						
							if(fppUltrasonido.equals(""))
							{
								ps.setString(8, null);
							}
							else
							{
								fppUltrasonido=UtilidadFecha.conversionFormatoFechaABD(fppUltrasonido);
								ps.setString(8, fppUltrasonido);
							}
							
							if(edadGestacional==0)
								ps.setString(9, null);
							else
								ps.setInt(9, edadGestacional);
							
							if(edadParto==0)
								ps.setString(10, null);
							else
								ps.setInt(10, edadParto);
							
							ps.setBoolean(11, finalizacionEmbarazo);
							ps.setString(12, datosMedico);
							ps.setString(13, login);
							ps.setInt(14, institucion);
							ps.setString(15, observacionesGrales);
							ps.setString(16, confiable);
							ps.setString(17, vigenteAntitetanica);
							if(!dosisAntitetanica.equals(""))
								ps.setString(18, dosisAntitetanica);
							else
								ps.setNull(18, Types.VARCHAR);
							
							if(UtilidadCadena.noEsVacio(mesesGestacionAntitetanica))
								ps.setInt(19, Integer.parseInt(mesesGestacionAntitetanica));
							else
								ps.setNull(19, Types.INTEGER);
							
							ps.setString(20, antirubeola);
							
							if(!UtilidadCadena.noEsVacio(fechaTerminacion))
							{
								ps.setString(21, null);
							}
							else
							{
								ps.setString(21, UtilidadFecha.conversionFormatoFechaABD(fechaTerminacion));
							}
							
							if(!UtilidadCadena.noEsVacio(motivoFinalizacion))
							{
								ps.setString(22, null);
							}
							else
							{
								ps.setString(22, motivoFinalizacion);
							}
							
							if(!UtilidadCadena.noEsVacio(egFinalizar+""))
							{
								ps.setInt(23, Types.INTEGER);
							}
							else
							{
								ps.setInt(23, egFinalizar);
							}
							
							ps.setInt(24,peso);
							ps.setInt(25,talla);
							ps.setString(26, embarazoDeseado);
							ps.setString(27,embarazoPlaneado);
																			
							ps.executeUpdate();
							if(finalizacionEmbarazo==true)
							{
								actualizarEmbarazoParaAntecedentes(con, codigoPaciente, embarazo);
							}

					}
			}//try
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos: SqlBaseHojaObstetricaDao "+e.toString() );
					codigoHoja=0;
			}
			return codigoHoja;
	
	}
	
	/**
	 * Método para insertar el resumen gestacional a la hoja obstetrica
	 * @param con
	 * @param secuencia -> String
	 * @param codigoHojaObstetrica -> int
	 * @param edadGestacionalResumen -> int
	 * @param fechaGestacional -> String
	 * @param horaGestacional  -> String
	 * @param datosMedico -> String
	 *
	 * @return codigoResumen
	 */
	
	public static int insertarResumenGestacional ( Connection con, String secuencia, int codigoHojaObstetrica, int edadGestacionalResumen, String fechaGestacional, String horaGestacional, String datosMedico)
	{
		int codigoResumen=0;
		try
			{
					
					/*fechaGestacional=UtilidadFecha.conversionFormatoFechaABD(fechaGestacional);*/
					
					
					//Se obtiene el código de la secuencia
					codigoResumen=obtenerCodigoSecuencia(con, secuencia);
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarResumenGestacionalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoResumen);
					ps.setInt(2, codigoHojaObstetrica);
					ps.setInt(3, edadGestacionalResumen);
					if(fechaGestacional.equals(""))
					{
						ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					}
				else
					{
					fechaGestacional=UtilidadFecha.conversionFormatoFechaABD(fechaGestacional);
					ps.setString(4, fechaGestacional);
					}
					if(horaGestacional.equals(""))
					{
						ps.setString(5, UtilidadFecha.getHoraActual());
					}
					else
					{
						ps.setString(5, horaGestacional);
					}
					ps.setString(6, datosMedico);
					
										
					ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos de resumen gestacional: SqlBaseHojaObstetricaDao "+e.toString() );
					codigoResumen=0;
			}
			return codigoResumen;
	}
	
	/**
	 * Método para insertar detalle del resumen gestacional
	 * @param con
	 * @param codigoResumenGestacional
	 * @param codigoTipoResumenGest
	 * @param valorTipoResumenGest
	 * @return
	 */
	public static int insertarDetalleResumenGestacional(Connection con, int codigoResumenGestacional, int codigoTipoResumenGest, String valorTipoResumenGest)
	{
		int resp=0;
		try
			{
									
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleResumenGestacionalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoResumenGestacional);
					ps.setInt(2, codigoTipoResumenGest);
					ps.setString(3, valorTipoResumenGest);
					
										
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del detalle resumen gestacional: SqlBaseHojaObstetricaDao "+e.toString() );
					resp=0;
			}
			return resp;
		
	}
	
	/**
	* Método para insertar el histórico del exámen de laboratorio
	 * @param con
	 * @param secuencia => String
	 * @param codigoHistoricoExamenLab => int
	 * @param codigoHojaObstetrica => int
	 * @param datosMedico => String
	 * @param edadGestacionalExamen -> int
	 * @param fechaExamen -> String
	 * @param horaExamen -> String
	 *
	 * @return codigoHistoricoExamenLab
	 */
	
	public static int insertarHistoricoExamenLab ( Connection con, String secuencia, int codigoHojaObstetrica, String datosMedico, int edadGestacionalExamen, String fechaExamen, String horaExamen)
	{
		int codigoHistoricoExamenLab=0;
		try
			{
									
					//Se obtiene el código de la secuencia
					codigoHistoricoExamenLab=obtenerCodigoSecuencia(con, secuencia);
					
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarHistoricoExamenLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoHistoricoExamenLab);
					ps.setInt(2, codigoHojaObstetrica);
					ps.setInt(3, edadGestacionalExamen);
					ps.setString(4, datosMedico);
					if(fechaExamen.equals(""))
					{
						ps.setString(5, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					}
				else
					{
					fechaExamen=UtilidadFecha.conversionFormatoFechaABD(fechaExamen);
					ps.setString(5, fechaExamen);
					}
					if(horaExamen.equals(""))
					{
						ps.setString(6, UtilidadFecha.getHoraActual());
					}
					else
					{
						ps.setString(6, horaExamen);
					}
														
					ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del histórico exámenes laboratorio: SqlBaseHojaObstetricaDao "+e.toString() );
					codigoHistoricoExamenLab=0;
			}
			return codigoHistoricoExamenLab;
	}
	
	/**
	 * Método para insertar el detalle del exámen de laboratorio
	 * @param con => conexion
	 * @param secuencia => String
	 * @param codigoHistoricoExamenLab => int
	 * @param tipoExamen => int
	 * @param resultadoExamenLab => String
	 * @param observacionExamenLab => String
	 * 
	 * @return codigoDetalleExamenLab
	 */
	public static int insertarDetalleExamenLab(Connection con, String secuencia,  int codigoHistoricoExamenLab, int tipoExamen, String resultadoExamenLab, String observacionExamenLab)
	{
		int codigoDetalleExamenLab=0;
		try
			{
									
					
					//Se obtiene el còdigo de la secuencia
					codigoDetalleExamenLab=obtenerCodigoSecuencia(con, secuencia);
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleExamenLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoDetalleExamenLab);
					ps.setInt(2, codigoHistoricoExamenLab);
					ps.setInt(3, tipoExamen);
					ps.setString(4, resultadoExamenLab);
					ps.setString(5, observacionExamenLab);
										
					ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del detalle del exámen de laboratorio: SqlBaseHojaObstetricaDao "+e.toString() );
					codigoDetalleExamenLab=0;
			}
			return codigoDetalleExamenLab;
	}
	
	/**
	 * Método para insertar el documento adjunto de un exámen de laboratorio
	 * @param con
	 * @param codigoDetalleExamenLab => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 */
	public static int insertarAdjuntoExamenLab(Connection con, int codigoDetalleExamenLab, String nombreOriginal, String nombreArchivo)
	{
		int resp=1;
		try
			{
									
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarAdjuntoExamenLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoDetalleExamenLab);
					ps.setString(2, nombreOriginal);
					ps.setString(3, nombreArchivo);
										
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del archivo adjunto del exámen de laboratorio: SqlBaseHojaObstetricaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	 /** Método para insertar el histórico del ultrasonido
	 * @param con una conexion abierta con una fuente de datos
	 * @param secuencia => String
	 * @param codigoHojaObstetrica  => int
	 * @param ultrasonidoFecha => String
	 * @param ultrasonidoHora => String
	  * @param datosMedico  => String
	 * @return codigoHistoricoUltrasonido
	 */
	public static int insertarHistoricoUltrasonido(Connection con, String secuencia, int codigoHojaObstetrica, String ultrasonidoFecha, String ultrasonidoHora, String datosMedico)
	{
		int codigoHistoricoUltrasonido=0;
		try
			{
									
					//Se obtiene el código de la secuencia
					codigoHistoricoUltrasonido=obtenerCodigoSecuencia(con, secuencia);
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarHistoricoUltrasonidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoHistoricoUltrasonido);
					ps.setInt(2, codigoHojaObstetrica);
					ps.setString(3, datosMedico);
					if(ultrasonidoFecha.equals(""))
					{
						ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					}
				else
					{
					ultrasonidoFecha=UtilidadFecha.conversionFormatoFechaABD(ultrasonidoFecha);
					ps.setString(4, ultrasonidoFecha);
					}
					if(ultrasonidoHora.equals(""))
					{
						ps.setString(5, UtilidadFecha.getHoraActual());
					}
					else
					{
						ps.setString(5, ultrasonidoHora);
					}
														
					ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del histórico de ultrasonidos: SqlBaseHojaObstetricaDao "+e.toString() );
					e.printStackTrace();
					codigoHistoricoUltrasonido=0;
			}
			return codigoHistoricoUltrasonido;
		
	}
	
	/**
	 * Método para insertar el detalle del ultrasonidol
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoHistoUltrasonido => int
	 * @param codigoTipoUltrasonido  => int
	 * @param valorTipoUltrasonido  => String
	 *@return codigoDetalleUltrasonido
	 */
	public static int insertarDetalleUltrasonido(Connection con, int codigoHistoUltrasonido, int codigoTipoUltrasonido, String valorTipoUltrasonido)
	{
	int resp=1;
		try
		{
			
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleUltrasonidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoHistoUltrasonido);
				ps.setInt(2, codigoTipoUltrasonido);
				ps.setString(3, valorTipoUltrasonido);
									
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos del detalle del ultrasonido: SqlBaseHojaObstetricaDao "+e.toString() );
				e.printStackTrace();
				resp=-1;
		}
		return resp;
	}
	
	/**
	 * Método para insertar el documento adjunto a un ultrasonido
	 * @param con
	 * @param codigo_histo_ultrasonido => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 */
	public static int insertarAdjuntoUltrasonido(Connection con, int codigo_histo_ultrasonido, String nombreOriginal, String nombreArchivo)
	{
		int resp=1;
		try
			{
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarAdjuntoUltrasonidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigo_histo_ultrasonido);
					ps.setString(2, nombreOriginal);
					ps.setString(3, nombreArchivo);
										
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del archivo adjunto en ultrasonido: SqlBaseHojaObstetricaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	

	/**
	 * Funcion para listar la mujeres embarazadas 
	 * @param con
	 * @return
	 */
	public static Collection cargarListado(Connection con)
	{     
		
		
		String  consultaStr = " SELECT     UPPER(per.primer_nombre) || ' ' || UPPER(per.segundo_nombre) AS nombres, " +
          					  "	UPPER(per.primer_apellido) || ' ' || UPPER(per.segundo_apellido) AS apellidos, " + 
                              " per.tipo_identificacion as tipoid, per.numero_identificacion AS id, " +
                              " getEdad(per.fecha_nacimiento) AS edad," +
                              " coalesce(to_char(hoja.fpp, '"+ConstantesBD.formatoFechaAp+"'), '') AS fpp, " +
                              " coalesce(to_char(hoja.fpp, 'YYYY-MM-DD'), '') AS fppbd, " +
                              " CASE WHEN hoja.edad_gestacional IS NULL THEN ' ' ELSE  hoja.edad_gestacional || ' ' END  AS eg, " + 
                              " perso.primer_nombre || ' ' || perso.segundo_nombre || ' ' || perso.primer_apellido || ' ' || perso.segundo_apellido AS medico " +
							  "	FROM hoja_obstetrica hoja " + 
							  "	INNER JOIN personas per ON ( per.codigo = hoja.paciente ) " + 
							  "	INNER JOIN usuarios usu ON ( hoja.login_usuario = usu.login )   " +
							  " INNER JOIN personas perso ON ( perso.codigo = usu.codigo_persona ) " +
										  
							  //--Para lanzar el ultimo registro de hoja Obstetrica
							
							  " INNER JOIN ( SELECT MAX(fecha_grabacion || ' ' || hora_grabacion) as tiempo, paciente FROM hoja_obstetrica hoja GROUP BY paciente ) hoja_1 " +
							  "	ON ( hoja_1.paciente = hoja.paciente AND hoja_1.tiempo = (hoja.fecha_grabacion || ' ' || hoja.hora_grabacion)) " +
							  "	WHERE hoja.fin_embarazo = " + ValoresPorDefecto.getValorFalseParaConsultas() +
							  " ORDER BY apellidos";

			
			System.out.print("\n\n\n Consulta Listado Mujeres Embarazadas (SQL BASE) ==> " + consultaStr + "  \n\n\n");
		try
		{
		  PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		  
		  return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultando Hoja Obstetrica (SQL BASE)  "+e.toString());
			return null;
		}
	}
	
	/**
	 * Funcion que retorna una coleccion resultado de una buqueda avanzada sobre embarazadas
	 * @param con objeto de conexion
	 * @param apellido
	 * @param nombre
	 * @param id
	 * @param edad
	 * @param fpp
	 * @param edadGestacional
	 * @param nombreMedico
	 * @return
	 */
	public static Collection busquedaAbanzada(Connection con,String apellido,String nombre,String id, int edad, String fpp,int edadGestacional,String nombreMedico)
	{
		Collection lista;
		String consultaStr=""; 
		
		try 
		{
	        consultaStr=armarConsulta(apellido,nombre,id,edad,fpp,edadGestacional,nombreMedico);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lista = UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de hoja obstetrica" +e.toString());
			lista=null;
		}
		
		return lista;		
	}
	
	//-Para arma la consulta de busqueda avanzada
	
	private static String armarConsulta(String apellido,String nombre,String id, int edad, String fpp,int edadGestacional,String nombreMedico)
	{		
		
		String  consulta = " SELECT  per.primer_nombre || ' ' || per.segundo_nombre AS nombres, " +
								  "	per.primer_apellido || ' ' || per.segundo_apellido AS apellidos, " + 
						          " per.numero_identificacion AS id, per.tipo_identificacion as tipoid, " +
						          " getEdad(per.fecha_nacimiento) AS edad, to_char(hoja.fpp,'"+ConstantesBD.formatoFechaAp+"') AS fpp, hoja.edad_gestacional AS eg, " + 
						          " perso.primer_nombre || ' ' || perso.segundo_nombre || ' ' || perso.primer_apellido || ' ' || perso.segundo_apellido AS medico " +
								  "	FROM hoja_obstetrica hoja " + 
								  "	INNER JOIN personas per ON ( per.codigo = hoja.paciente ) " + 
								  "	INNER JOIN usuarios usu ON ( hoja.login_usuario = usu.login )   " +
								  " INNER JOIN personas perso ON ( perso.codigo = usu.codigo_persona ) " +
											  
								  //--Para lanzar el ultimo registro de hoja Obstetrica							
								  " INNER JOIN ( SELECT MAX(fecha_grabacion || ' ' || hora_grabacion) as tiempo, paciente FROM hoja_obstetrica hoja GROUP BY paciente ) hoja_1 " +
								  "	ON ( hoja_1.paciente = hoja.paciente AND hoja_1.tiempo = (hoja.fecha_grabacion || ' ' || hoja.hora_grabacion)) " +
								  "	WHERE hoja.fin_embarazo = " + ValoresPorDefecto.getValorFalseParaConsultas();

	   									
			if(nombre != null && !nombre.equals(""))
			{
				consulta = consulta + " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre) LIKE UPPER('%" +nombre+ "%') ";
			}
			if(apellido != null && !apellido.equals(""))
			{
				consulta = consulta + " AND UPPER(per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%" +apellido+ "%') ";
			}		
			if(id != null &&  !id.equals(""))  //-La cedula  o TI
			{
				consulta = consulta + " AND per.numero_identificacion LIKE '%"+id+"%' ";
			}
			if( edad > 0) 
			{
				consulta = consulta + " AND getEdad(per.fecha_nacimiento) LIKE  '%"+edad+"%' ";
			}
			if(fpp != null && !fpp.equals(""))
			{
				consulta = consulta + " AND hoja.fpp LIKE '%"+fpp+"%' ";
			}		
			if( edadGestacional > 0) 
			{
				consulta = consulta + " AND hoja.edad_gestacional LIKE  '%"+edadGestacional+"%' ";
			}
			if(nombreMedico != null &&  !nombreMedico.equals("-1"))  //-La cedula  o TI
			{
				consulta = consulta + " AND perso.codigo = " +nombreMedico+ "";  //-Aqui se envia es el codigo persona (del medico)
			}
			
		consulta = consulta + " ORDER BY per.primer_apellido";
				
		
/*		System.out.print("\n nombre "+ nombre +"\n"); 
		System.out.print("\n apellido "+ apellido +"\n"); 
		System.out.print("\n edad "+ edad +"\n"); 
		System.out.print("\n id "+ id +"\n"); 
		System.out.print("\n fpp "+ fpp +"\n"); 
		System.out.print("\n Edad Gestacional "+ edadGestacional +"\n"); 
		System.out.print("\n Medico "+ nombreMedico +"\n"); 
		System.out.print("\n Consulta:  "+ consulta +"\n"); 
	*/	
		return consulta;
	}

	/**
	 * Metodo para ingresar un embarazo en los antecedentes solamente con los
	 * datos de la valoracion gineco-obstetrica
	 * @param con
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param codigoPaciente
	 * @param secuencia
	 * @param usuario
	 * @param numeroEmbarazo
	 * @param insertarHoja
	 * @param duracion
	 * @param tiempoRupturaMemebranas
	 * @param legrado
	 * @param return codigo del embarazo insertado, 0 si no se insertó el embarazo
	 */
	public static int ingresarEmbarazo(Connection con, String fur, String fpp, String edadGestacional, int codigoPaciente, String secuencia, UsuarioBasico usuario, int numeroEmbarazo, boolean insertarHoja, String duracion, String tiempoRupturaMemebranas, String legrado)
	{
		try
		{
			/*if(!existeEmbarazoActivo(con, codigoPaciente))
			{*/
				if(insertarHoja)
				{
					String buscarSiguienteCodigoEmbarazo="SELECT count(1)+1 AS codigo FROM ant_gineco_embarazo WHERE codigo_paciente=?";
					PreparedStatementDecorator embarazoStm= new PreparedStatementDecorator(con.prepareStatement(buscarSiguienteCodigoEmbarazo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					embarazoStm.setInt(1, codigoPaciente);
					ResultSetDecorator resultado=new ResultSetDecorator(embarazoStm.executeQuery());
					if(resultado.next())
					{
						numeroEmbarazo=resultado.getInt("codigo");
					}
				}
				
				int embarazo=Utilidades.obtenerUltimoNumeroEmbarazo(con, codigoPaciente);
				
				if(embarazo<numeroEmbarazo)
				{
					PreparedStatementDecorator embarazoStm= new PreparedStatementDecorator(con.prepareStatement(insertarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					if(numeroEmbarazo!=0)
					{
						embarazoStm= new PreparedStatementDecorator(con.prepareStatement(insertarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						embarazoStm.setInt(1, codigoPaciente);
						embarazoStm.setInt(2, numeroEmbarazo);
						if(edadGestacional!=null && !edadGestacional.equals(""))
						{
							embarazoStm.setInt(3, Integer.parseInt(edadGestacional));
						}
						else
						{
							embarazoStm.setNull(3, Types.INTEGER);
						}
						embarazoStm.setString(4, duracion);
						embarazoStm.setString(5, tiempoRupturaMemebranas);
						embarazoStm.setString(6, legrado);
						int resultadoInt=embarazoStm.executeUpdate();
						if(resultadoInt==0)
						{
							return resultadoInt;
						}
					}
					
					if(insertarHoja)
					{
						embarazoStm= new PreparedStatementDecorator(con.prepareStatement(insertarHojaDesdeValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						embarazoStm.setInt(1, obtenerCodigoSecuencia(con, secuencia));
						embarazoStm.setInt(2, codigoPaciente);
						embarazoStm.setInt(3, numeroEmbarazo);
						if(fur!=null && !fur.equals(""))
						{
							embarazoStm.setString(4, UtilidadFecha.conversionFormatoFechaABD(fur));
						}
						else
						{
							embarazoStm.setNull(4, Types.DATE);
						}
						if(fpp!=null && !fpp.equals(""))
						{
							embarazoStm.setString(5, UtilidadFecha.conversionFormatoFechaABD(fpp));
						}
						else
						{
							embarazoStm.setNull(5, Types.DATE);
						}
						if(edadGestacional!=null && !edadGestacional.equals(""))
						{
							embarazoStm.setInt(6, Integer.parseInt(edadGestacional));
						}
						else
						{
							embarazoStm.setNull(6, Types.INTEGER);
						}
						embarazoStm.setBoolean(7, false);
						embarazoStm.setInt(8, usuario.getCodigoInstitucionInt());
						embarazoStm.setString(9, usuario.getLoginUsuario());
						embarazoStm.setString(10, UtilidadTexto.agregarTextoAObservacion(null, null, usuario, false));
						int resultadoInt=embarazoStm.executeUpdate();
						if(resultadoInt>0)
						{
							return numeroEmbarazo;
						}
					}
				}
			//}
		}
		catch (SQLException e)
		{
			logger.error("Error insertando el embarazo : "+e);
		}
		return 0;
	}
	
	/**
	 * Método para verificar si ya existe un embarazo activo
	 * @param con
	 * @param codigoPaciente
	 * @return true si hay embarazo activo, false de lo contrario
	 */
	public static boolean existeEmbarazoActivo(Connection con, int codigoPaciente)
	{
		String embarazoActivoStr="SELECT count(1) AS numResultados FROM hoja_obstetrica WHERE fin_embarazo="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND paciente=?";
		PreparedStatementDecorator statementVerificarEmbarazo;
		try
		{
			statementVerificarEmbarazo =  new PreparedStatementDecorator(con.prepareStatement(embarazoActivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statementVerificarEmbarazo.setInt(1, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(statementVerificarEmbarazo.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")==1;
			}
			return false;
		}
		catch (SQLException e)
		{
			logger.error("Error verificando la existerncia de embarazos actuivos para el paciente "+codigoPaciente+" : "+e);
			return false;
		}
	}

	/**
	 * Método para ingresar un antecenete gineco-obstetrico con los datos necesarios
	 * desde la hoja obstetrica
	 * @param con
	 * @param codigoPaciente
	 * @param usuario
	 */
	public static int ingresarAntecedente(Connection con, int codigoPaciente, UsuarioBasico usuario)
	{
		int resp = 0;
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseHojaObstetricaDao)"+e1.toString());
		}
		
		try
		{
			String verificarPacienteAntecedenteStr="SELECT count(1) AS numResultados FROM antecedentes_pacientes WHERE codigo_paciente=?";
			PreparedStatementDecorator ingresarAntecedenteStm= new PreparedStatementDecorator(con.prepareStatement(verificarPacienteAntecedenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ingresarAntecedenteStm.setInt(1, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(ingresarAntecedenteStm.executeQuery());
			if(resultado.next())
			{
				if(resultado.getInt("numResultados")==0)
				{
					String insertarPacientesAntecedentesStr="INSERT INTO antecedentes_pacientes(codigo_paciente) VALUES (?)";
					ingresarAntecedenteStm= new PreparedStatementDecorator(con.prepareStatement(insertarPacientesAntecedentesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ingresarAntecedenteStm.setInt(1, codigoPaciente);
					resp = ingresarAntecedenteStm.executeUpdate();
				}
				else
					resp = 1;
			}
			
			if(resp>0)
			{
				ingresarAntecedenteStm= new PreparedStatementDecorator(con.prepareStatement(insertarAntecedenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ingresarAntecedenteStm.setInt(1, codigoPaciente);
				ingresarAntecedenteStm.setString(2, usuario.getLoginUsuario());
				resp = ingresarAntecedenteStm.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando el antecedente gineco - obstetrico : "+e);
			resp = 0;
		}
		
		return resp;
	}
	
	/**
	 * Método para obtener los campos del la hoja obstétrica
	 * que generan log
	 * @param con
	 * @param codigoHojaObstetrica
	 * @return
	 */
	public static ResultSetDecorator cargarDatosLog(Connection con, int codigoHojaObstetrica)
	{
		try
		{
			PreparedStatementDecorator cargarDatosLogStatemen= new PreparedStatementDecorator(con.prepareStatement(cargarDatosLogStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarDatosLogStatemen.setInt(1, codigoHojaObstetrica);
			return new ResultSetDecorator(cargarDatosLogStatemen.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de datos en el log de Hoja Obstetrica: SqlBaseHojaObstetricaDao "+e.toString());
			return null;
		}
	}

	/**
	 * Funcion para consultar la hoja obstetrica de un embarazo especefico de un paciente 
	 * @param con
	 * @param numeroEmbarazo
	 * @param codigoPaciente
	 * @return
	 */
	
	public static Collection consultarEmbarazo(Connection con, int numeroEmbarazo, int codigoPaciente) 
	{
	  
	String consultaStr = "";
 	
			if (numeroEmbarazo == -1)  //-Para consultar el el ultimo registro del embarazo activo 
			{	
			  consultaStr = " SELECT hoja.codigo as Codigo, hoja.paciente as paciente, hoja.embarazo as numero_embarazo, " +
				            " hoja.fecha_orden as fecha_orden, hoja.fur as fur, hoja.fpp as fpp, " + 
				            " hoja.fecha_ultrasonido as fecha_ultrasonido, hoja.fpp_ultrasonido as fpp_ultrasonido, "+ 
				            " hoja.edad_gestacional as edad_gestacional, hoja.edad_parto as edad_parto, " + 
				            " hoja.fin_embarazo as fin_embarazo, hoja.datos_medico as datos_medico, hoja.observaciones_grales as observaciones_grales," +
				            " hoja.confiable AS confiable,hoja.vigente_antitetanica AS vigente_antitetanica,hoja.dosis_antitetanica AS dosis_antitetanica," +
				            " hoja.mes_gestacion_antitetanica as mes_gestacion_antitetanica,hoja.antirubeola as antirubeola," +
				            " CASE WHEN hoja.peso IS NULL THEN '' ELSE hoja.peso || '' END as peso," +
				            " CASE WHEN hoja.talla IS NULL THEN '' ELSE hoja.talla || '' END as talla," +
				            " CASE WHEN hoja.embarazo_deseado IS NULL THEN '' ELSE embarazo_deseado END as embarazo_deseado,  " +
				            " CASE WHEN hoja.embarazo_planeado IS NULL THEN '' ELSE embarazo_planeado END as embarazo_planeado  " +
				   
							" FROM hoja_obstetrica hoja " + 
							 "  WHERE paciente=? AND hoja.fin_embarazo =  "  + ValoresPorDefecto.getValorFalseParaConsultas();
			}
			else  //--Para la consulta del ultimo historico de un embarazo finalizado
			{	
				consultaStr = "SELECT hoja.codigo as Codigo, hoja.paciente as paciente, hoja.embarazo as numero_embarazo, hoja.fecha_orden as fecha_orden, hoja.fur as fur, hoja.fpp as fpp, " +
		       				  " hoja.fecha_ultrasonido as fecha_ultrasonido, hoja.fpp_ultrasonido as fpp_ultrasonido, hoja.edad_gestacional as edad_gestacional, hoja.edad_parto as edad_parto, " +
		                      " hoja.fin_embarazo as fin_embarazo, hoja.datos_medico as datos_medico, hoja.observaciones_grales as observaciones_grales, hoja.confiable as confiable, " +
		                      " hoja.vigente_antitetanica AS vigente_antitetanica,hoja.dosis_antitetanica AS dosis_antitetanica,hoja.mes_gestacion_antitetanica as mes_gestacion_antitetanica,hoja.antirubeola as antirubeola," +
		                      " CASE WHEN hoja.peso IS NULL THEN '' ELSE hoja.peso || '' END as peso," +
					            " CASE WHEN hoja.talla IS NULL THEN '' ELSE hoja.talla || '' END as talla," +
					            " CASE WHEN hoja.embarazo_deseado IS NULL THEN '' ELSE embarazo_deseado END as embarazo_deseado,  " +
					            " CASE WHEN hoja.embarazo_planeado IS NULL THEN '' ELSE embarazo_planeado END as embarazo_planeado,  " +
		
							  //--Informacion de antecedentes gineco embarazo 
		                      " hoja.fecha_terminacion as fecha_terminacion,"+// ant_emb.otro_trabajo_parto as otro_motivo_finalizacion, " +
		                      " to_char(hoja.edad_gestacional_finalizar, '999') as eg_finalizar," +
		                      " hoja.motivo_finalizacion AS motivo_finalizacion," +
		                      " hoja.motivo_finalizacion AS otro_motivo_finalizacion " +
		                      
		                      
		
							  //-Informacion tipos partos
		                      //" tipos_parto.nombre As motivo_finalizacion " +  
							  
							  "	FROM hoja_obstetrica hoja " +
							 // " INNER JOIN ant_gineco_embarazo ant_emb " +
							  //" ON ( (ant_emb.codigo = hoja.embarazo) AND (hoja.paciente = ant_emb.codigo_paciente) )" +  //- Hoja Obstetrica ---> ant_gineco_emba 
							  /*" INNER JOIN tipos_trabajo_parto tipos_parto " +
							  " ON ( tipos_parto.codigo = ant_emb.trabajo_parto )" +  //-  ant_gineco_emba ---> tipos parto*/ 
							  "				WHERE hoja.paciente = ? "+ 
							  "				AND hoja.embarazo = ? " +
							  "             AND hoja.fin_embarazo =  "  + ValoresPorDefecto.getValorTrueParaConsultas(); //-Para tener la ultima de ese embarazo 
							 
				
				              //--- AND hoja.fecha_grabacion = (select MAX(fecha_grabacion) from hoja_obstetrica hojaAux where hojaAux.paciente = ? and hojaAux.embarazo=?) ";     
			}
     
		    System.out.print("\n\n\n Consultando Hoja SQLBASE (MUNDO HOJA OBSTETRICA) " + consultaStr + "\n\n\n");
		
		try
		{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		  
		  if (numeroEmbarazo == -1)  //-Para consultar el el ultimo registro del embarazo activo
		  {
		  	consultarNov.setInt(1, codigoPaciente);  
		  }
		 else
		 {		
			  consultarNov.setInt(1, codigoPaciente);  
			  consultarNov.setInt(2, numeroEmbarazo);  
		  }  
			  
		  
		  return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultando Alguna Hoja Obstetrica: "+e.toString());
			return null;
		}
	}
	
	/**
	 * Funcion para Actualizar Observaciones Generales de un registro historico de la hoja Obtetrica   
	 * @param con
	 * @param numeroEmbarazo
	 * @param codigoPaciente
	 * @return
	 */

	public static int actualizarEmbarazo(Connection con, int numeroEmbarazo, int codigoPaciente, String observaciones_grales) 
	{
	  
		String consultaStr = "";			 
				
		//-Realizar la actualizacion de solo los Embarazos
		
		consultaStr = " UPDATE hoja_obstetrica SET observaciones_grales = ? " +
					  "			WHERE paciente = ? "+ 
					  "			  AND embarazo = ? ";
	
		try
		{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    consultarNov.setString(1, observaciones_grales);  
		    consultarNov.setInt(2, codigoPaciente);  
			consultarNov.setInt(3, numeroEmbarazo);  

            //-Ejecutar la sentencia de consulta
			return consultarNov.executeUpdate();  
		} 
		catch (SQLException e)
		{
			logger.error("Error Actualizando Hoja Obstetrica: "+e.toString());
			return 0;  //-No Actualizo los cambios en Observaciones Generales 
		}
	}
	
	
	/**
	 * Modifica el detalle embarazo gineco-obstétrico, para ingresar los valores correpondientes
	 * a fecha terminación, motivo finalización y eg finalizar  
	 * @param con -> Connection
	 * @param numeroEmbarazo -> int
	 * @param paciente -> int
	 * @param fechaTerminacion -> String
	 * @param motivoFinalizacion -> int
	 * @param otroMotivoFinalizacion -> String
	 * @param egFinalizar -> int
	 * @param legrado
	 * @param tiempoRuptura
	 * @param duracion
	 * @return
	 */
	public static int modificarAntecedenteGinecoEmbarazo(Connection con, int numeroEmbarazo, int paciente, String fechaTerminacion, int motivoFinalizacion, String otroMotivoFinalizacion, int egFinalizar, String duracion, String tiempoRuptura, String legrado)
		{
		int resp=1;	
		try{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAntGinecoEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(fechaTerminacion.equals(""))
				{
					ps.setString(1, null);
				}
				else
				{
					fechaTerminacion=UtilidadFecha.conversionFormatoFechaABD(fechaTerminacion);
					ps.setString(1, fechaTerminacion);
				}
				
				ps.setInt(2, motivoFinalizacion);
				if(motivoFinalizacion!=0)
					ps.setString(3,null);
				else
					ps.setString(3,otroMotivoFinalizacion);
				ps.setInt(4, egFinalizar);
				if(duracion!=null && !duracion.equals(""))
				{
					ps.setString(5, duracion);
				}
				else
				{
					ps.setNull(5, Types.VARCHAR);
				}
				if(tiempoRuptura!=null && !tiempoRuptura.equals(""))
				{
					ps.setString(6, tiempoRuptura);
				}
				else
				{
					ps.setNull(6, Types.VARCHAR);
				}
				if(legrado!=null && !legrado.equals(""))
				{
					ps.setString(7, legrado);
				}
				else
				{
					ps.setNull(7, Types.VARCHAR);
				}
				ps.setInt(8,numeroEmbarazo);
				ps.setInt(9,paciente);
			
				ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualización de datos en ant_gineco_embarazo: SqlBaseHojaObstetricaDao "+e.toString());
			resp=0;			
		}	
		return resp;	
		}	
	
	/**
	 * Método para insertar el historico de antecedentes gineco-obstétrico con la fur
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente  => int
	 * @param fur -> String
	  * @return
	 **/
	public static int insertarAntecedenteGinecoHisto(Connection con, int codigoPaciente, String fur)
	{
		int resp=0;
		try
			{
										
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedenteGinecoHistoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoPaciente);
					ps.setInt(2, codigoPaciente);
					ps.setInt(3, -1);
					ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(fur));				
										
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del historico de antecedentes gineco-obstétrico: SqlBaseHojaObstetricaDao "+e.toString() );
					resp=-1;
			}
			return resp;
		
	}
	
	/**
	 * Modifica la hoja obstétrica cuando ya existe la hoja obstétrica
	  * @param con
	 * @param codigoHoja -> int
	 * @param codigoPaciente -> int
	 * @param embarazo -> int
	 * @param fechaOrden -> String
	 * @param fur -> String
	 * @param fpp -> String
	 * @param fechaUltrasonido -> String
	 * @param fppUltrasonido -> String
	 * @param edadGestacional -> int
	 * @param edadParto -> int
	 * @param finalizacionEmbarazo -> boolean
	 * @param observacionesGrales -> String
	 * @param embarazoPlaneado 
	 * @param embarazoDeseado 
	 * @param talla 
	 * @param peso 
	 
	 * @return
	 */
	public static int actualizarHojaObstetrica (Connection con, int codigoPaciente, int embarazo, String fechaOrden, String fur, String fpp, String fechaUltrasonido, String fppUltrasonido, int edadGestacional,
												int edadParto, boolean finalizacionEmbarazo, String observacionesGrales, String confiable,String vigenteAntitetanica, String dosisAntitetanica, String mesesGestacionAntitetanica, String antirubeola,
												String fechaTerminacion, String motivoFinalizacion, int egFinalizar, int peso, int talla, String embarazoDeseado, String embarazoPlaneado)
		{
		int resp=0;	
		
		try{
			int codigoHoja=0;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT codigo AS codigo FROM hoja_obstetrica WHERE embarazo=? AND paciente=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, embarazo);
			ps.setInt(2, codigoPaciente);
			ResultSetDecorator res=new ResultSetDecorator(ps.executeQuery());
			if(res.next())
			{
				codigoHoja=res.getInt("codigo");
			}
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarHojaObstetricaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, fechaOrden);
			if(fur.equals(""))
			{
				ps.setString(2, null);
			}
			else
			{
				fur=UtilidadFecha.conversionFormatoFechaABD(fur);
				ps.setString(2, fur);
			}
			
			if(fpp.equals(""))
			{
				ps.setString(3, null);
			}
			else
			{
				fpp=UtilidadFecha.conversionFormatoFechaABD(fpp);
				ps.setString(3, fpp);
			}
			
			if(fechaUltrasonido.equals(""))
			{
				ps.setString(4, null);
			}
			else
			{
				fechaUltrasonido=UtilidadFecha.conversionFormatoFechaABD(fechaUltrasonido);
				ps.setString(4, fechaUltrasonido);
			}
		
			if(fppUltrasonido.equals(""))
			{
				ps.setString(5, null);
			}
			else
			{
				fppUltrasonido=UtilidadFecha.conversionFormatoFechaABD(fppUltrasonido);
				ps.setString(5, fppUltrasonido);
			}
			
			if(edadGestacional==0)
				ps.setString(6, null);
			else
				ps.setInt(6, edadGestacional);
			
			if(edadParto==0)
				ps.setString(7, null);
			else
				ps.setInt(7, edadParto);
			
			ps.setBoolean(8, finalizacionEmbarazo);
			if (observacionesGrales.length()>3700)
				observacionesGrales=observacionesGrales.substring(0, 3900);
			ps.setString(9, observacionesGrales);
			ps.setString(10, confiable);
			ps.setString(11, vigenteAntitetanica);
			if(!dosisAntitetanica.equals(""))
				ps.setString(12, dosisAntitetanica);
			else
				ps.setNull(12,Types.VARCHAR);
			
			if(UtilidadCadena.noEsVacio(mesesGestacionAntitetanica))
				ps.setInt(13, Integer.parseInt(mesesGestacionAntitetanica));
			else
				ps.setNull(13, Types.INTEGER);
			
			ps.setString(14, antirubeola);
			
			if(!UtilidadCadena.noEsVacio(fechaTerminacion))
			{
				ps.setString(15, null);
			}
			else
			{
				ps.setString(15, UtilidadFecha.conversionFormatoFechaABD(fechaTerminacion));
			}
			
			if(!UtilidadCadena.noEsVacio(motivoFinalizacion))
			{
				ps.setString(16, null);
			}
			else
			{
				ps.setString(16, motivoFinalizacion);
			}
			
			if(!UtilidadCadena.noEsVacio(egFinalizar+""))
			{
				ps.setInt(17, Types.INTEGER);
			}
			else
			{
				ps.setInt(17, egFinalizar);
			}
			ps.setInt(18,peso);
			ps.setInt(19,talla);
			ps.setString(20,embarazoDeseado);
			ps.setString(21,embarazoPlaneado);
			
			ps.setInt(22, embarazo);
			ps.setInt(23, codigoPaciente);
								
			ps.executeUpdate();
			resp=codigoHoja;
			
			if(finalizacionEmbarazo==true)
			{
				actualizarEmbarazoParaAntecedentes(con, codigoPaciente, embarazo);
			}
				
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualización de datos en la hoja obstétrica: SqlBaseHojaObstetricaDao "+e.toString());
			resp=0;			
		}	
		return resp;	
		}
	
	/**
	 * Método para consultar el historico de los examenes de laboratorio
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return Collection con los examenes de laboratorio realizados al embarazo pasado por parámetro
	 */
	public static Collection consultarHistoricoExamenesLab(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarHistoricoExamenesLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigoEmbarazo);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los examenes de laboratorio: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los dos últimos historicos de los examenes de laboratorio
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return Collection con los dos últimos históricos de los examenes de laboratorio realizados al embarazo pasado por parámetro
	 */
	
	public static Collection consultarUltimosHistoricosExamenesLab(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		int registros=0;
		
		
		try
		{
			PreparedStatementDecorator numRegistrosStatement= new PreparedStatementDecorator(con.prepareStatement(consultaRegistrosExamenesLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			numRegistrosStatement.setInt(1, codigoPaciente);
			numRegistrosStatement.setInt(2, codigoEmbarazo);
			ResultSetDecorator resultado=new ResultSetDecorator(numRegistrosStatement.executeQuery());
			if(resultado.next())
			{
				registros=resultado.getInt("registros");
			}		
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del número de registros del historico de exámenes de laboratorio: SqlBaseHojaObstetricaDao "+e.toString());
		}
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarUltimosHistoricosExamenesLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigoEmbarazo);
			pst.setInt(3, registros);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los dos últimos históricos de los examenes de laboratorio: "+e);
			return null;
		}
	}
	

	private static void actualizarEmbarazoParaAntecedentes(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(actualizarEmbarazoFinalizadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoPaciente);
			stm.setInt(2, codigoEmbarazo);
			stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error actualizando el embarazo : "+e);
		}
	}
	
	/**
	 * Método para consultar el resumen gestacional de una hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> true si es ascendente o false si es descendente
	 * @return Collection con el valor, fecha, hora, edadgestacional del resumen gestacional
	 */
	public static Collection consultarResumenGestacional(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden)
	{
		/** 
		 *Sentencia para consultar el resumen gestacional de una hoja obstétrica
		 */
		String consultarResumenGestacionalStr=
									"SELECT " +
										"dr.tipo_resumen_gest AS tipo_resumen_gest, " +
										"rg.nombre AS nombre_resumen, " +
										"dr.valor AS valor, " +
										"rges.edad_gestacional AS edad_gestacional, " +
										"to_char(rges.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, " +
										"rges.hora AS hora, " +
										"rges.codigo AS codigo_resumen, " +
										"rges.datos_medico AS datos_medico " +
									"FROM " +
										"hoja_obstetrica h " +
									"INNER JOIN " +
										"resumen_gestacional rges " +
											"ON (rges.hoja_obstetrica=h.codigo) " +
									"INNER JOIN " +
										"detalle_resumen_gest dr " +
											"ON (dr.codigo_resumen_gest=rges.codigo) " +
									"INNER JOIN " +
										"resumen_gest_institucion rgins " +
											"ON (rgins.codigo=dr.tipo_resumen_gest) " +
									"INNER JOIN " +
										"tipos_resumen_gestacional rg " +
											"ON(rg.codigo=rgins.tipo_resumen_gestacional) " +
									"WHERE " +
										"h.paciente=? " +
										"AND embarazo=? " +
										"AND rgins.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
									"ORDER BY rges.codigo";
		if(!orden)
			consultarResumenGestacionalStr+=" desc";
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarResumenGestacionalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigoEmbarazo);
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el resumen gestacional: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los tipos de resumen gestacional parametrizados a la institucion
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del resumen gestacional
	 */
	public static Collection consultarTiposResumenGestacional (Connection con, int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarTiposResumenGestacionalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los tipo de resumen gestacional parametrizados a la institución: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los exámenes de laboratorio de una hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> boolean true si ordenamiento ascendente o false si es descendente
	 * @param consulta -> Identica la consulta a ejecutar 1=Consulta con todos los históricos
	 * 					2= Histórico de los exámenes parametrizados,  3=Histórico nuevos exámenes de laboratorio
	
	 * @return Collection con el resultado, observación, fecha, hora, edadgestacional de los exámenes de laboratorioa
	 */
	public static Collection consultarExamenesLaboratorio(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden, int consulta)
	{
		String consultarExamenesLaboratorioStr="";
		/** 
		 *Sentencia para consultar los exámenes de laboratorio de una hoja obstétrica
		 */
		//String consultarExamenesLaboratorioStr="SELECT  del.tipo_examen_lab AS tipo_examen_lab, del.codigo AS detalle_examen_lab, del.resultado AS resultado, del.observaciones, hel.edad_gestacional AS edad_gestacional, to_char(hel.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, to_char(hel.hora,'hh24:mi') AS hora, hel.codigo AS codigo_histo_examen, getNumeroAdjuntosHistoExamen(hel.codigo) AS num_adjuntos, hel.datos_medico AS datos_medico FROM hoja_obstetrica h INNER JOIN historico_examenes_lab hel ON (hel.hoja_obstetrica=h.codigo) INNER JOIN detalle_examen_lab del ON (del.codigo_hist_examen_lab=hel.codigo) WHERE h.paciente=? AND embarazo=? ORDER BY hel.codigo";
		
		if (consulta == 1)
			{
			consultarExamenesLaboratorioStr="SELECT * FROM " +
																					  "(SELECT  del.tipo_examen_lab AS tipo_examen_lab, '' AS descripcion_otro, del.codigo AS detalle_examen_lab, del.resultado AS resultado, del.observaciones AS observaciones, hel.edad_gestacional AS edad_gestacional, to_char(hel.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, hel.hora AS hora, hel.codigo AS codigo_histo_examen, hel.datos_medico AS datos_medico " +
																					  "FROM hoja_obstetrica h INNER JOIN historico_examenes_lab hel ON (hel.hoja_obstetrica=h.codigo)" +
																					  " INNER JOIN detalle_examen_lab del ON (del.codigo_hist_examen_lab=hel.codigo) " +
																					  "WHERE h.paciente=? AND embarazo=? " +
																					  "UNION " +
																					  "SELECT -1 AS tipo_examen_lab, deo.descripcion_otro AS descripcion_otro, deo.codigo AS detalle_examen_lab, deo.resultado AS resultado, deo.observaciones, hel.edad_gestacional AS edad_gestacional, to_char(hel.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, hel.hora AS hora, hel.codigo AS codigo_histo_examen, hel.datos_medico AS datos_medico " +
																					  "FROM hoja_obstetrica h INNER JOIN historico_examenes_lab hel ON (hel.hoja_obstetrica=h.codigo) " +
																					  "INNER JOIN detalle_otro_examen_lab deo ON (deo.codigo_hist_examen_lab=hel.codigo) " +
																					  "WHERE h.paciente=? AND embarazo=?)x " +
																					  "ORDER BY x.codigo_histo_examen";
		}
		if (consulta == 2)
			{
			consultarExamenesLaboratorioStr="SELECT  del.tipo_examen_lab AS tipo_examen_lab, del.codigo AS detalle_examen_lab, del.resultado AS resultado, del.observaciones, hel.edad_gestacional AS edad_gestacional, to_char(hel.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, hel.hora AS hora, hel.codigo AS codigo_histo_examen, getNumeroAdjuntosHistoExamen(hel.codigo) AS num_adjuntos, hel.datos_medico AS datos_medico " +
																				" FROM hoja_obstetrica h INNER JOIN historico_examenes_lab hel ON (hel.hoja_obstetrica=h.codigo) " +
																				"INNER JOIN detalle_examen_lab del ON (del.codigo_hist_examen_lab=hel.codigo) " +
																				"WHERE h.paciente=? AND embarazo=? ORDER BY hel.codigo";
			}
		if (consulta == 3)
			{
				consultarExamenesLaboratorioStr = "SELECT -1 AS tipo_examen_lab, deo.descripcion_otro AS descripcion_otro, deo.codigo AS detalle_examen_lab, deo.resultado AS resultado, deo.observaciones, hel.edad_gestacional AS edad_gestacional, to_char(hel.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, hel.hora AS hora, hel.codigo AS codigo_histo_examen, hel.datos_medico AS datos_medico " +
																					  "FROM hoja_obstetrica h INNER JOIN historico_examenes_lab hel ON (hel.hoja_obstetrica=h.codigo) " +
																					  "INNER JOIN detalle_otro_examen_lab deo ON (deo.codigo_hist_examen_lab=hel.codigo) " +
																					  "WHERE h.paciente=? AND embarazo=? ORDER BY hel.codigo";
			}
		
		if(!orden)
			consultarExamenesLaboratorioStr+=" desc";
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarExamenesLaboratorioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigoEmbarazo);
			if (consulta == 1)
				{
				pst.setInt(3, codigoPaciente);
				pst.setInt(4, codigoEmbarazo);
				}
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los exámenes de laboratorio de la hoja obstétrica: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los tipos de exámenes de laboratorio
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del exámen de laboratorio
	 */
	public static Collection consultarTiposExamenLaboratorio (Connection con, int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarTiposExamenLaboratorioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los tipo de exámen de laboratorio: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar los tipos de ultrasonido parametrizados por institución
	 * @param con
	 * @param institucion
	 * @return Collection con codigo y nombre del tipo de ultrasonido
	 */
	public static Collection consultarTiposUltrasonido (Connection con, int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarTiposUltrasonidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los tipos de ultrasonido por institución: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar el histórico de los ultrasonidos
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @param orden -> boolean true si ordenamiento ascendente o false si es descendente
	 * @return Collection con el tipo_ultrasonido, valor, fecha, hora, codigo_histo_ultrasonido 
	 */
	public static Collection consultarHistoricoUltrasonidos(Connection con, int codigoPaciente, int codigoEmbarazo, boolean orden)
	{
		/** 
		 *Sentencia para consultar el histórico de los ultrasonidos
		 */
		String consultarHistoricoUltrasonidosStr="SELECT  dul.ultrasonido_institucion AS tipo_ultrasonido, tul.nombre AS nom_tipo_ultrasonido, dul.valor AS valor, to_char(hul.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, hul.hora AS hora, hul.codigo AS codigo_histo_ultrasonido, hul.datos_medico AS datos_medico FROM hoja_obstetrica h INNER JOIN historico_ultrasonidos hul ON (hul.hoja_obstetrica=h.codigo) INNER JOIN detalle_ultrasonido dul ON (dul.codigo_histo_ultrasonido=hul.codigo) INNER JOIN ultrasonido_institucion uins ON (uins.codigo=dul.ultrasonido_institucion) INNER JOIN tipos_ultrasonido tul ON (tul.codigo=uins.tipo_ultrasonido) WHERE h.paciente=? AND embarazo=? ORDER BY hul.codigo";
		
		if(!orden)
			consultarHistoricoUltrasonidosStr+=" desc";
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarHistoricoUltrasonidosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			pst.setInt(2, codigoEmbarazo);
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el histórico del ultrasonido en una hoja obstétrica: "+e);
			return null;
		}
	}
	
	/**
	 * Método para actualizar la hoja Obstetrica desdfe la valoración
	 * @param con
	 * @param fur
	 * @param fpp
	 * @param edadGestacional
	 * @param usuario
	 * @param codigoPersona
	 */
	public static int actualizarDatosValoracion(Connection con, String fur, String fpp, String edadGestacional, int codigoPersona)
	{
		String buscarEmbarazo="SELECT codigo AS codigo FROM ant_gineco_embarazo WHERE codigo_paciente=? AND fin_embarazo=false";
		int codigo=0;
		
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseHojaObstetricaDao)"+e1.toString());
		}
		
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(buscarEmbarazo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codigoPersona);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				codigo=resultado.getInt("codigo");
			}
			else
			{
				return 0;
			}
			int res=0;
			if(fur!=null && fpp!=null && (!fur.equals("") || !fpp.equals("")))
			{
				String sentencia="UPDATE hoja_obstetrica SET fur=?, fpp=? WHERE embarazo=? AND paciente=?";
				stm= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				if(!fur.equals(""))
				{
					stm.setString(1, UtilidadFecha.conversionFormatoFechaABD(fur));
				}
				else
				{
					stm.setString(1, "");
				}
				if(!fpp.equals(""))
				{
					stm.setString(2, UtilidadFecha.conversionFormatoFechaABD(fpp));
				}
				else
				{
					stm.setString(2, "");
				}
				stm.setInt(3, codigo);
				stm.setInt(4, codigoPersona);
				res=stm.executeUpdate();
			}
			if(edadGestacional!="")
			{
				String sentencia="UPDATE ant_gineco_embarazo SET meses_gestacion=? WHERE codigo_paciente=? AND codigo=?";
				stm= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				stm.setString(1, edadGestacional);
				stm.setInt(2, codigoPersona);
				stm.setInt(3, codigo);
				res+=stm.executeUpdate();
			}
			return res;
		}
		catch (SQLException e)
		{
			logger.error("Error buscando el embarazo actual : "+e);
			return 0;
		} 
	}
	
	/**
	 * Método para insertar el detalle del exámen de laboratorio
	 * @param con => conexion
	 * @param secuencia => String
	 * @param codigoHistoricoExamenLab => int
	 * @param descripcionOtro => String
	 * @param resultadoExamenLab => String
	 * @param observacionExamenLab => String
	 * 
	 * @return codigoDetalleOtroExamenLab
	 */
	public static int insertarDetalleOtroExamenLab(Connection con, String secuencia,  int codigoHistoricoExamenLab, String descripcionOtro, String resultadoExamenLab, String observacionExamenLab)
	{
		int codigoDetalleOtroExamenLab=0;
		try
			{
														
					//Se obtiene el còdigo de la secuencia
					codigoDetalleOtroExamenLab=obtenerCodigoSecuencia(con, secuencia);
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleOtroExamenLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoDetalleOtroExamenLab);
					ps.setInt(2, codigoHistoricoExamenLab);
					ps.setString(3, descripcionOtro);
					ps.setString(4, resultadoExamenLab);
					ps.setString(5, observacionExamenLab);
										
					ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del detalle de otro exámen de laboratorio: SqlBaseHojaObstetricaDao "+e.toString() );
					codigoDetalleOtroExamenLab=0;
			}
			return codigoDetalleOtroExamenLab;
	}
	
	/**
	 * Método para insertar el documento adjunto de otro exámen de laboratorio
	 * @param con
	 * @param codigoDetalleOtroExamenLab => int
	 * @param nombreOriginal => String
	 * @param nombreArchivo => String
	 * @return 
	 */
	
	public static int insertarAdjuntoOtroExamenLab(Connection con, int codigoDetalleOtroExamenLab, String nombreOriginal, String nombreArchivo)
	{
		int resp=1;
		try
			{
					
					PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarAdjuntoOtroExamenLabStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoDetalleOtroExamenLab);
					ps.setString(2, nombreOriginal);
					ps.setString(3, nombreArchivo);
										
					resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del archivo adjunto de otro exámen de laboratorio: SqlBaseHojaObstetricaDao "+e.toString() );
					resp=0;
			}
			return resp;
	}
	
	/**
	 * Método que consulta los tipos de plan manejo parametrizados para la institución, y también los
	 * otros planes de menejo de tipo medicamento que hayan sido ingresados por la opción ingresar otro
	 * @param con
	 * @param institucion
	 * @param codigoPersona
	 * @param numeroEmbarazo
	 * @return
	 */
	public static HashMap consultarTiposPlanManejoInstitucion(Connection con, int institucion, int codigoPaciente, int numeroEmbarazo)
	{
		String consultaStr="SELECT * FROM " +
						   "		(" +
						   "	     SELECT pmi.codigo AS codigo, tpm.descripcion AS nombre_plan_manejo,tpm.seccion_plan_manejo AS seccion, 0 AS es_otro " + 
						   "	 		FROM plan_manejo_institucion pmi  " +
						   "			    	INNER JOIN tipo_plan_manejo tpm ON (pmi.tipo_plan_manejo=tpm.codigo) " +
						   "						WHERE pmi.institucion="+institucion+" AND pmi.activo='"+ConstantesBD.acronimoSi+"'"+
						   "		 UNION ALL " +
						   "		 SELECT dopm.otro_plan_manejo AS codigo, topm.descripcion AS nombre_plan_manejo,2 AS seccion, 1 AS es_otro " + 
						   "			FROM detalle_otro_plan_manejo dopm " + 
						   "				INNER JOIN historico_plan_manejo hpm ON (hpm.codigo=dopm.histo_plan_manejo) " +
						   "				INNER JOIN hoja_obstetrica ho ON (ho.codigo=hpm.hoja_obstetrica) " +
						   "				INNER JOIN tipo_otro_plan_manejo topm ON (dopm.otro_plan_manejo=topm.codigo)" +
						   "					WHERE ho.paciente="+codigoPaciente+" AND ho.embarazo="+numeroEmbarazo+
						   "						GROUP BY dopm.otro_plan_manejo,topm.descripcion "+
						   "		)x " +
						   "		ORDER BY x.seccion";

		try
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		ps.close();
		return mapaRetorno;	
		}
		catch (SQLException e)
		{
		logger.error("Error en consultarTiposPlanManejoInstitucion en SqlBaseHojaObstetricaDao"+e.toString());
		return null;
		}
	}
	
	/**
	 * Metodo que inserta el historico de plan de manejo
	 * @param con
	 * @param codigoHojaObstetrica
	 * @param datosMedico
	 * @return
	 */
	public static int insertarHistoricoPlanManejo(Connection con, int codigoHojaObstetrica, String datosMedico)
	{
		int codigoHistoPlanManejo=0;
		
		String insertarHistoricoPlanManejoStr="INSERT INTO historico_plan_manejo " +
											   "	(codigo, hoja_obstetrica, fecha, hora, datos_medico) " +
											   "		VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";
		
		
		try
			{
				//Se obtiene el código de la secuencia del historico de plan manejo
				codigoHistoPlanManejo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_historico_plan_manejo");
								
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarHistoricoPlanManejoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoHistoPlanManejo);
				ps.setInt(2, codigoHojaObstetrica);
				ps.setString(3, datosMedico);
																	
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos del histórico de Plan de Manejo: SqlBaseHojaObstetricaDao "+e.toString() );
					codigoHistoPlanManejo=0;
			}
			return codigoHistoPlanManejo;
	}
	
	/**
	 * Método que inserta el detalle del tipo de plan de manejo, ya sea parametrizado o otro, dependiendo
	 * del valor del parametro esOto
	 * @param con
	 * @param codHistoPlanManejo
	 * @param codTipoPlanManejo
	 * @param presenta
	 * @param observacion
	 * @param esOtro -> Indica si se va a insertar un plan de manejo parametrizado o otro
	 * @return
	 */
	public static int insertarDetallePlanManejo(Connection con, int codHistoPlanManejo, int codTipoPlanManejo, String presenta, String observacion, boolean esOtro)
	{
		PreparedStatementDecorator ps;
		int resp=0;
	
		String insertarStr = "";
		
		try	{
			//Verificar si es Otro plan de manejo o un parametrizado 
			if (esOtro)
			{
				//-Para Insertar En detalle de otro plan de manejo
				insertarStr = "INSERT INTO detalle_otro_plan_manejo  (histo_plan_manejo, " +
																	  "otro_plan_manejo, " +
																	  "presenta," +
																	  "observaciones) " +
												  "	VALUES (?, ?, ?, ?) ";
			}
			else
			{
				//-Para Insertar el detalle del plan de manejo parametrizado
				insertarStr = "INSERT INTO detalle_plan_manejo  (histo_plan_manejo, " +
															    "plan_manejo_inst, " +
																"presenta," +
																"observaciones) " +
												  "	VALUES (?, ?, ?, ?) ";
			}
				

				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setInt(1, codHistoPlanManejo);
				ps.setInt(2, codTipoPlanManejo);
				
				if (!UtilidadCadena.noEsVacio(presenta) ) 
					ps.setString(3,null);
				else 
					ps.setString(3, presenta);			
			
				ps.setString(4,observacion);
				
						
			//-----Ejecutar la insercion 
			resp = ps.executeUpdate();			
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en insertarDetallePlanManejo : SqlBaseHojaObstetricaDao "+e.toString() );
				resp = 0;
		}
		return resp;
	}
	
	/**
	 * Se inserta el nombre del nuevo tipo de plan de manejo 
	 * @param con
	 * @param descripcionOtro
	 * @return
	 */
	public static int insertarOtroPlanManejo(Connection con, String descripcionOtro)
	{
		int codigo=0;
		
		String insertarHistoricoPlanManejoStr="INSERT INTO tipo_otro_plan_manejo " +
											   "	(codigo, descripcion) " +
											   "		VALUES (?, ?)";
		
		
		try
			{
				//Se obtiene el código de la secuencia de los planes de manejo parametrizados por institucion
				codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_plan_manejo_institucion");
								
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarHistoricoPlanManejoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigo);
				ps.setString(2, descripcionOtro);
																	
				ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción del nuevo tipo de plan de manejo: SqlBaseHojaObstetricaDao "+e.toString() );
					codigo=0;
			}
			return codigo;
	}
	
	/**
	 * Metodo que consulta el historico de plan manejo tanto de los tipos parametrizados
	 * por institucion como los otros nuevos ingresados
	 * @param con
	 * @param codigoPaciente
	 * @parma nroEmbarazo
	 * @return
	 */
	public static HashMap consultaHistoricoPlanManejo(Connection con, int codigoPaciente, int nroEmbarazo)
	{
		String consultaStr="SELECT * FROM " +
						   "			( " +
						   "			SELECT " +
						   "			dpm.histo_plan_manejo AS codigo_histo, " +
						   "			dpm.plan_manejo_inst AS codigo_tipo, " +
						   "			dpm.presenta AS presenta, " +
						   "			dpm.observaciones AS observaciones, " + 
						   "			to_char(hpm.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, " +
						   "			hpm.hora AS hora," +
						   "			hpm.datos_medico " +
						   "				FROM detalle_plan_manejo dpm " +
						   "						INNER JOIN historico_plan_manejo hpm ON (dpm.histo_plan_manejo=hpm.codigo) " +
						   "						INNER JOIN hoja_obstetrica ho ON (ho.codigo=hpm.hoja_obstetrica) " +
						   "							WHERE ho.paciente="+codigoPaciente+" AND ho.embarazo="+nroEmbarazo+
						   "			UNION ALL " +
						   "			SELECT " +
						   "			dopm.histo_plan_manejo AS codigo_histo, " +
						   "			dopm.otro_plan_manejo AS codigo_tipo, " +
						   "			dopm.presenta AS presenta," +
						   "			dopm.observaciones AS observaciones," +
						   "			to_char(hpm.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, " +
						   "			hpm.hora AS hora," +
						   "			hpm.datos_medico " +
						   "				FROM detalle_otro_plan_manejo dopm " +
						   "						INNER JOIN historico_plan_manejo hpm ON (dopm.histo_plan_manejo=hpm.codigo) " +
						   "						INNER JOIN hoja_obstetrica ho ON (ho.codigo=hpm.hoja_obstetrica) " +
						   "							WHERE ho.paciente="+codigoPaciente+" AND ho.embarazo="+nroEmbarazo+
						   "			)x " +
						   "				ORDER BY x.codigo_histo DESC";

		try
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		ps.close();
		return mapaRetorno;	
		}
		catch (SQLException e)
		{
		logger.error("Error en consultaHistoricoPlanManejo en SqlBaseHojaObstetricaDao"+e.toString());
		return null;
		}
	}

}
