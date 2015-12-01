package com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta;

import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public interface IValidacionIngresoVentaTarjetaServicio {
	
	/**
	 * Metodo para validar si un usuario tiene rol definido turno Caja
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @return
	 */
	public  boolean usuarioTieneRolDefinidoTurnoCaja(Usuario usuario);

	/**
	 * Metodo para validar si un usuario tiene asociado cajas de tipo recaudo en estado activo
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @return
	 */
	public  boolean usuarioTieneAsociadoCajaTipoRecaudo(UsuarioBasico usuario);

	/**
	 * Metodo para validar si un usuario tiene el turno de caja abierto 
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @return
	 */
	public boolean usuarioTieneTurnoCajaAbierto(UsuarioBasico usuario);
	
	/**
	 * Metodo que valida si un usuario es cajero
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 */
	public boolean usuarioEsCajero(UsuarioBasico usuario);
	
	
}
