/**
 * 
 */
package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.ConstantesIncosistenciasInterfaz;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoCierreCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDetCierreSaldoInicialCartera;

/**
 * @author armando
 *
 */
public class SqlBaseCierreSaldoInicialCarteraPacienteDao 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCierreSaldoInicialCarteraPacienteDao.class);
	
	/**
	 * 
	 */
	private static String consultaPosblesDocumetoscierre="SELECT  " +
																" codigo_pk as codigodoc," +
																" tipo_documento as tipodoc," +
																" valor as valor," +
																" fecha_documento as fechadocumento " +
														 " from documentos_garantia " +
														 " where " +
														 			" estado='"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' " +
														 			" and tipo_documento in ('"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"','"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"')" +
														 			" and ((convertiranumero(to_char(fecha_documento,'yyyy'))=? " +
														 			" and convertiranumero(to_char(fecha_documento,'mm'))<=?) or convertiranumero(to_char(fecha_documento,'yyyy'))<?) and codigo_pk not in (select documento from carterapaciente.detalle_cierre_carpar)"; 

	/**
	 * 
	 */
	private static String consultaValorPagosDocumento="select " +
																" coalesce(sum(valor),0) " +
													" from aplicac_pagos_cartera_pac apcp " +
													" inner join datos_financiacion df on (df.codigo_pk=apcp.datos_financiacion) " +
													" where df.codigo_pk_docgarantia=?"; 
	
	/**
	 * 
	 */
	private static String cadenaInsertarMaestroCierre=" insert into carterapaciente.cierre_saldos_carpac (" +
																	"codigo," +
																	"institucion," +
																	"anio_cierre," +
																	"mes_cierre," +
																	"observaciones," +
																	"saldo_inicial," +
																	"fecha_generacion," +
																	"hora_generacion," +
																	"usuario)" +
																"values (" +
																"?,?,?,?,?,?,?,?,?" +
																")";
	
	
	private static String cadenaInsercionDetCierre="insert into carterapaciente.detalle_cierre_carpar(codigo_cierre,documento,valor_documento,valor_pagos) values (?,?,?,?)";
	
	/**
	 * 
	 */
	private static String consultaMaestroCierre="SELECT codigo,institucion,anio_cierre,mes_cierre,observaciones,saldo_inicial,fecha_generacion,hora_generacion,usuario from cierre_saldos_carpac where institucion=? and saldo_inicial='"+ConstantesBD.acronimoSi+"'";
	
	/**
	 * 
	 * 
	 * @param anioCierre
	 * @param mesCierre
	 * @return
	 * @throws SQLException 
	 */
	public static ArrayList<DtoDetCierreSaldoInicialCartera> consultarPosibleListadoDocumentosCierre(String anioCierre, String mesCierre)  
	{
		ArrayList<DtoDetCierreSaldoInicialCartera> resultado=new ArrayList<DtoDetCierreSaldoInicialCartera>();
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consultaPosblesDocumetoscierre);
			ps.setInt(1, Utilidades.convertirAEntero(anioCierre));
			ps.setInt(2, Utilidades.convertirAEntero(mesCierre));
			ps.setInt(3, Utilidades.convertirAEntero(anioCierre));
			logger.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDetCierreSaldoInicialCartera dtoDetCierre=new DtoDetCierreSaldoInicialCartera();
				dtoDetCierre.setCodigoDocumento(rs.getInt("codigodoc"));
				dtoDetCierre.setTipoDocumento(rs.getString("tipodoc"));
				dtoDetCierre.setValorDocumentos(new BigDecimal(rs.getDouble("valor")));
				dtoDetCierre.setValorPagos(new BigDecimal(valorPagosDocumentos(con,rs.getInt("codigodoc"))));
				resultado.add(dtoDetCierre);
			}
			rs.close();
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("ERROR ",e);
		}
		return resultado;
	}

	
	/**
	 * 
	 * @param con 
	 * @param codigoDocumento
	 * @return
	 */
	private static double valorPagosDocumentos(Connection con, int codigoDocumento)
	{
		double resultado=0;
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consultaValorPagosDocumento);
			ps.setInt(1, codigoDocumento);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			logger.info(ps);
			if(rs.next())
			{
				resultado=rs.getDouble(1);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.error("ERROR ",e);
		}
		return resultado;
	}


	/**
	 * 
	 * @param cierreCarteraPaciente
	 * @return
	 */
	public static int insertarCierreSaldoInicial(DtoCierreCarteraPaciente cierreCarteraPaciente) 
	{
		int codigopk=ConstantesBD.codigoNuncaValido;
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,cadenaInsertarMaestroCierre);
			codigopk=UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_cierre_saldos_carpac");
			ps.setInt(1, codigopk);
			ps.setInt(2,cierreCarteraPaciente.getInstitucion());
			ps.setInt(3, Utilidades.convertirAEntero(cierreCarteraPaciente.getAnioCierre()));
			ps.setInt(4,  Utilidades.convertirAEntero(cierreCarteraPaciente.getMesCierre()));
			ps.setString(5, cierreCarteraPaciente.getObservaciones());
			ps.setString(6, cierreCarteraPaciente.getSaldoIncial());
			ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(cierreCarteraPaciente.getFechaGeneracion()));
			ps.setString(8, cierreCarteraPaciente.getHoraGeneracion());
			ps.setString(9, cierreCarteraPaciente.getUsuarioGenracion());
			logger.info(ps);
			if(ps.executeUpdate()>0)
			{
				for(DtoDetCierreSaldoInicialCartera dtoDetalle : cierreCarteraPaciente.getDetalleCierreSaldoIncial())
				{
					PreparedStatementDecorator psDetalle = new PreparedStatementDecorator(con,cadenaInsercionDetCierre);
					psDetalle.setInt(1, codigopk);
					psDetalle.setInt(2, dtoDetalle.getCodigoDocumento());
					psDetalle.setDouble(3, dtoDetalle.getValorDocumentos().doubleValue());
					psDetalle.setDouble(4,dtoDetalle.getValorPagos().doubleValue());
					psDetalle.executeUpdate();
					psDetalle.close();
					logger.info(psDetalle);
				}
			}
			else
			{
				codigopk=ConstantesBD.codigoNuncaValido;
			}
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("ERROR ",e);
			codigopk=ConstantesBD.codigoNuncaValido;
		}
		return codigopk;
	}


	public static DtoCierreCarteraPaciente consultarCierreInicial(int institucion) 
	{
		DtoCierreCarteraPaciente dto=new DtoCierreCarteraPaciente();
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consultaMaestroCierre);
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigo(new BigDecimal(rs.getInt("codigo")));
				dto.setAnioCierre(rs.getInt("anio_cierre")+"");
				dto.setMesCierre(rs.getInt("mes_cierre")<0?"0"+rs.getInt("mes_cierre"):rs.getInt("mes_cierre")+"");
				dto.setObservaciones(rs.getString("observaciones"));
				dto.setSaldoIncial(ConstantesBD.acronimoSi);
				dto.setFechaGeneracion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_generacion")));
				dto.setHoraGeneracion(rs.getString("hora_generacion"));
				dto.setUsuarioGenracion(rs.getString("usuario"));
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("ERROR ",e);
		}
		return dto;
	}

}
