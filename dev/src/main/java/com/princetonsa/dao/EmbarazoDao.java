/*
 * @(#)EmbarazoDao.java
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
import java.util.Vector;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Embarazo</code>.
 *
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public interface EmbarazoDao 
{
	/*
	/**
	 * Inserta los datos del embarazo para esta paciente (En antecedentes
	 * GinecoObstetricos)
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente codigo de la paciente a la que se le va a ingresar
	 * un antecedente GinecoObstetrico perteneciente a un embarazo particular
	 * @param mesesGestacion Meses que tardo este embarazo (Fecha Inicio a Fecha
	 * Parto)
	 * @param fechaTerminacion Fecha en la que ocurri� el parto
	 * @param codigoComplicacion C�digo de una complicaci�n del embarazo 
	 * @param otraComplicacion Texto escrito por el m�dico si encontr� que la
	 * complicaci�n 
	 * @return int Retorna el n�mero del embarazo
	 * @throws SQLException
	 *
	public int insertar (Connection con, int codigoPaciente, float mesesGestacion, String fechaTerminacion, int codigoComplicacion, String otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto) throws SQLException;
	*/
	
	/**
	 * Inserta los datos del embarazo para esta paciente (En antecedentes
	 * GinecoObstetricos) y permite definir el estado de la transacci�n con el
	 * par�metro estado
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente codigo de la paciente a la que se le va a ingresar
	 * un antecedente GinecoObstetrico perteneciente a un embarazo particular
	 * @param mesesGestacion Meses que tardo este embarazo (Fecha Inicio a Fecha
	 * Parto)
	 * @param fechaTerminacion Fecha en la que ocurri� el parto
	 * @param complicaciones C�digo de una complicaci�n del embarazo 
	 * @param otraComplicacion Texto escrito por el m�dico si encontr� que la
	 * complicaci�n 
	 * @param duracion
	 * @param tiempoRupturaMembranas
	 * @param legrado
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * 
	 * @return int Retorna el n�mero del embarazo
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, float mesesGestacion, String fechaTerminacion, int[] complicaciones, Vector otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto, String duracion, String tiempoRupturaMembranas, String legrado, String estado) throws SQLException;
	
	/*
	/**
	 * Modifica los datos del embarazo para esta paciente (En antecedentes
	 * GinecoObstetricos) 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente codigo de la paciente a la que se le va a modificar
	 * un antecedente GinecoObstetrico perteneciente a un embarazo particular
	 * @param codigoEmbarazo C�digo que identifica este embarazo de cualquier
	 * otro que haya tenido esta paciente
	 * @param mesesGestacion Meses que tardo este embarazo (Fecha Inicio a
	 * Fecha Parto)
	 * @param fechaTerminacion Fecha en la que ocurri� el parto
	 * @param codigoComplicacion C�digo de una complicaci�n del embarazo 
	 * @param otraComplicacion Texto escrito por el m�dico si encontr� que la
	 * complicaci�n 
	 * @return int
	 * @throws SQLException
	 */
	//public int modificar (Connection con, int codigoPaciente, int codigoEmbarazo, float mesesGestacion, String fechaTerminacion, int codigoComplicacion, String otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto) throws SQLException;
	
	/**
	 * Modifica los datos del embarazo para esta paciente (En antecedentes
	 * GinecoObstetricos) y permite definir el estado de la transacci�n con el
	 * par�metro estado
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente c�digo en la fuente de datos de la paciente a la
	 * que se le va a modificar un antecedente GinecoObstetrico perteneciente a
	 * un embarazo particular
	 * @param codigoEmbarazo C�digo que identifica este embarazo de cualquier
	 * otro que haya tenido esta paciente
	 * @param mesesGestacion Meses que tardo este embarazo (Fecha Inicio a
	 * Fecha Parto)
	 * @param fechaTerminacion Fecha en la que ocurri� el parto
	 * @param complicacion C�digo de una complicaci�n del embarazo 
	 * @param otraComplicacion Texto escrito por el m�dico si encontr� que la
	 * complicaci�n 
	 * @param duracion
	 * @param tiempoRupturaMembranas
	 * @param legrado
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * 
	 * @return int
	 * @throws SQLException
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int codigoEmbarazo, float mesesGestacion, String fechaTerminacion, int[] complicacion, Vector otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto, String duracion, String tiempoRupturaMembranas, String legrado, String estado) throws SQLException;

	/**
	 * M�todo que devuelve en un ResultSetDecorator la informaci�n de los hijos que
	 * existen en este embarazo
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente c�digo en la fuente de datos de la paciente a la que se le van a
	 * cargar los hijos en un antecedente GinecoObstetrico perteneciente a un
	 * embarazo particular
	 * @param numeroEmbarazo Embarazo sobre el cual se quieren cargar los hijos
	 * @return ResultSetDecorator Un ResultSetDecorator vacio si ese embarazo no tiene hijos ?? 
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarHijos (Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException;

	/**
	 * M�todo que revisa si un embarazo particular ya fue ingresado
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente c�digo en la fuente de datos de la paciente a la cual se le va
	 * a buscar un embarazo
	 * @param numeroId N�mero de Identificaci�n de la paciente a la cual se le va
	 * a buscar un embarazo
	 * @param numeroEmbarazo N�mero del embarazo que se esta buscando (Entre
	 * todos los que ha tenido la paciente)
	 * @return boolean Retorna un boolean con true si existe este embarazo o
	 * false si no.
	 * @throws SQLException
	 */
	public boolean existeEmbarazo(Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException;
	
	/**
	 * M�todo para ingresar complicaciones
	 * @param con
	 * @param secuencia
	 * @param codigoEmbarazo
	 * @param codigoPaciente
	 * @param complicaciones
	 * @param complicacionesOtras
	 * @return numero de inserciones hechas en la BD
	 */
	public int ingresarComplicaciones(Connection con, int codigoEmbarazo, int codigoPaciente, int[] complicaciones, Vector complicacionesOtras);

}
