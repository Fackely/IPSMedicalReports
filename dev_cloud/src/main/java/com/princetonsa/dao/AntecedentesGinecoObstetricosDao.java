/*
 * @(#)AntecedentesGinecoObstetricosDao.java
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AntecedenteGinecoObstetrico</code>.
 *
 * @version 1.0, Apr 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
@SuppressWarnings("rawtypes")
public interface AntecedentesGinecoObstetricosDao 
{
	/**
	 * Inserta el AntecedenteGinecoObstetrico de un paciente en la fuente de
	 * datos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, codigo del paciente en la fuente de datos
	 * @param codigoEdadMenarquia C�digo que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param codigoEdadMenopausia C�digo que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los m�todos
	 * anticonceptivos usados y su descripci�n
	 * @return int N�mero de Antecedentes Insertados (1 Inserci�n exitosa, 0
	 * problemas inserci�n)
	 * @throws SQLException
	 */	
	public int insertar (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos) throws SQLException;
	
	/**
	 * Inserta el AntecedenteGinecoObstetrico de un paciente en la fuente de
	 * datos; permite definir el nivel de Transaccionalidad (Definiendo
	 * par�metro estado)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo del paciente a la que se le va a ingresar
	 * un antecedente GinecoObstetrico
	 * @param codigoEdadMenarquia C�digo que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param codigoEdadMenopausia C�digo que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los m�todos
	 * anticonceptivos usados y su descripci�n
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int N�mero de Antecedentes Insertados (1 Inserci�n exitosa, 0
	 * problemas inserci�n)
	 * @throws SQLException
	 */	
	public int insertarTransaccional (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos, String estado) throws SQLException;

	/**
	 * Met�do para modificar el antecedente gineco obstetrico de un paciente en
	 * la fuente de datos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente a la que se le va a
	 * modificar un antecedente GinecoObstetrico
	 * @param codigoEdadMenarquia C�digo que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param codigoEdadMenopausia C�digo que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso modifico este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los m�todos
	 * anticonceptivos usados y su descripci�n
	 * @return int N�mero de Antecedentes Modificados (1 modificaci�n exitosa, 0
	 * problemas modificaci�n)
	 * @throws SQLException
	 */
	public int modificar (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos) throws SQLException;
	
	/**
	 * 
	 * Met�do para modificar el antecedente gineco obstetrico de un paciente en
	 * la fuente de datos; permite definir el nivel de Transaccionalidad
	 * (Definiendo par�metro estado)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, codigo de la paciente a la que se le va a
	 * modificar un antecedente GinecoObstetrico
	 * @param codigoEdadMenarquia C�digo que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param codigoEdadMenopausia C�digo que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el c�digo corresponde al rango "otros", aqui
	 * se guarda la informaci�n
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso modifico este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los m�todos
	 * anticonceptivos usados y su descripci�n
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int N�mero de Antecedentes Modificados (1 modificaci�n exitosa, 0
	 * problemas modificaci�n)
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos, String estado) throws SQLException;

	/**
	 * M�todo que saca en un ResultSetDecorator la informaci�n de un antecedente
	 * ginecoobstetrico, (solo la informaci�n espec�fica de este antecedente,
	 * nada de los embarazos ni de los historicos)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente. c�digo de la paciente a la que se le va a cargar
	 * su antecedente GinecoObstetrico
	 * @return ResultSetDecorator con la informaci�n de este antecedente ginecoobstetrico
	 * @throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * Metodo para cargar Resumen de Atenciones.
	 * @param con
	 * @param mapa
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, HashMap mapa) throws SQLException;
	
	/**
	 * M�todo que saca en un ResultSetDecorator la informaci�n de todos los antecedentes
	 * ginecoobstetricos historicos
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente. c�digo de la paciente a la que se le van a cargar
	 * sus antecedente GinecoObstetrico Historicos
	 * @param numeroSolicitud
	 * 
	 * @return ResultSetDecorator  con la informaci�n de todos los antecedentes
	 * ginecoobstetricos historicos
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarHistorico (Connection con, int codigoPaciente, int numeroSolicitud) throws SQLException;
	
	/**
	 * M�todo que saca en un ResultSetDecorator la informaci�n de todos los embarazos de
	 * la paciente especificada
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente a la que se le van a cargar
	 * sus embarazos
	 * @return ResultSetDecorator con la informaci�n de todos los embarazos de esta
	 * paciente
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEmbarazos (Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo del paciente a la que se le van a cargar
	 * los m�todos anticonceptivos
	 * @return ResultSetDecorator con la informaci�n de todos los m�todos anticonceptivos
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMetodosAnticonceptivos (Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 *	M�todo	 que revisa si existe un antecedente para un paciente en
	 *particular
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo del paciente a la que se va a revisar si
	 * tiene antecedentes GinecoObstetrico
	 * @return boolean Retorna true si existen antecedentes GinecoObstetricos
	 * para esta paciente, false de lo contrario
	 * @throws SQLException
	 */
	public boolean existeAntecedente (Connection con, int codigoPaciente) throws SQLException;
	
	public ResultSetDecorator cargarUltInformacionEmbarazos (Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * Implementaci�n de cargar complicaciones de los embarazos 
	 * para una BD Gen�rica
	 */
	public Collection cargarComplicaciones(Connection con, int codigoPaciente, int codigoEmbarazo);
	
	/**
	 * Implementaci�n de cargar complicaciones otras de los embarazos 
	 * para una BD Gen�rica
	 */
	public Collection cargarComplicacionesOtras(Connection con, int codigoPaciente, int codigoEmbarazo);

	/**
	 * M�todo que inserta los datos propios de la valoraci�n gineco-obstetrica
	 * @param con
	 * @param edadMenarquia
	 * @param edadMenopausia
	 * @param otraEdadMenarquia
	 * @param otraEdadMenopausia
	 * @param cilcoMenstrual
	 * @param duracionMenstruacion
	 * @param furAnt
	 * @param dolorMenstruacion
	 * @param conceptoMenstruacion
	 * @param observacionesMenstruacion
	 * @param codigoPersona
	 * @param numeroSolicitud
	 */
	public int inseretarDatosHistMenstrual(Connection con, int edadMenarquia, int edadMenopausia, String otraEdadMenarquia, String otraEdadMenopausia, String cilcoMenstrual, String duracionMenstruacion, String furAnt, String dolorMenstruacion, int conceptoMenstruacion, String observacionesMenstruacion, int codigoPersona, int numeroSolicitud);

	/**
	 * Para resumen de Atenciones.
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarHistorico (Connection con, HashMap mapa) throws SQLException;
	
	/**
	 * Para resumen de Atenciones.
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEmbarazos (Connection con, HashMap mapa) throws SQLException;
	
	/**
	 * Para Resumen de Atenciones 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return
	 */
	public Collection cargarComplicaciones(Connection con, HashMap mapa);
	
	/**
	 * Para Resumen de Atenciones.
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return
	 */
	public Collection cargarComplicacionesOtras(Connection con,  HashMap mapa);

	/**
	 * Para historia de Anteciones 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMetodosAnticonceptivos (Connection con, HashMap mapa) throws SQLException;
		

}