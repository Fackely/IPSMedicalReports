package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IFormasPagoDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.TiposDetalleFormaPago;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con 
 * las Formas de Pago
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IFormasPagoMundo
 */


public class FormasPagoMundo implements IFormasPagoMundo {

	IFormasPagoDAO formasPagoDAO;
	
	public FormasPagoMundo() {
		inicializar();
	}

	private void inicializar() {
		formasPagoDAO = TesoreriaFabricaDAO.crearFormasPagoDAO();
	}

	@Override
	public List<FormasPago> obtenerFormasPagos() {
		
		//UtilidadTransaccion.getTransaccion().begin();
		List<FormasPago> fp = formasPagoDAO.obtenerFormasPagos();
		//UtilidadTransaccion.getTransaccion().commit();
		return fp;
	}

	
	
	
	
	
	@Override
	public List<TiposDetalleFormaPago> obtenerTiposDetalleFormaPago() {
		return formasPagoDAO.obtenerTiposDetalleFormaPago();
	}

	
	@Override
	public FormasPago findById(int consecutivo) {
		
		return formasPagoDAO.findById(consecutivo);
	}

	@Override
	public List<DtoFormaPago> obtenerFormasPagos(DtoFormaPago formaPagoFiltros)
	{
		return formasPagoDAO.obtenerFormasPagos(formaPagoFiltros);
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo#obtenerFormasPagosActivos()
	 */
	@Override
	public List<FormasPago> obtenerFormasPagosActivos() {
		// TODO Auto-generated method stub
		return formasPagoDAO.obtenerFormasPagosActivos();
	}
}
