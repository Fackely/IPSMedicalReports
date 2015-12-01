package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface ITurnoDeCajaDAO {
	
	/**
	 * Obtiene el ultimo movimiento de cierre para la caja dada 
	 * @param codigoCaja
	 * @return {@link MovimientosCaja}
	 */
	/*
	public MovimientosCaja obtenerUltimoTurnoCierre(String codigoCaja);
	*/
	
	/**
	 * Guarda un turno de caja
	 * @param turno
	 * @return {@link TurnoDeCaja}
	 */
	public boolean guardarTurno(TurnoDeCaja turno);
	
	
	/**
	 * Devuelve el turno en estado abierto para el usuario
	 * @param usuario
	 * @return
	 */
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero(UsuarioBasico usuario);
	
	
	/**
	 * Devuelve el turno en estado abierto para la caja
	 * @param caja
	 * @return
	 */
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja);
	
	
	
	/**
	 * 
	 * Retorna un {@link TurnoDeCaja} si el usuario enviado tiene uno en estado abierto y asociado a esa caja,
	 * a el centro de atenci&oacute;n espec&iacute;fico.
	 * 	
	 * @param usuario
	 * @param consecutivoCaja
	 * @param consecutivoCentroAtencion
	 * @return TurnoDeCaja para ese usuario/cajero en estado abierto.
	 */
	public TurnoDeCaja obtenerTurnoCajaAbiertoPorCajaCajero(DtoUsuarioPersona usuario, int consecutivoCaja,int consecutivoCentroAtencion);

	
	
	/**
	 * Retorna el turno
	 * @param pk
	 */
	public TurnoDeCaja findById(long id);
	
	
	
	
	/**
	 * Retorna el turno de caja del movimiento dado
	 * @param idMovimientoCaja
	 * @return TurnoDeCaja
	 */
	public TurnoDeCaja obtenerTurnoPorMovimiento(long idMovimientoCaja);

	/**
	 * Verifica si una caja tiene turno abierto
	 * @param loginUsuario Login del usuario de la caja
	 * @param tiposCaja Tipos de caja que se desea consultar (ConstantesBD.codigoTipoCajaRecaudado, ConstantesBD.codigoTipoCajaPrincipal, ConstantesBD.codigoTipoCajaMayor)
	 * @param consecutivoCA Consecutivo del centro de atención para validar si se tiene caja activa en éste
	 * @return {@link DtoCaja} Caja abierta, null en caso de no tener turno abierto
	 */
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA);
	
	/**
	 * Se valida si la caja esta abierta o no 
	 * @param loginUsuario
	 * @return si esta o no abierta la caja 
	 */
	public Boolean cajaAbierta(String loginUsuario);
	
}
