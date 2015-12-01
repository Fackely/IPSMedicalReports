/*
 * @(#)InformacionParametrizableDao.java
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

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Egreso</code>.
 * 
 * @version 1.0, Oct 29, 2003
 */
public interface InformacionParametrizableDao
{
	
	/**
	 * Método que búsca las combinaciones secciones / campos
	 * para una funcionalidad y médico dados
	 * 
	 * @param con una conexión abierta con la fuente de datos
	 * @param idFuncionalidad Código de la funcionalidad (donde se define
	 * a que página / funcionalidad -ej. Evolucion )pertenecen los nuevos
	 * campos  
	 * @param codigoTipoIdentificacionMedico Código del tipo de identificación 
	 * del Médico que esta llenando los datos
	 * @param numeroIdentificacionMedico Número de identificación del Médico 
	 * que esta llenando los datos
	 * @param codigoCentroCosto Código del centro de costo
	 * @param codigoInstitucion Código de la institución
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator buscarSeccionesYCamposDadaFuncionalidad (Connection con, int idFuncionalidad, int codigoMedico, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que inserta el valor de un campo parametrizado llenado por el
	 * usuario en la fuente de datos. Si llega a fallar se debe comportar  
	 * transacción (ya sea por código propio o por ser solo una actualización
	 * 
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigoTabla Código que diferencia (ej. llave primaria)
	 * una entrada en tabla de la funcionalidad a la que se están 
	 * añadiendo datos (Ej número solicitud en evolución)
	 * @param parametrizacionAsociada Código del campo a insertar
	 * dentro de la sección
	 * @param valor Valor llenado por el usuario en este campo a
	 * insertar
	 * @return
	 * @throws SQLException
	 */
	public int insertarLlenadoCampo (Connection con, int codigoTabla, int parametrizacionAsociada, String valor) throws SQLException;
	
	/**
	 * Método que inserta el valor de un campo parametrizado llenado por el
	 * usuario en la fuente de datos, soportando definir el estado de la transacción
	 * (empezar, continuar, finalizar)
	 * 
	 * @param con una conexión abierta con la fuente de datos
	 * @param codigoTabla
	 * @param ordenParametrizacion
	 * @param seccionParametrizacion
	 * @param valor
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int insertarLlenadoCampoTransaccional (Connection con, int codigoTabla, int parametrizacionAsociada, String valor, String estado) throws SQLException;
	
	/**
	 * Método que busca los campos parametrizados 
	 * previamente por un médico
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoMedico
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator buscarCamposParametrizadosPreviamente (Connection con, int codigoMedico, int institucion) throws SQLException;
	
	/**
	 * Método que inserta un campo parametrizable
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param orden Orden de este campo parametrizable
	 * @param codigoSeccion Código de la sección en la
	 * que se encuentra este campo parametrizable
	 * @param codigoTipo Código del tipo que maneja
	 * este campo parametrizable
	 * @param nombre Nombre de este campo parametrizable
	 * @param codigoMedico Código del médico que agrega
	 * este campo parametrizable
	 * @param activo Boolean que indica si este campo 
	 * parametrizable está activo o no
	 * @param codigoCentroCosto Código del centro de 
	 * costo para este campo parametrizable
	 * @param codigoInstitucion Código de la institución
	 * para este campo parametrizable
	 * @param codigoAlcance Código del alcance para este 
	 * campo parametrizable
	 * @return
	 * @throws SQLException
	 */
	public int insertarCampoParametrizado (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico, boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance) throws SQLException;
	
	/**
	 * Método que inserta un campo parametrizable. Maneja 
	 * la transacción aceptando un parámetro que define el 
	 * nivel de la transacción
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param orden Orden de este campo parametrizable
	 * @param codigoSeccion Código de la sección en la
	 * que se encuentra este campo parametrizable
	 * @param codigoTipo Código del tipo que maneja
	 * este campo parametrizable
	 * @param nombre Nombre de este campo parametrizable
	 * @param codigoMedico Código del médico que agrega
	 * este campo parametrizable
	 * @param activo Boolean que indica si este campo 
	 * parametrizable está activo o no
	 * @param codigoCentroCosto Código del centro de 
	 * costo para este campo parametrizable
	 * @param codigoInstitucion Código de la institución
	 * para este campo parametrizable
	 * @param codigoAlcance Código del alcance para este 
	 * campo parametrizable
	 * @param estado Parámetro que permite definir el
	 * lugar dentro de una transacción en la que se 
	 * encuentra esta operación
	 * @return
	 * @throws SQLException
	 */
	public int insertarCampoParametrizadoTransaccional (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico, boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance, String estado) throws SQLException;
	
	/**
	 * Método que actualiza un campo parametrizado en
	 * la fuente de datos. Maneja la transacción de forma
	 * atómica
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param activo Boolean que indica si este campo se
	 * debe mostrar o no
	 * @param codigoAlcance Indica el alcance que puede 
	 * tener este campo (Médico?, Centro Costo?, 
	 * Institución?)
	 * @param codigo Código del campo parametrizado a 
	 * actualizar
	 * @return
	 * @throws SQLException
	 */
	public int actualizarCampoParametrizado (Connection con, boolean activo, int codigoAlcance, int codigo, int centroCosto) throws SQLException;

	/**
	 * Método que actualiza un campo parametrizado en
	 * la fuente de datos. Maneja la transacción aceptando
	 * un parámetro que define el nivel de la transacción
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param activo Boolean que indica si este campo se
	 * debe mostrar o no
	 * @param codigoAlcance Indica el alcance que puede 
	 * tener este campo (Médico?, Centro Costo?, 
	 * Institución?)
	 * @param codigo Código del campo parametrizado a 
	 * actualizar
	 * @param estado Parámetro que permite definir el
	 * lugar dentro de una transacción en la que se 
	 * encuentra esta operación
	 * @return
	 * @throws SQLException
	 */
	public int actualizarCampoParametrizadoTransaccional (Connection con, boolean activo, int codigo, int codigoAlcance, int centroCosto, String estado) throws SQLException;
	
}
