package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

/**
 * Definición de la interfaz para el acceso a la base de datos.
 *
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface AntecedenteMedicamentoDao
{
	public ResultadoBoolean insertar(	Connection con,
												int codigoPaciente,
												int codigo,
												String nombre,
												String dosis,
												String frecuencia,
												String fechaInicio,
												String fechaFin,
												String observaciones, String cantidad, String dosisD, String tiempoT, 
												String tipofrecuencia,
												String unidosis);
												
												
	public ResultadoBoolean insertarTransaccional(	Connection con,
																		int codigoPaciente,
																		int codigo,
																		String codigoA,
																		String nombre,
																		String dosis,
																		String frecuencia,
																		String fechaInicio,
																		String fechaFin,
																		String observaciones,
																		String estado, String cantidad, String dosisD, String tiempoT,
																		String tipofrecuencia,
																		String unidosis) throws SQLException;
																		
	public ResultadoBoolean modificar(	Connection con,
													int codigoPaciente,
													int codigo,
													String dosis,
													String frecuencia,
													String fechaInicio,
													String fechaFin,
													String observaciones, String cantidad, String dosisD, String tiempoT,
													String tipofrecuencia,
													String unidosis);
													
	public ResultadoBoolean modificarTransaccional(	Connection con,
																			int codigoPaciente,
																			int codigo,
																			String dosis,
																			String frecuencia,
																			String fechaInicio,
																			String fechaFin,
																			String observaciones,
																			String estado, String cantidad, String dosisD, String tiempoT,
																			String tipofrecuencia,
																			String unidosis) throws SQLException;
													
	public ResultadoBoolean existeAntecedente(	Connection con, 
																	int codigoPaciente, 
																	int codigo);
}
