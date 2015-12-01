package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.GeneracionInterfaz1EDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseGeneracionInterfaz1EDao;
import com.princetonsa.dto.interfaz.DtoInterfazLineaS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;

public class PostgresqlGeneracionInterfaz1EDao implements GeneracionInterfaz1EDao
{
	/**
	 * consulta informacion de tipo doc por tipo movimiento 1e
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap> consultarTipoDocXTipoMov1E(Connection con,HashMap parametros)
	{
		return SqlBaseGeneracionInterfaz1EDao.consultarTipoDocXTipoMov1E(con, parametros);
	}
	
	/**
	 * Método para consultar los documentos contables de un tipo de movimiento específico
	 * @param con
	 * @param parametrizacion
	 * @return
	 */
	public ArrayList<DtoInterfazLineaS1E> consultarDocumentosContables(Connection con,DtoInterfazS1EInfo parametrizacion)
	{
		return SqlBaseGeneracionInterfaz1EDao.consultarDocumentosContables(con, parametrizacion); 
	}
}
