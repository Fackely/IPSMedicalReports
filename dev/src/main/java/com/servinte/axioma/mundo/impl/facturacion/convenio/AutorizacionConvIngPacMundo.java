package com.servinte.axioma.mundo.impl.facturacion.convenio;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.mundo.interfaz.facturacion.convenio.IAutorizacionConvIngPac;
import com.servinte.axioma.orm.AutorizacionConvIngPac;
import com.servinte.axioma.orm.delegate.facturacion.convenio.AutorizacionConvIngPacDelegate;


/**
 * 
 * @author axioma
 *
 */
public class AutorizacionConvIngPacMundo implements IAutorizacionConvIngPac {

	
	private AutorizacionConvIngPacDelegate delegate;
	/**
	 * Construtor
	 */
	public AutorizacionConvIngPacMundo(){
		 delegate= new AutorizacionConvIngPacDelegate();
	}
	
	
	@Override
	public AutorizacionConvIngPac obtenerUltimaAutorizacion(
															long conveniosIngresoPacientePk) {
		
		AutorizacionConvIngPac autorizacion = new AutorizacionConvIngPac();
		try {
			autorizacion=delegate.obtenerUltimaAutorizacion(conveniosIngresoPacientePk);
		} 
		catch (Exception e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return autorizacion;
	}

}
