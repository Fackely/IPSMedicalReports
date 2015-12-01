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
	 * M�todo que b�sca las combinaciones secciones / campos
	 * para una funcionalidad y m�dico dados
	 * 
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param idFuncionalidad C�digo de la funcionalidad (donde se define
	 * a que p�gina / funcionalidad -ej. Evolucion )pertenecen los nuevos
	 * campos  
	 * @param codigoTipoIdentificacionMedico C�digo del tipo de identificaci�n 
	 * del M�dico que esta llenando los datos
	 * @param numeroIdentificacionMedico N�mero de identificaci�n del M�dico 
	 * que esta llenando los datos
	 * @param codigoCentroCosto C�digo del centro de costo
	 * @param codigoInstitucion C�digo de la instituci�n
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator buscarSeccionesYCamposDadaFuncionalidad (Connection con, int idFuncionalidad, int codigoMedico, int codigoCentroCosto, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que inserta el valor de un campo parametrizado llenado por el
	 * usuario en la fuente de datos. Si llega a fallar se debe comportar  
	 * transacci�n (ya sea por c�digo propio o por ser solo una actualizaci�n
	 * 
	 * @param con una conexi�n abierta con la fuente de datos
	 * @param codigoTabla C�digo que diferencia (ej. llave primaria)
	 * una entrada en tabla de la funcionalidad a la que se est�n 
	 * a�adiendo datos (Ej n�mero solicitud en evoluci�n)
	 * @param parametrizacionAsociada C�digo del campo a insertar
	 * dentro de la secci�n
	 * @param valor Valor llenado por el usuario en este campo a
	 * insertar
	 * @return
	 * @throws SQLException
	 */
	public int insertarLlenadoCampo (Connection con, int codigoTabla, int parametrizacionAsociada, String valor) throws SQLException;
	
	/**
	 * M�todo que inserta el valor de un campo parametrizado llenado por el
	 * usuario en la fuente de datos, soportando definir el estado de la transacci�n
	 * (empezar, continuar, finalizar)
	 * 
	 * @param con una conexi�n abierta con la fuente de datos
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
	 * M�todo que busca los campos parametrizados 
	 * previamente por un m�dico
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoMedico
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator buscarCamposParametrizadosPreviamente (Connection con, int codigoMedico, int institucion) throws SQLException;
	
	/**
	 * M�todo que inserta un campo parametrizable
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param orden Orden de este campo parametrizable
	 * @param codigoSeccion C�digo de la secci�n en la
	 * que se encuentra este campo parametrizable
	 * @param codigoTipo C�digo del tipo que maneja
	 * este campo parametrizable
	 * @param nombre Nombre de este campo parametrizable
	 * @param codigoMedico C�digo del m�dico que agrega
	 * este campo parametrizable
	 * @param activo Boolean que indica si este campo 
	 * parametrizable est� activo o no
	 * @param codigoCentroCosto C�digo del centro de 
	 * costo para este campo parametrizable
	 * @param codigoInstitucion C�digo de la instituci�n
	 * para este campo parametrizable
	 * @param codigoAlcance C�digo del alcance para este 
	 * campo parametrizable
	 * @return
	 * @throws SQLException
	 */
	public int insertarCampoParametrizado (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico, boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance) throws SQLException;
	
	/**
	 * M�todo que inserta un campo parametrizable. Maneja 
	 * la transacci�n aceptando un par�metro que define el 
	 * nivel de la transacci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param orden Orden de este campo parametrizable
	 * @param codigoSeccion C�digo de la secci�n en la
	 * que se encuentra este campo parametrizable
	 * @param codigoTipo C�digo del tipo que maneja
	 * este campo parametrizable
	 * @param nombre Nombre de este campo parametrizable
	 * @param codigoMedico C�digo del m�dico que agrega
	 * este campo parametrizable
	 * @param activo Boolean que indica si este campo 
	 * parametrizable est� activo o no
	 * @param codigoCentroCosto C�digo del centro de 
	 * costo para este campo parametrizable
	 * @param codigoInstitucion C�digo de la instituci�n
	 * para este campo parametrizable
	 * @param codigoAlcance C�digo del alcance para este 
	 * campo parametrizable
	 * @param estado Par�metro que permite definir el
	 * lugar dentro de una transacci�n en la que se 
	 * encuentra esta operaci�n
	 * @return
	 * @throws SQLException
	 */
	public int insertarCampoParametrizadoTransaccional (Connection con, int orden, int codigoSeccion, int codigoTipo, String nombre, int codigoMedico, boolean activo, int codigoCentroCosto, String codigoInstitucion, int codigoAlcance, String estado) throws SQLException;
	
	/**
	 * M�todo que actualiza un campo parametrizado en
	 * la fuente de datos. Maneja la transacci�n de forma
	 * at�mica
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param activo Boolean que indica si este campo se
	 * debe mostrar o no
	 * @param codigoAlcance Indica el alcance que puede 
	 * tener este campo (M�dico?, Centro Costo?, 
	 * Instituci�n?)
	 * @param codigo C�digo del campo parametrizado a 
	 * actualizar
	 * @return
	 * @throws SQLException
	 */
	public int actualizarCampoParametrizado (Connection con, boolean activo, int codigoAlcance, int codigo, int centroCosto) throws SQLException;

	/**
	 * M�todo que actualiza un campo parametrizado en
	 * la fuente de datos. Maneja la transacci�n aceptando
	 * un par�metro que define el nivel de la transacci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param activo Boolean que indica si este campo se
	 * debe mostrar o no
	 * @param codigoAlcance Indica el alcance que puede 
	 * tener este campo (M�dico?, Centro Costo?, 
	 * Instituci�n?)
	 * @param codigo C�digo del campo parametrizado a 
	 * actualizar
	 * @param estado Par�metro que permite definir el
	 * lugar dentro de una transacci�n en la que se 
	 * encuentra esta operaci�n
	 * @return
	 * @throws SQLException
	 */
	public int actualizarCampoParametrizadoTransaccional (Connection con, boolean activo, int codigo, int codigoAlcance, int centroCosto, String estado) throws SQLException;
	
}
