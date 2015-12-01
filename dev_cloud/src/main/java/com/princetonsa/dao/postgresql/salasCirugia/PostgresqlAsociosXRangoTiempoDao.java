package com.princetonsa.dao.postgresql.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.salasCirugia.AsociosXRangoTiempoDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseAsociosXRangoTiempoDao;
import com.princetonsa.dto.salasCirugia.DtoAsociosXRangoTiempo;

/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 * 31/10/2007
 */
public class PostgresqlAsociosXRangoTiempoDao implements AsociosXRangoTiempoDao
{
	public HashMap consultarAsociosPorRangoFecha(Connection connection, DtoAsociosXRangoTiempo criteriosBusqueda) 
	{
		return SqlBaseAsociosXRangoTiempoDao.consultarAsociosPorRangoFecha(connection, criteriosBusqueda);
	}
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla 
	 * asocios_x_rango_tiempo filtrandolos por codigo e institucion.
	 * @param connection
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean eliminar (Connection connection,int codigo,int institucion )
	{
		return SqlBaseAsociosXRangoTiempoDao.eliminar(connection, codigo, institucion);
	}

	
	/**
	 *Metodo en cargado de modificar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	public boolean modificarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo)
	{
		return SqlBaseAsociosXRangoTiempoDao.modificarAsociosRangoTiempo(connection, asociosXrangoTiempo);
	}
	
	/**
	 *Metodo en cargado de insertar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	public boolean insertarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo)
	{
		return SqlBaseAsociosXRangoTiempoDao.insertarAsociosRangoTiempo(connection, asociosXrangoTiempo);
	}
	
	/**
	 * Metodo encargado de consultar las diferentes fechas de los asocios
	 * @param connection
	 * @return
	 */
	public HashMap obtenerFechasAsocios (Connection connection, int convenio)
	{
		return SqlBaseAsociosXRangoTiempoDao.obtenerFechasAsocios(connection, convenio);
	}
	
	/**
	 * Metodo encargado de consultar el codigo del consecutivo
	 * de la cabeza del esquema tarifario.
	 * @param connection
	 * @param esqtarfio
	 * @return
	 */
	public int consultarSqtarfio (Connection connection, int esqtarfio,int institucion, int tipoEsq)
	{
		return SqlBaseAsociosXRangoTiempoDao.consultarSqtarfio(connection, esqtarfio, institucion,tipoEsq);
	}
	
	/**
	 * Metodo que se encarga de verificar si se borra
	 * el encabezado o no en las tablas.
	 * @param connection
	 * @param codcab
	 * @param codigodet
	 * @param institucion
	 * @return
	 */
	public boolean eliminarGeneral (Connection connection,int codcab, int codigodet, int institucion)
	{
		return SqlBaseAsociosXRangoTiempoDao.eliminarGeneral(connection, codcab, codigodet, institucion);
	}
	
	public boolean elimiarcabezado (Connection connection, int codigo , int institucion)
	{
		return SqlBaseAsociosXRangoTiempoDao.elimiarcabezado(connection, codigo, institucion); 
	}
	
	
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
	public boolean insertarencazado (Connection connection, HashMap encabezado)
	{
		return SqlBaseAsociosXRangoTiempoDao.insertarencazado(connection, encabezado);
	}
	
	/**
	 * Metodo encargado de modificar los datos del encabezado.
	 * @param connection
	 * @param encab
	 * @return
	 */
	public boolean modificarEncabezado (Connection connection,HashMap encab)
	{
		return SqlBaseAsociosXRangoTiempoDao.modificarEncabezado(connection, encab);
	}
	
}