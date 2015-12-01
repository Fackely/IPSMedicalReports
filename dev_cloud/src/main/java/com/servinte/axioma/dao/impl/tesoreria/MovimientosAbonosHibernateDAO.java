package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosAbonosDAO;
import com.servinte.axioma.orm.delegate.tesoreria.MovimientosAbonosDelegate;

/**
 * Implementación de la interfaz {@link IMovimientosAbonosDAO}
 * 
 * @author Luis Fernando Hincapié Ospina
 * @since 08/03/2011
 */
public class MovimientosAbonosHibernateDAO implements
		IMovimientosAbonosDAO {

	private MovimientosAbonosDelegate movimientosAbonosDelegate;

	public MovimientosAbonosHibernateDAO() {
		movimientosAbonosDelegate = new MovimientosAbonosDelegate();
	}

	@Override
	public String validarSiTieneMovimientosAbonos(int codigoPaciente){
		
		return movimientosAbonosDelegate.validarSiTieneMovimientosAbonos(codigoPaciente);
	}

	
	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerMovimientosAbonosPorInstitucion(int codigoInstitucion){
		return movimientosAbonosDelegate.obtenerMovimientosAbonosPorInstitucion(codigoInstitucion);
	}
	
	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerConsolidadoPorTipoMovimiento(int codigoPaciente){
		return movimientosAbonosDelegate.obtenerConsolidadoPorTipoMovimiento(codigoPaciente);
	}

}
