/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.mundo.impl.consultaexterna;

import com.servinte.axioma.dao.fabrica.consultaexterna.ConsultaExternaFabricaDAO;
import com.servinte.axioma.dao.interfaz.consultaexterna.IMultasCitasDAO;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasCitasMundo;
import com.servinte.axioma.orm.MultasCitas;

/**
 * Define la lógica de negocio para las funcionalidades relacionadas
 * con las Multas asociadas a las Citas.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class MultasCitasMundo implements IMultasCitasMundo {
	
	
	private IMultasCitasDAO multasCitasDAO;

	
	public MultasCitasMundo() {
		inicializar();
	}

	private void inicializar() {
		multasCitasDAO = ConsultaExternaFabricaDAO.crearMultasCitasDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasCitasMundo#obtenerMultaCitaPorCodigo(long)
	 */
	@Override
	public MultasCitas obtenerMultaCitaPorCodigo(long codigoPk) {
		
		return multasCitasDAO.obtenerMultaCitaPorCodigo(codigoPk);
	}
}
