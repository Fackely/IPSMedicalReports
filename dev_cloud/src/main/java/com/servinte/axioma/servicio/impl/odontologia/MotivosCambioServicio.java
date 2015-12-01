/**
 * 
 */
package com.servinte.axioma.servicio.impl.odontologia;

import java.util.Collection;

import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.mundo.odontologia.MotivosCambioServicioMundo;
import com.servinte.axioma.servicio.interfaz.odontologia.IMotivosCambioServicio;

/**
 * implementacion de la interfaz de Motivos Cambio Servicio.
 * @author armando
 *
 */
public class MotivosCambioServicio implements IMotivosCambioServicio 
{

	@Override
	public Collection<DtoMotivosCambioServicio> listarMotivosCambioServicio(
			int codigoInstitucion, String tipoMotivo) 
	{
		return MotivosCambioServicioMundo.consultarMotivosCambioServicios(codigoInstitucion,tipoMotivo);
	}
	

}
