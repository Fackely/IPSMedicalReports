package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.odontologia.SqlBaseMotivosAtencionOdontologicaDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;


public class SqlBaseReportePagosCarteraPacienteDao 
{	
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReportePagosCarteraPacienteDao.class);

	
	private static String consultarDocumentos="SELECT DISTINCT dg.tipo_documento AS tipodoc, "+
													"dg.consecutivo, "+
													"dg.anio_consecutivo AS aniocons, "+
													"carterapaciente.getnomdeudoringreso(d.ingreso) AS deudor, "+
													"dg.valor AS valordoc, " +
													"dg.fecha_generacion AS fechagen, "+
													"dg.estado, " +
													"df.codigo_pk AS datosfin, " +
													"ca.descripcion AS centroatencion " +
												"FROM carterapaciente.deudorco d " +
												"INNER JOIN carterapaciente.deudores_datos_finan ddf ON(ddf.codigo_pk_deudor=d.codigo_pk) " +
												"INNER JOIN carterapaciente.datos_financiacion df ON(ddf.datos_financiacion=df.codigo_pk) "+
												"INNER JOIN carterapaciente.documentos_garantia dg ON(df.codigo_pk_docgarantia=dg.codigo_pk) " +
												"INNER JOIN manejopaciente.ingresos i ON(i.id=dg.ingreso) " +
												"INNER JOIN administracion.centro_atencion ca ON(i.centro_atencion=ca.consecutivo)" ;
	
	private static String consultarDocumentosReporte="SELECT DISTINCT getintegridaddominio(dg.tipo_documento) AS tipodoc, "+
														"dg.consecutivo AS consecutivo, "+
														"dg.anio_consecutivo AS aniocons, "+
														"carterapaciente.getnomdeudoringreso(d.ingreso) AS deudor, "+
														"dg.valor AS valordoc, " +
														"dg.fecha_generacion AS fechagen, "+
														"dg.estado AS estado, " +
														"df.codigo_pk AS datosfin, " +
														"ca.descripcion AS centroatencion, " +
														"carterapaciente.getAplicPagos(?,?,?) AS pagos " +
													"FROM carterapaciente.deudorco d " +
													"INNER JOIN carterapaciente.deudores_datos_finan ddf ON(ddf.codigo_pk_deudor=d.codigo_pk) " +
													"INNER JOIN carterapaciente.datos_financiacion df ON(ddf.datos_financiacion=df.codigo_pk) "+
													"INNER JOIN carterapaciente.documentos_garantia dg ON(df.codigo_pk_docgarantia=dg.codigo_pk) "+
													"INNER JOIN manejopaciente.ingresos i ON(i.id=dg.ingreso) " +
													"INNER JOIN administracion.centro_atencion ca ON(i.centro_atencion=ca.consecutivo)" ;
	
	private static String consultarAplicPagos="SELECT codigo_pk As codigopk, " +
												"datos_financiacion As datosfin, " +
												"valor, " +
												"fecha_aplicacion AS fecha, " +
												"consecutivo " +
												"FROM carterapaciente.aplicac_pagos_cartera_pac ";  
	
	
	public static HashMap consultarAplicPagos(int codigoPk, String fechaIni, String fechaFin, int anioIni, int anioFin, String tipoPeriodo) 
	{		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		HashMap<String, Object> resultados= new HashMap<String, Object>();
	
		String consulta=consultarAplicPagos;
		
		consulta+="WHERE datos_financiacion= "+codigoPk+" ";
		
		if(tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
			consulta += "AND to_char(fecha_aplicacion,'dd/mm/yyyy') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' ";
		else if(tipoPeriodo.equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
			consulta += "AND to_char(fecha_aplicacion,'YYYY') BETWEEN "+anioIni+" AND "+anioFin+" ";
					
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\nconsulta::::::: "+consulta+" codigopk: "+codigoPk);	
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);		
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
		
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO APLICACION DE PAGOS.------>>>>>>"+e);
			e.printStackTrace();
		}	
		return resultados;
	}
	
	public static ArrayList<DtoDatosFinanciacion> consultarDocumentos(int centroAtencion, String tipoDoc) {
		ArrayList<DtoDatosFinanciacion> listaDtoDatosFin = new ArrayList<DtoDatosFinanciacion>();
					
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		String consulta=consultarDocumentos;
		
		consulta += "WHERE 1=1 ";
		
		if(tipoDoc.equals("-1"))
			consulta += "AND (dg.tipo_documento="+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+" OR dg.tipo_documento="+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+" ) ";
		if(tipoDoc.equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
			consulta += "AND dg.tipo_documento= '"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"' ";
		if(tipoDoc.equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
			consulta += "AND dg.tipo_documento= '"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"' ";
		if(centroAtencion > 0)
			consulta += "AND i.centro_atencion= "+centroAtencion+" ";
						
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\nconsulta::::::: "+consulta);			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			
			while(rs.next())
			{
				DtoDatosFinanciacion dto= new DtoDatosFinanciacion();
				dto.setTipoDocumento(rs.getString("tipodoc"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.getDocumentoGarantia().setFechaGen(rs.getString("fechagen"));
				dto.getDeudor().setPrimerNombre(rs.getString("deudor"));
				dto.getDocumentoGarantia().setValor(rs.getString("valordoc"));
				dto.getDocumentoGarantia().setEstado(rs.getString("estado"));
				dto.setCodigoPk(rs.getInt("datosfin"));
				dto.getDocumentoGarantia().setCentroAtencion(rs.getString("centroatencion"));
				logger.info("\n\ncentro atencion sql: "+dto.getDocumentoGarantia().getCentroAtencion());
				listaDtoDatosFin.add(dto);
			}
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO DOCUMENTOS.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoDatosFin;
	}

	public static String getConsultarAplicPagos() {
		return consultarAplicPagos;
	}

	public static void setConsultarAplicPagos(String consultarAplicPagos) {
		SqlBaseReportePagosCarteraPacienteDao.consultarAplicPagos = consultarAplicPagos;
	}

	public static String getConsultarDocumentosReporte() {
		return consultarDocumentosReporte;
	}

	public static ArrayList<DtoDatosFinanciacion> ejecutarConsulta(String newquery) {
		ArrayList<DtoDatosFinanciacion> listaDtoDatosFin = new ArrayList<DtoDatosFinanciacion>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
							
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(newquery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\nconsulta::::::: "+newquery);			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			
			while(rs.next())
			{
				DtoDatosFinanciacion dto= new DtoDatosFinanciacion();
				dto.setTipoDocumento(rs.getString("tipodoc"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.getDocumentoGarantia().setFechaGen(rs.getString("fechagen"));
				dto.getDeudor().setPrimerNombre(rs.getString("deudor"));
				dto.getDocumentoGarantia().setValor(rs.getString("valordoc"));
				dto.getDocumentoGarantia().setEstado(rs.getString("estado"));
				dto.setCodigoPk(rs.getInt("datosfin"));
				dto.getDocumentoGarantia().setCentroAtencion(rs.getString("centroatencion"));
				dto.getDocumentoGarantia().setCartera(rs.getString("pagos"));
				logger.info("\n\ncentro atencion sql: "+dto.getDocumentoGarantia().getCentroAtencion());
				listaDtoDatosFin.add(dto);
			}
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO DOCUMENTOS.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoDatosFin;
	}	
}