package com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente;

import util.Utilidades;

import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IValidacionIngresoVentaTarjeta;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasCajerosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITurnoDeCajaMundo;


/**
 * 
 * Clase para validar el ingreso a la funcionalidad de ventas tarjeta cliente
 * @author Edgar Carvajal
 *
 */
public class ValidacionIngresoVentaTarjeta implements IValidacionIngresoVentaTarjeta {
	
	@Override
	public boolean usuarioEsCajero(UsuarioBasico usuario){
		ICajasCajerosMundo cajas = TesoreriaFabricaMundo.crearCajasCajerosMundo();
		return cajas.validarUsuarioEsCajero(usuario);
	}
	
	
	@Override
	public  boolean usuarioTieneAsociadoCajaTipoRecaudo(UsuarioBasico usuario) {
		ICajasCajerosMundo cajas = TesoreriaFabricaMundo.crearCajasCajerosMundo();
		return cajas.validarUsuarioEsCajero(usuario);
		
	}

	@Override
	public boolean usuarioTieneRolDefinidoTurnoCaja(Usuario usuario) {
		return Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 325); //TODO OJO Con el codigo de la funcionalidad
	}

	@Override
	public boolean usuarioTieneTurnoCajaAbierto(UsuarioBasico usuario) {
		ITurnoDeCajaMundo turnoCajaMundo=TesoreriaFabricaMundo.crearTurnoDeCajaMundo();
		return turnoCajaMundo.obtenerTurnoCajaAbiertoPorCajero(usuario).size()>0; // si tiene mas de un tiene turnos abiertos
	}
		

}
