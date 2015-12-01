package com.servinte.axioma.mundo.interfaz.capitacion;


import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;
import com.servinte.axioma.fwk.exception.IPSException;



public interface IProcesoNivelAutorizacionMundo
{

	/**
	 * 
	 * Este Método realiza el proceso de validación para saber el Nivel de Autorización que aplica tanto al usuario 
	 * como a cada uno de los servicios / artículos de las ordenes a autorizar y la entidad subcontratada autorizada.
	 * para niveles de autorizacion de tipo automatico
	 * 
	 * @author Fabian Becerra
	 *
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionAutomatica(
			DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion) throws IPSException;
}
