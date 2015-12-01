package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.Utilidades;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoTurnoDeCajaApta;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasCajerosDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITurnoDeCajaDAO;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITurnoDeCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CajasCajeros;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.TurnoDeCajaHome;

/**
 * Contiene la l&oacute;gica de Negocio para los turnos de caja
 * 
 * @author Cristhian Murillo
 * @see ITurnoDeCajaMundo
 */

public class TurnoDeCajaMundo implements ITurnoDeCajaMundo{

	private ITurnoDeCajaDAO turnoDeCajaDAO;
	private ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo;  
	private IMovimientosCajaMundo movimientosCajaMundo;
	private ICajasCajerosDAO cajaCajero;
	
	
	public TurnoDeCajaMundo() {
		inicializar();
	}
	
	private void inicializar() {
		turnoDeCajaDAO 				= TesoreriaFabricaDAO.crearTurnoDeCajaDAO();
		solicitudTrasladoCajaMundo	= TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		movimientosCajaMundo		= TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		cajaCajero					= TesoreriaFabricaDAO.crearCajasCajerosDAO();
		
	}
	
	
	@Override
	public DtoTurnoDeCajaApta esCajaAptaParaApertura(UsuarioBasico usuario, Cajas caja) 
	{
		DtoTurnoDeCajaApta dtoTurnoDeCajaApta = new DtoTurnoDeCajaApta();
		
		// solicitudes -----------------------------
		if(solicitudTrasladoCajaMundo.tieneSolicitudDeTraslado(caja)){
			dtoTurnoDeCajaApta.setTieneSolicitudDeTraslado(true);
		}else {
			dtoTurnoDeCajaApta.setTieneSolicitudDeTraslado(false);
		}
		
		// turnos cajero----------------------------------
		if(validarTurnosCajeroCerrados(usuario)){	
			dtoTurnoDeCajaApta.setTieneTurnoCajeroCerrado(true);
		}
		else {
			dtoTurnoDeCajaApta.setTieneTurnoCajeroCerrado(false); 
		}
		
		// turnos caja----------------------------------
		if(validarTurnosCajaCerrados(caja))	{	
			dtoTurnoDeCajaApta.setTieneTurnoCajaCerrado(true); 
		}
		else {
			dtoTurnoDeCajaApta.setTieneTurnoCajaCerrado(false); 
		}
		
		
		if(validarUsuarioTurnoCaja(caja, usuario.getLoginUsuario()))	{	
			dtoTurnoDeCajaApta.setUsuarioTurnoCaja(true); 
		}
		else {
			dtoTurnoDeCajaApta.setUsuarioTurnoCaja(false); 
		}

		return dtoTurnoDeCajaApta;
	}
	
	
		
	@Override
	public boolean validarTurnosCajeroCerrados(UsuarioBasico usuario)
	{
		boolean puedeAbrirNuevoTurno = false;
		if(Utilidades.isEmpty(turnoDeCajaDAO.obtenerTurnoCajaAbiertoPorCajero(usuario))){
			puedeAbrirNuevoTurno = true;
		}
		return puedeAbrirNuevoTurno;
	}
	
	
	@Override
	public boolean validarTurnosCajaCerrados(Cajas caja) 
	{
		boolean puedeAbrirNuevoTurno = false;
		if(Utilidades.isEmpty(turnoDeCajaDAO.obtenerTurnoCajaAbiertoPorCaja(caja)))
		{
			puedeAbrirNuevoTurno = true;
		}
		
		return puedeAbrirNuevoTurno;
	}
	
	
	
	@Override
	public boolean validarUsuarioTurnoCaja(Cajas caja, String loginUsuario) 
	{		
		ArrayList<TurnoDeCaja> turnoCaja = turnoDeCajaDAO.obtenerTurnoCajaAbiertoPorCaja(caja);
		
		for (TurnoDeCaja turnoDeCaja : turnoCaja) {
			
			if(turnoDeCaja!=null && turnoDeCaja.getUsuarios().getLogin().equals(loginUsuario)){
				
				return true;
			}
		}
		
		return false;
	}
	
	
	@Override
	public boolean guardarTurno(TurnoDeCaja turno, ArrayList<DtoInformacionEntrega>  listaDtoInformacionEntrega) 
	{
		
		turno.setEstado(ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto);
		
		if(Utilidades.isEmpty(listaDtoInformacionEntrega))
		{
			return turnoDeCajaDAO.guardarTurno(turno);
		}
		else
		{
			TurnoDeCajaHome turnoDeCajaHome = new TurnoDeCajaHome();
			turnoDeCajaHome.attachDirty(turno);
			Log4JManager.info(">>>>>>>>>>>>>>> Turno adjuntado: "+turno.getCodigoPk());
			
			for(DtoInformacionEntrega dtoInformacionEntrega : listaDtoInformacionEntrega)
			{
				Log4JManager.info(">>>>>>>>>>>>>>> Creanto instance de nuevo movimiento: ACEPTACION_APERTURA_TURNO");
				dtoInformacionEntrega.setMovimientosCaja(new MovimientosCaja());
				dtoInformacionEntrega.getMovimientosCaja().setTurnoDeCaja(turno);	
				dtoInformacionEntrega.setETipoMovimiento(ETipoMovimiento.ACEPTACION_APERTURA_TURNO);
			}
			
			return movimientosCajaMundo.guardarMovimientoCaja(listaDtoInformacionEntrega);
		}
	}

	

	@Override
	public TurnoDeCaja obtenerTurnoCajaAbiertoPorCajaCajero(DtoUsuarioPersona usuario,int consecutivoCaja, int consecutivoCentroAtencion) 
	{
		TurnoDeCaja turnoDeCaja = turnoDeCajaDAO.obtenerTurnoCajaAbiertoPorCajaCajero(usuario, consecutivoCaja, consecutivoCentroAtencion);
		
		return turnoDeCaja;
	}


	
	@Override
	public TurnoDeCaja findById(long id) {
		return turnoDeCajaDAO.findById(id);
	}


	@Override
	public TurnoDeCaja obtenerTurnoPorMovimiento(long idMovimientoCaja) {
		return turnoDeCajaDAO.obtenerTurnoPorMovimiento(idMovimientoCaja);
	}

	
	@Override
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero( UsuarioBasico usuario) {
		return turnoDeCajaDAO.obtenerTurnoCajaAbiertoPorCajero(usuario);
	}
	
	
	@Override
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA)
	{
		return turnoDeCajaDAO.validarTurnoUsuario(login, tiposCaja, consecutivoCA);
	}

	
	@Override
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja) {
		return turnoDeCajaDAO.obtenerTurnoCajaAbiertoPorCaja(caja);
	}
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ITurnoDeCajaMundo#cajaAbierta(java.lang.String)
	 */
	public Boolean cajaAbierta(String loginUsuario){
		return turnoDeCajaDAO.cajaAbierta(loginUsuario);
	}
	
	/**
	 * Este metodo permite consultar para todos los usuarios las cajas que tiene asociadas y que se encuentran activas
	 * @param usuario
	 * @return
	 * @author Diana Ruiz
	 */
	
	
	public ArrayList<CajasCajeros> obtenerTodasCajasCajero(int codigo_caja) {
		return cajaCajero.obtenerTodasCajasCajero(codigo_caja);		
	}
	
	
	/**
	 * Este metodo permite realizar una merge a la clase
	 * @param instance
	 * @return
	 * @author Diana Ruiz
	 */
	
	public CajasCajeros merge(CajasCajeros instance) {
		return cajaCajero.merge(instance);
	}

	/**
	 * Este metodo permite consultar por usuario las cajas que este tiene activas
	 * @param usuario
	 * @return
	 * @author Diana Ruiz
	 * @since 11/10/2011
	 */
	
	public ArrayList<CajasCajeros> obtenerCajasCajero(UsuarioBasico usuario){
		return cajaCajero.obtenerCajasCajero(usuario);		
	}
		
}
