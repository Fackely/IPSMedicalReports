package com.servinte.axioma.mundo.interfaz.odontologia.tipoTarjeta;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.orm.TiposTarjCliente;


/**
 * Cargar convenio tipo de Tarjeta 
 * @author axioma
 *
 */
public interface ITipoTarjetaClienteMundo {

	/**
	 * Método que recibe un objeto y lo inserta en la base de datos
	 * @author 
	 * @param objecto
	 */
	public void insertar(TiposTarjCliente objeto);

	/**
	 * Método que recibe un objeto y lo modifica en la base de datos
	 * @author 
	 * @param objecto
	 */
	public void modificar(TiposTarjCliente objeto);
	
	/**
	 * Método que recibe un objeto y lo elimina de la base de datos
	 * @author 
	 * @param objecto
	 */
	public void eliminar( TiposTarjCliente objeto);
	
	/**
	 * 
	 * Método que recibe un Id y retorna un tipo de objeto de la base de datos
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public TiposTarjCliente buscarxId(Number id);

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
