package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosAbonosDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosAbonosMundo;

/**
 * Implementación de la interfaz {@link IMovimientosAbonosMundo}
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 08/03/2011
 */
public class MovimientosAbonosMundo implements
	IMovimientosAbonosMundo {

	private IMovimientosAbonosDAO movimientosAbonosDAO;

	public MovimientosAbonosMundo() {
		movimientosAbonosDAO = TesoreriaFabricaDAO
				.crearMovimientosAbonosDAO();
	}

	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerMovimientosAbonosPorInstitucion(int codigoInstitucion){
		return movimientosAbonosDAO.obtenerMovimientosAbonosPorInstitucion(codigoInstitucion);
	}
	
	
	@Override
	public String validarSiTieneMovimientosAbonos(int codigoPaciente) {
		return movimientosAbonosDAO.validarSiTieneMovimientosAbonos(codigoPaciente);
	}
	
	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerConsolidadoPorTipoMovimiento(int codigoPaciente){
		return movimientosAbonosDAO.obtenerConsolidadoPorTipoMovimiento(codigoPaciente);
	}
}
