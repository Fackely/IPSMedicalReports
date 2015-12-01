
package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.HistoricoEncabezado;

/**
 * Interfaz donde se define el comportamiento del
 * DAO para las funciones de {@link HistoricoEncabezado}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IHistoricoEncabezadoDAO {

	/**
	 * M�todo que se encarga de realizar
	 * la consulta del objeto {@link HistoricoEncabezado}
	 * 
	 * @param codigoHistoricoEncabezado
	 * @return
	 */
	public HistoricoEncabezado findById (long codigoHistoricoEncabezado);
	
	/**
	 * M�todo que se encarga de realizar
	 * el registro del objeto {@link HistoricoEncabezado}
	 * 
	 * @param historicoEncabezado
	 * @return
	 */
	public boolean insertar (HistoricoEncabezado historicoEncabezado);
	
	
	/**
	 * M�todo que se encarga de eliminar
	 * el registro del objeto {@link HistoricoEncabezado}
	 * 
	 * @param historicoEncabezado
	 * @return
	 */
	public boolean eliminar (HistoricoEncabezado historicoEncabezado);
}