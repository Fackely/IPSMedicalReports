package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoReporteEdadCarteraPaciente;
import com.princetonsa.mundo.carteraPaciente.EdadCarteraPaciente;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

public class SqlBaseEdadCarteraPacienteDao
{
	private static Logger logger = Logger.getLogger(SqlBaseEdadCarteraPacienteDao.class);
	
	/**
	 * Cadena para la consulta del listado
	 * */
	private static final String consultaListadoStr = "" +
			"SELECT " +
			"cdf.codigo_pk as codigocuota," +
			"dg.tipo_documento," +
			"coalesce(dg.consecutivo,'') AS consecutivo," +
			"coalesce(dg.anio_consecutivo,'') AS anio_consecutivo, "+
			"to_char(df.fecha_inicio,'dd/mm/yyyy') AS fecha_inicio," +
			"df.dias_por_couta," +
			"df.nro_coutas," +
			"cdf.nro_cuota AS ind_cuota," +
			"getnombrecentatenxing(dg.ingreso) AS nombrecentroatencion, " +
			"(cdf.valor_couta -" +
			"(SELECT SUM(dapcp3.valor)" +
			"FROM carterapaciente.det_apli_pagos_cartera_pac dapcp3 " +
			"INNER JOIN carterapaciente.aplicac_pagos_cartera_pac apcp3  ON (apcp3.codigo_pk = dapcp3.apli_pago_cartera_pac " +
			"AND to_char(apcp3.fecha_aplicacion ,'yyyy-mm-dd') <= ?) " +
			"WHERE apcp3.datos_financiacion = df.codigo_pk AND dapcp3.cuota_dato_financiacion = cdf.codigo_pk " +
			")) AS valor " +
			"FROM " +
			"carterapaciente.documentos_garantia dg " +
			"INNER JOIN carterapaciente.datos_financiacion df ON (df.codigo_pk_docgarantia=dg.codigo_pk ) " +
			"INNER JOIN carterapaciente.cuotas_datos_financiacion cdf ON (df.codigo_pk = cdf.dato_financiacion) " +
			ConstantesBD.separadorSplit+"1 "+
			"WHERE " +
			"dg.tipo_documento IN ( "+ConstantesBD.separadorSplit+"2 ) AND " +
			"to_char(dg.fecha_generacion,'yyyy-mm-dd') <= ? AND " +
			"dg.cartera = '"+ConstantesBD.acronimoSi+"' AND " +
			"dg.estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' AND " +
			"(cdf.valor_couta - (SELECT SUM(dapcp1.valor) "+
			"FROM carterapaciente.det_apli_pagos_cartera_pac dapcp1 "+
			"INNER JOIN carterapaciente.aplicac_pagos_cartera_pac apcp1  ON (apcp1.codigo_pk = dapcp1.apli_pago_cartera_pac "+
			"AND to_char(apcp1.fecha_aplicacion ,'yyyy-mm-dd') <= ? ) "+
			"WHERE apcp1.datos_financiacion = df.codigo_pk AND dapcp1.cuota_dato_financiacion = cdf.codigo_pk )) > 0 "+
			"ORDER BY dg.tipo_documento ASC, nombrecentroatencion ASC, dg.fecha_generacion, dg.hora_generacion,dg.consecutivo, dg.anio_consecutivo ASC ";
 
	/**
	 * Consulta listado de edad glosa
	 * @param Connection con
	 * @param  HashMap parametros
	 * */
	public static ArrayList<DtoDatosFinanciacion> getListadoEdadGlosa(Connection con, HashMap parametros)
	{
		ArrayList<DtoDatosFinanciacion> array = new ArrayList<DtoDatosFinanciacion>();
		String cadena = consultaListadoStr;
		
		if(parametros.containsKey("centroAtencion") && 
				!parametros.get("centroAtencion").toString().equals(""))
			cadena = cadena.replace((ConstantesBD.separadorSplit+"1")," INNER JOIN manejopaciente.ingresos ing ON (ing.id = dg.ingreso AND ing.centro_atencion = "+parametros.get("centroAtencion").toString()+" ) ") ;
		else 
			cadena = cadena.replace((ConstantesBD.separadorSplit+"1"),"");
		
		if(parametros.get("tipoDocumento").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
			cadena = cadena.replace((ConstantesBD.separadorSplit+"2"),"'"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"','"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"'");
		else
			cadena = cadena.replace((ConstantesBD.separadorSplit+"2"),"'"+parametros.get("tipoDocumento").toString()+"'") ;
		
		try
		{
			logger.info("cadena "+cadena+" >> "+parametros);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setString(1,UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaCorte").toString()));
			ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaCorte").toString()));
			ps.setString(3,UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaCorte").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoDatosFinanciacion dto = new DtoDatosFinanciacion();
				dto.setTipoDocumento(rs.getString("tipo_documento"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setAnioConsecutivo(rs.getString("anio_consecutivo"));
				dto.setFechaInicio(rs.getString("fecha_inicio"));
				dto.setDiasPorCuota(rs.getInt("dias_por_couta"));
				dto.setNroCoutas(rs.getInt("nro_coutas"));
				dto.setValorSaldo(rs.getString("valor"));
				dto.setNombreCentroAtenDocGaran(rs.getString("nombrecentroatencion"));
				dto.setDiasVencimiento(EdadCarteraPaciente.calcularDiasVencimiento(dto.getFechaInicio(),parametros.get("fechaCorte").toString(),dto.getDiasPorCuota(),rs.getInt("ind_cuota")));
				dto.setIsNuevoDoc(ConstantesBD.acronimoSi);
				array.add(dto);
			}
		}
		catch (Exception e) {
			logger.info("error en getListadoEdadGlosa >> "+e+" >> "+parametros+" >> "+cadena);
		}
		
		return array;
	}
}