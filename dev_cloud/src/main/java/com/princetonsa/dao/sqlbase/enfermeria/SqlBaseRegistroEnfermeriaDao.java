/*
 * Creado en Feb 17, 2006
 */
package com.princetonsa.dao.sqlbase.enfermeria;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesValoresPorDefecto;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.enfermeria.DtoRegistroAlertaOrdenesMedicas;
import com.princetonsa.dto.manejoPaciente.DtoHistoricoValoracionEnfermeria;
import com.princetonsa.dto.manejoPaciente.DtoResultadoLaboratorio;
import com.princetonsa.dto.manejoPaciente.DtoValoracionEnfermeria;
import com.princetonsa.mundo.CuentasPaciente;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class SqlBaseRegistroEnfermeriaDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseRegistroEnfermeriaDao.class);
	
															
	private static final String ingresarNanda="INSERT INTO diagnostico_enfermeria(codigo_histo_enca, diag_nanda_inst, observacion) VALUES(?,?,?)";
	
	/**
	 * Consulta de soporte respiratorio ingresado en ordenes médicas
	 */
	private static final String consultarSoporteOrdenStr="SELECT eom.codigo AS codigo_encabezado, to_char(eom.fecha_orden, 'dd/mm/yyyy') || ' - ' || hora_orden AS fecha_orden, CASE WHEN ee.codigo IS NULL THEN -1 ELSE ee.codigo END AS codigo_equipo, CASE WHEN ee.descripcion IS NULL THEN '' ELSE ee.descripcion END AS equipo, osr.cantidad AS cantidad, getDatosMedico(eom.login) AS medico, CASE WHEN osr.oxigeno_terapia IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE  osr.oxigeno_terapia END AS oxigeno_terapia, to_char(eom.fecha_orden, 'yyyy-mm-dd') || '-' || eom.hora_orden  AS fecha_grabacion FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eom ON(eom.orden_medica=om.codigo) INNER JOIN orden_soporte_respira osr ON(osr.codigo_histo_enca=eom.codigo) LEFT OUTER JOIN equipo_elemento_cc_inst eecci ON(eecci.codigo=osr.equipo_elemento_cc_inst) LEFT OUTER JOIN equipo_elemento ee ON(ee.codigo=eecci.equipo_elemento)  ";
	
	private static final String consultarAntSoporteOrdenStr="SELECT eom.codigo AS codigo_encabezado, to_char(eom.fecha_orden, 'dd/mm/yyyy') || ' - ' || hora_orden  AS fecha_orden, CASE WHEN ee.codigo IS NULL THEN -1 ELSE ee.codigo END AS codigo_equipo, CASE WHEN ee.descripcion IS NULL THEN '' ELSE ee.descripcion END AS equipo, osr.cantidad AS cantidad, getDatosMedico(eom.login) AS medico, CASE WHEN osr.oxigeno_terapia IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE  osr.oxigeno_terapia END AS oxigeno_terapia, to_char(eom.fecha_orden, 'yyyy-mm-dd') || '-' || eom.hora_orden AS fecha_grabacion FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eom ON(eom.orden_medica=om.codigo) INNER JOIN orden_soporte_respira osr ON(osr.codigo_histo_enca=eom.codigo) LEFT OUTER JOIN equipo_elemento_cc_inst eecci ON(eecci.codigo=osr.equipo_elemento_cc_inst) LEFT OUTER JOIN equipo_elemento ee ON(ee.codigo=eecci.equipo_elemento) ";

	/**
	 * Consulta del último soporte respiratorio ingresado en ordenes médicas
	 */
	private static final String consultarSoporteOrdenUltimoStr="SELECT eom.codigo AS codigo_encabezado, to_char(eom.fecha_orden, 'dd/mm/yyyy') || ' - ' || hora_orden AS fecha_orden, CASE WHEN ee.codigo IS NULL THEN -1 ELSE ee.codigo END AS codigo_equipo,CASE WHEN ee.descripcion IS NULL THEN '' ELSE ee.descripcion END  AS equipo, osr.cantidad AS cantidad, getDatosMedico(eom.login) AS medico, CASE WHEN osr.oxigeno_terapia IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE  osr.oxigeno_terapia END AS oxigeno_terapia, to_char(eom.fecha_orden, 'yyyy-mm-dd') || '-' || eom.hora_orden AS fecha_grabacion FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eom ON(eom.orden_medica=om.codigo) INNER JOIN orden_soporte_respira osr ON(osr.codigo_histo_enca=eom.codigo) LEFT OUTER JOIN equipo_elemento_cc_inst eecci ON(eecci.codigo=osr.equipo_elemento_cc_inst) LEFT OUTER JOIN equipo_elemento ee ON(ee.codigo=eecci.equipo_elemento) WHERE om.cuenta IN("+ConstantesBD.separadorSplit+") AND eom.codigo=(SELECT max(eom.codigo) FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eom ON(eom.orden_medica=om.codigo) INNER JOIN orden_soporte_respira osr ON(osr.codigo_histo_enca=eom.codigo) WHERE om.cuenta IN("+ConstantesBD.separadorSplit+"))";

	/**
	 * En el caso en el que esxisten registros de enfermería,
	 * se debe buscar el encabezado de la orden médica anterior para armar
	 * el histórico con el encabezado correcto
	 */
	private static final String consultarFechaPrimerHistoricoStr="SELECT max(to_char(eom.fecha_orden, 'yyyy-mm-dd') || '-' || eom.hora_orden) AS fecha_orden FROM ordenes_medicas om INNER JOIN encabezado_histo_orden_m eom ON(eom.orden_medica=om.codigo) WHERE om.cuenta IN(?,?) AND to_char(eom.fecha_orden, 'yyyy-mm-dd') || '-' || eom.hora_orden < ?"; 
	
	/**
	 * Sentencia para consultar los ingresos de soporte respiratorio
	 * por las enfermeras
	 */
	private static final String consultarHistoricoSoporteEnferStr="SELECT cuenta, codigo_encabezado, " +
																  "		  to_char(fecha_registro, 'dd/mm/yyyy') || ' - ' || hora_registro AS fecha_registro, " +
																  "		  to_char(fecha_grabacion, 'dd/mm/yyyy') || ' - ' || hora_grabacion AS fecha_insercion, " +
																  "		  valor, soporte, tipo_soporte, observaciones, es_valor, medico, " +
																  "		  to_char(fecha_registro, 'yyyy-mm-dd') || '-' || hora_registro AS fecha_grabacion," +
																  "		  profesional_salud	" +
																  "		  FROM (" +
																  "				SELECT reg.cuenta AS cuenta, enc.codigo AS codigo_encabezado, " +
																  "					   enc.fecha_registro AS fecha_registro, enc.hora_registro AS hora_registro, " +
																  "					   enc.fecha_grabacion AS fecha_grabacion, enc.hora_grabacion AS hora_grabacion, " +
																  "					   val.valor AS valor, val.soporte_respira_cc_inst AS soporte," +
																  "					   enc.obs_soporte AS observaciones, "+ValoresPorDefecto.getValorTrueParaConsultas()+" AS es_valor, " +
																  "					   enc.datos_medico AS medico, srcci.tipo_soporte_respira AS tipo_soporte, getDatosMedicoEspecialidades(usuario,', ') as profesional_salud " +
																  "					   FROM enca_histo_registro_enfer enc " +
																  "							INNER JOIN soporte_resp_enfer_valor val ON(val.codigo_histo_enfer=codigo) " +
																  "							INNER JOIN registro_enfermeria reg ON(reg.codigo=enc.registro_enfer) " +
																  "							INNER JOIN soporte_respira_cc_inst srcci ON(val.soporte_respira_cc_inst=srcci.codigo) " +
																  "				UNION " +
																  "				SELECT reg.cuenta AS cuenta, enc.codigo AS codigo_encabezado, 						 " +
																  "					   fecha_registro AS fecha_registro, hora_registro AS hora_registro,			 " +
																  "					   enc.fecha_grabacion AS fecha_grabacion, enc.hora_grabacion AS hora_grabacion, " +
																  "					   to_char(op.opcion_soporte_respira,'99') AS valor,  soporte_respira_cc_inst AS soporte, " +
																  "					   enc.obs_soporte AS observaciones, " +ValoresPorDefecto.getValorFalseParaConsultas()+" AS es_valor, " +
																  "					   enc.datos_medico AS medico, srcci.tipo_soporte_respira AS tipo_soporte, getDatosMedicoEspecialidades(usuario,', ') as profesional_salud " +
																  "					   FROM enca_histo_registro_enfer enc " +
																  "							INNER JOIN soporte_resp_enfer_op op ON(op.codigo_histo_enfer=codigo) " +
																  "							INNER JOIN registro_enfermeria reg ON(reg.codigo=enc.registro_enfer) " +
																  "							INNER JOIN soporte_respira_cc_inst srcci ON(op.soporte_respira_cc_inst=srcci.codigo)) opciones " +
																  "								 WHERE cuenta IN("+ConstantesBD.separadorSplit+") AND fecha_registro || '-' || hora_registro > ?";
	
	/**
	 * Sentencia para ingresar valores de soporte respiratorio
	 */
	private static final String ingresarSoporteRespiratorioValStr="INSERT INTO soporte_resp_enfer_valor (codigo_histo_enfer, soporte_respira_cc_inst, valor) VALUES(?,?,?)";

	/**
	 * Sentencia para ingresar valores de soporte respiratorio
	 */
	private static final String ingresarSoporteRespiratorioOpStr="INSERT INTO soporte_resp_enfer_op (codigo_histo_enfer, soporte_respira_cc_inst, opcion_soporte_respira) VALUES(?,?,?)";

	/**
	 * Consultar las fechas en las cuales hay registros de soporte respiratorio
	 */
	private static final String consultarFechasHistoricoSoporteStr="SELECT to_char(enc.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') AS fecha_registro FROM (SELECT codigo_histo_enfer FROM soporte_resp_enfer_op UNION SELECT codigo_histo_enfer FROM soporte_resp_enfer_valor ) sop INNER JOIN enca_histo_registro_enfer enc ON(enc.codigo=sop.codigo_histo_enfer) INNER JOIN registro_enfermeria reg ON(enc.registro_enfer=reg.codigo) WHERE reg.cuenta IN(?) GROUP BY fecha_registro ORDER BY enc.fecha_registro DESC";
	/**
	 * Consultar las fechas en las cuales hay NO registros de soporte respiratorio
	 * pero si hay registros en ORDEN MÉDICA soporte respiratorio
	 */
	private static final String consultarFechasHistoricoSoporteOrdenStr=" SELECT to_char(fecha_orden, '"+ConstantesBD.formatoFechaBD+"') AS fecha_registro FROM orden_soporte_respira o INNER JOIN encabezado_histo_orden_m enc ON(enc.codigo=o.codigo_histo_enca)INNER JOIN ordenes_medicas om ON(om.codigo=enc.orden_medica) WHERE om.cuenta IN(?) GROUP BY enc.fecha_orden ORDER BY enc.fecha_orden DESC";

	/**
	 * Consultar las fechas en las cuales hay registros de Diagnósticos de Enfermería
	 */
	private static final String consultarFechasHistoricoNandaStr="SELECT " +
									"to_char(enc.fecha_registro, 'YYYY-MM-DD') AS fecha_registro " +
								"FROM " +
									"enfermeria.diagnostico_enfermeria diag " +
								"INNER JOIN " +
									"enfermeria.enca_histo_registro_enfer enc " +
										"ON(enc.codigo=diag.codigo_histo_enca) " +
								"INNER JOIN " +
									"enfermeria.registro_enfermeria reg " +
										"ON(enc.registro_enfer=reg.codigo) " +
								"WHERE " +
									"reg.cuenta IN (?) " +
								"GROUP BY " +
									"enc.fecha_registro ORDER BY enc.fecha_registro DESC";

	/**
	 * Método para consultar los históricos de
	 * cuidados de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return Collection con los registros consultados
	 */
	public static HashMap consultarCuidadosEnfermeria(Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		
		try
		{
			StringBuffer consulta=new StringBuffer();
			
			consulta.append("SELECT " +
									" fecha_registro AS fecha_registro, hora_registro AS hora_registro, cuidado AS cuidado, " +
									" presenta AS presenta, observaciones AS observaciones, es_medico AS es_medico, " +
									" es_otro AS es_otro, codigo_enca AS codigo_enca, codigo_tipo AS codigo_tipo, controlespecial as controlespecial " +
							" FROM " +
									" (SELECT " +
												" to_char(om.fecha_orden, 'yyyy-mm-dd') AS fecha_registro, om.hora_orden||'' AS hora_registro, " +
												" ce.cuidado_enfer_cc_inst AS cuidado, ce.presenta AS presenta, ce.descripcion AS observaciones, " +
												" 1 AS es_medico, 0 AS es_otro, om.codigo AS codigo_enca, tce.codigo AS codigo_tipo,tce.control_especial as controlespecial " +
										" FROM ordenes_medicas o " +
										" INNER JOIN encabezado_histo_orden_m om ON(om.orden_medica=o.codigo) " +
										" INNER JOIN detalle_cuidado_enfer ce ON(ce.cod_histo_enca=om.codigo) " +
										" INNER JOIN cuidado_enfer_cc_inst ceci ON (ce.cuidado_enfer_cc_inst=ceci.codigo) " +
										" INNER JOIN tipo_cuidado_enfermeria tce ON (ceci.cuidado_enfermeria=tce.codigo)");
									
										
											consulta.append(" WHERE o.cuenta IN ("+cuentas+") ");
										
										//FILTRO EN RANGO DE FECHAS
										if(UtilidadCadena.noEsVacio(fechaInicio))
											consulta.append(" AND to_char(om.fecha_orden, '"+ConstantesBD.formatoFechaBD+"') || '-' || om.hora_orden >= '"+fechaInicio+"'");
										if(UtilidadCadena.noEsVacio(fechaFin))
											consulta.append(" AND to_char(om.fecha_orden, '"+ConstantesBD.formatoFechaBD+"') || '-' || om.hora_orden < '"+fechaFin+"'");
															
					consulta.append(" UNION " +
									" SELECT " +
												" to_char(ere.fecha_registro, 'yyyy-mm-dd') AS fecha_registro, ere.hora_registro||'' AS hora_registro, " +
												" ce.cuidado_enfer_cc_inst AS cuidado, ce.presenta AS presenta, ce.descripcion AS observaciones, 0 AS es_medico, " +
												" 0 AS es_otro, ere.codigo AS codigo_enca, tce.codigo AS codigo_tipo,tce.control_especial as controlespecial " +
									" FROM registro_enfermeria re " +
									" INNER JOIN enca_histo_registro_enfer ere ON(ere.registro_enfer=re.codigo) " +
									" INNER JOIN detalle_cuidado_reg_enfer ce  ON(ce.codigo_histo_enfer=ere.codigo)  " +
									" INNER JOIN cuidado_enfer_cc_inst ceci ON (ce.cuidado_enfer_cc_inst=ceci.codigo) " +
									" INNER JOIN tipo_cuidado_enfermeria tce ON (ceci.cuidado_enfermeria=tce.codigo)");
									
									
										consulta.append(" WHERE re.cuenta IN ("+cuentas+") ");
									
									//FILTRO EN RANGO DE FECHAS
									if(UtilidadCadena.noEsVacio(fechaInicio))
										consulta.append(" AND to_char(ere.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || ere.hora_registro >= '"+fechaInicio+"'");
									if(UtilidadCadena.noEsVacio(fechaFin))
										consulta.append(" AND to_char(ere.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || ere.hora_registro < '"+fechaFin+"'");
													
					consulta.append(" UNION " +
									" SELECT " +
												" to_char(om.fecha_orden, 'yyyy-mm-dd') AS fecha_registro, om.hora_orden||'' AS hora_registro, " +
												" ce.otro_cual AS cuidado, ce.presenta AS presenta, ce.descripcion AS observaciones, 1 AS es_medico, " +
												" 1 AS es_otro, om.codigo AS codigo_enca, toce.codigo AS codigo_tipo,'N' as controlespecial " +
									" FROM  ordenes_medicas o " +
									" INNER JOIN encabezado_histo_orden_m om ON(om.orden_medica=o.codigo) " +
									" INNER JOIN detalle_otro_cuidado_enf ce ON (ce.cod_histo_cuidado_enfer=om.codigo) " +
									" INNER JOIN tipo_otro_cuidado_enf toce ON (ce.otro_cual=toce.codigo)");
										
									
										consulta.append(" WHERE o.cuenta IN ("+cuentas+") ");
									
									//FILTRO EN RANGO DE FECHAS
									if(UtilidadCadena.noEsVacio(fechaInicio))
										consulta.append(" AND to_char(om.fecha_orden, '"+ConstantesBD.formatoFechaBD+"') || '-' || om.hora_orden >= '"+fechaInicio+"'");
									if(UtilidadCadena.noEsVacio(fechaFin))
										consulta.append(" AND to_char(om.fecha_orden, '"+ConstantesBD.formatoFechaBD+"') || '-' || om.hora_orden < '"+fechaFin+"'");
													
					consulta.append(" UNION " +
									" SELECT " +
												" to_char(ehre.fecha_registro, 'yyyy-mm-dd') AS fecha_registro, ehre.hora_registro||'' AS hora_registro, " +
												" dore.otro_cual AS cuidado, dore.presenta AS presenta, dore.descripcion AS observaciones, 0 AS es_medico, " +
												" 1 AS es_otro, ehre.codigo AS codigo_enca, toce.codigo AS codigo_tipo,'N' as controlespecial " +
									" FROM registro_enfermeria re " +
									" INNER JOIN enca_histo_registro_enfer ehre ON(ehre.registro_enfer=re.codigo) " +
									" INNER JOIN detalle_otro_cuidado_renf dore ON(dore.codigo_histo_enfer=ehre.codigo) " +
									" INNER JOIN tipo_otro_cuidado_enf toce ON (dore.otro_cual=toce.codigo)");
										
									
										consulta.append(" WHERE re.cuenta IN ("+cuentas+") ");
									
									//FILTRO EN RANGO DE FECHAS
									if(UtilidadCadena.noEsVacio(fechaInicio))
										consulta.append(" AND to_char(ehre.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || ehre.hora_registro >= '"+fechaInicio+"'");
									if(UtilidadCadena.noEsVacio(fechaFin))
										consulta.append(" AND to_char(ehre.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || ehre.hora_registro < '"+fechaFin+"'");
									
			consulta.append(") tempo " +
							"ORDER BY controlespecial desc, cuidado");
			
			
			logger.info("consulta -->\n"+consulta);
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando los cuidados especiales de enfermería: "+e);
			return null;
		}
	}
	
	
	
	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return
	 */
	public static Collection consultarFechasHistoricoSoporte(Connection con, String cuentas)
	{
		try
		{
			//logger.info(consultarFechasHistoricoSoporteOrdenStr);
			
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarFechasHistoricoSoporteStr.replace("?", cuentas),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			Collection col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
			if(col.isEmpty())
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(consultarFechasHistoricoSoporteOrdenStr.replace("?", cuentas),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
			}
			return col; 
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el histórico de fechas (Soporte Respiratorio): "+e);
			return null;
		}
	}

	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de diagnósticos de enfermería
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return Colección de fechas
	 */
	public static Collection consultarFechasHistoricoNanda(Connection con, String cuentas)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultarFechasHistoricoNandaStr.replace("?", cuentas),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el histórico de fechas (Diagnosticos Nanda): "+e);
			return null;
		}
	}

	/**
	 * Método para consultar el soporte respiratorio
	 * ingresado en la orden médica
	 * @param cuenta Código de la cuenta del paciente
	 * @param fechaInicio
	 * @param incluirAnterior
	 * @param fechaFin
	 * @param cuentaAsocio @todo
	 * @return HashMap con los datos del sporte respiratorio
	 */
	public static HashMap consultarSoporteOrden(Connection con, String cuentas, String fechaInicio, boolean incluirAnterior, String fechaFin)
	{
		String where =" ";
		String order =" ORDER BY eom.codigo, eom.fecha_grabacion,eom.hora_grabacion";
		
		try
		{
			String nuevaFecha=fechaInicio;
			//si se dice que no incluya el anterior se filtra por cuenta activa
			if(incluirAnterior)
				where =" WHERE om.cuenta IN ("+cuentas+") ";
			else
				where =" WHERE om.cuenta IN ("+cuentas.split(",")[0]+") ";
			
			String consulta=consultarSoporteOrdenStr;
			
			if(UtilidadCadena.noEsVacio(fechaFin))
			{
				consulta=consultarAntSoporteOrdenStr;

				if(UtilidadCadena.noEsVacio(fechaFin))
					where+="  AND to_char(eom.fecha_orden, '"+ConstantesBD.formatoFechaBD+"') || '-' || eom.hora_orden <='"+fechaFin+"'";
			}
			
			consulta+=where+order;
			
			logger.info("\n cuentas [" + cuentas + "] nuevaFecha [" + nuevaFecha + "] fechaFin [" + fechaFin + "]");
			logger.info("\n LA CONSULTA DE SOPORTE [" + consulta + "] \n\n");
			
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			
			//logger.info(consulta+" "+nuevaFecha+" "+fechaFin);
			ResultSetDecorator resTempo=new ResultSetDecorator(stm.executeQuery());
			//logger.info(consultarSoporteOrdenUltimoStr);
			if(UtilidadBD.resultSet2Collection(resTempo).isEmpty())
			{
				stm= new PreparedStatementDecorator(con.prepareStatement(consultarSoporteOrdenUltimoStr.replaceAll(ConstantesBD.separadorSplit, cuentas),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(resultado));
			resultado.close();
			stm.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando el soporte respiratorio ingresado en la orden médica : "+e);
			return null;
		}
	}

	/**
	 * Consultar Histroricos de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param fechaInicio
	 * @param fechaFin @todo
	 * @param cuentaAsocio @todo
	 * @return Mapa con los resultados del histórico
	 */
	public static HashMap consultarHistoricoSoporteEnfer(Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		try
		{
			String consulta=consultarHistoricoSoporteEnferStr;
			consulta=consulta.replace(ConstantesBD.separadorSplit, cuentas);
			if(UtilidadCadena.noEsVacio(fechaFin))
			{
				consulta+=" AND to_char(fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || hora_registro < ?";
			}
			logger.info(consulta+" "+fechaInicio+" "+fechaFin);
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta.replace(ConstantesBD.separadorSplit, cuentas),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setString(1, fechaInicio);
			if(UtilidadCadena.noEsVacio(fechaFin))
			{
				stm.setString(2, fechaFin);
			}
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando el histórico del soporte respiratorio ingresado por las enfermeras: "+e);
			return null;
		}
	}
	
	/**
	 * Método para ingresar los diagnosticos de enfermería
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public static int ingresarDiagnosticosNanda(Connection con, int codEncabezado, Vector diagnosticosEnfermeria)
	{
		try
		{
			//logger.info(diagnosticosEnfermeria.size());
			int numResultados=0;
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(ingresarNanda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, codEncabezado);
			for(int i=0; i<diagnosticosEnfermeria.size();i++)
			{
				Vector fila=(Vector)diagnosticosEnfermeria.get(i);
				stm.setInt(2, Utilidades.convertirAEntero(fila.get(0).toString()));
				stm.setString(3, fila.get(1).toString());
				numResultados+=stm.executeUpdate();
			}
			return numResultados;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los diagnosticos nanda : "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Método para ingresar los datos del soporte respiratorio
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public static int ingresarSoporteRespiratorio(Connection con, int codEncabezado, Vector soporteRespiratorio)
	{
		try 
		{ 
			int numRegistrosIngresados=0;
			PreparedStatementDecorator stmVal= new PreparedStatementDecorator(con.prepareStatement(ingresarSoporteRespiratorioValStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator stmOp= new PreparedStatementDecorator(con.prepareStatement(ingresarSoporteRespiratorioOpStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stmVal.setInt(1, codEncabezado);
			stmOp.setInt(1, codEncabezado);
			//logger.info("Llega hasta aqui!!!!! "+soporteRespiratorio.size());
			for(int i=0; i<soporteRespiratorio.size();i++)
			{
				Vector valor=(Vector)soporteRespiratorio.get(i);
				int codigoElemento=((Integer)valor.get(0)).intValue();
				int tipoDato=((Integer)valor.get(1)).intValue();
				//logger.info("Tipo "+tipoDato);
				//logger.info("codigoElemento "+codigoElemento);
				if(tipoDato==ConstantesBD.codigoTipoDatoLista)
				{
					int opcion=((Integer)valor.get(2)).intValue();
					if(opcion!=0)
					{
						stmOp.setInt(2, codigoElemento);
						stmOp.setInt(3, opcion);
						//logger.info("codigoElemento --> "+codigoElemento);
						//logger.info("opcion "+opcion);
						numRegistrosIngresados+=stmOp.executeUpdate();
					}
				}
				else if(tipoDato==ConstantesBD.codigoTipoDatoTexto)
				{
					String text=(String)valor.get(2);
					stmVal.setInt(2, codigoElemento);
					stmVal.setString(3, text);
					//logger.info("codigoElemento --> "+codigoElemento);
					//logger.info("text "+text);
					numRegistrosIngresados+=stmVal.executeUpdate();
				}
			}
			return numRegistrosIngresados;
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los datos de soporte respiratorio : "+e);
			return 0;
		}
	}

	/**
	 * Método para consultar el historico de los diagnosticos de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio @todo
	 * @param institucion @todo
	 * @param fechaInicio @todo
	 * @param fechaFin @todo
	 * @return
	 */
	public static HashMap consultarDiagnosticosNanda(Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
	{
		logger.info("\n entre a consultarDiagnosticosNanda fecha ini -->"+fechaInicio+"   fecha fin--> "+fechaFin);
		String consulta="SELECT " +
								"d.diag_nanda_inst AS codigo, "+
								"to_char(e.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
								"e.hora_registro||''  AS hora_registro," +
								"e.hora_grabacion||'' AS hora_grabacion," +
								"getNombreDiagNanda(d.diag_nanda_inst, "+institucion+") AS nombre_nanda," +
								"enfermeria.getcodigodiagnanda(d.diag_nanda_inst, "+institucion+") AS codigo_nanda, " +
								"d.observacion"+
							" FROM " +
								"enca_histo_registro_enfer e " +
							" INNER JOIN " +
								"diagnostico_enfermeria d ON (d.codigo_histo_enca=e.codigo) " +
							" INNER JOIN " +
								"registro_enfermeria r ON (e.registro_enfer=r.codigo) " +
							" WHERE " +
								" r.cuenta IN ("+cuentas+")  " +
							" AND " +
								"to_char(e.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || e.hora_registro > '"+fechaInicio+"'"+
							" ORDER BY e.fecha_registro, e.hora_grabacion, enfermeria.getcodigodiagnanda(d.diag_nanda_inst, "+institucion+")";
		try
		{
			if(fechaFin!=null && !fechaFin.trim().equals(""))
			{
				consulta="SELECT " +
								"d.diag_nanda_inst AS codigo, "+
								"to_char(e.fecha_grabacion, 'dd/mm/yyyy') AS fecha_grabacion, " +
								"e.hora_grabacion||'' AS hora_grabacion," +
								"to_char(e.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
								"e.hora_registro||'' AS hora_registro," +
								"e.hora_grabacion||'' AS hora_grabacion," +
								"getNombreDiagNanda(d.diag_nanda_inst, "+institucion+") AS nombre_nanda," +
								"enfermeria.getcodigodiagnanda(d.diag_nanda_inst, "+institucion+") AS codigo_nanda, " +
								"d.observacion, "+
								"getdatosmedicoespecialidades(e.usuario, ',') AS usuario " +
							" FROM " +
								"enca_histo_registro_enfer e " +
							" INNER JOIN " +
								"diagnostico_enfermeria d ON (d.codigo_histo_enca=e.codigo) " +
							" INNER JOIN " +
								"registro_enfermeria r ON (e.registro_enfer=r.codigo) " +
							" WHERE " +
								" r.cuenta IN ("+cuentas+") " +
							" AND " +
								"to_char(e.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || e.hora_registro > '"+fechaInicio+"'"+
							" AND " +
								"to_char(e.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') || '-' || e.hora_registro <= '"+fechaFin+"'"+
							" ORDER BY e.fecha_registro, e.hora_grabacion, enfermeria.getcodigodiagnanda(d.diag_nanda_inst, "+institucion+")";

			}
			logger.info(consulta);
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error consultando los diagnosticos de enfermería : "+e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Metodo que realiza la busqueda de lista de los pacientes que se encuentran en las camas ocupadas 
	 * cuyo centro de costo de la cama corresponde al centro de costo seleccionado.
	 * @param con
	 * @param centroCosto
	 * @return listadoPacientesCentroCosto
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap consultarPacientesPiso(Connection con, HashMap campos)
	{
		//logger.info("Centro costo seleccionado-->"+centroCosto+"\n\n");
		
		//************SE SACAN LOS PARÁMETROS*******************************************************
		//int centroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
		String codigoPiso = campos.get("codigoPiso").toString();
		String codigoHabitacion = campos.get("codigoHabitacion").toString();
		String codigoCama = campos.get("codigoCama").toString();
		boolean pacientesNuevaInformacion = (Boolean)campos.get("pacientesNuevaInformacion");
		
		//***********SE ARMA CONSULTA DE HOSPITALIZACIÓN********************************************
		String consultaHospitalizacion = "SELECT " +
			"cu.id AS nro_cuenta," +
			"CASE WHEN cu.hospital_dia = '"+ConstantesBD.acronimoNo+"' THEN " +
				"getnombrepisocama(cam.codigo) || ' - ' || getnomhabitacioncama(cam.codigo) " +
			"ELSE " +
				"''" +
			"END AS piso_habitacion," +
			"CASE WHEN cu.hospital_dia = '"+ConstantesBD.acronimoNo+"' THEN " +
				"'P:' || substr(getnombrepisocama(cam.codigo),0,8) || ' H:' || substr(getnomhabitacioncama(cam.codigo),0,8) || ' C:' || substr(cam.numero_cama,0,8) " +
			"ELSE " +
				"''" +
			"END AS numero_cama," +
			"coalesce(cam.descripcion,'') AS descripcion_cama," +
			"getNomCentroCosto(cu.area) AS centro_costo,"+
			"per.primer_apellido || ' ' || coalesce(per.segundo_apellido,'') || ' ' || per.primer_nombre || ' ' || coalesce(per.segundo_nombre,'') AS paciente,"+
			"per.tipo_identificacion AS tipo_id," +
			"per.numero_identificacion AS nro_id," +
			"getedad(per.fecha_nacimiento) AS edad, " +
			"getdescripcionsexo(per.sexo) AS sexo, " +
			"to_char(ah.fecha_admision, '"+ConstantesBD.formatoFechaAp+"') AS fecha_ingreso, " +
			"cu.area AS cod_centro_costo, " +
			"raom.activo AS alerta "+
			"FROM cuentas cu " +
			" INNER JOIN personas per ON (per.codigo=cu.codigo_paciente)" +
			" INNER JOIN admisiones_hospi ah ON (ah.cuenta=cu.id) " +
			" LEFT OUTER JOIN traslado_cama tc ON (tc.cuenta=cu.id AND tc.fecha_finalizacion IS NULL AND tc.hora_finalizacion IS NULL) " +
			" LEFT OUTER JOIN camas1 cam ON (cam.codigo=tc.codigo_nueva_cama) ";
		
		//si es búsqueda sólo por piso entonces se hace el inner con habitaciones
		if(!codigoPiso.equals("")&&codigoHabitacion.equals("")&&codigoCama.equals(""))
			consultaHospitalizacion += " LEFT OUTER JOIN habitaciones hab ON(hab.codigo=cam.habitacion) ";
			
		if (pacientesNuevaInformacion) {
			consultaHospitalizacion += " INNER JOIN registro_alerta_ordenes_med raom ON (cu.id=raom.cuenta and raom.activo = '"+ ConstantesBD.acronimoSi +"')" ;
		} else {
			consultaHospitalizacion += " LEFT OUTER JOIN registro_alerta_ordenes_med raom ON (cu.id=raom.cuenta and raom.activo = '"+ ConstantesBD.acronimoSi +"')" ;
		}
		
		consultaHospitalizacion +=	" WHERE " +
			" cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") AND " +
			" (cu.hospital_dia = '"+ConstantesBD.acronimoSi+"' OR tc.codigo IS NOT NULL) "; //Hospital Día o Con Traslado Cama
		
		//agregación de clausulas WHERE según parámetros de busqueda
		/*if(centroCosto!=ConstantesBD.codigoNuncaValido)
			consultaHospitalizacion += " AND cu.area = "+centroCosto;
		else*/ 
		if(!codigoCama.equals(""))
			consultaHospitalizacion += " AND tc.codigo_nueva_cama = "+codigoCama;
		else if(!codigoHabitacion.equals(""))
			consultaHospitalizacion += " AND cam.habitacion = "+codigoHabitacion;
		else if(!codigoPiso.equals(""))
			consultaHospitalizacion += " AND hab.piso = "+codigoPiso;
		
		
		//**************************************************************************************************************
		//**********************SE ARMA CONSULTA DE URGENCIAS*********************************************************
		String consultaUrgencias =  " SELECT " +
			"cu.id AS nro_cuenta, " +
			"CASE WHEN au.cama_observacion IS NOT NULL THEN " +
				"getnombrepisocama(cam.codigo) || ' - ' || getnomhabitacioncama(cam.codigo) " +
			"ELSE " +
				"''" +
			"END AS piso_habitacion," +
			"CASE WHEN au.cama_observacion IS NOT NULL THEN " +
				"'P:' || substr(getnombrepisocama(cam.codigo),0,8) || ' H:' || substr(getnomhabitacioncama(cam.codigo),0,8) || ' C:' || substr(cam.numero_cama,0,8) " +
			"ELSE " +
				"''" +
			"END AS numero_cama," +
			"coalesce(cam.descripcion,'') AS descripcion_cama," +
			" getNomCentroCosto(cu.area) AS centro_costo," +
			"per.primer_apellido || ' ' || coalesce(per.segundo_apellido,'') || ' ' || per.primer_nombre || ' ' || coalesce(per.segundo_nombre,'') AS paciente,"+
			" per.tipo_identificacion AS tipo_id," +
			" per.numero_identificacion AS nro_id," +
			" getedad(per.fecha_nacimiento) AS edad, " +
			"getdescripcionsexo(per.sexo) AS sexo, " +
			"to_char(au.fecha_admision, '"+ConstantesBD.formatoFechaAp+"') AS fecha_ingreso, " +
			"cu.area AS cod_centro_costo, " +
			"raom.activo AS alerta "+
			"FROM cuentas cu " +
			" INNER JOIN personas per ON (per.codigo=cu.codigo_paciente) " +
			" INNER JOIN admisiones_urgencias au ON (au.cuenta=cu.id) " +
			" LEFT OUTER JOIN camas1 cam ON(cam.codigo=au.cama_observacion) ";
		
		//si es búsqueda sólo por piso entonces se hace el inner con habitaciones
		if(!codigoPiso.equals("")&&codigoHabitacion.equals("")&&codigoCama.equals(""))
			consultaUrgencias += " LEFT OUTER JOIN habitaciones hab ON(hab.codigo=cam.habitacion) ";
		
		if (pacientesNuevaInformacion) {
			consultaUrgencias += " INNER JOIN registro_alerta_ordenes_med raom ON (cu.id=raom.cuenta and raom.activo = '"+ ConstantesBD.acronimoSi +"')" ;
		} else {
			consultaUrgencias += " LEFT OUTER JOIN registro_alerta_ordenes_med raom ON (cu.id=raom.cuenta and raom.activo = '"+ ConstantesBD.acronimoSi +"')" ;
		}
		
		consultaUrgencias +=	" WHERE " +
		" (cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") OR (cu.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaAsociada+" AND  getcuentafinal(cu.id) IS NULL)) " ;
		
	
		//agregación de clausulas WHERE según parámetros de busqueda
		/*if(centroCosto!=ConstantesBD.codigoNuncaValido)
			consultaUrgencias += " AND cu.area = "+centroCosto;
		else */
		if(!codigoCama.equals(""))
			consultaUrgencias += " AND au.cama_observacion = "+codigoCama+" AND au.fecha_egreso_observacion IS NULL " ;
		else if(!codigoHabitacion.equals(""))
			consultaUrgencias += " AND cam.habitacion = "+codigoHabitacion+" AND au.fecha_egreso_observacion IS NULL ";
		else if(!codigoPiso.equals(""))
			consultaUrgencias += " AND hab.piso = "+codigoPiso+" AND au.fecha_egreso_observacion IS NULL ";
		//*************************************************************************************************************
		
		
		StringBuffer consultaStr = new StringBuffer();
		
		consultaStr.append("SELECT * FROM (" + consultaHospitalizacion + " UNION " + consultaUrgencias + ") x ");
		
		/*if(centroCosto!=ConstantesBD.codigoNuncaValido)
			consultaStr.append(" ORDER BY x.centro_costo,x.paciente");
		else*/
		consultaStr.append(" ORDER BY x.piso_habitacion,x.paciente");
		
		logger.info("consultaHospitalizacion=> "+consultaHospitalizacion+" \n\n\n");
		logger.info("consultaUrgencias=> "+consultaUrgencias+" \n\n\n");
		logger.info("consulta=> "+consultaStr);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
			pst.close();
			rs.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error en consultarPacientesCentroCosto de SqlBaseRegistroEnfermeriaDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Metodo que realiza la busqueda de lista de los pacientes centro de costo 
	 * corresponde al centro de costo seleccionado.
	 * @param con
	 * @param campos
	 * @return listadoPacientesCentroCosto
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap consultarPacientesCentroCosto(Connection con, HashMap campos)
	{
		//logger.info("Centro costo seleccionado-->"+centroCosto+"\n\n");
		
		//************SE SACAN LOS PARÁMETROS*******************************************************
		int centroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
		boolean pacientesNuevaInformacion = (Boolean)campos.get("pacientesNuevaInformacion");
		
		//***********SE ARMA CONSULTA DE HOSPITALIZACIÓN********************************************
		String consulta = " SELECT DISTINCT cu.id AS nro_cuenta, " +
			"COALESCE (getnombrepisocama(camho.codigo), getnombrepisocama(camau.codigo)) AS piso_habitacion, " +
			"COALESCE (camho.numero_cama, camau.numero_cama) AS numero_cama, " +
			"COALESCE (camho.descripcion, camau.descripcion) AS descripcion_cama, " +
			"getNomCentroCosto(cu.area) AS centro_costo, " +
			"per.primer_apellido " +
			"|| ' ' " +
			"|| COALESCE(per.segundo_apellido,'') " +
			"|| ' ' " +
			"|| per.primer_nombre " +
			"|| ' ' " +
			"|| COALESCE(per.segundo_nombre,'') AS paciente, " +
			"per.tipo_identificacion AS tipo_id, " +
			"per.numero_identificacion AS nro_id, " +
			"getedad(per.fecha_nacimiento) AS edad, " +
			"getdescripcionsexo(per.sexo) AS sexo, " +
			"TO_CHAR(cu.fecha_apertura, 'DD/MM/YYYY') AS fecha_ingreso, " +
			"cu.area AS cod_centro_costo, " +
			"raom.activo AS alerta " +
			"FROM cuentas cu " +
			"INNER JOIN personas per " +
			"ON (per.codigo=cu.codigo_paciente) " +
			
			/**MT 4227 - Diana Ruiz*/
			
			"LEFT OUTER JOIN admisiones_hospi ah " +
			"ON (cu.id=ah.cuenta) " +
			"LEFT OUTER JOIN camas1 camho " +
			"ON (camho.codigo=ah.cama) " +
			"LEFT OUTER JOIN habitaciones habho " +
			"ON(habho.codigo=camho.habitacion) " +
			"LEFT OUTER JOIN admisiones_urgencias au " +
			"ON (au.cuenta=cu.id) " +
			"LEFT OUTER JOIN camas1 camau " +
			"ON(camau.codigo=au.cama_observacion) " +
			"LEFT OUTER JOIN habitaciones habau " +
			"ON(habau.codigo=camau.habitacion) " ;		
		
		if (pacientesNuevaInformacion) {
			consulta += " INNER JOIN registro_alerta_ordenes_med raom ON (cu.id=raom.cuenta and raom.activo = '"+ ConstantesBD.acronimoSi +"')" ;
		} else {
			consulta += " LEFT OUTER JOIN registro_alerta_ordenes_med raom ON (cu.id=raom.cuenta and raom.activo = '"+ ConstantesBD.acronimoSi +"')" ;
		}
		
		consulta +=	" WHERE " +
				" (cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") " +
				"OR (cu.estado_cuenta = " + ConstantesBD.codigoEstadoCuentaAsociada +" AND  getcuentafinal(cu.id) IS NULL)) " ;
		
		if(centroCosto!=ConstantesBD.codigoNuncaValido)
			consulta += " AND cu.area = "+centroCosto;
		
		consulta += " ORDER BY centro_costo, paciente";
		
		logger.info("consulta=> "+consulta);
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
			pst.close();
			rs.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error en consultarPacientesCentroCosto de SqlBaseRegistroEnfermeriaDao: "+e);
			return null;
		}
	}
	
    /**
     * Metodo para consultar y cargar la información de la dieta registrada en orden medica.
     * @param con
     * @param codigoCuenta
     * @param tipoConsulta para determinar el tipo de información a retornar. 
     * @return Collection con los datos de la última dieta.
     */

	public static Collection cargarDieta(Connection con, String cuentas, int tipoConsulta)
    {  	
    	String consultaStr = "", cadCuenta = "";
    	
    	
    	//--Verificar tambien la informacion de la cuenta de asocio 
		
			cadCuenta = "  IN ( " +  cuentas  + " ) ";
		
		logger.info("\n\n\n\nTIPO CONSULTA------->>>>>>>>>"+tipoConsulta);
    	switch (tipoConsulta)
		{
    		case 0:
		    	{
		    		//--------Consultar si tiene via oral, parenteral, y las observaciones de la Dieta
					consultaStr  =   " SELECT od.via_oral AS nutricion_oral, od.via_parenteral AS nutricion_parenteral,							" +
		    						 "		  od.finalizar_dieta AS finalizar_dieta, 															" +
		    						 "		  CASE WHEN od.suspendido_enfermeria IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE od.suspendido_enfermeria END AS suspendido_enfermeria, 												" +
		    						 "		  coalesce (od.observaciones||'','') AS observaciones,																" +
		    						 "        getdatosmedicoespecialidades(ehom.login,',') as medico,  															" +
		    						 "		  to_char(ehom.fecha_orden, '"+ConstantesBD.formatoFechaAp+"') ||'  '|| ehom.hora_orden as fecha_orden, 				" +
		    						 "		  to_char(ehom.fecha_grabacion, '"+ConstantesBD.formatoFechaAp+"') ||'  '|| ehom.hora_grabacion as fecha_grabacion, 	" +
		    						 "		  om.descripcion_dieta_par  as observacion_parente		" +
		    						 "		  FROM orden_dieta od 																				 " +
		    						 "		  	   INNER JOIN encabezado_histo_orden_m ehom ON ( ehom.codigo = od.codigo_histo_enca  )			 " +
		    						 "		  	   INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica  )							 " +
		    						 "			   WHERE od.codigo_histo_enca = (SELECT MAX (codigo_histo_enca) as codigo_histo	 				 " +
		    						 "													FROM orden_dieta od										 " +
									 "													 	 INNER JOIN encabezado_histo_orden_m eh ON (od.codigo_histo_enca=eh.codigo) " +   
									 "							  							 INNER JOIN ordenes_medicas om ON (om.codigo=eh.orden_medica)  " +  
		                             "															 WHERE om.cuenta " + cadCuenta +  " )	 ";
		    	}
		    break;	
		    case 1:
		    	{
		    		//--------Consultar los tipos de nutriciones registrados por ultima vez en Ordenes medicas.
					consultaStr  =   " SELECT descripcion_dieta_oral as descripcion_dieta_oral, descripcion_soporte AS descripcion_soporte, " +
																"observaciones_generales AS observaciones_generales " +
																	"	  FROM ordenes_medicas om			     " +
																		"	        WHERE om.cuenta " +cadCuenta;	    	
				}
			break;
		    case 2:
		    	{
		    		//--------Consultar los tipos de nutriciones registrados por ultima vez en Ordenes medicas.
					consultaStr  =   " SELECT nutricion_oral_cc_inst as codigo, nombre as nombre 								 		  " +
									 "		  FROM orden_nutricion_oral ono 															  " +
									 "			   INNER JOIN nutricion_oral_cc_inst noci ON (noci.codigo = ono.nutricion_oral_cc_inst)   " +  
									 " 			   INNER JOIN tipo_nutricion_oral tno ON (tno.codigo = noci.nutricion_oral)   			  " + 
									 "	  		        WHERE ono.codigo_historico_dieta = (SELECT MAX (codigo_historico_dieta)			  " +
									 "															   FROM orden_nutricion_oral ono		  " +
									 "																	INNER JOIN orden_dieta od ON (ono.codigo_historico_dieta = od.codigo_histo_enca) " +    
									 "																	INNER JOIN encabezado_histo_orden_m eh ON (od.codigo_histo_enca=eh.codigo) " +   
									 "							  										INNER JOIN ordenes_medicas om ON (om.codigo=eh.orden_medica)  " +  
									 "							  											 WHERE om.cuenta "+ cadCuenta + " ) " +
									 "					 AND noci.activo = " +  ValoresPorDefecto.getValorTrueParaConsultas() +
									 "	UNION ALL " +
									 "	SELECT ono.codigo AS codigo, ono.descripcion AS nombre   "  +
									 "		   FROM otro_nutricion_oral ono  " +
									 "		   		INNER JOIN detalle_otro_nutri_oral dno ON (dno.otro_nutricion_oral=ono.codigo) " + 
									 "			    INNER JOIN orden_dieta od ON (dno.codigo_historico_dieta=od.codigo_histo_enca)  " + 
									 "			    INNER JOIN encabezado_histo_orden_m eh ON (eh.codigo=od.codigo_histo_enca) " +
									 "			    INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo) " +
									 "				     WHERE om.cuenta " + cadCuenta +
									 "				       AND dno.codigo_historico_dieta = ( SELECT MAX (codigo_historico_dieta) " +			     
									 "	  															   FROM orden_nutricion_oral ono  " +		     
									 "	  																	INNER JOIN orden_dieta od ON (ono.codigo_historico_dieta = od.codigo_histo_enca)  " +        
									 "	  																	INNER JOIN encabezado_histo_orden_m eh ON (od.codigo_histo_enca=eh.codigo) " +
									 "	  							  										INNER JOIN ordenes_medicas om ON (om.codigo=eh.orden_medica) " + 
									 "																			 WHERE om.cuenta " + cadCuenta + " )";
		    	}
		    break;	
		    case 3:
	    	{
	    		//--------Consultar los tipos de nutriciones registrados por ultima vez en Ordenes medicas.
				consultaStr  =  " SELECT doom.fecha_orden as fecha_orden, doom.observacion as observacion, doom.datos_medico as datos_medico, ordenes.getfechadetalleobserbacionorde(doom.id) AS fecha "+
								" FROM   ordenes.ordenes_medicas om INNER JOIN ordenes.detalle_observacion_orden_med doom ON (doom.id_orden_medica=om.codigo) " +
								" WHERE  om.cuenta " + cadCuenta +
								" ORDER  BY fecha ";
			}
	    	break;
    	
		    case 4:
		    {
		    	consultaStr  = " SELECT osr.descripcion AS descripcion FROM orden_soporte_respira osr " +
						"INNER JOIN encabezado_histo_orden_m ehom ON(ehom.codigo = osr.codigo_histo_enca) " +
						"INNER JOIN ordenes_medicas om ON(om.codigo = ehom.orden_medica) " +
						"WHERE om.cuenta "+ cadCuenta +" AND osr.descripcion IS NOT NULL " +
						"ORDER BY  osr.codigo_histo_enca DESC ";
		    	
		    }	
    	     break;
		}	  
    
		logger.info("\n\n  * El tipo de Consulta [" + tipoConsulta+ "  ]  En Cargar Dieta ------------> "+consultaStr + "\n");
		
		try
		{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			//consultarNov.setInt(1, codigoCuenta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado la última dieta de la Orden Medica :"+e.toString());
			return null;
		}    	
    }
    
	/**
	 * Metodo para Consultar los datos por institucion y centro de Costo (Urgencia y/o Hospitalización) 
	 * @param tablaCcInt
	 * @param campoTipo
	 * @param institucion
	 * @param centroCosto
	 * @param codigoCuentaAsocio
	 * @param colocarBool
	 * @param tablaDatosParametrizados : La tabla con informacion registrada parametrizada, para buscar la
	 * 									 informacion con centros de costo anteriores. 		 									 	
	 * @return
	 */	
	public static String tablaParametrizada( String tablaCcInt, String campoTipo, int institucion, int centroCosto, boolean colocarBool, String tablaDatosParametrizados, String codigoCuenta)
	{
		String cad = "", cad2="";

		if (colocarBool)
		{
			cad2 = " AND activo = " + ValoresPorDefecto.getValorTrueParaConsultas();
		}
		
		/*if ( codigoCuentaAsocio != 0 )
		{
			cad = "   SELECT * FROM " + tablaCcInt +  "" +
				  "				    WHERE institucion = " + institucion + 
				  "					  AND centro_costo = " + centroCosto + cad2 + 
				  "   UNION   " +
				  "	  SELECT * FROM " + tablaCcInt +
				  "				    WHERE institucion = " + institucion +  cad2 +
				  "					  AND centro_costo = " + Utilidades.convertirAEntero(ValoresPorDefecto.getCentroCostoUrgencias(institucion)) +
				  "   		          AND "+ campoTipo + " NOT IN (SELECT  "+ campoTipo + "  FROM " + tablaCcInt +" WHERE institucion = " + institucion + " AND centro_costo = " + centroCosto + cad2 + " ) ";
			
			//logger.info(" LA CADENA DE TABLA --------------> " + cad +  "\n\n");
		}
		else
		{
			cad = "   SELECT * FROM " + tablaCcInt +  "" +
			  	  "				     WHERE institucion = " + institucion + cad2 +
			      "					   AND centro_costo = " + centroCosto;
		}*/
		
	cad = "   SELECT * 																													  		" + 
		  " 		 FROM " + tablaCcInt + "																									" + 
		  " 			  WHERE codigo IN (  																									" +
		  "									SELECT codigo 																						" +
		  "										   FROM " + tablaCcInt + "																		" +
		  "								   				WHERE institucion =  " + institucion +
		  "								   			  	  AND centro_costo = " + centroCosto + cad2 +
		  "									UNION																								" +			 
		  "									SELECT " + tablaCcInt + " as codigo																	" +
		  "									 	   FROM " + tablaDatosParametrizados + " 														" +
		  "												 INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.codigo = " + tablaDatosParametrizados +".codigo_histo_enfer ) " +
		  "												 INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo )				" +
		  "												 INNER JOIN " + tablaCcInt  + " ON (" + tablaDatosParametrizados + "." + tablaCcInt + " = " + tablaCcInt + ".codigo ) " +   
		  "												 	  WHERE	re.cuenta = " + codigoCuenta +
		  "														AND " + tablaCcInt+ "." + campoTipo + "	NOT IN (" +
		  "																			 SELECT "+ campoTipo  + 
		  "																				    FROM " + tablaCcInt + 
		  " 																		  	   WHERE institucion = " + institucion +
		  "																					 AND centro_costo = " + centroCosto + cad2 + 
		  "																		    ) " +
		  "									 		 	   GROUP BY  " + tablaCcInt + " " +
		  "								  ) ";		 		 	 
		
	
	 /*INNER JOIN liquidos_elim_cc_inst leci ON ( balance_liq_elim_par.liquidos_elim_cc_inst = leci.codigo )
	 	  WHERE re.cuenta = 723 
	 	    AND leci.tipos_liquidos_elim NOT IN  ( 
			 	    								SELECT tipos_liquidos_elim
			 	    									   FROM liquidos_elim_cc_inst
				 			              			 	    WHERE institucion =  2        
			 			              						  AND centro_costo = 133
											   		 		  AND activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+"  
			 	    							 )
	*/

	
		//logger.info("\n\n La Cadena (Tabla cc_ints)--->  [" + cad + "] \n\n");
		return cad;  //-La Cadena que Forma la Tabla de los Tipos parametrizados CC inst.  
	}
	
    
    /**
     * Metodo para traer un mapa con el listado de liquidos y medicamentos registrados para
     * un paciente especifico. 
     * @param con
     * @param codigoCuenta
     * @param fechaUltimoFinTurno
     * @param fechaInicio
     * @param codigoCuentaAsocio 
     * @return
     */
	public static HashMap consultarLiqMedicamentosPaciente (Connection con, String cuentas, int centroCosto, int institucion, int nroConsulta, String fechaUltimoFinTurno, String fechaInicio)
	{
		String consulta ="";    
		String cadCuenta = "";
		String cuenta=cuentas.split(",")[0];
		
		logger.info("\n\n\n\n valor de la cuenta >>> "+cuentas);
		
		//-Consultar la información por el centro de costo. 
		
			cadCuenta = " IN (" + cuentas + ")  "  ;
		
			
		switch (nroConsulta)
		{
			case 0:  //---Consultar los medicamentos no parametrizados para cada paciente especifico.  (Seccion Liquidos y Medicamentos de Infusion).
			{
				consulta = "	SELECT * FROM (" +
						   "	SELECT mie.codigo as codigoMedEnfer, mie.registro_enfer, mie.consecutivo_liquido, mie.descripcion, mie.volumen_total||'' as volumen_total,   " + 
	     				   " 		   mie.velocidad_infusion, mie.suspendido,   																		" + 
						   "	       CASE WHEN (SELECT medicamento_infusion FROM balance_liq_admin_enfer blae  									 	" +
						   "							 INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.codigo = blae.codigo_histo_enfer )			" + 	
	     	     		   "		   					 INNER JOIN registro_enfermeria renfer ON ( renfer.codigo = ehre.registro_enfer ) 				" + 	
	     				   "								  WHERE blae.medicamento_infusion=mie.codigo 												" + 
	     				   "			 						AND renfer.cuenta " + cadCuenta +
	     				   "							   GROUP BY medicamento_infusion) IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END as ya_registrado,			" +
	     				   "		   "+ValoresPorDefecto.getValorFalseParaConsultas()+" as finalizaron, '' as medico_f, '' as fecha_registro_f, '' as fecha_grabacion_f," +
	     				  //debido a que una infusion no necesita despacho, se pone el despacho en 1 para indicar que ya fue despachado
	     				   "		   1 As despacho 							" +
						   " 		   FROM medicamento_infusion_enfer mie 																				" +
						   "				INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer )										" +
						   "					 WHERE re.cuenta " + cadCuenta +
						   "					   AND mie.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() + 
//						   "					   	   ORDER BY  mie.consecutivo_liquido";
						   "	UNION ALL																												" + 
						   "	SELECT ehom.codigo as codigoMedEnfer, m.consecutivo as registro_enfer, -1 as consecutivo_liquido,  						" + 
						   "		   m.nombre || ' (' || tm.nombre || ') ' || " +
						   "		   CASE WHEN sol.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada +" 								" +
						   "				THEN '     ( Pendiente )' ELSE '' END as descripcion,										  							" +
						   "	 	   od.volumen_total as volumen_total, od.velocidad_infusion as velocidad_infusion,      							" +
						   "		   CASE WHEN sol.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada +" 								" +
						   "				THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END as suspendido, 																	" +
						   "		   "+ValoresPorDefecto.getValorTrueParaConsultas()+" as ya_registrado, od.finaliza_sol as finalizaron,															" +
						   "       	   getdatosmedicoespecialidades(ehom.login,',') as medico_f,  														" +
						   "		   to_char(ehom.fecha_orden, '"+ConstantesBD.formatoFechaAp+"') ||'  '|| ehom.hora_orden as fecha_registro_f, 			" +
						   "		   to_char(ehom.fecha_grabacion, '"+ConstantesBD.formatoFechaAp+"') ||'  '|| ehom.hora_grabacion as fecha_grabacion_f," +
						   "           dp.orden As despacho	" +
						   "	 	   FROM orden_dieta od																								" +
						   "	 	  		INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )    													" +
						   "		 		INNER JOIN tipo_mezcla tm ON ( tm.codigo = m.cod_tipo_mezcla ) 												" +
						   "		 		INNER JOIN encabezado_histo_orden_m ehom ON ( od.codigo_histo_enca = ehom.codigo )							" +
						   "	 	 		INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )   										" +		
						   "	 	 		INNER JOIN solicitudes_medicamentos sm ON ( sm.orden_dieta = od.codigo_histo_enca )							" +   
						   "	 	 		INNER JOIN solicitudes sol ON ( sol.numero_solicitud = sm.numero_solicitud )" +
						   "				LEFT OUTER JOIN despacho dp ON (dp.numero_solicitud=sol.numero_solicitud)				" +
						   "	 	  			 WHERE om.cuenta " + cadCuenta +
						   "					   AND od.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() + 	
						   "					   AND sol.estado_historia_clinica " +
						   "							IN( " + ConstantesBD.codigoEstadoHCAdministrada +" ,"+ ConstantesBD.codigoEstadoHCSolicitada +") "+ 	
						   "  ) x ORDER BY  consecutivo_liquido ";	
			}break;
			case 1:	 //---Consultar los Medicamentos Administrados (parametrizados y no parametrizados) solo los nombre y sus codigos.	
			{
				consulta= "	SELECT * FROM ( " +
						  "		SELECT * FROM (	SELECT mie.codigo as codigo, -1 as codigo_ci,  mie.consecutivo_liquido as codMedPaciente, 0 as param, " +
						  "							   mie.descripcion as nombre, 'a' as tipoLiquido, 0 as ya_suspendido, "+ValoresPorDefecto.getValorFalseParaConsultas()+" as esmezcla, 0 as codMezcla " + 
						  "						   FROM medicamento_infusion_enfer mie  " +														   
						  "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) " + 			   
						  "			   		 				 WHERE re.cuenta  " + cadCuenta +
						  "			   		  				   AND mie.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() +
				  		  "					    UNION   " +
						  "						SELECT mie.codigo as codigo, -1 as codigo_ci,  mie.consecutivo_liquido as codMedPaciente, 0 as param, mie.descripcion as nombre," +
						  "							   'a' as tipoLiquido, 1 as ya_suspendido, "+ValoresPorDefecto.getValorFalseParaConsultas()+" as esmezcla, 0 as codMezcla  " + 
						  "							   FROM balance_liq_admin_enfer blae  " + 	
			 	 	  	  "								    INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) " +
				  		  "	 								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion )  " +
						  "									INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer )  " +   
						  "										 WHERE to_char(ehre.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +							  
						  "						  	           	   AND mie.suspendido = " + ValoresPorDefecto.getValorTrueParaConsultas() + 
						  "			   		 					   AND re.cuenta " + cadCuenta + " ) x " +
						  "		UNION ALL   " +
						  "		SELECT tla.codigo as codigo, laci.codigo as codigo_ci,  -1 as codMedPaciente, 1 as param, " +
						  "			   tla.nombre as nombre, 'a' as tipoLiquido, 0 as ya_suspendido, "+ValoresPorDefecto.getValorFalseParaConsultas()+" as esmezcla, 0 as codMezcla" +
						  "			   FROM  ( " + tablaParametrizada("liquidos_admin_cc_inst", "tipos_liquidos_admin", institucion, centroCosto, true, "balance_liq_admin_par", cuenta) +  " ) laci " +
						  "					INNER JOIN tipos_liquidos_admin tla ON ( tla.codigo = laci.tipos_liquidos_admin ) " + 			   
						  //--------Se Adicionan Las MEZCLAS para el paciente que estan en estado DESPACHADAS.  		  	
						  "		UNION ALL   " +							 	
					      "		SELECT od.codigo_histo_enca as codigo, -1 as codigo_ci, ehom.codigo as codMedPaciente, 0 as param,										" + 
						  "			   m.nombre || ' (' || tm.nombre || ') ' as nombre, 'a' as tipoLiquido, CASE WHEN od.suspendido = "+ValoresPorDefecto.getValorFalseParaConsultas()+" THEN 0 ELSE 1 END AS ya_suspendido, " +
						  "			   "+ValoresPorDefecto.getValorTrueParaConsultas()+" as esmezcla, 1 as codMezcla		" +
						  "	 	   	   FROM orden_dieta od																								" +
						  "	 	  			INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )    													" +
						  "		 			INNER JOIN tipo_mezcla tm ON ( tm.codigo = m.cod_tipo_mezcla ) 												" +
						  "		 			INNER JOIN encabezado_histo_orden_m ehom ON ( od.codigo_histo_enca = ehom.codigo )							" +
						  "	 	 			INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )   										" +		
						  "	 	 			INNER JOIN solicitudes_medicamentos sm ON ( sm.orden_dieta = od.codigo_histo_enca )							" +   
						  "	 	 			INNER JOIN solicitudes sol ON ( sol.numero_solicitud = sm.numero_solicitud )								" +
						  "	 	  				 WHERE om.cuenta  " + cadCuenta +
						  "					       AND sol.estado_historia_clinica = " + ConstantesBD.codigoEstadoHCAdministrada + 	
						  " 	) z ORDER BY param, codMezcla DESC, codMedPaciente ";
			}
			break;
			//--------Consultar la información del balance de liquidos registrados administrados anteriormente parametrizados y no parametrizados   
			case 2:
			{
				consulta    = "	SELECT * FROM (" +
							  "	SELECT * FROM ( " +
							  //----------Parte de Mezclas.
							  "					SELECT to_char(ehre.fecha_grabacion,'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion as hora_grabacion, " +   
							  "						   to_char(ehre.fecha_registro,'DD/MM/YYYY') as fecha_registro, ehre.hora_registro as hora_registro,	    " +
							  "						   ehre.codigo as codEncabezado, blam.orden_dieta as codigoMed, -1 as codigoMed_ci,  0 as paramMed, blam.valor as valorMed,	" +			 
							  "						   to_char(ehre.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fecha, ehre.hora_grabacion as hora, 'a' as tipoliquidoMed, 1 as codMezclaMed,to_char(ehre.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ehre.hora_grabacion || ':00' as hora_temp						" +
							  "						   FROM balance_liq_admin_mezcla blam 			 																			" +		
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
							  "							   		 WHERE re.cuenta " + cadCuenta +
							  "									   AND to_char(ehre.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "					UNION ALL  " +
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,  " +  
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ftbl.hora_registro||'' as hora_registro,  	" +  
		    				  "						   dftlm.codigo_turno*-1 as codEncabezado, dftlm.orden_dieta as codigoMed,													" +
		    				  "						   -1 as codigoMed_ci, 0 as paramMed, dftlm.total as valormed,    															" +
		  					  "						   to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha,  ftbl.hora||'' as hora, 'a' as tipoliquidoMed, 1 as codMezclaMed,to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ftbl.hora || ':01' as hora_temp											" + 
		  					  "						   FROM fin_turno_balance_liq ftbl																					   		" +
		  					  " 							INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
		  					  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
							  "								     WHERE re.cuenta " + cadCuenta +
		  					  "		  							   AND to_char(ftbl.fecha_registro,'"+ConstantesBD.formatoFechaBD+"')||'-'||ftbl.hora_registro > '" + fechaInicio  + "'"+  
							  //----------Parte de Mezclas.
							  "					UNION ALL  " +
							  "					SELECT to_char(ehre.fecha_grabacion, 'DD/MM/YYYY')  as fecha_grabacion, ehre.hora_grabacion as hora_grabacion, "  +
							  "						   to_char(ehre.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ehre.hora_registro as hora_registro, " +
							  "						   ehre.codigo as codEncabezado, mie.codigo as codigoMed, -1 as codigoMed_ci,  0 as paramMed, blae.valor as valorMed, " +
							  "		   				   to_char(ehre.fecha_grabacion, '"+ConstantesBD.formatoFechaBD+"') as fecha,  ehre.hora_grabacion as hora, 'a' as tipoliquidoMed, 0 as codMezclaMed,to_char(ehre.fecha_grabacion, '"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ehre.hora_grabacion || ':00' as hora_temp	" +
					  		  "						   FROM balance_liq_admin_enfer blae  " +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) " +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion ) " +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) " + 			    
							  "							   		 WHERE re.cuenta " + cadCuenta +
					//		  "							   		   AND mie.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() +
							  "									   AND to_char(ehre.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "					UNION ALL  " +
							  "					SELECT to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion as hora_grabacion, "  +
							  "						   to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ehre.hora_registro as hora_registro,	  " + 
					  		  "						   ehre.codigo as codEncabezado, tla.codigo as codigoMed, laci.codigo as codigoMed_ci,  1 as paramMed, blap.valor as valorMed, 				" +
							  "		   				   to_char(ehre.fecha_grabacion, '"+ConstantesBD.formatoFechaBD+"') as fecha,  ehre.hora_grabacion as hora, 'a' as tipoliquidoMed, 0 as codMezclaMed,to_char(ehre.fecha_grabacion, '"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ehre.hora_grabacion || ':00' as hora_temp	" +
							  "						   FROM balance_liq_admin_par blap 			" +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo ) " +
							  "								INNER JOIN  ( " + tablaParametrizada("liquidos_admin_cc_inst", "tipos_liquidos_admin", institucion, centroCosto, true, "balance_liq_admin_par", cuenta) +  " )" +
							  " 										laci ON ( laci.codigo = blap.liquidos_admin_cc_inst )							 " +
							  "								INNER JOIN tipos_liquidos_admin tla ON ( tla.codigo = laci.tipos_liquidos_admin )  " +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )  " +
							  "							   		 WHERE to_char(ehre.fecha_registro, '"+ConstantesBD.formatoFechaBD+"') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "				 					   AND re.cuenta " + cadCuenta +
							  "				 	UNION ALL						" +
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora as hora_grabacion, " +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro as hora_registro,  " +
							  " 					   codigo_turno*-1 as codEncabezado, tla.codigo as codigoMed, dftlap.liquidos_admin_cc_inst as codigoMed_ci, 1 as paramMed, dftlap.total as valormed,  " +  	
							  "						   to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, ftbl.hora||'' as hora, 'a' as tipoliquidoMed, 0 as codMezclaMed,to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ftbl.hora || ':01' as hora_temp  "  +
							  " 						   FROM fin_turno_balance_liq ftbl  " +
							  "									INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo ) " +
							  " 								INNER JOIN  ( " + tablaParametrizada("liquidos_admin_cc_inst", "tipos_liquidos_admin", institucion, centroCosto, true, "balance_liq_admin_par", cuenta) +  " ) laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) " +
							  "									INNER JOIN tipos_liquidos_admin tla ON ( tla.codigo = laci.tipos_liquidos_admin )  " + 
							  "									INNER JOIN registro_enfermeria re ON ( re.codigo = ftbl.registro_enfer )  " +
							  "										 WHERE  to_char(ftbl.fecha_registro,'"+ConstantesBD.formatoFechaBD+"')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "					 					   AND re.cuenta " + cadCuenta +
							  "					UNION ALL  "+ 
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora as hora_grabacion, " +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ftbl.hora_registro as hora_registro, " +
							  "  					   dftla.codigo_turno*-1 as codEncabezado, dftla.medicamento_infusion_enfer as codigoMed, -1 as codigoMed_ci, 0 as paramMed, dftla.total as valormed, " +  	
							  "						   to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, ftbl.hora||'' as hora, 'a' as tipoliquidoMed, 0 as codMezclaMed,to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ftbl.hora || ':01' as hora_temp  "  +
							  "						   FROM fin_turno_balance_liq ftbl " + 
							  "								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo ) " +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +
							  "								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo ) " +
							  "									 WHERE re.cuenta " + cadCuenta +
							  "								   	   AND to_char(ftbl.fecha_registro,'"+ConstantesBD.formatoFechaBD+"')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "				  ) x " +
							  " UNION ALL " +
							  "	SELECT * FROM (  SELECT	  to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion as hora_grabacion, 	" +
							  "							  to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ehre.hora_registro  as hora_registro,  		" +
							  "						   	  ehre.codigo as codEncabezado, tle.codigo as codigoMed, leci.codigo as codigoMed_ci,  1 as paramMed, blep.valor as valorMed,   " + 				   
				 			  "							  to_char(ehre.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fecha, ehre.hora_grabacion as hora, 'e' as tipoliquidoMed, 0 as codMezclaMed,to_char(ehre.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ehre.hora_grabacion || ':00' as hora_temp 											" +
							  "							  FROM balance_liq_elim_par blep  " + 			   
							  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
							  "							 	   INNER JOIN  ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, true, "balance_liq_elim_par", cuenta) +  " ) leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +
							  "								   INNER JOIN tipos_liquidos_elim tle ON ( tle.codigo = leci.tipos_liquidos_elim ) " + 
							  "								   INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) " + 
							  "								   		 WHERE to_char(ehre.fecha_registro,'"+ConstantesBD.formatoFechaBD+"') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "										   AND re.cuenta " + cadCuenta +			   
							  "					UNION ALL	" +						   
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora as hora_grabacion,		" +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro as hora_registro,  		" +     
							  "						   dftlep.codigo_turno*-1 as codEncabezado, tle.codigo as codigoMed, dftlep.liquidos_elim_cc_inst as codigoMed_ci, 1 as paramMed, dftlep.total as valormed, " +       	
							  "						   to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, ftbl.hora||'' as hora, 'e' as tipoliquidoMed, 0 as codMezclaMed,to_char(ftbl.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha_temp,ftbl.hora || ':01' as hora_temp   " +      
							  "						   FROM fin_turno_balance_liq ftbl " +   
							  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
							  "								INNER JOIN ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, true, "balance_liq_elim_par", cuenta) +  " ) leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " +
							  "								INNER JOIN tipos_liquidos_elim tle ON ( tle.codigo = leci.tipos_liquidos_elim ) " + 
							  "							    INNER JOIN registro_enfermeria re ON ( re.codigo = ftbl.registro_enfer ) " + 
							  "									 WHERE to_char(ftbl.fecha_registro,'"+ConstantesBD.formatoFechaBD+"')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "									   AND re.cuenta " + cadCuenta +			   
							  "					) y" +
							  "	) z ORDER BY fecha_temp, hora_temp, codEncabezado ";
			}
			break;
			//--------Retorna la suma de cada medicamento (no parametrizados) desde la ultima finalizacion del turno.    
			case 3:
			{
				if (!fechaUltimoFinTurno.equals("") )
				{
					fechaUltimoFinTurno = " AND (ehre.fecha_grabacion || '-' || ehre.hora_grabacion)   > '" + fechaUltimoFinTurno + "' ";  
				}
				consulta = "	SELECT * FROM ( SELECT mie.codigo as codigoMed, sum(blae.valor) as valorMed	  " +
						   "						   FROM balance_liq_admin_enfer blae		" + 
						   "								INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) 		" +
						   "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion ) 	" +
						   "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) 			    	" +
						   "									 WHERE re.cuenta " + cadCuenta + fechaUltimoFinTurno +
				//		   "									   AND mie.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() +
						   "									   	   GROUP BY  mie.codigo) x ORDER BY codigoMed ";
			}
			break;
			//--------Retorna la suma de cada medicamento (parametrizados) desde la ultima finalizacion del turno.    
			case 4:
			{
				if (!fechaUltimoFinTurno.equals("") )
				{
					fechaUltimoFinTurno = " WHERE (ehre.fecha_grabacion || '-' || ehre.hora_grabacion)   > '" + fechaUltimoFinTurno + "' " +
										  "   AND re.cuenta " + cadCuenta;
				}else { fechaUltimoFinTurno = " WHERE re.cuenta " + cadCuenta; }
				
				
				consulta = "	SELECT tla.codigo as codigoMed, laci.codigo as codigoMed_ci,  sum(blap.valor) as valorMed  " + 				   
		   				   "		   FROM balance_liq_admin_par blap						  " +	 			   
		   				   "	 			INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo )  " +  
						   "				INNER JOIN (" + tablaParametrizada("liquidos_admin_cc_inst", "tipos_liquidos_admin", institucion, centroCosto, true, "balance_liq_admin_par", cuenta ) +  " ) laci ON ( laci.codigo = blap.liquidos_admin_cc_inst )  " +   
						   "				INNER JOIN tipos_liquidos_admin tla ON ( tla.codigo = laci.tipos_liquidos_admin )  " + 
						   "				INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
						   "				     " +  fechaUltimoFinTurno +
						  // "					  AND re.cuenta " + cadCuenta +			   
						   "		   		  GROUP BY tla.codigo, laci.codigo  " +
		   				   "			   	  ORDER BY codigoMed";
			}
			break;

			//---Consultar los nombre de los liquidos eliminados (SOLAMENTE HAY PARATRIZABLES).
			case 5:
			{
				consulta =	 " SELECT tle.codigo as codigoElim, leci.codigo as codigoElim_ci, tle.nombre as nombreElim, 1 as paramElim, 'e' as tipoLiquidoElim " +
							 "    	FROM ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, true, "balance_liq_elim_par", cuenta) +  " ) leci   " +
							 "			 INNER JOIN tipos_liquidos_elim tle ON ( tle.codigo = leci.tipos_liquidos_elim ) " +
							 "			 ORDER BY tle.nombre	";
			}
			break;
			//--------Consultar la información del balance de liquidos eliminados registrados anteriormente parametrizados.   
			case 6:
			{
				consulta    = "	SELECT * FROM (  SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registroMedElim, ehre.hora_registro  as hora_registroMedElim,  " +
							  "						    ehre.codigo as codEncabezadoMedElim, leci.codigo as codigoMedElim, 1 as paramMedElim, blep.valor as valorMedElim,  " + 				   
				 			  "							  ehre.fecha_grabacion as fechaMedElim, ehre.hora_grabacion as horaMedElim, 'e' as tipoLiquidoMedElim " +
							  "							  FROM balance_liq_elim_par blep  " + 			   
							  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
							  "							 	   INNER JOIN  ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, true, "balance_liq_elim_par", cuenta) +  " )  leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +    
							  "								   INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
							  "								   		 WHERE ehre.fecha_registro ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
						      "									   AND re.cuenta " + cadCuenta +			   
							  "					UNION ALL	" +						   
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY')  as fecha_registroMedElim, ftbl.hora as hora_registroMedElim,  " +     
							  "						   dftlep.codigo_turno*-1 as codEncabezadoMedElim, dftlep.liquidos_elim_cc_inst as codigoMedElim, 1 as paramMedElim, dftlep.total as valorMedElim, " +       	
							  "						   ftbl.fecha as fechaMedElim, ftbl.hora as horaMedElim, 'e' as tipoLiquidoMedElim   " +      
							  "						   FROM fin_turno_balance_liq ftbl " +   
							  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
							  "								INNER JOIN ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, true, "balance_liq_elim_par", cuenta) +  " )  leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
							  "								   INNER JOIN registro_enfermeria re ON ( re.codigo = ftbl.registro_enfer ) 			    	" +
							  "									 WHERE ftbl.fecha_registro||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "								   AND re.cuenta " + cadCuenta +			   
							  "					) y ORDER BY fecha_registroMedElim, hora_registroMedElim ";				
				
			} 
			break;
			//--------Retorna la suma de cada medicamento ( parametrizados) desde la ultima finalizacion del turno.    
			case 7:
			{
				if (!fechaUltimoFinTurno.equals("") )
				{
					fechaUltimoFinTurno = " WHERE (ehre.fecha_grabacion || '-' || ehre.hora_grabacion)   > '" + fechaUltimoFinTurno + "' ";  
				}else { fechaUltimoFinTurno = "";  }
				
				consulta = "	SELECT tle.codigo as codigoMed, leci.codigo as codigoMed_ci,  sum(blep.valor) as valorMed  " + 				   
		   				   "		   FROM balance_liq_elim_par blep						  " +	 			   
		   				   "	 			INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo )  " +  
						   "				INNER JOIN  ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, true, "balance_liq_elim_par", cuenta) +  " )  leci ON ( leci.codigo = blep.liquidos_elim_cc_inst )    " +   
						   "			    INNER JOIN tipos_liquidos_elim tle ON ( tle.codigo = leci.tipos_liquidos_elim ) " + 
						   "			    INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
						   "		  		 "  + fechaUltimoFinTurno +  
						   "					   AND re.cuenta " + cadCuenta +			   
		   				   "		   		  GROUP BY tle.codigo, leci.codigo   " +
		   				   "			   	  ORDER BY codigoMed";
			}
			break;
			
			case 8:  //------------Consultar la suma de cada mezclas Ordenada desde Ordenes Medicas. 
			{
				if (!fechaUltimoFinTurno.equals("") )
				{
					fechaUltimoFinTurno = " AND (ehre.fecha_grabacion || '-' || ehre.hora_grabacion)   > '" + fechaUltimoFinTurno + "' ";  
				}
				consulta = " SELECT * FROM ( SELECT blam.orden_dieta as codigoMed, sum(blam.valor) as valorMed 					  " +
						   "	        			FROM balance_liq_admin_mezcla blam											  " +	
						   "	        			INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo ) 	  " +	
						   "	        			INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			      " +		
						   "	        			WHERE re.cuenta " + cadCuenta + fechaUltimoFinTurno +	
						   "	        			GROUP BY  blam.orden_dieta) x ORDER BY codigoMed";
			}
			break;
			
		}
		
		try
		{
			
			logger.info("NroConsulta [ " +  nroConsulta  + " ] Consulta (consultarLiqMedicamentosPaciente) ----> " + consulta + "\n\n");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error en consultarLiqMedicamentosPaciente de SqlBaseRegistroEnfermeriaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo para consultar los historicos de dieta 
	 * @param con
	 * @param codigoCuenta
	 * @param centroCosto
	 * @param institucion
	 * @param nroConsulta
	 * @param fechaUltimoFinTurno
	 * @param fechaInicio
	 * @return
	 */

	public static HashMap cargarHistoricosDieta(Connection con, String cuentas, int centroCosto, int institucion, int nroConsulta, String fechaUltimoFinTurno, String fechaInicio)
	{
		String cuenta=cuentas.split(",")[0];
		
		String tablaElim = " ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto,  false, "balance_liq_elim_par", cuenta) +  " ) ";
		String tablaAdm = " ( " +  tablaParametrizada("liquidos_admin_cc_inst", "tipos_liquidos_admin", institucion, centroCosto, false, "balance_liq_admin_par", cuenta)  +  " ) ";
		
		String consulta ="", fechaFinal =""; 

		String cadCuenta = "";

		//-Consultar la información por el centro de costo. 
	
			cadCuenta = " IN (" + cuentas + ")  "  ;
				
		if (fechaInicio.indexOf("&&&&")!=-1) ///-Para traer informacion en un intervalo de fecha.
		{
			fechaFinal = fechaInicio.split("&&&&")[1];
			fechaInicio = fechaInicio.split("&&&&")[0];
		}
		else
		{
			fechaFinal = fechaInicio.split("-")[0]  + "-"  + fechaInicio.split("-")[1]  + "-" + fechaInicio.split("-")[2];
			String hora = fechaInicio.split("-")[3];
			fechaFinal = UtilidadFecha.incrementarDiasAFecha(fechaFinal, 1, true) + "-" + hora;
		}
		
		//logger.info("\n\n\n\n\n\n\n\n\n\n\n\n FECHA INCIO [ " +  fechaInicio  + " ] Fecha FIN  [" + fechaFinal + "]\n\n");				
		
		
		switch (nroConsulta)
		{
			case 0:    //----------------Consulta para sacar solamente las fechas en las cuales se registraron historicos. 
			{
				  consulta  = "	SELECT * FROM (" +
							  "	SELECT * FROM (" +
							  //-----Inicio Mezclas 
							  "					SELECT to_char(ehre.fecha_registro,'DD/MM/YYYY') as fecha_registro  " +
							  "						   FROM balance_liq_admin_mezcla blam 			 																			" +		
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
							  "							   		 WHERE re.cuenta " + cadCuenta +
							  "									   AND ehre.fecha_registro ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "					UNION ALL  " +
							  "					SELECT to_char(ftbl.fecha_registro, 'DD/MM/YYYY') as fecha_registro   															" +
		  					  "						   FROM fin_turno_balance_liq ftbl																					   		" +
		  					  " 							INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
		  					  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
							  "								     WHERE re.cuenta " + cadCuenta +
		  					  "		  							   AND ftbl.fecha_registro||'-'||ftbl.hora_registro < '" + fechaFinal  + "'"+  
							  "					UNION ALL  " +
							  //-----Fin Mezclas
							  "					 SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY')  as fecha_registro " +
					  		  "						   FROM balance_liq_admin_enfer blae  " +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) " +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion ) " +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) " + 			    
							  "							   		 WHERE re.cuenta " + cadCuenta +
							  "									   AND ehre.fecha_registro ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "					UNION ALL  " +
							  "					SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro	" +
							  "						   FROM balance_liq_admin_par blap 			" +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo ) " +
							  "								INNER JOIN " + tablaAdm  + " laci ON ( laci.codigo = blap.liquidos_admin_cc_inst ) " +
							  "			 			  	    INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
							  "							   		 WHERE ehre.fecha_registro ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "							   		 AND re.cuenta " + cadCuenta +
							  "				 	UNION ALL						" +
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY')  as fecha_registro "  +
							  " 						   FROM fin_turno_balance_liq ftbl  " +
							  "									INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo ) " +
							  " 								INNER JOIN  " + tablaAdm  + "  laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) " +
							  "			 					    INNER JOIN registro_enfermeria re ON ( re.codigo = ftbl.registro_enfer ) 			    	" +
							  "										 WHERE ftbl.fecha_registro||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							  "							   			   AND re.cuenta " + cadCuenta +
							  "					UNION ALL  "+ 
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_registro "  +
							  "						   FROM fin_turno_balance_liq ftbl " + 
							  "								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo ) " +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +
							  "								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo ) " +
							  "									 WHERE re.cuenta " + cadCuenta +
							  "								   	   AND ftbl.fecha_registro||'-'||ftbl.hora_registro <  '" + fechaFinal + "'" +
							  "				  ) x " +
							  " UNION ALL " +
							  "	SELECT * FROM (  SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro " +
							  "							  FROM balance_liq_elim_par blep  " + 			   
							  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
							  "							 	   INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +    
							  "			 			  	       INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
							  "								   		 WHERE ehre.fecha_registro ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "							   			   AND re.cuenta " + cadCuenta +
							  "					UNION ALL	" +						   
							  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY')  as fecha_registro  " +      
							  "						   FROM fin_turno_balance_liq ftbl " +   
							  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
							  "								INNER JOIN  " + tablaElim  + "  leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
							  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
							  "									 WHERE ftbl.fecha_registro||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							  "							   		   AND re.cuenta " + cadCuenta +
							  "					) y" +
							  "	) z GROUP BY fecha_registro ORDER BY fecha_registro";
			}
			break;
			case 1:     //---Consutar el codigo y el nombre del medicamento. Solamente los que fueron registrados para ese intervalo de fecha.
			{           
				consulta  = "	SELECT * FROM (	SELECT mie.codigo as codigo, 0 as param, 'a' as tipoLiquido, mie.descripcion as nombre, " +
							"						   mie.consecutivo_liquido as codMedPaciente, 0 as codMezcla, 'false' as esmezcla	  " + 
							"						   FROM medicamento_infusion_enfer mie " +
							"		   		 				WHERE mie.codigo IN ( SELECT mie.codigo as codigo  " +
							"										  				     FROM balance_liq_admin_enfer blae " +    	
							"					 	 	  	  								  INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) " +  
							"						  		  	 							  INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion )  " +  
							"								  								  INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) " +     
						    "									  								   WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +							  
							"									  									 AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +						  	
							"					  							   		 				 AND re.cuenta " + cadCuenta +
							"									  					UNION   						 									 " +	
							"									  					SELECT  mie.codigo as codigo										 " +
							"									  						   FROM fin_turno_balance_liq ftbl  							 " +		 
							"									  								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo )	" +  
							"									  								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +  
							"									  								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo )  " +
							"									  									 WHERE re.cuenta " + cadCuenta +
							"									  								   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" + 
							"									  								   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							" 		  	   			   		 				    )  " +
							"					 UNION ALL      " +
							"					 SELECT laci.codigo as codigo, 1 as param, 'a' as tipoLiquido, tla.nombre as nombre, " +
							"							-1 as codMedPaciente, 0 as codMezcla, 'false' as esmezcla " +  
							"						    FROM liquidos_admin_cc_inst laci    " +
							"							 	 INNER JOIN tipos_liquidos_admin tla ON ( tla.codigo = laci.tipos_liquidos_admin ) " +     			   
							"						   			  WHERE laci.codigo IN (SELECT laci.codigo " +
							"										  						   FROM balance_liq_admin_par blap " + 			 
							"										  								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo ) " +  
							"										  								INNER JOIN " + tablaAdm  + "  laci ON ( laci.codigo = blap.liquidos_admin_cc_inst )    " +
							"																		INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
							"										  							   		 WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							"										  										   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							"										  										   AND re.cuenta " + cadCuenta +
							"										  					UNION    " +
							"										   					SELECT laci.codigo " +
							"										    					   FROM fin_turno_balance_liq ftbl  " +
							"										   								INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo )  " +
							"										    							INNER JOIN " + tablaAdm  + "  laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) " +
							"																		INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
							"										   									 WHERE to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" + 
							"																		   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							"										  									   AND re.cuenta " + cadCuenta +
							"											   				)				   	   " +
							"					UNION ALL 																	  " +				 	
							"		    		SELECT od.codigo_histo_enca as codigo, 0 as param,  'a' as tipoLiquido, " + 
							"		    			   m.nombre || ' (' || tm.nombre || ') ' as nombre, -1 as codMedPaciente,  1 as codMezcla, 'true' as esmezcla 		" +
							"		    	 	   	   FROM orden_dieta od																" +
							"	 	  						INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )    					" +
							"		 						INNER JOIN tipo_mezcla tm ON ( tm.codigo = m.cod_tipo_mezcla ) 				" +
							"						  		WHERE od.codigo_histo_enca IN (" +
							"														SELECT blam.orden_dieta as codigo					"	 +
							"															   FROM balance_liq_admin_mezcla blam 			 																			" +		
							"															   		INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
							"															   		INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
							"									   									 WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd')||'-'||ehre.hora_registro >= '" + fechaInicio + "'" + 
							"																	   	   AND to_char(ehre.fecha_registro,'yyyy-mm-dd')||'-'||ehre.hora_registro < '" + fechaFinal + "'" +
							"									  									   AND re.cuenta " + cadCuenta +
							"														UNION 																									" +
							"														SELECT dftlm.orden_dieta as codigo											" +
							"															   FROM fin_turno_balance_liq ftbl																					   		" +
							"															  	    INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
							"															   		INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
							"									   									 WHERE to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" + 
							"																	   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							"									  									   AND re.cuenta " + cadCuenta +
							"														)	" +	
							") z ORDER BY param, codMezcla DESC, codMedPaciente ";
			} 
			break;					
			//---Consultar los nombre de los liquidos eliminados (SOLAMENTE HAY PARATRIZABLES).
			case 2:
			{
				consulta =	 " 	SELECT leci.codigo as codigoElim, tle.nombre as nombreElim, 1 as paramElim, 'e' as tipoLiquidoElim  " + 
						     " 	       FROM liquidos_elim_cc_inst leci                            " +
							 "				INNER JOIN tipos_liquidos_elim tle ON ( tle.codigo = leci.tipos_liquidos_elim ) " + 
						     " 				     WHERE leci.codigo IN ( SELECT leci.codigo " + 
 							 "												   FROM balance_liq_elim_par blep " +   			 
							 " 							  							INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
							 " 							  							INNER JOIN " + tablaElim  + "  leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +
							 "														INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
							 " 							  						   		 WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" + 
							 " 															   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'"  +
							 "						  									   AND re.cuenta " + cadCuenta +
							 " 								  		    UNION     "+
							 " 			  							    SELECT leci.codigo 			"+  
							 " 												   FROM fin_turno_balance_liq ftbl  "+   								    					   
							 " 														INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo ) "+   
							 " 								  						INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )  "+ 
							 "														INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
							 "															 WHERE to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +  
							 "													  	 	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							 "						  									   AND re.cuenta " + cadCuenta +
							 " 											)	" +
							 "				ORDER BY tle.nombre ";  
			}
			break;
			case 3:     //------Consultar la información del balance de liquidos  registrados administrados y eliminados anteriormente parametrizados y no parametrizados
			{			//------Para el intervalo de fecha independientemente si esta activo o inactivo en el sistema, o suspendido o no.
				consulta    = "	SELECT * FROM (					 " + 
							  "	SELECT * FROM ( " +
							  //----------Parte de Mezclas.
							  "					SELECT getDatosMedico(ehre.usuario) as medico, " +
							  "						   to_char(ehre.fecha_grabacion,'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion, " +   
							  "						   to_char(ehre.fecha_registro,'DD/MM/YYYY') as fecha_registro, ehre.hora_registro||'' as hora_registro,		" +
							  "						   ehre.fecha_grabacion as fecha, ehre.hora_grabacion as hora, ehre.codigo as codEncabezado,								" +
							  "						   blam.orden_dieta as codigoMed, 0 as paramMed, 'a' as tipoliquidoMed, blam.valor as valorMed, 1 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp			" +
							  "						   FROM balance_liq_admin_mezcla blam 			 																			" +		
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
							  "							   		 WHERE re.cuenta " + cadCuenta +
							  "									   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "									   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "					UNION ALL  " +
							  "					SELECT  getDatosMedico(ftbl.usuario) as medico,	" +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,				    " +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro||'' as hora_registro,   " +
							  " 					   ftbl.fecha as fecha, ftbl.hora as hora, dftlm.codigo_turno*-1 as codEncabezado,  										" +
							  "						   dftlm.orden_dieta as codigomed, 0 as paramMed, 'a' as tipoliquidoMed,  dftlm.total as valormed, 1 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp	 	" +  	
		  					  "						   FROM fin_turno_balance_liq ftbl																					   		" +
		  					  " 							INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
		  					  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
							  "									 WHERE re.cuenta " + cadCuenta +
							  "								   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "								   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							  //----------Parte de Mezclas.
							  "					UNION ALL  " +
							  "					SELECT getDatosMedico(ehre.usuario) as medico, " +
							  "						   to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion, 	" +
							  "						   to_char(ehre.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ehre.hora_registro||'' as hora_registro, 		" +
							  "						   ehre.fecha_grabacion as fecha, ehre.hora_grabacion as hora, ehre.codigo as codEncabezado,							  		" +
							  "		   				   mie.codigo as codigoMed, 0 as paramMed, 'a' as tipoliquidoMed, blae.valor as valorMed, 0 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp								  		" +
					  		  "						   FROM balance_liq_admin_enfer blae 																					 		" +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo )							  		" +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion ) 						  		" +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) 										  		" + 			    
							  "							   		 WHERE re.cuenta " + cadCuenta +
							  "									   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "									   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "					UNION ALL  " +
							  "					SELECT getDatosMedico(ehre.usuario) as medico,	" +
							  "						   to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion,	 " +
							  "						   to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ehre.hora_registro||''  as hora_registro,		 " + 
					  		  "						   ehre.fecha_grabacion as fecha,  ehre.hora_grabacion as hora,  ehre.codigo as codEncabezado, 								     " +
					  		  "						   laci.codigo as codigoMed, 1 as paramMed, 'a' as tipoliquidoMed,  blap.valor as valorMed, 0 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp 								 		 " +
							  "						   FROM balance_liq_admin_par blap 																						  		 " +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo )							  		 " +
							  "								INNER JOIN " + tablaAdm  + " laci ON ( laci.codigo = blap.liquidos_admin_cc_inst ) 						 				 " +
							  "								INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
							  "							   		 WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "										   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "						  				   AND re.cuenta " + cadCuenta +
							  "				 	UNION ALL						" + 
							  "					SELECT  getDatosMedico(ftbl.usuario) as medico,	" +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,				    " +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro||'' as hora_registro,   " +
							  " 					   ftbl.fecha as fecha, ftbl.hora as hora, codigo_turno*-1 as codEncabezado,  												" +
							  "						   dftlap.liquidos_admin_cc_inst as codigomed, 1 as paramMed,  'a' as tipoliquidoMed,  dftlap.total as valormed, 0 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp  			" +  	
							  " 						   FROM fin_turno_balance_liq ftbl  																					" +
							  "									INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo ) 							" +
							  " 								INNER JOIN " + tablaAdm  + " laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) 							" +
							  "									INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) 										" +
//							  "									INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = ftbl.registro_enfer ) 					    " +
							  "										 WHERE to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "									   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							  "						  				   AND re.cuenta " + cadCuenta +
							  "					UNION ALL  "+ 
							  "					SELECT  getDatosMedico(ftbl.usuario) as medico,	" +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,					" +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ftbl.hora_registro||'' as hora_registro, 	" +
							  "						   ftbl.fecha as fecha, ftbl.hora as hora, codigo_turno*-1 as codEncabezado, dftla.medicamento_infusion_enfer as codigomed, " +
							  "						   0 as paramMed, 'a' as tipoliquidoMed,  dftla.total as valormed, 0 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp 	  					   									" +
							  "						   FROM fin_turno_balance_liq ftbl " + 
							  "								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo ) " +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +
							  "								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo ) " +
		//					  "							    INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = ftbl.registro_enfer ) 					    " +
							  "									 WHERE re.cuenta " + cadCuenta +
							  "								   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  "								   	   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							  "				  ) x " +
							  " UNION ALL " +
							  "	SELECT * FROM (  SELECT getDatosMedico(ehre.usuario) as medico, " +
							  "							to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion, 	" +
							  "							to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ehre.hora_registro||''  as hora_registro, 		" +
							  "						    ehre.fecha_grabacion as fecha, ehre.hora_grabacion as hora,																	" +
							  "							ehre.codigo as codEncabezado, leci.codigo as codigoMed, 1 as paramMed, 'e' as tipoliquidoMed, blep.valor as valorMed, 0 as codMezclaMed" +
							  " 		                ,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp " +
							  "							  FROM balance_liq_elim_par blep  																							" + 			   
							  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) 								" +    
							  "							 	   INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) 									" +    
							  "								   INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) 											" +
							  "								   		 WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
							  "											   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
							  "						  					   AND re.cuenta " + cadCuenta +
							  "					UNION ALL	" +						   
							  "					SELECT getDatosMedico(ftbl.usuario) as medico," +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,						" +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro||'' as hora_registro,  		" +     
							  "						   ftbl.fecha as fecha, ftbl.hora as hora, dftlep.codigo_turno*-1 as codEncabezado, dftlep.liquidos_elim_cc_inst as codigoMed,	" +
							  "						   1 as paramMed,  'e' as tipoliquidoMed,   dftlep.total as valormed, 0 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp " +       	     
							  "						   FROM fin_turno_balance_liq ftbl " +   
							  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
							  "								INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
							  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
				//			  "							    INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.registro_enfer = ftbl.registro_enfer ) 					    " +
							  "									 WHERE to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
							  " 									   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
							  "						  				   AND re.cuenta " + cadCuenta +
							  "					) y" +
							  "	) z ORDER BY fecha_temp, hora_temp, codEncabezado desc  ";
			}
			break;
			case  4:
			{
					consulta    = "	SELECT * FROM (  SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registroMedElim, ehre.hora_registro||''  as hora_registroMedElim,  " +
								  "						    ehre.codigo as codEncabezadoMedElim, leci.codigo as codigoMedElim, 1 as paramMedElim, blep.valor as valorMedElim,  " + 				   
								  "							  ehre.fecha_grabacion as fechaMedElim, ehre.hora_grabacion as horaMedElim, 'e' as tipoLiquidoMedElim " +
								  "							  FROM balance_liq_elim_par blep  " + 			   
								  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
								  "							 	   INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +    
								  "								   INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
								  "								   		 WHERE to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro >= '" + fechaInicio + "'" +
								  "											   AND to_char(ehre.fecha_registro,'yyyy-mm-dd') ||'-'|| ehre.hora_registro < '" + fechaFinal + "'" +
								  "						  					   AND re.cuenta " + cadCuenta +
								  "					UNION ALL	" +						   
								  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY')  as fecha_registroMedElim, ftbl.hora||'' as hora_registroMedElim,  " +     
								  "						   dftlep.codigo_turno*-1 as codEncabezadoMedElim, dftlep.liquidos_elim_cc_inst as codigoMedElim, 1 as paramMedElim, dftlep.total as valorMedElim, " +       	
								  "						   ftbl.fecha as fechaMedElim, ftbl.hora as horaMedElim, 'e' as tipoLiquidoMedElim   " +      
								  "						   FROM fin_turno_balance_liq ftbl " +   
								  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
								  "								INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
								  "								   INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
								  "									 WHERE to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro >= '" + fechaInicio + "'" +
								  " 									   AND to_char(ftbl.fecha_registro,'yyyy-mm-dd')||'-'||ftbl.hora_registro < '" + fechaFinal + "'" +
								  "						  				   AND re.cuenta " + cadCuenta +
								  "					) y ORDER BY fecha_registroMedElim, hora_registroMedElim ";
			}		
		}
		
		try
		{
			if(nroConsulta==3)
				logger.info("\n\n\n NroConsulta Histo [ " +  nroConsulta  + " ] Consulta (consultarLiqMedicamentosPaciente) ----> " + consulta + "\n\n");				

			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error en cargarHistoricosDieta de SqlBaseRegistroEnfermeriaDao: "+e);
			return null;
		}
		
	}
	
	
	
	
	/**
	 * Metodo que retorna el la ultima fecha de finalizacion del turno.
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String  ultimaHoraFinTurno (Connection con, int codigoCuenta) 
	{
		PreparedStatementDecorator ps;
		String cad = "		SELECT ftbl.fecha || '-' ||  ftbl.hora as fecha    " + 
			   		 "			   FROM fin_turno_balance_liq ftbl   " +
			   		 "					INNER JOIN registro_enfermeria re ON  ( re.codigo = ftbl.registro_enfer )  " +
			   		 "					     WHERE	re.cuenta =  " + codigoCuenta +
					 "	 					  	   ORDER BY fecha DESC ";
		try
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
				if(resultado.next())
				{
					return resultado.getString("fecha");
				}
				else
				{
					return "";
				}
			}
			catch(SQLException e)
			{
					logger.warn(" Error Consultanto la fecha y hora de la ultima fecha de finalización del turno : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					return "";
			}
	}

	
	/**
	 * Metodo para guardar el detalle del balance de liquidos
	 * @param con
	 * @param parametrizado
	 * @param codEncabezado
	 * @param codigoMedicamento
	 * @param valor
	 * @return
	 */
	public static int insertarBalanceLiquidos(Connection con, int parametrizado, int codEncabezado, int codigoMedicamento, float valor)
    {
		PreparedStatementDecorator ps;
		String cad = "";

		//logger.info("\n\n (insertarBalanceLiquidos)  Parametrizado "  + parametrizado  + " codigoMed  [" + codigoMedicamento + "]  cantidad  ["  + valor + "] \n");

		
		try
			{
				if (parametrizado == 1) //-Si son tipos parametrizados (hasta el momento via oral)
				{
					cad = " INSERT INTO balance_liq_admin_par (codigo_histo_enfer, liquidos_admin_cc_inst, valor) " +
						  "				VALUES (?, ?, ?) ";	
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codEncabezado);				
					ps.setInt(2, codigoMedicamento);
					ps.setDouble(3, Utilidades.convertirADouble(valor+""));
					return ps.executeUpdate();
				}
				if (parametrizado == 0) //-Si son liquidos y medicamentos de infusión registrados por el usuario (No Parametrizado).  
				{
					
					
					cad = " INSERT INTO balance_liq_admin_enfer (codigo_histo_enfer, medicamento_infusion, valor) " +
						  "				VALUES (?, ?, ?) ";	
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codEncabezado);				
					ps.setInt(2, codigoMedicamento);
					ps.setDouble(3, Utilidades.convertirADouble(valor+""));
					return ps.executeUpdate();
				}
				if (parametrizado == 2) //-Si son tipos parametrizados de liquidos eliminados
				{
					cad = " INSERT INTO balance_liq_elim_par (codigo_histo_enfer, liquidos_elim_cc_inst, valor) " +
						  "				VALUES (?, ?, ?) ";	
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codEncabezado);				
					ps.setInt(2, codigoMedicamento);
					ps.setDouble(3, Utilidades.convertirADouble(valor+""));
					return ps.executeUpdate();
				}
				if (parametrizado == 3) //-Si son MEZCLAS.
				{
					cad = " INSERT INTO balance_liq_admin_mezcla (codigo_histo_enfer, orden_dieta, valor) " +
						  "				VALUES (?, ?, ?) ";	
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codEncabezado);				
					ps.setInt(2, codigoMedicamento);
					ps.setDouble(3, Utilidades.convertirADouble(valor+""));
					return ps.executeUpdate();
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de Balance de liquidos : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					return ConstantesBD.codigoNuncaValido;
			}
			return ConstantesBD.codigoNuncaValido;
    }

	/**
	 * Metodo para finalizar el turno de enfermeria
	 * @param con
	 * @param secuencia
	 * @param codigoRegistroEnferia
	 * @param horaRegistro
	 * @param Registro
	 * @return
	 */
	public static int finalizarTurnoBalanceLiquidos(Connection con, String secuencia, int codigoRegistroEnferia, String fechaRegistro, String horaRegistro, String loginUsuario) 
	{
		PreparedStatementDecorator ps;
		String cad = "";
						
		try	{
					int cod = obtenerCodigoSecuencia(con, secuencia);
					cad = " INSERT INTO fin_turno_balance_liq ( codigo, " +
															"registro_enfer, " +
															"fecha, " +
															"hora, " +
															"fecha_registro, " +
															"hora_registro, " +
															"usuario) VALUES (?, ?, CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?)  ";
					if (cod > 0)
					{
						ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, cod);				
						ps.setInt(2, codigoRegistroEnferia);
						ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)));
						ps.setString(4, horaRegistro);
						ps.setString(5, loginUsuario);
						int res = ps.executeUpdate(); 
					    if (res > 0) { return cod; }
					    else { return res; }
					} 
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de finalizar turno de enfermeria : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					return ConstantesBD.codigoNuncaValido;
			}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo para insertar el detalle del la finalizacion del turno.
	 * @param con
	 * @param codigoTurno
	 * @param codigoMedicamento
	 * @param valor
	 * @param fechaUltimoFinTurno
	 * @param tabla
	 * @return
	 */
	public static int insertarDetFinTurnoBalanceLiquidos(Connection con, int codigoTurno, int codigoMedicamento, float valor, int tabla)
    {
		PreparedStatementDecorator ps;
		String cad = "";
						
		try 
			{
				if ( tabla == 0 )
				{
				 cad = " INSERT INTO detalle_fin_turno_liq_admin ( codigo_turno, medicamento_infusion_enfer, total) VALUES (?, ?, ?)  ";
				}
				if ( tabla == 1 )
				{
				 cad = " INSERT INTO det_fin_turno_liq_adm_par ( codigo_turno, liquidos_admin_cc_inst, total) VALUES (?, ?, ?)  ";
				}
				if ( tabla == 2 )
				{
				 cad = " INSERT INTO detalle_fin_turno_liq_elim ( codigo_turno, liquidos_elim_cc_inst, total) VALUES (?, ?, ?)  ";
				}
				if ( tabla == 3 )
				{
				 cad = " INSERT INTO detalle_fin_turno_liq_mezcla ( codigo_turno, orden_dieta, total) VALUES (?, ?, ?)  ";
				}
				
				//logger.info("\n\n insertarDetFinTurnoBalanceLiquidos  [" + cad +"] [" + codigoTurno + "] [" +codigoMedicamento + "] [" + valor+ "] \n\n");
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoTurno);				
				ps.setInt(2, codigoMedicamento);
				ps.setDouble(3, Utilidades.convertirADouble(valor+""));

				return ps.executeUpdate(); 
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de finalizar turno de enfermeria : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					return ConstantesBD.codigoNuncaValido;
			}
    }

	
    /**
 	 * Funcion para retornar una collecion con el listado de los tipos parametrizados
 	 * por centro de costo e institución en el registro de enfermería 
 	 * @param con
     * @param codigo de la institucion
     * @param codigo del centroCosto
     * @param codigoCuenta
     * @param cuentaAsocio
     * @param Nro Consulta parametro que indica la informacion a sacar
 	 *        1  Listado de tipos de signos vitales de enfermería        
 	 * @return Collection 
 	 */

      public static Collection consultarTiposInstitucionCCosto(Connection con, int institucion,String cuentas, int nroConsulta)
      {
    	  String consultaStr="", cuentaOrden="", cuentaRegistro="";
    	  boolean cuentaAsociada=false;
    	  String centroCosto="";
    	  String [] cuentasTmp=cuentas.split(",");
    	  Vector centrosCosto = new Vector();
    	 if (cuentasTmp.length>1)
    		 	cuentaAsociada=true;
    	  
    	 //se consultas los centros de costo
    	 
    	 for (int i=0;i<cuentasTmp.length;i++)
    		centrosCosto.add(UtilidadesHistoriaClinica.obtenerCentroCostoCuenta(con, Utilidades.convertirAEntero(cuentasTmp[i]+"")));
    	 	
    	 centroCosto=UtilidadTexto.convertirVectorACodigosSeparadosXComas(centrosCosto, false);
    	      	  
    	  
    	  final String consultarTiposSoporteStr="SELECT tsrci.codigo AS codigo, tsr.nombre AS nombre,tsr.tipo AS tipo_dato, tsr.codigo AS codigo_tipo " +
									"FROM soporte_respira_cc_inst tsrci " +
										"INNER JOIN tipo_soporte_respira tsr ON (tsrci.tipo_soporte_respira=tsr.codigo) " +
											"WHERE tsrci.centro_costo IN ("+centroCosto+") AND tsrci.institucion="+institucion+" AND tsrci.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY tsr.nombre";
    	  
    	  final String consultarTiposSoporteAsocioStr="SELECT tsrci.codigo AS codigo, tsr.nombre AS nombre,tsr.tipo AS tipo_dato, tsr.codigo AS codigo_tipo FROM "+ 
							"( "+
									"SELECT * from soporte_respira_cc_inst "+
										"WHERE institucion="+institucion+" AND centro_costo IN("+centroCosto+") AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
								"UNION "+
									"SELECT * from soporte_respira_cc_inst "+
										"WHERE institucion="+institucion+" AND centro_costo="+Utilidades.convertirAEntero(ValoresPorDefecto.getCentroCostoUrgencias(institucion))+" AND tipo_soporte_respira NOT IN "+
										"(SELECT tipo_soporte_respira from soporte_respira_cc_inst WHERE institucion="+institucion+" AND centro_costo IN ("+centroCosto+") AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+") "+
										") tsrci	INNER JOIN tipo_soporte_respira tsr ON (tsrci.tipo_soporte_respira=tsr.codigo)  "+
								"ORDER BY tsr.nombre";
    	  
    	 
  			cuentaOrden = " WHERE om.cuenta IN ( "+ cuentas+" ) ";
  			cuentaRegistro = " WHERE re.cuenta IN ( "+ cuentas+" ) ";
    	  
			
		//-Seleccionar el tipo de informacion a consultar
		    switch(nroConsulta)
			{
		    	//--------------------- Signos Vitales--- -----------------------//
		        case 1:
		        		consultaStr="SELECT svic.codigo AS codigo,sv.nombre AS nombre,sv.unidad_medida AS unidad_medida, sv.codigo AS codigo_tipo " +
		        									"		FROM signos_vitales sv " +
		        									"			INNER JOIN (" +
		        									"								SELECT svci.codigo AS codigo, svci.signo_vital AS codigo_tipo " +
		        									"									FROM signos_vitales_cc_inst svci" +
		        									"										INNER JOIN signos_vitales sv ON (svci.signo_vital=sv.codigo) " +
		        									"										INNER JOIN especialidades_val ev ON (sv.codigo_especialidad=ev.codigo)" +
		        									"											WHERE ev.codigo="+ConstantesBD.codigoEspecialidadValoracionEnfermeria+
		        									"														AND svci.centro_costo IN ("+centroCosto+") "+
		        									" 														AND svci.institucion="+institucion+
		        									" 														AND svci.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
		        									"								UNION"+
		        									"								SELECT svrep.signo_vital_cc_inst AS codigo,svci.signo_vital AS codigo_tipo" +
		        									"									FROM signo_vit_reg_enfer_par svrep" +
		        									"										INNER JOIN signos_vitales_cc_inst svci ON (svrep.signo_vital_cc_inst=svci.codigo)" +
		        									"										INNER JOIN enca_histo_registro_enfer ehre ON (ehre.codigo=svrep.codigo_histo_enca)" +
		        									"										INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)" +
		        																					cuentaRegistro+
		        									"													GROUP BY svrep.signo_vital_cc_inst,svci.signo_vital" +
		        									"								)svic ON (svic.codigo_tipo=sv.codigo)" +
		        									"			ORDER BY sv.nombre";
		        		break;
				
				//--------------------- Soportes Respiratorios -----------------------//			
		        case 2:
		        	if(cuentaAsociada)
	        		{
		        		consultaStr=consultarTiposSoporteStr;
	        		}
		        	else
		        	{
		        		consultaStr=consultarTiposSoporteAsocioStr;
		        	}
					break;
						
				//--------------------- Columnas de catéteres y sondas  -----------------------//			
		        case 3:
	        		consultaStr="SELECT ccsci.codigo AS codigo,ccs.nombre AS nombre, ccs.codigo AS codigo_tipo " +
	        								"		FROM columna_cateter_sonda ccs" +
	        								"			INNER JOIN (" +
	        								"								SELECT codigo AS codigo, columna_cateter_sonda AS codigo_tipo" +
	        								"									FROM col_cateter_sonda_cc_ins " +
	        								"										WHERE centro_costo IN (" +centroCosto+ ") "+
	        								" 											AND institucion="+institucion+" " +
	        								"											AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
	        								"								UNION" +
	        								"								SELECT dcsp.col_cateter_sonda_cc_ins AS codigo, ccsci.columna_cateter_sonda AS codigo_tipo" +
	        								"										FROM detalle_cat_sonda_param dcsp" +
	        								"											INNER JOIN col_cateter_sonda_cc_ins ccsci ON (dcsp.col_cateter_sonda_cc_ins=ccsci.codigo)" +
	        								"											INNER JOIN cateter_sonda_reg_enfer csre ON (dcsp.cateter_sonda_reg_enfer=csre.codigo)" +
	        								"											INNER JOIN enca_histo_registro_enfer ehre ON (ehre.codigo=csre.codigo_histo_enfer)" +
	        								"											INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)"+
	        																					cuentaRegistro+
	        								"												GROUP BY dcsp.col_cateter_sonda_cc_ins, ccsci.columna_cateter_sonda"+
	        								")ccsci ON (ccsci.codigo_tipo=ccs.codigo)" +
	        								"				ORDER BY ccs.nombre";
					break;
				//--------------------- Exámenes Físicos  -----------------------//			
		        case 4:
	        		consultaStr="SELECT efeci.codigo AS codigo,tef.nombre AS nombre, tef.codigo AS codigo_tipo " +
	        								"		FROM tipo_examen_fisico_enfer tef" +
	        								"			INNER JOIN (" +
	        								"								SELECT codigo AS codigo, tipo_examen_fisico_enfer AS codigo_tipo" +
	        								"									FROM examen_fisico_enf_cc_ins" +
	        											"								WHERE centro_costo IN ("+centroCosto+") "+
	        								" 														  AND institucion="+institucion+
	        								"														 AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
	        								"								UNION" +
	        								"								SELECT efre.examen_fisico_enf_cc_inst AS codigo, efeci.tipo_examen_fisico_enfer AS codigo_tipo" +
	        								"									FROM examen_fisico_reg_enfer efre " +
	        								"											INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo)" +
	        								"											INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo)"+
	        																					cuentaRegistro+
	        								"												GROUP BY efre.examen_fisico_enf_cc_inst, efeci.tipo_examen_fisico_enfer"+
	        								"								 )efeci ON (efeci.codigo_tipo=tef.codigo)" +
	        								"			ORDER BY tef.nombre";
					break;
					
				//--------------------- Articulos(insumos) de cateter sonda pametrizados por institución ------------------//
				case 5:
					consultaStr = "SELECT acsi.codigo AS codigo, art.descripcion AS nombre " +
												"		FROM articulo art " +
												"			INNER JOIN articulo_cat_sonda_ins acsi ON (acsi.articulo=art.codigo) " +
												"				WHERE acsi.institucion="+institucion+
												"							 AND acsi.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
					break;

				// ---------------------- Opciones select de soporte respiratorio ------------------------------------//
			    case 6:
		        	if(cuentaAsociada)
	        		{
		        		consultaStr="SELECT osr.codigo AS codigo, osr.acronimo AS acronimo, osr.descripcion AS descripcion, osr.tipo_soporte_cc_inst AS tipo, tsrci.codigo AS equipo" +
		        					" FROM ("+consultarTiposSoporteStr+") tsrci INNER JOIN opciones_soporte_respira osr ON (osr.tipo_soporte_cc_inst=tsrci.codigo)" ;
	        		}
		        	else
		        	{
		        		consultaStr="SELECT osr.codigo AS codigo, osr.acronimo AS acronimo, osr.descripcion AS descripcion, osr.tipo_soporte_cc_inst AS tipo, tsrci.codigo AS equipo" +
		        					" FROM ("+consultarTiposSoporteAsocioStr+") tsrci INNER JOIN opciones_soporte_respira osr ON (osr.tipo_soporte_cc_inst=tsrci.codigo)" ;
		        	}
			    	//logger.info("OPCIONES SOPORTE --> "+consultaStr);
	        		break;
					
				case 7:
					//--------------Cuidados Especiales de enfermería por centro de costo institución -------------------------------------//
						consultaStr="SELECT * FROM (SELECT  ceci.codigo as codigo, tce.descripcion  as descripcion, 0 as es_otro, tce.codigo AS codigo_tipo, " +
									"coalesce(getcodigopkfreccuidadoenfer(coalesce(getingresoxcuenta("+cuentasTmp[0]+"),"+ConstantesBD.codigoNuncaValido+"),ceci.codigo,0,'"+ConstantesBD.acronimoSi+"'),"+ConstantesBD.codigoNuncaValido+") AS codigopkfreccuidenfer,tce.control_especial as controlespecial "+
												"									FROM tipo_cuidado_enfermeria tce "+
												"										INNER JOIN (" +
												"															SELECT codigo as codigo, cuidado_enfermeria as codigo_tipo " +
												"																   FROM cuidado_enfer_cc_inst  " +
												"																			WHERE activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + 
												"																		   	  AND institucion = " + institucion +
												"																			  AND centro_costo IN (" + centroCosto + ") "+    
												"															 UNION " +
												"															SELECT dcre.cuidado_enfer_cc_inst AS codigo,ceci.cuidado_enfermeria AS codigo_tipo" +
												"																	FROM detalle_cuidado_reg_enfer dcre " +
												"																			INNER JOIN enca_histo_registro_enfer ehre ON (ehre.codigo=dcre.codigo_histo_enfer)" +
												"																			INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)" +
												"																			INNER JOIN cuidado_enfer_cc_inst ceci ON (dcre.cuidado_enfer_cc_inst=ceci.codigo)" +
																																	cuentaRegistro+
												"																						GROUP BY dcre.cuidado_enfer_cc_inst,ceci.cuidado_enfermeria" +
												"															UNION " +
												"																	SELECT dce.cuidado_enfer_cc_inst as codigo, ceci.cuidado_enfermeria as codigo_tipo " +
												"																		   FROM detalle_cuidado_enfer dce  " +
												"																				 INNER JOIN encabezado_histo_orden_m eho ON (eho.codigo=dce.cod_histo_enca) " + 
												"																				 INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo)  " +
												"																			     INNER JOIN cuidado_enfer_cc_inst ceci ON (dce.cuidado_enfer_cc_inst = ceci.codigo)" +
																															cuentaOrden+
												"																					GROUP BY dce.cuidado_enfer_cc_inst, ceci.cuidado_enfermeria	"+
												"														 )ceci ON (ceci.codigo_tipo=tce.codigo)"+
												"  							UNION ALL "+
												" 							SELECT toce.codigo as codigo, toce.descripcion as descripcion, 1 as es_otro, toce.codigo  AS codigo_tipo, "+
												"							coalesce(getcodigopkfreccuidadoenfer(coalesce(getingresoxcuenta("+cuentasTmp[0]+"),"+ConstantesBD.codigoNuncaValido+"),toce.codigo,1,'"+ConstantesBD.acronimoSi+"'),"+ConstantesBD.codigoNuncaValido+") AS codigopkfreccuidenfer,'N' as controlespecial "+
												"								FROM tipo_otro_cuidado_enf toce "+
												"										INNER JOIN detalle_otro_cuidado_enf doce ON (doce.otro_cual=toce.codigo)  "+
												"										INNER JOIN encabezado_histo_orden_m eho ON (eho.codigo=doce.cod_histo_cuidado_enfer)  "+    
												"										INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo) "+
																						cuentaOrden+
												"											GROUP BY toce.codigo,toce.descripcion)ce  " +
												"						Order By controlespecial desc,ce.descripcion";
					break;

				case 8:
					consultaStr="SELECT mensaje FROM mensaje_anota_enfer WHERE institucion="+institucion;
				break;
				
				//-----------Se consultan las características y especificaciones Glasgow por institución centro de costo-----------//
				case 9:
					consultaStr="SELECT eg.codigo AS codigo, eg.glasgow_cc_inst AS codigo_glasgow,tg.nombre AS nombre_glasgow," +
											"				eg.tipo_especificacion_glasgow AS codigo_especificacion, eg.valor AS valor_especificacion, teg.nombre AS nombre_especificacion"+
											" FROM especificaciones_glasgow eg "+ 
											" 		INNER JOIN (" +
											"							SELECT codigo AS codigo, tipo_glasgow AS codigo_tipo" +
											"									FROM	tipo_glasgow_cc_inst" +
											"										WHERE institucion="+institucion+
											" 												AND centro_costo IN ("+centroCosto+") "+
											"												AND activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
											"							UNION" +
											"							SELECT eg.glasgow_cc_inst AS codigo,tgci.tipo_glasgow AS codigo_tipo" +
											"								FROM especificaciones_glasgow eg" +
											"									INNER JOIN tipo_glasgow_cc_inst tgci ON (eg.glasgow_cc_inst=tgci.codigo)" +
											"									INNER JOIN detalle_escala_glasgow deg ON (deg.especificacion_glasgow=eg.codigo)" +
											"									INNER JOIN enca_histo_registro_enfer ehre ON (ehre.codigo=deg.enca_histo_reg_enfer)" +
	        								"									INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)"+
	        																			cuentaRegistro+
	        								"											GROUP BY eg.glasgow_cc_inst,tgci.tipo_glasgow"+
											"							)tgci ON (tgci.codigo=eg.glasgow_cc_inst)"+ 
											" 		INNER JOIN tipo_glasgow tg ON (tgci.codigo_tipo=tg.codigo) "+  
											" 		INNER JOIN tipo_especificacion_glasgow teg ON (eg.tipo_especificacion_glasgow=teg.codigo) "+ 
											"				ORDER BY eg.glasgow_cc_inst,eg.valor DESC";
				break;
				
				//--   Tamaños de las pupilas
				case 10:
					consultaStr="SELECT t.valor AS codigo, descripcion AS nombre, abreviatura AS abreviatura FROM descripcion_abrev_pupila da INNER JOIN tamano_pupila t ON(da.codigo=t.descripcion_abreviatura)";
				break;
				
				//---- Reacciones pupilas
				case 11:
					consultaStr="SELECT acronimo AS codigo, nombre AS nombre FROM reaccion_pupila";
				break;
				//-----------Se consultan las especificaciones Glasgow por institución centro de costo para la Impresión HC-----------//
				case 12:
					consultaStr=" SELECT * FROM " +
							    "	   ( "+
								"		SELECT tgci.codigo AS codigo, tgci.tipo_glasgow AS codigo_tipo, tg.nombre AS nombre_glasgow" +
								"			FROM tipo_glasgow_cc_inst tgci " +
								"			  INNER JOIN tipo_glasgow tg ON (tgci.tipo_glasgow=tg.codigo)" +
								"				WHERE tgci.institucion="+institucion+
								" 		 			  AND tgci.centro_costo IN ("+centroCosto+") "+
								"					  AND tgci.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+
								"		UNION" +
								"		SELECT eg.glasgow_cc_inst AS codigo,tgci.tipo_glasgow AS codigo_tipo,tg.nombre AS nombre_glasgow" +
								"			FROM especificaciones_glasgow eg" +
								"				INNER JOIN tipo_glasgow_cc_inst tgci ON (eg.glasgow_cc_inst=tgci.codigo)" +
								"				INNER JOIN detalle_escala_glasgow deg ON (deg.especificacion_glasgow=eg.codigo)" +
								"				INNER JOIN enca_histo_registro_enfer ehre ON (ehre.codigo=deg.enca_histo_reg_enfer)" +
	        					"				INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)" +
	        					"				INNER JOIN tipo_glasgow tg ON (tgci.tipo_glasgow=tg.codigo)"+
	        										cuentaRegistro+
	        					"		GROUP BY eg.glasgow_cc_inst,tgci.tipo_glasgow,tg.nombre"+
								"	   )x" +
								"	ORDER BY x.codigo";
				break;
					
		        default :
				{
					logger.warn(" [ERROR] No esta indicando ningun tipo de consulta el rango normal es [1-12]"+ nroConsulta + "\n\n" );
					return null;
				}
			}//switch
		    
		    PreparedStatementDecorator ps = null;
		    ResultSetDecorator rs = null; 
		    try
			{
		    	//logger.info("\n\n\n*****************************************************************************************************************************\n\n\n");
		    	//logger.info("nroConsulta-->"+nroConsulta);
		    	//logger.info("Centro costo-->"+centroCosto);
		    	//logger.info("Institucion-->"+institucion);
		    	logger.info("\n\n\n La sentencia SQL BASE (tipos)" + consultaStr + " \n\n\n");
		    	//logger.info("\n\n\n-----------------------------------------------------------------------------------------------------------------------------\n\n\n");
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
				
				//Si la consulta es diferente de la cinco se envían a la consulta el centro de costo y la institución
				/*if(nroConsulta!=5 && nroConsulta!=7)
					{	
					ps.setInt(1, centroCosto);
					ps.setInt(2, institucion);
					}
				else if (nroConsulta==7)
					{
					ps.setInt(1, institucion);
					ps.setInt(2, centroCosto);
					ps.setInt(3, codigoCuenta);
					
					if(cuentaAsocio != 0)
						ps.setInt(4, cuentaAsocio);
					}
				else
				{
					ps.setInt(1, institucion);
				}*/
				
				rs = new ResultSetDecorator(ps.executeQuery());
				return UtilidadBD.resultSet2Collection(rs);			
				
				
			}
		    catch (SQLException e)
			{
				logger.error("Error Consultado tipos parametrizados por institución centro de costo en Reg Enfermeria Nro Consulta ["+nroConsulta+"] "+e.toString()+" "+consultaStr);
				return null;
			}
		    finally{
		    	UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		    }
      	
      }

      
     
     /**
      * Actualiza el indicador de Nota finalizada
      * @param Connection con 
      * @param HashMap parametros
      * */
     public static boolean actualizarRegistroEnfermeria(Connection con, HashMap parametros)
     {
    	 logger.info("parametros finalizar Registro de Enfermeria >> "+parametros);
    	 
    	 String cadena = "UPDATE registro_enfermeria SET nota_finalizada = ? WHERE codigo = ? ";
    	 try
    	 {
    		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		 ps.setString(1,parametros.get("finalizada").toString());
    		 ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
    		 
    		 if(ps.executeUpdate()>0)
    			 return true;
    	 }
    	 catch (Exception e) {
			e.printStackTrace();
		}
    	 
    	 return false;
     }

  	/**
  	 * Metodo para insertar en la tabla principal sobre el registro de enfermeria.
	 * @param con
	 * @param secuencia
	 * @param cuenta
	 * @return
	 */
	public static int insertarRegistroEnfermeria(Connection con, String secuencia, int cuenta) {
		{
			PreparedStatementDecorator ps;
			int resp=0, codigo=0;
			String cad = "";
							
			try
				{
					codigo = existeRegistroEnfermeria(con,cuenta).getCodigo();
					if ( codigo == -2 ) {return -1;} 
					
					if ( codigo > 0  )  
					{  
						return  codigo;  //--retorna el codigo del registro de enfermeria 
					}
					else
					{
						codigo = obtenerCodigoSecuencia(con, secuencia);
						if (codigo != ConstantesBD.codigoNuncaValido) 
						{
							cad = " INSERT INTO registro_enfermeria (codigo,cuenta) VALUES (?, ?) ";
							ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							//logger.info("--->"+cad+"--"+codigo+"--"+cuenta);
							ps.setInt(1, codigo);				
							ps.setInt(2, cuenta);
							resp = ps.executeUpdate();
							if (resp > 0)
							{
								resp = codigo;
							}
						}
						else
						{
							return ConstantesBD.codigoNuncaValido;
						}
					}
				}
				catch(SQLException e)
				{
						logger.warn(e+" Error en la inserción de datos Orden Medica General : SqlBaseRegistroEnfermeriaDao "+e.toString() );
						resp = 0;
				}
				return resp;
		}	
	}

	/**
	 * Metodo para insertar los datos del encabezado de registro de enfermeria
	 * @param con
	 * @param codigoRegistroEnfermeria
	 * @param secuencia
	 * @param fechaRegistro
	 * @param horaRegistro
	 * @param loginUsuario
	 * @param datosMedico
	 * @param obsSoporte Observaciones de soporte respiratorio
	 * @return
	 */
	public static int insertarEncabezadoRegistroEnfermeria(Connection con, int codigoRegistroEnfermeria, String secuencia, String fechaRegistro, String horaRegistro, String loginUsuario, String datosMedico, String obsSoporte)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String cad = "";
						
		try
			{
					codigo = obtenerCodigoSecuencia(con, secuencia); 
					if ( codigo != -1 )
					{
						cad = " INSERT INTO enca_histo_registro_enfer " +
								"(codigo, " +
								"registro_enfer, " +
								"fecha_grabacion, " +
								"hora_grabacion, " +
								"fecha_registro, " +
								"hora_registro, " +
								"usuario, " +
								"datos_medico, " +
								"obs_soporte) " +
								"			VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?) ";
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigo);				
						ps.setInt(2, codigoRegistroEnfermeria);
						ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)));
						ps.setString(4, horaRegistro);
						ps.setString(5, loginUsuario);
						ps.setString(6, datosMedico);
						ps.setString(7, obsSoporte);

						resp = ps.executeUpdate();
						if (resp > 0)
						{
							resp = codigo;
						}
					}	
					else
					{
						return ConstantesBD.codigoNuncaValido;
					}
			}
			catch(SQLException e)
			{
					logger.error(" Error en la inserción insertarEncabezadoRegistroEnfermeria "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
	}

	
	/**
	 * Método para saber si existe o no el registro de enfermeria para el paciente. 
	 * @param con -> conexion
	 * @param cuenta
	 * @return codigo si existe sino retorna -1
	 */
	public static InfoDatosInt existeRegistroEnfermeria(Connection con, int cuenta)
	{
		InfoDatosInt resultadoInt = new InfoDatosInt(ConstantesBD.codigoNuncaValido,ConstantesBD.acronimoNo);
		
		String consulta="SELECT codigo as codigo,CASE WHEN nota_finalizada IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE nota_finalizada END AS nota_finalizada  from registro_enfermeria where cuenta = ? ";
		
		try
		{
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			obtenerCodigoStatement.setInt(1, cuenta);
			
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				resultadoInt.setCodigo(resultado.getInt("codigo"));
				resultadoInt.setDescripcion(resultado.getString(2));
				return resultadoInt;
			}
			else
			{
				resultadoInt.setCodigo(ConstantesBD.codigoNuncaValido);
				resultadoInt.setDescripcion(ConstantesBD.acronimoNo);
				return resultadoInt;				
			}	
		}
		catch(SQLException e)
		{
			logger.warn(" Vericando Existencia de Registro de Enfermeria : SqlBaseRegistroEnfermeriaDao "+e.toString());
			return resultadoInt;
		}
	}
	/**
	 * Metodo 
	 * @param con
	 * @param secuencia
	 * @return
	 */
	public static int obtenerCodigoSecuencia (Connection con, String secuencia)
	{
		String consultaSecuencia="SELECT "+secuencia;
		if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
		{
			consultaSecuencia=consultaSecuencia+" FROM DUAL";
		}
		try
		{
			
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo");
			}
			else
			{
				return ConstantesBD.codigoNuncaValido;
			}
		}
		catch(SQLException e)
		{
			logger.warn(" Error en la consulta del codigo del registro de enfermeria : SqlBaseRegistroEnfermeria "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}
	}


	/**
	 * Metodo para insertar la parte de liquiquidos y medicamentos de infusion de la seccion Dieta.
	 * @param con
	 * @param Codigo de la cuenta del paciente. 
	 * @param secuencia
	 * @param codigoRegistroEnferia
	 * @param descripcion
	 * @param volumen
	 * @param velocidad
	 * @param suspendido
	 * @return
	 */
	public static int insertarLiqMedInfusion(Connection con, int codigoCuenta, String secuencia, int codigoRegistroEnferia, String descripcion, String volumen, String velocidad, boolean suspendido, int tipoOperacion) 
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0, codigoMedicamento = 0;
		String cad = "";
						
		try
			{
					if (tipoOperacion == 0) //-Para inserciones 
					{
						codigo = obtenerCodigoSecuencia(con, secuencia);
						codigoMedicamento = obtenerCodigoMedicamento(con, codigoCuenta);
						
						if ( codigo != ConstantesBD.codigoNuncaValido && codigoMedicamento != ConstantesBD.codigoNuncaValido )
						{
							cad = " INSERT INTO medicamento_infusion_enfer (codigo, " +
																			"registro_enfer, " +
																			"consecutivo_liquido, " +
																			"descripcion, " +
																			"volumen_total, " +
																			"velocidad_infusion, " +
																			"suspendido) " +
									"								VALUES (?, ?, ?, ?, ?, ?, ?) ";
							
							ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, codigo);				
							ps.setInt(2, codigoRegistroEnferia);
							ps.setInt(3, codigoMedicamento);
							ps.setString(4, descripcion) ;
							ps.setDouble(5, Utilidades.convertirADouble(volumen)) ;
							ps.setString(6, velocidad) ;
							ps.setBoolean(7, suspendido);
							resp = ps.executeUpdate();
						}	
						else
						{
							return ConstantesBD.codigoNuncaValido;
						}
					}
					
					//-----------------Para eliminaciones
					if (tipoOperacion == 1)   
					{
							cad = " DELETE FROM medicamento_infusion_enfer WHERE codigo = ? ";
							ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, codigoCuenta);  //----Estoy aprovechando el campo cuenta para enviar el codigo del medicamento. 
							return ps.executeUpdate();
					}

					//-----------------Para Modificaciones Generales del medicamento.
					if (tipoOperacion == 2)   
					{
							cad = " UPDATE medicamento_infusion_enfer SET descripcion = ?, volumen_total = ?, velocidad_infusion = ?  WHERE codigo = ? ";
							ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setString(1, descripcion);       
							ps.setDouble(2, Utilidades.convertirADouble(volumen) ) ;
							ps.setString(3, velocidad ) ;
							ps.setInt(4, codigoCuenta);  //----Estoy aprovechando el campo cuenta para enviar el codigo del medicamento.
							return ps.executeUpdate();
					}
					//-----------------Para Suspender el medicamento.
					if (tipoOperacion == 3)   
					{
							cad = " UPDATE medicamento_infusion_enfer SET suspendido = ? WHERE codigo = ? ";
							ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setBoolean(1, suspendido);
							ps.setInt(2, codigoCuenta);  //----Estoy aprovechando el campo cuenta para enviar el codigo del medicamento.
							return ps.executeUpdate();
					}
					//-----------------Para Suspender PERO CONFIRMANDO LA ELIMINACION EN ORDEN DIETA.
					if (tipoOperacion == 4)   
					{
							cad = " UPDATE orden_dieta SET suspendido = ? WHERE codigo_histo_enca = ? ";
							//logger.info("\n\n [" + cad +  "]  [" + suspendido +"] [" + codigoCuenta +"] \n\n");
							ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setBoolean(1, suspendido);
							ps.setInt(2, codigoCuenta);  	//----Estoy aprovechando el campo cuenta para enviar el codigo del Encabezado de OrdenMedica
							return ps.executeUpdate();
					}
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la inserción en insertarLiqMedInfusion  : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
	}
	
	
	/**
	 * Actualiza las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap actualizarObservacionesMezcla(Connection con, HashMap parametros)
	{		
		String cadena = "SELECT " +
						"o.descripcion_dieta_par," +
						"o.descripcion_dieta_enfermera " +
						"FROM ordenes_medicas o " +
						"INNER JOIN solicitudes s ON (s.numero_solicitud = ? AND o.cuenta = s.cuenta) ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{	
			e.printStackTrace();
		}
		
		return new HashMap();		
	}
	
	
	
	
	/**
	 * Metodo para retornar el codigo del medicamento por paciente que sigue a insertar 
	 * @param con
	 * @param codigo de la cuenta del paciente.
	 * @return
	 */
	private static int obtenerCodigoMedicamento(Connection con, int  codigoCuenta) 
	{
		String cadena = "SELECT MAX (consecutivo_liquido) as codigo " +
						"		FROM medicamento_infusion_enfer mie " +
						"			 INNER JOIN registro_enfermeria re ON (re.codigo = mie.registro_enfer) " +
						"				  WHERE  re.cuenta = " + codigoCuenta;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			
			if(resultado.next())
			{
				return resultado.getInt("codigo") + 1;
			}
			else
			{
				return 1;
			}
		}
		catch(SQLException e)
		{
			logger.warn(" Error en la consulta del proximo codigo del medicamento_infusion_enfer : SqlBaseRegistroEnfermeria "+e.toString());
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
    /**
	 * Método para insertar los exámenes fìsicos del registro de enfermería
	 * @param con una conexion abierta con una fuente de datos
	 * @param codRegEnfer
	 * @param examenFisicoCcIns
	 * @param valorExamenFisico
	 * @return 
	 */
	public static int insertarExamenesFisicos (Connection con, int codRegEnfer, int examenFisicoCcIns, String valorExamenFisico)
	{
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String consultaStr = "";
		
		try
		{
			codigo = existeExamenFisico (con, codRegEnfer, examenFisicoCcIns);
			
			if ( codigo != ConstantesBD.codigoNuncaValido  )  
			{  
				 //-Si existe se modifica
				consultaStr = " UPDATE examen_fisico_reg_enfer SET valor = ? " +
													" WHERE codigo_registro_enfer = ? AND examen_fisico_enf_cc_inst= ?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, valorExamenFisico);
				ps.setInt(2, codRegEnfer);					
				ps.setInt(3, examenFisicoCcIns);
			}
			else
			{
				consultaStr = " INSERT INTO examen_fisico_reg_enfer (codigo_registro_enfer, " +
																	  "examen_fisico_enf_cc_inst, " +
																	  "valor) " +
																  " VALUES (?, ?, ?) ";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codRegEnfer);				
				ps.setInt(2, examenFisicoCcIns);								
				ps.setString(3, valorExamenFisico);
			}
			resp = ps.executeUpdate();
			
			if (resp > 0)
			{
				resp = codRegEnfer;
			}
					
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en la inserción de datos en el exámen fisico de Registro Enfermería : SqlBaseRegistroEnfermeriaDao "+e.toString() );
				resp = 0;
		}
		return resp;
	}

	/**
	 * Método para saber si existe o no un exámen fisico de registro de enfermería
	 * @param con -> conexion
	 * @param codRegistroEnfer
	 * @param examenFisicoCcIns
	 * @return codRegistroEnfer si existe sino retorna -1
	 */
	public static int existeExamenFisico (Connection con, int codRegistroEnfer, int examenFisicoCcIns)
	{
		String consultaStr="SELECT codigo_registro_enfer AS codigo_registro_enfer " +
												"FROM examen_fisico_reg_enfer " +
													"WHERE codigo_registro_enfer = ? AND examen_fisico_enf_cc_inst = ? ";

			try
				{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codRegistroEnfer);
				ps.setInt(2, examenFisicoCcIns);
				
				ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
				
				if(resultado.next())
					{
					return resultado.getInt("codigo_registro_enfer");
					}
				else
					{
					return ConstantesBD.codigoNuncaValido;
					}	
				}
			catch(SQLException e)
				{
				logger.warn(e+" Vericando Existencia del Exámen Físico : SqlBaseRegistroEnfermeriaDao "+e.toString());
				return ConstantesBD.codigoNuncaValido;
				}
	}
	
  	/**
	 * Método que ingresa los signos vitales fijos del registro de enfermería
	 * @param con
	 * @param codigoEncabezado
	 * @param frecuenciaCardiaca
	 * @param frecuenciaRespiratoria
	 * @param presionArterialSistolica
	 * @param presionArterialDiastolica
	 * @param presionArterialMedia
	 * @param temperaturaPaciente
	 * @return
	 */
	public static int insertarSignosVitalesFijos (Connection con, int codigoEncabezado, String frecuenciaCardiaca, String frecuenciaRespiratoria, String presionArterialSistolica, String presionArterialDiastolica, String presionArterialMedia, String temperaturaPaciente)
	{
		PreparedStatementDecorator ps;
		int resp=0;
		String cad = "";
						
		try
			{
						cad = " INSERT INTO signo_vital_reg_enfer (codigo_histo_enfer, fc, fr, pas, pad, pam, temp) " +
															"	VALUES (?, ?, ?, ?, ?, ?, ?) ";
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigoEncabezado);				
						ps.setString(2, frecuenciaCardiaca);
						ps.setString(3, frecuenciaRespiratoria);
						ps.setString(4, presionArterialSistolica);
						ps.setString(5, presionArterialDiastolica);
						ps.setString(6, presionArterialMedia);
						ps.setString(7, temperaturaPaciente);
	
						resp = ps.executeUpdate();
						if (resp > 0)
						{
							resp = codigoEncabezado;
						}
					
				}
			catch(SQLException e)
			{
					logger.warn(" Error en la inserción insertarSignosVitalesFijos : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
	}
	
	/**
	 * Método que inserta un signo vital parametrizado por institución centro de costo
	 * @param con
	 * @param codigoEncabezado
	 * @param signoVitalCcIns
	 * @param valorSignoVital
	 * @return
	 */
	public static int insertarSignoVitalPametrizado (Connection con, int codigoEncabezado, int signoVitalCcIns, String valorSignoVital)
	{
		PreparedStatementDecorator ps;
		int resp=0;
		String cad = "";
						
		try
			{
						cad = " INSERT INTO signo_vit_reg_enfer_par (codigo_histo_enca, signo_vital_cc_inst, valor) " +
															"	VALUES (?, ?, ?) ";
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigoEncabezado);				
						ps.setInt(2, signoVitalCcIns);
						ps.setString(3, valorSignoVital);
							
						resp = ps.executeUpdate();
						if (resp > 0)
						{
							resp = codigoEncabezado;
						}
					
				}
			catch(SQLException e)
			{
					logger.warn(" Error en la inserción insertarSignoVitalPametrizado : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
	}
	
	  /**
	   * Metodo para consultar y cargar la información de la sección exámenes físicos
	   * @param con
	   * @param codigoCuenta
	   * @param cuentaAsocio
	   * @return Collection -> Con la información de los exámenes físicos
	   */
	  public static Collection cargarExamenesFisicos (Connection con, String cuentas)
	  {
	  	StringBuffer consultaBuffer=new StringBuffer();
	  	
    	/*consultaBuffer.append("SELECT efre.examen_fisico_enf_cc_inst AS codigo,efre.valor AS valor, tefe.codigo AS codigo_tipo " +
    												"FROM examen_fisico_reg_enfer efre " +
															" INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo) " +
															" INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo)" +
															" INNER JOIN tipo_examen_fisico_enfer tefe ON (efeci.tipo_examen_fisico_enfer=tefe.codigo)" );
    	
    	if(cuentaAsocio != 0)
    		consultaBuffer.append(" WHERE re.cuenta IN (?, ?)");
    	else
    		consultaBuffer.append(" WHERE re.cuenta=?");*/
	  	/**
	  	 *se comenta y se reescribe mas abajo.
	  	if (cuentaAsocio == 0)
	  	{
	  		consultaBuffer.append("SELECT efre.examen_fisico_enf_cc_inst AS codigo,efre.valor AS valor, tefe.codigo AS codigo_tipo " +
					"FROM examen_fisico_reg_enfer efre " +
							" INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo) " +
							" INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo)" +
							" INNER JOIN tipo_examen_fisico_enfer tefe ON (efeci.tipo_examen_fisico_enfer=tefe.codigo) " +
								"WHERE re.cuenta="+codigoCuenta );
	  		
	  		consultaBuffer.append(" AND efeci.activo= "+ValoresPorDefecto.getValorTrueParaConsultas() + " ORDER BY tefe.codigo");
	  	}
	  	else
	  	{
	  		consultaBuffer.append("SELECT * FROM "+
														"	(SELECT efre.examen_fisico_enf_cc_inst AS codigo,efre.valor AS valor, tefe.codigo AS codigo_tipo "+ 
														"		FROM examen_fisico_reg_enfer efre  "+
														"		INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo) "+  
														"		INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo) "+
														"		INNER JOIN tipo_examen_fisico_enfer tefe ON (efeci.tipo_examen_fisico_enfer=tefe.codigo) "+ 
														"			WHERE re.cuenta = "+codigoCuenta+" AND efeci.activo= "+ValoresPorDefecto.getValorTrueParaConsultas() + 
														"		UNION "+
														"	SELECT efre.examen_fisico_enf_cc_inst AS codigo,efre.valor AS valor, tefe.codigo AS codigo_tipo "+ 
														"		FROM examen_fisico_reg_enfer efre  "+ 
														"		INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo) "+  
														"		INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo) "+ 
														"		INNER JOIN tipo_examen_fisico_enfer tefe ON (efeci.tipo_examen_fisico_enfer=tefe.codigo) "+ 
														"	WHERE re.cuenta = "+cuentaAsocio+" AND efeci.activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND tefe.codigo NOT IN  "+
														"		(SELECT tefe.codigo AS codigo_tipo FROM 		examen_fisico_reg_enfer efre "+  
														"				INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo) "+  
														"				INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo)  "+
														"				INNER JOIN tipo_examen_fisico_enfer tefe ON (efeci.tipo_examen_fisico_enfer=tefe.codigo) "+ 
														"					WHERE re.cuenta = "+codigoCuenta+" AND efeci.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"))x "+
														"	ORDER BY x.codigo_tipo");
	  	}
		*/										
    	
	  	
	  	consultaBuffer.append("SELECT efre.examen_fisico_enf_cc_inst AS codigo,efre.valor AS valor, tefe.codigo AS codigo_tipo " +
				"FROM examen_fisico_reg_enfer efre " +
						" INNER JOIN examen_fisico_enf_cc_ins efeci ON (efre.examen_fisico_enf_cc_inst=efeci.codigo) " +
						" INNER JOIN registro_enfermeria re ON (efre.codigo_registro_enfer=re.codigo)" +
						" INNER JOIN tipo_examen_fisico_enfer tefe ON (efeci.tipo_examen_fisico_enfer=tefe.codigo) " +
							"WHERE re.cuenta IN ("+cuentas+")");
  		
  		consultaBuffer.append(" AND efeci.activo= "+ValoresPorDefecto.getValorTrueParaConsultas() + " ORDER BY tefe.codigo");
    	//logger.info("cuenta->"+codigoCuenta);
    	//logger.info("cargarExamenesFisicos->"+consultaBuffer.toString()+"\n");
    	try
			{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			/*ps.setInt(1, codigoCuenta);
			
			if(cuentaAsocio != 0)
				ps.setInt(2, cuentaAsocio);*/
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
	    	}
		
		catch (SQLException e)
			{
			logger.error("Error Consultado los Exámenes Físicos en SqlBaseRegistroEnfermeriaDao :"+e.toString());
			return null;
			}  
	  }


	
      /**
	     * Método que inserta la anotación de enfermería
	     * @param con
	     * @param codigoEncabezado
	     * @param anotacionEnfermeria
	     * @return
	     */
		public static  int insertarAnotacionEnfermeria (Connection con, int codigoEncabezado, String anotacionEnfermeria)
		{
			PreparedStatementDecorator ps;
			int resp=0;
			String cad = "";
							
			try
				{
					cad = " INSERT INTO anotaciones_reg_enfer (codigo_histo_enfer, anotacion) " +
														"	VALUES (?, ?) ";
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoEncabezado);				
					ps.setString(2, anotacionEnfermeria);
						
					resp = ps.executeUpdate();
					if (resp > 0)
					{
						resp = codigoEncabezado;
					}
					
				}
			catch(SQLException e)
				{
						logger.warn(" Error en la inserción insertarAnotacionEnfermeria : SqlBaseRegistroEnfermeriaDao "+e.toString() );
						resp = ConstantesBD.codigoNuncaValido;
				}
				return resp;
		}
		
		/**
		 * Método para consultar los insumos solicitados al paciente a través de solicitud de
	     * medicamentos que hacen parte de catéteres y sondas que no se encuentran anuladas
	     * y se encuentran ya despachadas
		 * @param con
		 * @param codigoCuenta
		 * @param institucion
		 * @return articulosDespachados
		 */
		public static Collection consultarCateterSondaDespachados (Connection con, int codigoCuenta, int institucion)
		{
			String consultaStr="SELECT acsi.codigo AS codigo, art.descripcion AS nombre " +
													"FROM solicitudes sol " +
														"INNER JOIN solicitudes_medicamentos smed ON (sol.numero_solicitud=smed.numero_solicitud) " +
														"INNER JOIN despacho des ON (smed.numero_solicitud=des.numero_solicitud) " +
														"INNER JOIN detalle_despachos ddes ON (des.orden=ddes.despacho) " +
														"INNER JOIN articulo art ON (ddes.articulo=art.codigo) " +
														"INNER JOIN articulo_cat_sonda_ins acsi ON (acsi.articulo=art.codigo) " +
															"WHERE sol.cuenta=? AND acsi.institucion=? AND (sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+" " + " OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCAdministrada+")"+ 
																		   " AND acsi.activo="+ValoresPorDefecto.getValorTrueParaConsultas() + " AND art.estado="+ValoresPorDefecto.getValorTrueParaConsultas() + 
																		   " GROUP BY acsi.codigo,art.descripcion " +
																		   " ORDER BY art.descripcion";

			try
				{
				//logger.info("\n CODIGO CUENTA->"+codigoCuenta+"\n");
				//logger.info("consultarCateterSondaDespachados--> \n " + consultaStr + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoCuenta);
				ps.setInt(2, institucion);
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
				}
			catch (SQLException e)
				{
				logger.error("Error Consultado los catéteres sonda despachados al paciente en SqlBaseRegistroEnfermeria"+e.toString());
				return null;
				}
		}
		
		/**
		 * Método para consultar el listado de anotaciones de enfermería realizadas en el registro 
		 * de enfermería de acuerdo a la cuenta del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param fechaFin 
		 * @param fechaInicio 
		 * @return listadoAnotacionesEnfermeria
		 */
		public static Collection consultarAnotacionesEnfermeria (Connection con,String cuentas, String fechaInicio, String fechaFin)
		{
			StringBuffer consultaBuffer=new StringBuffer();
			
			consultaBuffer.append("SELECT " +
											" to_char(ehre.fecha_registro, 'DD/MM/YYYY') || ' - ' || ehre.hora_registro||'' AS fecha_hora_reg, " +
											" getnombreusuario(ehre.usuario) AS nombre_usuario,are.anotacion AS anotacion," +
											" to_char(ehre.fecha_grabacion, 'dd/mm/yyyy') AS fecha_grabacion,ehre.hora_grabacion||'' AS hora_grabacion " +
									" FROM registro_enfermeria re " +
									" INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) " +
									" INNER JOIN anotaciones_reg_enfer are ON (ehre.codigo=are.codigo_histo_enfer) ");
			
			
	    		consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+")");
	    	
			//FILTRO EN RANGO DE FECHAS
			if(UtilidadCadena.noEsVacio(fechaInicio))
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaInicio+"'");
			if(UtilidadCadena.noEsVacio(fechaFin))
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro < '"+fechaFin+"'");

			
			consultaBuffer.append(" ORDER BY ehre.fecha_registro || '-' || ehre.hora_registro");
			
			try
				{
					//logger.info("consultarAnotacionesEnfermeria-->\n" + consultaBuffer.toString() + " \n");
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								
					return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
				}
		    catch (SQLException e)
			{
				logger.error("Error Consultado el histórico de las anotaciones de enfermería en SqlBaseRegistroEnfermeria"+e.toString());
				return null;
			}
		}
		
		

		/**
		 * Método para consultar el histórico de los signos vitales fijos de la sección
	     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param fechaInicio
		 * @param fechaFin
		 * @return signosVitalesFijosHisto  ---
		 */
		public static Collection consultarSignosVitalesFijosHisto (Connection con, String cuentas, String fechaInicio, String fechaFin)
		{
			logger.info("\n entre a  consultarSignosVitalesFijosHisto fecha ini -->"+fechaInicio+"   fecha fin -->"+fechaFin);
			StringBuffer consultaBuffer=new StringBuffer();
			
			consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
																"ehre.hora_registro||'' AS hora_registro, svre.fc AS fc, " +
																"svre.fr AS fr, svre.pas AS pas, svre.pad AS pad, svre.pam AS pam, svre.temp AS temp " +
																	"FROM enca_histo_registro_enfer ehre " +
																		"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																		"INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo=svre.codigo_histo_enfer) ");
			
			
				consultaBuffer.append("WHERE re.cuenta IN ("+cuentas+") ");
			
			if(fechaInicio.length()>10)
				consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro >=  '"+fechaInicio+"'");
			else
				consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') >=  '"+fechaInicio+"'");
			
			//Si la fecha fin no es vacío se consulta en el rango osea que la consulta es de una fecha en ver anteriores
			if(UtilidadCadena.noEsVacio(fechaFin))
				consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro <  '"+fechaFin+"'");
			
			consultaBuffer.append(	" ORDER BY ehre.fecha_registro, ehre.hora_registro, ehre.codigo");

		try
			{
			//logger.info("consultarSignosVitalesFijosHisto-->\n" + consultaBuffer.toString() + " \n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			/*
					ps.setString(1, fechaInicio);
					
					if(UtilidadCadena.noEsVacio(fechaFin))
						ps.setString(2, fechaFin);
			*/
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
			}
		catch (SQLException e)
			{
			logger.error("Error Consultado el histórico de los Signos Vitales fijos en SqlBaseRegistroEnfermeria"+e.toString());
			return null;
			}
		}
		
		/**
		 * Método para consultar el histórico de los signos vitales parametrizados por institución y centro de costo de la sección
	     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @param fechaInicio
		 * @param fechaFin
		 * @return signosVitalesParamHisto
		 */
		public static Collection consultarSignosVitalesParamHisto (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
		{
			StringBuffer consultaBuffer=new StringBuffer();
			
			consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, svrep.signo_vital_cc_inst AS signo_vital_cc_ins, sv.codigo AS codigo_tipo, " +
								  "		  to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
								  "		  ehre.hora_registro||'' AS hora_registro, svrep.valor AS valor_sig_vital " +
								  "			 FROM  enca_histo_registro_enfer ehre " +
								  "				INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
								  "				INNER JOIN signo_vit_reg_enfer_par svrep ON (svrep.codigo_histo_enca=ehre.codigo) " +
								  "				INNER JOIN signos_vitales_cc_inst svci ON (svrep.signo_vital_cc_inst=svci.codigo) " +
								  "				INNER JOIN signos_vitales sv ON (svci.signo_vital = sv.codigo)");
			
		
				consultaBuffer.append("WHERE re.cuenta IN ("+cuentas+") ");
	    																
			/*consultaBuffer.append(" AND svci.institucion=? AND svci.centro_costo=? " +
														"AND ehre.fecha_registro || '-' || ehre.hora_registro >= ? ");*/
			consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro >=  '"+fechaInicio+"'");
			
			//Si la fecha fin no es vacío se consulta en el rango osea que la consulta es de una fecha en ver anteriores
			if(UtilidadCadena.noEsVacio(fechaFin))
				consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro <  '"+fechaFin+"'");
			
			
			
			consultaBuffer.append(" ORDER BY ehre.fecha_registro, ehre.hora_registro, ehre.codigo");

			try
				{
				//logger.info("\nconsultarSignosVitalesParamHisto-->\n" + consultaBuffer.toString() + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/*
					ps.setString(1, fechaInicio);
					
					if(UtilidadCadena.noEsVacio(fechaFin))
						ps.setString(2, fechaFin);

				*/
			
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
				}
			catch (SQLException e)
				{
				logger.error("Error Consultado el histórico de los Signos Vitales parametrizados  en SqlBaseRegistroEnfermeria"+e.toString());
				return null;
				}
		}
		
		/**
		 * Método para consultar el listado con los códigos històricos, fecha registro y hora registro,
	     * de los signos vitales fijos y parametrizados 
		 * @param con
		 * @param cuenta
		 * @param institucion
		 * @param centroCosto
		 * @param fechaInicio
		 * @param fechaFin
		 * @return signosVitalesHistoTodos
		 */
		public static Collection consultarSignosVitalesHistoTodos (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
		{
			logger.info("\n entre a consultarSignosVitalesHistoTodos --> fecha ini"+fechaInicio+"   fecha fin -->"+fechaFin);
			StringBuffer consultaBuffer=new StringBuffer();
			
			//Si la fecha inicio no es vacía se consulta el histórico teniendo en cuenta la fecha-hora actual con respecto a la hora del turno
			if(UtilidadCadena.noEsVacio(fechaInicio))
				{
				consultaBuffer.append("SELECT * FROM " +
																"(SELECT ehre.codigo AS codigo_histo_enfer, to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
																		"ehre.hora_registro||'' AS hora_registro, to_char(ehre.fecha_grabacion, 'dd/mm/yyyy') AS fecha_grabacion, " +
																		"ehre.hora_grabacion||'' AS hora_grabacion, getnombreusuario(ehre.usuario) AS nombre_usuario, " +
																		"getespecialidadesmedico(ehre.usuario, ' , ') AS especialidades_medico " +
																			"FROM  enca_histo_registro_enfer ehre " +
																				"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																				"INNER JOIN signo_vit_reg_enfer_par svrep ON (svrep.codigo_histo_enca=ehre.codigo) ");
																				//"INNER JOIN signos_vitales_cc_inst svci ON (svrep.signo_vital_cc_inst=svci.codigo) ");
																	
			
					consultaBuffer.append("WHERE re.cuenta IN ("+cuentas+") ");
				
																		
				/*consultaBuffer.append(	"AND svci.institucion=? AND svci.centro_costo=? " +
															"AND ehre.fecha_registro || '-' || ehre.hora_registro >= ? ");*/
				consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro >=  '"+fechaInicio+"'");
				
				
				
				

				//Si la fecha fin no es vacío se consulta en el rango osea que la consulta es de una fecha en ver anteriores
				if(UtilidadCadena.noEsVacio(fechaFin))
					consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro < '"+fechaFin+"'");
															
				consultaBuffer.append(	"UNION " +
																"SELECT ehre.codigo AS codigo_histo_enfer, to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
																"ehre.hora_registro||'' AS hora_registro, to_char(ehre.fecha_grabacion, 'dd/mm/yyyy') AS fecha_grabacion," +
																"ehre.hora_grabacion||'' AS hora_grabacion, getnombreusuario(ehre.usuario) AS nombre_usuario," +
																"getespecialidadesmedico(ehre.usuario, ' , ') AS especialidades_medico " +
																	"FROM enca_histo_registro_enfer ehre " +
																		"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																		"INNER JOIN signo_vital_reg_enfer svre ON (svre.codigo_histo_enfer=ehre.codigo) ");
				
				
				consultaBuffer.append("WHERE re.cuenta IN ("+cuentas+")");

				consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro >=  '"+fechaInicio+"'");
				
				//Si la fecha fin no es vacío se consulta en el rango osea que la consulta es de una fecha en ver anteriores
				if(UtilidadCadena.noEsVacio(fechaFin))
					consultaBuffer.append(" AND to_char(ehre.fecha_registro,'yyyy-mm-dd') || '-' || ehre.hora_registro <  '"+fechaFin+"'");

						
				consultaBuffer.append(")x GROUP BY x.codigo_histo_enfer,x.fecha_registro,x.hora_registro,x.fecha_grabacion,x.hora_grabacion,x.nombre_usuario ,x.especialidades_medico " +
																		"ORDER BY x.fecha_registro, x.hora_registro, x.codigo_histo_enfer");
				}
			else
				{
					consultaBuffer.append("SELECT * FROM " +
																	"(SELECT to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, ehre.fecha_registro AS fecha_reg " +
																		"FROM  enca_histo_registro_enfer ehre " +
																			"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																			"INNER JOIN signo_vit_reg_enfer_par svrep ON (svrep.codigo_histo_enca=ehre.codigo) " );
																			//"INNER JOIN signos_vitales_cc_inst svci ON (svrep.signo_vital_cc_inst=svci.codigo) ");
					
						consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+") ");
					
					
					//consultaBuffer.append(" AND svci.institucion=? AND svci.centro_costo=? " +
					consultaBuffer.append(" UNION " +
													"SELECT to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, ehre.fecha_registro AS fecha_reg " +
														"FROM enca_histo_registro_enfer ehre " +
															"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
															"INNER JOIN signo_vital_reg_enfer svre ON (svre.codigo_histo_enfer=ehre.codigo) ");
					
						consultaBuffer.append("WHERE re.cuenta IN ("+cuentas+") ");
			
					
					consultaBuffer.append(")x " +
																"GROUP BY x.fecha_reg, x.fecha_registro " +
																	"ORDER BY x.fecha_reg DESC ");
				}

		try
			{
			//logger.info("\nfecha inicio-->"+fechaInicio+"\n");
			logger.info("\nconsultarSignosVitalesHistoTodos-->\n" + consultaBuffer.toString() + " \n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//Si la fecha inicio no es vacía se consulta el histórico teniendo en cuenta la fecha-hora actual con respecto a la hora del turno
			if(UtilidadCadena.noEsVacio(fechaInicio))
				{
				
					/*
					//ps.setInt(1, codigoCuenta);
					//ps.setInt(2, cuentaAsocio);
					//ps.setInt(3, institucion);
					//ps.setInt(4, centroCosto);
					ps.setString(1, fechaInicio);					
					//Si la fecha fin no es vacío se consulta en el rango osea que la consulta es de una fecha en ver anteriores
					if(UtilidadCadena.noEsVacio(fechaFin))
						{
							ps.setString(2,fechaFin);
							ps.setString(3, fechaInicio);
							ps.setString(4, fechaFin);
						}
					else
						ps.setString(2, fechaInicio);
						*/
				}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
			}
		catch (SQLException e)
			{
			logger.error("Error Consultado el histórico de los Signos Vitales fijos y parametrizados  en SqlBaseRegistroEnfermeria"+e.toString());
			return null;
			}
		}
		
		
		/**
		 * Método que inserta el encabezado del cateter sonda para el articulo seleccionado
		 * @param con
		 * @param codigoEncabezado
		 * @param articulo
		 * @return codigo
		 */
		public static int insertarEncabezadoCateterSonda (Connection con, int codigoEncabezado, int articulo)
		{
			PreparedStatementDecorator ps;
			int resp=0, codigo=0;
		
			String insertarStr = 	"INSERT INTO cateter_sonda_reg_enfer (codigo, codigo_histo_enfer, articulo_cat_sonda_ins) VALUES " +
																																"(?, ?, ?) ";
			
			try	{					
							DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
							codigo=myFactory.incrementarValorSecuencia(con, "seq_cateter_sonda_reg_enfer");
													
							ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							ps.setInt(1, codigo);
							ps.setInt(2, codigoEncabezado);
							ps.setInt(3, articulo);
						
							resp = ps.executeUpdate();
													
							if (resp > 0)
							{
								resp = codigo;
							}
								
					}
					catch(SQLException e)
					{
							logger.warn(e+" Error en la inserción de datos en el Encabezado de Cateter Sonda del Artículo : insertarEncabezadoCateterSonda "+e.toString() );
							resp = ConstantesBD.codigoNuncaValido;
					}
									
					return resp;
		}
		
		/**
		 * Método para insertar el valor de los catéteres fijos de la sección para un artículo determinado
		 * @param con
		 * @param cateterEncabezado
		 * @param viaInsercion
		 * @param fechaInsercion
		 * @param horaInsercion
		 * @param fechaRetiro
		 * @param horaRetiro
		 * @param curaciones
		 * @param observaciones
		 */
		public static int insertarCateterSondaFijo (Connection con, int cateterEncabezado, String viaInsercion, String  fechaInsercion, String horaInsercion, String fechaRetiro, String horaRetiro, String curaciones, String observaciones)
		{
			PreparedStatementDecorator ps;
			int resp=0;
			String cad = "";
			
			//logger.info("cateterEncabezado-->"+cateterEncabezado);
			//logger.info("viaInsercion-->"+viaInsercion);
			//logger.info("fechaInsercion-->"+fechaInsercion);
			//logger.info("horaInsercion-->"+horaInsercion);
			//logger.info("fechaRetiro-->"+fechaRetiro);
			//logger.info("horaRetiro-->"+horaRetiro);
			//logger.info("curaciones-->"+curaciones);
			//logger.info("observaciones-->"+observaciones);
			
							
			try
				{
						cad = " INSERT INTO detalle_cat_sonda_fija (" +
																"cateter_sonda_reg_enfer, " +
																"via_insercion, " +
																"fecha_insercion, " +
																"hora_insercion, " +
																"fecha_retiro, " +
																"hora_retiro, " +
																"curaciones, " +
																"observaciones) " +
															"VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
						
						ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, cateterEncabezado);				
						ps.setString(2, viaInsercion);
						
						if (UtilidadCadena.noEsVacio(fechaInsercion))
							ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)));
						else
							ps.setNull(3, Types.DATE);
						
						if(UtilidadCadena.noEsVacio(horaInsercion))
							ps.setString(4, horaInsercion);
						else
							ps.setNull(4, Types.CHAR);
								
						if (UtilidadCadena.noEsVacio(fechaRetiro))
							ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)));
						else
							ps.setNull(5, Types.DATE);
						
						if(UtilidadCadena.noEsVacio(horaRetiro))
							ps.setString(6, horaRetiro);
						else
							ps.setNull(6, Types.CHAR);

						ps.setString(7, curaciones);
						ps.setString(8, observaciones);
								
						resp = ps.executeUpdate();
						
						if (resp > 0)
						{
							resp = cateterEncabezado;
						}
						
				}
	  	 catch(SQLException e)
				{
						logger.warn(" Error en la inserción insertarCateterSondaFijo : SqlBaseRegistroEnfermeriaDao "+e.toString() );
						resp = ConstantesBD.codigoNuncaValido;
				}
				return resp;
		}
		
		/**
		 * Método para insertar el valor de los catéteres fijos de la sección para un artículo determinado
		 * @param con
		 * @param cateterEncabezado
		 * @param colCateterCcIns
		 * @param valorCateterCcIns
		 */
		public static int insertarCateterSondaParam (Connection con, int cateterEncabezado, int colCateterCcIns, String valor)
		{
			PreparedStatementDecorator ps;
			int resp=0;
			String cad = "";
							
			try
				{
					cad = " INSERT INTO  detalle_cat_sonda_param (cateter_sonda_reg_enfer, col_cateter_sonda_cc_ins, valor ) " +
														"	VALUES (?, ?, ?) ";
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, cateterEncabezado);				
					ps.setInt(2, colCateterCcIns);
					ps.setString(3, valor);
														
					resp = ps.executeUpdate();
					
					if (resp > 0)
					{
						resp = cateterEncabezado;
					}
						
				}
			catch(SQLException e)
				{
						logger.warn(" Error en la inserción insertarCateterSondaParam : SqlBaseRegistroEnfermeriaDao "+e.toString() );
						resp = ConstantesBD.codigoNuncaValido;
				}
				return resp;
		}
		
		/**
		 * Método que consulta el histórico de los cateteres sonda fijos del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @return
		 */
		public static Collection consultarCateterSondaFijosHisto(Connection con, String cuentas)
		{
			StringBuffer consultaBuffer=new StringBuffer();
			
			consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, dcsf.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, " +
																"dcsf.via_insercion AS via_insercion,dcsf.fecha_insercion AS fecha_insercion, dcsf.hora_insercion||'' AS hora_insercion," +
																"dcsf.fecha_retiro AS fecha_retiro, dcsf.hora_retiro||'' AS hora_retiro, dcsf.curaciones AS curaciones, dcsf.observaciones AS observaciones," +
																"to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro,ehre.hora_registro AS hora_registro, " +
																"to_char(ehre.fecha_grabacion, 'dd/mm/yyyy') AS fecha_grabacion,ehre.hora_grabacion AS hora_grabacion," +
																"getnombreusuario(ehre.usuario) AS nombre_usuario  " +
																	"FROM enca_histo_registro_enfer ehre " +
																		"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																		"INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
																		"INNER JOIN detalle_cat_sonda_fija dcsf ON (dcsf.cateter_sonda_reg_enfer=csre.codigo)");
			
			
				consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+") ");
			
			consultaBuffer.append(	" ORDER BY dcsf.fecha_insercion,dcsf.hora_insercion,dcsf.cateter_sonda_reg_enfer");

		try
			{
				logger.info("consultarCateterSondaFijosHisto-->" + consultaBuffer.toString() + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
			}
		catch (SQLException e)
			{
				logger.error("Error Consultado el histórico de los Cateteres Sonda fijos en SqlBaseRegistroEnfermeria"+e.toString());
				return null;
			}
		}
		
		/**
		 * Método que consulta el histórico de los cateteres sonda parametrizados por institución centro costo
		 * del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @return
		 */
		public static Collection consultarCateterSondaParamHisto(Connection con, String cuentas, int institucion, int centroCosto)
		{
			StringBuffer consultaBuffer=new StringBuffer();
			
			consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, dcsp.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, " +
																"dcsp.col_cateter_sonda_cc_ins AS col_cateter_sonda_cc_ins, dcsp.valor AS valor, ccs.codigo AS codigo_tipo " +
																	 "FROM enca_histo_registro_enfer ehre " +
																		" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																		" INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
																		" INNER JOIN detalle_cat_sonda_param dcsp ON (dcsp.cateter_sonda_reg_enfer=csre.codigo) " +
																		" INNER JOIN col_cateter_sonda_cc_ins csci ON (dcsp.col_cateter_sonda_cc_ins=csci.codigo) "+
																		" INNER JOIN columna_cateter_sonda ccs ON (csci.columna_cateter_sonda=ccs.codigo) ");
			
			
			consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+") ");
	        	
																		
			//consultaBuffer.append(" AND csci.institucion=? AND csci.centro_costo=? ");
			
					
			consultaBuffer.append(" ORDER BY ehre.fecha_registro, ehre.hora_registro,ehre.codigo");

			try
				{
				//logger.info("\nconsultarCateterSondaParamHisto-->\n" + consultaBuffer.toString() + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
				}
			catch (SQLException e)
				{
				logger.error("Error Consultado el histórico de los Cateteres Sonda parametrizados  en SqlBaseRegistroEnfermeria"+e.toString());
				return null;
				}
		}
		
		/**
		 * Método que consulta el histórico de los catéteres sonda fijos y parametrizados, despues de realizar
		 * el agrupamiento por el codigo del articulo despachado
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param institucion
		 * @param centroCosto
		 * @return
		 */
		public static Collection consultarCateterSondaTodosHisto (Connection con, String cuentas, int institucion, int centroCosto)
		{
			StringBuffer consultaBuffer=new StringBuffer();
			
			consultaBuffer.append("SELECT * FROM " +
															"(SELECT dcsf.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, " +
																		"getdescripcionarticulo(acsi.articulo) AS nombre_articulo, csre.articulo_cat_sonda_ins AS codigo_articulo_cc_ins " +
																			"FROM enca_histo_registro_enfer ehre " +
																				"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																				"INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
																				"INNER JOIN detalle_cat_sonda_fija dcsf ON (dcsf.cateter_sonda_reg_enfer=csre.codigo) " +
																				"INNER JOIN articulo_cat_sonda_ins acsi ON (csre.articulo_cat_sonda_ins=acsi.codigo) ");
			
			
			consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+") ");
	        
			consultaBuffer.append("UNION " +
															"SELECT dcsp.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, getdescripcionarticulo(acsi.articulo) AS nombre_articulo, " +
																			"csre.articulo_cat_sonda_ins AS codigo_articulo_cc_ins " +
																				"FROM enca_histo_registro_enfer ehre " +
																					"INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
																					"INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
																					"INNER JOIN detalle_cat_sonda_param dcsp ON (dcsp.cateter_sonda_reg_enfer=csre.codigo) " +
																					"INNER JOIN col_cateter_sonda_cc_ins csci ON (dcsp.col_cateter_sonda_cc_ins=csci.codigo) " +
																					"INNER JOIN articulo_cat_sonda_ins acsi ON (csre.articulo_cat_sonda_ins=acsi.codigo) ");
			
			
				consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+") ");
	        															
			//consultaBuffer.append(" AND csci.institucion=? AND csci.centro_costo=?)x " +
			consultaBuffer.append(" )x GROUP BY x.cateter_sonda_reg_enfer,x.nombre_articulo,x.codigo_articulo_cc_ins ");
		
			try
				{
				//logger.info("\nconsultarCateterSondaTodosHisto-->\n" + consultaBuffer.toString() + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
				}
			catch (SQLException e)
				{
				logger.error("Error Consultado el histórico de los Cateteres Sonda fijos y parametrizados  en SqlBaseRegistroEnfermeria"+e.toString());
				return null;
				}
		}
		
		/**
		 * Método que actualiza el campo curaciones y observaciones del cateter sonda histórico
		 * @param con
		 * @param catSondaRegEnfer
		 * @param curacionesAntNueva
		 * @param observacionesAntNueva
		 * @param fechaInsercion
		 * @param horaInsercion
		 * @param fechaRetiro
		 * @param horaRetiro
		 * @return
		 */
		public static int actualizarCateterSondaFijo(Connection con, int catSondaRegEnfer, String curacionesAntNueva, String observacionesAntNueva, String fechaInsercion, String horaInsercion, String fechaRetiro, String horaRetiro)
		{
			PreparedStatementDecorator ps;
			int resp=ConstantesBD.codigoNuncaValido;
			StringBuffer cad = new StringBuffer();

			int existe=existeCateterSondaFijo(con, catSondaRegEnfer);
			boolean entro=false;
			
			//logger.info("\n CURACIONES-->"+curacionesAntNueva+"\n");
			//logger.info("\n OBSERVACIONES-->"+observacionesAntNueva+"\n");
			//logger.info("\n FECHA INSERCION-->"+fechaInsercion+"\n");
			//logger.info("\n FECHA RETIRO-->"+fechaRetiro+"\n");
			//logger.info("\n HORA INSERCION-->"+horaInsercion+"\n");
			//logger.info("\n HORA RETIRO-->"+horaRetiro+"\n");
			
			
			//----------------Se verifica si existe el cateter sonda fijo para realizar la actualización ----------//
			if (existe > 0)
				{
					try
						{
									cad.append("UPDATE  detalle_cat_sonda_fija set ");
									
									if (UtilidadCadena.noEsVacio(curacionesAntNueva))
										{
											cad.append("curaciones=\'"+curacionesAntNueva+"\'");
											entro=true;
										}
									
									if (UtilidadCadena.noEsVacio(observacionesAntNueva))
										{
											if (UtilidadCadena.noEsVacio(curacionesAntNueva))
													cad.append(", observaciones=\'"+observacionesAntNueva+"\'");
											else
												{	
													cad.append("observaciones=\'"+observacionesAntNueva+"\'");
													entro=true;
												}
										}
									
									//------Se veriifica si entro a alguna de los sets anteriores cuando son diferente de vacío curaciones u observaciones --//
									if (entro)
										{
											if(UtilidadCadena.noEsVacio(fechaInsercion))
											{
												cad.append(", fecha_insercion=\'"+UtilidadFecha.conversionFormatoFechaABD(fechaInsercion) + "\', hora_insercion=\'"+horaInsercion+"\'");
											}
											
											if (UtilidadCadena.noEsVacio(fechaRetiro))
											{
												cad.append(", fecha_retiro=\'"+UtilidadFecha.conversionFormatoFechaABD(fechaRetiro) + "\', hora_retiro=\'"+horaRetiro+"\'");
											}
										}//entro
									else
										{
											if(UtilidadCadena.noEsVacio(fechaInsercion))
												{
													cad.append("fecha_insercion=\'"+UtilidadFecha.conversionFormatoFechaABD(fechaInsercion) + "\', hora_insercion=\'"+horaInsercion+"\'");
												}
											
											if (UtilidadCadena.noEsVacio(fechaRetiro))
												{
												if (UtilidadCadena.noEsVacio(fechaInsercion))
													cad.append(", fecha_retiro=\'"+UtilidadFecha.conversionFormatoFechaABD(fechaRetiro) +"\', hora_retiro=\'"+horaRetiro+"\'" );
												else
														cad.append("fecha_retiro=\'"+UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)+ "\', hora_retiro=\'"+horaRetiro+"\'");
												}
										}//else
									
									cad.append(" WHERE cateter_sonda_reg_enfer="+catSondaRegEnfer);
									
									
									//logger.info("\n actualizar cateter-->"+cad.toString()+"\n");
									ps =  new PreparedStatementDecorator(con.prepareStatement(cad.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
							/*		if (UtilidadCadena.noEsVacio(curacionesAntNueva) && UtilidadCadena.noEsVacio(observacionesAntNueva))
										{
										ps.setString(1, curacionesAntNueva);
										ps.setString(2, observacionesAntNueva);
										ps.setInt(3, catSondaRegEnfer);	
										}
									else if (UtilidadCadena.noEsVacio(curacionesAntNueva))
											{
												ps.setString(1, curacionesAntNueva);
												ps.setInt(2, catSondaRegEnfer);
											}
									else
											{
												ps.setString(1, observacionesAntNueva);
												ps.setInt(2, catSondaRegEnfer);
											}*/
								
									resp = ps.executeUpdate();
									if (resp > 0)
									{
										resp = catSondaRegEnfer;
									}
								
							}
						catch(SQLException e)
						{
								logger.warn(" Error en la actualización del cateter sonda fijo actualizarCateterSondaFijo : SqlBaseRegistroEnfermeriaDao "+e.toString() );
								resp = ConstantesBD.codigoNuncaValido;
						}
				}//si existe cateter sonda fijo
			else
			{
				//--------- Se inserta el cateter sonda fijo ya que no existe--------------//
				resp=insertarCateterSondaFijo(con, catSondaRegEnfer, "", fechaInsercion, horaInsercion, fechaRetiro, horaRetiro, curacionesAntNueva, observacionesAntNueva);
			}
				return resp;
		}
		
		/**
		 * Método que consulta si existe un cateter de sonda fijo e inserta el valor
		 * @param con
		 * @param catSondaRegEnfer
		 * @return
		 */
		public static int existeCateterSondaFijo(Connection con,int catSondaRegEnfer)
		{
			String consultaStr="SELECT count(*) AS num FROM detalle_cat_sonda_fija WHERE cateter_sonda_reg_enfer = ?";

			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, catSondaRegEnfer);
			
				ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			
				if(resultado.next())
					{
					return resultado.getInt("num");
					}
				else
					{
					return ConstantesBD.codigoNuncaValido;
					}	
			}
			catch(SQLException e)
				{
				logger.warn(e+" Vericando Existencia del Cateter Sonda Fijo : SqlBaseRegistroEnfermeriaDao "+e.toString());
				return ConstantesBD.codigoNuncaValido;
				}
		}
		
		/**
		 * Método que inserta la información ingresada en los cuidados especiales de enfermería
		 * @param con
		 * @param codEncabezado
		 * @param codigoCuidadoEnf
		 * @param presenta
		 * @param descripcion
		 * @param tipoCuidado
		 * @return
		 */
		public static int insertarDetalleCuidadoEnfermeria(Connection con, int codEncabezado, int codigoCuidadoEnf, String presenta, String descripcion, int tipoCuidado)
		{
			PreparedStatementDecorator ps;
			int resp=ConstantesBD.codigoNuncaValido;
		
			String insertarDetCuidadoEnf = 	"";
			
			try	
			{
				//----Verifica si es otro cuidado de enfermería ya que es igual a 1 ------// 
				if (tipoCuidado == 1)
				{
					insertarDetCuidadoEnf = "INSERT INTO detalle_otro_cuidado_renf (codigo_histo_enfer, " +
																				  "otro_cual, " +
																				  "presenta," +
																				  "descripcion) VALUES (?, ?, ?, ?) ";
					
					//logger.info("\n codigoCuidadoEnf  ->"+codigoCuidadoEnf);
					//logger.info("\n descripcion->"+descripcion);
					//logger.info("\n presenta->"+presenta);*/

					ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetCuidadoEnf,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setInt(1, codEncabezado);
					ps.setInt(2, codigoCuidadoEnf);
					
					if (!UtilidadCadena.noEsVacio(presenta) ) 
					{
						ps.setString(3,null);
					}
					else if (presenta.equals("si"))
					{
						ps.setBoolean(3,true);		
					}
					else if (presenta.equals("no"))
					{
						ps.setBoolean(3,false);		
					}
					
					ps.setString(4,descripcion);
					
				}
				else		
				{            
							insertarDetCuidadoEnf = "INSERT INTO detalle_cuidado_reg_enfer (codigo_histo_enfer, " +
																						"cuidado_enfer_cc_inst, " +
																						"presenta, " +
																						"descripcion)" +
																						"VALUES " +
																						"(?, ?, ?, ?) ";

							//logger.info("Sentencia (Insertar Detalle cuidados Enfermeria)  -> "+insertarDetCuidadoEnf);				
							//logger.info("\n codigoCuidadoEnf  ->"+codigoCuidadoEnf);
							//logger.info("\n tipoCuidado ->"+tipoCuidado);
							//logger.info("\n presenta->"+presenta);
							//logger.info("\n descripcion->"+descripcion);*/
							
							ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetCuidadoEnf,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
							ps.setInt(1, codEncabezado);
							ps.setInt(2, codigoCuidadoEnf);
							
							if (!UtilidadCadena.noEsVacio(presenta) ) 
							{
								ps.setString(3,null);
							}
							else if (presenta.equals("si"))
							{
								ps.setBoolean(3,true);		
							}
							else if (presenta.equals("no"))
							{
								ps.setBoolean(3,false);		
							}
							
							ps.setString(4,descripcion);
						}
				
				//-----Ejecutar la insercion 
				resp = ps.executeUpdate();			
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en Insertar Detalle cuidados Enfermeria : SqlBaseRegistroEnfermeriaDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}
			return resp;
		}
		
		/**
		 * Método que consulta las columnas de los cuidados especiales de enfermería de la orden médica 
		 * y del registro de enfermería en el juego de información de la sección
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @param fechaFin 
		 * @param fechaInicio 
		 * @return HashMap
		 */
		public static HashMap consultarColsCuidadosEspeciales (Connection con, String cuentas, String fechaInicio, String fechaFin)
		{
			try
			{
				StringBuffer consulta=new StringBuffer();
				
				consulta.append("SELECT " +
										" ehom.codigo AS codigo_enca, ehom.fecha_orden AS fecha_reg, substr(ehom.hora_orden||'',0,5) AS hora_reg, " +
										" ehom.fecha_grabacion AS fecha_grabacion, substr(ehom.hora_grabacion||'',0,5)||'' AS hora_grabacion, 1 AS es_medico " +
								" FROM encabezado_histo_orden_m ehom " +
								" INNER JOIN ordenes_medicas om ON (ehom.orden_medica=om.codigo) " +
								" WHERE " +
											" ehom.codigo= " +
													" (" +
													" SELECT MAX(x.cod_enca) " +
													" FROM (" +
															" SELECT ehom.codigo AS cod_enca " +
															" FROM encabezado_histo_orden_m ehom " +
															" INNER JOIN ordenes_medicas om ON (ehom.orden_medica=om.codigo AND ");
				
														
																consulta.append(" om.cuenta IN ("+cuentas+") ) ");
																			
															consulta.append(" " +
															" WHERE ehom.codigo IN " +
																				" (" +
																				" SELECT cod_histo_enca AS cod_enca FROM detalle_cuidado_enfer " +
																				" UNION " +
																				" SELECT cod_histo_cuidado_enfer AS cod_enca FROM detalle_otro_cuidado_enf" +
																				" )" +
															" ) x" +
														" ) " );

											//FILTRO EN RANGO DE FECHAS
											if(UtilidadCadena.noEsVacio(fechaInicio))
												consulta.append(" AND ehom.fecha_orden || '-' || ehom.hora_orden >= '"+fechaInicio+"'");
											if(UtilidadCadena.noEsVacio(fechaFin))
												consulta.append(" AND ehom.fecha_orden || '-' || ehom.hora_orden < '"+fechaFin+"'");
											
				consulta.append( " UNION ALL " +
								 " SELECT * " +
								 "FROM (" +
								 		" SELECT * FROM " +
									         			"(" +
									         				" SELECT " +
									         						" ehom.codigo AS codigo_enca, ehom.fecha_orden AS fecha_reg, substr(ehom.hora_orden||'',0,5)||'' AS hora_reg, " +
									         						" ehom.fecha_grabacion AS fecha_grabacion, substr(ehom.hora_grabacion||'',0,5)||'' AS hora_grabacion, 1 AS es_medico " +
									         				" FROM encabezado_histo_orden_m ehom " +
									         				" INNER JOIN ordenes_medicas om ON (ehom.orden_medica=om.codigo AND ");
				
															
															consulta.append(" om.cuenta IN ("+cuentas+")) ");
															
															consulta.append(" " +
															" WHERE " +
																		" ehom.codigo IN " +
																				"( SELECT cod_histo_enca FROM detalle_cuidado_enfer " +
																				" UNION " +
																				" SELECT cod_histo_cuidado_enfer FROM detalle_otro_cuidado_enf" +
																				" ) " +
																		" AND ehom.codigo NOT IN " +
																								" (SELECT MAX(x.cod_enca) FROM " +
																																" (" +
																																	" SELECT ehom.codigo AS cod_enca FROM encabezado_histo_orden_m ehom " +
																			  														" INNER JOIN ordenes_medicas om ON (ehom.orden_medica=om.codigo AND ");
				
																																	
																																	consulta.append(" om.cuenta IN ("+cuentas+")) ");
																																	
																																	consulta.append(" " +
																																	" WHERE ehom.codigo IN " +
																																						"(SELECT cod_histo_enca AS cod_enca FROM detalle_cuidado_enfer " +
																																								"UNION " +
																																					 	  "SELECT cod_histo_cuidado_enfer AS cod_enca FROM detalle_otro_cuidado_enf)" +
																																" ) x " +
																								" ) " );

																			//FILTRO EN RANGO DE FECHAS
																			if(UtilidadCadena.noEsVacio(fechaInicio))
																				consulta.append(" AND ehom.fecha_orden || '-' || ehom.hora_orden >= '"+fechaInicio+"'");
																			if(UtilidadCadena.noEsVacio(fechaFin))
																				consulta.append(" AND ehom.fecha_orden || '-' || ehom.hora_orden < '"+fechaFin+"'");
																			
											consulta.append(" UNION ALL " +
															" SELECT " +
																	" ehre.codigo AS codigo_enca, ehre.fecha_registro AS fecha_reg, ehre.hora_registro||''  AS hora_reg, " +
												  					" ehre.fecha_grabacion AS fecha_grabacion, ehre.hora_grabacion AS hora_grabacion, 0 AS es_medico " +
												  			" FROM enca_histo_registro_enfer ehre " +
												  			" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo AND ");
				
															
															consulta.append(" re.cuenta IN ("+cuentas+")) ");
															
															consulta.append(" " +
															" WHERE ehre.codigo IN " +
																					"(SELECT codigo_histo_enfer FROM detalle_cuidado_reg_enfer " +
																							"UNION " +
																					 "SELECT codigo_histo_enfer FROM detalle_otro_cuidado_renf) " );
																					 
															//FILTRO EN RANGO DE FECHAS
															if(UtilidadCadena.noEsVacio(fechaInicio))
																consulta.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaInicio+"'");
															if(UtilidadCadena.noEsVacio(fechaFin))
																consulta.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro < '"+fechaFin+"'");

										consulta.append(" " +
													")z " +
													" ORDER BY z.fecha_reg || '-' || z.hora_reg || '-' || z.fecha_grabacion || '-' || z.hora_grabacion DESC" +
										" )r");				
				
				PreparedStatementDecorator stm= new PreparedStatementDecorator(con,consulta.toString());
				logger.info("Consulta --> \n"+stm);
				HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
				stm.close();
				return mapaRetorno;
			}
			catch (SQLException e)
			{
				HashMap mapaRetorno=new HashMap();
				logger.error("Error consultando las columnas históricas de los cuidados especiales de enfermería en el juego de información: ",e);
				mapaRetorno.put("numRegistros", "0");
				return mapaRetorno;
			}
		}
		
		/**
		 * Método que cargar la información ingresada en la orden médica de la hoja neurológica
		 * del paciente en la orden médica
		 * @param con
		 * @param codigoCuenta
		 * @param codigoCuentaAsocio
		 * @return Collection
		 */
		public static Collection cargarInfoHojaNeurologica(Connection con, String cuentas)
		{
			String consultaStr="";
			
			int numReg=0;
			String cuenta="";
			String[] cuentasArray=cuentas.split(",");
			//--------Se consulta si existe información de la hoja neurológica con el código de cada cuenta -----------//
			for (int i=0;i<cuentasArray.length && numReg<1;i++)
			{
				cuenta=cuentasArray[i];
				numReg=Utilidades.nroRegistrosConsulta(con, "SELECT hn.orden_medica FROM ordenes_medicas om INNER JOIN hoja_neurologica_orden_m hn ON (om.codigo=hn.orden_medica) WHERE om.cuenta="+cuenta);
			}
			
			consultaStr =  "SELECT hn.presenta AS presenta, hn.finalizada AS finalizada, " +
											"				to_char(hn.fecha_fin, 'dd/mm/yyyy') || '-' || hn.hora_fin AS fecha_fin," +
											"				to_char(hn.fecha_grabacion, 'dd/mm/yyyy') || '-' || hn.hora_grabacion AS fecha_grabacion," +
											"				getdatosmedicoespecialidades(hn.login, ',') AS medico" +
											"		FROM ordenes_medicas om " +
											"			INNER JOIN hoja_neurologica_orden_m hn ON (om.codigo=hn.orden_medica) " +
											"				WHERE om.cuenta="+cuenta;
				
			try
				{
				PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
					
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));
				} 
			catch (SQLException e)
				{
				logger.error("Error Consultado en la Orden Médica la información de la Hoja Neurológica :"+e.toString());
				return null;
				}
		}

		
		
		/**
		 * Método que consulta las mezclas del paciente registradas desde orden medica.   
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @return HashMap
		 */
		public static HashMap consultarMezclaOrdenes (Connection con, int tipoConsulta, String cuentas)
		{
			try
			{
				String consulta="", cue="";

				//-Para verificar si se tiene informacion por la cuenta de asocio.
				
					cue = " om.cuenta IN ("+ cuentas + ") ";
				
				
				if (tipoConsulta == 0) //-Se Consultan la Mezclas por paciente
				{
					/*consulta = "	SELECT od.codigo_histo_enca as encabezado, od.mezcla as cod_mezcla, m.nombre as nom_mezcla,					" +
							   "		   od.velocidad_infusion as velocidad_infusion, od.volumen_total as volumen_total						" +
							   "		   FROM orden_nutricion_parente onp																		" +		
							   "			    INNER JOIN orden_dieta od ON ( onp.codigo_historico_dieta = od.codigo_histo_enca )				" +
							   "			    INNER JOIN articulo a ON ( a.codigo = onp.articulo )											" + 
							   "			    INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )											" +
							   "			    INNER JOIN encabezado_histo_orden_m ehom ON ( ehom.codigo = od.codigo_histo_enca )				" +
							   "  			    INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )								" +
							   "					 WHERE  "+ cue +" 																			" +
							   "					 GROUP BY od.codigo_histo_enca, od.mezcla, m.nombre, od.volumen_total, od.velocidad_infusion" +
							   "					 ORDER BY od.codigo_histo_enca";*/					
					
					consulta = "	SELECT ehom.codigo as encabezado, od.mezcla as cod_mezcla, 											" + 
							   "		   m.nombre || ' (' || tm.nombre || ') ' as descripcion,  										" +
							   "	 	   od.volumen_total as volumen_total, od.velocidad_infusion as velocidad_infusion      			" +
							   "	 	   FROM orden_dieta od																			" +
							   "	 	  		INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )    								" +
							   "		 		INNER JOIN tipo_mezcla tm ON ( tm.codigo = m.cod_tipo_mezcla ) 							" +
							   "		 		INNER JOIN encabezado_histo_orden_m ehom ON ( od.codigo_histo_enca = ehom.codigo )		" +
							   "	 	 		INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )   					" +		
							   "	 	 		INNER JOIN solicitudes_medicamentos sm ON ( sm.orden_dieta = od.codigo_histo_enca )		" +   
							   "	 	 		INNER JOIN solicitudes sol ON ( sol.numero_solicitud = sm.numero_solicitud )			" +
							   "	 	  			 WHERE " + cue +
							   "					   AND od.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() + 	
							   "					   AND sol.estado_historia_clinica " +
							   "							 IN ( " + ConstantesBD.codigoEstadoHCAdministrada +" ,"+ ConstantesBD.codigoEstadoHCSolicitada +")" + 	
							   "  			   	  ORDER BY  ehom.codigo";	
				}
				if (tipoConsulta == 1) //-Se Consulta solo las mezclas y el detalle de cada articulo que esta en cada mezcla
				{
					
					consulta = "	 SELECT od.codigo_histo_enca as d_encabezado, od.mezcla as d_cod_mezcla, m.nombre as d_nom_mezcla,	" +
							   "		   onp.articulo as d_cod_articulo, a.descripcion as d_nom_articulo, onp.volumen as unidosis		" +
							   "		   FROM orden_nutricion_parente onp																" +
							   "		   	    INNER JOIN orden_dieta od ON ( onp.codigo_historico_dieta = od.codigo_histo_enca )		" +
							   "		   	    INNER JOIN articulo a ON ( a.codigo = onp.articulo )									" +
							   "		   	    INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )									" +
							   "		   		INNER JOIN encabezado_histo_orden_m ehom ON ( ehom.codigo = od.codigo_histo_enca )		" +
							   "		   	    INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )						" +
							   "	 	 		INNER JOIN solicitudes_medicamentos sm ON ( sm.orden_dieta = od.codigo_histo_enca )		" +   
							   "	 	 		INNER JOIN solicitudes sol ON ( sol.numero_solicitud = sm.numero_solicitud )			" +
							   "		   			 WHERE " + cue + 
							   "					   AND od.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() + 	
							   "					   AND sol.estado_historia_clinica " +
							   "							 IN ( " + ConstantesBD.codigoEstadoHCAdministrada +" ,"+ ConstantesBD.codigoEstadoHCSolicitada +")" + 	
							   "		   			 ORDER BY od.codigo_histo_enca ";
				}
				
				//logger.info("\n\n Mezclas Consulta ["+ tipoConsulta + "]  ["+consulta+"]   \n\n");
				PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
				stm.close();
				return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error consultando las mezclas del paciente registradas desde orden medica: "+e);
				return null;
			}
		}
		
		//--------------------------------------- METODOS DE ESCALA GLASGOW -------------------------------------------------//
		
		/**
		 * Método que inserta el detalle de la escala glasgow
		 * @param con
		 * @param codEncabezado
		 * @param especificacionGlasgow
		 * @param observacion
		 * @return
		 */
		public static int insertarDetalleEscalaGlasgow(Connection con, int codEncabezado, int especificacionGlasgow, String observacionEspGlasgow)
		{
			PreparedStatementDecorator ps;
			int resp=0;
			String cad = "";
							
			try
				{
					cad = " INSERT INTO  detalle_escala_glasgow (enca_histo_reg_enfer, especificacion_glasgow, observacion ) " +
														"	VALUES (?, ?, ?) ";
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codEncabezado);				
					ps.setDouble(2, Utilidades.convertirADouble(especificacionGlasgow+""));
					ps.setString(3, observacionEspGlasgow);
														
					resp = ps.executeUpdate();
					
					if (resp > 0)
					{
						resp = codEncabezado;
					}
						
				}
			catch(SQLException e)
				{
						logger.warn(" Error en la inserción insertarDetalleEscalaGlasgow : SqlBaseRegistroEnfermeriaDao "+e.toString() );
						resp = ConstantesBD.codigoNuncaValido;
				}
				return resp;
		}
		
		/**
		 * Método que consulta el histórico de la escala glasgow
		 * del paciente
		 * @param con
		 * @param codigoCuenta
		 * @param cuentaAsocio
		 * @return
		 */
		public static HashMap consultarHistoricoEscalaGlasgow(Connection con, String cuentas)
		{
			StringBuffer consultaStr=new StringBuffer();

			consultaStr.append("SELECT deg.enca_histo_reg_enfer AS codigo_histo_enfer, to_char(ehre.fecha_registro, 'dd/mm/yyyy') || '-' ||  ehre.hora_registro AS fecha_registro, " +
												"				 to_char(ehre.fecha_grabacion, 'dd/mm/yyyy') || '-' || ehre.hora_grabacion AS fecha_grabacion,deg.especificacion_glasgow AS especificacion_glasgow, " +
												"				 deg.observacion AS observacion,eg.valor AS valor_glasgow,eg.glasgow_cc_inst AS codigo_glasgow, getnombreusuario(ehre.usuario) AS medico, x.total AS total "+
												"		FROM detalle_escala_glasgow deg "+
												"			INNER JOIN enca_histo_registro_enfer ehre ON (deg.enca_histo_reg_enfer=ehre.codigo) "+
												"			INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) "+
												"			INNER JOIN especificaciones_glasgow eg ON (deg.especificacion_glasgow=eg.codigo) " +
												"			INNER JOIN (SELECT deg.enca_histo_reg_enfer AS codigo_histo_enfer, sum(eg.valor) AS total " +
												"				 			FROM detalle_escala_glasgow deg " +
												"								INNER JOIN enca_histo_registro_enfer ehre ON (deg.enca_histo_reg_enfer=ehre.codigo) " +
												"								INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
												"								INNER JOIN especificaciones_glasgow eg ON (deg.especificacion_glasgow=eg.codigo) ");
		
		consultaStr.append("	 									WHERE re.cuenta IN ("+cuentas+") ");
			
		consultaStr.append(	"						GROUP BY deg.enca_histo_reg_enfer)x ON x.codigo_histo_enfer=ehre.codigo	");
			
		
		consultaStr.append("	 	WHERE re.cuenta IN ("+cuentas+") ");
		
		consultaStr.append("		ORDER BY ehre.fecha_registro || '-' || ehre.hora_registro DESC, ehre.codigo DESC"); 
			
			try
			{
			///logger.info("\n consultarHistoricoEscalaGlasgow-->\n" + consultaStr.toString() + " \n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
	
			}
			catch (SQLException e)
			{
			logger.error("Error Consultado el histórico de Escala Glasgow en SqlBaseRegistroEnfermeria"+e.toString());
			return null;
			}
		}
		
		//--------------------------------------- FIN METODOS DE ESCALA GLASGOW -------------------------------------------------//

		
		//*********************************METODOS CONVULSIONES***********//////////////////

		/**
		 * 
		 * @param con
		 * @return
		 */
		public static HashMap obtenerTiposConvulsiones(Connection con) 
		{
			HashMap mapa=new HashMap();
			mapa.put("numRegistros","0");
			String consulta="SELECT codigo as codigo, nombre as nombre from tipo_convulsion";
			PreparedStatementDecorator ps;
			try {
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return mapa;
		}
		
		

		/**
		 * 
		 * @param con
		 * @param vo
		 * @return
		 */
		public static boolean guardarConvulsiones(Connection con, HashMap vo) 
		{
			String cadena="INSERT INTO detalle_convulsion(enca_histo_reg_enfer,tipo_convulsion,observacion) values (?,?,?)";
			try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Utilidades.convertirAEntero(vo.get("codEncabezado").toString()));
				ps.setDouble(2,Utilidades.convertirADouble(vo.get("tipoConvulstion").toString()));
				ps.setString(3,vo.get("observacion").toString());
				return ps.executeUpdate()>0;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		/**
		 * 
		 * @param con
		 * @param codigoCuenta
		 * @return
		 */
		public static HashMap obtenerHistoricosConvulsiones(Connection con, String cuentas) 
		{
			HashMap mapa=new HashMap();
			mapa.put("numRegistros","0");
			String consulta="SELECT ehre.codigo as codigohistoregistro,ehre.fecha_grabacion ||' ' || " +
								"substr(ehre.hora_grabacion||'',0,9) as fechahoragrabacion, " +
								"ehre.usuario as usuario,ehre.datos_medico as datosmedico, " +
								"tc.nombre as tipoconvulsion,dc.observacion as observacionconvulsion " +
								"FROM detalle_convulsion dc inner join  " +
								"enca_histo_registro_enfer ehre on (dc.enca_histo_reg_enfer=ehre.codigo) inner join " +
								"registro_enfermeria rf on (rf.codigo=ehre.registro_enfer) inner join " +
								"tipo_convulsion tc on(dc.tipo_convulsion=tc.codigo)" +
								"WHERE rf.cuenta IN ("+cuentas+") order by ehre.codigo desc";
			//logger.info("\n obtenerHistoricosConvulsiones->"+consulta+"\n");
			PreparedStatementDecorator ps;
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return mapa;
		}
		//*********************************FIN METODOS CONVULSIONES***********//////////////////
		
		//********************************METODOS CONTROL ESFINTERES*****************************
		
		/**
		 * Método para consultar el historico de la seccion de control de esfinteres
		 * segun un la cuenta del paciente
		 * @param con
		 * @param idCuenta
		 * @return
		 */
		public static HashMap consultarHistoricoControlEsfinteres(Connection con, String cuentas)
		{
			HashMap mapa = new HashMap();
			mapa.put("numRegistros","0");
			String consulta = " SELECT ehre.fecha_grabacion as fechagrabacion, " +
							  " ehre.fecha_grabacion ||' ' || substr(ehre.hora_grabacion||'',0,9) as fechahoragrabacion, " +
							  " ehre.usuario as usuario, " +
							  " ehre.hora_grabacion as horagrabacion, " +
							  " ehre.fecha_registro as fecharegistro, " +
							  " ehre.hora_registro as horaregistro, " +
							  " ehre.datos_medico as datosmedico, " +
							  " ehre.registro_enfer as codigoregistroenfer, " +
							  " re.cuenta as cuenta, " +
							  " dce.enca_histo_reg_enfer as codigoencabregenfer, " +
							  " dce.caracte_control_esfinter as codigocarac, " +
							  " dce.observacion as observacion, " +
							  " cce.descripcion as descripcioncaract, " +
							  " case when cce.ausente ='"+ValoresPorDefecto.getValorTrueParaConsultas()+"' then 'si' else 'no' end as ausente " +
							  " FROM detalle_control_esfinteres dce " +
							  " INNER JOIN caract_control_esfinter cce ON(dce.caracte_control_esfinter=cce.codigo) " +
							  " INNER JOIN enca_histo_registro_enfer ehre ON(dce.enca_histo_reg_enfer=ehre.codigo) " +
							  " INNER JOIN registro_enfermeria re ON(ehre.registro_enfer=re.codigo)" +
							  " WHERE re.cuenta IN ("+cuentas+") " +
							  " ORDER BY ehre.codigo DESC ";
			PreparedStatementDecorator ps;
			//logger.info("\n consultarHistoricoControlEsfinteres->"+consulta);
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			} 
			catch (SQLException e) 
			{
				logger.error("\n\nError consultarHistoricoControlEsfinteres [SqlBaseRegistroEnfermeriaDao]: "+e);
				e.printStackTrace();
			}
			return mapa;
			
		}
		
		/**
		 * Método para insertar el detalle de la seccion de Control de Esfinteres
		 * @param con
		 * @param codigoEncabeRegEnfer
		 * @param codigoCaracControlEnfinter
		 * @param observacion
		 * @return
		 */
		public static int insertarDetControlEsfinteres(Connection con, int codigoEncabeRegEnfer, int codigoCaracControlEnfinter, String observacion)
		{
			try
			{
				String insercionStr =" INSERT INTO detalle_control_esfinteres " +
									 " (enca_histo_reg_enfer, " +
									 " caracte_control_esfinter," +
									 " observacion) " +
									 " VALUES (?, ?, ?) ";
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insercionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoEncabeRegEnfer);
				ps.setDouble(2, Utilidades.convertirADouble(codigoCaracControlEnfinter+""));
				ps.setString(3, observacion);
				return ps.executeUpdate();
				 
			}
			catch (SQLException e) 
			{
				logger.error("\n\nError insertarDetControlEsfinteres [SqlBaseRegistroEnfermeriaDao]: "+e);
				e.printStackTrace();
				return ConstantesBD.codigoNuncaValido;
			}
		}

		
		//********************************FIN DE CONTROL DE ESFINTER*****************************
	
		//********************************METODOS FUERZA MUSCUALR***********//////////////////

		/**
		 * 
		 * @param con
		 * @return
		 */
		public static HashMap consultarTiposFuerzaMuscular(Connection con) 
		{
			HashMap mapa=new HashMap();
			mapa.put("numRegistros","0");
			String consulta="select acronimo , nombre from tipo_fuerza_muscular ORDER BY nombre";
			PreparedStatementDecorator ps;
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return mapa;
		}
		
		/**
		 * 
		 * @param con
		 * @param vo
		 * @return
		 */
		public static boolean guardarFuerzaMuscular(Connection con, HashMap vo) 
		{
			String cadena="INSERT INTO detalle_fuerza_muscular " +
												"(enca_histo_reg_enfer, " +
												"m_superior_derecho, " +
												"m_superior_izquierdo, " +
												"m_inferior_derecho, " +
												"m_inferior_izquierdo, " +
												"obs_m_sup_derecho, " +
												"obs_m_sup_izquierdo, " +
												"obs_m_inf_derecho, " +
												"obs_m_inf_izquierdo) " +
											"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,Utilidades.convertirAEntero(vo.get("codEncabezado").toString()));
				if(vo.get("superiorDerecho").toString().trim().equals(""))
					ps.setObject(2, null);
				else
					ps.setString(2,vo.get("superiorDerecho").toString());
				if(vo.get("superiorIzquierdo").toString().trim().equals(""))
					ps.setString(3, null);
				else
					ps.setString(3,vo.get("superiorIzquierdo").toString());
				if(vo.get("inferiorDerecho").toString().trim().equals(""))
					ps.setObject(4, null);
				else
					ps.setString(4,vo.get("inferiorDerecho").toString());
				if(vo.get("inferiorIzquierdo").toString().trim().equals(""))
					ps.setObject(5,null);
				else	
					ps.setString(5,vo.get("inferiorIzquierdo").toString());
				if(vo.containsKey("observSuperiorDerecho"))
					ps.setString(6,vo.get("observSuperiorDerecho").toString());
				else
					ps.setObject(6,null);
				if(vo.containsKey("observSuperiorIzquierdo"))
					ps.setString(7,vo.get("observSuperiorIzquierdo").toString());
				else
					ps.setObject(7, null);
				if(vo.containsKey("observInferiorDerecho"))
					ps.setString(8,vo.get("observInferiorDerecho").toString());
				else
					ps.setObject(8, null);
				if(vo.containsKey("observInferiorIzquierdo"))
					ps.setString(9,vo.get("observInferiorIzquierdo").toString());
				else
					ps.setObject(9, null);
				
				return ps.executeUpdate()>0;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		/**
		 * 
		 * @param con
		 * @param codigoCuenta
		 * @return
		 */
		public static HashMap obtenerHistoricosFuerzaMuscular(Connection con, String cuentas) 
		{
			HashMap mapa=new HashMap();
			mapa.put("numRegistros","0");
			String consulta="SELECT ehre.codigo as codigohistoregistro," +
							"to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') ||' ' || substr(ehre.hora_grabacion||'',0,9) as fechahoragrabacion," +
							"ehre.usuario as usuario," +
							"ehre.datos_medico as datosmedico, " +
							"CASE WHEN dfm.m_superior_derecho IS NULL THEN '' ELSE dfm.m_superior_derecho END AS acronimosuperiorderecho, " +
							"CASE WHEN dfm.m_superior_izquierdo IS NULL THEN '' ELSE dfm.m_superior_izquierdo END AS acronimosuperiorizquierdo, " +
							"CASE WHEN dfm.m_inferior_derecho IS NULL THEN '' ELSE dfm.m_inferior_derecho END AS acronimoinferiorderecho, " +
							"CASE WHEN dfm.m_inferior_izquierdo IS NULL THEN '' ELSE dfm.m_inferior_izquierdo END AS acronimoinferiorizquierdo, " +
							"CASE WHEN tfm1.nombre IS NULL THEN '' ELSE tfm1.nombre END AS nombresuperiorderecho, " +
							"CASE WHEN tfm2.nombre IS NULL THEN '' ELSE tfm2.nombre END AS nombresuperiorizquierdo, " +
							"CASE WHEN tfm3.nombre IS NULL THEN '' ELSE tfm3.nombre END AS nombreinferiorderecho, " +
							"CASE WHEN tfm4.nombre IS NULL THEN '' ELSE tfm4.nombre END AS nombreinferiorizquierdo, " +
							"CASE WHEN dfm.obs_m_sup_derecho IS NULL THEN '' ELSE dfm.obs_m_sup_derecho END AS observsuperiorderecho, " +
							"CASE WHEN dfm.obs_m_sup_izquierdo IS NULL THEN '' ELSE dfm.obs_m_sup_izquierdo END AS observsuperiorizquierdo, " +
							"CASE WHEN dfm.obs_m_inf_derecho IS NULL THEN '' ELSE dfm.obs_m_inf_derecho END AS observinferiorderecho, " +
							"CASE WHEN dfm.obs_m_inf_izquierdo IS NULL THEN '' ELSE dfm.obs_m_inf_izquierdo END AS observinferiorizquierdo " +
							"from " +
							"enca_histo_registro_enfer ehre " +
							"inner join registro_enfermeria rf on (rf.codigo=ehre.registro_enfer) " +
							"INNER JOIN detalle_fuerza_muscular dfm ON (dfm.enca_histo_reg_enfer=ehre.codigo) " +
							"LEFT OUTER JOIN tipo_fuerza_muscular tfm1 ON (tfm1.acronimo=dfm.m_superior_derecho) " +
							"LEFT OUTER JOIN tipo_fuerza_muscular tfm2 ON (tfm2.acronimo=dfm.m_superior_izquierdo) " +
							"LEFT OUTER JOIN tipo_fuerza_muscular tfm3 ON (tfm3.acronimo=dfm.m_inferior_derecho) " +
							"LEFT OUTER JOIN tipo_fuerza_muscular tfm4 ON (tfm4.acronimo=dfm.m_inferior_izquierdo) " +
							"where rf.cuenta IN ("+cuentas+") ORDER BY codigohistoregistro desc";
			PreparedStatementDecorator ps;
			//logger.info("\n obtenerHistoricosFuerzaMuscular-->"+consulta);
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			return mapa;
		}
		//*********************************FIN METODOS fuerza muscular***********//////////////////
		
			/**
	 * Método que ingresa en la BD los datos de la pupila
	 * @param con
	 * @param codEncabezado
	 * @param tamanioD
	 * @param tamanioI
	 * @param reaccionD
	 * @param reaccionI
	 * @param obsDerecha @todo
	 * @param obsIzquierda @todo
	 * @return
	 */
	public static int accionGuardarPupila(Connection con, int codEncabezado, int tamanioD, int tamanioI, String reaccionD, String reaccionI, String obsDerecha, String obsIzquierda)
	{
		//logger.info(" Ingresando \n\n\n");
		
		String consulta="INSERT INTO detalle_caracte_pupila " +
													"(enca_histo_reg_enfer, " +
													"derecha_tamano, " +
													"derecha_reaccion, " +
													"izquierda_tamano, " +
													"izquierda_reaccion, " +
													"derecha_observaciones, " +
													"izquierda_observaciones) " +
												"VALUES(?,?,?,?,?,?,?)";
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codEncabezado);
			if(tamanioD==0)
			{
				pst.setNull(2, Types.INTEGER);
			}
			else
			{
				pst.setDouble(2, Utilidades.convertirADouble(tamanioD+""));
			}
			pst.setString(3, reaccionD);
			if(tamanioI==0)
			{
				pst.setNull(4, Types.INTEGER);
			}
			else
			{
				pst.setDouble(4, Utilidades.convertirADouble(tamanioI+""));
			}
			pst.setString(5, reaccionI);
			pst.setString(6, obsDerecha);
			pst.setString(7, obsIzquierda);
			return pst.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando los datos de la pupila "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}



	/**
	 * @param con
	 * @param fechaInicio
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public static HashMap consultarHistoricoPupilas(Connection con, String fechaInicio, String cuentas)
	{
		String consultaHistoPupilas="" +
										"SELECT " +
											"dp.enca_histo_reg_enfer AS codigo_enc, " +
											"enc.fecha_registro AS fecha_registro, " +
											"enc.hora_registro AS hora_registro, " +
											"enc.fecha_grabacion AS fecha_grabacion, " +
											"enc.hora_grabacion AS hora_grabacion, " +
											"getnombreusuario(enc.usuario) AS usuario " +
										"FROM detalle_caracte_pupila dp " +
										"INNER JOIN enca_histo_registro_enfer enc ON(enc.codigo=dp.enca_histo_reg_enfer) " +
										"INNER JOIN registro_enfermeria re ON(re.codigo=enc.registro_enfer) " +
										"WHERE re.cuenta IN("+cuentas+") "+
											"ORDER BY " +
												"enc.fecha_registro DESC, " +
												"enc.hora_registro DESC " ;
		
		String consultarDatosHistoricos="SELECT"+
											"	enca_histo_reg_enfer AS codigo_enca,"+
											"	dad.descripcion AS tamano_d,"+
											"	dai.descripcion AS tamano_i,"+
											"	rpd.nombre AS reaccion_d,"+
											"	rpi.nombre AS reaccion_i,"+
											"	dp.derecha_observaciones AS observaciones_d,"+
											"	dp.izquierda_observaciones AS observaciones_i"+
											"	FROM registro_enfermeria re"+
											"		INNER JOIN enca_histo_registro_enfer enc"+
											"			ON(enc.registro_enfer=re.codigo)"+
											"		INNER JOIN detalle_caracte_pupila dp"+
											"			ON(dp.enca_histo_reg_enfer=enc.codigo)"+
											"		LEFT OUTER JOIN tamano_pupila tpd"+
											"			ON(tpd.valor=dp.derecha_tamano)"+
											"		LEFT OUTER JOIN tamano_pupila tpi"+
											"			ON(tpi.valor=dp.izquierda_tamano)"+
											"		LEFT OUTER JOIN descripcion_abrev_pupila dad"+
											"			ON(dad.codigo=tpd.descripcion_abreviatura)"+
											"		LEFT OUTER JOIN descripcion_abrev_pupila dai"+
											"			ON(dai.codigo=tpi.descripcion_abreviatura)"+
											"		LEFT OUTER JOIN reaccion_pupila rpd"+
											"			ON(rpd.acronimo=dp.derecha_reaccion)"+
											"		LEFT OUTER JOIN reaccion_pupila rpi"+
											"			ON(rpi.acronimo=dp.izquierda_reaccion)"+
											"		WHERE"+
											"			re.cuenta IN("+cuentas+")" +
											"		ORDER BY " +
											"			enc.fecha_registro DESC, " +
											"			enc.hora_registro DESC " ;
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaHistoPupilas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n consultaHistoPupilas-> "+consultaHistoPupilas);
			HashMap mapaFechas=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			int numRegistros=Utilidades.convertirAEntero(mapaFechas.get("numRegistros")+"");
			mapaFechas.put("numFechas",numRegistros);
			pst= new PreparedStatementDecorator(con.prepareStatement(consultarDatosHistoricos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n consultarDatosHistoricos-> "+consultarDatosHistoricos);
			mapaFechas.putAll(UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery())));
			//logger.info("mapaFechas "+mapaFechas);
			return mapaFechas;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando históricos de pupilas"+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar la información de anotaciones de enfermería de acuerdo a los parámetros
	 * de búsqueda, para ser mostrada como sección en la impresión de historía clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos			
	 * @return listadoAnotacionesEnfermeria
	 */
	public static Collection consultarAnotacionesEnfermeriaImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY') || ' - ' || ehre.hora_registro AS fecha_hora_reg, " +
							  "		  are.anotacion AS anotacion, getnombreusuario(ehre.usuario) AS nombre_usuario " +
							  "			FROM registro_enfermeria re " +
							  "				INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) " +
							  "				INNER JOIN anotaciones_reg_enfer are ON (ehre.codigo=are.codigo_histo_enfer) ");
		
		
		Vector cuentasConsulta= new Vector ();
		logger.info("CUENTAS -->"+cuentas);
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			
 			String [] cuentaTmp=cuentas.split(",");
 			
 			logger.info("mostrarInformacion--->"+mostrarInformacion+"\n numCuentas-->"+cuentaTmp.length+"  veccuenta -->"+"--->"+cuentasConsulta.size());
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
				//filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 				filtroCuenta=" WHERE re.cuenta IN ("+ cuentas+")";
 		}
		
		logger.info("--->"+cuentasConsulta.size());
		
		consultaBuffer.append(filtroCuenta);
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
			}
		}
		
		if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
			}
		}
		
		consultaBuffer.append(" ORDER BY ehre.fecha_registro || '-' || ehre.hora_registro");
		
		PreparedStatementDecorator ps = null; 
		ResultSetDecorator rs = null; 
		try
			{
	         	logger.info("\n\n consultarAnotacionesEnfermeriaImpresionHC-->\n" + consultaBuffer.toString() + " \n");
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
				
				rs=new ResultSetDecorator(ps.executeQuery());
				Collection col=UtilidadBD.resultSet2Collection(rs);
				logger.info("--->"+col.size());
				return col;
			}
	    catch (SQLException e)
		{
			logger.error("ERROR", e);
			return null;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
	}
	
	/**
	 * Método para consultar la información historica de los signos vitales fijos de acuerdo a los parámetros
	 * de búsqueda, para ser mostrada como sección en la impresión de historía clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos			
	 * @return 
	 */
	public static Collection consultarSignosVitalesFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		logger.info("\n entre a consultarSignosVitalesFijosHistoImpresionHC");
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, svre.fc AS fc, " +
							  "		  svre.fr AS fr, svre.pas AS pas, svre.pad AS pad, svre.pam AS pam, svre.temp AS temp " +
							  "			FROM enca_histo_registro_enfer ehre " +
							  "				INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							  "				INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo=svre.codigo_histo_enfer) ");
		
		//-------Si tiene cuenta de asocio -----------//
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
	 			if(cuentasConsulta.size()==0)
	 			{
	 				//en caso de venir de ordenes medicas y consulta externa, envia el mostrarInformacion en U pero se debe cargar las cuentas.
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				cuentasConsulta.add(cuentaTmp[i]);
		 			}
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		consultaBuffer.append(filtroCuenta);
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
			}
		}
		
		if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
			}
		}
		
		consultaBuffer.append(	" ORDER BY ehre.fecha_registro, ehre.hora_registro, ehre.codigo");

	try
		{
		//logger.info("\n\n consultarSignosVitalesFijosHistoImpresionHC-->\n" + consultaBuffer.toString() + " \n");
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
		}
	catch (SQLException e)
		{
		logger.error("Error en consultarSignosVitalesFijosHistoImpresionHC: SqlBaseRegistroEnfermeria"+e.toString());
		return null;
		}
	}
	
	/**
	 * Mètodo que consulta la información histórica de los signos vitales parametrizados por institución centro de
	 * costo, de acuerdo a los parámetros de busqueda, para ser mostrada como sección en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public static Collection consultarSignosVitalesParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, svrep.signo_vital_cc_inst AS signo_vital_cc_ins, sv.codigo AS codigo_tipo, " +
							  "		  svrep.valor AS valor_sig_vital " +
							  "			 FROM  enca_histo_registro_enfer ehre " +
							  "				INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							  "				INNER JOIN signo_vit_reg_enfer_par svrep ON (ehre.codigo=svrep.codigo_histo_enca) " +
							  "				INNER JOIN signos_vitales_cc_inst svci ON (svrep.signo_vital_cc_inst=svci.codigo) " +
							  "				INNER JOIN signos_vitales sv ON (svci.signo_vital = sv.codigo)");
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		consultaBuffer.append(filtroCuenta);
		
																	
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
			}
		}
		
		if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
			}
		}
		
		consultaBuffer.append(" ORDER BY ehre.fecha_registro, ehre.hora_registro, ehre.codigo");

		try
			{
			//logger.info("\n\n\nconsultarSignosVitalesParamHistoImpresionHC-->\n" + consultaBuffer.toString() + " \n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
			}
		catch (SQLException e)
			{
			logger.error("Error en consultarSignosVitalesParamHistoImpresionHC (SqlBaseRegistroEnfermeria)"+e.toString());
			return null;
			}
	}
	
	/**
	 * Método para consultar el listado con los códigos històricos, fecha registro,hora registro, usuario
     * de los signos vitales fijos y parametrizados, para la impresión de historia clínica
	 * @param con
	 * @param cuentas ejemp: 123,122,56,896 
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public static Collection consultarSignosVitalesHistoTodosImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="",filtroCuenta="", filtroFechaInicial="", filtroFechaFinal="";
		
 		consultaBuffer.append(   " SELECT * FROM " +
								 "     (" +
								 "		 SELECT ehre.codigo AS codigo_histo_enfer, to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
								 "			    ehre.hora_registro||'' AS hora_registro, getnombreusuario(ehre.usuario) AS nombre_usuario," +
								 "				ehre.fecha_registro || '' || ehre.hora_registro AS fecha " +
								 "					FROM  enca_histo_registro_enfer ehre " +
								 "						INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
								 "						INNER JOIN signo_vit_reg_enfer_par svrep ON (ehre.codigo=svrep.codigo_histo_enca) ");
																
 		//-------Si tiene cuenta de asocio -----------//
 			
 		
 		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		consultaBuffer.append(filtroCuenta);
																	
		if(UtilidadCadena.noEsVacio(fechaInicial))
			{
				if(UtilidadCadena.noEsVacio(horaInicial))
				{
					fechaHoraInicial=fechaInicial+"-"+horaInicial;
					filtroFechaInicial=" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'";
				}
				else
				{
				   fechaHoraInicial=fechaInicial;
				   filtroFechaInicial=" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'";
				}
			}
			
		if(UtilidadCadena.noEsVacio(fechaFinal))
			{
				if(UtilidadCadena.noEsVacio(horaFinal))
				{
					fechaHoraFinal=fechaFinal+"-"+horaFinal;
					filtroFechaFinal=" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'";
				}
				else
				{
					fechaHoraFinal=fechaFinal;
					filtroFechaFinal=" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'";
				}
			}
			
		consultaBuffer.append(filtroFechaInicial);
		consultaBuffer.append(filtroFechaFinal);

		consultaBuffer.append("  UNION " +
							 "      SELECT ehre.codigo AS codigo_histo_enfer, to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
							 "			   ehre.hora_registro||'' AS hora_registro, getnombreusuario(ehre.usuario) AS nombre_usuario, " +
							 "			   ehre.fecha_registro || '' || ehre.hora_registro AS fecha " +
							 "					FROM enca_histo_registro_enfer ehre " +
							 "						INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							 "						INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo=svre.codigo_histo_enfer) ");
			
		consultaBuffer.append(filtroCuenta);
		consultaBuffer.append(filtroFechaInicial);
		consultaBuffer.append(filtroFechaFinal);
					
		consultaBuffer.append(" )x " +
							  "	  GROUP BY x.codigo_histo_enfer,x.fecha_registro,x.hora_registro,x.nombre_usuario,x.fecha " +
							  "		ORDER BY x.fecha, x.codigo_histo_enfer");
	try
		{
		//logger.info("\n\n\nconsultarSignosVitalesHistoTodosImpresionHC-->\n" + consultaBuffer.toString() + " \n");
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
		}
	catch (SQLException e)
		{
		logger.error("Error en consultarSignosVitalesHistoTodosImpresionHC  (SqlBaseRegistroEnfermeria)"+e.toString());
		return null;
		}
	}
	
	/**
	 * Método para consultar la información de cateter sonda de las columnas fijas, para la 
	 * impresión de la historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public static Collection consultarCateterSondaFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, dcsf.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, " +
							  "		  dcsf.via_insercion AS via_insercion,to_char(dcsf.fecha_insercion, 'dd/mm/yyyy') AS fecha_insercion, dcsf.hora_insercion||'' AS hora_insercion," +
							  " 	  to_char(dcsf.fecha_retiro,'dd/mm/yyyy') AS fecha_retiro, dcsf.hora_retiro||'' AS hora_retiro, dcsf.curaciones AS curaciones, dcsf.observaciones AS observaciones," +
							  "		  to_char(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro,ehre.hora_registro||'' AS hora_registro " +
							  "			  FROM enca_histo_registro_enfer ehre " +
							  "				INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							  "				INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
							  "				INNER JOIN detalle_cat_sonda_fija dcsf ON (dcsf.cateter_sonda_reg_enfer=csre.codigo)");
		
	

		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		consultaBuffer.append(filtroCuenta);
		
		
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
			}
		}
		
	  if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
			}
		}
		
		consultaBuffer.append(	" ORDER BY dcsf.fecha_insercion,dcsf.hora_insercion,dcsf.cateter_sonda_reg_enfer");

	
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
	try
		{
		//logger.info("\n\n consultarCateterSondaFijosHistoImpresionHC-->\n" + consultaBuffer.toString() + " \n");
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
		rs = new ResultSetDecorator(ps.executeQuery());
		return UtilidadBD.resultSet2Collection(rs);			
		}
	catch (SQLException e)
		{
		logger.error("Error en consultarCateterSondaFijosHistoImpresionHC (SqlBaseRegistroEnfermeria)"+e.toString());
		return null;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
	}
	
	/**
	 * Método para consultar la información de cateter sonda de las columnas parametrizadas, para la 
	 * impresión de la historia clínica 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static Collection consultarCateterSondaParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT ehre.codigo AS codigo_histo_enfer, dcsp.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, " +
							  "		  dcsp.col_cateter_sonda_cc_ins AS col_cateter_sonda_cc_ins, dcsp.valor AS valor, ccs.codigo AS codigo_tipo " +
							  "				FROM enca_histo_registro_enfer ehre " +
							  "					 INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							  "					 INNER JOIN cateter_sonda_reg_enfer csre ON (ehre.codigo=csre.codigo_histo_enfer) " +
							  "					 INNER JOIN detalle_cat_sonda_param dcsp ON (dcsp.cateter_sonda_reg_enfer=csre.codigo) " +
							  "					 INNER JOIN col_cateter_sonda_cc_ins csci ON (dcsp.col_cateter_sonda_cc_ins=csci.codigo) "+
							  "					 INNER JOIN columna_cateter_sonda ccs ON (csci.columna_cateter_sonda=ccs.codigo) ");
		

		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		consultaBuffer.append(filtroCuenta);
		
		
	  if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
			}
		}
		
	  if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
			}
		}
																	
		consultaBuffer.append(" ORDER BY ehre.fecha_registro, ehre.hora_registro,ehre.codigo");
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
			{
			//logger.info("\n\n\n consultarCateterSondaParamHistoImpresionHC-->\n" + consultaBuffer.toString() + " \n");
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
				rs = new ResultSetDecorator(ps.executeQuery());
				return UtilidadBD.resultSet2Collection(rs);			
			}
		catch (SQLException e)
			{
				logger.error("Error en consultarCateterSondaParamHistoImpresionHC  (SqlBaseRegistroEnfermeria)"+e.toString());
				return null;
			}
			finally{
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			}
	}
	
	/**
	 * Mètodo para consultar los codigos históricos, fecha hora de registro, nombre usuario de los cateter sonda
	 * fijos y parametrizados
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static Collection consultarCateterSondaTodosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="",filtroCuenta="", filtroFechaInicial="", filtroFechaFinal="";
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
			{
				if(UtilidadCadena.noEsVacio(horaInicial))
				{
					fechaHoraInicial=fechaInicial+"-"+horaInicial;
					filtroFechaInicial=" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'";
				}
				else
				{
				   fechaHoraInicial=fechaInicial;
				   filtroFechaInicial=" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'";
				}
			}
			
		if(UtilidadCadena.noEsVacio(fechaFinal))
			{
				if(UtilidadCadena.noEsVacio(horaFinal))
				{
					fechaHoraFinal=fechaFinal+"-"+horaFinal;
					filtroFechaFinal=" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'";
				}
				else
				{
					fechaHoraFinal=fechaFinal;
					filtroFechaFinal=" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'";
				}
			}
		
		consultaBuffer.append("SELECT * FROM " +
							  "			(" +
							  "			  SELECT dcsf.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, " +
							  "					 getdescripcionarticulo(acsi.articulo) AS nombre_articulo, csre.articulo_cat_sonda_ins AS codigo_articulo_cc_ins, " +
							  "		 			 getnombreusuario(ehre.usuario) AS nombre_usuario  " +
							  "						 FROM enca_histo_registro_enfer ehre " +
							  "							INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							  "							INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
							  "							INNER JOIN detalle_cat_sonda_fija dcsf ON (dcsf.cateter_sonda_reg_enfer=csre.codigo) " +
							  "							INNER JOIN articulo_cat_sonda_ins acsi ON (csre.articulo_cat_sonda_ins=acsi.codigo) ");
		
		consultaBuffer.append(filtroCuenta);
		consultaBuffer.append(filtroFechaInicial);
		consultaBuffer.append(filtroFechaFinal);
		
		consultaBuffer.append(" UNION " +
							  "	SELECT dcsp.cateter_sonda_reg_enfer AS cateter_sonda_reg_enfer, getdescripcionarticulo(acsi.articulo) AS nombre_articulo, " +
							  "		   csre.articulo_cat_sonda_ins AS codigo_articulo_cc_ins, " +
							  "		   getnombreusuario(ehre.usuario) AS nombre_usuario  " +
							  "				FROM enca_histo_registro_enfer ehre " +
							  "					INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
							  "					INNER JOIN cateter_sonda_reg_enfer csre ON (csre.codigo_histo_enfer=ehre.codigo) " +
							  "					INNER JOIN detalle_cat_sonda_param dcsp ON (dcsp.cateter_sonda_reg_enfer=csre.codigo) " +
							  "					INNER JOIN col_cateter_sonda_cc_ins csci ON (dcsp.col_cateter_sonda_cc_ins=csci.codigo) " +
							  "					INNER JOIN articulo_cat_sonda_ins acsi ON (csre.articulo_cat_sonda_ins=acsi.codigo) ");
		
		consultaBuffer.append(filtroCuenta);
		consultaBuffer.append(filtroFechaInicial);
		consultaBuffer.append(filtroFechaFinal);
																	
		consultaBuffer.append(" )x GROUP BY x.cateter_sonda_reg_enfer,x.nombre_articulo,x.codigo_articulo_cc_ins,x.nombre_usuario");
	
		try
			{
			logger.info("\n\n\nconsultarCateterSondaTodosHistoImpresionHC-->\n" + consultaBuffer.toString() + " \n");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
			}
		catch (SQLException e)
			{
			logger.error("Error en consultarCateterSondaTodosHistoImpresionHC (SqlBaseRegistroEnfermeria)"+e.toString(),e);
			return null;
			}
	}
	
	/**
	 * Método que consulta la información del encabezado histórico de los cuidados especiales de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarCuidadosEspecialesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="",filtroCuentaOrden="", filtroFechaInicialOrden="", filtroFechaFinalOrden="";
		String filtroCuentaRegEnfer="", filtroFechaInicialRegEnfer="", filtroFechaFinalRegEnfer="";
		
		String filtroPresentaOrden=" AND ((dce.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND dce.descripcion<>'') OR dce.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (dce.presenta IS NULL AND dce.descripcion<>''))";
		String filtroPresentaRegEnfer=" AND ((dcre.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND dcre.descripcion<>'') OR dcre.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (dcre.presenta IS NULL AND dcre.descripcion<>''))";
		String filtroPresentaOrdenOtro=" AND ((doce.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND doce.descripcion<>'') OR doce.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (doce.presenta IS NULL AND doce.descripcion<>''))";
		String filtroPresentaRegEnferOtro=" AND ((docre.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND docre.descripcion<>'') OR docre.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (docre.presenta IS NULL AND docre.descripcion<>''))";


		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
 		{
 			filtroCuentaOrden=" WHERE om.cuenta  IN ("+cuentas+")";
 			filtroCuentaRegEnfer=" WHERE re.cuenta  IN ("+cuentas+")";
 		}
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
 			filtroCuentaOrden=" WHERE om.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 			filtroCuentaRegEnfer=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				filtroFechaInicialOrden=" AND eho.fecha_orden || '-' || eho.hora_orden >= '"+fechaHoraInicial+"'";
				filtroFechaInicialRegEnfer=" AND ere.fecha_registro || '-' || ere.hora_registro >= '"+fechaHoraInicial+"'";
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   filtroFechaInicialOrden=" AND to_date(eho.fecha_orden||'','yyyy-mm-dd') >= to_date('"+fechaHoraInicial+"','yyyy-mm-dd')";
			   filtroFechaInicialRegEnfer=" AND to_date(ere.fecha_registro||'','yyyy-mm-dd') >= to_date('"+fechaHoraInicial+"','yyyy-mm-dd')";
			}
		}
		
	  if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				filtroFechaFinalOrden=" AND eho.fecha_orden || '-' || eho.hora_orden <= '"+fechaHoraFinal+"'";
				filtroFechaFinalRegEnfer=" AND ere.fecha_registro || '-' || ere.hora_registro <= '"+fechaHoraFinal+"'";
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				filtroFechaFinalOrden=" AND to_date(eho.fecha_orden||'','yyyy-mm-dd')<= to_date('"+fechaHoraFinal+"','yyyy-mm-dd')";
				filtroFechaFinalRegEnfer=" AND to_date(ere.fecha_registro||'','yyyy-mm-dd') <= to_date('"+fechaHoraFinal+"','yyyy-mm-dd')";
			}
		}
		
	  //--------------Se consulta el histórico de cuidados enfer en la orden médica -------------------//
		consultaBuffer.append("SELECT * FROM "+ 
							  "	  ("+
							  "		SELECT eho.codigo AS codigo_enca, to_char(eho.fecha_orden, 'dd/mm/yyyy') AS fecha_registro,eho.hora_orden||'' AS hora_registro, " +
							  "			   getnombreusuario(eho.login) AS nombre_usuario, 1 AS es_medico, eho.fecha_orden || '' || eho.hora_orden AS fecha "+
							  "				 FROM encabezado_histo_orden_m eho"+
							  "					INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo)"+
							  "					INNER JOIN detalle_cuidado_enfer dce ON (eho.codigo=dce.cod_histo_enca)");
		
		consultaBuffer.append(filtroCuentaOrden+filtroFechaInicialOrden+filtroFechaFinalOrden);
		consultaBuffer.append(filtroPresentaOrden);
		
		//--------------Se consulta el histórico de cuidados enfer en el registro enfermería -------------------//
		consultaBuffer.append(" UNION "+
							  " SELECT ere.codigo AS codigo_enca, to_char(ere.fecha_registro,'dd/mm/yyyy') AS fecha_registro,ere.hora_registro||'' AS hora_registro, " +
							  "		  getnombreusuario(ere.usuario) AS nombre_usuario, 0 AS es_medico, ere.fecha_registro || '' || ere.hora_registro AS fecha "+
							  "			FROM enca_histo_registro_enfer ere "+
							  "			INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) "+
							  "			INNER JOIN detalle_cuidado_reg_enfer dcre ON (ere.codigo=dcre.codigo_histo_enfer) ");
		
		consultaBuffer.append(filtroCuentaRegEnfer+filtroFechaInicialRegEnfer+filtroFechaFinalRegEnfer);
		consultaBuffer.append(filtroPresentaRegEnfer);
		
		//--------------Se consulta el histórico de otros cuidados enfer en la orden médica -------------------//
		consultaBuffer.append(" UNION " +
							  " SELECT eho.codigo AS codigo_enca, to_char(eho.fecha_orden, 'dd/mm/yyyy') AS fecha_registro,eho.hora_orden||'' AS hora_registro, " + 
							  "		   getnombreusuario(eho.login) AS nombre_usuario, 1 AS es_medico, eho.fecha_orden || '' || eho.hora_orden AS fecha " +
							  "			  FROM encabezado_histo_orden_m eho " +
							  "				INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo) " +
							  "				INNER JOIN detalle_otro_cuidado_enf doce ON (eho.codigo=doce.cod_histo_cuidado_enfer) ");
		
		consultaBuffer.append(filtroCuentaOrden+filtroFechaInicialOrden+filtroFechaFinalOrden);
		consultaBuffer.append(filtroPresentaOrdenOtro);
		
		//--------------Se consulta el histórico de otros cuidados enfer en el registro enfermería -------------------//
		consultaBuffer.append(" UNION " +
							  " SELECT ere.codigo AS codigo_enca, to_char(ere.fecha_registro,'dd/mm/yyyy') AS fecha_registro,ere.hora_registro||'' AS hora_registro, " +
							  "		   getnombreusuario(ere.usuario) AS nombre_usuario, 0 AS es_medico, ere.fecha_registro || '' || ere.hora_registro AS fecha " +
							  "				FROM enca_histo_registro_enfer ere " +
							  "					INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) " +
							  "					INNER JOIN detalle_otro_cuidado_renf docre ON (ere.codigo=docre.codigo_histo_enfer)");
		
		consultaBuffer.append(filtroCuentaRegEnfer+filtroFechaInicialRegEnfer+filtroFechaFinalRegEnfer);
		consultaBuffer.append(filtroPresentaRegEnferOtro);
		
		consultaBuffer.append(")x " +
							  " ORDER BY x.fecha,x.codigo_enca");
		
		logger.info("\n\nconsultarCuidadosEspecialesHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null; 
		try
		{
		stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
		rs = new ResultSetDecorator(stm.executeQuery());
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarCuidadosEspecialesHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
			e.printStackTrace();
			return null;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
		}
	}
	
	/**
	 * Método que consulta el detalle histórico de los cuidados especiales de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarCuidadosEspecialesDetalleHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="",filtroCuentaOrden="", filtroFechaInicialOrden="", filtroFechaFinalOrden="";
		String filtroCuentaRegEnfer="", filtroFechaInicialRegEnfer="", filtroFechaFinalRegEnfer="";
		
		String filtroPresentaOrden=" AND ((dce.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND dce.descripcion<>'') OR dce.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (dce.presenta IS NULL AND dce.descripcion<>''))";
		String filtroPresentaRegEnfer=" AND ((dcre.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND dcre.descripcion<>'') OR dcre.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (dcre.presenta IS NULL AND dcre.descripcion<>''))";
		String filtroPresentaOrdenOtro=" AND ((doce.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND doce.descripcion<>'') OR doce.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (doce.presenta IS NULL AND doce.descripcion<>''))";
		String filtroPresentaRegEnferOtro=" AND ((docre.presenta="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND docre.descripcion<>'') OR docre.presenta="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (docre.presenta IS NULL AND docre.descripcion<>''))";
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
 		{
 			filtroCuentaOrden=" WHERE om.cuenta  IN ("+cuentas+")";
 			filtroCuentaRegEnfer=" WHERE re.cuenta  IN ("+cuentas+")";
 		}
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
 			filtroCuentaOrden=" WHERE om.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 			filtroCuentaRegEnfer=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
 		
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				filtroFechaInicialOrden=" AND eho.fecha_orden || '-' || eho.hora_orden >= '"+fechaHoraInicial+"'";
				filtroFechaInicialRegEnfer=" AND ere.fecha_registro || '-' || ere.hora_registro >= '"+fechaHoraInicial+"'";
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   filtroFechaInicialOrden=" AND to_date(eho.fecha_orden||'','yyyy-mm-dd') >= to_date('"+fechaHoraInicial+"','yyyy-mm-dd')";
			   filtroFechaInicialRegEnfer=" AND to_date(ere.fecha_registro||'','yyyy-mm-dd') >= to_date('"+fechaHoraInicial+"','yyyy-mm-dd')";
			}
		}
		
	  if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				filtroFechaFinalOrden=" AND eho.fecha_orden || '-' || eho.hora_orden <= '"+fechaHoraFinal+"'";
				filtroFechaFinalRegEnfer=" AND ere.fecha_registro || '-' || ere.hora_registro <= '"+fechaHoraFinal+"'";
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				filtroFechaFinalOrden=" AND to_date(eho.fecha_orden||'','yyyy-mm-dd')<= to_date('"+fechaHoraFinal+"','yyyy-mm-dd')";
				filtroFechaFinalRegEnfer=" AND to_date(ere.fecha_registro||'','yyyy-mm-dd') <= to_date('"+fechaHoraFinal+"','yyyy-mm-dd')";
			}
		}
	  
	  //	--------------Se consulta el histórico de cuidados enfer en la orden médica -------------------//
		consultaBuffer.append("SELECT * FROM "+ 
							  "	  ("+
							  "		SELECT eho.codigo AS codigo_enca, tce.descripcion AS nombre_cuidado, dce.presenta AS presenta," +
							  "			   dce.descripcion AS observaciones, 1 AS es_medico,tce.control_especial as controlespecial "+
							  "				  FROM encabezado_histo_orden_m eho "+
							  "					INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo) "+
							  "					INNER JOIN detalle_cuidado_enfer dce ON (eho.codigo=dce.cod_histo_enca) "+
							  "					INNER JOIN cuidado_enfer_cc_inst ceci ON (dce.cuidado_enfer_cc_inst=ceci.codigo) "+
							  "					INNER JOIN tipo_cuidado_enfermeria tce ON (ceci.cuidado_enfermeria=tce.codigo)");
		
		consultaBuffer.append(filtroCuentaOrden+filtroFechaInicialOrden+filtroFechaFinalOrden);
		consultaBuffer.append(filtroPresentaOrden);
		
		//--------------Se consulta el histórico de cuidados enfer en el registro enfermería -------------------//
		consultaBuffer.append(" UNION "+
							  " SELECT ere.codigo AS codigo_enca, tce.descripcion AS nombre_cuidado, dcre.presenta AS presenta, " +
							  "		   dcre.descripcion AS observaciones, 0 AS es_medico,tce.control_especial as controlespecial "+
							  "				FROM enca_histo_registro_enfer ere "+
							  "					INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) "+
							  "					INNER JOIN detalle_cuidado_reg_enfer dcre ON (ere.codigo=dcre.codigo_histo_enfer) "+
							  "					INNER JOIN cuidado_enfer_cc_inst ceci ON (dcre.cuidado_enfer_cc_inst=ceci.codigo) "+
							  "					INNER JOIN tipo_cuidado_enfermeria tce ON (ceci.cuidado_enfermeria=tce.codigo)");
		
		consultaBuffer.append(filtroCuentaRegEnfer+filtroFechaInicialRegEnfer+filtroFechaFinalRegEnfer);
		consultaBuffer.append(filtroPresentaRegEnfer);
		
		//--------------Se consulta el histórico de otros cuidados enfer en la orden médica -------------------//
		consultaBuffer.append(" UNION " +
							  " SELECT eho.codigo AS codigo_enca, toce.descripcion AS nombre_cuidado, doce.presenta AS presenta, " +
							  "		   doce.descripcion AS observaciones, 1 AS es_medico,'' as controlespecial " +
							  "			  FROM encabezado_histo_orden_m eho " +
							  "				 INNER JOIN ordenes_medicas om ON (eho.orden_medica=om.codigo) " +
							  "				 INNER JOIN detalle_otro_cuidado_enf doce ON (eho.codigo=doce.cod_histo_cuidado_enfer) " +
							  "				 INNER JOIN tipo_otro_cuidado_enf toce ON (doce.otro_cual=toce.codigo)");
		
		consultaBuffer.append(filtroCuentaOrden+filtroFechaInicialOrden+filtroFechaFinalOrden);
		consultaBuffer.append(filtroPresentaOrdenOtro);
		
		//--------------Se consulta el histórico de otros cuidados enfer en el registro enfermería -------------------//
		consultaBuffer.append(" UNION " +
							  " SELECT ere.codigo AS codigo_enca, toce.descripcion AS nombre_cuidado, docre.presenta AS presenta, " +
							  "		   docre.descripcion AS observaciones, 0 AS es_medico,'' as controlespecial " +
							  "				FROM enca_histo_registro_enfer ere " +
							  "					INNER JOIN registro_enfermeria re ON (ere.registro_enfer=re.codigo) " +
							  "					INNER JOIN detalle_otro_cuidado_renf docre ON (ere.codigo=docre.codigo_histo_enfer) " +
							  "					INNER JOIN tipo_otro_cuidado_enf toce ON (docre.otro_cual=toce.codigo)");
		
		consultaBuffer.append(filtroCuentaRegEnfer+filtroFechaInicialRegEnfer+filtroFechaFinalRegEnfer);
		consultaBuffer.append(filtroPresentaRegEnferOtro);
		
		consultaBuffer.append(")x " +
							  " ORDER BY x.codigo_enca");
		
		//logger.info("\n\nconsultarCuidadosEspecialesDetalleHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
		try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		stm.close();
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarCuidadosEspecialesDetalleHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta el histórico de escala glagow de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarEscalaGlasgowHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="", filtroCuenta="";
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		
		
		
	  consultaBuffer.append("SELECT deg.enca_histo_reg_enfer AS codigo_histo_enfer,"+ 
							"		to_char(ehre.fecha_registro, 'dd/mm/yyyy') || '-' ||  ehre.hora_registro||'' AS fecha_registro, "+
							"		tgci.tipo_glasgow AS codigo_esc_glasgow,teg.nombre AS nombre_especificacion,eg.valor AS valor_glasgow, "+
							"		deg.observacion AS observacion,getnombreusuario(ehre.usuario) AS usuario,x.total AS total "+ 
							"			FROM detalle_escala_glasgow deg "+
							"			INNER JOIN enca_histo_registro_enfer ehre ON (deg.enca_histo_reg_enfer=ehre.codigo) "+                   
							"			INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo)                 "+  
							"			INNER JOIN especificaciones_glasgow eg ON (deg.especificacion_glasgow=eg.codigo) "+
							"			INNER JOIN tipo_glasgow_cc_inst tgci ON (eg.glasgow_cc_inst=tgci.codigo)           "+          
							"			INNER JOIN tipo_especificacion_glasgow teg ON (eg.tipo_especificacion_glasgow=teg.codigo) "+
							"			INNER JOIN "+
							"				(SELECT deg.enca_histo_reg_enfer AS codigo_histo_enfer, sum(eg.valor) AS total "+ 
							"				    FROM detalle_escala_glasgow deg  "+
							"					INNER JOIN enca_histo_registro_enfer ehre ON (deg.enca_histo_reg_enfer=ehre.codigo) "+
							"					INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) "+
							"					INNER JOIN especificaciones_glasgow eg ON (deg.especificacion_glasgow=eg.codigo) " +
							filtroCuenta);
	  
	  consultaBuffer.append(" GROUP BY deg.enca_histo_reg_enfer "+
							")x ON x.codigo_histo_enfer=ehre.codigo");
	  
	  consultaBuffer.append(filtroCuenta);
		
	  if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
			}
		}
		
	  if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
			}
		}
	  
	  consultaBuffer.append(" ORDER BY ehre.fecha_registro || '-' || ehre.hora_registro, ehre.codigo");
	  
	  //logger.info("\n\nconsultarEscalaGlasgowHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
		
	   try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		stm.close();
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarEscalaGlasgowHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta el histórico de pupilas de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarPupilasHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="", filtroCuenta="",filtroFechaInicial="", filtroFechaFinal="";
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				filtroFechaInicial=" AND enc.fecha_registro || '-' || enc.hora_registro >= '"+fechaHoraInicial+"'";
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   filtroFechaInicial=" AND enc.fecha_registro >= '"+fechaHoraInicial+"'";
			}
		}
		
	  if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				filtroFechaFinal=" AND enc.fecha_registro || '-' || enc.hora_registro <= '"+fechaHoraFinal+"'";
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				filtroFechaFinal=" AND enc.fecha_registro <= '"+fechaHoraFinal+"'";
			}
		}
	  
	  consultaBuffer.append("SELECT * FROM "+ 
							"	( "+
							"	SELECT  enca_histo_reg_enfer AS codigo_enca, 'Derecha' AS pupila, "+ 
							"			to_char(enc.fecha_registro,'dd/mm/yyyy') AS fecha_registro,enc.hora_registro||'' AS hora_registro,enc.fecha_registro || '-' ||  enc.hora_registro AS fecha, " +
							"			tpd.valor AS valor_tamano,dad.abreviatura AS abreviatura_tamano,dad.descripcion AS nombre_tamano,rpd.nombre AS nombre_reaccion, "+       
							"			dp.derecha_observaciones AS observaciones, getnombreusuario(enc.usuario) AS usuario     "+
							"				FROM registro_enfermeria re             "+
							"					INNER JOIN enca_histo_registro_enfer enc ON(enc.registro_enfer=re.codigo) "+         
							"					INNER JOIN detalle_caracte_pupila dp ON(dp.enca_histo_reg_enfer=enc.codigo) "+             
							"					INNER JOIN tamano_pupila tpd ON(tpd.valor=dp.derecha_tamano) "+ 
							"					INNER JOIN descripcion_abrev_pupila dad ON(dad.codigo=tpd.descripcion_abreviatura) "+ 
							"					INNER JOIN reaccion_pupila rpd ON(rpd.acronimo=dp.derecha_reaccion) " +
							filtroCuenta+filtroFechaInicial+filtroFechaFinal);
	  
	  consultaBuffer.append(" UNION " +
	  						" SELECT  enca_histo_reg_enfer AS codigo_enca, 'Izquierda' AS pupila, "+ 
							"		  to_char(enc.fecha_registro,'dd/mm/yyyy') AS fecha_registro,enc.hora_registro||'' AS hora_registro,enc.fecha_registro || '-' ||  enc.hora_registro AS fecha, " + 
							"		  tpi.valor AS valor_tamano,dai.abreviatura AS abreviatura_tamano,dai.descripcion AS nombre_tamano,rpi.nombre AS nombre_reaccion, "+      
							"		  dp.izquierda_observaciones AS observaciones, getnombreusuario(enc.usuario) AS usuario     "+ 
							"			FROM registro_enfermeria re             "+
							"				INNER JOIN enca_histo_registro_enfer enc ON(enc.registro_enfer=re.codigo) "+         
							"				INNER JOIN detalle_caracte_pupila dp ON(dp.enca_histo_reg_enfer=enc.codigo) "+              
							"				INNER JOIN tamano_pupila tpi ON(tpi.valor=dp.izquierda_tamano) "+
							"				INNER JOIN descripcion_abrev_pupila dai ON(dai.codigo=tpi.descripcion_abreviatura) "+ 
							"				INNER JOIN reaccion_pupila rpi ON(rpi.acronimo=dp.izquierda_reaccion) 	" +
							filtroCuenta+filtroFechaInicial+filtroFechaFinal);
	  
	  consultaBuffer.append(")x "+
			  				"	ORDER BY x.fecha,x.codigo_enca");
	  
	  //logger.info("\n\nconsultarPupilasHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
		
	   try
		{
		PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
		stm.close();
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarPupilasHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta el histórico de convulsiones de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarConvulsionesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT ehre.codigo AS codigohistoregistro,to_char(ehre.fecha_registro,'dd/mm/yyyy') AS fecha_registro," +
							  "		  ehre.hora_registro||'' AS hora_registro, tc.nombre as tipo_convulsion," +
							  "		  getnombreusuario(ehre.usuario) AS nombre_usuario,dc.observacion AS observacion_convulsion "+ 
							  "			  FROM detalle_convulsion dc "+
							  "				INNER JOIN  enca_histo_registro_enfer ehre ON (ehre.codigo=dc.enca_histo_reg_enfer) "+ 
							  "				INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) "+
							  "				INNER JOIN tipo_convulsion tc ON (dc.tipo_convulsion=tc.codigo)");
		

		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
    		consultaBuffer.append(filtroCuenta);
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
			{
				if(UtilidadCadena.noEsVacio(horaInicial))
				{
					fechaHoraInicial=fechaInicial+"-"+horaInicial;
					consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
				}
				else
				{
				   fechaHoraInicial=fechaInicial;
				   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
				}
			}
			
		if(UtilidadCadena.noEsVacio(fechaFinal))
			{
				if(UtilidadCadena.noEsVacio(horaFinal))
				{
					fechaHoraFinal=fechaFinal+"-"+horaFinal;
					consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
				}
				else
				{
					fechaHoraFinal=fechaFinal;
					consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
				}
			}
		
		consultaBuffer.append(" ORDER BY ehre.fecha_registro,ehre.hora_registro,ehre.codigo");
		
		 //logger.info("\n\nconsultarConvulsionesHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
			
		   try
			{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarConvulsionesHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
				return null;
			}
	}
	
	/**
	 * Método que consulta el histórico de control de esfinteres de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarControlEsfinteresHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="";
		String filtroCuenta="";
		consultaBuffer.append("SELECT ehre.codigo AS codigohisto,to_char(ehre.fecha_registro,'dd/mm/yyyy') AS fecha_registro," +
							  "		  ehre.hora_registro||'' AS hora_registro, cce.descripcion as control_esfinter, " +
							  "		  getnombreusuario(ehre.usuario) AS nombre_usuario, dce.observacion AS observacion, cce.ausente AS ausente "+  
							  "			FROM detalle_control_esfinteres dce  "+
							  "				INNER JOIN caract_control_esfinter cce ON(dce.caracte_control_esfinter=cce.codigo) "+  
							  "				INNER JOIN enca_histo_registro_enfer ehre ON(dce.enca_histo_reg_enfer=ehre.codigo) "+
							  "				INNER JOIN registro_enfermeria re ON(ehre.registro_enfer=re.codigo) ");
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
    		consultaBuffer.append(filtroCuenta);
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
			{
				if(UtilidadCadena.noEsVacio(horaInicial))
				{
					fechaHoraInicial=fechaInicial+"-"+horaInicial;
					consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'");
				}
				else
				{
				   fechaHoraInicial=fechaInicial;
				   consultaBuffer.append(" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'");
				}
			}
			
		if(UtilidadCadena.noEsVacio(fechaFinal))
			{
				if(UtilidadCadena.noEsVacio(horaFinal))
				{
					fechaHoraFinal=fechaFinal+"-"+horaFinal;
					consultaBuffer.append(" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'");
				}
				else
				{
					fechaHoraFinal=fechaFinal;
					consultaBuffer.append(" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'");
				}
			}
		
		consultaBuffer.append(" ORDER BY ehre.fecha_registro,ehre.hora_registro,ehre.codigo");
		
		 //logger.info("\n\nconsultarControlEsfinteresHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
			
		   try
			{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(stm.executeQuery()));
			stm.close();
			return mapaRetorno;
			}
			catch (SQLException e)
			{
				logger.error("Error en consultarControlEsfinteresHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
				return null;
			}	
	}
	
	/**
	 * Método que consulta el histórico de fuerza muscular de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public static HashMap consultarFuerzaMuscularHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		String fechaHoraInicial="", fechaHoraFinal="",filtroCuenta="",filtroFechaInicial="",filtroFechaFinal="";
		
		Vector cuentasConsulta= new Vector ();
 		if(mostrarInformacion.equals("A") || !UtilidadCadena.noEsVacio(mostrarInformacion))
			filtroCuenta=" WHERE re.cuenta IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(mostrarInformacion.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(mostrarInformacion.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
				filtroCuenta=" WHERE re.cuenta IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
    		
		
		
		if(UtilidadCadena.noEsVacio(fechaInicial))
		{
			if(UtilidadCadena.noEsVacio(horaInicial))
			{
				fechaHoraInicial=fechaInicial+"-"+horaInicial;
				filtroFechaInicial=" AND ehre.fecha_registro || '-' || ehre.hora_registro >= '"+fechaHoraInicial+"'";
			}
			else
			{
			   fechaHoraInicial=fechaInicial;
			   filtroFechaInicial=" AND ehre.fecha_registro >= '"+fechaHoraInicial+"'";
			}
		}
		
		if(UtilidadCadena.noEsVacio(fechaFinal))
		{
			if(UtilidadCadena.noEsVacio(horaFinal))
			{
				fechaHoraFinal=fechaFinal+"-"+horaFinal;
				filtroFechaFinal=" AND ehre.fecha_registro || '-' || ehre.hora_registro <= '"+fechaHoraFinal+"'";
			}
			else
			{
				fechaHoraFinal=fechaFinal;
				filtroFechaFinal=" AND ehre.fecha_registro <= '"+fechaHoraFinal+"'";
			}
		}

		//----------Miembro superior derecho ------------------//
		consultaBuffer.append("SELECT * FROM ("+
							  "		SELECT ehre.codigo AS codigohisto,to_char(ehre.fecha_registro, 'DD/MM/YYYY') AS fecha_registro,ehre.hora_registro||'' AS hora_registro, 'MIEMBRO SUPERIOR - Derecho' AS caracteristica, "+
							  "			   CASE WHEN tfm.nombre IS NULL THEN '' ELSE tfm.nombre END AS nombre_fuerza,"+
							  "			   CASE WHEN dfm.obs_m_sup_derecho IS NULL THEN '' ELSE dfm.obs_m_sup_derecho END AS observacion, "+
							  "			   getnombreusuario(ehre.usuario) AS nombre_usuario,ehre.fecha_registro || '-' ||  ehre.hora_registro AS fecha, '1' AS orden "+
							  "					FROM enca_histo_registro_enfer ehre "+
							  "						INNER JOIN registro_enfermeria re on (ehre.registro_enfer=re.codigo) "+ 
							  "						INNER JOIN detalle_fuerza_muscular dfm ON (ehre.codigo=dfm.enca_histo_reg_enfer) "+ 
							  "						INNER JOIN tipo_fuerza_muscular tfm ON (dfm.m_superior_derecho=tfm.acronimo)" +
							  filtroCuenta+filtroFechaInicial+filtroFechaFinal);
		
		//----------Miembro superior izquierdo---------------------//
		consultaBuffer.append("		UNION "+ 
							  "		SELECT ehre.codigo AS codigohisto,to_char(ehre.fecha_registro, 'DD/MM/YYYY') AS fecha_registro,ehre.hora_registro||'' AS hora_registro, 'MIEMBRO SUPERIOR - Izquierdo' AS caracteristica, "+
							  "			   CASE WHEN tfm.nombre IS NULL THEN '' ELSE tfm.nombre END AS nombre_fuerza, "+
							  "			   CASE WHEN dfm.obs_m_sup_izquierdo IS NULL THEN '' ELSE dfm.obs_m_sup_izquierdo END AS observacion, "+
							  "			   getnombreusuario(ehre.usuario) AS nombre_usuario,ehre.fecha_registro || '-' ||  ehre.hora_registro AS fecha,'2' AS orden "+
							  "			   	   FROM enca_histo_registro_enfer ehre "+
							  "			   	   		INNER JOIN registro_enfermeria re on (ehre.registro_enfer=re.codigo) "+ 
							  "						INNER JOIN detalle_fuerza_muscular dfm ON (ehre.codigo=dfm.enca_histo_reg_enfer) "+ 
							  "						INNER JOIN tipo_fuerza_muscular tfm ON (dfm.m_superior_izquierdo=tfm.acronimo) " +
							  filtroCuenta+filtroFechaInicial+filtroFechaFinal);
		
		//-----------Miembro Inferior derecho ------------------------------//
		consultaBuffer.append("		UNION "+
							  "		SELECT ehre.codigo AS codigohisto,to_char(ehre.fecha_registro, 'DD/MM/YYYY') AS fecha_registro,ehre.hora_registro||'' AS hora_registro, 'MIEMBRO INFERIOR - Derecho' AS caracteristica, "+
							  "			   CASE WHEN tfm.nombre IS NULL THEN '' ELSE tfm.nombre END AS nombre_fuerza, "+
							  "			   CASE WHEN dfm.obs_m_inf_derecho IS NULL THEN '' ELSE dfm.obs_m_inf_derecho END AS observacion, "+
							  "			   getnombreusuario(ehre.usuario) AS nombre_usuario,ehre.fecha_registro || '-' ||  ehre.hora_registro AS fecha,'3' AS orden "+
							  "					FROM enca_histo_registro_enfer ehre  "+
							  "						INNER JOIN registro_enfermeria re on (ehre.registro_enfer=re.codigo) "+ 
							  "						INNER JOIN detalle_fuerza_muscular dfm ON (ehre.codigo=dfm.enca_histo_reg_enfer) "+ 
							  "						INNER JOIN tipo_fuerza_muscular tfm ON (dfm.m_inferior_derecho=tfm.acronimo)" +
							  filtroCuenta+filtroFechaInicial+filtroFechaFinal);
		
		//-----------Miembro Inferior Izquierdo-----------------------------------//
		consultaBuffer.append("		UNION "+ 
							  "		SELECT ehre.codigo AS codigohisto,to_char(ehre.fecha_registro, 'DD/MM/YYYY') AS fecha_registro,ehre.hora_registro||'' AS hora_registro, 'MIEMBRO INFERIOR - Izquierdo' AS caracteristica, "+
							  "		CASE WHEN tfm.nombre IS NULL THEN '' ELSE tfm.nombre END AS nombre_fuerza,"+
							  "		CASE WHEN dfm.obs_m_inf_izquierdo IS NULL THEN '' ELSE dfm.obs_m_inf_izquierdo END AS observacion, "+
							  "		getnombreusuario(ehre.usuario) AS nombre_usuario,ehre.fecha_registro || '-' ||  ehre.hora_registro AS fecha, '4' AS orden "+
							  "			FROM enca_histo_registro_enfer ehre "+
							  "				INNER JOIN registro_enfermeria re on (ehre.registro_enfer=re.codigo) "+ 
							  "				INNER JOIN detalle_fuerza_muscular dfm ON (ehre.codigo=dfm.enca_histo_reg_enfer) "+ 
							  "				INNER JOIN tipo_fuerza_muscular tfm ON (dfm.m_inferior_izquierdo=tfm.acronimo) " +
							  filtroCuenta+filtroFechaInicial+filtroFechaFinal);
		
		consultaBuffer.append("  )x "+
							  "     ORDER BY x.fecha,x.orden");
		
		//logger.info("\n\nconsultarFuerzaMuscularHistoImpresionHC-->"+consultaBuffer.toString()+"\n");
		
		PreparedStatementDecorator stm = null;
		ResultSetDecorator rs = null;
		try
		{
		stm= new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString()));
		rs = new ResultSetDecorator(stm.executeQuery());
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs);
		
		return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarFuerzaMuscularHistoImpresionHC [SqlBaseRegistroEnfermeriaDao] "+e);
			return null;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(stm, rs, null);
		}
	}


	/**
	 * Metodo para Consultar Toda La informacion registrada desde control de liquidos desde registro de Atenciones.
	 * @param con
	 * @param codigoCuenta
	 * @param centroCosto
	 * @param institucion
	 * @param nroConsulta
	 * @param fechaUltimoFinTurno
	 * @param fechaInicio
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public static HashMap consultarControlLiquidos(Connection con, HashMap parametros)
	{
		//-- Parametros de Busqueda para el paciente especifico.
		int nroConsulta = UtilidadCadena.vInt(parametros.get("nroConsulta")+""); 
		String cuentas = parametros.get("cuentas")+""; 
		int institucion = UtilidadCadena.vInt(parametros.get("institucion")+""); 
		int centroCosto = UtilidadCadena.vInt(parametros.get("centroCosto")+"");
		String cuenta=cuentas.split(",")[0];
		
		logger.info("\n cuentas -->"+cuentas); 
		//-- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.noEsVacio(parametros.get("fechaInicio")+"") ? parametros.get("fechaInicio")+"":""; 
		String horaInicio = UtilidadCadena.noEsVacio(parametros.get("horaInicio")+"") ? parametros.get("horaInicio")+"":""; 
		String fechaFinal = UtilidadCadena.noEsVacio(parametros.get("fechaFinal")+"") ? parametros.get("fechaFinal")+"":""; 
		String horaFinal = UtilidadCadena.noEsVacio(parametros.get("horaFinal")+"") ? parametros.get("horaFinal")+"":"";
		String filtroAsocio = UtilidadCadena.noEsVacio(parametros.get("filtroAsocio")+"") ? parametros.get("filtroAsocio")+"":"";
		
		logger.info("\n filtroAsocio-->"+filtroAsocio);
		//-- Para insertar las condiciones de Busqueda a  las consultas. 
		String cadEnca = "", cadFTurno = "";
		if ( !fechaInicio.equals("") )
		{
			if ( !horaInicio.equals("") )
			{
				  cadEnca = " AND ehre.fecha_registro ||'-'|| ehre.hora_registro >= '" + fechaInicio + "-" + horaInicio + "'";
				  cadFTurno = "	AND ftbl.fecha_registro ||'-'|| ftbl.hora_registro >= '" + fechaInicio + "-" + horaInicio  +  "'";
			}
			else
			{
				  cadEnca = "  AND ehre.fecha_registro >= '" + fechaInicio + "'";
				  cadFTurno = "  AND ftbl.fecha_registro >= '" + fechaInicio + "'";
			}
		}
		if ( !fechaFinal.equals("") )
		{
			if ( !horaFinal.equals("") )
			{
			  cadEnca   += " AND ehre.fecha_registro ||'-'|| ehre.hora_registro <= '" + fechaFinal + "-" + horaFinal + "'";
			  cadFTurno += " AND ftbl.fecha_registro ||'-'|| ftbl.hora_registro <= '" + fechaFinal + "-" + horaFinal + "'";
			}
			else
			{
			  cadEnca +=   " AND ehre.fecha_registro <= '" + fechaFinal + "'";
			  cadFTurno += " AND ftbl.fecha_registro <= '" + fechaFinal + "'";
			}
		}

		
		
		//-- Generar las Consultas de las tablas parametrizadas
		String tablaElim = " ( " + tablaParametrizada("liquidos_elim_cc_inst", "tipos_liquidos_elim", institucion, centroCosto, false, "balance_liq_elim_par", cuenta) + " ) ";
		String tablaAdm  = " ( " + tablaParametrizada("liquidos_admin_cc_inst", "tipos_liquidos_admin", institucion, centroCosto,  false, "balance_liq_admin_par", cuenta)  + " ) ";
		
		String consulta ="", cadCuenta = "";

		Vector cuentasConsulta= new Vector ();
 		if(filtroAsocio.equals("A") || !UtilidadCadena.noEsVacio(filtroAsocio))
 			cadCuenta="  IN ("+cuentas+")";
 		else
 		{
 			String [] cuentaTmp=cuentas.split(",");
 			
 			if(filtroAsocio.equals("U"))
			{
	 			for (int i=0;i<cuentaTmp.length;i++)
	 			{
	 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
	 			 				
	 				if (viaIng.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
	 					cuentasConsulta.add(cuentaTmp[i]);
	 			}
			}
			else
				if(filtroAsocio.equals("H"))
				{
					for (int i=0;i<cuentaTmp.length;i++)
		 			{
		 				String viaIng=Utilidades.obtenerViaIngresoCuenta(con, cuentaTmp[i]+"");
		 			 				
		 				if (viaIng.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		 					cuentasConsulta.add(cuentaTmp[i]);
		 			}
				}
					
 			cadCuenta=" IN ("+ UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasConsulta, false)+")";
 		}
		
    		logger.info("\n cadCuenta -->"+cadCuenta);
		
		
		

		switch (nroConsulta)
		{
			case 1:    //----------------Consulta Solamente si hay registros de Control de liquidos. 
				{
				  consulta  = "	SELECT sum(cantidad) as cantidad FROM (" +
				  "	SELECT cantidad FROM (" +
				  //-----Inicio Mezclas 
				  "					SELECT count(1) as cantidad  " +
				  "						   FROM balance_liq_admin_mezcla blam 			 																			" +		
				  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
				  "								INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
				  "							   		 WHERE re.cuenta " + cadCuenta + cadEnca +
				  "					UNION ALL  " +
				  "					SELECT count(1) as cantidad  " +
				  "						   FROM fin_turno_balance_liq ftbl																					   		" +
				  " 							INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
				  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
				  "								     WHERE re.cuenta " + cadCuenta + cadFTurno +
				  "					UNION ALL  " +
				  //-----Fin Mezclas
				  "					SELECT count(1) as cantidad  " +
		  		  "						   FROM balance_liq_admin_enfer blae  " +
				  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) " +
				  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion ) " +
				  "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) " + 			    
				  "							   		 WHERE re.cuenta " + cadCuenta + cadEnca +
				  "					UNION ALL  " +
				  "					SELECT count(1) as cantidad  " +
				  "						   FROM balance_liq_admin_par blap 			" +
				  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo ) " +
				  "								INNER JOIN " + tablaAdm  + " laci ON ( laci.codigo = blap.liquidos_admin_cc_inst ) " +
				  "			 			  	    INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
				  "							   		 WHERE re.cuenta " + cadCuenta + cadEnca +
				  "				 	UNION ALL						" +
				  "					SELECT count(1) as cantidad  " +
				  " 						   FROM fin_turno_balance_liq ftbl  " +
				  "									INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo ) " +
				  " 								INNER JOIN  " + tablaAdm  + "  laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) " +
				  "			 					    INNER JOIN registro_enfermeria re ON ( re.codigo = ftbl.registro_enfer ) 			    	" +
				  "										 WHERE re.cuenta " + cadCuenta + cadFTurno + 
				  "					UNION ALL  "+ 
				  "					SELECT count(1) as cantidad  " +
				  "						   FROM fin_turno_balance_liq ftbl " + 
				  "								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo ) " +
				  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +
				  "								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo ) " +
				  "									 WHERE re.cuenta " + cadCuenta + cadFTurno +
				  "				  ) x " +
				  " UNION ALL " +
				  "	SELECT cantidad FROM ( " +
				  "					SELECT count(1) as cantidad  " +
				  "							  FROM balance_liq_elim_par blep  " + 			   
				  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
				  "							 	   INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +    
				  "			 			  	       INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer ) 			    	" +
				  "								   		 WHERE re.cuenta " + cadCuenta + cadEnca +
				  "					UNION ALL	" +						   
				  "					SELECT count(1) as cantidad  " +
				  "						   FROM fin_turno_balance_liq ftbl " +   
				  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
				  "								INNER JOIN  " + tablaElim  + "  leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
				  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
				  "									 WHERE re.cuenta " + cadCuenta + cadFTurno +
				  "					) y	" +
				  "	) z ";
			}
			break;
			case 2:     //-- Consutar el codigo y el nombre del medicamento. Solamente los que fueron registrados para ese intervalo de fecha.
			{           
				consulta  = "	SELECT * FROM (	SELECT mie.codigo as codigo, 0 as param, 'a' as tipoLiquido, mie.descripcion as nombre, " +
							"						   mie.consecutivo_liquido as codMedPaciente, 0 as codMezcla, 'false' as esmezcla	  " + 
							"						   FROM medicamento_infusion_enfer mie " +
							"		   		 				WHERE mie.codigo IN ( SELECT mie.codigo as codigo  " +
							"										  				     FROM balance_liq_admin_enfer blae " +    	
							"					 	 	  	  								  INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo ) " +  
							"						  		  	 							  INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion )  " +  
							"								  								  INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) " +     
						    "									  								   WHERE re.cuenta " + cadCuenta + cadEnca +
							"									  					UNION   						 									 " +	
							"									  					SELECT mie.codigo as codigo										 " +
							"									  						   FROM fin_turno_balance_liq ftbl  							 " +		 
							"									  								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo )	" +  
							"									  								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +  
							"									  								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo )  " +
							"									  									 WHERE re.cuenta " + cadCuenta + cadFTurno +
							" 		  	   			   		 				    )  " +
							"					 UNION ALL      " +
							"					 SELECT laci.codigo as codigo, 1 as param, 'a' as tipoLiquido, tla.nombre as nombre, " +
							"							-1 as codMedPaciente, 0 as codMezcla, 'false' as esmezcla " +  
							"						    FROM liquidos_admin_cc_inst laci    " +
							"							 	 INNER JOIN tipos_liquidos_admin tla ON ( tla.codigo = laci.tipos_liquidos_admin ) " +     			   
							"						   			  WHERE laci.codigo IN (SELECT laci.codigo " +
							"										  						   FROM balance_liq_admin_par blap " + 			 
							"										  								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo ) " +  
							"										  								INNER JOIN " + tablaAdm  + "  laci ON ( laci.codigo = blap.liquidos_admin_cc_inst )    " +
							"																		INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
							"										  							   		 WHERE re.cuenta " + cadCuenta + cadEnca +
							"										  					UNION    " +
							"										   					SELECT laci.codigo " +
							"										    					   FROM fin_turno_balance_liq ftbl  " +
							"										   								INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo )  " +
							"										    							INNER JOIN " + tablaAdm  + "  laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) " +
							"																		INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
							"										   									 WHERE re.cuenta " + cadCuenta + cadFTurno +
							"											   				)				   	   " +
							"					UNION ALL 																	  " +				 	
							"		    		SELECT od.codigo_histo_enca as codigo, 0 as param,  'a' as tipoLiquido, " + 
							"		    			   m.nombre || ' (' || tm.nombre || ') ' as nombre, -1 as codMedPaciente,  1 as codMezcla, 'true' as esmezcla 		" +
							"		    	 	   	   FROM orden_dieta od																" +
							"	 	  						INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )    					" +
							"		 						INNER JOIN tipo_mezcla tm ON ( tm.codigo = m.cod_tipo_mezcla ) 				" +
							"						  		WHERE od.codigo_histo_enca IN (" +
							"														SELECT blam.orden_dieta as codigo					"	 +
							"															   FROM balance_liq_admin_mezcla blam 			 																			" +		
							"															   		INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
							"															   		INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
							"									   									 WHERE  re.cuenta " + cadCuenta + cadEnca +
							"														UNION 																									" +
							"														SELECT dftlm.orden_dieta as codigo											" +
							"															   FROM fin_turno_balance_liq ftbl																					   		" +
							"															  	    INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
							"															   		INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
							"									   									 WHERE re.cuenta " + cadCuenta + cadFTurno +
							"														)	" +	
							" ) z ORDER BY param, codMezcla DESC, codMedPaciente ";
			} 
			break;					
			//---Consultar los nombre de los liquidos eliminados (SOLAMENTE HAY PARATRIZABLES).
			case 3:
			{
				consulta =	 " 	SELECT leci.codigo as codigoElim, tle.nombre as nombreElim, 1 as paramElim, 'e' as tipoLiquidoElim  " + 
						     " 	       FROM liquidos_elim_cc_inst leci                            " +
							 "				INNER JOIN tipos_liquidos_elim tle ON ( tle.codigo = leci.tipos_liquidos_elim ) " + 
						     " 				     WHERE leci.codigo IN ( SELECT leci.codigo " + 
 							 "												   FROM balance_liq_elim_par blep " +   			 
							 " 							  							INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
							 " 							  							INNER JOIN " + tablaElim  + "  leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +
							 "														INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
							 " 							  						   		 WHERE re.cuenta " + cadCuenta + cadEnca + 
							 " 								  		    UNION     "+
							 " 			  							    SELECT leci.codigo 			"+  
							 " 												   FROM fin_turno_balance_liq ftbl  "+   								    					   
							 " 														INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo ) "+   
							 " 								  						INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )  "+ 
							 "														INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
							 "															 WHERE re.cuenta " + cadCuenta + cadFTurno +
							 " 											)	" +
							 "				ORDER BY tle.nombre ";  
			}
			break;
			case 4:     //------Consultar la informaci?n del balance de liquidos  registrados administrados y eliminados anteriormente parametrizados y no parametrizados
			{			//------Para el intervalo de fecha independientemente si esta activo o inactivo en el sistema, o suspendido o no.
				consulta    = "	SELECT * FROM (					 " + 
							  "	SELECT * FROM ( " +
							  //----------Parte de Mezclas.
							  "					SELECT getDatosMedico(ehre.usuario) as medico, " +
							  "						   to_char(ehre.fecha_grabacion,'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion, " +   
							  "						   to_char(ehre.fecha_registro,'DD/MM/YYYY') as fecha_registro, ehre.hora_registro||'' as hora_registro,		" +
							  "						   ehre.fecha_grabacion as fecha, ehre.hora_grabacion as hora, ehre.codigo as codEncabezado,								" +
							  "						   blam.orden_dieta as codigoMed, 0 as paramMed, 'a' as tipoliquidoMed, blam.valor as valorMed, 1 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp			" +
							  "						   FROM balance_liq_admin_mezcla blam 			 																			" +		
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blam.codigo_histo_enfer = ehre.codigo )  							" +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = ehre.registro_enfer )   											" +
							  "							   		 WHERE re.cuenta " + cadCuenta + cadEnca +
							  "					UNION ALL  " +
							  "					SELECT  getDatosMedico(ftbl.usuario) as medico,	" +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,				    " +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro||'' as hora_registro,   " +
							  " 					   ftbl.fecha as fecha, ftbl.hora as hora, dftlm.codigo_turno*-1 as codEncabezado,  										" +
							  "						   dftlm.orden_dieta as codigomed, 0 as paramMed, 'a' as tipoliquidoMed,  dftlm.total as valormed, 1 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp	 	" +  	
		  					  "						   FROM fin_turno_balance_liq ftbl																					   		" +
		  					  " 							INNER JOIN detalle_fin_turno_liq_mezcla dftlm ON ( dftlm.codigo_turno = ftbl.codigo )  								" +
		  					  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo )											" +
							  "									 WHERE re.cuenta " + cadCuenta + cadFTurno +
							  //----------Parte de Mezclas.
							  "					UNION ALL  " +
							  "					SELECT getDatosMedico(ehre.usuario) as medico, " +
							  "						   to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion, 	" +
							  "						   to_char(ehre.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ehre.hora_registro||'' as hora_registro, 		" +
							  "						   ehre.fecha_grabacion as fecha, ehre.hora_grabacion as hora, ehre.codigo as codEncabezado,							  		" +
							  "		   				   mie.codigo as codigoMed, 0 as paramMed, 'a' as tipoliquidoMed, blae.valor as valorMed, 0 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp								  		" +
					  		  "						   FROM balance_liq_admin_enfer blae 																					 		" +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blae.codigo_histo_enfer = ehre.codigo )							  		" +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = blae.medicamento_infusion ) 						  		" +
							  "								INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer ) 										  		" + 			    
							  "							   		 WHERE re.cuenta " + cadCuenta + cadEnca + 
							  "					UNION ALL  " +
							  "					SELECT getDatosMedico(ehre.usuario) as medico,	" +
							  "						   to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion,	 " +
							  "						   to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ehre.hora_registro||''  as hora_registro,		 " + 
					  		  "						   ehre.fecha_grabacion as fecha,  ehre.hora_grabacion as hora,  ehre.codigo as codEncabezado, 								     " +
					  		  "						   laci.codigo as codigoMed, 1 as paramMed, 'a' as tipoliquidoMed,  blap.valor as valorMed, 0 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp 								 		 " +
							  "						   FROM balance_liq_admin_par blap 																						  		 " +
							  "								INNER JOIN enca_histo_registro_enfer ehre ON ( blap.codigo_histo_enfer = ehre.codigo )							  		 " +
							  "								INNER JOIN " + tablaAdm  + " laci ON ( laci.codigo = blap.liquidos_admin_cc_inst ) 						 				 " +
							  "								INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
							  "							   		 WHERE re.cuenta " + cadCuenta + cadEnca +
							  "				 	UNION ALL						" + 
							  "					SELECT  getDatosMedico(ftbl.usuario) as medico,	" +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,				    " +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro||'' as hora_registro,   " +
							  " 					   ftbl.fecha as fecha, ftbl.hora as hora, codigo_turno*-1 as codEncabezado,  												" +
							  "						   dftlap.liquidos_admin_cc_inst as codigomed, 1 as paramMed,  'a' as tipoliquidoMed,  dftlap.total as valormed, 0 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp  			" +  	
							  " 						   FROM fin_turno_balance_liq ftbl  																					" +
							  "									INNER JOIN det_fin_turno_liq_adm_par dftlap ON ( dftlap.codigo_turno = ftbl.codigo ) 							" +
							  " 								INNER JOIN " + tablaAdm  + " laci ON ( laci.codigo = dftlap.liquidos_admin_cc_inst ) 							" +
							  "									INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) 										" +
							  "										 WHERE re.cuenta " + cadCuenta + cadFTurno + 
							  "					UNION ALL  "+ 
							  "					SELECT  getDatosMedico(ftbl.usuario) as medico,	" +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,					" +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ftbl.hora_registro||'' as hora_registro, 	" +
							  "						   ftbl.fecha as fecha, ftbl.hora as hora, codigo_turno*-1 as codEncabezado, dftla.medicamento_infusion_enfer as codigomed, " +
							  "						   0 as paramMed, 'a' as tipoliquidoMed,  dftla.total as valormed, 0 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp 	  					   									" +
							  "						   FROM fin_turno_balance_liq ftbl " + 
							  "								INNER JOIN detalle_fin_turno_liq_admin dftla ON ( dftla.codigo_turno = ftbl.codigo ) " +
							  "								INNER JOIN medicamento_infusion_enfer mie ON ( mie.codigo = dftla.medicamento_infusion_enfer ) " +
							  "								INNER JOIN registro_enfermeria re ON ( mie.registro_enfer = re.codigo ) " +
							  "									 WHERE re.cuenta " + cadCuenta + cadFTurno + 
							  "				  ) x " +
							  " UNION ALL " +
							  "	SELECT * FROM (  SELECT getDatosMedico(ehre.usuario) as medico, " +
							  "							to_char(ehre.fecha_grabacion, 'DD/MM/YYYY') as fecha_grabacion, ehre.hora_grabacion||'' as hora_grabacion, 	" +
							  "							to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registro, ehre.hora_registro||''  as hora_registro, 		" +
							  "						    ehre.fecha_grabacion as fecha, ehre.hora_grabacion as hora,																	" +
							  "							ehre.codigo as codEncabezado, leci.codigo as codigoMed, 1 as paramMed, 'e' as tipoliquidoMed, blep.valor as valorMed, 0 as codMezclaMed,ehre.fecha_registro as fecha_temp,ehre.hora_registro as hora_temp 		" +
							  "							  FROM balance_liq_elim_par blep  																							" + 			   
							  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) 								" +    
							  "							 	   INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) 									" +    
							  "								   INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) 											" +
							  "								   		 WHERE re.cuenta " + cadCuenta + cadEnca +
							  "					UNION ALL	" +						   
							  "					SELECT getDatosMedico(ftbl.usuario) as medico," +
							  "						   to_char(ftbl.fecha, 'DD/MM/YYYY') as fecha_grabacion, ftbl.hora||'' as hora_grabacion,						" +
							  "						   to_char(ftbl.fecha_registro, 'DD/MM/YYYY')  as fecha_registro, ftbl.hora_registro||'' as hora_registro,  		" +     
							  "						   ftbl.fecha as fecha, ftbl.hora as hora, dftlep.codigo_turno*-1 as codEncabezado, dftlep.liquidos_elim_cc_inst as codigoMed,	" +
							  "						   1 as paramMed,  'e' as tipoliquidoMed,   dftlep.total as valormed, 0 as codMezclaMed,ftbl.fecha_registro as fecha_temp,ftbl.hora_registro as hora_temp " +       	     
							  "						   FROM fin_turno_balance_liq ftbl " +   
							  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
							  "								INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
							  "								INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
							  "									 WHERE re.cuenta " + cadCuenta + cadFTurno +
							  "					) y" +
							  "	) z ORDER BY fecha_temp, hora_temp,codEncabezado desc ";
			}
			break;
			case  5:
			{
					consulta    = "	SELECT * FROM (  SELECT to_char(ehre.fecha_registro, 'DD/MM/YYYY') as fecha_registroMedElim, ehre.hora_registro||''  as hora_registroMedElim,  " +
								  "						    ehre.codigo as codEncabezadoMedElim, leci.codigo as codigoMedElim, 1 as paramMedElim, blep.valor as valorMedElim,  " + 				   
								  "							  ehre.fecha_grabacion as fechaMedElim, ehre.hora_grabacion as horaMedElim, 'e' as tipoLiquidoMedElim " +
								  "							  FROM balance_liq_elim_par blep  " + 			   
								  "		 						   INNER JOIN enca_histo_registro_enfer ehre ON ( blep.codigo_histo_enfer = ehre.codigo ) " +    
								  "							 	   INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = blep.liquidos_elim_cc_inst ) " +    
								  "								   INNER JOIN registro_enfermeria re ON ( ehre.registro_enfer = re.codigo ) " +
								  "								   		 WHERE re.cuenta " + cadCuenta + cadEnca +
								  "					UNION ALL	" +						   
								  "					SELECT to_char(ftbl.fecha, 'DD/MM/YYYY')  as fecha_registroMedElim, ftbl.hora||'' as hora_registroMedElim,  " +     
								  "						   dftlep.codigo_turno*-1 as codEncabezadoMedElim, dftlep.liquidos_elim_cc_inst as codigoMedElim, 1 as paramMedElim, dftlep.total as valorMedElim, " +       	
								  "						   ftbl.fecha as fechaMedElim, ftbl.hora as horaMedElim, 'e' as tipoLiquidoMedElim   " +      
								  "						   FROM fin_turno_balance_liq ftbl " +   
								  "								INNER JOIN detalle_fin_turno_liq_elim dftlep ON ( dftlep.codigo_turno = ftbl.codigo )  " +    
								  "								INNER JOIN " + tablaElim  + " leci ON ( leci.codigo = dftlep.liquidos_elim_cc_inst )    " + 
								  "								   INNER JOIN registro_enfermeria re ON ( ftbl.registro_enfer = re.codigo ) " +
								  "									 WHERE re.cuenta " + cadCuenta + cadFTurno + 
								  "					) y ORDER BY fecha_registroMedElim, hora_registroMedElim ";
			}
			case 6:   //---Consultar los medicamentos no parametrizados para cada paciente especifico.  (Seccion Liquidos y Medicamentos de Infusion).
			{
				consulta = "	SELECT * FROM (" +
						   "	SELECT mie.codigo as codigoMedEnfer, mie.registro_enfer, mie.consecutivo_liquido, mie.descripcion, mie.volumen_total||'',   " + 
	     				   " 		   mie.velocidad_infusion, mie.suspendido,   																		" + 
						   "	       CASE WHEN (SELECT medicamento_infusion FROM balance_liq_admin_enfer blae  									 	" +
						   "							 INNER JOIN enca_histo_registro_enfer ehre ON ( ehre.codigo = blae.codigo_histo_enfer )			" + 	
	     	     		   "		   					 INNER JOIN registro_enfermeria renfer ON ( renfer.codigo = ehre.registro_enfer ) 				" + 	
	     				   "								  WHERE blae.medicamento_infusion=mie.codigo 												" + 
	     				   "			 						AND renfer.cuenta " + cadCuenta +
	     				   "							   GROUP BY medicamento_infusion) IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END as ya_registrado,			" +
	     				   "		   "+ValoresPorDefecto.getValorFalseParaConsultas()+" as finalizaron, '' as medico_f, '' as fecha_registro_f, '' as fecha_grabacion_f							" +
						   " 		   FROM medicamento_infusion_enfer mie 																				" +
						   "				INNER JOIN registro_enfermeria re ON ( re.codigo = mie.registro_enfer )										" +
						   "					 WHERE re.cuenta " + cadCuenta +
					//	   "					   AND mie.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() + 
						   "	UNION ALL																												" + 
						   "	SELECT ehom.codigo as codigoMedEnfer, m.consecutivo as registro_enfer, -1 as consecutivo_liquido,  						" + 
						   "		   m.nombre || ' (' || tm.nombre || ') ' || " +
						   "		   CASE WHEN sol.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada +" 								" +
						   "				THEN '     ( Pendiente )' ELSE '' END as descripcion,										  							" +
						   "	 	   od.volumen_total as volumen_total, od.velocidad_infusion as velocidad_infusion,      							" +
						   "		   CASE WHEN sol.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada +" 								" +
						   "				THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END as suspendido, 																	" +
						   "		   "+ValoresPorDefecto.getValorTrueParaConsultas()+" as ya_registrado, od.finaliza_sol as finalizaron,															" +
						   "       	   getdatosmedicoespecialidades(ehom.login,',') as medico_f,  														" +
						   "		   to_char(ehom.fecha_orden,'DD/MM/YYYY') ||'  '|| ehom.hora_orden||'' as fecha_registro_f, 			" +
						   "		   to_char(ehom.fecha_grabacion,'DD/MM/YYYY') ||'  '|| ehom.hora_grabacion||'' as fecha_grabacion_f  " +
						   "	 	   FROM orden_dieta od																								" +
						   "	 	  		INNER JOIN mezcla m ON ( m.consecutivo = od.mezcla )    													" +
						   "		 		INNER JOIN tipo_mezcla tm ON ( tm.codigo = m.cod_tipo_mezcla ) 												" +
						   "		 		INNER JOIN encabezado_histo_orden_m ehom ON ( od.codigo_histo_enca = ehom.codigo )							" +
						   "	 	 		INNER JOIN ordenes_medicas om ON ( om.codigo = ehom.orden_medica )   										" +		
						   "	 	 		INNER JOIN solicitudes_medicamentos sm ON ( sm.orden_dieta = od.codigo_histo_enca )							" +   
						   "	 	 		INNER JOIN solicitudes sol ON ( sol.numero_solicitud = sm.numero_solicitud )								" +
						   "	 	  			 WHERE om.cuenta " + cadCuenta +
			//			   "					   AND od.suspendido = " + ValoresPorDefecto.getValorFalseParaConsultas() + 	
						   "					   AND sol.estado_historia_clinica " +
						   "							IN( " + ConstantesBD.codigoEstadoHCAdministrada +" ,"+ ConstantesBD.codigoEstadoHCSolicitada +") "+ 	
						   "  ) x ORDER BY  consecutivo_liquido ";	
			}
		}
		
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null; 
		try
		{
			logger.info("\n\n NroConsulta Historia Atenciones [ " +  nroConsulta  + " ] Consulta (consultarLiqMedicamentosPaciente) ----> " + consulta + "\n\n");				
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs = new ResultSetDecorator(pst.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(rs,true,false);
			
			return mapaRetorno;

		}
		catch (SQLException e)
		{
			logger.error("Error en cargarHistoricosDieta de SqlBaseRegistroEnfermeriaDao: "+e);
			return null;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		
	}
	
	/**
	 * Consultar Toma de Muestras.
	 * @param con
	 * @param parametros
	 * @param paraHistorico TODO
	 * @return
	 */
	public static HashMap consultarTomaMuestras(Connection con, HashMap parametros, boolean paraHistorico)
	{
		String cuentas = parametros.get("cuentas")+""; 
		
		String consulta;
		
		if(!paraHistorico){
			
			consulta  = "  SELECT to_char(s.fecha_solicitud, 'DD/MM/YYYY') ||' '|| s.hora_solicitud||'' as fecha,	" +
						   " 	 	 s.numero_solicitud as solicitud, ser.especialidad || '-' ||  ser.codigo || '  ' || rser.descripcion as nom_servicio,  " +  
						   " 	 	 getnombrepersona(s.codigo_medico) as medico, " +
						   "		 CASE WHEN sp.fecha_toma_muestra IS NULL THEN 'no' ELSE 'si' END as esmuestranueva 					" +
						   "		 FROM solicitudes s																					" +
						   "			  INNER JOIN sol_procedimientos sp ON ( sp.numero_solicitud = s.numero_solicitud )				" +
						   "			  INNER JOIN servicios ser ON ( ser.codigo = sp.codigo_servicio_solicitado )					" +
						   "			  INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo )						" +
						   "			  INNER JOIN cuentas c ON ( c.id = s.cuenta AND c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"') " +
					       "			 	  WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +  		
						   " 			 	    AND c.id  IN (" +cuentas + ")"+
						   " 			 	    AND ser.toma_muestra = '"  + ConstantesBD.acronimoSi + "'" + 
						   "			 	  	AND s.estado_historia_clinica =  " + ConstantesBD.codigoEstadoHCSolicitada; 
		}
		else{
			consulta  = "  SELECT s.numero_solicitud as solicitud FROM solicitudes s " +
			   "			  INNER JOIN sol_procedimientos sp ON ( sp.numero_solicitud = s.numero_solicitud ) " +
			   "			  INNER JOIN servicios ser ON ( ser.codigo = sp.codigo_servicio_solicitado ) " +
			   "			  INNER JOIN referencias_servicio rser ON ( rser.servicio = ser.codigo ) " +
			   "			  INNER JOIN cuentas c ON ( c.id = s.cuenta AND c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"') " +
		       "			 	  WHERE rser.tipo_tarifario = " + ConstantesBD.codigoTarifarioCups +  		
			   " 			 	    AND c.id  IN (" +cuentas + ")"+
			   " 			 	    AND ser.toma_muestra = '"  + ConstantesBD.acronimoSi + "'" +
			   "			 	  	AND s.estado_historia_clinica in (" + ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCTomaDeMuestra+","+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+")"; 

		}
		
		
		
		try
		{
			logger.info("\n\n Consultando Muestras [" + consulta + "\n\n");				
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			logger.info("tamaño mapa muestra "+mapaRetorno);

			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en consultarTomaMuestras de SqlBaseRegistroEnfermeriaDao: "+e);
			return null;
		}
		
	}



	public static Collection consultarAnotacionesEnfermeriaFechas(Connection con, String cuentas)
	{
		StringBuffer consultaBuffer=new StringBuffer();
		
		consultaBuffer.append("SELECT ehre.fecha_registro AS fecha_reg " +
								" FROM registro_enfermeria re " +
								" INNER JOIN enca_histo_registro_enfer ehre ON (ehre.registro_enfer=re.codigo) " +
								" INNER JOIN anotaciones_reg_enfer are ON (ehre.codigo=are.codigo_histo_enfer) ");
		
	
    	consultaBuffer.append(" WHERE re.cuenta IN ("+cuentas+")");
    	
		
		consultaBuffer.append(" group by ehre.fecha_registro order by ehre.fecha_registro");
		
		try
			{
				//logger.info("consultarAnotacionesEnfermeriaFecha-->\n" + consultaBuffer.toString() + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				Collection coleccionRetorno=UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));
				ps.close();
				return coleccionRetorno;
			}
	    catch (SQLException e)
		{
			logger.error("Error Consultado el histórico de las anotaciones de enfermería en SqlBaseRegistroEnfermeria"+e.toString());
			return null;
		}
	}



	public static int consultarUltimaDietaActiva(Connection con, int codigoCuenta) 
	{
		String consultaStr = " SELECT max(od.codigo_histo_enca) as codigo_encabezado " +
							 "  from ordenes_medicas om INNER JOIN encabezado_histo_orden_m enca " +
							 "	ON(enca.orden_medica=om.codigo) INNER JOIN orden_dieta od " +
							 "	ON(od.codigo_histo_enca=enca.codigo) " +
							 "	where om.cuenta=? ";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigo_encabezado");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}



	public static boolean actualizarOrdenDieta(Connection con, int codOrdenDieta, String suspendidoEnfermeria, String observacionesEnfermeria) 
	{
		String actualizarOrdenDietaStr = "UPDATE orden_dieta SET suspendido_enfermeria =?, " +
										 " observaciones =? where codigo_histo_enca=? ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarOrdenDietaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(codOrdenDieta > 0)
			{
				ps.setString(1, suspendidoEnfermeria);
				ps.setString(2, observacionesEnfermeria);
				ps.setInt(3, codOrdenDieta);
								
				if (ps.executeUpdate()>0)
					return true;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return false;
		
	}



	public static String consultarInterfazNutricion(Connection con) 
	{
		try
		{
			String consultaStr="SELECT valor from valores_por_defecto where parametro=?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, ConstantesValoresPorDefecto.nombreValoresDefectoInterfazNutricion);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}



	public static HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String consultaStr = "SELECT o.nutricion_oral_cc_inst AS codigo, '' AS descripcion, " +
							"tno.codigo_interfaz as codigoInterfaz FROM orden_nutricion_oral o INNER JOIN nutricion_oral_cc_inst NOCCI " +
							"on(NOCCI.codigo=o.nutricion_oral_cc_inst) inner join tipo_nutricion_oral tno " +
							"on(tno.codigo=NOCCI.nutricion_oral) WHERE " +
							"codigo_historico_dieta=(SELECT MAX (codigo_historico_dieta) as codigo_histo " +
							"FROM orden_nutricion_oral ono INNER JOIN orden_dieta od ON " +
							"(ono.codigo_historico_dieta=od.codigo_histo_enca) INNER JOIN encabezado_histo_orden_m eh ON " +
							"(eh.codigo=od.codigo_histo_enca)  INNER JOIN ordenes_medicas om ON (eh.orden_medica=om.codigo)  " +
							"WHERE om.cuenta=? ) "; 
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCuenta);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * Consultar la Fecha de la Orden de la Dieta para Enviar a otro sistema Interfaz
	 * @param con
	 * @param codOrdenDieta
	 * @return
	 */
	public static String consultarFechaDieta(Connection con, int codOrdenDieta) 
	{
		try
		{
			String consultaStr="SELECT fecha_orden from encabezado_histo_orden_m where codigo =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codOrdenDieta);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("fecha_orden");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	/**
	 * Consultar la Hora de la Orden de la Dieta para Enviar a otro sistema Interfaz
	 * @param con
	 * @param codOrdenDieta
	 * @return
	 */
	public static String consultarHoraDieta(Connection con, int codOrdenDieta) 
	{
		try
		{
			String consultaHoraStr="SELECT hora_orden from encabezado_histo_orden_m where codigo =? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaHoraStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codOrdenDieta);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("hora_orden");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	/**
	 * Consultar El piso al que esta asociado el paciente para enviar a sistema interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public static String consultarPisoCama(Connection con, int codigoCama) 
	{
		try
		{
			String consultaStr="SELECT pi.codigo_piso AS piso from camas1 cam " +
											"INNER JOIN habitaciones hab ON (hab.codigo=cam.habitacion) " +
											"INNER JOIN pisos pi ON (pi.codigo=hab.piso) where cam.codigo=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoCama);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("piso");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	
	/**
	 * Consultar el Numero de Cama que esta asociado al paciente para enviar por sistema interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public static String consultarNumeroCama(Connection con, int codigoCama) 
	{
		try
		{
			String consultaStr="SELECT numero_cama from camas1 where codigo=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoCama);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("numero_cama");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	

	/**
	 * Consultar el convenio que tiene asociado el paciente es VIP para enviar a sistema interfaz
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static String consultarConvenioVip(Connection con, int codigoConvenio) 
	{
		try
		{
			String consultaStr="SELECT vip from convenios where codigo=? ";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoConvenio);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("vip");	
						
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
	 * @param cuentasPacientes
	 * @return
	 */
	public static boolean pacienteTieneOrdenesMezclas(Connection con,ArrayList<CuentasPaciente> cuentasPacientes, boolean todasMezclas) 
	{
		boolean resultado=false;
		String cuentas=ConstantesBD.codigoNuncaValido+"";
		for(int i=0;i<cuentasPacientes.size();i++)
		{
			cuentas=cuentas+","+cuentasPacientes.get(i).getCodigoCuenta();
		}
		String cadena = "SELECT count(1) from solicitudes s inner join solicitudes_medicamentos sm on(sm.numero_solicitud=s.numero_solicitud)   where s.cuenta in("+cuentas+") and sm.orden_dieta is not null and sm.pendiente_completar='"+ConstantesBD.acronimoSi+"'";
		
		//logger.info("valor de la cadena >> "+cadena);
		
		 try
		 {
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			 if(rs.next())
				resultado=(rs.getInt(1)>0);
		 }
		 catch (Exception e) 
		 {
			e.printStackTrace();
		}
		 return resultado;

	}



	/**
	 * 
	 * @param con
	 * @param cuentasPacientes
	 * @return
	 */
	public static HashMap mezclasPendientesPaciente(Connection con, ArrayList<CuentasPaciente> cuentasPacientes) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cuentas=ConstantesBD.codigoNuncaValido+"";
		for(int i=0;i<cuentasPacientes.size();i++)
		{
			cuentas=cuentas+","+cuentasPacientes.get(i).getCodigoCuenta();
		}
		//indices=fecha,fechaoriginal,numeroorden,codigomezcla,mezcla,dosificacion,pendientecompletar,codigoestado,estado
		String cadena="SELECT " +
								" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fecha," +
								" s.fecha_solicitud as fechaoriginal," +
								" sm.orden_dieta as numeroorden," +
								" s.numero_solicitud as numerosolicitud," +
								" od.mezcla as codigomezcla," +
								" m.nombre as mezcla," +
								" od.dosificacion as dosificacion," +
								" sm.pendiente_completar as pendientecompletar," +
								" s.estado_historia_clinica as codigoestado," +
								" getEstadoSolhis(estado_historia_clinica) as estado" +
						" from solicitudes s inner " +
						" join solicitudes_medicamentos sm on(sm.numero_solicitud=s.numero_solicitud) " +
						" inner join orden_dieta od on (sm.orden_dieta=od.codigo_histo_enca) " +
						" inner join mezcla m on (od.mezcla=m.consecutivo) " +
						" where s.cuenta in("+cuentas+") and " +
						" sm.pendiente_completar='"+ConstantesBD.acronimoSi+"' and " +
						" s.estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada+
						" order by s.fecha_solicitud ";
		
		try
		{
			logger.info(cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
		
	}

	public static HashMap consultarTomaMuestrasHistorico(Connection con, HashMap parametros) {
//		logger.info("parametros "+parametros.toString());
		HashMap mapaHistorico=consultarTomaMuestras(con, parametros, true);
		
		int numRegistros=Utilidades.convertirAEntero(mapaHistorico.get("numRegistros").toString());
		String solicitudes="";
//		logger.info("numero solicitudes "+numRegistros);
		for(int i=0; i<numRegistros; i++)
		{
			if(!solicitudes.equals(""))
			{
				solicitudes+=",";
			}
			solicitudes+=mapaHistorico.get("solicitud_"+i);
		}
		
		StringBuffer consultaBuffer=new StringBuffer();
		
//		logger.info("solicitudes  "+solicitudes);
		consultaBuffer.append("SELECT "+
				"hist.numero_solicitud AS numero_solicitud, "+
				"hist.fecha AS fecha, "+
				"hist.hora AS hora, "+
				"hist.usuario AS usuario, "+
				"getnombrepersona(s.codigo_medico) AS nombre_profesional, "+
				"	ser.especialidad || '-' ||  ser.codigo || '  ' || rs.descripcion as nom_servicio, "+
				"	to_char(s.fecha_solicitud, 'DD/MM/YYYY') ||' '|| s.hora_solicitud||'' as fecha_orden "+
				"FROM "+
				"	toma_mues_his_solpro hist "+ 
				"INNER JOIN "+
				"	sol_procedimientos sp "+
				"		ON(hist.numero_solicitud=sp.numero_solicitud) "+
				"INNER JOIN "+
				"	servicios ser "+
				"		ON(sp.codigo_servicio_solicitado=ser.codigo) "+
				"INNER JOIN "+
				"	referencias_servicio rs "+
				"		ON(ser.codigo=rs.servicio AND rs.tipo_tarifario=0) "+
				"INNER JOIN "+
				"	solicitudes s "+
				"		ON(sp.numero_solicitud=s.numero_solicitud) "+
				"WHERE "+
				"	hist.numero_solicitud "+
				"		IN("+(solicitudes.trim().equals("")?ConstantesBD.codigoNuncaValido+"":solicitudes)+") "+
				"ORDER BY " +
				"	hist.numero_solicitud"); 
		
		try
			{
				logger.info("consulta historico Toma Muestra -->\n" + consultaBuffer.toString() + " \n");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaBuffer.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							
				return UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
			}
	    catch (SQLException e)
		{
			logger.error("Error Consultado el histórico de toma de muestras en SqlBaseRegistroEnfermeria"+e.toString());
			return null;
		}

	}
	
	
	

	/**
	 * 
	 * @param con
	 * @param ingresoPaciente
	 * @param cuentaPaciente
	 * @param centroCostoPaciente
	 * @param cargarParametrizacion
	 * @return
	 */
	public static ArrayList<Object> cargarValoracionEnfermeria(Connection con, int ingresoPaciente, int cuentaPaciente,int centroCostoPaciente, boolean cargarParametrizacion,boolean historico) 
	{
		ArrayList<Object> resultado=new ArrayList<Object>();
		Connection conLocal=null;
		//cargar lo que ya se ha llenado al paciente.
		String consulta="";
		if(!historico)
		{
			consulta="SELECT DISTINCT " +
								" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
								" ETIQUIETA_CAMPO as etiquetacampo," +
								" coalesce(CODIGO_SECCION,-1) as codigoseccion," +
								" ETIQUETA_SECCION as etiquetaseccion," +
								" ORDEN_SECCION as ordenseccion,"+
								" ORDEN_CAMPO as ordencampo," +
								" CENTRO_COSTO as centrocosto," +
								" CAMPO_OTRO as campootro" +
						" FROM manejopaciente.valoracion_enfermeria ve " ;
		}
		else
		{
			consulta="SELECT " +
								" CODIGO_HISTO_ENCA as codigohistoenca," +
								" coalesce(CODIGO_CAMPO,-1) as codigocampo," +
								" ETIQUIETA_CAMPO as etiquetacampo," +
								" coalesce(CODIGO_SECCION,-1) as codigoseccion," +
								" ETIQUETA_SECCION as etiquetaseccion," +
								" ORDEN_SECCION as ordenseccion,"+
								" ORDEN_CAMPO as ordencampo," +
								" CENTRO_COSTO as centrocosto," +
								" VALOR as valor," +
								" CAMPO_OTRO as campootro," +
								" to_char(eh.fecha_registro,'dd/mm/yyyy') as fecha," +
								" substr(eh.hora_registro,0,5) as hora," +
								" usuario as loginusuario," +
								" getdatosmedico(usuario) as nombreusuario " +
						" FROM manejopaciente.valoracion_enfermeria ve " ;
		}
		String where1="";
		if(!historico)
		{
			if(cuentaPaciente>0)
				where1=" where CODIGO_HISTO_ENCA IN  (SELECT eh.codigo FROM enca_histo_registro_enfer eh INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo)  WHERE re.cuenta="+cuentaPaciente+")";
			else if(ingresoPaciente>0)
				where1=" where CODIGO_HISTO_ENCA IN  (SELECT eh.codigo FROM enca_histo_registro_enfer eh INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo)  WHERE re.cuenta in (select id from cuentas where id_ingreso="+ingresoPaciente+"))";
			consulta=consulta+" "+where1;
			if(cargarParametrizacion)
			{
				//cargar lo parametrizado, pero que no tiene informacion.
				consulta=consulta+" UNION select  "+
									" cve.codigo as codigocampo," +
									" cve.etiqueta_campo as etiquetacampo," +
									" sve.codigo as codigoseccion," +
									" sve.etiqueta_subseccion as etiquetaseccion," +
									" sve.orden as ordenseccion, " +
									" cve.orden as ordencampo," +
									" sve.centro_costo as centrocosto," +
									" 'N' as campootro " +
								" FROM  manejopaciente.campos_val_enfermeria cve inner join manejopaciente.secciones_val_enfermeria sve on (cve.seccion=sve.codigo) where sve.centro_costo = "+centroCostoPaciente+" and cve.codigo not in (select codigo_campo from manejopaciente.valoracion_enfermeria "+where1+" and codigo_campo is not null)"; 
				
			}
		}
		else
		{
			consulta=consulta+" inner join enca_histo_registro_enfer eh on (ve.CODIGO_HISTO_ENCA=eh.codigo) INNER JOIN registro_enfermeria re ON (eh.registro_enfer=re.codigo) ";
			if(cuentaPaciente>0)
				where1="  WHERE re.cuenta="+cuentaPaciente;
			else if(ingresoPaciente>0)
				where1=" where re.cuenta in (select id from cuentas where id_ingreso="+ingresoPaciente+")";
			consulta=consulta+" "+where1;
		}
		if(con==null)
		{
			conLocal=UtilidadBD.abrirConexion();
		}
		else
		{
			conLocal=con;
		}
		if(!historico)
		{
			consulta=consulta+" order by campootro ASC, ordenseccion asc, ordencampo asc ";
		}
		else
		{
			consulta=consulta+" order by codigohistoenca desc,campootro desc, ordenseccion asc, ordencampo asc ";
		}
		PreparedStatementDecorator ps=new PreparedStatementDecorator(conLocal, consulta);
		try
		{
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int codigoEncaAnterior=ConstantesBD.codigoNuncaValido;
			while(rs.next())
			{
				DtoValoracionEnfermeria dto=new DtoValoracionEnfermeria();
				dto.setCodigoCampo(rs.getInt("codigocampo"));
				dto.setEtiquietaCampo(rs.getString("etiquetacampo"));
				dto.setOrdenCampo(rs.getInt("ordencampo"));
				dto.setCodigoSeccion(rs.getInt("codigoseccion"));
				dto.setEtiquietaSeccion(rs.getString("etiquetaseccion"));
				dto.setOrdenSeccion(rs.getInt("ordenseccion"));
				dto.setCentroCosto(rs.getInt("centrocosto"));
				dto.setCampoOtro(rs.getString("campootro"));
				if(historico)
				{
					dto.setCodigoHistoEnca(rs.getInt("codigohistoenca"));
					dto.setValor(rs.getString("valor"));
					
					//cuadrando historicos.
					DtoHistoricoValoracionEnfermeria histo=null;
					if(codigoEncaAnterior!=rs.getInt("codigohistoenca"))
					{
						histo=new DtoHistoricoValoracionEnfermeria();
						histo.setFecha(rs.getString("fecha"));
						histo.setHora(rs.getString("hora"));
						histo.setLoginUsuario(rs.getString("loginusuario"));
						histo.setCodigoHistoEnca(rs.getInt("codigohistoenca"));
						histo.setNombreUsuario(rs.getString("nombreusuario"));
						
					}
					else
					{
						histo=((DtoHistoricoValoracionEnfermeria)resultado.get(resultado.size()-1));
					}
					histo.getResultados().add(dto);
					if(codigoEncaAnterior!=rs.getInt("codigohistoenca"))
					{
						resultado.add(histo);
					}
					codigoEncaAnterior=rs.getInt("codigohistoenca");
				}
				else
				{
					resultado.add(dto);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		if(con==null)
		{
			UtilidadBD.closeConnection(conLocal);
		}
		return resultado;
	}





	public static int insertarValoracionEnfermeria(Connection con,
			int codigoEncabezado,
			ArrayList<Object> valoracionEnfermeria) 
	{
		String cadena="insert into manejopaciente.valoracion_enfermeria (codigo_pk,CODIGO_HISTO_ENCA,CODIGO_CAMPO,ETIQUIETA_CAMPO,ORDEN_CAMPO,CODIGO_SECCION,ETIQUETA_SECCION,ORDEN_SECCION,CENTRO_COSTO,VALOR,CAMPO_OTRO) values(?,?,?,?,?,?,?,?,?,?,?)";
		for(int i=0;i<valoracionEnfermeria.size();i++)
		{
			try
			{
				DtoValoracionEnfermeria dto=(DtoValoracionEnfermeria)valoracionEnfermeria.get(i);
				if(!dto.getValor().isEmpty())
				{
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cadena);
					int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejopaciente.seq_val_enf");
					ps.setInt(1, codigo);
					ps.setInt(2, codigoEncabezado);
					if(!UtilidadTexto.getBoolean(dto.getCampoOtro()))
						ps.setInt(3, dto.getCodigoCampo());
					else
						ps.setObject(3, null);
					ps.setString(4, dto.getEtiquietaCampo());
					ps.setInt(5, dto.getOrdenCampo());
					if(!UtilidadTexto.getBoolean(dto.getCampoOtro()))
						ps.setInt(6, dto.getCodigoSeccion());
					else
						ps.setObject(6, null);
					ps.setString(7, dto.getEtiquietaSeccion());
					ps.setInt(8, dto.getOrdenSeccion());
					ps.setInt(9, dto.getCentroCosto());
					ps.setString(10, dto.getValor());
					ps.setString(11, dto.getCampoOtro());
					ps.executeUpdate();
				}
			}
			catch(Exception e)
			{
				Log4JManager.error("error",e);
				return ConstantesBD.codigoNuncaValido;
			}
			
		}
		return 1;
	}
	


	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public static int insertaResultadosLaboratorios(Connection con,
			int codigoEncabezado,
			ArrayList<Object> resultadoLaboratorios) 
	{
		String cadena="insert into manejopaciente.resultado_laboratorio_regenf (codigo_pk,CODIGO_HISTO_ENCA,CODIGO_CAMPO,ETIQUIETA_CAMPO,ORDEN,CENTRO_COSTO,VALOR,CAMPO_OTRO) values(?,?,?,?,?,?,?,?)";
		// TODO Auto-generated method stub
		for(int i=0;i<resultadoLaboratorios.size();i++)
		{
			try
			{
				DtoResultadoLaboratorio dto=(DtoResultadoLaboratorio)resultadoLaboratorios.get(i);
				if(!dto.getValor().isEmpty())
				{
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cadena);
					int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejopaciente.seq_reslab_enf");
					ps.setInt(1, codigo);
					ps.setInt(2, codigoEncabezado);
					if(!UtilidadTexto.getBoolean(dto.getCampoOtro()))
						ps.setInt(3, dto.getCodigoCampo());
					else
						ps.setObject(3, null);
					ps.setString(4, dto.getEtiquietaCampo());
					ps.setInt(5, dto.getOrden());
					ps.setInt(6, dto.getCentroCosto());
					ps.setString(7, dto.getValor());
					ps.setString(8, dto.getCampoOtro());
					ps.executeUpdate();
				}
			}
			catch(Exception e)
			{
				Log4JManager.error("error",e);
				return ConstantesBD.codigoNuncaValido;
			}
			
		}
		return 1;
	}
	
	/**
	 * Inserta los registros de alerta cada vez que se genera un registro de orden medica
	 * @param con
	 * @param listaRegistroOrdenesMedicas
	 * @return Arreglo con el nombre de las secciones que no se guardaron para mostrar en el log de errores.
	 * 		   Si se guardaron todos los registros este arreglo debe retornar vacio.
	 */
	public static ArrayList<String> insertarRegistroAlertaOrdenesMedicas(Connection con, 
			ArrayList<DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas) {
		
		ArrayList<String> listaErrores = new ArrayList<String>();
		PreparedStatement ps = null;
		long codigo = ConstantesBD.codigoNuncaValidoLong;
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO registro_alerta_ordenes_med ");
		sql.append("(codigo, tipo_seccion_orden_med, cuenta, activo, medico_ordena, fecha_orden, ");
		sql.append("hora_orden, registro_enfermeria, usuario_registra, fecha_registro, hora_registro) ");
		sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?)");
		
		try {
			
			for (DtoRegistroAlertaOrdenesMedicas dtoRegistroAlertaOrdenesMedicas : listaRegistroOrdenesMedicas) {
				
				if (puedeInsertarRegistroAlertaOrdenMedica(con, dtoRegistroAlertaOrdenesMedicas.getCuenta(), 
						dtoRegistroAlertaOrdenesMedicas.getTipoSeccionOrden())) {

					codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reg_alerta_ordenes_med");

					if (codigo != ConstantesBD.codigoNuncaValidoLong) {

						ps = con.prepareStatement(sql.toString());
						ps.setLong(1, codigo);
						ps.setInt(2, dtoRegistroAlertaOrdenesMedicas.getTipoSeccionOrden());
						ps.setLong(3, dtoRegistroAlertaOrdenesMedicas.getCuenta());
						ps.setString(4, UtilidadTexto.convertirSN(dtoRegistroAlertaOrdenesMedicas.getActivo()));
						ps.setString(5, dtoRegistroAlertaOrdenesMedicas.getMedicoOrdena());
						ps.setDate(6, dtoRegistroAlertaOrdenesMedicas.getFechaOrden());
						ps.setString(7, dtoRegistroAlertaOrdenesMedicas.getHoraOrden());
						if (dtoRegistroAlertaOrdenesMedicas.getRegistroEnfermeria() > 0) {
							ps.setLong(8, dtoRegistroAlertaOrdenesMedicas.getRegistroEnfermeria());
						} else {
							ps.setNull(8, Types.INTEGER);
						}
						ps.setString(9, dtoRegistroAlertaOrdenesMedicas.getUsuarioRegistra());
						ps.setDate(10, dtoRegistroAlertaOrdenesMedicas.getFechaRegistro());
						ps.setString(11, dtoRegistroAlertaOrdenesMedicas.getHoraRegistro());

						if(ps.executeUpdate() <= 0) {
							listaErrores.add(dtoRegistroAlertaOrdenesMedicas.getNombreSeccionOrden());
						}
					} else {
						listaErrores.add(dtoRegistroAlertaOrdenesMedicas.getNombreSeccionOrden());
					}
				}
			}
			
		} catch (Exception e) {
			Log4JManager.error("Insertando registro alerta orden medica: ", e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					Log4JManager.error("Cerrando PreparedStatement: ", e);
				}
			}
		}
		
		return listaErrores;
	}
	
	/**
	 * Metódo encargado de validar si se puede insertar un registro de alerta de ingreso de 
	 * órden médica. Solo se puede insertar el registro no existe una registro previamente insertado
	 * para una cuenta, seccion y estado activo
	 * @param con
	 * @param cuenta
	 * @param seccion
	 * @return
	 */
	public static boolean puedeInsertarRegistroAlertaOrdenMedica (Connection con, long cuenta, int seccion) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM registro_alerta_ordenes_med ");
		sql.append("WHERE cuenta = ? AND tipo_seccion_orden_med = ? AND activo = '" + ConstantesBD.acronimoSi +"'");
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean puedeInsertarRegistro = false;
		
		try {
			
			if(cuenta > 0 && seccion > 0) {
				ps = con.prepareStatement(sql.toString());
				ps.setLong(1, cuenta);
				ps.setInt(2, seccion);
				
				rs = ps.executeQuery();
				
				if (rs.next()) {
					if(rs.getInt(1) < 1) {
						puedeInsertarRegistro = true;
					}
				}
			}
			
		} catch (Exception e) {
			Log4JManager.error("Consultando registro alerta orden medica: ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Cerrando PreparedStatement: ", e);
			}
		}
		
		return puedeInsertarRegistro;
	}
	
	
	/**
	 * Metódo encargado de consultar las secciones con alerta de registro de órden mádica para
	 * para una cuenta dada y estado activo
	 * @param con
	 * @param cuenta
	 * @param seccion
	 * @return
	 */
	public static HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> consultarAlertaOrdenMedicaCuenta (Connection con, long cuenta) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo, tipo_seccion_orden_med, cuenta, activo, medico_ordena, fecha_orden, ");
		sql.append("hora_orden, registro_enfermeria, usuario_registra, fecha_registro, hora_registro ");
		sql.append("FROM registro_alerta_ordenes_med WHERE cuenta = ? AND activo = '" + ConstantesBD.acronimoSi +"'");
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaRegistrosAlerta = 
			new HashMap<Integer, DtoRegistroAlertaOrdenesMedicas>();
		
		try {
			
			if(cuenta > 0) {
				ps = con.prepareStatement(sql.toString());
				ps.setLong(1, cuenta);
								
				rs = ps.executeQuery();
				
				while (rs.next()) {
					DtoRegistroAlertaOrdenesMedicas dtoRegistro = new DtoRegistroAlertaOrdenesMedicas();
					dtoRegistro.setCodigo(rs.getLong("codigo"));
					dtoRegistro.setTipoSeccionOrden(rs.getInt("tipo_seccion_orden_med"));
					dtoRegistro.setCuenta(cuenta);
					dtoRegistro.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					dtoRegistro.setMedicoOrdena(rs.getString("medico_ordena"));
					dtoRegistro.setFechaOrden(rs.getDate("fecha_orden"));
					dtoRegistro.setHoraOrden(rs.getString("hora_orden"));
					dtoRegistro.setRegistroEnfermeria(rs.getLong("registro_enfermeria"));
					dtoRegistro.setUsuarioRegistra(rs.getString("usuario_registra"));
					dtoRegistro.setFechaRegistro(rs.getDate("fecha_registro"));
					dtoRegistro.setHoraRegistro(rs.getString("hora_registro"));
					
					listaRegistrosAlerta.put(rs.getInt("tipo_seccion_orden_med"), dtoRegistro);
				}
			}
			
		} catch (Exception e) {
			Log4JManager.error("Consultando registro alerta orden medica: ", e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Cerrando PreparedStatement: ", e);
			}
		}
		
		return listaRegistrosAlerta;
	}
	
	
	/**
	 * Método encargado de actualizar el registro de alerta de nuevas ordenes medicas e
	 * inactivar los registros que ya fueron revisados.
	 * Se recibe la fecha en que se inicio el registro de enfermería para validar que 
	 * no se cambie el estado a las alertas que hayan sido generadas por el medico mientras
	 * la enfermera tenía abierta la ventana de registro de enfermería.
	 * 
	 * @param con
	 * @param listaRegistroOrdenesMedicas
	 * @param fechaInicioRevision
	 * @param horaInicioRevision
	 * @return 
	 */
	public static boolean actualizarRegistroAlertaOrdenesMedicas(Connection con, 
			HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas, 
			String fechaInicioRevision, String horaInicioRevision, long registroEnfermeria,
			String usuarioModifica) {
		
		boolean resultado = false;
		PreparedStatement ps = null;
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE registro_alerta_ordenes_med ");
		sql.append("SET activo = ?, registro_enfermeria = ?, ");
		sql.append("usuario_registra = ?, fecha_registro = ?, hora_registro = ? ");
		sql.append("WHERE codigo = ? AND cuenta = ? AND activo = ?");
		
		try {
			
			Set<Integer> setRegistros = listaRegistroOrdenesMedicas.keySet();
			
			for (Integer codigoSeccion : setRegistros) {
				DtoRegistroAlertaOrdenesMedicas dtoRegistroAlertaOrdenesMedicas = 
					listaRegistroOrdenesMedicas.get(codigoSeccion);
				
				if(UtilidadFecha.compararFechasMenorOIgual(
						UtilidadFecha.conversionFormatoFechaAAp(dtoRegistroAlertaOrdenesMedicas.getFechaOrden()), 
						dtoRegistroAlertaOrdenesMedicas.getHoraOrden(), 
						UtilidadFecha.conversionFormatoFechaAAp(fechaInicioRevision), horaInicioRevision).isTrue()){

					ps = con.prepareStatement(sql.toString());
					ps.setString(1, ConstantesBD.acronimoNo);
					ps.setLong(2, registroEnfermeria);
					ps.setString(3, usuarioModifica);
					ps.setDate(4, Date.valueOf(Utilidades.capturarFechaBD(con)));
					ps.setString(5, Utilidades.capturarHoraBD(con));
					ps.setLong(6, dtoRegistroAlertaOrdenesMedicas.getCodigo());
					ps.setLong(7, dtoRegistroAlertaOrdenesMedicas.getCuenta());
					ps.setString(8, ConstantesBD.acronimoSi);

					if(ps.executeUpdate() > 0) {
						resultado = true;
					}
				}
			}
			
		} catch (Exception e) {
			Log4JManager.error("Modificando registro alerta orden medica: ", e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					Log4JManager.error("Cerrando PreparedStatement: ", e);
				}
			}
		}
		
		return resultado;
	}
	
}
