package com.servinte.axioma.servicio.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;

public interface IProcesoNivelAutorizacionManualServicio {
	
	/**
	 * 
	 * Este M�todo se encarga de validar el nivel de autorizaci�n del 
	 * usuario y los servicios o art�culos para autorizaciones de tipo manual
	 * 
	 * @author, Fabian Becerra
	 *
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionesPoblacionCapitada(
			DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion, int codigoInstitucion);

}
