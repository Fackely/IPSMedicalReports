package com.servinte.axioma.servicio.interfaz.capitacion;

import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 27/12/2010
 */
public interface IProcesoNivelAutorizacionServicio {
	
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionAutomatica(
			DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion) throws IPSException;

}
