package com.princetonsa.dao.postgresql.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseAsocioSalaCirugiaDao;
import com.princetonsa.dao.salasCirugia.AsocioSalaCirugiaDao;


public class PostgresqlAsocioSalaCirugiaDao implements AsocioSalaCirugiaDao
{
	
	/**
	 * Cadena de Insercion de Asocios
	 * */
	private static final String strInsertarAsocioSalaCirugia = " INSERT INTO " +
			"tipos_asocio(codigo,institucion,codigo_asocio,nombre_asocio,tipos_servicio,centro_costo_ejecuta,participa_cir,usuario_modifica,fecha_modifica,hora_modifica) VALUES (nextval('seq_tipos_asocio'),?,?,?,?,?,?,?,?,?)";
	
	
	
	/**
	 * Consulta la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param int institucion 
	 * */
	public HashMap consultaAsocioSalaCirugia(Connection con, int institucion)
	{
		return SqlBaseAsocioSalaCirugiaDao.consultaAsocioSalaCirugia(con,institucion);
	}
	
	/**
	 * Consulta la informacion de los asocios de Cirugia por medio del acronimo
	 * @param Connection con
	 * @param int institucion 
	 * @param String codigoAsocio
	 * */
	public HashMap consultaAsocioSalaCirugia(Connection con, int institucion,String codigoAsocio)
	{
		return SqlBaseAsocioSalaCirugiaDao.consultaAsocioSalaCirugia(con, institucion, codigoAsocio);
	}
	
	
	/**
	 * Actualiza la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarAsocioSalaCirugia(Connection con, HashMap parametros)
	{
		return SqlBaseAsocioSalaCirugiaDao.actualizarAsocioSalaCirugia(con, parametros);
	}
	
	
	/**
	 * Inserta informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean insertarAsocioSalaCirugia(Connection con, HashMap parametros)
	{
		return SqlBaseAsocioSalaCirugiaDao.insertarAsocioSalaCirugia(con, parametros,strInsertarAsocioSalaCirugia);
	}

	
	/**
	 * Elimina informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean eliminaAsocioSalaCirugia(Connection con, int institucion, String codigo)
	{
		return SqlBaseAsocioSalaCirugiaDao.eliminaAsocioSalaCirugia(con,institucion,codigo);
	}
	
	/**
	 * Consulta de Tipos de Servicio
	 * @param Connection con
	 * */
	public ArrayList<HashMap<String,Object>> consultaTiposServicios(Connection con)
	{
		return SqlBaseAsocioSalaCirugiaDao.consultaTiposServicios(con);
	}	
}