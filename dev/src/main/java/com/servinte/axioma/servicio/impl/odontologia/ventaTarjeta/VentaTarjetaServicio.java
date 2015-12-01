package com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.mundo.fabrica.odontologia.ventaTarjeta.VentaTarjetaMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IVentaTarjetaClienteMundo;
import com.servinte.axioma.orm.VentaTarjetaCliente;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IVentaTarjetaClienteServicio;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class VentaTarjetaServicio implements IVentaTarjetaClienteServicio{

	
	/**
	 * Interfaz Venta ciente  Mundo 
	 */
	private IVentaTarjetaClienteMundo mundoVenta;
	
	/**
	 * Construtor
	 */
	public VentaTarjetaServicio(){
		mundoVenta= VentaTarjetaMundoFabrica.crearVentaTarjeta();
	}
	
	
	
	@Override
	public VentaTarjetaCliente buscarxId(Number id) {
		return mundoVenta.buscarxId(id);
	}

	@Override
	public void eliminar(VentaTarjetaCliente objeto) {
		mundoVenta.eliminar(objeto);
		
	}

	@Override
	public ArrayList<Errores> guardarVenta(DtoVentaTarjetasCliente dtoVenta, DtoFacturaVaria dtoFacturaVaria, DtoPersonas dtoCompradorTarjeta){
		return mundoVenta.guardarVenta(dtoVenta, dtoFacturaVaria, dtoCompradorTarjeta);
	}

	@Override
	public void modificar(VentaTarjetaCliente objeto) {
		mundoVenta.modificar(objeto);
		
	}

	@Override
	public ArrayList<DtoFormaPago> listarFormasPagoActivas() {
		return mundoVenta.listarFormasPagoActivas();
	}

}
