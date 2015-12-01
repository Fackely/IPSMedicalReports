/*
 * Creado en 2/07/2004
 *
 * Juan David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

/**
 * @author Juan David Ramírez
 *
 */
public interface MontosCobroDao
{
	/**
	 * Ingresar montos
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @param fecha
	 * @return
	 */
	public boolean insertarMonto(Connection con, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @param esModificar
	 * @return collection con los resultados de la búsqueda de montos
	 */
	public Collection buscarMontos(Connection con, int codigo, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, String activo, boolean esModificar, String fecha);
	
	/**
	 * Clase para modificar los montos de cobro
	 * @param con
	 * @param codigo
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @param activo
	 * @return
	 */
	public boolean modificarMonto(Connection con, int codigo, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha);

	/**
	 * Método para cargar un monto de cobro
	 * @param con Conexión con la BD
	 * @param codigo del monto de cobro
	 * @return Collection con los datos del monto de cobro
	 */
	public Collection cargar(Connection con, int codigo);
	
	/**
	 * Adición Sebastián
	 * Método para cargar los códigos de los montos de cobro de acuerdo
	 * a ciertos parámetros, estos parámetros de búsqueda pueden omitirse
	 * con 0 (Enteros) o "" (String)
	 * @param con
	 * @param convenio
	 * @param tipoAfiliado
	 * @param estrato
	 * @param viaIngreso
	 * @return
	 */
	public Collection consultarMontosCobro(Connection con,int convenio,String tipoAfiliado,int estrato,int viaIngreso);

}
