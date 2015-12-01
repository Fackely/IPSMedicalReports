/*
 * Marzo 19, 2006
 */
package com.princetonsa.dao.postgresql.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.salasCirugia.ExTarifasAsociosDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseExTarifasAsociosDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Postgresql para el acceso a la fuente
 * de datos en la funcionalidad Parametrización de Excepciones Tarifas Asocios
 */
public class PostgresqlExTarifasAsociosDao implements ExTarifasAsociosDao 
{
	
	private static String strInsertarEncabezado="INSERT INTO ex_tarifas_asocios(codigo,convenio,institucion,fecha_inicial,fecha_final,usuario_modifica,fecha_modifica,hora_modifica) VALUES(nextval('seq_ex_tarifas_asocios'),?,?,?,?,?,?,?)";
	
	private static String strInsertarMedia="INSERT INTO ex_tarifas_asocios_xvitpcc(codigo,codigo_encab,via_ingreso,tipo_paciente,centro_costo,usuario_modifica,fecha_modifica,hora_modifica) VALUES(nextval('seq_extar_asoc_xvitpcc'),?,?,?,?,?,?,?)";
	
	private static String strInsertarDetalle="INSERT INTO det_ex_tarifas_asocios(codigo,codigo_encab_xvit,tipo_servicio,especialidad,grupo_servicio,servicio,tipo_cirugia,asocio,rango_inicial,rango_final,porcentaje,valor,tipo_excepcion,usuario_modifica,fecha_modifica,hora_modifica) VALUES(nextval('seq_det_ex_tarifa'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Insertar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros	
	 * @return 
	 * */	
	public boolean insertarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.insertarEncabezado(con, parametros, strInsertarEncabezado);
	}
	
	
	/**
	 * Modificar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.modificarEncabezado(con, parametros);
	}
	
	
	/**
	 * Eliminar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.EliminarEncabezado(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarEncabezado(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.consultarEncabezado(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.insertarMedia(con, parametros, strInsertarMedia);
	}
	
	/**
	 * Modificar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.modificarMedia(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.consultarMedia(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarMedia(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.EliminarMedia(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.insertarDetalle(con, parametros, strInsertarDetalle);
	}
	
	/**
	 * Modificar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.modificarDetalle(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.EliminarDetalle(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarDetalle(Connection con, HashMap parametros)
	{
		return SqlBaseExTarifasAsociosDao.consultarDetalle(con, parametros);
	}
}