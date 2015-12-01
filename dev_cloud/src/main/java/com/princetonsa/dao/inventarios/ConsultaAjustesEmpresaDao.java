/*
 * 
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * 
 * @author axioma
 *
 */
public interface ConsultaAjustesEmpresaDao
{

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract ResultSetDecorator busquedaGeneralAjustes(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public abstract ResultSetDecorator consltarDetalleAjuste(Connection con, double codigoAjuste);

}
