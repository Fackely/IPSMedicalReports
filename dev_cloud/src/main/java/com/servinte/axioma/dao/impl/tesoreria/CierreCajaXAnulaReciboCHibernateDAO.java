package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXAnulaReciboCDAO;
import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CierreCajaXAnulaReciboCDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICierreCajaXAnulaReciboCDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CierreCajaXAnulaReciboCDelegate.
 */

public class CierreCajaXAnulaReciboCHibernateDAO implements ICierreCajaXAnulaReciboCDAO {

	private CierreCajaXAnulaReciboCDelegate cierreCajaXAnulaReciboCDelegate;

	public CierreCajaXAnulaReciboCHibernateDAO() {
		cierreCajaXAnulaReciboCDelegate  = new CierreCajaXAnulaReciboCDelegate();
	}

	
	@Override
	public boolean asociarAnulacionesRecibosCajaConCierreTurno(List<AnulacionRecibosCaja> listaAnulacionesRecibosCaja, MovimientosCaja movimientosCaja) {
	
		return cierreCajaXAnulaReciboCDelegate.asociarAnulacionesRecibosCajaConCierreTurno(listaAnulacionesRecibosCaja, movimientosCaja);
	}

}
