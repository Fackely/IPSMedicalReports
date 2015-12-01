/**
 * 
 */
package com.princetonsa.dao.capitacion;

import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;

/**
 * @author armando
 *
 */
public interface ProcesoNivelAutorizacionDao 
{

	public abstract DTOValidacionNivelAutoAutomatica validarNivelAutorizacion(DtoParametrosValidacionNivelAutorizacion dtoParametrosValidacionNivelAutorizacion);

}
