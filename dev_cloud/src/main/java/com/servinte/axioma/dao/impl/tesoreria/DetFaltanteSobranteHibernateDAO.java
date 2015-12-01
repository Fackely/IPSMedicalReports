package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.Set;

import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetFaltanteSobranteDAO;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.delegate.tesoreria.DetFaltanteSobranteDelegate;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos de negocio
 * para la entidad detalle faltante sobrante
 * 
 * @author Angela Maria Aguirre
 * @since 22/07/2010
 */
public class DetFaltanteSobranteHibernateDAO implements IDetFaltanteSobranteDAO {

	DetFaltanteSobranteDelegate delegate = new DetFaltanteSobranteDelegate();
	
	 /** 
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
		return delegate.consultarRegistroFaltanteSobrantePorID(registro);
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
		return delegate.actualizarFaltanteSobrante(registro);
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar los datos necesarios
	 * para el cambio del responsable del detalle de faltante/sobrante
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante, con los datos necesarios para realizar la consulta  
	 * @return List<DTOCambioResponsableDetFaltanteSobrante>, con los datos obtenidos en la consulta
	 * @author Angela Aguirre
	 */
	@Override
	public ArrayList<DTOCambioResponsableDetFaltanteSobrante> busquedaDetFaltanteSobrante(DTOCambioResponsableDetFaltanteSobrante dto){
		return delegate.busquedaFaltantesSobrantes(dto);
	}

	
	/**
	 * Devuelve el total de los detalles de los Faltantes / Sobrantes asociados a las Entregas a 
	 * Transportadora de Valores.
	 * 
	 * @param setIdDetalleFaltanteSobrante
	 * @return Valor total de las diferencias asociadas a las entregas a Transportadora de valores 
	 * realizadas en un turno espec&iacute;fico
	 */
	@Override
	public double obtenerTotalDiferenciaEfectivoEntregasTransportadora(Set<Long> setIdDetalleFaltanteSobrante) {
		
		return delegate.obtenerTotalDiferenciaEfectivoEntregasTransportadora(setIdDetalleFaltanteSobrante);
	}

}
