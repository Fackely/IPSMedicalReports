package com.servinte.axioma.mundo.impl.tesoreria;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.impl.tesoreria.TipoCuentaBancariaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITipoCuentaBancariaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITipoCuentaBancariaMundo;
import com.servinte.axioma.orm.TipoCuentaBancaria;


/**
 * Esta clase se encarga de ejecutar los procesos
 * de negocio de la entidad Tipo Cuenta 
 * 
 * @author Diana Carolina G
 *
 */
public class TipoCuentaBancariaMundo implements ITipoCuentaBancariaMundo {
	
	/**
	 * Instancia DAO de ITipoCuentaBancariaDAO
	 */
	private TipoCuentaBancariaDAO tipoCuentaBancariaDAO;
	
	public TipoCuentaBancariaMundo(){
		setTipoCuentaBancariaDAO(TesoreriaFabricaDAO.crearTipoCuentaBancariaDAO());
	}
	
	
	
	public void setTipoCuentaBancariaDAO(ITipoCuentaBancariaDAO tipoCuentaBancariaDAO) {
		this.tipoCuentaBancariaDAO = (TipoCuentaBancariaDAO) tipoCuentaBancariaDAO; 
		
	}


	public ITipoCuentaBancariaDAO getTipoCuentaBancariaDAO() {
		return tipoCuentaBancariaDAO;
	}


	@Override
	public TipoCuentaBancaria findById(char id) {
		return tipoCuentaBancariaDAO.findById(id);
	
	}

	


}
