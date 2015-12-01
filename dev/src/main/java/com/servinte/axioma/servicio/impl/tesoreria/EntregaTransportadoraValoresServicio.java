package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.Date;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.IEntregaTransportadoraValoresServicio;


/**
 * 
 * Implementaci&oacute;n de la interfaz {@link IEntregaTransportadoraValoresServicio}
 * @author Diana Carolina G
 * @since 25/07/2010
 */
public class EntregaTransportadoraValoresServicio implements IEntregaTransportadoraValoresServicio {

	private IEntregaTransportadoraValoresMundo entregaTransportadoraValoresMundo;
	
	public EntregaTransportadoraValoresServicio(){
		entregaTransportadoraValoresMundo=TesoreriaFabricaMundo.crearEntregaTransportadoraValoresMundo();
	}
	
	
	/**
	 * M&eacute;todo que retorna una entrega a Transportadora de valores asociada a un c&oacute;digo espec&iacute;fico.
	 * 
	 * @param codigoPk
	 * @return
	 */
	public EntregaTransportadora obtenerEntregaTransportadoraPorCodigo(long codigoPk) {
	
		return entregaTransportadoraValoresMundo.obtenerEntregaTransportadoraPorCodigo(codigoPk);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IEntregaTransportadoraValoresServicio#obtenerFechaUltimoMovimientoEntTransValores(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoEntTransValores(TurnoDeCaja turnoDeCaja) {
		
		return entregaTransportadoraValoresMundo.obtenerFechaUltimoMovimientoEntTransValores(turnoDeCaja);
	}

}
