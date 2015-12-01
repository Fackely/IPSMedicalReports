package com.servinte.axioma.dao.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.EntregaTransportadoraDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IEntregaTransportadoraValoresDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see EntregaTransportadoraDelegate.
 */

public class EntregaTransportadoraValoresHibernateDAO implements IEntregaTransportadoraValoresDAO{

	private EntregaTransportadoraDelegate entrTransportadoraDelegate;

	public EntregaTransportadoraValoresHibernateDAO() {
		entrTransportadoraDelegate  = new EntregaTransportadoraDelegate();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO#obtenerEntregasTransportadoraXTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<EntregaTransportadora> obtenerEntregasTransportadoraXTurnoCaja(TurnoDeCaja turnoDeCaja) {
	
		return entrTransportadoraDelegate.obtenerEntregasTransportadoraXTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO#obtenerFechaUltimoMovimientoEntTransValores(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoEntTransValores(TurnoDeCaja turnoDeCaja) {
		
		return entrTransportadoraDelegate.obtenerFechaUltimoMovimientoEntTransValores(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO#obtenerEntregasTransportadoraValores(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValores(MovimientosCaja movimientosCaja) {
		
		return entrTransportadoraDelegate.obtenerEntregasTransportadoraValores(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO#obtenerEntregasTransportadoraValores(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValoresPorFecha(MovimientosCaja movimientosCaja) {
		
		return entrTransportadoraDelegate.obtenerEntregasTransportadoraValoresPorFecha(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO#obtenerEntregaTransportadoraPorCodigo(long)
	 */
	@Override
	public EntregaTransportadora obtenerEntregaTransportadoraPorCodigo(long codigoPk) {
	
		return entrTransportadoraDelegate.obtenerEntregaTransportadoraPorCodigo(codigoPk);
	}

}
