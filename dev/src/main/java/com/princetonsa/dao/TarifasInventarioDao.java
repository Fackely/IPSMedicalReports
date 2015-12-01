/*
 * TarifasInventarioDao.java 
 * Autor			:  Juan David Ramírez
 * Creado el	:  01-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * 
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public interface TarifasInventarioDao {

	/**
	 * Método para ingresar una tarifa de inventario
	 * @param con Conexión con la BD
	 * @param codigo_esquema_tarifario Código del esquema tarifario
	 * @param cod_articulo Codigo del articulo
	 * @param valor_tarifa Valor de la tarifa
	 * @param porcentaje_iva Porcentaje de IVA
	 * @param porcentaje Porcentaje de la Tarifa
	 * @param actualiz_automatic Actualizar los precios de las tarifas
	 * @param tipo_tarifa Los Tipos de Tarifas a seleccionar
	 * @param usuario_modifica Usuario logueado en el sistema
	 * @param fecha_modifica Fecha de ingreso de la tarifa
	 * @param hora_modifica Hora de ingreso de la tarifa
	 * @param fechaVigencia
	 * @return Número de elementos modificados
	 */
	public int insertar(Connection con, int codigo_esquema_tarifario, int cod_articulo, double valor_tarifa, double porcentaje_iva, double porcentaje, String actualiz_automatic, String tipo_tarifa, String usuario_modifica, String fecha_modifica, String hora_modifica, String fechaVigencia);
	
	/**
	 * Metodo para modificar una tarifa de inventario
	 * @param con Conexión con la BD
	 * @param codigo Código del registro a modificar
	 * @param valor_tarifa Nuevo valor de la tarifa
	 * @param porcentaje_iva Nuevo valor del IVA
	 * @param porcentaje Nuevo Porcentaje de la Tarifa
	 * @param actualiz_automatic Nueva Actualizacion de los precios de las tarifas
	 * @param tipo_tarifa Nuevo Tipo de Tarifa a seleccionar
	 * @param usuario_modifica Nuevo Usuario logueado en el sistema
	 * @param fecha_modifica Nueva Fecha de ingreso de la tarifa
	 * @param hora_modifica Nueva Hora de ingreso de la tarifa
	 * @param fechaVigencia
	 * @return Número de elementos modificados
	 * @return Número de elementos modificados
	 */
	public int modificar(Connection con, int codigo, double valor_tarifa, double porcentaje_iva, double porcentaje, String actualiz_automatic, String tipo_tarifa, String usuario_modifica, String fecha_modifica, String hora_modifica, int esquema_tarifario, String fechaVigencia);
	
	/**
	 * Método para eliminar una terifa de un artículo dado el codigo del registro
	 * @param con Conexión con la BD
	 * @param codigo Codigo del registro a eliminar
	 * @return Número de elementos eliminados
	 */
	public int eliminar(Connection con, int codigo);

	/**
	 * Método para cargar una tarifa de inventario dado el código del articulo
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param codigoArticulo Código del articulo 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return Collection con los datos cargados
	 */
	public Collection cargar(Connection con, int articulo, int esquemaTarifario);
	
	/**
	 * Método para buscar una tarifa en el sistema
	 * @param con Conexión con la BD
	 * @param articulo Criterio de Busqueda
	 * @param descripcionArticulo Criterio de Busqueda
	 * @param esquemaTarifario Criterio de Busqueda
	 * @param tarifa Criterio de Busqueda
	 * @param iva Criterio de Busqueda
	 * @param porcentaje Criterio de Busqueda
	 * @param actualizAutomatic Criterio de Busqueda
	 * @param tipoTarifa Criterio de Busqueda
	 * @return Collection con el listado de las tarifas que cumplen con los criterios de búsqueda
	 */
	public Collection buscar(Connection con, String articulo, String descripcionArticulo, String naturalezaArticulo, String formaFarmaceutica, String concentracionArticulo, int esquemaTarifario, double tarifa, double iva, double porcentaje, String actualizAutomatic, String tipoTarifa, int institucion, String remite);
	
	/**
	 * Metodo que consulta fechas vigencia inventario por esquema articulo
	 * @param con
	 * @param esquemaTarifario
	 * @param articulo
	 * @param codigosArticulos
	 * @return
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String articulo, String cadenaCodigosArticulos);
	
	/**
	 * Metodo que consulta todas las Tarifas Inventarios por esquema articulo
	 * @param con
	 * @param esquemaTarifario
	 * @param codArticulo
	 * @return
	 */
	public HashMap consultarTodasTarifasInventarios(Connection con, String esquemaTarifario, String codArticulo);
}
