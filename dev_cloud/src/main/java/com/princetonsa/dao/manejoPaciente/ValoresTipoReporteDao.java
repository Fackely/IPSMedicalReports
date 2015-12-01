package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

public interface ValoresTipoReporteDao 
{

	
	/**
	 * 
	 * @param con
	 * @param codigoReporte
	 * @return
	 */
	HashMap consultarInfoTipoReporte(Connection con, String codigoReporte);
	
	/**
	 * 
	 * @param con
	 * @param codigoReporte
	 * @return
	 */
	HashMap consultarParametrizacion(Connection con, String codigoReporte);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	boolean eliminarRegistro(Connection con, String codigo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificar(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertar(Connection con, HashMap vo);
	
	/**
	 * Método implementado para verificar si existe parametrizacion de los valores
	 * de un tipo de reporte específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean existeParametrizacionValoresTipoReporte(Connection con,HashMap campos);
}
