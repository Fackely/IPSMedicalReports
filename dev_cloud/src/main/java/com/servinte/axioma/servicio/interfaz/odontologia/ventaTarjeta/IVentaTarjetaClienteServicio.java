package com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.orm.VentaTarjetaCliente;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public interface IVentaTarjetaClienteServicio {

	/**
	 * Guardar Venta Tarjeta Cliente
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoVenta
	 * @param dtoFacturaVaria 
	 * @param dtoCompradorTarjeta
	 * @return ArrayList de {@link Errores} Errores presentados en el ingreso
	 */
	public ArrayList<Errores> guardarVenta(DtoVentaTarjetasCliente dtoVenta, DtoFacturaVaria dtoFacturaVaria, DtoPersonas dtoCompradorTarjeta);
	
	/**
	 * Metodo que recibe un objeto de Tipo Venta Tarjeta  y lo modifica de la base de datos
	 * @author Edgar Carvajal 
	 * @param objecto
	 */
	 public void modificar(VentaTarjetaCliente objeto);
	
	/**
	 * Metodo que recibe un objeto de tipo VentaTarjetaCliente y lo elimina de la base de datos
	 * @author Edgar Carvajal
	 * @param objecto
	 */
	public void eliminar( VentaTarjetaCliente objeto);

	/**
	 * Metodo que recive un Id y retorna un Tipo de Objeto Venta Tarjeta Cliente
	 * @author Edgar Carvajal
	 * @param objeto
	 * @param id
	 * @return
	 */
	public VentaTarjetaCliente buscarxId(Number id);

	/**
	 * Listar formas de pago
	 * @return ArrayList<DtoFormaPago> Listado de las formas de pago activas en el sistema
	 */
	public ArrayList<DtoFormaPago> listarFormasPagoActivas();
	
}
