package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Interfaz para el acceder a la fuente de datos de la 
 * Consulta de profesional por Pool
 *
 * @version 1.0, 17 /Mar/ 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
 
public interface ConsultaProfesionalPoolDao 
{
	/**
	 * Metodo para consultar los datos de los pooles de un medico dado 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public HashMap consultaProfesionalPool(Connection con,int codigoMedico)  throws SQLException;
}