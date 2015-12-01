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
 * Implementaci�n de los m�todos para acceder a la fuente de datos, la parte de
 * Antecedentes M�rbidos M�dicos, predefinidos y otros, para Postgres.
 *
 * @version 1.0, Agosto 12, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class OracleAntecedenteMorbidoMedicoDao implements AntecedenteMorbidoMedicoDao
{
	
	
	/**
	 * Inserta un nuevo antecedente m�rbido m�dico en la base de datos. Este
	 * antecedente m�rbido hace parte de los predefinidos en la base de datos.
	 * @param		Connection, con. Conexi�n abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. c�digo en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. C�digo del antecedente m�rbido en la base de datos.
	 * @param 	String, fechaInicio. Fecha en la que se inici� el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se est� tratando.
	 * @param 	String, restriccionDietaria. Restricci�n dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el m�dico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacci�n, 
	 * 					"empezar", "finalizar" y "continuar"
	 * @return 		Resultado, retorna si la inserci�n fue realizada con exito
	 * 					"true" o no "false" y su descripci�n asociada en caso de 
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
	 * Inserta un nuevo antecedente m�rbido m�dico en la base de datos. Este
	 * antecedente m�rbido NO hace parte de los predefinidos en la base de
	 * datos. La inserci�n de este antecedente m�rbido solo se hace para este
	 * paciente, no aparecera en la lista de opciones de los otros.
	 * @param		Connection, con. Conexi�n abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. c�digo en la fuente de datos del
	 * paciente.
	 * @param 	String, nombre. Nombre del antecedente m�rbido m�dico nuevo 
	 * 					que se va a ingresar en la base de datos, solo para este 
	 * 					paciente.
	 * @param 	String, fechaInicio. Fecha en la que se inici� el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se est� tratando.
	 * @param 	String, restriccionDietaria. Restricci�n dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el m�dico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacci�n, 
	 * 					"empezar", "finalizar" y "continuar". Los estados est�n 
	 * 					definidos en la interfaz de Constantes util.ConstantesBD.
	 * @return 		booelan, retorna si la inserci�n fue realizada con exito
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
	 * Modifica un antecedente m�rbido m�dico existente en la base de datos.
	 * Este antecedente m�rbido hace parte de los predefinidos en la base de
	 * datos. La modificaci�n hace parte de una transacci�n
	 * @param		Connection, con. Conexi�n abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. c�digo en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. C�digo del antecedente m�rbido en la base de datos.
	 * @param 	String, fechaInicio. Fecha en la que se inici� el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se est� tratando.
	 * @param 	String, restriccionDietaria. Restricci�n dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el m�dico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacci�n, 
	 * 					"empezar", "finalizar" y "continuar". Los estados est�n 
	 * 					definidos en la interfaz de Constantes util.ConstantesBD.
	 * @return 		booelan, retorna si la inserci�n fue realizada con exito
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
	 * del antecedente m�rbido predefinido dado.
	 * @param 	Connection, con. Conexi�n con la base de datos
	 * @param 	String, codigoPaciente. c�digo en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. C�digo en la base de datos del antecedente
	 * 					m�rbido predefinido
	 * @return 		Resultado, retorna true si ya existia en la base de datos
	 * 					false de lo contrario con su descripci�n.
	 */
	public ResultadoBoolean existeAntecedentePredefinido(	Connection con, 
																									int codigoPaciente, 
																									int codigo)
	{
		return SqlBaseAntecedenteMorbidoMedicoDao.existeAntecedentePredefinido(con,codigoPaciente,codigo);
	}
	
	
	

	/**
	 * Modifica un antecedente m�rbido m�dico de los adicionales existente en la
	 * base de datos. Este antecedente m�rbido NO hace parte de los predefinidos
	 * en la base de datos.
	 * @param		Connection, con. Conexi�n abierta con la fuente de datos 
	 * @param 	String, codigoPaciente. c�digo en la fuente de datos del
	 * paciente.
	 * @param		int, codigo. C�digo del antecedente m�rbido en la bd.
	 * @param 	String, nombre. Nombre del antecedente m�rbido m�dico nuevo 
	 * 					que se va a ingresar en la base de datos, solo para este 
	 * 					paciente.
	 * @param 	String, fechaInicio. Fecha en la que se inici� el antecedente.
	 * 					En formato de fecha.
	 * @param 	String, tratamiento. Tratamiento con el cual se est� tratando.
	 * @param 	String, restriccionDietaria. Restricci�n dietaria que se tiene
	 * 					para manejar el antecedente.
	 * @param 	String, observaciones. Cadena con las observaciones escritas por
	 * 					el m�dico.
	 * @param		String, finalizar. Estado en el cual se encuentra la transacci�n, 
	 * 					"empezar", "finalizar" y "continuar". Los estados est�n 
	 * 					definidos en la interfaz de Constantes util.ConstantesBD.
	 * @return 		Resultado, retorna si la inserci�n fue realizada con exito
	 * 					"true" o no "false" y su descripci�n asociada en caso de 
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
	 * del antecedente m�rbido predefinido dado.
	 * @param 	Connection, con. Conexi�n con la base de datos
	 * @param 	String, codigoPaciente. c�digo en la fuente de datos del
	 * paciente.
	 * @param 	int, codigo. C�digo en la base de datos del antecedente
	 * 					m�rbido predefinido
	 * @return 		Resultado, retorna true si ya existia en la base de datos
	 * 					false de lo contrario con su descripci�n.
	 */
	public ResultadoBoolean existeAntecedenteOtro(	Connection con, 
																			int codigoPaciente, 
																			int codigo)
	{
		return SqlBaseAntecedenteMorbidoMedicoDao.existeAntecedenteOtro(con,codigoPaciente,codigo);
	}
	
}
