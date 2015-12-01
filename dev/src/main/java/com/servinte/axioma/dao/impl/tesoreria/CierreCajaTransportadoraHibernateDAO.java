package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ICierreCajaTransportadoraDAO;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CierreCajaTransportadoraDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICierreCajaTransportadoraDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see CierreCajaTransportadoraDelegate
 */

public class CierreCajaTransportadoraHibernateDAO implements ICierreCajaTransportadoraDAO{

	
	private CierreCajaTransportadoraDelegate cierreCajaTransportadoraDelegate;

	public CierreCajaTransportadoraHibernateDAO() {
		cierreCajaTransportadoraDelegate  = new CierreCajaTransportadoraDelegate();
	}

	
	@Override
	public boolean asociarEntregaTransportadoraCierreCaja(List<EntregaTransportadora> listaEntregaTransportadora,MovimientosCaja movimientosCaja) {
		
		return cierreCajaTransportadoraDelegate.asociarEntregaTransportadoraCierreCaja(listaEntregaTransportadora, movimientosCaja);
	}

}
