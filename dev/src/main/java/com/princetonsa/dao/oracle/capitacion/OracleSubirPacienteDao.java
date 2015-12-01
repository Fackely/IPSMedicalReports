/**
 * Juan David Ramírez 13/06/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.SubirPacienteDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseSubirPacienteDao;
import com.servinte.axioma.dto.capitacion.DtoUsuariosXConvenio;

/**
 * @author Juan David Ramírez
 *
 */
public class OracleSubirPacienteDao implements SubirPacienteDao
{
	/**
	 * Métodos para consultar el listado de los tipos para los selects
	 * @param con
	 * @return
	 */
	public Collection consultarTipos(Connection con, int tipoConsulta, int institucion, int codigoConvenio)
	{
		return SqlBaseSubirPacienteDao.consultarTipos(con, tipoConsulta, institucion, codigoConvenio);
	}

	/**
	 * Metodo para ingresar un usuario por convenio
	 * @param con
	 * @param contrato
	 * @param codigoPersona
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoCargue
	 * @param usuario
	 * @return
	 */
	public int ingresarUsuarioXConvenio(Connection con, DtoUsuariosXConvenio datosCapitacion)
	{
		return SqlBaseSubirPacienteDao.ingresarUsuarioXConvenio(con, datosCapitacion);
	}

	/**
	 * Metodo para ingresar el en la tabla capitacion.contrato_cargue
	 * @param con
	 * @param contrato
	 * @param totalPacientes
	 * @param upc
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public int ingresarContratoCargue(Connection con, int contrato, int totalPacientes, double upc, String fechaInicial, String fechaFinal, int tipoPago, double valorTotal) throws SQLException
	{
		return SqlBaseSubirPacienteDao.ingresarContratoCargue(con, contrato, totalPacientes, upc, fechaInicial, fechaFinal, tipoPago, valorTotal);
	}

	/**
	 * Método para ingresar cargue grupo etareo
	 * @param con
	 * @param contratoCargue
	 * @param grupoEtareo
	 * @param totalPacientes
	 * @param upc
	 * @return
	 */
	public int ingresarGrupoEtareoCargue(Connection con, int contratoCargue, int grupoEtareo, int totalPacientes, double upc)
	{
		return SqlBaseSubirPacienteDao.ingresarGrupoEtareoCargue(con, contratoCargue, grupoEtareo, totalPacientes, upc);
	}
	
	/**
	 * Método para ingresar el log tipo BD del cargue
	 * @param con
	 * @param contrato
	 * @param registrosLeidos
	 * @param registrosGuardados
	 * @param loginUsuario
	 */
	public boolean ingresarLogBD(Connection con, int contrato, int registrosLeidos, int registrosGuardados, String loginUsuario, String archivoInconsistencias)
	{
		return SqlBaseSubirPacienteDao.ingresarLogBD(con, contrato, registrosLeidos, registrosGuardados, loginUsuario, archivoInconsistencias);
	}
	
	/**
	 * Método para consultar la existencia de usuarios capitados
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public boolean existeUsuarioXConvenio(Connection con, int codigoPersona, String fechaInicial, String fechaFinal, int convenio, int contrato)
	{
		return SqlBaseSubirPacienteDao.existeUsuarioXConvenio(con, codigoPersona, fechaInicial, fechaFinal,convenio,contrato);
	}
	
	/**
	 * Método que consulta un usuario capitado por tipo y número identificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarUsuarioCapitado(Connection con,HashMap campos)
	{
		return SqlBaseSubirPacienteDao.consultarUsuarioCapitado(con,campos);
	}
	
	/**
	 * Método que elimina un usuario capitado 
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarUsuarioCapitado(Connection con,HashMap campos)
	{
		return SqlBaseSubirPacienteDao.eliminarUsuarioCapitado(con,campos);
	}
	
	/**
	 * Método implementado para consultar los datos del usuario x convenio
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultarUsuarioXConvenio(Connection con,String codigoPaciente)
	{
		return SqlBaseSubirPacienteDao.consultarUsuarioXConvenio(con,codigoPaciente);
	}

	public Collection consultarGruposEtareos(Connection con, int institucion, int codigoConvenio, String fechaInicial, String fechaFinal)
	{
		return SqlBaseSubirPacienteDao.consultarGruposEtareos(con, institucion, codigoConvenio, fechaInicial, fechaFinal);
	}

	public boolean existeUsuarioCapitadoPeriodo(Connection con, int codigoUsuarioCapitado, String fechaInicial, String fechaFinal, int convenio, int contrato) 
	{
		return SqlBaseSubirPacienteDao.existeUsuarioCapitadoPeriodo(con, codigoUsuarioCapitado, fechaInicial, fechaFinal,convenio,contrato);
	}

	public int insertarUsuarioCapitado(Connection con, String numeroIdentificacion, String tipoIdentificacion, String fechaNacimiento, int sexo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String direccion, String telefono, String email, String numeroFicha,
			String tipoIdEmpleador,String numIdEmpleador,String razonSociEmpleador,String tipoIdCotizante,String numIdCotizante,String nombresCotizante,String apellidosCotizante,int parentesco,int centroAtencion,String tipoAfiliado,String excepcionMonto ,	String pais,String departamento ,String municipio,
			String localidad,String barrio)
	{
		return SqlBaseSubirPacienteDao.insertarUsuarioCapitado(con, numeroIdentificacion, tipoIdentificacion, fechaNacimiento, sexo, primerNombre, segundoNombre, primerApellido, segundoApellido, direccion, telefono, email,numeroFicha,
				tipoIdEmpleador,numIdEmpleador,razonSociEmpleador,tipoIdCotizante,numIdCotizante,nombresCotizante,apellidosCotizante,parentesco,centroAtencion,tipoAfiliado,excepcionMonto ,pais,departamento ,	municipio,localidad,barrio);
	}

	public int ingresarConvUsuarioCapitado(Connection con, int contrato, String fechaInicial, String fechaFinal, int codUsuarioCapitado, int tipoCargue, String convenio, String clasificacionSE, String codigoTipoAfiliado)
	{
		return SqlBaseSubirPacienteDao.ingresarConvUsuarioCapitado(con, contrato, fechaInicial, fechaFinal, codUsuarioCapitado, tipoCargue, convenio, clasificacionSE,codigoTipoAfiliado);
	}

	public int consultarCodigoContratoCarguePeriodo(Connection con, int contrato, String fechaInicial, String fechaFinal)
	{
		return SqlBaseSubirPacienteDao.consultarCodigoContratoCarguePeriodo(con, contrato, fechaInicial, fechaFinal);
	}

	public int aumentarPacientesContratoCargue(Connection con, int codigo, int totalPacientes, double valorTotal) throws SQLException
	{
		return SqlBaseSubirPacienteDao.aumentarPacientesContratoCargue(con, codigo, totalPacientes, valorTotal);
	}

	public int consultarCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{
		return SqlBaseSubirPacienteDao.consultarCodigoUsuarioCapitado(con, tipoIdentificacion, numeroIdentificacion);
	}

	public HashMap consultarUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{
		return SqlBaseSubirPacienteDao.consultarUsuarioCapitado(con, tipoIdentificacion, numeroIdentificacion);
	}

	public int modificarGrupoEtareoCargue(Connection con, int codigo, int totalPacientes, double upc)
	{
		return SqlBaseSubirPacienteDao.modificarGrupoEtareoCargue(con, codigo, totalPacientes, upc);
	}

	public int consultarCodigoCargueGrupoEtareo(Connection con, int codigoContratoCargue, int codigoGrupoEtareo)
	{
		return SqlBaseSubirPacienteDao.consultarCodigoCargueGrupoEtareo(con, codigoContratoCargue, codigoGrupoEtareo);
	}

	/**
	 * 
	 */
	public double obtenerValorUnidadPagoCapitacionMesAnio(Connection con, int mes, int anio)
	{
		return SqlBaseSubirPacienteDao.obtenerValorUnidadPagoCapitacionMesAnio(con, mes, anio);
	}
	

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public boolean existeConvenioCapitado(Connection con, String convenio)
	{
		return SqlBaseSubirPacienteDao.existeConvenioCapitado(con,convenio);
	}

	/**
	 * 
	 * @param con
	 * @param clasificacionSE
	 * @return
	 */
	public boolean existeClasificacionSocioEconomica(Connection con, String clasificacionSE, int codigoConvenio)
	{
		return SqlBaseSubirPacienteDao.existeClasificacionSocioEconomica(con, clasificacionSE,codigoConvenio);
	}
	
	/**
	 * 
	 */
	public int codigoEmpresaConvenio(Connection con, int convenio) 
	{
		return SqlBaseSubirPacienteDao.codigoEmpresaConvenio(con, convenio);
	}
	
	@Override
	public int obtenerCodigoPersona(Connection con, String tipoID,String numeroID) 
	{
		return SqlBaseSubirPacienteDao.obtenerCodigoPersona(con, tipoID,numeroID);
	}
}
