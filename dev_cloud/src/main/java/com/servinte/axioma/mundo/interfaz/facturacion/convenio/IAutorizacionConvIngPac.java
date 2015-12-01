package com.servinte.axioma.mundo.interfaz.facturacion.convenio;

import com.servinte.axioma.orm.AutorizacionConvIngPac;



/**
 * 
 * @author axioma
 *
 */
public interface IAutorizacionConvIngPac {
	
	/**
	 * obtener Ultima Autorizacion 
	 * @param conveniosIngresoPacientePk
	 * @return
	 */
	public AutorizacionConvIngPac obtenerUltimaAutorizacion(long conveniosIngresoPacientePk);

}
