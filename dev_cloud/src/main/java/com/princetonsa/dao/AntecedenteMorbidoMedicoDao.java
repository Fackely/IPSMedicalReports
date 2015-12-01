/*
 * @(#)AntecedenteMorbidoMedicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;

import util.ResultadoBoolean;


/**
 * Interfaz para acceder a la fuente de datos, la parte de Antecedentes M�rbidos
 * M�dicos, predefinidos y otros
 *
 * @version 1.0, Agosto 12, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */

public interface AntecedenteMorbidoMedicoDao
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
	public ResultadoBoolean insertarPredefinidoTransaccional (Connection con, int codigoPaciente, int codigo, String fechaInicio, String tratamiento, String restriccionDietaria, String observaciones, String estado) throws Exception;

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
	 * @return 		Resultado, retorna si la inserci�n fue realizada con exito
	 * 					"true" o no "false" y su descripci�n asociada en caso de 
	 * 					ser necesaria.
	 */		
	public ResultadoBoolean insertarOtroTransaccional(Connection con, int codigoPaciente, int codigo, String nombre, String fechaInicio, String tratamiento, String restriccionDietaria, String observaciones, String estado ) throws Exception;

	
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
	 * @return 		booelan, retorna si la inserci�n fue realizada con exito
	 * 					"true" o no "false"
	 * @see com.princetonsa.dao.AntecedenteMorbidoMedicoDao#insertarPredefinido
	 * (java.sql.Connection, java.lang.String, java.lang.String, int, java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarPredefinidoTransaccional(	Connection con, int codigoPaciente, int codigo, String fechaInicio, String tratamiento, String restriccionDietaria, String observaciones, String estado ) throws Exception;
	
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
	public ResultadoBoolean existeAntecedentePredefinido(Connection con, int codigoPaciente, int codigo);
	
	
	/**
	 * Modifica un antecedente m�rbido m�dico de los adicionales existente en la
	 * base de datos. Este antecedente m�rbido NO hace parte de los predefinidos
	 * en la base de datos. Esta modificaci�n hace parte de una transacci�n
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
	public ResultadoBoolean modificarOtroTransaccional(Connection con, int codigoPaciente, int codigo, String nombre, String fechaInicio, String tratamiento, String restriccionDietaria, String observaciones, String estado ) throws Exception;
	
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
	public ResultadoBoolean existeAntecedenteOtro(Connection con, int codigoPaciente, int codigo);
}
