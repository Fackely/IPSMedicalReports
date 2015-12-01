package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteTransfusionalDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedenteTransfusionalDao;

/**
 * Implementación de la interfaz para el acceso a la base de datos del
 * antecedente transfusional
 *
 * @version 1.0, Septiembre 1, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleAntecedenteTransfusionalDao implements AntecedenteTransfusionalDao
{
	public ResultadoBoolean insertar(	Connection con,
												int codigoPaciente,
												int codigo,
												String componente,
												String fechaTransf,
												String causa,
												String lugar,
												String edad,
												String donante,
												String observaciones)
	{
		return SqlBaseAntecedenteTransfusionalDao.insertar(	con, codigoPaciente, codigo, componente, fechaTransf, causa, lugar, edad, donante, observaciones);
	}

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
																		String estado) throws SQLException
	{
	    return SqlBaseAntecedenteTransfusionalDao.insertarTransaccional(	con,
				codigoPaciente,
				codigo,
				componente,
				fechaTransf,
				causa,
				lugar,
				edad,
				donante,
				observaciones,
				estado) ;
	}
	
	public ResultadoBoolean modificar(	Connection con,
													int codigoPaciente,
													int codigo,
													String fechaTransf,
													String causa,
													String lugar,
													String edad,
													String donante,
													String observaciones)
	{		
		return SqlBaseAntecedenteTransfusionalDao.modificar(con,	codigoPaciente, codigo, fechaTransf, causa, lugar, edad, donante, observaciones);
	}

	public ResultadoBoolean modificarTransaccional(	Connection con,
																			int codigoPaciente,
																			int codigo,
																			String fechaTransf,
																			String causa,
																			String lugar,
																			String edad,
																			String donante,
																			String observaciones,
																			String estado) throws SQLException
	{
	    return SqlBaseAntecedenteTransfusionalDao.modificarTransaccional(	con,
	    		codigoPaciente,
	    		codigo,
	    		fechaTransf,
	    		causa,
	    		lugar,
	    		edad,
	    		donante,
	    		observaciones,
	    		estado);	
	}
	
	public ResultadoBoolean existeAntecedente(	Connection con, 
																	int codigoPaciente, 
																	int codigo)
	{
		return SqlBaseAntecedenteTransfusionalDao.existeAntecedente(con, codigoPaciente, codigo);
	}
}
