/*
 * @(#)SqlBasePacienteDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoMotivoCitaPaciente;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaInsercionPersona;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Persona;

/**
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a Servicios
 *
 * @version 1.0 Jan 6, 2005
 */
public class SqlBasePacienteDao 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBasePacienteDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un paciente en la tabla de pacientes.
	 */
	private static final String insertarPacienteStr = "insert into manejopaciente.pacientes " +
			" (codigo_paciente, zona_domicilio, ocupacion, tipo_sangre, esta_vivo, centro_atencion_pyp, lee_escribe, etnia, estudio, " +
			" grupo_poblacional, historia_clinica, anio_historia_clinica, religion, histo_sistema_anterior,fecha_motivo_cita, " +
			" hora_motivo_cita,activo,obs_motivo_cita,convenio_reserva, nro_hijos) " +
			"VALUES (?, ?, ?, ?,"+ValoresPorDefecto.getValorTrueParaConsultas()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un paciente con foto en la tabla de pacientes.
	 */
	private static final String insertarPacienteFotoStr = "insert into manejopaciente.pacientes (codigo_paciente, zona_domicilio, ocupacion, tipo_sangre, foto, esta_vivo, centro_atencion_pyp, lee_escribe, etnia, estudio, grupo_poblacional, historia_clinica, anio_historia_clinica, religion,histo_sistema_anterior,fecha_motivo_cita,hora_motivo_cita,activo,obs_motivo_cita,convenio_reserva, nro_hijos )VALUES ( ?, ?, ?, ?,?,"+ValoresPorDefecto.getValorTrueParaConsultas()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/**
	 * Cadena constante con el <i>statement</i> necesario para 
	 * saber si necesito insertar una persona o ya existe.
	 */
	private static final String deboInsertarPersonaStr = "select codigo as codigoPaciente from administracion.personas " +
			"where tipo_identificacion=? and numero_identificacion=?";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para modificar los datos de un paciente
	*/
	private static final String modificarPacienteStr =
		"UPDATE manejopaciente.pacientes SET zona_domicilio=?, ocupacion=?, tipo_sangre=?, foto=?, centro_atencion_pyp=?, lee_escribe=?, etnia=?, estudio=?, grupo_poblacional = ?, religion = ? , histo_sistema_anterior=?,historia_clinica = ?, anio_historia_clinica = ?, fecha_motivo_cita = ?, hora_motivo_cita = ?, activo = ?, obs_motivo_cita = ?, convenio_reserva = ?, nro_hijos = ?  WHERE codigo_paciente=? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los datos de un paciente.
	 */
	//Consulta antigua sin usar inner join: select per.numero_identificacion as numeroIdentificacion, per.tipo_identificacion as codigoTipoIdentificacion, ti.nombre as tipoIdentificacion, per.codigo_departamento_nacimiento as codDepartamentoIdentificacion, dep.nombre as departamentoIdentificacion, per.codigo_ciudad_nacimiento as codigoCiudadIdentificacion, ciu.nombre as ciudadIdentificacion, par.tipo_sangre as codigoTipoSangre, tipsan.nombre as tipoSangre, per.tipo_persona as codigoTipoPersona, tp.nombre as tipoPersona, to_char(per.fecha_nacimiento, 'YYYY-MM-DD')  as fechaNacimiento, per.estado_civil as codigoEstadoCivil, estc.nombre as estadoCivil, per.sexo as codigoSexo, sex.nombre as sexo, per.primer_nombre as primerNombrePersona, per.segundo_nombre as segundoNombrePersona, per.primer_apellido as primerApellidoPersona, per.segundo_apellido as segundoApellidoPersona, per.direccion as direccion, per.codigo_departamento_vivienda as codigoDepartamento, dep2.nombre as departamento, per.codigo_ciudad_vivienda as codigoCiudad,ciu2.nombre as ciudad, per.codigo_barrio_vivienda as codigoBarrio, bv.nombre as barrio, per.telefono as telefono, per.email as email, par.zona_domicilio as codigoZonaDomicilio, zdom.nombre as zonaDomicilio, par.ocupacion as codigoOcupacion, ocup.nombre as ocupacion, par.foto from personas per, pacientes par, tipos_identificacion ti, ciudades ciu, departamentos dep, tipos_personas tp, estados_civiles estc, sexo sex, ciudades ciu2, departamentos dep2, barrios bv, zonas_domicilio zdom, ocupaciones ocup, tipos_sangre tipsan where per.codigo=par.codigo_paciente and per.tipo_identificacion=ti.acronimo and per.codigo_ciudad_nacimiento=ciu.codigo_ciudad and per.codigo_departamento_nacimiento=dep.codigo and per.codigo_departamento_nacimiento=ciu.codigo_departamento and per.tipo_persona=tp.codigo and per.estado_civil=estc.acronimo and per.sexo=sex.codigo and per.codigo_departamento_vivienda=ciu2.codigo_departamento and per.codigo_departamento_vivienda=dep2.codigo and per.codigo_ciudad_vivienda=ciu2.codigo_ciudad and per.codigo_departamento_vivienda=bv.codigo_departamento and per.codigo_ciudad_vivienda=bv.codigo_ciudad and  per.codigo_barrio_vivienda=bv.codigo_barrio and par.zona_domicilio=zdom.acronimo and par.ocupacion=ocup.codigo and par.tipo_sangre=tipsan.codigo and per.codigo=?
	private static final String cargarPacienteStr = " Select per.numero_identificacion as numeroIdentificacion,			" +
													"		 per.tipo_identificacion as codigoTipoIdentificacion, 		" +
													"		 ti.nombre as tipoIdentificacion, " +
													"		 ti.acronimo as acronimoTipoIden , " +
													"		 coalesce(per.codigo_departamento_nacimiento,'') as codDepartamentoIdentificacion, " +
													"		 coalesce(administracion.getnombredepto(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento),'') as departamentoIdentificacion, " +
													"		 coalesce(per.codigo_ciudad_nacimiento,'') as codigoCiudadIdentificacion, " +
													"		 coalesce(administracion.getnombreciudad(per.codigo_pais_nacimiento,per.codigo_departamento_nacimiento,per.codigo_ciudad_nacimiento),'') as ciudadIdentificacion," +
													"		 coalesce(per.codigo_pais_nacimiento,'') AS codigoPaisIdentificacion, " +
													"		 coalesce(administracion.getdescripcionpais(per.codigo_pais_nacimiento),'') AS paisIdentificacion, " +
													"		 coalesce(par.tipo_sangre,"+ConstantesBDManejoPaciente.codigoTipoSangreDesconocido+") as codigoTipoSangre, " +
													"        coalesce(tipsan.nombre,'Desconocido') as tipoSangre, " +
													"		 per.tipo_persona as codigoTipoPersona, " +
													"        tp.nombre as tipoPersona," +
													"		 to_char(per.fecha_nacimiento, 'YYYY-MM-DD') as fechaNacimiento, " +
													"		 per.estado_civil as codigoEstadoCivil, " +
													"		 estc.nombre as estadoCivil, " +
													" 		 per.sexo as codigoSexo," +
													"		 sex.nombre as sexo, " +
													"		 per.primer_nombre as primerNombrePersona, " +
													"		coalesce(per.segundo_nombre,'') as segundoNombrePersona," +
													"		 per.primer_apellido as primerApellidoPersona, " +
													"		coalesce(per.segundo_apellido,'') as segundoApellidoPersona, " +
													"		 per.direccion as direccion, " +
													"		coalesce(per.codigo_departamento_vivienda,'') as codigoDepartamento, " +
													"		coalesce(administracion.getnombredepto(per.codigo_pais_vivienda,per.codigo_departamento_vivienda),'') as departamento," +
													"		coalesce(per.codigo_ciudad_vivienda,'') as codigoCiudad," +
													"		coalesce(administracion.getnombreciudad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda,per.codigo_ciudad_vivienda),'') as ciudad, " +
													"       coalesce(per.codigo_pais_vivienda,'') AS codigoPais, " +
													"       coalesce(administracion.getdescripcionpais(per.codigo_pais_vivienda),'') AS pais, " +
													"		coalesce(per.codigo_barrio_vivienda||'','') as codigoBarrio, " +
													"       CASE WHEN per.codigo_localidad_vivienda IS NULL THEN '' ELSE per.codigo_localidad_vivienda END AS codigoLocalidad, " +
													"       CASE WHEN per.codigo_localidad_vivienda IS NULL THEN '' ELSE administracion.getdesclocalidad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda, per.codigo_ciudad_vivienda, per.codigo_localidad_vivienda) END AS localidad, " +
													"		 CASE WHEN per.codigo_barrio_vivienda IS NULL THEN '' ELSE bv.codigo_barrio || ' - ' || bv.descripcion END as barrio," +
													"		per.telefono as telefono, " +
													"		coalesce(per.telefono_fijo,"+ConstantesBD.codigoNuncaValido+") as telefono_fijo, " +
													"       coalesce(per.telefono_celular,"+ConstantesBD.codigoNuncaValido+") as telefono_celular, " +
													"		per.email as email, " +
													"		coalesce(par.zona_domicilio||'','') as codigoZonaDomicilio," +
													"		coalesce(zdom.nombre,'') as zonaDomicilio, " +
													"		coalesce(par.ocupacion||'','') as codigoOcupacion, " +
													"		coalesce(ocup.nombre,'') as ocupacion, " +
													"        par.foto,			 " +
													"		 CASE WHEN par.centro_atencion_pyp IS NULL THEN " +
													"		coalesce( ( SELECT centro_atencion from capitacion.usuario_x_convenio WHERE persona = par.codigo_paciente  AND centro_atencion >0  " +
													"		AND (to_char(CURRENT_DATE,'yyyy-mm-dd') BETWEEN to_char(fecha_inicial,'yyyy-mm-dd') " +
													"		AND to_char(fecha_final,'yyyy-mm-dd'))), 0 ) ELSE par.centro_atencion_pyp END as centro_atencion,			 " +
													"		 CASE WHEN par.centro_atencion_pyp IS NULL THEN '' ELSE administracion.getnomcentroatencion(par.centro_atencion_pyp) END as nombre_centro_atencion,			 " +
													"		 par.lee_escribe,			 " +
													"		 CASE WHEN par.etnia IS NULL THEN 0 ELSE par.etnia END as etnia,			 " +
													"		 CASE WHEN par.etnia IS NULL THEN '' ELSE et.nombre END as nombre_etnia,			 " +
													"		 CASE WHEN par.estudio IS NULL THEN 0 ELSE par.estudio END as estudio, 			 " +
													"		 CASE WHEN par.estudio IS NULL THEN '' ELSE est.nombre END as nombre_estudio, 			 " +
															"coalesce(per.codigo_depto_id,'') AS codigoDeptoId, " +
															"coalesce(per.codigo_ciudad_id,'') AS codigoCiudadId, " +
															"coalesce(administracion.getnombreciudad(per.codigo_pais_id,per.codigo_depto_id,per.codigo_ciudad_id),'') AS nombreCiudadId, "+
															"coalesce(administracion.getnombredepto(per.codigo_pais_id,per.codigo_depto_id),'') AS nombreDeptoId, " +
															"coalesce(per.codigo_pais_id,'') AS codigoPaisId, " +
															"coalesce(administracion.getdescripcionpais(per.codigo_pais_id),'') AS nombrePaisId, " +
															"par.grupo_poblacional AS grupoPoblacional," +
															"coalesce(par.historia_clinica,'') AS historiaClinica, " +
															"coalesce(par.anio_historia_clinica,'') As anioHistoriaClinica, " +
															"CASE WHEN par.religion IS NULL THEN '' ELSE par.religion || '' END AS codigoReligion, " +
															"CASE WHEN par.religion IS NULL THEN '' ELSE getdescripcionreligion(par.religion) END AS religion," +
															"par.histo_sistema_anterior, "+															
															"to_char(par.fecha_motivo_cita, 'YYYY-MM-DD')  AS fechamotivocita, " +															
															"coalesce(par.hora_motivo_cita,'') AS horamotivocita, " +
															"coalesce(par.activo,'') AS activopaciente, " +
															"coalesce(par.obs_motivo_cita,'') AS obsmotivocita, " +
															"coalesce(par.convenio_reserva,"+ConstantesBD.codigoNuncaValido+") AS convenioreserva," +
															"coalesce(par.nro_hijos,0) AS nrohijos,  " +
													"		ingreso.id AS codigoIngreso "+
													"		 FROM manejopaciente.pacientes par" +
													"			  INNER JOIN administracion.personas per ON (per.codigo=par.codigo_paciente) " +
													"			  INNER JOIN administracion.tipos_identificacion ti ON (per.tipo_identificacion=ti.acronimo) " +
													"			  LEFT OUTER JOIN administracion.sexo sex ON (per.sexo=sex.codigo) " +
													"             INNER JOIN administracion.tipos_personas tp ON (per.tipo_persona=tp.codigo) " +
													"             LEFT OUTER JOIN administracion.estados_civiles estc ON (per.estado_civil=estc.acronimo) " +
													"             LEFT OUTER JOIN manejopaciente.tipos_sangre tipsan ON (par.tipo_sangre=tipsan.codigo)  " +
													"			  LEFT OUTER JOIN administracion.zonas_domicilio zdom ON (par.zona_domicilio=zdom.acronimo) " +
													"             LEFT OUTER JOIN manejopaciente.ocupaciones ocup ON (par.ocupacion=ocup.codigo) " +
													"			  LEFT OUTER JOIN administracion.barrios bv ON ( per.codigo_barrio_vivienda=bv.codigo ) " +
													"             LEFT OUTER JOIN manejopaciente.etnia et ON(et.codigo=par.etnia) "+
													"             LEFT OUTER JOIN manejopaciente.estudio est ON(est.codigo=par.estudio) "+
													"			  LEFT OUTER JOIN manejopaciente.ingresos ingreso ON (ingreso.codigo_paciente = par.codigo_paciente) "+
													"		WHERE per.codigo=?";


	/**
	 * String con el statement necesario para actualizar las observaciones generales que el usuario
	 * desee en el popUp de "Mas Informaci�n" del cabezote
	 */
	private final static String actualizarNotaObservconPacienteStr =" UPDATE manejopaciente.pacientes " +
															   		" SET observaciones =?" +
																	" WHERE codigo_paciente=? ";
	
	/**
	 * String con el statement necesario para consultar las observaciones del paciente
	 */
	private static final String consultarObservacionesPacienteStr=" SELECT p.observaciones as observaciones" +
																  " FROM manejopaciente.pacientes p " +
																  " WHERE p.codigo_paciente=? ";
	
	/**
	 * Cadena que inserta un paciente para triage
	 */
	private static final String insertarPacienteTriageStr = "INSERT INTO historiaclinica.pacientes_triage " +
		" (codigo,codigo_paciente,fecha,hora,usuario,centro_atencion,consecutivo_triage,consecutivo_fecha_triage,atendido,clasificacion_triage) ";
	
	
	/**
	 * Cadena Sql para insertar un motivo de cita Paciente Odontologia
	 */
	private static final String insertarMotivoCitaPacinente="INSERT INTO manejopaciente.motivo_cita_paciente ( " +
			                    "codigo_pk, codigo_paciente, motivo, fecha_modificacion, hora_modificacion, usuario_modificacion ) " +
			                    "VALUES (?, ?, ?,  CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?) ";
	
	
	/**
	 * Cadena Sql para consultar Motivo Cita de un paciente
	 */
	private static final String consultarMotivoCitaPacienteStr="SELECT  " +
			                    "motcitpac.codigo_pk AS codigopk," +
			                    "motcitpac.codigo_paciente AS codigopaciente, " +
			                    "motcitpac.motivo AS codmotivo, " +
			                    "motcit.descripcion AS descripcionmotivo, " +
			                    "motcitpac.fecha_modificacion AS fechamodificacion, " +
			                    "motcitpac.hora_modificacion AS horamodificacion, " +
			                    "motcitpac.usuario_modificacion AS usuariomodificacion, " +
			                    "coalesce(serv.conocimiento_servicio,"+ConstantesBD.codigoNuncaValido+") AS codconocimiento " +
			                    "FROM manejopaciente.motivo_cita_paciente  motcitpac " +
			                    "INNER JOIN odontologia.motivos_cita motcit ON (motcit.codigo_pk = motcitpac.motivo)" +
			                    "LEFT OUTER JOIN manejopaciente.conocimiento_serv_pac serv ON (serv.codigo_paciente = motcitpac.codigo_paciente) "+
			                    "WHERE motcitpac.codigo_paciente = ? ";
	
	/**
	 * Cadena Sql para modificar un Motivo de cita de un paciente
	 */
	private static final String modificarMotivoCitaPacienteStr="UPDATE manejopaciente.motivo_cita_paciente SET " +
			                    "motivo = ?," +
			                    "fecha_modificacion= CURRENT_DATE, " +
			                    "hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			                    "usuario_modificacion = ? " +
			                    "WHERE " +
			                    "codigo_paciente = ? ";
	
	/**
	 * Cadena sql para consultar existencia motivo cita paciente 
	 */
	private static final String existeMotivoCitaPacienteStr="SELECT " +
			                    "codigo_pk AS codigo " +
			                    "FROM manejopaciente.motivo_cita_paciente " +
			                    "WHERE codigo_paciente = ? ";
	
	
	/**
	 * Cadea Sql para realizar el ingreso de un beneficiario
	 */
	private static final String ingresarBeneficiarioPacienteStr ="INSERT INTO manejopaciente.beneficiarios_paciente (" +
								"codigo_pk, " +
								"codigo_paciente, " +
								"persona_beneficiario, " +
   							    "parentezco, " +
								"ocupacion, " +
								"estudio, " +
								"tipo_ocupacion, " +
								"fecha_modificacion," +
								"hora_modificacion," +
								"usuario_modificacion " +
								") VALUES (?,?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?) " ;
	
	/**
	 * Cadena Sql para modificar un Beneficiario asociado a un paciente
	 */
    private static final String modificarBeneficiarioPacienteStr = "UPDATE manejopaciente.beneficiarios_paciente SET " +    		                   
    		                    "parentezco = ?, " +
    		                    "ocupacion = ?, " +
    		                    "estudio = ?, " +
    		                    "tipo_ocupacion = ?, " +
    		                    "fecha_modificacion = CURRENT_DATE ," +
    		                    "hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ," +
    		                    "usuario_modificacion = ? " +
    		                    "WHERE codigo_paciente = ? AND persona_beneficiario = ?"; 
	
    /**
     * Cadena Sql para consultar si existe un Beneficiario
     */
    private static final String existeBeneficiarioPacienteOdontStr= "SELECT " +
    		                    "codigo_pk " +
    		                    "FROM manejopaciente.beneficiarios_paciente " +
    		                    "WHERE codigo_paciente = ? AND persona_beneficiario = ? ";
    
    
    /**
     * Cadena Sql para insertar un conocimiento del servicio odontologia
     */
    private static final  String ingresarConocimientoServPac= "INSERT INTO manejopaciente.conocimiento_serv_pac ( " +
    		                     "codigo_pk, " +
    		                     "codigo_paciente, " +
    		                     "conocimiento_servicio, " +
    		                     "fecha_modificacion, " +
    		                     "hora_modificacion, " +
    		                     "usuario_modificacion ) " +
    		                     "VALUES ( ?, ?, ? ,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?) ";
    /**
     * Cadena Sql para consultar el conocimiento del servicio
     */
    private static final String consultaConocimientoServPac = "SELECT " +
    		                     "codigo_pk AS codigopk, " +
    		                     "codigo_paciente AS codigopaciente, " +
    		                     "conocimiento_servicio AS conocimientoservicio, " +
    		                     "fecha_modificacion AS fechamodificacion, " +
    		                     "hora_modificacion AS horamodificacion, " +
    		                     "usuario_modificacion AS usuariomodificacion " +
    		                     "FROM manejopaciente.conocimiento_serv_pac " +
    		                     "WHERE codigo_paciente = ?";
	
    /**
     * Cadena Sql para consultar existencia conociemiento del servicio 
     */
    private static final String existeConocimientoServPac="SELECT " +
    		                    "codigo_pk AS codigo " +
    		                    "FROM manejopaciente.conocimiento_serv_pac " +
    		                    "WHERE codigo_paciente = ? ";
    
    /**
     * Cadena Sql para modificar el conocimeinto de un servicio
     */
    private static final String modificarConocimientoServPac = "UPDATE manejopaciente.conocimiento_serv_pac  SET " +
    		                     "conocimiento_servicio = ?, " +
    		                     "fecha_modificacion = CURRENT_DATE , " +
    		                     "hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    		                     "usuario_modificacion = ? " +
    		                     "WHERE codigo_paciente = ? ";
    
    
	/**
	* Dados el c�digo de un paciente modifica los datos del mismo. Utiliza una conexi�n abierta
	* y finaliza cualquier transacci�n iniciada sobre esa conexi�n.
	* 
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_codigoPersona		C�digo interno del paciente
	* @param as_codigoOcupacion		C�digo de la ocupacion del paciente
	* @param as_codigoTipoSangre	C�digo del tipo de sangre del paciente
	* @param as_codigoZonaDomicilio	C�digo de la zona de domicilio del paciente
	* @param as_foto				Ruta del archivo de la foto del paciente
	 * @param histo_sistema_anterior 
	* @return	1	Si la modificaci�n del paciente fue exitoso
	*			0	Error de base de datos
	*			-3	La conexi�n a base de datos no v�lida
	*/
	public static int modificarPaciente(
		Connection	ac_con,
		int			ai_codigoPersona,
		String		as_codigoOcupacion,
		String		as_codigoTipoSangre,
		String		as_codigoZonaDomicilio,
		String		as_foto,
		int			centroAtencion,
		int 		etnia,
		boolean		leeEscribe,
		int 		estudio,
		String grupoPoblacional,
		String codigoReligion, 
		Boolean histo_sistema_anterior,
		String numeroHistoriaClinica,
		String anioHistoriaClinica,
		String fechaMotivoCita,
		String horaMotivoCita,
		String observacionesMotivoCita, 
		String activo,
		String convenioReserva,
		int nroHijos
		

	)throws SQLException
	{
		PreparedStatementDecorator ps = null;
		boolean		lb_continuar;
		int			li_resp;

		li_resp	= 1;

		/* La conexion debe estar abierta */
		lb_continuar = ac_con != null && !ac_con.isClosed() ;
		if(lb_continuar )
		{
			/* Modificar los datos del paciente */

			ps = new PreparedStatementDecorator(ac_con.prepareStatement(modificarPacienteStr));
			
			logger.info("codigo de la ocupacion >> "+as_codigoOcupacion);
			logger.info("Activo >>"+activo);
			logger.info("Convenio Reserva >>"+convenioReserva);
			
			if(!as_codigoOcupacion.equals(""))
				ps.setInt(2, Utilidades.convertirAEntero(as_codigoOcupacion) );
			else
				ps.setNull(2, Types.INTEGER );
			
			if(as_codigoZonaDomicilio!=null && !as_codigoZonaDomicilio.equals(""))
				ps.setString(1, as_codigoZonaDomicilio);
			else
				ps.setNull(1, Types.VARCHAR);
			
			if(!as_codigoTipoSangre.equals(""))
			   ps.setString(3, as_codigoTipoSangre);
			else
				ps.setNull(3, Types.INTEGER );
			
			if(as_foto!=null && !as_foto.equals(""))
			  ps.setString(4, as_foto);
			else
				ps.setNull(4, Types.VARCHAR);

			if ( centroAtencion == 0 )
			{
				ps.setNull( 5, Types.INTEGER );
			}	
			else
				ps.setInt( 5, centroAtencion );
				
			
			ps.setBoolean( 6, leeEscribe );
	
			if ( etnia == 0 )
				ps.setNull( 7, Types.INTEGER );
			else
				ps.setInt(7, etnia );
		
			if ( estudio == 0 )
				ps.setNull( 8, Types.INTEGER );
			else
				ps.setInt(8, estudio );
			
			if(grupoPoblacional!= null && !grupoPoblacional.equals(""))
			   ps.setString(9,grupoPoblacional);
			else
				ps.setString(9,ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales);
			
			if( codigoReligion != null && !codigoReligion.equals(""))
				ps.setObject(10,codigoReligion);
			else
				ps.setNull(10,Types.INTEGER);
			
			
			ps.setBoolean( 11, histo_sistema_anterior);
			
			ps.setString(12,numeroHistoriaClinica);
			
			ps.setString(13,anioHistoriaClinica);
			
			
			if(!fechaMotivoCita.equals(""))
				ps.setDate(14,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaMotivoCita)));
			else
				ps.setNull(14,Types.DATE);
			
			if(!horaMotivoCita.equals(""))
				ps.setString(15,horaMotivoCita);
			else
				ps.setNull(15,Types.VARCHAR);
			
			if(!activo.equals(""))
				ps.setString(16,activo);
			else
				ps.setNull(16,Types.VARCHAR);
			
			if(!observacionesMotivoCita.equals(""))
				ps.setString(17,observacionesMotivoCita);
			else
				ps.setNull(17,Types.VARCHAR);
			
			if(convenioReserva!=null  &&  !convenioReserva.equals("") && Utilidades.convertirAEntero(convenioReserva)>0 )
				ps.setInt(18,Utilidades.convertirAEntero(convenioReserva));
			else
				ps.setNull(18,Types.INTEGER);
			
         	if ( nroHijos >= 0 )
         		ps.setInt(19, nroHijos );
			else
				ps.setNull( 19, Types.INTEGER );
				     
		
			
			ps.setInt(20, ai_codigoPersona);

			
			
			//centro_atencion_pyp=?, lee_escribe=?, etnia=?, estudio=?

			try
			{
				lb_continuar = ps.executeUpdate() == 1;
			}
			catch(Exception le_e)
			{
				le_e.printStackTrace();
				li_resp = 0;
			}finally{
				try{
					if(ps!=null){
						ps.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
			}
		}
		else
		{
			li_resp = -3;
		}

		/**if(lb_continuar)
			ldf_df.endTransaction(ac_con);
		else
			ldf_df.abortTransaction(ac_con);**/

		return lb_continuar ? 1 : li_resp;
	}

	/**
	 * Dados el numero y tipo de identificacion, retorna los datos de un paciente. Reutiliza una
	 * conexion abierta con una base de datos Gen�rica
	 * @param con una conexion abierta
	 * @param tipoId codigo de la identificacion del paciente
	 * @param numeroId numero de identificacion del paciente
	 * @return un objeto <code>Answer</code> con una conexion abierta y los datos del paciente
	 */
	public static Answer cargarPaciente (Connection con, int codigoPaciente)
	{
		PreparedStatementDecorator cargarPacienteStatement = null;
		try{
			cargarPacienteStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarPacienteStr));
		     logger.info("consulta paciente=> "+cargarPacienteStr.replace("?", codigoPaciente+""));
			  logger.info(" \n codigo paciente=> "+codigoPaciente);
			cargarPacienteStatement.setInt(1, codigoPaciente);
			
			ResultSetDecorator rs = new ResultSetDecorator(cargarPacienteStatement.executeQuery());
			return new Answer(rs, con);
		}
		catch(Exception e)
		{
			logger.error("Error cargando el paciente: "+e);
			return null;
		}
	}

	/**
	 * Inserta un paciente en una base de datos Gen�rica. No termina la transacci�n, porque esta requiere
	 * crear una historia cl�nica despues de ejecutarse. Si la conexi�n no esta abierta, la crea.
	 * @param con una conexion abierta con una base de datos Gen�rica
	 * @param codigoTipoAfiliado codigo indicando el tipo de afilizacion del paciente
	 * @param codigoTipoRegimen codigo con el tipo de regimen del paciente
	 * @param estratoSocial numero con el estrato social del paciente
	 * @param codigoZonaDomicilio codigo con la zona de domicilio del paciente
	 * @param codigoOcupacion codigo de la ocupacion del paciente
	 * @param numeroPoliza numero de la poliza de seguros del paciente
	 * @param codigoTipoPaciente codigo del tipo de paciente
	 * @param numeroIdentificacion numero de identificacion del paciente
	 * @param codigoTipoIdentificacion codigo con el tipo de identificacion del paciente
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedido el numero de identificacion del paciente
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedido el numero de identificacion del paciente
	 * @param codigoTipoPersona codigo del tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del paciente
	 * @param mesNacimiento mes de nacimiento del paciente
	 * @param anioNacimiento a�o de nacimiento del paciente
	 * @param codigoEstadoCivil codigo del estado civil del paciente
	 * @param codigoSexo codigo del sexo del paciente
	 * @param primerNombrePersona primer nombre del paciente
	 * @param segundoNombrePersona segundo nombre del paciente
	 * @param primerApellidoPersona primer apellido del paciente
	 * @param segundoApellidoPersona segundo apellido del paciente
	 * @param direccion direccion de la residencia del paciente
	 * @param codigoDepartamento codigo del departamento donde reside el paciente
	 * @param codigoCiudad codigo de la ciudad donde reside el paciente
	 * @param codigoBarrio codigo del barrio donde reside el paciente
	 * @param telefono telefono del paciente
	 * @param email correo electronico del paciente
	 * @param codigoTipoSangre codigo del tipo de sangre del paciente
	 * @param foto nombre de la foto almacenada
	 * @param codigoInstitucion
	 * @param centro_atencion
	 * @param etnia
	 * @param lee_escribe
	 * @param estudio
	 * @param grupoPoblacional
	 * @param infoHistoSistemaAnt 
	 * @return el numero de filas insertadas en la BD
	 */
	public static RespuestaInsercionPersona insertarPaciente (
		Connection con,
		String numeroHistoriaClinica, String anioHistoriaClinica, 
		String codigoZonaDomicilio, String codigoOcupacion, 
		String numeroIdentificacion, String codigoTipoIdentificacion,String codigoDeptoId, 
		String codigoCiudadId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, 
		String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, 
		String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, 
		String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, 
		String codigoCiudad, String codigoBarrio, String telefono, String email, String codigoTipoSangre, 
		String foto,int codigoInstitucion, int centro_atencion, int etnia, boolean lee_escribe, int estudio,
		String grupoPoblacional, String codigoPaisId, String codigoPaisIdentificacion, String codigoPais, 
		String codigoLocalidad, String codigoReligion, Boolean infoHistoSistemaAnt, String telefonoCelular, String telefonoFijo,
		String fechaMotivoCita,String horaMotivoCita,String observacionesMotivoCita, String activo,String convenioReserva, int nroHijos)  
	{
		int resp2, resp0, resp3=1, codigoPaciente=0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		RespuestaInsercionPersona respInsercion = new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
		PreparedStatementDecorator deboInsertarStatement = null;
		PreparedStatementDecorator insertarPacienteStatement = null;
		ResultSetDecorator rs = null;
		try
		{				
	
			//Abre una transacion y mas abajo se esta abriendo tambien.
			/**if (myFactory.beginTransaction(con))
			{
			    resp0=1;
			}
			else
			{
			    resp0=0;
			}**/
			resp0 = 1;
			/* Solo se debe insertar en personas si no existe una previamente El caso en que se quiera agregar un paciente
				con la misma identificacion, ya esta manejado por la clase util.UtilidadValidacion */
			deboInsertarStatement =  new PreparedStatementDecorator(con.prepareStatement(deboInsertarPersonaStr));
			deboInsertarStatement.setString(1, codigoTipoIdentificacion);
			deboInsertarStatement.setString(2, numeroIdentificacion);
	
			 rs=new ResultSetDecorator(deboInsertarStatement.executeQuery());
			if (rs.next())
			{
				codigoPaciente=rs.getInt("codigoPaciente");
			}
			//No hay necesidad de colocar el c�digo del paciente
			//En 0 o -1, y que inicialmente tiene estos valores
			// Solo si no hay una persona insertada, hacemos la insercion en persona
			if (codigoPaciente<1)
			{
				respInsercion  = Persona.insertarPersona(con, numeroIdentificacion, codigoTipoIdentificacion, codigoDeptoId, codigoCiudadId, codigoPaisId, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, codigoLocalidad, telefono, email, telefonoCelular, ConstantesBD.tipoPersonaPaciente,codigoInstitucion,telefonoFijo);
				codigoPaciente=respInsercion.getCodigoPersona();
				logger.info("codigoPaciente >>" +codigoPaciente);
			}
			else
			{
				int resModificacion = Persona.modificarPersona(con, codigoTipoIdentificacion, numeroIdentificacion, codigoDeptoId, codigoCiudadId, codigoPaisId, codigoPaciente, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, codigoLocalidad, telefono, email, telefonoCelular, ConstantesBD.continuarTransaccion, ConstantesBD.tipoPersonaPaciente, codigoInstitucion,telefonoFijo);
				//No necesita cambio de identificaci�n porque todo salio bien
				respInsercion.setNecesitaCambioIdentificacion(false);
				if(resModificacion>0)
				{
					respInsercion.setSalioBien(true);
					respInsercion.setCodigoPersona(codigoPaciente);
				}
				else
				{
					respInsercion.setSalioBien(false);
					respInsercion.setCodigoPersona(0);
				}
			}
			if (foto==null||foto.equals(""))
			{
				insertarPacienteStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarPacienteStr));
				insertarPacienteStatement.setInt(1, codigoPaciente);
				if(!codigoZonaDomicilio.equals(""))
					insertarPacienteStatement.setString(2, codigoZonaDomicilio);
				else
					insertarPacienteStatement.setNull(2, Types.VARCHAR);
				
				if(codigoOcupacion!= null && !codigoOcupacion.equals("") && Utilidades.convertirAEntero(codigoOcupacion)>0)
					insertarPacienteStatement.setInt(3,Utilidades.convertirAEntero(codigoOcupacion));
				else
					insertarPacienteStatement.setNull(3, Types.INTEGER);
				
				if(codigoTipoSangre!= null && !codigoTipoSangre.equals("") && Utilidades.convertirAEntero(codigoTipoSangre)>0)
					insertarPacienteStatement.setInt(4,Utilidades.convertirAEntero(codigoTipoSangre));
				else
					insertarPacienteStatement.setNull(4, Types.INTEGER);
				
				
				
				
				//-- Cambios Por la Nueva Funcionalidad PYP 
				
				if ( centro_atencion == 0 )
					insertarPacienteStatement.setNull(5, Types.INTEGER);
				else
					insertarPacienteStatement.setInt(5, centro_atencion);
					
				if(lee_escribe)
					insertarPacienteStatement.setObject(6, ValoresPorDefecto.getValorTrueParaConsultas());
				else
					insertarPacienteStatement.setObject(6, ValoresPorDefecto.getValorFalseParaConsultas());
					
			
				if ( etnia == 0 )
					insertarPacienteStatement.setNull(7, Types.INTEGER);
				else
					insertarPacienteStatement.setInt(7, etnia);
				
				
				if ( estudio == 0 )
					insertarPacienteStatement.setNull(8, Types.INTEGER);
				else
					insertarPacienteStatement.setInt(8, estudio);
				
				if(grupoPoblacional!=null && !grupoPoblacional.equals(""))
				  insertarPacienteStatement.setString(9,grupoPoblacional);
				else
					insertarPacienteStatement.setString(9,ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales);
				
				insertarPacienteStatement.setString(10,numeroHistoriaClinica);
				insertarPacienteStatement.setString(11,anioHistoriaClinica);
				
				if(!codigoReligion.equals(""))
					insertarPacienteStatement.setInt(12,Utilidades.convertirAEntero(codigoReligion));
				else
					insertarPacienteStatement.setNull(12,Types.INTEGER);
				
				if(infoHistoSistemaAnt)
					insertarPacienteStatement.setObject(13, ValoresPorDefecto.getValorTrueParaConsultas());
				else
					insertarPacienteStatement.setObject(13, ValoresPorDefecto.getValorFalseParaConsultas());
				
				
				if(!fechaMotivoCita.equals(""))
					insertarPacienteStatement.setDate(14,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaMotivoCita)));
				else
					insertarPacienteStatement.setNull(14,Types.DATE);
				
				if(!horaMotivoCita.equals(""))
					insertarPacienteStatement.setString(15,horaMotivoCita);
				else
					insertarPacienteStatement.setNull(15,Types.VARCHAR);
				
				logger.info("valor de activo: "+activo);
				if(!activo.equals(""))
					insertarPacienteStatement.setString(16,activo);
				else
					insertarPacienteStatement.setString(16,ConstantesBD.acronimoSi);
				
				if(!observacionesMotivoCita.equals(""))
					insertarPacienteStatement.setString(17,observacionesMotivoCita);
				else
					insertarPacienteStatement.setNull(17,Types.VARCHAR);				
				
				logger.info("convenio Reserva "+convenioReserva +" hijos "+nroHijos);
				if(convenioReserva != null && !convenioReserva.equals("") && Utilidades.convertirAEntero(convenioReserva)>0)
					insertarPacienteStatement.setInt(18,Utilidades.convertirAEntero(convenioReserva));
				else
					insertarPacienteStatement.setNull(18,Types.INTEGER);
				
				if(nroHijos>=0)
					insertarPacienteStatement.setInt(19,nroHijos);
				else
					insertarPacienteStatement.setNull(19,Types.INTEGER);
                  			
				logger.info("convenio Reserva "+convenioReserva +" hijos "+nroHijos);	
			}
			else
			{
				insertarPacienteStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarPacienteFotoStr));
				insertarPacienteStatement.setInt(1, codigoPaciente);
				insertarPacienteStatement.setString(2, codigoZonaDomicilio);
				if(codigoOcupacion!= null && !codigoOcupacion.equals("") && Utilidades.convertirAEntero(codigoOcupacion)>0)
					insertarPacienteStatement.setInt(3, Utilidades.convertirAEntero(codigoOcupacion));
				else
					insertarPacienteStatement.setNull(3, Types.INTEGER);
				
				if(codigoTipoSangre!= null && !codigoTipoSangre.equals("") && Utilidades.convertirAEntero(codigoTipoSangre)>0)
					insertarPacienteStatement.setInt(4,Utilidades.convertirAEntero(codigoTipoSangre));
				else
					insertarPacienteStatement.setNull(4, Types.INTEGER);
				
				if(foto!=null && !foto.equals(""))
				   insertarPacienteStatement.setString(5, foto);
				else
					insertarPacienteStatement.setNull(5,Types.VARCHAR);
				
				//-- Cambios Por la Nueva Funcionalidad PYP 
				if ( centro_atencion == 0 )
					insertarPacienteStatement.setNull(6, Types.INTEGER);
				else
					insertarPacienteStatement.setInt(6, centro_atencion);

				if(lee_escribe)
					insertarPacienteStatement.setObject(7, ValoresPorDefecto.getValorTrueParaConsultas());
				else
					insertarPacienteStatement.setObject(7, ValoresPorDefecto.getValorFalseParaConsultas());
				
				if ( etnia == 0 )
					insertarPacienteStatement.setNull(8, Types.INTEGER);
				else
					insertarPacienteStatement.setInt(8, etnia);
				
				if ( estudio == 0 )
					insertarPacienteStatement.setNull(9, Types.INTEGER);
				else
					insertarPacienteStatement.setInt(9, estudio);
				
				
				if(grupoPoblacional!=null && !grupoPoblacional.equals(""))
					  insertarPacienteStatement.setString(10,grupoPoblacional);
					else
						insertarPacienteStatement.setString(10,ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales);
				
				insertarPacienteStatement.setString(11,numeroHistoriaClinica);
				insertarPacienteStatement.setString(12,anioHistoriaClinica);
				
				if(!codigoReligion.equals(""))
					insertarPacienteStatement.setInt(13,Utilidades.convertirAEntero(codigoReligion));
				else
					insertarPacienteStatement.setNull(13,Types.INTEGER);
				
				if(infoHistoSistemaAnt)
					insertarPacienteStatement.setObject(14, ValoresPorDefecto.getValorTrueParaConsultas());
				else
					insertarPacienteStatement.setObject(14, ValoresPorDefecto.getValorFalseParaConsultas());
				
				
				if(!fechaMotivoCita.equals(""))
					insertarPacienteStatement.setDate(15,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaMotivoCita)));
				else
					insertarPacienteStatement.setNull(15,Types.DATE);
				
				if(!horaMotivoCita.equals(""))
					insertarPacienteStatement.setString(16,horaMotivoCita);
				else
					insertarPacienteStatement.setNull(16,Types.VARCHAR);
				
				logger.info("valor de activo: "+activo);
				if(!activo.equals(""))
					insertarPacienteStatement.setString(17,activo);
				else
					insertarPacienteStatement.setString(17,ConstantesBD.acronimoSi);
				
				if(!observacionesMotivoCita.equals(""))
					insertarPacienteStatement.setString(18,observacionesMotivoCita);
				else
					insertarPacienteStatement.setNull(18,Types.VARCHAR);				
				
				if(convenioReserva != null && !convenioReserva.equals("") && Utilidades.convertirAEntero(convenioReserva)>0)
					insertarPacienteStatement.setInt(19,Utilidades.convertirAEntero(convenioReserva));
				else
					insertarPacienteStatement.setNull(19,Types.INTEGER);				

				if(nroHijos>0)
					insertarPacienteStatement.setInt(20,nroHijos);
				else
					insertarPacienteStatement.setNull(20,Types.INTEGER);
				
			}
			
			logger.info("Va realizar INsercion Paciente ");
	
			resp2 = insertarPacienteStatement.executeUpdate();
			logger.info("Insercion >> resp2="+resp2+ " resp0="+resp0+ " resp3="+resp3);
			if (resp0==0 || resp2==0 || resp3==0)
			{
			    myFactory.abortTransaction(con);
				resp0=0;
				respInsercion = new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
			}
			else
			{
			    //myFactory.endTransaction(con);
			}
			return respInsercion;
		}
		catch(SQLException e)
		{
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1)
			{
				logger.error("Error en  insertarPaciente => "+e);
			}
			
			logger.error("Error en  insertarPaciente => "+e);
			//Verificaci�n de error de concurrencia
			if(e.getSQLState().equals("23505"))
				respInsercion = new RespuestaInsercionPersona(false,false, "El paciente ya existe.", 0);
			
			return respInsercion;
		}finally{
			try{
				if(deboInsertarStatement!=null){
					deboInsertarStatement.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(insertarPacienteStatement!=null){
					insertarPacienteStatement.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * M�todo para verificar que un paciente nuevo que se vaya a ingresar no tenga 
	 * los mismos nombres y apellidos de otro paciente que ya est� registrado en el sistema
	 * @param con
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param numeroID 
	 * @param tipoID 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> validarPacienteIgualNombre(Connection con,String primerNombre,String segundoNombre,String primerApellido,String segundoApellido, String tipoID, String numeroID)
	{
		String consultarPacienteStr = ""+"" +
											"SELECT "+
											"per.numero_identificacion as numeroid,"+
											"per.tipo_identificacion as tipoid, "+
											"administracion.getnombretipoidentificacion(per.tipo_identificacion) AS nomtipoid, " +
											"getnombrepersona(per.codigo) as nombrepersona " +
											"FROM administracion.personas per "+
											"INNER JOIN manejopaciente.pacientes pac ON(pac.codigo_paciente=per.codigo) "+
											"WHERE "+
											"UPPER(per.primer_nombre) = UPPER('"+primerNombre+"') AND "+
											"UPPER(per.primer_apellido) = UPPER('"+primerApellido+"') AND "+
											"per.tipo_identificacion!='"+tipoID+"' and per.numero_identificacion!='"+numeroID+"' ";
		if(UtilidadTexto.isEmpty(segundoNombre))
			consultarPacienteStr+=" and (per.segundo_nombre is null or per.segundo_nombre='') ";
		else
			consultarPacienteStr+=" and UPPER(per.segundo_nombre) = UPPER('"+segundoNombre+"') ";
			
		if(UtilidadTexto.isEmpty(segundoApellido))
			consultarPacienteStr+=" and (per.segundo_apellido is null or per.segundo_apellido='') ";
		else
			consultarPacienteStr+=" and UPPER(per.segundo_apellido) = UPPER('"+segundoApellido+"') ";
				
	
		ArrayList<HashMap<String, Object>> pacientes = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst=  null;
		ResultSetDecorator rs = null;
		try
		{
			pst= new PreparedStatementDecorator(con.prepareStatement(consultarPacienteStr));
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("numeroId",rs.getString("numeroid"));
				elemento.put("tipoId",rs.getString("tipoid"));
				elemento.put("nomTipoId", rs.getString("nomtipoid"));
				elemento.put("nombrePersona", rs.getString("nombrepersona"));
				pacientes.add(elemento);
			}	
			
		}
		catch(SQLException e){
			logger.error("Error consultando a un paciente en SqlBasePacienteDao: "+e);
			
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}
		
		return pacientes;
	}
	
	
	
	
	/**
	 * M�todo para insertar una observacion para un paciente
	 * @param con
	 * @param codigoPaciente
	 * @param observaciones
	 * @return
	 * @throws SQLException
	 */
	public static int insertarObservacionPaciente( Connection con, int codigoPaciente, String observaciones) throws SQLException
	{
		int resp=0;
		ResultSetDecorator rs = null;
		PreparedStatementDecorator ps= null;
		try
			{
				ps=  new PreparedStatementDecorator(con.prepareStatement(actualizarNotaObservconPacienteStr));
				ps.setString(1, observaciones);
				ps.setInt(2, codigoPaciente);
				resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la insercion de las observaciones gernerales de un paciente [SqlBasePacienteDao]:"+e.toString() );
				resp=0;
			}finally{
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(ps!=null){
						ps.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
				
			}
			return resp;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param motivoPaciente
	 * @return
	 * @throws SQLException
	 */
	public static int insertarMotivoCitaPacOdontologia( Connection con, DtoMotivoCitaPaciente motivoPaciente) 
	{
		int resp=0;
		String cadenaInsercion=insertarMotivoCitaPacinente;
		String cadenaConocServ=ingresarConocimientoServPac ;
		
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejoPaciente.seq_motivo_cita_paciente");
		
		PreparedStatementDecorator ps= null;
		PreparedStatementDecorator ps2= null;
		try
			{
			logger.info("ingreso Motivo Codigo "+motivoPaciente.getCodMotivo());
			logger.info("COdigoPaciente >>"+motivoPaciente.getCodigoPaciente());
				ps=  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion));
				ps.setInt(1, consecutivo);
				ps.setInt(2, Utilidades.convertirAEntero(motivoPaciente.getCodigoPaciente()));
				ps.setInt(3, Utilidades.convertirAEntero(motivoPaciente.getCodMotivo()));				
				ps.setString(4, motivoPaciente.getUsuarioModificacion());
				
				if(ps.executeUpdate()>0)
				{
					if(motivoPaciente.getCodMedioConocimiento()!=null && !motivoPaciente.getCodMedioConocimiento().equals(""))
					{	
					 try
					   {
						 logger.info("codigo Medio Conocimiento >>"+motivoPaciente.getCodMedioConocimiento()+"<<");
						int consecutivoServ= UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejoPaciente.seq_conocimiento_serv_pac");
						ps2=  new PreparedStatementDecorator(con.prepareStatement(cadenaConocServ));
						ps2.setInt(1, consecutivoServ);
						ps2.setInt(2, Utilidades.convertirAEntero(motivoPaciente.getCodigoPaciente()));
						ps2.setInt(3, Utilidades.convertirAEntero(motivoPaciente.getCodMedioConocimiento()));
						ps2.setString(4, motivoPaciente.getUsuarioModificacion());	
						
						if(ps2.executeUpdate()>0)
						{
							resp=1;
						}
						
					 } catch(SQLException e)
					   {
						logger.warn(e+" Error en la insercion del conocimiento del Servicio [SqlBasePacienteDao]:"+e.toString() );
						logger.info("Cadena Insercion COnocimiento Servicio >> "+cadenaConocServ);
						resp=0;
					  }finally{
							try {
								if(ps2!=null){
									ps2.close();
								}
								
							} catch(SQLException sqlException){
								logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
							}
					  }
					}else
					{
						logger.info("NO SE PARAMETRIZO CONOCIMIENTO");
						resp=1;
					}
				}
				
			
				
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la inserci�n de los datos Motivo Cita Paciente Odontologico [SqlBasePacienteDao]:"+e.toString() );
				resp=0;
			}finally{
				try{
					if(ps!=null){
						ps.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
			
				
			}
			
			return resp;
	}
	
	
	/**
	 * M�todo apra consultar las observaciones de un paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public static String consultarObservacionesPaciente( Connection con, int codigoPaciente) throws SQLException
	{
		String observaciones="";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		try
			{
				 ps=  new PreparedStatementDecorator(con.prepareStatement(consultarObservacionesPacienteStr));
				ps.setInt(1, codigoPaciente);
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					String temp=rs.getString("observaciones")+"";
					if(!temp.trim().equals(null) && !temp.trim().equals("null"))
					{
						observaciones=rs.getString("observaciones");
					}
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la consulta de las obervaciones de un paciente [SqlBasePacienteDao]:"+e.toString() );
					observaciones="";
			}finally{
				try{
					if(ps!=null){
						ps.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
			return observaciones;
	}
	
	/**
	 * M�todo implementado para insertar el paciente al triage
	 * @param con
	 * @param datosTriage
	 * @return
	 */
	public static int insertarPacienteTriage(Connection con,HashMap datosTriage)
	{
		PreparedStatementDecorator pst= null;
		
		try
		{
			String consulta = insertarPacienteTriageStr + 
				" VALUES ("+datosTriage.get("secuencia")+",?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?) ";
			
			 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setObject(1,datosTriage.get("codigoPaciente"));
			pst.setObject(2,datosTriage.get("usuario"));
			pst.setObject(3,datosTriage.get("centroAtencion"));
			pst.setNull(4,Types.NUMERIC);
			pst.setNull(5,Types.VARCHAR);
			pst.setBoolean(6,false);
			if(UtilidadTexto.isEmpty(datosTriage.get("clasificacion")+"")||Utilidades.convertirAEntero(datosTriage.get("clasificacion")+"")<=0)
				pst.setObject(7,null);
			else
				pst.setObject(7,datosTriage.get("clasificacion"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarPacienteTriage de SqlBasePacienteDao: ",e);
			return -1;
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * M�todo que actualiza el grupo poblacional de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarGrupoPoblacional(Connection con,HashMap campos)
	{
		int resultado = 0;
		PreparedStatementDecorator pst= null;
		
		try
		{
			//*************SE TOMAN PAR�METROS**************************************
			int codigoPaciente = Integer.parseInt(campos.get("codigoPaciente").toString());
			String grupoPoblacional = campos.get("grupoPoblacional").toString();
			//**********************************************************************
			
			String consulta = "UPDATE pacientes SET grupo_poblacional = ? WHERE codigo_paciente = ?";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			pst.setString(1, grupoPoblacional);
			pst.setInt(2, codigoPaciente);
			resultado = pst.executeUpdate();
			
			pst.close();
		}		
		catch(SQLException e)
		{
			logger.error("Error en actualizarGrupoPoblacional: "+e);
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}
		return resultado;
	}

	/**
	 * Metodo para consultar Motivo de Cita de un paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoMotivoCitaPaciente consultarMotivoCitaPacOdontologia(Connection con, int codigoPaciente) {
		DtoMotivoCitaPaciente dtoMotivoCita= new DtoMotivoCitaPaciente();
		String consultaMotivo=consultarMotivoCitaPacienteStr;
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			pst= new PreparedStatementDecorator(con.prepareStatement(consultaMotivo));
			pst.setInt(1,codigoPaciente);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				dtoMotivoCita.setCodigo(rs.getInt("codigopk")+"");
				dtoMotivoCita.setCodigoPaciente(rs.getInt("codigopaciente")+"");
				dtoMotivoCita.setCodMotivo(rs.getInt("codmotivo")+"");
				dtoMotivoCita.setDescripcionMotivo(rs.getString("descripcionmotivo"));
				dtoMotivoCita.setCodMedioConocimiento(rs.getInt("codconocimiento")+"");
				dtoMotivoCita.setFechaModificacion(rs.getString("fechamodificacion"));
				dtoMotivoCita.setHoraModificacion(rs.getString("horamodificacion"));
				dtoMotivoCita.setUsuarioModificacion(rs.getString("usuariomodificacion"));				
			}		
			
			rs.close();
			pst.close();
		}
		catch(SQLException e){
			logger.error("Error consultando Motivo Cita Paciente SqlBasePacienteDao: "+e);
			
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}
		
		return dtoMotivoCita;
	}

	/**
	 * Metodo para consultar existencia de un motivo de cita de Paciente Odontologia
	 * @param codigoPaciente
	 * @return
	 */
	 public static boolean existeMotivoCitaPaciente(int codigoPaciente)
	 {
		 boolean existe =false;
		 String consultaMotivo=existeMotivoCitaPacienteStr;
		 Connection con = null;
		 con = UtilidadBD.abrirConexion();
		 PreparedStatementDecorator pst= null;
		 ResultSetDecorator rs = null;
			try
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaMotivo));
				pst.setInt(1,codigoPaciente);
				
				 rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					existe = true;			
				}		
			}
			catch(SQLException e){
				logger.error("Error consultando Existe Motivo Cita Paciente SqlBasePacienteDao: "+e);
				
			}finally{
				try{
					if(pst!=null){
						pst.close();
					}					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
			}
			UtilidadBD.closeConnection(con);
		 return existe;
	 }
	 
	 
	 /**
	  * Metodo para consultar si existe conocimiento del Servicio asociado a un paciente Odontologia
	  * @param codigoPaciente
	  * @return
	  */
	 public static boolean existeConocimientoServPac(int codigoPaciente)
	 {
		 boolean existe =false;
		 String consultaMotivo=existeConocimientoServPac;
		 Connection con = null;
		 con = UtilidadBD.abrirConexion();
		 PreparedStatementDecorator pst= null;
		 ResultSetDecorator rs = null;
			try
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaMotivo));
				pst.setInt(1,codigoPaciente);
				
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					existe = true;			
				}	
				
			
			}
			catch(SQLException e){
				logger.error("Error consultando Existe Conocimientos del Servcio Paciente SqlBasePacienteDao: "+e);
				
			}finally{
				try{
					if(pst!=null){
						pst.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
			UtilidadBD.closeConnection(con);
		 return existe;
	 }
	
	 
	 /**
	  * Metodo que consulta si existe un beneficiario asociadio a un paciente
	  * @param codigoPaciente
	  * @param codigoBeneficiario
	  * @return
	  */
	 public static boolean existeBeneficiarioPacienteOdont(int codigoPaciente, int codigoBeneficiario)
	 {
		 boolean existe= false;
		 String consultaBeneficiario=existeBeneficiarioPacienteOdontStr;
		 
		 Connection con = null;
		 con = UtilidadBD.abrirConexion();
		 PreparedStatementDecorator pst= null;
		 ResultSetDecorator rs = null;
			try
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(consultaBeneficiario));
				pst.setInt(1,codigoPaciente);
				pst.setInt(2,codigoBeneficiario);
				
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					existe = true;			
				}	
				
				rs.close();
				pst.close();
			}
			catch(SQLException e){
				logger.error("Error consultando Existe Beneficiario Paciente SqlBasePacienteDao: "+e);
				
			}finally{
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(pst!=null){
						pst.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
			UtilidadBD.closeConnection(con);
		 
		 
		 return existe;
	 }
	
	 /**
	  * Metodo para modificar el Motivo de cita paciente odontologia
	  * @param con
	  * @param motivoCitaPaciente
	  * @return
	  */
	 public static int modificarMotivoCitaPacOdontologia(Connection con,DtoMotivoCitaPaciente motivoCitaPaciente) {
			int resultado = 1;
			String cadenaModificar= modificarMotivoCitaPacienteStr;
			String cadenaConocServ=ingresarConocimientoServPac ;
			logger.info("\n Modificacion Motivo Codigo "+motivoCitaPaciente.getCodMotivo());
			logger.info("\n Modificacion Motivo Codigo Paciente "+motivoCitaPaciente.getCodigoPaciente());
			PreparedStatementDecorator pst= null;

			if(existeMotivoCitaPaciente(Utilidades.convertirAEntero(motivoCitaPaciente.getCodigoPaciente())))
			 {			 
				try
				{							
					pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificar));
					
					pst.setInt(1, Utilidades.convertirAEntero(motivoCitaPaciente.getCodMotivo()));
					pst.setString(2, motivoCitaPaciente.getUsuarioModificacion());
					pst.setInt(3, Utilidades.convertirAEntero(motivoCitaPaciente.getCodigoPaciente()));
				    
					if(pst.executeUpdate()>0)
					{
						
					 if(!motivoCitaPaciente.getCodMedioConocimiento().equals(""))
					 {
						if(existeConocimientoServPac(Utilidades.convertirAEntero(motivoCitaPaciente.getCodigoPaciente())))
						 {
							logger.info("Si existe Conociemiento ");
							resultado = modificarConociemientoServicioPac(con, motivoCitaPaciente); 
						 }else
						 {
							 logger.info("NO existe Conocimiento ");
								PreparedStatementDecorator ps2= null;
							 try
							   {
								int consecutivoServ= UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejoPaciente.seq_conocimiento_serv_pac");
								 ps2=  new PreparedStatementDecorator(con.prepareStatement(cadenaConocServ));
								ps2.setInt(1, consecutivoServ);
								ps2.setInt(2, Utilidades.convertirAEntero(motivoCitaPaciente.getCodigoPaciente()));
								ps2.setInt(3, Utilidades.convertirAEntero(motivoCitaPaciente.getCodMedioConocimiento()));
								ps2.setString(4, motivoCitaPaciente.getUsuarioModificacion());	
								
								if(ps2.executeUpdate()>0)
								{
									resultado=1;
								}
								ps2.close();
							 } catch(SQLException e)
							   {
								 resultado=0;
								logger.warn(e+" Error en la insercion del conocimiento del Servicio [SqlBasePacienteDao]:"+e.toString() );
								
							   } finally{
								   try {
										if(ps2!=null){
											ps2.close();
										}
										
									} catch(SQLException sqlException){
										logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUtilidadesJustificacionNoPosDao "+sqlException.toString() );
									}
							   }
						 }
					  }else
					   {
						 resultado=0;
					   }
					}else
					{
						resultado=0;
					}
				    
					pst.close();
				}
				catch(SQLException e)
				{
					resultado=0;
					logger.error("Error en actualizarMotivoCitaPaciente: "+e);
				}finally{
					try{
						if(pst!=null){
							pst.close();
						}
						
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
					}
					
					
				}
			 }else
			 {
				 resultado= insertarMotivoCitaPacOdontologia(con, motivoCitaPaciente); 
			 }
			
			
			return resultado;
		}
	 
	 
	
	/**
	 * Metodo para modificar un motivo de cita de un paciente
	 * @param con
	 * @param motivoCitaPaciente
	 * @return
	 */
	public static int modificarConociemientoServicioPac(Connection con,DtoMotivoCitaPaciente motivoCitaPaciente) {
		int resultado = 1;
		String cadenaModificar= modificarConocimientoServPac;
		logger.info("ingreso MedioConocimiento Codigo "+motivoCitaPaciente.getCodMedioConocimiento());
		PreparedStatementDecorator pst =  null;
		    try
			{				
				 pst =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificar));
				
				pst.setInt(1, Utilidades.convertirAEntero(motivoCitaPaciente.getCodMedioConocimiento()));
				pst.setString(2, motivoCitaPaciente.getUsuarioModificacion());
				pst.setInt(3, Utilidades.convertirAEntero(motivoCitaPaciente.getCodigoPaciente()));
			    pst.executeUpdate();
			    
			
			}
			catch(SQLException e)
			{
				resultado=0;
				logger.error("Error en actualizarConocimientoServicioPaciente: "+e);
			}finally{
				try{
					if(pst!=null){
						pst.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
			logger.info("YA ingreso MedioConocimiento Codigo "+motivoCitaPaciente.getCodMedioConocimiento());	
		return resultado;
	}

	
	/**
	 * Metodo para insertar Beneficiario paciente
	 * @param con
	 * @param beneficiario
	 * @param loginUsuario
	 * @return
	 */
	public static int insertarBeneficiarioPaciente(Connection con,DtoBeneficiarioPaciente beneficiario, String loginUsuario,int codInstitucion) {
		
		int resp=1;
		int codigoPaciente=0;
		String cadenaIngresarBeneficiario=ingresarBeneficiarioPacienteStr;
		String cadenaModificarBeneficiario=modificarBeneficiarioPacienteStr;
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejoPaciente.seq_beneficiarios_paciente");
		String anioNacimiento="", mesNacimiento="",diaNacimiento="";
		PreparedStatementDecorator deboInsertarStatement = null;
		ResultSetDecorator rs= null;
		if(beneficiario.getFechaNacimiento()!=null && !beneficiario.getFechaNacimiento().equals(""))
		{
			beneficiario.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaAAp(beneficiario.getFechaNacimiento()));	
			String[] fechaNac = beneficiario.getFechaNacimiento().split("/");
			anioNacimiento=fechaNac[2];
			mesNacimiento =fechaNac[1];
			diaNacimiento=fechaNac[0];
		}

				
		try
		{
		
			RespuestaInsercionPersona respInsercion = new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
			
			deboInsertarStatement =  new PreparedStatementDecorator(con, deboInsertarPersonaStr);
			deboInsertarStatement.setString(1, beneficiario.getCodigoTipoIdentificacion());
			deboInsertarStatement.setString(2, beneficiario.getNumeroId());
			
			 rs=new ResultSetDecorator(deboInsertarStatement.executeQuery());
			if (rs.next())
			{
				codigoPaciente=rs.getInt("codigoPaciente");
			}
			rs.close();
			//No hay necesidad de colocar el c�digo del paciente
			//En 0 o -1, y que inicialmente tiene estos valores
			// Solo si no hay una persona insertada, hacemos la insercion en persona
			if (codigoPaciente<1)
			{
				respInsercion  = Persona.insertarPersona(con, beneficiario.getNumeroId(), beneficiario.getCodigoTipoIdentificacion(), "", "", "", "", "", "", ConstantesBDManejoPaciente.codigoTipoPersonaNatural+"", diaNacimiento,mesNacimiento, anioNacimiento, "", beneficiario.getSexo(), beneficiario.getPrimerNombre(), beneficiario.getSegundoNombre(), beneficiario.getPrimerApellido(), beneficiario.getSegundoApellido(), "", "", "", "", "", "", beneficiario.getTelefono(), "", beneficiario.getCelular(), ConstantesBD.tipoPersonaPaciente,codInstitucion,beneficiario.getTelefono());
				codigoPaciente=respInsercion.getCodigoPersona();
			}
			else
			{
				int resModificacion = Persona.modificarPersona(con, beneficiario.getCodigoTipoIdentificacion(), beneficiario.getNumeroId(), "", "", "", codigoPaciente, "", "", "", ConstantesBDManejoPaciente.codigoTipoPersonaNatural+"", diaNacimiento, mesNacimiento, anioNacimiento, "", beneficiario.getSexo(), beneficiario.getPrimerNombre(), beneficiario.getSegundoNombre(), beneficiario.getPrimerApellido(), beneficiario.getSegundoApellido(), "", "", "", "", "", "", beneficiario.getTelefono(), "", beneficiario.getCelular(), ConstantesBD.continuarTransaccion, ConstantesBD.tipoPersonaPaciente, codInstitucion,beneficiario.getTelefono());
				                                                                                                          	
				//No necesita cambio de identificaci�n porque todo salio bien
				respInsercion.setNecesitaCambioIdentificacion(false);
				
				if(resModificacion>0)
				{
					respInsercion.setSalioBien(true);
				    respInsercion.setCodigoPersona(codigoPaciente);   
				   
				}
				 
			}
			beneficiario.setCodigoBeneficiario(codigoPaciente+"");
			
			if(existeBeneficiarioPacienteOdont(Utilidades.convertirAEntero(beneficiario.getCodigoPaciente()),Utilidades.convertirAEntero(beneficiario.getCodigoBeneficiario())))
			{
				PreparedStatementDecorator modificarBeneficiarioStatement = null;
				try
				{
					 modificarBeneficiarioStatement =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarBeneficiario));
					
					if(!beneficiario.getParentezco().equals("") && Utilidades.convertirAEntero(beneficiario.getParentezco())>0)
						modificarBeneficiarioStatement.setInt(1, Utilidades.convertirAEntero(beneficiario.getParentezco()));
					else
						modificarBeneficiarioStatement.setNull(1,Types.INTEGER);
					
					if(!beneficiario.getOcupacion().equals("") && Utilidades.convertirAEntero(beneficiario.getOcupacion())>0)
						modificarBeneficiarioStatement.setInt(2, Utilidades.convertirAEntero(beneficiario.getOcupacion()));
					else
						modificarBeneficiarioStatement.setNull(2,Types.INTEGER);
					
					if(!beneficiario.getEstudio().equals("") && Utilidades.convertirAEntero(beneficiario.getNivelEstudios())>0 )
						modificarBeneficiarioStatement.setInt(3, Utilidades.convertirAEntero(beneficiario.getNivelEstudios()));
					else
						modificarBeneficiarioStatement.setNull(3,Types.NUMERIC);
					
					if(!beneficiario.getTipoOcupacion().equals(""))
						modificarBeneficiarioStatement.setString(4, beneficiario.getTipoOcupacion());
					else
						modificarBeneficiarioStatement.setNull(4,Types.VARCHAR);
					
					modificarBeneficiarioStatement.setString(5, loginUsuario);
					modificarBeneficiarioStatement.setInt(6, Utilidades.convertirAEntero(beneficiario.getCodigoPaciente()));
					modificarBeneficiarioStatement.setInt(7, Utilidades.convertirAEntero(beneficiario.getCodigoBeneficiario()));
					
					if(modificarBeneficiarioStatement.executeUpdate()<=0)
						resp=0;
					
					modificarBeneficiarioStatement.close();
					
				}catch(SQLException e){
					logger.info("error Modificando en la tabla Beneficiario Paciente : SqlPacienteDao"+ e);
					resp=0;
				}finally{
					try{
						if(	modificarBeneficiarioStatement!=null){
							modificarBeneficiarioStatement.close();
						}
						
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
					}
					
					
				}
			}else
			{	
				PreparedStatementDecorator insertarBeneficiarioStatement =   null;
				try
				{  		
					insertarBeneficiarioStatement =  new PreparedStatementDecorator(con.prepareStatement(cadenaIngresarBeneficiario));
					insertarBeneficiarioStatement.setInt(1, consecutivo);
					insertarBeneficiarioStatement.setInt(2, Utilidades.convertirAEntero(beneficiario.getCodigoPaciente()));
					insertarBeneficiarioStatement.setInt(3, Utilidades.convertirAEntero(beneficiario.getCodigoBeneficiario()));
					
					if(!beneficiario.getParentezco().equals("") && Utilidades.convertirAEntero(beneficiario.getParentezco())>0 )
						insertarBeneficiarioStatement.setInt(4, Utilidades.convertirAEntero(beneficiario.getParentezco()));
					else
						insertarBeneficiarioStatement.setNull(4,Types.INTEGER);
					
					if(!beneficiario.getOcupacion().equals("") && Utilidades.convertirAEntero(beneficiario.getOcupacion())>0)
						insertarBeneficiarioStatement.setInt(5, Utilidades.convertirAEntero(beneficiario.getOcupacion()));
					else
						insertarBeneficiarioStatement.setNull(5,Types.INTEGER);
					
					if(!beneficiario.getEstudio().equals("") && Utilidades.convertirAEntero(beneficiario.getNivelEstudios())>0)
						insertarBeneficiarioStatement.setInt(6, Utilidades.convertirAEntero(beneficiario.getNivelEstudios()));
					else
						insertarBeneficiarioStatement.setNull(6,Types.NUMERIC);
					
					if(!beneficiario.getTipoOcupacion().equals(""))
						insertarBeneficiarioStatement.setString(7, beneficiario.getTipoOcupacion());
					else
						insertarBeneficiarioStatement.setNull(7,Types.VARCHAR);
									
					insertarBeneficiarioStatement.setString(8, loginUsuario);
					
				    if(insertarBeneficiarioStatement.executeUpdate()<=0)
				    {
				    	resp=0;
				    }
			    
				insertarBeneficiarioStatement.close();
				}catch(SQLException e){
					logger.info("error Insertando en la tabla Beneficiario Paciente : SqlPacienteDao"+ e);
					resp=0;
				}finally{
					try{
						if(insertarBeneficiarioStatement!=null){
							insertarBeneficiarioStatement.close();
						}
						
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
					}
					
					
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error al insertar un beneficiario: ",e);
			resp=0;
		}finally{
			try {
				if(deboInsertarStatement!=null){
					deboInsertarStatement.close();
				}
			} catch (SQLException e2) {
				logger.warn(e2+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+e2.toString() );
			}
			try {
				if(rs!=null){
					rs.close();
				}
			} catch (SQLException e2) {
				logger.warn(e2+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+e2.toString() );
			}
		}
		return resp;
	}

	
	/**
	 * 
	 * @param con
	 * @param beneficiario
	 * @param loginUsuario
	 * @param codInstitucion
	 * @return
	 */
	public static int modificarBeneficiarioPaciente(Connection con,	DtoBeneficiarioPaciente beneficiario, String loginUsuario,int codInstitucion) {
		int resp=1;
		int codigoPaciente=0;
		String cadenaModificarBeneficiario=modificarBeneficiarioPacienteStr;
		String anioNacimiento="", mesNacimiento="",diaNacimiento="";
		
		
		if(!beneficiario.getFechaNacimiento().equals(""))
		{
		 String[] fechaNac = beneficiario.getFechaNacimiento().split("/");
		 anioNacimiento=fechaNac[2];
		 mesNacimiento =fechaNac[1];
		 diaNacimiento=fechaNac[0];
			
		}
		RespuestaInsercionPersona respInsercion =  null;
		PreparedStatementDecorator deboInsertarStatement = null;
		ResultSetDecorator rs= null;
		try
		{
			respInsercion = new RespuestaInsercionPersona(false,false, "Error en la insercion", 0);
			
			deboInsertarStatement =  new PreparedStatementDecorator(con.prepareStatement(deboInsertarPersonaStr));
			deboInsertarStatement.setString(1, beneficiario.getCodigoTipoIdentificacion());
			deboInsertarStatement.setString(2, beneficiario.getNumeroId());
			
			rs=new ResultSetDecorator(deboInsertarStatement.executeQuery());
			if (rs.next())
			{
				codigoPaciente=rs.getInt("codigoPaciente");
			}
			rs.close();
			//No hay necesidad de colocar el c�digo del paciente
			//En 0 o -1, y que inicialmente tiene estos valores
			// Solo si no hay una persona insertada, hacemos la insercion en persona
			if (codigoPaciente<1)
			{
				respInsercion  = Persona.insertarPersona(con, beneficiario.getNumeroId(), beneficiario.getCodigoTipoIdentificacion(), "", "", "", "", "", "", ConstantesBDManejoPaciente.codigoTipoPersonaNatural+"", diaNacimiento,mesNacimiento, anioNacimiento, "", beneficiario.getSexo(), beneficiario.getPrimerNombre(), beneficiario.getSegundoNombre(), beneficiario.getPrimerApellido(), beneficiario.getSegundoApellido(), "", "", beneficiario.getCiudadResidencia(), beneficiario.getPaisResidencia(), "", "", beneficiario.getTelefono(), beneficiario.getEmail(), beneficiario.getCelular(), ConstantesBD.tipoPersonaPaciente,codInstitucion,beneficiario.getTelefono());
				codigoPaciente=respInsercion.getCodigoPersona();
			}
			else
			{
				int resModificacion = Persona.modificarPersona(con, beneficiario.getCodigoTipoIdentificacion(), beneficiario.getNumeroId(), "", "", "", codigoPaciente, "", "", "", ConstantesBDManejoPaciente.codigoTipoPersonaNatural+"", diaNacimiento, mesNacimiento, anioNacimiento, "", beneficiario.getSexo(), beneficiario.getPrimerNombre(), beneficiario.getSegundoNombre(), beneficiario.getPrimerApellido(), beneficiario.getSegundoApellido(), "", "", beneficiario.getCiudadResidencia(), beneficiario.getPaisResidencia(), "", "", beneficiario.getTelefono(), beneficiario.getEmail(), beneficiario.getCelular(), ConstantesBD.continuarTransaccion, ConstantesBD.tipoPersonaPaciente, codInstitucion,beneficiario.getTelefono());
				                                                                                                          	
				//No necesita cambio de identificaci�n porque todo salio bien
				respInsercion.setNecesitaCambioIdentificacion(false);
				
				if(resModificacion>0)
				{
					respInsercion.setSalioBien(true);
					respInsercion.setCodigoPersona(codigoPaciente);
				}				
			}
			beneficiario.setCodigoBeneficiario(codigoPaciente+"");
			PreparedStatementDecorator modificarBeneficiarioStatement = null;
			try
			{
			 modificarBeneficiarioStatement =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarBeneficiario));
							
			if(!beneficiario.getParentezco().equals("") && Utilidades.convertirAEntero(beneficiario.getParentezco())>0)
				modificarBeneficiarioStatement.setInt(1, Utilidades.convertirAEntero(beneficiario.getParentezco()));
			else
				modificarBeneficiarioStatement.setNull(1,Types.INTEGER);
			
			if(!beneficiario.getOcupacion().equals("") && Utilidades.convertirAEntero(beneficiario.getOcupacion())>0)
				modificarBeneficiarioStatement.setInt(2, Utilidades.convertirAEntero(beneficiario.getOcupacion()));
			else
				modificarBeneficiarioStatement.setNull(2,Types.INTEGER);
			
			if(!beneficiario.getEstudio().equals("") && Utilidades.convertirAEntero(beneficiario.getNivelEstudios())>0 )
				 modificarBeneficiarioStatement.setInt(3, Utilidades.convertirAEntero(beneficiario.getNivelEstudios()));
			else
				modificarBeneficiarioStatement.setNull(3,Types.NUMERIC);
			
			if(!beneficiario.getTipoOcupacion().equals(""))
				modificarBeneficiarioStatement.setString(4, beneficiario.getTipoOcupacion());
			else
				modificarBeneficiarioStatement.setNull(4,Types.VARCHAR);
	       							
	        modificarBeneficiarioStatement.setString(5, loginUsuario);
			modificarBeneficiarioStatement.setInt(6, Utilidades.convertirAEntero(beneficiario.getCodigoPaciente()));
			modificarBeneficiarioStatement.setInt(7, Utilidades.convertirAEntero(beneficiario.getCodigoBeneficiario()));
			
			 if(modificarBeneficiarioStatement.executeUpdate()<=0)
				 resp=0;
			 
			 modificarBeneficiarioStatement.close();
			
			}catch(SQLException e){
				logger.info("error Modificando en la tabla Beneficiario Paciente : SqlPacienteDao"+ e);
				resp=0;
			}finally{
				try{
					if(modificarBeneficiarioStatement!=null){
						modificarBeneficiarioStatement.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
		
		
	}
	catch(SQLException e)
	{
		resp=0;
		logger.info("error Modificando Beneficiario Paciente : SqlPacienteDao"+ e);
	}finally{
		
		try{
			if(deboInsertarStatement!=null){
				deboInsertarStatement.close();
			}
			
		}catch(SQLException sqlException){
			logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
		}
		try{
			if(rs!=null){
				rs.close();
			}
			
		}catch(SQLException sqlException){
			logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
		}
		
		
	}
	
	return resp;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ArrayList<DtoBeneficiarioPaciente> consultarBeneficiariosPaciente(Connection con,int codigoPaciente) {
		
		ArrayList<DtoBeneficiarioPaciente> arrayBeneficiarios= new ArrayList<DtoBeneficiarioPaciente>();
		String consultaBeneficiarios= "SELECT " +
				    "benpac.codigo_pk AS codigopk, " +
					"benpac.codigo_paciente AS codigopaciente, " +
					"benpac.persona_beneficiario AS personabeneficiario, " +
					"benpac.parentezco AS parentezco, " +
					"benpac.ocupacion AS ocupacionbeneficiario, " +								
					"CASE WHEN benpac.estudio  IS NULL THEN 0 ELSE benpac.estudio  END AS estudiobeneficiario, " +
					"CASE WHEN benpac.estudio  IS NULL THEN '' ELSE est.nombre END AS nombre_estudio, " +								
					"benpac.tipo_ocupacion AS tipocupacion, " +
					"to_char(benpac.fecha_modificacion, '"+ConstantesBD.formatoFechaAp+"') AS fechamodificacion, " +
					"benpac.hora_modificacion AS horamodificacion, " +
					"benpac.usuario_modificacion AS usuariomodificacion, " +								
					"per.numero_identificacion AS numeroidentificacion,	" +
					"per.tipo_identificacion AS codigotipotdentificacion, " +
					"per.tipo_persona AS codigotipopersona, " +
					"tp.nombre as nomtipopersona," +
					"to_char(per.fecha_nacimiento, '"+ConstantesBD.formatoFechaAp+"') AS fechanacimiento, " +
					"per.primer_nombre AS primernombrepersona, " +
					"coalesce(per.segundo_nombre,'') AS segundonombrepersona," +
					"per.primer_apellido AS primerApellidoPersona, " +
					"coalesce(per.segundo_apellido,'') AS segundoapellidopersona, " +								
					"coalesce(per.codigo_ciudad_vivienda,'') AS codigociudad," +
					"coalesce(administracion.getnombreciudad(per.codigo_pais_vivienda,per.codigo_departamento_vivienda,per.codigo_ciudad_vivienda),'') AS ciudad, " +
					"coalesce(per.codigo_pais_vivienda,'') AS codigoPais, " +
					"coalesce(administracion.getdescripcionpais(per.codigo_pais_vivienda),'') AS pais, " +
					"coalesce(to_char(per.telefono_fijo, '9999999'),'') AS telefono_fijo, " +
					"coalesce(to_char(per.telefono_celular, '9999999999'),'') AS telefono_celular, " +
					"per.email AS email, " +
					"per.sexo AS sexo " +																					
					"FROM manejopaciente.beneficiarios_paciente benpac " +
                    "INNER JOIN administracion.personas per ON ( per.codigo = benpac.persona_beneficiario ) " +    		                   
                    "LEFT OUTER JOIN manejopaciente.estudio est ON (est.codigo = benpac.estudio) "+ 
                	"INNER JOIN administracion.tipos_personas tp ON (per.tipo_persona=tp.codigo) " +
                    "WHERE benpac.codigo_paciente = ? ";

		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		try
		{
			pst= new PreparedStatementDecorator(con,consultaBeneficiarios);
			pst.setInt(1,codigoPaciente);
			//Log4JManager.info(pst);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoBeneficiarioPaciente dtoBeneficiario= new DtoBeneficiarioPaciente();
				dtoBeneficiario.setCodigo(rs.getInt("codigopk"));
				dtoBeneficiario.setCodigoPaciente(rs.getInt("codigopaciente")+"");
				dtoBeneficiario.setPersonaBeneficiario(rs.getInt("personabeneficiario")+"");
				dtoBeneficiario.setParentezco(rs.getInt("parentezco")+"");
				dtoBeneficiario.setOcupacion(rs.getInt("ocupacionbeneficiario")+"");
				dtoBeneficiario.setTipoOcupacion(rs.getString("tipocupacion"));
				dtoBeneficiario.setEstudio(rs.getInt("estudiobeneficiario")+"");
				dtoBeneficiario.setNumeroId(rs.getString("numeroidentificacion"));
				dtoBeneficiario.setCodigoTipoIdentificacion(rs.getString("codigotipotdentificacion"));				
				dtoBeneficiario.setTipoPersona(rs.getInt("codigotipopersona")+"");
				dtoBeneficiario.setCodTipoPersona(rs.getString("nomtipopersona"));
				dtoBeneficiario.setFechaNacimiento(rs.getString("fechanacimiento"));		
				dtoBeneficiario.setPrimerNombre(rs.getString("primernombrepersona"));
				dtoBeneficiario.setSegundoNombre(rs.getString("segundonombrepersona"));
				dtoBeneficiario.setPrimerApellido(rs.getString("primerapellidopersona"));				
				dtoBeneficiario.setSegundoApellido(rs.getString("segundoapellidopersona"));
				dtoBeneficiario.setCiudadResidencia(rs.getString("codigociudad"));
				dtoBeneficiario.setPaisResidencia(rs.getString("codigopais"));
				dtoBeneficiario.setTelefono(rs.getString("telefono_fijo"));			
				dtoBeneficiario.setCelular(rs.getString("telefono_celular"));
				dtoBeneficiario.setEmail(rs.getString("email"));
				dtoBeneficiario.setNivelEstudios(rs.getInt("estudiobeneficiario")+"");
				Log4JManager.info("ESTE ES EL SEXOOOOOOOOOOOOOOOOOOOOOO "+rs.getString("sexo"));
				dtoBeneficiario.setSexo(rs.getString("sexo"));
				
				arrayBeneficiarios.add(dtoBeneficiario);
			}		
			rs.close();
		}
		catch(SQLException e){
			logger.error("Error consultando Beneficiarios Paciente SqlBasePacienteDao: "+e);
			
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}	
		return arrayBeneficiarios;
	}

	/**
	 * 
	 * Metodo para insertar el centro de atencion duenio cuando no exista
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean actualizarCentroAtencionDuenioPaciente(Connection con, int codigoPaciente, int centroAtencionDuenio)
	{
		String consulta="UPDATE manejopaciente.pacientes SET centro_atencion_duenio=? WHERE codigo_paciente=? and centro_atencion_duenio is null ";
		boolean retorna=true;
		PreparedStatementDecorator pst = null;
		try
		{
			pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1, centroAtencionDuenio);
			pst.setInt(2, codigoPaciente);
			pst.executeUpdate();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosCuenta ",e);
			retorna=false;
		}finally{
			try{
				if(pst!=null){
					pst.close();
				}
				
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
			}
			
			
		}
		return retorna;
	}
	
	public static String consultarFechaHoraIngreso(Connection con,String codigoPaciente){
		String consulta = "select  i.fecha_ingreso||' '||i.hora_ingreso fechaHora from ingresos i where i.codigo_paciente=?";
		String fechaHora="";
		ResultSetDecorator rs = null;
		PreparedStatementDecorator ps= null;
		try
			{
				ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
				ps.setString(1, codigoPaciente);
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					String temp=String.valueOf(rs.getString("fechaHora"));
					if(!temp.trim().equals(null) && !temp.trim().equals("null"))
					{
						fechaHora=rs.getString("fechaHora");
					}
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la consulta de las obervaciones de un paciente [SqlBasePacienteDao]:"+e.toString() );
			}finally{
				try{
					if(ps!=null){
						ps.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
			return fechaHora;
		
	}
	
	public static String consultarCentroAtencionPaciente(Connection con,String codigoPaciente){
		String consulta = "SELECT ca.descripcion centroatencion FROM ingresos i,  centro_atencion ca WHERE i.codigo_paciente=? AND i.centro_atencion  =ca.consecutivo";
		String centroAtencion="";
		ResultSetDecorator rs = null;
		PreparedStatementDecorator ps= null;
		try
			{
				ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
				ps.setString(1, codigoPaciente);
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
				{
					String temp=String.valueOf(rs.getString("centroatencion"));
					if(!temp.trim().equals(null) && !temp.trim().equals("null"))
					{
						centroAtencion=rs.getString("centroatencion");
					}
				}
				
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la consulta de las obervaciones de un paciente [SqlBasePacienteDao]:"+e.toString() );
			}finally{
				try{
					if(ps!=null){
						ps.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				try{
					if(rs!=null){
						rs.close();
					}
					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseIngresoGeneralDao "+sqlException.toString() );
				}
				
				
			}
			return centroAtencion;
		
	}
	
	
}

