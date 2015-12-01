package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.Set;

import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.servinte.axioma.orm.DetFaltanteSobrante;

/**
 * Esta clase se encarga de definir los m&eacute;todos de
 * negocio para la entidad detalle faltante sobrante
 * 
 * @author Angela Maria Aguirre
 * @since 22/07/2010
 */
public interface IDetFaltanteSobranteMundo {

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
	public DetFaltanteSobrante consultarRegistroDetFaltanteSobrantePorID(DetFaltanteSobrante registro);
	
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
	public boolean actualizarDetFaltanteSobrante(DetFaltanteSobrante registro);
		
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el responsable del
	 * detalle de un registro de faltante sobrante y guardar su hist&oacute;rico
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */	
	public boolean actualizarResponsableDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto);
	
	/**
	 * Este m&eacute;todo se encarga de consultar los datos necesarios
	 * para el cambio del responsable del detalle de faltante/sobrante
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante, con los datos necesarios para realizar la consulta  
	 * @return List<DTOCambioResponsableDetFaltanteSobrante>, con los datos obtenidos en la consulta
	 * @author Angela Aguirre
	 */
	public ArrayList<DTOCambioResponsableDetFaltanteSobrante> busquedaDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto);
	
	
	/**
	 * Devuelve el total de los detalles de los Faltantes / Sobrantes asociados a las Entregas a 
	 * Transportadora de Valores.
	 * 
	 * @param setIdDetalleFaltanteSobrante
	 * @return Valor total de las diferencias asociadas a las entregas a Transportadora de valores 
	 * realizadas en un turno espec&iacute;fico
	 */
	public double obtenerTotalDiferenciaEfectivoEntregasTransportadora (Set<Long> setIdDetalleFaltanteSobrante);
}
