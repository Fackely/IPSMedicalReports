package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Anexo 714
 * Creado el 17 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public interface TextosRespuestaProcedimientosDao
{
	/**
	 * Metodo encargado de consultar la informacion de
	 * TextosRespuestaProcedimientos - Historia Clinica, en la tabla "textos_resp_proc".
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- institucion	--> Requerido
	 * -- activo	 	--> Opcional
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo_,
	 * servicio_,
	 * descripcionTexto_,
	 * textoPredeterminado_,
	 * activo_,
	 * estaBd_
	 */
	public HashMap consultaTextosRespuestaProcedimientos (Connection connection, int institucion);
	
	/**
	 * Metodo encargado de Insertar
	 * los datos de TextosRespuestaProcedimientos.
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- codigo 				--> Requerido
	 * -- descripcionTexto	 	--> Requerido
	 * -- textoPredeterminado 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion 			--> Requerido
	 * @return false/true
	 */
	public boolean insertarTextosRespuestaProcedimientos (Connection connection, HashMap datos);
	
	/**
	 * @param connection
	 * @param codigoServicio
	 * @return
	 */
	public HashMap cargarTextosRespuestaProcedimientos(Connection connection, int codigoServicio, int institucion);
	
	/**
	 * Método encargado de actualizar la tabla de "textos_resp_proc".
	 * 
	 * @author Ing. Felipe Pérez Granda
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo			--> Requerido
	 * -- consecutivo		--> Requerido
	 * -- descripcion		--> Requerido
	 * -- mod				--> Requerido
	 * -- institucion		--> Requerido
	 * -- usuarioModifica 	--> Requerido
	 * @return false/true
	 */
	public boolean actualizaTextosRespuestaProcedimientos (Connection connection, HashMap datos);
	
	/**
	 * Método encargado de eliminar datos de la tabla
	 * textos_resp_proc
	 * @author Ing. Felipe Pérez Granda
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * Keys el mapa de datos
	 * -- codigo		--> Requerido
	 * -- institucion 	--> Requerido
	 * -----------------------------------
	 * @return true/false
	 */
	public boolean eliminarTextosRespuestaProcedimientos (Connection connection, int codigoServicio, int institucion);

	/**
	 * @param connection
	 * @param codServicio
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean eliminarTextoProcedimiento(Connection connection, int codigo, int codigoInstitucionInt);
	
	/**
	 * Metodo encargado de consultar mediante el servicio y la institución
	 * los códigos asociados a ese servicio en la tabla "textos_resp_proc".
	 * @author Ing. Felipe Perez Granda
	 * @param servicio
	 * @param institucion
	 * ---------------------------------
	 * ---------------------------------
	 * KEY'S DEL MAPA 
	 * ---------------------------------
	 * -- codigo
	 * @return mapa
	 */
	public HashMap consultarCodigosTextos (Connection connection, String servicio, int institucion);
}