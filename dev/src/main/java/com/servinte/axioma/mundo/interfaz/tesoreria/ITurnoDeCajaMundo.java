package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoTurnoDeCajaApta;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * Define la l&oacute;gica de negocio relacionada con los turnos de caja
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.TurnoDeCajaMundo
 */

public interface ITurnoDeCajaMundo {

	
	/**
	 * Valida que el cajero tenga turnos cerrados y que la caja no tenga solicitud
	 * de traslado
	 * @param caja
	 * @param usuario
	 * @return
	 */
	public DtoTurnoDeCajaApta esCajaAptaParaApertura(UsuarioBasico usuario, Cajas caja);
	
	
	/**
	 * Guarda un turno de caja
	 * @param turno
	 * @param dtoInformacionEntrega
	 * @return {@link TurnoDeCaja}
	 */
	public boolean guardarTurno(TurnoDeCaja turno, ArrayList<DtoInformacionEntrega> listaDtoInformacionEntrega);
	

	/**
	 * 
	 * @param usuario
	 * @return
	 */
	boolean validarTurnosCajeroCerrados(UsuarioBasico usuario);

	
	/**
	 * 
	 * @param caja
	 * @return
	 */
	boolean validarTurnosCajaCerrados(Cajas caja);
	
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
	 * Metodo que carga una lista de turnos de caja en estado abierto
	 * Recibe un Usuario Basico 
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @return
	 */
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero(UsuarioBasico usuario) ;

	/**
	 * Verifica si una caja tiene turno abierto
	 * @param loginUsuario Login del usuario de la caja
	 * @param tiposCaja Tipos de caja que se desea consultar (ConstantesBD.codigoTipoCajaRecaudado, ConstantesBD.codigoTipoCajaPrincipal, ConstantesBD.codigoTipoCajaMayor)
	 * @param consecutivoCA Consecutivo del centro de atención para validar si se tiene caja activa en éste
	 * @return {@link DtoCaja} Caja abierta, null en caso de no tener turno abierto
	 */
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA);

	
	/**
	 * Devuelve el turno en estado abierto para la caja
	 * @param caja
	 * @return
	 */
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja);
	
	
	/**
	 * Método que se encarga de validar si el usuario es quien abrio
	 * la caja.
	 * 
	 * @param caja
	 * @param loginUsuario
	 * @return
	 */
	public boolean validarUsuarioTurnoCaja(Cajas caja, String loginUsuario);
	
	/**
	 * Metodo que se encarga de validar si esta abierta o no la caja 
	 * @param loginUsuario
	 * @return si esta o no abierta la caja 
	 */
	public Boolean cajaAbierta(String loginUsuario);
	
}
