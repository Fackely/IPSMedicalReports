package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.servinte.axioma.dao.interfaz.tesoreria.IAceptacionTrasladoCajaDAO;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.AceptacionTrasladoCajaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IAceptacionTrasladoCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see AceptacionTrasladoCajaDelegate.
 */

public class AceptacionTrasladoCajaHibernateDAO implements IAceptacionTrasladoCajaDAO{

	private AceptacionTrasladoCajaDelegate aceptacionTrasladoCajaDelegate;

	
	public AceptacionTrasladoCajaHibernateDAO() {
		aceptacionTrasladoCajaDelegate  = new AceptacionTrasladoCajaDelegate();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IAceptacionTrasladoCajaDAO#obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<AceptacionTrasladoCaja> obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(TurnoDeCaja turnoDeCaja) {
	
		return aceptacionTrasladoCajaDelegate.obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(turnoDeCaja);
		
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IAceptacionTrasladoCajaDAO#obtenerTotalesAceptacionSolicitudFormaPagoNinguno(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesAceptacionSolicitudFormaPagoNinguno(TurnoDeCaja turnoDeCaja) {
		
		return aceptacionTrasladoCajaDelegate.obtenerTotalesAceptacionSolicitudFormaPagoNinguno(turnoDeCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IAceptacionTrasladoCajaDAO#existeAceptacionTrasladoPorSolicitudes(java.lang.String)
	 */
	@Override
	public boolean existeAceptacionTrasladoPorSolicitudes(ArrayList<Long> codigosSolicitudes) {
		
		return aceptacionTrasladoCajaDelegate.existeAceptacionTrasladoPorSolicitudes(codigosSolicitudes);
	}
}
