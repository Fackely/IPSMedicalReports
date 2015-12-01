/*
 * @(#)OracleAntecedenteMorbidoMedicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedenteMorbidoMedicoDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedenteMorbidoMedicoDao;

/**
 * Implementación de los métodos para acceder a la fuente de datos, la parte de
 * Antecedentes Mórbidos Médicos, predefinidos y otros, para Postgres.
 *
 * @version 1.0, Agosto 12, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleAntecedenteMorbidoMedicoDao implements AntecedenteMorbidoMedicoDao
{
	
	
	/**
	 * Inserta un nuevo antecedente mórbido médico en la base de datos. Este
	 * antecedente mórbido hace parte de los predefinidos en la base de datos.
	 * @param		Connection, con. Conexión abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. código en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. Código del antecedente mórbido en la base de datos.
	 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
	 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el médico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
	 * 					"empezar", "finalizar" y "continuar"
	 * @return 		Resultado, retorna si la inserción fue realizada con exito
	 * 					"true" o no "false" y su descripción asociada en caso de 
	 * 					ser necesaria.
	 */	
	public ResultadoBoolean insertarPredefinidoTransaccional (Connection con, 
																									int codigoPaciente, 
																									int codigo, 
																									String fechaInicio, 
																									String tratamiento, 
																									String restriccionDietaria, 
																									String observaciones, 
																									String estado) throws SQLException	
	{
	    return SqlBaseAntecedenteMorbidoMedicoDao.insertarPredefinidoTransaccional (con, 
				codigoPaciente, 
				codigo, 
				fechaInicio, 
				tratamiento, 
				restriccionDietaria, 
				observaciones, 
				estado) ; 
   	}
	
	

	/**
	 * Inserta un nuevo antecedente mórbido médico en la base de datos. Este
	 * antecedente mórbido NO hace parte de los predefinidos en la base de
	 * datos. La inserción de este antecedente mórbido solo se hace para este
	 * paciente, no aparecera en la lista de opciones de los otros.
	 * @param		Connection, con. Conexión abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. código en la fuente de datos del
	 * paciente.
	 * @param 	String, nombre. Nombre del antecedente mórbido médico nuevo 
	 * 					que se va a ingresar en la base de datos, solo para este 
	 * 					paciente.
	 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
	 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el médico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
	 * 					"empezar", "finalizar" y "continuar". Los estados están 
	 * 					definidos en la interfaz de Constantes util.ConstantesBD.
	 * @return 		booelan, retorna si la inserción fue realizada con exito
	 * 					"true" o no "false" 
	 */		
	public ResultadoBoolean insertarOtroTransaccional(	Connection con,
																				int codigoPaciente,
																				int codigo,
																				String nombre,
																				String fechaInicio,
																				String tratamiento,
																				String restriccionDietaria,
																				String observaciones,
																				String estado ) throws SQLException
	{
	    return SqlBaseAntecedenteMorbidoMedicoDao.insertarOtroTransaccional(con,
				codigoPaciente,
				codigo,
				nombre,
				fechaInicio,
				tratamiento,
				restriccionDietaria,
				observaciones,
				estado ); 	
	}
	
	
	
	/**
	 * Modifica un antecedente mórbido médico existente en la base de datos.
	 * Este antecedente mórbido hace parte de los predefinidos en la base de
	 * datos. La modificación hace parte de una transacción
	 * @param		Connection, con. Conexión abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. código en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. Código del antecedente mórbido en la base de datos.
	 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
	 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el médico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
	 * 					"empezar", "finalizar" y "continuar". Los estados están 
	 * 					definidos en la interfaz de Constantes util.ConstantesBD.
	 * @return 		booelan, retorna si la inserción fue realizada con exito
	 * 					"true" o no "false"
	 */
	public ResultadoBoolean modificarPredefinidoTransaccional(	Connection con,
																							int codigoPaciente,
																							int codigo,
																							String fechaInicio,
																							String tratamiento,
																							String restriccionDietaria,
																							String observaciones,
																							String estado ) throws SQLException
	{
	    return SqlBaseAntecedenteMorbidoMedicoDao.modificarPredefinidoTransaccional(	con,
				codigoPaciente,
				codigo,
				fechaInicio,
				tratamiento,
				restriccionDietaria,
				observaciones,
				estado ); 
	}	
	
	/**
	 * Retorna true si ya ha sido ingresado un registro para el paciente dado,
	 * del antecedente mórbido predefinido dado.
	 * @param 	Connection, con. Conexión con la base de datos
	 * @param 	String, codigoPaciente. código en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. Código en la base de datos del antecedente
	 * 					mórbido predefinido
	 * @return 		Resultado, retorna true si ya existia en la base de datos
	 * 					false de lo contrario con su descripción.
	 */
	public ResultadoBoolean existeAntecedentePredefinido(	Connection con, 
																									int codigoPaciente, 
																									int codigo)
	{
		return SqlBaseAntecedenteMorbidoMedicoDao.existeAntecedentePredefinido(con,codigoPaciente,codigo);
	}
	
	
	

	/**
	 * Modifica un antecedente mórbido médico de los adicionales existente en la
	 * base de datos. Este antecedente mórbido NO hace parte de los predefinidos
	 * en la base de datos.
	 * @param		Connection, con. Conexión abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. código en la fuente de datos del
	 * paciente.
	 * @param		int, codigo. Código del antecedente mórbido en la bd.
	 * @param 	String, nombre. Nombre del antecedente mórbido médico nuevo 
	 * 					que se va a ingresar en la base de datos, solo para este 
	 * 					paciente.
	 * @param 	String, fechaInicio. Fecha en la que se inició el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se está tratando.
	 * @param 	String, restriccionDietaria. Restricción dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el médico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacción, 
	 * 					"empezar", "finalizar" y "continuar". Los estados están 
	 * 					definidos en la interfaz de Constantes util.ConstantesBD.
	 * @return 		Resultado, retorna si la inserción fue realizada con exito
	 * 					"true" o no "false" y su descripción asociada en caso de 
	 * 					ser necesaria.
	 */		
	public ResultadoBoolean modificarOtroTransaccional(	Connection con,
																					int codigoPaciente,
																					int codigo,
																					String nombre,
																					String fechaInicio,
																					String tratamiento,
																					String restriccionDietaria,
																					String observaciones,
																					String estado ) throws SQLException
	{
	    return SqlBaseAntecedenteMorbidoMedicoDao.modificarOtroTransaccional(	con,
				codigoPaciente,
				codigo,
				nombre,
				fechaInicio,
				tratamiento,
				restriccionDietaria,
				observaciones,
				estado );
	}	

	/**
	 * Retorna true si ya ha sido ingresado un registro para el paciente dado,
	 * del antecedente mórbido predefinido dado.
	 * @param 	Connection, con. Conexión con la base de datos
	 * @param 	String, codigoPaciente. código en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. Código en la base de datos del antecedente
	 * 					mórbido predefinido
	 * @return 		Resultado, retorna true si ya existia en la base de datos
	 * 					false de lo contrario con su descripción.
	 */
	public ResultadoBoolean existeAntecedenteOtro(	Connection con, 
																			int codigoPaciente, 
																			int codigo)
	{
		return SqlBaseAntecedenteMorbidoMedicoDao.existeAntecedenteOtro(con,codigoPaciente,codigo);
	}
	
}
