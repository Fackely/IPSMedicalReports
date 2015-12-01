package com.servinte.axioma.mundo.interfaz.presupuesto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;


/**
 * 
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public interface IViewPresupuestoTotalesConvMundo {
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de obtenere los datos
	 * sobre el presupuesto contratado a trav&eacute;s de
	 * la vista ViewPresupuestoTotalesConv
	 * @param dto
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public List<ViewPresupuestoTotalesConv> obtenerViewPresupuesto(DtoViewPresupuesTotalesConv dto);

}
