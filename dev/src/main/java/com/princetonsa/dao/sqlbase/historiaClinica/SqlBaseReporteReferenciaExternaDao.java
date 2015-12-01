package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eclipse.birt.report.model.api.DesignEngine;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * Anexo 679
 * Creado el 12 de Septiembre de 2008
 * @author Ing. Felipe Perez
 * @mail lfperez@princetonsa.com
 */
public class SqlBaseReporteReferenciaExternaDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseReporteReferenciaExternaDao.class);
	
	/**
	 * Cadena de consulta que verifica el estado en la tabla de referencia	
	 */
	public static final String strConsultaRefenciaExternaR =" (SELECT DISTINCT " +
			" r.numero_referencia AS numero_referencia, "+
			" getinicialesusuario(r.usuario_modifica) || getdiames(r.codigo_paciente) || r.numero_referencia AS codigo_aceptacion, "+
			" to_char(r.fecha_referencia,'DD/MM/YYYY') As fecha, "+
			" r.hora_referencia As hora, "+			
			" getnumeroytipoid(r.codigo_paciente) AS idpaciente, "+
			" getnombrepersona(r.codigo_paciente) As paciente, "+
			" getedadsimpleresumido(current_date,to_date(getfechanacimientopaciente(r.codigo_paciente), 'DD/MM/YYYY')) AS edad, "+
			//" getdiagnosticoreferencia(r.numero_referencia) As diagnostico, "+
			" getDiagPrinValSircSinTipocie(r.numero_referencia) As diagnostico, "+
			//" getinstitucionremite(cr.numero_referencia_contra) "
			" getinstitucionorigen(r.numero_referencia) " +
			" ||'-'|| getdescripcioninstitucionsirc(getinstitucionorigen(r.numero_referencia),r.institucion) As institucion_remite, "+
			" r.profesional_salud AS nombre_quien_remite, "+
			" coalesce (getnombreusuario2(i.usuario_modifica),'') AS nombre_acepta_admin, "+
			" getnombrepersona(cr.profesional_responde) AS nombre_acepta_medica, "+
			" r.estado AS estado, "+
			" cr.hora_remision As hora_aceptacion, "+
			//" getnomcentroatencion(cc.centro_atencion) As centro_atencion "+
			" COALESCE (getnomcentroatencion(cc.centro_atencion), 'SIN CENTRO DE ATENCIÓN ASIGNADO') As centro_atencion "+
			" FROM referencia r "+
			" LEFT OUTER JOIN cuentas c ON (c.id_ingreso=r.ingreso) "+
			" LEFT OUTER JOIN centros_costo cc ON (cc.codigo=c.area) "+
			" LEFT OUTER JOIN personas p ON (p.codigo=r.codigo_paciente) "+
			" LEFT OUTER JOIN contrarreferencia cr ON (r.numero_referencia=cr.numero_referencia_contra) "+
			" LEFT OUTER JOIN ingresos i ON (i.id=r.ingreso) ";

	/**
	 * cadena de consulta que verifica el estado en la tabla de contrarreferencia
	 */
	public static final String strConsultaRefenciaExternaCR =" (SELECT DISTINCT " +
			" cr.numero_referencia_contra AS numref, "+
			" getinicialesusuario(r.usuario_modifica) || getdiames(r.codigo_paciente) || r.numero_referencia AS codigo_aceptacion, "+
			" to_char(r.fecha_referencia,'DD/MM/YYYY') As fecha, "+
			" r.hora_referencia As hora, "+			
			" getnumeroytipoid(r.codigo_paciente) AS idpaciente, "+
			" getnombrepersona(r.codigo_paciente) As paciente, "+
			" getedadsimpleresumido(current_date,to_date(getfechanacimientopaciente(r.codigo_paciente), 'DD/MM/YYYY')) AS edad, "+
			//" getDiagnostContrarreferencia(cr.numero_referencia_contra) As diagnostico, "+
			" coalesce(getDiagPrinValSircSinTipocie(cr.numero_referencia_contra),'') As diagnostico, "+
			//" getinstitucionremite(cr.numero_referencia_contra) As institucion_remite, "+
			" getinstitucionorigen(r.numero_referencia) " +
				" ||'-'|| getdescripcioninstitucionsirc(getinstitucionorigen(r.numero_referencia),r.institucion) As institucion_remite, "+
			" r.profesional_salud AS nombre_quien_remite, "+
			" coalesce (getnombreusuario2(i.usuario_modifica),'') AS nombre_acepta_admin, "+
			" getnombrepersona(cr.profesional_responde) AS nombre_acepta_medica, "+
			" cr.estado AS estado, "+
			" cr.hora_remision As hora_aceptacion, "+
			//" getnomcentroatencion(cc.centro_atencion) As centro_atencion "+
			" COALESCE (getnomcentroatencion(cc.centro_atencion), 'SIN CENTRO DE ATENCIÓN ASIGNADO') As centro_atencion "+
			" FROM referencia r "+
			" INNER JOIN personas p ON (p.codigo=r.codigo_paciente) "+
			" INNER JOIN cuentas c ON (c.id_ingreso=r.ingreso) "+
			" INNER JOIN centros_costo cc ON (cc.codigo=c.area) "+
			" INNER JOIN contrarreferencia cr ON (r.numero_referencia=cr.numero_referencia_contra) "+
			" INNER JOIN ingresos i ON (i.id=r.ingreso) ";
	
	/**
	 * consulta genérica que trae todos los valores necesitados
	 */
	public static final String strConsultaRefenciaExterna =" SELECT DISTINCT " +
			" t.\"numero_referencia\" AS numref, "+
			" t.\"codigo_aceptacion\" AS codigo_aceptacion, "+  
			" t.\"fecha\" AS fecha, "+
			" t.\"hora\" AS hora, "+
			" t.\"idpaciente\" AS idpaciente, "+
			" t.\"paciente\" AS paciente, "+
			" t.\"edad\" AS edad, "+
			" t.\"diagnostico\" AS diagnostico, "+  
			" t.\"institucion_remite\" AS institucion, "+ 
			" t.\"nombre_quien_remite\" AS nombre_quien_remite, "+
			" t.\"nombre_acepta_admin\" AS nombre_acepta_admin, "+
			" t.\"nombre_acepta_medica\" AS nombre_acepta_medica, "+
			" t.\"estado\" AS estado, "+
			" t.\"hora_aceptacion\" AS hora_aceptacion, "+
			" t.\"centro_atencion\" AS centro_atencion";
	
	/**
	 * Consulta referencia externa estado a la tabla de referencia
	 */
	public static final String ConsultaRefenciaExterna =" SELECT DISTINCT " +
			" r.numero_referencia AS numref, "+
			" getinicialesusuario(r.usuario_modifica) || getdiames(r.codigo_paciente) || r.numero_referencia AS codigo_aceptacion, "+
			" to_char(r.fecha_referencia,'DD/MM/YYYY') As fecha, "+
			" r.hora_referencia As hora, "+			
			" getnumeroytipoid(r.codigo_paciente) AS idpaciente, "+
			" getnombrepersona(r.codigo_paciente) As paciente, "+
			" getedadsimpleresumido(current_date,to_date(getfechanacimientopaciente(r.codigo_paciente), 'DD/MM/YYYY')) AS edad, "+
			//" getdiagnosticoreferencia(r.numero_referencia) As diagnostico, "+
			" getDiagPrinValSircSinTipocie(r.numero_referencia) As diagnostico, "+
			//" getinstitucionremite(cr.numero_referencia_contra) As institucion_remite, "+
			" getinstitucionorigen(r.numero_referencia) " +
			" ||'-'|| getdescripcioninstitucionsirc(getinstitucionorigen(r.numero_referencia),r.institucion) As institucion_remite, "+
			" r.profesional_salud AS nombre_quien_remite, "+
			" coalesce (getnombreusuario2(i.usuario_modifica),'') AS nombre_acepta_admin, "+
			" getnombrepersona(cr.profesional_responde) AS nombre_acepta_medica, "+
			" r.estado AS estado, "+
			" cr.hora_remision As hora_aceptacion, "+
			//" getnomcentroatencion(cc.centro_atencion) As centro_atencion "+
			" COALESCE (getnomcentroatencion(cc.centro_atencion), 'SIN CENTRO DE ATENCIÓN ASIGNADO') As centro_atencion "+
			" FROM referencia r "+
			" LEFT OUTER JOIN cuentas c ON (c.id_ingreso=r.ingreso) "+
			" LEFT OUTER JOIN centros_costo cc ON (cc.codigo=c.area) "+
			" LEFT OUTER JOIN personas p ON (p.codigo=r.codigo_paciente) "+
			" LEFT OUTER JOIN contrarreferencia cr ON (r.numero_referencia=cr.numero_referencia_contra) "+
			" LEFT OUTER JOIN ingresos i ON (i.id=r.ingreso) ";
	
	/**
	 * Consulta referencia externa estado a la tabla de contrarreferncia
	 */
	public static final String ConsultaRefenciaExternaContra =" SELECT DISTINCT " +
			" cr.numero_referencia_contra AS numref, "+
			" getinicialesusuario(r.usuario_modifica) || getdiames(r.codigo_paciente) || r.numero_referencia AS codigo_aceptacion, "+
			" to_char(r.fecha_referencia,'DD/MM/YYYY') As fecha, "+
			" r.hora_referencia As hora, "+			
			" getnumeroytipoid(r.codigo_paciente) AS idpaciente, "+
			" getnombrepersona(r.codigo_paciente) As paciente, "+
			" getedadsimpleresumido(current_date,to_date(getfechanacimientopaciente(r.codigo_paciente), 'DD/MM/YYYY')) AS edad, "+
			//" getDiagnostContrarreferencia(cr.numero_referencia_contra) As diagnostico, "+
			" coalesce(getDiagPrinValSircSinTipocie(cr.numero_referencia_contra),'') As diagnostico, "+
			//" getinstitucionremite(cr.numero_referencia_contra) As institucion_remite, "+
			" getinstitucionorigen(r.numero_referencia) " +
			" ||'-'|| getdescripcioninstitucionsirc(getinstitucionorigen(r.numero_referencia),r.institucion) As institucion_remite, "+
			" r.profesional_salud AS nombre_quien_remite, "+
			" coalesce (getnombreusuario2(i.usuario_modifica),'') AS nombre_acepta_admin, "+
			" getnombrepersona(cr.profesional_responde) AS nombre_acepta_medica, "+
			" cr.estado AS estado, "+
			" cr.hora_remision As hora_aceptacion, "+
			//" getnomcentroatencion(cc.centro_atencion) As centro_atencion "+
			" COALESCE (getnomcentroatencion(cc.centro_atencion), 'SIN CENTRO DE ATENCIÓN ASIGNADO') As centro_atencion "+
			" FROM referencia r "+
			" LEFT OUTER JOIN cuentas c ON (c.id_ingreso=r.ingreso) "+
			" LEFT OUTER JOIN centros_costo cc ON (cc.codigo=c.area) "+
			" LEFT OUTER JOIN personas p ON (p.codigo=r.codigo_paciente) "+
			" LEFT OUTER JOIN contrarreferencia cr ON (r.numero_referencia=cr.numero_referencia_contra) "+
			" LEFT OUTER JOIN ingresos i ON (i.id=r.ingreso) ";
	
	/**
	 * Cadena de ordenamiento
	 */
	public static final String groupby= " GROUP BY " +
											" t.\"numero_referencia\", "+
											" t.\"centro_atencion\", "+
											" t.\"codigo_aceptacion\", "+
											" t.\"fecha\", "+
											" t.\"hora\", "+
											" t.\"idpaciente\", "+
											" t.\"paciente\", "+
											" t.\"edad\", "+
											" t.\"diagnostico\", "+
											" t.\"institucion_remite\", "+
											" t.\"nombre_quien_remite\", "+
											" t.\"nombre_acepta_admin\", "+
											" t.\"nombre_acepta_medica\", "+
											" t.\"estado\", "+
											" t.\"hora_aceptacion\" ";	
	
	/**
	 * Método que realiza la consulta para el reporte referencia externa
	 * @param connection
	 * @param HashMap criterios
	 * @return cargarValueObject / NULL
	 */
	public static HashMap consultaReporteReferenciaExterna (Connection connection, HashMap criterios)
	{
		logger.info("\n Entre a consultaReporteReferenciaExterna -->"+criterios);
		String cadena0 = strConsultaRefenciaExterna;
		String cadena1 = strConsultaRefenciaExternaR;
		String cadena2 = strConsultaRefenciaExternaCR;
		String agrupa = groupby;
		
		cadena1+=obtenerWhere(criterios,1)+" ";
		cadena2+=obtenerWhere(criterios,2)+")) ";
		cadena0+=" FROM "+cadena1+" UNION "+cadena2+" t "+agrupa;
		
		logger.info("\n *** cadena referencia --> \n"+cadena1);
		logger.info("\n *** cadena contrareferencia--> \n"+cadena2);
		
		cadena0=UtilidadBD.ponerComillasAlias(cadena0);
		logger.info("\n *** cadena principal --> \n"+cadena0);
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(connection.prepareStatement(cadena0));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		
			return mapaRetorno;	
		}
		catch (SQLException e)
		{
			logger.error("\n Problema consultando datos Reporte Referencia Externa "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReporteReferenciaExternaDao "+sqlException.toString() );
			}
		}
		return null;
		
	}
	
	/**
	 * Método en cargado de realizar la consulta para el reporte referencia externa cuando ha elegido todos los estados
	 * @param connection
	 * @param HashMap criterios
	 * @return cargarValueObject / NULL
	 */
	public static HashMap consultaReporteReferenciaExternaConEstado (Connection connection, HashMap criterios)
	{
		logger.info("\n Entre a consultaReporteReferenciaExternaConEstado -->"+criterios);
		String cadena3 = ConsultaRefenciaExterna;
		cadena3+=obtenerWhere(criterios,1)+" ";
		logger.info("\n *** Cadena Con Estado --> \n"+cadena3);
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(connection.prepareStatement(cadena3));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		
			return mapaRetorno;
					
		}
		catch (SQLException e)
		{
			logger.error("\n Problema consultando datos Reporte Referencia Externa "+e);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReporteReferenciaExternaDao "+sqlException.toString() );
			}
		}
		return null;
		
	}
	
	/**
	 * Método encargado de generar una cadena where
	 * @param HashMap criterios
	 * @param int bandera
	 * @return where
	 */
	public static String obtenerWhere (HashMap criterios, int bandera)
	{
		String where= " WHERE getcuentafinalasocio(c.id_ingreso,c.id) IS NULL" +
						" AND r.referencia='"+ConstantesIntegridadDominio.acronimoExterna+"'";
		
		//*********************************************************************************************************************
		/*
		 * 	Se organizaron los filtros dependiendo el estado de la referencia
		 * 	Hay varias opciones para la defininción de filtro para el centro de atención
		 * 	1. Para centro de atención: TODOS:
		 * 		Mostrar todos los estados posibles de consulta.
		 *  2. Para centro de atención: SELECCIONADO:
		 *  	Mostrar SOLO los estados FIN y ADM.
		 *  3. Para centro de atención: SELECCIONE:
		 *  	Mostrar mensaje de error pidiendo que elija un centro de atención.
		 *  
		 *  Si el estado de la referencia es Admitido, En Tramite o Anulado, entonces se mira el rango de fechas
		 *  de la referencia en la tabla referencia campo fecha_referencia
		 */
		
		if (UtilidadCadena.noEsVacio(criterios.get("estadoReferencia")+"") && 
			!(criterios.get("estadoReferencia")).equals(ConstantesBD.codigoNuncaValido+""))
		{
			if((criterios.get("centroAtencion")+"").equals("-2"))
			{
				if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAdmitido) ||
						(criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite) ||
						(criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
					{
						logger.info("*** Entré al estado Admitido--->>> "+criterios.get("estadoReferencia")+"");
						
						if((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAnulado) ||
						   (criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite))
						{
							logger.info("*** Entré al estado ANU o TRA --->>> "+criterios.get("estadoReferencia")+"");
							where+= " AND r.estado='"+criterios.get("estadoReferencia")+"'" +
						    " AND to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
						    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
						}
						
						if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAdmitido))
						{
							logger.info("*** Entré al estado ADM --->>> "+criterios.get("estadoReferencia")+"");
							where+= " AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
							" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
							" AND r.estado='"+criterios.get("estadoReferencia")+"'" +
							" AND getEstadoFinSiEstaAdm(numero_referencia)<>'FIN'"+
						    " AND to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND "+
						    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
						}
					}
					
					if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
					{
						logger.info("*** Entré al estado FIN de la cadena con estado !!!--->>> "+criterios.get("estadoReferencia")+"");
						where+= " AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
								" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
								" AND cr.estado='"+criterios.get("estadoReferencia")+"'" +
								" AND to_char(cr.fecha_finaliza,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
						   		                            "'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
					}
			}
			
			else
			{
				if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAdmitido) ||
						(criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite) ||
						(criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
					{
						logger.info("*** Entré al estado Admitido--->>> "+criterios.get("estadoReferencia")+"");
						
						if((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAnulado) ||
						   (criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoEnTramite))
						{
							logger.info("*** Entré al estado ANU o TRA --->>> "+criterios.get("estadoReferencia")+"");
							where+= " AND r.estado='"+criterios.get("estadoReferencia")+"'" +
						    " AND to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
						    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
						}
						
						if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAdmitido))
						{
							logger.info("*** Entré al estado ADM --->>> "+criterios.get("estadoReferencia")+"");
							where+= " AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
							" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
							" AND cc.centro_atencion="+criterios.get("centroAtencion"+"")+
							" AND r.estado='"+criterios.get("estadoReferencia")+"'" +
							" AND getEstadoFinSiEstaAdm(numero_referencia)<>'FIN'"+
						    " AND to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND "+
						    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
						}
					}
					
					if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
					{
						logger.info("*** Entré al estado FIN de la cadena con estado !!!--->>> "+criterios.get("estadoReferencia")+"");
						where+= " AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
								" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
								" AND cr.estado='"+criterios.get("estadoReferencia")+"'" +
								" AND cc.centro_atencion="+criterios.get("centroAtencion"+"") +
								" AND to_char(cr.fecha_finaliza,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
						   		                            "'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'" ;
					}
			}
			
		}
		else
		{
			/*
			 * Esto es para el caso en el cual no se halla seleccionado ningun estado de la referencia;
			 * se consultan en la tabla referencia solo las referencias en los estados:
			 * en tramite, admitido, finalizada, anulada, y se miran las fechas de las tablas
			 * tabla referencia campo fecha_referencia y tabla servic_instit_referencia el campo fecha_tramite
			 */
		 
			logger.info("*** Entré a Todos los estados en obtener where --->>> "+criterios.get("estadoReferencia")+"");
			if((criterios.get("centroAtencion")+"").equals("-2"))
			{
				logger.info("*** El centro de atención es TODOS --->>> "+criterios.get("centroAtencion")+"");
				logger.info("*** La agregación a la consulta mediante el WHERE," +
						"se quitarán todas las validaciones de centro de atención--->>> "+
						criterios.get("centroAtencion")+"");
				
				if(bandera==1)
				{
					/*
					 * En el caso de la cadena1, este es el obtener where
					 */
					logger.info("*** Entré al obtener where de la cadena1 bandera--->>> "+bandera);
					where+= " AND (((getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
							" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
							" AND (r.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"','"
								+ConstantesIntegridadDominio.acronimoEstadoAdmitido+"','"
								+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'))"+
							" AND getEstadoFinSiEstaAdm(numero_referencia)<>'FIN' )"+
							//" AND cc.centro_atencion="+criterios.get("centroAtencion")+")" +
							" OR (r.ingreso is null AND r.estado IN ('TRA','ANU')))"+
							" AND (to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
			   		        	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'"+
			   		        " OR to_char(cr.fecha_finaliza,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
				   		    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"')" ;
					logger.info("\n\n*** la cadena1 obteniendo el where es--->>> "+where);
				}
				
				if(bandera==2)
				{
					/*
					 * En el caso de la cadena2, este es el obtener where
					 */
					logger.info("*** Entré al obtener where de la cadena2 bandera--->>> "+bandera);
					where+= " AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
					" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
					//" AND cc.centro_atencion="+criterios.get("centroAtencion"+"") +
					" AND cr.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"'"+
					" AND (to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
	   		        	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'"+
	   		        " OR to_char(cr.fecha_finaliza,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
		   		    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"')" ;
					logger.info("\n\n*** la cadena2 obteniendo el where es--->>> "+where);
				}
			}
			else
			{
				logger.info("===> Entré a mostrar todos los estados de un centro de atención, Sólo se pueden mostrar los estados:" +
						"ADM y FIN");
				
				if(bandera==1)
				{
					/*
					 * En el caso de la cadena1, este es el obtener where
					 */
					logger.info("*** Entré al obtener where de la cadena1 bandera--->>> "+bandera);
					where+= " AND (((getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
							" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
							" AND (r.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"','"
								+ConstantesIntegridadDominio.acronimoEstadoAdmitido+"','"
								+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'))"+
							" AND getEstadoFinSiEstaAdm(numero_referencia)<>'FIN'"+
							" AND cc.centro_atencion="+criterios.get("centroAtencion")+")" +
							" OR (r.ingreso is null AND r.estado IN ('TRA','ANU')))"+
							" AND cc.centro_atencion IS NOT NULL "+
							" AND (to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
			   		        	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'"+
			   		        " OR to_char(cr.fecha_finaliza,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
				   		    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"')";
					logger.info("\n\n*** la cadena1 obteniendo el where es--->>> "+where);
				}
				
				if(bandera==2)
				{
					/*
					 * En el caso de la cadena2, este es el obtener where
					 */
					logger.info("*** Entré al obtener where de la cadena2 bandera--->>> "+bandera);
					where+= " AND (getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
					" OR getestadoingreso(r.ingreso)='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') "+
					" AND cc.centro_atencion="+criterios.get("centroAtencion"+"") +
					" AND cr.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"'"+
					" AND cc.centro_atencion IS NOT NULL "+
					" AND (to_char(r.fecha_referencia,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
	   		        	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"'"+
	   		        " OR to_char(cr.fecha_finaliza,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND " +
		   		    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"')";
					logger.info("\n\n*** la cadena2 obteniendo el where es--->>> "+where);
				}
			}			
		}
			
		//*********************************************************************************************************************************
		//se acomoda el filtro para institucion solicita.
		if (UtilidadCadena.noEsVacio(criterios.get("institucionSolicita")+"") && !(criterios.get("institucionSolicita")+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			where+=" AND r.institucion_sirc_solicita='"+criterios.get("institucionSolicita")+"'";
		}

		return where;
	}
	
	/**
	 * Método encargado de obtener la consulta sin los estados genéricos
	 * @param connection
	 * @param criterios
	 * @return cadena
	 */
	
	public static String obtenerConsulta (Connection connection, HashMap criterios)
	{
		logger.info("\n Entre a obtenerConsulta -->"+criterios);
		String cadena0 = strConsultaRefenciaExterna;
		String cadena1 = strConsultaRefenciaExternaR;
		String cadena2 = strConsultaRefenciaExternaCR;
		String agrupa = groupby;
		
		cadena1+=obtenerWhere(criterios,1)+" ";
		cadena2+=obtenerWhere(criterios,2)+")) ";
		cadena0+=" FROM "+cadena1+" UNION "+cadena2+" t "+agrupa;
		
		return cadena0;
	}
	
	/**
	 * Método encargado de obtener la consulta con todos los estados
	 * @param connection
	 * @param criterios
	 * @return cadena
	 */
	
	public static String obtenerConsultaConEstado (Connection connection, HashMap criterios)
	{
		String cadena3 = "";
		logger.info("\n Entre a consultaReporteReferenciaExternaConEstado -->"+criterios);
		if ((criterios.get("estadoReferencia")+"").equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
		{
			logger.info("*** Entré al estado FIN de la cadena con estado tabla contrareferencia !!!--->>> "+criterios.get("estadoReferencia")+"");
			cadena3 = ConsultaRefenciaExternaContra;
		}
		else
		{
			logger.info("*** Entré al estado ADM, ANU o TRA de la cadena con estado tabla referencia !!!--->>> "+criterios.get("estadoReferencia")+"");
			cadena3 = ConsultaRefenciaExterna;
		}
		cadena3+=obtenerWhere(criterios,1)+" ";
		logger.info("\n *** Cadena Con Estado --> \n"+cadena3);
		
		return cadena3;
	}
}