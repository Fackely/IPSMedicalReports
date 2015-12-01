

package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoricoEncabezadoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo;
import com.servinte.axioma.orm.HistoricoEncabezado;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad HistoricoEncabezadoMundo
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public class HistoricoEncabezadoMundo implements IHistoricoEncabezadoMundo{


	private IHistoricoEncabezadoDAO historicoEncabezadoDAO;
	
	/**
	 * 
	 */
	public HistoricoEncabezadoMundo() {
		
		inicializar();
	}
	
	private void inicializar() {
		historicoEncabezadoDAO = FacturacionFabricaDAO.crearHistoricoEncabezadoDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo#insertar(com.servinte.axioma.orm.HistoricoEncabezado)
	 */
	@Override
	public boolean insertar(HistoricoEncabezado historicoEncabezado) {
		
		return historicoEncabezadoDAO.insertar(historicoEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo#eliminar(com.servinte.axioma.orm.HistoricoEncabezado)
	 */
	@Override
	public boolean eliminar(HistoricoEncabezado historicoEncabezado) {
		
		return historicoEncabezadoDAO.eliminar(historicoEncabezado);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo#findById(long)
	 */
	@Override
	public HistoricoEncabezado findById(long codigoHistoricoEncabezado) {
		return historicoEncabezadoDAO.findById(codigoHistoricoEncabezado);
	}

}
