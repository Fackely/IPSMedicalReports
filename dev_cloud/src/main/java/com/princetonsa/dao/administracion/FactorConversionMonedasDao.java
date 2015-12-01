package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.administracion.DtoFactorConversionMonedas;

/**
 * 
 * @author wilson
 *
 */
public interface FactorConversionMonedasDao 
{
	/**
	 * Consulta  
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap cargar(Connection con, DtoFactorConversionMonedas dtoFactor);
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, DtoFactorConversionMonedas dtoFactor);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificar(Connection con, DtoFactorConversionMonedas dtoFactor);
}
