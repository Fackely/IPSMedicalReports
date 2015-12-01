/*
 * @(#)AuxiliarDiagnosticosDao.java
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
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AuxiliarDiagnosticos</code>.
 *
 * @version 1.0, Jun 16, 2003
 */
public interface AuxiliarDiagnosticosDao 
{
	/**
	 * Método que carga los diagnosticos de acuerdo al anexo 37
	 * En caso de interconsulta:
	 * -Si no hay evoluciones, se trae la valoración inicial
	 * -Si hay evoluciones se precargar los diagnosticos definitivos
	 * de la última evolución como presuntivos - > Modo Manejo
	 * definido para Interconsulta
	 * 
	 * En caso de evolución
	 * -Se trae los datos de la última evolución y en caso de ser
	 * la primera evolución después de un conjunto de interconsultas
	 * se traen los diagnosticos de estas
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroCuenta Número de la cuenta a la cual se le quieren
	 * cargar los diagnosticos
	 * @param esEvolucion Boolean que me indica si el auxiliar se está
	 * ejecutando para evoluciones o interconsultas
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDiagnosticos (Connection con, int numeroCuenta, boolean esEvolucion) throws SQLException;
	
	/**
	 * Método que carga el diagnostico de complicación de la última 
	 * evolución si esta retorna null
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param numeroCuenta Número de la cuenta a la cual se le quiere
	 * cargar el diagnostico de complicación
	 * @return
	 * @throws SQLException
	 */
	
	public ResultSetDecorator cargarDiagnosticoComplicacion(Connection con, int numeroCuenta) throws SQLException;
}
