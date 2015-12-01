/*
 * @(#)SqlBaseResponderCirugiasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */


package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de la Respuesta de Cirugias
 */
public class SqlBaseResponderCirugiasDao 
{ 
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseResponderCirugiasDao.class);
	
	/**
	 * Statement para consultar las peticiones de un paciente cargado en session
	 */
	private final static String consultarPeticionesPacienteStr=	" select cuenta, " +
																" paciente, " +
																" codigopeticion, " +
																" fechacirugia, " +
																" consecutivoordenes, " +
																" estadoMedico, "+
																" numerosolicitud, " +
																" solicitante, " +
																" especialidad," +
																" pyp," +
																"justificacionsolicitud " +
																" from " +
																	"(" +
																		"SELECT " +
																			"null as cuenta," +
																			"null as ingreso, "+
																			"p.paciente as paciente,p.codigo as codigoPeticion,  " +
																			"(to_char(p.fecha_cirugia,'DD/MM/YYYY')) as fechaCirugia, " +
																			"'' as consecutivoOrdenes, " +
																			"'' as estadoMedico, "+
																			"'' as numeroSolicitud, " +
																			"administracion.getnombremedico(p.solicitante) as solicitante, " +
																			"''  as especialidad, " +
																			"p.estado_peticion as estadopeticion,  " +
																			"p.fecha_peticion as fechapeticion, " +
																			""+ValoresPorDefecto.getValorFalseParaConsultas()+" as pyp, " +
																			"p.institucion as institucion," +
																			"null as  justificacionsolicitud " +
																		"FROM peticion_qx  p  " +
																		//"INNER JOIN solicitudes_cirugia sc ON (p.codigo=sc.codigo_peticion) " +
																		"LEFT OUTER JOIN peticiones_servicio ps on(p.codigo=ps.peticion_qx) " +
																		"LEFT OUTER JOIN servicios se on(ps.servicio=se.codigo) " +
																		"where " +
																			"p.paciente = ? and "+ 
																			"p.institucion = ? and "+
																			"p.codigo not in(select codigo_peticion from solicitudes_cirugia where codigo_peticion is not null) " +
																			//"AND sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' " +
																			"AND (se.tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"' OR se.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"') "+
																		"UNION " +
																		"select " +
																			"c.id as cuenta, " +
																			"c.id_ingreso as ingreso, "+
																			"p.paciente as paciente, " +
																			"p.codigo as codigoPeticion, " +
																			"(to_char(p.fecha_cirugia,'DD/MM/YYYY')) as fechaCirugia, " +
																			"s.consecutivo_ordenes_medicas||'' as consecutivoOrdenes, " +
																			"getestadosolhis(s.estado_historia_clinica)||'' as estadoMedico, "+
																			"sc.numero_solicitud||'' as numeroSolicitud, " +
																			"administracion.getnombremedico(s.codigo_medico) as solicitante, " +
																			"getnombreespecialidad(s.especialidad_solicitante) as especialidad, " +
																			"p.estado_peticion as estadopeticion, " +
																			"p.fecha_peticion as fechapeticion," +
																			"s.pyp as pyp, " +
																			"p.institucion as institucion, " +
																			"s.justificacion_solicitud as  justificacionsolicitud " +
																		"FROM cuentas c "+ 
																		"INNER JOIN solicitudes s on(s.cuenta = c.id) "+ 
																		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud = s.numero_solicitud) "+  
																		"INNER JOIN peticion_qx p ON(p.codigo = sc.codigo_peticion) "+
																		"LEFT OUTER JOIN peticiones_servicio ps on(ps.peticion_qx=p.codigo) "+ 
																		"LEFT OUTER JOIN servicios se on(ps.servicio=se.codigo)  " + 
																		"WHERE " +
																		"c.id_ingreso = ? and " +
																		"sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' "+
																		"AND se.tipo_servicio IN ('"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioPartosCesarea+"') "+
																	") tabla" +
																" where estadopeticion IN ("+ConstantesBD.codigoEstadoPeticionProgramada+","+ConstantesBD.codigoEstadoPeticionReprogramada+","+ConstantesBD.codigoEstadoPeticionPendiente+","+ConstantesBD.codigoEstadoPeticionAtendida+")  " +
																" ";
	
	
	/**
	 * Statement para consultar las peticiones de un paciente cargado en session 
	 * validando que exista la orden 
	 */
	private final static String consultarPeticionesPacienteOrdenStr=													
										"SELECT c.id as cuenta, " +
										"p.paciente as paciente, " +
										"p.codigo as codigoPeticion, " +
										"(to_char(p.fecha_cirugia,'DD/MM/YYYY')) as fechaCirugia, " +
										"s.consecutivo_ordenes_medicas||'' as consecutivoOrdenes, " +
										"getestadosolhis(s.estado_historia_clinica) as estadoMedico, "+
										"sc.numero_solicitud||'' as numeroSolicitud, " +
										"administracion.getnombremedico(s.codigo_medico) as solicitante, " +
										"getnombreespecialidad(s.especialidad_solicitante) as especialidad, " +
										"p.estado_peticion as estadopeticion, " +
										"p.fecha_peticion as fechapeticion," +
										"s.pyp as pyp, " +
										"s.justificacion_solicitud as justificacionsolicitud, " +
										"p.institucion as institucion " +
										"FROM peticion_qx p " +
										"inner join solicitudes_cirugia sc ON(sc.codigo_peticion=p.codigo) " +
										"inner join solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) " +
										"inner join cuentas c on (c.id=s.cuenta) " +
										"LEFT OUTER JOIN peticiones_servicio ps on(p.codigo=ps.peticion_qx) " +
										"LEFT OUTER JOIN servicios se on(ps.servicio=se.codigo) " +
										"WHERE " +
										"sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' "+
										"AND (se.tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"' OR se.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"') "+
										"AND p.institucion=? " +
										"AND (c.id = ? or (c.id is null and p.paciente=?)) " +
										"AND (p.estado_peticion="+ConstantesBD.codigoEstadoPeticionProgramada+" OR p.estado_peticion="+ConstantesBD.codigoEstadoPeticionReprogramada+" OR p.estado_peticion="+ConstantesBD.codigoEstadoPeticionPendiente+" OR p.estado_peticion="+ConstantesBD.codigoEstadoPeticionAtendida+")  " +
										" ";
	
	
	/**
	 * Statement para consultar las peticiones asociadas al usuario que ingreso en el sistema
	 */
	private final static String consultarPeticionesMedicoStr= "SELECT  DISTINCT "+ 
		"p.codigo as codigoPeticion, "+  
		"(to_char(p.fecha_peticion,'"+ConstantesBD.formatoFechaAp+"')) as fechaCirugia, "+  
		"p.fecha_peticion as fechaPeticionBD, "+  
		"s.consecutivo_ordenes_medicas as consecutivoOrdenes, "+  
		"CASE WHEN getestadosolhis(s.estado_historia_clinica) IS NULL THEN '' ELSE getestadosolhis(s.estado_historia_clinica) END AS estadoMedico, "+
		"sc.numero_solicitud as numeroSolicitud, "+  
		"(per.tipo_identificacion||' '||per.numero_identificacion) as tipoId, "+   
		"per.primer_apellido || coalesce(' ' || per.segundo_apellido,'') || ' ' || per.primer_nombre || coalesce(' '||per.segundo_nombre,'') as paciente, "+ 
		"s.pyp as pyp, " +
		"s.justificacion_solicitud as justificacionsolicitud, " +   
		"p.paciente as codigoPaciente "+  
		"FROM peticion_qx p "+  
		"INNER JOIN solicitudes_cirugia  sc ON(p.codigo=sc.codigo_peticion) "+  
		"INNER JOIN personas per ON(p.paciente=per.codigo) "+  
		"INNER JOIN cuentas cue ON(cue.codigo_paciente=per.codigo) "+  
		"INNER JOIN  centros_costo cc ON (cc.codigo=cue.area ) "+  
		"LEFT OUTER JOIN solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) "+  
		"WHERE "+ 
		"p.institucion=?  AND "+ 
		"p.estado_peticion IN ("+ConstantesBD.codigoEstadoPeticionPendiente+","+ConstantesBD.codigoEstadoPeticionProgramada+","+ConstantesBD.codigoEstadoPeticionReprogramada+","+ConstantesBD.codigoEstadoPeticionAtendida+") AND "+ 
		"sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' AND "+ 
		"( "+
			"s.centro_costo_solicitante in (select centro_costo from centros_costo_usuario where usuario=?)  OR "+ 
			"s.centro_costo_solicitado in (select centro_costo from centros_costo_usuario where usuario=?) "+ 
		") AND "+ 
		"cue.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")  AND "+ 
		"cc.centro_atencion=? "+  
		"ORDER BY fechaPeticionBD asc";
		
		
	
	
	/**
	 * Statement para consultar los servicios de la petición
	 */
	private static final String consultarServiciosPeticionStr = " SELECT ps.numero_servicio AS codigoServicio, " +
																" getcodigopropservicio2(ps.servicio, "+ConstantesBD.codigoTarifarioCups+") AS codCups, "+
																" (s.codigo||'-'||s.especialidad||' '||getnombreservicio(ps.servicio,"+ConstantesBD.codigoTarifarioCups+")) AS servicio, " +
																" getnombreespecialidad(s.especialidad) as especialidad, "+
																" "+ConstantesBD.codigoNuncaValido+" AS tipoCirugia "+
																" FROM peticiones_servicio ps "+
																" INNER JOIN servicios s ON(ps.servicio=s.codigo) "+
																//" INNER JOIN tipos_cirugia_inst tci ON(ps.tipo_cirugia=tci.codigo) "+
																" WHERE ps.peticion_qx=? " +
																" ORDER BY ps.numero_servicio asc";
	
	/**
	 * Método que carga las peticiones de un paciente cargado en session
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static ResultSetDecorator cargarPeticionesPaciente(Connection con, int codigoPaciente, int institucionPaciente, int idIngreso, int consecutivoOrdenesMedicas)  throws SQLException
	{
		String consulta=consultarPeticionesPacienteStr;
		if(consecutivoOrdenesMedicas>=0)
			consulta+=" AND consecutivoordenes="+consecutivoOrdenesMedicas;
		consulta+=" ORDER BY fechapeticion asc";
		
		logger.info("valor del sql >> "+consulta+" >> "+institucionPaciente+" >> "+idIngreso+" >> "+codigoPaciente);
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPaciente);
			cargarStatement.setInt(2, institucionPaciente);
			cargarStatement.setInt(3, idIngreso);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.warn("Error en la consulta de las peticiones del paciente : SqlBaseResponderCirugiasDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Método que carga las peticiones de un paciente cargado en session
	 * que posean peticion
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static ResultSetDecorator cargarPeticionesPacienteConOrden(Connection con, int codigoPaciente, int institucionPaciente, int idCuenta, int consecutivoOrdenesMedicas)  throws SQLException
	{
		String consulta=consultarPeticionesPacienteOrdenStr;
		if(consecutivoOrdenesMedicas>=0)
			consulta+=" AND consecutivoordenes="+consecutivoOrdenesMedicas;
		consulta+=" ORDER BY p.fecha_peticion asc";
		
		logger.info("valor del sql >> "+consulta+" >> "+institucionPaciente+" >> "+idCuenta+" >> "+codigoPaciente);
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, institucionPaciente);
			cargarStatement.setInt(2, idCuenta);
			cargarStatement.setInt(3, codigoPaciente);
			return new ResultSetDecorator(cargarStatement.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.warn("Error en la consulta de las peticiones del paciente Con Orden : SqlBaseResponderCirugiasDao "+e.toString());
			return null;
		}
	}
	
	
	
	/**
	 * Método para consultar las peticiones asociadas al usuario que ingreso en el sistema
	 * @param con
	 * @param usuario
	 * @param institucionUsuario 
	 * @return 
	 * @throws SQLException 
	 */
	public static ResultSetDecorator cargarPeticionesMedico(Connection con, String usuario, int institucionUsuario, int centroCosto, int centroAtencion)  throws SQLException
	{
		try
		{
//			usuario=usuario;
			logger.info("consultarPeticionesMedico: "+consultarPeticionesMedicoStr+"");
					//", institucionUsuario: "+institucionUsuario+", usuario: "+usuario+", centroAtencion: "+centroAtencion);
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarPeticionesMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, institucionUsuario);
			cargarStatement.setString(2, usuario);
			cargarStatement.setString(3, usuario);
			cargarStatement.setInt(4, centroAtencion);
			//cargarStatement.setInt(4, centroCosto);
			//cargarStatement.setString(2, usuario);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e) 
		{
			logger.warn("Error en la consulta de las peticiones del medico : SqlBaseResponderCirugiasDao "+e.toString());
			return null;  
		}
	}
	
	/**
	 * Método para cargar los servicios de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarServiciosPeticion(Connection con, int codigoPeticion)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarServiciosPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPeticion);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la consulta de los servicios de la peticion del paciente : SqlBaseResponderCirugiasDao "+e.toString());
			return null;
		}
	}
}