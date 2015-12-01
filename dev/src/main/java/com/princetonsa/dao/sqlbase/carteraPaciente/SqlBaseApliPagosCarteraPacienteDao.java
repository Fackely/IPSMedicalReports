package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.nio.charset.CodingErrorAction;
import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.actionform.carteraPaciente.ApliPagosCarteraPacienteForm;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDocGarantiaAplicarPago;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import javax.swing.text.StyledEditorKit.BoldAction;

public class SqlBaseApliPagosCarteraPacienteDao
{
	
	/**
	 * mensajes de arror
	 * */
	static Logger logger = Logger.getLogger(SqlBaseApliPagosCarteraPacienteDao.class);
	
	public static String consultaRecibosCajaStr	=	"SELECT " +
																"rc.numero_recibo_caja AS numrecibocaja, " +
																"to_char(rc.fecha,'dd/mm/yyyy') AS fechageneracion," +
																"dcrc.valor AS valordocumento, " +
																"d.numero_identificacion AS iddeudor, " +
																"d.primer_nombre AS primernombre," +
																"d.segundo_nombre AS segundonombre," +
																"d.primer_apellido AS primerapellido, " +
																"d.segundo_apellido AS segundoapellido," +
																"cit.codigo_tipo_ingreso AS codigotipoing," +
																"'"+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+"'"+ "AS tipodocpagos," +
																"(" +
																	"SELECT SUM(apcp.valor) " +
																"FROM " +
																	"carterapaciente.aplicac_pagos_cartera_pac apcp " +
																"WHERE " +
																	"apcp.numero_documento=rc.numero_recibo_caja  " +
																") AS totalaplic," +
																"rc.contabilizado AS contabilizado "+	
															"FROM " +
																"recibos_caja rc " +
															"LEFT OUTER JOIN " +
																"tesoreria.detalle_conceptos_rc dcrc ON (dcrc.numero_recibo_caja=rc.numero_recibo_caja AND dcrc.institucion=rc.institucion) " +
															"LEFT OUTER JOIN " +
																"carterapaciente.deudorco d ON (d.ingreso=dcrc.ingreso AND d.institucion=dcrc.inst_deudor AND d.clase_deudorco=dcrc.clase_deudorco AND d.numero_identificacion=dcrc.num_id_deudorco) " +
															"LEFT OUTER JOIN " +
																"tesoreria.conceptos_ing_tesoreria cit ON (cit.codigo=dcrc.concepto AND cit.institucion=dcrc.institucion) " +
															"WHERE " +
																"rc.estado!='"+ConstantesBD.codigoEstadoReciboCajaAnulado+"' AND " ;
	
	public static String consultaDocsGarantia	=	"SELECT " +
														"coalesce(dg.tipo_documento,'') AS tipodoc," +
														"coalesce(dg.consecutivo,'') AS codigogarantia," +
														"coalesce(dg.ingreso,"+ConstantesBD.codigoNuncaValido+") AS ingreso, " +
														"coalesce(dg.anio_consecutivo,'') AS anio," +
														"df.nro_coutas AS numerocuotas," +
														"dg.valor AS valorinicial," +
														"df.codigo_pk AS codigodatosfinanciacion, " +
														"(" +
															"SELECT " +
																"SUM(apcp.valor)" +
															"FROM " +
																"carterapaciente.aplicac_pagos_cartera_pac apcp " +
															"INNER JOIN " +
																"carterapaciente.datos_financiacion df ON(df.codigo_pk =apcp.datos_financiacion) " +
															"INNER JOIN " +
																"carterapaciente.deudores_datos_finan ddf ON(ddf.datos_financiacion=df.codigo_pk) " +
															"WHERE " +
																"dc.numero_identificacion=? " +
															"AND " +
																"apcp.numero_documento=?" +
														") AS total " +
													"FROM " +
														"carterapaciente.deudores_datos_finan ddf " +
													"INNER JOIN " +
														"carterapaciente.deudorco dc ON(ddf.codigo_pk_deudor=dc.codigo_pk)" +
													"INNER JOIN " +
														"carterapaciente.datos_financiacion df ON(df.codigo_pk=ddf.datos_financiacion) " +
													"INNER JOIN " +
														"carterapaciente.documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) " +
													"WHERE " +
														"dg.estado='"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' " +
													"AND "+ 
														"dg.cartera='"+ConstantesBD.acronimoSi+"' " +
													"AND " +
														"dg.tipo_documento IN ('"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"','"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"') " +
													"AND " +
														"dc.numero_identificacion=?";
	
	
	public static String ingresarEncabezadoAplicPagos	=	"INSERT INTO " +
																"carterapaciente.aplicac_pagos_cartera_pac " +
																"(" +
																	"codigo_pk," +
																	"consecutivo," +
																	"datos_financiacion," +
																	"valor," +
																	"fecha_aplicacion," +
																	"fecha," +
																	"hora," +
																	"usuario," +
																	"numero_documento," +
																	"tipo_documento," +
																	"institucion" +
																") " +
																"VALUES " +
																"(" +
																	"?,?,?,?,?,?,?,?,?,?,?" +
																")";
	
	public static String consultaCuotasDatosFin		=	"SELECT " +
															"cdf.codigo_pk AS cuotadatfin," +
															"cdf.dato_financiacion AS datofin," +
															"cdf.valor_couta AS valorcuota, " +
															"(cdf.valor_couta - " +
															"(SELECT coalesce(SUM (dapcp.valor),0) " +
															"FROM carterapaciente.det_apli_pagos_cartera_pac dapcp " +
															"WHERE dapcp.cuota_dato_financiacion=cdf.codigo_pk)) AS valordebe " +
														"FROM " +
															"carterapaciente.cuotas_datos_financiacion cdf " +
														"INNER JOIN " +
															"carterapaciente.datos_financiacion df ON(df.codigo_pk=cdf.dato_financiacion) " +
														"WHERE " +
															"cdf.activo='"+ConstantesBD.acronimoSi+"' " +
														"AND" +
															"(cdf.valor_couta - " +
																"(SELECT coalesce(SUM (dapcp.valor),0) " +
																"FROM carterapaciente.det_apli_pagos_cartera_pac dapcp " +
																"WHERE dapcp.cuota_dato_financiacion=cdf.codigo_pk))>0 " +
														"AND " +
															"df.codigo_pk =? " +
														"ORDER BY cdf.codigo_pk ASC ";
	
	public static String ingresarDetApliPagosCarteraPac	=	"INSERT INTO " +
																"carterapaciente.det_apli_pagos_cartera_pac " +
															"(" +
																"codigo_pk," +
																"apli_pago_cartera_pac," +
																"cuota_dato_financiacion," +
																"valor" +
															")" +
															"VALUES " +
															"(" +
																"?,?,?,?" +
															")";
	
	public static String consultarAplicacion	=	"SELECT " +
														"apcp.consecutivo AS nroapli," +
														"to_char(apcp.fecha_aplicacion,'dd/mm/yyyy') AS fechaapli," +
														"apcp.valor," +
														"apcp.usuario AS usuapli," +
														"df.consecutivo AS codgarantia, " +
														"df.tipo_documento AS tipodoc " +
													"FROM " +
														"carterapaciente.aplicac_pagos_cartera_pac apcp " +
													"INNER JOIN " +
														"carterapaciente.datos_financiacion df ON(df.codigo_pk =apcp.datos_financiacion) " +
													"INNER JOIN " +
														"carterapaciente.documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) "+
													"WHERE " +
														"apcp.codigo_pk=? ";
	
	public static String strCancelarDocGarantia	=	"UPDATE " +
														"carterapaciente.documentos_garantia " +
													"SET " +
														"estado='"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"' " +
													"WHERE " +
														"ingreso=? AND tipo_documento=? AND consecutivo=? ";
														//"AND anio_consecutivo=? ";
	
	
	public static String existeApliPagosReciboCajaStr = "SELECT " +
															"codigo_pk " +
														"FROM " +
															"aplicac_pagos_cartera_pac " +
														"WHERE " +
															"to_number(numero_documento, '999999999.')=?";
	
	public static ArrayList<DtoRecibosCaja> consultarRecibosCaja(Connection con)
	{
		String consultaRC	=	consultaRecibosCajaStr;
		
		consultaRC	+=	"cit.codigo_tipo_ingreso="+ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+" AND ";
		
		consultaRC	+=	" 1=1";
		
		ArrayList<DtoRecibosCaja> listaRecibosCaja= new ArrayList<DtoRecibosCaja>();
		logger.info("LA CONSULTA ES----->"+consultaRC);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaRC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoRecibosCaja dto=new DtoRecibosCaja();
				dto.setTipoDocumento(rs.getString("tipodocpagos"));
				dto.setConsecutivo(rs.getString("numrecibocaja"));
				dto.setFecha(rs.getString("fechageneracion"));
				dto.setCodDeudor(rs.getString("iddeudor"));
				dto.setValorDocumento(Utilidades.convertirADouble(rs.getString("valordocumento")));
				dto.setNombreDeudor(rs.getString("primernombre")+" "+rs.getString("segundonombre")+" "+rs.getString("primerapellido")+" "+rs.getString("segundoapellido"));
				dto.setTotalAplicacion(Utilidades.convertirADouble(rs.getString("totalaplic")));
				if (dto.getTotalAplicacion()!=ConstantesBD.codigoNuncaValido)
					dto.setSaldo(Utilidades.convertirADouble(rs.getString("valordocumento"))-Utilidades.convertirADouble(rs.getString("totalaplic")));
				else
					dto.setSaldo(Utilidades.convertirADouble(rs.getString("valordocumento")));
				dto.setContabilizado(rs.getString("contabilizado"));
				listaRecibosCaja.add(dto);
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return listaRecibosCaja;
	}
	
	public static ArrayList<DtoRecibosCaja> consultarRecibosCajaFiltros(Connection con, HashMap filtros)
	{
		String consultaRC	=	consultaRecibosCajaStr;
	
		consultaRC	+=	"cit.codigo_tipo_ingreso="+ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+" AND " ;
		
		if (!filtros.get("fechaInicialGen").equals("")&&!filtros.get("fechaFinalGen").equals(""))
			consultaRC	+=	"(rc.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicialGen")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinalGen")+"")+"') AND ";
		
		if (!filtros.get("docInicial").equals("")&&!filtros.get("docFinal").equals(""))
			consultaRC	+=	"(rc.numero_recibo_caja BETWEEN '"+filtros.get("docInicial")+"' AND '"+filtros.get("docFinal")+"') AND ";
			
		consultaRC	+=	" 1=1";
		
		ArrayList<DtoRecibosCaja> listaRecibosCaja= new ArrayList<DtoRecibosCaja>();
		logger.info("LA CONSULTA ES----->"+consultaRC);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaRC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoRecibosCaja dto=new DtoRecibosCaja();
				dto.setTipoDocumento(rs.getString("tipodocpagos"));
				dto.setConsecutivo(rs.getString("numrecibocaja"));
				dto.setFecha(rs.getString("fechageneracion"));
				dto.setCodDeudor(rs.getString("iddeudor"));
				dto.setValorDocumento(Utilidades.convertirADouble(rs.getString("valordocumento")));
				dto.setNombreDeudor(rs.getString("primernombre")+" "+rs.getString("segundonombre")+" "+rs.getString("primerapellido")+" "+rs.getString("segundoapellido"));
				dto.setTotalAplicacion(Utilidades.convertirADouble(rs.getString("totalaplic")));
				if (dto.getTotalAplicacion()!=ConstantesBD.codigoNuncaValido)
					dto.setSaldo(Utilidades.convertirADouble(rs.getString("valordocumento"))-Utilidades.convertirADouble(rs.getString("totalaplic")));
				else
					dto.setSaldo(Utilidades.convertirADouble(rs.getString("valordocumento")));
				dto.setContabilizado(rs.getString("contabilizado"));
				listaRecibosCaja.add(dto);
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return listaRecibosCaja;
	}
	
	public static ArrayList<DtoAplicacPagosCarteraPac> consultarDocsFarantia(Connection con, String nroRC,String deudor)
	{
		ArrayList<DtoAplicacPagosCarteraPac> listaDocsGarantia= new ArrayList<DtoAplicacPagosCarteraPac>();
		logger.info("CONSULTA DE LOS DOCS DE GARANTIA------->"+consultaDocsGarantia);
		logger.info("EL DEUDOR QUE APLICA PARA LA BSUQEUDA------->"+deudor);
		
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDocsGarantia, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, deudor);
			ps.setString(2, nroRC);
			ps.setString(3, deudor);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoAplicacPagosCarteraPac dto= new DtoAplicacPagosCarteraPac();
				dto.setTipoDocumento(rs.getString("tipodoc"));
				dto.setNumeroDocumento(rs.getString("codigogarantia"));
				dto.setCuotas(Utilidades.convertirAEntero(rs.getString("numerocuotas")));
				dto.setValor(Utilidades.convertirADouble(rs.getString("valorinicial")));
				dto.setTotalAplicaciones(Utilidades.convertirADouble(rs.getString("total")));
				dto.setAnioDocumento(rs.getString("anio"));
				dto.setIngresoDocumento(rs.getString("ingreso"));
				
				if (dto.getTotalAplicaciones()!=ConstantesBD.codigoNuncaValido)
					dto.setSaldoActual(dto.getValor()-dto.getTotalAplicaciones());
				else
					dto.setSaldoActual(dto.getValor());	
				
				dto.setCodigoDatosFinanciacion(Utilidades.convertirADouble(rs.getString("codigodatosfinanciacion")));

				listaDocsGarantia.add(dto);
			}
			ps.close();
		}
		
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return listaDocsGarantia;
	}
	
	public static ArrayList<DtoCuotasDatosFinanciacion> consultaCuotasDatosFin(Connection con, String nroRC)
	{
		logger.info("LA CONSULTA ES ----->"+consultaCuotasDatosFin);
		logger.info("EL DATO FINAN--------> "+nroRC);
		
		ArrayList<DtoCuotasDatosFinanciacion> listadoCuotasDatosFin= new ArrayList<DtoCuotasDatosFinanciacion>();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaCuotasDatosFin, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, nroRC);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoCuotasDatosFinanciacion dto = new DtoCuotasDatosFinanciacion();
				
				dto.setCodigoPK(Utilidades.convertirAEntero(rs.getString("cuotadatfin")));
				dto.setDatosFinanciacion(Utilidades.convertirAEntero(rs.getString("datofin")));
				dto.setValorCuota(rs.getBigDecimal("valorcuota"));
				dto.setValorDebe(rs.getBigDecimal("valordebe"));
				listadoCuotasDatosFin.add(dto);
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return listadoCuotasDatosFin;
	}
	
	
	public static double ingresarEncabezadoAplicPagos(Connection con,DtoAplicacPagosCarteraPac dto)
	{
		double codigoPK=0;
		double consecutivo=0;
		try
		{
			codigoPK =UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_apli_pagos_cartera_pac");
			consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_apli_pag_car_pac_con");
			logger.info("Si llega hasta aqui");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarEncabezadoAplicPagos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, codigoPK);
			ps.setDouble(2, consecutivo);
			ps.setDouble(3, dto.getDatosFinanciacion());
			ps.setDouble(4, dto.getValor());
			ps.setString(5, dto.getFechaAplicacion());
			ps.setString(6, dto.getFecha());
			ps.setString(7, dto.getHora());
			ps.setString(8, dto.getUsuario());
			ps.setDouble(9, Utilidades.convertirADouble(dto.getNumeroDocumento()));
			ps.setInt(10, Utilidades.convertirAEntero(dto.getTipoDocumento()));
			ps.setInt(11, dto.getInstitucion());
			
			if(ps.executeUpdate()>0)
			{		
				ps.close();
				return codigoPK;
			}
			else
			{
				ps.close();
				return 0;
			}
			
		}
		
		catch (Exception e) 
		{
			logger.info("Consulta: "+ingresarEncabezadoAplicPagos);
			e.printStackTrace();
		}
		return codigoPK;
	}
	
	public static boolean ingresarDetApliPagosCarteraPac(Connection con, DtoDetApliPagosCarteraPac dto)
	{
		boolean transaccionExitosa=false;
		try
		{
			double codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_det_ap_pag_car_pac_con");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarDetApliPagosCarteraPac,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setDouble(1, codigoPK);
			ps.setDouble(2, dto.getApliPagoCarteraPac());
			ps.setDouble(3, dto.getCuotaDatoFinanciacion());
			ps.setBigDecimal(4, dto.getValor());
			
			if(ps.executeUpdate()>0)
			{	
				transaccionExitosa=true;
				ps.close();
				return transaccionExitosa;
			}
			else
			{
				transaccionExitosa=false;
				ps.close();
				return transaccionExitosa;
			}
		}
		catch (Exception e) 
		{
			logger.info("Consulta: "+ingresarDetApliPagosCarteraPac);
			e.printStackTrace();
		}
		
		return transaccionExitosa;
	}
	
	private static String consultarUltimoCodigoInsertado="select max(codigo_pk)||'' as ultimo from aplicac_pagos_cartera_pac";
	
	public static DtoAplicacPagosCarteraPac consultarAplicacion(Connection con)
	{
		DtoAplicacPagosCarteraPac dto= new DtoAplicacPagosCarteraPac();
		try
		{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(consultarUltimoCodigoInsertado));
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			int codigoPK=0;
			if(rsd.next())
			{
				codigoPK=Integer.parseInt(rsd.getString("ultimo"));
			}
			else
			{
				logger.info("No se ha insertado ning�n ap....");
				return null;
			}
			double codAplicacion = codigoPK;//UtilidadBD.obtenerUltimoValorSecuencia(con, "carterapaciente.seq_apli_pagos_cartera_pac");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarAplicacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codAplicacion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setConsecutivo(rs.getDouble("nroapli"));
				dto.setFechaAplicacion(rs.getString("fechaapli"));
				dto.setValor(rs.getDouble("valor"));
				dto.setUsuario(rs.getString("usuapli"));
				dto.setCodigoGarantia(rs.getString("codgarantia"));
				dto.setTipoDocumentoGarantia(rs.getString("tipodoc"));
			}
			
			rs.close();
			ps.close();
		}
		catch (Exception e) 
		{
			logger.info("Consulta: "+consultarAplicacion);
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public static boolean cancelarDocGarantia(Connection con, HashMap datosDoc)
	{
		boolean transaccionExitosa=false;
		
		try
		{
			logger.info("ENTRE A INACTIVAR!!!!!");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCancelarDocGarantia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info(datosDoc.get("ingreso")+"");
			logger.info(datosDoc.get("tipodocumento")+"");
			logger.info(datosDoc.get("consecutivo")+"");
			logger.info(datosDoc.get("anio")+"");
			
			ps.setInt(1, Utilidades.convertirAEntero(datosDoc.get("ingreso")+""));
			ps.setString(2, datosDoc.get("tipodocumento")+"");
			ps.setString(3, datosDoc.get("consecutivo")+"");
//			if (!datosDoc.get("anio").toString().equals(""))
//				ps.setString(4, datosDoc.get("anio")+"");
//			else
//				ps.setNull(4, Types.NULL);
			
			if(ps.executeUpdate()>0)
			{	
				transaccionExitosa=true;
				ps.close();
				return transaccionExitosa;
			}
			else
			{
				transaccionExitosa=false;
				ps.close();
				return transaccionExitosa;
			}
		}
		catch (Exception e) 
		{
			logger.info("Consulta: "+strCancelarDocGarantia);
			e.printStackTrace();
		}
		
		return transaccionExitosa;
	}

	public static boolean existeApliPagosReciboCaja(Connection con,String numeroReciboCaja) {
		boolean existe=false;
		try
		{
			logger.info("Consulta: "+existeApliPagosReciboCajaStr);
			logger.info("Paramaetro 1: "+numeroReciboCaja);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(existeApliPagosReciboCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(numeroReciboCaja));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				existe=true;
			
			rs.close();
			ps.close();
		}
		catch (Exception e) 
		{
			logger.error("ERROR", e);
		}
		return existe;
	}
}