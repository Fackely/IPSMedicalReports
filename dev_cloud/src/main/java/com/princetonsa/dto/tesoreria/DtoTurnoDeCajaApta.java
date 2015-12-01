/**
 * 
 */
package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

/**
 * Encaps&uacute;la la respuesta a la validaci&oacute;n si la caja esta apta o no para apertura
 * @author Cristhian Murillo
 */
@SuppressWarnings("serial")
public class DtoTurnoDeCajaApta implements Serializable{
	
	
	/**
	 * Indica si la caja tiene o no una solicitud de traslado
	 */
	private boolean tieneSolicitudDeTraslado;
	
	/**
	 * Indica si el cajero tiene o no turnos de caja cerrados
	 */
	private boolean  tieneTurnoCajeroCerrado;
	
	/**
	 * Indica si el cajero tiene o no turnos de caja cerrados
	 */
	private boolean  tieneTurnoCajaCerrado;
	
	/**
	 * Indica si el usuario es el que abrio la caja
	 */
	private boolean usuarioTurnoCaja;
	
	
	
	
	
	/**
	 * @return completamenteApto
	 */
	public boolean isCompletamenteApto() 
	{
		boolean apta = false;
	
		if(isTieneSolicitudDeTraslado() == false){
			if(isTieneTurnoCajaCerrado()){
				if(isTieneTurnoCajeroCerrado()){
					apta = true;
				}
			}
		}
		
		return apta;
	}

	public boolean isTurnoAbierto() 
	{
		boolean apta = false;

		if(!isTieneTurnoCajaCerrado()){
			if(!isTieneTurnoCajeroCerrado()){
				apta = true;
			}
		}
		return apta;
	}

	
	
	/**
	 * @return tieneSolicitudDeTraslado
	 */
	public boolean isTieneSolicitudDeTraslado() {
		return tieneSolicitudDeTraslado;
	}
	
	/**
	 * @param tieneSolicitudDeTraslado  tieneSolicitudDeTraslado a setear
	 */
	public void setTieneSolicitudDeTraslado(boolean tieneSolicitudDeTraslado) {
		this.tieneSolicitudDeTraslado = tieneSolicitudDeTraslado;
	}
	
	/**
	 * @return tieneTurnoCajeroCerrado
	 */
	public boolean isTieneTurnoCajeroCerrado() {
		return tieneTurnoCajeroCerrado;
	}
	
	/**
	 * @param tieneTurnoCajeroCerrado  tieneTurnoCajeroCerrado a setear
	 */
	public void setTieneTurnoCajeroCerrado(boolean tieneTurnoCajeroCerrado) {
		this.tieneTurnoCajeroCerrado = tieneTurnoCajeroCerrado;
	}
	
	/**
	 * @return the usuarioTurnoCaja
	 */
	public boolean isUsuarioTurnoCaja() {
		return usuarioTurnoCaja;
	}

	/**
	 * @param usuarioTurnoCaja the usuarioTurnoCaja to set
	 */
	public void setUsuarioTurnoCaja(boolean usuarioTurnoCaja) {
		this.usuarioTurnoCaja = usuarioTurnoCaja;
	}

	/**
	 * @return the tieneTurnoCajaCerrado
	 */
	public boolean isTieneTurnoCajaCerrado() {
		return tieneTurnoCajaCerrado;
	}

	/**
	 * @param tieneTurnoCajaCerrado the tieneTurnoCajaCerrado to set
	 */
	public void setTieneTurnoCajaCerrado(boolean tieneTurnoCajaCerrado) {
		this.tieneTurnoCajaCerrado = tieneTurnoCajaCerrado;
	}

	
	
}
