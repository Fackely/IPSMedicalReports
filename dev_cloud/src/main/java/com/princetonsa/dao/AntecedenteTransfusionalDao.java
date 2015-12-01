package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

/**
 * Definición de la interfaz para el acceso a la base de datos del módulo de
 * antecedente transfusional
 *
 * @version 1.0, Septiembre 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface AntecedenteTransfusionalDao
{
	public ResultadoBoolean insertar(	Connection con,
												int  codigoPaciente,
												int codigo,
												String componente,
												String fechaTransf,
												String causa,
												String lugar,
												String edad,
												String donante,
												String observaciones);

	public ResultadoBoolean insertarTransaccional(	Connection con,
																		int codigoPaciente,
																		int codigo,
																		String componente,
																		String fechaTransf,
																		String causa,
																		String lugar,
																		String edad,
																		String donante,
																		String observaciones,
																		String estado) throws SQLException;
																		
	public ResultadoBoolean modificar(	Connection con,
													int codigoPaciente,
													int codigo,
													String fechaTransf,
													String causa,
													String lugar,
													String edad,
													String donante,
													String observaciones);
													
	public ResultadoBoolean modificarTransaccional(	Connection con,
																			int codigoPaciente,
																			int codigo,
																			String fechaTransf,
																			String causa,
																			String lugar,
																			String edad,
																			String donante,
																			String observaciones,
																			String estado) throws SQLException;
													
	public ResultadoBoolean existeAntecedente(	Connection con, 
																	int codigoPaciente, 
																	int codigo);
													
}
