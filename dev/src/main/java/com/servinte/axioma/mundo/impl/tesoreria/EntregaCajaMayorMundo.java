package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntregaCajaMayorDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * Entregas a Caja Mayor Principal
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IEntregaCajaMayorDAO
 */
public class EntregaCajaMayorMundo implements IEntregaCajaMayorMundo {

	private IEntregaCajaMayorDAO entregaCajaMayorDAO;
	
	 public EntregaCajaMayorMundo(){
		 
		inicializar();
	}

	private void inicializar() {
		entregaCajaMayorDAO = TesoreriaFabricaDAO.crearEntregaCajaMayorDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo#obtenerEntregasCajaMayorPrincipalXTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<EntregaCajaMayor> obtenerEntregasCajaMayorPrincipalXTurnoCaja(
			TurnoDeCaja turnoDeCaja) {
		
		return entregaCajaMayorDAO.obtenerEntregasCajaMayorPrincipalXTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo#obtenerFechaUltimoMovimientoEntregaCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoEntregaCaja(TurnoDeCaja turnoDeCaja) {
	
		return entregaCajaMayorDAO.obtenerFechaUltimoMovimientoEntregaCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo#obtenerEntregasCajaMayor(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasCajaMayor(MovimientosCaja movimientosCaja) {
		
		return entregaCajaMayorDAO.obtenerEntregasCajaMayor(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo#obtenerEntregasCajaMayorPorFecha(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasCajaMayorPorFecha(MovimientosCaja movimientosCaja) {
		
		return entregaCajaMayorDAO.obtenerEntregasCajaMayorPorFecha(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo#obtenerEntregaCajaMayorPorCodigo(long)
	 */
	@Override
	public EntregaCajaMayor obtenerEntregaCajaMayorPorCodigo(long codigoEntrega) {
		
		return entregaCajaMayorDAO.obtenerEntregaCajaMayorPorCodigo(codigoEntrega);
	}
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo#obtenerUsuarioArqueo(long)
	 */
	public String obtenerUsuarioArqueo (long codigoEntrega){
		return entregaCajaMayorDAO.obtenerUsuarioArqueo(codigoEntrega);
	}
}