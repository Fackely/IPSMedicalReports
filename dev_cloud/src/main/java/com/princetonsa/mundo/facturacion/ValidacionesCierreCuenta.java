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
 * @author sebasti�n g�mez
 *
 * Mundo para manejar las validaciones necesarias para
 * permitir cerrar la cuenta en el m�dulo de facturaci�n
 */
public class ValidacionesCierreCuenta {
	
	/**
	 * M�todo que verifica si la cuenta est� abierta para que
	 * se pueda cerrar.
	 * @param con
	 * @param idCuenta
	 * @return true=> cuenta abierta , false=> otro estado o error
	 */
	public static boolean validarEstadoCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarEstadoCuenta(con,idCuenta);
	}
	
	/**
	 * M�todo para validar si los cargos de servicios de la cuenta tienen valores pendientes
	 * por cobrar
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido , false=> inv�lido (hay cargos pendientes por cobrar)
	 */
	public static boolean validarCargosSeviciosXCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarCargosSeviciosXCuenta(con,idCuenta);
	}
	
	/**
	 * M�todo para validar si los cargos de medicamentos de la cuenta tienen valores pendientes 
	 * por cobrar, esto se hace calculando el n�mero de medicamentos despachados y el n�mero de devoluciones.
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido, false=> inv�lido (todav�a hay ordenes de medicamentos)
	 */
	public static boolean validarCargosMedicamentosXCuenta(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarCargosMedicamentosXCuenta(con,idCuenta);
	}
	
	/**
	 * M�todo que verifica si la cuenta tiene solicitudes en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido , false=> inv�lido (hay solicitudes en estado pendiente)
	 */
	public static boolean validarEstadosFactSolicitudes(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarEstadosFactSolicitudes(con,idCuenta);
	}
	
	
	/**
	 * M�todo para verificar si la cuenta de Consulta Externa tiene citas pendientes
	 * @param con
	 * @param idCuenta
	 * @return true=> hay citas pendientes, false=>no hay citas pendientes
	 */
	public static boolean validarSolicitudesConsultaExterna(Connection con,int idCuenta){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarSolicitudesConsultaExterna(con,idCuenta);
	}
	
	/**
	 * M�todo que verifica si la cuenta de urgencias u hospitalizaci�n 
	 * tiene la solicitud de valoraci�n en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido , false=> inv�lido (hay solicitudes en estado pendiente)
	 */
	public static boolean validarEstadoFactSolicitudValoracion(Connection con,int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesCierreCuentaDao().validarEstadoFactSolicitudValoracion(con,idCuenta);
	}
	
}
