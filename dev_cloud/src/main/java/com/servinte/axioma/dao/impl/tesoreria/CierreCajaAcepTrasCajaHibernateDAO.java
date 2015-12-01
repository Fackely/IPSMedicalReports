package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaAcepTrasCajaDAO;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CierreCajaAcepTrasCajaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ICierreCajaAcepTrasCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CierreCajaAcepTrasCajaDelegate.
 */


public class CierreCajaAcepTrasCajaHibernateDAO implements ICierreCajaAcepTrasCajaDAO {

	private CierreCajaAcepTrasCajaDelegate cierreCajaAcepTrasCajaDelegate;
	
	public CierreCajaAcepTrasCajaHibernateDAO() {
		
		cierreCajaAcepTrasCajaDelegate = new CierreCajaAcepTrasCajaDelegate();
	}
	
	@Override
	public boolean asociarAceptacionesSolicitudTrasladoCierreCaja(List<AceptacionTrasladoCaja> listaSolicitudesAceptadas,MovimientosCaja movimientosCaja) {
		
		return cierreCajaAcepTrasCajaDelegate.asociarAceptacionesSolicitudTrasladoCierreCaja(listaSolicitudesAceptadas, movimientosCaja);
	}

}
