package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.HistoDetalleMonto;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * de la entidad HistoDetalleMonto
 * 
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public interface IHistoDetalleMontoDAO {
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico del detalle
	 * de un monto de cobro
	 * 
	 * @param HistoDetalleMonto
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetalleMonto histoDetalleMonto);
	
	
	/**
	 * 
	 * Este Método se encarga de buscar el histórico del detalle de un monto
	 * de cobro
	 * 
	 * @param int
	 * @return HistoDetalleMonto
	 * @author, Angela Maria Aguirre
	 *
	 */
	public HistoDetalleMonto buscarHistoricoDetallePorID(int idHistorico);

}
