package com.servinte.axioma.bl.facturacion.interfaz;

import com.servinte.axioma.dto.facturacion.InfoCreacionHistoricoCargosDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que expone los servicios de Negocio correspondientes a la lógica asociada a la
 * Distribucion de Cuenta
 * 
 * @author hermorhu
 * @created 24-Nov-2012 
 */
public interface IDistribucionCuentaMundo {
	
	/**
	 * Metodo encargado de crear el historico de los cargos en una distribucion de cuentas
	 * @param infoCreacionHistoricoCargosDto
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 * @created 24-Nov-2012 
	 */
	boolean guardarHistoricoCargosXLiquidacionDistribucion (InfoCreacionHistoricoCargosDto infoCreacionHistoricoCargosDto) throws IPSException;

}
