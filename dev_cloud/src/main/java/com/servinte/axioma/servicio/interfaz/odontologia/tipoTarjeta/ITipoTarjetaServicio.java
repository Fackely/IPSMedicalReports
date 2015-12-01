package com.servinte.axioma.servicio.interfaz.odontologia.tipoTarjeta;

import com.servinte.axioma.orm.TiposTarjCliente;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public interface ITipoTarjetaServicio {
	
	/**
	 * Metodo que recibe un objeto y lo inserta en la base de datos
	 * @author 
	 * @param objecto
	 */
	public void insertar(TiposTarjCliente objeto);

	
	/**
	 * Metodo que recibe un objeto y lo modifica en la base de datos
	 * @author 
	 * @param objecto
	 */
	public void modificar(TiposTarjCliente objeto);
	
	
	/**
	 * Metodo que recibe un objeto y lo elimina de la base de datos
	 * @author 
	 * @param objecto
	 */
	public void eliminar( TiposTarjCliente objeto);

	
	/**
	 * 
	 * Metodo que recibe un Id y retorna un tipo de objeto de la base de datos
	* @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public TiposTarjCliente buscarxId(Number id);

}
