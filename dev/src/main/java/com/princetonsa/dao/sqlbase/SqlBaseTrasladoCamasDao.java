
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;




/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *
 * Clase para las transacciones de la Funcionalidad de Traslado de camas
 */
public class SqlBaseTrasladoCamasDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTrasladoCamasDao.class);
	
	/**
	 * Statement para consultar la cama actual de un paciente
	 */
	private final static String consultarCamaActualStr=" SELECT  " +
													   " c.codigo AS codigo, "+	
													   " c.habitacion as habitacion, " +
													   " getnomhabitacioncama(c.codigo) AS nombre_habitacion, " +
													   " getnombrepisocama(c.codigo) AS piso, " +
													   " getnomtipohabitacioncama(c.codigo) AS tipo_habitacion, "+
													   " c.numero_cama as cama, " +
													   " c.descripcion as descripcionCama, " +
													   " tu.nombre as tipoUsuario, " +
													   " cc.nombre as centroCosto, " +
													   " CASE WHEN c.es_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUci," +
													   " '' as fecha_traslado," +
													   " '' as hora_traslado, " +
													   " CASE WHEN c.es_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' (' || getnombretipomonitoreo(sc.tipo_monitoreo) || ')' ELSE '' END AS tipo_monitoreo " +
													   " FROM camas1 c " +
													   " INNER JOIN tipos_usuario_cama tu ON(c.tipo_usuario_cama=tu.codigo) " +
													   " INNER JOIN centros_costo cc ON(c.centro_costo=cc.codigo) " +
													   " INNER JOIN admisiones_hospi ah ON(c.codigo=ah.cama) " +
													   " INNER JOIN cuentas cu ON(ah.cuenta=cu.id) " +
													   " INNER JOIN ingresos i ON(i.id=cu.id_ingreso) " +
													   " LEFT OUTER JOIN servicios_cama sc ON(sc.codigo_cama=c.codigo) " +
													   " WHERE cu.codigo_paciente=? AND i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'";
	
	
	
/*	private static final String consultarCamaActualEnTrasladoCamaStr=" SELECT " +
																	" cam1.codigo As codigo, "+
																	" (to_char(tc.fecha_asignacion,'"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora_asignacion, 'HH24:MI'))) as fechaHoraTraslado, "+
																	" cam1.habitacion as habitacion, " +
																	" getnomhabitacioncama(cam1.codigo) AS nombre_habitacion, " +
																	" getnombrepisocama(cam1.codigo) AS piso, " +
																	" getnomtipohabitacioncama(cam1.codigo) AS tipo_habitacion, "+
																	" cam1.numero_cama as cama, "+
																	" cam1.descripcion as descripcionCama, "+
																	" tu.nombre as tipoUsuario, "+
																	" cc.nombre as centroCosto, "+
																	" CASE WHEN cam1.es_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUci,   "+
																	" CASE WHEN tc.fecha_asignacion IS NULL THEN '' ELSE to_char(tc.fecha_asignacion,'DD/MM/YYYY') END as fecha_traslado, "+
																	" CASE WHEN tc.hora_asignacion IS NULL THEN '' ELSE tc.hora_asignacion || '' END as hora_traslado, "+
																	" CASE WHEN cam1.es_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' (' || getnombretipomonitoreo(sc.tipo_monitoreo) || ')' ELSE '' END AS tipo_monitoreo " +
																	" FROM traslado_cama tc "+
																	" INNER JOIN cuentas c ON(c.id=tc.cuenta) " +
																	" INNER JOIN ingresos i ON(i.id=c.id_ingreso)  " +
																	" INNER JOIN  camas1 cam1 ON(cam1.codigo=tc.codigo_nueva_cama)   "+  
																	" INNER JOIN tipos_usuario_cama tu ON(cam1.tipo_usuario_cama=tu.codigo)   "+  
																	" INNER JOIN centros_costo cc ON(cam1.centro_costo=cc.codigo)  "+
																	" LEFT OUTER JOIN servicios_cama sc ON(sc.codigo_cama=cam1.codigo) " +
																	" WHERE tc.codigo_paciente=?  "+
																	" and tc.fecha_finalizacion is null and tc.hora_finalizacion is null " +
																	" and i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'";
	*/	
/* SELECT tc.nueva_habitacion as habitacion,  tc.nueva_cama as cama, c.descripcion as descripcionCama,  tu.nombre as tipoUsuario, cc.nombre as centroCosto, CASE WHEN c.es_uci=+ValoresPorDefecto.getValorTrueParaConsultas()+ THEN 'SI' ELSE 'NO' END as esUci  FROM traslado_cama tc INNER JOIN  camas1 c ON(upper(c.numero_cama)=upper(tc.nueva_cama)) INNER JOIN tipos_usuario_cama tu ON(c.tipo_usuario_cama=tu.codigo) INNER JOIN centros_costo cc ON(c.centro_costo=cc.codigo) WHERE tc.codigo_paciente=? ORDER BY tc.oid desc "+ValoresPorDefecto.getValorLimit1()+" 1 ;*/
	
	/**
	 * Statement para cargar los traslados segun el centro de costo que se seleccion. Por defecto carga todos.
	 */
	private final static String cargarTrasladosAreaStr=" SELECT " +
		   " getnomhabitacioncama(cam1.codigo) as habitacion,    " +
		   " cam1.numero_cama as cama,    " +
		   " cam1.descripcion as descripcionCama,    " +
		   " (per.primer_apellido||' '||per.segundo_apellido||' '||per.primer_nombre||' '||per.segundo_nombre) as paciente,      " +
		   " (per.tipo_identificacion||' '||per.numero_identificacion) as tipoId,  " +
		   " (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp +"')) as fechaIngreso,     " +
		   " cc.nombre as responsable, getnomcentrocosto(cam1.centro_costo)as  centroCosto,"   +
		   " cue.codigo_paciente as codigoPaciente," +
		   " getnombrepisocama(cam1.codigo) AS piso, " +
		   " getnomtipohabitacioncama(cam1.codigo) AS tipo_habitacion " +
		   " FROM camas1 cam1  " +
		   " INNER JOIN traslado_cama tc ON(cam1.codigo=tc.codigo_nueva_cama) " +
		   " INNER JOIN cuentas cue ON(tc.cuenta=cue.id AND cue.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"))  " +
		   " INNER JOIN ingresos i ON(i.id=cue.id_ingreso AND i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"')" +
		   " INNER JOIN admisiones_hospi admH ON(cue.id=admH.cuenta)   " +
		   " INNER JOIN pacientes pac ON(cue.codigo_paciente=pac.codigo_paciente)" +
		   " INNER  JOIN personas per ON(pac.codigo_paciente=per.codigo) " +
		   " INNER JOIN convenios cc ON(tc.convenio=cc.codigo)   " +
		   " WHERE tc.fecha_finalizacion is null and tc.hora_finalizacion is null ";
		
/* SELECT c.habitacion as habitacion, c.numero_cama as cama,  c.descripcion as descripcionCama, (per.primer_apellido||' '||per.segundo_apellido||' '||per.primer_nombre||' '||per.segundo_nombre) as paciente, (per.tipo_identificacion||' '||per.numero_identificacion) as tipoId,  (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp +"')) as fechaIngreso, cc.nombre as responsable,  cc2.nombre as centroCosto, " cue.codigo_paciente as codigoPaciente 
 FROM camas1 c " LEFT OUTER JOIN admisiones_hospi admH ON(c.codigo=admH.cama) INNER JOIN cuentas cue ON(admH.cuenta=cue.id) INNER JOIN pacientes pac ON(cue.codigo_paciente=pac.codigo_paciente) INNER JOIN personas per ON(pac.codigo_paciente=per.codigo) INNER JOIN montos_cobro mc ON(cue.monto_cobro=mc.codigo) LEFT OUTER JOIN convenios cc ON(mc.convenio=cc.codigo) LEFT OUTER JOIN centros_costo cc2 ON(c.centro_costo=cc.codigo) WHERE 1=1 ;*/
	
	
	

	/* private final static String cargarTrasladosPacienteStr=" SELECT (to_char(tc.fecha_asignacion,'"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora_asignacion, 'HH24:MI'))) as fechaHoraTraslado, "+ 
													       " c.habitacion as habitacion,  " +
													       " c.numero_cama as cama, "+
														   " c.descripcion as descripcionCama, "+
														   " tu.nombre as usuario," +
														   " tc.codigo AS codigoTraslado " +
														   " FROM traslado_cama as tc  "+
														   " INNER JOIN camas1 c ON(tc.codigo_nueva_cama=c.codigo AND tc.institucion=c.institucion) "+
														   " INNER JOIN  tipos_usuario_cama tu ON(c.tipo_usuario_cama=tu.codigo) "+
														   " INNER JOIN admisiones_hospi ah ON(c.codigo=ah.cama) "+
														   " INNER JOIN cuentas cu ON(ah.cuenta=cu.id) "+
														   " WHERE tc.cuenta=? " +
														   " ORDER BY tc.fecha_asignacion DESC , tc.hora_asignacion DESC";
	*/	
	/*SELECT (to_char(tc.fecha,'"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora, 'HH24:MI'))) as fechaHoraTraslado, tc.nueva_habitacion as habitacion, tc.nueva_cama as cama, c.descripcion as descripcionCama, tu.nombre as usuario FROM traslado_cama tc INNER JOIN camas1 c ON(tc.nueva_habitacion=c.habitacion AND tc.nueva_cama=c.numero_cama AND tc.institucion=c.institucion) INNER JOIN tipos_usuario_cama tu ON(c.tipo_usuario_cama=tu.codigo)  INNER JOIN admisiones_hospi ah ON(c.codigo=ah.cama) INNER JOIN cuentas cu ON(ah.cuenta=cu.id) WHERE cu.codigo_paciente=?*/
	
	
	
	
	/*
	private final static String detalleTrasladoPacienteStr=" SELECT (to_char(tc.fecha_asignacion, '"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora_asignacion, 'HH24:MI'))) as fechaHoraTraslado, "+  
																" getnomhabitacioncama(cam1.codigo) as nuevaHabitacion, " +
																" getnombrepisocama(cam1.codigo) AS pisoNuevaCama, " +
																" getnomtipohabitacioncama(cam1.codigo) As tipoHabitacionNuevaCama, "+
																" cam1.numero_cama as nuevaCama, "+
																" coalesce(cam1.descripcion,'') as descripcionNuevaCama, " +   
																" tu1.nombre as tipoUsuarioNuevaCama, "+    
																" cc1.nombre as centroCostoNuevaCama,  "+
																" CASE WHEN cam1.es_Uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
																	"'SI' || " +
																	"CASE WHEN getnombretipomonitoreo(sc1.tipo_monitoreo) IS NULL THEN " +
																		"'' " +
																	"ELSE " +
																		" ' (' || getnombretipomonitoreo(sc1.tipo_monitoreo) || ')' " +
																	"END  " +
																" ELSE " +
																	"'NO' " +
																"END as esUciNuevaCama, "+  
																" getnomhabitacioncama(cam2.codigo) as habitacionAntigua, " +
																" getnombrepisocama(cam2.codigo) AS pisoCamaAntigua, " +
																" getnomtipohabitacioncama(cam2.codigo) As tipoHabitacionCamaAntigua, "+
																" cam2.numero_cama as camaAntigua, "+
																" coalesce(cam2.descripcion,'') as descripcionCamaAntigua, "+ 
																" tu2.nombre as tipoUsuarioCamaAntigua,   "+
																" cc2.nombre as centroCostoCamaAntigua,  "+
																" CASE WHEN cam2.es_Uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
																	"'SI' ||  " +
																	"CASE WHEN getnombretipomonitoreo(sc2.tipo_monitoreo) IS NULL THEN " +
																		"'' " +
																	"ELSE " +
																		" ' (' || getnombretipomonitoreo(sc2.tipo_monitoreo) || ')' " +
																	"END  " +
																" ELSE " +
																	"'NO' " +
																" END as esUciCamaAntigua,  "+   
																" (to_char(tc.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora_grabacion, 'HH24:MI'))) as fechaGrabacion,  "+ 
																" tc.usuario_asigna as usuario,  "+
																" (per.primer_apellido||' '||per.segundo_apellido||' '||per.primer_nombre||' '||per.segundo_nombre) as paciente,  "+    
																" (per.tipo_identificacion||' '||per.numero_identificacion) as tipoId,  "+
																" s.nombre as sexo,  "+
																" (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp +"')) as fechaIngreso,  "+     
																" (to_char(eg.fecha_egreso, '"+ConstantesBD.formatoFechaAp+"')) as fechaEgreso,   "+  
																" cc.nombre as responsable  "+
																" FROM traslado_cama tc    "+   
																" INNER JOIN camas1 cam1 ON(tc.codigo_nueva_cama=cam1.codigo AND tc.institucion=cam1.institucion)  "+   
																" INNER JOIN tipos_usuario_cama tu1 ON(cam1.tipo_usuario_cama=tu1.codigo)  "+
																" LEFT OUTER JOIN camas1 cam2 ON(tc.codigo_cama_antigua=cam2.codigo AND tc.institucion=cam2.institucion)  "+
																" LEFT OUTER JOIN tipos_usuario_cama tu2 ON(cam2.tipo_usuario_cama=tu2.codigo)  "+
																" LEFT OUTER JOIN servicios_cama sc1 ON(sc1.codigo_cama=cam1.codigo)  "+
																" LEFT OUTER JOIN servicios_cama sc2 ON(sc2.codigo_cama=cam2.codigo)  "+
																" INNER JOIN centros_costo cc1 ON(cam1.centro_costo=cc1.codigo)  "+   
																" LEFT OUTER JOIN centros_costo cc2 ON(cam2.centro_costo=cc2.codigo)  "+    
																" INNER JOIN cuentas cue ON(tc.codigo_paciente=cue.codigo_paciente)  "+   
																" INNER JOIN convenios cc ON(tc.convenio=cc.codigo)  "+ 
																" INNER JOIN personas per ON(tc.codigo_paciente=per.codigo)   "+   
																" INNER JOIN sexo s ON(per.sexo=s.codigo)   "+  
																" LEFT OUTER JOIN admisiones_hospi admH ON(cue.id=admH.cuenta)   "+   
																" LEFT OUTER JOIN egresos eg ON (cue.id=eg.cuenta)   "+  
																" WHERE tc.codigo=?";
		
		
	*/	
		
		
/*SELECT (to_char(tc.fecha, '"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora, 'HH24:MI'))) as fechaHoraTraslado, tc.nueva_habitacion as nuevaHabitacion,  tc.nueva_cama as nuevaCama,  getDescripcionCama(tc.nueva_cama) as descripcionNuevaCama, tu1.nombre as tipoUsuarioNuevaCama, cc1.nombre as centroCostoNuevaCama, CASE WHEN c1.es_Uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUciNuevaCama, tc.habitacion_antigua as habitacionAntigua, tc.cama_antigua as camaAntigua, getDescripcionCama(tc.cama_antigua) as descripcionCamaAntigua, tu2.nombre as tipoUsuarioCamaAntigua, cc2.nombre as centroCostoCamaAntigua, CASE WHEN c2.es_Uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUciCamaAntigua, "+
to_char(tc.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"'||'-'||tc.hora_grabacion) as fechaGrabacion, tc.usuario as usuario, (per.primer_apellido||' '||per.segundo_apellido||' '||per.primer_nombre||' '||per.segundo_nombre) as paciente, (per.tipo_identificacion||' '||per.numero_identificacion) as tipoId, s.nombre as sexo, (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp +"')) as fechaIngreso, (to_char(eg.fecha_egreso, '"+ConstantesBD.formatoFechaAp+"')) as fechaEgreso, cc.nombre as responsable FROM traslado_cama tc  INNER JOIN camas1 c1 ON(tc.nueva_habitacion=c1.habitacion  AND tc.nueva_cama=c1.numero_cama AND tc.institucion=c1.institucion) INNER JOIN tipos_usuario_cama tu1 ON(c1.tipo_usuario_cama=tu1.codigo) " +
INNER JOIN tipos_usuario_cama tu2 ON(c1.tipo_usuario_cama=tu2.codigo) INNER JOIN camas1 c2 ON(tc.nueva_habitacion=c2.habitacion  AND tc.nueva_cama=c2.numero_cama AND tc.institucion=c2.institucion) INNER JOIN centros_costo cc1 ON(c1.centro_costo=cc1.codigo) INNER JOIN centros_costo cc2 ON(c2.centro_costo=cc2.codigo)  INNER JOIN cuentas cue ON(tc.codigo_paciente=cue.codigo_paciente) INNER JOIN montos_cobro mc ON(cue.monto_cobro=mc.codigo) INNER JOIN convenios cc ON(mc.convenio=cc.codigo) INNER JOIN personas per ON(tc.codigo_paciente=per.codigo)  INNER JOIN sexo s ON(per.sexo=s.codigo) LEFT OUTER JOIN admisiones_hospi admH ON(c1.codigo=admH.cama)  LEFT OUTER JOIN egresos eg ON (cue.id=eg.cuenta) WHERE upper(tc.nueva_cama)=upper(?) ";*/
							
		
	
	/*private final static String detalleTrasladoAnteriorPacienteStr=" SELECT (to_char(tc.fecha_asignacion, '"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora_asignacion, 'HH24:MI'))) as fechaHoraTraslado, "+  
																	" getnomhabitacioncama(cam1.codigo) as nuevaHabitacion, "+
																	" cam1.numero_cama as nuevaCama, "+
																	" coalesce(cam1.descripcion,'') as descripcionNuevaCama, " +
																	" getnombrepisocama(cam1.codigo) AS pisoNuevaCama, " +
																	" getnomtipohabitacioncama(cam1.codigo) AS tipoHabitacionNuevaCama, " +   
																	" tu1.nombre as tipoUsuarioNuevaCama, "+    
																	" cc1.nombre as centroCostoNuevaCama,  "+
																	" CASE WHEN cam1.es_Uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUciNuevaCama, "+  
																	" getnomhabitacioncama(cam2.codigo) as habitacionAntigua, "+
																	" cam2.numero_cama as camaAntigua, "+
																	" coalesce(cam2.descripcion,'') as descripcionCamaAntigua, "+ 
																	" getnombrepisocama(cam2.codigo) AS pisoCamaAntigua, " +
																	" getnomtipohabitacioncama(cam2.codigo) AS tipoHabitacionCamaAntigua, " +
																	" tu2.nombre as tipoUsuarioCamaAntigua,   "+
																	" cc2.nombre as centroCostoCamaAntigua,  "+
																	" CASE WHEN cam2.es_Uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUciCamaAntigua,  "+   
																	" to_char(tc.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"'||'-'||tc.hora_grabacion) as fechaGrabacion,  "+ 
																	" tc.usuario_asigna as usuario,  "+
																	" (per.primer_apellido||' '||per.segundo_apellido||' '||per.primer_nombre||' '||per.segundo_nombre) as paciente,  "+    
																	" (per.tipo_identificacion||' '||per.numero_identificacion) as tipoId,  "+
																	" s.nombre as sexo,  "+
																	" (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp +"')) as fechaIngreso,  "+     
																	" (to_char(eg.fecha_egreso, '"+ConstantesBD.formatoFechaAp+"')) as fechaEgreso,   "+  
																	" cc.nombre as responsable  "+
																	" FROM traslado_cama tc    "+   
																	" INNER JOIN camas1 cam1 ON(tc.codigo_nueva_cama=cam1.codigo AND tc.institucion=cam1.institucion)  "+   
																	" INNER JOIN tipos_usuario_cama tu1 ON(cam1.tipo_usuario_cama=tu1.codigo)  "+   
																	" LEFT OUTER JOIN camas1 cam2 ON(tc.codigo_cama_antigua=cam2.codigo AND tc.institucion=cam1.institucion)  "+
																	" LEFT OUTER JOIN tipos_usuario_cama tu2 ON(cam2.tipo_usuario_cama=tu2.codigo)  "+
																	" INNER JOIN centros_costo cc1 ON(cam1.centro_costo=cc1.codigo)  "+   
																	" LEFT OUTER JOIN centros_costo cc2 ON(cam2.centro_costo=cc2.codigo)  "+    
																	" INNER JOIN cuentas cue ON(tc.codigo_paciente=cue.codigo_paciente)  "+   
																	" INNER JOIN convenios cc ON(tc.convenio=cc.codigo)  "+ 
																	" INNER JOIN personas per ON(tc.codigo_paciente=per.codigo)   "+   
																	" INNER JOIN sexo s ON(per.sexo=s.codigo)   "+  
																	" LEFT OUTER JOIN admisiones_hospi admH ON(cam1.codigo=admH.cama)   "+   
																	" LEFT OUTER JOIN egresos eg ON (cue.id=eg.cuenta)   "+  
																	" WHERE tc.cuenta=? ";
														
	
	*/
	
	
	/*private final static String consultaIngresosAnterioresStr=" SELECT " +
															  " getnomcentroatencion(cc.centro_atencion) as centro_atencion, "+
															  "cue.id as cuenta, " +
															  " ec.nombre as estadoCuenta, " +
															  " cue.via_ingreso as codigoViaIngreso, " +
															  " getnombreviaingreso(cue.via_ingreso) as viaIngreso, " +
															  " (to_char(adh.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||'-'||to_char(adh.hora_admision, 'HH24:MI')) as fechaHoraIngreso, " +
															  " (to_char(eg.fecha_egreso, '"+ConstantesBD.formatoFechaAp+"')||'-'||to_char(eg.hora_egreso, 'HH24:MI')) as fechaHoraEgreso, " +
															  " getresumenespecialidad(cue.via_ingreso,cue.id) as especialidad  " +
															  " FROM cuentas cue " +
															  " INNER JOIN estados_cuenta ec ON (cue.estado_cuenta=ec.codigo) " +
															  " INNER JOIN centros_costo cc ON (cc.codigo=cue.area) " +
															  " LEFT OUTER JOIN admisiones_hospi adh ON (cue.id=adh.cuenta)  " +
															  " INNER JOIN egresos eg ON (cue.id=eg.cuenta) " +
															  " WHERE cue.codigo_paciente=? " +
															  " ORDER BY cue.fecha_apertura desc, cue.hora_apertura desc";
	
	*/
	
	/**
	 * Statement para cargar los traslados segun la fecha que se seleccione. Por defecto carga los del la fecha del sistema .
	 */
	private final static String cargarTrasladosFechaStr="SELECT tc.hora_asignacion as hora, "+
														" getnomhabitacioncama(cam1.codigo) as habitacionActual, " +
														" getnombrepisocama(cam1.codigo) as pisoCamaActual, " +
														" getnomtipohabitacioncama(cam1.codigo) AS tipoHabitacionCamaActual, "+
														" cam1.numero_cama as camaActual, "+
														" coalesce(cam1.descripcion,'') as descripcionCamaActual,   "+
														" (p.primer_apellido||' '||p.segundo_apellido||' '||p.primer_nombre||' '||p.segundo_nombre) as paciente,  "+ 
														" (p.tipo_identificacion||' '||p.numero_identificacion) as tipoId, "+    
														" (to_char(aH.fecha_admision,'"+ConstantesBD.formatoFechaAp +"')) as fechaIngreso, "+    
														" getnombreconvenio(tc.convenio) as responsable, "+ 
														" cam2.numero_cama as camaAnterior, "+
														" getnomhabitacioncama(cam2.codigo) as habitacionAnterior, " +
														" getnombrepisocama(cam2.codigo) as pisoCamaAnterior, " +
														" getnomtipohabitacioncama(cam2.codigo) AS tipoHabitacionCamaAnterior, "+
														" coalesce(cam2.descripcion,'') as descripcionCamaAnterior,  "+ 
														" tc.fecha_asignacion as fechaTraslado, "+
														" tc.codigo_paciente as codigoPaciente," +
														" tc.codigo AS codigoTraslado "+
														" FROM traslado_cama tc "+
														" INNER JOIN camas1 cam1 ON(tc.codigo_nueva_cama=cam1.codigo AND tc.institucion=cam1.institucion) "+
														" INNER JOIN camas1 cam2 ON(tc.codigo_cama_antigua=cam2.codigo AND tc.institucion=cam2.institucion) "+
														" INNER JOIN cuentas cue on (cue.id=tc.cuenta) "+   
														" INNER JOIN centros_costo cc on (cc.codigo=cue.area) "+
														" INNER JOIN admisiones_hospi ah on (ah.cuenta=cue.id)  "+   
														" INNER JOIN personas p on (p.codigo=tc.codigo_paciente)   "+
														" WHERE tc.fecha_asignacion=? and cc.centro_atencion = ?";
		
		
/* SELECT tc.hora as hora, tc.nueva_habitacion as habitacionActual, tc.nueva_cama as camaActual,  getDescripconCama(tc.nueva_cama) as descripcionCamaActual,  (p.primer_apellido||' '||p.segundo_apellido||' '||p.primer_nombre||' '||p.segundo_nombre) as paciente, (p.tipo_identificacion||' '||p.numero_identificacion) as tipoId, (to_char(aH.fecha_admision,'"+ConstantesBD.formatoFechaAp  getnombreconvenio(tc.convenio) as responsable , tc.cama_antigua as camaAnterior,  tc.habitacion_antigua as habitacionAnterior, getDescripconCama(tc.cama_antigua) as descripcionCamaAnterior,  tc.fecha as fechaTraslado,  tc.codigo_paciente as codigoPaciente  FROM traslado_cama tc  inner join cuentas cue on (tc.codigo_paciente=cue.codigo_paciente) inner join admisiones_hospi ah on (ah.cuenta=cue.id)  inner join personas p on (p.codigo=tc.codigo_paciente)  WHERE tc.fecha=?";*/
		
	/**
	 * Statement para actualizar el estado de una cama cuando es asignada a un paciente
	 */
	private static final String updateEstadoCamaStr=" UPDATE camas1 SET estado=? " +
													" WHERE codigo=? " +
													" and institucion=?";
	
	/**
	 * Statement para saber si existe algun ingreso registrado en la tabla trasaldo_cama dado el codigo del paciente
	 */
	private static final String consultarIngresoEnTrasladoCamaStr="SELECT count(1) as cantidad " +
			"FROM traslado_cama tc " +
			"INNER JOIN cuentas c ON(c.id=tc.cuenta) " +
			"INNER JOIN ingresos i ON(i.id=c.id_ingreso)  " +
			"WHERE tc.codigo_paciente=? AND i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'";
	
	/**
	 * Cadena para la actualización de la hora fecha de finalizacion de la estancia en la cama
	 */
	private static final String actualizarFehaHoraFinalizacionStr=" UPDATE traslado_cama SET " +
																  " fecha_finalizacion=?," +
																  " hora_finalizacion=? " ;
																 
																 
	
	/**
	 * Cadena para tomar el codigo de una cama de la tabla camas1 dado su numero_cama 
	 */
	private static final String consultarCodigoCamaStr=" SELECT codigo as codigo " +
													   " FROM camas1 " +
													   " WHERE upper(numero_cama)=upper(?)";
	
	
	
	/*private static final String consultarCamasEnTrasladoCamaStr=" SELECT (to_char(tc.fecha_asignacion,'"+ConstantesBD.formatoFechaAp+"'||'-'||to_char(tc.hora_asignacion, 'HH24:MI'))) as fechaHoraTraslado, "+
																	" getnomhabitacioncama(cam1.codigo) as habitacion, "+
																	" cam1.numero_cama as cama, "+
																	" cam1.descripcion as descripcionCama, "+
																	" tu.nombre as tipoUsuario, "+
																	" tc.codigo AS codigoTraslado,"+
																	" cc.nombre as centroCosto, " +
																	" tc.codigo_paciente as paciente, " +
																	" CASE WHEN cam1.es_uci = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END as esUci," +
																	" getnombrepisocama(cam1.codigo) AS piso, " +
																	" getnomtipohabitacioncama(cam1.codigo) AS tipo_habitacion "+
																	" FROM traslado_cama tc "+
																	" INNER JOIN  camas1 cam1 ON(cam1.codigo=tc.codigo_nueva_cama)   "+  
																	" INNER JOIN tipos_usuario_cama tu ON(cam1.tipo_usuario_cama=tu.codigo)   "+  
																	" INNER JOIN centros_costo cc ON(cam1.centro_costo=cc.codigo)  "+   
																	" WHERE tc.codigo_paciente=?  " +
																	" ORDER BY tc.fecha_asignacion DESC,tc.hora_asignacion DESC ";
	
	*/
	
	/**
	 * Cadena de actualizacion el estado de la reserva
	 */
	private static final String cadenaCambiarEstadoReservaStr="UPDATE reservar_cama SET estado=? WHERE institucion=? AND codigo_cama=? AND codigo_paciente=? AND estado=?"; 	
	
	/**
	 * Cambiar el estado de la cama de reservada
	 */
	private static final String cadenaCambiarEstadoCamaStr="UPDATE camas1 SET estado=? WHERE institucion=? AND codigo=?";
	
	/**
	 *Metodo encargdo de cambiar el estado de la cama,
	 *este devuelve true para indicar operacion exitosa
	 *de lo contrario devuelve false.
	 *El HashMap aprametros contiene las siguientes key's
	 *--------------------------------------------------
	 *--nuevoEstadoReserva --> Requerido
	 *--institucion --> Requerido
	 *--codigoCama --> Requerido
	 *--codigoPaciente --> Requerido
	 *--estadoReserva --> Requerido
	 *--nuevoEstadoCama --> Requerido
	 *-------------------------------------------------- 
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static boolean cambiarEstaReserva (Connection connection,HashMap parametros)
	{
		boolean result = false;
		
		String cadenaReserva = cadenaCambiarEstadoReservaStr;
		String cadenaCama = cadenaCambiarEstadoCamaStr;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaReserva,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("nuevoEstadoReserva")+"");
			ps.setInt(2, Integer.parseInt(parametros.get("institucion")+""));
			ps.setInt(3, Integer.parseInt(parametros.get("codigoCama")+""));
			ps.setInt(4, Integer.parseInt(parametros.get("codigoPaciente")+""));
			ps.setString(5, parametros.get("estadoReserva")+"");
			if(ps.executeUpdate()>0)
				ps =  new PreparedStatementDecorator(connection.prepareStatement(cadenaCama,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Integer.parseInt(parametros.get("nuevoEstadoCama")+""));
				ps.setInt(2, Integer.parseInt(parametros.get("institucion")+""));
				ps.setInt(3, Integer.parseInt(parametros.get("codigoCama")+""));
				if(ps.executeUpdate()>0)
					result= true;
			
		}
		catch (SQLException e) 
		{
		 logger.error("\n\n *** Problema Actualizando el estado de la reserva de la cama "+e);
		 result= false;
		}
		
		
		
		return result;
	}
	
	/**
	 * Método que carga la cama actual de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static ResultSetDecorator cargarCamaActualPaciente(Connection con, int codigoPaciente, String consultaCamaActualEnTrasladoCamaStr)  throws SQLException
	{
		try
		{
			ResultSetDecorator rs=null;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarIngresoEnTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			rs=new ResultSetDecorator(pst.executeQuery());
			int tmp=0;
			
			logger.info("sQl0 : " + consultarIngresoEnTrasladoCamaStr);	
			
			while(rs.next())
			{
				tmp=rs.getInt("cantidad");
			}
			if(tmp>1)
			{
				PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaCamaActualEnTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStatement.setInt(1, codigoPaciente);
				
				logger.info("sQl1 : " + consultaCamaActualEnTrasladoCamaStr);
				
				
				return new ResultSetDecorator(cargarStatement.executeQuery());
			}
			else
			{
				PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarCamaActualStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStatement.setInt(1, codigoPaciente);

				logger.info("sQl2: " + consultarCamaActualStr);
				
				
				return new ResultSetDecorator(cargarStatement.executeQuery());
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la cama actual del paciente : SqlBaseTrasladoCamasDao   "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Mètodo para buscar los traslados de cama por centro de costo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static ResultSetDecorator cargarTrasladosArea (Connection con,int codigoCentroCosto) throws SQLException
	{
				try
				{
					PreparedStatementDecorator ps = null;
				
					if (con == null || con.isClosed()) 
					{
						DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						con = myFactory.getConnection();
					}  
				
					String avanzadaStr = "";
					
					if(codigoCentroCosto>0)
					{
						avanzadaStr+=" AND cam1.centro_costo="+codigoCentroCosto;
					}
					
					
					String consulta= cargarTrasladosAreaStr + avanzadaStr;
					ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					return new ResultSetDecorator(ps.executeQuery());
				}
				catch(SQLException e)
				{
					logger.warn(e+"Error en Busqueda de Traslados de Camas por centro de costo : SqlBaseTrasladoCamasDao  "+e.toString() );
					return null;
				}	    
	}
	
	/**
	 * Método que carga los traslados de cama de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static ResultSetDecorator cargarTrasladosPaciente(Connection con, int codigoPaciente, String sqlCargarTrasladosPaciente, String sqlConsultarCamasEnTrasladoCamaStr )  throws SQLException
	{
		try
		{
//			ResultSetDecorator rs=null;
//			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarIngresoEnTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
//			pst.setInt(1, codigoPaciente);
//			rs=new ResultSetDecorator(pst.executeQuery());
//			int tmp=0;
//			
//			while(rs.next())
//			{
//				tmp=rs.getInt("cantidad");
//			}
//			
//			if(tmp > 0)
//			{
//				PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(sqlConsultarCamasEnTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
//				cargarStatement.setInt(1, codigoPaciente);
//				return new ResultSetDecorator(cargarStatement.executeQuery());
//			}
//			else
//			{
//				PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(sqlCargarTrasladosPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
//				cargarStatement.setInt(1, codigoPaciente);
//				return new ResultSetDecorator(cargarStatement.executeQuery());
//			}
//			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(cargarTrasladosPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
//			cargarStatement.setInt(1, codigoPaciente);
//			return cargarStatement.executeQuery());
			
			
			//Se elimina todo lo anterior debido a la MT 5654
			String consultaIngresoConTrasladoCama =
				"SELECT "+
				  "tc.cuenta "+
				"FROM traslado_cama tc "+
				"INNER JOIN cuentas c ON(c.id=tc.cuenta) "+
				"INNER JOIN ingresos i ON(i.id=c.id_ingreso) "+
				"WHERE tc.codigo_paciente = ? "+
				"AND i.estado = ? ";
			
			PreparedStatement ps = con.prepareStatement(consultaIngresoConTrasladoCama);
			ps.setInt(1, codigoPaciente);
			ps.setString(2, ConstantesIntegridadDominio.acronimoEstadoAbierto);
			ResultSet rs = ps.executeQuery();
			int cuenta = 0;
			
			if(rs.next()){
				cuenta = rs.getInt("cuenta");
			}
			
			if(cuenta != 0){
				return cargarTrasladosPacientePorCuenta(con, cuenta);
			}
			else{
				PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(sqlCargarTrasladosPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarStatement.setInt(1, codigoPaciente);
				return new ResultSetDecorator(cargarStatement.executeQuery());
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los traslados del paciente : SqlBaseTrasladoCamasDao  "+e.toString());
			return null;
		}
	}
	
	public static ResultSetDecorator cargarTrasladosPacientePorCuenta(Connection con, int cuenta) throws SQLException {
	
		String consultaTrasladosPacientePorCuenta;

		if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
		{
			consultaTrasladosPacientePorCuenta =
			"SELECT (TO_CHAR(tc.fecha_asignacion,'DD/MM/YYYY' "+
			  "||'-' "+
			  "||TO_CHAR(tc.hora_asignacion, 'HH24:MI'))) AS fechaHoraTraslado, "+
			  "getnomhabitacioncama(cam1.codigo)          AS habitacion, "+
			  "cam1.numero_cama                           AS cama, "+
			  "cam1.descripcion                           AS descripcionCama, "+
			  "tu.nombre                                  AS tipoUsuario, "+
			  "tc.codigo                                  AS codigoTraslado, "+
			  "cc.nombre                                  AS centroCosto, "+
			  "tc.codigo_paciente                         AS paciente, "+
			  "CASE "+
			    "WHEN cam1.es_uci = true "+
			    "THEN 'SI' "+
			    "ELSE 'NO' "+
			  "END                                   AS esUci, "+
			  "getnombrepisocama(cam1.codigo)        AS piso, "+
			  "getnomtipohabitacioncama(cam1.codigo) AS tipo_habitacion "+
			"FROM traslado_cama tc "+
			"INNER JOIN camas1 cam1 "+
			"ON(cam1.codigo=tc.codigo_nueva_cama) "+
			"INNER JOIN tipos_usuario_cama tu "+
			"ON(cam1.tipo_usuario_cama=tu.codigo) "+
			"INNER JOIN centros_costo cc "+
			"ON(cam1.centro_costo    =cc.codigo) "+
			"WHERE tc.cuenta=? "+
			"ORDER BY tc.fecha_asignacion DESC, "+
			  "tc.hora_asignacion DESC ";
		}
		else
		{
			consultaTrasladosPacientePorCuenta =
			"SELECT "+
					  "to_char(tc.fecha_asignacion, 'DD/MM/YYYY') "+
					  "|| '-' "+
					  "||tc.hora_asignacion                       AS fechaHoraTraslado, "+ 
					  "getnomhabitacioncama(cam1.codigo)          AS habitacion, "+
					  "cam1.numero_cama                           AS cama, "+
					  "cam1.descripcion                           AS descripcionCama, "+
					  "tu.nombre                                  AS tipoUsuario, "+
					  "tc.codigo                                  AS codigoTraslado, "+
					  "cc.nombre                                  AS centroCosto, "+
					  "tc.codigo_paciente                         AS paciente, "+
					 "CASE "+
					    "WHEN cam1.es_uci = 1 "+
					    "THEN 'SI' "+
					    "ELSE 'NO' "+
					  "END                                   AS esUci, "+  
					  "getnombrepisocama(cam1.codigo)        AS piso, "+
					  "getnomtipohabitacioncama(cam1.codigo) AS tipo_habitacion "+
					"FROM traslado_cama tc "+
					"INNER JOIN camas1 cam1 "+
					"ON(cam1.codigo=tc.codigo_nueva_cama) "+
					"INNER JOIN tipos_usuario_cama tu "+
					"ON(cam1.tipo_usuario_cama=tu.codigo) "+
					"INNER JOIN centros_costo cc "+
					"ON(cam1.centro_costo =cc.codigo) "+
					"WHERE tc.cuenta      = ? "+
					"ORDER BY tc.fecha_asignacion DESC,tc.hora_asignacion DESC ";
		}
		PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaTrasladosPacientePorCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarStatement.setInt(1, cuenta);
		return new ResultSetDecorator(cargarStatement.executeQuery());
	}
	
	/**
	 * Método apra cargar el detalle de un traslado de un paciente
	 * @param con
	 * @param codigoTraslado
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarDetalleTrasladoPaciente(Connection con,  int codigoTraslado, String sqlDetalleTrasladoPaciente)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(sqlDetalleTrasladoPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoTraslado);
			
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle de un traslado de un paciente cargado en session : SqlBaseTrasladoCamasDao  "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Método para cargar las ingresos anteriores del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarIngresosAnteriores(Connection con, int codigoPaciente, String sqlconsultaIngresosAnteriores)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(sqlconsultaIngresosAnteriores,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPaciente);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de los ingresos anteriores  del paciente : SqlBaseTrasladoCamasDao"+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Método apra cargar el detalle de un traslado anterior de un paciente dada su cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarDetalleTrasladoAnteriorPaciente(Connection con, int cuenta, String sqlDetalleTrasladoAnteriorPaciente )  throws SQLException
	{
		try
		{
			logger.info("consulta traslado anterior => "+sqlDetalleTrasladoAnteriorPaciente+", cuentas=> "+cuenta);
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(sqlDetalleTrasladoAnteriorPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, cuenta);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del detalle de un traslado Anterior de un paciente cargado en session : SqlBaseTrasladoCamasDao"+e.toString());
			return null;
		}
	}
	
	
	
	/**
	 * Método para buscar los traslados por Fecha
	 * @param con
	 * @param fechaTraslado
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarTrasladosFecha (Connection con,String fechaTraslado, int centroAtencion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarTrasladosFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//ps.setString(1, "'"+fechaTraslado+"'");
			ps.setDate(1, new java.sql.Date(UtilidadFecha.conversionFormatoFechaStringDate(fechaTraslado).getTime()));
			ps.setInt(2, centroAtencion);
			return new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en Busqueda de Traslados de Camas por fecha : SqlBaseTrasladoCamasDao "+e.toString() );
			return null;
		}	    
	}
	
	
	
	/**
	 * Método para actualizar el estado de una cama 
	 * @param con
	 * @param estadoCama
	 * @param codigoCama
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static int modificarEstadoCama (Connection con,int estadoCama,int codigoCama, int institucion) throws SQLException
	{
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(updateEstadoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, estadoCama);
			ps.setInt(2, codigoCama);
			ps.setInt(3, institucion);
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la actualizacion del estado de la cama : SqlBaseTrasladoCamasDao "+e.toString() );
			return -1;
		}	    
	}
	
	/**
	 * Metodo que actualiza la fecha - hora de finalización de la estancia de un paciente en una cama
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 */
	public static boolean  actualizarFechaHoraFinalizacion(	Connection con, 
																						int codigoCuenta, 
																						String fechaFin,  
																						String horaFin,
																						String observaciones
																					) 
	{
		String cadena = actualizarFehaHoraFinalizacionStr;
		
		if (UtilidadCadena.noEsVacio(observaciones))
			cadena+=", observaciones='"+observaciones+"'";
		
		String where= " WHERE cuenta=? " +
							" AND fecha_finalizacion IS NULL " +
							" AND hora_finalizacion IS NULL";
		
		boolean resp=false;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			cadena+=where;
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			ps.setString(2,horaFin);
			
			ps.setInt(3,codigoCuenta);
			
			
			
			if(ps.executeUpdate()>0)
			    resp= true;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseTrasladoCamasDao "+e.toString());
			resp=false;			
		}	
		return resp;	
	}
	
	
	
	/**
	 * Metodo que actualiza la fecha y hora de finalizacion de ocupacion de una cama 
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 */
	public static boolean  actualizarFechaHoraFinalizacionNoTransaccional(	Connection con,int codigoCuenta,String fechaFin,String horaFin,String observaciones) 
	{
		boolean resp=false;	
		try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarFehaHoraFinalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaFin));
				ps.setString(2,horaFin);
				if (!observaciones.equals(""))
					ps.setObject(3, observaciones);
				else
					ps.setNull(3, Types.VARCHAR);
				ps.setInt(4,codigoCuenta);
				if(ps.executeUpdate()>0)
				{
					resp= true;
				}
			}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de la fecha y hora de finalizacion no transaccional: SqlBaseTrasladoCamaDao "+e.toString());
			resp=false;			
		}	
		return resp;	
	}
	
	
	
	
	/**
	 *  Método para la insercion en la base de datos de un traslado de cama
	 * @param con
	 * @param fechaAsignacion
	 * @param horaAsignacion
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @param fechaFinalizacion
	 * @param horaFinalizacion
	 * @param codigoCuenta
	 * @param codigoNuevaCama
	 * @param codigoCamaAntigua
	 * @return
	 * @throws SQLException
	 */
	public static int insertarTrasladoCama(	Connection con, 
	        											String fechaAsignacion, 
	        											String horaAsignacion, 
	        											int codigoInstitucion, 
	        											String loginUsuario, 
	        											int codigoPaciente, 
	        											int codigoConvenio, 
	        											String fechaFinalizacion, 
	        											String horaFinalizacion, 
	        											int codigoCuenta,
	        											int codigoNuevaCama,
	        											int codigoCamaAntigua,
	        											String insertarTrasladoCamaStr) 
	{
		
	    int resp=0;
		try
		{
			/*
			 * "codigo, " +
			"fecha_asignacion, " +
			"hora_asignacion, " +
			"institucion, " +
			"fecha_grabacion, " +
			"hora_grabacion, " +
			"usuario_asigna," +
			"codigo_paciente," +
			"convenio," +
			"fecha_finalizacion, " +
			"hora_finalizacion, " +
			"cuenta, " +
			"codigo_nueva_cama," +
			"codigo_cama_antigua) " +
			"VALUES ( " 
			(seq_traslado_cama.NEXTVAL), " +
			"?, ?, ?, CURRENT_DATE, to_char(sysdate,'HH24:MI'), ?, ?, ?, ?, ?, ?, ?, ? )";*/
		    PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(insertarTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaAsignacion));
			statement.setString(2,horaAsignacion);
			statement.setInt(3, codigoInstitucion);
			statement.setString(4, loginUsuario);
			statement.setInt(5, codigoPaciente);
			statement.setInt(6, codigoConvenio);
			if(!fechaFinalizacion.equals(""))
			    statement.setString(7, UtilidadFecha.conversionFormatoFechaABD(fechaFinalizacion));
			else
			    statement.setObject(7, null);
			if(!horaFinalizacion.equals(""))
			    statement.setString(8, horaFinalizacion);
			else
			    statement.setObject(8, null);
			statement.setInt(9, codigoCuenta);
			statement.setInt(10, codigoNuevaCama);
			if(codigoCamaAntigua<=0)
			    statement.setObject(11, null);
			else
			    statement.setInt(11, codigoCamaAntigua);
			resp=statement.executeUpdate();
			return resp;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la insercion del traslado de cama : SqlBaseTrasladoCamasDao"+e.toString());
			return -1;
		}
	}
}
