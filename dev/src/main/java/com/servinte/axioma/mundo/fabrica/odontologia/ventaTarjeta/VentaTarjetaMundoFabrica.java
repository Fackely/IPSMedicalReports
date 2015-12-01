package com.servinte.axioma.mundo.fabrica.odontologia.ventaTarjeta;

import com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente.BeneficiarioMundo;
import com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente.ValidacionIngresoVentaTarjeta;
import com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente.VentaEmpresarialMundo;
import com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente.VentaTarjetaClienteMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IBeneficiarioMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IValidacionIngresoVentaTarjeta;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IVentaEmpresarialMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IVentaTarjetaClienteMundo;


/**
 * Fabrica para La funcionalidad Venta de Tarjeta cliente
 * Esta clase proporpociona una serie de instancia para las intefazces venta Tarjeta 
 * cada metodo se comporta como un patron singleton.
 *
 */
public abstract class VentaTarjetaMundoFabrica {

	
	
	/**
	 * Interfaz venta Empresarial
	 */
	private static IVentaEmpresarialMundo interfazVentaEmpresarial;
	
	/**
	 * Constructor private para evitar la instancia
	 */
	private VentaTarjetaMundoFabrica(){
		
	}
	
	/**
	 * Método para crear le venta Empresarial
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final synchronized IVentaEmpresarialMundo crearVentaEmpresarialMundo(){
			
		if(interfazVentaEmpresarial==null)
		{
			return new VentaEmpresarialMundo();
		}
		
		return interfazVentaEmpresarial;
	}
	
	
	/**
	 * Interfaz Venta Tarjeta Mundo 
	 */
	private static IVentaTarjetaClienteMundo interfazVentaTarjetaMundo;
	
	
	
	/**
	 * Método para crear instancia de venta tarjeta cliente
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public  static synchronized IVentaTarjetaClienteMundo crearVentaTarjeta(){
		
		if(interfazVentaTarjetaMundo==null)
		{
			return new VentaTarjetaClienteMundo(); 
		}
		
		return interfazVentaTarjetaMundo;
		
	}
	
	
	
	/**
	 * Interfaz Beneficiarios 
	 */
	private static IBeneficiarioMundo interfazBeneficiarioMundo;
	
	
	/**
	 * Metodo para  construir la instancia beneficiario  
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IBeneficiarioMundo crearBeneficiarioMundo(){
		
		
		if(interfazBeneficiarioMundo==null)
		{
			return new BeneficiarioMundo();
		}
		return interfazBeneficiarioMundo;
	}
	
	/**
	 * Interfaz validacion venta
	 */
	private static IValidacionIngresoVentaTarjeta interfazValidacionVentaTarjeta;
	
	/**
	 * Método que crea una instancia de validación venta Tarjeta Cliente
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IValidacionIngresoVentaTarjeta crearValidacionVentaTarjeta(){
			if(interfazValidacionVentaTarjeta==null)
			{
				return new ValidacionIngresoVentaTarjeta();
			}
			return interfazValidacionVentaTarjeta;
	}
	
	
	
	
	
	
}
