package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cargos.DTOCalculoTarifaServicioArticulo;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.facturacion.ConsultarContratosEntidadesSubcontratadas;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class SqlBaseAutorizacionesEntidadesSubcontratadasDao {

	private static Logger logger = Logger.getLogger(SqlBaseAutorizacionesEntidadesSubcontratadasDao.class);
	
	/**
	 * Cadena Sql para consultar las Solicitudes pendientes por autorizar
	 */
 private static String consultaSolicitudesPendientesStr= "SELECT * FROM ( " +
				        "SELECT sol.numero_solicitud AS numerosolicitud, " +
				        "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
				        "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
						"sol.centro_costo_solicitado AS codcentrocosto, " +
						"getNomCentroCosto(sol.centro_costo_solicitado) AS nombrecentrocosto, " +
						"sol.tipo AS tiposolicitud, " +
						"cencos.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
						"sol.estado_historia_clinica AS estodohclinica, " +
						"sol.cuenta AS cuenta, " +
						"sol.codigo_medico AS codprofesional, " +
						"getNombrePersona(sol.codigo_medico) AS nombreprofesional, " +
						"cu.codigo_paciente AS codpaciente,  " +
						"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
						"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
						"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
						"cu.tipo_paciente AS codtipopaciente, " +
						"getNombreTipoPaciente(cu.tipo_paciente) AS nombretipopaciente, " +
						"cu.via_ingreso AS codviaingreso, " +
						"getNombreViaIngreso(cu.via_ingreso) AS nomviaingreso, " + 
						"ing.consecutivo AS ingreso, " +
						"ing.centro_atencion  AS centroatencion , " +
						"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  " +
						"solproced.codigo_servicio_solicitado AS codservicio, " +
						"getnombreservicio(solproced.codigo_servicio_solicitado, " +ConstantesBD.separadorSplit+" ) AS nombreservicio, " +
						"getcodigocups(solproced.codigo_servicio_solicitado, " +ConstantesBD.codigoTarifarioCups+" ) AS codigocups, " +
						"getcodigopropservicio2(solproced.codigo_servicio_solicitado, "+ConstantesBD.separadorSplit+") AS codservcodmanestandar, " +
						"-1 AS codigoarticuart, "+
						"'' as descarticuloart, "+
						"'' AS unidadmedidaart, "+
						"'' AS dosisart, "+
						"-1 AS frecuart, "+
						"'' AS tipofrecart, "+
						"'' AS viaart, "+
						"-1 AS duracionart, "+
						"-1 AS cantidadart, "+
						"'' as esmediart,"+
						"'' as nombrenaturaleza,"+
						"'' AS obserarticuart " +						
						"FROM solicitudes sol " + 
						"INNER JOIN sol_procedimientos solproced ON (solproced.numero_solicitud = sol.numero_solicitud) " +
						"INNER JOIN cuentas cu ON (cu.id = sol.cuenta) " +
						"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						"INNER JOIN centros_costo cencos ON (sol.centro_costo_solicitado = cencos.codigo ) " +						
						"WHERE  cu.codigo_paciente = ? "  +
						" " +
						"UNION " +
						" " +
						"SELECT sol.numero_solicitud AS numerosolicitud, " +
				        "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
				        "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
						"sol.centro_costo_solicitado AS codcentrocosto, " +
						"getNomCentroCosto(sol.centro_costo_solicitado) AS nombrecentrocosto, " +
						"sol.tipo AS tiposolicitud, " +
						"cencos.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
						"sol.estado_historia_clinica AS estodohclinica, " +
						"sol.cuenta AS cuenta, " +
						"sol.codigo_medico AS codprofesional, " +
						"getNombrePersona(sol.codigo_medico) AS nombreprofesional, " +
						"cu.codigo_paciente AS codpaciente,  " +
						"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
						"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
						"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
						"cu.tipo_paciente AS codtipopaciente, " +
						"getNombreTipoPaciente(cu.tipo_paciente) AS nombretipopaciente, " +
						"cu.via_ingreso AS codviaingreso, " +
						"getNombreViaIngreso(cu.via_ingreso) AS nomviaingreso, " + 
						"ing.consecutivo AS ingreso, " +
						"ing.centro_atencion  AS centroatencion ," +
						"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  "  +
						"solinter.codigo_servicio_solicitado AS codservicio, " +
						"getnombreservicio(solinter.codigo_servicio_solicitado, " +ConstantesBD.separadorSplit+" ) AS nombreservicio, " +
						"getcodigocups(solinter.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+" ) AS codigocups, " +
						"getcodigopropservicio2(solinter.codigo_servicio_solicitado, "+ConstantesBD.separadorSplit+") AS codservcodmanestandar, " +
						"-1 AS codigoarticuart, "+
						"'' as descarticuloart, "+
						"'' AS unidadmedidaart, "+
						"'' AS dosisart, "+
						"-1 AS frecuart, "+
						"'' AS tipofrecart, "+
						"'' AS viaart, "+
						"-1 AS duracionart, "+
						"-1 AS cantidadart, "+
						"'' as esmediart,"+
						"'' as nombrenaturaleza,"+
						"'' AS obserarticuart " +						
						"FROM solicitudes sol " + 
						"INNER JOIN solicitudes_consulta solinter ON (solinter.numero_solicitud = sol.numero_solicitud) " +  //SE CAMBIA POR LEFT
						"INNER JOIN ordenes_amb_solicitudes ord ON (ord.numero_solicitud = sol.numero_solicitud) " +
						"INNER JOIN cuentas cu ON (cu.id = sol.cuenta) " +
						"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						"INNER JOIN centros_costo cencos ON (sol.centro_costo_solicitado = cencos.codigo  ) " +						
						"WHERE  cu.codigo_paciente = ? " +
						" "+
						"UNION " +
						" " +
						"SELECT sol.numero_solicitud AS numerosolicitud, " +
				        "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
				        "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
						"sol.centro_costo_solicitado AS codcentrocosto, " +
						"getNomCentroCosto(sol.centro_costo_solicitado) AS nombrecentrocosto, " +
						"sol.tipo AS tiposolicitud, " +
						"cencos.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
						"sol.estado_historia_clinica AS estodohclinica, " +
						"sol.cuenta AS cuenta, " +
						"sol.codigo_medico AS codprofesional, " +
						"getNombrePersona(sol.codigo_medico) AS nombreprofesional, " +
						"cu.codigo_paciente AS codpaciente,  " +
						"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
						"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
						"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
						"cu.tipo_paciente AS codtipopaciente, " +
						"getNombreTipoPaciente(cu.tipo_paciente) AS nombretipopaciente, " +
						"cu.via_ingreso AS codviaingreso, " +
						"getNombreViaIngreso(cu.via_ingreso) AS nomviaingreso, " + 
						"ing.consecutivo AS ingreso, " +
						"ing.centro_atencion  AS centroatencion ," +
						"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  "  +
						"-1 AS codservicio, " +
						"'' AS nombreservicio, " +
						"'' AS codigocups, " +
						"'' AS codservcodmanestandar, " +
						"ds.articulo AS codigoarticuart, "+
						"arti.descripcion as descarticuloart, "+
						"unid.nombre AS unidadmedidaart, "+
						"ds.dosis AS dosisart, "+
						"ds.frecuencia AS frecuart, "+
						"ds.tipo_frecuencia AS tipofrecart, "+
						"ds.via AS viaart, "+
						"ds.dias_tratamiento AS duracionart, "+
						"ds.cantidad AS cantidadart, "+
						"na.es_medicamento as esmediart,"+
						"na.nombre as nombrenaturaleza,"+
						"ds.observaciones AS obserarticuart " +
						//------------------------------FIN
						"FROM solicitudes sol " +
						"INNER JOIN solicitudes_medicamentos sm ON (sol.numero_solicitud=sm.numero_solicitud) " +
						"INNER JOIN detalle_solicitudes ds ON (ds.numero_solicitud=sm.numero_solicitud) " +
						"INNER JOIN cuentas cu ON (cu.id = sol.cuenta)  " +
						"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						"INNER JOIN centros_costo cencos ON (sol.centro_costo_solicitado = cencos.codigo  )" +
						"INNER JOIN articulo arti ON (arti.codigo=ds.articulo) "+
						"INNER JOIN unidad_medida unid ON (unid.acronimo=arti.unidad_medida) " +
						"INNER JOIN naturaleza_articulo na ON (na.acronimo=arti.naturaleza AND na.institucion=arti.institucion) " +
						"WHERE  cu.codigo_paciente = ? " +
						"AND sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+
						" ) tabl " +
						
						"WHERE tabl.tipoentidadejecuta IN ('"+ConstantesIntegridadDominio.acronimoExterna+"','"+ConstantesIntegridadDominio.acronimoAmbos+"') " +
						"AND  tabl.estodohclinica  IN ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+ ") " +
						"AND tabl.numerosolicitud NOT IN " +
							
							// PermitirAutorizarDiferenteDeSolicitudes
							"(SELECT aess.numero_solicitud FROM manejopaciente.autorizaciones_entidades_sub a " +
							"INNER JOIN ordenes.auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = a.consecutivo)" +
							"WHERE aess.numero_solicitud = tabl.numerosolicitud AND a.estado <> '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
						"ORDER BY tabl.fechasolicitud DESC ";
 
 
 
 /**
	 * Cadena Sql para consultar las Solicitudes pendientes por autorizar
	 */
private static String consultaSolicitudesPendientesRangoStr= "SELECT * FROM ( " +
				        "SELECT sol.numero_solicitud AS numerosolicitud, " +
				        "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
				        "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
						"sol.centro_costo_solicitado AS codcentrocosto, " +
						"getNomCentroCosto(sol.centro_costo_solicitado) AS nombrecentrocosto, " +
						"sol.tipo AS tiposolicitud, " +
						"cencos.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
						"sol.estado_historia_clinica AS estodohclinica, " +
						"sol.cuenta AS cuenta, " +
						"sol.codigo_medico AS codprofesional, " +
						"getNombrePersona(sol.codigo_medico) AS nombreprofesional, " +
						"cu.codigo_paciente AS codpaciente,  " +
						"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
						"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
						"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
						"cu.tipo_paciente AS codtipopaciente, " +
						"getNombreTipoPaciente(cu.tipo_paciente) AS nombretipopaciente, " +
						"cu.via_ingreso AS codviaingreso, " +
						"getNombreViaIngreso(cu.via_ingreso) AS nomviaingreso, " + 
						"ing.consecutivo AS ingreso, " +
						"ing.centro_atencion  AS centroatencion , " +
						"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  " +
						"solproced.codigo_servicio_solicitado AS codservicio, " +
						"getnombreservicio(solproced.codigo_servicio_solicitado, " +ConstantesBD.separadorSplit+" ) AS nombreservicio, " +
						"getcodigocups(solproced.codigo_servicio_solicitado, " +ConstantesBD.codigoTarifarioCups+" ) AS codigocups, " +
						"getcodigopropservicio2(solproced.codigo_servicio_solicitado, "+ConstantesBD.separadorSplit+") AS codservcodmanestandar, " +
						"-1 AS codigoarticuart, "+
						"'' as descarticuloart, "+
						"'' AS unidadmedidaart, "+
						"'' AS dosisart, "+
						"-1 AS frecuart, "+
						"'' AS tipofrecart, "+
						"'' AS viaart, "+
						"-1 AS duracionart, "+
						"-1 AS cantidadart, "+
						"'' as esmediart,"+
						"'' as nombrenaturaleza,"+
						"'' AS obserarticuart " +
						"FROM solicitudes sol " + 
						"INNER JOIN sol_procedimientos solproced ON (solproced.numero_solicitud = sol.numero_solicitud) " +
						"INNER JOIN cuentas cu ON (cu.id = sol.cuenta) " +
						"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						"INNER JOIN centros_costo cencos ON (sol.centro_costo_solicitado = cencos.codigo ) " +
						""+ConstantesBD.separadorSplitComplejo+"1 "+
						""+ConstantesBD.separadorSplitComplejo+"2 "+
						"WHERE  1=1 "  +
						" " +
						"UNION " +
						" " +
						"SELECT sol.numero_solicitud AS numerosolicitud, " +
				        "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
				        "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
						"sol.centro_costo_solicitado AS codcentrocosto, " +
						"getNomCentroCosto(sol.centro_costo_solicitado) AS nombrecentrocosto, " +
						"sol.tipo AS tiposolicitud, " +
						"cencos.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
						"sol.estado_historia_clinica AS estodohclinica, " +
						"sol.cuenta AS cuenta, " +
						"sol.codigo_medico AS codprofesional, " +
						"getNombrePersona(sol.codigo_medico) AS nombreprofesional, " +
						"cu.codigo_paciente AS codpaciente,  " +
						"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
						"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
						"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
						"cu.tipo_paciente AS codtipopaciente, " +
						"getNombreTipoPaciente(cu.tipo_paciente) AS nombretipopaciente, " +
						"cu.via_ingreso AS codviaingreso, " +
						"getNombreViaIngreso(cu.via_ingreso) AS nomviaingreso, " + 
						"ing.consecutivo AS ingreso, " +
						"ing.centro_atencion  AS centroatencion ," +
						"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  "  +
						"solinter.codigo_servicio_solicitado AS codservicio, " +
						"getnombreservicio(solinter.codigo_servicio_solicitado, " +ConstantesBD.separadorSplit+" ) AS nombreservicio, " +
						"getcodigocups(solinter.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+" ) AS codigocups, " +
						"getcodigopropservicio2(solinter.codigo_servicio_solicitado, "+ConstantesBD.separadorSplit+") AS codservcodmanestandar, " +
						"-1 AS codigoarticuart, "+
						"'' as descarticuloart, "+
						"'' AS unidadmedidaart, "+
						"'' AS dosisart, "+
						"-1 AS frecuart, "+
						"'' AS tipofrecart, "+
						"'' AS viaart, "+
						"-1 AS duracionart, "+
						"-1 AS cantidadart, "+
						"'' as esmediart,"+
						"'' as nombrenaturaleza,"+
						"'' AS obserarticuart " +
						"FROM solicitudes sol " + 
						"INNER JOIN solicitudes_consulta solinter ON (solinter.numero_solicitud = sol.numero_solicitud) " +
						"INNER JOIN ordenes_amb_solicitudes ord ON (ord.numero_solicitud = sol.numero_solicitud) " +
						"INNER JOIN cuentas cu ON (cu.id = sol.cuenta) " +
						"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						"INNER JOIN centros_costo cencos ON (sol.centro_costo_solicitado = cencos.codigo  ) " +
						""+ConstantesBD.separadorSplitComplejo+"1 "+
						""+ConstantesBD.separadorSplitComplejo+"2 "+
						"WHERE  1=1 " +
						" " +////////////////////////////////////////
						"UNION " +
						" " +
						"SELECT sol.numero_solicitud AS numerosolicitud, " +
				        "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
				        "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
						"sol.centro_costo_solicitado AS codcentrocosto, " +
						"getNomCentroCosto(sol.centro_costo_solicitado) AS nombrecentrocosto, " +
						"sol.tipo AS tiposolicitud, " +
						"cencos.tipo_entidad_ejecuta AS tipoentidadejecuta, " +
						"sol.estado_historia_clinica AS estodohclinica, " +
						"sol.cuenta AS cuenta, " +
						"sol.codigo_medico AS codprofesional, " +
						"getNombrePersona(sol.codigo_medico) AS nombreprofesional, " +
						"cu.codigo_paciente AS codpaciente,  " +
						"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
						"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
						"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
						"cu.tipo_paciente AS codtipopaciente, " +
						"getNombreTipoPaciente(cu.tipo_paciente) AS nombretipopaciente, " +
						"cu.via_ingreso AS codviaingreso, " +
						"getNombreViaIngreso(cu.via_ingreso) AS nomviaingreso, " + 
						"ing.consecutivo AS ingreso, " +
						"ing.centro_atencion  AS centroatencion ," +
						"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  "  +
						"-1 AS codservicio, " +
						"'' AS nombreservicio, " +
						"'' AS codigocups, " +
						"'' AS codservcodmanestandar, " +	
						"ds.articulo AS codigoarticuart, "+
						"arti.descripcion as descarticuloart, "+
						"unid.nombre AS unidadmedidaart, "+
						"ds.dosis AS dosisart, "+
						"ds.frecuencia AS frecuart, "+
						"ds.tipo_frecuencia AS tipofrecart, "+
						"ds.via AS viaart, "+
						"ds.dias_tratamiento AS duracionart, "+
						"ds.cantidad AS cantidadart, "+
						"na.es_medicamento as esmediart,"+
						"na.nombre as nombrenaturaleza,"+
						"ds.observaciones AS obserarticuart " +
						"FROM solicitudes sol " +
						"INNER JOIN solicitudes_medicamentos sm ON (sol.numero_solicitud=sm.numero_solicitud) " +
						"INNER JOIN detalle_solicitudes ds ON (ds.numero_solicitud=sm.numero_solicitud) " +
						"INNER JOIN cuentas cu ON (cu.id = sol.cuenta)  " +
						"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						"INNER JOIN centros_costo cencos ON (sol.centro_costo_solicitado = cencos.codigo  )" +
						"INNER JOIN articulo arti ON (arti.codigo=ds.articulo) "+
						
						"INNER JOIN unidad_medida unid ON (unid.acronimo=arti.unidad_medida) " +
												
						"INNER JOIN naturaleza_articulo na ON (na.acronimo=arti.naturaleza AND na.institucion=arti.institucion) " +
						"WHERE sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+
						") tabl " +
						"WHERE tabl.tipoentidadejecuta IN ('"+ConstantesIntegridadDominio.acronimoExterna+"','"+ConstantesIntegridadDominio.acronimoAmbos+"') " +
						"AND  tabl.estodohclinica  IN ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+ ") " +
						"AND tabl.numerosolicitud NOT IN (" +
							
						//PermitirAutorizarDiferenteDeSolicitudes
						"SELECT aess.numero_solicitud FROM autorizaciones_entidades_sub a " +
						" INNER JOIN auto_entsub_solicitudes aess" +
						" ON aess.autorizacion_ent_sub = a.consecutivo " +
						" WHERE aess.numero_solicitud = tabl.numerosolicitud AND a.estado <> '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
						 /*se elimina linea segun MT 4975*/
						" ORDER BY tabl.fechasolicitud DESC ";

 
 /**
  * Cadena Sql para realizar la consulta del Convenio y el Contrato de una solicitud pendiente por autorizar
  */
	 private static String consultaConvenioContratoIngreso=" SELECT " +
	 						"detcar.convenio AS codconvenio, " +
	 						"getNombreConvenio(detcar.convenio) AS nombreconvenio, " +
	 						"getNombreTipoContrato(con.tipo_contrato) AS tipocontrato, " +
	 						"subcuent.naturaleza_paciente AS codnaturalezapaciente, " +
	 						"getNomNatPacientes(subcuent.naturaleza_paciente) AS nombrenaturalezapaciente, " +
	 						"subcuent.nro_prioridad AS prioridad " +
	 						"FROM sub_cuentas subcuent " +
	 						"INNER JOIN det_cargos detcar ON (detcar.sub_cuenta = subcuent.sub_cuenta) "+
	                        "INNER JOIN convenios con ON (con.codigo = detcar.convenio ) " +
	                        "WHERE detcar.solicitud = ? " +
	                        
	                        // Filtro agregado seg�n Anexo 785 - Cambio 1.6	   
	                        // Se agrega OR con.capitacion_subcontratada IS NULL por tarea 30752 - Que genera cambio en el anexo 785
	                        "AND (con.capitacion_subcontratada = '"+ConstantesBD.acronimoNo+"' OR con.capitacion_subcontratada IS NULL) " +                        
	                        "ORDER BY subcuent.nro_prioridad  ASC";  
 
 
 /**
  * Cadena Sql que realiza la consulta del nivel de atenci�n de Servicio 
  */
 private static String consultaNivelServicioStr = "SELECT " +
 					   "ser.codigo AS codigoservicio, " +
 					   "nivser.descripcion AS nombrenivelservicio " +
 					   "FROM servicios ser " +
 					   "INNER JOIN nivel_atencion nivser ON (nivser.consecutivo = ser.nivel) " +
 					   "WHERE ser.codigo = ? ";
 
 /**
  * Cadena Sql que realiza la consulta de los Centros de Costo activos y cuyo tipo entidada que ejecuta es Externa o Ambos
  */
 private static String consultaCentrosCostoRango="SELECT " +
 					   "codigo AS codigo, " +
 					   "nombre AS nombrecentrocosto  " +
 					   "FROM administracion.centros_costo " +
 					   "WHERE  centro_atencion = ? AND  es_activo = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' AND tipo_entidad_ejecuta IN ('"+ConstantesIntegridadDominio.acronimoExterna+"','"+ConstantesIntegridadDominio.acronimoAmbos+"')";
 
 
 /**
  * Cadena Sql para la insercion de la Autorizacion anexo 785
  */
 private static String insertarAutorizacion="INSERT INTO manejopaciente.autorizaciones_entidades_sub " +
 					   "( consecutivo, " + 			//1
 					   "fecha_autorizacion, " + 	//--
 					   "hora_autorizacion, " +  	//--
 					   "entidad_autorizada_sub, " + //2
 					   //"numero_solicitud, " +       //3  
 					   "convenio, " +   			//3
 					   "fecha_vencimiento, " +		//4
 					   	//"servicio, " +				//6
 					   	//"cantidad, " +				//7
 					   "observaciones, " +			//5
 					   "tipo, " +					//6
 					   "estado, " +					//7
 					   "contabilizado, " +			//8
 					   "fecha_modificacion, " +		//--
 					   "hora_modificacion, " +		//--
 					   "usuario_modificacion, " +	//9
 					   "institucion, " +			//10
 					   "consecutivo_autorizacion, "+//11
 					   "codigo_paciente, " +		//12
 					   "anio_consecutivo, " +       //13 
 					   "contabilizado_anulacion ) " + //14
 					   "VALUES (?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,? ) ";	
 
 
 private static String insertarAutorizacionConSolicitud="INSERT INTO ordenes.auto_entsub_solicitudes " +
															  "( " +
															  	"codigo_pk, " + 
															  	"autorizacion_ent_sub, " + 
															  	"numero_solicitud ) " + 
															  "VALUES (?,?,?)";
 
 
 
 private static String consultarAutorizacion="SELECT " +
 						"a.consecutivo AS consecutivo, " +
 						"aess.numero_solicitud AS numerosolicitud  " +
 						"FROM autorizaciones_entidades_sub a " +
 						// PermitirAutorizarDiferenteDeSolicitudes
 						"INNER JOIN ordenes.auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = a.consecutivo)" +
 						"WHERE aess.numero_solicitud=?";
 
 
 private static String insertarAutorizacionEntSubArticulo="INSERT INTO manejopaciente.autorizaciones_ent_sub_articu "+
 						"(codigo_pk, " +				//1			
 						"autorizacion_ent_sub, " +		//2 						
 						"articulo, " +					//3
 						"dosis, " +						//4
 						"frecuencia, " +				//5
 						"dias_tratamiento, " +			//6
 						"estado, " +					//7
 						"via, " +						//8 						
 						"nro_dosis_total, " +			//9
 						"tipo_frecuencia) " +			//10
 						"VALUES (?,?,?,?,?,?,?,?,?,?)";			
 						
 private static String insertarAutorizacionEntSubServicio="INSERT INTO manejopaciente.autorizaciones_ent_sub_servi "+
 						"(codigo_pk, " +				//1	
 						"autorizacion_ent_sub, " +		//2
 						"servicio, " +					//3
 						"cantidad) " +					//4 						
 						"VALUES (?,?,?,?)";	
 
 /**
  * Metodo para obtener el listado de Solicitudes Autorizadad.
  * 
  * @param codigoPersona
  * @param tar
  * @return  ArrayList<DtoSolicitudesSubCuenta>
  */
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, String tar) 
	{
		
		ArrayList<DtoSolicitudesSubCuenta> arraySolicitudes=new ArrayList<DtoSolicitudesSubCuenta>();
		String cadenaConsulta = consultaSolicitudesPendientesStr;
		String cadenaConsultaConvenio=consultaConvenioContratoIngreso;
		int codigoPaciente = codigoPersona;
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoPaciente);
			ps.setInt(2,codigoPaciente);
			ps.setInt(3,codigoPaciente);
			
			logger.info("\n CADENA CONSULTA SOLICITUDES >>  cadena >> "+cadenaConsulta+"  CodigoPaciente >> "+codigoPaciente);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
				dto.setCodigo(rs.getString("numerosolicitud")==null?"":rs.getString("numerosolicitud"));
				dto.setNumeroSolicitud(rs.getString("consecutivordenes")==null?"":rs.getString("consecutivordenes"));
				dto.setFechaSolicitud(rs.getString("fechasolicitud"));
				dto.setCentroCostoEjecuta(new InfoDatosInt(rs.getInt("codcentrocosto"),rs.getString("nombrecentrocosto")==null?"":rs.getString("nombrecentrocosto").trim()));
				dto.setCodigoCups(UtilidadTexto.isEmpty(rs.getString("codigocups"))?"":rs.getString("codigocups"));
				dto.setServicio(new InfoDatosString(rs.getString("codservicio")==null?"":rs.getString("codservicio").trim(),
						rs.getString("nombreservicio")==null?"":rs.getString("nombreservicio").trim()));	
				dto.setCodServicioCodManualEstandar(rs.getString("codservcodmanestandar")==null?"":rs.getString("codservcodmanestandar"));  
				dto.setIngreso(rs.getInt("ingreso"));
				
				dto.setCodCentroAtencionIngreso(rs.getInt("centroatencion"));
				dto.setCentroAtencionIngreso(rs.getString("nombrecentroantencion")==null?"":rs.getString("nombrecentroantencion"));
				dto.setTipoEntidadEjecuta(rs.getString("tipoentidadejecuta")==null?"":rs.getString("tipoentidadejecuta"));
				
				dto.setCodigoPaciente(rs.getInt("codpaciente"));
				dto.setNombrePaciente(rs.getString("nombrepaciente")==null?"":rs.getString("nombrepaciente"));
				dto.setTipoIdPaciente(rs.getString("tipoidpaciente")==null?"":rs.getString("tipoidpaciente"));
				dto.setNumeroIdPaciente(rs.getString("identificacionpaciente")==null?"":rs.getString("identificacionpaciente"));
				dto.setCodTipoPaciente(rs.getString("codtipopaciente"));
				dto.setCodViaIngreso(rs.getInt("codviaingreso"));
				dto.setViaIngreso(rs.getString("nomviaingreso")==null?"":rs.getString("nomviaingreso"));
				dto.setProfesional(rs.getString("nombreprofesional")==null?"":rs.getString("nombreprofesional"));
				/*-------------------Articulos---------------------*/
				//dto.setCodigoArticulo(rs.getString("codigoarticuart"));
				//dto.setDescArticulo(rs.getString("descarticuloart"));
				dto.setArticulo(new InfoDatosString(rs.getString("codigoarticuart")==null?"":rs.getString("codigoarticuart").trim(),
						rs.getString("descarticuloart")==null?"":rs.getString("descarticuloart").trim()));				
				dto.setUnidadMedidaArticulo(rs.getString("unidadmedidaart"));
				dto.setDosisArticulo(rs.getString("dosisart"));
				dto.setFrecuArticulo(rs.getString("frecuart"));
				dto.setTipoFrecueArticulo(rs.getString("tipofrecart"));
				dto.setViaArticulo(rs.getString("viaart"));
				dto.setDuracionArticulo(rs.getString("duracionart"));
				dto.setNroDosisTotalArticulo(rs.getString("cantidadart"));
				dto.setEsMedicamento(rs.getString("esmediart"));
				dto.setNaturalezaArticulo(rs.getString("nombrenaturaleza"));
				dto.setObservaArticulo(rs.getString("obserarticuart"));				
				
				try
				{			
					PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
					ps2.setInt(1,Utilidades.convertirAEntero(dto.getCodigo()));
					
					logger.info("\n CADENA CONSULTA SOLICITUDES (convenio-tipoContrato) >>  cadena >> "+cadenaConsultaConvenio+" ");
					ResultSetDecorator rs2= new ResultSetDecorator(ps2.executeQuery());
					
					if(rs2.next())
					{ 
						dto.setConvenio(UtilidadTexto.isEmpty(rs2.getString("nombreconvenio"))?"":rs2.getString("nombreconvenio"));
						dto.setCodConvenio(rs2.getInt("codconvenio"));
						dto.setTipoContrato(UtilidadTexto.isEmpty(rs2.getString("tipocontrato"))?"":rs2.getString("tipocontrato"));		
					    dto.setCodNaturalezaPaciente(rs2.getInt("codnaturalezapaciente"));
					    arraySolicitudes.add(dto);
					}
					ps2.close();
					rs2.close();
				
				}
				catch (Exception e) {			
					e.printStackTrace();
					logger.info("\n error en  consulta Solicitudes Pendientes (convenio-tipoContrato) >>  cadena >> "+cadenaConsultaConvenio+" ");
				}			
				
			}
			ps.close();
			rs.close();
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Solicitudes Pendientes por Autorizar >>  cadena >> "+cadenaConsulta+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return arraySolicitudes;
	}
	
	
 
	/**
	 * 
	 * @param parametros
	 * @param tar
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametros, String tar)
	{
		
		ArrayList<DtoSolicitudesSubCuenta> arraySolicitudes=new ArrayList<DtoSolicitudesSubCuenta>();
		String cadenaConsulta = consultaSolicitudesPendientesRangoStr;
		String cadenaParametros = new String("");
		String cadenaConsultaConvenio=consultaConvenioContratoIngreso;
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);

		// Parametro Centro Atencion
		if(parametros.containsKey("centroatencion") && !parametros.get("centroatencion").toString().equals(""))
		{
			cadenaParametros = " ing.centro_atencion = "+parametros.get("centroatencion").toString()+" ";
		}
		
		// Parametro Fechas
		if(parametros.containsKey("fechaSolicitudInicial") && 
				parametros.containsKey("fechaSolicitudFinal") && 
					!parametros.get("fechaSolicitudInicial").toString().equals("") && 
						!parametros.get("fechaSolicitudFinal").toString().equals(""))
		    {
				if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
				{
					cadenaParametros += " AND  to_char(sol.fecha_solicitud,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaSolicitudInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaSolicitudFinal").toString())+"'  ";
				}
				else
				{
					cadenaParametros += " AND  sol.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaSolicitudInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaSolicitudFinal").toString())+"'  ";
				}
			
		    }
		
		if(parametros.containsKey("centrocosto") && parametros.get("centrocosto").toString().equals(""))
		  {	
		  HashMap centrosCosto = (HashMap)parametros.get("mapaCentrosCosto");
		
		  String cadenaCentrosCosto=new String("");
		  
		  if(Utilidades.convertirAEntero(centrosCosto.get("numRegistros")+"")>0)
		    {
			  for(int i=0; i<Utilidades.convertirAEntero(centrosCosto.get("numRegistros")+""); i++)
			     {
				   cadenaCentrosCosto += centrosCosto.get("codigo_"+i)+", ";																	
			     } 
			    
			    cadenaCentrosCosto +=ConstantesBD.codigoNuncaValido+"";
			    String aux[]= cadenaCentrosCosto.split(", "+ConstantesBD.codigoNuncaValido);
			    cadenaCentrosCosto = aux[0];
			    
			    cadenaParametros += " AND  sol.centro_costo_solicitado IN ("+cadenaCentrosCosto+") ";
		   }	    
	   	
		  }else
		  {
			  if(parametros.containsKey("centrocosto") && !parametros.get("centrocosto").toString().equals(""))
			  {
				  cadenaParametros += " AND  sol.centro_costo_solicitado = "+parametros.get("centrocosto")+" ";
			  }
		  }
		
		//Parametro Via Ingreso
		if(parametros.containsKey("viaingreso") && parametros.get("viaingreso").toString().equals(""))
		  {	
		  HashMap viasIngreso = (HashMap)parametros.get("mapaViasIngreso");
		  String cadenaViasIngreso=new String("");
		  
		  if(Utilidades.convertirAEntero(viasIngreso.get("numRegistros")+"")>0)
		    {
			  for(int i=0; i<Utilidades.convertirAEntero(viasIngreso.get("numRegistros")+""); i++)
			     {
				  cadenaViasIngreso += viasIngreso.get("codigo_"+i)+", ";																	
			     } 
			    
			    cadenaViasIngreso +=ConstantesBD.codigoNuncaValido+"";
			    String aux[]= cadenaViasIngreso.split(", "+ConstantesBD.codigoNuncaValido);
			    cadenaViasIngreso = aux[0];
			    
			    cadenaParametros += " AND  cu.via_ingreso IN ("+cadenaViasIngreso+") ";
		    }
		  }else
		  {
			  if(parametros.containsKey("viaingreso") && !parametros.get("viaingreso").toString().equals(""))
			  {
				  cadenaParametros += " AND cu.via_ingreso = "+parametros.get("viaingreso")+" ";
			  }
		  }
		
		//Parametro Tipo Paciente
		if(parametros.containsKey("tipoPaciente") && parametros.get("tipoPaciente").toString().equals(""))
		  {	
		  ArrayList tiposPaciente = (ArrayList)parametros.get("listaTipoPaciente");
		  String cadenaTiposPaciente=new String("");
		  
		  if(tiposPaciente.size()>0)
		    {
			  for(int i=0; i<tiposPaciente.size(); i++)
			     {
				  cadenaTiposPaciente += "'"+((HashMap)tiposPaciente.get(i)).get("tipopaciente")+"', ";																	
			     } 
			    
			    cadenaTiposPaciente +=ConstantesBD.codigoNuncaValido+"";
			    String aux[]= cadenaTiposPaciente.split(", "+ConstantesBD.codigoNuncaValido);
			    cadenaTiposPaciente = aux[0];
			    
			    cadenaParametros += " AND  cu.tipo_paciente IN ("+cadenaTiposPaciente+") ";
		    } 		
		  }else
		  {
			  if(parametros.containsKey("tipoPaciente") && !parametros.get("tipoPaciente").toString().equals(""))
			  {
				  cadenaParametros += " AND cu.tipo_paciente = '"+parametros.get("tipoPaciente")+"' ";
			  }
		  }
		
		if(parametros.containsKey("convenio") && !parametros.get("convenio").toString().equals("")){
			
			cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplitComplejo+"1", "INNER JOIN det_cargos detcar ON (detcar.solicitud = sol.numero_solicitud)");
			cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplitComplejo+"2", "INNER JOIN sub_cuentas subcuent ON (subcuent.sub_cuenta = detcar.sub_cuenta  AND subcuent.nro_prioridad = 1 AND  subcuent.convenio = "+parametros.get("convenio").toString()+")");
			
		}else
		{
			if(parametros.containsKey("convenio") && parametros.get("convenio").toString().equals(""))
			{
				cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplitComplejo+"1", "");
				cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplitComplejo+"2", "");
			}
			
		}
		
		cadenaConsulta=cadenaConsulta.replace("1=1",cadenaParametros);
		
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			logger.info("\n CADENA CONSULTA SOLICITUDES >>  cadena >> "+cadenaConsulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
				dto.setCodigo(rs.getString("numerosolicitud")==null?"":rs.getString("numerosolicitud"));
				dto.setNumeroSolicitud(rs.getString("consecutivordenes")==null?"":rs.getString("consecutivordenes"));
				dto.setFechaSolicitud(rs.getString("fechasolicitud"));
				dto.setCentroCostoEjecuta(new InfoDatosInt(rs.getInt("codcentrocosto"),rs.getString("nombrecentrocosto")==null?"":rs.getString("nombrecentrocosto").trim()));
				dto.setCodigoCups(UtilidadTexto.isEmpty(rs.getString("codigocups"))?"":rs.getString("codigocups"));
				dto.setServicio(new InfoDatosString(rs.getString("codservicio")==null?"":rs.getString("codservicio").trim(),rs.getString("nombreservicio")==null?"":rs.getString("nombreservicio").trim()));	
				dto.setCodServicioCodManualEstandar(rs.getString("codservcodmanestandar")==null?"":rs.getString("codservcodmanestandar"));  
				dto.setIngreso(rs.getInt("ingreso"));
				
				dto.setCodCentroAtencionIngreso(rs.getInt("centroatencion"));
				dto.setCentroAtencionIngreso(rs.getString("nombrecentroantencion")==null?"":rs.getString("nombrecentroantencion"));
				dto.setTipoEntidadEjecuta(rs.getString("tipoentidadejecuta")==null?"":rs.getString("tipoentidadejecuta"));
				
				dto.setCodigoPaciente(rs.getInt("codpaciente"));
				dto.setNombrePaciente(rs.getString("nombrepaciente")==null?"":rs.getString("nombrepaciente"));
				dto.setTipoIdPaciente(rs.getString("tipoidpaciente")==null?"":rs.getString("tipoidpaciente"));
				dto.setNumeroIdPaciente(rs.getString("identificacionpaciente")==null?"":rs.getString("identificacionpaciente"));
				dto.setCodTipoPaciente(rs.getString("codtipopaciente"));
				dto.setCodViaIngreso(rs.getInt("codviaingreso"));
				dto.setViaIngreso(rs.getString("nomviaingreso"));
				dto.setProfesional(rs.getString("nombreprofesional")==null?"":rs.getString("nombreprofesional"));
				
				/*-------------------Articulos---------------------*/
				//dto.setCodigoArticulo(rs.getString("codigoarticuart"));
				//dto.setDescArticulo(rs.getString("descarticuloart"));
				dto.setArticulo(new InfoDatosString(rs.getString("codigoarticuart")==null?"":rs.getString("codigoarticuart").trim(),
						rs.getString("descarticuloart")==null?"":rs.getString("descarticuloart").trim()));
				dto.setUnidadMedidaArticulo(rs.getString("unidadmedidaart"));
				dto.setDosisArticulo(rs.getString("dosisart"));
				dto.setFrecuArticulo(rs.getString("frecuart"));
				dto.setTipoFrecueArticulo(rs.getString("tipofrecart"));
				dto.setViaArticulo(rs.getString("viaart"));
				dto.setDuracionArticulo(rs.getString("duracionart"));
				dto.setNroDosisTotalArticulo(rs.getString("cantidadart"));
				dto.setEsMedicamento(rs.getString("esmediart"));
				dto.setNaturalezaArticulo(rs.getString("nombrenaturaleza"));
				dto.setObservaArticulo(rs.getString("obserarticuart"));		
				
				logger.info("\n\n>>>>> VIA INGRESO EN EL SQLBASE : NOMBRE > "+dto.getViaIngreso()+ " CODIGO > "+dto.getCodViaIngreso());
				try
				{			
					PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
					ps2.setInt(1,Utilidades.convertirAEntero(dto.getCodigo()));
					
					logger.info("\n CADENA CONSULTA SOLICITUDES (convenio-tipoContrato) >>  cadena >> "+cadenaConsultaConvenio+" ");
					ResultSetDecorator rs2= new ResultSetDecorator(ps2.executeQuery());
					
					if(rs2.next())
					{ 
						dto.setConvenio(UtilidadTexto.isEmpty(rs2.getString("nombreconvenio"))?"":rs2.getString("nombreconvenio"));
						dto.setCodConvenio(rs2.getInt("codconvenio"));
						dto.setTipoContrato(UtilidadTexto.isEmpty(rs2.getString("tipocontrato"))?"":rs2.getString("tipocontrato"));		
					    dto.setCodNaturalezaPaciente(rs2.getInt("codnaturalezapaciente"));
					    
					    arraySolicitudes.add(dto);
					}
					ps2.close();
					rs2.close();
				
				}
				catch (Exception e) {			
					e.printStackTrace();
					logger.info("\n error en  consulta Solicitudes Pendientes (convenio-tipoContrato) >>  cadena >> "+cadenaConsultaConvenio+" ");					
				}				
			}
			ps.close();
			rs.close();			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Solicitudes Pendientes por Autorizar >>  cadena >> "+cadenaConsulta+" ");			
		}		
		UtilidadBD.closeConnection(con);
		return arraySolicitudes;
	}
 
	
	public static String nivelServicio(int servicio)
	{
		String cadena=consultaNivelServicioStr;
		String nombreNivelServicio="";
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,servicio);
			
			logger.info("\n Cadena Consultar Nivel Servicio >>  cadena >> "+cadena+"  servicio : "+servicio);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
		     if(rs.next())
			   {
		    	 nombreNivelServicio=rs.getString("nombrenivelservicio"); 
		    	 UtilidadBD.closeConnection(con); 
				 return nombreNivelServicio; 
			   }	
		     rs.close();
		     ps.close();
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Nivel Servicio >>  cadena >> "+cadena+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return "";
		
	}
	
	
	
	@SuppressWarnings({"unchecked", "deprecation", "rawtypes" })
	public static HashMap ingresarAutorizacion(Connection con, HashMap parametros, ArrayList<DtoSolicitudesSubCuenta> articulosServicios)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		CargosEntidadesSubcontratadas cargosEntidadesSubCont = new  CargosEntidadesSubcontratadas();
		resultado.put("codigoAut",ConstantesBD.codigoNuncaValido+"");
		resultado.put("estadoAut","");
		resultado.put("error",errores);
				
		//Se inserta los datos generales en Autorizaciones Entidades Subcontratadas 
		String cadenaInsercion = insertarAutorizacion;
		String cadenaInsercion2 = insertarAutorizacionConSolicitud;
		int entidad = Utilidades.convertirAEntero(parametros.get("codEntidad").toString());
		int convenio = Utilidades.convertirAEntero(parametros.get("codConvenio").toString());
		int solicitud= Utilidades.convertirAEntero(parametros.get("codSolicitud").toString());
		//int servicio= Utilidades.convertirAEntero(parametros.get("codServicio").toString()); 
		int institucion=Utilidades.convertirAEntero(parametros.get("codInstitucion").toString()); 
		int codPaciente=Utilidades.convertirAEntero(parametros.get("codPaciente").toString());
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_autor_ent_sub_contr");
		int consecutivo2 = UtilidadBD.obtenerSiguienteValorSecuencia(con, "ordenes.seq_auto_entsub_solicitudes");	 
		String valorConsecutivoAuto=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAutorizacionEntiSub,Utilidades.convertirAEntero(parametros.get("codInstitucion").toString()));
		String anioConsecutivoAuto=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionEntiSub,Utilidades.convertirAEntero(parametros.get("codInstitucion").toString()),valorConsecutivoAuto);
		
		
		if(Utilidades.convertirAEntero(valorConsecutivoAuto) < 0)
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Inicializar el consecutivo [Autorizaciones Entidades Subcontratadas] ubicado en el modulo Manejo Paciente."));
			resultado.put("consecutivoError",errores);
			
			return resultado;
		}
		if(parametros.get("fechaVencimiento").toString().equals(""))
		{	
		  errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Vencimiento"));
		  resultado.put("error",errores);
		}
		if(parametros.get("codEntidad").toString().equals(""))
		{	
		  errores.add("descripcion",new ActionMessage("errors.required","La Entidad Autorizada"));
		  resultado.put("error",errores);
		}
		if(!parametros.get("fechaVencimiento").toString().equals(""))
		{
			if(!UtilidadFecha.validarFecha(parametros.get("fechaVencimiento").toString()))
			{
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Vencimiento"));
				resultado.put("error",errores);
			}	
			
		  if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaVencimiento").toString() , UtilidadFecha.getFechaActual()))
		   {
			errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," de Vencimiento"," Actual"));
			resultado.put("error",errores);
		   }
	    }
		if(entidad == ConstantesBD.codigoNuncaValido)
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No Posee Entidad SubContratada Autorizada "));
			resultado.put("error",errores);
		}
		if(!errores.isEmpty())
		{
			logger.info("\n>>>>> ENTRO AL EMPTY.........");
			resultado.put("consecutivoError", valorConsecutivoAuto);
			return resultado;
		}
		
		try {

			UtilidadTransaccion.getTransaccion().begin();

			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			String tipo=null;
			if(parametros.get("tipo")!=null)
			{
				tipo="'"+parametros.get("tipo")+"'";
			}// sino entonces guarda null

			cadenaInsercion=cadenaInsercion.replace(", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," , ", "+entidad+", "+solicitud+", "+convenio+", '"+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaVencimiento").toString()))+"'" +
					", '"+parametros.get("observaciones").toString()+"' , "+tipo+", '"+ConstantesIntegridadDominio.acronimoAutorizado+"', '"+ConstantesBD.acronimoNo +"', ");
			//", "+servicio+", 1, '"+parametros.get("observaciones").toString()+"' , "+tipo+", '"+ConstantesIntegridadDominio.acronimoAutorizado+"', '"+ConstantesBD.acronimoNo +"', ");
			cadenaInsercion=cadenaInsercion.replace(",?,?,?,?,?,?",", '"+parametros.get("usuario").toString()+"', "+institucion+", '"+valorConsecutivoAuto+"', "+codPaciente+",'"+anioConsecutivoAuto+"', '"+ConstantesBD.acronimoNo +"'"); 
			cadenaInsercion=cadenaInsercion.replace("?, CURRENT_DATE",""+consecutivo+", CURRENT_DATE" );

			logger.info("\n \n CADENA  Insertar Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenaInsercion+" ");
			logger.info("consecutivo:"+consecutivo+" solicitud:"+solicitud+" valorConsecutivoAuto:"+valorConsecutivoAuto);


			ps.setInt(1, consecutivo);
			ps.setInt(2, entidad);
			//ps.setInt(3, solicitud);
			ps.setInt(3, convenio);
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaVencimiento").toString())));
			//ps.setInt(6, servicio);
			//ps.setInt(7,1);
			ps.setString(5, parametros.get("observaciones").toString());
			ps.setString(6,(String)parametros.get("tipo"));
			ps.setString(7,ConstantesIntegridadDominio.acronimoAutorizado);
			ps.setString(8,ConstantesBD.acronimoNo);
			ps.setString(9,parametros.get("usuario").toString());
			ps.setInt(10,institucion);
			if(parametros.get("tipo").equals(ConstantesIntegridadDominio.acronimoExterna))
				ps.setString(11,valorConsecutivoAuto);
			else
				ps.setString(11,null );
			ps.setInt(12,codPaciente);
			ps.setString(13,anioConsecutivoAuto);
			ps.setString(14,ConstantesBD.acronimoNo);
			/*Se ejecuta la insercion para mandar el consecutivo a las tablas de articulos o servicios*/
			int autorizExecute=ps.executeUpdate();        

			// ordenes.auto_entsub_solicitudes
			ps2.setInt(1, consecutivo2);
			ps2.setInt(2, consecutivo);
			ps2.setInt(3, solicitud); 
			int autorizExecute2 = ps2.executeUpdate();    

			//int servicio=ConstantesBD.codigoNuncaValido;
			//int articulo=ConstantesBD.codigoNuncaValido;
			boolean guardoArtiSer=false;

			/*Va a ingresar los articulos y los servicios de acuerdo a la solicitud y datos generales de la solicitud*/
			ArrayList<DtoSolicitudesSubCuenta> artiServi= ingresarAutorizacionArticulosServicios(con,articulosServicios,String.valueOf(consecutivo));

			logger.info("tama�o de artiServi: "+artiServi.size()); 
			/*for(int i=0;i<artiServi.size();i++)
         {	if(artiServi.get(i).getArticulo().getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
			{	servicio=Utilidades.convertirAEntero(artiServi.get(i).getServicio().getCodigo());
         		guardoArtiSer=artiServi.get(i).isGuardoArticuloServicio();
			}
			else 
			{	articulo=Utilidades.convertirAEntero(artiServi.get(i).getArticulo().getCodigo());
				guardoArtiSer=artiServi.get(i).isGuardoArticuloServicio();
			}
         }*/

			ArrayList<DtoSolicitudesSubCuenta> servicios= new ArrayList<DtoSolicitudesSubCuenta>();
			ArrayList<DtoSolicitudesSubCuenta> articulos= new ArrayList<DtoSolicitudesSubCuenta>();
			for(int i=0;i<artiServi.size();i++)	{	
				if(artiServi.get(i).getArticulo().getCodigo().equals(ConstantesBD.codigoNuncaValido+"")) {	
					guardoArtiSer=artiServi.get(i).isGuardoArticuloServicio();
					servicios.add(artiServi.get(i));
				} else {	
					guardoArtiSer=artiServi.get(i).isGuardoArticuloServicio();
					articulos.add(artiServi.get(i));
				}
			}

			logger.info("se guardo!!! el articulo o servicio, entonces variable guardoArtiSer: "+guardoArtiSer);

			if(!(autorizExecute>0) && !(guardoArtiSer==true)  && !(autorizExecute2>0) )
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion de la Autorizacion"));
				resultado.put("error",errores);
				resultado.put("consecutivoError", valorConsecutivoAuto);
				UtilidadTransaccion.getTransaccion().rollback();
				ps.close();

				return resultado;
			}
			else
			{		
				UtilidadTransaccion.getTransaccion().commit();

				//***************CAMBIOS DCU 785 V1.8*****************************
				String fechaAutorizacion = UtilidadFecha.getFechaActual();
				//Se valida si la entidad subcontratada del par�metro tiene contrato vigente
				CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
				DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(parametros.get("codEntidad").toString()),fechaAutorizacion);

				double tarifa = ConstantesBD.codigoNuncaValidoDouble;

				DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
				dtoEntidadSub.setCodigoPk(entidad);				

				contratoEntidadSub.setEntidad(dtoEntidadSub);		

				//Se determina el tipo de tarifa de la entidad subcontratada
				ConsultarContratosEntidadesSubcontratadas.consultarTipoTarifaEntidadSubcontratada(con, contratoEntidadSub);

				if(!UtilidadTexto.isEmpty(contratoEntidadSub.getTipoTarifa()))
				{
					if(contratoEntidadSub.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente))
					{
						//Se calcula la tarifa por la entidad subcontratada CU 799
						if(!Utilidades.isEmpty(servicios))
						{
							for(DtoSolicitudesSubCuenta dtoServ:servicios)
							{
								cargosEntidadesSubCont.generarCargoAutorizacion(con, consecutivo+"",Utilidades.convertirAEntero(dtoServ.getServicio().getCodigo()),entidad+"",UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),(UsuarioBasico)parametros.get("usuarioTodo"),parametros.get("observaciones")+"",solicitud+"");
								resultado.put("error",cargosEntidadesSubCont.getErroresProceso());
							}
						}
						if(!Utilidades.isEmpty(articulos)){ 
							for(DtoSolicitudesSubCuenta dtoArt:articulos)
							{
								Integer almacen = Integer.parseInt(parametros.get("almacenEjecuta").toString()); 
								cargosEntidadesSubCont.generarCargoArticulo(con, almacen, Utilidades.convertirAEntero(dtoArt.getArticulo().getCodigo()), ConstantesBD.codigoNuncaValido, solicitud+"", null, UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), false, (UsuarioBasico)parametros.get("usuarioTodo"), parametros.get("observaciones")+"", consecutivo+"");
								resultado.put("error",cargosEntidadesSubCont.getErroresProceso());
							}
						}
					}
					else if(contratoEntidadSub.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
					{
						//Se calcula la tarifa por cargo modificado CU 438
						if(!Utilidades.isEmpty(artiServi))
						{
							UsuarioBasico usuario= new UsuarioBasico();
							usuario.setLoginUsuario(parametros.get("usuario").toString());
							for(DtoSolicitudesSubCuenta dtoArtServ:artiServi)
							{
								DTOCalculoTarifaServicioArticulo dtoCalculo = new DTOCalculoTarifaServicioArticulo();
								EsquemaTarifario esquema = new EsquemaTarifario();
								int codigoArticuloServicio = ConstantesBD.codigoNuncaValido;
								dtoEntidadSub.setConsecutivo(String.valueOf(dtoEntidadSub.getCodigoPk()));
								dtoCalculo.setEntidadSubcontratada(dtoEntidadSub);
								dtoCalculo.setFechaVigencia(UtilidadFecha.getFechaActual());
								if(dtoArtServ.getArticulo().getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
								{
									dtoCalculo.setEsServicio(true);
									codigoArticuloServicio = Utilidades.convertirAEntero(dtoArtServ.getServicio().getCodigo());
								}else
								{
									dtoCalculo.setEsServicio(false);
									codigoArticuloServicio = Utilidades.convertirAEntero(dtoArtServ.getArticulo().getCodigo());
								}

								dtoCalculo.setCodigoArticuloServicio(codigoArticuloServicio);

								tarifa = cargosEntidadesSubCont.calcularTarifaEntidadSubcontratada(con, dtoCalculo, esquema);

								if(dtoCalculo.isEsServicio())
								{
									cargosEntidadesSubCont.generarCargoAutorizacionConTarifaCalculada(con, 
											tarifa, 
											usuario, 
											consecutivo+"",
											contratoEntidadSub,
											dtoEntidadSub, 
											esquema, 
											UtilidadFecha.getFechaActual(), 
											UtilidadFecha.getHoraActual(), 
											parametros.get("observaciones").toString() ,
											solicitud+"");
								}
								else
								{
									cargosEntidadesSubCont.generarCargoArticuloConTarifaCalculada(con, 
											tarifa, 
											usuario, 
											Utilidades.convertirAEntero(dtoArtServ.getArticulo().getCodigo()), 
											solicitud+"", 
											esquema, 
											contratoEntidadSub, 
											dtoEntidadSub, 
											parametros.get("observaciones").toString(), 
											UtilidadFecha.getFechaActual(), 
											UtilidadFecha.getHoraActual(), 
											false, 
											ConstantesBD.codigoNuncaValido,
											consecutivo+"");
								}

								resultado.put("error",cargosEntidadesSubCont.getErroresCargo());
							}

						}
					}

				}
				//**************************************************************************	

				//cargosEntidadesSubCont.setErroresProceso(errores);
				/*if(servicio!=ConstantesBD.codigoNuncaValido)
      	{
      		cargosEntidadesSubCont.generarCargoAutorizacion(con, consecutivo+"",servicio,entidad+"",UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),(UsuarioBasico)parametros.get("usuarioTodo"),parametros.get("observaciones")+"");
      		resultado.put("error",cargosEntidadesSubCont.getErroresProceso());
      	}
      	else if(articulo!=ConstantesBD.codigoNuncaValido){ 
      		//cargosEntidadesSubCont.generarCargoAutorizacion(con, consecutivo+"",articulo,entidad+"",UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),(UsuarioBasico)parametros.get("usuarioTodo"),parametros.get("observaciones")+"");
      		// FIXME 


      		Integer almacen = Integer.parseInt(parametros.get("almacenEjecuta").toString()); 
      		cargosEntidadesSubCont.generarCargoArticulo(con, almacen, articulo, ConstantesBD.codigoNuncaValido, solicitud+"", null, UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), false, (UsuarioBasico)parametros.get("usuarioTodo"), parametros.get("observaciones")+"");
      		resultado.put("error",cargosEntidadesSubCont.getErroresProceso());
      	}*/



				if(!cargosEntidadesSubCont.getErroresProceso().isEmpty())
				{
					logger.info("\n>>>>> ENTRO AL EMPTY2.........Cargos EntidadesSubcontratadas......");
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub,Utilidades.convertirAEntero(parametros.get("codInstitucion").toString()), valorConsecutivoAuto,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);

					return resultado;
				}

				//resultado.put("codigoAut",consecutivo);
				resultado.put("codigoAut",valorConsecutivoAuto);
				resultado.put("estadoAut",ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAutorizado));
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub,Utilidades.convertirAEntero(parametros.get("codInstitucion").toString()), valorConsecutivoAuto,ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				ps.close();

				return resultado;	        	
			}   		
		}
		catch (Exception e) {			
			e.printStackTrace();
			UtilidadTransaccion.getTransaccion().rollback();
			resultado.put("consecutivoError", valorConsecutivoAuto);
			logger.info("\n \n error al  Insertar Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenaInsercion+" ");	 
		}		
		return resultado;
	}
	
	
	
	/**Guarda los articulos y los servicios que se autorizaron 
	 * 
	 * @param con
	 * @param articulosServicios
	 * @param consecutivoAutorizacionSub
	 * @return Articulos o sevicios autorizados
	 * 
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public static ArrayList<DtoSolicitudesSubCuenta> ingresarAutorizacionArticulosServicios(Connection con,
									ArrayList<DtoSolicitudesSubCuenta> articulosServicios,
									String consecutivoAutorizacionSub)
	{
		ActionErrors errores = new ActionErrors();
		ArrayList<DtoSolicitudesSubCuenta> resultado2 = new ArrayList<DtoSolicitudesSubCuenta>();
		//CargosEntidadesSubcontratadas cargosEntidadesSubCont = new  CargosEntidadesSubcontratadas();
		String numSolicitud="";
		String numConsecutivo="";
				
		//Se inserta en Autorizaciones Entidades Subcontratadas Servicios 
		String cadenaInsercionAutoServ = insertarAutorizacionEntSubServicio;
		String cadenainsercionAutoArti = insertarAutorizacionEntSubArticulo;
		String consultaConsecutivoAutoriz=consultarAutorizacion;
		boolean entroArticulos=false;		
		logger.info("\n\n La cadena de consulta de numero de solicitud en Autorizaciones: "+consultaConsecutivoAutoriz);
		logger.info("consecutivoAutorizacion: "+consecutivoAutorizacionSub);
		
		try{
			PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(consultaConsecutivoAutoriz,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionAutoServ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			PreparedStatementDecorator ps3 =  new PreparedStatementDecorator(con.prepareStatement(cadenainsercionAutoArti,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ArrayList<DtoSolicitudesSubCuenta> dtoArti=new ArrayList<DtoSolicitudesSubCuenta>();
			
			
			logger.info("tama�o de los articulosServicios: "+articulosServicios.size());
			for(int i=0;i< articulosServicios.size();i++)
				logger.info("arti: "+articulosServicios.get(i).getArticulo().getCodigo()+"   servi: "+articulosServicios.get(i).getServicio().getCodigo()); 
			
			for(int i=0;i< articulosServicios.size();i++)
			{	
				numSolicitud=articulosServicios.get(i).getCodigo();
				ps1.setInt(1,Utilidades.convertirAEntero(numSolicitud));
				ResultSetDecorator rs= new ResultSetDecorator(ps1.executeQuery());
						
				if(articulosServicios.get(i).getArticulo().getCodigo().equals(ConstantesBD.codigoNuncaValido+"")) 
				{
					logger.info("\n\n La cadena a insertar es: "+cadenaInsercionAutoServ);
					ps2.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_auto_ent_sub_servi"));
					ps2.setInt(2, Utilidades.convertirAEntero(consecutivoAutorizacionSub)); 						
					ps2.setInt(3, Utilidades.convertirAEntero(articulosServicios.get(i).getServicio().getCodigo()));
					ps2.setInt(4, 1);						
						
					if(ps2.executeUpdate()>0)
					{	articulosServicios.get(i).setGuardoArticuloServicio(true);
						resultado2.add(articulosServicios.get(i));
					}
							
				}else
				{
					logger.info("\n\n La cadena a insertar es: "+cadenainsercionAutoArti);
					if(articulosServicios.get(i).getEsMedicamento().equals(ConstantesBD.acronimoSi)) 
					{	
						ps3.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_auto_ent_sub_articulo"));
						ps3.setInt(2, Utilidades.convertirAEntero(consecutivoAutorizacionSub));																
						ps3.setInt(3, Utilidades.convertirAEntero(articulosServicios.get(i).getArticulo().getCodigo()));
						ps3.setString(4, articulosServicios.get(i).getDosisArticulo());
						ps3.setInt(5, Utilidades.convertirAEntero(articulosServicios.get(i).getFrecuArticulo()));
						ps3.setInt(6, Utilidades.convertirAEntero(articulosServicios.get(i).getDuracionArticulo()));
						ps3.setString(7, ConstantesIntegridadDominio.acronimoEstadoPendiente);//Tarea 31784: Se deja pendiente ya que el articulo no ha sido despachado
						ps3.setString(8, articulosServicios.get(i).getViaArticulo());
						ps3.setInt(9, Utilidades.convertirAEntero(articulosServicios.get(i).getNroDosisTotalArticulo()));
						ps3.setString(10, articulosServicios.get(i).getTipoFrecueArticulo());							
					}else
					{
						ps3.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_auto_ent_sub_articulo"));
						ps3.setInt(2, Utilidades.convertirAEntero(consecutivoAutorizacionSub));																
						ps3.setInt(3, Utilidades.convertirAEntero(articulosServicios.get(i).getArticulo().getCodigo()));
						ps3.setString(4,null);
						ps3.setNull(5, Types.INTEGER);
						ps3.setNull(6, Types.INTEGER);
						ps3.setString(7, ConstantesIntegridadDominio.acronimoEstadoPendiente);//Tarea 31784: Se deja pendiente ya que el articulo no ha sido despachado
						ps3.setString(8,null);
						ps3.setInt(9, Utilidades.convertirAEntero(articulosServicios.get(i).getNroDosisTotalArticulo()));
						ps3.setString(10, null);							
					}	
					logger.info("\n\n La cadena a insertar es: "+cadenainsercionAutoArti);
					if(ps3.executeUpdate()>0)
					{	articulosServicios.get(i).setGuardoArticuloServicio(true);
						resultado2.add(articulosServicios.get(i));
					}
					entroArticulos=true;
				}								
			}
			return resultado2;
		
		}catch (Exception e) {			
			 e.printStackTrace();
			 if(entroArticulos==true)
				 logger.info("\n \n error al  Insertar los articulos para la Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenainsercionAutoArti+" ");
			 else
				 logger.info("\n \n error al  Insertar el servicio para la Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenaInsercionAutoServ+" ");
		}		
		return null;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap centroCostoRango(int centroAtencion)
	{
		String cadena=consultaCentrosCostoRango;
		HashMap centrosCosto= new HashMap();
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
            ps.setInt(1, centroAtencion);
			logger.info("\n Cadena Consultar Centros Costo >>  cadena >> "+cadena + " >>>> Centro Atencion:>>  "+centroAtencion);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			centrosCosto = UtilidadBD.cargarValueObject(rs, true, false);
		    rs.close();
		    ps.close();
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Centros Costo >>  cadena >> "+cadena+" >>>> Centro Atencion:>>  "+centroAtencion);
			
		}
		UtilidadBD.closeConnection(con);
		return centrosCosto;
		
	}
	
	
 
}
