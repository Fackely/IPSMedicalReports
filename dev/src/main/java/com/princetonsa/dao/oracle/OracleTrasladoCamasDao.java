/*
 * @(#)OracleTrasladoCamasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TrasladoCamasDao;
import com.princetonsa.dao.sqlbase.SqlBaseTrasladoCamasDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 07 /Jul/ 2005
 */
public class OracleTrasladoCamasDao implements TrasladoCamasDao 
{
	
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(OracleTrasladoCamasDao.class);
	
	/**
	 * Statement para insertar el log correspondiete al traslado de cama de un paciente cargado en session
	 */
	private final static String insertarlogTrasladoCamaStr="INSERT INTO traslado_cama(" +
		 	                                                                         "codigo," +
		 	                                                                         "fecha_asignacion," +
		 	                                                           			     "hora_asignacion," +
		 	                                                           			     "codigo_nueva_cama," +
		 	                                                           			     "codigo_cama_antigua," +
		 	                                                           			     "institucion," +
		 	                                                           			     "fecha_grabacion," +
		 	                                                           			     "hora_grabacion," +
		 	                                                           			     "usuario_asigna," +
		 	                                                           			     "codigo_paciente," +
		 	                                                           			     "cuenta," +
		 	                                                           			     "convenio," +
		 	                                                           			     "observaciones)" +
		 	                                                                         " VALUES((seq_traslado_cama.NEXTVAL),?,?,?,?,?, CURRENT_DATE, to_char(sysdate,'HH24:MI'),?,?,?,?,?)";
	
	/**
	 * Statement para insertar el traslado de la cama 
	 */
	private final static String insertarTrasladoCamaStr=	"INSERT INTO traslado_cama (" +
																					"codigo, " +
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
																					"VALUES ( " +
																					"(seq_traslado_cama.NEXTVAL), " +
																					"?, ?, ?, CURRENT_DATE, to_char(sysdate,'HH24:MI'), ?, ?, ?, ?, ?, ?, ?, ? )";
	
	
	/**
	 * Statement para consultar el ultimo ingreso de un trasladao de cama de un paciente en la tabla de traslado_cama dado el codigo del paciente
	 */
	private final static String consultarCamaActualEnTrasladoCamaStr=  " SELECT " +
																		" cam1.codigo As codigo, "+
																		" (to_char(tc.fecha_asignacion,'"+ConstantesBD.formatoFechaAp+"')||'-'||tc.hora_asignacion) as fechaHoraTraslado, "+
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
	
	
	/**
	 * Statement para la consulta de todos los traslados por paciente cargado en session
	 */
	private final static String cargarTrasladosPacienteStr=" SELECT (to_char(tc.fecha_asignacion,'"+ConstantesBD.formatoFechaAp+"')||'-'||tc.hora_asignacion) as fechaHoraTraslado, "+ 
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
														
	
	/**
	 * Statement para consultar los traslados de un paciente
	 */
	private static final String consultarCamasEnTrasladoCamaStr=" SELECT (to_char(tc.fecha_asignacion,'"+ConstantesBD.formatoFechaAp+"')||'-'||tc.hora_asignacion) as fechaHoraTraslado, "+
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
	

	/**
	 * 	Statement que contiene el detalle de un traslado cuando se consulta por paciente cargado en session
	 */
	private final static String detalleTrasladoPacienteStr=" SELECT (to_char(tc.fecha_asignacion, '"+ConstantesBD.formatoFechaAp+"')||'-'||tc.hora_asignacion) as fechaHoraTraslado, "+  
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
															" (to_char(tc.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"')||'-'||tc.hora_grabacion) as fechaGrabacion,  "+ 
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
	
	
	/**
	 * Statement que contiene el detalle de un traslado cuando se consulta por paciente
	 */
	private final static String detalleTrasladoAnteriorPacienteStr=" SELECT (to_char(tc.fecha_asignacion, '"+ConstantesBD.formatoFechaAp+"')||'-'||tc.hora_asignacion) as fechaHoraTraslado, "+  
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
	
	
	/**
	 * Statement para la consulta de los ingresos anteriores de un paciente cargado en session
	 */
	private final static String consultaIngresosAnterioresStr=" SELECT " +
															  " getnomcentroatencion(cc.centro_atencion) as centro_atencion, "+
															  "cue.id as cuenta, " +
															  " ec.nombre as estadoCuenta, " +
															  " cue.via_ingreso as codigoViaIngreso, " +
															  " getnombreviaingreso(cue.via_ingreso) as viaIngreso, " +
															  " (to_char(adh.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||'-'||adh.hora_admision) as fechaHoraIngreso, " +
															  " (to_char(eg.fecha_egreso, '"+ConstantesBD.formatoFechaAp+"')||'-'||eg.hora_egreso) as fechaHoraEgreso, " +
															  " getresumenespecialidad(cue.via_ingreso,cue.id) as especialidad  " +
															  " FROM cuentas cue " +
															  " INNER JOIN estados_cuenta ec ON (cue.estado_cuenta=ec.codigo) " +
															  " INNER JOIN centros_costo cc ON (cc.codigo=cue.area) " +
															  " LEFT OUTER JOIN admisiones_hospi adh ON (cue.id=adh.cuenta)  " +
															  " INNER JOIN egresos eg ON (cue.id=eg.cuenta) " +
															  " WHERE cue.codigo_paciente=? " +
															  " AND cue.via_ingreso = '" + ConstantesBD.codigoViaIngresoHospitalizacion + "' " +
															  " ORDER BY cue.fecha_apertura desc, cue.hora_apertura desc";
	
	
	
	/**
	 * Método que carag la cama actual de paciente dado el codigo del paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarCamaActualPaciente (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarCamaActualPaciente(con, codigoPaciente, consultarCamaActualEnTrasladoCamaStr);
	}
	
	
	
	/**
	 *  Método para la insercion en la base de datos la nueva cama que sera asiganda al paciente
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param nuevaCama
	 * @param nuevaHabitacion
	 * @param camaAntigua
	 * @param habitacionAntigua
	 * @param institucion
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param usuario
	 * @param codigoPaciente
	 * @param convenio
	 * @return
	 * @throws SQLException
	 */
	public int insertarTrasladoCamaPaciente(Connection con, String fecha_asignacion, String hora_asignacion, int codigoNuevaCama,  int codigoCamaAntigua, int institucion, String usuario, int codigoPaciente, int cuenta, int convenio, String observaciones) throws SQLException
	{
		int resp=0;
		try
		{
				
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(insertarlogTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setString(1,fecha_asignacion);
			statement.setString(2,hora_asignacion);
			statement.setInt(3,codigoNuevaCama);
			statement.setInt(4,codigoCamaAntigua);
			statement.setInt(5,institucion);
			statement.setString(6,usuario);
			statement.setInt(7,codigoPaciente);
			statement.setInt(8, cuenta);
			statement.setInt(9,convenio);
			statement.setString(10, observaciones);
			resp=statement.executeUpdate();
			
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_traslado_cama");
			
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la insercion de la nueva cama del paciente : OracleTrasladoCamasDao"+e.toString());
			return -1;
		}
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
	public int insertarTrasladoCama(	Connection con, 
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
	        											int codigoCamaAntigua) throws SQLException
	{
		return SqlBaseTrasladoCamasDao.insertarTrasladoCama(con, fechaAsignacion, horaAsignacion, codigoInstitucion, loginUsuario, codigoPaciente, codigoConvenio, fechaFinalizacion, horaFinalizacion, codigoCuenta, codigoNuevaCama, codigoCamaAntigua, insertarTrasladoCamaStr);
	}
	    /**int resp=0;
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(insertarTrasladoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setString(1,fechaAsignacion);
			statement.setString(2,horaAsignacion);
			statement.setInt(3, codigoInstitucion);
			statement.setString(4, loginUsuario);
			statement.setInt(5, codigoPaciente);
			statement.setInt(6, codigoConvenio);
			if(!fechaFinalizacion.equals(""))
			    statement.setString(7, fechaFinalizacion);
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
			logger.warn(e+" Error en la insercion del traslado de cama : PostgresqlTrasladoCamasDao"+e.toString());
			return -1;
		}
	}
	**/
	
	/**
	 * Mètodo para buscar los traslados de cama por centro de costo
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 */
	public ResultSetDecorator cargarTrasladosArea (Connection con,int codigoCentroCosto) throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarTrasladosArea(con, codigoCentroCosto);
	}
	
	
	/**
	 * Método apra cargar el detalle de un traslado de un paciente
	 * @param con
	 * @param codigoTraslado
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDetalleTrasladoPaciente(Connection con, int codigoTraslado)  throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarDetalleTrasladoPaciente(con, codigoTraslado, detalleTrasladoPacienteStr);
	}
	
	/**
	 * Método para cargar las ingresos anteriores del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarIngresosAnteriores(Connection con, int codigoPaciente)  throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarIngresosAnteriores(con, codigoPaciente, consultaIngresosAnterioresStr);
	}
	
	
	/**
	 * Método para buscar los traslados por Fecha
	 * @param con
	 * @param fechaTraslado
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarTrasladosFecha (Connection con,String fechaTraslado,int centroAtencion) throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarTrasladosFecha(con, fechaTraslado,centroAtencion);
	}
	
	/**
	 * Método que carga los traslados de cama de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarTrasladosPaciente(Connection con, int codigoPaciente )  throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarTrasladosPaciente(con, codigoPaciente, cargarTrasladosPacienteStr, consultarCamasEnTrasladoCamaStr);
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
	public int modificarEstadoCama (Connection con,int estadoCama,int codigoCama, int institucion) throws SQLException
	{
		return SqlBaseTrasladoCamasDao.modificarEstadoCama(con, estadoCama, codigoCama, institucion);
	}
	
	
	/**
	 * Método apra cargar el detalle de un traslado anterior de un paciente dada su cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDetalleTrasladoAnteriorPaciente(Connection con, int cuenta)  throws SQLException
	{
		return SqlBaseTrasladoCamasDao.cargarDetalleTrasladoAnteriorPaciente(con, cuenta, detalleTrasladoAnteriorPacienteStr);
	}
	
	/**
	 * Metodo que actualiza la fecha - hora de finalización de la estancia de un paciente en una cama
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 */
	public  boolean  actualizarFechaHoraFinalizacion(	Connection con, 
																						int codigoCuenta, 
																						String fechaFin,  
																						String horaFin,
																						String observaciones
																					)
	{
	    return SqlBaseTrasladoCamasDao.actualizarFechaHoraFinalizacion(con, codigoCuenta, fechaFin, horaFin,observaciones);
	}
	
	
	
	
	/**
	 * Metodo que actualiza la fecha y hora de finalizacion de ocupacion de una cama 
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFin
	 * @param horaFin
	 * @return
	 */
	public boolean  actualizarFechaHoraFinalizacionNoTransaccional(	Connection con,int codigoCuenta,String fechaFin,String horaFin, String observaciones) 
	{
		return SqlBaseTrasladoCamasDao.actualizarFechaHoraFinalizacionNoTransaccional(con, codigoCuenta,fechaFin,horaFin,observaciones);
	}
	
	/**
	 *Metodo encargdo de cambiar el estado de la cama,
	 *este devuelve true para indicar operacion exitosa
	 *de lo contrario devuelve false.
	 *El HashMap aprametros contiene las siguientes key's
	 *--------------------------------------------------
	 *--nuevoEstadoCama --> Requerido
	 *--institucion --> Requerido
	 *--codigoCama --> Requerido
	 *--codigoPaciente --> Requerido
	 *--estadoCama --> Requerido
	 *-------------------------------------------------- 
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public boolean cambiarEstaReserva (Connection connection,HashMap parametros)
	{
		return SqlBaseTrasladoCamasDao.cambiarEstaReserva(connection, parametros);
	}
	
}
