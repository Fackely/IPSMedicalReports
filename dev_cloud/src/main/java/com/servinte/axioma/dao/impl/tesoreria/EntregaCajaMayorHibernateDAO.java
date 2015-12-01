package com.servinte.axioma.dao.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.EntregaCajaMayorDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IEntregaCajaMayorDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see EntregaCajaMayorDelegate.
 */
public class EntregaCajaMayorHibernateDAO  implements IEntregaCajaMayorDAO{

	EntregaCajaMayorDelegate entregaCajaMayorDelegate;
	
	public EntregaCajaMayorHibernateDAO() {
		entregaCajaMayorDelegate = new EntregaCajaMayorDelegate();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO#obtenerEntregasCajaMayorPrincipalXTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<EntregaCajaMayor> obtenerEntregasCajaMayorPrincipalXTurnoCaja(
			TurnoDeCaja turnoDeCaja) {
		
		return entregaCajaMayorDelegate.obtenerEntregasCajaMayorPrincipalXTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO#obtenerFechaUltimoMovimientoEntregaCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoEntregaCaja(TurnoDeCaja turnoDeCaja) {
	
		return entregaCajaMayorDelegate.obtenerFechaUltimoMovimientoEntregaCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO#obtenerEntregasCajaMayor(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasCajaMayor(MovimientosCaja movimientosCaja) {
		
		return entregaCajaMayorDelegate.obtenerEntregasCajaMayor(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO#obtenerEntregasCajaMayorPorFecha(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasCajaMayorPorFecha(MovimientosCaja movimientosCaja) {
		
		return entregaCajaMayorDelegate.obtenerEntregasCajaMayorPorFecha(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO#obtenerEntregaCajaMayorPorCodigo(long)
	 */
	@Override
	public EntregaCajaMayor obtenerEntregaCajaMayorPorCodigo(long codigoEntrega) {
		
		return entregaCajaMayorDelegate.obtenerEntregaCajaMayorPorCodigo(codigoEntrega);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO#obtenerUsuarioArqueo(long)
	 */
	public String obtenerUsuarioArqueo (long codigoEntrega){
		return entregaCajaMayorDelegate.obtenerUsuarioArqueo(codigoEntrega);
	}
	
}