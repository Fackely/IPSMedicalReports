/*
 * Marzo 21 del 2007
 */
package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.salasCirugia.HojaGastosDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseHojaGastosDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad hoja de gastos
 */
public class OracleHojaGastosDao implements HojaGastosDao 
{
	/**
	 * Método implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int consultarConsecutivoXServicio(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.consultarConsecutivoXServicio(con, campos);
	}
	
	/**
	 * Método implementado para consultar la parametrizacion de una hoja de gastos por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarHojaGastosXConsecutivo(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.consultarHojaGastosXConsecutivo(con, campos);
	}
	
	/**
	 * Método implementado para guardar la hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,HashMap campos)
	{
		campos.put("secuencia","seq_paquetes_qx.nextval");
		return SqlBaseHojaGastosDao.guardar(con, campos);
	}
	
	/**
	 * Método que consulta los paquetes de materiales quirúrgicos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> consultarPaquetesMaterialesQx (Connection con,int institucion,boolean conProcedimientos,boolean sinProcedimientos)
	{
		return SqlBaseHojaGastosDao.consultarPaquetesMaterialesQx(con, institucion, conProcedimientos,sinProcedimientos);
	}
	
	/**
	 * Método que realiza la inserción de un paquete
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarPaqueteMaterialesQx(Connection con,HashMap campos)
	{
		campos.put("secuencia", "seq_paquetes_qx.nextval");
		return SqlBaseHojaGastosDao.insertarPaqueteMaterialesQx(con, campos);
	}
	
	/**
	 * Método que realiza la modificacion de un paquete de materiales qx
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarPaqueteMaterialesQx(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.modificarPaqueteMaterialesQx(con, campos);
	}
	
	/**
	 * Método que consulta un paquete
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String,Object> consultarPaqueteMaterialesQx(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.consultarPaqueteMaterialesQx(con, campos);
	}
	
	/**
	 * Método que realiza la eliminación de un paquete
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int eliminarPaqueteMaterialesQx(Connection con,String consecutivo)
	{
		return SqlBaseHojaGastosDao.eliminarPaqueteMaterialesQx(con, consecutivo);
	}
	
	/**
	 * Método que consulta los articulos de un paquete de materiales quirúrgicos
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap<String, Object> consultarArticulosXConsecutivo(Connection con,String consecutivo)
	{
		return SqlBaseHojaGastosDao.consultarArticulosXConsecutivo(con, consecutivo);
	}
	
	/**
	 * Método que consulta los servicios del paquete material quirúrgico
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap<String, Object> consultarServiciosXConsecutivo(Connection con,String consecutivo)
	{
		return SqlBaseHojaGastosDao.consultarServiciosXConsecutivo(con, consecutivo);
	}
	
	/**
	 * Método implementado para insertar un artículo a un paquete de materiales quirúrgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarArticulo (Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.insertarArticulo(con, campos);
	}
	
	/**
	 * Método implementado para insertar un servicio a un paquete de materiales quirúrgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarServicio (Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.insertarServicio(con, campos);
	}
	
	/**
	 * Método implementado para modificar un articulo de un paquete de materiales quirurgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarArticulo (Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.modificarArticulo(con, campos);
	}
	
	/**
	 * Método implementado para eliminar un art{iculo de un paquete de materiales quirúrgicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarArticulo(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.eliminarArticulo(con, campos);
	}
	
	/**
	 * Método implementado para eliminar un art{iculo de un paquete de materiales quirúrgicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarServicio(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.eliminarServicio(con, campos);
	}
	
	/**
	 * Método que realiza la busqueda avanzada de los paquetes materiales Qx.
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap busquedaGenericaPaquetesMateriales(Connection con,HashMap campos)
	{
		return SqlBaseHojaGastosDao.busquedaGenericaPaquetesMateriales(con, campos);
	}
}
