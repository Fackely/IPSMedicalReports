package com.sysmedica.dao.postgresql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.sysmedica.dao.IngresoPacienteEpiDao;
import com.sysmedica.dao.sqlbase.SqlBaseIngresoPacienteEpiDao;

public class PostgresqlIngresoPacienteEpiDao implements IngresoPacienteEpiDao {

	private String secuenciaStr = "SELECT nextval('seq_personas')";
	
	public ResultSet consultaPaciente(Connection con, String identificacion, String tipoIdentificacion)
	{
		return SqlBaseIngresoPacienteEpiDao.consultarPaciente(con,identificacion,tipoIdentificacion);
	}
	
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
									String paisResidencia)
	{
		return SqlBaseIngresoPacienteEpiDao.ingresarPaciente(con,
																secuenciaStr,
																primerApellido,
																segundoApellido,
																primerNombre,
																segundoNombre,
																fechaNacimiento,
																genero,
																municipioResidencia,
																departamentoResidencia,
																lugarResidencia,
																codigoBarrioResidencia,
																municipioNacimiento,
																departamentoNacimiento,
																lugarNacimiento,
																estadoCivil,
																estaVivo,
																direccion,
																zonaDomicilio,
																telefono,
																ocupacion,
																tipoRegimen,
																aseguradora,
																etnia,
																numeroIdentificacion,
																tipoIdentificacion,
																municipioIdentifica,
																departamentoIdentifica,
																lugarIdentifica,
																tipoSangre,
																grupoPoblacional,
																codigoInstitucion,
																paisExpedicion,
																paisNacimiento,
																paisResidencia);
	}
	
	
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
									String paisResidencia
									)
	{
		return SqlBaseIngresoPacienteEpiDao.modificarPaciente(con,
						    									codigoPaciente,
																primerApellido,
																segundoApellido,
																primerNombre,
																segundoNombre,
																fechaNacimiento,
																genero,
																municipioResidencia,
																departamentoResidencia,
																lugarResidencia,
																codigoBarrioResidencia,
																municipioNacimiento,
																departamentoNacimiento,
																lugarNacimiento,
																estadoCivil,
																estaVivo,
																direccion,
																zonaDomicilio,
																telefono,
																ocupacion,
																tipoRegimen,
																aseguradora,
																etnia,
																numeroIdentificacion,
																tipoIdentificacion,
																nuevoNumeroIdentificacion,
																nuevoTipoIdentificacion,
																municipioIdentifica,
																departamentoIdentifica,
																lugarIdentifica,
																tipoSangre,
																grupoPoblacional,
																paisExpedicion,
																paisNacimiento,
																paisResidencia);
	}
	
	
	public ResultSet consultarConvenioPaciente(Connection con, int codigoPaciente)
	{
		return SqlBaseIngresoPacienteEpiDao.consultarConvenioPaciente(con,codigoPaciente);
	}
}
