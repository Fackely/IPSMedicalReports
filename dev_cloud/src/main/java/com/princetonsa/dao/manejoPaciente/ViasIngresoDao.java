package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

public interface ViasIngresoDao {
	
	/**
	 * Insertar
	 */
	public abstract boolean insertar(Connection con, HashMap vo);
	
	/**
	 * consultar 
	 */
	public abstract HashMap consultarViasIngresoExistentes(Connection con, HashMap vo);

	/**
	 * Modificar
	 * */
	public abstract boolean modificar(Connection con,HashMap vo);
	
	/**
	 * Eliminar
	 * */
	public abstract boolean eliminarRegistro(Connection con, int codigo);
	
	/**
	 * Insertar Garantia Paciente
	 */
	 public abstract boolean insertarGarantiaPaciente(Connection con, HashMap vo);
	
	/**
	 * Consultar Garantia Paciente
	 * */
	public abstract HashMap consultarGarantiaPaciente(Connection con, int codigo);
	
	/**
	 * Consultar Número de tipo paciente por vía de ingreso
	 * */
	public abstract int consultarNoTipoPacViaIng(Connection con, int codigo);
	
	/**
	 * Modificar Garantia Paciente
	 * */
	public abstract boolean modificarGarantiaPaciente(Connection con, HashMap vo); 
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionDerechosViaIngreso( Connection con, int codigoViaIngreso);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionCierreNotasEnferViaIngreso( Connection con, int codigoViaIngreso);
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionEpicrisisFinalizadaViaIngreso( Connection con, int codigoViaIngreso);

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @return
	 */
	public abstract int obtenerConvenioDefecto(Connection con, int viaIngreso);

}
