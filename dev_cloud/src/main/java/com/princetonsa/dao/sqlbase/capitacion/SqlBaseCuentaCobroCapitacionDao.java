/*
 * Creado en Jun 13, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseCuentaCobroCapitacionDao
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCuentaCobroCapitacionDao.class);
	
	/**
	 * Método que consulta los cargues que cumplen con los parámetros de búsqueda
	 * ya sea por periodo o por convenio
	 * del paciente
	 * @param con
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	public static HashMap consultarCargues(Connection con, String fechaFinal, String fechaInicial, int convenio)
	{
		StringBuffer consultaStr=new StringBuffer();
		
		//--------------Si convenio es != -1 se realiza la busqueda por convenio-------------//
		if (convenio != -1)
		{
			consultaStr.append("SELECT cc.codigo AS codigo, con.nombre AS nombre_convenio, c.numero_contrato AS contrato, to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
												"				to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
												"				cc.total_pacientes AS total_usuarios, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
												"				cc.total_pacientes AS total_usuarios_old, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc_old, " +
												"				cc.total_pacientes*cc.upc AS valor_total, c.tipo_pago AS tipo_pago, " +
												"				c.convenio AS codigo_convenio, 1 AS cargue "+
												"		FROM capitacion.contrato_cargue cc  "+
												"			INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
												"			INNER JOIN convenios con ON (con.codigo=c.convenio) "+
												"				WHERE cc.fecha_inicial >= ? AND cc.fecha_final <= ? " +
												"						AND c.convenio=?" +
												"						AND (cc.cuenta_cobro IS NULL OR cc.cuenta_cobro=0) " +
												"						AND cc.total_pacientes>0" +
												"						AND con.tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado+
												"						AND cc.anulado="+ValoresPorDefecto.getValorFalseParaConsultas()+
												"			ORDER BY con.nombre");
		}
		else
		{
			consultaStr.append("SELECT cc.codigo AS codigo, con.nombre AS nombre_convenio, c.numero_contrato AS contrato, to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
												"				to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
												"				cc.total_pacientes AS total_usuarios, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
												"				cc.total_pacientes AS total_usuarios_old, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc_old, " +
												"				cc.total_pacientes*cc.upc AS valor_total, c.tipo_pago AS tipo_pago, " +
												"				c.convenio AS codigo_convenio, 1 AS cargue "+
												"		FROM capitacion.contrato_cargue cc  "+
												"			INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
												"			INNER JOIN convenios con ON (con.codigo=c.convenio) "+
												"				WHERE cc.fecha_inicial >= ? AND cc.fecha_final <= ? " +
												"						AND (cc.cuenta_cobro IS NULL OR cc.cuenta_cobro=0) " +
												"						AND cc.total_pacientes>0" +
												"						AND con.tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado+
												"						AND cc.anulado="+ValoresPorDefecto.getValorFalseParaConsultas()+
												"			ORDER BY con.nombre");
		}
		PreparedStatementDecorator ps = null;
		try
		{
		
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString()));
		logger.info("\n====>Consulta: "+consultaStr.toString());
		logger.info("\n====>Fecha Inicial: "+fechaInicial+" ====>Fecha Final: "+fechaFinal+" ====>Convenio: "+convenio);
		if (convenio != -1)
			{
				ps.setString(1, fechaInicial);
				ps.setString(2, fechaFinal);
				ps.setInt(3, convenio);
			}
		else
			{
				ps.setString(1, fechaInicial);
				ps.setString(2, fechaFinal);
			}
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		return mapaRetorno;
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando los cargues en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que actualiza el contrato cargue con la cuenta de cobro y el valor total
	 * @param con
	 * @param codigoCargue
	 * @param consecutivoCxCCapitacion
	 * @param valorTotal
	 * @param institucion
	 * @return
	 */
	public static int actualizarContratoCargue(Connection con, int codigoCargue, int consecutivoCxCCapitacion, double valorTotal, int institucion)
	{
		PreparedStatementDecorator ps =null;;
		int resp=0;
								
		try
			{
				String cad = " UPDATE  capitacion.contrato_cargue SET cuenta_cobro=?, valor_total=?, institucion=?" +
										"		WHERE codigo=?	";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setDouble(1, Utilidades.convertirADouble(consecutivoCxCCapitacion+""));				
				ps.setDouble(2, valorTotal);
				ps.setInt(3, institucion);
				ps.setDouble(4, Utilidades.convertirADouble(codigoCargue+""));
													
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = codigoCargue;
				}
					
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la actualización del contrato_cargue  actualizarContratoCargue: SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}
	
	/**
	 * Método que actualiza el total a pagar en cargue_grupo_etareo, el valor total y
	 * cuenta de cobro en contrato_cargue
	 * @param con
	 * @param codigoCargue
	 * @param consecutivoCartera
	 * @param institucion
	 * @return valorTotalCargue
	 */
	public static int actualizarCargueGrupoEtareo(Connection con, int codigoCargue, int consecutivoCartera, int institucion)
	{
		PreparedStatementDecorator ps =null;
		int resp=0;
								
		try
			{
				//-----Se actualiza el total a pagar en la relación cargue grupo etáreo--------//
				String cad = " UPDATE capitacion.cargue_grupo_etareo SET total_a_pagar=total_usuarios*upc " +
												"WHERE contrato_cargue=?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setInt(1,  codigoCargue);				
																	
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = codigoCargue;
				}
				
				//---------Se realiza la sumatoria de los total a pagar y se actualiza en contrato_cargue el valor total y el código de la cuenta de cobro---------// 
				cad="UPDATE capitacion.contrato_cargue SET cuenta_cobro=?,  institucion=?, valor_total=" +
								"(SELECT SUM(total_a_pagar) FROM capitacion.cargue_grupo_etareo " +
									"WHERE contrato_cargue=? GROUP BY contrato_cargue) " +
								"WHERE codigo=?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setDouble(1,  Utilidades.convertirADouble(consecutivoCartera+""));
				ps.setInt(2,  institucion);
				ps.setDouble(3,  Utilidades.convertirADouble(codigoCargue+""));				
				ps.setDouble(4,  Utilidades.convertirADouble(codigoCargue+""));
																	
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = codigoCargue;
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la actualización del cargue grupo etáreo: SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp=-1;
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}
	
	/**
	 * Método que inserta la cuenta de cobro de capitación
	 * @param con
	 * @param numeroCuentaCobro
	 * @param convenioCxC
	 * @param estadoCartera
	 * @param loginUsuario
	 * @param valorInicialCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observaciones
	 * @param codigoInstitucionInt
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @return
	 */
	public static  int insertarCuentaCobroCapitacion(Connection con, int numeroCuentaCobro, int convenioCxC, int estadoCartera, String loginUsuario, double valorInicialCuenta, String fechaInicial, String fechaFinal, String observaciones, int codigoInstitucion, String fechaElaboracion, String horaElaboracion, String centroAtencion)
	{
		PreparedStatementDecorator ps =null;
		int resp=0;
		String cad = "";
						
		try
			{
				cad = "INSERT INTO capitacion.cuentas_cobro_capitacion " +
								"(" +
									"numero_cuenta_cobro," + 	//1
									"convenio," +			 	//2
									"estado," +					//3
									"usuario_genera," +			//4
									"fecha_radicacion," +		//5
									"numero_radicacion," +		//6
									"usuario_radica," +			//7
									"valor_inicial_cuenta," +	//8
									"saldo_cuenta," +			//9
									"fecha_inicial," +			//10
									"fecha_final," +			//11
									"obs_generacion," +			//12
									"obs_radicacion," +			//13
									"institucion, " +			//14
									"fecha_elaboracion," +		//15
									"hora_elaboracion, " +		//16
									"ajustes_debito, " +		
									"ajustes_credito, " +
									"valor_pagos," +
									"contabilizado," +
									"centro_atencion) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0,'"+ConstantesBD.acronimoNo+"',?)";
				
				logger.info("\n\nnumero_cuenta_cobro:: "+numeroCuentaCobro);
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setDouble(1, Utilidades.convertirADouble(numeroCuentaCobro+""));				
				ps.setInt(2, convenioCxC);
				ps.setInt(3, estadoCartera);
				ps.setString(4, loginUsuario);
				ps.setNull(5, Types.DATE);
				ps.setNull(6, Types.VARCHAR);
				ps.setNull(7, Types.VARCHAR);
				ps.setDouble(8, valorInicialCuenta);
				ps.setDouble(9, valorInicialCuenta);
				ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
				ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
				ps.setString(12, observaciones);
				ps.setNull(13, Types.VARCHAR);
				ps.setInt(14, codigoInstitucion);
				ps.setDate(15, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaElaboracion)));
				if(horaElaboracion.length()>5)
					ps.setString(16, horaElaboracion.substring(0,5));
				else
					ps.setString(16, horaElaboracion);
				ps.setInt(17, Utilidades.convertirAEntero(centroAtencion));
								
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = numeroCuentaCobro;
				}
					
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la inserción insertarCuentaCobroCapitacion : SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp = -1;
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}

	/**
	 * Método que consulta las cuentas de cobro por convenio, de acuerdo a la fecha 
	 * inicial y final
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarCuentasCobroConvenio(Connection con, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
		String consultaStr="SELECT " +
			"cxc.numero_cuenta_cobro AS cuenta_cobro," +
			"cxc.valor_inicial_cuenta AS valor_inicial_cuenta," +
			"cxc.valor_pagos AS valor_pagos," +
			"cxc.saldo_cuenta AS saldo_cuenta," +
			"cxc.ajustes_debito AS ajuste_debito," +
			"cxc.ajustes_credito AS ajuste_credito," +
			"ec.descripcion AS nombre_estado," +
			"to_char(cxc.fecha_elaboracion, 'dd/mm/yyyy') AS fecha_elaboracion," +
			"to_char(cxc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial,"+
			"to_char(cxc.fecha_final, 'dd/mm/yyyy') AS fecha_final," +
			"cxc.obs_generacion AS obs_generacion," +
			"getNitConvenio(cxc.convenio) AS nit_convenio, "+
			"CASE WHEN cxc.fecha_radicacion IS NULL THEN '' ELSE to_char(cxc.fecha_radicacion,'DD/MM/YYYY') END AS fecha_radicacion, "+
			"CASE WHEN cxc.numero_radicacion IS NULL THEN '' ELSE cxc.numero_radicacion END AS numero_radicacion, "+
			"c.nombre AS nombre_convenio, " +
			"e.direccion AS direccion, " +
			"e.telefono AS telefono " +
			"FROM capitacion.cuentas_cobro_capitacion cxc " +
			"INNER JOIN convenios c ON(c.codigo=cxc.convenio) " +
			"INNER JOIN empresas e ON(e.codigo=c.empresa) " +
			"INNER JOIN cartera.estados_cartera ec ON (cxc.estado=ec.codigo) "+
			"WHERE fecha_inicial=? AND fecha_final=? AND cxc.institucion=?" +
			"AND estado="+ConstantesBD.codigoEstadoCarteraGenerado;
		PreparedStatementDecorator ps =null;
		
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		ps.setDate(1, Date.valueOf(fechaInicial));
		ps.setDate(2, Date.valueOf(fechaFinal));
		ps.setInt(3, codigoInstitucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando las cuentas de cobro por convenio:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que consulta los contratos_cargue asociados a la cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarDetalleCuentaCobro(Connection con, int numeroCuentaCobro, int institucion)
	{
		String consultaStr=	"SELECT " +
							"cc.codigo as cargue, " +
							"c.numero_contrato AS contrato, " +
							"c.numero_contrato AS numero_contrato, " +
							"to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
							"to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, " +
							"to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
							"cc.total_pacientes AS total_pacientes, " +
							"CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
							"cc.valor_total AS valor_total, " +
							"CASE WHEN cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) IS NULL THEN 0 " +
							"ELSE cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) END AS saldo," +
							"abs(abs(cc.ajustes_debito) - abs(cc.ajustes_credito)) AS ajustes," +
							"cc.valor_total + (abs(cc.ajustes_debito)-abs(cc.ajustes_credito)) AS total," +
							"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc || '' END AS valor_upc_contrato, " + //se usará para la impresion
							"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc || '' END AS porcentaje_upc_contrato, " + //se usará para la impresion
							"c.valor AS valor_contrato " + //se usará para la impresion
							"	FROM " +
							"	capitacion.contrato_cargue cc " +
							"		INNER JOIN contratos c ON (cc.contrato=c.codigo)" +
							"			WHERE cc.cuenta_cobro=? AND cc.institucion=? " +
							"				 ORDER BY cc.fecha_cargue,c.numero_contrato";
		PreparedStatementDecorator ps =null;

		try
		{
		
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		ps.setDouble(1, Utilidades.convertirADouble(numeroCuentaCobro+""));
		ps.setInt(2, institucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando el detalle de la cuenta de cobro:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que consulta la información de la cuenta de cobro de acuerdo
	 * al numero de la cuenta de cobro y a la institución
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoInstitucion
	 * @param estadoCartera   1 -> Se filtra por estado generado, radicado
	 * 												  2  ->Se filtra por estado generado, radicado, anulado
	 * 												  -1- > No se filtra por el estado de cartera		 															
	 * @return
	 */
	public static HashMap consultarCuentaCobro(Connection con, int numeroCuentaCobro, int codigoInstitucion, int estadoCartera)
	{
		StringBuffer consultaStr=new StringBuffer();
		
		
		consultaStr.append("SELECT " +
			"cxc.numero_cuenta_cobro AS numero_cuenta_cobro," +
			"i.pref_factura as prefijo_factura, " +
			"ec.descripcion AS nombre_estado, " +
			"cxc.estado AS codigo_estado, " +
			"c.nombre AS nombre_convenio," +
			"cxc.convenio AS codigo_convenio," +
			"to_char(cxc.fecha_elaboracion, 'dd/mm/yyyy') AS fecha_elaboracion," +
			"cxc.hora_elaboracion AS hora_elaboracion," +
			"to_char(cxc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
			"to_char(cxc.fecha_final, 'dd/mm/yyyy') AS fecha_final," +
			"cxc.obs_generacion AS obs_generacion," +
			"getNitConvenio(cxc.convenio) AS nit_convenio, " +
			"to_char(cxc.fecha_radicacion, 'dd/mm/yyyy') AS fecha_radicacion, " +
			"cxc.valor_inicial_cuenta as valor_inicial_cuenta," +
			"cxc.numero_radicacion AS numero_radicacion, " +
			"cxc.valor_pagos AS valor_pagos," +
			"cxc.saldo_cuenta AS saldo_cuenta," +
			"cxc.ajustes_debito AS ajuste_debito," +
			"cxc.ajustes_credito AS ajuste_credito," +
			"e.direccion AS direccion_convenio, " +
			"e.telefono AS telefono_convenio "+ 
			"FROM cuentas_cobro_capitacion cxc  "+
			"INNER JOIN convenios c ON(c.codigo=cxc.convenio) " +
			"INNER JOIN empresas e ON(e.codigo=c.empresa) "+
			"INNER JOIN estados_cartera ec ON (cxc.estado=ec.codigo) " +
			"INNER JOIN instituciones i ON (i.codigo=cxc.institucion) "+
			"WHERE cxc.numero_cuenta_cobro=? AND cxc.institucion=?" );
		
		if (estadoCartera != -1)
			{
				consultaStr.append("				AND (cxc.estado="+ConstantesBD.codigoEstadoCarteraGenerado+" OR cxc.estado="+ConstantesBD.codigoEstadoCarteraRadicada);
					
				//-----Si es estadoCartera es igual a 2 se permite el estado anulado ----//
				if(estadoCartera==2)
					consultaStr.append("	OR cxc.estado="+ConstantesBD.codigoEstadoCarteraAnulado+")");
				else
					consultaStr.append(")");
			}
		PreparedStatementDecorator ps =null;
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString()));
		ps.setDouble(1, Utilidades.convertirADouble(numeroCuentaCobro+""));
		ps.setInt(2, codigoInstitucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando la información de la cuenta de cobro consultarCuentaCobro:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que realiza la busqueda avanzada de las cuentas de cobro de acuerdo
	 * a los parámetros de búsqueda
	 * @param con
	 * @param cuentaCobro
	 * @param cuentaCobroFinal
	 * @param convenio
	 * @param fechaElaboracion
	 * @param estadoCuentaCobro
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultaAvanzadaCuentaCobro(Connection con, String cuentaCobro, String cuentaCobroFinal, int convenio, String fechaElaboracion, int estadoCuentaCobro, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
		String consultaStr=armarConsulta(cuentaCobro, cuentaCobroFinal, convenio, fechaElaboracion, estadoCuentaCobro, fechaInicial, fechaFinal, codigoInstitucion);
		PreparedStatementDecorator ps =null;
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
				
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

		Utilidades.imprimirMapa(mapaRetorno);
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando  consultaAvanzadaCuentaCobro:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 *  Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada.
	 * @param cuentaCobro
	 * @param cuentaCobroFinal
	 * @param convenio
	 * @param fechaElaboracion
	 * @param estadoCuentaCobro
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	private static String armarConsulta  (String cuentaCobro, String cuentaCobroFinal, int convenio, String fechaElaboracion, int estadoCuentaCobro, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
			StringBuffer consulta=	new StringBuffer();
			
			consulta.append("SELECT cxc.numero_cuenta_cobro AS numero_cuenta_cobro,getnombreconvenio(cxc.convenio) AS nombre_convenio,to_char(cxc.fecha_elaboracion, 'dd/mm/yyyy') AS fecha_elaboracion," +
											"				to_char(cxc.fecha_inicial, 'dd/mm/yyyy') || ' - ' || to_char(cxc.fecha_final, 'dd/mm/yyyy') AS periodo,ec.descripcion AS nombre_estado,cxc.saldo_cuenta AS saldo_cuenta "+
											"	FROM capitacion.cuentas_cobro_capitacion cxc "+ 
											"		INNER JOIN cartera.estados_cartera ec ON (cxc.estado=ec.codigo) "+
											"	WHERE cxc.institucion="+codigoInstitucion);
			
		if (UtilidadCadena.noEsVacio(cuentaCobro))
		{
			//----------Si no es vacío la cuenta de cobro final se verifica en el rango
			if (UtilidadCadena.noEsVacio(cuentaCobroFinal) && !cuentaCobroFinal.equals("-1"))
				consulta.append(" AND cxc.numero_cuenta_cobro>="+cuentaCobro+" AND cxc.numero_cuenta_cobro<="+cuentaCobroFinal);
			else
				consulta.append(" AND cxc.numero_cuenta_cobro="+cuentaCobro);
		}
		
		if (convenio!=-1)
		{
			consulta.append(" AND cxc.convenio="+convenio);
		}
				
		if (UtilidadCadena.noEsVacio(fechaElaboracion))
		{
			consulta.append(" AND cxc.fecha_elaboracion='"+UtilidadFecha.conversionFormatoFechaABD(fechaElaboracion)+"'");
		}
		
		if (estadoCuentaCobro!=-1)
		{
			consulta.append(" AND cxc.estado="+estadoCuentaCobro);
		}
		else
		{
			//----------Si la cuenta de cobro final es igual a -1 la búsqueda avanzada es desde modificar ----------//
			if(cuentaCobroFinal.equals("-1"))
				consulta.append(" AND (cxc.estado="+ConstantesBD.codigoEstadoCarteraGenerado+" OR cxc.estado="+ConstantesBD.codigoEstadoCarteraRadicada+")");
		}
		
		if (UtilidadCadena.noEsVacio(fechaInicial) && UtilidadCadena.noEsVacio(fechaFinal))
		{
			consulta.append(" AND cxc.fecha_inicial>='"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND cxc.fecha_final<='"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'");
		}
				
		consulta.append(" ORDER BY cxc.numero_cuenta_cobro");
		
		logger.info("SQL -> "+consulta);
		
		return consulta.toString();	
	}	
	
	/**
	 * Método que realiza la anulación de la cuenta de cobro, modificando su estado y
	 * liberando los cargues asociados a ella (se ponen en null)
	 * @param con
	 * @param cuentaCobro
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public static int anularCuentaCobro(Connection con, String cuentaCobro, String motivoAnulacion, String loginUsuario, int codigoInstitucion)
	{ 
		
		int resp=0;
		PreparedStatementDecorator ps =null;				
		try
			{
				//-----Se actualiza el estado de la cuenta de cobro a anulado--------//
				String cad = " UPDATE capitacion.cuentas_cobro_capitacion " +
										"		SET estado="+ConstantesBD.codigoEstadoCarteraAnulado+
										"			WHERE numero_cuenta_cobro=? AND institucion=?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setDouble(1,  Utilidades.convertirADouble(cuentaCobro));				
				ps.setInt(2,  codigoInstitucion);
																	
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = Utilidades.convertirAEntero(cuentaCobro);
				}
				
				//---------Se libera cada uno de los cargues asociados a la cuenta de cobro colocando el campo cuenta cobro en NULL ---------// 
				cad="UPDATE capitacion.contrato_cargue SET cuenta_cobro=?, institucion=?,valor_total=0" +
								"	WHERE cuenta_cobro=? AND institucion=?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setNull(1, Types.NUMERIC);
				ps.setNull(2, Types.INTEGER);
				ps.setDouble(3,  Utilidades.convertirADouble(cuentaCobro));
				ps.setInt(4,  codigoInstitucion);
				
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = Utilidades.convertirAEntero(cuentaCobro);
				}
				
				//----------Se guarda el registro de la anulación de la cuenta de cobro -------------------------//
				cad="INSERT INTO capitacion.anulaciones_cxc_capitacion " +
						"						(numero_cuenta_cobro,fecha_anulacion,hora_anulacion,usuario_anula,motivo_anulacion,institucion) "+
						"		VALUES (?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ?, ?)";

				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setDouble(1,  Utilidades.convertirADouble(cuentaCobro));
				ps.setString(2, loginUsuario);
				ps.setString(3, motivoAnulacion);
				ps.setInt(4, codigoInstitucion);
				
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = Utilidades.convertirAEntero(cuentaCobro);
				}
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la anulación de la cuente de cobro: SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp=ConstantesBD.codigoNuncaValido;
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}
	
	/**
	 * Método que consulta los cargues que cumplen con los paràmetros en modificar
	 * @param con
	 * @param cuentaCobro
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 */
	public static HashMap consultarCarguesModificar(Connection con, int cuentaCobro, String fechaFinal, int codigoConvenio)
	{
		StringBuffer consultaStr = new StringBuffer();
		
		//---------Si codigo convenio es diferente de -1 fue porque no modificaron ningún parámetro -------//
		if (codigoConvenio !=ConstantesBD.codigoNuncaValido)
			{
			consultaStr.append("SELECT * FROM " +
												"	(SELECT 1 AS cargue,cc.codigo AS codigo, c.numero_contrato AS numero_contrato, to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
												"				to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
												"				cc.total_pacientes AS total_pacientes, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
												"				cc.total_pacientes*cc.upc AS valor_total,c.tipo_pago AS tipo_pago, "+
												"				CASE WHEN cc.ajustes_debito=0 AND ajustes_credito=0 THEN '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' ELSE '"+ValoresPorDefecto.getValorTrueParaConsultas()+ "' END AS tiene_ajustes "+
												"		FROM capitacion.contrato_cargue cc "+ 
												"			INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
												"				WHERE to_char(cc.fecha_final, 'YYYY-MM-DD')<='"+fechaFinal+"' AND cc.cuenta_cobro="+cuentaCobro);
			
			consultaStr.append("	UNION "+
													"	SELECT 0 AS cargue,cc.codigo AS codigo, c.numero_contrato AS numero_contrato, to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
													"					to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
													"					cc.total_pacientes AS total_pacientes, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
													"					cc.total_pacientes*cc.upc AS valor_total,c.tipo_pago AS tipo_pago,"+
													"					CASE WHEN cc.ajustes_debito=0 AND ajustes_credito=0 THEN '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' ELSE '"+ValoresPorDefecto.getValorTrueParaConsultas()+ "' END AS tiene_ajustes "+
													"		FROM capitacion.contrato_cargue cc "+ 
													"		INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
													"	WHERE to_char(cc.fecha_final, 'YYYY-MM-DD')<='"+fechaFinal+"' AND c.convenio="+codigoConvenio+"" +
													"					AND (cc.cuenta_cobro IS NULL OR cc.cuenta_cobro=0) " +
													"					AND cc.anulado=" +ValoresPorDefecto.getValorFalseParaConsultas()+
													"						AND cc.total_pacientes>0) x");
			
			consultaStr.append(" ORDER BY x.fecha_cargue,x.numero_contrato");
			}
		else
			{
			consultaStr.append("SELECT " +
					"1 AS cargue," +
					"cc.codigo AS codigo, " +
					"c.numero_contrato AS numero_contrato, " +
					"to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
					"to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, " +
					"to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
					"cc.total_pacientes AS total_pacientes, " +
					"CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
					"cc.valor_total AS valor_total," +
					"CASE WHEN cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) IS NULL THEN 0 " +
					"ELSE cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) END AS saldo," +
					"abs(abs(cc.ajustes_debito) - abs(cc.ajustes_credito)) AS ajustes," +
					"cc.valor_total + (abs(cc.ajustes_debito)-abs(cc.ajustes_credito)) AS total," +
					"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc || '' END AS valor_upc_contrato, " + //se usará para la impresion
					"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc || '' END AS porcentaje_upc_contrato, " + //se usará para la impresion
					"c.valor AS valor_contrato, " + //se usará para la impresion
					"c.tipo_pago AS tipo_pago," +
					"CASE WHEN cc.ajustes_debito=0 AND ajustes_credito=0 THEN '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' ELSE '"+ValoresPorDefecto.getValorTrueParaConsultas()+ "' END AS tiene_ajustes "+  
					"	FROM capitacion.contrato_cargue cc "+ 
					"		INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
					"			WHERE to_char(cc.fecha_final, 'YYYY-MM-DD')<='"+fechaFinal+"' AND cc.cuenta_cobro="+cuentaCobro);
			
			consultaStr.append(" ORDER BY cc.fecha_cargue,c.numero_contrato");
			}
		PreparedStatementDecorator ps =null;

		try
		{
		logger.info("consultaStr-->"+consultaStr);
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString()));
				
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error consultarCarguesModificar:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que libera los cargues asociados a una cuenta de cobro de acuerdo
	 * a la fecha final 
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param fechaFinal
	 * @return
	 */
	public static int liberarCarguesCuentaCobro(Connection con, int cuentaCobro, int institucion, String fechaFinal)
	{
		PreparedStatementDecorator ps =null;
		int resp=0;
								
		try
			{
				String cad = "UPDATE capitacion.contrato_cargue SET cuenta_cobro=? " +
											"WHERE cuenta_cobro=? AND institucion=? AND fecha_final<=?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setNull(1, Types.NUMERIC);
				ps.setDouble(2, Utilidades.convertirADouble(cuentaCobro+""));
				ps.setInt(3, institucion);
				ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
																	
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = cuentaCobro;
				}
					
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la liberación de los cargues de la cuenta de cobro liberarCarguesCuentaCobro: SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}
	
	/**
	 * Método que actualiza una cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param loginUsuario
	 * @param valorInicialCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observaciones
	 * @param codigoInstitucion
	 * @return
	 */
	public static int actualizarCuentaCobroCapitacion(Connection con, int cuentaCobro, String loginUsuario, double valorInicialCuenta, String fechaInicial, String fechaFinal, String observaciones, int codigoInstitucion)
	{
		PreparedStatementDecorator ps =null;
		int resp=0;
								
		try
			{
				String cad = "UPDATE capitacion.cuentas_cobro_capitacion SET usuario_genera=?, valor_inicial_cuenta=?," +
										"				saldo_cuenta=?,fecha_inicial=?,fecha_final=?,obs_generacion=? "+ 
										"					WHERE numero_cuenta_cobro=? AND institucion=?";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setString(1,  loginUsuario);
				ps.setDouble(2, valorInicialCuenta);
				ps.setDouble(3, valorInicialCuenta);
				ps.setString(4,  UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
				ps.setString(5,  UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
				ps.setString(6,  observaciones);
				ps.setDouble(7,  Utilidades.convertirADouble(cuentaCobro+""));
				ps.setInt(8,  codigoInstitucion);
																	
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					resp = cuentaCobro;
				}
					
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la actualización de la cuenta de cobro actualizarCuentaCobroCapitacion: SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}
	
	/**
	 * Método que consulta el listado de facturas asociadas a la 
	 * cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarFacturasCxC(Connection con, int cuentaCobro, int codigoInstitucion)
	{
		String consultaStr="SELECT fac.consecutivo_factura AS numero_factura,to_char(fac.fecha, 'dd/mm/yyyy') AS fecha_factura," +
											"				vi.nombre AS via_ingreso,valor_cartera AS valor "+ 
											"	FROM facturas fac "+ 
											"		INNER JOIN vias_ingreso vi ON (fac.via_ingreso=vi.codigo) "+
											"			WHERE fac.cuenta_cobro_capitacion=? AND fac.institucion=?" +
											"	ORDER BY fac.consecutivo_factura";

		PreparedStatementDecorator ps =null;
		try
		{
		
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		ps.setDouble(1, Utilidades.convertirADouble(cuentaCobro+""));
		ps.setInt(2, codigoInstitucion);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando el detalle de la facturas asociadas a la cuenta de cobro:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Mètodo que realiza la búsqueda avanzada de las facturas
	 * @param con
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param viaIngreso
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultaAvanzadaFacturasCxC(Connection con, String numeroFactura, String fechaFactura, int viaIngreso, int codigoInstitucion, String cuentaCobroCapitacion)
	{
		String consultaStr=armarConsultaFacturas(numeroFactura, fechaFactura, viaIngreso, codigoInstitucion, cuentaCobroCapitacion);
		PreparedStatementDecorator ps =null;
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
				
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando  consultaAvanzadaFacturasCxC:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * Método que arma la consulta de la búsqueda de facturas
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param viaIngreso
	 * @param codigoInstitucion
	 * @param cuentaCobroCapitacion
	 * @return
	 */
	private static String armarConsultaFacturas  (String numeroFactura, String fechaFactura, int viaIngreso, int codigoInstitucion, String cuentaCobroCapitacion)
	{
			StringBuffer consulta=	new StringBuffer();
			
			consulta.append("SELECT fac.consecutivo_factura AS numero_factura,to_char(fac.fecha, 'dd/mm/yyyy') AS fecha_factura," +
											"				vi.nombre AS via_ingreso,valor_cartera AS valor "+ 
											"	FROM facturas fac "+ 
											"		INNER JOIN vias_ingreso vi ON (fac.via_ingreso=vi.codigo) "+
											"			WHERE fac.institucion="+codigoInstitucion+ " AND fac.cuenta_cobro_capitacion="+cuentaCobroCapitacion);
			
		if (UtilidadCadena.noEsVacio(numeroFactura))
		{
			consulta.append(" AND fac.consecutivo_factura="+numeroFactura);
		}
		
		if (UtilidadCadena.noEsVacio(fechaFactura))
		{
			consulta.append(" AND fac.fecha='"+UtilidadFecha.conversionFormatoFechaABD(fechaFactura)+"'");
		}
		
		if (viaIngreso!=ConstantesBD.codigoNuncaValido)
		{
			consulta.append(" AND fac.via_ingreso="+viaIngreso);
		}
		
		consulta.append(" ORDER BY fac.consecutivo_factura");
		
		return consulta.toString();	
	}
	
	/**
	 * metodo que actualiza los campos de total_ingresos y diferencia_cuenta de las cuentas de cobro capitadas
	 * @param con
	 * @param numeroCuentaCobro
	 * @param totalIngresos
	 * @return
	 */
	public static boolean updateTotalIngresosDiferenciaCuenta(Connection con, String numeroCuentaCobro, String totalIngresos)
	{
		String updateStr="update cuentas_cobro_capitacion set total_ingresos=?, diferencia_cuenta_inicial= (valor_inicial_cuenta-"+totalIngresos+") where numero_cuenta_cobro="+numeroCuentaCobro;
		PreparedStatementDecorator ps =null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(updateStr));
			ps.setDouble(1, Utilidades.convertirADouble(totalIngresos));
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			logger.warn("Error en el update del total ingresos y laa diferencia de cuenta " +e.toString());
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	/**
	 * Método que consulta los contratos_cargue asociados al listado
	 * de cuentas de cobro enviado por parámetro, separados por coma
	 * se utiliza en la impresión
	 * @param con
	 * @param listadoCuentasCobro
	 * @return
	 */
	public static HashMap consultarDetalleCuentasCobroImpresion(Connection con, String listadoCuentasCobro)
	{
		String consultaStr="SELECT " +
			"c.numero_contrato AS contrato," +
			"to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
			"to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final," +
			"to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue," +
			"cc.total_pacientes AS total_pacientes," +
			"CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
			"c.tipo_pago, " + 
			"cc.valor_total AS valor_total, " +
			"cc.codigo AS contrato_cargue," +
			"cuenta_cobro AS cuenta_cobro, " +
			"abs(abs(cc.ajustes_debito) - abs(cc.ajustes_credito)) AS ajustes," +
			"cc.valor_total + (abs(cc.ajustes_debito)-abs(cc.ajustes_credito)) AS total, " +
			"CASE WHEN cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) IS NULL THEN 0 " +
			"ELSE cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) END AS saldo," +
			"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc || '' END AS valor_upc_contrato, " + //se usará para la impresion
			"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc || '' END AS porcentaje_upc_contrato, " + //se usará para la impresion
			"c.valor AS valor_contrato " + //se usará para la impresion
						   "		FROM capitacion.contrato_cargue cc " +
						   "			INNER JOIN contratos c ON (cc.contrato=c.codigo)" +
						   "				WHERE cc.cuenta_cobro IN ("+listadoCuentasCobro+")"+
						   "					ORDER BY cc.fecha_cargue,c.numero_contrato";

		PreparedStatementDecorator ps =null;
		try
		{
		
		 ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
				
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error en consultarDetalleCuentasCobroImpresion:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
	/**
	 * indica si existe o no un numero de cuenta de cobro dado x institucion
	 * @param con
	 * @param numeroCxC
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existeNumeroCxC (Connection con, String numeroCxC, int codigoInstitucion)
	{
		String consulta=" select numero_cuenta_cobro from cuentas_cobro_capitacion where numero_cuenta_cobro=? and institucion=?";
		PreparedStatementDecorator ps =null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setDouble(1, Utilidades.convertirADouble(numeroCxC));
			ps.setInt(2, codigoInstitucion);
			if(ps.executeQuery().next())
				return true;
		}
		catch (SQLException e)
		{
			logger.error("Error en existeNumeroCxC:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	/**
	 * Método que consulta el detalle de los cargues de tipo grupo etáreo para mostrarse en la impresión
	 * @param con
	 * @param listadoContratosCargue
	 * @return
	 */
	public static HashMap consultarDetalleGrupoEtareo(Connection con, String listadoContratosCargue)
	{
		String consultaStr="SELECT " +
			"gec.edad_inicial AS edad_inicial," +
			"gec.edad_final AS edad_final," +
			"gec.sexo AS sexo," +
			"gec.valor AS valor, " +
			"gec.convenio AS convenio," +
			"gec.porcentaje_pyp AS porcentaje_pyp, " +
			"cge.total_usuarios AS total_usuarios," +
			"cge.upc AS valor_upc," +
			"cge.total_a_pagar AS valor_total," +
			"cge.contrato_cargue AS contrato_cargue," +
			"cc.contrato AS contrato, " +
			"cc.cuenta_cobro AS cuenta_cobro "+
											"		FROM capitacion.grupos_etareos_x_convenio gec "+ 
											"				INNER JOIN capitacion.cargue_grupo_etareo  cge ON (cge.grupo_etareo=gec.codigo) "+ 
											"				INNER JOIN capitacion.contrato_cargue cc ON (cge.contrato_cargue=cc.codigo) "+
											"					WHERE cge.contrato_cargue IN ("+listadoContratosCargue+")"+
											"						ORDER BY gec.edad_inicial,gec.edad_final";

		
		PreparedStatementDecorator ps =null;
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error en consultarDetalleGrupoEtareo:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param cuentasCobro
	 * @return
	 */
	public static HashMap consultarCuentasCobroConvenio(Connection con, int institucion, String cuentasCobro)
	{
		String[] cuentas=cuentasCobro.split(ConstantesBD.separadorSplit);
		String filtro="'-1'";
		for(int i=0;i<cuentas.length;i++)
		{
			filtro+=",'"+cuentas[i]+"'";
		}
		
		String consultaStr="SELECT " +
								"cxc.numero_cuenta_cobro AS cuenta_cobro," +
								"cxc.valor_inicial_cuenta AS valor_inicial_cuenta," +
								"cxc.valor_pagos AS valor_pagos," +
								"cxc.saldo_cuenta AS saldo_cuenta," +
								"cxc.ajustes_debito AS ajuste_debito," +
								"cxc.ajustes_credito AS ajuste_credito," +
								"ec.descripcion AS nombre_estado," +
								"to_char(cxc.fecha_elaboracion, 'dd/mm/yyyy') AS fecha_elaboracion," +
								"to_char(cxc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial,"+
								"to_char(cxc.fecha_final, 'dd/mm/yyyy') AS fecha_final," +
								"cxc.obs_generacion AS obs_generacion," +
								"getNitConvenio(cxc.convenio) AS nit_convenio, "+
								"CASE WHEN cxc.fecha_radicacion IS NULL THEN '' ELSE to_char(cxc.fecha_radicacion,'DD/MM/YYYY') END AS fecha_radicacion, "+
								"CASE WHEN cxc.numero_radicacion IS NULL THEN '' ELSE cxc.numero_radicacion END AS numero_radicacion, "+
								"c.nombre AS nombre_convenio, " +
								"e.direccion AS direccion, " +
								"e.telefono AS telefono " +
							"FROM capitacion.cuentas_cobro_capitacion cxc " +
							"INNER JOIN convenios c ON(c.codigo=cxc.convenio) " +
							"INNER JOIN empresas e ON(e.codigo=c.empresa) " +
							"INNER JOIN cartera.estados_cartera ec ON (cxc.estado=ec.codigo) "+
							"WHERE cxc.institucion=? AND cxc.numero_cuenta_cobro in ("+filtro+")";
	
		PreparedStatementDecorator ps =null;
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		ps.setInt(1, institucion);
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando las cuentas de cobro por convenio:  en SqlBaseCuentaCobroCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoCargue
	 * @param codigoInstitucionInt
	 * @param totalUsuarios
	 * @param upc
	 * @param totalUsuariosOld
	 * @param upcOld
	 * @param usuario
	 * @return
	 */
	public static int actualizarUpcTotalUserContratoCargue(Connection con, int codigoCargue, int codigoInstitucionInt, String totalUsuarios, String upc, String totalUsuariosOld, String upcOld, String usuario) 
	{
		PreparedStatementDecorator ps = null;
		int resp=0;
		try
		{
			String cad = " UPDATE  capitacion.contrato_cargue SET total_pacientes=?, upc=?" +
										"		WHERE codigo=?	";
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
				ps.setDouble(1, Utilidades.convertirADouble(totalUsuarios));				
				ps.setDouble(2, Utilidades.convertirADouble(upc));
				ps.setDouble(3, Utilidades.convertirADouble(codigoCargue+""));
				resp = ps.executeUpdate();
				
				if (resp > 0)
				{
					String cadena="insert into log_mod_contrato_cargue(codigo," +
																	" codigo_contrato_cargue," +
																	" total_pacientes_viejo," +
																	" total_pacientes_nuevo," +
																	" upc_viejo," +
																	" upc_nuevo," +
																	" fecha_modifica," +
																	" hora_modifica," +
																	" usuario_modifica) values (?,?,?,?,?,?,?,?,?)";
					ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
					ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_mod_con_cargue"));				
					ps.setDouble(2, Utilidades.convertirADouble(codigoCargue+""));
					ps.setDouble(3, Utilidades.convertirADouble(totalUsuariosOld));
					ps.setDouble(4, Utilidades.convertirADouble(totalUsuarios));
					ps.setDouble(5, Utilidades.convertirADouble(upcOld));
					ps.setDouble(6, Utilidades.convertirADouble(upc));
					ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
					ps.setObject(8, UtilidadFecha.getHoraActual());
					ps.setObject(9, usuario);
					resp = ps.executeUpdate();
					if (resp > 0)
					{
						resp = codigoCargue;
					}
				}
			}
			catch(SQLException e)
			{
					logger.warn(" Error en la actualización del contrato_cargue  actualizarContratoCargue: SqlBaseCuentaCobroCapitacionDao "+e.toString() );
					resp = ConstantesBD.codigoNuncaValido;
					e.printStackTrace();
			}finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
				}
				
			}
			return resp;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fechaFinal
	 * @param fechaInicial
	 * @param convenioCapitado
	 * @return
	 */
	public static HashMap consultarNumeroCargue(Connection con, String fechaFinal, String fechaInicial, int convenio) 
	{
		
		StringBuffer consultaStr=new StringBuffer();
		
		//--------------Si convenio es != -1 se realiza la busqueda por convenio-------------//
		if (convenio != -1)
		{
			consultaStr.append("SELECT cc.codigo AS codigo, cc.cuenta_cobro AS cuenta_cobro, con.nombre AS nombre_convenio, c.numero_contrato AS contrato, to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
												"				to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
												"				cc.total_pacientes AS total_usuarios, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
												"				cc.total_pacientes AS total_usuarios_old, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc_old, " +
												"				cc.total_pacientes*cc.upc AS valor_total, c.tipo_pago AS tipo_pago, " +
												"				c.convenio AS codigo_convenio, 1 AS cargue "+
												"		FROM capitacion.contrato_cargue cc  "+
												"			INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
												"			INNER JOIN convenios con ON (con.codigo=c.convenio) "+
												"				WHERE cc.fecha_inicial >= ? AND cc.fecha_final <= ? " +
												"						AND c.convenio=?" +
												"						AND cc.total_pacientes>0" +
												"						AND con.tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado+
												"						AND cc.anulado="+ValoresPorDefecto.getValorFalseParaConsultas()+
												"			ORDER BY con.nombre");
		}
		else
		{
			consultaStr.append("SELECT cc.codigo AS codigo, cc.cuenta_cobro AS cuenta_cobro, con.nombre AS nombre_convenio, c.numero_contrato AS contrato, to_char(cc.fecha_inicial, 'dd/mm/yyyy') AS fecha_inicial, " +
												"				to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_final, to_char(cc.fecha_cargue, 'dd/mm/yyyy') AS fecha_cargue, " +
												"				cc.total_pacientes AS total_usuarios, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc, " +
												"				cc.total_pacientes AS total_usuarios_old, CASE WHEN c.tipo_pago = "+ConstantesBD.codigoTipoPagoGrupoEtareo+" THEN -1 ELSE cc.upc END AS upc_old, " +
												"				cc.total_pacientes*cc.upc AS valor_total, c.tipo_pago AS tipo_pago, " +
												"				c.convenio AS codigo_convenio, 1 AS cargue "+
												"		FROM capitacion.contrato_cargue cc  "+
												"			INNER JOIN contratos c ON (cc.contrato=c.codigo) "+
												"			INNER JOIN convenios con ON (con.codigo=c.convenio) "+
												"				WHERE cc.fecha_inicial >= ? AND cc.fecha_final <= ? " +
												"						AND cc.total_pacientes>0" +
												"						AND con.tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado+
												"						AND cc.anulado="+ValoresPorDefecto.getValorFalseParaConsultas()+
												"			ORDER BY con.nombre");
		}
		
		PreparedStatementDecorator ps =null;
		try
		{
		
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString()));
			logger.info("\n====>Consulta: "+consultaStr.toString());
			logger.info("\n====>Fecha Inicial: "+fechaInicial+" ====>Fecha Final: "+fechaFinal+" ====>Convenio: "+convenio);
			if (convenio != -1)
				{
					ps.setString(1, fechaInicial);
					ps.setString(2, fechaFinal);
					ps.setInt(3, convenio);
				}
			else
				{
					ps.setString(1, fechaInicial);
					ps.setString(2, fechaFinal);
				}
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
			return mapaRetorno;
	
		}
		catch (SQLException e)
		{
			logger.error("Error Consultando los cargues en SqlBaseCuentaCobroCapitacionDao"+e.toString());
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAjusteCxCDao "+sqlException.toString() );
			}
			
		}
	}
	
}
