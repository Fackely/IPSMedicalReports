/*
 * Creado el 27/04/2006
 * Juan David Ramírez López
 */
package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.InfoDatosInt;


public interface ParamInterfazDao
{
	/**
	 * Método que consulta los campos parametrizados en la funcionalidad Parametrización Campos Interfaz
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarCamposInterfaz(Connection con, int institucion);

	/**
	 * Método para consultar las opciones de los selectores de la funcionalidad
	 * @param con
	 * @param numeroConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int numeroConsulta);
	
	/**
	 * Método para ingresar la parametrización del registro de interfaz
	 * @param con
	 * @param tipoInterfaz
	 * @param descripcion
	 * @param login
	 * @param tipoRegistro
	 * @param codigoModificacion @todo
	 * @return
	 */
	public int ingresarRegistroInterfaz(Connection con, int tipoInterfaz, String descripcion, String login, int tipoRegistro, int codigoModificacion);

	/**
	 * Método que ingresa el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistroInterfaz
	 * @param campoRegistroInterfaz
	 * @param nombre
	 * @param valor
	 * @param valorSecundario
	 * @param indicativoExiste
	 * @param tamanio
	 * @return
	 */
	public int ingresarDetalleRegistroInterfaz(Connection con, int codigoRegistroInterfaz, int campoRegistroInterfaz, String nombre, InfoDatosInt valor, InfoDatosInt valorSecundario, InfoDatosInt indicativoExiste, InfoDatosInt tamanio);

	/**
	 * Método para consultar la parametrización del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarRegistroInterfaz(Connection con, int codigoRegistro);

	/**
	 * Método para consultar el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarDetalleRegistroInterfaz(Connection con, int codigoRegistro);
	
	/**
	 * Método para consultar los valores de un detalle especifico de registro de interfaz
	 * @param con
	 * @param codigoDetalle
	 * @return
	 */
	public HashMap consultarValoresRegistroInterfaz(Connection con, int codigoDetalle);
	
	/**
	 * Método que lista los registros de parametrización de interfaz 
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoInterfaz 
	 * @param esOrdenado
	 * @return
	 */
	public Collection listadoRegistrosInterfaz(Connection con, int codigoInstitucion, int tipoInterfaz,boolean esOrdenado);

}
