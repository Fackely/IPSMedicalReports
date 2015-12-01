package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * Anexo 678
 * Creado el 16 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class SqlBaseReporteReferenciaInternaDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseReporteReferenciaInternaDao.class);


	/**
	 * Método que consulta el reporte de referencia interna
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static HashMap consultaReporteReferenciaInterna (Connection connection, HashMap criterios,String cadena)
	{
		logger.info("\n Entre a consultaReporteReferenciaInterna -->"+criterios);		
		
		cadena+=obtenerWhere(criterios);
		logger.info("\n ********************** Reporte Referencia Interna Cadena --> "+cadena);
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(connection.prepareStatement(cadena));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			return mapaRetorno;
			
		
			
		}
		catch (SQLException e)
		{
			logger.error("\n Problema consultando datos Reporte Referencia Interna "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		return null;
		
	}
	
	/**
	 * Metodo encargado de generar una cadena where
	 * @param criterios
	 * @return
	 */
	public static String obtenerWhere (HashMap criterios)
	{
		
		String where= " WHERE (sir.activo='"+ConstantesBD.acronimoSi+"' OR sir.activo IS NULL)" +
						" AND getcuentafinalasocio(c.id_ingreso,c.id) IS NULL" +
						" AND r.referencia='"+ConstantesIntegridadDominio.acronimoInterna+"'" +
						" AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
						" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') " +
						" AND cc.centro_atencion="+criterios.get("centroAtencion");

		//*********************************************************************************************************************
		//se organizaron los filtros dependiendo el estado de la referencia
		if (UtilidadCadena.noEsVacio(criterios.get("estadoReferencia")+"") && 
			!(criterios.get("estadoReferencia")).equals(ConstantesBD.codigoNuncaValido+""))
		{
			//si en estado de la referencia es solicitado o anulado, entonces se mira el rango de fechas
			//re la referencia en la tabla referencia campo fecha_referencia
			if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado) || 
				(criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			{
				logger.info("\n Estado de referencia es Anulado o Solicitado -->"+criterios.get("estadoReferencia")+"");
				where+=" AND r.estado='"+criterios.get("estadoReferencia")+"'" +
					   " AND r.fecha_referencia BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
					   		                            "'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
			}
			else//si el estado de la referencia es en tramite o finalizado, entonces se mira la fecha de tramite
				//de la tabla servic_instit_referencia el campo fecha_tramite
				if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite) || 
						(criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
				{
					logger.info("\n Estado de referencia es Tramite o Finalizado -->"+criterios.get("estadoReferencia")+"");
					where+=" AND r.estado='"+criterios.get("estadoReferencia")+"'" +
					   " AND sir.fecha_tramite BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
					   "'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
				}
		}
		else//esto es para el caso en el cual no se halla seleccionado ningun estado de la referencia;
			//se consultan en la tabla referencia solo las referencias en los estados:
			//entremite, solicitado, finalizada, anulada, y se miran las fechas de las tablas
			// tabla referencia campo fecha_referencia y tabla servic_instit_referencia el campo fecha_tramite
		{
			logger.info("\n Estado de referencia es Todos -->"+criterios.get("estadoReferencia")+"");
			where+= " AND r.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"','"+
				ConstantesIntegridadDominio.acronimoEstadoFinalizado+"'," +"'"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"','"+
				ConstantesIntegridadDominio.acronimoEstadoAnulado+"')"+" AND r.fecha_referencia BETWEEN '"+
				UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND "+"'"+
				UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'";
			   		                            //+
			   	   //" AND sir.fecha_tramite BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
			   	//	                            "'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'";
		}
		//*********************************************************************************************************************************
		
		//se acomodan los filtros para el tipo de reporte.
		//para el tipo de reporte remitido 
		if ((criterios.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoPacientesReferidosAOtraInstitucion))
		{
			where+=" AND r.tipo_referencia='"+ConstantesIntegridadDominio.acronimoRemision+"'";
		}
		else if ((criterios.get("tipoReporte")+"").equals(ConstantesIntegridadDominio.acronimoPacientesReferidosParaExamenes))
		{
			where+= " AND r.tipo_referencia IN ('"+ConstantesIntegridadDominio.acronimoOrdenServicio+"','"+
				ConstantesIntegridadDominio.acronimoTransferenciaTecnologia+"',"+"'"+
				ConstantesIntegridadDominio.acronimoRemisionInterconsulta+"')";
		}
		
			
		//se acomoda el filtro para institucion solicitada.
		if (UtilidadCadena.noEsVacio(criterios.get("institucionSolicitada")+"") && 
			!(criterios.get("institucionSolicitada")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			where+=" AND  itr.institucion_referir='"+criterios.get("institucionSolicitada")+"'";
		}
		
		
		
		return where;
	}
	
	
}
	