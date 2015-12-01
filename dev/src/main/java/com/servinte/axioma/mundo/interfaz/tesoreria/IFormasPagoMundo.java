package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.TiposDetalleFormaPago;


/**
 * Define la l&oacute;gica de negocio relacionada con
 * las Formas de Pago
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.mundo.impl.tesoreria.FormasPagoMundo
 */

public interface IFormasPagoMundo {

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
	
	/**
	 * @return lista de formas de pago activas
	 */
	public List<FormasPago> obtenerFormasPagosActivos();
	
	
}
