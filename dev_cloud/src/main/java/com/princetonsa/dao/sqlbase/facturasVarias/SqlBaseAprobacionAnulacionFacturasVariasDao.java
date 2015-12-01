package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import org.apache.log4j.Logger;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * 
 * @author axioma
 *
 */
public class SqlBaseAprobacionAnulacionFacturasVariasDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseAprobacionAnulacionFacturasVariasDao.class);
	
	
	/**
	 * Cadena para la consulta de las Factuas Varias que se encuentran en estado generadas.
	 */
	public static String cadenaConsultaFacturasVarias="SELECT " +
			"fv.consecutivo as consecutivo, " +
			"to_char(fv.fecha, 'YYYY-MM-DD') as fechaelaboracion, " +
			"fv.deudor as codigodeudor, " +			
			"coalesce(t.descripcion,'') as descripciondeudor," +
			"coalesce(t.numero_identificacion,'') as iddeudor," +			
			"coalesce(e.razon_social,'') as descripcionrazonsocial," +			
			"coalesce(getnombrepersona(d.codigo_paciente),'') as descripcionpersona," +
			"coalesce(getidpaciente(d.codigo_paciente),'') as idpersona," +
			"coalesce(facturasvarias.getnombredeudorpaciente(d.codigo),'' ) as nombrePersona ," +
			"coalesce(codigo_tercero,"+ConstantesBD.codigoNuncaValido+") AS codigo_tercero," +
			"coalesce(codigo_paciente,"+ConstantesBD.codigoNuncaValido+") AS codigo_paciente," +
			"coalesce(codigo_empresa,"+ConstantesBD.codigoNuncaValido+") AS codigo_empresa," +						
			"fv.concepto as codigoconcepto, " +
			"CASE WHEN (" +
				"select count(0) " +
					"FROM conceptos_facturas_varias cfvs " +
				"where cfvs.tipo_concepto='"+ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica+
				"' and  cfvs.consecutivo=cfv.consecutivo)>0 then 'S' else 'N' end as esConceptoTipoVenta , " +
			"cfv.descripcion as descripcionconcepto, " +
			"fv.valor_factura as valor, " +
			"fv.estado_factura as estadold, " +
			"fv.estado_factura as estado, " +
			"fv.codigo_fac_var as codigofactura, " +
			"fv.centro_atencion as centroatencion, " +
			"fv.centro_costo as centrocosto, " +
			"d.tipo as tipodeudor, " +
			"fv.observaciones as observaciones," +
			"cfv.tipo_concepto," +
			"CASE WHEN fv.fecha_anulacion IS NULL THEN to_char(fv.fecha_aprobacion,'YYYY-MM-DD') ELSE to_char(fv.fecha_anulacion,'YYYY-MM-DD') END as fechaaprobanulacion, "+
			"to_char(fv.fecha_anulacion,'dd/mm/yyyy') AS fechaaprobanulacion," +
			"coalesce(motivo_anulacion,'') AS  motivoanulacion , " +
			
			//Tarea 135153
			"CASE WHEN ( " +
				"SELECT SUM(coalesce(apffv.valor_pago, 0)) AS valorpago " +
					"FROM pagos_general_empresa pge " +
					"INNER JOIN aplicacion_pagos_fvarias apfv ON (pge.codigo = apfv.pagos_general_fvarias) " +
					"INNER JOIN aplicac_pagos_factura_fvarias apffv ON (apfv.codigo = apffv.aplicacion_pagos) " +
					/*
					 * Según tarea 144687
					 */
					"LEFT OUTER JOIN facturas_varias fv ON (fv.consecutivo = apffv.factura) "+
				"WHERE apffv.factura =fv.codigo_fac_var AND apfv.estado=2)>0  then 'S' else 'N' end  as facturaPagada  "+
			"FROM facturas_varias fv " +
			"inner join conceptos_facturas_varias cfv on(cfv.consecutivo=fv.concepto) " +
			"inner join deudores d on(d.codigo=fv.deudor) " +
			"left  outer join terceros t on(t.codigo = d.codigo_tercero) " +
			"left  outer join empresas e on(e.codigo = d.codigo_empresa) " +			
			"WHERE 1=1 ";
	
	/**
	 * CAdena para cpnsultar el numero de ajustes pendientes que tiene una factura
	 */
	public static String consultarAjustesPendientesFacturaStr = "SELECt count(1) as cuenta from ajus_facturas_varias where factura = ? and estado = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"'";
	
	/**
	 * Cadena para consultar la suma del valor de los ajsutes aprobados 
	 */
	public static String consultarSumaAjustesAprobadosFacturaStr ="select coalesce(sum(valor_ajuste),0) as valor from ajus_facturas_varias where factura = ? and estado = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' and tipo_ajuste = ?";
	
	/**
	 * Cadena para la consulta de la Factuas Varia
	 */
	public static String cadenaConsultaFacturaVaria="SELECT " +
			"fv.consecutivo as consecutivo, " +
			"fv.estado_factura as estado, " +
			"fv.codigo_fac_var as codigofactura, " +			
			"cfv.tipo_concepto "+
			"FROM facturas_varias fv " +
			"inner join conceptos_facturas_varias cfv on(cfv.consecutivo=fv.concepto) " +									
			"WHERE fv.codigo_fac_var = ? ";

	/**
	 * cadena para la modificacion
	 */
	private static final String cadenaModificacionAnulStr="UPDATE facturas_varias SET usuario_anulacion=?, motivo_anulacion=?, fecha_anulacion=?, estado_factura=?, anula_fac_apro = ?,fecha_gen_anulacion=?, hora_gen_anulacion=?  WHERE codigo_fac_var=? ";
	
	private static final String cadenaModificacionAproStr="UPDATE facturas_varias SET usuario_aprobacion=?, motivo_aprobacion=?, fecha_aprobacion=?, estado_factura=?, anula_fac_apro = ?,fecha_gen_aprobacion=?, hora_gen_aprobacion=?  WHERE codigo_fac_var=? ";
	
	
	/**
	 * 
	 * */
	private static final String cadenaActEstadoMultaStr = "" +
			"UPDATE multas_citas " +
			"SET estado = ? " +
			"WHERE consecutivo IN (SELECT m.multa_cita FROM multas_facturas_varias m WHERE m.factura_varia = ?) ";

	
	
	
	/**
	 * 
	 * */
	private static final String cadenaAsocioFacVarPagos = 
		"SELECT " +
		"COUNT(a.aplicacion_pagos) AS contador " +
		"FROM aplicac_pagos_factura_fvarias a " +
		"INNER JOIN aplicacion_pagos_fvarias ap ON (ap.codigo = a.aplicacion_pagos AND ap.estado IN ("+ConstantesBD.separadorSplit+")) " +
		"INNER JOIN pagos_general_empresa pg ON (pg.codigo = ap.pagos_general_fvarias AND pg.estado = ?) " +
		"WHERE a.factura = ? ";
	
	
	/**
	 * indica si una multa de la factura varia esta asociada a otra factura 
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public static boolean esMultaAsociadaOtraFactura(Connection con,HashMap mapa)
	{
		String cadenaContadorMultas = 
			" SELECT COUNT(mf.multa_cita) AS contador "+
			" FROM multas_facturas_varias mf "+
			" INNER JOIN multas_citas mc "+
			" ON (mc.consecutivo = mf.multa_cita "+
			" AND mc.estado = '"+mapa.get("estado")+"' ) "+
			" inner join facturas_varias fv ON "+
			" (fv.codigo_fac_var= mf.factura_varia) "+
			" WHERE mf.multa_cita  IN "+
			" ( "+
				" SELECT " +
					" s1.multa_cita "+
				" FROM " +
					" multas_facturas_varias s1 "+
				" INNER JOIN "+
					" facturas_varias fvi "+
				" ON(fvi.codigo_fac_var=s1.factura_varia) "+
				" WHERE " +
					" s1.factura_varia = "+mapa.get("codFacturaVar")+" "+
				" AND " +
					"fvi.estado_factura='"+mapa.get("estado")+"' "+
			" ) "+
			" AND fv.estado_factura='GEN' "+
			" AND mf.factura_varia != "+mapa.get("codFacturaVar");
		
		try
		{
			int codFact=Utilidades.convertirAEntero(mapa.get("codFacturaVar")+"");
			String est=mapa.get("estado")+"";
			logger.info("esMultaAsociadaOtraFactura valor del sql >> "+cadenaContadorMultas+" >> est: "+est+"--codFact: "+codFact);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaContadorMultas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("contador")>0)
				{
					ps.close();
					rs.close();
					return true;
				}
			}
			ps.close();
			rs.close();
		}
		
		catch (SQLException e) 
		{
			logger.info("ERROR  ",e);			
		}
		
		return false;
	}
	
	/**
	 * indica si la factura varia tiene asociado aplicaciones de pago y consecutivos de pagos
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public static boolean esAsociadaPagos(Connection con,HashMap mapa)
	{
		String cadena = cadenaAsocioFacVarPagos;
		cadena = cadena.replace(ConstantesBD.separadorSplit, mapa.get("estadoAplicaciones").toString());
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(mapa.get("estadoPagos").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(mapa.get("codFacturaVar").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())			
			{
				if(rs.getInt("contador")>0)
				{
					ps.close();
					rs.close();
					return true;
				}
			}	
			ps.close();
			rs.close();
			logger.info("esAsociadaPagos valor del sql >> "+cadena+" >> "+mapa);
		}
		
		catch (SQLException e) 
		{
			logger.info("ERROR esAsociadaPagos "+e+" "+mapa+" "+cadena);			
		}
		
		return false;
	}
	
	/**
	 * actualiza las multas dependientes de una factura varia
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public static boolean actualizarEstadoMultasXFacturaVaria(Connection con,HashMap mapa)
	{	 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActEstadoMultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,mapa.get("estado").toString());
			ps.setInt(2,Utilidades.convertirAEntero(mapa.get("codFacturaVar").toString()));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			else
			{
				ps.close();
				return false;
			}
		}
		
		catch (SQLException e) 
		{
			logger.info("ERROR actualizarEstadoMultasXFacturaVaria "+e+" "+mapa);			
		}
		
		return true;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarFacturasVarias(Connection con) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena = cadenaConsultaFacturasVarias+" AND (fv.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"' OR fv.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"') "; 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			logger.info("CADENA CONSULTA FACTURAS>>>>>>>>>"+cadena);
		}
		
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE LAS FACTURAS VARIAS "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap consultarFacturaVaria(Connection con,HashMap parametros) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
				 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaFacturaVaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			ps.close();
		}
		
		catch (SQLException e) 
		{
			logger.info("ERROR consultarFacturaVaria "+e+" "+parametros);			
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param factura
	 * @param centroAtencion
	 * @param tipoDeudor
	 * @param deudor
	 * @return
	 */
	public static HashMap busquedaFacturas(
			Connection con,
			String fechaInicial,
			String fechaFinal,
			String factura,
			String estado,
			String tipoDeudor, 
			String deudor,
			String centroAtencion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaFacturasVarias;
		
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND  fv.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		}		
		if(!factura.equals(""))
		{
			cadena += " AND fv.consecutivo="+factura+" ";
			
		}		
		if(!estado.equals(""))
		{
			if(estado.equals(ConstantesIntegridadDominio.acronimoAmbos))
				cadena += " AND (fv.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"' OR fv.estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"') ";
			else
				cadena += " AND fv.estado_factura='"+estado+"' ";
		}		
		if(!tipoDeudor.equals(""))
		{
			cadena+=" AND d.tipo= '"+tipoDeudor+"'";
		}
		if(!deudor.equals(""))
		{
			cadena+=" AND d.codigo ="+deudor; //TODO ESTA PARTE ESTA POR CORREGIR OJO/*/*****************************
		}	
		if(!centroAtencion.equals(""))
		{
			cadena+=" AND fv.centro_atencion="+centroAtencion;
		}
		
		
		
		cadena+=" order by consecutivo ASC ";
		
		logger.info("CADENA BUSQUEDA AVANZADA>>>>>>>>>"+cadena);
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
			busqueda.close();
			
			//****************************VALIDACION DE FACTURAS VARIAS APROBADAS********************************************
			for(int i=0;i<Utilidades.convertirADouble(mapa.get("numRegistros")+"");i++)
			{
				mapa.put("puedo_anular_"+i, ConstantesBD.acronimoSi);
				mapa.put("mensaje_validacion_"+i, "");
				
				if(mapa.get("estado_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
				{
					/**
					 * VALIDACION DE ANULACION DE VENTA TARJEA CLIENTE 
					 * ANEXO 830
					 */
					if( mapa.get("esconceptotipoventa_"+i).toString().equals(ConstantesBD.acronimoSi) && mapa.get("facturapagada_"+i).toString().equals(ConstantesBD.acronimoSi) )
					{
						mapa.put("puedo_anular_"+i, ConstantesBD.acronimoNo);
						mapa.put("mensaje_validacion_"+i, "");
					}
					
					//1) Se verifica si la factura tiene ajsutes pendientes
					busqueda = new PreparedStatementDecorator(con.prepareStatement(consultarAjustesPendientesFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					busqueda.setLong(1,Long.parseLong(mapa.get("codigofactura_"+i)+""));
					ResultSetDecorator rs = new ResultSetDecorator(busqueda.executeQuery());
					if(rs.next())
					{
						if(rs.getInt("cuenta")>0)
						{
							mapa.put("puedo_anular_"+i, ConstantesBD.acronimoNo);
							mapa.put("mensaje_validacion_"+i, "No se puede anular porque tiene ajustes pendientes.");
						}
					}
					busqueda.close();
					rs.close();
					
					if(UtilidadTexto.getBoolean(mapa.get("puedo_anular_"+i)+""))
					{
						double totalDebito = 0;
						double totalCredito = 0;
						
						//Se verifica totales de las ajustes aporbados
						busqueda = new PreparedStatementDecorator(con.prepareStatement(consultarSumaAjustesAprobadosFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						busqueda.setLong(1,Long.parseLong(mapa.get("codigofactura_"+i)+""));
						busqueda.setString(2,ConstantesIntegridadDominio.acronimoDebito);
						rs = new ResultSetDecorator(busqueda.executeQuery());
						if(rs.next())
						{
							totalDebito = rs.getDouble("valor");
						}
						busqueda.close();
						rs.close();
						
						busqueda = new PreparedStatementDecorator(con.prepareStatement(consultarSumaAjustesAprobadosFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						busqueda.setLong(1,Long.parseLong(mapa.get("codigofactura_"+i)+""));
						busqueda.setString(2,ConstantesIntegridadDominio.acronimoCredito);
						rs = new ResultSetDecorator(busqueda.executeQuery());
						if(rs.next())
						{
							totalCredito = rs.getDouble("valor");
						}
						busqueda.close();
						rs.close();
						
						if(totalDebito!=totalCredito)
						{
							mapa.put("puedo_anular_"+i, ConstantesBD.acronimoNo);
							mapa.put("mensaje_validacion_"+i, "No se puede anular porque suma debito/credito de ajustes aprobados es desigual.");
						}
					}
				}
				
				
				
				
			}
			//*****************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaFacturas: "+e);
		}
		
		//Utilidades.imprimirMapa(mapa);
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificar(Connection con, HashMap vo)
	{
		
		try
		{
			String consulta="";
			if(vo.get("estado_factura").equals("APR"))
			{
				consulta=cadenaModificacionAproStr;
			}
			else if(vo.get("estado_factura").equals("ANU"))
			{
				consulta=cadenaModificacionAnulStr;
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(vo.get("estado_factura").equals("APR"))
			{
				ps.setString(1, vo.get("usuario_aprobacion")+"");

				if(UtilidadTexto.isEmpty(vo.get("motivo_aprobacion")+""))
					ps.setObject(2, null);
				else
					ps.setString(2, vo.get("motivo_aprobacion")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("fecha_aprobacion")+""))
					ps.setObject(3, null);
				else
					ps.setDate(3, Date.valueOf(vo.get("fecha_aprobacion")+""));
				
				if(UtilidadTexto.isEmpty(vo.get("fecha_gene_aprobacion")+""))
					ps.setObject(6, null);
				else
					ps.setDate(6, Date.valueOf(vo.get("fecha_gene_aprobacion")+""));
				
				ps.setString(7, vo.get("hora_gene_aprobacion")+"");
			} 
			else
			{
				ps.setString(1, vo.get("usuario_anulacion")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("motivo_anulacion")+""))
					ps.setObject(2, null);
				else
					ps.setString(2, vo.get("motivo_anulacion")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("fecha_anulacion")+""))
					ps.setObject(3, null);
				else
					ps.setDate(3, Date.valueOf(vo.get("fecha_anulacion")+""));
				
				if(UtilidadTexto.isEmpty(vo.get("fecha_gene_anulacion")+""))
					ps.setObject(6, null);
				else
					ps.setDate(6, Date.valueOf(vo.get("fecha_gene_anulacion")+""));
				
				ps.setString(7, vo.get("hora_gene_anulacion")+"");
			}
			/*ps.setString(4, vo.get("usuario_modifica")+"");
			ps.setDate(5, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(6, vo.get("hora_modifica")+"");*/
			ps.setString(4, vo.get("estado_factura")+"");
			ps.setString(5, vo.get("anulaFacApro")+"");
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo_fac_var")+""));
			boolean resultado = ps.executeUpdate()>0;
			ps.close();
			return resultado;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	

	/**
	 * ACTUALIZAR ESTADO FACTURA VENTA TARJETA CLIENTE 
	 * @param con
	 * @param codigoFactua
	 * @param fechaAnulacion
	 * @param estado
	 * @param usuario
	 * @param hora
	 * @return
	 */
	public static boolean actualizarEstadoFacturaVentaTarjeta(Connection con,  String codigoFactua, String fechaAnulacion, String estado , String usuario, String hora )
	{
		logger.info("\n\n\n\n");
		logger.info("**************************************************************************************************");
		logger.info("-------------------------------------------- ACTUALIZAR ESTADO------------------------------------ ");
		boolean retorna= true;
		String cadenaModificacionVentasTarjetaCliente="update odontologia.venta_tarjeta_cliente set estado_venta=? , fecha_modifica=? , hora_modifica=?, usuario_modifica =?  where codigo_fac_var=? ";
		 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, cadenaModificacionVentasTarjetaCliente);
			ps.setString(1, estado);
			ps.setString(2, fechaAnulacion);
			ps.setString(3, hora);
			ps.setString(4, usuario);
			ps.setInt(5, Utilidades.convertirAEntero(codigoFactua));
			logger.info("\n\n\n\n\n\n\n\n");
			logger.info("Consulta");
			logger.info(ps);
			logger.info("\n\n\n\n\n\n\n\n");
			retorna =ps.executeUpdate()>0;
		}
		
		catch (SQLException e) 
		{
		 logger.info("ERROR al modificar  "+e);			
		}
		return retorna;
	}
	
	
}
