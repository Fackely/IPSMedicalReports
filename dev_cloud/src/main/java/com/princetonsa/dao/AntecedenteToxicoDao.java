package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

/**
 * Interfaz para acceder a la fuente de datos, la parte de Antecedentes
 * Tóxicos predefinidos y otros
 *
 * @version 1.0, Noviembre 28, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */

public interface AntecedenteToxicoDao
{
	public ResultadoBoolean insertarPredefinido (Connection con, int codigoPaciente, int codTipoPredefinido, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones);
	
	public ResultadoBoolean insertarPredefinidoTransaccional (Connection con, int codigoPaciente, int codTipoPredefinido, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean insertarOtro (Connection con, int codigoPaciente, int codigo, String nombre, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones);
	
	public ResultadoBoolean insertarOtroTransaccional (Connection con, int codigoPaciente, int codigo, String nombre, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones, String estado)  throws SQLException;
	
	public ResultadoBoolean modificarPredefinido (Connection con, int codigoPaciente, int codigo, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones);
	
	public ResultadoBoolean modificarPredefinidoTransaccional (Connection con, int codigoPaciente, int codigo, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean modificarOtro (Connection con, int codigoPaciente, int codigo,  boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones);
	
	public ResultadoBoolean modificarOtroTransaccional (Connection con, int codigoPaciente, int codigo, String nombre, boolean habitoActual, String cantidad, String frecuencia, String tiempo, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean existeAntecedentePredefinido(Connection con, int codigoPaciente, int codigo);
	
	public ResultadoBoolean existeAntecedenteOtro(Connection con, int codigoPaciente, int codigo);	
}
