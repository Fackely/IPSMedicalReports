/*
 * @(#)HijoBasicoDao.java
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
import java.util.ArrayList;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>HijoBasico</code>.
 *
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public interface HijoBasicoDao 
{
	/**
	 * M�todo que maneja la inserci�n de un hijo en antecedentes gineco-
	 * obstetricos
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente a la que se le va a ingresar
	 * informaci�n de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo N�mero del embarazo en el que nacio este hijo (Ej.
	 * ni�o naci� en el tercer embarazo de la se�ora)
	 * @param numeroHijo N�mero del hijo dentro de este embarazo (Ej. ni�a naci�
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el ni�o nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican caracter�sticas del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el ni�o naci� por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican caracter�sticas del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int N�mero de Hijos Insertados (1 Inserci�n exitosa, 0 problemas
	 * inserci�n)
	 * @throws SQLException
	 */
	public int insertar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException;

	/**
	 * M�todo que maneja la inserci�n de un hijo en antecedentes gineco-
	 * obstetricos y permite definir el nivel de Transaccionalidad (Definiendo
	 * par�metro estado)
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente a la que se le va a ingresar
	 * informaci�n de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo N�mero del embarazo en el que nacio este hijo (Ej.
	 * ni�o naci� en el tercer embarazo de la se�ora)
	 * @param numeroHijo N�mero del hijo dentro de este embarazo (Ej. ni�a naci�
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el ni�o nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican caracter�sticas del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el ni�o naci� por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican caracter�sticas del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int N�mero de Hijos Insertados (1 Inserci�n exitosa, 0 problemas
	 * inserci�n)
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException;

	/**
	 * M�todo que maneja la modificaci�n de un hijo en antecedentes gineco-
	 * obstetricos
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente a la que se le va a
	 * modificar informaci�n de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo N�mero del embarazo en el que nacio este hijo (Ej.
	 * ni�o naci� en el tercer embarazo de la se�ora)
	 * @param numeroHijo N�mero del hijo dentro de este embarazo (Ej. ni�a naci�
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el ni�o nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican caracter�sticas del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el ni�o naci� por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican caracter�sticas del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int N�mero de Hijos Modificados (1 Modificaci�n exitosa, 0
	 * problemas modificaci�n)
	 * @throws SQLException
	 */
	public int modificar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException;
	
	/**
	 * M�todo que maneja la modificaci�n de un hijo en antecedentes gineco-
	 * obstetricos y permite definir el nivel de Transaccionalidad (Definiendo
	 * par�metro estado)
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente a la que se le va a
	 * modificar informaci�n de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo N�mero del embarazo en el que nacio este hijo (Ej.
	 * ni�o naci� en el tercer embarazo de la se�ora)
	 * @param numeroHijo N�mero del hijo dentro de este embarazo (Ej. ni�a naci�
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el ni�o nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican caracter�sticas del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el ni�o naci� por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican caracter�sticas del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int N�mero de Hijos Modificados (1 Modificaci�n exitosa, 0
	 * problemas modificaci�n)
	 * @throws SQLException
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException;

	/**
	 * M�todo que carga todos los tipos de parto vaginal que apliquen a un ni�o
	 * en particular (sobre todos sus hermanos, si nacieron varios)
	 * 
	 * @param codigoPaciente, c�digo de la paciente a la que se le va a
	 * cargar los tipos de parto vaginal que apliquen a un hijo particular
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo N�mero del embarazo en el que nacio este hijo (Ej.
	 * ni�o naci� en el tercer embarazo de la se�ora)
	 * @param numeroHijo N�mero del hijo dentro de este embarazo (Ej. ni�a naci�
	 * de segunda en un embarazo de mellizos)
	 * @return ResultSetDecorator Vacio si este ni�o no tuvo ning�n tipo de parto vaginal
	 * en particular o la lista de tipos de parto vaginales que apliquen
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarTiposPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException;
	
	/**
	 * M�todo que permite averiguar si un hijo particular (Para una paciente y
	 * un embarazo particular) existe
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, c�digo de la paciente madre del ni�o al
	 * cual se va a buscar
	 * @param numeroEmbarazo N�mero del embarazo en el que nacio este hijo (Ej.
	 * ni�o naci� en el tercer embarazo de la se�ora)
	 * @param numeroHijo N�mero del hijo dentro de este embarazo (Ej. ni�a naci�
	 * de segunda en un embarazo de mellizos)
	 * @return boolean true si existe el hijo especificado, false si no
	 * @throws SQLException
	 */
	public boolean existeHijo (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException;
}
