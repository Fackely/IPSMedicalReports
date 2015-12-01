/*
 * @(#)HistoricoEvolucionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase define el contrato de operaciones que debe implementar
 * un objeto que preste servicio de acceso de BD a <code>HistoricoEvoluciones</code>.
 *
 * @version Jun 10, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public interface HistoricoEvolucionesDao 
{
	/**
	 * Informa si existe al menos una evolucion grabada para la cuenta dada por parametro
	 * @param con conexión abierta con la fuente de datos
	 * @param idCuenta número de identificación de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public boolean existeHistoricoEvoluciones(Connection con, int idCuenta) ;
	
	/**
	 * Carga un conjunto de evoluciones desde la fuente de datos,
	 * dada la cuenta a la que pertenecen.
	 * @param con conexión abierta con la fuente de datos
	 * @param idCuenta número de identificación de la cuenta
	 * cuyas evoluciones se desean cargar
	 * @return
	 */
	public ResultSetDecorator cargar(Connection con, int idCuenta) throws SQLException;
	
	/**
	 * Carga un conjunto de evoluciones desde la fuente de datos,
	 * dada la cuenta y la cuenta asociada a la que pertenecen.
	 * 
	 * @param con conexión abierta con la fuente de datos
	 * @param idCuenta número de la cuenta cuyas evoluciones se 
	 * desean cargar
	 * @param idCuentaAsociada número de la cuenta y su asociada
	 * cuyas evoluciones se desean cargar
	 * @return
	 */
	public ResultSetDecorator cargarEvolucionesCuentaYAsocio(Connection con, int idCuenta, int idCuentaAsociada) throws SQLException;
	
	/**
	 * Carga un conjunto de evoluciones (dado su codigo de tipo, hospitalizacion
	 * urgencias ... definidos en la clase Evolucion)desde la fuente de datos,
	 * dado el paciente al que pertenecen.
	 * 
	 * @param con conexión abierta con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de identificación
	 * del paciente al que se quieren sacar las evoluciones
	 * @param numeroIdentificacionPaciente Número de identificación
	 * del paciente al que se quieren sacar las evoluciones 
	 * @param codigoTipoEvolucion Código del tipo de evolución 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente, int codigoTipoEvolucion) throws SQLException;

	/**
	 * Carga un conjunto de evoluciones desde la fuente de datos,
	 * dado el paciente al que pertenecen.
	 * 
	 * @param con conexión abierta con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de identificación
	 * del paciente al que se quieren sacar las evoluciones
	 * @param numeroIdentificacionPaciente Número de identificación
	 * del paciente al que se quieren sacar las evoluciones 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente) throws SQLException;
}