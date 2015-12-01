package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITiposMovimientoCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITiposMovimientoCajaMundo;
import com.servinte.axioma.orm.TiposMovimientoCaja;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con los Tipos de Movimiento de caja
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ITiposMovimientoCajaMundo
 */

public class TiposMovimientoCajaMundo implements ITiposMovimientoCajaMundo {

	/**
	 * DAO de los Tipos de Movimiento de Caja.
	 */
	private ITiposMovimientoCajaDAO tiposMovimientoCajaDAO;

	public TiposMovimientoCajaMundo() {
		inicializar();
	}

	private void inicializar() {
		tiposMovimientoCajaDAO = TesoreriaFabricaDAO.crearTiposMovimientoCajaDAO();
	}
	
	
	@Override
	public List<TiposMovimientoCaja> obtenerListadoTiposArqueo() {

		//UtilidadTransaccion.getTransaccion().begin();
		
		List<TiposMovimientoCaja> listaTiposMovimientoCajas =  tiposMovimientoCajaDAO.obtenerListadoTiposArqueo();
		
		//UtilidadTransaccion.getTransaccion().commit();
		
		return listaTiposMovimientoCajas;
	}

	@Override
	public TiposMovimientoCaja findById(int codigoTipoMovimiento) {
		
		return tiposMovimientoCajaDAO.findById(codigoTipoMovimiento);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los turnos
	 * de apertura y cierre de las cajas
	 * 
	 * @return ArrayList<TiposMovimientoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<TiposMovimientoCaja> obtenerTiposMovimientoCajaFiltradoPorID(Integer[] filtro){
		return tiposMovimientoCajaDAO.obtenerTiposMovimientoCajaFiltradoPorID(filtro);
	}
	
	
}
