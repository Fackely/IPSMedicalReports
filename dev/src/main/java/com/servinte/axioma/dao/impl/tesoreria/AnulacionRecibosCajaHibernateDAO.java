package com.servinte.axioma.dao.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.IAnulacionRecibosCajaDAO;
import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.AnulacionRecibosCajaDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IAnulacionRecibosCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see AnulacionRecibosCajaDelegate
 */


public class AnulacionRecibosCajaHibernateDAO implements IAnulacionRecibosCajaDAO{

	
	private AnulacionRecibosCajaDelegate anulacionRecibosCajaDelegate;
	
	public AnulacionRecibosCajaHibernateDAO() {
		
		anulacionRecibosCajaDelegate = new AnulacionRecibosCajaDelegate();
	}
	
	@Override
	public List<AnulacionRecibosCaja> obtenerAnulacionesRecibosCajaXTurnoCaja(TurnoDeCaja turnoDeCaja) {
		
		return anulacionRecibosCajaDelegate.obtenerAnulacionesRecibosCajaXTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IAnulacionRecibosCajaDAO#obtenerFechaUltimoMovimientoAnulaciones(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoAnulaciones(TurnoDeCaja turnoDeCaja) {
		
		return anulacionRecibosCajaDelegate.obtenerFechaUltimoMovimientoAnulaciones(turnoDeCaja);
	}

}
