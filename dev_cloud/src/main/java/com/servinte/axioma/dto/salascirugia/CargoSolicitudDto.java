/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;

/**
 * 
 * @author Oscar Pulido
 * @created 22/07/2013
 *
 */
public class CargoSolicitudDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 251731680553976516L;
	/**
	 * Estado de facturacion del cargo
	 */
	private boolean estadoFacturacion;
	/**
	 * Estado de liquidacion del cargo
	 */
	private boolean estadoLiquidacion;
	
	

	public CargoSolicitudDto() {

	}
	
	
	public boolean isEstadoFacturacion() {
		return estadoFacturacion;
	}
	public void setEstadoFacturacion(boolean estadoFacturacion) {
		this.estadoFacturacion = estadoFacturacion;
	}
	public boolean isEstadoLiquidacion() {
		return estadoLiquidacion;
	}
	public void setEstadoLiquidacion(boolean estadoLiquidacion) {
		this.estadoLiquidacion = estadoLiquidacion;
	}
}
