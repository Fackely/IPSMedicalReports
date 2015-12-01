package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.dao.interfaz.tesoreria.IFormasPagoDAO;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.TiposDetalleFormaPago;
import com.servinte.axioma.orm.delegate.tesoreria.FormasPagoDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IFormasPagoDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see FormasPagoDelegate
 */


public class FormasPagoHibernateDAO  implements IFormasPagoDAO{

	FormasPagoDelegate formasPagoDelegate;
	
	public FormasPagoHibernateDAO() {
		formasPagoDelegate =  new FormasPagoDelegate();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IFormasPagoDAO#obtenerFormasPagos()
	 */
	@Override
	public List<FormasPago> obtenerFormasPagos() {
		
		return formasPagoDelegate.obtenerFormasPagos();
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IFormasPagoDAO#obtenerFormasPagosActivos()
	 */
	public List<FormasPago> obtenerFormasPagosActivos(){
		return 	formasPagoDelegate.obtenerFormasPagosActivos();
	}
	
	
	@Override
	public List<TiposDetalleFormaPago> obtenerTiposDetalleFormaPago() {
			return formasPagoDelegate.obtenerTiposDetalleFormaPago();
	}


	@Override
	public FormasPago findById(int consecutivo) {
		
		return formasPagoDelegate.findById(consecutivo);
	}


	@Override
	public List<DtoFormaPago> obtenerFormasPagos(DtoFormaPago formaPagoFiltros)
	{
		return formasPagoDelegate.obtenerFormasPagos(formaPagoFiltros);
	}

}
