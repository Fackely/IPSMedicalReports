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
	 * @param codigoEdadMenarquia Código que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param codigoEdadMenopausia Código que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los métodos
	 * anticonceptivos usados y su descripción
	 * @return int Número de Antecedentes Insertados (1 Inserción exitosa, 0
	 * problemas inserción)
	 * @throws SQLException
	 */	
	public int insertar (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos) throws SQLException;
	
	/**
	 * Inserta el AntecedenteGinecoObstetrico de un paciente en la fuente de
	 * datos; permite definir el nivel de Transaccionalidad (Definiendo
	 * parámetro estado)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código del paciente a la que se le va a ingresar
	 * un antecedente GinecoObstetrico
	 * @param codigoEdadMenarquia Código que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param codigoEdadMenopausia Código que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los métodos
	 * anticonceptivos usados y su descripción
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int Número de Antecedentes Insertados (1 Inserción exitosa, 0
	 * problemas inserción)
	 * @throws SQLException
	 */	
	public int insertarTransaccional (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos, String estado) throws SQLException;

	/**
	 * Metódo para modificar el antecedente gineco obstetrico de un paciente en
	 * la fuente de datos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente a la que se le va a
	 * modificar un antecedente GinecoObstetrico
	 * @param codigoEdadMenarquia Código que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param codigoEdadMenopausia Código que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso modifico este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los métodos
	 * anticonceptivos usados y su descripción
	 * @return int Número de Antecedentes Modificados (1 modificación exitosa, 0
	 * problemas modificación)
	 * @throws SQLException
	 */
	public int modificar (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos) throws SQLException;
	
	/**
	 * 
	 * Metódo para modificar el antecedente gineco obstetrico de un paciente en
	 * la fuente de datos; permite definir el nivel de Transaccionalidad
	 * (Definiendo parámetro estado)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, codigo de la paciente a la que se le va a
	 * modificar un antecedente GinecoObstetrico
	 * @param codigoEdadMenarquia Código que corresponde al rango de edad de
	 * menarquia de esta paciente
	 * @param otraEdadMenarquia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param codigoEdadMenopausia Código que corresponde al rango de edad de
	 * menopausia de esta paciente
	 * @param otraEdadMenopausia Si el código corresponde al rango "otros", aqui
	 * se guarda la información
	 * @param observaciones Observaciones generales sobre este Antecedente
	 * @param loginUsuario Login del usuario que puso modifico este antecedente
	 * GinecoObstetrico
	 * @param metodosAnticonceptivos ArrayList con un listado de los métodos
	 * anticonceptivos usados y su descripción
  	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return int Número de Antecedentes Modificados (1 modificación exitosa, 0
	 * problemas modificación)
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos, String estado) throws SQLException;

	/**
	 * Método que saca en un ResultSetDecorator la información de un antecedente
	 * ginecoobstetrico, (solo la información específica de este antecedente,
	 * nada de los embarazos ni de los historicos)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente. código de la paciente a la que se le va a cargar
	 * su antecedente GinecoObstetrico
	 * @return ResultSetDecorator con la información de este antecedente ginecoobstetrico
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
	 * Método que saca en un ResultSetDecorator la información de todos los antecedentes
	 * ginecoobstetricos historicos
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente. código de la paciente a la que se le van a cargar
	 * sus antecedente GinecoObstetrico Historicos
	 * @param numeroSolicitud
	 * 
	 * @return ResultSetDecorator  con la información de todos los antecedentes
	 * ginecoobstetricos historicos
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarHistorico (Connection con, int codigoPaciente, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método que saca en un ResultSetDecorator la información de todos los embarazos de
	 * la paciente especificada
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente a la que se le van a cargar
	 * sus embarazos
	 * @return ResultSetDecorator con la información de todos los embarazos de esta
	 * paciente
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEmbarazos (Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código del paciente a la que se le van a cargar
	 * los métodos anticonceptivos
	 * @return ResultSetDecorator con la información de todos los métodos anticonceptivos
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMetodosAnticonceptivos (Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 *	Método	 que revisa si existe un antecedente para un paciente en
	 *particular
	 *  
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código del paciente a la que se va a revisar si
	 * tiene antecedentes GinecoObstetrico
	 * @return boolean Retorna true si existen antecedentes GinecoObstetricos
	 * para esta paciente, false de lo contrario
	 * @throws SQLException
	 */
	public boolean existeAntecedente (Connection con, int codigoPaciente) throws SQLException;
	
	public ResultSetDecorator cargarUltInformacionEmbarazos (Connection con, int codigoPaciente) throws SQLException;
	
	/**
	 * Implementación de cargar complicaciones de los embarazos 
	 * para una BD Genérica
	 */
	public Collection cargarComplicaciones(Connection con, int codigoPaciente, int codigoEmbarazo);
	
	/**
	 * Implementación de cargar complicaciones otras de los embarazos 
	 * para una BD Genérica
	 */
	public Collection cargarComplicacionesOtras(Connection con, int codigoPaciente, int codigoEmbarazo);

	/**
	 * Método que inserta los datos propios de la valoración gineco-obstetrica
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