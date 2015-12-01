/*
 * Created on May 5, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;

/**
 * @author sebasti�n g�mez
 *
 *Interfaz que define los m�todos necesarios para realizar las validaciones de
 *cierre de cuente en el m�dulo de Facturaci�n
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ValidacionesCierreCuentaDao
{
	/**
	 * M�todo que verifica si la cuenta est� abierta para que
	 * se pueda cerrar.
	 * @param con
	 * @param idCuenta
	 * @return true=> cuenta abierta , false=> otro estado o error
	 */
	public boolean validarEstadoCuenta(Connection con,int idCuenta);
	
	/**
	 * M�todo para validar si los cargos de servicios de la cuenta tienen valores pendientes
	 * por cobrar
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido , false=> inv�lido (hay cargos pendientes por cobrar)
	 */
	public boolean validarCargosSeviciosXCuenta(Connection con,int idCuenta);
	
	/**
	 * M�todo para validar si los cargos de medicamentos de la cuenta tienen valores pendientes 
	 * por cobrar, esto se hace calculando el n�mero de medicamentos despachados y el n�mero de devoluciones.
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido, false=> inv�lido (todav�a hay ordenes de medicamentos)
	 */
	public boolean validarCargosMedicamentosXCuenta(Connection con,int idCuenta);
	
	/**
	 * M�todo que verifica si la cuenta tiene solicitudes en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido , false=> inv�lido (hay solicitudes en estado pendiente)
	 */
	public boolean validarEstadosFactSolicitudes(Connection con,int idCuenta);
	
	/**
	 * M�todo para verificar si la cuenta de Consulta Externa tiene citas pendientes
	 * @param con
	 * @param idCuenta
	 * @return true=> hay citas pendientes, false=>no hay citas pendientes
	 */
	public boolean validarSolicitudesConsultaExterna(Connection con,int idCuenta);
	
	/**
	 * M�todo que verifica si la cuenta de urgencias u hospitalizaci�n 
	 * tiene la solicitud de valoraci�n en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> v�lido , false=> inv�lido (hay solicitudes en estado pendiente)
	 */
	public boolean validarEstadoFactSolicitudValoracion(Connection con,int idCuenta);
}

