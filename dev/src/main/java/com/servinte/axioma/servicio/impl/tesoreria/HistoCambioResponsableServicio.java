package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IHistoCambioResponsableMundo;
import com.servinte.axioma.orm.HistoCambioResponsable;
import com.servinte.axioma.servicio.interfaz.tesoreria.IHistoCambioResponsableServicio;

/**
 * Esta clase se encarga de implementar los procesos
 * de negocio de la entidad Historial Cambio Responsable de
 * un registro Faltante Sobrante  
 * 
 * @author Angela Maria Aguirre
 * @since 19/07/2010
 */
public class HistoCambioResponsableServicio implements
		IHistoCambioResponsableServicio {
	
	private IHistoCambioResponsableMundo mundo; 
	
	public HistoCambioResponsableServicio(){
		mundo = TesoreriaFabricaMundo.crearHistorialCambioResponsableMundo();
	}

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
	public Boolean registrarHistorialFaltanteSobrante(HistoCambioResponsable dto){
		return mundo.registrarHistorialFaltanteSobrante(dto);
	}
	
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
	public ArrayList<DTOHistoCambioResponsable> consultarHistorialPorDetalleFaltanteSobranteID(DTOHistoCambioResponsable dto){
		return mundo.consultarHistorialPorDetalleFaltanteSobranteID(dto);
	}

}
