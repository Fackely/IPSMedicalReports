package com.princetonsa.dao.sqlbase.tramiteReferencia;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.tramiteReferencia.TramiteReferencia;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;


/**
 * Sql Tramitar Referencia
 * @author Jose Eduardo Arias Doncel
 * */
public class SqlBaseTramiteReferenciaDao
{
	private static Logger logger = Logger.getLogger(TramiteReferencia.class);
	
	//------------------------------Atributos 
	
	/**
	 * Cadena Statica de sentencia sql de consulta de Listado de Referencia 
	 * */	
	private static final String cadenaConsultaReferencia = "SELECT r.numero_referencia AS numeroreferencia, " +
														   "r.codigo_paciente AS codigopaciente, " +
														   "getnombrepersona(codigo_paciente) AS infopaciente, " +
														   "r.institucion AS institucion, " +
														   "r.ingreso AS ingreso, " +
														   "r.referencia AS referencia, " +
														   "to_char(r.fecha_referencia, 'YYYY-MM-DD') AS fechareferencia, " +
														   "r.hora_referencia AS horareferencia, " +
														   "r.tipo_referencia AS tiporeferencia, " +
														   "r.tipo_atencion AS tipoatencion, " +
														   "r.estado AS estado, " +
														   "r.centro_costo AS centrocosto, " +
														   "'' AS tiempoespera " +
														   "FROM historiaclinica.referencia r ";
	
	
	/**************************************************************************
	/***************************Grupo de sentencias sql de tramite de referencia 
	 
	
	/**
	 * Cadena sentencia sql de consulta de tramite de referencia  
	 * */
	private static final String cadenaConsultaTramiteReferencia = "SELECT numero_referencia_tramite AS numeroreferenciatramite, " +
																  "apellidos_responsable AS apellidosresponsable, " +
																  "nombres_responsable AS nombresresponsable, " +
																  "direccion_responsable AS direccionresponsable, " +
																  "telefono_responsable AS telefonoresponsable, " +
																  "finalizar_tramite AS finalizar, " +
																  "usuario_modifica AS usuariomodifica, " +
																  "fecha_modifica AS fechamodifica, " +
																  "hora_modifica AS horamodifica, " +
																  "'"+ConstantesBD.acronimoSi+"' AS estabd  " +
																  "FROM historiaclinica.tramite_referencia " +
																  "WHERE  numero_referencia_tramite=? ";
	
	/**
	 * Cadena de actualizacon de tramite de referencia
	 * */
	private static final String cadenaActualizacionTramiteReferencia = "UPDATE historiaclinica.tramite_referencia " +
																	   "SET apellidos_responsable=?, " +
																	   "nombres_responsable=?, " +
																	   "direccion_responsable=?, " +
																	   "telefono_responsable=?, " +
																	   "finalizar_tramite=?, " +
																	   "usuario_modifica=?, " +
																	   "fecha_modifica=?, " +
																	   "hora_modifica=? " +
																	   "WHERE numero_referencia_tramite=? ";
	
	
	/**
	 * Cadena de Inserccion de datos en tramite referencia 
	 * */
	private static final String cadenaInserccionTramiteReferencia = "INSERT INTO historiaclinica.tramite_referencia" +
																    "(numero_referencia_tramite,apellidos_responsable,nombres_responsable,direccion_responsable,telefono_responsable,finalizar_tramite,usuario_modifica,fecha_modifica,hora_modifica) " +
																    "VALUES(?,?,?,?,?,?,?,?,?)";
	
	
	/*************************************************************************************
	/***************************Grupo de sentencias sql de Instituciones Tramite Referencia 
	
	/**
	 * Cadena de consulta de instituciones tramite referencia
	 * */
	private static final String cadenaConsultaInstitucionesTramiteReferencia = "SELECT " +
																			   "itr.numero_referencia_tramite AS numeroreferenciatramite, " +
																			   "itr.institucion_referir AS institucionreferir, " +
																			   "isr.descripcion AS descripcioninstitucionreferir, " +
																			   "itr.institucion AS institucion, " +
																			   "itr.departamento_referir || '"+ConstantesBD.separadorSplit+"' || itr.ciudad_referir AS departamentoreferir, " +
																			   "dep.descripcion AS departamentonombre, " +																			   
																			   "itr.ciudad_referir AS ciudadreferir, " +
																			   "itr.pais_referir AS paisreferir, " +
																			   "ciu.descripcion AS ciudadnombre, " +
																			   "p.descripcion AS paisnombre, " +
																			   "isr.tipo_red AS tipored, " +																			   
																			   "'"+ConstantesBD.acronimoSi+"' AS estabd " +
																			   "FROM historiaclinica.instit_tramite_referencia itr " +
																			   "INNER JOIN historiaclinica.instituciones_sirc isr ON (itr.institucion_referir=isr.codigo AND itr.institucion=isr.institucion) " +
																			   "INNER JOIN administracion.departamentos dep ON (dep.codigo_departamento=itr.departamento_referir AND dep.codigo_pais=itr.pais_referir) " +
																			   "INNER JOIN administracion.ciudades ciu ON (ciu.codigo_ciudad=itr.ciudad_referir AND ciu.codigo_departamento=itr.departamento_referir AND ciu.codigo_pais=itr.pais_referir) " +
																			   "INNER JOIN administracion.paises p ON (p.codigo_pais=itr.pais_referir) ";
																			
	
	/**
	 * Cadena de actualizacion de instituciones tramite referencia 
	 * */
	private static final String cadenaActualizacionInstitucionesTramiteReferencia = "UPDATED historiaclinica.instit_tramite_referencia SET " +																					
																					"institucion_referir = ?, " +																					
																					"institucion = ?, " +
																					"departamento_referir = ?, " +
																					"ciudad_referir = ? " +
																					"pais_referir = ? " +
																					"WHERE numero_referencia_tramite = ? AND institucion_referir = ? AND institucion = ? ";
																	
	/**
	 * Inserccion de instituciones tramite referencia
	 * */
	private static final String cadenaInsercionInstitucionesTramiteReferencia = "INSERT INTO historiaclinica.instit_tramite_referencia " +
																				"(numero_referencia_tramite,institucion_referir,institucion,departamento_referir,ciudad_referir,pais_referir) " +
																				"VALUES(?,?,?,?,?,?) ";
	
	
	/***************************************************************************************
	/****************************Grupo de sentencias sql de Servicios Insititucion Referencia 
	
	/**
	 * Cadena de consulta de servicios instituciones referencia
	 * */
	private static final String cadenaConsultaServiciosInstitucionReferencia = "SELECT " +
																			   "sir.numero_referencia_tramite AS numeroreferenciatramite, " +
																			   "sir.codigo_servicio_sirc AS codigoserviciosirc, " +
																			   "ss.descripcion AS nombreserviciosirc, " +																			   
																			   "sir.codigo_servicio AS codigoservicio, " +
																			   "getcodigopropservicio2(sir.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigocups, " +
																			   "getnombreservicio(sir.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombrecups, " +
																			   "sir.institucion_referir AS institucionreferir, " +
																			   "sir.institucion AS institucion, " +																			   
																			   "sir.activo AS activo, " +
																			   "sir.estado AS estado, " +
																			   "CASE WHEN sir.motivo IS NOT NULL THEN sir.motivo " +
																			   "ELSE "+ConstantesBD.codigoNuncaValido+" END AS motivo, " +
																			   "sir.funcionario_contactado AS funcionariocontactado, " +
																			   "sir.funcionario_contactado AS funcionariocontactadoold, " +
																			   "sir.cargo AS cargo, " +
																			   "sir.cargo AS cargoold, " +
																			   "sir.fecha_tramite AS fechatramite, " +
																			   "sir.hora_tramite AS horatramite, " +
																			   "sir.numero_verificacion AS numeroverificacion, " +
																			   "sir.observaciones AS observaciones, " +
																			   "sir.usuario_modifica AS usuariomodifica, " +
																			   "sir.fecha_modifica AS fechamodifica, " +
																			   "sir.hora_modifica AS horamodifica, " +
																			   "'"+ConstantesBD.acronimoSi+"' AS estabd " +
																			   "FROM historiaclinica.servic_instit_referencia sir " +
																			   "INNER JOIN historiaclinica.servicios_sirc ss ON (ss.codigo=sir.codigo_servicio_sirc) ";
	
	/**
	 * Cadena de Consulta de Historial de Servicios Instituciones Referencia 
	 * */
	private static final String cadenaConsultaHistorialServiciosInstitucion =  "SELECT "+ 
		"hsi.codigo AS codigo, "+
		"hsi.codigo_servicio_sirc AS codigo_servicio_sirc, "+
		"hsi.codigo_servicio AS codigo_servicio, "+
		"sc.descripcion || ' - ' || '(' || getcodigopropservicio2(hsi.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") || ') ' || getnombreservicio(hsi.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreserviciosirc, "+
		"hsi.funcionario_contactado AS funcionariocontactado, "+
		"hsi.estado AS estado, "+
		"hsi.cargo AS cargo, "+
		"to_char(hsi.fecha_tramite,'"+ConstantesBD.formatoFechaAp+"') AS fechatramite, "+
		"hsi.hora_tramite AS horatramite, "+
		"CASE WHEN hsi.motivo IS NULL THEN '' ELSE ms.descripcion END AS motivo "+ 
		"FROM his_serv_inst_referencia hsi "+ 
		"INNER JOIN servicios_sirc sc ON(sc.codigo=hsi.codigo_servicio_sirc AND sc.institucion=hsi.institucion) "+ 
		"LEFT OUTER JOIN motivos_sirc ms ON(ms.consecutivo=hsi.motivo) "+ 
		"WHERE "+ 
		"hsi.institucion_referir = ? AND hsi.institucion = ? AND hsi.numero_referencia_tramite = ?  " +
		"ORDER BY hsi.fecha_tramite, hsi.hora_tramite";
	
	
	/***************************************************************************************
	/****************************Grupo de sentencias sql de Traslado Paciente
	
	/*
	 * Cadena de Consulta de Traslado Paciente
	 * */
	private static final String cadenaConsultaTrasladoPaciente = "SELECT " +
			"itp.numero_referencia_tramite AS numeroreferenciatramite, " +
			"itp.institucion_sirc AS institucionsirc, " +
			"isr.descripcion AS descripcioninstitucionsirc, " +			
			"itp.institucion AS institucion ," +
			"itp.estado AS estado, " +
			"CASE WHEN motivo IS NOT NULL THEN itp.motivo||'' " +
			"ELSE '"+ConstantesBD.codigoNuncaValido+"' END AS motivo, " +			
			"itp.funcionario_contactado AS funcionariocontactado, " +
			"itp.funcionario_contactado AS funcionariocontactadoold, " +
			"itp.activo AS activo, "+			
			"itp.cargo AS cargo, " +
			"itp.cargo AS cargoold, " +
			"to_char(itp.fecha_tramite,'"+ConstantesBD.formatoFechaAp+"')  AS fechatramite, " +
			"itp.hora_tramite AS horatramite, " +
			"itp.responsable_tras_ambulancia AS responsabletrasambulancia, " +
			"itp.numero_movil AS numeromovil," +
			"itp.placa AS placa, " +
			"itp.observaciones AS observaciones, " +
			"itp.usuario_modifica AS usuario_modifica, " +
			"itp.fecha_modifica AS fechamodifica, " +
			"itp.hora_modifica AS hora_modifica," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd,  " +
			"'"+ConstantesBD.acronimoNo+"' AS hayerrores " +			
			"FROM historiaclinica.instituc_traslado_paciente itp " +
			"INNER JOIN historiaclinica.instituciones_sirc isr ON (itp.institucion_sirc=isr.codigo AND itp.institucion=isr.institucion) ";
	
	/**
	 * Cadena de Inserccion de Traslado Paciente
	 * */
	private static final String cadenaInserccionTrasladoPaciente = "INSERT INTO historiaclinica.instituc_traslado_paciente (" +
			"numero_referencia_tramite, " +
			"institucion_sirc, " +
			"institucion," +
			"estado, " +
			"motivo, " +			
			"funcionario_contactado, " +
			"activo, " +
			"cargo, " +
			"fecha_tramite, " +
			"hora_tramite, " +
			"responsable_tras_ambulancia, " +
			"numero_movil," +
			"placa, " +
			"observaciones, " +
			"usuario_modifica, " +
			"fecha_modifica, " +
			"hora_modifica)  " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * Cadena de Actualizacion de Traslado Paciente
	 * */
	private static final String cadenaActualizacionTrasladoPaciente = "UPDATE historiaclinica.instituc_traslado_paciente SET " +
			"estado=?, " +
			"motivo=?, " +			
			"funcionario_contactado=?, " +
			"activo=?, " +
			"cargo=?, " +
			"fecha_tramite=?, " +
			"hora_tramite=?, " +
			"responsable_tras_ambulancia=?, " +
			"numero_movil=?, " +
			"placa=?, " +
			"observaciones=?, " +
			"usuario_modifica=?, " +
			"fecha_modifica=?, " +
			"hora_modifica=? " +
			"WHERE numero_referencia_tramite=? AND " +
			"institucion_sirc=? AND  " +
			"institucion=? ";
	
	/**
	 * Cadena de Actualizacion de Activo, Traslado Paciente
	 * */
	private static final String cadenaActualizacionActivoTrasladoPaciente ="UPDATE historiaclinica.instituc_traslado_paciente SET " +
			"activo = ?, " +
			"usuario_modifica=?, " +
			"fecha_modifica=?, " +
			"hora_modifica=? " +
			"WHERE numero_referencia_tramite=? AND " +
			"institucion_sirc=? AND  " +
			"institucion=? ";
	
	/**
	 * Cadena de Consulta Historial de Traslado Paciente
	 * */
	private static final String cadenaConsultaHistorialTrasladoPaciente = "SELECT " +
			"codigo AS codigo, " +
			"numero_referencia_tramite AS numeroreferenciatramite, " +
			"institucion_sirc AS institucionsirc, " +
			"institucion AS institucion, " +
			"estado AS estado, " +
			"CASE WHEN motivo IS NOT NULL THEN motivo " +
			"ELSE '"+ConstantesBD.codigoNuncaValido+"' END AS motivo, " +			
			"funcionario_contactado AS funcionariocontactado, " +			
			"cargo AS cargo, " +
			"to_char(fecha_tramite,'"+ConstantesBD.formatoFechaAp+"') AS fechatramite, "+			
			"hora_tramite AS horatramite, " +
			"responsable_tras_ambulancia AS responsabletrasambulancia, " +
			"numero_movil AS numeromovil," +
			"placa AS placa, " +
			"observaciones AS observaciones, " +
			"usuario_modifica AS usuario_modifica, " +
			"fecha_modifica AS fechamodifica, " +
			"hora_modifica AS hora_modifica," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +			
			"FROM historiaclinica.his_institu_traslado_paciente " +
			"WHERE numero_referencia_tramite = ? AND institucion_sirc = ? AND institucion = ? " +
			"ORDER BY fecha_tramite DESC, hora_tramite DESC "; 
	
	/***********************************************************
	/****************************Declaracion de Vectores String 
	  
	 
	/**
	 * Vector String, guarda el valor de indices del mapa
	 * */
	private static final String[] indicesListadoReferenciaMapa= {"numeroreferencia_","codigopaciente_","infopaciente_","institucion_","referencia_","ingreso_","fechareferencia_","horareferencia_","tiporeferencia_","tiporeferencianom_","tipoatencion_","tipoatencionnom_","tiempoespera_","estado_"};
	
	/**
	 * Vector String, guarda el valor del indices del mapa tramite referencia
	 * */
	private static final String[] indicesTramiteReferenciaMapa = {"numeroreferenciatramite","apellidosresponsable","nombresresponsable","direccionresponsable","telefonoresponsable","finalizar","usuariomodifica","fechamodifica","horamodifica","estabd"};
	
	/**
	 * Vector String, guarda el valor del indices del mapa instituciones tramite referencia 
	 * */
	private static final String[] indicesInstitucionesTramiteReferenciaMapa = {"numeroreferenciatramite_","institucionreferir_","descripcioninstitucionreferir_","institucion_","departamentoreferir_","departamentonombre_","ciudadreferir_","ciudadnombre_","paisreferir_","paisnombre_","tipored_","estabd_"};
	
	/**
	 * Vectir String, guarda el valor del indices del mapa servicios institucion referencia 
	 * */
	private static final String[] indicesServiciosInstitucionReferenciaMapa = {"numeroreferenciatramite_","codigoserviciosirc_","codigoservicio_","codigocups_","nombrecups_","institucionreferir_","institucion_","activo_","estado_","motivo_","funcionariocontactado_","cargo_","fechatramite_","horatramite_","numeroverificacion_","observaciones_","usuariomodifica_","fechamodifica_","horamodifica_"};
	
	/**
	 * Vector String, guarda el valor del inidces del mapa traslados pacientes
	 * */
	private static final String[] indicesTrasladoPacienteMapa = {"numeroreferenciatramite_","institucionsirc_","descripcioninstitucionsirc_","institucion_","activo_","estado_","motivo_","funcionariocontactado_","cargo_","fechatramite_","horatramite_","responsabletrasambulancia_","numeromovil_","placa_","observaciones_","usuariomodifica_","fecha_modifica_","horamodifica_","estabd_"};
	//------------------------------Fin Atributos
	
	
	
	//------------------------------Metodos
	
	
	/**
	 * Consultar Listado Referencia 
	 * @param Connection 
	 * @param HashMap parametrosBusqueda (institucion,referencia,estado1,estado2)
	 * */
	public static HashMap consultarListadoReferencia(Connection con, HashMap parametrosBusqueda)
	{		
		HashMap mapa = new HashMap();
		String cadena = cadenaConsultaReferencia;
		
		try
		{			
			if(!parametrosBusqueda.get("centroatencion").equals(ConstantesBD.codigoNuncaValido+""))
				cadena+=" INNER JOIN administracion.centros_costo cc ON (r.centro_costo=cc.codigo AND cc.centro_atencion="+parametrosBusqueda.get("centroatencion").toString()+") ";
			
			cadena += " WHERE r.institucion = ? " +
				   	  " AND r.referencia = ? " +
				   	  " AND r.estado!= ? AND r.estado!= ?  " +
				      " ORDER BY r.fecha_referencia ASC ,r.hora_referencia ASC ";
			
			logger.info("cadenaConsultaReferencia / "+cadenaConsultaReferencia);
			logger.info("Institucion = "+parametrosBusqueda.get("institucion").toString());
			logger.info("referencia = "+parametrosBusqueda.get("referencia").toString());
			logger.info("estado1 = "+parametrosBusqueda.get("estado1").toString());
			logger.info("estado2 = "+parametrosBusqueda.get("estado2").toString());
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Integer.parseInt(parametrosBusqueda.get("institucion").toString()));
			ps.setString(2,parametrosBusqueda.get("referencia").toString());
			ps.setString(3,parametrosBusqueda.get("estado1").toString());
			ps.setString(4,parametrosBusqueda.get("estado2").toString());
			
			mapa =  UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch(SQLException e)
		{
			logger.error("ERROR ", e);			
		}
		
		mapa.put("INDICES_MAPA", indicesListadoReferenciaMapa);		
		return mapa;
	}
	
	
	
	/***************************************************************
	/****************************Grupo de Metodos Tramite Referencia
	
	/**
	 * Consultar Tramite Referencia
	 * @param Connection 
	 * @param int numero de referencia tramite  
	 * */
	public static HashMap consultarTramiteReferencia(Connection con, int numeroReferenciaTramite)
	{
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaTramiteReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroReferenciaTramite);			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		mapa.put("INDICES_MAPA",indicesTramiteReferenciaMapa);
		return mapa;
	}	
	
	
	
	/**
	 * Actualiza Tramite Referencia
	 * @param Connection 
	 * @param HashMap tramiteReferencia  
	 * */
	public static boolean actualizarTramiteReferencia(Connection con, HashMap tramiteReferencia)
	{
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionTramiteReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1,tramiteReferencia.get("apellidosresponsable").toString());
			ps.setString(2,tramiteReferencia.get("nombresresponsable").toString());
			ps.setString(3,tramiteReferencia.get("direccionresponsable").toString());
			ps.setString(4,tramiteReferencia.get("telefonoresponsable").toString());
			ps.setString(5,tramiteReferencia.get("finalizar").toString());
			ps.setString(6,tramiteReferencia.get("usuariomodifica").toString());
			ps.setString(7,tramiteReferencia.get("fechamodifica").toString());
			ps.setString(8,tramiteReferencia.get("horamodifica").toString());
			ps.setInt(9,Integer.parseInt(tramiteReferencia.get("numeroreferenciatramite").toString()));
			
			if(ps.executeUpdate()>0)
				return true;			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return false;
	}
	
	
	
	/**
	 * Inserta un registro en la tabla tramite referencia 
	 * Connection con
	 * HashMap tramiteReferencia 
	 * */
	public static boolean insertarTramiteReferencia(Connection con, HashMap tramiteReferencia)
	{
		try
		{						
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInserccionTramiteReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
						
			ps.setInt(1,Integer.parseInt(tramiteReferencia.get("numeroreferenciatramite").toString()));
			ps.setString(2,tramiteReferencia.get("apellidosresponsable").toString());
			ps.setString(3,tramiteReferencia.get("nombresresponsable").toString());
			ps.setString(4,tramiteReferencia.get("direccionresponsable").toString());
			ps.setString(5,tramiteReferencia.get("telefonoresponsable").toString());
			ps.setString(6,tramiteReferencia.get("finalizar").toString());
			ps.setString(7,tramiteReferencia.get("usuariomodifica").toString());
			ps.setString(8,tramiteReferencia.get("fechamodifica").toString());
			ps.setString(9,tramiteReferencia.get("horamodifica").toString());			
			
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return false;
	}
	
	
	/*****************************************************************************
	/****************************Grupo de Metodos Instituciones Tramite Referencia 
	
	/**
	 * Consulta instituciones tramite referencia
	 * @param Connection con
	 * @param HashMap parametros	 
	 * */
	public static HashMap consultarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = "";
		try
		{
			cadena = cadenaConsultaInstitucionesTramiteReferencia +" WHERE itr.institucion = "+parametros.get("institucion");
								
			if(parametros.containsKey("numeroreferenciatramite"))
				cadena+=" AND itr.numero_referencia_tramite = "+parametros.get("numeroreferenciatramite").toString();
			if(parametros.containsKey("institucionreferir"))
				cadena+=" AND itr.institucion_referir = '"+parametros.get("institucionreferir")+"' ";							
				
			cadena+=" ORDER BY isr.descripcion DESC ";			
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		mapa.put("INDICES_MAPA",indicesInstitucionesTramiteReferenciaMapa);
		return mapa;
	}
	
	
	/**
	 * Actualiza un registro en instituciones tramite referencia 
	 * @param Connection con
	 * @param HashMap Parametros 
	 * */
	public static boolean actualizarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionInstitucionesTramiteReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,parametros.get("institucionreferir").toString());			
			ps.setInt(2,Integer.parseInt(parametros.get("institucion").toString()));			
			ps.setInt(3,Integer.parseInt(parametros.get("departamentoreferir").toString()));
			ps.setInt(4,Integer.parseInt(parametros.get("ciudadreferir").toString()));
			ps.setInt(5,Integer.parseInt(parametros.get("paisreferir").toString()));
			ps.setInt(6,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));
			ps.setString(7,parametros.get("institucionreferir").toString());
			ps.setInt(8,Integer.parseInt(parametros.get("institucion").toString()));			
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return false;
	}
	
	/**
	 * Inserta un registro en instituciones tramite referencia
	 * @param Connection 
	 * @param HashMap parametros
	 * */
	public static boolean insertarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		if(parametros.containsKey("numeroreferenciatramite") && parametros.containsKey("institucionreferir") && parametros.containsKey("institucion") & parametros.containsKey("departamentoreferir") && parametros.containsKey("ciudadreferir") && parametros.containsKey("paisreferir")){
			try
			{
				// ********* LOGGER
				logger.info("Cadena SQL / "+cadenaInsercionInstitucionesTramiteReferencia);
				logger.info("numeroreferenciatramite "+parametros.get("numeroreferenciatramite").toString());
				logger.info("institucionreferir "+parametros.get("institucionreferir"));
				logger.info("institucion "+parametros.get("institucion"));
				if(parametros.get("departamentoreferir")!=null && !parametros.get("departamentoreferir").toString().equals(ConstantesBD.codigoNuncaValido))
				{
					logger.info("departamentoreferir "+parametros.get("departamentoreferir"));
					logger.info("ciudadreferir "+parametros.get("ciudadreferir"));
				}	
				else
				{
					logger.info("departamentoreferir null");
					logger.info("ciudadreferir null");
				}
				
				if(parametros.get("paisreferir")!=null && !parametros.get("paisreferir").toString().equals(ConstantesBD.codigoNuncaValido))
					logger.info("paisreferir "+parametros.get("paisreferir"));
				else
					logger.info("paisreferir null");
				// *********
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionInstitucionesTramiteReferencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroreferenciatramite").toString()));
				ps.setString(2,parametros.get("institucionreferir").toString());
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				
				if(parametros.get("departamentoreferir")!=null && !parametros.get("departamentoreferir").toString().equals(ConstantesBD.codigoNuncaValido))
				{
					ps.setString(4,parametros.get("departamentoreferir").toString());
					ps.setString(5,parametros.get("ciudadreferir").toString());
				}	
				else
				{
					ps.setNull(4,Types.VARCHAR);
					ps.setNull(5,Types.VARCHAR);
				}
				
				if(parametros.get("paisreferir")!=null && !parametros.get("paisreferir").toString().equals(ConstantesBD.codigoNuncaValido))
					ps.setString(6,parametros.get("paisreferir").toString());
				else
					ps.setNull(6,Types.VARCHAR);
				
				if(ps.executeUpdate()>0)
				{
					ps.close();
					return true;
				}
				ps.close();
			}	
			catch(SQLException e)
			{
				logger.error("ERROR", e);
			}
		} else {
			logger.error("ERROR: FALTAN PARAMETROS");
		}
		return false;
	}	
	
	/*******************************************************************************
	/****************************Grupo de Funciones Servicios Institucion Referencia
	 
	 /**
	  * Consultar Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public static HashMap consultarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = cadenaConsultaServiciosInstitucionReferencia;
		
		cadena+=" WHERE sir.institucion = "+parametros.get("institucion");
		
		if(parametros.containsKey("codigoserviciosirc"))
			cadena+=" AND sir.codigo_servicio_sirc = "+parametros.get("codigoserviciosirc");		
		if(parametros.containsKey("codigoservicio"))
			cadena+=" AND sir.codigo_servicio = "+parametros.get("codigoservicio");
		if(parametros.containsKey("institucionreferir"))
			cadena+=" AND sir.institucion_referir = '"+parametros.get("institucionreferir")+"' ";
		if(parametros.containsKey("numeroreferenciatramite"))
			cadena+=" AND sir.numero_referencia_tramite = "+parametros.get("numeroreferenciatramite");
		
		cadena+=" ORDER BY activo ";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}	
		catch(SQLException e)		
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA",indicesServiciosInstitucionReferenciaMapa);		
		return mapa;
	}	 
	
	/**
	  * Consultar Historial Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public static HashMap consultarHistorialServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaHistorialServiciosInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,parametros.get("institucionSIRC").toString());
			ps.setInt(2,Integer.parseInt(parametros.get("institucion").toString()));
			ps.setInt(3,Integer.parseInt(parametros.get("numeroReferencia").toString()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}	
		catch(SQLException e)		
		{
			e.printStackTrace();
		}
		
		return mapa;
	}	 
	
	/*******************************************************************************
	/****************************Grupo de Funciones Servicios Institucion Referencia

	/**
	  * Consultar Traslado Paciente 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public static HashMap consultarTrasladoPaciente(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = cadenaConsultaTrasladoPaciente;
		
		cadena+=" WHERE itp.institucion = "+parametros.get("institucion");
				
		if(parametros.containsKey("institucionsirc"))
			cadena+=" AND itp.institucion_sirc = '"+parametros.get("institucionsirc")+"' ";
		if(parametros.containsKey("numeroreferenciatramite"))
			cadena+=" AND itp.numero_referencia_tramite = "+parametros.get("numeroreferenciatramite");
		
		logger.info("SQL / consultarTrasladoPaciente / "+cadena);
					
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}	
		catch(SQLException e)		
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA",indicesTrasladoPacienteMapa);		
		return mapa;
	}	 
	
	
	/**
	 * Actualizar Traslado Paciente 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarTrasladoPaciente(Connection con, HashMap parametros)
	{ 
		String cadena ="";
		
		 if(parametros.containsKey("cargo"))
			 cadena = cadenaActualizacionTrasladoPaciente;	
		 else
			 cadena = cadenaActualizacionActivoTrasladoPaciente;
 
        try
		{
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	        	
            if(parametros.containsKey("cargo"))
            {   			
    			ps.setString(1,parametros.get("estado").toString());
    			
    			if(parametros.get("motivo").toString().equals(ConstantesBD.codigoNuncaValido+""))
    				ps.setNull(2,Types.NUMERIC);
    			else
    				ps.setString(2,parametros.get("motivo").toString());
    			
    			ps.setString(3,parametros.get("funcionariocontactado").toString());
    			ps.setString(4,parametros.get("activo").toString());
    			ps.setString(5,parametros.get("cargo").toString());			
    			ps.setString(6,UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechatramite").toString()));
    			ps.setString(7,parametros.get("horatramite").toString());
    			ps.setString(8,parametros.get("responsabletrasambulancia").toString());
    			ps.setString(9,parametros.get("numeromovil").toString());
    			ps.setString(10,parametros.get("placa").toString());
    			ps.setString(11,parametros.get("observaciones").toString());
    			ps.setString(12,parametros.get("usuariomodifica").toString());
    			ps.setString(13,parametros.get("fechamodifica").toString());
    			ps.setString(14,parametros.get("horamodifica").toString());			
    			
    			ps.setInt(15,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));
    			ps.setString(16,parametros.get("institucionsirc").toString());
    			ps.setInt(17,Integer.parseInt(parametros.get("institucion").toString()));      	
            }
            else
            {  			
    			ps.setString(1,parametros.get("activo").toString());    			
    			ps.setString(2,parametros.get("usuariomodifica").toString());
    			ps.setString(3,parametros.get("fechamodifica").toString());
    			ps.setString(4,parametros.get("horamodifica").toString());
    			
    			ps.setInt(5,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));
    			ps.setString(6,parametros.get("institucionsirc").toString());
    			ps.setInt(7,Integer.parseInt(parametros.get("institucion").toString()));        	
            }  
			
			
			if(ps.executeUpdate()>0)
				return true;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Insertar Traslado Paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean insertarTrasladoPaciente(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInserccionTrasladoPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		

			ps.setInt(1,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));
			ps.setString(2,parametros.get("institucionsirc").toString());
			ps.setInt(3,Integer.parseInt(parametros.get("institucion").toString()));
			
			ps.setString(4,parametros.get("estado").toString());
			
			if(parametros.get("motivo").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setNull(5,Types.NUMERIC);
			else
				ps.setString(5,parametros.get("motivo").toString());
			
			ps.setString(6,parametros.get("funcionariocontactado").toString());
			ps.setString(7,parametros.get("activo").toString());
			ps.setString(8,parametros.get("cargo").toString());
			ps.setString(9,UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechatramite").toString()));
			ps.setString(10,parametros.get("horatramite").toString());
			ps.setString(11,parametros.get("responsabletrasambulancia").toString());
			ps.setString(12,parametros.get("numeromovil").toString());
			ps.setString(13,parametros.get("placa").toString());
			ps.setString(14,parametros.get("observaciones").toString());
			ps.setString(15,parametros.get("usuariomodifica").toString());
			ps.setString(16,parametros.get("fechamodifica").toString());
			ps.setString(17,parametros.get("horamodifica").toString());		
									
			if(ps.executeUpdate()>0)
				return true;
		
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}	

	/**
	  * Consultar Historial Traslado Paciente 
	  * @param Connection con
	  * @param int numeroreferenciatramite
	  * @param int institucionsirc
	  * @param int institucion
	  * */
	public static HashMap consultarHistorialTrasladoPaciente(Connection con, int numeroreferenciatramite, String institucionsirc, int institucion )
	{
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaHistorialTrasladoPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroreferenciatramite);
			ps.setString(2,institucionsirc);
			ps.setInt(3,institucion);
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}	
		catch(SQLException e)		
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA",indicesTrasladoPacienteMapa);
		return mapa;
	}
	
	//------------------------------Fin Metodos
}