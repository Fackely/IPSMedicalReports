package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ITiposMovimientoCajaDAO;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.delegate.tesoreria.TiposMovimientoCajaDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ITiposMovimientoCajaDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see TiposMovimientoCajaDelegate.
 */

public class TiposMovimientoCajaHibernateDAO implements ITiposMovimientoCajaDAO{

	private TiposMovimientoCajaDelegate tiposMovimientoCajaImpl;

	public TiposMovimientoCajaHibernateDAO() {
		tiposMovimientoCajaImpl  = new TiposMovimientoCajaDelegate();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ITiposMovimientoCajaDAO#obtenerListadoTiposArqueo()
	 */
	@Override
	public List<TiposMovimientoCaja> obtenerListadoTiposArqueo() {
		
		return tiposMovimientoCajaImpl.obtenerListadoTiposArqueo();
	}
	
	@Override
	public TiposMovimientoCaja findById (int codigoTipoMovimiento)
	{
		return tiposMovimientoCajaImpl.findById(codigoTipoMovimiento);
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
	public ArrayList<TiposMovimientoCaja> obtenerTiposMovimientoCajaFiltradoPorID(Integer[] filtro){
		return tiposMovimientoCajaImpl.obtenerTiposMovimientoCajaFiltradoPorID(filtro);
	}
}
