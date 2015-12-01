package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.TiposDetalleFormaPago;


/**
 * Servicio que le delega al negocio las operaciones relacionados con las Formas de
 * Pago 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.servicio.impl.tesoreria.FormasPagoServicio
 */

public interface IFormasPagoServicio {

	/**
	 * Retorna un listado con las formas de pago registradas en el sistema
	 * 
	 * @return List<{@link FormasPago}>
	 */
	public List<FormasPago> obtenerFormasPagos ();


	/**
	 * Retorna un listado con las formas de pago registradas en el sistema
	 * según los atributos pasados en el DtoFormaPago
	 * 
	 * @return List<{@link DtoFormaPago}>
	 */
	public List<DtoFormaPago> obtenerFormasPagos(DtoFormaPago formaPagoFiltros);

	
	
	/**
	 * Retorna un listado con los tipos basicos de las formas de pago 
	 * @return
	 */
	public List<TiposDetalleFormaPago> obtenerTiposDetalleFormaPago();
	
	
	/**
	 * Retorna una Forma de Pago espec&iacute;fica
	 * 
	 * @param consecutivo
	 * @return
	 */
	public FormasPago findById (int consecutivo);
	
	
}
