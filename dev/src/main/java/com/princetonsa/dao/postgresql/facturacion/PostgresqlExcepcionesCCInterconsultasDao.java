package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.facturacion.ExcepcionesCCInterconsultasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseExcepcionesCCInterconsultasDao;

/**
 * Método para consultar las excepciones CC de Interconsultas
 * @param con
 * @return centroAtencion
 */
public class PostgresqlExcepcionesCCInterconsultasDao implements ExcepcionesCCInterconsultasDao {

	public HashMap consultarXCentroAtencion(Connection con,String centroAtencion){
		return SqlBaseExcepcionesCCInterconsultasDao.consultarXCentroAtencion(con, centroAtencion);
	}
	
	public boolean guardarExcepcion(Connection con, HashMap listadoXCentroAtencion, String login, String institucion, String centroAtencion)
	{
		return SqlBaseExcepcionesCCInterconsultasDao.guardarExcepcion(con, listadoXCentroAtencion, login, institucion, centroAtencion);
	}
	
	public boolean eliminarExcepcion(Connection con,int indice){
		return SqlBaseExcepcionesCCInterconsultasDao.eliminarExcepcion(con,indice);
	}
	
	public HashMap obtenerMedicos(Connection con,int institucion){
		return SqlBaseExcepcionesCCInterconsultasDao.obtenerMedicos(con, institucion);
	}
	
	public boolean modificarExcepcion(Connection con, HashMap listadoXCentroAtencion, String login, String institucion, String centroAtencion)
	{
		return SqlBaseExcepcionesCCInterconsultasDao.modificarExcepcion(con, listadoXCentroAtencion, login, institucion, centroAtencion);
	}
	
	public HashMap obtenerCentrosCosto(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion)
	{
		return SqlBaseExcepcionesCCInterconsultasDao.obtenerCentrosCosto(con, centroAtencion,tipoAreaDirecto, institucion);
	}
	
	public HashMap obtenerCentrosCostoEjecutan(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion)
	{
		return SqlBaseExcepcionesCCInterconsultasDao.obtenerCentrosCostoEjecutan(con, centroAtencion,tipoAreaDirecto, institucion);
	}
}