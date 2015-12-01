package com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta;

import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.odontologia.ventaTarjeta.VentaTarjetaMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IValidacionIngresoVentaTarjeta;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class ValidacionIngresoVentaTarjetaServicio implements IValidacionIngresoVentaTarjeta {

	
	/**
	 * Interfaz de validacion
	 */
	private IValidacionIngresoVentaTarjeta validacionIngreso;
	
	/**
	 * Construtor
	 */
	public ValidacionIngresoVentaTarjetaServicio(){
		this.setValidacionIngreso(VentaTarjetaMundoFabrica.crearValidacionVentaTarjeta());
	}
	
	
	
	@Override
	public boolean usuarioEsCajero(UsuarioBasico usuario) {
		return validacionIngreso.usuarioEsCajero(usuario);
	}

	@Override
	public boolean usuarioTieneAsociadoCajaTipoRecaudo(UsuarioBasico usuario) {
		
		return validacionIngreso.usuarioTieneAsociadoCajaTipoRecaudo(usuario);
	}

	@Override
	public boolean usuarioTieneRolDefinidoTurnoCaja(Usuario usuario) {
		return validacionIngreso.usuarioTieneRolDefinidoTurnoCaja(usuario);
	}

	@Override
	public boolean usuarioTieneTurnoCajaAbierto(UsuarioBasico usuario) {
		return  validacionIngreso.usuarioTieneTurnoCajaAbierto(usuario);
	}



	
	/**
	 * Metodo que recibe la instancia de la interfaz validacion venta Tarjeta
	 * @author Edgar Carvajal Ruiz
	 * @param validacionIngreso
	 */
	public void setValidacionIngreso(IValidacionIngresoVentaTarjeta validacionIngreso) {
		this.validacionIngreso = validacionIngreso;
	}


	/**
	 * Metodo que retorna la interfaz IValidacion Ingreso venta Tarjeta
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public IValidacionIngresoVentaTarjeta getValidacionIngreso() {
		return validacionIngreso;
	}

}
