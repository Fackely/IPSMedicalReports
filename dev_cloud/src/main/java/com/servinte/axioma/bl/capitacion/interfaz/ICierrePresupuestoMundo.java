package com.servinte.axioma.bl.capitacion.interfaz;

import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;

public interface ICierrePresupuestoMundo {

	
	
	/**
	 * Metodo que se encarga de recalcular el cierre de presupuesto.
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @param esServicios
	 * @throws IPSException
	 */
	public void recalcularCierreTemporalPresupuesto(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto, int institucion, boolean esServicios)throws IPSException;
}
