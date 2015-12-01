package com.servinte.axioma.servicio.impl.capitacion;

import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoNivelAutorizacionManualMundo;
import com.servinte.axioma.servicio.interfaz.capitacion.IProcesoNivelAutorizacionManualServicio;

public class ProcesoNivelAutorizacionManualServicio implements IProcesoNivelAutorizacionManualServicio{

	IProcesoNivelAutorizacionManualMundo mundo;
	
	public ProcesoNivelAutorizacionManualServicio() {
		mundo=CapitacionFabricaMundo.crearProcesoNivelAutorizacionManualMundo();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de validar el nivel de autorizaci�n del 
	 * usuario y los servicios o art�culos para autorizaciones de tipo manual
	 * 
	 * @author, Fabian Becerra
	 *
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionesPoblacionCapitada(
			DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion, int codigoInstitucion){
		
		return mundo.validarNivelAutorizacionParaAutorizacionesPoblacionCapitada(dtoParametrosValidacionNivelAutorizacion, codigoInstitucion );
	}
}
