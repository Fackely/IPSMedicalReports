/*
 * @(#)OracleSistemasMotivoConsultaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.historiaClinica.SistemasMotivoConsultaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseSistemasMotivoConsultaDao;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 31 /May/ 2006
 */
public class OracleSistemasMotivoConsultaDao implements SistemasMotivoConsultaDao 
{
	
	/**
	 * Cadena con el statement necesario para insertar un motivo de consulta de urgencias
	 */
	private final static String insertarMotivoConsultaUrgStr = " INSERT INTO motivo_consulta_urg " +
														  	   " (codigo, " +
														  	   " identificador, " +
														  	   " descripcion, " +
															   " institucion ) " +
														 	   " VALUES (seq_motivo_consulta_urg.nextval, ?, ?, ? )" ;
	
	
	/**
	 * Método para consultar los sistemas motivos de consulta de urgencias existenes en la base de datos
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMotivosConsultaUrg(Connection con , int institucion) throws SQLException
	{
		return SqlBaseSistemasMotivoConsultaDao.consultarMotivosConsultaUrg(con, institucion);
	}
	/**
	 * Método para eliminar un motivo de consulta de urgencias dado su codigo
	 * @param con
	 * @param codigoMotivo
	 * @return int
	 * @throws SQLException
	 */
	public int eliminarMotivoConsultaUrg(Connection con, int codigoMotivo) throws SQLException
	{
		return SqlBaseSistemasMotivoConsultaDao.eliminarMotivoConsultaUrg(con, codigoMotivo);
	}
	
	/**
	 * Método para la insercion de un nuevo Motivo de Consulta de Urgencias con todos sus atributos
	 * @param con
	 * @param codigoMotivo
	 * @param descripcion
	 * @param identificador
	 * @param institucion
	 * @param insertarMotivoConsultaUrgStr -> Postgres - Oracle
	 * @return
	 * @throws SQLException
	 */
	public int insertarMotivoConsultaUrg(Connection con, String codigoMotivo, String descripcion, String identificador, int institucion) throws SQLException
	{
		return SqlBaseSistemasMotivoConsultaDao.insertarMotivoConsultaUrg(con, codigoMotivo, descripcion, identificador, institucion, insertarMotivoConsultaUrgStr);
	}
	
	/**
	 * Método para saber si un sistema motivo de consulta de urgencias esta siendo utilizado en 
	 * la funcionalidad de signos sintomas x sistema
	 * @param con
	 * @param codigoMotivo
	 * @param institucion
	 * @return
	 */
	public boolean estaSiendoUtilizada(Connection con, int codigoMotivo, int institucion)
	{
		return SqlBaseSistemasMotivoConsultaDao.estaSiendoUtilizada(con, codigoMotivo, institucion);
	}
	
}