package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseConsultarAdmisionDao {
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	public static Logger logger=Logger.getLogger(SqlBaseConsultarAdmisionDao.class);
	
	/**
	 * Cadena para la consulta de todas las admisiones para el paciente cargado
	 */
	public static String cadenaConsultaIngresoCuentaPaciente="SELECT " +
															" i.consecutivo as consecutivo,"+
															" i.id as idingreso," +
															" i.estado as estadoingreso," +
															" i.fecha_ingreso as fechaingreso," +
															" i.hora_ingreso as horaingreso,"+
															" i.fecha_egreso as fechaegreso," +
															" i.hora_egreso as horaegreso,"+
															" c.id as cuenta," +
															" c.estado_cuenta as codestadocuenta," +
															" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
															" getnomorigenadmision(origen_admision) as origenadmision, "+
															" getnomcentrocosto(area) as centrocosto, "+
															" getcentroatencioncc(area) as centroatencion, "+
															" c.via_ingreso as codviaingreso, " +
															" getNombreViaIngresoTipoPac(c.id) as nomviaingreso, " +
															" ah.fecha_admision as fechaadmision, "+
															" ah.hora_admision||'' as horaadmision, "+
															" 'Piso:'||pi.nombre||' Hab. '||h.nombre||' Cama: '||cam.numero_cama||' Tipo Hab. '||tuc.nombre ||' Descripci�n: '|| cam.descripcion as cama, "+
															" m.numero_registro as registromedico, "+
															" CASE WHEN p.primer_nombre IS NULL THEN '' ELSE p.primer_nombre END as nommedico, "+
															" CASE WHEN p.primer_apellido IS NULL THEN '' ELSE p.primer_apellido END as apellidomedico, " +
															" getDescripEntidadSubXingreso(i.id) AS descentidadsub, "+
															" administracion.getnombreusuario(i.usuario_modifica) as usuarioingreso "+
															" FROM cuentas c " +
															" INNER JOIN  admisiones_hospi ah ON (c.id=ah.cuenta) " +
															" INNER JOIN  ingresos i on(c.id_ingreso=i.id) " +
															" LEFT OUTER JOIN medicos m ON (ah.codigo_medico=m.codigo_medico) "+
															" LEFT OUTER JOIN personas p ON (m.codigo_medico=p.codigo) "+
															" LEFT OUTER JOIN camas1 cam ON (ah.cama=cam.codigo) "+
															" LEFT OUTER JOIN habitaciones h ON (cam.habitacion=h.codigo) "+
															" LEFT OUTER JOIN pisos pi ON (h.piso=pi.codigo) "+
															" LEFT OUTER JOIN tipos_usuario_cama tuc ON (cam.tipo_usuario_cama=tuc.codigo) "+
															" WHERE c.codigo_paciente=? AND (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva +
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial +
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+")"  +
															" AND (i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'" +
															" OR i.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')"+
															" AND c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+
															" UNION"+
															" SELECT "+
															" i.consecutivo as consecutivo,"+
															" i.id as idingreso," +
															" i.estado as estadoingreso," +
															" i.fecha_ingreso as fechaingreso," +
															" i.hora_ingreso as horaingreso,"+
															" i.fecha_egreso as fechaegreso," +
															" i.hora_egreso as horaegreso,"+
															" c.id as cuenta," +
															" c.estado_cuenta as codestadocuenta," +
															" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
															" getnomorigenadmision(origen_admision) as origenadmision, "+
															" getnomcentrocosto(area) as centrocosto, "+
															" getcentroatencioncc(area) as centroatencion, "+
															" c.via_ingreso as codviaingreso, " +
															" getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
															" au.fecha_admision as fechaadmision, "+
															" au.hora_admision||'' as horaadmision, "+
															" 'P. '||pi.nombre||' H. '||h.nombre||' C. '||cam.numero_cama||' U. '||tuc.nombre as cama, "+
															" m.numero_registro as registromedico, "+
															" p.primer_nombre as nommedico, "+
															" p.primer_apellido as apellidomedico," +
															" getDescripEntidadSubXingreso(i.id) AS descentidadsub, "+ 
															" administracion.getnombreusuario(i.usuario_modifica) as usuarioingreso "+
															" FROM cuentas c " +
															" INNER JOIN admisiones_urgencias au ON (c.id=au.cuenta) " +
															" INNER JOIN ingresos i on(c.id_ingreso=i.id) " +
															" LEFT OUTER JOIN medicos m ON (au.codigo_medico=m.codigo_medico) "+
															" LEFT OUTER JOIN personas p ON (m.codigo_medico=p.codigo) "+
															" LEFT OUTER JOIN camas1 cam ON (au.cama_observacion=cam.codigo) "+
															" LEFT OUTER JOIN habitaciones h ON (cam.habitacion=h.codigo) "+
															" LEFT OUTER JOIN pisos pi ON (h.piso=pi.codigo) "+
															" LEFT OUTER JOIN tipos_usuario_cama tuc ON (cam.tipo_usuario_cama=tuc.codigo) "+
															" WHERE c.codigo_paciente=? AND (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva +
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial +
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+")"  +
															" AND (i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'"+
															" OR i.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')"+
															" AND c.via_ingreso="+ConstantesBD.codigoViaIngresoUrgencias+
															" UNION "+ 
															" SELECT " +
															" i.consecutivo as consecutivo,"+
															" i.id as idingreso," +
															" i.estado as estadoingreso," +
															" i.fecha_ingreso as fechaingreso," +
															" i.hora_ingreso as horaingreso,"+
															" i.fecha_egreso as fechaegreso," +
															" i.hora_egreso as horaegreso,"+
															" c.id as cuenta," +
															" c.estado_cuenta as codestadocuenta," +
															" getnombreestadocuenta(c.estado_cuenta) as nomestadocuenta, " +
															" getnomorigenadmision(origen_admision) as origenadmision, "+
															" getnomcentrocosto(area) as centrocosto, "+
															" getcentroatencioncc(area) as centroatencion, "+
															" c.via_ingreso as codviaingreso, " +
															" getnombreviaingreso(c.via_ingreso) as nomviaingreso, " +
															" c.fecha_apertura as fechaadmision, "+
															" c.hora_apertura||'' as horaadmision, "+
															" '' as cama,"+
															" '' as registromedico,"+
															" '' as nommedico,"+
															" '' as apellidomedico," +
															" getDescripEntidadSubXingreso(i.id) AS descentidadsub, "+
															" administracion.getnombreusuario(i.usuario_modifica) as usuarioingreso "+
															" FROM cuentas c " +
															" INNER JOIN  ingresos i on(c.id_ingreso=i.id) " +
															" WHERE c.codigo_paciente=? AND (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva +
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial +
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+
															" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+")"  +
															" AND (i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'" +
															" OR i.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')"+
															" AND c.via_ingreso="+ConstantesBD.codigoViaIngresoAmbulatorios;
															
	/**
	 * Cadena para la consulta del paciente
	 */
	private static String cadenaConsultaPaciente="SELECT p.primer_apellido as primerapellido,"+
													"p.segundo_apellido as segundoapellido,"+
													"p.primer_nombre as primernombre,"+
													"p.segundo_nombre as segundonombre,"+
													"p.numero_identificacion as numeroidentificacion,"+
													"p.tipo_identificacion as tipoidentificacion,"+
													"getnombreciudad(codigo_pais_nacimiento,codigo_departamento_nacimiento,codigo_ciudad_nacimiento)"+
													" as ciudad,p.fecha_nacimiento as fechanacimiento,"+
													" getedaddetallada(p.fecha_nacimiento,current_date) as edad,"+
													"s.nombre as sexo,ec.nombre as estadocivil,p.direccion as direccion,"+
													"getdescripcionbarrio(codigo_barrio_vivienda) as barrio,"+
													"getnombreciudad(codigo_pais_vivienda,codigo_departamento_vivienda,codigo_ciudad_vivienda) as ciudadvive,"+
													"p.telefono as telefono,"+
													"pac.historia_clinica as historiaclinica,"+
													"zd.nombre as zona,o.nombre as ocupacion, "+
													"ts.nombre as tiposangre "+
													"FROM personas p "+
													"INNER JOIN sexo s ON (s.codigo=p.sexo) "+
													"INNER JOIN pacientes pac ON (pac.codigo_paciente=p.codigo) "+
													"INNER JOIN zonas_domicilio zd ON (pac.zona_domicilio=zd.acronimo) "+
													"INNER JOIN ocupaciones o ON (pac.ocupacion=o.codigo) "+
													"INNER JOIN estados_civiles ec ON (p.estado_civil=ec.acronimo) "+
													"INNER JOIN tipos_sangre ts ON (pac.tipo_sangre=ts.codigo) "+
													"WHERE pac.codigo_paciente=?";
	
	/**
	 * Cadena para la consulta de la informacion de le entidad responsable
	 */
	private static String cadenaConsultaEntidadResponsable="SELECT " +
			                                                "sc.sub_cuenta AS numsubcuenta, "+
															"CASE WHEN c.tipo_regimen='P' then null else numero_identificacion end as nit, "  +
															"getnomtiporegimen(tipo_regimen) as regimen,getnombreconvenio(convenio) as convenio, "+ 
															"getnombretipoafiliado(tipo_afiliado) as tipoafiliado, "+
															"CASE WHEN c.tipo_regimen='P' then 'PARTICULAR' else e.razon_social end as razonsocial, "  +
															"sc.nro_carnet as numerocarnet, " +
															//"sc.nro_autorizacion as numeroautorizacion, "+
															"dg.tipo_documento as documentogarantia,"+
															"dg.entidad_financiera as banco "+
															"FROM sub_cuentas sc "+
															"INNER JOIN convenios c ON (sc.convenio=c.codigo) "+
															"INNER JOIN empresas e ON (c.empresa=e.codigo) "+
															"INNER JOIN terceros t ON (e.tercero=t.codigo) "+
															"LEFT OUTER JOIN carterapaciente.documentos_garantia dg ON (sc.ingreso=dg.ingreso) "+
															"WHERE sc.nro_prioridad=1 AND sc.ingreso=?";
	/**
	 * Cadena para consultar numero autorizacion asociado a una subcuenta
	 */
	private static String cadenaConsultaNumeroAutorizacion1="SELECT " +
														   "aut.sub_cuenta AS numsubcuenta, " +
														   "aut.tipo AS tipoatuorizacion, " +
														   "detaut.estado AS estadodetauto, " +
														   "detaut.activo AS activodetauto, " +
														   "coalesce (respauto.numero_autorizacion,'') AS numeroautorizacion " +
														   "FROM autorizaciones aut " +
														   "INNER JOIN det_autorizaciones detaut ON (aut.codigo = detaut.autorizacion AND detaut.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado +"' AND detaut.activo = '"+ConstantesBD.acronimoSi +"') " +
														   "INNER JOIN resp_autorizaciones respauto ON (respauto.det_autorizacion = detaut.codigo) " +
														   "WHERE aut.sub_cuenta = ? AND aut.cuenta = ? AND aut.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision +"' "; 
														   
	/**
	 * 
	 */
	private static String cadenaConsultaNumeroAutorizacion2="SELECT " +
															"coalesce(numero_verificacion,'') AS numverificacion " +
															"FROM verificaciones_derechos " +
															"WHERE sub_cuenta = ? ";
	
	/**
	 * Cadena para la consulta de responsable paciente
	 */
	private static String cadenaConsultaResponsablePaciente="SELECT primer_apellido,segundo_apellido,primer_nombre,segundo_nombre,"+
															"tipo_identificacion,numero_identificacion,"+
															"getnombreciudad(codigo_pais_doc,codigo_depto_doc,codigo_ciudad_doc) as ciudaddocumento,"+
															"getnombreciudad(codigo_pais,codigo_depto,codigo_ciudad) as ciudadvive,"+
															"direccion,telefono,relacion_paciente "+
															"FROM responsables_pacientes "+
															"INNER JOIN cuentas c ON (c.codigo_responsable_paciente=codigo) "+
															"WHERE c.id=?";
	
	/**
	 * Consulta del ingreso cuenta del paciente
	 * @param con
	 * @param codigoPersona
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaIngresoCuentaPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			ps.setInt(2, codigoPersona);
			ps.setInt(3, codigoPersona);
			logger.info("consulta \n\n\n\n -->"+(cadenaConsultaIngresoCuentaPaciente+"").replace("?", codigoPersona+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE CUENTAS DEL PACIENTE "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Cadena para la consulta de pacientes
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarPaciente(Connection con, int codigoPersona) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			logger.info("\n\n CONSULTA DE PACIENTES  CADENA >> "+cadenaConsultaPaciente+ "  - CodPersona: "+codigoPersona);	
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE PACIENTES "+e);
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE PACIENTES  CADENA >> "+cadenaConsultaPaciente+ "  - CodPersona: "+codigoPersona);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consultar los responsables del paciente
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap<String, Object> consultarEntidadResponsable(Connection con, int codigoIngreso,int codCuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
	
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEntidadResponsable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("numeroautorizacion_0","");
			
			
			logger.info(" \n consultarEntidadResponsable >> CODIGO SUBCUENTA  >> "+mapa.get("numsubcuenta_0")+"  CODIGO CUENTA >>  "+codCuenta + "    NUMERO AUTORIZACION >>" + mapa.get("numeroautorizacion_0") );
			PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaNumeroAutorizacion1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps2.setInt(1, Utilidades.convertirAEntero(mapa.get("numsubcuenta_0").toString()));
			ps2.setInt(2, codCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps2.executeQuery());
			   if(rs.next())
				 {
					    if(rs.getInt("numeroautorizacion")==0)
					     {
					    	PreparedStatementDecorator ps3= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaNumeroAutorizacion2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps3.setInt(1, Utilidades.convertirAEntero(mapa.get("numsubcuenta_0").toString()));
							ResultSetDecorator rs2=new ResultSetDecorator(ps3.executeQuery());
								if(rs2.next())
								{
									mapa.put("numeroautorizacion_0", rs2.getString("numverificacion"));
								}
								 
							
					     }else
					     {
					    	 mapa.put("numeroautorizacion_0", rs.getInt("numeroautorizacion"));
					     }
				}else
				 {
					PreparedStatementDecorator ps4= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaNumeroAutorizacion2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps4.setInt(1, Utilidades.convertirAEntero(mapa.get("numsubcuenta_0").toString()));
					ResultSetDecorator rs3=new ResultSetDecorator(ps4.executeQuery());
					
						if(rs3.next())
						 {
							 if(!rs3.getString("numverificacion").equals(""))
						     {
							    mapa.put("numeroautorizacion_0", rs3.getString("numverificacion"));
						     } 
							
						    	 
						 }
					
				 }
			
			
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE ENTIDADES RESPONSABLES "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consultar responsables paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap<String, Object> consultarResponsablePaciente(Connection con, int cuenta) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaResponsablePaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cuenta);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR AL EJECUTAR LA CONSULTA DE ENTIDADES RESPONSABLES "+e);
			e.printStackTrace();
		}
		return mapa;
	}
															
}
