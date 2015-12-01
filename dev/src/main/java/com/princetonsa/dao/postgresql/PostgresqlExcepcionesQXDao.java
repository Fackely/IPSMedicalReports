/*
 * @(#)OracleExcepcionesQXDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;

import com.princetonsa.dao.ExcepcionesQXDao;
import com.princetonsa.dao.sqlbase.SqlBaseExcepcionesQXDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para excepciones qx
 *
 * @version 1.0, Oct  10 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 * 
 * Clase reestruturada totalmente por el documento 525 Cambio
 * en funcionalidades de parametrizacion cirugia DyT.
 * 
 * Modificado el 4/12/2007
 * Modificado por Jhony Alexander Duque A.
 */
public class PostgresqlExcepcionesQXDao implements ExcepcionesQXDao 
{
	/**
	 * Metodo encargado de Buscar las
	 * Vigencias para el convenio seleccionado.
	 * Parametros:
	 * @connection
	 * @criterios
	 * ------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ------------------------------
	 * -- convenio1_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * ---------------------------------
	 * Retorna
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL HASHMAP MAPA
	 * --------------------------------
	 * -- codigoExcepcionQx6_
	 * -- fechaInicial2_
	 * -- fechaFinal3_ 
	 */
	public HashMap buscar0 (Connection connection,HashMap criterios)
	{
		return SqlBaseExcepcionesQXDao.buscar0(connection, criterios);
	}
	
	/**
	 * Metodo encargado de buscar los 
	 * centros de costo para una vigencia.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------
	 * KEY'S PARA EL HASHMAP CRITERIOS
	 * -----------------------------------------
	 * -- codigoExcepcionQx6_
	 * ---------------------------------------
	 * @return
	 * ---------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * ---------------------------------
	 * -- codigoExcepcionQxCc8_
	 * -- centroCosto7_
	 */
	public HashMap buscar1 (Connection connection, HashMap criterios )
	{
		return SqlBaseExcepcionesQXDao.buscar1(connection, criterios);
	}
	
	/**
	 * Metodo encargado  de buscar los datos 
	 * de  excepciones quirurjicas que pertenecen
	 * a un convenio, una vigencia y un centro de costo.
	 * @param connection
	 * @param criterios
	 * ----------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ----------------------------------
	 * -- detCodigoExcepcionQxCc0_ --> Opcional
	 * -- centroCosto7_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoSala21_ --> Opcional
	 * -- servicio13_ --> Opcional
	 * -- tipoAsocio12_ --> Opcional
	 * -- tipoCirugia10_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- tipoLiquidacion11_ --> Opcional
	 * -- valor19_ --> Opcional
	 * @return
	 */
	public HashMap buscar2 (Connection connection, HashMap criterios )
	{
		return SqlBaseExcepcionesQXDao.buscar2(connection, criterios);
	}
	
	/**
	 * Metodo encargado de insertar los datos
	 * en la tabla excepciones_qx.
	 * Parametros: 
	 * @connection
	 * @datos
	 * --------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------
	 * -- convenio1_ --> Requerido
	 * -- fechaInicial2_ --> Requerido
	 * -- fechaFinal3_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- contrato23_ --> Requerido
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.				
	 */
	public boolean insetar0 (Connection connection, HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.insetar0(connection, datos);
	}
		
		
	/**
	 * Metodo encargado de eliminar los datos
	 * de la tabla  (excepciones_qx)
	 * @connection
	 * @datos
	 * ------------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------------
	 * -- codigoExcepcionQx6_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public boolean eliminar0 (Connection connection,HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.eliminar0(connection, datos);
	}
	
	/**
	 * Metodo encargado de modificar los datos
	 * de la tabla excepciones_qx
	 * @connection
	 * @datos
	 * -------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -------------------------------
	 * -- fechaInicial2_
	 * -- fechaFinal3_
	 * -- usuarioModifica5_
	 * -- ExcepcionQx6_
	 * -- institucion4_
	 *  * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public boolean modificar0 (Connection connection, HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.modificar0(connection, datos);
	}
	
	/**
	 * Metodo encargado de insertar los datos de
	 * la tabla (det_excepciones_qx_cc) 
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- tipoSala21_
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public boolean insetar2 (Connection connection, HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.insetar2(connection, datos);
	}
	
	/**
	 *Metodo encargado de elegir donde seran insertados los datos
	 *ya sea en la tabla excepciones_qx_cc o en la tabla
	 *det_excepciones_qx_cc.
	 *@connection
	 *@datos
	 *-----------------------
	 *KEY'S DEL HASHMAP DATOS
	 *-----------------------
	 * -- codigoExcepcionQx6_ -->Requerido
	 * -- centroCosto7_ --> Requerido
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- tipoSala21_
	 * 
	 */
	public int insertarGeneral (Connection connection,HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.insertarGeneral(connection, datos);
	}
	
	/**
	 * Metodo encargado de identificar en que
	 * parte de la BD eliminar los datos
	 * @param connection
	 * @param datos
	 * 
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * @return
	 */
	public boolean eliminarGeneral (Connection connection, HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.eliminarGeneral(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de modificar los datos de
	 * la tabla (det_excepciones_qx_cc) 
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * -- tipoSala21_ --> Opcional
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public boolean modificar1 (Connection connection, HashMap datos)
	{
		return SqlBaseExcepcionesQXDao.modificar1(connection, datos);
	}
}