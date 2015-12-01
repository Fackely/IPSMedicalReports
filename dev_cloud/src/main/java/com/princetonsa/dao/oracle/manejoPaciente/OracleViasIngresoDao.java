package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ViasIngresoDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseViasIngresoDao;

public class OracleViasIngresoDao implements ViasIngresoDao {
	
	/**
	 * Insertar
	 * */
	public boolean insertar(Connection con, HashMap vo)
	{
		return SqlBaseViasIngresoDao.insertar(con, vo);
	}
	
	/**
	 * Consultar vias de ingreso existente
	 * */
	public HashMap consultarViasIngresoExistentes(Connection con, HashMap vo) 
	{
		return SqlBaseViasIngresoDao.consultarViasIngresoExistentes(con,vo);
	}
	
	/**
	 * Modificar 
	 */
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBaseViasIngresoDao.modificar(con, vo);
	}
	
	/**
	 * Eliminar 
	 */
	public boolean eliminarRegistro(Connection con, int codigo)
	{
		return SqlBaseViasIngresoDao.eliminarRegistro(con,codigo);
	}
	
	/**
	 * Insertar garantia paciente
	 * */
	public boolean insertarGarantiaPaciente(Connection con, HashMap vo)
	{
		return SqlBaseViasIngresoDao.insertarGarantiaPaciente(con, vo);
	}
	
	/**
	 * Consultar garantia paciente
	 * */
	public HashMap consultarGarantiaPaciente(Connection con, int codigo)
	{
		return SqlBaseViasIngresoDao.consultarGarantiaPaciente(con,codigo);
	}
	
	/**
	 * Consultar Número de tipo paciente por vía de ingreso
	 * */
	public int consultarNoTipoPacViaIng(Connection con, int codigo)
	{
		return SqlBaseViasIngresoDao.consultarNoTipoPacViaIng(con,codigo);
	}
	
	/**
	 * Modificar garantia paciente 
	 */
	public boolean modificarGarantiaPaciente(Connection con, HashMap vo)
	{
		return SqlBaseViasIngresoDao.modificarGarantiaPaciente(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionDerechosViaIngreso( Connection con, int codigoViaIngreso)
	{
		return SqlBaseViasIngresoDao.existeVerificacionDerechosViaIngreso(con, codigoViaIngreso);
	}

	/**
	 * 
	 */
	public int obtenerConvenioDefecto(Connection con, int viaIngreso) 
	{
		return SqlBaseViasIngresoDao.obtenerConvenioDefecto(con, viaIngreso);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionCierreNotasEnferViaIngreso( Connection con, int codigoViaIngreso)
	{
		return SqlBaseViasIngresoDao.existeVerificacionCierreNotasEnferViaIngreso(con, codigoViaIngreso);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionEpicrisisFinalizadaViaIngreso( Connection con, int codigoViaIngreso)
	{
		return SqlBaseViasIngresoDao.existeVerificacionEpicrisisFinalizadaViaIngreso(con, codigoViaIngreso);		
	}
}