/**
 * 
 */
package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITurnoDeCajaServicio;

/**
 * Implementación para el servicio de turno de caja
 * @author Juan David Ramírez
 *
 */
public class TurnoDeCajaServicio implements ITurnoDeCajaServicio
{

	@Override
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA)
	{
		return TesoreriaFabricaMundo.crearTurnoDeCajaMundo().validarTurnoUsuario(login, tiposCaja, consecutivoCA);
	}

	@Override
	public TurnoDeCaja findById(long id) {
		return TesoreriaFabricaMundo.crearTurnoDeCajaMundo().findById(id);
	}

	
	@Override
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja) {
		return TesoreriaFabricaMundo.crearTurnoDeCajaMundo().obtenerTurnoCajaAbiertoPorCaja(caja);
	}

	@Override
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero(UsuarioBasico usuario) {
		return  TesoreriaFabricaMundo.crearTurnoDeCajaMundo().obtenerTurnoCajaAbiertoPorCajero(usuario);
	}

}
