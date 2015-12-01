package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Definición de la interfaz para el acceso a la base de datos del módulo de
 * antecedentes de medicamentos.
 *
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface AntecedentesMedicamentosDao
{
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente);
	
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente);
	
	public ResultadoBoolean existenAntecedentesMedicamentos( Connection con, int codigoPaciente);
	
	public ResultSetDecorator consultarAntecedentesMedicamentos( Connection con, int codigoPaciente);
	
	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones);
	
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones);
	
	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException;
	
	public ResultSetDecorator consultarMedicamentos( Connection con, int codigoPaciente);
	
	public HashMap<String, Object> consultaFormaConc (Connection con, int codigoArticulo);
}
