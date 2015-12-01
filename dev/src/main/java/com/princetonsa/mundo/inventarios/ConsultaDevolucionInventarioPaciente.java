package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaDevolucionInventarioPacienteDao;

public class ConsultaDevolucionInventarioPaciente
{
	private static Logger logger = Logger.getLogger(ConsultaDevolucionInventarioPaciente.class);
	
	private static ConsultaDevolucionInventarioPacienteDao getConsultaDevolucionInventarioPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaDevolucionInventarioPacienteDao();		
	}
	
	/**
	 * Metodo de consulta de los centros de costo relacionados con el centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaCentroCosto (Connection con)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaCentroCosto(con);
	}
	
	/**
	 * Metodo de consulta de los centros de los Estados de Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaEstadosDevolucion (Connection con)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaEstadosDevolucion(con);
	}
	
	/**
	 * Metodo de consulta de los centros de los Motivos de Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaMotivosDevolucion (Connection con)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaMotivosDevolucion(con);
	}
	
	/**
	 * Metodo de consulta de los ingresos del paciente
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaListadoIngresos (Connection con, int codigoPaciente)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaListadoIngresos(con, codigoPaciente);
	}
	
	/**
	 * Metodo de consulta del detalle de ingresos-cuenta
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaDetalleIC (Connection con, String codigoCuenta)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaDetalleIC(con, codigoCuenta);
	}
	
	/**
	 * Metodo de consulta del detalle de los articulos de ingresos-cuenta
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaDetalleAIC (Connection con, String codigoDevolucion, String tipoDevolucion)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaDetalleAIC(con, codigoDevolucion, tipoDevolucion);
	}
	
	/**
	 * Metodo de consulta de las Devoluciones por Rango
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaDevR (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String tipoDevolucion, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe)
	{
		return getConsultaDevolucionInventarioPacienteDao().consultaDevR(con, numeroDevolucion, centroAtencion, almacen, centroCosto, fechaIni, fechaFin, estadoDevolucion, tipoDevolucion, motivoDevolucion, usuarioDevuelve, usuarioRecibe);
	}
}