package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import com.princetonsa.dto.manejoPaciente.DTOProcesoAutorizacion;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para  el proceso de generación de autorizacion
 *  
 * @author Angela Maria Aguirre
 * @since 2/12/2010
 */
public interface IProcesoGeneracionAutorizacionMundo {

	
	public DTOProcesoAutorizacion generacionAutorizacion(
			DTOProcesoAutorizacion dtoProcesoAutorizacion)throws Exception;
}
