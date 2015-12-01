package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.CensoCamasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseCensoCamasDao;




/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class OracleCensoCamasDao implements CensoCamasDao
{
	/**
	 * Cadena de Consulta de cuenta por Cama
	 */
	private static final String strCadenaConsultaCuentaXCama = "SELECT "+
			"getcuentacamallena(?) AS cuentafechahora FROM DUAL";
	
	/**
	 * Cadena de consulta de Diagnostico por Cuenta
	 */
	private static final String strCadenaConsultaDiagnosticoXCuenta = "SELECT "+
			"getultdiagpac(?) AS diagnostico FROM DUAL";

	private static String sqlCensoOracle = 
			"SELECT * FROM "+
			"( "+
			  "(SELECT pisos.nombre          AS piso, "+
			    "camas1.numero_cama          AS cama, "+
			    "habitaciones.codigo_habitac AS codigoHabitacion, "+
			    "estados_cama.nombre         AS estado, "+
			    "personas.primer_apellido "+
			    "|| ' ' "+
			    "|| personas.segundo_apellido "+
			    "|| ' ' "+
			    "|| personas.primer_nombre "+
			    "|| ' ' "+
			    "|| personas.segundo_nombre     AS nombrePaciente, "+
			    "personas.numero_identificacion AS idPaciente, "+
			    "ingresos.consecutivo             AS ingreso, "+
			    "pacientes.historia_clinica     AS historiClinica, "+
			    "personas.fecha_nacimiento      AS fechaNacimiento, "+
			    "sexo.nombre                    AS sexo, "+
			    "TO_CHAR(fecha_ingreso, 'DD/MM/YYYY') "+
			    "|| ' ' "+
			    "|| hora_ingreso                                   AS fechaIngreso, "+
			    "convenios.nombre                                           AS convenio, "+
			    "centros_costo.nombre                                       AS centroCosto, "+
			    "estados_cama.color                                         AS colorCama, "+
			    "estados_cama.codigo                                        AS codigoEstado, "+
			    "camas1.codigo                                              AS codigoCama, "+
			    "cuentas.id                                                 AS idCuenta, "+
			    "ingresos.codigo_paciente                                   AS codigopersona, "+
			    "cuentas.via_ingreso                                        AS viaIngreso, "+
			    "personas.tipo_identificacion                               AS tipoIdentificacion, "+
			    "centros_costo.codigo		                                AS codigoCentroCosto, "+
			    "pisos.codigo				                                AS codigoPiso, "+
			    "admisiones_urgencias.FECHA_INGRESO_OBSERVACION AS fechaAsignacion, " +
			    "admisiones_urgencias.HORA_INGRESO_OBSERVACION AS horaAsignacion, " +
			    "admisiones_urgencias.FECHA_EGRESO_OBSERVACION AS fechaRetiro, " +
			    "admisiones_urgencias.HORA_EGRESO_OBSERVACION AS horaRetiro " +
			  "FROM manejopaciente.camas1, "+
			    "manejopaciente.admisiones_urgencias, "+
			    "manejopaciente.cuentas, "+
			    "manejopaciente.sub_cuentas, "+
			    "manejopaciente.ingresos, "+
			    "manejopaciente.estados_cama, "+
			    "manejopaciente.pisos, "+
			    "manejopaciente.habitaciones, "+
			    "administracion.personas, "+
			    "administracion.centros_costo, "+
			    "administracion.sexo, "+
			    "facturacion.convenios, "+
			    "manejopaciente.pacientes "+
			  "WHERE camas1.estado                       = estados_cama.codigo "+
			  "AND camas1.habitacion                     = habitaciones.codigo "+
			  "AND camas1.centro_costo                   = centros_costo.codigo "+
			  "AND admisiones_urgencias.cama_observacion = camas1.codigo "+

			  "AND admisiones_urgencias.cuenta           = cuentas.id  "+
			  "AND cuentas.id_ingreso                    = ingresos.id "+
			  "AND cuentas.codigo_paciente               = personas.codigo "+
			  "AND cuentas.codigo_paciente               = pacientes.codigo_paciente "+
			  "AND sub_cuentas.ingreso                   = ingresos.id "+
			  "AND sub_cuentas.convenio                  = convenios.codigo "+
			  
			  "AND habitaciones.piso                     = pisos.codigo "+
			  "AND personas.sexo                         = sexo.codigo "+
			  "AND sub_cuentas.nro_prioridad             = 1 "+ //convenio con prioridad uno definido en la sub cuenta
			  "reemplasoRestriccionesUrg "+
			  ") "+
			  "UNION ALL "+
			  "( "+
			  "SELECT pisos.nombre           AS piso, "+
			    "camas1.numero_cama          AS cama, "+
			    "habitaciones.codigo_habitac AS codigoHabitacion, "+
			    "estados_cama.nombre         AS estado, "+
			    "personas.primer_apellido "+
			    "|| ' ' "+
			    "|| personas.segundo_apellido "+
			    "|| ' ' "+
			    "|| personas.primer_nombre "+
			    "|| ' ' "+
			    "|| personas.segundo_nombre     AS nombrePaciente, "+
			    "personas.numero_identificacion AS idPaciente, "+
			    "ingresos.consecutivo             AS ingreso, "+
			    "pacientes.historia_clinica     AS historiClinica, "+
			    "personas.fecha_nacimiento      AS fechaNacimiento, "+
			    "sexo.nombre                    AS sexo, "+
			    "TO_CHAR(fecha_ingreso, 'DD/MM/YYYY') "+
			    "|| ' ' "+
			    "|| hora_ingreso                                   AS fechaIngreso, "+
			    "convenios.nombre                                           AS convenio, "+
			    "centros_costo.nombre                                       AS centroCosto, "+
			    "estados_cama.color                                         AS colorCama, "+
			    "estados_cama.codigo                                        AS codigoEstado, "+
			    "camas1.codigo                                              AS codigoCama, "+
			    "cuentas.id                                                 AS idCuenta, "+
			    "ingresos.codigo_paciente                                   AS codigopersona, "+
			    "cuentas.via_ingreso                                        AS viaIngreso, "+
			    "personas.tipo_identificacion                               AS tipoIdentificacion, "+
			    "centros_costo.codigo		                                AS codigoCentroCosto, "+
			    "pisos.codigo				                                AS codigoPiso, "+
			    "traslado_cama.FECHA_ASIGNACION AS fechaAsignacion, " +
			    "traslado_cama.HORA_ASIGNACION AS horaAsignacion, " +
			    "traslado_cama.FECHA_FINALIZACION AS fechaRetiro, " +
			    "traslado_cama.HORA_FINALIZACION AS horaRetiro " +
			  "FROM manejopaciente.camas1, "+
			    "manejopaciente.traslado_cama, "+
			    "manejopaciente.habitaciones, "+
			    "administracion.personas, "+
			    "manejopaciente.cuentas, "+
			    "manejopaciente.sub_cuentas, "+
			    "manejopaciente.pacientes, "+
			    "manejopaciente.ingresos, "+
			    "administracion.centros_costo, "+
			    "manejopaciente.estados_cama, "+
			    "manejopaciente.pisos, "+
			    "administracion.sexo, "+
			    "facturacion.convenios "+
			  "WHERE camas1.codigo               = traslado_cama.codigo_nueva_cama "+
			  "AND camas1.habitacion             = habitaciones.codigo "+
			  "AND camas1.centro_costo           = centros_costo.codigo "+
			  "AND traslado_cama.codigo_paciente = personas.codigo "+
			  
			  "AND traslado_cama.cuenta          = cuentas.id "+
			  "AND cuentas.codigo_paciente       = pacientes.codigo_paciente "+
			  "AND cuentas.id_ingreso            = ingresos.id "+
			  "AND sub_cuentas.ingreso = ingresos.id "+
			  "AND convenios.codigo = sub_cuentas.convenio "+
			  
			  "AND camas1.estado                 = estados_cama.codigo "+
			  "AND personas.sexo                 = sexo.codigo "+
			  "AND habitaciones.piso             = pisos.codigo "+
			  "AND sub_cuentas.nro_prioridad     = 1 "+ //convenio con prioridad uno definido en la sub cuenta
			  "reemplasoRestriccionesHos "+
			  ") "+
			  "UNION ALL "+
			  "( "+
			    "SELECT pisos.nombre           AS piso, "+
			      "camas1.numero_cama          AS cama, "+
			      "habitaciones.codigo_habitac AS codigoHabitacion, "+
			      "estados_cama.nombre         AS estado, "+
			      "NULL                        AS nombrePaciente, "+
			      "NULL                        AS idPaciente, "+
			      "NULL                        AS ingreso, "+
			      "NULL                        AS historiClinica, "+
			      "NULL                        AS fechaNacimiento, "+
			      "NULL                        AS sexo, "+
			      "NULL                        AS fechaIngreso, "+
			      "NULL                        AS convenio, "+
			      "centros_costo.nombre        AS centroCosto, "+
			      "estados_cama.color          AS colorCama, "+
			      "estados_cama.codigo         AS codigoEstado, "+
			      "camas1.codigo               AS codigoCama, "+
			      "NULL                        AS idCuenta, "+
			      "NULL                        AS codigopersona, "+
			      "NULL                        AS viaIngreso, "+
			      "NULL                        AS tipoIdentificacion, "+
			      "centros_costo.codigo        AS codigoCentroCosto, "+
			      "pisos.codigo                AS codigoPiso, "+
			      "NULL AS fechaAsignacion, " +
			      "NULL AS horaAsignacion, " +
			      "NULL AS fechaRetiro, " +
			      "NULL AS horaRetiro " +
			    "FROM manejopaciente.camas1, "+
			      "manejopaciente.habitaciones, "+
			      "manejopaciente.estados_cama, "+
			      "administracion.centros_costo, "+
			      "manejopaciente.pisos "+
			    "WHERE camas1.habitacion = habitaciones.codigo "+
			    "AND camas1.estado       = estados_cama.codigo "+
			    "AND camas1.centro_costo = centros_costo.codigo "+
			    "AND habitaciones.piso   = pisos.codigo "+
			    "AND camas1.estado IN(0,2,3,4) "+
			    "reemplasoRestriccionesNuncaUsadas "+
			    "AND camas1.codigo NOT IN "+
			    "( "+
			      "SELECT traslado_cama.codigo_nueva_cama id_cama "+
			      "FROM manejopaciente.traslado_cama "+
			      "WHERE codigo_nueva_cama IS NOT NULL "+
			      "UNION "+
			      "SELECT admisiones_urgencias.cama_observacion "+
			      "FROM manejopaciente.admisiones_urgencias "+
			      "WHERE cama_observacion IS NOT NULL "+
			    ") "+
			  ") "+
			") subconsulta "+
			"ORDER BY fechaAsignacion DESC NULLS LAST, horaAsignacion DESC NULLS LAST, fechaRetiro DESC, horaRetiro DESC ";
			
	
	/**
	 * Metodo Principal encargado de consultar el censo de camas
	 * el HashMap parametros puede contener los siguentes parametros
	 * institucion --> Requerido
	 * convenio --> Opcional
	 * centroatencion --> Opcional
	 * centrocosto --> Opcional
	 * piso --> Opcional
	 * estado --> Opcional
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @return mapa
	 * -------------------------------------------------------
	 * 			KEY'S QUE CONTIENE EL HASHMAP CENSOCAMAS
	 * --------------------------------------------------------
	 * El mapa de salida contiene los siguientes key's
	 * nombrehabitacion, cama, estadocama, nombretipohabitacion,
	 * habitacioncama, tipousuariocama, sexocama, restriccioncama
	 * cuenta, fecha, diasestancia, hora, diagnostico, convenio,
	 * nombrepac, identificacionpac, tipoidentificacionpac, 
	 * sexopac, fechanacimientopac, edadpac, restriccioncama
	 *  
	 */
	public HashMap consultaCenso (Connection connection,HashMap parametros)
	{
		//return SqlBaseCensoCamasDao.consultaCenso(connection, parametros, strCadenaConsultaCuentaXCama, strCadenaConsultaDiagnosticoXCuenta);
		return SqlBaseCensoCamasDao.consultaCenso(connection, parametros, sqlCensoOracle);
	}
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas reservadas
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public String ConsultaFechaOcupacionReserva (Connection connection, int codigoCama)
	{
		String srtCadenaConsultaFechaHoraOcupacionReserva = "SELECT * FROM (" +
				"SELECT "
				+ " to_char(lrc.fecha,'DD/MM/YYYY')  ||'@@@@@'|| lrc.hora "
				+ " FROM log_reservar_cama lrc " + " WHERE lrc.codigo_cama=? "
				+ " ORDER BY lrc.codigo DESC " 
				+ ") WHERE ROWNUM <= 1";


		return SqlBaseCensoCamasDao.ConsultaFechaOcupacionReserva (connection, codigoCama, srtCadenaConsultaFechaHoraOcupacionReserva);
	}
	
	/**
	 *Metodo utilizado para conseguir la fecha de ocupacion de las camas en
	 * estados diferentes a resercama. 
	 *esta informacion es utilizada al momento de generar las alertas. 
	 */
	public String ConsultaFechaOcupacionOtrosEst(Connection connection, int cuenta)
	{
		String sentenciaSQL = "SELECT * FROM (" +
				"SELECT "
				+ "to_char(lec.fecha,'DD/MM/YYYY') ||'@@@@@'||lec.hora "
				+ "FROM log_estados_cama lec " + "WHERE lec.cuenta=?"
				+ " ORDER BY lec.codigo DESC "
				+ ") WHERE ROWNUM <= 1";

		return SqlBaseCensoCamasDao.ConsultaFechaOcupacionOtrosEst(connection, cuenta, sentenciaSQL);
	}
	
	/**
	 *Metodo que consulta informacion estadistica y lo inserta en una tabla para
	 *luego ser consultada por otra funcionalidad. 
	 */
	public boolean corrrerProcesoAutomatico ()
	{
		String strCadenaProcesoAutomatico = " INSERT INTO proceso_automatico_censo" +
		" (estadocama,cantidad,piso,centro_atencion,institucion, codigo) SELECT c.estado,count(*) ," +
		" h.piso, h.centro_atencion, c.institucion, seq_proc_aut_censo.nextval" +
		" FROM camas1 c " +
		" INNER JOIN habitaciones h ON (h.codigo = c.habitacion) group by c.institucion, h.centro_atencion, h.piso,c.estado";
		
		return SqlBaseCensoCamasDao.corrrerProcesoAutomatico(strCadenaProcesoAutomatico);
	}
}