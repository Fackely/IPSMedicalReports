package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.salasCirugia.DtoAsociosXRangoTiempo;

/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
public interface AsociosXRangoTiempoDao
{
	/**
	 * Metodo que consulta la tabla asocios_x_rango_tiempo
	 * permitiendo filtrar por diferentes criterios  
	 * @param connection
	 * @param criteriosBusqueda --> DtoAsociosXRangoTiempo
	 * @return HashMap
	 */
	public HashMap consultarAsociosPorRangoFecha (Connection connection, DtoAsociosXRangoTiempo criteriosBusqueda);
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla 
	 * asocios_x_rango_tiempo filtrandolos por codigo e institucion.
	 * @param connection
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean eliminar (Connection connection,int codigo,int institucion );
	
	
	/**
	 *Metodo en cargado de modificar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	public boolean modificarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo);
	
	/**
	 *Metodo en cargado de insertar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	public boolean insertarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo);
	
	/**
	 * Metodo encargado de consultar las diferentes fechas de los asocios
	 * @param connection
	 * @return
	 */
	public HashMap obtenerFechasAsocios (Connection connection,int convenio);
	
	
	/**
	 * Metodo encargado de consultar el codigo del consecutivo
	 * de la cabeza del esquema tarifario.
	 * @param connection
	 * @param esqtarfio
	 * @return
	 */
	public int consultarSqtarfio (Connection connection, int esqtarfio,int institucion,int tipo);
	
	/**
	 * Metodo que se encarga de verificar si se borra
	 * el encabezado o no en las tablas.
	 * @param connection
	 * @param codcab
	 * @param codigodet
	 * @param institucion
	 * @return
	 */
	public boolean eliminarGeneral (Connection connection,int codcab, int codigodet, int institucion);
	
	
	/**
	 * Metodo encargado de eliminar el encabezado de la la 
	 * tabla cabeza_asoc_x_ran_tiem  
	 * @param connection
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean elimiarcabezado (Connection connection, int codigo , int institucion);
	
	
	/**
	 * Metodo encargado de insertar datos del encabezado,
	 * a la Tabla (cabeza_asoc_x_ran_tiem).
	 * ----------------------------------
	 *             PARAMETROS
	 * ----------------------------------
	 * @connection	Connection
	 * @encabezado	HashMap
	 * ------------------------------------
	 * 		   KEY'S DE ENCABEZADO
	 * -------------------------------------
	 * --convenio --> Opcional
	 * --esqTar --> Opcional
	 * --fecIniAsoc --> Opcional
	 * --fecFinAsoc --> Opcional
	 * --institucion --> Requerido
	 * --usuarioModifica --> Requerido
	 * ------------------------------------
	 * 		RETORNA UN BOOLEANO
	 * ------------------------------------
	 */
	public boolean insertarencazado (Connection connection, HashMap encabezado);
	
	
	/**
	 * Metodo encargado de modificar los datos del encabezado.
	 * @param connection
	 * @param encab
	 * @return
	 */
	public boolean modificarEncabezado (Connection connection,HashMap encab);
	
}