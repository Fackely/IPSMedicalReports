/*
 * @(#)PacienteDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.Answer;
import util.RespuestaInsercionPersona;

import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoMotivoCitaPaciente;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Paciente</code>.
 *
 * @version 1.0, Oct 7, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface PacienteDao {

	/**
	 * Inserta un paciente en una fuente de datos. Usa una conexion abierta.
	 * despues de ejecutarse. Si no esta abierta, la crea.
	 * @param con una conexion abierta con una fuente de datos
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
	 * @param anioNacimiento año de nacimiento del paciente
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
	 * @param codigoTipoSangre código del tipo de sangre del paciente
	 * @param foto nombre con el que se encuentra la foto en el sistema
	 * @param codigoInstitucion
	 * @param centro_atencion
	 * @param etnia
	 * @param lee_escribe
	 * @param estudio
	 * @param grupoPoblacional
	 * @param infoHistoSistemaAnt 
	 * @return el numero de pacientes insertados
	 */
	public RespuestaInsercionPersona insertarPaciente (Connection con,
			String numeroHistoriaClinica, String anioHistoriaClinica,
			String codigoZonaDomicilio, String codigoOcupacion, String numeroIdentificacion,String codigoTipoIdentificacion,
			String codigoDeptoId, String codigoCiudadId,
			String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, 
		   String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, 
		   String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, 
		   String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento,
		   String codigoCiudad, String codigoBarrio, String telefono, String email, String codigoTipoSangre, 
		   String foto,int codigoInstitucion, int centro_atencion, int etnia, boolean lee_escribe, int estudio,
		   String grupoPoblacional,String codigoPaisId, String codigoPaisIdentificacion, String codigoPais, 
		   String codigoLocalidad, String codigoReligion, Boolean infoHistoSistemaAnt,String telefonoCelular, String telefonoFijo,
		   String fechaMotivoCita,String horaMotivoCita,String observacionesMotivoCita, String activo,String convenioReserva, int numHijos) throws SQLException;

	/**
	 * Dados el numero y tipo de identificacion, retorna los datos de un paciente. Reutiliza una conexion
	 * abierta con una fuente de datos.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente Código de este paciente en la fuente de datos
	 * @return un objeto <code>Answer</code> con una conexion abierta y los datos del paciente
	 */
	public Answer cargarPaciente (Connection con, int codigoPaciente) throws SQLException;

	/**
	* Dados el código de un paciente modifica los datos del mismo. Utiliza una conexión abierta
	* y finaliza cualquier transacción iniciada sobre esa conexión.
	* @param ac_con					Conexión abierta con una fuente de datos
	* @param ai_codigoPersona		Código interno del paciente
	* @param as_codigoOcupacion		Código de la ocupacion del paciente
	* @param as_codigoTipoSangre	Código del tipo de sangre del paciente
	* @param as_codigoZonaDomicilio	Código de la zona de domicilio del paciente
	* @param as_foto				Ruta del archivo de la foto del paciente
	* @param centroAtencion
	* @param etnia
	* @param leeEscribe
	* @param estudio
	* @param grupoPoblacional
	 * @param boolean1 
	* @return	1	Si la modificaciòn del paciente fue exitoso
	*			0	Error de base de datos
	*			-3	La conexión a base de datos no válida
	*/
	public int modificarPaciente(
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
		int numHijos
	)throws SQLException;
	/**
	 * Método para verificar que un paciente nuevo que se vaya a ingresar no tenga 
	 * los mismos nombres y apellidos de otro paciente que ya esté registrado en el sistema
	 * @param con
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> validarPacienteIgualNombre(Connection con,String primerNombre,String segundoNombre,String primerApellido,String segundoApellido,String tipoID,String numeroID);
	
	/**
	 * Método para insertar una observacion para un paciente
	 * @param con
	 * @param codigoPaciente
	 * @param observaciones
	 * @return
	 * @throws SQLException
	 */
	public int insertarObservacionPaciente( Connection con, int codigoPaciente, String observaciones) throws SQLException;
	
	/**
	 * Método implementado para insertar el paciente al triage
	 * @param con
	 * @param datosTriage Mapa donde se guardan los atributos del triage
	 * @return
	 */
	public int insertarPacienteTriage(Connection con,HashMap datosTriage);
	
	/**
	 * Método que actualiza el grupo poblacional de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarGrupoPoblacional(Connection con,HashMap campos);

	/**
	 * Metodo que realiza la insercion de un Motivo de Cita de un Paciente
	 * @param con
	 * @param motivoCitaPaciente
	 */
	public int insertarMotivoCitaPacOdontologia(Connection con,DtoMotivoCitaPaciente motivoCitaPaciente);

	/**
	 * Metodo que realiza la modificacion a un motivo de cita de un paciente
	 * @param con
	 * @param motivoCitaPaciente
	 * @return
	 */
	public int modificarMotivoCitaPacOdontologia(Connection con,DtoMotivoCitaPaciente motivoCitaPaciente);

	/**
	 * Metodo que realiza la consulta de un Motivo de Cita de un Paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public DtoMotivoCitaPaciente consultarMotivoCitaPacOdontologia(Connection con, int codigoPaciente);

	
	/**
	 * Metodo implementado para insertar un Beneficiario..asociado a un paciente
	 * @param con
	 * @param beneficiario
	 * @param loginUsuario
	 * @return
	 */
	public int insertarBeneficiarioPaciente(Connection con,	DtoBeneficiarioPaciente beneficiario, String loginUsuario, int codInstitucion);

	/**
	 * Metodo para modificar un beneficioario del paciente
	 * @param con
	 * @param beneficiario
	 * @param loginUsuario
	 * @param codInstitucion
	 * @return
	 */
	public int modificarBeneficiarioPaciente(Connection con,DtoBeneficiarioPaciente beneficiario, String loginUsuario,int codInstitucion);

	
	/**
	 * Metodo para consultar Beneficiarios del Pacienete odontologia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ArrayList<DtoBeneficiarioPaciente> consultarBeneficiariosPaciente(Connection con, int codigo);
	
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
	public boolean actualizarCentroAtencionDuenioPaciente(Connection con, int codigoPaciente, int centroAtencionDuenio);

	/**
	 * @param con
	 * @param codigoPaciente
	 * @return fecha hora de ingreso
	 */
	public  String consultarFechaHoraIngreso(Connection con,String codigoPaciente);
	
	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @return consulta el centro de atencion del paciente 
	 */
	public  String consultarCentroAtencionPaciente(Connection con,String codigoPaciente);
}