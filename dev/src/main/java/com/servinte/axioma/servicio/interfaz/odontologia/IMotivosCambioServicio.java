/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.odontologia;

import java.util.Collection;

import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;

/**
 * Esta interface define el comportamiento de todo lo que tiene que ver con Motivos Cambio Servicio
 * 
 * @author armando
 * 
 * 
 */
public interface IMotivosCambioServicio 
{

	/**
	 * Retorna una coleccion de Motivos Cambio de Servicio, de acurdo a los parametros 
	 * que se envian. Si no se especifica el tipo de motivo, retorna todos los parametrizados
	 * para la institucion.
	 * @param codigoInstitucion Codigo de la insititucion en la que se encuentra el sistema.
	 * @param tipoMotivo tipo de motivo por el que se desea filtrar <code>ANULAR</code> 
	 * <code>ADICIO</code> <code>ELIMIN</code>
	 * @return una coleccion de Motivos Cambio de Servicio
	 */
	public Collection<DtoMotivosCambioServicio> listarMotivosCambioServicio(
			int codigoInstitucion, String tipoMotivo);
}
