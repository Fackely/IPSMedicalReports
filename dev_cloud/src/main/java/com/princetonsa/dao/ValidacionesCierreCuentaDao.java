/*
 * Created on May 5, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;

/**
 * @author sebastián gómez
 *
 *Interfaz que define los métodos necesarios para realizar las validaciones de
 *cierre de cuente en el módulo de Facturación
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ValidacionesCierreCuentaDao
{
	/**
	 * Método que verifica si la cuenta está abierta para que
	 * se pueda cerrar.
	 * @param con
	 * @param idCuenta
	 * @return true=> cuenta abierta , false=> otro estado o error
	 */
	public boolean validarEstadoCuenta(Connection con,int idCuenta);
	
	/**
	 * Método para validar si los cargos de servicios de la cuenta tienen valores pendientes
	 * por cobrar
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay cargos pendientes por cobrar)
	 */
	public boolean validarCargosSeviciosXCuenta(Connection con,int idCuenta);
	
	/**
	 * Método para validar si los cargos de medicamentos de la cuenta tienen valores pendientes 
	 * por cobrar, esto se hace calculando el número de medicamentos despachados y el número de devoluciones.
	 * @param con
	 * @param idCuenta
	 * @return true=> válido, false=> inválido (todavía hay ordenes de medicamentos)
	 */
	public boolean validarCargosMedicamentosXCuenta(Connection con,int idCuenta);
	
	/**
	 * Método que verifica si la cuenta tiene solicitudes en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay solicitudes en estado pendiente)
	 */
	public boolean validarEstadosFactSolicitudes(Connection con,int idCuenta);
	
	/**
	 * Método para verificar si la cuenta de Consulta Externa tiene citas pendientes
	 * @param con
	 * @param idCuenta
	 * @return true=> hay citas pendientes, false=>no hay citas pendientes
	 */
	public boolean validarSolicitudesConsultaExterna(Connection con,int idCuenta);
	
	/**
	 * Método que verifica si la cuenta de urgencias u hospitalización 
	 * tiene la solicitud de valoración en estado de facturacion pendiente
	 * @param con
	 * @param idCuenta
	 * @return true=> válido , false=> inválido (hay solicitudes en estado pendiente)
	 */
	public boolean validarEstadoFactSolicitudValoracion(Connection con,int idCuenta);
}

