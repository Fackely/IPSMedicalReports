/*
 * Sep 05/2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 *  @author Sebasti�n G�mez R
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de Salas
 */
public interface SalasDao {
	
	/**
	 * M�todo usado para cargar el listado de salas existentes
	 * por institucion
	 * @param con
	 * @param institucion
	 * @param centroAtencion 
	 * @return
	 */
	public HashMap cargarSalas(Connection con,int institucion, int centroAtencion);
	
	/**
	 * M�todo para insertar una nueva sala
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param centroAtencion 
	 * @param insertarSalasStr
	 * @return
	 */
	public int insertarSala(Connection con,String codigo,int institucion,int tipoSala,boolean activo,String descripcion, int centroAtencion, String medico);
	
	/**
	 * M�todo usado para actualizar los datos de una sala
	 * @param con
	 * @param codigo
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param consecutivo
	 * @param centroAtencion 
	 * @return
	 */
	public String actualizarSala(Connection con,String codigo,int tipoSala,boolean activo,String descripcion,int consecutivo, int centroAtencion, String medico);
	
	/**
	 * M�todo usado para eliminar una sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int eliminarSala(Connection con,int consecutivo);
	
	/**
	 * M�todo usado para cargar una sala de las tablas existentes
	 * de acuerdo a su consecutivo interno de Axioma
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap cargarSala(Connection con,int consecutivo);
	
	/**
	 * M�todo usado para cargar la disponiblidad de la sala
	 * @param con
	 * @param consecutivoSala
	 * @return HashMap con la disponibilidad de la sala
	 */
	public HashMap cargarDisponibilidadSala(Connection con, String consecutivoSala);
	
	/**
	 * M�todo usado para actualizar un rango de disponibilidad para una sala espec�fica
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param rangoInicialAnterior
	 * @param rangoFinalAnterior
	 * @return 
	 */
	public int actualizarDisponibilidadSala (Connection con, String codigoSala, String rangoInicial, String rangoFinal, String rangoInicialAnterior, String rangoFinalAnterior);
	
	/**
	 * M�todo para insertar un nuevo rango de disponibilidad de la sala
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @return
	 */
	public String insertarDisponibilidadSala(Connection con, String codigoSala, String rangoInicial, String rangoFinal);
	
	
	/**
	 * @param con
	 * @param Codigo Sala
	 * @param Rango Inicial
	 * @param Rango Final
	 * @return
	 */
	public int accionEliminarRango(Connection con, String codigoSala, String rangoInicial, String rangoFinal);

}
