/*
 * Created on Aug 20, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author sebacho
 *
 * Dao para el Acceso a la fuente de datos de todos los procesos
 * vinculados con la Estancia Autom�tica
 */
public interface EstanciaAutomaticaDao {
	
	/**
	 * M�todo usado para consultar las cuentas a las cuales se les va a
	 * generar una solicitud de estancia y su cargo, en la opci�n
	 * de Estancia Autom�tica Por �rea
	 * @param con
	 * @param centroCosto
	 * @param fechaEstancia
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultaCuentasEstanciaPorArea(Connection con,String fechaEstancia,int centroAtencion, int institucion,int codigoCentroCosto);
	
	/**
	 * M�todo que consulta los traslados cama de la cuenta que
	 * va a generar solicitud de estancia
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param centroCosto
	 * @return
	 */
	public HashMap consultarTrasladosCuenta(Connection con,int idCuenta,String fechaEstancia,int centroCosto);
	
	/**
	 * M�todo usado para consultar los tipos de monitoreo de una cuenta 
	 * teniendo como referencia de b�squeda la fecha de estancia
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @return
	 */
	public HashMap consultarTiposMonitoreo(Connection con,int idCuenta,String fechaEstancia);
	
	/**
	 * Metodo que consulta el servicio del cargo en la tabla
	 * servicios_cama
	 * @param con
	 * @param cama
	 * @param tipoMonitoreo
	 * @return
	 */
	public int consultarServicioCama(Connection con,int cama,int tipoMonitoreo);
	
	/**
	 * M�todo usado para registrar el LOG tipo Base de Datos del proceso
	 * de generaci�n de Estancia Autom�tica
	 * @param con
	 * @param tipo
	 * @param fechaInicialEstancia
	 * @param fechaFinalEstancia
	 * @param usuario
	 * @param institucion
	 * @param area
	 * @param paciente
	 * @param centroAtencion
	 * @param inconsistencia
	 * @param reporte
	 * @return
	 */
	public int insertarLogEstanciaAutomatica(Connection con,int tipo,String fechaInicialEstancia,String fechaFinalEstancia,String usuario,int institucion,int area,int paciente,int centroAtencion,boolean inconsistencia,String reporte, String indica);
	
	/**
	 * M�todo que consulta y verifica que sobre la cuenta se
	 * pueda generar estancia autom�tica para el d�a asignado.
	 * En el caso de que no se pueda generar estancia para ese d�a
	 * el m�todo retorna un HashMap vac�o
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public HashMap consultaCuentaEstanciaPorPaciente(Connection con,int idCuenta,String fechaEstancia,int institucion);
	
	/**
	 * M�todo usaod para consultar los registros del registro LOG
	 * de Estancia Autom�tica
	 * @param con
	 * @param anio
	 * @param mes
	 * @param area
	 * @param tipo
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultaLogEstanciaAutomatica(Connection con,int anio,int mes,int area,int tipo,String usuario,int centroAtencion,String indica);
	
	/**
	 * M�todo que consulta el numero de estancias generadas para una cuenta 
	 * en un rango de fechas determinado
	 * @param con
	 * @param campos
	 * @return
	 */
	public int numeroEstanciasPaciente(Connection con,HashMap campos);
}
