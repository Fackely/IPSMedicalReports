package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Interfaz para el acceso a la base de datos del módulo de antecedentes
 * transfusionales, va  a ser implementada por cualquier fuente de datos.
 *
 * @version 1.0, Septiembre 2, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface AntecedentesTransfusionalesDao
{
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente);
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente);
	
	public ResultadoBoolean existenAntecedentesTransfusionales( Connection con, int codigoPaciente);
	
	public ResultSetDecorator consultarAntecedentesTransfusionales( Connection con, int codigoPaciente);	
	
	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones);
	
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones);
	
	public ResultadoBoolean modificarTransaccional(Connection con,	int codigoPaciente, String observaciones, String estado) throws SQLException;
	
	public ResultSetDecorator consultarTransfusiones( Connection con, int codigoPaciente);
}
