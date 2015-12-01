package com.servinte.axioma.dao.impl.tesoreria;


import com.servinte.axioma.dao.interfaz.tesoreria.ITipoCuentaBancariaDAO;
import com.servinte.axioma.orm.TipoCuentaBancaria;
import com.servinte.axioma.orm.delegate.tesoreria.TipoCuentaBancariaDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ITipoCuentaBancariaDAO}.
 * @author Diana Carolina G
 *
 */

public class TipoCuentaBancariaDAO implements ITipoCuentaBancariaDAO {
	
	private TipoCuentaBancariaDelegate tipoCuentaBancariaImp;
	
	public TipoCuentaBancariaDAO(){
		
		setTipoCuentaBancariaImp(new TipoCuentaBancariaDelegate());
	}
	
	
	public void setTipoCuentaBancariaImp(TipoCuentaBancariaDelegate tipoCuentaBancariaImp) {
		this.tipoCuentaBancariaImp = tipoCuentaBancariaImp;
	}


	public TipoCuentaBancariaDelegate getTipoCuentaBancariaImp() {
		return tipoCuentaBancariaImp;
	}


	


	@Override
	public TipoCuentaBancaria findById(char id) {
		return tipoCuentaBancariaImp.findById(id);
	}
		
	}

 


		



		
	
	

