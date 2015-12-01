package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * 
 * 
 * @author Juan David Ramírez
 * @see com.servinte.axioma.mundo.impl.tesoreria.TurnoDeCajaMundo
 */

public interface ITurnoDeCajaServicio {

	/**
	 * Verifica si una caja tiene turno abierto
	 * @param loginUsuario Login del usuario de la caja
	 * @param tiposCaja Tipos de caja que se desea consultar (ConstantesBD.codigoTipoCajaRecaudado, ConstantesBD.codigoTipoCajaPrincipal, ConstantesBD.codigoTipoCajaMayor)
	 * @param consecutivoCA Consecutivo del centro de atención para validar si se tiene caja activa en éste
	 * @return {@link DtoCaja} Caja abierta, null en caso de no tener turno abierto
	 */
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA);
	
	
	/**
	 * Retorna el turno
	 * @param pk
	 */
	public TurnoDeCaja findById(long id);
	
	
	/**
	 * Devuelve el turno en estado abierto para la caja
	 * @param caja
	 * @return
	 */
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja);
	
	
	
	/**
	 * Devuelve el turno en estado abierto para el usuario
	 * @param usuario
	 * @return
	 */
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero(UsuarioBasico usuario);

}


