package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.interfaz.DtoInterfazLineaS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;

public interface GeneracionInterfaz1EDao
{
	/**
	 * consulta informacion de tipo doc por tipo movimiento 1e
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap> consultarTipoDocXTipoMov1E(Connection con,HashMap parametros);
	
	/**
	 * Método para consultar los documentos contables de un tipo de movimiento específico
	 * @param con
	 * @param parametrizacion
	 * @return
	 */
	public ArrayList<DtoInterfazLineaS1E> consultarDocumentosContables(Connection con,DtoInterfazS1EInfo parametrizacion);
}