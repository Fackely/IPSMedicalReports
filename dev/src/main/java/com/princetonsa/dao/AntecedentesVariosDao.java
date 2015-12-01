/*
 * @(#)AntecedentesVariosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AntecedentesVarios</code>.
 *
 * @version 1.0, Apr 15, 2003
 * @author <a href="mailto:diego@PrincetonSA.com">Diego Andrés Ramírez Aragón</a>,
 */
public interface AntecedentesVariosDao {

	/**
	 * Permite insertar en la base de datos un nuevo Antecedente vario
	 * @param con Una conexi&oacute;n abierta con una fuente de datos
	 * @param tipo Codigo del tipo de antecedente que se va a insertar 
	 * @param descripcion Descripci&oacute;n del antecente
	 * @param login Login de la persona que esta agregando el antecedente
	 * @return El codigo del antecedente actual
	 * @throws SQLException 
	 */
	public int insertar (Connection con, int tipo,String descripcion, String login ,PersonaBasica paciente) throws SQLException;
	
	/**
	 * Permite modificar los datos del antedecente especificado
	 * @param con Una conexi&oacute;n abierta con una fuente de datos
	 * @param idAntecedente Identificador del antecedente vario que se quiere modificar 
	 * @param descripcion Nueva descripci&oacute;n del antecedente vario
	 * @param usuario Usuario que hace la modificacion
	 * @return 1 Si la modificacion fue llevada a cabo
	 * @throws SQLException
	 */
	public int modificar (Connection con,int idAntecedente,String descripcion,String usuario,PersonaBasica paciente) throws SQLException;
	
	/**
	 * Permite cargar los datos de un antecedente vario 
	 * @param con Una conexi&oacute;n abierta con una fuente de datos
	 * @param idAntecedente Identificador del antedecente que se quiere cargar
	 * @return Un ResultSetDecorator con los datos del antedente vario
	 * @throws SQLException
	 */
	public ResultSetDecorator cargar (Connection con, int idAntecedente) throws SQLException;		
}
