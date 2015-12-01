/* Princeton S.A (Parquesoft-Manizales)
*  Andrés Mauricio Ruiz Vélez
*  Creado 14-nov-2006 11:23:16
*/


package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.CrecimientoDesarrolloDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseCrecimientoDesarrolloDao;

public class PostgresqlCrecimientoDesarrolloDao implements CrecimientoDesarrolloDao 
{

	/**
	 * Metodo estandar para consultar la informacion del paciente.
	 * @param con
	 * @param parametros
	 * @return
	 */	
	public HashMap consultarInformacion(Connection con, HashMap parametros)
	{
		return SqlBaseCrecimientoDesarrolloDao.consultarInformacion(con, parametros);
	}
}
