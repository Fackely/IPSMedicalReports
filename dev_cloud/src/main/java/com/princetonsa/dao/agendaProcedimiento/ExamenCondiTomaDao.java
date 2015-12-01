package com.princetonsa.dao.agendaProcedimiento;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.agendaProcedimiento.ExamenCondiToma;

public interface ExamenCondiTomaDao
{
	//-- Metodos 
	
	/**
	 * Inserta un registro de condicion de toma de examen 
	 * @param Connection con 
	 * @param ExamenCondiToma exameCt
	 * */
	public boolean insertarExamenCt(Connection con, ExamenCondiToma examenCt);
	
	/**
	 * Modificar un registro de condicion de toma de examen 
	 * @param Connection con
	 * @param ExamenCondiToma examenCt 
	 * */
	public boolean modificarExamenCt(Connection con, ExamenCondiToma examenCt);
	
	/**
	 * Elimina un registro de condicion de toma de examen
	 * @param Connection con
	 * @param String codigoExamenCt
	 * @param int institucion 
	 * */
	public boolean eliminarExamenCt(Connection con, String codigoExamenCt, int institucion);
	
	/**
	 * Consulta basica de condiciones de toma de examen
	 * si los parametros son -> ("",int) genera una consulta de todos los campos por institucion
	 * @param String codigoExamenCt
	 * @param int institucion
	 * */
	public HashMap consultarExamentCtBasica(Connection con, String codigoExamenCt, int institucion);
	
	//-- Fin Metodos
	
}