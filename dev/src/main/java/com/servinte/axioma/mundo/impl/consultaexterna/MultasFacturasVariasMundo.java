/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.mundo.impl.consultaexterna;

import com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasFacturasVariasMundo;
import com.servinte.axioma.orm.MultasFacturasVarias;

/**
 * Define la lógica de negocio para las funcionalidades relacionadas
 * con las Multas asociadas a las Facturas Varias.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class MultasFacturasVariasMundo implements IMultasFacturasVariasMundo {
	
	
	//TODO Esta pendiente por continuar la estructura.
	
	public MultasFacturasVariasMundo() {
		inicializar();
	}

	private void inicializar() {
		
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasFacturasVariasMundo#obtenerMultaFacturaVariaPorCodigo(long)
	 */
	@Override
	public MultasFacturasVarias obtenerMultaFacturaVariaPorCodigo(long codigoPk) {
		// TODO Auto-generated method stub
		return null;
	}
}
