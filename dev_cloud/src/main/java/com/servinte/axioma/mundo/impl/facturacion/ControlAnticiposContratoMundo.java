
package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IControlAnticiposContratoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo;
import com.servinte.axioma.orm.ControlAnticiposContrato;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad {@link ControlAnticiposContrato}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class ControlAnticiposContratoMundo implements IControlAnticiposContratoMundo {

	
	private IControlAnticiposContratoDAO controlAnticiposContratoDAO;
	
	public ControlAnticiposContratoMundo() {
		inicializar();
	}

	private void inicializar() {
		controlAnticiposContratoDAO = FacturacionFabricaDAO.crearControlAnticiposContratoDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo#determinarContratoRequiereAnticipo(int)
	 */
	@Override
	public ArrayList<ControlAnticiposContrato> determinarContratoRequiereAnticipo(int contrato) {
		
		return controlAnticiposContratoDAO.determinarContratoRequiereAnticipo(contrato);
	}

}
