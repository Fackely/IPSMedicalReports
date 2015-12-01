package com.princetonsa.dao.sqlbase.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

public class SqlBaseConsultaImpresionDevolucionDao
{
	private static Logger logger = Logger.getLogger(SqlBaseConsultaImpresionDevolucionDao.class);
	
	private static String [] indicesMapC={"consecutivo_","descripcion_"};
	
	private static String [] indicesMapM={"codigo_","descripcion_"};
	
	private static String [] indicesMapD={"codigo_","fechadevolucion_","nombreBeneficiario_","tipoIdBeneficiario_","numeroIdBeneficiario_","descripcion_","valorDevolucion_","estado_"};
	
	private static String [] indicesMapDD={"codigo_","fecha_aprobacion_","fecha_anulacion_","hora_aprobacion_","hora_anulacion_","usuario_aprobacion_","usuario_anulacion_","motivo_anulacion_","forma_pago_","observaciones_","fecha_","hora_","numero_rc_","consecutivorc_","usuario_devolucion_","fecha_devolucion_","nombre_beneficiario_","tipo_id_beneficiario_","numero_id_beneficiario_","descripcion_","valor_devolucion_","estado_"};
	
	private static String consultaStrC="SELECT consecutivo, descripcion FROM cajas";
	
	private static String consultaStrM="SELECT codigo, descripcion FROM motivos_devolucion_rc";
	
	private static String consultaStrD;
	
	public static String consultaStrDD;
	
	public static HashMap<String, Object> consultaCajas (Connection con)
	{
		HashMap<String, Object> resultadosC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrC, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosC.put("INDICES",indicesMapC);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Cajas");
		}
		return resultadosC;
	}
	
	public static HashMap<String, Object> consultaMotivos (Connection con)
	{
		HashMap<String, Object> resultadosM = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrM, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosM = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosM.put("INDICES",indicesMapM);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Motivos");
		}
		return resultadosM;
	}
	
	public static HashMap<String, Object> consultaDevoluciones (Connection con, String fechaIni, String fechaFin, String devolucionI, String devolucionF, String motivoD, String estadoD, String tipoId, String numeroId, String centroAtencion, String caja)
	{
		HashMap<String, Object> resultadosD = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		consultaStrD = "SELECT " +
							"dvrc.consecutivo AS codigo, " +
							"to_char(dvrc.fecha_devolucion, 'DD/MM/YYYY') AS fechaDevolucion, " +
							"dtcrc.nombre_beneficiario, " +
							"dtcrc.tipo_id_beneficiario, " +
							"dtcrc.numero_id_beneficiario, " +
							"md.descripcion, " +
							"dvrc.valor_devolucion || '' AS valor_devolucion, " +
							"getintegridaddominio(dvrc.estado) AS estado " +
						"FROM devol_recibos_caja dvrc INNER JOIN recibos_caja rc ON (dvrc.numero_rc = rc.numero_recibo_caja AND dvrc.institucion = rc.institucion) " +
								"INNER JOIN detalle_conceptos_rc dtcrc ON (dtcrc.numero_recibo_caja = rc.numero_recibo_caja AND dtcrc.institucion = rc.institucion) " +
								"INNER JOIN motivos_devolucion_rc md ON (dvrc.motivo_devolucion = md.codigo) " +
								"INNER JOIN cajas c ON (dvrc.caja_devolucion = c.consecutivo) where 1=1 ";
		
		if(!fechaIni.trim().equals(""))
		{
			consultaStrD += " and dvrc.fecha_devolucion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaIni)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFin)+"' ";
		}
		
		if(!devolucionI.equals(""))
		{
			consultaStrD += "AND dvrc.consecutivo BETWEEN '"+devolucionI+"' AND '"+devolucionF+"' ";
		}
		
		if(!motivoD.equals(ConstantesBD.codigoNuncaValido+""))
		{
			consultaStrD += "AND md.codigo = " +motivoD+ " ";
		}
		
		if(!estadoD.equals(ConstantesBD.codigoNuncaValido+""))
		{
			consultaStrD += "AND dvrc.estado = '" +estadoD+ "' ";
		}
		
		if(!tipoId.equals(""))
		{
			consultaStrD += "AND dtcrc.tipo_id_beneficiario = '" +tipoId+ "' AND dtcrc.numero_id_beneficiario = '" +numeroId+ "' ";
		}
		
		if(!centroAtencion.equals("*"))
		{
			consultaStrD += "AND c.centro_atencion = " +centroAtencion+ " ";
		}
		
		
		if(!caja.equals(ConstantesBD.codigoNuncaValido+""))
		{
			consultaStrD += "AND c.consecutivo = " +caja+ " ";
		}
		
		consultaStrD += "ORDER BY dvrc.codigo ";
		
		logger.info("====>Consulta Devoluciones: "+consultaStrD);
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrD, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultadosD = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultadosD.put("INDICES",indicesMapD);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Devoluciones");
		}
		return resultadosD;
	}
	
	public static HashMap<String, Object> consultaDetalleD (Connection con, String codigoD)
	{
		HashMap<String, Object> resultadosDD = new HashMap<String, Object>();
		
		Statement pst;
		
		consultaStrDD="SELECT " +
							"dvrc.consecutivo AS codigo, " +
							"to_char(dvrc.fecha_devolucion, 'DD/MM/YYYY') AS fechaDevolucion, " +
							"dvrc.valor_devolucion || '' AS valor_devolucion, " +
							"dvrc.usuario_devolucion AS usuario_devolucion, " +
							"getintegridaddominio(dvrc.estado) AS estado, " +
							"md.descripcion AS descripcion, " +
							"dtcrc.nombre_beneficiario AS nombre_beneficiario, " +
							"dtcrc.tipo_id_beneficiario AS tipo_id_beneficiario, "+
							"dtcrc.numero_id_beneficiario AS numero_id_beneficiario, " +
							"rc.consecutivo_recibo AS numero_rc, " +
							"to_char(rc.fecha, 'DD/MM/YYYY') AS fecha, " +
							 /*
      						 Tipo Modificacion: Segun MT4611
      						 Autor: Jesús Darío Ríos
      						 usuario: jesrioro
      						 Fecha: 22-02-2013
      						 Descripcion: cambio en el formato de la hora 
                             */
							"to_char(rc.hora,'HH24:MI:SS') as hora,"+
							"dvrc.forma_pago AS forma_pago, " +
							"dvrc.observaciones AS observaciones, " +
							"coalesce(dvrc.fecha_aprobacion, dvrc.fecha_devolucion) AS fecha_arp_format," +
							"dvrc.hora_aprobacion AS hora_aprobacion, " +
							"dvrc.usuario_aprobacion AS usuario_aprobacion, " +
							"cj.descripcion AS caja_devolucion, " +
							"dvrc.doc_soporte AS doc_soporte, " +
							"dvrc.fecha_anulacion AS fecha_anulacion, " +
							"dvrc.hora_anulacion AS hora_anulacion, " +
							"dvrc.usuario_anulacion AS usuario_anulacion, " +
							"dvrc.motivo_anulacion AS motivo_anulacion," +
							"rc.consecutivo_recibo as consecutivorc " +
					 "FROM devol_recibos_caja dvrc INNER JOIN recibos_caja rc ON (dvrc.numero_rc = rc.numero_recibo_caja AND dvrc.institucion = rc.institucion) " +
							"INNER JOIN detalle_conceptos_rc dtcrc ON (dtcrc.numero_recibo_caja = rc.numero_recibo_caja AND dtcrc.institucion = rc.institucion) " +
							"INNER JOIN motivos_devolucion_rc md ON (dvrc.motivo_devolucion = md.codigo) "+
							"INNER JOIN tesoreria.cajas cj ON (dvrc.caja_devolucion = cj.consecutivo) ";
		consultaStrDD += "WHERE dvrc.consecutivo = " + codigoD + "";
		 
		try{
			pst = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			resultadosDD = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery(consultaStrDD)), true, true);
			
			resultadosDD.put("INDICES",indicesMapDD);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta del detalle de una Devolucion");
		}
		return resultadosDD;
	}
}