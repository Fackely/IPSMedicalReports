package com.servinte.axioma.dao.interfaz.odontologia.tipoTarjeta;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.TiposTarjCliente;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public interface ITipoTarjetaDAO extends IBaseDAO<TiposTarjCliente>{

	/**
	 * Consultar el tipo de tarjeta cliente para la clase de
	 * venta y el tipo de tarjeta específico
	 * @param tipoTarjeta
	 * @param claseVenta TODO
	 * @author Juan David Ramírez
	 * @return {@link DtoTipoTarjetaCliente}
	 * @since 08 Septiembre 2010
	 */
	public DtoTipoTarjetaCliente consultarTipoTarjetaCliente(double tipoTarjeta, String claseVenta);

}
