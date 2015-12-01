package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.TiposDetalleFormaPago;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Formas de Pago
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public interface IFormasPagoDAO {

	/**
	 * Retorna un listado con las formas de pago registradas en el sistema
	 * 
	 * @return List<{@link FormasPago}>
	 */
	public List<FormasPago> obtenerFormasPagos ();

	
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
	 * Retorna un listado con las formas de pago registradas en el sistema
	 * seg&uacute;n los atributos pasados en el DtoFormaPago
	 * 
	 * @return List<{@link DtoFormaPago}>
	 */
	public List<DtoFormaPago> obtenerFormasPagos(DtoFormaPago formaPagoFiltros);
	
	/**
	 * @return lista de formas de pago activas
	 */
	public List<FormasPago> obtenerFormasPagosActivos();
	
}
