/*
 * @(#)CentrosCostoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 31 /May/ 2006
 */
public interface SistemasMotivoConsultaDao 
{
	/**
	 * Método para consultar los sistemas motivos de consulta de urgencias existenes en la base de datos
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarMotivosConsultaUrg(Connection con , int institucion) throws SQLException;
	
	/**
	 * Método para eliminar un motivo de consulta de urgencias dado su codigo
	 * @param con
	 * @param codigoMotivo
	 * @return int
	 * @throws SQLException
	 */
	public int eliminarMotivoConsultaUrg(Connection con, int codigoMotivo) throws SQLException;
	
	/**
	 * Método para la insercion de un nuevo Motivo de Consulta de Urgencias con todos sus atributos
	 * @param con
	 * @param codigoMotivo
	 * @param descripcion
	 * @param identificador
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public int insertarMotivoConsultaUrg(Connection con, String codigoMotivo, String descripcion, String identificador, int institucion) throws SQLException;
	
	
	/**
	 * Método para saber si un sistema motivo de consulta de urgencias esta siendo utilizado en 
	 * la funcionalidad de signos sintomas x sistema
	 * @param con
	 * @param codigoMotivo
	 * @param institucion
	 * @return
	 */
	public boolean estaSiendoUtilizada(Connection con, int codigoMotivo, int institucion);
}