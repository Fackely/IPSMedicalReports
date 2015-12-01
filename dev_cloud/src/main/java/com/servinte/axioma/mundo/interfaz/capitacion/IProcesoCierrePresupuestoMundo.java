package com.servinte.axioma.mundo.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;


/**
 * @author Cristhian Murillo
 */
public interface IProcesoCierrePresupuestoMundo {

	
	/**
	 * Genera el cierre del presupuesto
	 * @param presupuestoCapitado
	 *
	 * @autor Cristhian Murillo
	 */
	public void generarCierrePresupuesto(DtoProcesoPresupuestoCapitado presupuestoCapitado);
	
	
	/**
	 * Genera el cierre del presupuesto
	 * @param presupuestoCapitado
	 *
	 * @autor Cristhian Murillo
	 */
	public void generarCierrePresupuestoAutomatico(DtoProcesoPresupuestoCapitado presupuestoCapitado);
		
	
}
