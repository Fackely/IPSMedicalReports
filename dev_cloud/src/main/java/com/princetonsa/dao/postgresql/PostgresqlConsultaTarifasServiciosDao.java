/*
 * @(#)PostgresqlConsultaTarifasServiciosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.ConsultaTarifasServiciosDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaTarifasServiciosDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 24 /May/ 2005
 */
public class PostgresqlConsultaTarifasServiciosDao implements ConsultaTarifasServiciosDao 
{
	/**
	 * Metodo para realizar la busqueda avanzada de los servicios por los campos
	 */
	public HashMap busquedaServicios (Connection con, String codigoInterno, String tipoTarifario, String codigoServicio, String descripcionServicio, int codigoEspecialidad, String acronimoTipoServicio, String acronimoNaturaleza,int codigoGrupoServicio) throws SQLException
	{
		return SqlBaseConsultaTarifasServiciosDao.busquedaServicios(con, codigoInterno, tipoTarifario,codigoServicio,descripcionServicio, codigoEspecialidad, acronimoTipoServicio, acronimoNaturaleza, codigoGrupoServicio);
	}
	
	/**
	 * Método para consultar el encabzado del detalle de una tarifa de servicios dado el codigo de servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaEncabezadoDetalle(Connection con, int codigoServicio)  throws SQLException
	{
		return SqlBaseConsultaTarifasServiciosDao.consultaEncabezadoDetalle(con, codigoServicio);
	}
	
	/**
	 * Método para consultar el cuerpo del detalle de una tarifa de servicios dado el codigo interno del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaCuerpoDetalle(Connection con, int codigoServicio) throws SQLException
	{
		return SqlBaseConsultaTarifasServiciosDao.consultaCuerpoDetalle(con, codigoServicio);
	}
}
