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
	 * M�todo que carga los diagnosticos de acuerdo al anexo 37
	 * En caso de interconsulta:
	 * -Si no hay evoluciones, se trae la valoraci�n inicial
	 * -Si hay evoluciones se precargar los diagnosticos definitivos
	 * de la �ltima evoluci�n como presuntivos - > Modo Manejo
	 * definido para Interconsulta
	 * 
	 * En caso de evoluci�n
	 * -Se trae los datos de la �ltima evoluci�n y en caso de ser
	 * la primera evoluci�n despu�s de un conjunto de interconsultas
	 * se traen los diagnosticos de estas
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroCuenta N�mero de la cuenta a la cual se le quieren
	 * cargar los diagnosticos
	 * @param esEvolucion Boolean que me indica si el auxiliar se est�
	 * ejecutando para evoluciones o interconsultas
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDiagnosticos (Connection con, int numeroCuenta, boolean esEvolucion) throws SQLException;
	
	/**
	 * M�todo que carga el diagnostico de complicaci�n de la �ltima 
	 * evoluci�n si esta retorna null
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param numeroCuenta N�mero de la cuenta a la cual se le quiere
	 * cargar el diagnostico de complicaci�n
	 * @return
	 * @throws SQLException
	 */
	
	public ResultSetDecorator cargarDiagnosticoComplicacion(Connection con, int numeroCuenta) throws SQLException;
}
