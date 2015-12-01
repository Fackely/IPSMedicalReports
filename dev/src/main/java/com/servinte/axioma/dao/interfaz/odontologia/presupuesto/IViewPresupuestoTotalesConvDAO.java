package com.servinte.axioma.dao.interfaz.odontologia.presupuesto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;


/**
 * ]
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public interface IViewPresupuestoTotalesConvDAO {

	
	/**
	 * 
	 * Este m&eacute;todo se encarga de 
	 * @param dto
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public List<ViewPresupuestoTotalesConv> obtenerViewPresupuesto(DtoViewPresupuesTotalesConv dto);
	
}
