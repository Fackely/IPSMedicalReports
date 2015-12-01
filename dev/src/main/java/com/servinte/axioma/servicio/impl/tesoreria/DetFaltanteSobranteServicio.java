package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetFaltanteSobranteMundo;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.servicio.interfaz.tesoreria.IDetFaltanteSobranteServicio;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos de
 * negocio para la entidad faltante sobrante
 * 
 * @author Angela Maria Aguirre
 * @since 22/07/2010
 */
public class DetFaltanteSobranteServicio implements
		IDetFaltanteSobranteServicio {
	
	/**
	 * Instancia de la clase IDetFaltanteSobranteMundo
	 */
	IDetFaltanteSobranteMundo mundo;
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */	
	public DetFaltanteSobranteServicio() {
		mundo = TesoreriaFabricaMundo.crearDetFaltanteSobranteMundo();
	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de consular por ID los datos del detalle de un
	 * registro faltante sobrante
	 * 
	 * @param DetFaltanteSobrante
	 * @return DetFaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public DetFaltanteSobrante consultarRegistroDetFaltanteSobrantePorID(
			DetFaltanteSobrante registro) {
		return mundo.consultarRegistroDetFaltanteSobrantePorID(registro);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el detalle
	 * de un faltante sobrante
	 * 
	 * @param DetFaltanteSobrante
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean actualizarDetFaltanteSobrante(DetFaltanteSobrante registro){
		return mundo.actualizarDetFaltanteSobrante(registro);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el responsable del
	 * detalle de un registro de faltante sobrante y guardar su hist&oacute;rico
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */	
	public boolean actualizarResponsableDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto){
		return mundo.actualizarResponsableDetFaltanteSobrante(dto);
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar los datos necesarios
	 * para el cambio del responsable del detalle de faltante/sobrante
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante, con los datos necesarios para realizar la consulta  
	 * @return List<DTOCambioResponsableDetFaltanteSobrante>, con los datos obtenidos en la consulta
	 * @author Angela Aguirre
	 */
	public ArrayList<DTOCambioResponsableDetFaltanteSobrante> busquedaDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto){
		return mundo.busquedaDetFaltanteSobrante(dto);
	}

}
