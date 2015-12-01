package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXDevolReciboDAO;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CierreCajaXDevolReciboDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICierreCajaXDevolReciboDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CierreCajaXDevolReciboDelegate.
 */

public class CierreCajaXDevolReciboHibernateDAO implements ICierreCajaXDevolReciboDAO {

	private CierreCajaXDevolReciboDelegate cierreCajaXDevolReciboDelegate;

	public CierreCajaXDevolReciboHibernateDAO() {
		cierreCajaXDevolReciboDelegate  = new CierreCajaXDevolReciboDelegate();
	}

	@Override
	public boolean asociarDevolucionesCierreCaja(List<DtoReciboDevolucion> listaDevolRecibosCajaTurno, MovimientosCaja movimientosCaja) {
		
		return cierreCajaXDevolReciboDelegate.asociarDevolucionesCierreCaja(listaDevolRecibosCajaTurno, movimientosCaja);
	}

}
