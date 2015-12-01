/*
 * Creado el 28/12/2005
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 * 
 * CopyRight Princeton S.A.
 * 28/12/2005
 */
public interface ConsultaHonorariosMedicosDao
{

	/**
	 * Metodo que consulta los profesionales de la salud.
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarProfesionalesSalud(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param profesional
	 * @param restricciones 
	 * @return
	 */
	public abstract HashMap consultarHonorariosProfesional(Connection con, int institucion, int profesional, HashMap restricciones);
	

}
