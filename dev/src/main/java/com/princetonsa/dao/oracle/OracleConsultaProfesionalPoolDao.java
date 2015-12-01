/* @(#)OracleConsultaProfesionalPoolDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import com.princetonsa.dao.ConsultaProfesionalPoolDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaProfesionalPoolDao;

/**
 * Interfaz para el acceder a la fuente de datos de la consulta
 * de profesionales por pool
 *
 * @version 1.0, 17 /Mar/ 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
 
public class OracleConsultaProfesionalPoolDao implements ConsultaProfesionalPoolDao
{
	/**
	 * Metodo para consultar los datos de los pooles de un medico dado 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public HashMap consultaProfesionalPool(Connection con,int codigoMedico)  throws SQLException
	{
		return SqlBaseConsultaProfesionalPoolDao.consultaProfesionalPool(con, codigoMedico);
	}
}