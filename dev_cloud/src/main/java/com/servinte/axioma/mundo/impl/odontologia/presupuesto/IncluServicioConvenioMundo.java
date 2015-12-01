
package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.math.BigDecimal;

import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluServicioConvenioDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluServicioConvenioMundo;
import com.servinte.axioma.orm.IncluServicioConvenio;


/**
 * Esta clase se encarga de implementar la l&oacute;gica de negocio 
 * para la entidad {@link IncluServicioConvenio}
 *
 * @autor Jorge Armando Agudelo Quintero
 *
 */

public class IncluServicioConvenioMundo  implements IIncluServicioConvenioMundo{

	IIncluServicioConvenioDAO incluServicioConvenioDAO;
	
	/**
	 * Constructor de la clase
	 */
	public IncluServicioConvenioMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que se encarga de inicializar el objeto DAO encargado de manejar 
	 * la capa de integraci&oacute;n de los objetos {@link IncluServicioConvenio}
	 */
	private void inicializar() {
		incluServicioConvenioDAO = PresupuestoFabricaDAO.crearIncluServicioConvenioDAO();
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluServicioConvenioMundo#actualizarPorcentajeDctoOdontologico(long, java.math.BigDecimal)
	 */
	@Override
	public boolean actualizarPorcentajeDctoOdontologico(long codigoIncluPresuEncabezado, BigDecimal porcentajeDescuentoOdontologico) {
		
		return incluServicioConvenioDAO.actualizarPorcentajeDctoOdontologico(codigoIncluPresuEncabezado, porcentajeDescuentoOdontologico);
	}
}
