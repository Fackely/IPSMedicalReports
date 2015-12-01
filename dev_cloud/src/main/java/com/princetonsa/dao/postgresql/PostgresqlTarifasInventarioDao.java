/*
 * PostgresqlTarifasInventarioDao.java 
 * Autor			:  mdiaz
 * Creado el	:  01-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TarifasInventarioDao;
import com.princetonsa.dao.sqlbase.SqlBaseTarifasInventarioDao;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 01-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class PostgresqlTarifasInventarioDao implements TarifasInventarioDao {

	/**
	 * Statement de inserción
	 */
	private final String insertarTarifaInventarioStr = "INSERT INTO tarifas_inventario (codigo, esquema_tarifario, articulo, valor_tarifa, porcentaje_iva, porcentaje, actualiz_automatic, tipo_tarifa, usuario_modifica, fecha_modifica, hora_modifica, fecha_vigencia) VALUES (nextval('Seq_tarifas_inventario'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Metodo para la insercion de nuevas tarifas
	 */
	public int insertar(Connection con, int esquemaTarifario, int articulo, double tarifa, double iva, double porcentaje, String actualizAutomatic, String tipoTarifa, String usuarioModifica, String fechaModifica, String horaModifica, String fechaVigencia)
	{
		return SqlBaseTarifasInventarioDao.insertar(con, esquemaTarifario, articulo, tarifa, iva, porcentaje, actualizAutomatic, tipoTarifa, usuarioModifica, fechaModifica, horaModifica, insertarTarifaInventarioStr, fechaVigencia);
	}
	
	/**
	 * Metodo para la modificacion  de tarifas de inventario
	 */
	public int modificar(Connection con, int codigo, double valor_tarifa, double porcentaje_iva, double porcentaje, String actualiz_automatic, String tipo_tarifa, String usuario_modifica, String fecha_modifica, String hora_modifica, int esquema_tarifario, String fechaVigencia)
	{
		return SqlBaseTarifasInventarioDao.modificar(con,codigo, valor_tarifa, porcentaje_iva, porcentaje, actualiz_automatic, tipo_tarifa, usuario_modifica, fecha_modifica, hora_modifica, esquema_tarifario, fechaVigencia);
	}
	
	/**
	 * Metodo para la eliminacion de tarifas inventario
	 */
	public int eliminar(Connection con, int codigo)
	{
		return SqlBaseTarifasInventarioDao.eliminar(con, codigo);		
	}
	
	/**
	 * Método para buscar una tarifa en el sistema
	 * @param con Conexión con la BD
	 * @param articulo Criterio de Busqueda
	 * @param esquemaTarifario Criterio de Busqueda
	 * @param tarifa Criterio de Busqueda
	 * @param iva Criterio de Busqueda
	 * @param porcentaje Criterio de Busqueda
	 * @param actualizAutomatic Criterio de Busqueda
	 * @param tipoTarifa Criterio de Busqueda
	 * @return Collection con el listado de las tarifas que cumplen con los criterios de búsqueda
	 */
	public Collection buscar(Connection con, String articulo, String descripcionArticulo, String naturalezaArticulo, String formaFarmaceutica, String concentracionArticulo, int esquemaTarifario, double tarifa, double iva, double porcentaje, String actualizAutomatic, String tipoTarifa, int institucion, String remite)
	{
		return SqlBaseTarifasInventarioDao.buscar(con, articulo, descripcionArticulo, naturalezaArticulo, formaFarmaceutica, concentracionArticulo, esquemaTarifario, tarifa, iva, porcentaje, actualizAutomatic, tipoTarifa, institucion, remite, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Metodo que consulta fechas vigencia inventario por esquema articulo
	 * @param con Conexión con la BD
	 * @param esquemaTarifario Criterio de Busqueda
	 * @param articulo Criterio de Busqueda
	 * @param cadena Codigos Articulos
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String articulo, String cadenaCodigosArticulos)
	{
		return SqlBaseTarifasInventarioDao.consultarFechasVigencia(con, esquemaTarifario, articulo, cadenaCodigosArticulos);
	}

	/**
	 * Método para cargar una tarifa de inventario dado el código del articulo
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param codigoArticulo Código del articulo 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return true si se cargó correctamente
	 */
	public Collection cargar(Connection con, int articulo, int esquemaTarifario)
	{
		return SqlBaseTarifasInventarioDao.cargar(con, articulo, esquemaTarifario);
	}
	
	/**
	 * Metodo que consulta todas las Tarifas por esquema articulo
	 * @param con conexión con la BD  
	 * @param esquemaTarifario Código del esquema tarifario
	 * @param codigoArticulo Código del articulo
	 * @return HashMap
	 */
	public HashMap consultarTodasTarifasInventarios(Connection con, String esquemaTarifario, String codArticulo)
	{
		return SqlBaseTarifasInventarioDao.consultarTodasTarifasInventarios(con, esquemaTarifario, codArticulo, DaoFactory.POSTGRESQL);
	}
}
