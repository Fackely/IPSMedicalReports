package com.servinte.axioma.bl.capitacion.interfaz;

import java.util.List;

import com.servinte.axioma.dto.capitacion.NivelAtencionDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que expone los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Capitación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public interface ICatalogoCapitacionMundo {

	/**
	 * Servicio encargado de obtener los niveles de atención parametrizados
	 * en el sistema
	 * 
	 * @return
	 * @throws IPSException
	 */
	List<NivelAtencionDto> consultarNivelesAtencion() throws IPSException;
	

}