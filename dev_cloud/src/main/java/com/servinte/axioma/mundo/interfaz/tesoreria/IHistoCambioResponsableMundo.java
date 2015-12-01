package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.servinte.axioma.orm.HistoCambioResponsable;

/**
 * Esta clase se encarga de definir los procesos
 * de negocio de la entidad Historial Cambio Responsable de
 * un registro Faltante Sobrante  
 * 
 * @author Angela Maria Aguirre
 * @since 19/07/2010
 */
public interface IHistoCambioResponsableMundo {
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de registrar un historial de 
	 * cambio de responsable para un registro faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return Boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public Boolean registrarHistorialFaltanteSobrante(HistoCambioResponsable dto);
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar el historial del  cambio de responsable
	 * del registro de faltante sobrante
	 * 
	 * @param DTOHistoCambioResponsable
	 * @return ArrayList<DTOHistoCambioResponsable> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOHistoCambioResponsable> consultarHistorialPorDetalleFaltanteSobranteID(DTOHistoCambioResponsable dto);
	
}
