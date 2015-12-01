package com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta;

import com.servinte.axioma.mundo.fabrica.odontologia.ventaTarjeta.VentaTarjetaMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IVentaEmpresarialMundo;
import com.servinte.axioma.orm.VentaEmpresarial;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IVentaEmpresarialServicio;

/**
 * Clase que brinda de los servicios de administracion tarjeta cliente
 * @author Edgar Carvajal Ruiz
 *
 */
public class VentaEmpresarialServicio implements IVentaEmpresarialServicio {

	
	
	/**
	 *  Interfaz de Venta Tarjeta Mundo
	 */
	private IVentaEmpresarialMundo mundoVenta;

	
	/**
	 * Construtor 
	 */
	public VentaEmpresarialServicio(){
		mundoVenta=VentaTarjetaMundoFabrica.crearVentaEmpresarialMundo();
	}
	
	
	
	@Override
	public VentaEmpresarial buscarxId(Number id) {
		return mundoVenta.buscarxId(id);
	}

	@Override
	public void eliminar(VentaEmpresarial objeto) {
		mundoVenta.eliminar(objeto);
		
	}

	@Override
	public void insertar(VentaEmpresarial objeto) {
		mundoVenta.insertar(objeto);
		
	}

	@Override
	public void modificar(VentaEmpresarial objeto) {
		mundoVenta.modificar(objeto);
	}

}
