package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.GruposServicios;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 8/09/2010
 */
public interface IGruposServiciosServicio {
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los grupos de servicios
	 * activos en el sistema
	 * 
	 * @return ArrayList<GruposServicios>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GruposServicios> buscarGruposServicioActivos();
	public ArrayList<GruposServicios> buscarGruposServicio();

	/**
	 * 	
	 * Este Metodo consulta los grupos de servicio
	 * 
	 * @return List<GrupoServicioDto>
	 * @author ginsotfu
	 *
	 */
	List<GrupoServicioDto> consultarGruposServicio() throws IPSException;

}
