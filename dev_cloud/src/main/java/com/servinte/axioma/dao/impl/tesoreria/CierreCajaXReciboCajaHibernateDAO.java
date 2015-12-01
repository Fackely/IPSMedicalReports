package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXReciboCajaDAO;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CierreCajaXReciboCajaDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICierreCajaXReciboCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CierreCajaXReciboCajaDelegate.
 */

public class CierreCajaXReciboCajaHibernateDAO implements ICierreCajaXReciboCajaDAO {

	private CierreCajaXReciboCajaDelegate cierreCajaXReciboCajaDelegate;

	public CierreCajaXReciboCajaHibernateDAO() {
		cierreCajaXReciboCajaDelegate  = new CierreCajaXReciboCajaDelegate();
	}

	@Override
	public boolean asociarReciboCajaConCierreTurno(List<RecibosCaja> listaRecibosCajaTurno,	MovimientosCaja movimientosCaja) {
		
		return cierreCajaXReciboCajaDelegate.asociarReciboCajaConCierreTurno(listaRecibosCajaTurno, movimientosCaja);
	}
}
