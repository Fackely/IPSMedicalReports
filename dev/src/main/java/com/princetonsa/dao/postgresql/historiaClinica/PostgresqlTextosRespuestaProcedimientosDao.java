package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.historiaClinica.TextosRespuestaProcedimientosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseTextosRespuestaProcedimientosDao;

/**
 * Anexo 714
 * Creado el 17 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class PostgresqlTextosRespuestaProcedimientosDao implements TextosRespuestaProcedimientosDao
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
	 * -- action 		--> Opcional
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

	public HashMap consultaTextosRespuestaProcedimientos (Connection connection, int institucion)
	{
		return SqlBaseTextosRespuestaProcedimientosDao.consultaTextosRespuestaProcedimientos(connection, institucion);
	}
	/**
	 * Metodo encargado de insertar
	 * los datos de TextosRespuestaProcedimientos.
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- codigo	 			--> Requerido
	 * -- descripcionTexto	 	--> Requerido
	 * -- textoPredeterminado 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion 			--> Requerido
	 * @return false/true
	 */
	
	public boolean insertarTextosRespuestaProcedimientos (Connection connection, HashMap datos)
	{
		return SqlBaseTextosRespuestaProcedimientosDao.insertarTextosRespuestaProcedimientos(connection, datos);
	}
	
	/**
	 * Metodo encargado de cargar los Textos de Respuesta a Procedimientos
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param codigoServicio
	 * @param institucion
	 */
	public HashMap cargarTextosRespuestaProcedimientos(Connection connection, int codigoServicio, int institucion)
	{
		return SqlBaseTextosRespuestaProcedimientosDao.cargarTextosRespuestaProcedimientos(connection, codigoServicio, institucion);
	}
	
	/**
	 * Método encargado de actualizar la tabla de "textos_resp_proc".
	 * @author Ing. Felipe Pérez
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo				--> Requerido
	 * -- servicio				--> Requerido
	 * -- descripcionTexto		--> Requerido
	 * -- textoPredetermiando 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion			--> Requerido
	 * -- usuarioModifica		--> Requerido
	 * @return false/true
	 */
	
	public boolean actualizaTextosRespuestaProcedimientos (Connection connection,HashMap datos )
	{
		return SqlBaseTextosRespuestaProcedimientosDao.actualizaTextosRespuestaProcedimientos(connection, datos);
	}
	
	/**
	 * Metodo encargado de eliminar los Textos de Respuesta a Procedimientos
	 * desde la primera pantalla, eliminado todo el mapa
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param codigoServicio
	 * @param institucion
	 * @return false/true
	 * 
	 */
	public boolean eliminarTextosRespuestaProcedimientos(Connection connection, int codigoServicio, int institucion)
	{
		return SqlBaseTextosRespuestaProcedimientosDao.eliminarTextosRespuestaProcedimientos(connection, codigoServicio, institucion);
	}
	
	/**
	 * Metodo encargado de eliminar los Textos de Respuesta a Procedimientos
	 * desde la página individual de eliminacion de descripcion_texto y texto_predeterminado
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param codigoServicio
	 * @param codigoInstitucionInt
	 * @return false/true
	 * 
	 */
	public boolean eliminarTextoProcedimiento(Connection connection, int codigo, int codigoInstitucionInt)
	{
		return SqlBaseTextosRespuestaProcedimientosDao.eliminarTextoProcedimiento(connection, codigo, codigoInstitucionInt);
	}
	
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
	
	public HashMap consultarCodigosTextos (Connection connection, String servicio, int institucion)
	{
		return SqlBaseTextosRespuestaProcedimientosDao.consultarCodigosTextos(connection, servicio, institucion);
	}
	
}