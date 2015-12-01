package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public interface ContratoCargueDao
{
	/**
	 * Método que inserta un nuevo contrato_cargue
	 * @param con
	 * @param codigoContrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param totalPacientes
	 * @param valorTotal
	 * @param upc
	 * @param fechaFinalModificada 
	 * @param cuentaCobro, inserta null si es ""
	 * @throws SQLException
	 */
	public int insertarContratoCargue(
			Connection con, int codigoContrato, String fechaCargue, String fechaInicial, String fechaFinal, int totalPacientes, double valorTotal, double upc, String cuentaCobro, int codigoInstitucion, String fechaFinalModificada) throws SQLException;
	
	/**
	 * Método para modificar los datos modificables de un contrato_cargue existente
	 * @param con
	 * @param codigoContratoCargue
	 * @param totalPacientes
	 * @param upc
	 * @param fechaFinalModificada 
	 * @throws SQLException
	 */
	public void modificarContratoCargue(Connection con, int codigoContratoCargue, int totalPacientes, double upc, double valorTotal, String fechaFinalModificada) throws SQLException;

	/**
	 * Método para anular un contrato_cargue
	 * @param con
	 * @param codigoContratoCargue
	 * @throws SQLException
	 */
	public void anularContratoCargue(Connection con, int codigoContratoCargue) throws SQLException;
	
	/**
	 * Método que informa si un contrato cargue se encuentra anulado
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws SQLException
	 */
	public boolean estaAnuladoContratoCargue(Connection con, int codigoContratoCargue) throws SQLException;
	
	/**
	 * Verifica si existen cargues para el convenio en el periodo indicado
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 * @throws SQLException
	 */
	public boolean existenContratosCargueConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException;

	/**
	 * Verifica si existen cuentas de cobro para los cargues del convenio y el periodo especificado
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoContrato
	 * @return
	 * @throws SQLException
	 */
	public boolean existenCuentasCobroConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException;

	/**
	 * Método para buscar contratos_cargue
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 * @throws SQLException
	 */
	public HashMap buscarContratosCargue(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException;

	/**
	 * Busca los logs de subir paciente según los parametros especificados
	 * @param con
	 * @param convenio que correspondan al convenio especificado
	 * @param fechaInicial cuya fecha de cargue sea mayor o igual a esta fecha inicial
	 * @param fechaFinal cuya fecha de cargue sea menor o igual a esta fecha final
	 * @param usuario que corresponda al usuario especificado
	 * @return
	 * @throws SQLException
	 */
	public Collection buscarLogsSubirPacientes(Connection con, String convenio, String fechaInicial, String fechaFinal, String usuario) throws SQLException;

	/**
	 * Método que actualiza el upc y el total a pagar=upc*total_usuarios de los cargues_grupo_etareo correspondientes al cargue indicado
	 * @param con
	 * @param codigoContratoCargue
	 * @param upc
	 * @throws SQLException
	 */
	public void actualizarTotalAPagarCarguesGrupoEtareo(Connection con, int codigoContratoCargue, double upc) throws SQLException;

	/**
	 * Calcula sumatoria de total_a_pagar de los cargues grupo etareo del contrato indicado
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws SQLException
	 */
	public double calcularSumatoriaTotalAPagarCarguesGrupoEtareo(Connection con, int codigoContratoCargue) throws SQLException;
	
	/**
	 * Consulta los cargue_grupo_etareo correspondientes al contrato_cargue indicado
	 * @param con
	 * @param codigoContratoCargue
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCarguesGrupoEtareoContrato(Connection con, int codigoContratoCargue) throws SQLException;

	/**
	 * Inserta un nuevo cargue_grupo_etareo 
	 * @param con
	 * @param codigoContratoCargue
	 * @param codigoGrupoEtareoConvenio
	 * @param totalUsuarios
	 * @param upc
	 * @param totalPagar
	 * @throws SQLException
	 */
	public void insertarCargueGrupoEtareo(Connection con, int codigoContratoCargue, int codigoGrupoEtareoConvenio, 
			int totalUsuarios, double upc, double totalPagar) throws SQLException;

	/**
	 * Actualiza los valores del cargue_grupo_etareo indicado
	 * @param con
	 * @param codigoCargueGrupoEtareo
	 * @param totalUsuarios
	 * @param upc
	 * @param totalPagar
	 * @throws SQLException
	 */
	public void actualizarCargueGrupoEtareo(Connection con, int codigoCargueGrupoEtareo, int totalUsuarios, double upc, double totalPagar) throws SQLException;

	/**
	 * Elimina un cargue_grupo_etareo especificado
	 * @param con
	 * @param codigoCargueGrupoEtareo
	 * @throws SQLException
	 */
	public void eliminarCargueGrupoEtareo(Connection con, int codigoCargueGrupoEtareo) throws SQLException;

	/**
	 * Permite consultar los usuarios cargados en un periodo específico para un contrato dado y con determinados parametros de
	 * busqueda como el tipo de identificacion, numero de identificacion, nombres y apellidos
	 * @param con
	 * @param codigoContrato
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoId
	 * @param numeroId
	 * @param nombre
	 * @param apellido
	 * @param numeroFicha 
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarUsuariosCargados(Connection con, int codigoContrato, String fechaInicial, String fechaFinal, String tipoId, String numeroId, String nombre, String apellido, String numeroFicha) throws SQLException;

	
	/**
	 * 
	 * @param con
	 * @param contratosCargueEliminados
	 */
	public boolean eliminarContratos(Connection con, HashMap contratosCargueEliminados);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param activo
	 * @return
	 */
	public boolean inactivarUsuarios(Connection con, String consecutivo, String activo);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param activo
	 * @return
	 */
	public boolean inactivarUsuariosActivos(Connection con, String consecutivo, String activo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarUsuarios(Connection con, String consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap consultarDatosEliminado(Connection con, String consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarLogEliminacion(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap consultarDaotosInactivar(Connection con, String consecutivo);

	
}
