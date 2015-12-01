package com.princetonsa.enu.administracion;


/**
 * 	ESTRUCTURA PARA LOS CONSECUTIVOS CENTRO DE ATENCION
 * @author axioma
 *
 */

public interface IEstructuraConsecutivosCentroAtencion    {

	
	/**
	 * RETORNA EL NOMBRE DEL CONSECUTIVO EN INTERFAZ GRAFICA 
	 * @return
	 */
	public String getNombreConsecutivoInterfazGrafica();
	
	/**
	 * RETORNA EL NOMBRE DEL CONSECUTIVO EN BASE DE DATOS 
	 * @return
	 */
	public String getNombreConsecutivoBaseDatos();
	
}
