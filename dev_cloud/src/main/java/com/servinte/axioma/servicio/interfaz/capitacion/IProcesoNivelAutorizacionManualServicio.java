package com.servinte.axioma.servicio.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;

public interface IProcesoNivelAutorizacionManualServicio {
	
	/**
	 * 
	 * Este Método se encarga de validar el nivel de autorización del 
	 * usuario y los servicios o artículos para autorizaciones de tipo manual
	 * 
	 * @author, Fabian Becerra
	 *
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionesPoblacionCapitada(
			DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion, int codigoInstitucion);

}
