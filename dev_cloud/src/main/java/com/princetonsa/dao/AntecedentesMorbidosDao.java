/*
 * @(#)AntecedentesMorbidosDao.java
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

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Interfaz para acceder a la fuente de datos, la parte de Antecedentes Mórbidos
 *
 * @version 1.0, Agosto 15, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface AntecedentesMorbidosDao
{
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente);
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente);
	
	public ResultadoBoolean existenAntecedentesMorbidos(Connection con, int codigoPaciente);
	
	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones);
	
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones);
	
	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException;
	
	public ResultSetDecorator cargarMorbidosMedicosPredefinidos(Connection con, int codigoPaciente);
	
	public ResultSetDecorator cargarMorbidosMedicosOtros(Connection con, int codigoPaciente);
	
	public ResultSetDecorator cargarMorbidosQuirurgicos(Connection con, int codigoPaciente);
	
	public ResultSetDecorator consultarAntecedentesMorbidos( Connection con, int codigoPaciente);
}
