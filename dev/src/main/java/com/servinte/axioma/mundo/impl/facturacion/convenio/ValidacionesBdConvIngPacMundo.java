package com.servinte.axioma.mundo.impl.facturacion.convenio;

import java.util.Date;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.mundo.interfaz.facturacion.convenio.IValidacionesBdConvIngPacMundo;
import com.servinte.axioma.orm.ValidacionesBdConvIngPac;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ValidacionesBdConvIngPacDelegate;


/**
 * 
 * @author axioma
 *
 */
public class ValidacionesBdConvIngPacMundo implements IValidacionesBdConvIngPacMundo {

	
	/**
	 * Delegate
	 */
	private ValidacionesBdConvIngPacDelegate validacionDelegate;
	
	
	/**
	 * 
	 */
	public ValidacionesBdConvIngPacMundo(){
		validacionDelegate = new ValidacionesBdConvIngPacDelegate();
	}
	
	
	
	@Override
	public ValidacionesBdConvIngPac obtenerUltimaValidacionBd( long conveniosIngresoPacientePk) {
		
		ValidacionesBdConvIngPac validacionBD=new ValidacionesBdConvIngPac();
		try {
			validacionBD=validacionDelegate.obtenerUltimaValidacionBd(conveniosIngresoPacientePk);
			if( validacionBD==null)
			{
				validacionBD=new ValidacionesBdConvIngPac();
				
			}
			
			
		} catch (Exception e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		return validacionBD;
	}

	
	
}
