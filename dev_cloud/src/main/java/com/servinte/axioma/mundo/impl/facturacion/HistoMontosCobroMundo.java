package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoMontosCobroDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontosCobroMundo;
import com.servinte.axioma.orm.HistoMontosCobro;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad HistoMontosCobro
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoMontosCobroMundo implements IHistoMontosCobroMundo {
	
	IHistoMontosCobroDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public HistoMontosCobroMundo(){
		dao = FacturacionFabricaDAO.crearHistoMontosCobroDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico de un
	 * monto de cobro
	 * 
	 * @param HistoMontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarHistoDetalleMontoCobro(HistoMontosCobro histoMonto){
		return dao.guardarHistoDetalleMontoCobro(histoMonto);
	}

}
