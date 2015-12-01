
package com.servinte.axioma.mundo.interfaz.facturacion;

import com.servinte.axioma.orm.HistoricoEncabezado;

/**
 * Define la l&oacute;gica de negocio relacionada con
 * la entidad {@link HistoricoEncabezado}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IHistoricoEncabezadoMundo {

	/**
	 * Método que se encarga de realizar
	 * la consulta del objeto {@link HistoricoEncabezado}
	 * 
	 * @param codigoHistoricoEncabezado
	 * @return
	 */
	public HistoricoEncabezado findById (long codigoHistoricoEncabezado);
	
	/**
	 * Método que se encarga de realizar
	 * el registro del objeto {@link HistoricoEncabezado}
	 * 
	 * @param historicoEncabezado
	 * @return
	 */
	public boolean insertar (HistoricoEncabezado historicoEncabezado);
	
	/**
	 * Método que se encarga de eliminar
	 * el registro del objeto {@link HistoricoEncabezado}
	 * 
	 * @param historicoEncabezado
	 * @return
	 */
	public boolean eliminar (HistoricoEncabezado historicoEncabezado);
}
