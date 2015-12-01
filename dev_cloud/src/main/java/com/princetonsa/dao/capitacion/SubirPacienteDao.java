/**
 * Juan David Ramírez 13/06/2006
 * Princeton S.A.
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.servinte.axioma.dto.capitacion.DtoUsuariosXConvenio;

/**
 * @author Juan David Ramírez
 *
 */
public interface SubirPacienteDao
{
	/**
	 * Métodos para consultar el listado de los tipos para los selects
	 * @param con
	 * @param institucion @todo
	 * @param codigoConvenio @todo
	 * @return
	 */
	public Collection consultarTipos(Connection con, int tipoConsulta, int institucion, int codigoConvenio);

	/**
	 * Metodo para ingresar un usuario por convenio
	 * @param con
	 * @param contrato
	 * @param codigoPersona
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoCargue
	 * @param usuario
	 * @param tipoAfiliado 
	 * @return
	 */
	public int ingresarUsuarioXConvenio(Connection con, DtoUsuariosXConvenio datosCapitacion);

	/**
	 * Método para ingresar los suarios capitados
	 * y el convenios para los mismos
	 * @param con
	 * @param contrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param numeroIdentificacion
	 * @param tipoIdentificacion
	 * @param fechaNacimiento
	 * @param sexo
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param direccion
	 * @param telefono
	 * @param email
	 * @param numeroFicha 
	 * @param clasificacionSE 
	 * @param convenio 
	 * @param tipoCargue @todo
	 * @param codigoPersona
	 * @return
	 */
	public int insertarUsuarioCapitado(Connection con, String numeroIdentificacion, String tipoIdentificacion, String fechaNacimiento, int sexo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String direccion, String telefono, String email, String numeroFicha,
			String tipoIdEmpleador,String numIdEmpleador,String razonSociEmpleador,String tipoIdCotizante,String numIdCotizante,String nombresCotizante,String apellidosCotizante,int parentesco,int centroAtencion,String tipoAfiliado,String excepcionMonto ,String pais,String departamento,
			String municipio,String localidad,String barrio);

	/**
	 * Ingresa la información del usuario capitado convenio
	 * @param con
	 * @param contrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codUsuarioCapitado
	 * @param tipoCargue
	 * @param codigoTipoAfiliado 
	 * @return
	 */
	public int ingresarConvUsuarioCapitado(Connection con, int contrato, String fechaInicial, String fechaFinal, int codUsuarioCapitado, int tipoCargue, String convenio, String clasificacionSE, String codigoTipoAfiliado);
	
	/**
	 * Metodo para ingresar el en la tabla capitacion.contrato_cargue
	 * @param con
	 * @param contrato
	 * @param totalPacientes
	 * @param upc
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoPago
	 * @return
	 */
	public int ingresarContratoCargue(Connection con, int contrato, int totalPacientes, double upc, String fechaInicial, String fechaFinal, int tipoPago, double valorTotal) throws SQLException;

	/**
	 * Método para ingresar cargue grupo etareo
	 * @param con
	 * @param contratoCargue
	 * @param grupoIngresado
	 * @param totalPacientes
	 * @param upc
	 * @return
	 */
	public int ingresarGrupoEtareoCargue(Connection con, int contratoCargue, int grupoIngresado, int totalPacientes, double upc);

	/**
	 * Método para ingresar el log tipo BD del cargue
	 * @param con
	 * @param contrato
	 * @param registrosLeidos
	 * @param registrosGuardados
	 * @param loginUsuario
	 */
	public boolean ingresarLogBD(Connection con, int contrato, int registrosLeidos, int registrosGuardados, String loginUsuario, String archivoInconsistencias);

	/**
	 * Método para consultar la existencia de usuarios capitados
	 * @param con
	 * @param codigoPersona
	 * @param fechaInicial @todo
	 * @param fechaFinal @todo
	 * @param contrato 
	 * @param convenio 
	 * @return
	 */
	public boolean existeUsuarioXConvenio(Connection con, int codigoPersona, String fechaInicial, String fechaFinal, int convenio, int contrato);
	
	/**
	 * Consulta si un usuario ya tiene cargue en el periodo indicado
	 * @param con
	 * @param codigoUsuarioCapitado
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param contrato 
	 * @param convenio 
	 * @return
	 */
	public boolean existeUsuarioCapitadoPeriodo(Connection con, int codigoUsuarioCapitado, String fechaInicial, String fechaFinal, int convenio, int contrato);
	
	public int consultarCodigoContratoCarguePeriodo(Connection con, int contrato, String fechaInicial, String fechaFinal);

	public int aumentarPacientesContratoCargue(Connection con, int codigo, int totalPacientes, double valorTotal) throws SQLException;

	/**
	 * Consulta si existe el usuario capitado y retorna su codigo o -1
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public int consultarCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion);
	
	/**
	 * Consulta la información de un usuario capitado
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public HashMap consultarUsuarioCapitado(Connection con, String tipoIdentificacion, String numeroIdentificacion);
	
	/**
	 * Método que consulta un usuario capitado por tipo y número identificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarUsuarioCapitado(Connection con,HashMap campos);
	
	/**
	 * Método que elimina un usuario capitado 
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarUsuarioCapitado(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar los datos del usuario x convenio
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap consultarUsuarioXConvenio(Connection con,String codigoPaciente);

	/**
	 * Metodo para consultar los grupos_etareos_x_convenio relativos al cargue a realizar con fechaInicial y fechaFinal indicadas
	 * @param con
	 * @param institucion
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public Collection consultarGruposEtareos(Connection con, int institucion, int codigoConvenio, String fechaInicial, String fechaFinal);

	/**
	 * Metodo para actualizar el cargue grupo etareo
	 * @param con
	 * @param codigo
	 * @param totalPacientes
	 * @param upc
	 * @return
	 */
	public int modificarGrupoEtareoCargue(Connection con, int codigo, int totalPacientes, double upc);

	/**
	 * Consulta el codigo de un cargue grupo_etareo especificado el contrato y el grupo_etareo
	 * @param con
	 * @param codigoContratoCargue
	 * @param codigoGrupoEtareo
	 * @return
	 */
	public int consultarCodigoCargueGrupoEtareo(Connection con, int codigoContratoCargue, int codigoGrupoEtareo);

	/**
	 * 
	 * @param con
	 * @param mes
	 * @param anio
	 * @return
	 */
	public double obtenerValorUnidadPagoCapitacionMesAnio(Connection con, int mes, int anio);

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public boolean existeConvenioCapitado(Connection con, String convenio);

	/**
	 * 
	 * @param con
	 * @param clasificacionSE
	 * @param codigoConvenio 
	 * @return
	 */
	public boolean existeClasificacionSocioEconomica(Connection con, String clasificacionSE, int codigoConvenio);

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public int codigoEmpresaConvenio(Connection con, int convenio);

	/**
	 * 
	 * @param con
	 * @param tipoID
	 * @param numeroID
	 * @return
	 */
	public int obtenerCodigoPersona(Connection con, String tipoID,String numeroID);
}
