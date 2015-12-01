package com.servinte.axioma.servicio.impl.tesoreria;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITipoCuentaBancariaMundo;
import com.servinte.axioma.orm.TipoCuentaBancaria;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITipoCuentaBancariaServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link ITipoCuentaBancariaServicio}
 * @author Diana Carolina G
 *
 */

public class TipoCuentaBancariaServicio implements ITipoCuentaBancariaServicio {
	
	/**
	 * Instancia Mundo de ITipoCuentaBancariaMundo
	 */
	
	private ITipoCuentaBancariaMundo tipoCuentaBancariaMundo;

	
	public TipoCuentaBancariaServicio(){
		setTipoCuentaBancariaMundo(TesoreriaFabricaMundo.crearTipoCuentaBancariaMundo());
	}


	public void setTipoCuentaBancariaMundo(ITipoCuentaBancariaMundo tipoCuentaBancariaMundo) {
		this.tipoCuentaBancariaMundo = tipoCuentaBancariaMundo;
	}


	public ITipoCuentaBancariaMundo getTipoCuentaBancariaMundo() {
		return tipoCuentaBancariaMundo;
	}


	@Override
	public TipoCuentaBancaria findById(char id) {
		return tipoCuentaBancariaMundo.findById(id);
	}
	
	
	

}
