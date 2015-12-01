package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.interfaz.tesoreria.ITurnoDeCajaDAO;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.TurnoDeCajaDelegate;

/**
 * Implementación de la interfaz {@link ITurnoDeCajaDAO}.
 * 
 * @author Cristhian Murillo
 * @see TurnoDeCajaDelegate.
 */

public class TurnoDeCajasHibernateDAO implements ITurnoDeCajaDAO{

	
	private TurnoDeCajaDelegate delegate = new TurnoDeCajaDelegate();
	
	@Override
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja) {
		return delegate.obtenerTurnoCajaAbiertoPorCaja(caja);
	}

	@Override
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero(UsuarioBasico usuario) {
		return delegate.obtenerTurnoCajaAbiertoPorCajero(usuario);
	}
	
	
	
	@Override
	public boolean guardarTurno(TurnoDeCaja turno) {
		boolean save = false;
		try{
			delegate.persist(turno);
			save = true;
		}
		catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el Turno de Caja",e);
		}
		return save;
	}
	

	@Override
	public TurnoDeCaja obtenerTurnoCajaAbiertoPorCajaCajero(DtoUsuarioPersona usuario,int consecutivoCaja, int consecutivoCentroAtencion) {
		
		return delegate.obtenerTurnoCajaAbiertoPorCajaCajero(usuario, consecutivoCaja, consecutivoCentroAtencion);
	}

	
	
	@Override
	public TurnoDeCaja findById(long id) {
		return delegate.findById(id);
	}

	@Override
	public TurnoDeCaja obtenerTurnoPorMovimiento(long idMovimientoCaja) {
		return delegate.obtenerTurnoPorMovimiento(idMovimientoCaja);
	}

	@Override
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA)
	{
		return delegate.validarTurnoUsuario(login, tiposCaja, consecutivoCA);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ITurnoDeCajaDAO#cajaAbierta(java.lang.String)
	 */
	public Boolean cajaAbierta(String loginUsuario) {
		return delegate.cajaAbierta(loginUsuario);
	}

	
}
