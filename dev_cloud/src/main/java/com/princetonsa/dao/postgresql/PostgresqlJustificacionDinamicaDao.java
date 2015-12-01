/*
 * Created on May 27, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;

import com.princetonsa.dao.JustificacionDinamicaDao;
import com.princetonsa.dao.sqlbase.SqlBaseJustificacionDinamicaDao;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlJustificacionDinamicaDao implements  JustificacionDinamicaDao {
	
	/**
	 * Método para insertar un atributo de una justificacion de servicio o medicamento
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro
	 * @param atributo
	 * @param descripcion
	 * @param esArticulo
	 * @return
	 */
	public int insertarAtributoJustificacion(Connection con,int numeroSolicitud,int parametro,int atributo,String descripcion,boolean esArticulo)
	{
		return SqlBaseJustificacionDinamicaDao.insertarAtributoJustificacion(con,numeroSolicitud,parametro,atributo,descripcion,esArticulo);
	}
	
	/**
	 * Metodo para modificar el atributo de la justificacion de una solicitud de servicios o medicamento
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro
	 * @param atributo
	 * @param descripcion
	 * @param esArticulo
	 * @return
	 */
	public int modificarAtributoJustificacion(Connection con,int numeroSolicitud,int parametro,int atributo,String descripcion,boolean esArticulo)
	{
		return SqlBaseJustificacionDinamicaDao.modificarAtributoJustificacion(con,numeroSolicitud,parametro,atributo,descripcion,esArticulo);		
	}

}
