
package com.servinte.axioma.mundo.interfaz.consultaexterna;

import com.servinte.axioma.orm.MultasFacturasVarias;

/**
 * Define la l�gica de negocio relacionada con las funcionalidades 
 * de las Multas asociadas a las Facturas Varias
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface IMultasFacturasVariasMundo {

	
	/**
	 * M�todo que se encarga de buscar el registro de la Multa
	 * asociada a una Factura Varia.
	 * 
	 * @param codigoPk
	 * @return
	 */
	public MultasFacturasVarias obtenerMultaFacturaVariaPorCodigo(long codigoPk);
}
