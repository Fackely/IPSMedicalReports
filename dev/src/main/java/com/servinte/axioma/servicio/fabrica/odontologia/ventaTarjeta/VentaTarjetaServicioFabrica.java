package com.servinte.axioma.servicio.fabrica.odontologia.ventaTarjeta;

import com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta.BeneficiarioServicio;
import com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta.ValidacionIngresoVentaTarjetaServicio;
import com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta.VentaEmpresarialServicio;
import com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta.VentaTarjetaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IBeneficiarioServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IValidacionIngresoVentaTarjetaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IVentaEmpresarialServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IVentaTarjetaClienteServicio;


/**
 * Esta clase es una fábrica de para generar instancia de venta de tarjeta cliente
 * @author Edgar Carvajal Ruiz
 *
 */
public abstract class VentaTarjetaServicioFabrica {

	
	
	/**
	 * Constructor para no instanciar la Fábrica
	 */
	private VentaTarjetaServicioFabrica(){
	}
	
	
	
	/**
	 * Interfaz venta empresarial servicio 
	 */
	private static  IVentaEmpresarialServicio interfazVentaEmpresarial;
	
	
	/**
	 * Método que retorna una Venta Empresarial servicio
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IVentaEmpresarialServicio crearVentaEmpresarialServicio(){
		
		if(interfazVentaEmpresarial==null)
		{
			new VentaEmpresarialServicio();
		}
		return interfazVentaEmpresarial;
	}
	
	/**
	 * Interfaz venta Tarjeta
	 */
	private static IVentaTarjetaClienteServicio interfazVentaTarjeta;
	
	
	
	/**
	 *Metodo que retorna Venta tarjeta Cliente
	 */
	public static final IVentaTarjetaClienteServicio crearVentaTarjetaClienteServicio(){
		
		if(interfazVentaTarjeta==null)
		{
			return new VentaTarjetaServicio();
		}
		return interfazVentaTarjeta;
	}
	
	
	
	/**
	 * Interfaz beneficiario 
	 */
	private static IBeneficiarioServicio interfazBeneficiario;
	
	
	
	
	/**
	 * Método que retorna un beneficiairo servicio 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IBeneficiarioServicio crearBeneficiarioServicio(){
	
			if(interfazBeneficiario==null)
			{
				return new  BeneficiarioServicio(); 
			}
			return interfazBeneficiario;
	}
	
	/**
	 * Interfaz de validación Venta 
	 */
	private static  IValidacionIngresoVentaTarjetaServicio interfazValidacionIngreso;
	
	/**
	 * Método que crea una interfaz de validación 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IValidacionIngresoVentaTarjetaServicio crearValidacionIngresoVenta(){
		
		if(interfazValidacionIngreso==null){
			new ValidacionIngresoVentaTarjetaServicio();
		}
		return interfazValidacionIngreso;
	}

	
	
	
	
}
