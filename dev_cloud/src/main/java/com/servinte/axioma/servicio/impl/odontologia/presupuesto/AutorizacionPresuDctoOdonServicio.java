
package com.servinte.axioma.servicio.impl.odontologia.presupuesto;

import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonMundo;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link IAutorizacionPresuDctoOdonServicio}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see AutorizacionPresuDctoOdonServicio
 */

public class AutorizacionPresuDctoOdonServicio implements IAutorizacionPresuDctoOdonServicio {

	
	IAutorizacionPresuDctoOdonMundo autorizacionPresuDctoOdonMundo;
	
	/**
	 * Constructor de la clase
	 */
	public AutorizacionPresuDctoOdonServicio() {
		
		autorizacionPresuDctoOdonMundo = PresupuestoFabricaMundo.crearAutorizacionPresuDctoOdonMundo();
	}
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonServicio#eliminarAutorizacionPresuDctoOdon(long)
	 */
	@Override
	public boolean eliminarAutorizacionPresuDctoOdon(long codigoAutorizacionPresuDctoOdon) {
		
		return autorizacionPresuDctoOdonMundo.eliminarAutorizacionPresuDctoOdon(codigoAutorizacionPresuDctoOdon);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonServicio#guardarAutorizacionPresuDctoOdon(com.servinte.axioma.orm.AutorizacionPresuDctoOdon, long, java.lang.String)
	 */
	@Override
	public boolean guardarAutorizacionPresuDctoOdon(AutorizacionPresuDctoOdon autorizacionPresuDctoOdon,long codigoDescuento, String tipoDescuento) {
		
		return autorizacionPresuDctoOdonMundo.guardarAutorizacionPresuDctoOdon(autorizacionPresuDctoOdon, codigoDescuento, tipoDescuento);
	}

}
