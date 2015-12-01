package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IEntidadesFinancierasDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntidadesFinancierasMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Contiene la l&oacute;gica de Negocio para los turnos de caja
 * 
 * @author Cristhian Murillo
 * @see IEntidadesFinancierasMundo
 */

public class EntidadesFinancierasMundo implements IEntidadesFinancierasMundo{

	private IEntidadesFinancierasDAO entFinancierasDAO;  
	
	public EntidadesFinancierasMundo() {
		inicializar();
	}

	
	private void inicializar() {
		entFinancierasDAO = TesoreriaFabricaDAO.crearEntidadFinancieraDAO();
	}

	
	@Override
	public List<DtoEntidadesFinancieras> obtenerEntidadesPorInstitucion(int codigoInstitucion, boolean activo){
		
		UtilidadTransaccion.getTransaccion().begin();
		
		return entFinancierasDAO.obtenerEntidadesPorInstitucion(codigoInstitucion,activo);
	}

	@Override
	public ArrayList<DtoEntidadesFinancieras> consultarEntidadesFinancieras(boolean todas) {
		return entFinancierasDAO.consultarEntidadesFinancieras(todas);
	}
}
