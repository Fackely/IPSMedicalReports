/*
 * @(#)SqlBaseReferenciaContrareferenciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.ConsultaReferenciaContrareferencia;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * Consultas estandar 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class SqlBaseConsultaReferenciaContrareferenciaDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultaReferenciaContrareferenciaDao.class);
	
	/**
	 * encabezado de la consulta de contrareferencia
	 */
	private static String consultaContrareferenciaStr= "SELECT " +
															"'Contrareferencia' as tipo, " +
															"c.numero_referencia_contra as nroreferencia, " +
															"c.institucion_sirc_origen ||' - '|| c.consecutivo_punto_atencion as nrosolicitud, " +
															"to_char(c.fecha_finaliza, 'DD/MM/YYYY') as fechafinaliza, " +
															"c.fecha_finaliza as fechafinalizabd, " +
															"substr(c.hora_finaliza, 1, 5) as horafinaliza, " +
															"insirc.codigo as codinstorig, " +
															"insirc.tipo_red as tiporedorig, " +
															"insirc.descripcion as institucionorigen, " +
															"insirc1.codigo as codinstdest, " +
															"insirc1.tipo_red as tiporeddest, " +
															"insirc1.descripcion as instituciondestino," +
															"r.ingreso as ingreso  " +
														"FROM " +
															"contrarreferencia c " +
															"INNER JOIN referencia r ON (r.numero_referencia= c.numero_referencia_contra)" +
															"LEFT OUTER JOIN instituciones_sirc insirc ON (c.institucion_sirc_origen=insirc.codigo and c.institucion=insirc.institucion) " +
															"LEFT OUTER JOIN instituciones_sirc insirc1 ON (r.institucion_sirc_solicita=insirc1.codigo and r.institucion=insirc1.institucion) ";
	
	/**
	 * encabezade de la consulta de referencia
	 */
	private static String consultaReferenciaStr=	"SELECT " +
														"'Referencia Interna' as tipo, " +
														"r.numero_referencia as nroreferencia, " +
														"r.institucion_sirc_solicita ||' - '|| r.consecutivo_punto_atencion as nrosolicitud, " +
														"to_char(r.fecha_modifica, 'DD/MM/YYYY') as fechafinaliza, " +
														"r.fecha_modifica as fechafinalizabd, " +
														"substr(r.hora_modifica, 1, 5) as horafinaliza, " +
														"insirc.codigo as codinstitucionorigen, " +
														"insirc.tipo_red as trinstitucionorigen, " +
														"insirc.descripcion as institucionorigen, " +
														"'' as codinstituciondestino, " +
														"'' as trinstituciondestino, " +
														"'' as instituciondestino, " +
														"r.ingreso as ingreso " +
													"FROM " +
														"referencia r " +
														"LEFT OUTER JOIN instituciones_sirc insirc ON (r.institucion_sirc_solicita=insirc.codigo and r.institucion=insirc.institucion) ";
													
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param esBusquedaXpaciente
	 * @return
	 */
	public static HashMap busquedaReferenciaContrareferencia(Connection con, ConsultaReferenciaContrareferencia mundo)
	{
		String consulta="";
		consulta= armarBusqueda(mundo);
		logger.info("busuqeda Referencia -->"+consulta);
		String[] indicesMapa={"tipo_", "nroreferencia_", "nrosolicitud_", "fechafinaliza_", "fechafinalizabd_", "horafinaliza_", "institucionorigen_", "instituciondestino_", "ingreso_"};
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaReferenciaContrareferenciaDao "+sqlException.toString() );
				}
			}	
			
		
		}
		mapa.put("INDICES_MAPA", indicesMapa);
		return mapa;
	}

	/**
	 * 
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static String armarBusqueda(ConsultaReferenciaContrareferencia mundo) 
	{
		String consultaContrareferencia= consultaContrareferenciaStr+" WHERE c.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' ";
		String consultaRefencia= consultaReferenciaStr+" WHERE r.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' and r.referencia='"+ConstantesIntegridadDominio.acronimoInterna+"' ";
		String consultaTotal="";
		
		
		if(mundo.getEsBusquedaXpaciente())
		{	
			consultaContrareferencia+=" AND r.codigo_paciente = "+mundo.getCodigoPaciente();
			consultaRefencia+=" AND r.codigo_paciente = "+mundo.getCodigoPaciente();
			
			consultaTotal="SELECT * FROM ("+consultaContrareferencia+" UNION "+consultaRefencia+" )tabla ORDER BY fechafinaliza, horafinaliza, nroreferencia ";
		}
		else
		{
			if(mundo.getTipoReferenciaInterna().equals(ConstantesBD.acronimoSi))
			{
				if(!UtilidadTexto.isEmpty(mundo.getRangoNroReferenciaInicial()) && !UtilidadTexto.isEmpty(mundo.getRangoNroReferenciaFinal()))
					consultaRefencia+=" AND ( r.numero_referencia  BETWEEN "+mundo.getRangoNroReferenciaInicial()+" AND "+mundo.getRangoNroReferenciaFinal()+") ";
				
				if(!UtilidadTexto.isEmpty(mundo.getFechaFinalizacionInicial()) && !UtilidadTexto.isEmpty(mundo.getFechaFinalizacionFinal()))
					consultaRefencia+=" AND (r.fecha_modifica BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinalizacionInicial())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinalizacionFinal())+"') ";
				
				if(!UtilidadTexto.isEmpty(mundo.getCodigoInstitucionOrigen()))
					consultaRefencia+=" AND (r.institucion_sirc_solicita= '"+mundo.getCodigoInstitucionOrigen()+"' AND r.institucion = '"+mundo.getCodigoInstitucion()+"') ";
				
			}
			if(mundo.getTipoContrareferencia().equals(ConstantesBD.acronimoSi))
			{
				//para este punto el tipo contrarefencia siempre tendra una referencia de tipo externa r.referencia = ConstantesIntegridadDominio.acronimoExterna
				if(!UtilidadTexto.isEmpty(mundo.getRangoNroReferenciaInicial()) && !UtilidadTexto.isEmpty(mundo.getRangoNroReferenciaFinal()))
					consultaContrareferencia+=" AND ( c.numero_referencia_contra  BETWEEN "+mundo.getRangoNroReferenciaInicial()+" AND "+mundo.getRangoNroReferenciaFinal()+") ";
				
				if(!UtilidadTexto.isEmpty(mundo.getFechaFinalizacionInicial()) && !UtilidadTexto.isEmpty(mundo.getFechaFinalizacionFinal()))
					consultaContrareferencia+=" AND (c.fecha_finaliza BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinalizacionInicial())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(mundo.getFechaFinalizacionFinal())+"') ";
				
				if(!UtilidadTexto.isEmpty(mundo.getCodigoInstitucionOrigen()))
					consultaContrareferencia+=" AND (c.institucion_sirc_origen= '"+mundo.getCodigoInstitucionOrigen()+"' AND c.institucion = '"+mundo.getCodigoInstitucion()+"') ";
			}
			
			//si seleccionaron las dos entonces se debe hacer un union
			if(mundo.getTipoReferenciaInterna().equals(ConstantesBD.acronimoSi) && mundo.getTipoContrareferencia().equals(ConstantesBD.acronimoSi))
			{
				consultaTotal="SELECT * FROM ("+consultaContrareferencia+" UNION "+consultaRefencia+" )tabla ORDER BY fechafinaliza, horafinaliza, nroreferencia ";
			}
			else
			{
				if(mundo.getTipoReferenciaInterna().equals(ConstantesBD.acronimoSi))
				{
					consultaTotal=consultaRefencia+" ORDER BY fechafinaliza, horafinaliza, nroreferencia ";
				}
				else if(mundo.getTipoContrareferencia().equals(ConstantesBD.acronimoSi))
				{
					consultaTotal=consultaContrareferencia+" ORDER BY fechafinaliza, horafinaliza, nroreferencia ";
				}
			}
		}
		return consultaTotal;
	}
	
}
