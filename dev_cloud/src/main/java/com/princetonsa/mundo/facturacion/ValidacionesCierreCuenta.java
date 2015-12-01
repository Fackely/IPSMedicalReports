/*
 * Created on May 5, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;

/**
 * @author sebastián gómez
 *
 * Mundo para manejar las validaciones necesarias para
 * permitir cerrar la cuenta en el módulo de facturación
 */
public class ValidacionesCierreCuenta {
	
	/**
	 * Método que verifica si la cuenta está abierta para que
	 * se pueda cerrar.
	 * @param con
	 * @param idCuenta
	 * @return true=> cuenta abierta , false=> otro estado o error
	 */
	public static boolean validarEstadoCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarEstadoCuenta(con,idCuenta);
	}
	
	/**
	 * Método para validar si los cargos de servicios de la cuenta tienen valores pendientes
	 * por cobrar
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay cargos pendientes por cobrar)
	 */
	public static boolean validarCargosSeviciosXCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarCargosSeviciosXCuenta(con,idCuenta);
	}
	
	/**
	 * Método para validar si los cargos de medicamentos de la cuenta tienen valores pendientes 
	 * por cobrar, esto se hace calculando el número de medicamentos despachados y el número de devoluciones.
	 * @param con
	 * @param idCuenta
	 * @return true=> válido, false=> inválido (todavía hay ordenes de medicamentos)
	 */
	public static boolean validarCargosMedicamentosXCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarCargosMedicamentosXCuenta(con,idCuenta);
	}
	
	/**
	 * Método que verifica si la cuenta tiene solicitudes en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay solicitudes en estado pendiente)
	 */
	public static boolean validarEstadosFactSolicitudes(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarEstadosFactSolicitudes(con,idCuenta);
	}
	
	
	/**
	 * Método para verificar si la cuenta de Consulta Externa tiene citas pendientes
	 * @param con
	 * @param idCuenta
	 * @return true=> hay citas pendientes, false=>no hay citas pendientes
	 */
	public static boolean validarSolicitudesConsultaExterna(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarSolicitudesConsultaExterna(con,idCuenta);
	}
	
	/**
	 * Método que verifica si la cuenta de urgencias u hospitalización 
	 * tiene la solicitud de valoración en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay solicitudes en estado pendiente)
	 */
	public static boolean validarEstadoFactSolicitudValoracion(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarEstadoFactSolicitudValoracion(con,idCuenta);
	}
	
}
