/**
 * 
 */
package com.princetonsa.mundo.nivelAutorizacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;

/**
 * @author armando
 *
 */
public class ProcesoNivelAutorizacion 
{

	/**
	 * 
	 * @param dtoParametrosValidacionNivelAutorizacion
	 * @return
	 */
	public static DTOValidacionNivelAutoAutomatica validarNivelAutorizacion(DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesoNivelAutorizacionDao().validarNivelAutorizacion(dtoParametrosValidacionNivelAutorizacion);
	}

}
