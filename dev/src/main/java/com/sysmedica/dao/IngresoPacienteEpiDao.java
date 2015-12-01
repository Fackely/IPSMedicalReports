package com.sysmedica.dao;

import java.sql.ResultSet;
import java.sql.Connection;

public interface IngresoPacienteEpiDao {

	public ResultSet consultaPaciente(Connection con, String identificacion, String tipoIdentificacion);
	
	public int ingresarPaciente(Connection con,
								String primerApellido,
								String segundoApellido,
								String primerNombre,
								String segundoNombre,
								String fechaNacimiento,
								int genero,
								String municipioResidencia,
								String departamentoResidencia,
								String lugarResidencia,
								int codigoBarrioResidencia,
								String municipioNacimiento,
								String departamentoNacimiento,
								String lugarNacimiento,
								String estadoCivil,
								boolean estaVivo,
								String direccion,
								String zonaDomicilio,
								String telefono,
								int ocupacion,
								String tipoRegimen,
								int aseguradora,
								int etnia,
								String numeroIdentificacion,
								String tipoIdentificacion,
								String municipioIdentifica,
								String departamentoIdentifica,
								String lugarIdentifica,
								int tipoSangre,
								String grupoPoblacional,
								int codigoInstitucion,
								String paisExpedicion,
								String paisNacimiento,
								String paisResidencia);
	
	
	public int modificarPaciente(Connection con,
									int codigoPaciente,
									String primerApellido,
									String segundoApellido,
									String primerNombre,
									String segundoNombre,
									String fechaNacimiento,
									int genero,
									String municipioResidencia,
									String departamentoResidencia,
									String lugarResidencia,
									int codigoBarrioResidencia,
									String municipioNacimiento,
									String departamentoNacimiento,
									String lugarNacimiento,
									String estadoCivil,
									boolean estaVivo,
									String direccion,
									String zonaDomicilio,
									String telefono,
									int ocupacion,
									String tipoRegimen,
									int aseguradora,
									int etnia,
									String numeroIdentificacion,
									String tipoIdentificacion,
									String nuevoNumeroIdentificacion,
									String nuevoTipoIdentificacion,
									String municipioIdentifica,
									String departamentoIdentifica,
									String lugarIdentifica,
									int tipoSangre,
									String grupoPoblacional,
									String paisExpedicion,
									String paisNacimiento,
									String paisResidencia);
	
	
	public ResultSet consultarConvenioPaciente(Connection con, int codigoPaciente);
}
