package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXEntregaCajaDAO;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CierreCajaXEntregaCajaDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICierreCajaXEntregaCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CierreCajaXEntregaCajaDelegate
 */

public class CierreCajaXEntregaCajaHibernateDAO implements ICierreCajaXEntregaCajaDAO{

	
	private CierreCajaXEntregaCajaDelegate cierreCajaXEntregaCajaDelegate;

	public CierreCajaXEntregaCajaHibernateDAO() {
		cierreCajaXEntregaCajaDelegate  = new CierreCajaXEntregaCajaDelegate();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaXEntregaCajaDAO#asociarEntregaCajaCierreCaja(java.util.List, com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public boolean asociarEntregaCajaCierreCaja(List<EntregaCajaMayor> listaEntregasCajaMayorPrincipal,	MovimientosCaja movimientosCaja) {
		
		return cierreCajaXEntregaCajaDelegate.asociarEntregaCajaCierreCaja(listaEntregasCajaMayorPrincipal, movimientosCaja);
	}


}
