package com.princetonsa.dao.postgresql.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.ContratoCargueDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseContratoCargueDao;

public class PostgresqlContratoCargueDao implements ContratoCargueDao
{
	
	/**
	 * 
	 */
	public int insertarContratoCargue(Connection con, int codigoContrato,
			String fechaCargue, String fechaInicial, String fechaFinal, int totalPacientes,
			double valorTotal, double upc, String cuentaCobro, int codigoInstitucion,String fechaFinalModificada) throws SQLException
	{
		return SqlBaseContratoCargueDao.insertarContratoCargue(con, codigoContrato, fechaCargue, fechaInicial, fechaFinal, totalPacientes, valorTotal, upc, cuentaCobro, codigoInstitucion,fechaFinalModificada);
	}
	
	/**
	 * 
	 */
	public void modificarContratoCargue(Connection con,
			int codigoContratoCargue, int totalPacientes, double upc, double valorTotal,String fechaFinalModificada)
			throws SQLException
	{
		SqlBaseContratoCargueDao.modificarContratoCargue(con, codigoContratoCargue, totalPacientes, upc, valorTotal,fechaFinalModificada);
	}
	
	/**
	 * 
	 */
	public void anularContratoCargue(Connection con, int codigoContratoCargue)
			throws SQLException
	{
		SqlBaseContratoCargueDao.anularContratoCargue(con, codigoContratoCargue);
	}

	/**
	 * 
	 */
	public boolean estaAnuladoContratoCargue(Connection con, int codigoContratoCargue) throws SQLException
	{
		return SqlBaseContratoCargueDao.estaAnuladoContratoCargue(con, codigoContratoCargue);
	}

	/**
	 * 
	 */
	public boolean existenContratosCargueConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException
	{
		return SqlBaseContratoCargueDao.existenContratosCargueConvenioPeriodo(con, fechaInicial, fechaFinal, codigoConvenio);
	}
	
	/**
	 * 
	 */
	public HashMap buscarContratosCargue(Connection con, String fechaInicial,
			String fechaFinal, int codigoConvenio) throws SQLException
	{
		return SqlBaseContratoCargueDao.buscarContratosCargue(con, fechaInicial, fechaFinal, codigoConvenio);
	}

	/**
	 * 
	 */
	public Collection buscarLogsSubirPacientes(Connection con, String convenio, String fechaInicial, String fechaFinal, String usuario) throws SQLException
	{
		return SqlBaseContratoCargueDao.buscarLogsSubirPacientes(con, convenio, fechaInicial, fechaFinal, usuario);
	}
	
	/**
	 * 
	 */
	public void actualizarTotalAPagarCarguesGrupoEtareo(Connection con, int codigoContratoCargue, double upc) throws SQLException
	{
		SqlBaseContratoCargueDao.actualizarTotalAPagarCarguesGrupoEtareo(con, codigoContratoCargue, upc);
	}

	/**
	 * 
	 */
	public double calcularSumatoriaTotalAPagarCarguesGrupoEtareo(Connection con, int codigoContratoCargue) throws SQLException
	{
		return SqlBaseContratoCargueDao.calcularSumatoriaTotalAPagarCarguesGrupoEtareo(con, codigoContratoCargue);
	}
	
	/**
	 * 
	 */
	public HashMap consultarCarguesGrupoEtareoContrato(Connection con, int codigoContratoCargue) throws SQLException
	{
		return SqlBaseContratoCargueDao.consultarCarguesGrupoEtareoContrato(con, codigoContratoCargue);
	}

	/**
	 * 
	 */
	public void actualizarCargueGrupoEtareo(Connection con, int codigoCargueGrupoEtareo, int totalUsuarios, double upc, double totalPagar) throws SQLException
	{
		SqlBaseContratoCargueDao.actualizarCargueGrupoEtareo(con, codigoCargueGrupoEtareo, totalUsuarios, upc, totalPagar);
	}

	/**
	 * 
	 */
	public void insertarCargueGrupoEtareo(Connection con, int codigoContratoCargue, int codigoGrupoEtareoConvenio, int totalUsuarios, double upc, double totalPagar) throws SQLException
	{
		SqlBaseContratoCargueDao.insertarCargueGrupoEtareo(con, codigoContratoCargue, codigoGrupoEtareoConvenio, totalUsuarios, upc, totalPagar);
	}

	/**
	 * 
	 */
	public void eliminarCargueGrupoEtareo(Connection con, int codigoCargueGrupoEtareo) throws SQLException
	{
		SqlBaseContratoCargueDao.eliminarCargueGrupoEtareo(con, codigoCargueGrupoEtareo);
	}

	/**
	 * 
	 */
	public boolean existenCuentasCobroConvenioPeriodo(Connection con, String fechaInicial, String fechaFinal, int codigoConvenio) throws SQLException
	{
		return SqlBaseContratoCargueDao.existenCuentasCobroConvenioPeriodo(con, fechaInicial, fechaFinal, codigoConvenio);
	}

	/**
	 * 
	 */
	public HashMap consultarUsuariosCargados(Connection con, int codigoContrato, String fechaInicial, String fechaFinal, String tipoId, String numeroId, String nombre, String apellido, String numeroFicha) throws SQLException
	{
		return SqlBaseContratoCargueDao.consultarUsuariosCargados(con, codigoContrato, fechaInicial, fechaFinal, tipoId, numeroId, nombre, apellido,numeroFicha);
	}
	
	/**
	 * 
	 * @param con
	 * @param contratosCargueEliminados
	 */
	public boolean eliminarContratos(Connection con, HashMap contratosCargueEliminados)
	{
		return SqlBaseContratoCargueDao.eliminarContratos(con, contratosCargueEliminados);
	}

	/**
	 * 
	 */
	public boolean inactivarUsuarios(Connection con, String consecutivo, String activo) 
	{
		return SqlBaseContratoCargueDao.inactivarUsuarios(con, consecutivo, activo);
	}

	/**
	 * 
	 */
	public boolean inactivarUsuariosActivos(Connection con, String consecutivo, String activo) 
	{
		return SqlBaseContratoCargueDao.inactivarUsuariosActivos(con, consecutivo, activo);
	}

	/**
	 * 
	 */
	public boolean eliminarUsuarios(Connection con, String consecutivo) 
	{
		return SqlBaseContratoCargueDao.eliminarUsuarios(con, consecutivo);
	}

	/**
	 * 
	 */
	public HashMap consultarDatosEliminado(Connection con, String consecutivo) 
	{
		return SqlBaseContratoCargueDao.consultarDatosEliminado(con, consecutivo);
	}

	/**
	 * 
	 */
	public boolean insertarLogEliminacion(Connection con, HashMap vo) 
	{
		return SqlBaseContratoCargueDao.insertarLogEliminacion(con, vo);
	}

	/**
	 * 
	 */
	public HashMap consultarDaotosInactivar(Connection con, String consecutivo) 
	{
		return SqlBaseContratoCargueDao.consultarDaotosInactivar(con, consecutivo);
	}
	
	
}