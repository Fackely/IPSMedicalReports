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
	 * Método que maneja la inserción de un hijo en antecedentes gineco-
	 * obstetricos
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente a la que se le va a ingresar
	 * información de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * niño nació en el tercer embarazo de la señora)
	 * @param numeroHijo Número del hijo dentro de este embarazo (Ej. niña nació
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el niño nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican características del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el niño nació por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican características del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int Número de Hijos Insertados (1 Inserción exitosa, 0 problemas
	 * inserción)
	 * @throws SQLException
	 */
	public int insertar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException;

	/**
	 * Método que maneja la inserción de un hijo en antecedentes gineco-
	 * obstetricos y permite definir el nivel de Transaccionalidad (Definiendo
	 * parámetro estado)
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente a la que se le va a ingresar
	 * información de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * niño nació en el tercer embarazo de la señora)
	 * @param numeroHijo Número del hijo dentro de este embarazo (Ej. niña nació
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el niño nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican características del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el niño nació por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican características del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int Número de Hijos Insertados (1 Inserción exitosa, 0 problemas
	 * inserción)
	 * @throws SQLException
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException;

	/**
	 * Método que maneja la modificación de un hijo en antecedentes gineco-
	 * obstetricos
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente a la que se le va a
	 * modificar información de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * niño nació en el tercer embarazo de la señora)
	 * @param numeroHijo Número del hijo dentro de este embarazo (Ej. niña nació
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el niño nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican características del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el niño nació por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican características del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int Número de Hijos Modificados (1 Modificación exitosa, 0
	 * problemas modificación)
	 * @throws SQLException
	 */
	public int modificar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException;
	
	/**
	 * Método que maneja la modificación de un hijo en antecedentes gineco-
	 * obstetricos y permite definir el nivel de Transaccionalidad (Definiendo
	 * parámetro estado)
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente a la que se le va a
	 * modificar información de un hijo (antecedentes gineco-obstetricos)
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * niño nació en el tercer embarazo de la señora)
	 * @param numeroHijo Número del hijo dentro de este embarazo (Ej. niña nació
	 * de segunda en un embarazo de mellizos)
	 * @param vivo boolean que dice si el niño nacio vivo o muerto 
	 * @param otroTipoPartoVaginal texto donde se explican características del
	 * tipo de parto vaginal que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param cesarea boolean que dice si el niño nació por cesarea o no
	 * @param aborto boolean que dice si la mama tuvo un aborto o no
	 * @param otroTipoParto texto donde se explican características del
	 * tipo de parto que no coinciden con las disponibles en la tabla de
	 * referencia
	 * @param tiposPartoVaginal ArrayList con todos los tipos de parto vaginal
  	 * que aplican a este usuario
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @return int Número de Hijos Modificados (1 Modificación exitosa, 0
	 * problemas modificación)
	 * @throws SQLException
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException;

	/**
	 * Método que carga todos los tipos de parto vaginal que apliquen a un niño
	 * en particular (sobre todos sus hermanos, si nacieron varios)
	 * 
	 * @param codigoPaciente, código de la paciente a la que se le va a
	 * cargar los tipos de parto vaginal que apliquen a un hijo particular
	 * (antecedentes gineco- obstetricos)
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * niño nació en el tercer embarazo de la señora)
	 * @param numeroHijo Número del hijo dentro de este embarazo (Ej. niña nació
	 * de segunda en un embarazo de mellizos)
	 * @return ResultSetDecorator Vacio si este niño no tuvo ningún tipo de parto vaginal
	 * en particular o la lista de tipos de parto vaginales que apliquen
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarTiposPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException;
	
	/**
	 * Método que permite averiguar si un hijo particular (Para una paciente y
	 * un embarazo particular) existe
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente, código de la paciente madre del niño al
	 * cual se va a buscar
	 * @param numeroEmbarazo Número del embarazo en el que nacio este hijo (Ej.
	 * niño nació en el tercer embarazo de la señora)
	 * @param numeroHijo Número del hijo dentro de este embarazo (Ej. niña nació
	 * de segunda en un embarazo de mellizos)
	 * @return boolean true si existe el hijo especificado, false si no
	 * @throws SQLException
	 */
	public boolean existeHijo (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException;
}
