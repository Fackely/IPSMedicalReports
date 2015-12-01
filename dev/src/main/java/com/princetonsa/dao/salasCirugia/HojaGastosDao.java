/*
 * Marzo 21 del 2007
 */
package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de hoja de gastos
 */
public interface HojaGastosDao 
{
	/**
	 * M�todo implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int consultarConsecutivoXServicio(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para consultar la parametrizacion de una hoja de gastos por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarHojaGastosXConsecutivo(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para guardar la hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los paquetes de materiales quir�rgicos
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> consultarPaquetesMaterialesQx (Connection con,int institucion, boolean conProcedimientos,boolean sinProcedimientos);
	
	/**
	 * M�todo que realiza la inserci�n de un paquete
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarPaqueteMaterialesQx(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la modificacion de un paquete de materiales qx
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarPaqueteMaterialesQx(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta un paquete
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String,Object> consultarPaqueteMaterialesQx(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la eliminaci�n de un paquete
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int eliminarPaqueteMaterialesQx(Connection con,String consecutivo);
	
	/**
	 * M�todo que consulta los articulos de un paquete de materiales quir�rgicos
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap<String, Object> consultarArticulosXConsecutivo(Connection con,String consecutivo);
	
	/**
	 * M�todo que consulta los servicios del paquete material quir�rgico
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap<String, Object> consultarServiciosXConsecutivo(Connection con,String consecutivo);
	
	/**
	 * M�todo implementado para insertar un art�culo a un paquete de materiales quir�rgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarArticulo (Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para insertar un servicio a un paquete de materiales quir�rgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarServicio (Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para modificar un articulo de un paquete de materiales quirurgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificarArticulo (Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para eliminar un art{iculo de un paquete de materiales quir�rgicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarArticulo(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para eliminar un art{iculo de un paquete de materiales quir�rgicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminarServicio(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la busqueda avanzada de los paquetes materiales Qx.
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap busquedaGenericaPaquetesMateriales(Connection con,HashMap campos);

}
