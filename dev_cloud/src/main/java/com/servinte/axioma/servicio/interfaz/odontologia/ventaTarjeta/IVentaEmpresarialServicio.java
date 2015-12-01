package com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta;

import com.servinte.axioma.orm.VentaEmpresarial;


/**
 * Interfaz que define la politicas de comportamiento de la venta Empresarial
 * @author Edgar Carvajal Ruiz
 *
 */
public interface IVentaEmpresarialServicio {
	
	
	
	/**
	 * Guardar Venta Tarjeta Cliente
	 * 
	 * @author Edgar Carvajal Ruiz
	 */
	public void insertar(VentaEmpresarial objeto);
	
		
	/**
	 * Metodo que recibe un objeto de Tipo Venta Tarjeta  y lo modifica de la base de datos
	 * @author Edgar Carvajal 
	 * @param objecto
	 */
	 public void modificar(VentaEmpresarial objeto);
	
	
	/**
	 * Metodo que recibe un objeto de tipo VentaTarjetaCliente y lo elimina de la base de datos
	 * @author Edgar Carvajal
	 * @param objecto
	 */
	public void eliminar( VentaEmpresarial objeto);

	
	/**
	 * Metodo que recive un Id y retorna un Tipo de Objeto Venta Tarjeta Cliente
	 * @author Edgar Carvajal
	 * @param objeto
	 * @param id
	 * @return
	 */
	public VentaEmpresarial buscarxId(Number id);

}
