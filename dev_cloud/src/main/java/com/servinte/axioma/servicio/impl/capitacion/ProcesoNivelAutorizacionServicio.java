package com.servinte.axioma.servicio.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoNivelAutorizacionMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IProcesoNivelAutorizacionServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 27/12/2010
 */
public class ProcesoNivelAutorizacionServicio implements IProcesoNivelAutorizacionServicio {
	
	IProcesoNivelAutorizacionMundo mundo;
	
	public ProcesoNivelAutorizacionServicio(){
		mundo=CapitacionFabricaMundo.crearProcesoNivelAutorizacionMundo();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de validar el nivel de autorización del 
	 * usuario y los servicios o artículos para autorizaciones tipo automatico
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionAutomatica(
			DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion) throws IPSException{
		
		return mundo.validarNivelAutorizacionParaAutorizacionAutomatica(dtoParametrosValidacionNivelAutorizacion);
		//return ProcesoNivelAutorizacion.validarNivelAutorizacion(dtoParametrosValidacionNivelAutorizacion); 
	}

}
