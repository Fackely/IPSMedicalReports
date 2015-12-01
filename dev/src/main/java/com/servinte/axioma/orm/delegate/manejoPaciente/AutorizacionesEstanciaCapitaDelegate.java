package com.servinte.axioma.orm.delegate.manejoPaciente;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;
import com.servinte.axioma.orm.AutorizacionesEstanciaCapitaHome;

/**
 * Esta clase se encarga de ejecutar las consultas sobre 
 * la entidad  AutorizacionesEstanciaCapita
 * 
 * @author Angela Maria Aguirre
 * @since 15/12/2010
 */
public class AutorizacionesEstanciaCapitaDelegate extends
		AutorizacionesEstanciaCapitaHome {
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * el registro que asocia la autorizaci�n de ingreso estancia con la
	 * autorizaci�n de capitaci�n subcontratada
	 * 
	 * @param AutorizacionesEstanciaCapita autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(AutorizacionesEstanciaCapita autorizacion){
		boolean save = true;					
		try{
			super.persist(autorizacion);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro que " +
					"asocia la autorizaci�n de ingreso estancia con la autorizaci�n de capitaci�n subcontratada: ",e);
		}				
		return save;				
	}

}
