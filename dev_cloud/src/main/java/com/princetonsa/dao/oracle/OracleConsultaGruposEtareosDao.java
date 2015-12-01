/*
 * @(#)OracleConsultaGruposEtareosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.ConsultaGruposEtareosDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaGruposEtareosDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 26 /May/ 2006
 */
public class OracleConsultaGruposEtareosDao implements ConsultaGruposEtareosDao 
{
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun unos parametros de busqueda
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposEtareos (Connection con, String fechaIncial, String fechaFinal, int codigoConvenio, int institucion) throws SQLException
	{
		return SqlBaseConsultaGruposEtareosDao.consultarGruposEtareos(con, fechaIncial, fechaFinal, codigoConvenio, institucion);
	}
}