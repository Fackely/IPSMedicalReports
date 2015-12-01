package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.servinte.axioma.dao.interfaz.tesoreria.ITrasladosAbonosDAO;
import com.servinte.axioma.orm.TrasladosAbonos;
import com.servinte.axioma.orm.delegate.tesoreria.TrasladosAbonosDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ITrasladosAbonosDAO}.
 * 
 * @author Cristhian Murillo
 * @see TrasladosAbonosDelegate.
 */

public class TrasladosAbonosHibernateDAO implements ITrasladosAbonosDAO{

	
	private TrasladosAbonosDelegate delegate = new TrasladosAbonosDelegate();

	
	@Override
	public boolean guardarTraslado(TrasladosAbonos transientInstance) {
		return delegate.guardarTraslado(transientInstance);
	}


	@Override
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(
			DtoConsultaTrasladoAbonoPAciente dtoConsulta) {
		return delegate.obtenerDetallesTrasladoAbonos(dtoConsulta);
	}
	
	
}
