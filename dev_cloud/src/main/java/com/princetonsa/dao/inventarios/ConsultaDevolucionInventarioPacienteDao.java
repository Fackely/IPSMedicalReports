package com.princetonsa.dao.inventarios;

import java.util.HashMap;

import java.sql.Connection;

public interface ConsultaDevolucionInventarioPacienteDao
{
	
	/**
	 * Metodo de consulta de los centros de costo
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaCentroCosto (Connection con);
	
	/**
	 * Metodo de consulta de los estados Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaEstadosDevolucion (Connection con);
	
	/**
	 * Metodo de consulta de los motivos Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaMotivosDevolucion (Connection con);
	
	/**
	 * Metodo de consulta de los ingresos del Paciente
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaListadoIngresos (Connection con, int codigoPaciente);
	
	/**
	 * Metodo de consulta del detalle de ingresos-cuenta
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleIC (Connection con, String codigoCuenta);
	
	/**
	 * Metodo de consulta del detalle de los articulos de ingresos-cuenta
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleAIC (Connection con, String codigoDevolucion, String tipoDevolucion);
	
	/**
	 * Metodo de consulta de las Devoluciones por Rango
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaDevR (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String tipoDevolucion, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe);
	
}