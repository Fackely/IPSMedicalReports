package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.HibernateException;

import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.servinte.axioma.dao.interfaz.administracion.IMedicosDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IViewPresupuestoTotalesConvDAO;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.ViewPresupuestoTotalesConvDelegate;

/**
 * 
 * Implementaci&oacute;n de la interfaz {@link IViewPresupuestoTotalesConvDAO}.
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public class ViewPresupuestoTotalesConvDAO implements IViewPresupuestoTotalesConvDAO {

	/**
	 * Instancia de la clase ViewPresupuestoTotalesConvDelegate
	 */
	private ViewPresupuestoTotalesConvDelegate delegate;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public ViewPresupuestoTotalesConvDAO(){
		
		this.delegate= new ViewPresupuestoTotalesConvDelegate();
	}
	
	
	@Override
	public List<ViewPresupuestoTotalesConv> obtenerViewPresupuesto(	DtoViewPresupuesTotalesConv dto) {
		
		
		List<ViewPresupuestoTotalesConv> listTMP= new ArrayList<ViewPresupuestoTotalesConv>();
		try {
		
			listTMP=delegate.obtenerViewPresupuesto(dto);
		
		} catch (HibernateException e) {
		
			
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return listTMP;
	}

	public void setDelegate(ViewPresupuestoTotalesConvDelegate delegate) {
		this.delegate = delegate;
	}

	public ViewPresupuestoTotalesConvDelegate getDelegate() {
		return delegate;
	}
	

}
