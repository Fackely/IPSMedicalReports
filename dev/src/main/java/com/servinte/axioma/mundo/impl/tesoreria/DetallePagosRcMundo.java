package com.servinte.axioma.mundo.impl.tesoreria;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetallePagosRcDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetallePagosRcMundo;
import com.servinte.axioma.orm.DetallePagosRc;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con
 * los DetallesPagosRc
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IDetallePagosRcMundo
 */

public class DetallePagosRcMundo implements IDetallePagosRcMundo {


	private IDetallePagosRcDAO detallePagosRcDAO;
	
	public DetallePagosRcMundo() {
		inicializar();
	}

	private void inicializar() {
		detallePagosRcDAO = TesoreriaFabricaDAO.crearDetallePagosRcDAO();
	}


	@Override
	public DetallePagosRc findById(int codigoDetalle) {
		
		return detallePagosRcDAO.findById(codigoDetalle);
	}
}
