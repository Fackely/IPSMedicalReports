package com.servinte.axioma.dao.impl.facturacion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.facturacion.IViewFinalidadesServDAO;
import com.servinte.axioma.orm.ViewFinalidadesServ;
import com.servinte.axioma.orm.delegate.facturacion.ViewFinalidadesServDelegate;

public class ViewFinalidadesServDAO implements IViewFinalidadesServDAO {

	
	private ViewFinalidadesServDelegate delegate;
	
	
	public ViewFinalidadesServDAO(){
		this.setDelegate(new ViewFinalidadesServDelegate());
	}
	
	@Override
	public List<ViewFinalidadesServ> obtenerViewFinalidadesServ(
			int codigoServicio) {
		return delegate.obtenerViewFinalidadesServ(codigoServicio);
	}

	public void setDelegate(ViewFinalidadesServDelegate delegate) {
		this.delegate = delegate;
	}

	public ViewFinalidadesServDelegate getDelegate() {
		return delegate;
	}

}
