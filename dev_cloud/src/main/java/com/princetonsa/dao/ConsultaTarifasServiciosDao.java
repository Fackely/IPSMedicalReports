/*
 * @(#)ConsultaTarifasServiciosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 24 /Mar/ 2006
 */
public interface ConsultaTarifasServiciosDao 
{
	
	/**
	 * Metodo para realizar la busqueda avanzada de los servicios por los campos
	 * que se especifiquen
	 * @param con
	 * @param codigoInterno
	 * @param codigoCups
	 * @param descripcionCups
	 * @param codigoIss
	 * @param descripcionIss
	 * @param codigoSoat
	 * @param descripcionSoat
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 * @param acronimoNaturaleza
	 * @param codigoGrupoServicio
	 * @return
	 */
	public HashMap busquedaServicios (Connection con, String codigoInterno, String tipoTarifario, String codigoServicio, String descripcionServicio, int codigoEspecialidad, String acronimoTipoServicio, String acronimoNaturaleza,int codigoGrupoServicio) throws SQLException;
	
	
	/**
	 * Método para consultar el encabzado del detalle de una tarifa de servicios dado el codigo de servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaEncabezadoDetalle(Connection con, int codigoServicio)  throws SQLException;
	
	/**
	 * Método para consultar el cuerpo del detalle de una tarifa de servicios dado el codigo interno del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaCuerpoDetalle(Connection con, int codigoServicio) throws SQLException;
	
	

}