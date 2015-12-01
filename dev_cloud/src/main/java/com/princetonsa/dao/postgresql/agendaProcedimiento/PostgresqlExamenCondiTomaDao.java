package com.princetonsa.dao.postgresql.agendaProcedimiento;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.agendaProcedimiento.ExamenCondiTomaDao;
import com.princetonsa.dao.sqlbase.agendaProcedimiento.SqlBaseExamenCondiTomaDao;
import com.princetonsa.mundo.agendaProcedimiento.ExamenCondiToma;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * Funcionalidad descrita en Anexo 393 - Condiciones de Toma de Examen  
 * */

public class PostgresqlExamenCondiTomaDao implements ExamenCondiTomaDao
{
//-- Metodos 
	
	/**
	 * Inserta un registro de condicion de toma de examen 
	 * @param Connection con 
	 * @param ExamenCondiToma exameCt
	 * */
	public boolean insertarExamenCt(Connection con, ExamenCondiToma examenCt)
	{
		examenCt.SetCadenaSecuenciaStr("NEXTVAL('seq_exam_condi_toma')");
		return SqlBaseExamenCondiTomaDao.insertarExamenCt(con, examenCt);		
	}
	
	/**
	 * Modificar un registro de condicion de toma de examen 
	 * @param Connection con
	 * @param ExamenCondiToma examenCt 
	 * */
	public boolean modificarExamenCt(Connection con, ExamenCondiToma examenCt)
	{
		return SqlBaseExamenCondiTomaDao.modificarExamenCt(con, examenCt);
	}
	
	/**
	 * Elimina un registro de condicion de toma de examen
	 * @param Connection con
	 * @param String codigoExamenCt
	 * @param int institucion 
	 * */
	public boolean eliminarExamenCt(Connection con, String codigoExamenCt, int institucion)
	{
		return SqlBaseExamenCondiTomaDao.eliminarExamenCt(con, codigoExamenCt, institucion);
	}
	
	/**
	 * Consulta basica de condiciones de toma de examen
	 * si los parametros son -> ("",int) genera una consulta de todos los campos por institucion
	 * @param String codigoExamenCt
	 * @param int institucion
	 * */
	public HashMap consultarExamentCtBasica(Connection con, String codigoExamenCt, int institucion)
	{
		return SqlBaseExamenCondiTomaDao.consultarExamentCtBasica(con, codigoExamenCt, institucion);
	}
	
	//-- Fin Metodos		
}
