package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IViewPresupuestoTotalesConvDAO;
import com.servinte.axioma.mundo.interfaz.presupuesto.IViewPresupuestoTotalesConvMundo;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;

/**
 * 
 * Esta clase se encarga de
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public class ViewPresupuestoTotalesConvMundo implements IViewPresupuestoTotalesConvMundo {

	
	/**
	 * Instancia de la clase IViewPresupuestoTotalesConvDAO
	 */
	private IViewPresupuestoTotalesConvDAO daoView;
	
	
	/**
	 * M&eacute;todo constructor de la clase
	 *
	 * @author Yennifer Guerrero
	 */
	public ViewPresupuestoTotalesConvMundo(){
		this.setDaoView(PresupuestoFabricaDAO.crearViewPresupuestoTotalDAO());
		
	};
	
	@Override
	public List<ViewPresupuestoTotalesConv> obtenerViewPresupuesto(
			DtoViewPresupuesTotalesConv dto) {
		return daoView.obtenerViewPresupuesto(dto);
	}
	
	public void setDaoView(IViewPresupuestoTotalesConvDAO daoView) {
		this.daoView = daoView;
	}

	public IViewPresupuestoTotalesConvDAO getDaoView() {
		return daoView;
	}
}
