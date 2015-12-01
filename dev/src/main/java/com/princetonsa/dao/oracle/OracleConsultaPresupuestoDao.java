/*
 * @(#)OracleConsultaPresupuestoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.ConsultaPresupuestoDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaPresupuestoDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 16 /Ene/ 2006
 */
public class OracleConsultaPresupuestoDao implements ConsultaPresupuestoDao 
{

	/**
	 * Método para consultar el detalle básico de un presupuesto
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetallePresupuesto(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarDetallePresupuesto(con, codigoPresupuesto);
	}
	
	/**
	 * Método para buscar un presupuesto segun unos parametros dados
	 * @param con
	 * @param presupuestoInicial
	 * @param presupuestoFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param tipoId
	 * @param numeroId
	 * @param codigoMedico
	 * @param responsable
	 * @return
	 */
	public HashMap busquedaPresupuestos (Connection con,int presupuestoInicial, int presupuestoFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, String tipoId, int numeroId, int codigoMedico, int responsable)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.busquedaPresupuestos(con, presupuestoInicial, presupuestoFinal, fechaElaboracionIncial, fechaElaboracionFinal, tipoId, numeroId, codigoMedico, responsable);
	}
	
	
	/**
	 * Método para consultar los Articulos relacionados a un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarArticulos(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarArticulos(con, codigoPresupuesto);
	}
	
	/**
	 * 
	 */
	public HashMap consultarArticulosMedIns(Connection con, int codigoPresupuesto, boolean dividirMedIns)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarArticulosMedIns(con, codigoPresupuesto, dividirMedIns);
	}
	
	/**
	 * Método para consultar los servicios relacionados a un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarServicios(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarServicios(con, codigoPresupuesto);
	}
	
	
	/**
	 * Método para consultar las intervenciones (servicio - especialidad) de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarIntenervenciones(Connection con, int codigoPresupuesto)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarIntenervenciones(con, codigoPresupuesto);
	}
	
	/**
	 * Método para consultar los formatos de impresion existentes en el moento de descidir la impresion
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatosExistentes(Connection con, int institucion)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarFormatosExistentes(con, institucion);
	}
	
	/**
	 * Metodo para consultar los profesionales de la salud que
	 * su ocupacion medica sea estrictamente "MEDICO"
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMedicos(Connection con, int codigoOcupMedicoGeneral, int codigoOcupMedicoEspecialista)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarMedicos(con,codigoOcupMedicoGeneral, codigoOcupMedicoEspecialista);
	}
	
	/**
	 * Método apra consultar ñlos tipos de identificacion existentes en el sistema
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarTiposId(Connection con)  throws SQLException
	{
		return SqlBaseConsultaPresupuestoDao.consultarTiposId(con);
	}
}
