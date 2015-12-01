package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntregaTransportadoraValoresDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * Entregas a Transportadora de Valores
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IEntregaTransportadoraValoresMundo
 */

public class EntregaTransportadoraValoresMundo implements IEntregaTransportadoraValoresMundo {


	private IEntregaTransportadoraValoresDAO entregaTransportadoraValoresDAO;
	
	public EntregaTransportadoraValoresMundo() {
		inicializar();
	}

	private void inicializar() {
		entregaTransportadoraValoresDAO = TesoreriaFabricaDAO.crearEntregaTransportadoraValoresDAO();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo#obtenerEntregasTransportadoraXTurnoCaja(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<EntregaTransportadora> obtenerEntregasTransportadoraXTurnoCaja(TurnoDeCaja turnoDeCaja) {
		
		return entregaTransportadoraValoresDAO.obtenerEntregasTransportadoraXTurnoCaja(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo#obtenerFechaUltimoMovimientoEntTransValores(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoEntTransValores(TurnoDeCaja turnoDeCaja) {
		
		return entregaTransportadoraValoresDAO.obtenerFechaUltimoMovimientoEntTransValores(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo#obtenerEntregasTransportadoraValores(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValores(MovimientosCaja movimientosCaja) {
		
		return entregaTransportadoraValoresDAO.obtenerEntregasTransportadoraValores(movimientosCaja);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo#obtenerEntregasTransportadoraValoresPorFecha(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValoresPorFecha(MovimientosCaja movimientosCaja) {
		
		return entregaTransportadoraValoresDAO.obtenerEntregasTransportadoraValoresPorFecha(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo#obtenerEntregaTransportadoraPorCodigo(long)
	 */
	@Override
	public EntregaTransportadora obtenerEntregaTransportadoraPorCodigo(	long codigoPk) {
		
		return entregaTransportadoraValoresDAO.obtenerEntregaTransportadoraPorCodigo(codigoPk);
	}

}
